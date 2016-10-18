/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mylib.rf.linkbudget;

import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JInternalFrame;

import com.csvreader.CsvWriter;

/**
 *
 * @author OfirBD
 */
public class LinkBudgetMain {
    
    LinkBudgetGUI window = null;
    
    DecimalFormat df = new DecimalFormat("##.#");
    
    // application variables
    private double linkMargin;
    private double noisePower;
    private double totalPathLoss;
    private double transmitPower;
    private double txLoss; // cable losses, implementation losses
    private double Gb;
    private double eirp;
    private double Gm;
    private double rxLoss; // cable losses, implementation losses
    private double rxGain;
    private double rxSignalLevel;
    private double receivedSNR;
    private double rxSensitivity;
    private double BW;
    private double NF;
    private double foliageDepth;
    private double dataRate;
    private double freq;
    private double Hb;
    private double Hm;
    private double requiredLinkDistance;
    private double linkBudget;
    private double snr;
    private String propagationModel;
    private String hataType; // urban, suburban or rural
    private int reliability; // for calculation of reliability margin: usually 50%, 90% or 99%
    private double reliabilityMargin; // Margin in dB relatively to the reliability in percentage
    private double maxLinkDistance;
    private double maxLinkDistanceAtReliabilityMargin;
    
    // log distance model variables
    private double pathLossAtd0;
    private double d0;
    private double pathLossExp;
    private double flatFaddingAttn;
    
    // instance variables for the charts
    private double vegetationLoss;
    private double chartDisplayMaxDistance;
    private Double[] distance; // distance in km
    private Double[] fadeMargin;
    private Double[] pathLoss50; // median path loss
    private Double[] pathLoss90; // 90% path loss
    private Double[] pathLoss99; // 99% path loss
    private Double[] hataRuralPathLoss50;
    private Double[] hataSuburbanPathLoss50;
    private Double[] hataSmallCityPathLoss50;
    private Double[] hataLargeCityPathLoss50;
    private Double[] egliPathLoss50;
    private Double[] groundBouncePathLoss50;
    private Double[] logDistancePathLoss50;
    private Double[] fsl;
    private Double[] rsl; // receive signal level
    
    // chart
    LinkBudgetChart chart;
    
    
    // constructor
    public LinkBudgetMain(LinkBudgetGUI window){
        this.window = window;
        
        chartDisplayMaxDistance = 100; //km
        int range = (int) (chartDisplayMaxDistance / 0.1); 
        distance = new Double[range];
        pathLoss50 = new Double[range];
        pathLoss90 = new Double[range];
        pathLoss99 = new Double[range];
        fsl = new Double[range];
        hataRuralPathLoss50 = new Double[range];
        hataSuburbanPathLoss50 = new Double[range];
        hataSmallCityPathLoss50 = new Double[range];
        hataLargeCityPathLoss50 = new Double[range];
        egliPathLoss50 = new Double[range];
        groundBouncePathLoss50 = new Double[range];
        logDistancePathLoss50 = new Double[range];
        fadeMargin = new Double[range];
        rsl = new Double[range];
        
        linkBudget = 0;
        reliability = 99; //99%
        
        chart = new LinkBudgetChart("Link Budget");
    }

    /**
     * calculate the reliability margin in dB
     * 
     * @param reliability
     */
    private double calcReliability(int reliability){
    	double reliabilityMargin;
    	
    	if(reliability==99){
    		reliabilityMargin = 20; // +20dB for 99% success rate
    	}else if(reliability==90){
    		reliabilityMargin = 10; // +10dB for 90% success rate
    	}else if(reliability==50){
    		reliabilityMargin = 0; // 0dB margin for median path loss
    	}else{
    		reliabilityMargin = 0; // 0dB margin for median path loss
    	}
    	
    	return reliabilityMargin;
    }

