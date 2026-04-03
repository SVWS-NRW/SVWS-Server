package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.HerkunftSchulformKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den Katalog der Herkunftsschulform.
 */
public class HerkunftSchulform extends CoreTypeSimple<HerkunftSchulformKatalogEintrag, HerkunftSchulform> {

	/**
	 * Erstellt eine HerkunftSchulform mit Standardwerten
	 */
	public HerkunftSchulform() {
		// nichts zu tun
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<HerkunftSchulformKatalogEintrag, HerkunftSchulform> manager) {
		CoreTypeDataManager.putManager(HerkunftSchulform.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<HerkunftSchulformKatalogEintrag, HerkunftSchulform> data() {
		return CoreTypeDataManager.getManager(HerkunftSchulform.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull HerkunftSchulform @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(HerkunftSchulform.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public HerkunftSchulform getInstance() {
		return new HerkunftSchulform();
	}

}
