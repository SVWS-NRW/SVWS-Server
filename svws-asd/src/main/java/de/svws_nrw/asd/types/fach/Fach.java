package de.svws_nrw.asd.types.fach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.RGBFarbe;
import de.svws_nrw.asd.data.fach.FachKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für den Zeugnisdruck erforderlichen Fachgruppen
 */
public enum Fach implements CoreType<FachKatalogEintrag, Fach> {

	/** Fach Arbeits- und Betriebswirtschaftslehre */
	AB,

	/** Fach Neigungs- und Projektgruppen */
	AG,

	/** Fach Arbeitslehre - Schwerpunkt Hauswirtschaft */
	AH,

	/** Fach Arbeitslehre */
	AL,

	/** Fach Unterricht in der Herkunftssprache - Arabisch */
	AM,

	/** Fach Alevitische Religionslehre nach den Grundsätzen des AABF */
	AR,

	/** Fach Arbeitslehre - Schwerpunkt Technik */
	AT,

	/** Fach Arbeitsvorbereitung */
	AV,

	/** Fach Arbeitslehre - Schwerpunkt Wirtschaft */
	AW,

	/** Fach Arbeitslehre - Technik/Wirtschaft (nur Wahlpflichtunterricht) */
	AX,

	/** Fach Arbeitslehre - Hauswirtschaft/Wirtschaft (nur Wahlpflichtunterricht) */
	AY,

	/** Fach Berufs- und Arbeitspädagogik */
	BA,

	/** Fach Bürowirtschaft */
	BF,

	/** Fach Betrieb und Gesellschaft/Politik */
	BG,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Bosnisch */
	BH,

	/** Fach Biologie */
	BI,

	/** Fach Betriebsinformatik */
	BK,

	/** Fach Unterricht in der Herkunftssprache - Bosnisch */
	BM,

	/** Fach Braille'sche Punktschrift */
	BN,

	/** Fach Betriebspraxis */
	BP,

	/** Fach Betriebslehre */
	BR,

	/** Fach Betriebssoziologie / Arbeitsrecht */
	BS,

	/** Fach Berufsvorbereitung */
	BV,

	/** Fach Betriebswirtschaftslehre */
	BW,

	/** Fach Betriebssysteme / Netzwerke */
	BY,

	/** Fach Chinesisch */
	C,

	/** Fach Chinesisch, regulärer Beginn in der Einführungsphase */
	C0,

	/** Fach Chinesisch, regulärer Beginn in Jahrgang 11 */
	C1,

	/** Fach Chinesisch, regulärer Beginn in Jahrgang 5 */
	C5,

	/** Fach Chinesisch, regulärer Beginn in Jahrgang 6 */
	C6,

	/** Fach Chinesisch, regulärer Beginn in Jahrgang 7 */
	C7,

	/** Fach Chinesisch, regulärer Beginn in Jahrgang 8 */
	C8,

	/** Fach Chinesisch, regulärer Beginn in Jahrgang 9 */
	C9,

	/** Fach Chemie */
	CH,

	/** Fach Unterricht in der Herkunftssprache - Kroatisch */
	CM,

	/** Fach Chinesisch, außerhalb des regulären Fachunterrichts */
	CQ,

	/** Fach Deutsch */
	D,

	/** Fach Datenbanken */
	DB,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Rumänisch */
	DH,

	/** Fach Didaktik u. Methodik der soz.päd. Praxis mit Übungen */
	DM,

	/** Fach Darstellen und Gestalten */
	DS,

	/** Fach Datenverarbeitung */
	DV,

	/** Fach Englisch */
	E,

	/** Fach Erweitertes Bildungsangebot */
	EB,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Serbisch */
	EH,

	/** Fach Erdkunde/ Geographie */
	EK,

	/** Fach Ernährungslehre */
	EL,

	/** Fach Unterricht in der Herkunftssprache - Serbisch */
	EM,

	/** Fach Evangelische Religionslehre (konfessionell kooperativ) */
	EN,

	/** Fach Evangelische Religionslehre */
	ER,

	/** Fach Französisch */
	F,

	/** Fach Französisch, regulärer Beginn in der Einführungsphase */
	F0,

	/** Fach Französisch, regulärer Beginn in Jahrgang 11 */
	F1,

	/** Fach Französisch, regulärer Beginn in Jahrgang 5 */
	F5,

	/** Fach Französisch, regulärer Beginn in Jahrgang 6 */
	F6,

	/** Fach Französisch, regulärer Beginn in Jahrgang 7 */
	F7,

