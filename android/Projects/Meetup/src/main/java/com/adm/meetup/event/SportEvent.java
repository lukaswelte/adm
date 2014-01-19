package com.adm.meetup.event;

public class SportEvent extends EventDecorator {
	public enum Fields implements IFields {
		SPORTNAME("sportname");
		
		private String name;
		Fields(String name) {
			this.name = name;
		}
		 
		public String getName() {
			return this.name;
		}
		
	}
	private String sportName;
	
    public SportEvent(BaseEvent event) {
		super(event);
		this.addField(SportEvent.Fields.SPORTNAME, null);
		// TODO Auto-generated constructor stub
	}
    
    public String getSportName() {
        return this.sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
        this.addField(SportEvent.Fields.SPORTNAME, this.sportName);
    }
}