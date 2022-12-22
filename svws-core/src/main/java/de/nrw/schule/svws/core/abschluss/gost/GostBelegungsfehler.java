package de.nrw.schule.svws.core.abschluss.gost;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung beinhaltet die Fehlercodes, die bei einer Belegprüfung auftreten können.
 * Die Aufzählungsobjekte enthalten zusätzlich die Textnachrichten für die unterschiedlichen
 * Belegprüfungsarten (EF.1 und GESAMT), die durchgeführt werden könnten.
 */
public class GostBelegungsfehler {

	/** BelegungsfehlerArt ABI_10: Unter den vier Abiturfächern müssen zwei der Fächer Deutsch, Mathematik oder Fremdsprache sein. */
	public static final @NotNull GostBelegungsfehler ABI_10 = new GostBelegungsfehler("ABI_10", GostBelegungsfehlerArt.BELEGUNG,
			"Unter den vier Abiturfächern müssen zwei der Fächer Deutsch, Mathematik oder Fremdsprache sein.",
			null);

	/** BelegungsfehlerArt ABI_11 */
	public static final @NotNull GostBelegungsfehler ABI_11 = new GostBelegungsfehler("ABI_11", GostBelegungsfehlerArt.BELEGUNG,
			"Religionslehre und Sport dürfen nicht gleichzeitig Abiturfächer sein.",
			null);

	/** BelegungsfehlerArt ABI_12 */
	public static final @NotNull GostBelegungsfehler ABI_12 = new GostBelegungsfehler("ABI_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"In Q2.2 muss das 3. Abiturfach schriftlich belegt sein.",
			null);

	/** BelegungsfehlerArt 	ABI_13 */
	public static final @NotNull GostBelegungsfehler ABI_13 = new GostBelegungsfehler("ABI_13", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"In Q2.2 muss das 4. Abiturfach mündlich belegt sein.",
			null);

	/** BelegungsfehlerArt ABI_15 */
	public static final @NotNull GostBelegungsfehler ABI_15 = new GostBelegungsfehler("ABI_15", GostBelegungsfehlerArt.BELEGUNG,
			"Sport kann nur als 2. oder als 4. Abiturfach gewählt werden.",
			null);

	/** BelegungsfehlerArt ABI_16 */
	public static final @NotNull GostBelegungsfehler ABI_16 = new GostBelegungsfehler("ABI_16", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Fächer, die keine Abiturfächer sind, müssen in Q2.2 mündlich belegt werden.",
			null);

	/** BelegungsfehlerArt ABI_17 */
	public static final @NotNull GostBelegungsfehler ABI_17 = new GostBelegungsfehler("ABI_17", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Das 3. Abiturfach muss von Q1.1 bis Q2.2 schriftlich belegt sein.",
			null);

	/** BelegungsfehlerArt  */
	public static final @NotNull GostBelegungsfehler ABI_18 = new GostBelegungsfehler("ABI_18", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Das 4. Abiturfach muss von Q1.1 bis Q2.1 schriftlich belegt sein.",
			null);

	/** BelegungsfehlerArt ABI_19: Unter den vier Abiturfächern müssen zwei der Fächer Deutsch, Mathematik oder Fremdsprache sein. Dies kann nicht durch zwei Fremdsprachen erfüllt werden. */
	public static final @NotNull GostBelegungsfehler ABI_19 = new GostBelegungsfehler("ABI_19", GostBelegungsfehlerArt.BELEGUNG,
			"Unter den vier Abiturfächern müssen zwei der Fächer Deutsch, Mathematik oder Fremdsprache sein. Dies kann nicht durch zwei Fremdsprachen erfüllt werden.",
			null);

	/** BelegungsfehlerArt ABI_21: Es kann nur ein Fach 1. Abiturfach sein. */
	public static final @NotNull GostBelegungsfehler ABI_21 = new GostBelegungsfehler("ABI_21", GostBelegungsfehlerArt.BELEGUNG,
			"Es kann nur ein Fach 1. Abiturfach sein.",
			null);

	/** BelegungsfehlerArt ABI_22: Es kann nur ein Fach 2. Abiturfach sein. */
	public static final @NotNull GostBelegungsfehler ABI_22 = new GostBelegungsfehler("ABI_22", GostBelegungsfehlerArt.BELEGUNG,
			"Es kann nur ein Fach 2. Abiturfach sein.",
			null);

	/** BelegungsfehlerArt ABI_23: Es kann nur ein Fach 3. Abiturfach sein. */
	public static final @NotNull GostBelegungsfehler ABI_23 = new GostBelegungsfehler("ABI_23", GostBelegungsfehlerArt.BELEGUNG,
			"Es kann nur ein Fach 3. Abiturfach sein.",
			null);

	/** BelegungsfehlerArt ABI_24: Es kann nur ein Fach 4. Abiturfach sein. */
	public static final @NotNull GostBelegungsfehler ABI_24 = new GostBelegungsfehler("ABI_24", GostBelegungsfehlerArt.BELEGUNG,
			"Es kann nur ein Fach 4. Abiturfach sein.",
			null);

	/** BelegungsfehlerArt ABI_18: In der Einführungsphase müssen in jedem Halbjahr mindestens 10 Fächer belegt werden. Vertiefungskurse werden bei der Zählung nicht berücksichtigt.
	 *  <br>In EF.1 müssen mindestens 10 Kurse belegt werden. Bei der Kurszählung werden Vertiefungskurse nicht mitgezählt. */
	public static final @NotNull GostBelegungsfehler ANZ_10 = new GostBelegungsfehler("ANZ_10", GostBelegungsfehlerArt.BELEGUNG,
			"In der Einführungsphase müssen in jedem Halbjahr mindestens 10 Fächer belegt werden. Vertiefungskurse werden bei der Zählung nicht berücksichtigt.",
			"In EF.1 müssen mindestens 10 Kurse belegt werden. Bei der Kurszählung werden Vertiefungskurse nicht mitgezählt.");

