# Writing a property editor #

Every property editor must extend EditorWidget and must contain a constructor that accepts exactly one argument of type `Composite`, which must be passed on to `EditorWidget`'s constructor.
Editors are themselves responsible for calling storeValue - this should typically done when the editor loses focus.
The property that the editor is responsible for can be accessed as the protected field property, which is of type `IProperty` and implements the methods `getValue` and `setValue`.
Any exception that the getter/setter methods generate (typically `IllegalArgumentException` or `NullPointerException`) will be wrapped in a `PropertyException`.
```
public class SimpleEditor extends EditorWidget {
	private Label lblValue;

	public SimpleEditor(Composite parent) {
		super(parent);
	}

	public void createControls() {
		lblValue = toolkit.createLabel(this, "");
	}

	public void storeValue() {
		//
	}

	public void loadValue() {
		try {
			lblValue.setText(String.valueOf(property.getValue()));
		} catch (PropertyException e) {
			showError(e.getLocalizedMessage());
		}
	}

}
```

The editor can be registered for a certain property type by registering at the extension point `jasima_gui.objectEditors`.
The plugin uses the following rules to find an editor for a type:
  1. if the property can be null, only consider editors whose `handlesLifeCycle` attribute is true
  1. if the property can't be null:
    1. consider all editors whose `handlesLifeCycle` attribute is not true
    1. if no matches are found, repeat the search considerung all editors
  1. look for an editor that directly handles the property type
  1. if none were found, repeat the search starting from 3. for any interface that is implemented by the property type but not its super class
  1. if none were found, repeat the search starting from 3. for the super class
  1. if none were found, use the editor for `java.lang.Object`
Special rules apply for determining a type's super class:
  1. the super class of a primitive type is its wrapper type
  1. the super class of an array is created by replacing the element type with its super class

For example, the search order of `LinkedHashSet` is:
LinkedHashSet, HashSet, Cloneable. Serializable, AbstractSet, Set, Collection

# Writing a property filter #

Property filters can be used to add or hide properties of a Java bean and are registered at the extension point `jasima_gui.propertyFilters`.
Filters are matched differently than editor widgets:
  * instead of looking for the most specific type first, the most generic filters are applied first
  * the search does not stop after a filter matched, but all matching filters are applied
Filters can create new properties using either an own `IProperty` implementation or `DirectProperty`, which directly passes the bean's value to the editor.
Setting the property type to an `EditorWidget` implementation will lead to the search being skipped and that type being used as the editor widget.
This means that an editor for some aspect of a bean can be implemented without registering it as an object editor and without writing an `IProperty` implementation.