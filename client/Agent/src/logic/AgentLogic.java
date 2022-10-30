package logic;

import agent.AgentTask;
import com.sun.istack.internal.NotNull;
import components.main.AgentMainAppController;
import constants.Constants;
import contestDtos.AgentProgressDTO;
import contestDtos.CandidateDataDTO;
import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.AgentTaskDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import machine.Machine;
import okhttp3.*;
import util.RefresherController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static constants.Constants.REQUEST_PATH_IS_CONTEST_ON;
import static util.Constants.REQUEST_PATH_PULL_TASKS;
import static util.Constants.REQUEST_PATH_PUSH_CANDIDATES;

/*
* החלק הלוגי של הסוכן
* מייצר בקשת משיכת משימות ומכניס אותם לטרד פול
* מבצע את המשימות ומכניס את התשובות לתור התשובות
* מעדכן את השרת בתשובות
*
*
* */
public class AgentLogic extends RefresherController {//need to change name of the refresher

    private final AgentMainAppController appController;
    private ThreadPoolExecutor threadPool;
    private BlockingQueue<Runnable> agentTasks;
    private BlockingQueue<AgentAnswerDTO> answersQueue = new LinkedBlockingQueue<>();
    private final String name;
    private final int amountOfTasksInSingleTake;
    private final int amountOfThreads;
    private AtomicInteger tasksLeftBeforeNewTake;
    private Boolean inContest;

    private AtomicInteger totalTakenTasks;
    private AtomicInteger totalFinishedTasks;
    private AtomicInteger totalAmountOfCandidates;

    @Override
    public void updateList(String jsonUserList) {
        try{
            //very sensitive
            inContest = Constants.GSON_INSTANCE.fromJson(jsonUserList, Boolean.class);
            if(inContest && agentTasks.size() == 0){
                startContest();
            }
        }catch (Exception e){
            System.out.println("There was a problem with the body of the response and couldnt convert string to boolean");
        }
    }

    public class WebAgentTask extends AgentTask {
        public WebAgentTask(Machine machine, AgentTaskDTO details) {
            super(machine, details);
        }
        @Override
        public void run() {
            if(!inContest){
                finishContest();
            }
            else{
                super.run();
                if(tasksLeftBeforeNewTake.decrementAndGet() == 0){
                    Platform.runLater(pullTasks());
                    tasksLeftBeforeNewTake.set(amountOfTasksInSingleTake);
                }
                Platform.runLater(pushAnswers());
            }
        }
    }
    public AgentLogic(AgentMainAppController appController, String name, int amountOfTasksInSingleTake, int amountOfThreads) {
        this.appController = appController;
        this.name = name;
        this.amountOfTasksInSingleTake = amountOfTasksInSingleTake;
        this.amountOfThreads = amountOfThreads;
        this.tasksLeftBeforeNewTake = new AtomicInteger(amountOfTasksInSingleTake);

        agentTasks = new LinkedBlockingQueue<>(amountOfThreads);
        //need to check for the keep alive parameter
        threadPool = new ThreadPoolExecutor(amountOfThreads, amountOfThreads + 5,5, TimeUnit.SECONDS,agentTasks,createThreadFactory());

        startListRefresher(REQUEST_PATH_IS_CONTEST_ON);
    }

    private ThreadFactory createThreadFactory(){

        return new ThreadFactory() {
            final String name = "Thread";
            int threadNumber = 1;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,name + threadNumber++);
            }
        };
    }

    public Runnable pullTasks(){

        String finalUrl = HttpUrl
                .parse(REQUEST_PATH_PULL_TASKS)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Could not response well");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    if(response.body() != null){
                        AgentTask[] newTasks = Constants.GSON_INSTANCE.fromJson(response.body().toString(), AgentTask[].class);
                        List<WebAgentTask> improvedTasks = new ArrayList<>();
                        for (AgentTask task:newTasks) {
                            task.getDetails().setAnswerConsumer(answersConsumer());
                            improvedTasks.add(new WebAgentTask(task.getEnigmaMachine(), task.getDetails()));
                            totalTakenTasks.incrementAndGet();
                        }

                        for (WebAgentTask newTask: improvedTasks) {
                            try {
                                agentTasks.put(newTask);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        response.body().close();
                    }

                    System.out.println("Allies was added successfully!");
                }
                else {
                    System.out.println("Could not response well, url:" + finalUrl);
                }
            }
        });
        return null;
    }

    public Runnable pushAnswers(){
        List<AgentAnswerDTO> newAnswers = new ArrayList<>();
        answersQueue.drainTo(newAnswers);

        List<CandidateDataDTO> newCandidates = new ArrayList<>();
        for (AgentAnswerDTO answer: newAnswers) {
            //need to make sure who is the configuration and who is the message
            answer.getDecryptedMessagesCandidates().forEach((configuration, message)->{
                newCandidates.add(new CandidateDataDTO(message.toString(), name, configuration));
                totalAmountOfCandidates.incrementAndGet();
            });
            totalFinishedTasks.incrementAndGet();
        }
        //need to check if we need to put it in platform run later
        appController.updateCandidates(newCandidates);
        String json = constants.Constants.GSON_INSTANCE.toJson(newCandidates);

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("candidates", json)
                        .build();

        HttpClientUtil.runAsyncPost(REQUEST_PATH_PUSH_CANDIDATES, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("exception was thrown after trying to update the server with the new candidates");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try(ResponseBody responseBody = response.body()){
                    if (response.code() == 200) {
                        appController.updateTasksData(new AgentProgressDTO(agentTasks.size(), totalTakenTasks.get(), totalFinishedTasks.get(), totalAmountOfCandidates.get()));
                        System.out.println("server was updated with the new candidates");
                    } else {
                        System.out.println("there was a problem to update the server with the new candidates");
                    }
                }
            }
        });

        return null;
    }

    public void finishContest(){
        appController.setPassive();
        agentTasks.clear();
        //threadPool.shutdown();
    }

    private Consumer<AgentAnswerDTO> answersConsumer(){

        return answer ->
        {
            try {
                answersQueue.put(answer);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public void startContest(){
        pullTasks();
    }

    public void logOut() {

    }
}
