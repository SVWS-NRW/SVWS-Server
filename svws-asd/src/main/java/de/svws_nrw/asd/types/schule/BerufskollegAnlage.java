package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.BerufskollegAnlageKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Arten von Anlagen am Berufskolleg.
 */
public enum BerufskollegAnlage implements CoreType<BerufskollegAnlageKatalogEintrag, BerufskollegAnlage> {

	/** Anlage A: Fachklassen duales System und Ausbildungsvorbereitung */
	A,

	/** Anlage B: Berufsfachschule */
	B,

	/** Anlage C: Berufsfachschule und Fachoberschule */
	C,

	/** Anlage D: Berufliches Gymnasium und Fachoberschule */
	D,

	/** Anlage E: Fachschule */
	E,

	/** Anlage H: Berufsgrundbildung und Berufsausbildung an einer freien Waldorfschule / Hiberniakolleg */
	H,

	/** Anlage X: Ehemalige Kollegschule */
	X,

	/** Anlage Z: TODO */
	Z;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<BerufskollegAnlageKatalogEintrag, BerufskollegAnlage> manager) {
		CoreTypeDataManager.putManager(BerufskollegAnlage.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<BerufskollegAnlageKatalogEintrag, BerufskollegAnlage> data() {
		return CoreTypeDataManager.getManager(BerufskollegAnlage.class);
	}

}