	/** Fach Französisch, regulärer Beginn in Jahrgang 8 */
	F8,

	/** Fach Französisch, regulärer Beginn in Jahrgang 9 */
	F9,

	/** Fach Fächer des berufsbezogenen Bereichs */
	FB,

	/** Fach Fremdsprachliche Kommunikation */
	FK,

	/** Fach Fachpraxis */
	FP,

	/** Fach Französisch, außerhalb des regulären Fachunterrichts */
	FQ,

	/** Fach Förderunterricht */
	FU,

	/** Fach Griechisch */
	G,

	/** Fach Griechisch, regulärer Beginn in der Einführungsphase */
	G0,

	/** Fach Griechisch, regulärer Beginn in Jahrgang 11 */
	G1,

	/** Fach Griechisch, regulärer Beginn in Jahrgang 5 */
	G5,

	/** Fach Griechisch, regulärer Beginn in Jahrgang 6 */
	G6,

	/** Fach Griechisch, regulärer Beginn in Jahrgang 7 */
	G7,

	/** Fach Griechisch, regulärer Beginn in Jahrgang 8 */
	G8,

	/** Fach Griechisch, regulärer Beginn in Jahrgang 9 */
	G9,

	/** Fach Geräte- und Maschinenlehre */
	GA,

	/** Fach Grundbildung */
	GB,

	/** Fach Geschichte */
	GE,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Neugriechisch */
	GH,

	/** Fach Gesellschaftslehre */
	GL,

	/** Fach Unterricht in der Herkunftssprache - Neugriechisch */
	GM,

	/** Fach Geologie (Oberstufenkolleg Bielefeld) */
	GO,

	/** Fach Geschichte / Politik */
	GP,

	/** Fach Altgriechisch, außerhalb des regulären Fachunterrichts */
	GQ,

	/** Fach Gestaltungslehre */
	GS,

	/** Fach Gesamtunterricht (nur für Förderschulkindergarten) */
	GU,

	/** Fach Geschichte und Sozialwissenschaft */
	GW,

	/** Fach Hebräisch */
	H,

	/** Fach Hebräisch, regulärer Beginn in der Einführungsphase */
	H0,

	/** Fach Hebräisch, regulärer Beginn in Jahrgang 11 */
	H1,

	/** Fach Hebräisch, regulärer Beginn in Jahrgang 5 */
	H5,

	/** Fach Hebräisch, regulärer Beginn in Jahrgang 6 */
	H6,

	/** Fach Hebräisch, regulärer Beginn in Jahrgang 7 */
	H7,

	/** Fach Hebräisch, regulärer Beginn in Jahrgang 8 */
	H8,

	/** Fach Hebräisch, regulärer Beginn in Jahrgang 9 */
	H9,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Kroatisch */
	HH,

	/** Fach Hebräisch, außerhalb des regulären Fachunterrichts */
	HQ,

	/** Fach Jüdische Religionslehre */
	HR,

	/** Fach Hausunterricht */
	HU,

	/** Fach Hauswirtschaft */
	HW,

	/** Fach Hygiene */
	HY,

	/** Fach Italienisch */
	I,

	/** Fach Italienisch, regulärer Beginn in der Einführungsphase */
	I0,

	/** Fach Italienisch, regulärer Beginn in Jahrgang 11 */
	I1,

	/** Fach Italienisch, regulärer Beginn in Jahrgang 5 */
	I5,

	/** Fach Italienisch, regulärer Beginn in Jahrgang 6 */
	I6,

	/** Fach Italienisch, regulärer Beginn in Jahrgang 7 */
	I7,

	/** Fach Italienisch, regulärer Beginn in Jahrgang 8 */
	I8,

	/** Fach Italienisch, regulärer Beginn in Jahrgang 9 */
	I9,

	/** Fach Individuelles Lernen (Ergänzungsstunden, Ganztag- und Betreuungsangebote) */
	IE,

	/** Fach Informatik */
	IF,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Italienisch */
	IH,

	/** Fach Individuelles Lernen (dem Kernstundenkontingent entnommen) */
	IK,

	/** Fach Islamische Religionslehre */
	IL,

	/** Fach Unterricht in der Herkunftssprache - Italienisch */
	IM,

	/** Fach Instrumentalpraktischer Grundkurs */
	IN,

	/** Fach Italienisch, außerhalb des regulären Fachunterrichts */
	IQ,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Niederländisch */
	JH,

	/** Fach Japanisch */
	K,

