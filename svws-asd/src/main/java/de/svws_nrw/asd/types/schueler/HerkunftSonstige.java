package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.HerkunftSonstigeKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den Katalog der sonstigen Herkünfte.
 */
public class HerkunftSonstige extends CoreTypeSimple<HerkunftSonstigeKatalogEintrag, HerkunftSonstige> {


	/**
	 * Erstellt eine HerkunftSonstige mit Standardwerten
	 */
	public HerkunftSonstige() {
		// nichts zu tun
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<HerkunftSonstigeKatalogEintrag, HerkunftSonstige> manager) {
		CoreTypeDataManager.putManager(HerkunftSonstige.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<HerkunftSonstigeKatalogEintrag, HerkunftSonstige> data() {
		return CoreTypeDataManager.getManager(HerkunftSonstige.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull HerkunftSonstige @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(HerkunftSonstige.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public HerkunftSonstige getInstance() {
		return new HerkunftSonstige();
	}

}