	/** BelegungsfehlerArt ANZ_11_INFO */
	public static final @NotNull GostBelegungsfehler ANZ_11_INFO = new GostBelegungsfehler("ANZ_11_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Die Stundenbandbreite sollte pro Halbjahr 32 bis 36 Stunden betragen, um eine gleichmäßige Stundenbelastung zu gewährleisten.",
			"Die Gesamtstundenzahl sollte 32 bis 36 Stunden betragen, um eine gleichmäßige Stundenbelastung in der Oberstufe zu gewährleisten.");

	/** BelegungsfehlerArt ANZ_12 */
	public static final @NotNull GostBelegungsfehler ANZ_12 = new GostBelegungsfehler("ANZ_12", GostBelegungsfehlerArt.BELEGUNG,
			"In der Qualifikationsphase müssen mindestens 38 anrechenbare Kurse belegt werden.",
			null);

	/** BelegungsfehlerArt ANZ_13 */
	public static final @NotNull GostBelegungsfehler ANZ_13 = new GostBelegungsfehler("ANZ_13", GostBelegungsfehlerArt.BELEGUNG,
			"In der Qualifikationsphase müssen mindestens 32 anrechenbare Kurse belegt werden.",
			null);

	/** BelegungsfehlerArt BIL_4_INFO */
	public static final @NotNull GostBelegungsfehler BIL_4_INFO = new GostBelegungsfehler("BIL_4_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Programmfehler: Dieser Hinweis ist für eine Gesamtbelegprüfung der Sek II nicht vorgesehen!",
			"Soll ein bilinguales Sachfach zum Sprachenschwerpunkt beitragen, so muss es schriftlich belegt werden.");

	/** BelegungsfehlerArt BIL_10 */
	public static final @NotNull GostBelegungsfehler BIL_10 = new GostBelegungsfehler("BIL_10", GostBelegungsfehlerArt.BELEGUNG,
			"Im bilingualen Bildungsgang muss die bilinguale Fremdsprache in EF.1 und EF.2 schriftlich und in Q1.1 bis Q2.2 als Leistungskurs belegt werden.",
			"Im bilingualen Bildungsgang muss die bilinguale Fremdsprache schriftlich belegt werden.");

	/** BelegungsfehlerArt BIL_11_INFO */
	public static final @NotNull GostBelegungsfehler BIL_11_INFO = new GostBelegungsfehler("BIL_11_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Im bilingualen Bildungsgang werden in EF.1 und EF.2 in der Regel zwei bilinguale Sachfächer belegt.",
			"Im bilingualen Bildungsgang sollten zwei bilinguale Sachfächer belegt werden.");

	/** BelegungsfehlerArt BIL_12 */
	public static final @NotNull GostBelegungsfehler BIL_12 = new GostBelegungsfehler("BIL_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Im bilingualen Bildungsgang muss ein bilinguales Sachfach durchgehend von Q1.1 bis Q2.1 schriftlich belegt werden.",
			null);

	/** BelegungsfehlerArt BIL_13 */
	public static final @NotNull GostBelegungsfehler BIL_13 = new GostBelegungsfehler("BIL_13", GostBelegungsfehlerArt.BELEGUNG,
			"Ein bilinguales Sachfach muss unter den Abiturfächern sein.",
			null);

	/** BelegungsfehlerArt BIL_14 */
	public static final @NotNull GostBelegungsfehler BIL_14 = new GostBelegungsfehler("BIL_14", GostBelegungsfehlerArt.BELEGUNG,
			"Es können nur bilinguale Sachfächer belegt werden, deren Sprache in der Sekundarstufe erlernt wurde.",
			"Es können nur bilinguale Sachfächer belegt werden, deren Sprache in der Sekundarstufe erlernt wurde.");

	/** BelegungsfehlerArt BIL_15 */
	public static final @NotNull GostBelegungsfehler BIL_15 = new GostBelegungsfehler("BIL_15", GostBelegungsfehlerArt.BELEGUNG,
			"Im bilingualen Bildungsgang muss in EF.1 und EF.2 mindestens ein bilinguales Sachfach belegt werden.",
			"Im bilingualen Bildungsgang muss mindestens ein bilinguales Sachfach belegt werden.");

	/** BelegungsfehlerArt D_10 */
	public static final @NotNull GostBelegungsfehler D_10 = new GostBelegungsfehler("D_10", GostBelegungsfehlerArt.BELEGUNG,
			"Deutsch muss von EF.1 bis Q2.2 belegt werden.",
			"Deutsch muss in EF.1 schriftlich belegt werden.");

	/** BelegungsfehlerArt D_11 */
	public static final @NotNull GostBelegungsfehler D_11 = new GostBelegungsfehler("D_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Deutsch muss von EF.1 bis wenigstens Q2.1 schriftlich belegt werden.",
			"Deutsch muss in EF.1 schriftlich belegt werden.");

	/** BelegungsfehlerArt E1BEL_10 */
	public static final @NotNull GostBelegungsfehler E1BEL_10 = new GostBelegungsfehler("E1BEL_10", GostBelegungsfehlerArt.BELEGUNG,
			"Bis auf Literatur, vokal- und instrumentalpraktische Kurse, Zusatzkurse, Vertiefungsfächer und Projektkurse können keine Fächer hinzugewählt werden, die nicht schon ab EF.1 belegt wurden.",
			null);

