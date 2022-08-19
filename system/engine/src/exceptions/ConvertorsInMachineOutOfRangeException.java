package exceptions;

public class ConvertorsInMachineOutOfRangeException extends Error {

    String exceptionMsg;

    public ConvertorsInMachineOutOfRangeException(String objectName, int currAmount, int minAmount, int maxAmount){
        exceptionMsg = "You have " + currAmount + " " + objectName + "." + System.lineSeparator() +
                "You should have at least " + minAmount + " "+ objectName + " in the system for using the machine and at most " + maxAmount;
    }

    @Override
    public String getMessage() {
        return exceptionMsg;
    }
}
