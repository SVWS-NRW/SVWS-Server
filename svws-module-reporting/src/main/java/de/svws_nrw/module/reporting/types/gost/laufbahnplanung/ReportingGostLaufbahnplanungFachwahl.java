package de.svws_nrw.module.reporting.types.gost.laufbahnplanung;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostLaufbahnplanungFachwahl.
 */
public class ReportingGostLaufbahnplanungFachwahl extends ReportingBaseType {

	/** Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde */
	protected String abiturfach;

	/** Fachbelegung in der EF.1 */
	protected String belegungEF1;

	/** Fachbelegung in der EF.2 */
	protected String belegungEF2;

	/** Fachbelegung in der Q1.1 */
	protected String belegungQ11;

	/** Fachbelegung in der Q1.2 */
	protected String belegungQ12;

	/** Fachbelegung in der Q2.1 */
	protected String belegungQ21;

	/** Fachbelegung in der Q2.2 */
	protected String belegungQ22;

	/** Das Fach der Fachwahl */
	protected ReportingFach fach;

	/** Fach ist in mindestens einem Halbjahr der GOSt belegt. */
	protected Boolean fachIstBelegtInGOSt;

	/** Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache */
	protected Boolean fachIstFortfuehrbareFremdspracheInGOSt;

	/** Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung */
	protected String jahrgangFremdsprachenbeginn;

	/** Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk */
	protected String positionFremdsprachenfolge;

	/** Das Referenzfach eines Projektkurses (nur für Abiturjahrgänge ab 2030 relevant). */
	protected ReportingFach referenzfach;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abiturfach								Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde.
	 * @param belegungEF1								Fachbelegung in der EF.1
	 * @param belegungEF2								Fachbelegung in der EF.2
	 * @param belegungQ11								Fachbelegung in der Q1.1
	 * @param belegungQ12								Fachbelegung in der Q1.2
	 * @param belegungQ21								Fachbelegung in der Q2.1
	 * @param belegungQ22								Fachbelegung in der Q2.2
	 * @param fach 										Das Fach der Fachwahl.
	 * @param fachIstBelegtInGOSt						Fach ist in mindestens einem Halbjahr der GOSt belegt.
	 * @param fachIstFortfuehrbareFremdspracheInGOSt	Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache.
	 * @param jahrgangFremdsprachenbeginn				Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung.
	 * @param positionFremdsprachenfolge				Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk.
	 * @param referenzfach								Das Referenzfach eines Projektkurses (nur für Abiturjahrgänge ab 2030 relevant).
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingGostLaufbahnplanungFachwahl(final String abiturfach, final String belegungEF1, final String belegungEF2, final String belegungQ11,
			final String belegungQ12, final String belegungQ21, final String belegungQ22, final ReportingFach fach, final Boolean fachIstBelegtInGOSt,
			final Boolean fachIstFortfuehrbareFremdspracheInGOSt, final String jahrgangFremdsprachenbeginn, final String positionFremdsprachenfolge,
			final ReportingFach referenzfach) {
		this.abiturfach = ersetzeNullBlankTrim(abiturfach);
		this.belegungEF1 = ersetzeNullBlankTrim(belegungEF1);
		this.belegungEF2 = ersetzeNullBlankTrim(belegungEF2);
		this.belegungQ11 = ersetzeNullBlankTrim(belegungQ11);
		this.belegungQ12 = ersetzeNullBlankTrim(belegungQ12);
		this.belegungQ21 = ersetzeNullBlankTrim(belegungQ21);
		this.belegungQ22 = ersetzeNullBlankTrim(belegungQ22);
		this.fach = fach;
		this.fachIstBelegtInGOSt = fachIstBelegtInGOSt;
		this.fachIstFortfuehrbareFremdspracheInGOSt = fachIstFortfuehrbareFremdspracheInGOSt;
		this.jahrgangFremdsprachenbeginn = ersetzeNullBlankTrim(jahrgangFremdsprachenbeginn);
		this.positionFremdsprachenfolge = ersetzeNullBlankTrim(positionFremdsprachenfolge);
		this.referenzfach = referenzfach;
	}



	// ###### Getter ######

	/**
	 * Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde
	 *
	 * @return Inhalt des Feldes abiturfach; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String abiturfach() {
		return abiturfach;
	}

	/**
	 * Fachbelegung in der EF.1
	 *
	 * @return Inhalt des Feldes belegungEF1; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String belegungEF1() {
		return belegungEF1;
	}

	/**
	 * Fachbelegung in der EF.2
	 *
	 * @return Inhalt des Feldes belegungEF2; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String belegungEF2() {
		return belegungEF2;
	}

	/**
	 * Fachbelegung in der Q1.1
	 *
	 * @return Inhalt des Feldes belegungQ11; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String belegungQ11() {
		return belegungQ11;
	}

	/**
	 * Fachbelegung in der Q1.2
	 *
	 * @return Inhalt des Feldes belegungQ12; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String belegungQ12() {
		return belegungQ12;
	}

	/**
	 * Fachbelegung in der Q2.1
	 *
	 * @return Inhalt des Feldes belegungQ21; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String belegungQ21() {
		return belegungQ21;
	}

	/**
	 * Fachbelegung in der Q2.2
	 *
	 * @return Inhalt des Feldes belegungQ22; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String belegungQ22() {
		return belegungQ22;
	}

	/**
	 * Das Fach der Fachwahl
	 *
	 * @return Inhalt des Feldes fach; kann {@code null} sein, wenn kein Fach zugeordnet ist.
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Fach ist in mindestens einem Halbjahr der GOSt belegt.
	 *
	 * @return Inhalt des Feldes fachIstBelegtInGOSt
	 */
	public Boolean fachIstBelegtInGOSt() {
		return fachIstBelegtInGOSt;
	}

	/**
	 * Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache
	 *
	 * @return Inhalt des Feldes fachIstFortfuehrbareFremdspracheInGOSt
	 */
	public Boolean fachIstFortfuehrbareFremdspracheInGOSt() {
		return fachIstFortfuehrbareFremdspracheInGOSt;
	}

	/**
	 * Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung
	 *
	 * @return Inhalt des Feldes jahrgangFremdsprachenbeginn; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String jahrgangFremdsprachenbeginn() {
		return jahrgangFremdsprachenbeginn;
	}

	/**
	 * Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk
	 *
	 * @return Inhalt des Feldes positionFremdsprachenfolge; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String positionFremdsprachenfolge() {
		return positionFremdsprachenfolge;
	}

	/**
	 * Das Referenzfach eines Projektkurses (nur für Abiturjahrgänge ab 2030 relevant).
	 *
	 * @return Inhalt des Feldes referenzfach; kann {@code null} sein, wenn kein Referenzfach zugeordnet ist.
	 */
	public ReportingFach referenzfach() {
		return referenzfach;
	}

}
