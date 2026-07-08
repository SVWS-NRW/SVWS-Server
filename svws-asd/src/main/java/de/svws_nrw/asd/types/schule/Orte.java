package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.OrteKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den Katalog der Orten.
 */
public class Orte extends CoreTypeSimple<OrteKatalogEintrag, Orte> {

	/**
	 * Erstellt ein Ort mit Standardwerten
	 */
	public Orte() {
		// nichts zu tun
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<OrteKatalogEintrag, Orte> manager) {
		CoreTypeDataManager.putManager(Orte.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<OrteKatalogEintrag, Orte> data() {
		return CoreTypeDataManager.getManager(Orte.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Orte @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Orte.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public Orte getInstance() {
		return new Orte();
	}

}
