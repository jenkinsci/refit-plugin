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

import hudson.tasks.test.TestResult;
import hudson.util.ChartUtil;
import hudson.util.ColorPalette;
import hudson.util.Graph;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.StackedAreaRenderer2;

import java.awt.Color;
import java.awt.Paint;
import java.util.Calendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;

/**
 * Trend graph of Fit test results which appears on the project page of each project
 * with reFit enabled in its configuration.
 * <p>
 * This is a modified copy of {@code hudson.tasks.junit.History.GraphImpl}
 * 
 * @author Harald Wellmann
 *
 */
public class ReFitGraph extends Graph {

    private CategoryDataset data;

    /**
     * Creates a trend graph for a given data set
     * @param data  dataset with test results per build.
     */
    public ReFitGraph(CategoryDataset data) {
        /*
         * timestamp, graph width, graph height
         * TODO compute graph dimensions dynamically
         */
        super(Calendar.getInstance(), 500, 200);
        this.data = data;
    }

    /**
     * Creates a chart from the test result data set.
     * <p>
     * The i-th row of the data set corresponds to the results from the i-th build.
     * Each row has columns 0, 1, 2 containing the number of failed, skipped and passed tests.
     * The chart stacks these values from bottom to top, joins the values for the same columns
     * with lines and fills the areas between the lines with given colours.
     * <p> 
     * TODO Find out if Jenkins core provides any caching for the chart or the data set.
     */
    @Override
    protected JFreeChart createGraph() {

        final JFreeChart chart = ChartFactory.createStackedAreaChart(null, // chart title
                null, // domain axis label 
                "count", // range axis label
                data, // data
                PlotOrientation.VERTICAL, // orientation
                false, // include legend
                true, // tooltips
                false // urls
                );

        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setForegroundAlpha(0.8f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.black);

        CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
        plot.setDomainAxis(domainAxis);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setCategoryMargin(0.0);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        ChartUtil.adjustChebyshev(data, rangeAxis);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRange(true);

        StackedAreaRenderer ar = new StackedAreaRenderer2() {
            private static final long serialVersionUID = 1L;

            /**
             * Colour brush for the given data point.
             */
            @Override
            public Paint getItemPaint(int row, int column) {
                return super.getItemPaint(row, column);
            }

            /**
             * URL for the given data point, which gets opened when the user clicks on or near
             * the data point in the chart.
             */
            @Override
            public String generateURL(CategoryDataset dataset, int row, int column) {
                ChartLabel label = (ChartLabel) dataset.getColumnKey(column);
                return label.getUrl();
            }

            /**
             * Tooltip to be displayed on the chart when the user hovers at or near the
             * data point.
             * <p>
             * The tooltip has the format {@code #17 : 300 Passed}, indicating the build number
             * and the number of tests with the given result.
             */
            @Override
            public String generateToolTip(CategoryDataset dataset, int row, int column) {
                ChartLabel label = (ChartLabel) dataset.getColumnKey(column);
                TestResult result = label.getResult();
                String rowKey = (String) dataset.getRowKey(row);
                return result.getOwner().getDisplayName() + " : " + 
                    dataset.getValue(row, column) + " " + rowKey.substring(1);
            }
        };
        plot.setRenderer(ar);
        
        // Define colours for the data points by column index
        ar.setSeriesPaint(0, ColorPalette.RED); // Failed
        ar.setSeriesPaint(1, ColorPalette.YELLOW); // Skipped
        ar.setSeriesPaint(2, ColorPalette.BLUE); // Passed

        // crop extra space around the graph
        plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));

        return chart;
    }
}
