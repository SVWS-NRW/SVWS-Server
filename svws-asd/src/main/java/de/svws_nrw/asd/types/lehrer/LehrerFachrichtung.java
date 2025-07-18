package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Fachrichtungen von
 * Lehrern an der Schule zur Verfügung.
 */
public enum LehrerFachrichtung implements @NotNull CoreType<LehrerFachrichtungKatalogEintrag, LehrerFachrichtung> {

	/** Fachrichtung 'Kraftfahrzeugtechnik' */
	ID_03,

	/** Fachrichtung 'Maschinentechnik, Metalltechnik, Maschinenbau (außer Kfz), Verfahrens-, Fertigungstechnik' */
	ID_04,

	/** Fachrichtung 'Sanitär-, Heizungs-, Klima-, Lüftungstechnik' */
	ID_05,

	/** Fachrichtung 'Bergtechnik, Bergbau' */
	ID_08,

	/** Fachrichtung 'Hütten-, Gießereiwesen' */
	ID_09,

	/** Fachrichtung 'Elektrotechnik: Energietechnik' */
	ID_11,

	/** Fachrichtung 'Elektrotechnik: Nachrichtentechnik' */
	ID_12,

	/** Fachrichtung 'Bautechnik, Bauingenieurwesen' */
	ID_21,

	/** Fachrichtung 'Holztechnik' */
	ID_22,

	/** Fachrichtung 'Architektur' */
	ID_23,

	/** Fachrichtung 'Textil-, Bekleidungstechnik, Textilgestaltung' */
	ID_31,

	/** Fachrichtung 'Informatik' */
	ID_35,

	/** Fachrichtung 'Chemie- / Verfahrens- / Chemotechnik' */
	ID_41,

	/** Fachrichtung 'Lebensmittelchemie' */
	ID_42,

	/** Fachrichtung 'Farb- u. Raumgestaltung, Gestaltungstechnik, Malerei, Graphik, Design, Fotografie, Glas- und Keramiktechnik' */
	ID_55,

	/** Fachrichtung 'Wirtschaftswissenschaft in kaufmännischen Berufen' */
	ID_60,

	/** Fachrichtung 'Hauswirtschaftswissenschaft' */
	ID_65,

	/** Fachrichtung 'Ernährungswissenschaft' */
	ID_66,

	/** Fachrichtung 'Sozialwesen, Sozialpädagogik, Sozialpflege, Sozialwissenschaft' */
	ID_70,

	/** Fachrichtung 'Psychologie' */
	ID_71,

	/** Fachrichtung 'Körperpflege / Biotechnik' */
	ID_80,

	/** Fachrichtung 'Landbauwissenschaft, Agrarwissenschaft, Agrarwirtschaft' */
	ID_91,

	/** Fachrichtung 'Gartenbauwissenschaft' */
	ID_92,

	/** Fachrichtung 'Sondererziehung und Rehabilitation der Blinden' */
	ID_BL,

	/** Fachrichtung 'Sondererziehung und Rehabilitation der Erziehungsschwierigen' */
	ID_EZ,

	/** Fachrichtung 'Sondererziehung und Rehabilitation der Geistigbehinderten' */
	ID_GB,

	/** Fachrichtung 'Sondererziehung und Rehabilitation der Gehörlosen' */
	ID_GH,

	/** Fachrichtung 'Sondererziehung und Rehabilitation der Körperbehinderten' */
	ID_KB,

	/** Fachrichtung 'Sondererziehung und Rehabilitation der Lernbehinderten' */
	ID_LB,

	/** Fachrichtung 'Sondererziehung und Rehabilitation der Sprachbehinderten' */
	ID_SB,

	/** Fachrichtung 'Sondererziehung und Rehabilitation der Schwerhörigen' */
	ID_SG,

	/** Fachrichtung 'Sondererziehung und Rehabilitation der Sehbehinderten' */
	ID_SH,

	/** Fachrichtung 'Drucktechnik, Reproduktionstechnik' */
	ID_51,

	/** Fachrichtung 'Fertigungstechnik' */
	ID_FT,

	/** Fachrichtung 'Fahrzeugtechnik' */
	ID_KT,

	/** Fachrichtung 'Versorgungstechnik' */
	ID_VT,

	/** Fachrichtung 'Technische Informatik' */
	ID_TI,

	/** Fachrichtung 'Energietechnik' */
	ID_ET,

	/** Fachrichtung 'Nachrichtentechnik' */
	ID_NT,

	/** Fachrichtung 'Hochbau' */
	ID_HC,

	/** Fachrichtung 'Holztechnik' */
	ID_HT,

	/** Fachrichtung 'Tiefbau' */
	ID_TB,

	/** Fachrichtung 'Banken, Bankbetriebslehre / Finanzwirtschaft' */
	ID_BK,

	/** Fachrichtung 'Handel, Handelsbetriebslehre / Absatzwirtschaft' */
	ID_HB,

	/** Fachrichtung 'Industrie, Industriebetriebslehre / Produktionswirtschaft' */
	ID_IB,

	/** Fachrichtung 'Verkehr' */
	ID_VR,

	/** Fachrichtung 'Versicherung' */
	ID_VL,

	/** Fachrichtung 'Organisation und Bürokommunikation / Datenverarbeitung / Organisationslehre / Betriebsinformatik' */
	ID_OL,

	/** Fachrichtung 'Betriebswirtschaftliche Steuerlehre / Revisions- und Treuhandwesen' */
	ID_ST,

	/** Fachrichtung 'Wirtschaftliche Warenlehre' */
	ID_WL,

