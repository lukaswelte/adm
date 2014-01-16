package com.adm.model;

/**
 * Created by florian on 01/12/2013.
 */
public class EventItem {
    private String title;
    private int icon;
    private String location;
    private String description;


    public EventItem() {
    }
    public EventItem(String title, int icon, String location, String description) {
        this.title = title;
        this.icon=icon;
        this.location = location;
        this.description= description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location;  }

    public String getDescription() {  return description; }

    public void setDescription(String description) { this.description = description; }
}
