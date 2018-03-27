package club.crazyai.utils.log;

public class L {
	private static final ThreadLocal<L> context = new ThreadLocal<L>();
	private String gid;
	private String formatGid;
	private boolean isEnable = true;
	
	public static void setGid(String gid) {
		L container = getContainer();
		if (container.isEnable) {
			container.gid = gid;
			container.formatGid = String.format("gid:%s; ", gid);
		}
	}
	
	public static String gid() {
		L container = getContainer();
		return container.gid;
	}
	
	public static String g() {
		L container = getContainer();
		return container.formatGid;
	}
	
	public static boolean isEnable() {
		L container = getContainer();
		return container.isEnable;
	}
	
	public static void setEnable() {
		L container = getContainer();
		container.isEnable = true;
	}
	
	public static final L getContainer() {
		L container = context.get();
		if (container == null) {
			container = new L();
			context.set(container);
		}
		return container;
	}
	
	//清理threadlocal变量，防止oom
	public static final void releaseContainer() {
		L container = context.get();
		if (container != null) {
			context.set(null);
			container = null;
		}
	}

}
