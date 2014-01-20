package com.adm.meetup.event;

public class FacebookEvent extends EventDecorator {
    public enum Fields implements IFields {
        FACEBOOK("isFacebook");

        private String name;

        Fields(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

    public FacebookEvent(BaseEvent event) {
        super(event);
        this.addField(FacebookEvent.Fields.FACEBOOK, new Integer(1).toString());
    }

    public boolean isFacebook() {
        return true;
    }
}