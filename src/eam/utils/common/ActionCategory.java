package eam.utils.common;

public enum ActionCategory {

	START_MONITOR("Eclipse-Start","1");

	private String action;
	
	private String category;
	
	private ActionCategory(String action,String category) {
		this.action = action;
		this.category = category;
	}
	
	public void setAction(String action) {
		this.action = action;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAction() {
		return action;
	}

	public String getCategory() {
		return category;
	}
	
}
