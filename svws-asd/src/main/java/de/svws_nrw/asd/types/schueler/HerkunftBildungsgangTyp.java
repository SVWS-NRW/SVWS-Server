package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.HerkunftBildungsgangTypKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen
 * Bildungsgangs-(schul-)typen bei einem Berufskolleg oder Weiterbildungskolleg
 * als Herkunft von Schülern eines Weiterbildungskollegs.
 */
public enum HerkunftBildungsgangTyp implements CoreType<HerkunftBildungsgangTypKatalogEintrag, HerkunftBildungsgangTyp> {

	/** Weiterbildungskolleg: Abendgymnasium */
	AG,

	/** Weiterbildungskolleg: Abendrealschule */
	AR,

	/** Weiterbildungskolleg: Abendrealschule */
	KL,

	/** Berufskolleg: Berufsfachschule */
	BF,

	/** Berufskolleg: Berufschule */
	BS,

	/** Berufskolleg: Berufliches Gymnasium */
	BY,

	/** Berufskolleg: Fachoberschule */
	FO,

	/** Berufskolleg: Fachschule */
	FS;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<HerkunftBildungsgangTypKatalogEintrag, HerkunftBildungsgangTyp> manager) {
		CoreTypeDataManager.putManager(HerkunftBildungsgangTyp.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<HerkunftBildungsgangTypKatalogEintrag, HerkunftBildungsgangTyp> data() {
		return CoreTypeDataManager.getManager(HerkunftBildungsgangTyp.class);
	}

}
