package jasima_gui.editor;

import java.util.Map;

import org.osgi.framework.Bundle;

public class DirectProperty implements IProperty {

	protected IProperty parent;
	protected boolean isImportant;
	protected String name;
	protected String description;
	protected Class<?> type;
	protected boolean canBeNull;

	public DirectProperty(Bundle contributingBundle, IProperty parent,
			Map<String, String> attributes) {
		this.parent = parent;
		isImportant = "true".equals(attributes.get("important"));
		name = attributes.get("name");
		description = attributes.get("description");
		try {
			type = contributingBundle.loadClass(attributes.get("type"));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		canBeNull = "true".equals(attributes.get("canBeNull"));
	}

	@Override
	public boolean isImportant() {
		return isImportant;
	}

	@Override
	public boolean isWritable() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getHTMLDescription() {
		return description;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public boolean canBeNull() {
		return canBeNull;
	}

	@Override
	public Object getValue() throws PropertyException {
		return parent.getValue();
	}

	@Override
	public void setValue(Object val) throws PropertyException {
		parent.setValue(val);
	}

}
