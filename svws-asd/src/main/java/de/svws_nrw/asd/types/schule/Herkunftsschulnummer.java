package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.HerkunftsschulnummerKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Herkunftsschulnummern.
 */
public final class Herkunftsschulnummer extends CoreTypeSimple<HerkunftsschulnummerKatalogEintrag, Herkunftsschulnummer> {


	/**
	 * Erstellt einen Eintrag für die Herkunftsschulnummern mit Standardwerten
	 */
	public Herkunftsschulnummer() {
		// nichts zu tun
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<HerkunftsschulnummerKatalogEintrag, Herkunftsschulnummer> manager) {
		CoreTypeDataManager.putManager(Herkunftsschulnummer.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<HerkunftsschulnummerKatalogEintrag, Herkunftsschulnummer> data() {
		return CoreTypeDataManager.getManager(Herkunftsschulnummer.class);
	}


	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Herkunftsschulnummer @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Herkunftsschulnummer.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	@Override
	public Herkunftsschulnummer getInstance() {
		return new Herkunftsschulnummer();
	}

}
