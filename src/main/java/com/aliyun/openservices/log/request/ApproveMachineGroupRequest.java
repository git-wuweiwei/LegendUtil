package com.aliyun.openservices.log.request;

public class ApproveMachineGroupRequest extends Request {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3351040215690628252L;
	protected String groupName = "";

	public ApproveMachineGroupRequest(String project, String groupName) {
		super(project);
		this.groupName = groupName;
	}

	public String GetGroupName() {
		return groupName;
	}

	public void SetGroupName(String groupName) {
		this.groupName = groupName;
	}
}
