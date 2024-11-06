package de.svws_nrw.module.reporting.types.schueler.sprachen;

import de.svws_nrw.asd.types.fach.Sprachreferenzniveau;
import de.svws_nrw.module.reporting.types.fach.ReportingStatistikFach;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKursbelegung.
 */
public class ReportingSchuelerSprachbelegung {

	/** Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde */
	protected String belegungBisJahrgang;

	/** Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde */
	protected Integer belegungBisAbschnitt;

	/** Der Jahrgang, in dem die Sprache zum ersten Mal belegt wurde */
	protected String belegungVonJahrgang;

	/** Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten Mal belegt wurde */
	protected Integer belegungVonAbschnitt;

	/** Gibt an, ob das kleine Latinum erreicht wurde oder nicht. */
	protected boolean hatKleinesLatinum;

	/** Gibt an, ob das Latinum erreicht wurde oder nicht. */
	protected boolean hatLatinum;

	/** Gibt an, ob das Graecum erreicht wurde oder nicht. */
	protected boolean hatGraecum;

	/** Gibt an, ob das Hebraicum erreicht wurde oder nicht. */
	protected boolean hatHebraicum;

	/** Das Referenzniveau, welches bisher erreicht wurde */
	protected Sprachreferenzniveau referenzniveau;

	/** Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde */
	protected Integer reihenfolge;

	/** Das einstellige Sprachkürzel des Faches der Sprache */
	protected String sprache;

	/** Das Statistik-Fach zur Sprache */
	protected ReportingStatistikFach statistikfach;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param belegungBisJahrgang Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde
	 * @param belegungBisAbschnitt Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde
	 * @param belegungVonJahrgang Der Jahrgang, in dem die Sprache zum ersten Mal belegt wurde
	 * @param belegungVonAbschnitt Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten Mal belegt wurde
	 * @param hatKleinesLatinum Gibt an, ob das kleine Latinum erreicht wurde oder nicht.
	 * @param hatLatinum Gibt an, ob das Latinum erreicht wurde oder nicht.
	 * @param hatGraecum Gibt an, ob das Graecum erreicht wurde oder nicht.
	 * @param hatHebraicum Gibt an, ob das Hebraicum erreicht wurde oder nicht.
	 * @param referenzniveau Das Referenzniveau, welches bisher erreicht wurde
	 * @param reihenfolge Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde
	 * @param sprache Das einstellige Sprachkürzel des Faches der Sprache
	 * @param statistikfach Das Statistik-Fach zur Sprache
	 */
	public ReportingSchuelerSprachbelegung(final Integer belegungBisAbschnitt, final String belegungBisJahrgang, final Integer belegungVonAbschnitt,
			final String belegungVonJahrgang, final boolean hatGraecum, final boolean hatHebraicum, final boolean hatKleinesLatinum, final boolean hatLatinum,
			final Sprachreferenzniveau referenzniveau, final Integer reihenfolge, final String sprache, final ReportingStatistikFach statistikfach) {
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



	// ##### Getter #####

	/**
	 * Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde
	 *
	 * @return Inhalt des Feldes belegungBisJahrgang
	 */
	public String belegungBisJahrgang() {
		return belegungBisJahrgang;
	}

	/**
	 * Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde
	 *
	 * @return Inhalt des Feldes belegungBisAbschnitt
	 */
	public Integer belegungBisAbschnitt() {
		return belegungBisAbschnitt;
	}

	/**
	 * Der Jahrgang, in dem die Sprache zum ersten Mal belegt wurde
	 *
	 * @return Inhalt des Feldes belegungVonJahrgang
	 */
	public String belegungVonJahrgang() {
		return belegungVonJahrgang;
	}

	/**
	 * Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten Mal belegt wurde
	 *
	 * @return Inhalt des Feldes belegungVonAbschnitt
	 */
	public Integer belegungVonAbschnitt() {
		return belegungVonAbschnitt;
	}

	/**
	 * Gibt an, ob das kleine Latinum erreicht wurde oder nicht.
	 *
	 * @return Inhalt des Feldes hatKleinesLatinum
	 */
	public boolean hatKleinesLatinum() {
		return hatKleinesLatinum;
	}

	/**
	 * Gibt an, ob das Latinum erreicht wurde oder nicht.
	 *
	 * @return Inhalt des Feldes hatLatinum
	 */
	public boolean hatLatinum() {
		return hatLatinum;
	}

	/**
	 * Gibt an, ob das Graecum erreicht wurde oder nicht.
	 *
	 * @return Inhalt des Feldes hatGraecum
	 */
	public boolean hatGraecum() {
		return hatGraecum;
	}

	/**
	 * Gibt an, ob das Hebraicum erreicht wurde oder nicht.
	 *
	 * @return Inhalt des Feldes hatHebraicum
	 */
	public boolean hatHebraicum() {
		return hatHebraicum;
	}

	/**
	 * Das Referenzniveau, welches bisher erreicht wurde
	 *
	 * @return Inhalt des Feldes referenzniveau
	 */
	public Sprachreferenzniveau referenzniveau() {
		return referenzniveau;
	}

	/**
	 * Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde
	 *
	 * @return Inhalt des Feldes reihenfolge
	 */
	public Integer reihenfolge() {
		return reihenfolge;
	}

	/**
	 * Das einstellige Sprachkürzel des Faches der Sprache
	 *
	 * @return Inhalt des Feldes sprache
	 */
	public String sprache() {
		return sprache;
	}

	/**
	 * Das Statistik-Fach zur Sprache
	 *
	 * @return Inhalt des Feldes fach
	 */
	public ReportingStatistikFach statistikfach() {
		return statistikfach;
	}

}