    /**
     * plot chart
     * 
     * @param panelSize
     * @return
     */
    public JInternalFrame plotChart(Dimension panelSize, String chartType){
    	JInternalFrame frame = null;
    	
    	// create y=linkBudget line for the chart
    	Double[] lb = new Double[distance.length];
    	for(int i=0;i<distance.length;i++){
    		lb[i] = linkBudget;
    	}
    	
    	// create rxThreshold sensitivity line for the chart
    	Double[] rxth = new Double[distance.length];
    	for(int i=0;i<distance.length;i++){
    		rxth[i] = rxSensitivity;
    	}
    	
    	// create rxSensitivity with reliabilityMargin line for the chart
    	Double[] rmSensitivity = new Double[distance.length];
    	for(int i=0;i<distance.length;i++){
    		rmSensitivity[i] = rxSensitivity + reliabilityMargin;
    	}
    	
    	// create link budget margin with reliabilityMargin line for the chart
    	Double[] linkBudgetRM = new Double[distance.length];
    	for(int i=0;i<distance.length;i++){
    		linkBudgetRM[i] = linkBudget - reliabilityMargin;
    	}

    	// create reliability margin line for the chart
    	Double[] rm = new Double[distance.length];
    	for(int i=0;i<distance.length;i++){
    		rm[i] = reliabilityMargin;
    	}


    	// create rxNoisePower line for the chart
    	Double[] rxNoisePower = new Double[distance.length];
    	for(int i=0;i<distance.length;i++){
    		rxNoisePower[i] = noisePower;
    	}

    	//boolean pl99Visible=false;
    	//boolean pl90Visible=false;
    	boolean pl50Visible=false;
    	
		pl50Visible = true;
    	// update fade margin
		for(int i=0;i<distance.length;i++){
			fadeMargin[i] = linkBudget - pathLoss50[i];
			rsl[i] = eirp - pathLoss50[i] + rxGain;
		}

//    	if(reliability==99){
//    		pl99Visible = true;
//        	// update fade margin
//    		for(int i=0;i<distance.length;i++){
//    			fadeMargin[i] = linkBudget - pathLoss99[i];
//    			rsl[i] = eirp - pathLoss99[i] + rxGain;
//    		}
//    	}else if(reliability==90){
//    		pl90Visible = true;
//        	// update fade margin
//    		for(int i=0;i<distance.length;i++){
//    			fadeMargin[i] = linkBudget - pathLoss90[i];
//    			rsl[i] = eirp - pathLoss90[i] + rxGain;
//    		}
//    	}else if(reliability==50){
//    		pl50Visible = true;
//        	// update fade margin
//    		for(int i=0;i<distance.length;i++){
//    			fadeMargin[i] = linkBudget - pathLoss50[i];
//    			rsl[i] = eirp - pathLoss50[i] + rxGain;
//    		}
//    	}

		boolean fadeMarginVisible = false;
		boolean fslVisible = true;
		boolean linkBudgetVisible = true;
		boolean rslVisible = false;
		if(chartType.equals("fadeMargin")){
			//pl99Visible = false;
			//pl90Visible = false;
			pl50Visible = false;
			fslVisible = false;
			linkBudgetVisible = false;
			fadeMarginVisible = true;
			chart.setTitle("Fade Margin");
		}else if(chartType.equals("rsl")){
			//pl99Visible = false;
			//pl90Visible = false;
			pl50Visible = false;
			fslVisible = false;
			linkBudgetVisible = false;
			rslVisible = true;
			chart.setTitle("Receive Strength Level");
		}else{
			chart.setTitle("Path Loss");
		}

    	ChartSeries distanceSeries = new ChartSeries("Distance", distance, true);
    	ChartSeries pathLoss50Series = new ChartSeries("PathLoss", pathLoss50, pl50Visible);
    	ChartSeries linkBudgetReliabilityMargin = new ChartSeries("ReliabilityMargin", linkBudgetRM, pl50Visible);
    	ChartSeries reliabilityMarginSeries = new ChartSeries("Reliability Margin", rm, fadeMarginVisible);
    	ChartSeries fadeMarginSeries = new ChartSeries("Fade Margin", fadeMargin, fadeMarginVisible);
    	ChartSeries fslSeries = new ChartSeries("Free Space Loss", fsl, fslVisible);
    	ChartSeries linkBudgetSeries = new ChartSeries("linkBudget", lb, linkBudgetVisible);
    	ChartSeries rslSeries = new ChartSeries("Rx Signal Level", rsl, rslVisible);
    	ChartSeries rxThreshold = new ChartSeries("Rx Sensitivity", rxth, rslVisible);
    	ChartSeries rxNoise = new ChartSeries("Rx Noise Floor", rxNoisePower, rslVisible);
    	ChartSeries rxReliabilitySensitivity = new ChartSeries("Rx Margin", rmSensitivity, rslVisible);
    	
    	chart.setX(distanceSeries);
    	chart.setY1(linkBudgetSeries);
    	chart.setY2(fslSeries);
    	chart.setY3(pathLoss50Series);
    	chart.setY4(linkBudgetReliabilityMargin);
    	chart.setY5(reliabilityMarginSeries);
    	chart.setY6(fadeMarginSeries);
    	chart.setY7(rslSeries);
    	chart.setY8(rxThreshold);
    	chart.setY9(rxNoise);
    	chart.setY10(rxReliabilitySensitivity);
    	
//    	if(chartType.equals("fadeMargin")){
//    		chart.setY6(fadeMarginSeries);
//    	}else if(chartType.equals("rsl")){
//    		chart.setY6(rslSeries);
//    	}else{
//    		chart.setY6(rslSeries); // default
//    	}
    	
    	// set x max value
    	double xMaxValue = Math.ceil(maxLinkDistance) + 5;// the x axis will be maxLinkDistance + 5km
    	chart.setxMaxValue(xMaxValue);

    	frame = chart.createIFrame(panelSize);
    	
    	return frame;
    }

