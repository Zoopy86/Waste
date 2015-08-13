package com.zinc.zoopy.waste;

public enum KeypadButton{
    ZERO("0", KeypadButtonCategory.NUMBER),
    ONE("1", KeypadButtonCategory.NUMBER),
    TWO("2", KeypadButtonCategory.NUMBER),
    THREE("3", KeypadButtonCategory.NUMBER),
    FOUR("4", KeypadButtonCategory.NUMBER),
    FIVE("5", KeypadButtonCategory.NUMBER),
    SIX("6", KeypadButtonCategory.NUMBER),
    SEVEN("7", KeypadButtonCategory.NUMBER),
    EIGHT("8", KeypadButtonCategory.NUMBER),
    NINE("9", KeypadButtonCategory.NUMBER),
    PLUS("+", KeypadButtonCategory.OPERATOR),
    MINUS("-", KeypadButtonCategory.OPERATOR),
    MULTIPLY("*", KeypadButtonCategory.OPERATOR),
    DIVIDE("/", KeypadButtonCategory.OPERATOR),
    SIGN("+/-", KeypadButtonCategory.OTHER),
    BACKSPACE("<-", KeypadButtonCategory.CLEAR),
    CALCULATE("=", KeypadButtonCategory.OPERATOR),
    C("C", KeypadButtonCategory.CLEAR),
    CE("CE", KeypadButtonCategory.CLEAR),
    CLOSE("Close", KeypadButtonCategory.OTHER),
    DOT(".", KeypadButtonCategory.NUMBER),
    NONE("",KeypadButtonCategory.OTHER);

    CharSequence mText; // Display Text
    KeypadButtonCategory mCategory;

    KeypadButton(CharSequence text, KeypadButtonCategory category) {
        mText = text;
        mCategory = category;
    }
    public CharSequence getText() {
        return mText;
    }
}