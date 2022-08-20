public interface EnigmaMachineUI {

    @SortedMethod(value = 1)
    public void loadNewXmlFile();
    @SortedMethod(value = 2)
    public void displayingMachineSpecification();
    @SortedMethod(value = 3)
    public void manualInitialCodeConfiguration();
    @SortedMethod(value = 4)
    public void automaticInitialCodeConfiguration();
    @SortedMethod(value = 5)
    public void encryptInput();
    @SortedMethod(value = 6)
    public void resetCurrentCode();
    @SortedMethod(value = 7)
    public void getHistoryAndStatistics();
    @SortedMethod(value = 8)
    public void exit();
}