    /**
     * link budget calculation from the radio parameters
     * 
     * @return linkbudget
     */
    public double linkBudgetCalc(){
    	double linkbudget = 0;
    	
    	linkbudget = eirp + rxGain - rxSensitivity;
    	
    	return linkbudget;
    }
    
    
    /**
     * Link Margin = EIRP - pathLoss + Grx -THrx
     * 
     * @param pathLoss the channel path loss 
     * @return linkMargin
     */
    public boolean linkMarginCalc(double pathLoss, boolean selectSNR){
    	boolean flag = true;
    	// eirp
    	this.eirp = transmitPower + Gb - txLoss;
    	// total path loss
    	this.totalPathLoss = pathLoss;
    	// rx gain
    	this.rxGain = Gm - rxLoss;
    	// rx received signal (RSL)
    	this.rxSignalLevel = eirp - pathLoss + rxGain - reliabilityMargin;
    	// Noise Power
    	this.noisePower = -174 + 10*Math.log10(BW*1e3) + NF;
    	// predicted snr
    	receivedSNR = rxSignalLevel - noisePower;
    	// receiver threshold - the receiver minimum received signal level that will provide reliable operation
    	double THrx;
    	
    	if(selectSNR){
    		THrx = noisePower+snr;
    		rxSensitivity = THrx;
    	}else{
    		THrx = rxSensitivity;
    	}
    	// if the rx sensitivity lower than the noise power then it is wrong
    	// the user must input higher sensitivity value
    	if(rxSensitivity<noisePower){
           flag = false;
    	}
    	// the link margin
    	this.linkMargin = this.rxSignalLevel - THrx;
    	
		return flag;
    }
    
    public double findMaxLinkDistance(){
    	
    	double maxLinkDistance=0;
    	
    	// get the distance where the pathloss equal to the linkbudget
    	for(int i=0;i<distance.length;i++){
			if(linkBudget<=pathLoss50[i]){
				maxLinkDistance = distance[i];
				break;
			}
		}
			
    	// find maximum link distance with reliability margin
    	for(int i=0;i<distance.length;i++){
			if(linkBudget<=(pathLoss50[i]+reliabilityMargin)){
				maxLinkDistanceAtReliabilityMargin = distance[i];
				break;
			}
			
		}

//    	if(reliability==99){
//    		for(int i=0;i<distance.length;i++){
//    			if(linkBudget<=pathLoss99[i]){
//    				maxLinkDistance = distance[i];
//    				break;
//    			}
//    		}
//    	}else if(reliability==90){
//    		for(int i=0;i<distance.length;i++){
//    			if(linkBudget<=pathLoss90[i]){
//    				maxLinkDistance = distance[i];
//    				break;
//    			}
//    		}
//    	}else if(reliability==50){
//    		for(int i=0;i<distance.length;i++){
//    			if(linkBudget<=pathLoss50[i]){
//    				maxLinkDistance = distance[i];
//    				break;
//    			}
//    		}
//    	}
    	return maxLinkDistance;
    	
    }
    