	/** BelegungsfehlerArt FS_10 */
	public static final @NotNull GostBelegungsfehler FS_10 = new GostBelegungsfehler("FS_10", GostBelegungsfehlerArt.BELEGUNG,
			"Mindestens eine Fremdsprache muss von EF.1 bis Q2.2 durchgehend belegt werden. Handelt es sich hierbei um eine neu einsetzende Fremdsprache, so muss zusätzlich mindestens eine aus der SI fortgeführte Fremdsprache von EF.1 bis EF.2 belegt werden.",
			"Da die gewählte Fremdsprache in der Oberstufe nicht durchgehend angeboten wird, muss entweder zusätzliche eine neu einsetzende Fremdsprache, oder eine andere in der Sekundarstufe I begonnene Fremdsprache belegt werden.");

	/** BelegungsfehlerArt FS_11 */
	public static final @NotNull GostBelegungsfehler FS_11 = new GostBelegungsfehler("FS_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Mindestens eine durchgehend belegte Fremdsprache muss von EF.1 bis Q2.1 schriftlich belegt sein.",
			"Mindestens eine Fremdsprache muss in EF.1 schriftlich belegt werden. Die zu wählende Fremdsprache muss durchgehend angeboten werden.");

	/** BelegungsfehlerArt FS_12 */
	public static final @NotNull GostBelegungsfehler FS_12 = new GostBelegungsfehler("FS_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"In EF.1 und EF.2 müssen alle gewählten Fremdsprachenfächer schriftlich belegt werden.",
			"In EF.1 müssen alle belegten Fremdsprachen schriftlich belegt werden.");

	/** BelegungsfehlerArt FS_13 */
	public static final @NotNull GostBelegungsfehler FS_13 = new GostBelegungsfehler("FS_13", GostBelegungsfehlerArt.BELEGUNG,
			"Bei unvollendeter 2. Fremdsprache, muss die in der Sekundarstufe 1 begonnene 2. Fremdsprache in EF schriftlich oder eine neu einsetzende Fremdsprache durchgehend schriftlich belegt werden.",
			"Wurde die 2. Fremdsprache erst ab Klasse 8 erlernt, muss die in der Sekundarstufe 1 begonnene 2. Fremdsprache oder eine neu einsetzende Fremdsprache schriftlich in EF.1 belegt werden.");

	/** BelegungsfehlerArt FS_14 */
	public static final @NotNull GostBelegungsfehler FS_14 = new GostBelegungsfehler("FS_14", GostBelegungsfehlerArt.BELEGUNG,
			"Bei fehlender 2. Fremdsprache muss eine neu einsetzende Fremdsprache durchgehend schriftlich belegt werden.",
			"Wurde bisher keine 2. Fremdsprache erlernt, muss eine neu einsetzende Fremdsprache in EF.1 schriftlich belegt werden.");

	/** BelegungsfehlerArt FS_15 */
	public static final @NotNull GostBelegungsfehler FS_15 = new GostBelegungsfehler("FS_15", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Kurse in neu einsetzenden Fremdsprachen müssen schriftlich belegt werden.",
			null);

	/** BelegungsfehlerArt FS_16 */
	public static final @NotNull GostBelegungsfehler FS_16 = new GostBelegungsfehler("FS_16", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Mindestens eine fortgeführte Fremdsprache muss in EF.1 und EF.2 schriftlich belegt werden.",
			"Mindestens eine in der Sekundarstufe I begonnene Fremdsprache muss in EF.1 schriftlich belegt werden.");

	/** BelegungsfehlerArt FS_17 */
	public static final @NotNull GostBelegungsfehler FS_17 = new GostBelegungsfehler("FS_17", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Neu einsetzende Fremdsprachen dürfen nicht als Leistungskurs belegt werden.",
			null);

	/** BelegungsfehlerArt FS_18 */
	public static final @NotNull GostBelegungsfehler FS_18 = new GostBelegungsfehler("FS_18", GostBelegungsfehlerArt.BELEGUNG,
			"Mindestens eine Fremdsprache muss von EF.1 bis Q2.2 belegt werden.",
			"Mindestens eine in der Sekundarstufe I begonnene Fremdsprache muss in EF.1 schriftlich belegt werden.");

	/** BelegungsfehlerArt FS_19_INFO */
	public static final @NotNull GostBelegungsfehler FS_19_INFO = new GostBelegungsfehler("FS_19_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Eine erfolgreiche Feststellungsprüfung in der Muttersprache am Ende der Sekundarstufe I und am Ende von EF.2 ist notwendig, um die Fremdsprachenbedingungen zu erfüllen.",
			"Eine erfolgreiche Feststellungsprüfung in der Muttersprache am Ende der Sekundarstufe I und am Ende von EF.2 ist notwendig, um die Fremdsprachenbedingungen zu erfüllen.");

	/** BelegungsfehlerArt FS_20 */
	public static final @NotNull GostBelegungsfehler FS_20 = new GostBelegungsfehler("FS_20", GostBelegungsfehlerArt.BELEGUNG,
			"Eine neu einsetzende Fremdsprache kann nur gewählt werden, wenn diese nicht zuvor in der Sekundarstaufe I belegt wurde. Die Sprachenfolge ist fehlerhaft.",
			"Eine neu einsetzende Fremdsprache kann nur gewählt werden, wenn diese nicht zuvor in der Sekundarstaufe I belegt wurde. Die Sprachenfolge ist fehlerhaft.");

