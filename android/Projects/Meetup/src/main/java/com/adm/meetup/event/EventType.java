package com.adm.meetup.event;

import java.util.HashMap;
import java.util.Map;

public enum EventType {
    DEFAULT(Integer.valueOf(0).byteValue(), "default"),
    SPORT(Integer.valueOf(1).byteValue(), "sport"),
    DANCE(Integer.valueOf(2).byteValue(), "dance"),
    TRIP(Integer.valueOf(3).byteValue(), "trip");

    private static final Map<Byte, EventType> typesByValue = new HashMap<Byte, EventType>();

    static {
        for (EventType type : EventType.values()) {
            typesByValue.put(type.id, type);
        }
    }

    private String name;
    private Byte id;

    EventType(Byte id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EventType forValue(byte value) {
        return typesByValue.get(value);
    }

    public String getName() {
        return this.name;
    }

    public Byte getId() {
        return this.id;
    }
}