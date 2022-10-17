package components.body.main;

public interface EncryptableByDictionary extends EncryptParentController{

    Boolean checkWordsInTheDictionary(String message);
    String getStringWithoutSpecialChars(String word);
}
