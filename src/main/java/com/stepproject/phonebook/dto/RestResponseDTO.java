package com.stepproject.phonebook.dto;


public class RestResponseDTO<T> {
    private T data;
    private String message;


    public RestResponseDTO(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public RestResponseDTO() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RestResponseDTO{" +
                "data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
