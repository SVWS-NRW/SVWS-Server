package de.nrw.schule.svws.core.types.lehrer;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogFachrichtungEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Fachrichtungen von Lehrkräften 
 * an der Schule zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerFachrichtung {

	/** Fachrichtung 'Kraftfahrzeugtechnik' */
	ID_03(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(78, "03", "Kraftfahrzeugtechnik", null, null)
	}),

	/** Fachrichtung 'Metalltechnik, Maschinenbau (außer Kfz), Verfahrens-, Fertigungstechnik' */
	ID_04(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(79, "04", "Metalltechnik, Maschinenbau (außer Kfz), Verfahrens-, Fertigungstechnik", null, null)
	}),

	/** Fachrichtung 'Sanitär-, Heizungs-, Klima-, Lüftungstechnik' */
	ID_05(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(80, "05", "Sanitär-, Heizungs-, Klima-, Lüftungstechnik", null, null)
	}),

	/** Fachrichtung 'Bergtechnik, Bergbau' */
	ID_08(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(81, "08", "Bergtechnik, Bergbau", null, null)
	}),

	/** Fachrichtung 'Hütten-, Gießereiwesen' */
	ID_09(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(82, "09", "Hütten-, Gießereiwesen", null, null)
	}),

	/** Fachrichtung 'Elektrotechnik: Energietechnik' */
	ID_11(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(83, "11", "Elektrotechnik: Energietechnik", null, null)
	}),

	/** Fachrichtung 'Elektrotechnik: Nachrichtentechnik' */
	ID_12(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(84, "12", "Elektrotechnik: Nachrichtentechnik", null, null)
	}),

	/** Fachrichtung 'Bautechnik, Bauingenieurwesen' */
	ID_21(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(85, "21", "Bautechnik, Bauingenieurwesen", null, null)
	}),

	/** Fachrichtung 'Holztechnik' */
	ID_22(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(86, "22", "Holztechnik", null, null)
	}),

	/** Fachrichtung 'Architektur' */
	ID_23(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(87, "23", "Architektur", null, null)
	}),

	/** Fachrichtung 'Textil-, Bekleidungstechnik, Textilgestaltung' */
	ID_31(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(88, "31", "Textil-, Bekleidungstechnik, Textilgestaltung", null, null)
	}),

	/** Fachrichtung 'Informatik' */
	ID_35(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(89, "35", "Informatik", null, null)
	}),

	/** Fachrichtung 'Chemie- / Verfahrens- / Chemotechnik' */
	ID_41(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(90, "41", "Chemie- / Verfahrens- / Chemotechnik", null, null)
	}),

	/** Fachrichtung 'Lebensmittelchemie' */
	ID_42(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(91, "42", "Lebensmittelchemie", null, null)
	}),

	/** Fachrichtung 'Farb- u. Raumgestaltung, Gestaltungstechnik, Malerei, Graphik, Design, Fotografie, Glas- und Keramiktechnik' */
	ID_55(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(92, "55", "Farb- u. Raumgestaltung, Gestaltungstechnik, Malerei, Graphik, Design, Fotografie, Glas- und Keramiktechnik", null, null)
	}),

	/** Fachrichtung 'Wirtschaftswissenschaft in kaufmännischen Berufen' */
	ID_60(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(93, "60", "Wirtschaftswissenschaft in kaufmännischen Berufen", null, null)
	}),

	/** Fachrichtung 'Hauswirtschaftswissenschaft' */
	ID_65(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(94, "65", "Hauswirtschaftswissenschaft", null, null)
	}),

	/** Fachrichtung 'Ernährungswissenschaft' */
	ID_66(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(95, "66", "Ernährungswissenschaft", null, null)
	}),

	/** Fachrichtung 'Sozialwesen, Sozialpädagogik, Sozialpflege, Sozialwissenschaft' */
	ID_70(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(96, "70", "Sozialwesen, Sozialpädagogik, Sozialpflege, Sozialwissenschaft", null, null)
	}),

	/** Fachrichtung 'Psychologie' */
	ID_71(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(97, "71", "Psychologie", null, null)
	}),

	/** Fachrichtung 'Körperpflege / Biotechnik' */
	ID_80(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(98, "80", "Körperpflege / Biotechnik", null, null)
	}),

	/** Fachrichtung 'Landbauwissenschaft, Agrarwissenschaft, Agrarwirtschaft' */
	ID_91(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(99, "91", "Landbauwissenschaft, Agrarwissenschaft, Agrarwirtschaft", null, null)
	}),

	/** Fachrichtung 'Gartenbauwissenschaft' */
	ID_92(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(100, "92", "Gartenbauwissenschaft", null, null)
	}),

	/** Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Blinden' */
	ID_BL(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(101, "BL", "Fachrichtung für Sondererziehung und Rehabilitation der Blinden", null, null)
	}),

	/** Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Erziehungsschwierigen' */
	ID_EZ(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(102, "EZ", "Fachrichtung für Sondererziehung und Rehabilitation der Erziehungsschwierigen", null, null)
	}),

	/** Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Geistigbehinderten' */
	ID_GB(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(103, "GB", "Fachrichtung für Sondererziehung und Rehabilitation der Geistigbehinderten", null, null)
	}),

	/** Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Gehörlosen' */
	ID_GH(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(104, "GH", "Fachrichtung für Sondererziehung und Rehabilitation der Gehörlosen", null, null)
	}),

	/** Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Körperbehinderten' */
	ID_KB(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(105, "KB", "Fachrichtung für Sondererziehung und Rehabilitation der Körperbehinderten", null, null)
	}),

	/** Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Lernbehinderten' */
	ID_LB(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(106, "LB", "Fachrichtung für Sondererziehung und Rehabilitation der Lernbehinderten", null, null)
	}),

	/** Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Sprachbehinderten' */
	ID_SB(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(107, "SB", "Fachrichtung für Sondererziehung und Rehabilitation der Sprachbehinderten", null, null)
	}),

	/** Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Schwerhörigen' */
	ID_SG(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(108, "SG", "Fachrichtung für Sondererziehung und Rehabilitation der Schwerhörigen", null, null)
	}),

	/** Fachrichtung 'Fachrichtung für Sondererziehung und Rehabilitation der Sehbehinderten' */
	ID_SH(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(109, "SH", "Fachrichtung für Sondererziehung und Rehabilitation der Sehbehinderten", null, null)
	}),

	/** Fachrichtung 'Drucktechnik, Reproduktionstechnik' */
	ID_51(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(110, "51", "Drucktechnik, Reproduktionstechnik", null, null)
	}),

	/** Fachrichtung 'Fertigungstechnik (spez. Fachrichtung)' */
	ID_FT(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(111, "FT", "Fertigungstechnik (spez. Fachrichtung)", null, null)
	}),

	/** Fachrichtung 'Fahrzeugtechnik (spez. Fachrichtung)' */
	ID_KT(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(112, "KT", "Fahrzeugtechnik (spez. Fachrichtung)", null, null)
	}),

	/** Fachrichtung 'Versorgungstechnik (spez. Fachrichtung)' */
	ID_VT(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(113, "VT", "Versorgungstechnik (spez. Fachrichtung)", null, null)
	}),

	/** Fachrichtung 'Technische Informatik (spez. Fachrichtung)' */
	ID_TI(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(114, "TI", "Technische Informatik (spez. Fachrichtung)", null, null)
	}),

	/** Fachrichtung 'Energietechnik (spez. Fachrichtung)' */
	ID_ET(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(115, "ET", "Energietechnik (spez. Fachrichtung)", null, null)
	}),

	/** Fachrichtung 'Nachrichtentechnik (spez. Fachrichtung)' */
	ID_NT(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(116, "NT", "Nachrichtentechnik (spez. Fachrichtung)", null, null)
	}),

	/** Fachrichtung 'Hochbau (spez. Fachrichtung)' */
	ID_HC(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(117, "HC", "Hochbau (spez. Fachrichtung)", null, null)
	}),

	/** Fachrichtung 'Holztechnik (spez. Fachrichtung)' */
	ID_HT(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(118, "HT", "Holztechnik (spez. Fachrichtung)", null, null)
	}),

	/** Fachrichtung 'Tiefbau (spez. Fachrichtung)' */
	ID_TB(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(119, "TB", "Tiefbau (spez. Fachrichtung)", null, null)
	}),

	/** Fachrichtung 'Banken, Bankbetriebslehre / Finanzwirtschaft' */
	ID_BK(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(120, "BK", "Banken, Bankbetriebslehre / Finanzwirtschaft", null, null)
	}),

	/** Fachrichtung 'Handel, Handelsbetriebslehre / Absatzwirtschaft' */
	ID_HB(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(121, "HB", "Handel, Handelsbetriebslehre / Absatzwirtschaft", null, null)
	}),

	/** Fachrichtung 'Industrie, Industriebetriebslehre / Produktionswirtschaft' */
	ID_IB(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(122, "IB", "Industrie, Industriebetriebslehre / Produktionswirtschaft", null, null)
	}),

	/** Fachrichtung 'Verkehr' */
	ID_VR(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(123, "VR", "Verkehr", null, null)
	}),

	/** Fachrichtung 'Versicherung' */
	ID_VL(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(124, "VL", "Versicherung", null, null)
	}),

	/** Fachrichtung 'Organisation und Bürokommunikation / Datenverarbeitung / Organisationslehre / Betriebsinformatik' */
	ID_OL(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(125, "OL", "Organisation und Bürokommunikation / Datenverarbeitung / Organisationslehre / Betriebsinformatik", null, null)
	}),

	/** Fachrichtung 'Betriebswirtschaftliche Steuerlehre / Revisions- und Treuhandwesen' */
	ID_ST(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(126, "ST", "Betriebswirtschaftliche Steuerlehre / Revisions- und Treuhandwesen", null, null)
	}),

	/** Fachrichtung 'Wirtschaftliche Warenlehre' */
	ID_WL(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(127, "WL", "Wirtschaftliche Warenlehre", null, null)
	}),

	/** Fachrichtung 'Wirtschaftsgeographie' */
	ID_WG(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(128, "WG", "Wirtschaftsgeographie", null, null)
	}),

	/** Fachrichtung 'Wirtschaftsinformatik' */
	ID_WI(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(129, "WI", "Wirtschaftsinformatik", null, null)
	}),

	/** Fachrichtung 'Lebensmitteltechnologie (spez. Fachrichtung)' */
	ID_LT(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(130, "LT", "Lebensmitteltechnologie (spez. Fachrichtung)", null, null)
	}),

	/** Fachrichtung 'Sonstige Fachrichtung' */
	ID_99(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(131, "99", "Sonstige Fachrichtung", null, null)
	}),

	/** Fachrichtung 'Absatz und Marketing' */
	ID_AS(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(132, "AS", "Absatz und Marketing", null, null)
	}),

	/** Fachrichtung 'Betriebswirtschaftliche Finanzierungslehre' */
	ID_BS(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(133, "BS", "Betriebswirtschaftliche Finanzierungslehre", null, null)
	}),

	/** Fachrichtung 'Unternehmensrechnung' */
	ID_UR(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(134, "UR", "Unternehmensrechnung", null, null)
	}),

	/** Fachrichtung 'Gesundheit' */
	ID_81(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(135, "81", "Gesundheit", null, null)
	}),

	/** Fachrichtung 'Maschinenbautechnik' */
	ID_02(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(136, "02", "Maschinenbautechnik", null, null)
	}),

	/** Fachrichtung 'Fertigungstechnik' */
	ID_06(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(137, "06", "Fertigungstechnik", null, null)
	}),

	/** Fachrichtung 'Versorgungstechnik' */
	ID_07(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(138, "07", "Versorgungstechnik", null, null)
	}),

	/** Fachrichtung 'Energietechnik' */
	ID_13(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(139, "13", "Energietechnik", null, null)
	}),

	/** Fachrichtung 'Hochbautechnik' */
	ID_24(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(140, "24", "Hochbautechnik", null, null)
	}),

	/** Fachrichtung 'Tiefbautechnik' */
	ID_25(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(141, "25", "Tiefbautechnik", null, null)
	}),

	/** Fachrichtung 'Technische Informatik' */
	ID_36(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(142, "36", "Technische Informatik", null, null)
	}),

	/** Fachrichtung 'Lebensmitteltechnologie' */
	ID_67(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(143, "67", "Lebensmitteltechnologie", null, null)
	}),

	/** Fachrichtung 'Bankbetriebslehre' */
	ID_BB(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(144, "BB", "Bankbetriebslehre", null, null)
	}),

	/** Fachrichtung 'Betriebswirtschaftliche Steuerlehre' */
	ID_BW(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(145, "BW", "Betriebswirtschaftliche Steuerlehre", null, null)
	}),

	/** Fachrichtung 'Personalwirtschaft' */
	ID_PW(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(146, "PW", "Personalwirtschaft", null, null)
	}),

	/** Fachrichtung 'Versicherungsbetriebslehre' */
	ID_VB(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(147, "VB", "Versicherungsbetriebslehre", null, null)
	}),

	/** Fachrichtung 'Emotionale und soziale Entwicklung' */
	ID_ES(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(148, "ES", "Emotionale und soziale Entwicklung", null, null)
	}),

	/** Fachrichtung 'Geistige Entwicklung' */
	ID_GG(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(149, "GG", "Geistige Entwicklung", null, null)
	}),

	/** Fachrichtung 'Hören und Kommunikation' */
	ID_HK(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(150, "HK", "Hören und Kommunikation", null, null)
	}),

	/** Fachrichtung 'Körperliche und motorische Entwicklung' */
	ID_KM(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(151, "KM", "Körperliche und motorische Entwicklung", null, null)
	}),

	/** Fachrichtung 'Lernen' */
	ID_LE(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(152, "LE", "Lernen", null, null)
	}),

	/** Fachrichtung 'Sprache' */
	ID_SQ(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(153, "SQ", "Sprache", null, null)
	}),

	/** Fachrichtung 'Sehen' */
	ID_SE(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(154, "SE", "Sehen", null, null)
	}),

	/** Fachrichtung 'Textiltechnik' */
	ID_32(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(155, "32", "Textiltechnik", null, null)
	}),

	/** Fachrichtung 'Mediendesign und Designtechnik' */
	ID_57(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(156, "57", "Mediendesign und Designtechnik", null, null)
	}),

	/** Fachrichtung 'Druck- und Medientechnik' */
	ID_58(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(157, "58", "Druck- und Medientechnik", null, null)
	}),

	/** Fachrichtung 'Farbtechnik/Raumgestaltung/Oberflächentechnik' */
	ID_59(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(158, "59", "Farbtechnik/Raumgestaltung/Oberflächentechnik", null, null)
	}),

	/** Fachrichtung 'Lebensmitteltechnik' */
	ID_68(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(159, "68", "Lebensmitteltechnik", null, null)
	}),

	/** Fachrichtung 'Gesundheitswissenschaft/Pflege' */
	ID_86(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(160, "86", "Gesundheitswissenschaft/Pflege", null, null)
	}),

	/** Fachrichtung 'Agrarwissenschaft' */
	ID_93(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(161, "93", "Agrarwissenschaft", null, null)
	}),

	/** Fachrichtung 'Informationstechnik' */
	ID_IT(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(162, "IT", "Informationstechnik", null, null)
	}),

	/** Fachrichtung 'Automatisierungstechnik' */
	ID_AU(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(163, "AU", "Automatisierungstechnik", null, null)
	}),

	/** Fachrichtung 'Vermessungstechnik' */
	ID_VE(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(164, "VE", "Vermessungstechnik", null, null)
	}),

	/** Fachrichtung 'Sektorales Management mit dem Profil: Verwaltung und Rechtswesen' */
	ID_6A(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(165, "6A", "Sektorales Management mit dem Profil: Verwaltung und Rechtswesen", null, null)
	}),

	/** Fachrichtung 'Sektorales Management mit dem Profil: Medien' */
	ID_6B(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(166, "6B", "Sektorales Management mit dem Profil: Medien", null, null)
	}),

	/** Fachrichtung 'Sektorales Management mit dem Profil: Gesundheitsökonomie' */
	ID_6C(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(167, "6C", "Sektorales Management mit dem Profil: Gesundheitsökonomie", null, null)
	}),

	/** Fachrichtung 'Sektorales Management mit dem Profil: Freizeitökonomie' */
	ID_6D(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(168, "6D", "Sektorales Management mit dem Profil: Freizeitökonomie", null, null)
	}),

	/** Fachrichtung 'Sektorales Management mit dem Profil: Tourismus und Gastronomie' */
	ID_6E(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(169, "6E", "Sektorales Management mit dem Profil: Tourismus und Gastronomie", null, null)
	}),

	/** Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Produktionswirtschaft' */
	ID_6F(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(170, "6F", "Produktion, Logistik, Absatz mit dem Profil: Produktionswirtschaft", null, null)
	}),

	/** Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Verkehr und Logistik' */
	ID_6G(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(171, "6G", "Produktion, Logistik, Absatz mit dem Profil: Verkehr und Logistik", null, null)
	}),

	/** Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Marketing/Handel' */
	ID_6H(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(172, "6H", "Produktion, Logistik, Absatz mit dem Profil: Marketing/Handel", null, null)
	}),

	/** Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Steuerung und Dokumentation' */
	ID_6I(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(173, "6I", "Finanz- und Rechnungswesen mit dem Profil: Steuerung und Dokumentation", null, null)
	}),

	/** Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Finanzdienstleistungen' */
	ID_6J(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(174, "6J", "Finanz- und Rechnungswesen mit dem Profil: Finanzdienstleistungen", null, null)
	}),

	/** Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Steuern' */
	ID_6K(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(175, "6K", "Finanz- und Rechnungswesen mit dem Profil: Steuern", null, null)
	}),

	/** Fachrichtung 'Politik' */
	ID_PK(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(176, "PK", "Politik", null, null)
	}),

	/** Fachrichtung 'Gastronomie' */
	ID_GZ(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(177, "GZ", "Gastronomie", null, null)
	}),

	/** Fachrichtung 'Gartenbau' */
	ID_GA(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(178, "GA", "Gartenbau", null, null)
	}),

	/** Fachrichtung 'Garten- Landschaftsbau' */
	ID_GL(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(179, "GL", "Garten- Landschaftsbau", null, null)
	}),

	/** Fachrichtung 'Pflanzenbau' */
	ID_PB(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(180, "PB", "Pflanzenbau", null, null)
	}),

	/** Fachrichtung 'Tierhaltung' */
	ID_TH(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(181, "TH", "Tierhaltung", null, null)
	}),

	/** Fachrichtung 'Natur- und Umweltschutz' */
	ID_NU(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(182, "NU", "Natur- und Umweltschutz", null, null)
	}),
	
	/** Fachrichtung 'Medizintechnik"' */
	ID_82(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(183, "82", "Medizintechnik", 2022, null)
	}),
	
	/** Fachrichtung 'Augenoptik' */
	ID_AO(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(184, "AO", "Augenoptik", 2022, null)
	}),
	
	/** Fachrichtung 'Orthopädietechnik' */
	ID_OT(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(185, "OT", "Orthopädietechnik", 2022, null)
	}),

	/** Fachrichtung 'Zahntechnik' */
	ID_ZT(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(186, "ZT", "Zahntechnik", 2022, null)
	}),
	
	/** Fachrichtung 'Ingenieurtechnik' */
	ID_IG(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(187, "IG", "Ingenieurtechnik", 2022, null)
	}),
	
	/** Fachrichtung 'Hörakustik' */
	ID_HA(new LehrerKatalogFachrichtungEintrag[]{
		new LehrerKatalogFachrichtungEintrag(188, "HA", "Hörakustik", 2022, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Fachrichtung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogFachrichtungEintrag daten;
	
	/** Die Historie mit den Einträgen der Fachrichtung */
	public final @NotNull LehrerKatalogFachrichtungEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Fachrichtungen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerFachrichtung> _fachrichtungenByID = new HashMap<>();

	/** Eine Hashmap mit allen Fachrichtungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerFachrichtung> _fachrichtungenByKuerzel = new HashMap<>();


	/**
	 * Erzeugt eine neuen Fachrichtung in der Aufzählung.
	 * 
	 * @param historie   die Historie der Fachrichtung, welches ein Array von {@link LehrerKatalogFachrichtungEintrag} ist  
	 */
	private LehrerFachrichtung(final @NotNull LehrerKatalogFachrichtungEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Fachrichtungen auf die zugehörigen Fachrichtungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Fachrichtungen auf die zugehörigen Fachrichtungen
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerFachrichtung> getMapFachrichtungByID() {
		if (_fachrichtungenByID.size() == 0)
			for (final LehrerFachrichtung g : LehrerFachrichtung.values())
				_fachrichtungenByID.put(g.daten.id, g);				
		return _fachrichtungenByID;
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Fachrichtungen auf die zugehörigen Fachrichtungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Fachrichtungen auf die zugehörigen Fachrichtungen
	 */
	private static @NotNull HashMap<@NotNull String, LehrerFachrichtung> getMapFachrichtungByKuerzel() {
		if (_fachrichtungenByKuerzel.size() == 0)
			for (final LehrerFachrichtung g : LehrerFachrichtung.values())
				_fachrichtungenByKuerzel.put(g.daten.kuerzel, g);				
		return _fachrichtungenByKuerzel;
	}
	
	
	/**
	 * Gibt die Fachrichtung anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Fachrichtung
	 * 
	 * @return die Fachrichtung oder null, falls die ID ungültig ist
	 */
	public static LehrerFachrichtung getByID(final long id) {
		return getMapFachrichtungByID().get(id);
	}


	/**
	 * Gibt die Fachrichtung anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Fachrichtung
	 * 
	 * @return die Fachrichtung oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerFachrichtung getByKuerzel(final String kuerzel) {
		return getMapFachrichtungByKuerzel().get(kuerzel);
	}

}
