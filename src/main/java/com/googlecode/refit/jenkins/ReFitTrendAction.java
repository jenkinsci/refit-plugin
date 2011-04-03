package com.googlecode.refit.jenkins;

import java.io.IOException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.model.Action;
import hudson.util.ChartUtil;

public class ReFitTrendAction implements Action {

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
        return true;
    }
    
    public void doGraph( StaplerRequest req, StaplerResponse rsp ) throws IOException {
//        if( ChartUtil.awtProblemCause != null ){
//            rsp.sendRedirect2( req.getContextPath() + DEFAULT_IMAGE );
//            return;
//        }

        getGraph().doPng( req, rsp );
    }
    
    public void doMap( StaplerRequest req, StaplerResponse rsp ) throws IOException {
        getGraph().doMap( req, rsp );
    }
    
    
    
   public ReFitGraph getGraph() {
       return new ReFitGraph();
   }
    
}
