package com.googlecode.refit.jenkins;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Calendar;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.ui.RectangleInsets;

import hudson.util.ColorPalette;
import hudson.util.DataSetBuilder;
import hudson.util.Graph;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;

// Just a dummy graph currently.
public class ReFitGraph extends Graph {

    public ReFitGraph() {
        super(Calendar.getInstance(), 350, 150);
    }

    @Override
    protected JFreeChart createGraph() {

        DataSetBuilder<String, String> dataSetBuilder = new DataSetBuilder<String, String>();
        dataSetBuilder.add(20, "bugcount", "#1");
        dataSetBuilder.add(25, "bugcount", "#2");
//              new NumberOnlyBuildLabel(point.getBuild()));
//        for (GraphPoint point : points) {
//            dataSetBuilder.add(point.getBugCount(), "bugcount",
//                    new NumberOnlyBuildLabel(point.getBuild()));
//        }

        final JFreeChart chart = ChartFactory.createLineChart(null, // chart title
                null, // unused
                "Bugs", // range axis label
                dataSetBuilder.build(), // data
                PlotOrientation.VERTICAL, // orientation
                false, // include legend
                true, // tooltips
                true // urls
                );

        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.black);

        CategoryAxis domainAxis = new ShiftedCategoryAxis("Build Number");
        plot.setDomainAxis(domainAxis);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setCategoryMargin(0.0);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // Using a custom render here so i can control how URLs and tooltips are added to the
        // imagemap
//        URLAndTooltipBuilder builder = new URLAndTooltipBuilder(points);
//        URLAndTooltipRenderer urlRenderer = new URLAndTooltipRenderer(builder);
//        urlRenderer.setBaseStroke(new BasicStroke(4.0f));
//        urlRenderer.setSeriesShapesVisible(0, true);
//        ColorPalette.apply(urlRenderer);
//        plot.setRenderer(urlRenderer);

        // crop extra space around the graph
        plot.setInsets(new RectangleInsets(5.0, 0, 0, 5.0));
        return chart;
    }

}
