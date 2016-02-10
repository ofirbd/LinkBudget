package ofirbd.linkbudget;

public class ChartSeries {
	
	private Double[] series;
	private String name;
	private boolean visible;
	
	public ChartSeries(String name, Double[] series, boolean visible){
		this.visible = visible; // default value
		this.name = name;
		this.series = series;
	}

	public Double[] getSeries() {
		return series;
	}

	public void setSeries(Double[] series) {
		this.series = series;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	

}
