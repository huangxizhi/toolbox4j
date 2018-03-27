package club.crazyai.utils.log;

public class DebugPoint {
	private String branchName;
	private String nodeName;
	private String nodeVal;
	private int ptype = 0;
	
	public DebugPoint(String name, String val) {
		this.nodeName = name;
		this.nodeVal = val;
		this.ptype = 1;
	}
	
	//带分支的point
	public DebugPoint(String branch, String name, String val) {
		this.branchName = branch;
		this.nodeName = name;
		this.nodeVal = val;
		this.ptype = 2;
	}
	
	@Override
	public String toString() {
		if (ptype == 1)
			return String.format("(%s,%s)", nodeName, nodeVal);
		else 
			return String.format("(@%s,%s,%s)", branchName, nodeName, nodeVal);
	}
}