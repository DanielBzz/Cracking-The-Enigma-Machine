import exceptions.MultipleMappingException;

public interface EnigmaMachineUI {

    public void loadNewXmlFile() throws Exception;

    public void displayingMachineSpecification();

    public void manualInitialCodeConfiguration() throws MultipleMappingException;

    public void automaticInitialCodeConfiguration();

    public void encryptInput();

    public void resetCurrentCode();

    public void getHistoryAndStatistics();

    public void exit();
}
