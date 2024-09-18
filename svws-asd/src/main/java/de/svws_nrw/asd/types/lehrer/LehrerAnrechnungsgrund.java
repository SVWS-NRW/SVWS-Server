package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerAnrechnungsgrundKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Anrechnungsgründe.
 */
public class LehrerAnrechnungsgrund extends CoreTypeSimple<LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund> {

	/**
	 * Erstellt einen LehrerAnrechnungsgrund mit Standardwerten
	 */
	public LehrerAnrechnungsgrund() {
		// nichts zu tun
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund> manager) {
		CoreTypeDataManager.putManager(LehrerAnrechnungsgrund.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund> data() {
		return CoreTypeDataManager.getManager(LehrerAnrechnungsgrund.class);
	}


	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull LehrerAnrechnungsgrund @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(LehrerAnrechnungsgrund.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	@Override
	public LehrerAnrechnungsgrund getInstance() {
		return new LehrerAnrechnungsgrund();
	}

}
