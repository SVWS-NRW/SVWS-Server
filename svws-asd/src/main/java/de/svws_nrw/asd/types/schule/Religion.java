package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.ReligionKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik erhobenen Religionen.
 */
public enum Religion implements @NotNull CoreType<ReligionKatalogEintrag, Religion> {

	/** Religion: alevitisch */
	AR,

	/** Religion: evangelisch */
	ER,

	/** Religion: jüdisch */
	HR,

	/** Religion: islamisch */
	IR,

	/** Religion: katholisch */
	KR,

	/** Religion: mennonitische BG NRW */
	ME,

	/** Religion: ohne Bekenntnis */
	OH,

	/** Religion: griechisch-orthodox */
	OR,

	/** Religion: syrisch-orthodox */
	SO,

	/** Religion: sonstige orthodoxe */
	XO,

	/** Religion: andere Religionen */
	XR;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<ReligionKatalogEintrag, Religion> manager) {
		CoreTypeDataManager.putManager(Religion.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<ReligionKatalogEintrag, Religion> data() {
		return CoreTypeDataManager.getManager(Religion.class);
	}

}
