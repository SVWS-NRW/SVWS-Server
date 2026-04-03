package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.HerkunftsartenKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den Katalog der Herkunftsarten.
 */
public class Herkunftsarten extends CoreTypeSimple<HerkunftsartenKatalogEintrag, Herkunftsarten> {

	/**
	 * Erstellt eine Herkunftsarten mit Standardwerten
	 */
	public Herkunftsarten() {
		// nichts zu tun
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<HerkunftsartenKatalogEintrag, Herkunftsarten> manager) {
		CoreTypeDataManager.putManager(Herkunftsarten.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<HerkunftsartenKatalogEintrag, Herkunftsarten> data() {
		return CoreTypeDataManager.getManager(Herkunftsarten.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Herkunftsarten @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Herkunftsarten.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public Herkunftsarten getInstance() {
		return new Herkunftsarten();
	}

}
