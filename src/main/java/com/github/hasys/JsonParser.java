package com.github.hasys;

import static java.lang.Character.isWhitespace;

public class JsonParser {

    private int currentPosition = 0;
    private final String data;

    public JsonParser(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Json string can not be null");
        }

        this.data = data.trim();
    }

    public JsonObject parse() {
        currentPosition = 0;
        return parseObject();
    }

    private JsonObject parseObject() {
        if (parseNextToken() != JsonToken.START_OBJECT) {
            throw new IllegalArgumentException(
                    String.format("Unexpected token %s at position %s", currentToken(), currentPosition)
            );
        }
        currentPosition++;

        if (parseNextToken() == JsonToken.END_OBJECT && currentPosition == data.length()-1) {
            return JsonObject.EMPTY;
        } else {
            throw new IllegalArgumentException(data + System.lineSeparator() +" is not a valid json object");
        }
    }

    private JsonToken parseNextToken() {
        JsonToken token = null;
        for (int i = currentPosition; i < data.length(); i++) {
            if (isWhitespace(data.charAt(i))) {
                continue;
            }

            currentPosition = i;
            token = currentToken();
            break;
        }

        if (token == null) {
            throw new IllegalArgumentException(
                    String.format("Token not found in string: \"%s\"", data)
            );
        }

        return token;
    }

    private JsonToken currentToken() {
        switch (data.charAt(currentPosition)) {
            case '{':
                return JsonToken.START_OBJECT;
            case '}':
                return JsonToken.END_OBJECT;
            default:
                throw new IllegalArgumentException(
                        String.format("Unknown token %s at position %s", data.charAt(currentPosition), currentPosition)
                );
        }
    }
}
