package com.ir.models;

import com.opencsv.bean.CsvBindByName;

public class State {
    @CsvBindByName(column = "StateID")
    private String StateID;

    @CsvBindByName(column = "StateName")
    private String StateName;


    public State() {
    }

    public String getStateID() {
        if (StateID == null){
            StateID = "";
        }
        return StateID;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }

    public String getStateName() {
        if (StateName == null){
            StateName = "";
        }
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    @Override
    public String toString() {
        return "State{" +
                "StateID='" + StateID + '\'' +
                ", StateName='" + StateName + '\'' +
                '}';
    }
}
