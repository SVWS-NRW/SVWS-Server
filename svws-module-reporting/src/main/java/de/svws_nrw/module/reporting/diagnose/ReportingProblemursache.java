package de.svws_nrw.module.reporting.diagnose;

import java.net.ConnectException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTransientConnectionException;

/**
 * Die fachliche Ursache eines Ausgabeproblems - die einzige Ursachen-Aufzählung des Moduls. Sie bleibt intern und darf feiner sein als die öffentlichen
 * Kategorien, die daraus abgeleitet werden. Ein vom Benutzerfilter ausgeschlossener Datensatz gehört nicht hierher: Das ist eine Auswahlentscheidung.
 * Aus dem Fehler eines Zugriffs bestimmt {@link #fuerLadefehler} die Ursache - als einzige Stelle des Moduls, damit keine Meldestelle selbst rät.
 */
public enum ReportingProblemursache {

	/** Der Datensatz existiert nicht. Eine leere Liste gehört nicht hierher: Sie ist ein regulär geladener Wert. */
	NICHT_VORHANDEN,

	/** Das Laden des einzelnen Datensatzes ist gescheitert, während der Server arbeitsfähig bleibt. */
	DATENSATZBEZOGENER_LADEFEHLER,

	/** Der Server ist nicht arbeitsfähig - etwa bei abgerissener Datenbankverbindung. Diese Ursache bricht die Ausgabe ab, unabhängig davon, wie viele
	 * Datensätze betroffen sind. */
	INFRASTRUKTURSTOERUNG,

	/** Der Wert ist vorhanden, lässt sich aber nicht ausgeben - etwa ein Barcode-Inhalt mit Zeichen, die der Zeichensatz des Codes nicht kennt. */
	NICHT_DARSTELLBAR,

	/** Ein fachlich optionaler Wert ist nicht gesetzt. */
	OPTIONALER_WERT_FEHLT;


	/** So viele Glieder der Ursachenkette werden höchstens geprüft. Eine im Kreis verkettete Ursache liefe sonst endlos. */
	private static final int MAX_GLIEDER_URSACHENKETTE = 20;


	/**
	 * Bestimmt aus dem Fehler eines gescheiterten Datenzugriffs dessen Ursache. Ein Verbindungsabbruch in der Ursachenkette bricht die Ausgabe ab, alles
	 * Übrige gilt als datensatzbezogen und wird hingenommen - ein unsicherer Fall lässt nicht die ganze Ausgabe scheitern.
	 *
	 * @param fehler Der Fehler des Zugriffs oder {@code null}, wenn der Zugriff ohne Ausnahme erfolglos blieb.
	 *
	 * @return {@link #INFRASTRUKTURSTOERUNG} bei einem Verbindungsfehler, sonst {@link #DATENSATZBEZOGENER_LADEFEHLER}.
	 */
	public static ReportingProblemursache fuerLadefehler(final Throwable fehler) {
		Throwable glied = fehler;
		for (int i = 0; (glied != null) && (i < MAX_GLIEDER_URSACHENKETTE); i++) {
			if (istVerbindungsfehler(glied)) {
				return INFRASTRUKTURSTOERUNG;
			}
			glied = glied.getCause();
		}
		return DATENSATZBEZOGENER_LADEFEHLER;
	}

	/**
	 * Gibt an, ob die übergebene Ausnahme eine Verbindung bezeichnet, die abgerissen ist oder nicht zustande kam. Die Aufzählung genügt für den Betrieb, weil
	 * der Server auf MariaDB läuft und dessen Treiber jeden Verbindungsfehler auf diese beiden JDBC-Typen abbildet - die übrigen DBMS erscheinen allein in
	 * Sicherungen und Migrationen und nie unter einem laufenden Report.
	 *
	 * @param fehler Die zu prüfende Ausnahme.
	 *
	 * @return true, wenn die Ausnahme einen Verbindungsfehler bezeichnet, sonst false.
	 */
	private static boolean istVerbindungsfehler(final Throwable fehler) {
		return (fehler instanceof SQLNonTransientConnectionException) || (fehler instanceof SQLTransientConnectionException)
				|| (fehler instanceof ConnectException);
	}


	/**
	 * Gibt an, ob diese Ursache die Ausgabe abbricht. Die Grenze verläuft entlang der Ursache, nicht der Anzahl: Ein Datenfehler wird auch dann hingenommen,
	 * wenn er alle Datensätze trifft; eine Infrastrukturstörung bricht auch bei einem einzigen ab. Unsichere Fälle gelten als datensatzbezogen.
	 *
	 * @return true, wenn die Ursache zum Abbruch führt, sonst false.
	 */
	public boolean istAbbruch() {
		return this == INFRASTRUKTURSTOERUNG;
	}


	/**
	 * Gibt an, ob diese Ursache einen gescheiterten Datenzugriff bezeichnet. {@link #NICHT_VORHANDEN} zählt nicht dazu: Der Zugriff hat dort einwandfrei
	 * gearbeitet und nichts gefunden.
	 *
	 * @return true, wenn die Ursache einen Fehlschlag bezeichnet, sonst false.
	 */
	public boolean istFehlschlagUrsache() {
		return (this == DATENSATZBEZOGENER_LADEFEHLER) || (this == INFRASTRUKTURSTOERUNG);
	}

}
