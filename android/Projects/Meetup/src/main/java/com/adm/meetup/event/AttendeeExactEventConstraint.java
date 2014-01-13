package com.adm.meetup.event;

public class AttendeeExactEventConstraint extends EventDecorator implements IEventConstraint {
	BaseEvent event = null;
	Long value = 0l;
	public class AttendeeExactEventConstraintException extends EventValidationException {
		private static final long serialVersionUID = 1L;
	}
	public AttendeeExactEventConstraint(BaseEvent event)  throws EventValidationException {
		super(event);
		this.event = event; 	 
	}
	
    public AttendeeExactEventConstraint(BaseEvent event, Long value) throws EventValidationException {
		super(event);
		this.event = event;
		this.value = value;
		if(!this.isValid()) {
			throw new AttendeeExactEventConstraintException();
		}
	}
    
	public boolean isValid() throws EventValidationException {
        if(this.event.getAttendee() == this.value) {
        	return true;
        }
        return false;
    }
}