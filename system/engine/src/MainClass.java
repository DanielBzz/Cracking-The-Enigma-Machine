import exceptions.IdMissingInRangeException;

public class MainClass {

    public static void main(String[] args) {

        EnigmaEngine engine = new EnigmaEngine();

        try {
            engine.loadXmlFile("C:\\Users\\DanielBazis\\IdeaProjects\\CarckingTheEnigma\\resources\\ex1-sanity-small.xml");
            engine.automaticMachineInit();

       /*     EngineInfoDTO info = engine.displayingMachineSpecification();

            System.out.println(info.getNumOfOptionalReflectors());
            System.out.println(info.getNumOfOptionalRotors());
            System.out.println(info.getNumOfUsedRotors());

            MachineInfoDTO machineInfo = info.getMachineInfo();
            System.out.println(machineInfo.getRotorsID());
            System.out.println(machineInfo.getReflectorID());
            System.out.println(machineInfo.getPlugs());
            System.out.println(machineInfo.getRotorsInitPosition());
            System.out.println(machineInfo.getNotchDistanceFromPositions());
*/

            System.out.println(engine.encryptString("aaa"));

            engine.resetTheMachine();

            System.out.println(engine.encryptString("aaa"));

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }



    }
}
