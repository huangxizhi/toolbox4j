package club.crazyai.utils.log;

import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

public class DebugPointContainer extends LinkedList<DebugPoint> {
	private static final long serialVersionUID = 2231920833393859099L;
	private static final ThreadLocal<DebugPointContainer> context = new ThreadLocal<DebugPointContainer>();
	private boolean isEnable = false;
	
	public static void addPoint(DebugPoint point) {
		DebugPointContainer container = getContainer();
		if (container.isEnable) {
			container.add(point);
		}
	}
	
	public static boolean isEnable() {
		DebugPointContainer container = getContainer();
		return container.isEnable;
	}
	
	public static void setEnable() {
		DebugPointContainer container = getContainer();
		container.isEnable = true;
	}
	
	public static String format() {
		return StringUtils.join(getContainer(), "->");
	}
	
	public static final DebugPointContainer getContainer() {
		DebugPointContainer container = context.get();
		if (container == null) {
			container = new DebugPointContainer();
			context.set(container);
		}
		
		return context.get();
	}
	
	//清理threadlocal变量，防止oom
	public static final void releaseContainer() {
		DebugPointContainer container = context.get();
		if (container != null) {
			context.set(null);
			container = null;
		}
	}
}
