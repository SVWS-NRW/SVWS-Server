package de.nrw.schule.svws.json;

import de.nrw.schule.svws.core.data.schule.AbgangsartKatalog;
import de.nrw.schule.svws.core.utils.schule.AbgangsartenManager;

/**
 * Diese Klasse dient dem Zugriff auf Daten aus JSON-Dateien.
 * Hierfür wird die Klasse {@link JsonReader} genutzt.
 */
public class JsonDaten {
	
	/**
	 * Liest den Katalog der Abgangsarten an allgemeinbildenden Schulformen ein.
	 * 
	 * @return der Katalog der Abgangsarten
	 */
	private static final AbgangsartKatalog getAbgangsartenAllgemeinbildend() {
		return JsonReader.fromResource("daten/json/AbgangsartenAllgemeinbildend.json", AbgangsartKatalog.class);
	}

	/**
	 * Liest den Katalog der Abgangsarten an berufsbildenden Schulformen ein.
	 * 
	 * @return der Katalog der Abgangsarten
	 */
	private static final AbgangsartKatalog getAbgangsartenBerufsbildend() {
		return JsonReader.fromResource("daten/json/AbgangsartenBerufsbildend.json", AbgangsartKatalog.class);
	}

	/** Der Core-Manager für die Abgangsarten für allgemeinbildende und berfusbildende Schulformen. */
	public static final AbgangsartenManager abgangsartenManager = new AbgangsartenManager(getAbgangsartenAllgemeinbildend(), getAbgangsartenBerufsbildend());
	
}
