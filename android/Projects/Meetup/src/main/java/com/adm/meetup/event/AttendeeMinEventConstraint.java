package com.adm.meetup.event;

public class AttendeeMinEventConstraint extends EventDecorator implements IEventConstraint {
	BaseEvent event = null;
	Long value = Long.MIN_VALUE;
	
	public class AttendeeMinEventConstraintException extends EventValidationException {
		private static final long serialVersionUID = 1L;
	}
	
	public AttendeeMinEventConstraint(BaseEvent event)  throws EventValidationException {
		super(event);
		this.event = event; 	 
	}
	
    public AttendeeMinEventConstraint(BaseEvent event, Long value) throws EventValidationException {
		super(event);
		this.event = event;
		this.value = value;
		if(!this.isValid()) {
			throw new AttendeeMinEventConstraintException();
		}
	}
    
	public boolean isValid() throws EventValidationException {
        if(this.event.getAttendee() >= this.value) {
        	return true;
        }
        return false;
    }
}