	/** BelegungsfehlerArt FS_21 */
	public static final @NotNull GostBelegungsfehler FS_21 = new GostBelegungsfehler("FS_21", GostBelegungsfehlerArt.BELEGUNG,
			"Eine fortgeführte Fremdsprache wurde in der Sprachenfolge als neu einsetzend eingetragen. Die Sprachenfolge ist fehlerhaft.",
			"Eine fortgeführte Fremdsprache wurde in der Sprachenfolge als neu einsetzend eingetragen. Die Sprachenfolge ist fehlerhaft.");

	/** BelegungsfehlerArt FS_22_INFO */
	public static final @NotNull GostBelegungsfehler FS_22_INFO = new GostBelegungsfehler("FS_22_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Englisch wurde nicht in der Sprachenfolge eingetragen. Bitte Prüfen Sie die Sprachenfolge auf Korrektheit.",
			"Englisch wurde nicht in der Sprachenfolge eingetragen. Bitte Prüfen Sie die Sprachenfolge auf Korrektheit.");

    /** BelegungsfehlerArt FS_23 */
    public static final @NotNull GostBelegungsfehler FS_23 = new GostBelegungsfehler("FS_23", GostBelegungsfehlerArt.BELEGUNG,
        "Es wird das Fach einer fortgeführten Fremdsprache belegt, zu dem es keine entsprechende Eintragung einer Belegung oder Sprachprüfung in der Sekundarstufe I gibt. Bitte die Sprachenfolge und Sprachprüfungen prüfen.",
        "Es wird das Fach einer fortgeführten Fremdsprache belegt, zu dem es keine entsprechende Eintragung einer Belegung oder Sprachprüfung in der Sekundarstufe I gibt. Bitte die Sprachenfolge und Sprachprüfungen prüfen.");

    /** BelegungsfehlerArt FS_24 */
    public static final @NotNull GostBelegungsfehler FS_24 = new GostBelegungsfehler("FS_24", GostBelegungsfehlerArt.BELEGUNG,
        "Ist die Bedingungen für die Belegung einer zweiten Fremdsprache in der Sekundarstufe I noch nicht erfüllt worden, so müssen in der Einführungsphase zwei Fremdsprachen belegt werden.",
        "Ist die Bedingungen für die Belegung einer zweiten Fremdsprache in der Sekundarstufe I noch nicht erfüllt worden, so müssen in der Einführungsphase zwei Fremdsprachen belegt werden.");

    /** BelegungsfehlerArt FS_25 */
    public static final @NotNull GostBelegungsfehler FS_25 = new GostBelegungsfehler("FS_25", GostBelegungsfehlerArt.BELEGUNG,
        "Es wurde keine fortführbare Fremdsprache in der Sprachbelegung gefunden. Bitte prüfen Sie die Sprachenfolge.",
        "Es wurde keine fortführbare Fremdsprache in der Sprachbelegung gefunden. Bitte prüfen Sie die Sprachenfolge.");

    /** BelegungsfehlerArt  */
	public static final @NotNull GostBelegungsfehler GE_1_INFO = new GostBelegungsfehler("GE_1_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Programmfehler: Dieser Hinweis ist für eine Gesamtbelegprüfung der Sek II nicht vorgesehen!",
			"Wird Geschichte nicht in EF.1 belegt, so muss Geschichte in der Qualifikationsphase als Zusatzkurs gewählt werden.");

	/** BelegungsfehlerArt GE_1_INFO */
	public static final @NotNull GostBelegungsfehler GE_10 = new GostBelegungsfehler("GE_10", GostBelegungsfehlerArt.BELEGUNG,
			"Geschichte muss von EF.1 bis wenigstens Q1.2 oder als Zusatzkurs (in der Regel von Q2.1 bis Q2.2) belegt werden.",
			null);

	/** BelegungsfehlerArt GKS_10 */
	public static final @NotNull GostBelegungsfehler GKS_10 = new GostBelegungsfehler("GKS_10", GostBelegungsfehlerArt.BELEGUNG,
			"In der Qualifikationsphase sind pro Halbjahr mindestens 7 Fächer in Grundkursen zu wählen.",
			null);

	/** BelegungsfehlerArt GKS_11 */
	public static final @NotNull GostBelegungsfehler GKS_11 = new GostBelegungsfehler("GKS_11", GostBelegungsfehlerArt.BELEGUNG,
			"In der Qualifikationsphase sind pro Halbjahr mindestens 6 Fächer in Grundkursen zu wählen.",
			null);

	/** BelegungsfehlerArt GW_10 */
	public static final @NotNull GostBelegungsfehler GW_10 = new GostBelegungsfehler("GW_10", GostBelegungsfehlerArt.BELEGUNG,
			"Mindestens eine Gesellschaftswissenschaft muss von Q1.1 bis Q2.2 durchgehend belegt werden.",
			"Mindestens eine Gesellschaftswissenschaft muss in EF.1 belegt werden und durchgängig belegbar sein.");

	/** BelegungsfehlerArt GW_11 */
	public static final @NotNull GostBelegungsfehler GW_11 = new GostBelegungsfehler("GW_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"In EF.1 und EF.2 muss mindestens eine Gesellschaftswissenschaft schriftlich belegt sein.",
			"Mindestens eine Gesellschaftswissenschaft muss in EF.1 schriftlich belegt werden.");

	/** BelegungsfehlerArt GW_12 */
	public static final @NotNull GostBelegungsfehler GW_12 = new GostBelegungsfehler("GW_12", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Mindestens eine Gesellschaftswissenschaft oder Religionslehre muss von Q1.1 bis wenigstens Q2.1 schriftlich belegt werden.",
			null);