	/** Fachrichtung 'Wirtschaftsgeographie' */
	ID_WG,

	/** Fachrichtung 'Wirtschaftsinformatik' */
	ID_WI,

	/** Fachrichtung 'Lebensmitteltechnologie' */
	ID_LT,

	/** Fachrichtung 'Sonstige Fachrichtung' */
	ID_99,

	/** Fachrichtung 'Absatz und Marketing' */
	ID_AS,

	/** Fachrichtung 'Betriebswirtschaftliche Finanzierungslehre' */
	ID_BS,

	/** Fachrichtung 'Unternehmensrechnung' */
	ID_UR,

	/** Fachrichtung 'Gesundheit' */
	ID_81,

	/** Fachrichtung 'Maschinenbautechnik' */
	ID_02,

	/** Fachrichtung 'Fertigungstechnik' */
	ID_06,

	/** Fachrichtung 'Versorgungstechnik' */
	ID_07,

	/** Fachrichtung 'Energietechnik' */
	ID_13,

	/** Fachrichtung 'Hochbautechnik' */
	ID_24,

	/** Fachrichtung 'Tiefbautechnik' */
	ID_25,

	/** Fachrichtung 'Technische Informatik' */
	ID_36,

	/** Fachrichtung 'Lebensmitteltechnologie' */
	ID_67,

	/** Fachrichtung 'Bankbetriebslehre' */
	ID_BB,

	/** Fachrichtung 'Betriebswirtschaftliche Steuerlehre' */
	ID_BW,

	/** Fachrichtung 'Personalwirtschaft' */
	ID_PW,

	/** Fachrichtung 'Versicherungsbetriebslehre' */
	ID_VB,

	/** Fachrichtung 'Emotionale und soziale Entwicklung' */
	ID_ES,

	/** Fachrichtung 'Geistige Entwicklung' */
	ID_GG,

	/** Fachrichtung 'Hören und Kommunikation' */
	ID_HK,

	/** Fachrichtung 'Körperliche und motorische Entwicklung' */
	ID_KM,

	/** Fachrichtung 'Lernen' */
	ID_LE,

	/** Fachrichtung 'Sprache' */
	ID_SQ,

	/** Fachrichtung 'Sehen' */
	ID_SE,

	/** Fachrichtung 'Textiltechnik' */
	ID_32,

	/** Fachrichtung 'Mediendesign und Designtechnik' */
	ID_57,

	/** Fachrichtung 'Druck- und Medientechnik' */
	ID_58,

	/** Fachrichtung 'Farbtechnik / Raumgestaltung / Oberflächentechnik' */
	ID_59,

	/** Fachrichtung 'Lebensmitteltechnik' */
	ID_68,

	/** Fachrichtung 'Gesundheitswissenschaft / Pflege' */
	ID_86,

	/** Fachrichtung 'Agrarwissenschaft' */
	ID_93,

	/** Fachrichtung 'Informationstechnik' */
	ID_IT,

	/** Fachrichtung 'Automatisierungstechnik' */
	ID_AU,

	/** Fachrichtung 'Vermessungstechnik' */
	ID_VE,

	/** Fachrichtung 'Sektorales Management mit dem Profil: Verwaltung und Rechtswesen' */
	ID_6A,

	/** Fachrichtung 'Sektorales Management mit dem Profil: Medien' */
	ID_6B,

	/** Fachrichtung 'Sektorales Management mit dem Profil: Gesundheitsökonomie' */
	ID_6C,

	/** Fachrichtung 'Sektorales Management mit dem Profil: Freizeitökonomie' */
	ID_6D,

	/** Fachrichtung 'Sektorales Management mit dem Profil: Tourismus und Gastronomie' */
	ID_6E,

	/** Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Produktionswirtschaft' */
	ID_6F,

	/** Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Verkehr und Logistik' */
	ID_6G,

	/** Fachrichtung 'Produktion, Logistik, Absatz mit dem Profil: Marketing / Handel' */
	ID_6H,

	/** Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Steuerung und Dokumentation' */
	ID_6I,

	/** Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Finanzdienstleistungen' */
	ID_6J,

	/** Fachrichtung 'Finanz- und Rechnungswesen mit dem Profil: Steuern' */
	ID_6K,

	/** Fachrichtung 'Politik' */
	ID_PK,

	/** Fachrichtung 'Gastronomie' */
	ID_GZ,

	/** Fachrichtung 'Gartenbau' */
	ID_GA,

	/** Fachrichtung 'Garten- und Landschaftsbau' */
	ID_GL,

	/** Fachrichtung 'Pflanzenbau' */
	ID_PB,

	/** Fachrichtung 'Tierhaltung' */
	ID_TH,

	/** Fachrichtung 'Natur- und Umweltschutz' */
	ID_NU,

	/** Fachrichtung 'Medizintechnik"' */
	ID_82,

	/** Fachrichtung 'Augenoptik' */
	ID_AO,

	/** Fachrichtung 'Orthopädietechnik' */
	ID_OT,

	/** Fachrichtung 'Zahntechnik' */
	ID_ZT,

	/** Fachrichtung 'Ingenieurtechnik' */
	ID_IG,

	/** Fachrichtung 'Hörakustik' */
	ID_HA;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerFachrichtungKatalogEintrag, LehrerFachrichtung> manager) {
		CoreTypeDataManager.putManager(LehrerFachrichtung.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerFachrichtungKatalogEintrag, LehrerFachrichtung> data() {
		return CoreTypeDataManager.getManager(LehrerFachrichtung.class);
	}

}
