package com.googlecode.refit.jenkins;

import hudson.model.AbstractBuild;
import hudson.tasks.test.TestObject;
import hudson.tasks.test.TestResult;

import com.googlecode.refit.jenkins.jaxb.Summary;

public class ReFitTestResult extends TestResult {
    
    private static final long serialVersionUID = 1L;
    
    private AbstractBuild<?, ?> build;

    private int passCount;
    private int failCount;
    private int skipCount;
    
    public ReFitTestResult(AbstractBuild<?, ?> build, Summary summary) {
        this.build = build;
        this.passCount = summary.getRight();
        this.failCount = summary.getWrong() + summary.getExceptions();
        this.skipCount = summary.getIgnored();
    }
    
    @Override
    public int getPassCount() {
        return passCount;
    }
    
    @Override
    public int getFailCount() {
        return failCount;
    }
    
    @Override
    public int getSkipCount() {
        return skipCount;
    }

    @Override
    public String getDisplayName() {
        return "ReFitTestResult";
    }

    @Override
    public AbstractBuild<?, ?> getOwner() {
        return build;
    }

    /**
     * Not used.
     */
    @Override
    public TestObject getParent() {
        return null;
    }

    /**
     * Not used.
     */
    @Override
    public TestResult findCorrespondingResult(String id) {
        return null;
    }
}
