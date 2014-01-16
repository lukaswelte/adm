package com.adm.model;

/**
 * Created by florian on 30/11/2013.
 */
public class SideBarItem {

    private String title;
    private int icon;


    public SideBarItem(){}

    public SideBarItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }


    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }


    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

}