    /**
     * pathLoss
     * 
     * @return
     */
    public double pathLossCalc(){
    	double loss = 0;
    	
    	// calculate the reliability margin in dB
    	reliabilityMargin = calcReliability(reliability);
    	
    	vegetationLoss = 0.2 * Math.pow(freq,0.3)* Math.pow(foliageDepth, 0.6);
    	
        distance[0] = 0.1;
        for(int i=1; i<distance.length;i++){
        	distance[i] = distance[i-1] + chartDisplayMaxDistance/distance.length;
        }

    	
    	// calculate fsl
    	fsl();
    	
    	switch(propagationModel){
    	    case "Hata": loss = hata();
    	    	break;
    	    case "Plane-Earth": loss = groundBounce();
    	        break;
    	    case "Egli": loss = egli();
    	    	break;
            case "Log-Distance": loss = logDistance(d0, pathLossAtd0, pathLossExp, flatFaddingAttn);
    	        break;
    	    default: 
    	    	break;
    	}
    	System.out.println(propagationModel);
    	
    	loss = loss + vegetationLoss;
    	//loss = loss + vegetationLoss + reliabilityMargin;
    	
//    	if(reliability==50){
//    		loss = loss + vegetationLoss;
//    	}else if(reliability==90){
//    		loss = loss + vegetationLoss+ 10; // +10dB for 90% success rate
//    	}else if(reliability==99){
//    		loss = loss + vegetationLoss+ 20; // +20dB for 99% success rate
//    	}
//    	
    	//loss = loss + vegetationLoss;
    	
    	
    	for(int i=0;i<pathLoss50.length;i++){
    		pathLoss50[i] = pathLoss50[i] + vegetationLoss;
//    		pathLoss50[i] = pathLoss50[i] + vegetationLoss + reliabilityMargin;
    		pathLoss90[i] = pathLoss50[i] + vegetationLoss + 10; // +10dB for 90% success rate - used for writing csv file
    		pathLoss99[i] = pathLoss50[i] + vegetationLoss + 20; // +20dB for 99% success rate - used for writing csv file
//    		pathLoss50[i] = pathLoss50[i] + vegetationLoss;
//    		pathLoss90[i] = pathLoss50[i] + vegetationLoss + 10; // +10dB for 90% success rate
//    		pathLoss99[i] = pathLoss50[i] + vegetationLoss + 20; // +20dB for 99% success rate
    	}
    	
    	
    	return loss;
    }

   
    private double hata(){
    	double loss = 0;
    	
    	double a_hm = (1.1 * Math.log10(freq) - 0.7)*Hm - (1.56*Math.log10(freq)-0.8);
    	double a_hm_largeCity = 3.2*(Math.pow((Math.log10(11.75*Hm)),2)) - 4.97;
    	
    	double A = 69.55 + 26.16*Math.log10(freq) - 13.82*Math.log10(Hb);
    	double B = 44.9 - 6.55*Math.log10(Hb);
    	double C_rural = 4.78*(Math.pow((Math.log10(freq)),2)) - 18.33*Math.log10(freq) + 40.98;
    	double C_suburban = (2*Math.pow((Math.log10(freq)),2)) + 5.4;
    	double C_urban = 0;
    	
    	// L_hata = A + a_hm + B*log(distance) - C
    	for(int i=0; i<distance.length;i++){
    		hataRuralPathLoss50[i] = A - a_hm + B*Math.log10(distance[i]) - C_rural ;
    		hataSuburbanPathLoss50[i] = A - a_hm + B*Math.log10(distance[i]) - C_suburban ;
    		hataSmallCityPathLoss50[i] = A - a_hm + B*Math.log10(distance[i]) - C_urban ;
    		hataLargeCityPathLoss50[i] = A - a_hm_largeCity + B*Math.log10(distance[i]) - C_urban ;
    	}
    	
    	
    	if(hataType.equals("urban")){
    		loss = A - a_hm_largeCity + B*Math.log10(requiredLinkDistance) - C_urban ;
   			pathLoss50 = hataLargeCityPathLoss50;
    	}else if(hataType.equals("smallcity")){
    		loss = A - a_hm + B*Math.log10(requiredLinkDistance) - C_urban ;
   			pathLoss50 = hataSmallCityPathLoss50;
    	}else if(hataType.equals("suburban")){
    		loss = A - a_hm + B*Math.log10(requiredLinkDistance) - C_suburban ;
   			pathLoss50 = hataSuburbanPathLoss50;
    	}else if(hataType.equals("rural")){
    		loss = A - a_hm + B*Math.log10(requiredLinkDistance) - C_rural ;
   			pathLoss50 = hataRuralPathLoss50;
    	}
    	
    	return loss;
    }
    
    /**
     * FSL
     * Free Space Line Of Site Model
     * 
     * @return Loss the loss in dB
     */
    private double fsl(){
    	double loss = 0;
    	
    	loss = 32.45 + 20*Math.log10(requiredLinkDistance)+20*Math.log10(freq);
    	for(int i=0; i<distance.length;i++){
    		fsl[i] = 32.45 + 20*Math.log10(distance[i])+20*Math.log10(freq);
    	}
    	
    	return loss;
    }
    
