package com.adm.meetup.event;

public interface IEventConstraint {
    public abstract boolean isValid() throws EventValidationException;
}