	/** BelegungsfehlerArt IGF_10 */
	public static final @NotNull GostBelegungsfehler IGF_10 = new GostBelegungsfehler("IGF_10", GostBelegungsfehlerArt.BELEGUNG,
			"Inhaltsgleiche Fächer und Projektkurse dürfen in jedem Halbjahr nur einmal belegt werden.",
			"Inhaltsgleiche Fächer dürfen nur einmal belegt werden.");

	/** BelegungsfehlerArt KU_MU_10 */
	public static final @NotNull GostBelegungsfehler KU_MU_10 = new GostBelegungsfehler("KU_MU_10", GostBelegungsfehlerArt.BELEGUNG,
			"Mindestens eines der Fächer Kunst oder Musik muss von EF.1 bis wenigstens Q1.2 durchgehend belegt werden. In der Qualifikationsphase kann auch alternativ Literatur, ein vokalpraktisches oder ein instrumentalpraktisches Fach mit zwei Kursen belegt werden.",
			"Mindestens eines der Fächer Kunst oder Musik muss in EF.1 belegt werden");

	/** BelegungsfehlerArt L_10_INFO */
	public static final @NotNull GostBelegungsfehler L_10_INFO = new GostBelegungsfehler("L_10_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Um das Latinum zu erlangen muss Latein in EF.1 und EF.2 belegt werden.",
			"Um das Latinum zu erlangen muss Latein in EF.1 schriftlich fortgeführt werden.");

	/** BelegungsfehlerArt L_11_INFO */
	public static final @NotNull GostBelegungsfehler L_11_INFO = new GostBelegungsfehler("L_11_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Um das Latinum zu erlangen muss Latein mindestens bis Q1.2, je nach Stundenvolumen sogar bis Q2.2 belegt werden.",
			null);

	/** BelegungsfehlerArt L_12_INFO */
	public static final @NotNull GostBelegungsfehler L_12_INFO = new GostBelegungsfehler("L_12_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Um das Latinum zu erlangen muss Latein mindestens bis EF.2, je nach Beginn und Stundenvolumen sogar bis Q2.2 belegt werden.",
			null);

	/** BelegungsfehlerArt LI_IV_10 */
	public static final @NotNull GostBelegungsfehler LI_IV_10 = new GostBelegungsfehler("LI_IV_10", GostBelegungsfehlerArt.BELEGUNG,
			"Die Fächer Literatur, instrumentalpraktischer bzw. vokalpraktischer Grundkurs dürfen maximal in zwei aufeinanderfolgenden Halbjahren belegt werden.",
			null);

	/** BelegungsfehlerArt LI_IV_11 */
	public static final @NotNull GostBelegungsfehler LI_IV_11 = new GostBelegungsfehler("LI_IV_11", GostBelegungsfehlerArt.BELEGUNG,
			"Es darf nur eins der Fächer Literatur, IP oder VP belegt werden. (Schulen mit genehmigtem musisch-künstlerischem Schwerpunkt müssen betroffene Schüler in SchILD-NRW manuell zulassen).",
			null);

	/** BelegungsfehlerArt LK_10 */
	public static final @NotNull GostBelegungsfehler LK_10 = new GostBelegungsfehler("LK_10", GostBelegungsfehlerArt.BELEGUNG,
			"In der Qualifikationsphase müssen zwei Fächer durchgehend in Leistungskursen belegt werden.",
			null);

	/** BelegungsfehlerArt LK_11 */
	public static final @NotNull GostBelegungsfehler LK_11 = new GostBelegungsfehler("LK_11", GostBelegungsfehlerArt.BELEGUNG,
			"Es dürfen nicht mehr als zwei Fächer als Leistungskurse belegt werden.",
			null);

	/** BelegungsfehlerArt LK1_10 */
	public static final @NotNull GostBelegungsfehler LK1_10 = new GostBelegungsfehler("LK1_10", GostBelegungsfehlerArt.BELEGUNG,
			"Erstes Abiturfach nicht eindeutig identifizierbar.",
			null);

	/** BelegungsfehlerArt LK1_11 */
	public static final @NotNull GostBelegungsfehler LK1_11 = new GostBelegungsfehler("LK1_11", GostBelegungsfehlerArt.BELEGUNG,
			"Das erste Leistungskursfach muss eine fortgeführte Fremdsprache, Mathematik, eine klassische Naturwissenschaft oder Deutsch sein.",
			null);

	/** BelegungsfehlerArt LK1_12 */
	public static final @NotNull GostBelegungsfehler LK1_12 = new GostBelegungsfehler("LK1_12", GostBelegungsfehlerArt.BELEGUNG,
			"Ist Deutsch erstes Leistungskursfach, muss Mathematik oder eine Fremdsprache unter den vier Abiturfächern sein.",
			null);

	/** BelegungsfehlerArt LK1_13 */
	public static final @NotNull GostBelegungsfehler LK1_13 = new GostBelegungsfehler("LK1_13", GostBelegungsfehlerArt.BELEGUNG,
			"Die Abiturfächer müssen alle drei Aufgabenfelder abdecken. Insgesamt sind vier Abiturfächer zu belegen.",
			null);

	/** BelegungsfehlerArt M_10 */
	public static final @NotNull GostBelegungsfehler M_10 = new GostBelegungsfehler("M_10", GostBelegungsfehlerArt.BELEGUNG,
			"Mathematik muss von EF.1 bis Q2.2 belegt werden.",
			"Mathematik muss in EF.1 schriftlich belegt werden.");

