package com.googlecode.refit.jenkins;

import hudson.model.Action;

import com.googlecode.refit.jenkins.jaxb.Summary;

public class ReFitBuildAction implements Action {
    
    private int numRight;
    private int numWrong;
    private int numExceptions;
    private int numIgnored;
    
    
    public ReFitBuildAction(Summary summary) {
        this.numRight = summary.getRight();
        this.numWrong = summary.getWrong();
        this.numExceptions = summary.getExceptions();
        this.numIgnored = summary.getIgnored();
    }


    public int getNumRight() {
        return numRight;
    }


    public int getNumWrong() {
        return numWrong;
    }


    public int getNumExceptions() {
        return numExceptions;
    }


    public int getNumIgnored() {
        return numIgnored;
    }


    @Override
    public String getIconFileName() {
        return null;
    }


    @Override
    public String getDisplayName() {        
        return "Fit Results per Build";
    }


    @Override
    public String getUrlName() {
        return "refit";
    }
    
    

}
