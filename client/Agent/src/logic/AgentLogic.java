package logic;

import agent.AgentTask;
import com.sun.istack.internal.NotNull;
import constants.Constants;
import contestDtos.CandidateDataDTO;
import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.AgentTaskDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import machine.Machine;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
public class AgentLogic {

    private ThreadPoolExecutor threadPool;
    private BlockingQueue<Runnable> agentTasks;
    private BlockingQueue<AgentAnswerDTO> answersQueue = new LinkedBlockingQueue<>();
    private final String name;
    private final int amountOfTasksInSingleTake;
    private final int amountOfThreads;
    private AtomicInteger tasksLeftBeforeNewTake;

    public class WebAgentTask extends AgentTask {
        public WebAgentTask(Machine machine, AgentTaskDTO details) {
            super(machine, details);
        }
        @Override
        public void run() {
            super.run();
            if(tasksLeftBeforeNewTake.decrementAndGet() == 0){
                Platform.runLater(pullTasks());
                tasksLeftBeforeNewTake.set(amountOfTasksInSingleTake);
            }
            Platform.runLater(pushAnswers());
        }
    }
    public AgentLogic(String name, int amountOfTasksInSingleTake, int amountOfThreads) {
        this.name = name;
        this.amountOfTasksInSingleTake = amountOfTasksInSingleTake;
        this.amountOfThreads = amountOfThreads;
        this.tasksLeftBeforeNewTake = new AtomicInteger(amountOfTasksInSingleTake);

        agentTasks = new LinkedBlockingQueue<>(amountOfThreads);
        //need to check for the keep alive parameter
        threadPool = new ThreadPoolExecutor(amountOfThreads, amountOfThreads + 5,5, TimeUnit.SECONDS,agentTasks,createThreadFactory());
    }

    private ThreadFactory createThreadFactory(){

        return new ThreadFactory() {
            final String name = "Thread";
            int agentNumber = 1;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,name + agentNumber++);
            }
        };
    }

    public Runnable pullTasks(){

        String finalUrl = HttpUrl
                .parse(REQUEST_PATH_PULL_TASKS)
                .newBuilder()
                .addQueryParameter("amount", String.valueOf(amountOfTasksInSingleTake))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
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
                            improvedTasks.add(new WebAgentTask(task.getEnigmaMachine(), task.getDetails()));
                        }

                        for (WebAgentTask newTask: improvedTasks) {
                            try {
                                agentTasks.put(newTask);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
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
            //need to check who is the configuration and who is the message
            answer.getDecryptedMessagesCandidates().forEach((configuration, message)->newCandidates.add(new CandidateDataDTO(message.toString(), name, configuration)));
        }
        //need to check the creation of the body
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), Constants.GSON_INSTANCE.toJson(newCandidates));

        try (Response res = HttpClientUtil.runPost(REQUEST_PATH_PUSH_CANDIDATES, body)) {

            if (res.code() == 200) {
                System.out.println("server was updated with the new candidates");
            } else {
                System.out.println("there was a problem to update the server with the new candidates");
            }
        } catch (IOException e) {
            System.out.println("exception was thrown after trying to update the server with the new candidates");
        }

        return null;
    }

    public void logOut() {

    }
}
