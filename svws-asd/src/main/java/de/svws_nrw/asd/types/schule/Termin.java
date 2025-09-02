package de.svws_nrw.asd.types.schule;

import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.schule.TerminKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
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


	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	private static final @NotNull Map<Integer, DateManager> _mapSchuljahrToLetzterUnterrichtstag = new HashMap<>();



	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<TerminKatalogEintrag, Termin> manager) {
		CoreTypeDataManager.putManager(Termin.class, manager);
		_mapSchuljahrToLetzterUnterrichtstag.clear();
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<TerminKatalogEintrag, Termin> data() {
		return CoreTypeDataManager.getManager(Termin.class);
	}



	/**
	 * Gibt den Date-Manger für den letzten Unterrichtstag des ersten Halbjahres in dem übergebenen
	 * Schuljahr zurück, sofern dieser in diesem Core-Type spezifiziert ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return das Datum des letzten Unterrichtstages des ersten Halbjahres
	 */
	public static DateManager getLetzterUnterrichtstagImErstenHalbjahr(final int schuljahr) {
		DateManager result = _mapSchuljahrToLetzterUnterrichtstag.get(schuljahr);
		if (result != null)
			return result;
		final TerminKatalogEintrag eintrag = data().getEintragBySchuljahrUndWert(schuljahr, HALBJAHR_LETZTER_UNTERRICHTSTAG);
		if (eintrag == null)
			return null;
		try {
			result = DateManager.from(eintrag.von);
		} catch (final InvalidDateException e) {
			throw new CoreTypeException("Fehlerhafter Termin-Eintrag für HALBJAHR_LETZTER_UNTERRICHTSTAG im Schuljahr " + schuljahr, e);
		}
		_mapSchuljahrToLetzterUnterrichtstag.put(schuljahr, result);
		return result;
	}

}
