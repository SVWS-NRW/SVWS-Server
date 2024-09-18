package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerMehrleistungsartKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Anrechnungsgründe.
 */
public class LehrerMehrleistungsarten extends CoreTypeSimple<@NotNull LehrerMehrleistungsartKatalogEintrag, @NotNull LehrerMehrleistungsarten> {

	/**
	 * Erstellt einen LehrerMehrleistungsart mit Standardwerten
	 */
	public LehrerMehrleistungsarten() {
		// nichts zu tun
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerMehrleistungsartKatalogEintrag, LehrerMehrleistungsarten> manager) {
		CoreTypeDataManager.putManager(LehrerMehrleistungsarten.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerMehrleistungsartKatalogEintrag, LehrerMehrleistungsarten> data() {
		return CoreTypeDataManager.getManager(LehrerMehrleistungsarten.class);
	}


	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull LehrerMehrleistungsarten @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(LehrerMehrleistungsarten.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	@Override
	public LehrerMehrleistungsarten getInstance() {
		return new LehrerMehrleistungsarten();
	}

}
