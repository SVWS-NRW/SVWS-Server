package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.TerminKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für allgemeine Termine.
 */
public enum Termin implements @NotNull CoreType<TerminKatalogEintrag, Termin> {

	/** Der letzte Unterrichtstag in dem ersten Halbjahr des Schuljahres */
	HALBJAHR_LETZTER_UNTERRICHTSTAG,

	/** Der letzte Unterrichtstag in dem ersten Halbjahr des zweiten Schuljahres der Qualifikationsphase */
	GOST_HALBJAHR_LETZTER_UNTERRICHTSTAG_Q2,

	/** Der letzte Unterrichtstag des Schuljahres */
	SCHULJAHR_LETZTER_UNTERRICHTSTAG,

	/** Der letzte Unterrichtstag in dem zweiten Halbjahr des zweiten Schuljahres der Qualifikationsphase */
	GOST_SCHULJAHR_LETZTER_UNTERRICHTSTAG_Q2;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<TerminKatalogEintrag, Termin> manager) {
		CoreTypeDataManager.putManager(Termin.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<TerminKatalogEintrag, Termin> data() {
		return CoreTypeDataManager.getManager(Termin.class);
	}

}
