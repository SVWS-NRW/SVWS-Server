package de.svws_nrw.module.pdf.types.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse enthält statische Hilfsmethoden rund um die DruckTypes.
 */
public final class ReportingTypesUtils {

	private ReportingTypesUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Die Methode ist primär für die von CoreTypes abgeleitetnen DruckTypes gedacht, da die CoreTypes keine entsprechenden Konstruktoren
	 * enthalten. Daher werden hier die Felder und ihre Inhalte mittels Reflection von einem Quellobjekt auf ein Zielobjekt übertragen,
	 * aber nur, wenn die Klasse des Quellobjektes eine Superklasse des Zielobjektes ist. Andernfalls findet keine Übertragung statt.
	 *
	 * @param Quellobjekt	Quellobjekt der Übertragung. Die Klasse dieses Objektes muss eine Superklasse der Klasse des Zielobjektes sein.
	 * @param Zielobjekt	Zielobjekt der Übertragung.
	 */
	public static void setFelderAusSuperklassenObjekt(final Object Quellobjekt, final Object Zielobjekt) {
		/*
		Die folgende Übertragung der Felder ist nur dann sicher möglich, wenn das Quellobjekt auf eine
		übergeordnete Klasse des Zielobjektes ist. Prüfe dies zunächst. Andernfalls wird die Methode abgebrochen.
		*/
		if (Quellobjekt == null || Zielobjekt == null)
			return;

		final Class<?> klasseQuelle = Quellobjekt.getClass();
		final Class<?> klasseZiel = Zielobjekt.getClass();

		boolean istQuelleSuperklasseVonZiel = (klasseQuelle == klasseZiel);

		if (!istQuelleSuperklasseVonZiel) {
			/*
			Keine direkte Übereinstimmung der Klassen, durchlaufe daher den Baum der Klassen des Zielobjektes,
			ob darin die Klasse des Quellobjektes enthalten ist.
			*/
			Class<?> klassePruefung = klasseZiel;
			while (!istQuelleSuperklasseVonZiel && klassePruefung != null && klassePruefung != Object.class) {
				klassePruefung = klassePruefung.getSuperclass();
				istQuelleSuperklasseVonZiel = (klassePruefung == klasseQuelle);
			}

			// Wenn die Prüfung keinen geeigneten Zusammenhang zwischen den Klassen ergeben hat, breche hier ab.
			if (!istQuelleSuperklasseVonZiel)
				return;
		}

		/*
		Ermittle alle Felder der Klasse des Quellobjektes. Da es sich hierbei um ein
		Objekt der Elternklasse handelt, sind alle diese Felder auch Teil dieser Klasse.
		Im Folgenden existiert damit garantiert zu jedem Feld des übergebenen Objektes ein
		gleichnamiges Feld mit gleichem Typ in dieser Klasse.
		*/
		final Field[] felderQuellobjekt = Quellobjekt.getClass().getDeclaredFields();

		/*
		Filtere nun aus allen Felder die heraus, die den Modifier Public haben und nicht static sind.
		Dies ist bei den CoreTypes die Regel.
		Dadurch können die Werte später sicher zugewiesen, da die Felder der Elternklasse und dieser
		Klasse übereinstimmen.
		*/
		int modifiers;
		final List<Field> editierbareFelder = new ArrayList<>();
		for (final Field feld : felderQuellobjekt) {
			modifiers = feld.getModifiers();
			if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
				editierbareFelder.add(feld);
		}

		/*
		Ab hier werden nur die Werte der Public Felder aus dem übergebenen Objekt der Elternklasse
		ausgelesen und den Feldern dieser davon abgeleiteten KLasse zugewiesen.
		Da die Felder bereits Public sind, kann die Warnung "This accessibility bypass should be removed"
		(Sonar-Warnung java: S3011) ignoriert werden. Ebenso kann daher der Fehler vom Type
		IllegalAccessException nicht auftreten.
		*/
		for (final Field feld : editierbareFelder) {
			try {
				feld.setAccessible(true);
				feld.set(Zielobjekt, feld.get(Quellobjekt));
			} catch (@SuppressWarnings("unused") final IllegalAccessException ignore) {
				// Gemäß obigen Kommentaren sollte hier kein Fehlerfall auftreten.
			}
		}
	}
}
