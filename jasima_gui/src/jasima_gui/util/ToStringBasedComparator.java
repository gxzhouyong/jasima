package jasima_gui.util;

import java.util.Comparator;

public class ToStringBasedComparator implements Comparator<Object> {
	public static final ToStringBasedComparator INSTANCE = new ToStringBasedComparator();

	protected ToStringBasedComparator() {
	}

	@Override
	public int compare(Object o1, Object o2) {
		return String.valueOf(o1).compareTo(String.valueOf(o2));
	}

}
