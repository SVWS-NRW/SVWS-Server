package de.svws_nrw.asd.utils;

import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.utils.json.JsonValidatorFehlerartKontextData;
import de.svws_nrw.asd.validate.ValidatorManager;


/**
 * Diese Klasse stellt allgemeine Hilfsmethoden für die ASD-Core-Types zur Verfügung.
 */
public final class ASDCoreTypeUtils {

	private static JsonValidatorFehlerartKontextData dataValidatorenFehlerartKontext;

	private ASDCoreTypeUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Gibt die Daten zu den Fehlerarten zurück.
	 *
	 * @return die Daten zu den Fehlerarten
	 */
	public static JsonValidatorFehlerartKontextData getDataValidatorenFehlerartKontext() {
		return dataValidatorenFehlerartKontext;
	}

	/**
	 * Initialisiert alle Core-Types für die amtlichen Schuldaten.
	 */
	public static void initAll() {
		CoreTypeRessource.initAll();
		dataValidatorenFehlerartKontext = JsonReader.fromResourceGetValidatorData("de/svws_nrw/asd/validate/ValidatorenFehlerartKontext.json");
		ValidatorManager.init(dataValidatorenFehlerartKontext.getVersion(), dataValidatorenFehlerartKontext.getData());
	}

}
