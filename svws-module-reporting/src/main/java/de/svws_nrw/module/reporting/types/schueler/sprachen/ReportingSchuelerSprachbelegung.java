package de.svws_nrw.module.reporting.types.schueler.sprachen;

import de.svws_nrw.core.types.fach.Sprachreferenzniveau;
import de.svws_nrw.module.reporting.types.fach.ReportingStatistikFach;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKursbelegung.</p>
 * <p>Sie enthält die Daten zur Belegung eines Kurses durch einen Schüler im Rahmen der Kursplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 *  einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingSchuelerSprachbelegung {

	/** Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde */
	private String belegungBisJahrgang;

	/** Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde */
	private Integer belegungBisAbschnitt;

	/** Der Jahrgang, in dem die Sprache zum ersten mal belegt wurde */
	private String belegungVonJahrgang;

	/** Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten mal belegt wurde */
	private Integer belegungVonAbschnitt;

	/** Gibt an, ob das kleine Latinum erreicht wurde oder nicht. */
	private boolean hatKleinesLatinum;

	/** Gibt an, ob das Latinum erreicht wurde oder nicht. */
	private boolean hatLatinum;

	/** Gibt an, ob das Graecum erreicht wurde oder nicht. */
	private boolean hatGraecum;

	/** Gibt an, ob das Hebraicum erreicht wurde oder nicht. */
	private boolean hatHebraicum;

	/** Das Referenzniveau, welches bisher erreicht wurde */
	private Sprachreferenzniveau referenzniveau;

	/** Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde */
	private Integer reihenfolge;

	/** Das einstellige Sprachkürzel des Faches der Sprache */
	private String sprache;

	/** Das Statistik-Fach zur Sprache */
	private ReportingStatistikFach statistikfach;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param belegungBisJahrgang Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde
	 * @param belegungBisAbschnitt Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde
	 * @param belegungVonJahrgang Der Jahrgang, in dem die Sprache zum ersten mal belegt wurde
	 * @param belegungVonAbschnitt Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten mal belegt wurde
	 * @param hatKleinesLatinum Gibt an, ob das kleine Latinum erreicht wurde oder nicht.
	 * @param hatLatinum Gibt an, ob das Latinum erreicht wurde oder nicht.
	 * @param hatGraecum Gibt an, ob das Graecum erreicht wurde oder nicht.
	 * @param hatHebraicum Gibt an, ob das Hebraicum erreicht wurde oder nicht.
	 * @param referenzniveau Das Referenzniveau, welches bisher erreicht wurde
	 * @param reihenfolge Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde
	 * @param sprache Das einstellige Sprachkürzel des Faches der Sprache
	 * @param statistikfach Das Statistik-Fach zur Sprache
	 */
	public ReportingSchuelerSprachbelegung(final Integer belegungBisAbschnitt, final String belegungBisJahrgang, final Integer belegungVonAbschnitt, final String belegungVonJahrgang, final boolean hatGraecum, final boolean hatHebraicum, final boolean hatKleinesLatinum, final boolean hatLatinum, final Sprachreferenzniveau referenzniveau, final Integer reihenfolge, final String sprache, final ReportingStatistikFach statistikfach) {
		this.belegungBisAbschnitt = belegungBisAbschnitt;
		this.belegungBisJahrgang = belegungBisJahrgang;
		this.belegungVonAbschnitt = belegungVonAbschnitt;
		this.belegungVonJahrgang = belegungVonJahrgang;
		this.hatGraecum = hatGraecum;
		this.hatHebraicum = hatHebraicum;
		this.hatKleinesLatinum = hatKleinesLatinum;
		this.hatLatinum = hatLatinum;
		this.referenzniveau = referenzniveau;
		this.reihenfolge = reihenfolge;
		this.sprache = sprache;
		this.statistikfach = statistikfach;
	}



	// ##### Getter und Setter #####

	/**
	 * Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde
	 * @return Inhalt des Feldes belegungBisJahrgang
	 */
	public  String belegungBisJahrgang() {
		return belegungBisJahrgang;
	}

	/**
	 * Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde, wird gesetzt
	 * @param belegungBisJahrgang Neuer Wert für das Feld belegungBisJahrgang wird gesetzt.
	 */
	public void setBelegungBisJahrgang(final  String belegungBisJahrgang) {
		this.belegungBisJahrgang = belegungBisJahrgang;
	}

	/**
	 * Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde
	 * @return Inhalt des Feldes belegungBisAbschnitt
	 */
	public  Integer belegungBisAbschnitt() {
		return belegungBisAbschnitt;
	}

	/**
	 * Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde, wird gesetzt.
	 * @param belegungBisAbschnitt Neuer Wert für das Feld belegungBisAbschnitt
	 */
	public void setBelegungBisAbschnitt(final  Integer belegungBisAbschnitt) {
		this.belegungBisAbschnitt = belegungBisAbschnitt;
	}

	/**
	 * Der Jahrgang, in dem die Sprache zum ersten mal belegt wurde
	 * @return Inhalt des Feldes belegungVonJahrgang
	 */
	public  String belegungVonJahrgang() {
		return belegungVonJahrgang;
	}

	/**
	 * Der Jahrgang, in dem die Sprache zum ersten mal belegt wurde wird gesetzt.
	 * @param belegungVonJahrgang Neuer Wert für das Feld belegungVonJahrgang
	 */
	public void setBelegungVonJahrgang(final  String belegungVonJahrgang) {
		this.belegungVonJahrgang = belegungVonJahrgang;
	}

	/**
	 * Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten mal belegt wurde
	 * @return Inhalt des Feldes belegungVonAbschnitt
	 */
	public  Integer belegungVonAbschnitt() {
		return belegungVonAbschnitt;
	}

	/**
	 * Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten mal belegt wurde wird gesetzt.
	 * @param belegungVonAbschnitt Neuer Wert für das Feld belegungVonAbschnitt
	 */
	public void setBelegungVonAbschnitt(final  Integer belegungVonAbschnitt) {
		this.belegungVonAbschnitt = belegungVonAbschnitt;
	}

	/**
	 * Gibt an, ob das kleine Latinum erreicht wurde oder nicht.
	 * @return Inhalt des Feldes hatKleinesLatinum
	 */
	public  boolean hatKleinesLatinum() {
		return hatKleinesLatinum;
	}

	/**
	 * Gibt an, ob das kleine Latinum erreicht wurde oder nicht wird gesetzt.
	 * @param hatKleinesLatinum Neuer Wert für das Feld hatKleinesLatinum
	 */
	public void setHatKleinesLatinum(final  boolean hatKleinesLatinum) {
		this.hatKleinesLatinum = hatKleinesLatinum;
	}

	/**
	 * Gibt an, ob das Latinum erreicht wurde oder nicht.
	 * @return Inhalt des Feldes hatLatinum
	 */
	public  boolean hatLatinum() {
		return hatLatinum;
	}

	/**
	 * Gibt an, ob das Latinum erreicht wurde oder nicht wird gesetzt.
	 * @param hatLatinum Neuer Wert für das Feld hatLatinum
	 */
	public void setHatLatinum(final  boolean hatLatinum) {
		this.hatLatinum = hatLatinum;
	}

	/**
	 * Gibt an, ob das Graecum erreicht wurde oder nicht.
	 * @return Inhalt des Feldes hatGraecum
	 */
	public  boolean hatGraecum() {
		return hatGraecum;
	}

	/**
	 * Gibt an, ob das Graecum erreicht wurde oder nicht wird gesetzt.
	 * @param hatGraecum Neuer Wert für das Feld hatGraecum
	 */
	public void setHatGraecum(final  boolean hatGraecum) {
		this.hatGraecum = hatGraecum;
	}

	/**
	 * Gibt an, ob das Hebraicum erreicht wurde oder nicht.
	 * @return Inhalt des Feldes hatHebraicum
	 */
	public  boolean hatHebraicum() {
		return hatHebraicum;
	}

	/**
	 * Gibt an, ob das Hebraicum erreicht wurde oder nicht wird gesetzt.
	 * @param hatHebraicum Neuer Wert für das Feld hatHebraicum
	 */
	public void setHatHebraicum(final  boolean hatHebraicum) {
		this.hatHebraicum = hatHebraicum;
	}

	/**
	 * Das Referenzniveau, welches bisher erreicht wurde
	 * @return Inhalt des Feldes referenzniveau
	 */
	public Sprachreferenzniveau referenzniveau() {
		return referenzniveau;
	}

	/**
	 * Das Referenzniveau, welches bisher erreicht wurde, wird gesetzt.
	 * @param referenzniveau Neuer Wert für das Feld referenzniveau
	 */
	public void setReferenzniveau(final Sprachreferenzniveau referenzniveau) {
		this.referenzniveau = referenzniveau;
	}

	/**
	 * Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde
	 * @return Inhalt des Feldes reihenfolge
	 */
	public Integer reihenfolge() {
		return reihenfolge;
	}

	/**
	 * Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde, wird gesetzt.
	 * @param reihenfolge Neuer Wert für das Feld reihenfolge
	 */
	public void setReihenfolge(final Integer reihenfolge) {
		this.reihenfolge = reihenfolge;
	}

	/**
	 * Das einstellige Sprachkürzel des Faches der Sprache
	 * @return Inhalt des Feldes sprache
	 */
	public String sprache() {
		return sprache;
	}

	/**
	 * Das einstellige Sprachkürzel des Faches der Sprache wird gesetzt.
	 * @param sprache Neuer Wert für das Feld sprache
	 */
	public void setSprache(final String sprache) {
		this.sprache = sprache;
	}

	/**
	 * Das Statistik-Fach zur Sprache
	 * @return Inhalt des Feldes fach
	 */
	public ReportingStatistikFach statistikfach() {
		return statistikfach;
	}

	/**
	 * Das Statistik-Fach zur Sprache wird gesetzt.
	 * @param statistikfach Neuer Wert für das Feld fach
	 */
	public void setStatistikfach(final ReportingStatistikFach statistikfach) {
		this.statistikfach = statistikfach;
	}
}
