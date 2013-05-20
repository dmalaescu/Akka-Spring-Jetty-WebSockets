package com.examples.akka.communication;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
public class Message<T> {
    private MessageType type;
    private T data;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
