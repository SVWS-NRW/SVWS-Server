package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.DQRNiveauKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die möglichen DQR-Niveaus
 */
public class DQRNiveau extends CoreTypeSimple<DQRNiveauKatalogEintrag, DQRNiveau> {

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<DQRNiveauKatalogEintrag, DQRNiveau> manager) {
		CoreTypeDataManager.putManager(DQRNiveau.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<DQRNiveauKatalogEintrag, DQRNiveau> data() {
		return CoreTypeDataManager.getManager(DQRNiveau.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull DQRNiveau @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(DQRNiveau.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public DQRNiveau getInstance() {
		return new DQRNiveau();
	}

}
