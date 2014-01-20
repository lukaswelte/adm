package com.adm.meetup.event;

public enum EventType {
    SPORT("sport"), DEFAULT("default");
    private String name;

    EventType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}