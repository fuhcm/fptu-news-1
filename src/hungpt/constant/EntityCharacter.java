package hungpt.constant;

public enum EntityCharacter {
    LESS_THAN("<"),
    GREAT_THAN(">"),
    EQUAL("="),
    SLASH("/"),
    SPACE(" "),
    SINGLE_QOUT("\'"),
    DOUBLE_QOUT("\""),
    UNDERSCORE("_"),
    COLON(":"),
    HYPHEN("-"),
    PERIOD(".");
    private char character;

    EntityCharacter(String s) {
        this.character = s.charAt(0);
    }

    public char getCharacter() {
        return character;
    }
}
