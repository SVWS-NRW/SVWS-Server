package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Lehrbefähigungen von Lehrkräften
 * an der Schule zur Verfügung.
 */
public enum LehrerLehrbefaehigung implements @NotNull CoreType<LehrerLehrbefaehigungKatalogEintrag, LehrerLehrbefaehigung> {

	/** Lehrbefähigung 'Arbeitslehre / Schwerpunkt Haushaltslehre' */
	AH,

	/** Lehrbefähigung 'Arbeitslehre' */
	AL,

	/** Lehrbefähigung 'Arabisch (Muttersprachl. Unterricht)' */
	AM,

	/** Lehrbefähigung 'Arbeitslehre / Schwerpunkt Technik' */
	AT,

	/** Lehrbefähigung 'Arbeitslehre / Wirtschaft' */
	AW,

	/** Lehrbefähigung 'Betreuung' */
	BE,

	/** Lehrbefähigung 'Biologie' */
	BI,

	/** Lehrbefähigung 'Bosnisch (Muttersprachl. Unterricht)' */
	BM,

	/** Lehrbefähigung 'Chinesisch' */
	C,

	/** Lehrbefähigung 'Chemie' */
	CH,

	/** Lehrbefähigung 'Kroatisch (Muttersprachl. Unterricht)' */
	CM,

	/** Lehrbefähigung 'Deutsch' */
	D,

	/** Lehrbefähigung 'Englisch' */
	E,

	/** Lehrbefähigung 'Erdkunde' */
	EK,

	/** Lehrbefähigung 'Serbisch (Muttersprachl. Unterricht)' */
	EM,

	/** Lehrbefähigung 'Evangelische Religionslehre' */
	ER,

	/** Lehrbefähigung 'Französisch' */
	F,

	/** Lehrbefähigung 'Fachpraxis' */
	FP,

	/** Lehrbefähigung 'Griechisch' */
	G,

	/** Lehrbefähigung 'Geschichte' */
	GE,

	/** Lehrbefähigung 'Griechisch (Muttersprachl. Unterricht)' */
	GM,

	/** Lehrbefähigung 'Geschichte / Politische Bildung' */
	GP,

	/** Lehrbefähigung 'Kunst/Gestalten (Kunst oder Textilgestaltung)' */
	GS,

	/** Lehrbefähigung 'Gesamtunterricht' */
	GU,

	/** Lehrbefähigung 'Hebräisch' */
	H,

	/** Lehrbefähigung 'Hauswirtschaft' */
	HA,

	/** Lehrbefähigung 'Jüdische Religionslehre' */
	HR,

	/** Lehrbefähigung 'Hauswirtschaft (-swissenschaft)' */
	HW,

	/** Lehrbefähigung 'Italienisch' */
	I,

	/** Lehrbefähigung 'Informatik (nachgewiesen durch 2. Staatsprüfung, sonst siehe IK)' */
	IF,

	/** Lehrbefähigung 'Italienisch (Muttersprachl. Unterricht)' */
	IM,

	/** Lehrbefähigung 'Islamkunde' */
	IR,

	/** Lehrbefähigung 'Japanisch' */
	K,

	/** Lehrbefähigung 'Kunst/Gestalten' */
	KG,

	/** Lehrbefähigung 'Katholische Religionslehre' */
	KR,

	/** Lehrbefähigung 'Kurzschrift' */
	KS,

	/** Lehrbefähigung 'Kunst / Werken' */
	KU,

	/** Lehrbefähigung 'Kunstwissenschaft' */
	KW,

	/** Lehrbefähigung 'Lateinisch' */
	L,

	/** Lehrbefähigung 'Literaturwissenschaft' */
	LI,

	/** Lehrbefähigung 'Albanisch (Muttersprachl. Unterricht)' */
	LM,

	/** Lehrbefähigung 'Linguistik' */
	LN,

	/** Lehrbefähigung 'Mathematik' */
	M,

	/** Lehrbefähigung 'Makedonisch (Muttersprachl. Unterricht)' */
	MM,

	/** Lehrbefähigung 'Maschinenschreiben' */
	MS,

	/** Lehrbefähigung 'Musik' */
	MU,

	/** Lehrbefähigung 'Niederländisch' */
	N,

	/** Lehrbefähigung 'Portugiesisch' */
	O,

	/** Lehrbefähigung 'Ohne Angabe' */
	OA,

	/** Lehrbefähigung 'Portugiesisch (Muttersprachl. Unterricht)' */
	OM,

	/** Lehrbefähigung 'Griechisch-orthodoxe Religionslehre' */
	OR,

	/** Lehrbefähigung 'Pädagogik' */
	PA,

	/** Lehrbefähigung 'Physik (Astronomie)' */
	PH,

	/** Lehrbefähigung 'Philosophie/Praktische Philosophie' */
	PI,

	/** Lehrbefähigung 'Politik' */
	PK,

	/** Lehrbefähigung 'Philosophie' */
	PL,

	/** Lehrbefähigung 'Polnisch (Muttersprachl. Unterricht)' */
	PM,

