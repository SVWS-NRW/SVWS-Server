package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.SchulabschlussBerufsbildendKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den Katalog der berufsbildenden Schulabschlüsse.
 */
public enum SchulabschlussBerufsbildend implements CoreType<SchulabschlussBerufsbildendKatalogEintrag, SchulabschlussBerufsbildend> {

	/** Es liegt kein Abschluss vor */
	OA,

	/** Abschluss der Ausbildungsvorbereitung */
	VORB,

	/** Versetzungszeugnis */
	VERS,

	/** Abschlusszeugnis in Aufbaubildungsgängen */
	AUFB,

	/** Abschluss der Berufschulvorbereitung */
	BV,

	/** Vorpraktikum */
	VP,

	/** Vorpraktikum */
	BP,

	/** Abschluss der Berufschulgrundjahres */
	BG,

	/** Abschlusszeugnis berufliche Kenntnisse */
	ASZBK,

	/** Berufschulabschluss */
	BS,

	/** Berufliche Kenntnisse, Fähigkeiten und Fertigkeiten */
	BK,

	/** Berufsabschluss */
	BAB,

    /** Fachschulabschluss (berufliche Weiterbildung) */
	BW,

	/** Erweiterte berufliche Kenntnisse, Fähigkeiten und Fertigkeiten */
	EBK,

	/** Vertiefte berufliche Kenntnisse, Fähigkeiten und Fertigkeiten */
	VBK,

	/** Pseudoabschluss: Schulwechsler, die im selben Bildungsgang verbleiben */
	WECHSEL;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<SchulabschlussBerufsbildendKatalogEintrag, SchulabschlussBerufsbildend> manager) {
		CoreTypeDataManager.putManager(SchulabschlussBerufsbildend.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<SchulabschlussBerufsbildendKatalogEintrag, SchulabschlussBerufsbildend> data() {
		return CoreTypeDataManager.getManager(SchulabschlussBerufsbildend.class);
	}

}
