package jasima_gui.editor;

public class PropertyException extends Exception {

	private static final long serialVersionUID = 4801173525092352477L;

	public PropertyException(String message) {
		super(message);
	}

	public PropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public static PropertyException newGetException(IProperty prop,
			Throwable cause) {
		return new PropertyException(String.format(
				"Couldn't get value of property %s.", prop), cause);
	}

	public static PropertyException newSetException(IProperty prop,
			Throwable cause) {
		if (cause instanceof IllegalArgumentException) {
			return new PropertyException(String.format(
					"Bad property value: %s", cause.getLocalizedMessage()),
					cause);
		}
		return new PropertyException(String.format(
				"Couldn't set value of property %s.", prop), cause);
	}

}
