package de.svws_nrw.module.reporting.html.contexts.initializer;

/**
 * Die Schlüssel, unter denen die Daten-Contexts eines Reports in der Context-Map der HTML-Erzeugung abgelegt werden.
 * <p>Die Schlüssel sind <b>nicht</b> die Namen der Thymeleaf-Variablen: Der Renderer führt nur die Thymeleaf-Contexts der Werte zusammen, der Schlüssel selbst
 * erreicht keine Vorlage. Benutzt wird er allein beim Nachschlagen und Ersetzen des Haupt-Contexts für die Einzelausgabe. Wo Schlüssel und Variablenname
 * voneinander abweichen (z. B. großes "S" in "FachwahlStatistiken"), ist das Bestand und wird nicht angeglichen.</p>
 */
public final class HtmlContextSchluessel {

	private HtmlContextSchluessel() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}

	/** Schlüssel des Basisdaten-Kontextes, der bei jedem Report mitgeliefert wird. */
	public static final String BASISDATEN = "Basisdaten";

	/** Schlüssel des Schüler-Kontextes. */
	public static final String SCHUELER = "Schueler";

	/** Schlüssel des Klassen-Kontextes. */
	public static final String KLASSEN = "Klassen";

	/** Schlüssel des Kurse-Kontextes. */
	public static final String KURSE = "Kurse";

	/** Schlüssel des Lehrer-Kontextes. */
	public static final String LEHRER = "Lehrer";

	/** Schlüssel des Kontextes mit den Fachwahlstatistiken der GOSt-Laufbahnplanung eines Abiturjahrgangs. */
	public static final String GOST_LAUFBAHNPLANUNG_FACHWAHLSTATISTIKEN = "GostLaufbahnplanungAbiturjahrgangFachwahlStatistiken";

	/** Schlüssel des Kontextes mit dem Blockungsergebnis der GOSt-Kursplanung. */
	public static final String GOST_BLOCKUNGSERGEBNIS = "GostBlockungsergebnis";

	/** Schlüssel des Kontextes mit dem Klausurplan der GOSt-Klausurplanung. */
	public static final String GOST_KLAUSURPLAN = "GostKlausurplan";

	/** Schlüssel des Kontextes mit den Stundenplänen der Fächer. */
	public static final String STUNDENPLANUNG_FAECHER = "FaecherStundenplaene";

	/** Schlüssel des Kontextes mit den Stundenplänen der Klassen. */
	public static final String STUNDENPLANUNG_KLASSEN = "KlassenStundenplaene";

	/** Schlüssel des Kontextes mit den Stundenplänen der Lehrkräfte. */
	public static final String STUNDENPLANUNG_LEHRER = "LehrerStundenplaene";

	/** Schlüssel des Kontextes mit den Stundenplänen der Räume. */
	public static final String STUNDENPLANUNG_RAEUME = "RaeumeStundenplaene";

	/** Schlüssel des Kontextes mit den Stundenplänen der Schüler. */
	public static final String STUNDENPLANUNG_SCHUELER = "SchuelerStundenplaene";

}
