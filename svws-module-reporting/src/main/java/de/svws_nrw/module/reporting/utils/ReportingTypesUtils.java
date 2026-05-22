package de.svws_nrw.module.reporting.utils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Die Klasse enthält statische Hilfsmethoden rund um die Reporting-Types.
 */
public final class ReportingTypesUtils {

	private ReportingTypesUtils() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zu den Reporting-Types. Initialisierung nicht möglich.");
	}

	/**
	 * Hilfsmethode zur Ermittelung der Methodennamen als String einer übergebenen Methodenreferenz. Wird für die Attributnamen der Reporting-Types
	 * bei der Sortierung oder Filterung verwendet.
	 *
	 * @param methodenreferenz eine Methodenreferenz, z. B. ReportingKlasse::sortierung
	 * @param <E> Typ des Eingabeparameters der Funktion
	 * @param <R> Rückgabetyp der Funktion
	 *
	 * @return der Name der implementierenden Methode (z. B. "sortierung"). Bei einem Fehler wird ein leerer String zurückgegeben.
	 */
	@SuppressWarnings("java:S3011") // Betrifft Aufruf von writeReplace zur Extraktion des Methodennamens, siehe dazu Kommentierung im Code unten.
	public static <E, R> String methodeToString(final SerializableFunction<E, R> methodenreferenz) {
		try {
			final Method writeReplace = methodenreferenz.getClass().getDeclaredMethod("writeReplace");
			// Anmerkung zum Aufruf von writeReplace und der suppressed warning S3011:
			// writeReplace ist eine vom Compiler synthetisch erzeugte, package-private Methode der Lambda-Klasse.
			// Sie ist nicht Teil der öffentlichen API und daher ohne setAccessible(true) nicht aufrufbar.
			// Es gibt keine alternative, reflection-freie API in Java SE, um über eine serialisierbare
			// Methodenreferenz zur Laufzeit an das SerializedLambda-Objekt und damit an den Methodennamen zu gelangen.
			// Der Zugriff ist auf diese einzelne klar definierte Methode beschränkt und birgt kein Sicherheitsrisiko.
			writeReplace.setAccessible(true);
			final Object serializedForm = writeReplace.invoke(methodenreferenz);
			if (serializedForm instanceof final SerializedLambda lambda) {
				return lambda.getImplMethodName();
			}
			return "";
		} catch (final ReflectiveOperationException e) {
			return "";
		}
	}

	/**
	 * Functional Interface für serialisierbare Methodenreferenzen, damit der Methodenname über eine SerializedLambda
	 * ermittelt werden kann. Wird sowohl von der Sortierung als auch der Filterung verwendet.
	 *
	 * @param <E> Typ des Eingabeparameters der Funktion
	 * @param <R> Rückgabetyp der Funktion
	 */
	@FunctionalInterface
	public interface SerializableFunction<E, R> extends Function<E, R>, Serializable {
	}

	/**
	 * Die Methode ist primär für die von CoreTypes abgeleiteten ReportingTypes gedacht, da die CoreTypes keine entsprechenden Konstruktoren
	 * enthalten. Daher werden hier die Felder und ihre Inhalte mittels Reflection von einem Quellobjekt auf ein Zielobjekt übertragen,
	 * aber nur, wenn die Klasse des Quellobjektes eine Superklasse des Zielobjektes ist. Andernfalls findet keine Übertragung statt.
	 *
	 * @param quellobjekt	quellobjekt der Übertragung. Die Klasse dieses Objektes muss eine Superklasse der Klasse des Zielobjektes sein.
	 * @param zielobjekt	Zielobjekt der Übertragung.
	 */
	public static void setFelderAusSuperklassenObjekt(final Object quellobjekt, final Object zielobjekt) {
		/*
		Die folgende Übertragung der Felder ist nur dann sicher möglich, wenn das quellobjekt auf eine
		übergeordnete Klasse des Zielobjektes ist. Prüfe dies zunächst. Andernfalls wird die Methode abgebrochen.
		*/
		if ((quellobjekt == null) || (zielobjekt == null)) {
			return;
		}

		final Class<?> klasseQuelle = quellobjekt.getClass();
		final Class<?> klasseZiel = zielobjekt.getClass();

		boolean istQuelleSuperklasseVonZiel = (klasseQuelle == klasseZiel);

		if (!istQuelleSuperklasseVonZiel) {
			/*
			Keine direkte Übereinstimmung der Klassen, durchlaufe daher den Baum der Klassen des Zielobjektes,
			ob darin die Klasse des Quellobjektes enthalten ist.
			*/
			Class<?> klassePruefung = klasseZiel;
			while (!istQuelleSuperklasseVonZiel && (klassePruefung != null) && (klassePruefung != Object.class)) {
				klassePruefung = klassePruefung.getSuperclass();
				istQuelleSuperklasseVonZiel = (klassePruefung == klasseQuelle);
			}

			// Wenn die Prüfung keinen geeigneten Zusammenhang zwischen den Klassen ergeben hat, breche hier ab.
			if (!istQuelleSuperklasseVonZiel) {
				return;
			}
		}

		/*
		Ermittle alle Felder der Klasse des Quellobjektes. Da es sich hierbei um ein
		Objekt der Elternklasse handelt, sind alle diese Felder auch Teil dieser Klasse.
		Im Folgenden existiert damit garantiert zu jedem Feld des übergebenen Objektes ein
		gleichnamiges Feld mit gleichem Typ in dieser Klasse.
		*/
		final Field[] felderQuellobjekt = quellobjekt.getClass().getDeclaredFields();

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
			if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
				editierbareFelder.add(feld);
			}
		}

		/*
		Ab hier werden nur die Werte der Public Felder aus dem übergebenen Objekt der Elternklasse
		ausgelesen und den Feldern dieser davon abgeleiteten KLasse zugewiesen.
		Da die Felder bereits Public sind, kann die Warnung "This accessibility bypass should be removed"
		(Sonar-Warnung java: S3011) ignoriert werden. Ebenso kann daher der Fehler vom Type
		IllegalAccessException nicht auftreten.
		*/
		setzeFelder(quellobjekt, zielobjekt, editierbareFelder);
	}

	/**
	 * Setzt die Werte der Public Felder aus dem übergebenen Objekt der Elternklasse auf die Felder dieser davon abgeleiteten Klasse.
	 *
	 * @param quellobjekt das Objekt der Elternklasse
	 * @param zielobjekt das Objekt der abgeleiteten Klasse
	 * @param editierbareFelder die Liste der editierbaren Felder
	 */
	@SuppressWarnings("squid:S3011") // Betrifft Aufruf von setAccessible, siehe dazu Kommentierung im Code unten.
	private static void setzeFelder(final Object quellobjekt, final Object zielobjekt, final List<Field> editierbareFelder) {
		for (final Field feld : editierbareFelder) {
			// Ab hier werden nur die Werte der Public Felder aus dem übergebenen Objekt der Elternklasse
			// ausgelesen und den Feldern dieser davon abgeleiteten KLasse zugewiesen.
			// Da die Felder bereits Public sind, kann die Warnung "This accessibility bypass should be removed"
			// (Sonar-Warnung java: S3011) ignoriert werden.
			// Ebenso kann daher der Fehler vom Type IllegalAccessException nicht auftreten.
			try {
				feld.setAccessible(true);
				feld.set(zielobjekt, feld.get(quellobjekt));
			} catch (@SuppressWarnings("unused") final IllegalAccessException ignore) {
				// Gemäß obigen Kommentaren sollte hier kein Fehlerfall auftreten.
			}
		}
	}
}
