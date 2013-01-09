package jasima_gui.editor;

import java.lang.reflect.Type;

public interface IProperty {

	/**
	 * Of course, all properties are important - some are just especially
	 * important.
	 * 
	 * @return true if this property is especially important
	 */
	public boolean isImportant();

	public boolean isWritable();

	public String getName();

	public String getHTMLDescription();

	public Type getType();

	/**
	 * Determines if the property can be set to null. This is always false if
	 * the property has a primitive type. It may still be true if the setter
	 * prevents null values. It can artificially be set to false by EditorWidget
	 * implementations that handle the object's life cycle, so that those who
	 * don't can still be used as soon as an actual instance exists.
	 * 
	 * @return false if the property can never be set to null
	 */
	public boolean canBeNull();

	public Object getValue() throws PropertyException;

	public void setValue(Object val) throws PropertyException;

}