	/** Fach Japanisch, regulärer Beginn in der Einführungsphase */
	K0,

	/** Fach Japanisch, regulärer Beginn in Jahrgang 11 */
	K1,

	/** Fach Japanisch, regulärer Beginn in Jahrgang 5 */
	K5,

	/** Fach Japanisch, regulärer Beginn in Jahrgang 6 */
	K6,

	/** Fach Japanisch, regulärer Beginn in Jahrgang 7 */
	K7,

	/** Fach Japanisch, regulärer Beginn in Jahrgang 8 */
	K8,

	/** Fach Japanisch, regulärer Beginn in Jahrgang 9 */
	K9,

	/** Fach Katholische Religionslehre (konfessionell kooperativ) */
	KN,

	/** Fach Kommunikation */
	KO,

	/** Fach Japanisch, außerhalb des regulären Fachunterrichts */
	KQ,

	/** Fach Katholische Religionslehre */
	KR,

	/** Fach Kurzschrift */
	KS,

	/** Fach Kunst */
	KU,

	/** Fach Lateinisch */
	L,

	/** Fach Lateinisch, regulärer Beginn in der Einführungsphase */
	L0,

	/** Fach Lateinisch, regulärer Beginn in Jahrgang 11 */
	L1,

	/** Fach Lateinisch, regulärer Beginn in Jahrgang 5 */
	L5,

	/** Fach Lateinisch, regulärer Beginn in Jahrgang 6 */
	L6,

	/** Fach Lateinisch, regulärer Beginn in Jahrgang 7 */
	L7,

	/** Fach Lateinisch, regulärer Beginn in Jahrgang 8 */
	L8,

	/** Fach Lateinisch, regulärer Beginn in Jahrgang 9 */
	L9,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Albanisch */
	LH,

	/** Fach Literatur */
	LI,

	/** Fach Unterricht in der Herkunftssprache - Albanisch */
	LM,

	/** Fach Lateinisch, außerhalb des regulären Fachunterrichts */
	LQ,

	/** Fach Mathematik */
	M,

	/** Fach Religionsunterricht der mennonitischen Brüdergemeinden in NRW als Schulversuch */
	MB,

	/** Fach Medienerziehung */
	MD,

	/** Fach Mechanik */
	ME,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Mazedonisch */
	MH,

	/** Fach Unterricht in der Herkunftssprache - Mazedonisch */
	MM,

	/** Fach Meß- und Prüftechnik */
	MP,

	/** Fach Musik */
	MU,

	/** Fach Methodik des wissenschaftl. orientierten Arbeitens */
	MW,

	/** Fach Spezielle sonderpädagogische Maßnahme */
	MX,

	/** Fach Fächerübergreifender Unterricht in SI und SII (Berufspraxisstufe) */
	MY,

	/** Fach Niederländisch */
	N,

	/** Fach Niederländisch, regulärer Beginn in der Einführungsphase */
	N0,

	/** Fach Niederländisch, regulärer Beginn in Jahrgang 11 */
	N1,

	/** Fach Niederländisch, regulärer Beginn in Jahrgang 5 */
	N5,

	/** Fach Niederländisch, regulärer Beginn in Jahrgang 6 */
	N6,

	/** Fach Niederländisch, regulärer Beginn in Jahrgang 7 */
	N7,

	/** Fach Niederländisch, regulärer Beginn in Jahrgang 8 */
	N8,

	/** Fach Niederländisch, regulärer Beginn in Jahrgang 9 */
	N9,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Polnisch */
	NH,

	/** Fach Unterricht in der Herkunftssprache - Niederländisch */
	NM,

	/** Fach Niederländisch, außerhalb des regulären Fachunterrichts */
	NQ,

	/** Fach Naturwissenschaften */
	NW,

	/** Fach Portugiesisch */
	O,

	/** Fach Portugiesisch, regulärer Beginn in der Einführungsphase */
	O0,

	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 11 */
	O1,

	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 5 */
	O5,

	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 6 */
	O6,

	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 7 */
	O7,

	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 8 */
	O8,

	/** Fach Portugiesisch, regulärer Beginn in Jahrgang 9 */
	O9,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Portugiesisch */
	OH,

	/** Fach Organisationslehre/Büroorganisation */
	OL,

	/** Fach Unterricht in der Herkunftssprache - Portugiesisch */
	OM,

	/** Fach Portugiesisch, außerhalb des regulären Fachunterrichts */
	OQ,

	/** Fach Orthodoxe Religionslehre */
	OR,

