public interface EnigmaSystemEngine {

    void loadXmlFile(String path) throws Exception;

    EngineInfoDTO displayingMachineSpecification();

    void manualMachineInit(EnigmaMachineDTO args);

    void automaticMachineInit();

    String encryptString(String input);

    void resetTheMachine();

    HistoryAndStatisticDTO  getHistoryAndStatistics();
}