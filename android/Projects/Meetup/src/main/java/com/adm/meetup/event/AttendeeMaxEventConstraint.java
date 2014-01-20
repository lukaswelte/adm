package com.adm.meetup.event;

public class AttendeeMaxEventConstraint extends EventDecorator implements IEventConstraint {
	BaseEvent event = null;
	Long value = Long.MAX_VALUE;
	public class AttendeeMaxEventConstraintException extends EventValidationException {
		private static final long serialVersionUID = 1L;
	}
	public AttendeeMaxEventConstraint(BaseEvent event)  throws EventValidationException {
		super(event);
		this.event = event; 	 
	}
	
    public AttendeeMaxEventConstraint(BaseEvent event, Long value) throws EventValidationException {
		super(event);
		this.event = event;
		this.value = value;
		if(!this.isValid()) {
			throw new AttendeeMaxEventConstraintException();
		}
	}
    
	public boolean isValid() throws EventValidationException {
        if(this.event.getAttendee() <= this.value) {
        	return true;
        }
        return false;
    }
}