	/** Fach Pädagogik/ Erziehungswissenschaft */
	PA,

	/** Fach Physik */
	PH,

	/** Fach Politik */
	PK,

	/** Fach Philosophie */
	PL,

	/** Fach Unterricht in der Herkunftssprache - Polnisch */
	PM,

	/** Fach Praktische Philosophie */
	PP,

	/** Fach Psychologie */
	PS,

	/** Fach Fachübergreifender Unterricht */
	PU,

	/** Fach Personalwirtschaft und Soziologie/Politik */
	PW,

	/** Fach Projektkurs (mit einem oder zwei Leitfächern) */
	PX,

	/** Fach Politik/Ökonomische Grundbildung */
	POE,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Farsi */
	QH,

	/** Fach Unterricht in der Herkunftssprache - Farsi */
	QM,

	/** Fach Russisch */
	R,

	/** Fach Russisch, regulärer Beginn in der Einführungsphase */
	R0,

	/** Fach Russisch, regulärer Beginn in Jahrgang 11 */
	R1,

	/** Fach Russisch, regulärer Beginn in Jahrgang 5 */
	R5,

	/** Fach Russisch, regulärer Beginn in Jahrgang 6 */
	R6,

	/** Fach Russisch, regulärer Beginn in Jahrgang 7 */
	R7,

	/** Fach Russisch, regulärer Beginn in Jahrgang 8 */
	R8,

	/** Fach Russisch, regulärer Beginn in Jahrgang 9 */
	R9,

	/** Fach Rechnungswesen */
	RE,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch */
	RH,

	/** Fach Rechtskunde */
	RK,

	/** Fach Unterricht in der Herkunftssprache - Russisch */
	RM,

	/** Fach Russisch, außerhalb des regulären Fachunterrichts */
	RQ,

	/** Fach Recht und Verwaltung */
	RW,

	/** Fach Spanisch */
	S,

	/** Fach Spanisch, regulärer Beginn in der Einführungsphase */
	S0,

	/** Fach Spanisch, regulärer Beginn in Jahrgang 11 */
	S1,

	/** Fach Sport für Jungen */
	S3,

	/** Fach Sport für Mädchen */
	S4,

	/** Fach Spanisch, regulärer Beginn in Jahrgang 5 */
	S5,

	/** Fach Spanisch, regulärer Beginn in Jahrgang 6 */
	S6,

	/** Fach Spanisch, regulärer Beginn in Jahrgang 7 */
	S7,

	/** Fach Spanisch, regulärer Beginn in Jahrgang 8 */
	S8,

	/** Fach Spanisch, regulärer Beginn in Jahrgang 9 */
	S9,

	/** Fach Softwareentwicklung und -engineering */
	SE,

	/** Fach sonderpädag. Förderung für Schüler/-innen mit sonderpädag. Förderbedarf */
	SG,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Spanisch */
	SH,

	/** Fach Spiel */
	SI,

	/** Fach Soziologie */
	SL,

	/** Fach Unterricht in der Herkunftssprache - Spanisch */
	SM,

	/** Fach Sport */
	SP,

	/** Fach Spanisch, außerhalb des regulären Fachunterrichts */
	SQ,

	/** Fach Sonstige Fremdsprachen */
	SR,

	/** Fach Sachunterricht */
	SU,

	/** Fach Sozialwissenschaften */
	SW,

	/** Fach Sozialwissenschaften/Wirtschaft */
	SZ,

	/** Fach Türkisch */
	T,

	/** Fach Türkisch, regulärer Beginn in der Einführungsphase */
	T0,

	/** Fach Türkisch, regulärer Beginn in Jahrgang 11 */
	T1,

	/** Fach Türkisch, regulärer Beginn in Jahrgang 5 */
	T5,

	/** Fach Türkisch, regulärer Beginn in Jahrgang 6 */
	T6,

	/** Fach Türkisch, regulärer Beginn in Jahrgang 7 */
	T7,

	/** Fach Türkisch, regulärer Beginn in Jahrgang 8 */
	T8,

	/** Fach Türkisch, regulärer Beginn in Jahrgang 9 */
	T9,

	/** Fach Technik */
	TC,

	/** Fach Technische Grundbildung */
	TG,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Türkisch */
	TH,

	/** Fach Technische Informatik */
	TI,

	/** Fach Unterricht in der Herkunftssprache - Türkisch */
	TM,

	/** Fach Türkisch, außerhalb des regulären Fachunterrichts */
	TQ,

