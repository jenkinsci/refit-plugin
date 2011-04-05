/*
 * The MIT License
 * 
 * Copyright (c) 2011, Harald Wellmann
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.googlecode.refit.jenkins;

import hudson.model.AbstractBuild;
import hudson.tasks.test.TestObject;
import hudson.tasks.test.TestResult;

import com.googlecode.refit.jenkins.jaxb.Summary;

/**
 * Fit test results (only the counts) for a given build. This is similar to {@link Summary}, but
 * inherits from Hudson base classes.
 * <p>
 * TODO Not sure if we really need this. One point is that we strip all the individual test
 * results from Summary, to avoid persisting the whole lot in build.xml. But do we really have
 * to extend {@link TestResult}?
 * 
 * @author Harald Wellmann
 *
 */
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
