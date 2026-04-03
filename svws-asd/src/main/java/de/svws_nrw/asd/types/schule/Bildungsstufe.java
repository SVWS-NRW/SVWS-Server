package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.BildungsstufeKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die möglichen Hauptgruppen der Bildungsstufe
 */
public class Bildungsstufe extends CoreTypeSimple<BildungsstufeKatalogEintrag, Bildungsstufe> {

	/**
	 * Erstellt einer Bildungsstufe mit Standardwerten
	 */
	public Bildungsstufe() {
		// leer
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<BildungsstufeKatalogEintrag, Bildungsstufe> manager) {
		CoreTypeDataManager.putManager(Bildungsstufe.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<BildungsstufeKatalogEintrag, Bildungsstufe> data() {
		return CoreTypeDataManager.getManager(Bildungsstufe.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Bildungsstufe @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Bildungsstufe.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public Bildungsstufe getInstance() {
		return new Bildungsstufe();
	}

}
