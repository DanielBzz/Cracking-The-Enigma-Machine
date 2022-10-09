package logic;

import machineDtos.EngineDTO;
import machineDtos.EnigmaMachineDTO;
import machineDtos.HistoryAndStatisticDTO;

import java.io.InputStream;

public interface EnigmaSystemEngine {

    void loadXmlFile(InputStream xmlFile) throws Exception;

    EngineDTO displayingMachineSpecification();

    void manualMachineInit(EnigmaMachineDTO args);

    void automaticMachineInit();

    String encryptString(String input);

    void resetTheMachine();

    HistoryAndStatisticDTO getHistoryAndStatistics();
}