    /**
     * Egli model
     * 
     * @return loss
     */
    private double egli(){
    	double loss = 0;
    	double egliFactor = 76.3;
    	
    	if(Hm>10){
    		egliFactor = 85.9;
    	}

    	//loss = -(20 * Math.log10(freq) + 40 * Math.log10(requiredLinkDistance*1000) - 20 * Math.log10(Hb) + egliFactor - 10 * Math.log10(Hm));
    	 
    	double beta = Math.pow((40 / freq),2);
    	loss = -10*Math.log10(Math.pow(Hb*Hm / (Math.pow(requiredLinkDistance*1000, 2)),2)*beta);
    	for(int i=0; i<distance.length;i++){
    		egliPathLoss50[i] = -10*Math.log10(Math.pow(Hb*Hm / (Math.pow(distance[i]*1000, 2)),2)*beta);
    		//egliPathLoss50[i] = -(20 * Math.log10(freq) + 40 * Math.log10(distance[i]*1000) - 20 * Math.log10(Hb) + egliFactor - 10 * Math.log10(Hm));
    	}
    	
    	pathLoss50 = egliPathLoss50;
    	
    	return loss;
    }
    
    private double groundBounce(){
    	double loss = 0;
    	double lossFsl = 0;
    	
    	lossFsl = 32.45 + 20*Math.log10(requiredLinkDistance)+20*Math.log10(freq);
    	
    	double beta = 1;
    	loss = -10*Math.log10(Math.pow(Hb*Hm / (Math.pow(requiredLinkDistance*1000, 2)),2)*beta);
    	// if loss < free LOS path loss than the loss is equal to fsl
    	if(loss<lossFsl){
    		loss = lossFsl;
    	}
    	
    	for(int i=0; i<distance.length;i++){
    		groundBouncePathLoss50[i] = -10*Math.log10(Math.pow(Hb*Hm / (Math.pow(distance[i]*1000, 2)),2)*beta);
    		// if loss < free LOS path loss than the loss is equal to fsl
    		if(groundBouncePathLoss50[i]<fsl[i]){
    			groundBouncePathLoss50[i] = fsl[i];
    		}
    	}
    	
    	pathLoss50 = groundBouncePathLoss50;
    	
    	return loss;
    }
    
    /**
     * The log-distance model
     * L50 = PL(d0) + 10*Gama*log10(d/d0) + Xs
     * 
     * @param d0 the measured reference distance
     * @param pathLossAtd0 the measured path loss at the reference distance 
     * @param pathLossExp the path loss exponent (between 2 to 5)
     * @param flatFaddingAttn a constant attenuation according to the environment
     * @return loss the path loss at required distance
     */
    private double logDistance(double d0, double pathLossAtd0, double pathLossExp, double flatFaddingAttn){
        double loss = 0;
        
        loss = pathLossAtd0 + 10 * pathLossExp * Math.log10(requiredLinkDistance/d0) + flatFaddingAttn;
        
        for(int i=0; i<distance.length;i++){
        	logDistancePathLoss50[i] = pathLossAtd0 + 10 * pathLossExp * Math.log10(distance[i]/d0) + flatFaddingAttn;
        }
        
        pathLoss50 = logDistancePathLoss50;
        
        return loss;
    }

    
	public void saveCsvResultsFile(String outputFile){
		
		// before we open the file check to see if it already exists
				boolean alreadyExists = new File(outputFile).exists();
					
				try {
					// use FileWriter constructor that specifies open (no appending)
					CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, false), ',');
					
					csvOutput.write("Distance");
					csvOutput.write("FSL");
					csvOutput.write("L50");
					csvOutput.write("L90");
					csvOutput.write("L99");
					csvOutput.write("Fade Margin");
					csvOutput.endRecord();

					// write the records
					for(int i=0; i<distance.length;i++){
						csvOutput.write(df.format(distance[i]).toString());
						csvOutput.write(df.format(fsl[i]).toString());
						csvOutput.write(df.format(pathLoss50[i]).toString());
						csvOutput.write(df.format(pathLoss90[i]).toString());
						csvOutput.write(df.format(pathLoss99[i]).toString());
						csvOutput.write(df.format(fadeMargin[i]).toString());
						csvOutput.endRecord();
					}
					
					csvOutput.close();
					System.out.println("file " + outputFile + " successfully generated");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
    