	/** Fach Textverarbeitung */
	TV,

	/** Fach Textilgestaltung */
	TX,

	/** Fach Technisches Zeichnen / Fachzeichnen */
	TZ,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Russisch */
	UH,

	/** Fach Unterricht in der Herkunftssprache - Rumänisch */
	UM,

	/** Fach fächerübergreifender Unterricht (Sprache/Sachunt./Mathematik/Förderunt.) */
	UU,

	/** Fach Unterweisung */
	UW,

	/** Fach Verwaltungskunde */
	VE,

	/** Fach Fächer im genehmigten Schulversuch und sonstige Fächer */
	VF,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Bulgarisch */
	VH,

	/** Fach Verkaufskunde */
	VK,

	/** Fach Unterricht in der Herkunftssprache - Bulgarisch */
	VM,

	/** Fach vokalpraktischer Grundkurs */
	VO,

	/** Fach Volkswirtschaftslehre/Politik */
	VP,

	/** Fach Verfahrenstechnik */
	VT,

	/** Fach Volkswirtschaftslehre */
	VW,

	/** Fach Vertiefungsfach */
	VX,

	/** Fach Werken */
	W,

	/** Fach Wirtschaftsgeographie */
	WG,

	/** Fach Wirtschaftsinformatik/Organisationslehre */
	WI,

	/** Fach Wirtschaft-Politik */
	WP,

	/** Fach Wirtschaftslehre */
	WW,

	/** Fach Wirtschaft und Arbeitswelt - Schwerpunkt Hauswirtschaft */
	WX,

	/** Fach Wirtschaft und Arbeitswelt - Schwerpunkt Technik */
	WY,

	/** Fach Wirtschaft und Arbeitswelt - Schwerpunkt Wirtschaft */
	WZ,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - sonstige Sprache */
	XH,

	/** Fach Unterricht in der Herkunftssprache - sonstige Sprache */
	XM,

	/** Fach Sonstige Fächer ohne Fremdsprachen (kein Import nach ASDPC) */
	XX,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Koreanisch */
	YH,

	/** Fach Unterricht in der Herkunftssprache - Koreanisch */
	YM,

	/** Fach orthodoxe Religionslehre (Syrisch) */
	YR,

	/** Fach Neugriechisch */
	Z,

	/** Fach Neugriechisch, regulärer Beginn in der Einführungsphase */
	Z0,

	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 11 */
	Z1,

	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 5 */
	Z5,

	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 6 */
	Z6,

	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 7 */
	Z7,

	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 8 */
	Z8,

	/** Fach Neugriechisch, regulärer Beginn in Jahrgang 9 */
	Z9,

	/** Fach Zusätzliche Förderung */
	ZF,

	/** Fach Herkunftssprache anstelle einer Pflichtfremdsprache - Kurdische Sprachen */
	ZH,

	/** Fach Unterricht in der Herkunftssprache - Kurdische Sprachen (Sorani, Komanci, Zaza) */
	ZM,

	/** Fach Neugriechisch, außerhalb des regulären Fachunterrichts */
	ZQ,

	/** Fach Zeichnen / Werken */
	ZW;


	/** Eine Liste mit allen atomaren Kürzeln von Fremdsprachen */
	private static final @NotNull HashMap<Integer, List<String>> _mapFremdsprachenKuerzelListe = new HashMap<>();

