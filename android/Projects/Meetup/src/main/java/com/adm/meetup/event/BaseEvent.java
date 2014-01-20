package com.adm.meetup.event;

import java.util.Iterator;

public abstract class BaseEvent implements IEvent {
    public abstract String get(IFields name) throws EventFieldMissingException;

    public abstract void set(IFields name, String value) throws EventFieldMissingException;

    public abstract Iterator<IFields> getFields();

    protected abstract String getField(IFields name) throws EventFieldMissingException;

    protected abstract void addField(IFields name, String value);
}