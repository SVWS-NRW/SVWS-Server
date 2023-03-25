package de.svws_nrw.core.types.lehrer;

import java.util.HashMap;

import de.svws_nrw.core.data.lehrer.LehrerKatalogLehrbefaehigungEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Lehrbefähigungen von Lehrkräften 
 * an der Schule zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerLehrbefaehigung {

	// GENERIERTER CODE ANFANG

	/** Lehrbefähigung 'Arbeitslehre / Schwerpunkt Haushaltslehre' */
	AH(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(174, "AH", "Arbeitslehre / Schwerpunkt Haushaltslehre", null, null)
	}),

	/** Lehrbefähigung 'Arbeitslehre' */
	AL(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(175, "AL", "Arbeitslehre", null, null)
	}),

	/** Lehrbefähigung 'Arabisch (Muttersprachl. Unterricht)' */
	AM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(176, "AM", "Arabisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Arbeitslehre / Schwerpunkt Technik' */
	AT(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(177, "AT", "Arbeitslehre / Schwerpunkt Technik", null, null)
	}),

	/** Lehrbefähigung 'Arbeitslehre / Wirtschaft' */
	AW(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(178, "AW", "Arbeitslehre / Wirtschaft", null, null)
	}),

	/** Lehrbefähigung 'Betreuung' */
	BE(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(179, "BE", "Betreuung", null, null)
	}),

	/** Lehrbefähigung 'Biologie' */
	BI(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(180, "BI", "Biologie", null, null)
	}),

	/** Lehrbefähigung 'Bosnisch (Muttersprachl. Unterricht)' */
	BM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(181, "BM", "Bosnisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Chinesisch' */
	C(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(182, "C", "Chinesisch", null, null)
	}),

	/** Lehrbefähigung 'Chemie' */
	CH(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(183, "CH", "Chemie", null, null)
	}),

	/** Lehrbefähigung 'Kroatisch (Muttersprachl. Unterricht)' */
	CM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(184, "CM", "Kroatisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Deutsch' */
	D(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(185, "D", "Deutsch", null, null)
	}),

	/** Lehrbefähigung 'Englisch' */
	E(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(186, "E", "Englisch", null, null)
	}),

	/** Lehrbefähigung 'Erdkunde' */
	EK(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(187, "EK", "Erdkunde", null, null)
	}),

	/** Lehrbefähigung 'Serbisch (Muttersprachl. Unterricht)' */
	EM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(188, "EM", "Serbisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Evangelische Religionslehre' */
	ER(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(189, "ER", "Evangelische Religionslehre", null, null)
	}),

	/** Lehrbefähigung 'Französisch' */
	F(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(190, "F", "Französisch", null, null)
	}),

	/** Lehrbefähigung 'Fachpraxis' */
	FP(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(191, "FP", "Fachpraxis", null, null)
	}),

	/** Lehrbefähigung 'Griechisch' */
	G(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(192, "G", "Griechisch", null, null)
	}),

	/** Lehrbefähigung 'Geschichte' */
	GE(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(193, "GE", "Geschichte", null, null)
	}),

	/** Lehrbefähigung 'Griechisch (Muttersprachl. Unterricht)' */
	GM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(194, "GM", "Griechisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Geschichte / Politische Bildung' */
	GP(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(195, "GP", "Geschichte / Politische Bildung", null, null)
	}),

	/** Lehrbefähigung 'Kunst/Gestalten (Kunst oder Textilgestaltung)' */
	GS(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(196, "GS", "Kunst/Gestalten (Kunst oder Textilgestaltung)", null, null)
	}),

	/** Lehrbefähigung 'Gesamtunterricht' */
	GU(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(197, "GU", "Gesamtunterricht", null, null)
	}),

	/** Lehrbefähigung 'Hebräisch' */
	H(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(198, "H", "Hebräisch", null, null)
	}),

	/** Lehrbefähigung 'Hauswirtschaft' */
	HA(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(199, "HA", "Hauswirtschaft", null, null)
	}),

	/** Lehrbefähigung 'Jüdische Religionslehre' */
	HR(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(200, "HR", "Jüdische Religionslehre", null, null)
	}),

	/** Lehrbefähigung 'Hauswirtschaft (-swissenschaft)' */
	HW(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(201, "HW", "Hauswirtschaft (-swissenschaft)", null, null)
	}),

	/** Lehrbefähigung 'Italienisch' */
	I(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(202, "I", "Italienisch", null, null)
	}),

	/** Lehrbefähigung 'Informatik (nachgewiesen durch 2. Staatsprüfung, sonst siehe IK)' */
	IF(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(203, "IF", "Informatik (nachgewiesen durch 2. Staatsprüfung, sonst siehe IK)", null, null)
	}),

	/** Lehrbefähigung 'Italienisch (Muttersprachl. Unterricht)' */
	IM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(204, "IM", "Italienisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Islamkunde' */
	IR(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(205, "IR", "Islamkunde", null, null)
	}),

	/** Lehrbefähigung 'Japanisch' */
	K(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(206, "K", "Japanisch", null, null)
	}),

	/** Lehrbefähigung 'Kunst/Gestalten' */
	KG(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(207, "KG", "Kunst/Gestalten", null, null)
	}),

	/** Lehrbefähigung 'Katholische Religionslehre' */
	KR(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(208, "KR", "Katholische Religionslehre", null, null)
	}),

	/** Lehrbefähigung 'Kurzschrift' */
	KS(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(209, "KS", "Kurzschrift", null, null)
	}),

	/** Lehrbefähigung 'Kunst / Werken' */
	KU(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(210, "KU", "Kunst / Werken", null, null)
	}),

	/** Lehrbefähigung 'Kunstwissenschaft' */
	KW(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(211, "KW", "Kunstwissenschaft", null, null)
	}),

	/** Lehrbefähigung 'Lateinisch' */
	L(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(212, "L", "Lateinisch", null, null)
	}),

	/** Lehrbefähigung 'Literaturwissenschaft' */
	LI(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(213, "LI", "Literaturwissenschaft", null, null)
	}),

	/** Lehrbefähigung 'Albanisch (Muttersprachl. Unterricht)' */
	LM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(214, "LM", "Albanisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Linguistik' */
	LN(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(215, "LN", "Linguistik", null, null)
	}),

	/** Lehrbefähigung 'Mathematik' */
	M(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(216, "M", "Mathematik", null, null)
	}),

	/** Lehrbefähigung 'Makedonisch (Muttersprachl. Unterricht)' */
	MM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(217, "MM", "Makedonisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Maschinenschreiben' */
	MS(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(218, "MS", "Maschinenschreiben", null, null)
	}),

	/** Lehrbefähigung 'Musik' */
	MU(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(219, "MU", "Musik", null, null)
	}),

	/** Lehrbefähigung 'Niederländisch' */
	N(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(220, "N", "Niederländisch", null, null)
	}),

	/** Lehrbefähigung 'Portugiesisch' */
	O(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(221, "O", "Portugiesisch", null, null)
	}),

	/** Lehrbefähigung 'Ohne Angabe' */
	OA(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(222, "OA", "Ohne Angabe", null, null)
	}),

	/** Lehrbefähigung 'Portugiesisch (Muttersprachl. Unterricht)' */
	OM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(223, "OM", "Portugiesisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Griechisch-orthodoxe Religionslehre' */
	OR(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(224, "OR", "Griechisch-orthodoxe Religionslehre", null, null)
	}),

	/** Lehrbefähigung 'Pädagogik' */
	PA(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(225, "PA", "Pädagogik", null, null)
	}),

	/** Lehrbefähigung 'Physik (Astronomie)' */
	PH(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(226, "PH", "Physik (Astronomie)", null, null)
	}),

	/** Lehrbefähigung 'Philosophie/Praktische Philosophie' */
	PI(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(227, "PI", "Philosophie/Praktische Philosophie", null, null)
	}),

	/** Lehrbefähigung 'Politik' */
	PK(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(228, "PK", "Politik", null, null)
	}),

	/** Lehrbefähigung 'Philosophie' */
	PL(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(229, "PL", "Philosophie", null, null)
	}),

	/** Lehrbefähigung 'Polnisch (Muttersprachl. Unterricht)' */
	PM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(230, "PM", "Polnisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Praktische Philosophie' */
	PP(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(231, "PP", "Praktische Philosophie", null, null)
	}),

	/** Lehrbefähigung 'Psychologie' */
	PS(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(232, "PS", "Psychologie", null, null)
	}),

	/** Lehrbefähigung 'Russisch' */
	R(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(233, "R", "Russisch", null, null)
	}),

	/** Lehrbefähigung 'Russisch (Muttersprachl. Unterricht)' */
	RM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(234, "RM", "Russisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Rechtswissenschaft' */
	RW(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(235, "RW", "Rechtswissenschaft", null, null)
	}),

	/** Lehrbefähigung 'Spanisch' */
	S(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(236, "S", "Spanisch", null, null)
	}),

	/** Lehrbefähigung 'Sozial- und Erziehungswissenschaft' */
	SE(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(237, "SE", "Sozial- und Erziehungswissenschaft", null, null)
	}),

	/** Lehrbefähigung 'Sozialpflege' */
	SF(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(238, "SF", "Sozialpflege", null, null)
	}),

	/** Lehrbefähigung 'Spanisch (Muttersprachl. Unterricht)' */
	SM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(239, "SM", "Spanisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Sonderpädagogik' */
	SN(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(240, "SN", "Sonderpädagogik", null, null)
	}),

	/** Lehrbefähigung 'Sozialpädagogik' */
	SO(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(241, "SO", "Sozialpädagogik", null, null)
	}),

	/** Lehrbefähigung 'Sport' */
	SP(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(242, "SP", "Sport", null, null)
	}),

	/** Lehrbefähigung 'sonstige Sprachen' */
	SR(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(243, "SR", "sonstige Sprachen", null, null)
	}),

	/** Lehrbefähigung 'Sachunterricht' */
	SU(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(244, "SU", "Sachunterricht", null, null)
	}),

	/** Lehrbefähigung 'Sozialwissenschaften' */
	SW(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(245, "SW", "Sozialwissenschaften", null, null)
	}),

	/** Lehrbefähigung 'Türkisch' */
	T(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(246, "T", "Türkisch", null, null)
	}),

	/** Lehrbefähigung 'Technik' */
	TC(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(247, "TC", "Technik", null, null)
	}),

	/** Lehrbefähigung 'Technologie' */
	TE(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(248, "TE", "Technologie", null, null)
	}),

	/** Lehrbefähigung 'Türkisch (Muttersprachl. Unterricht)' */
	TM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(249, "TM", "Türkisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Textilgestaltung' */
	TX(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(250, "TX", "Textilgestaltung", null, null)
	}),

	/** Lehrbefähigung 'Unterweisung' */
	UW(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(251, "UW", "Unterweisung", null, null)
	}),

	/** Lehrbefähigung 'Werken (Musisches)' */
	W(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(252, "W", "Werken (Musisches)", null, null)
	}),

	/** Lehrbefähigung 'Slowenisch (Muttersprachl. Unterricht)' */
	WM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(253, "WM", "Slowenisch (Muttersprachl. Unterricht)", null, null)
	}),

	/** Lehrbefähigung 'Wirtschaftslehre/Politik' */
	WP(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(254, "WP", "Wirtschaftslehre/Politik", null, null)
	}),

	/** Lehrbefähigung 'Technisches Werken' */
	WT(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(255, "WT", "Technisches Werken", null, null)
	}),

	/** Lehrbefähigung 'Wirtschafts- und Arbeitslehre' */
	WW(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(256, "WW", "Wirtschafts- und Arbeitslehre", null, null)
	}),

	/** Lehrbefähigung 'sonstige Muttersprache' */
	XM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(257, "XM", "sonstige Muttersprache", null, null)
	}),

	/** Lehrbefähigung 'Syrisch-orthodoxe Religionslehre' */
	YR(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(258, "YR", "Syrisch-orthodoxe Religionslehre", null, null)
	}),

	/** Lehrbefähigung 'Neugriechisch' */
	Z(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(259, "Z", "Neugriechisch", null, null)
	}),

	/** Lehrbefähigung 'Alevitische Religionslehre nach den Grundsätzen des AABF' */
	AR(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(260, "AR", "Alevitische Religionslehre nach den Grundsätzen des AABF", null, null)
	}),

	/** Lehrbefähigung 'Ästhetische Erziehung' */
	AE(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(261, "AE", "Ästhetische Erziehung", null, null)
	}),

	/** Lehrbefähigung 'Deutsch für Schülerinnen und Schüler mit Zuwanderungsgeschichte' */
	DZ(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(262, "DZ", "Deutsch für Schülerinnen und Schüler mit Zuwanderungsgeschichte", null, null)
	}),

	/** Lehrbefähigung 'Mathematische Grundbildung' */
	MG(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(263, "MG", "Mathematische Grundbildung", null, null)
	}),

	/** Lehrbefähigung 'Natur- und Gesellschaftswissenschaften' */
	NG(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(264, "NG", "Natur- und Gesellschaftswissenschaften", null, null)
	}),

	/** Lehrbefähigung 'Sprachliche Grundbildung' */
	SB(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(265, "SB", "Sprachliche Grundbildung", null, null)
	}),

	/** Lehrbefähigung 'Religionslehre der mennonitischen Brüdergemeinden in RW (Lehrerlaubnis)' */
	MB(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(266, "MB", "Religionslehre der mennonitischen Brüdergemeinden in RW (Lehrerlaubnis)", null, null)
	}),

	/** Lehrbefähigung 'Bulgarisch (Herkunftssprache)' */
	VM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(267, "VM", "Bulgarisch (Herkunftssprache)", null, null)
	}),

	/** Lehrbefähigung 'Farsi (Herkunftssprache)' */
	QM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(268, "QM", "Farsi (Herkunftssprache)", null, null)
	}),

	/** Lehrbefähigung 'Koreanisch (Herkunftssprache)' */
	YM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(269, "YM", "Koreanisch (Herkunftssprache)", null, null)
	}),

	/** Lehrbefähigung 'Kurdische Sprachen (Herkunftssprache) (Sorani, Komaci, Zaza)' */
	ZM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(270, "ZM", "Kurdische Sprachen (Herkunftssprache) (Sorani, Komaci, Zaza)", null, null)
	}),

	/** Lehrbefähigung 'Niederländisch (Herkunftssprache)' */
	NM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(271, "NM", "Niederländisch (Herkunftssprache)", null, null)
	}),

	/** Lehrbefähigung 'Rumänisch (Herkunftssprache)' */
	UM(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(272, "UM", "Rumänisch (Herkunftssprache)", null, null)
	}),

	/** Lehrbefähigung 'Islamische Religionslehre' */
	IL(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(273, "IL", "Islamische Religionslehre", null, null)
	}),

	/** Lehrbefähigung 'Arabisch' */
	A(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(274, "A", "Arabisch", null, null)
	}),

	/** Lehrbefähigung 'Braille´sche Punktschrift' */
	BN(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(275, "BN", "Braille´sche Punktschrift", null, null)
	}),

	/** Lehrbefähigung 'Darstellen und Gestalten' */
	DS(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(276, "DS", "Darstellen und Gestalten", null, null)
	}),

	/** Lehrbefähigung 'Hauswirtschaft (Konsum/Ernährung/Gesundheit)' */
	EL(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(277, "EL", "Hauswirtschaft (Konsum/Ernährung/Gesundheit)", null, null)
	}),

	/** Lehrbefähigung 'Design/Fotografie' */
	DF(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(278, "DF", "Design/Fotografie", null, null)
	}),

	/** Lehrbefähigung 'Gesellschaftswissenschaften' */
	GW(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(279, "GW", "Gesellschaftswissenschaften", null, null)
	}),

	/** Lehrbefähigung 'Malerei/Grafik/Gestaltung' */
	MJ(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(280, "MJ", "Malerei/Grafik/Gestaltung", null, null)
	}),

	/** Lehrbefähigung 'Naturwissenschaften' */
	NW(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(281, "NW", "Naturwissenschaften", null, null)
	}),

	/** Lehrbefähigung 'Sozialwesen' */
	SI(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(282, "SI", "Sozialwesen", null, null)
	}),

	/** Lehrbefähigung 'Zusatzqualifikation Bilinguales Lernen' */
	ZB(new LehrerKatalogLehrbefaehigungEintrag[]{
		new LehrerKatalogLehrbefaehigungEintrag(283, "ZB", "Zusatzqualifikation Bilinguales Lernen", null, null)
	});



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Lehrbefähigungen, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogLehrbefaehigungEintrag daten;
	
	/** Die Historie mit den Einträgen der Lehrbefähigungen */
	public final @NotNull LehrerKatalogLehrbefaehigungEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Lehrbefähigungen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerLehrbefaehigung> _lehrbefaehigungenByID = new HashMap<>();

	/** Eine Hashmap mit allen Lehrbefähigungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerLehrbefaehigung> _lehrbefaehigungenByKuerzel = new HashMap<>();


	/**
	 * Erzeugt eine neue Lehrbefähigung in der Aufzählung.
	 * 
	 * @param historie   die Historie der Lehrbefähigungen, welches ein Array von {@link LehrerKatalogLehrbefaehigungEintrag} ist  
	 */
	private LehrerLehrbefaehigung(final @NotNull LehrerKatalogLehrbefaehigungEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerLehrbefaehigung> getMapLehrbefaehigungByID() {
		if (_lehrbefaehigungenByID.size() == 0)
			for (final LehrerLehrbefaehigung l : LehrerLehrbefaehigung.values())
				_lehrbefaehigungenByID.put(l.daten.id, l);				
		return _lehrbefaehigungenByID;
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Lehrbefähigungen auf die zugehörigen Lehrbefähigungen
	 */
	private static @NotNull HashMap<@NotNull String, LehrerLehrbefaehigung> getMapLehrbefaehigungByKuerzel() {
		if (_lehrbefaehigungenByKuerzel.size() == 0)
			for (final LehrerLehrbefaehigung l : LehrerLehrbefaehigung.values())
				_lehrbefaehigungenByKuerzel.put(l.daten.kuerzel, l);				
		return _lehrbefaehigungenByKuerzel;
	}
	

	/**
	 * Gibt die Lehrbefähigung anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Lehrbefähigung
	 * 
	 * @return die Lehrbefähigung oder null, falls die IF ungültig ist
	 */
	public static LehrerLehrbefaehigung getByID(final long id) {
		return getMapLehrbefaehigungByID().get(id);
	}


	/**
	 * Gibt die Lehrbefähigung anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Lehrbefähigung
	 * 
	 * @return die Lehrbefähigung oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerLehrbefaehigung getByKuerzel(final String kuerzel) {
		return getMapLehrbefaehigungByKuerzel().get(kuerzel);
	}

}
