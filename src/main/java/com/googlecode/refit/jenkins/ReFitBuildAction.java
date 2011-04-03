package com.googlecode.refit.jenkins;

import hudson.model.Action;

public class ReFitBuildAction implements Action {
    
    private ReFitTestResult result;
    
    public ReFitBuildAction(ReFitTestResult result) {
        this.result = result;
    }


    public int getNumRight() {
        return result.getPassCount();
    }


    public int getNumWrong() {
        return result.getFailCount();
    }


    public int getNumExceptions() {
        return 0;
    }


    public int getNumIgnored() {
        return result.getSkipCount();
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


    public ReFitTestResult getTestResult() {
        return result;
    }
    
}
