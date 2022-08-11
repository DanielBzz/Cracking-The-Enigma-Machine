public interface EnigmaSystemEngine {

    void loadXmlFile(String path);

    EngineInfoDTO displayingMachineSpecification();  // SHOULD RETURN MACHINE DTO THAT I CAN ACCESS TO ALL THE DETAILS FROM THERE

    void manualMachineInit();               // get some paramaters according to the format in the file

    void automaticMachineInit();               // get some paramaters according to the format in the file

    void encryptString(String input);

    void resetTheMachine();

    void getHistoryAndStatistics();     // return values according the format

    void exit();
}