	/** Eine HashMap mit den zulässigen Fremdsprachen-Fächern. Der Zugriff erfolgt dabei über das atomare Kürzel des Faches. Sie enthält nur das Fach, wo das atomare Kürzel mit dem Statistik-Kürzel übereinstimmt. */
	private static final @NotNull HashMap<Integer, Map<String, Fach>> _mapFremdsprachenKuerzelAtomar = new HashMap<>();


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<FachKatalogEintrag, Fach> manager) {
		CoreTypeDataManager.putManager(Fach.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<FachKatalogEintrag, Fach> data() {
		return CoreTypeDataManager.getManager(Fach.class);
	}


	/**
	 * Gibt die Liste aller atomaren Kürzeln von Fremdsprachen-Fächern zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @param schuljahr		das Schuljahr, für das die Liste benötigt wird.
	 * @return die Liste aller atomaren Kürzeln von Fremdsprachen-Fächern
	 */
	public static @NotNull List<String> getListFremdsprachenKuerzelAtomar(final int schuljahr) {
		List<String> listFremdsprachen = _mapFremdsprachenKuerzelListe.get(schuljahr);
		if (listFremdsprachen == null) {
			listFremdsprachen = new ArrayList<>();
			_mapFremdsprachenKuerzelListe.put(schuljahr, listFremdsprachen);
		}
		if (listFremdsprachen.isEmpty()) {
			for (final @NotNull Fach f : Fach.values()) {
				final FachKatalogEintrag fke = f.daten(schuljahr);
				if (fke != null && fke.istFremdsprache && fke.schluessel.equals(fke.kuerzel))
					listFremdsprachen.add(fke.kuerzel);
			}
		}

		return listFremdsprachen;
	}


	/**
	 * Gibt eine Map von den atomaren Kürzeln der Fremdsprachen auf eine Liste der zugehörigen
	 * Sprach-Fächer zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @param schuljahr		das Schuljahr, für das die Liste benötigt wird.
	 * @return die Map von den atomaren Kürzeln der Fremdsprachen auf eine Liste der zugehörigen Sprach-Fächer
	 */
	public static @NotNull Map<String, Fach> getMapFremdsprachenKuerzelAtomar(final @NotNull Integer schuljahr) {
		Map<String, Fach> mapFremdsprachen = _mapFremdsprachenKuerzelAtomar.get(schuljahr);
		if (mapFremdsprachen == null) {
			mapFremdsprachen = new HashMap<>();
			_mapFremdsprachenKuerzelAtomar.put(schuljahr, mapFremdsprachen);
		}
		if (mapFremdsprachen.isEmpty()) {
			for (final Fach f : Fach.values()) {
				final FachKatalogEintrag fke = f.daten(schuljahr);
				if (fke != null && fke.istFremdsprache && fke.schluessel.equals(fke.kuerzel))
					mapFremdsprachen.put(fke.kuerzel, f);
			}
		}
		return mapFremdsprachen;
	}


	/**
	 * Gibt die Fachgruppe zurück, der das Fach in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Fachgruppe oder null, falls keine Zuordnung vorliegt
	 */
	public Fachgruppe getFachgruppe(final int schuljahr) {
		final FachKatalogEintrag fke = Fach.data().getEintragBySchuljahrUndWert(schuljahr, this);
		if ((fke == null) || (fke.fachgruppe == null))
			return null;
		return Fachgruppe.data().getWertByBezeichner(fke.fachgruppe);
	}


	/**
	 * Gibt den Jahrgang zurück, ab wann dieses Faches in dem angegebenen Schuljahr zulässig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Jahrgang
	 */
	public Jahrgaenge getJahrgangAb(final int schuljahr) {
		final FachKatalogEintrag fke = Fach.data().getEintragBySchuljahrUndWert(schuljahr, this);
		if ((fke == null) || (fke.abJahrgang == null))
			return null;
		return Jahrgaenge.data().getWertByBezeichner(fke.abJahrgang);
	}


	/**
	 * Gibt die Farbe des zulässigen Faches zurück.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Anfrage bezieht
	 *
	 * @return die Farbe des zulässigen Faches
	 */
	public @NotNull RGBFarbe getFarbe(final int schuljahr) {
		final Fachgruppe gruppe = getFachgruppe(schuljahr);
		if (gruppe == null)
			return new RGBFarbe();
		return gruppe.getFarbe(schuljahr);
	}


	/**
	 * Gibt die HTML-Farbe des zulässigen Faches als Aufruf der rgb-Funktion
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Anfrage bezieht
	 *
	 * @return die RGB-HTML-Farbdefinition als String
	 */
	public @NotNull String getHMTLFarbeRGB(final int schuljahr) {
		final @NotNull RGBFarbe farbe = getFarbe(schuljahr);
		return "rgb(" + farbe.red + "," + farbe.green + "," + farbe.blue + ")";
	}


	/**
	 * Gibt die HTML-Farbe des zulässigen Faches als Aufruf der rgba-Funktion
	 * mit der übergebenen Transparenz zurück.
	 *
	 * @param schuljahr   das Schuljahr, auf welches sich die Anfrage bezieht
	 * @param alpha   gibt die Deckkraft der Farbe an
	 *
	 * @return die RGBA-HTML-Farbdefinition als String
	 */
	public @NotNull String getHMTLFarbeRGBA(final int schuljahr, final double alpha) {
		final @NotNull RGBFarbe farbe = getFarbe(schuljahr);
		final double a = Math.clamp(alpha, 0.0, 1.0);
		return "rgba(" + farbe.red + "," + farbe.green + "," + farbe.blue + ", " + a + ")";
	}

}