	/** BelegungsfehlerArt M_11 */
	public static final @NotNull GostBelegungsfehler M_11 = new GostBelegungsfehler("M_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Mathematik muss von EF.1 bis wenigstens Q2.1 schriftlich belegt werden.",
			"Mathematik muss in EF.1 schriftlich belegt werden.");

	/** BelegungsfehlerArt NW_10 */
	public static final @NotNull GostBelegungsfehler NW_10 = new GostBelegungsfehler("NW_10", GostBelegungsfehlerArt.BELEGUNG,
			"Mindestens eine klassische Naturwissenschaft (Physik, Biologie, Chemie) muss durchgehend von Q1.1 bis Q2.2 belegt werden.",
			"Mindestens eines der Fächer Physik, Chemie oder Biologie muss in EF.1 belegt werden und durchgängig belegbar sein.");

	/** BelegungsfehlerArt NW_11 */
	public static final @NotNull GostBelegungsfehler NW_11 = new GostBelegungsfehler("NW_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"In EF.1 und EF.2 muss mindestens eine klassische Naturwissenschaft schriftlich belegt sein.",
			"Mindestens eines der Fächer Physik, Chemie oder Biologie muss in EF.1 schriftlich belegt werden.");

	/** BelegungsfehlerArt NW_FS_10 */
	public static final @NotNull GostBelegungsfehler NW_FS_10 = new GostBelegungsfehler("NW_FS_10", GostBelegungsfehlerArt.BELEGUNG,
			"Von EF.1 bis Q2.2 müssen entweder zwei Naturwissenschaften oder zwei Fremdsprachen durchgehend gewählt werden. Hierbei ist eine Naturwissenschaft oder sind zwei Fremdsprachen schriftlich zu belegen. Zu den Fremdsprachen zählen auch in einer weiteren Fremdsprache unterrichtete Sachfächer.",
			"In EF.1 müssen entweder zwei Naturwissenschaften oder zwei Fremdsprachen belegt werden. Hierbei ist eine Naturwissenschaft oder sind zwei Fremdsprachen schriftlich zu belegen. Zu den Fremdsprachen zählen auch in einer weiteren Fremdsprache unterrichtete Sachfächer.");

	/** BelegungsfehlerArt NW_FS_11 */
	public static final @NotNull GostBelegungsfehler NW_FS_11 = new GostBelegungsfehler("NW_FS_11", GostBelegungsfehlerArt.SCHRIFTLICHKEIT,
			"Zwei Fremdsprachen oder eines von mindestens zwei naturwissenschaftlichen Fächern muss von Q1.1 bis wenigstens Q2.1 schriftlich belegt werden.",
			null);

	/** BelegungsfehlerArt NW_FS_12_INFO */
	public static final @NotNull GostBelegungsfehler NW_FS_12_INFO = new GostBelegungsfehler("NW_FS_12_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Da von EF.1 bis Q2.2 weniger als zwei naturwissenschaftliche Fächer durchgehend belegt wurden, oder kein naturwissenschaftliches Fach schriftlich belegt wurde, liegt ausschließlich ein Sprachenschwerpunkt vor.",
			"Da in EF.1 weniger als zwei Naturwissenschaften belegt, oder keine schriftlich belegt wurden, müssen zwei Fremdsprachen bis Q2.2 durchgehend schriftlich belegt werden.");

	/** BelegungsfehlerArt NW_FS_13_INFO */
	public static final @NotNull GostBelegungsfehler NW_FS_13_INFO = new GostBelegungsfehler("NW_FS_13_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Da von EF.1 bis Q2.2 weniger als zwei Fremdsprachen schriftlich belegt wurden, liegt ausschließlich ein naturwissenschaftlicher Schwerpunkt vor.",
			"Da in EF.1 weniger als zwei Fremdsprachen schriftlich belegt wurden, müssen zwei Naturwissenschaften bis Q2.2 belegt, davon mindestens eine schriftlich belegt werden.");

	/** BelegungsfehlerArt PF_10 */
	public static final @NotNull GostBelegungsfehler PF_10 = new GostBelegungsfehler("PF_10", GostBelegungsfehlerArt.BELEGUNG,
			"In der Einführungsphase können keine Projektkurse belegt werden.",
			"In EF.1 können keine Projektkurse belegt werden.");

	/** BelegungsfehlerArt PF_11 */
	public static final @NotNull GostBelegungsfehler PF_11 = new GostBelegungsfehler("PF_11", GostBelegungsfehlerArt.BELEGUNG,
			"In der Qualifikationsphase kann maximal ein Projektkurs belegt werden.",
			null);

	/** BelegungsfehlerArt PF_12 */
	public static final @NotNull GostBelegungsfehler PF_12 = new GostBelegungsfehler("PF_12", GostBelegungsfehlerArt.BELEGUNG,
			"Projektkurse können nur dann belegt werden, wenn gleichzeitig oder maximal zwei Halbjahre vorher auch das Leitfach im gleichem Umfang belegt wurde.",
			null);

	/** BelegungsfehlerArt PF_13 */
	public static final @NotNull GostBelegungsfehler PF_13 = new GostBelegungsfehler("PF_13", GostBelegungsfehlerArt.BELEGUNG,
			"Ein Projektkurs kann nur belegt werden, wenn in der Qualifikationsphase zeitgleich oder vorab auch sein Leitfach zwei Halbjahre lang belegt wurde.",
			null);

