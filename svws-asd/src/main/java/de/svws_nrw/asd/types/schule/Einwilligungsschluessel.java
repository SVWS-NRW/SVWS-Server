package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.EinwilligungsschluesselKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen Hauptgruppen des Einwilligungsschlüssels
 */
public class Einwilligungsschluessel extends CoreTypeSimple<EinwilligungsschluesselKatalogEintrag, Einwilligungsschluessel> {

	/**
	 * Erstellt einen Einwilligungsschlüssel mit Standardwerten
	 */
	public Einwilligungsschluessel() {
		// leer
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<EinwilligungsschluesselKatalogEintrag, Einwilligungsschluessel> manager) {
		CoreTypeDataManager.putManager(Einwilligungsschluessel.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<EinwilligungsschluesselKatalogEintrag, Einwilligungsschluessel> data() {
		return CoreTypeDataManager.getManager(Einwilligungsschluessel.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Einwilligungsschluessel @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Einwilligungsschluessel.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public Einwilligungsschluessel getInstance() {
		return new Einwilligungsschluessel();
	}

}
