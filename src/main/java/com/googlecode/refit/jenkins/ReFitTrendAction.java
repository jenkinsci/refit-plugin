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

import hudson.model.Action;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;

import java.io.IOException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class ReFitTrendAction implements Action {
    
    private AbstractProject<?, ?> project;



    public ReFitTrendAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Fit Test Result Trend";
    }

    @Override
    public String getUrlName() {
        return "refitTrend";
    }

    public boolean buildDataExists() {
        return project.getLastSuccessfulBuild() != null;
    }
    
    public void doGraph( StaplerRequest req, StaplerResponse rsp ) throws IOException {
        if (ChartUtil.awtProblemCause != null) {
            // not available. send out error message
            rsp.sendRedirect2(req.getContextPath() + "/images/headless.png");
            return;
        }

        getGraph().doPng(req, rsp);
    }
    
    public void doMap( StaplerRequest req, StaplerResponse rsp ) throws IOException {
        if (ChartUtil.awtProblemCause != null) {
            // not available. send out error message
            rsp.sendRedirect2(req.getContextPath() + "/images/headless.png");
            return;
        }
        getGraph().doMap(req, rsp);
    }
    
    
    
   private ReFitGraph getGraph() {
       DataSetBuilder<String, ChartLabel> data = new DataSetBuilder<String, ChartLabel>();
       
       for (AbstractBuild<?, ?> build = project.getLastSuccessfulBuild(); 
           build != null; 
           build = build.getPreviousBuild() ) {
           
           ReFitBuildAction action = build.getAction(ReFitBuildAction.class);
           if (action == null) 
               continue;
           
           ReFitTestResult result = action.getTestResult();
           if (result == null)
               continue;
           
           data.add(action.getNumRight(), "2Passed", new ChartLabel(result));
           data.add(action.getNumWrong(), "1Failed", new ChartLabel(result));
           data.add(action.getNumIgnored(), "0Skipped", new ChartLabel(result));
       }
       
       
       return new ReFitGraph(data.build());
   }    
}
