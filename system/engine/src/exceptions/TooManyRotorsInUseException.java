package exceptions;

public class TooManyRotorsInUseException extends Exception{

     String exceptionMsg;

     public TooManyRotorsInUseException(int numOfRotorsInTheSystem){
          exceptionMsg = "Too many rotors in use, you have " + numOfRotorsInTheSystem + " and you can use at most 99";
     }

     @Override
     public String getMessage() {
          return exceptionMsg;
     }
}
