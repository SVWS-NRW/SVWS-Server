package de.svws_nrw.davapi.api;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.davapi.model.dav.Prop;
import jakarta.validation.constraints.NotNull;

/**
 * Utility-Klasse vereinfacht das Auslesen der angeforderten Properties aus
 * einem Prop-Objekt. Das Prop-Objekt wird als Bestandteil des DAV-Requests
 * gesendet und legt fest, welche Properties einer Ressource angefordert werden.
 * Die im Prop-Objekt enthaltenen Properties sind generisch.
 */
class DynamicPropUtil {

	/** Prop-Objekt aus dem DAV-Request */
	private final Prop propRequested;

	/** Liste von Properties (Reflection), die im Prop-Objekt enthalten sind */
	private final List<Field> fieldsRequested;

	/**
	 * Konstruktor
	 *
	 * @param propRequested Prop-Objekt aus dem Request.
	 */
	DynamicPropUtil(@NotNull final Prop propRequested) {
		this.propRequested = propRequested;
		this.fieldsRequested = this.getPropsFieldsNotNull(propRequested);
	}

	/**
	 * Prüft, ob eine bestimmte Property im Prop-Objekt angefordert wurde.
	 *
	 * @param fieldType Typklasse der Property, für die Prüfung erfolgen soll.
	 * @return true, falls die Property zur gegebenen Typklasse im Prop-Objekt
	 *         enthalten ist.
	 */
	protected final boolean getIsFieldRequested(@NotNull final Class<?> fieldType) {
		for (final Field field : fieldsRequested) {
			if (field.getType() == fieldType) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Ermittelt per Reflection die angefragten Properties den Prop-Objekt.
	 *
	 * @param propRequested Prop-Objekt aus dem Request.
	 * @return Liste der angefragten Properties als Field-Objekte (Relection)
	 */
	@SuppressWarnings("static-method")
	private List<Field> getPropsFieldsNotNull(@NotNull final Prop propRequested) {
		final Field[] fields = propRequested.getClass().getDeclaredFields();
		final List<Field> requestedFields = new ArrayList<>();
		for (final Field field : fields) {
			field.setAccessible(true);
			try {
				if (field.get(propRequested) != null) {
					requestedFields.add(field);
				}
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return requestedFields;
	}

	/**
	 * Ermittelt alle Properties aus dem Prop-Objekt des Requests, zu denen keine
	 * Entsprechung im Prop-Ergebnisobjekt enthalten ist - also Properties, die vom
	 * Dispatcher nicht unterstützt werden.
	 *
	 * @param propResponded Prop-Objekt, Ergebnisobjekt des Dispatchers.
	 * @return Prop-Objekt mit allen nicht-unterstützten Properties, bzw. null,
	 *         falls keine nicht-unterstützten Properties gefunden wurden.
	 */
	public Prop getUnsupportedProps(@NotNull final Prop propResponded) {
		final List<Field> requestedFields = getPropsFieldsNotNull(propRequested);
		final List<Field> respondedFields = getPropsFieldsNotNull(propResponded);
		final List<Field> fds = requestedFields.stream().filter(f -> !respondedFields.contains(f)).toList();
		if (!fds.isEmpty()) {
			final Prop prop404 = new Prop();
			for (final Field field : fds) {
				try {
					final Object instance = field.getType().getDeclaredConstructor().newInstance();
					field.set(prop404, instance);

				} catch (InstantiationException | IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
			return prop404;
		}
		return null;
	}

}
