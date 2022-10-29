package exceptions;

import contestDtos.CandidateDataDTO;

public class ContestIsFinishedException extends Exception{

    public ContestIsFinishedException(CandidateDataDTO winner){
        super("the winner is: " + winner.getFoundersName() + System.lineSeparator()
                + "The decrypted message is: " + winner.getDecryptedMessage() + System.lineSeparator()
                + "The configuration is: " + winner.getConfiguration());
    }
}
