package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerRechtsverhaeltnisKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Arten von Rechtsverhältnissen für Lehrer an der Schule.
 */
public enum LehrerRechtsverhaeltnis implements @NotNull CoreType<LehrerRechtsverhaeltnisKatalogEintrag, LehrerRechtsverhaeltnis> {

	/** Rechtsverhältnis 'Beamter auf Lebenszeit' */
	L,

	/** Rechtsverhältnis 'Beamter auf Probe' */
	P,

	/** Rechtsverhältnis 'Beamter auf Probe zur Anstellung' */
	A,

	/** Rechtsverhältnis 'Beamter, nebenamtlich (nicht hauptamtlich im Schuldienst)' */
	N,

	/** Rechtsverhältnis 'Beamter auf Widerruf (LAA)' */
	W,

	/** Rechtsverhältnis 'Angestellte, unbefristet (BAT-Vertrag)' */
	U,

	/** Rechtsverhältnis 'Angestellte, befristet (BAT-Vertrag)' */
	B,

	/** Rechtsverhältnis 'Angestellte, nicht BAT-Vertrag' */
	J,

	/** Rechtsverhältnis 'Gestellungsvertrag' */
	S,

	/** Rechtsverhältnis 'Unentgeltlich Beschäftigte' */
	X;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerRechtsverhaeltnisKatalogEintrag, LehrerRechtsverhaeltnis> manager) {
		CoreTypeDataManager.putManager(LehrerRechtsverhaeltnis.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerRechtsverhaeltnisKatalogEintrag, LehrerRechtsverhaeltnis> data() {
		return CoreTypeDataManager.getManager(LehrerRechtsverhaeltnis.class);
	}


	/**
	 * Gibt das Rechtsverhältnis zu dem übergebenen Schlüssel zurück. Bei einem ungültigen Schlüssel
	 * wird null zurückgegeben,
	 *
	 * @param schluessel   der Schlüsselwert
	 *
	 * @return das zugehörige Rechtsverhältnis oder null
	 */
	public static LehrerRechtsverhaeltnis getBySchluessel(final String schluessel) {
		if (schluessel == null)
			return null;
		return LehrerRechtsverhaeltnis.data().getWertBySchluessel(schluessel);
	}

}