	/** Lehrbefähigung 'Praktische Philosophie' */
	PP,

	/** Lehrbefähigung 'Psychologie' */
	PS,

	/** Lehrbefähigung 'Russisch' */
	R,

	/** Lehrbefähigung 'Russisch (Muttersprachl. Unterricht)' */
	RM,

	/** Lehrbefähigung 'Rechtswissenschaft' */
	RW,

	/** Lehrbefähigung 'Spanisch' */
	S,

	/** Lehrbefähigung 'Sozial- und Erziehungswissenschaft' */
	SE,

	/** Lehrbefähigung 'Sozialpflege' */
	SF,

	/** Lehrbefähigung 'Spanisch (Muttersprachl. Unterricht)' */
	SM,

	/** Lehrbefähigung 'Sonderpädagogik' */
	SN,

	/** Lehrbefähigung 'Sozialpädagogik' */
	SO,

	/** Lehrbefähigung 'Sport' */
	SP,

	/** Lehrbefähigung 'sonstige Sprachen' */
	SR,

	/** Lehrbefähigung 'Sachunterricht' */
	SU,

	/** Lehrbefähigung 'Sozialwissenschaften' */
	SW,

	/** Lehrbefähigung 'Türkisch' */
	T,

	/** Lehrbefähigung 'Technik' */
	TC,

	/** Lehrbefähigung 'Technologie' */
	TE,

	/** Lehrbefähigung 'Türkisch (Muttersprachl. Unterricht)' */
	TM,

	/** Lehrbefähigung 'Textilgestaltung' */
	TX,

	/** Lehrbefähigung 'Unterweisung' */
	UW,

	/** Lehrbefähigung 'Werken (Musisches)' */
	W,

	/** Lehrbefähigung 'Slowenisch (Muttersprachl. Unterricht)' */
	WM,

	/** Lehrbefähigung 'Wirtschaftslehre/Politik' */
	WP,

	/** Lehrbefähigung 'Technisches Werken' */
	WT,

	/** Lehrbefähigung 'Wirtschafts- und Arbeitslehre' */
	WW,

	/** Lehrbefähigung 'sonstige Muttersprache' */
	XM,

	/** Lehrbefähigung 'Syrisch-orthodoxe Religionslehre' */
	YR,

	/** Lehrbefähigung 'Neugriechisch' */
	Z,

	/** Lehrbefähigung 'Alevitische Religionslehre nach den Grundsätzen des AABF' */
	AR,

	/** Lehrbefähigung 'Ästhetische Erziehung' */
	AE,

	/** Lehrbefähigung 'Deutsch für Schülerinnen und Schüler mit Zuwanderungsgeschichte' */
	DZ,

	/** Lehrbefähigung 'Mathematische Grundbildung' */
	MG,

	/** Lehrbefähigung 'Natur- und Gesellschaftswissenschaften' */
	NG,

	/** Lehrbefähigung 'Sprachliche Grundbildung' */
	SB,

	/** Lehrbefähigung 'Religionslehre der mennonitischen Brüdergemeinden in RW (Lehrerlaubnis)' */
	MB,

	/** Lehrbefähigung 'Bulgarisch (Herkunftssprache)' */
	VM,

	/** Lehrbefähigung 'Farsi (Herkunftssprache)' */
	QM,

	/** Lehrbefähigung 'Koreanisch (Herkunftssprache)' */
	YM,

	/** Lehrbefähigung 'Kurdische Sprachen (Herkunftssprache) (Sorani, Komaci, Zaza)' */
	ZM,

	/** Lehrbefähigung 'Niederländisch (Herkunftssprache)' */
	NM,

	/** Lehrbefähigung 'Rumänisch (Herkunftssprache)' */
	UM,

	/** Lehrbefähigung 'Islamische Religionslehre' */
	IL,

	/** Lehrbefähigung 'Arabisch' */
	A,

	/** Lehrbefähigung 'Braille´sche Punktschrift' */
	BN,

	/** Lehrbefähigung 'Darstellen und Gestalten' */
	DS,

	/** Lehrbefähigung 'Hauswirtschaft (Konsum/Ernährung/Gesundheit)' */
	EL,

	/** Lehrbefähigung 'Design/Fotografie' */
	DF,

	/** Lehrbefähigung 'Gesellschaftswissenschaften' */
	GW,

	/** Lehrbefähigung 'Malerei/Grafik/Gestaltung' */
	MJ,

	/** Lehrbefähigung 'Naturwissenschaften' */
	NW,

	/** Lehrbefähigung 'Sozialwesen' */
	SI,

	/** Lehrbefähigung 'Zusatzqualifikation Bilinguales Lernen' */
	ZB;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerLehrbefaehigungKatalogEintrag, LehrerLehrbefaehigung> manager) {
		CoreTypeDataManager.putManager(LehrerLehrbefaehigung.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerLehrbefaehigungKatalogEintrag, LehrerLehrbefaehigung> data() {
		return CoreTypeDataManager.getManager(LehrerLehrbefaehigung.class);
	}

}
