package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.FloskelgruppenartKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen Hauptgruppen der Floskelgruppenart
 */
public class Floskelgruppenart extends CoreTypeSimple<FloskelgruppenartKatalogEintrag, Floskelgruppenart> {

	/**
	 * Erstellt eine Floskelgruppenart mit Standardwerten
	 */
	public Floskelgruppenart() {
		// leer
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<FloskelgruppenartKatalogEintrag, Floskelgruppenart> manager) {
		CoreTypeDataManager.putManager(Floskelgruppenart.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<FloskelgruppenartKatalogEintrag, Floskelgruppenart> data() {
		return CoreTypeDataManager.getManager(Floskelgruppenart.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Floskelgruppenart @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Floskelgruppenart.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public Floskelgruppenart getInstance() {
		return new Floskelgruppenart();
	}

}
