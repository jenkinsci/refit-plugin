package com.googlecode.refit.jenkins;

import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.tasks.test.TestResult;

import java.awt.Color;

public class ChartLabel implements Comparable<ChartLabel> {
    TestResult o;
    private String url;

    public ChartLabel(TestResult o) {
        this.o = o;
        this.url = null;
    }

    public String getUrl() {
        if (this.url == null)
            generateUrl();
        return url;
    }

    private void generateUrl() {
        AbstractBuild<?, ?> build = o.getOwner();
        String buildLink = build.getUrl();
        String actionUrl = o.getTestResultAction().getUrlName();
        this.url = Hudson.getInstance().getRootUrl() + buildLink + actionUrl + o.getUrl();
    }

    public int compareTo(ChartLabel that) {
        return this.o.getOwner().number - that.o.getOwner().number;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChartLabel)) {
            return false;
        }
        ChartLabel that = (ChartLabel) o;
        return this.o == that.o;
    }

    public Color getColor() {
        return null;
    }

    @Override
    public int hashCode() {
        return o.hashCode();
    }

    @Override
    public String toString() {
        String l = o.getOwner().getDisplayName();
        String s = o.getOwner().getBuiltOnStr();
        if (s != null)
            l += ' ' + s;
        return l;
        // return o.getDisplayName() + " " + o.getOwner().getDisplayName();
    }

}
