package decryptionDtos;

import java.util.Set;

public class DictionaryDTO {

    private final Set<String> wordsDictionary;
    private final String excludeChars;

    public DictionaryDTO(Set<String> wordsDictionary, String excludeChars) {
        this.wordsDictionary = wordsDictionary;
        this.excludeChars = excludeChars;
    }

    public Set<String> getWordsDictionary() {
        return wordsDictionary;
    }

    public String getExcludeChars() {
        return excludeChars;
    }
}
