package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {
    private final int status;
    private final String body;

    public ServerException(int status, String body) {
        super(body);
        this.status = status;
        this.body = body;
    }
}