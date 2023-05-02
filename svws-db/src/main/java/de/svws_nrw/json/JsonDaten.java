package de.svws_nrw.json;

import de.svws_nrw.core.data.schule.AbgangsartKatalog;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalog;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogIndex;
import de.svws_nrw.core.utils.schule.AbgangsartenManager;
import de.svws_nrw.core.utils.schule.BerufskollegFachklassenManager;

/**
 * Diese Klasse dient dem Zugriff auf Daten aus JSON-Dateien.
 * Hierfür wird die Klasse {@link JsonReader} genutzt.
 */
public final class JsonDaten {

	private JsonDaten() {
	}

	/**
	 * Liest den Katalog der Abgangsarten an allgemeinbildenden Schulformen ein.
	 *
	 * @return der Katalog der Abgangsarten
	 */
	private static AbgangsartKatalog getAbgangsartenAllgemeinbildend() {
		return JsonReader.fromResource("daten/json/AbgangsartenAllgemeinbildend.json", AbgangsartKatalog.class);
	}

	/**
	 * Liest den Katalog der Abgangsarten an berufsbildenden Schulformen ein.
	 *
	 * @return der Katalog der Abgangsarten
	 */
	private static AbgangsartKatalog getAbgangsartenBerufsbildend() {
		return JsonReader.fromResource("daten/json/AbgangsartenBerufsbildend.json", AbgangsartKatalog.class);
	}

	/** Der Core-Manager für die Abgangsarten für allgemeinbildende und berufsbildende Schulformen. */
	public static final AbgangsartenManager abgangsartenManager = new AbgangsartenManager(getAbgangsartenAllgemeinbildend(), getAbgangsartenBerufsbildend());


	/**
	 * Liest den Katalog der Fachklassen an berufsbildenden Schulformen ein.
	 *
	 * @return der Katalog der Fachklassen
	 */
	private static BerufskollegFachklassenKatalog getFachklassen() {
		final BerufskollegFachklassenKatalog katalog = new BerufskollegFachklassenKatalog();
		final int[] indizes = {
			10, 20, 30, 40, 50, 55, 56, 57, 58,
			60, 61, 62, 63, 70, 80, 90, 91, 92, 93, 94,
			100, 110, 120, 130, 140, 141, 145, 146,
			150, 160, 170, 180, 190,
			200, 210, 220, 230, 240, 250, 260, 270, 280, 290,
			300, 310, 320, 330, 340, 350, 370,
			940, 960, 970, 980
		};
		for (final int index : indizes) {
			final BerufskollegFachklassenKatalogIndex katIndex = JsonReader.fromResource("daten/json/fachklassen/Index" + index + ".json", BerufskollegFachklassenKatalogIndex.class);
			katalog.version += katIndex.version;
			katalog.indizes.add(katIndex);
		}
		return katalog;
	}

	/** Der Core-Manager für die Fachklassen an berufsbildenden Schulformenen. */
	public static final BerufskollegFachklassenManager fachklassenManager = new BerufskollegFachklassenManager(getFachklassen());

}