	/** BelegungsfehlerArt PF_14 */
	public static final @NotNull GostBelegungsfehler PF_14 = new GostBelegungsfehler("PF_14", GostBelegungsfehlerArt.BELEGUNG,
			"In der Qualifikationsphase kann maximal ein Projektkurs in zwei aufeinanderfolgenden Halbjahren belegt werden.",
			null);

	/** BelegungsfehlerArt PF_15 */
	public static final @NotNull GostBelegungsfehler PF_15 = new GostBelegungsfehler("PF_15", GostBelegungsfehlerArt.BELEGUNG,
			"Es existiert kein Projektkurs, der als besondere Lernleistung eingebracht werden kann.",
			null);

	/** BelegungsfehlerArt PF_16_INFO */
	public static final @NotNull GostBelegungsfehler PF_16_INFO = new GostBelegungsfehler("PF_16_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Wird der Projektkurs als besondere Lernleistung in das Abitur eingebracht, so zählt er nicht mehr zu den einbringungsfähigen Kursen in Block I.",
			null);

	/** BelegungsfehlerArt PF_17_INFO */
	public static final @NotNull GostBelegungsfehler PF_17_INFO = new GostBelegungsfehler("PF_17_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Ein Projektkurs soll nur im Ausnahmefall abgewählt werden. Bei einem abgewählten Projektkurs werden lediglich die Wochenstunden auf die Laufbahn angerechnet.",
			null);

	/** BelegungsfehlerArt PF_18 */
	public static final @NotNull GostBelegungsfehler PF_18 = new GostBelegungsfehler("PF_18", GostBelegungsfehlerArt.BELEGUNG,
			"Ein Projektkurs kann nicht in der Q2.2 beginnen.",
			null);

	/** BelegungsfehlerArt RE_10 */
	public static final @NotNull GostBelegungsfehler RE_10 = new GostBelegungsfehler("RE_10", GostBelegungsfehlerArt.BELEGUNG,
			"Religionslehre muss wenigstens von EF.1-Q1.2 durchgehend belegt werden. Als Ersatz kann Philosophie dienen, sofern Philosophie nicht die einzige von EF.1 bis Q2.2 durchgehend belegte Gesellschaftswissenschaft ist. In diesen Fällen muss ein weiteres Fach der Gesellschaftswissenschaften als Religionsersatz dienen.",
			"Ein Religionskurs muss in EF.1 belegt werden. Als Ersatz kann Philosophie belegt werden, sofern eine weitere Gesellschaftswissenschaft bis zum Abitur belegt wird.");

	/** BelegungsfehlerArt RE_11 */
	public static final @NotNull GostBelegungsfehler RE_11 = new GostBelegungsfehler("RE_11", GostBelegungsfehlerArt.BELEGUNG,
			"Philosophie darf nicht die einzige durchgehend von Q1.1 bis Q2.2 belegte Geisteswissenschaft sein.",
			null);

	/** BelegungsfehlerArt SP_10 */
	public static final @NotNull GostBelegungsfehler SP_10 = new GostBelegungsfehler("SP_10", GostBelegungsfehlerArt.BELEGUNG,
			"Sport muss von EF.1 bis Q2.2 belegt werden.",
			"Sport muss in EF.1 belegt werden.");

	/** BelegungsfehlerArt STD_10 */
	public static final @NotNull GostBelegungsfehler STD_10 = new GostBelegungsfehler("STD_10", GostBelegungsfehlerArt.BELEGUNG,
			"Der Pflichtunterricht darf 102 Stunden nicht unterschreiten.",
			null);

	/** BelegungsfehlerArt STD_11_INFO */
	public static final @NotNull GostBelegungsfehler STD_11_INFO = new GostBelegungsfehler("STD_11_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Der Pflichtunterricht darf nur in begründeten Ausnahmefällen 102 Stunden unterschreiten.",
			null);

	/** BelegungsfehlerArt SW_1_INFO */
	public static final @NotNull GostBelegungsfehler SW_1_INFO = new GostBelegungsfehler("SW_1_INFO", GostBelegungsfehlerArt.HINWEIS,
			"Programmfehler: Dieser Hinweis ist für eine Gesamtbelegprüfung der Sek II nicht vorgesehen!",
			"Wird Sozialwissenschaften nicht in EF.1 belegt, so muss Sozialwissenschaften in der Qualifikationsphase als Zusatzkurs gewählt werden.");

	/** BelegungsfehlerArt SW_10 */
	public static final @NotNull GostBelegungsfehler SW_10 = new GostBelegungsfehler("SW_10", GostBelegungsfehlerArt.BELEGUNG,
			"Sozialwissenschaften muss von EF.1 bis wenigstens Q1.2 oder als Zusatzkurs (in der Regel von Q2.1 bis Q2.2) belegt werden.",
			null);

	/** BelegungsfehlerArt VF_10 */
	public static final @NotNull GostBelegungsfehler VF_10 = new GostBelegungsfehler("VF_10", GostBelegungsfehlerArt.BELEGUNG,
			"In der Einführungsphase können maximal vier Vertiefungskurse belegt werden.",
			null);

	/** BelegungsfehlerArt VF_11 */
	public static final @NotNull GostBelegungsfehler VF_11 = new GostBelegungsfehler("VF_11", GostBelegungsfehlerArt.BELEGUNG,
			"In der Qualifikationsphase können maximal zwei Vertiefungskurse belegt werden.",
			null);

	/** BelegungsfehlerArt WST_10 */
	public static final @NotNull GostBelegungsfehler WST_10 = new GostBelegungsfehler("WST_10", GostBelegungsfehlerArt.BELEGUNG,
			"Die Summe der durchschnittlichen Jahreskursstunden von EF.1 bis Q2.2 darf 100 nicht unterschreiten.",
			null);

