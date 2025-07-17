package de.svws_nrw.asd.types.schule;

import java.util.HashMap;

import de.svws_nrw.asd.data.schule.VerkehrsspracheKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Verkehrssprachen.
 * Die Daten orientieren sich an dem ISO-Standard 639-1 (siehe https://de.wikipedia.org/wiki/ISO_639).
 */
public class Verkehrssprache extends CoreTypeSimple<VerkehrsspracheKatalogEintrag, Verkehrssprache> {

	/** Eine Hashmap mit allen definierten Verkehrssprachen, zugeordnet zu ihren dreistelligen ISO 639-2-Codes */
	private static final @NotNull HashMap<String, Verkehrssprache> _mapIso3 = new HashMap<>();


	/**
	 * Erstellt eine Verkehrssprache mit Standardwerten
	 */
	public Verkehrssprache() {
		// nichts zu tun
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<VerkehrsspracheKatalogEintrag, Verkehrssprache> manager) {
		CoreTypeDataManager.putManager(Verkehrssprache.class, manager);
		_mapIso3.clear();
		for (final Verkehrssprache sprache : manager.getWerte())
			for (final VerkehrsspracheKatalogEintrag eintrag : sprache.historie())
				if (eintrag.iso3 != null)
					_mapIso3.put(eintrag.iso3, sprache);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<VerkehrsspracheKatalogEintrag, Verkehrssprache> data() {
		return CoreTypeDataManager.getManager(Verkehrssprache.class);
	}


	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Verkehrssprache @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Verkehrssprache.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	@Override
	public Verkehrssprache getInstance() {
		return new Verkehrssprache();
	}



	/**
	 * Gibt die Verkehrssprache für das angegebene Kürzel zurück.
	 * Dabei wird anhand der Länge des Kürzels automatisch geprüft, ob
	 * eine Sprache nach ISO 639-1 bzw. ISO 639-2 angegeben wurde.
	 *
	 * @param kuerzel   das Kürzel der Verkehrssprache
	 *
	 * @return die Verkehrssprache oder null, falls das Kürzel unbekannt ist
	 */
	public static Verkehrssprache getByIsoKuerzel(final String kuerzel) {
		if (kuerzel == null)
			return null;
		if (kuerzel.length() == 2)
			return Verkehrssprache.data().getWertByKuerzel(kuerzel);
		return _mapIso3.get(kuerzel);
	}

}
