package de.svws_nrw.module.reporting.types.gost.laufbahnplanung;

import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostLaufbahnplanungFachwahl.</p>
 * <p>Sie enthält die Daten einer Fachwahl für die Laufbahnplanung der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostLaufbahnplanungFachwahl {

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



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
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
	 */
	public ReportingGostLaufbahnplanungFachwahl(final String abiturfach, final String belegungEF1, final String belegungEF2, final String belegungQ11,
			final String belegungQ12, final String belegungQ21, final String belegungQ22, final ReportingFach fach, final Boolean fachIstBelegtInGOSt,
			final Boolean fachIstFortfuehrbareFremdspracheInGOSt, final String jahrgangFremdsprachenbeginn, final String positionFremdsprachenfolge) {
		this.abiturfach = abiturfach;
		this.belegungEF1 = belegungEF1;
		this.belegungEF2 = belegungEF2;
		this.belegungQ11 = belegungQ11;
		this.belegungQ12 = belegungQ12;
		this.belegungQ21 = belegungQ21;
		this.belegungQ22 = belegungQ22;
		this.fach = fach;
		this.fachIstBelegtInGOSt = fachIstBelegtInGOSt;
		this.fachIstFortfuehrbareFremdspracheInGOSt = fachIstFortfuehrbareFremdspracheInGOSt;
		this.jahrgangFremdsprachenbeginn = jahrgangFremdsprachenbeginn;
		this.positionFremdsprachenfolge = positionFremdsprachenfolge;
	}



	// ###### Getter ######

	/**
	 * Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde
	 * @return Inhalt des Feldes abiturfach
	 */
	public String abiturfach() {
		return abiturfach;
	}

	/**
	 * Fachbelegung in der EF.1
	 * @return Inhalt des Feldes belegungEF1
	 */
	public String belegungEF1() {
		return belegungEF1;
	}

	/**
	 * Fachbelegung in der EF.2
	 * @return Inhalt des Feldes belegungEF2
	 */
	public String belegungEF2() {
		return belegungEF2;
	}

	/**
	 * Fachbelegung in der Q1.1
	 * @return Inhalt des Feldes belegungQ11
	 */
	public String belegungQ11() {
		return belegungQ11;
	}

	/**
	 * Fachbelegung in der Q1.2
	 * @return Inhalt des Feldes belegungQ12
	 */
	public String belegungQ12() {
		return belegungQ12;
	}

	/**
	 * Fachbelegung in der Q2.1
	 * @return Inhalt des Feldes belegungQ21
	 */
	public String belegungQ21() {
		return belegungQ21;
	}

	/**
	 * Fachbelegung in der Q2.2
	 * @return Inhalt des Feldes belegungQ22
	 */
	public String belegungQ22() {
		return belegungQ22;
	}

	/**
	 * Das Fach der Fachwahl
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Fach ist in mindestens einem Halbjahr der GOSt belegt.
	 * @return Inhalt des Feldes fachIstBelegtInGOSt
	 */
	public Boolean fachIstBelegtInGOSt() {
		return fachIstBelegtInGOSt;
	}

	/**
	 * Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache
	 * @return Inhalt des Feldes fachIstFortfuehrbareFremdspracheInGOSt
	 */
	public Boolean fachIstFortfuehrbareFremdspracheInGOSt() {
		return fachIstFortfuehrbareFremdspracheInGOSt;
	}

	/**
	 * Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung
	 * @return Inhalt des Feldes jahrgangFremdsprachenbeginn
	 */
	public String jahrgangFremdsprachenbeginn() {
		return jahrgangFremdsprachenbeginn;
	}

	/**
	 * Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk
	 * @return Inhalt des Feldes positionFremdsprachenfolge
	 */
	public String positionFremdsprachenfolge() {
		return positionFremdsprachenfolge;
	}

}