	/** BelegungsfehlerArt WST_20 */
	public static final @NotNull GostBelegungsfehler WST_20 = new GostBelegungsfehler("WST_20", GostBelegungsfehlerArt.BELEGUNG,
			"Die durchschnittliche Wochenstundenzahl muss in der Einführungsphase mindestens 34 Stunden betragen.",
			null);

	/** BelegungsfehlerArt WST_21 */
	public static final @NotNull GostBelegungsfehler WST_21 = new GostBelegungsfehler("WST_21", GostBelegungsfehlerArt.BELEGUNG,
			"Die durchschnittliche Wochenstundenzahl muss in der Qualifikationsphase mindestens 34 Stunden betragen.",
			null);

	/** BelegungsfehlerArt ZK_10 */
	public static final @NotNull GostBelegungsfehler ZK_10 = new GostBelegungsfehler("ZK_10", GostBelegungsfehlerArt.BELEGUNG,
			"Ein Zusatzkurs in Geschichte oder Sozialwissenschaften kann nur angewählt werden, wenn das Fach im vorangegangenen Halbjahr nicht belegt wurde.",
			null);

	/** BelegungsfehlerArt ZK_11 */
	public static final @NotNull GostBelegungsfehler ZK_11 = new GostBelegungsfehler("ZK_11", GostBelegungsfehlerArt.BELEGUNG,
			"Ein Zusatzkurs in Geschichte oder Sozialwissenschaften muss in zwei aufeinanderfolgenden Halbjahren belegt werden.",
			null);

	/** BelegungsfehlerArt ZK_12 */
	public static final @NotNull GostBelegungsfehler ZK_12 = new GostBelegungsfehler("ZK_12", GostBelegungsfehlerArt.BELEGUNG,
			"Zusatzkurse dürfen maximal in zwei aufeinanderfolgenden Halbjahren belegt werden.",
			null);

	/** BelegungsfehlerArt ZK_13 */
	public static final @NotNull GostBelegungsfehler ZK_13 = new GostBelegungsfehler("ZK_13", GostBelegungsfehlerArt.BELEGUNG,
			"Zusatzkurse dürfen nur einmal pro Fach belegt werden und können nicht in einem bilingualen Fach gewählt werden.",
			null);


	/** Der eindeutige Code des Belegungsfehlers */
	public final @NotNull String code;

	/** Die Art des Fehlers */
	public final @NotNull GostBelegungsfehlerArt art;

	/** Der Text des Fehlers, der bei einer Gesamtprüfung ausgegeben wird */
	public final @NotNull String textGESAMT;

	/** Der alternative Text, der bei der eingeschränkten Prüfung für die EF.1 ausgegeben wird */
	public final @NotNull String textEF1;



	/**
	 * Erstellt einen neuen Belegungsfehler für die Aufzählung (s.o.). Dabei wird ein
	 * Text für die Gesamtprüfung und die EF.1-Prüfung angegeben.
	 *
	 * @param code         der eindeutige Code des Belegungsfehlers
	 * @param art          die Fehlerart (Belegungsfehler, Schriftlichkeit oder Information)
	 * @param textGESAMT   der zugeordnete Text für die Gesamtbelegprüfung oder null
	 * @param textEF1      der zugeordnete Text für die EF.1-Prüfung oder null
	 */
	private GostBelegungsfehler(final @NotNull String code, final @NotNull GostBelegungsfehlerArt art, final String textGESAMT, final String textEF1) {
		this.code = code;
		this.art = art;
		this.textGESAMT = textGESAMT != null ? textGESAMT : "Programmfehler: Diese Belegungsfehlerart ist für eine Gesamt-Prüfung nicht vorgesehen!";
		this.textEF1 = (textEF1 != null) ? textEF1 : "Programmfehler: Diese Belegungsfehlerart ist für eine Prüfung eingeschränkt auf die EF.1 nicht vorgesehen!";
	}


	/**
	 * Gibt zurück, ob es sich bei dem Belegungsfehler nur um eine Information
	 * und nicht um einen "echten" Fehler handelt.
	 *
	 * @return true, falls es sich nur um eine Information handelt, sonst false
	 */
	public boolean istInfo() {
		return (this.art == GostBelegungsfehlerArt.HINWEIS);
	}


	/**
	 * Gibt zurück, ob es sich bei dem Belegungsfehler um einen "echten" Fehler handelt
	 * und nicht nur um eine Information.
	 *
	 * @return true, falls es sich um einen "echten" Fehler handelt, sonst false
	 */
	public boolean istFehler() {
		return (this.art != GostBelegungsfehlerArt.HINWEIS);
	}


	/**
	 * Gibt die Art des Belegungsfehlers zurück.
	 *
	 * @return die Art des Belegungsfehlers
	 */
	public @NotNull GostBelegungsfehlerArt getArt() {
		return art;
	}


	/**
	 * Gibt je nach angegebenener Belegprüfungsart den zugehörigen Text für den Belegungsfehler
	 * zurück.
	 *
	 * @param pruef_art   die Belegprüfungsart
	 *
	 * @return der zugehörige Text des Belegungsfehlers
	 */
	public @NotNull String getText(final @NotNull GostBelegpruefungsArt pruef_art) {
		if (pruef_art == GostBelegpruefungsArt.EF1)
			return textEF1;
		if (pruef_art == GostBelegpruefungsArt.GESAMT)
			return textGESAMT;
		return textGESAMT;
	}


	@Override
	public @NotNull String toString() {
		return code;
	}

}
