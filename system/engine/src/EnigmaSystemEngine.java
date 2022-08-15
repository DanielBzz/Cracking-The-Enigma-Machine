public interface EnigmaSystemEngine {

    void loadXmlFile(String path) throws Exception;

    EngineInfoDTO displayingMachineSpecification();

    void manualMachineInit();               // get some paramaters according to the format in the file

    void automaticMachineInit();

    String encryptString(String input);

    void resetTheMachine();

    void getHistoryAndStatistics();     // return values according the format

    void exit();
}
