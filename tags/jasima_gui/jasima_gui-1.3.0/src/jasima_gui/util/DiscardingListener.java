package jasima_gui.util;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Responds to all delivered events by setting {@link Event#doit} to
 * <code>false</code>.
 * 
 * @author Robin Kreis
 */
public class DiscardingListener implements Listener {
	@Override
	public void handleEvent(Event event) {
		event.doit = false;
	}
}
