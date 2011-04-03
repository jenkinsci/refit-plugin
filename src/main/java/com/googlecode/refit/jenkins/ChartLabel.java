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
