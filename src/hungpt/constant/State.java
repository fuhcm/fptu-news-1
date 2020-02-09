package hungpt.constant;

public enum State {
    CONTENT,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    CLOSE_TAG_SLASH,
    CLOSE_TAG_NAME,
    WAIT_END_TAG_CLOSE,
    EMPTY_SLASH,
    OPEN_TAG_NAME,
    TAG_INNER,
    ATT_NAME,
    EQUAL_WAIT,
    EQUAL,
    ATT_VALUE_NQ,
    ATT_VALUE_Q,
}
