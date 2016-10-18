package com.mylib.rf.linkbudget;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JInternalFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class LinkBudgetChart extends ApplicationFrame{
	private static final long serialVersionUID = 1L;
	
	private String title;
    public void setTitle(String title) {
		this.title = title;
	}

	private JFreeChart chart;
    private ChartPanel chartPanel;
    
    private double xMaxValue;
    
    public void setxMaxValue(double xMaxValue) {
		this.xMaxValue = xMaxValue;
	}

	private ChartSeries x;
    private ChartSeries y1;
    private ChartSeries y2;
    private ChartSeries y3;
    private ChartSeries y4;
    private ChartSeries y5;
    private ChartSeries y6;
    private ChartSeries y7;
    private ChartSeries y8;
    private ChartSeries y9;
    private ChartSeries y10;

    public void setX(ChartSeries x) {
		this.x = x;
	}

	public void setY1(ChartSeries y1) {
		this.y1 = y1;
	}

	public void setY2(ChartSeries y2) {
		this.y2 = y2;
	}

	public void setY3(ChartSeries y3) {
		this.y3 = y3;
	}
	public void setY4(ChartSeries y4) {
		this.y4 = y4;
	}
	public void setY5(ChartSeries y5) {
		this.y5 = y5;
	}
	public void setY6(ChartSeries y6) {
		this.y6 = y6;
	}

	public void setY7(ChartSeries y7) {
		this.y7 = y7;
	}

	public void setY8(ChartSeries y8) {
		this.y8 = y8;
	}

	public void setY9(ChartSeries y9) {
		this.y9 = y9;
	}

	public void setY10(ChartSeries y10) {
		this.y10 = y10;
	}

	/**
     * constructor
     * 
     * @param title
     */
	public LinkBudgetChart(String title) {
		super(title);
		this.title = title;
		
	}
	
	/**
	 * createIFrame
	 * 
	 * @param panelSize
	 * @return
	 */
	public JInternalFrame createIFrame(Dimension panelSize){

		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		//JFreeChart chart = createCombinedChart();
		ChartPanel chartPanel = new ChartPanel(chart);        
        //chartPanel.setPreferredSize(new Dimension(100, 200));
        chartPanel.setPreferredSize(panelSize);
    	// creates the internal frame
        //JInternalFrame frame = new JInternalFrame(title,
        JInternalFrame frame = new JInternalFrame("Link Budget", 
				true,  //resizable
				true,  //closable
				true,  //maximizable
				true); //iconifiable

        frame.getContentPane().add(chartPanel);
        
        return frame;
	}
	
    private XYDataset createDataset() {
    	XYSeries series1 = new XYSeries(y1.getName());
    	XYSeries series2 = new XYSeries(y2.getName());
    	XYSeries series3 = new XYSeries(y3.getName());
    	XYSeries series4 = new XYSeries(y4.getName());
    	XYSeries series5 = new XYSeries(y5.getName());
    	XYSeries series6 = new XYSeries(y6.getName());
    	XYSeries series7 = new XYSeries(y7.getName());
    	XYSeries series8 = new XYSeries(y8.getName());
    	XYSeries series9 = new XYSeries(y9.getName());
    	XYSeries series10 = new XYSeries(y10.getName());
    	
    	for(int i=0; i<x.getSeries().length;i++){
        	if(y1.isVisible()){
        		series1.add(x.getSeries()[i], y1.getSeries()[i]);
        	}
    		
        	if(y2.isVisible()){
        		series2.add(x.getSeries()[i], y2.getSeries()[i]);
        	}
    		
        	if(y3.isVisible()){
        		series3.add(x.getSeries()[i], y3.getSeries()[i]);
        	}
    		
        	if(y4.isVisible()){
        		series4.add(x.getSeries()[i], y4.getSeries()[i]);
        	}
    		
        	if(y5.isVisible()){
        		series5.add(x.getSeries()[i], y5.getSeries()[i]);
        	}
        	
        	if(y6.isVisible()){
        		series6.add(x.getSeries()[i], y6.getSeries()[i]);
        	}
        	if(y7.isVisible()){
        		series7.add(x.getSeries()[i], y7.getSeries()[i]);
        	}
        	if(y8.isVisible()){
        		series8.add(x.getSeries()[i], y8.getSeries()[i]);
        	}
        	if(y9.isVisible()){
        		series9.add(x.getSeries()[i], y9.getSeries()[i]);
        	}
        	if(y10.isVisible()){
        		series10.add(x.getSeries()[i], y10.getSeries()[i]);
        	}
    		
    	}
    	
    	XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);
        dataset.addSeries(series5);
        dataset.addSeries(series6);
        dataset.addSeries(series7);
        dataset.addSeries(series8);
        dataset.addSeries(series9);
        dataset.addSeries(series10);
        
        return dataset;
    }
    
    /**
     * Creates a chart.
     *
     * @param dataset  the data for the chart.
     *
     * @return a chart.
     */
    private JFreeChart createChart(XYDataset dataset) {

        // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            title,      // chart title
            String.format("Distance [km]"), // x axis label
            String.format(title + " [dB]"), // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // get a reference to the plot for further customisation...
        XYPlot plot1 = (XYPlot) chart.getPlot();
        

        XYLineAndShapeRenderer renderer
                = (XYLineAndShapeRenderer) plot1.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        renderer.setBaseSeriesVisibleInLegend(true);
        
        renderer.setSeriesLinesVisible(0, y1.isVisible());
        renderer.setSeriesLinesVisible(1, y2.isVisible());
        renderer.setSeriesLinesVisible(2, y3.isVisible());
        renderer.setSeriesLinesVisible(3, y4.isVisible());
        renderer.setSeriesLinesVisible(4, y5.isVisible());
        renderer.setSeriesLinesVisible(5, y6.isVisible());
        renderer.setSeriesLinesVisible(6, y7.isVisible());
        renderer.setSeriesLinesVisible(7, y8.isVisible());
        renderer.setSeriesLinesVisible(8, y9.isVisible());
        renderer.setSeriesLinesVisible(9, y10.isVisible());
        renderer.setSeriesItemLabelsVisible(0, y1.isVisible());
        renderer.setSeriesItemLabelsVisible(1, y2.isVisible());
        renderer.setSeriesItemLabelsVisible(2, y3.isVisible());
        renderer.setSeriesItemLabelsVisible(3, y4.isVisible());
        renderer.setSeriesItemLabelsVisible(4, y5.isVisible());
        renderer.setSeriesItemLabelsVisible(5, y6.isVisible());
        renderer.setSeriesItemLabelsVisible(6, y7.isVisible());
        renderer.setSeriesItemLabelsVisible(7, y8.isVisible());
        renderer.setSeriesItemLabelsVisible(8, y9.isVisible());
        renderer.setSeriesItemLabelsVisible(9, y10.isVisible());
        renderer.setSeriesVisibleInLegend(0, y1.isVisible());
        renderer.setSeriesVisibleInLegend(1, y2.isVisible());
        renderer.setSeriesVisibleInLegend(2, y3.isVisible());
        renderer.setSeriesVisibleInLegend(3, y4.isVisible());
        renderer.setSeriesVisibleInLegend(4, y5.isVisible());
        renderer.setSeriesVisibleInLegend(5, y6.isVisible());
        renderer.setSeriesVisibleInLegend(6, y7.isVisible());
        renderer.setSeriesVisibleInLegend(7, y8.isVisible());
        renderer.setSeriesVisibleInLegend(8, y9.isVisible());
        renderer.setSeriesVisibleInLegend(9, y10.isVisible());
        renderer.setSeriesPaint(0, Color.red); //linkBudget
        renderer.setSeriesPaint(1, Color.blue);//fsl
        renderer.setSeriesPaint(2, Color.black);//pathLoss50
        renderer.setSeriesPaint(3, Color.orange);//link budget with reliabilityMargin
        renderer.setSeriesPaint(4, Color.orange);//reliabilityMargin
        renderer.setSeriesPaint(5, Color.blue);//fadeMargin
        renderer.setSeriesPaint(6, Color.black);//rsl
        renderer.setSeriesPaint(7, Color.green);//rxThreshold
        renderer.setSeriesPaint(8, Color.RED);//rxNoise
        renderer.setSeriesPaint(9, Color.orange);//rxReliabilitySensitivity
        //renderer.setSeriesStroke(8, stroke);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesShapesVisible(2, false);
        renderer.setSeriesShapesVisible(3, false);
        renderer.setSeriesShapesVisible(4, false);
        renderer.setSeriesShapesVisible(5, false);
        renderer.setSeriesShapesVisible(6, false);
        renderer.setSeriesShapesVisible(7, false);
        renderer.setSeriesShapesVisible(8, false);
        renderer.setSeriesShapesVisible(9, false);

        // set chart y scale
        // ####
        
        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot1.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        ValueAxis domainAxis = (ValueAxis) plot1.getDomainAxis();
        domainAxis.setRange(0, xMaxValue); // set x axis max value
        //domainAxis.centerRange(0.0);
        
        // draw x,y lines at 0,0
        plot1.setDomainZeroBaselineVisible(true);
        plot1.setRangeZeroBaselineVisible(true);
        
        //CombinedDomainXYPlot plot = new CombinedDomainXYPlot(domainAxis);

        return chart;

    }

	
}
