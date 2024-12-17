package de.svws_nrw.module.reporting.types;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Basis-Klasse im Rahmen des Reportings auf der alle anderen Reportings-Types aufbauen.
 * Hier können Methoden implementiert werden, die für alle Types relevant sind.
 */
public abstract class ReportingBaseType {

	/**
	 * Erstellt ein neues Objekt auf Basis dieser Klasse.
	 */
	protected ReportingBaseType() {
		// Standardkonstruktor ohne Inhalt, da Klasse ohne Attribute.
	}

	protected static String ersetzeNullBlankTrim(final String value) {
		if ((value == null) || value.isBlank())
			return "";
		return value.trim();
	}


	/**
	 * Ersetzt im Quellobjekt in den darin enthaltenen String-Attributen null durch einen leeren String.
	 *
	 * @param quellobjekt 					Das Objekt, in dem die Werte ersetzt werden sollen.
	 * @param ersetzeFelderAusObjektClass	Legt fest, ob die Werte in den Feldern der aufrufenden Klasse ebenfalls ersetzt werden sollen.
	 */
	protected static void ersetzeStringNullDurchEmpty(final Object quellobjekt, final boolean ersetzeFelderAusObjektClass) {
		if (quellobjekt == null)
			return;

		// Ermittle alle Felder der Klasse des Quellobjektes und ersetze deren Werte im Quellobjekt.
		if (ersetzeFelderAusObjektClass) {
			final Field[] felderClass = quellobjekt.getClass().getDeclaredFields();
			ersetzeFeldwerte(quellobjekt, felderClass);
		}

		// Ermittle alle Felder der Superklasse des Quellobjektes und ersetze deren Werte im Quellobjekt.
		Class<?> klasse = quellobjekt.getClass().getSuperclass();
		while ((klasse != null) && (klasse != Object.class) && (klasse != ReportingBaseType.class)) {
			final Field[] felderClass = klasse.getDeclaredFields();
			ersetzeFeldwerte(quellobjekt, felderClass);
			klasse = klasse.getSuperclass();
		}
	}

	private static void ersetzeFeldwerte(final Object quellobjekt, final Field[] felderQuellobjekt) {
		// Filtere nun aus allen Felder die heraus, die nicht static sind. Dadurch können die Werte später sicher zugewiesen.
		// Wenn es sich dann dabei noch um Strings handelt, welche null sind, dann ersetze null durch einen leeren String.
		int modifiers;
		for (final Field feld : felderQuellobjekt) {
			modifiers = feld.getModifiers();
			if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
				try {
					feld.setAccessible(true);
					if ((feld.getType() == String.class) && (feld.get(quellobjekt) == null)) {
						// Da der Aufruf aus der Basisklasse erfolgt, können normalerweise nur public-Felder geändert werden.
						// Setze daher das Flag für den Zugriff per Reflection auf true. Dies ändert nichts an den Festlegungen in der Klasse.
						feld.set(quellobjekt, "");
					}
				} catch (@SuppressWarnings("unused") final IllegalAccessException ignore) {
					// Aufgrund der Struktur und des vorherigen Kommentars sollte hier kein Fehler auftreten.
					// Sollte dies dennoch der Fall sein, dann kann er ignoriert werden und der Wert des Feldes wird nicht geändert.
				}
			}
		}
	}
}
