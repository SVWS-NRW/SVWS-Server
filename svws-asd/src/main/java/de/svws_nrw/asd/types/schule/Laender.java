package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.LaenderKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Länder.
 */
public enum Laender implements CoreType<LaenderKatalogEintrag, Laender> {

	/** Land: Übriges Ausland */
	AL,

	/** Land: Belgien */
	B,

	/** Land: Brandenburg */
	BB,

	/** Land: Berlin */
	BE,

	/** Land: Baden-Württemberg */
	BW,

	/** Land: Bayern */
	BY,

	/** Land: Bremen */
	HB,

	/** Land: Hessen */
	HE,

	/** Land: Hamburg */
	HH,

	/** Land: Luxemburg */
	L,

	/** Land: Mecklenburg-Vorpommern */
	MV,

	/** Land: Niedersachsen */
	NI,

	/** Land: Niederlande */
	NL,

	/** Land: Nordrhein-Westfalen */
	NW,

	/** Land: Rheinland-Pfalz */
	RP,

	/** Land: Saarland */
	SL,

	/** Land: Sachsen */
	SN,

	/** Land: Sachsen-Anhalt */
	ST,

	/** Land: Schleswig-Holstein */
	SH,

	/** Land: Thüringen */
	TH;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LaenderKatalogEintrag, Laender> manager) {
		CoreTypeDataManager.putManager(Laender.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die CoreType-Daten zurück.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LaenderKatalogEintrag, Laender> data() {
		return CoreTypeDataManager.getManager(Laender.class);
	}

}