    /**
     * method to delete a file
     * @param file_name
     */
	public static void deleteFile(String file_name)
	{
		File file = new File(file_name);
		boolean exist;
		try {
			exist = file.createNewFile();
			//System.out.println("exist file: " + exist);
			if (!exist)
			{
				file.delete();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} // end method
    
    // getters and setters
	public double getTransmitPower() {
		return transmitPower;
	}


	public double getMaxLinkDistanceAtReliabilityMargin() {
		return maxLinkDistanceAtReliabilityMargin;
	}

	public double getReliabilityMargin() {
		return reliabilityMargin;
	}

	public double getMaxLinkDistance() {
		return maxLinkDistance;
	}


	public void setMaxLinkDistance(double maxLinkDistance) {
		this.maxLinkDistance = maxLinkDistance;
	}


	public double getLinkMargin() {
		return linkMargin;
	}


	public double getReceivedSNR() {
		return receivedSNR;
	}


	public double getNoisePower() {
		return noisePower;
	}


	public double getRxGain() {
		return rxGain;
	}


	public double getTotalPathLoss() {
		return totalPathLoss;
	}


	public double getEirp() {
		return eirp;
	}


	public double getRxSignalLevel() {
		return rxSignalLevel;
	}


	public double getVegetationLoss() {
		return vegetationLoss;
	}


	public void setTxLoss(double txLoss) {
		this.txLoss = txLoss;
	}


	public void setRxLoss(double rxLoss) {
		this.rxLoss = rxLoss;
	}


	public void setLinkMargin(double linkMargin) {
		this.linkMargin = linkMargin;
	}


	public Double[] getDistance() {
		return distance;
	}


	public Double[] getPathLoss50() {
		return pathLoss50;
	}


	public Double[] getFsl() {
		return fsl;
	}


	public void setTransmitPower(double transmitPower) {
		this.transmitPower = transmitPower;
	}


	public double getGb() {
		return Gb;
	}


	public void setGb(double gb) {
		Gb = gb;
	}


	public double getGm() {
		return Gm;
	}


	public void setGm(double gm) {
		Gm = gm;
	}


	public double getRxSensitivity() {
		return rxSensitivity;
	}


	public void setRxSensitivity(double rxSensitivity) {
		this.rxSensitivity = rxSensitivity;
	}


	public double getBW() {
		return BW;
	}


	public void setBW(double bW) {
		BW = bW;
	}


	public double getNF() {
		return NF;
	}


	public void setNF(double nF) {
		NF = nF;
	}


	public double getFoliageDepth() {
		return foliageDepth;
	}


	public void setFoliageDepth(double foliageDepth) {
		this.foliageDepth = foliageDepth;
	}


	public double getDataRate() {
		return dataRate;
	}


	public void setDataRate(double dataRate) {
		this.dataRate = dataRate;
	}


	public double getfreq() {
		return freq;
	}


	public void setfreq(double freq) {
		this.freq = freq;
	}


	public double getHb() {
		return Hb;
	}


	public void setHb(double hb) {
		Hb = hb;
	}


	public double getHm() {
		return Hm;
	}


	public void setHm(double hm) {
		Hm = hm;
	}


	public double getRequiredLinkDistance() {
		return requiredLinkDistance;
	}


	public void setRequiredLinkDistance(double requiredLinkDistance) {
		this.requiredLinkDistance = requiredLinkDistance;
	}


	public double getLinkBudget() {
		return linkBudget;
	}


	public void setLinkBudget(double linkBudget) {
		this.linkBudget = linkBudget;
	}


	public double getSnr() {
		return snr;
	}


	public void setSnr(double snr) {
		this.snr = snr;
	}


	public String getPropagationModel() {
		return propagationModel;
	}


	public void setPropagationModel(String propagationModel) {
		this.propagationModel = propagationModel;
	}


	public String getHataType() {
		return hataType;
	}


	public void setHataType(String hataType) {
		this.hataType = hataType;
	}


	public int getReliability() {
		return reliability;
	}


	public void setReliability(int reliability) {
		this.reliability = reliability;
	}


	public void setPathLossAtd0(double pathLossAtd0) {
		this.pathLossAtd0=pathLossAtd0;
	}


	public void setD0(double d0) {
		this.d0 = d0;
	}


	public void setPathLossExp(double pathLossExp) {
		this.pathLossExp = pathLossExp;
	}


	public void setFlatFaddingAttn(double flatFaddingAttn) {
		this.flatFaddingAttn = flatFaddingAttn;
	}
    
}
