package hungpt.utils;

import hungpt.constant.EntityCharacter;

public class StringHelper {

    public static boolean isNameCharacter(char c){
        return Character.isLetterOrDigit(c) || c == EntityCharacter.UNDERSCORE.getCharacter()
                || c == EntityCharacter.HYPHEN.getCharacter() || c == EntityCharacter.PERIOD.getCharacter();
    }

    public static boolean isStartCharacter(char c){
        return Character.isLetter(c) || c == EntityCharacter.COLON.getCharacter() || c == EntityCharacter.UNDERSCORE.getCharacter();
    }
}
