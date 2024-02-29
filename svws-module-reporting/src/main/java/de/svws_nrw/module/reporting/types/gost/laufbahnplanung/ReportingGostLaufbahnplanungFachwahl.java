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
	private String abiturfach;

	/** Fachbelegung in der EF.1 */
	private String belegungEF1;

	/** Fachbelegung in der EF.2 */
	private String belegungEF2;

	/** Fachbelegung in der Q1.1 */
	private String belegungQ11;

	/** Fachbelegung in der Q1.2 */
	private String belegungQ12;

	/** Fachbelegung in der Q2.1 */
	private String belegungQ21;

	/** Fachbelegung in der Q2.2 */
	private String belegungQ22;

	/** Das Fach der Fachwahl */
	private ReportingFach fach;

	/** Fach ist in mindestens einem Halbjahr der GOSt belegt. */
	private Boolean fachIstBelegtInGOSt;

	/** Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache */
	private Boolean fachIstFortfuehrbareFremdspracheInGOSt;

	/** Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung */
	private String jahrgangFremdsprachenbeginn;

	/** Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk */
	private String positionFremdsprachenfolge;



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
	public ReportingGostLaufbahnplanungFachwahl(final String abiturfach, final String belegungEF1, final String belegungEF2, final String belegungQ11, final String belegungQ12, final String belegungQ21, final String belegungQ22, final ReportingFach fach, final Boolean fachIstBelegtInGOSt, final Boolean fachIstFortfuehrbareFremdspracheInGOSt, final String jahrgangFremdsprachenbeginn, final String positionFremdsprachenfolge) {
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



	// ###### Getter und Setter ######

	/**
	 * Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde
	 * @return Inhalt des Feldes abiturfach
	 */
	public String abiturfach() {
		return abiturfach;
	}

	/**
	 * Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde, wird gesetzt.
	 * @param abiturfach Neuer Wert für das Feld abiturfach
	 */
	public void setAbiturfach(final String abiturfach) {
		this.abiturfach = abiturfach;
	}

	/**
	 * Fachbelegung in der EF.1
	 * @return Inhalt des Feldes belegungEF1
	 */
	public String belegungEF1() {
		return belegungEF1;
	}

	/**
	 * Fachbelegung in der EF1 wird gesetzt.
	 * @param belegungEF1 Neuer Wert für das Feld belegungEF1
	 */
	public void setBelegungEF1(final String belegungEF1) {
		this.belegungEF1 = belegungEF1;
	}

	/**
	 * Fachbelegung in der EF.2
	 * @return Inhalt des Feldes belegungEF2
	 */
	public String belegungEF2() {
		return belegungEF2;
	}

	/**
	 * Fachbelegung in der EF2 wird gesetzt.
	 * @param belegungEF2 Neuer Wert für das Feld belegungEF2
	 */
	public void setBelegungEF2(final String belegungEF2) {
		this.belegungEF2 = belegungEF2;
	}

	/**
	 * Fachbelegung in der Q1.1
	 * @return Inhalt des Feldes belegungQ11
	 */
	public String belegungQ11() {
		return belegungQ11;
	}

	/**
	 * Fachbelegung in der Q11 wird gesetzt.
	 * @param belegungQ11 Neuer Wert für das Feld belegungQ11
	 */
	public void setBelegungQ11(final String belegungQ11) {
		this.belegungQ11 = belegungQ11;
	}

	/**
	 * Fachbelegung in der Q1.2
	 * @return Inhalt des Feldes belegungQ12
	 */
	public String belegungQ12() {
		return belegungQ12;
	}

	/**
	 * Fachbelegung in der Q12 wird gesetzt.
	 * @param belegungQ12 Neuer Wert für das Feld belegungQ12
	 */
	public void setBelegungQ12(final String belegungQ12) {
		this.belegungQ12 = belegungQ12;
	}

	/**
	 * Fachbelegung in der Q2.1
	 * @return Inhalt des Feldes belegungQ21
	 */
	public String belegungQ21() {
		return belegungQ21;
	}

	/**
	 * Fachbelegung in der Q21 wird gesetzt.
	 * @param belegungQ21 Neuer Wert für das Feld belegungQ21
	 */
	public void setBelegungQ21(final String belegungQ21) {
		this.belegungQ21 = belegungQ21;
	}

	/**
	 * Fachbelegung in der Q2.2
	 * @return Inhalt des Feldes belegungQ22
	 */
	public String belegungQ22() {
		return belegungQ22;
	}

	/**
	 * Fachbelegung in der Q22 wird gesetzt.
	 * @param belegungQ22 Neuer Wert für das Feld belegungQ22
	 */
	public void setBelegungQ22(final String belegungQ22) {
		this.belegungQ22 = belegungQ22;
	}

	/**
	 * Das Fach der Fachwahl
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Das Fach der Fachwahl wird gesetzt.
	 * @param fach Neuer Wert für das Feld fach
	 */
	public void setFach(final ReportingFach fach) {
		this.fach = fach;
	}

	/**
	 * Fach ist in mindestens einem Halbjahr der GOSt belegt.
	 * @return Inhalt des Feldes fachIstBelegtInGOSt
	 */
	public Boolean fachIstBelegtInGOSt() {
		return fachIstBelegtInGOSt;
	}

	/**
	 * Fach ist in mindestens einem Halbjahr der GOSt belegt wird gesetzt.
	 * @param fachIstBelegtInGOSt Neuer Wert für das Feld fachIstBelegtInGOSt
	 */
	public void setFachIstBelegtInGOSt(final Boolean fachIstBelegtInGOSt) {
		this.fachIstBelegtInGOSt = fachIstBelegtInGOSt;
	}

	/**
	 * Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache
	 * @return Inhalt des Feldes fachIstFortfuehrbareFremdspracheInGOSt
	 */
	public Boolean fachIstFortfuehrbareFremdspracheInGOSt() {
		return fachIstFortfuehrbareFremdspracheInGOSt;
	}

	/**
	 * Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache wird gesetzt.
	 * @param fachIstFortfuehrbareFremdspracheInGOSt Neuer Wert für das Feld fachIstFortfuehrbareFremdspracheInGOSt
	 */
	public void setFachIstFortfuehrbareFremdspracheInGOSt(final Boolean fachIstFortfuehrbareFremdspracheInGOSt) {
		this.fachIstFortfuehrbareFremdspracheInGOSt = fachIstFortfuehrbareFremdspracheInGOSt;
	}

	/**
	 * Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung
	 * @return Inhalt des Feldes jahrgangFremdsprachenbeginn
	 */
	public String jahrgangFremdsprachenbeginn() {
		return jahrgangFremdsprachenbeginn;
	}

	/**
	 * Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung wird gesetzt.
	 * @param jahrgangFremdsprachenbeginn Neuer Wert für das Feld jahrgangFremdsprachenbeginn
	 */
	public void setJahrgangFremdsprachenbeginn(final String jahrgangFremdsprachenbeginn) {
		this.jahrgangFremdsprachenbeginn = jahrgangFremdsprachenbeginn;
	}

	/**
	 * Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk
	 * @return Inhalt des Feldes positionFremdsprachenfolge
	 */
	public String positionFremdsprachenfolge() {
		return positionFremdsprachenfolge;
	}

	/**
	 * Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw Prüfungsvermerk wird gesetzt.
	 * @param positionFremdsprachenfolge Neuer Wert für das Feld positionFremdsprachenfolge
	 */
	public void setPositionFremdsprachenfolge(final String positionFremdsprachenfolge) {
		this.positionFremdsprachenfolge = positionFremdsprachenfolge;
	}
}
