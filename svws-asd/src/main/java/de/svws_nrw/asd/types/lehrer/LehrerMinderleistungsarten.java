package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerMinderleistungsartKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Anrechnungsgründe.
 */
public class LehrerMinderleistungsarten extends CoreTypeSimple<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten> {

	/**
	 * Erstellt einen LehrerMinderleistungsart mit Standardwerten
	 */
	public LehrerMinderleistungsarten() {
		// nichts zu tun
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten> manager) {
		CoreTypeDataManager.putManager(LehrerMinderleistungsarten.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten> data() {
		return CoreTypeDataManager.getManager(LehrerMinderleistungsarten.class);
	}


	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull LehrerMinderleistungsarten @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(LehrerMinderleistungsarten.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	@Override
	public LehrerMinderleistungsarten getInstance() {
		return new LehrerMinderleistungsarten();
	}

}
