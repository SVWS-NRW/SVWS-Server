package de.svws_nrw.module.pdf.reptypes.gost.laufbahnplanung;

/**
 * Die Klasse enthält die Fachwahl eines Schülers für die GOSt-Laufbahnplanung.
 */

public class RepGostLaufbahnplanungFachwahl {

	/** Die Bezeichnung des Faches */
	public String bezeichnung;

	/** Das Kürzel des Faches */
	public String kuerzel;

	/** Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache */
	public Boolean fachIstFortfuehrbareFremdspracheInGOSt;

	/** Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung */
	public String jahrgangFremdsprachenbeginn;

	/** Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk */
	public String positionFremdsprachenfolge;

	/** Fachbelegung in der EF.1 */
	public String belegungEF1;

	/** Fachbelegung in der EF.2 */
	public String belegungEF2;

	/** Fachbelegung in der Q1.1 */
	public String belegungQ11;

	/** Fachbelegung in der Q1.2 */
	public String belegungQ12;

	/** Fachbelegung in der Q2.1 */
	public String belegungQ21;

	/** Fachbelegung in der Q2.2 */
	public String belegungQ22;

	/** Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde */
	public String abiturfach;

	/** Fach ist in mindestens einem Halbjahr der GOSt belegt. */
	public Boolean fachIstBelegtInGOSt;

	/** Aufgabenfeld des Faches */
	public int aufgabenfeld;

	/** Fachgruppe des Faches */
	public String fachgruppe;

	/** Farbe des Faches im Web-Client */
	public String farbeClientRGB;


	/**
	 * Erstellt eine neue Fachwahl eines Schülers für die GOSt-Laufbahnplanung.
	 *
	 * @param bezeichnung								Die Bezeichnung des Faches.
	 * @param kuerzel									Das Kürzel des Faches.
	 * @param fachIstFortfuehrbareFremdspracheInGOSt	Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache.
	 * @param jahrgangFremdsprachenbeginn				Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung.
	 * @param positionFremdsprachenfolge				Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk.
	 * @param belegungEF1								Fachbelegung in der EF.1
	 * @param belegungEF2								Fachbelegung in der EF.2
	 * @param belegungQ11								Fachbelegung in der Q1.1
	 * @param belegungQ12								Fachbelegung in der Q1.2
	 * @param belegungQ21								Fachbelegung in der Q2.1
	 * @param belegungQ22								Fachbelegung in der Q2.2
	 * @param abiturfach								Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde.
	 * @param fachIstBelegtInGOSt						Fach ist in mindestens einem Halbjahr der GOSt belegt.
	 * @param aufgabenfeld								Aufgabenfeld des Faches.
	 * @param fachgruppe								Fachgruppe des Faches.
	 * @param farbeClientRGB							Farbe des Faches im Web-Client.
	 */
	public RepGostLaufbahnplanungFachwahl(final String bezeichnung,
										  final String kuerzel,
										  final Boolean fachIstFortfuehrbareFremdspracheInGOSt,
										  final String jahrgangFremdsprachenbeginn,
										  final String positionFremdsprachenfolge,
										  final String belegungEF1,
										  final String belegungEF2,
										  final String belegungQ11,
										  final String belegungQ12,
										  final String belegungQ21,
										  final String belegungQ22,
										  final String abiturfach,
										  final Boolean fachIstBelegtInGOSt,
										  final int aufgabenfeld,
										  final String fachgruppe,
										  final String farbeClientRGB) {
		this.bezeichnung = bezeichnung;
		this.kuerzel = kuerzel;
		this.fachIstFortfuehrbareFremdspracheInGOSt = fachIstFortfuehrbareFremdspracheInGOSt;
		this.jahrgangFremdsprachenbeginn = jahrgangFremdsprachenbeginn;
		this.positionFremdsprachenfolge = positionFremdsprachenfolge;
		this.belegungEF1 = belegungEF1;
		this.belegungEF2 = belegungEF2;
		this.belegungQ11 = belegungQ11;
		this.belegungQ12 = belegungQ12;
		this.belegungQ21 = belegungQ21;
		this.belegungQ22 = belegungQ22;
		this.abiturfach = abiturfach;
		this.fachIstBelegtInGOSt = fachIstBelegtInGOSt;
		this.aufgabenfeld = aufgabenfeld;
		this.fachgruppe = fachgruppe;
		this.farbeClientRGB = farbeClientRGB;
	}
}
