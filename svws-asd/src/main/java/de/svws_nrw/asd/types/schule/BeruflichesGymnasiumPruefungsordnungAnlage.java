package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Anlage des Beruflichen Gymnasiums am Berufskolleg.
 */
public enum BeruflichesGymnasiumPruefungsordnungAnlage implements CoreType<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag, BeruflichesGymnasiumPruefungsordnungAnlage> {

	/** Anlage D1: Bautechnische/-r Assistent/-in/AHR */
	D1,

	/** Elektrotechnische/-r Assistent/-in/AHR */
	D2,

	/** Erzieher/-in/AHR */
	D3,

	/** Informationstechnische/-r Assistent/-in/AHR */
	D3a,

	/** Gestaltungstechnische/-r Assistent/-in/AHR */
	D4,

	/** Assistent/-in für Konstruktions- und Fertigungstechnik/AHR */
	D6,

	/** Assistent/-in für Konstruktions- und Fertigungstechnik/AHR */
	D7,

	/** Chemisch-technische/-r Assistent/-in/AHR */
	D8,

	/** Physikalisch-technische/-r Assistent/-in/AHR */
	D9,

	/** Umwelttechnische/-r Assistent/-in/AHR */
	D10,

	/** Kaufmännische/-r Assistent/-in/AHR */
	D12,

	/** Technische/-r Assistent/-in für Betriebsinformatik/AHR */
	D13,

	/** Allgemeine Hochschulreife/Bautechnik */
	D14,

	/** Allgemeine Hochschulreife/Elektrotechnik */
	D15,

	/** Allgemeine Hochschulreife/Ingenieurwissenschaften */
	D15a,

	/** Allgemeine Hochschulreife/Erziehungswissenschaften */
	D16,

	/** Allgemeine Hochschulreife/Freizeitsportleiter/-in (Sport, Gesundheitsförderung, Biologie) */
	D17,

	/** Allgemeine Hochschulreife/Gesundheit */
	D17a,

	/** Allgemeine Hochschulreife/Kunst, Englisch */
	D18,

	/** Allgemeine Hochschulreife/Ernährung */
	D19,

	/** Allgemeine Hochschulreife/Maschinenbautechnik */
	D20,

	/** Allgemeine Hochschulreife/Mathematik, Informatik */
	D21,

	/** Allgemeine Hochschulreife/Biologie, Chemie */
	D22,

	/** Allgemeine Hochschulreife/Chemie, Chemietechnik */
	D23,

	/** Allgemeine Hochschulreife/Deutsch, Englisch */
	D25,

	/** Allgemeine Hochschulreife/Betriebswirtschaftslehre */
	D27,

	/** Allgemeine Hochschulreife/Fremdsprachenkorrespondent/-in (Betriebswirtschaftslehre, Sprachen) -> 2024
	    Allgemeine Hochschulreife/International Business Communication (Betriebswirtschaftslehre Sprachen 2025 -> */
	D28;



	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag, BeruflichesGymnasiumPruefungsordnungAnlage> manager) {
		CoreTypeDataManager.putManager(BeruflichesGymnasiumPruefungsordnungAnlage.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<BeruflichesGymnasiumPruefungsordnungAnlageKatalogEintrag, BeruflichesGymnasiumPruefungsordnungAnlage> data() {
		return CoreTypeDataManager.getManager(BeruflichesGymnasiumPruefungsordnungAnlage.class);
	}

}

