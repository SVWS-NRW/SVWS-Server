package de.svws_nrw.asd.utils;

import de.svws_nrw.asd.utils.json.JsonValidatorFehlerartKontextData;
import de.svws_nrw.asd.validate.ValidatorManager;


/**
 * Diese Klasse stellt allgemeine Hilfsmethoden für die ASD-Core-Types zur Verfügung.
 */
public final class ASDCoreTypeUtils {

	private ASDCoreTypeUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Initialisiert alle Core-Types un Validatoren für die amtlichen Schuldaten.
	 */
	public static void initAll() {
		CoreTypeRessource.initAll();
		final JsonValidatorFehlerartKontextData dataValidatorenFehlerartKontext = new JsonValidatorFehlerartKontextData();
		ValidatorManager.init(dataValidatorenFehlerartKontext.getVersions(), dataValidatorenFehlerartKontext.getData());
	}

}
