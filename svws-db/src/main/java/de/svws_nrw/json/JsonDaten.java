package de.svws_nrw.json;

import java.io.File;

import de.svws_nrw.core.data.bk.BKLehrplanKatalog;
import de.svws_nrw.core.data.bk.BKLehrplanKatalogEintrag;
import de.svws_nrw.core.data.schule.AbgangsartKatalog;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalog;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogIndex;
import de.svws_nrw.core.utils.schule.AbgangsartenManager;
import de.svws_nrw.core.utils.schule.BerufskollegFachklassenManager;
import de.svws_nrw.core.utils.schule.BerufskollegLehrplanManager;

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


	/**
	 * Liest den Katalog der berufsbezogenen Lehrpläne an berufsbildenden Schulformen ein.
	 *
	 * @return der Katalog der berufsbezogenen Lehrpläne
	 */
    /* Einlesen der BK-Jasons in folgendem Ablauf
     * - Die Daten sind nach Gliederungsindizes in Unterverzeichnissen gruppiert
     * - in jedem dieser Unterverzeichnisse ist eine fachklassen_<id>.json Datei enthalten, die die Fachklassen des Gliederungsindex enthält
     * - es sind eine Reihe von Lehrplänen enthalten, die die Namensgebung Lehrplan_<index>_<fachklasse>.json haben, die
     *   mittels sweep durch das Verzeichnis automatisch eingelesen werden sollen.
     */

	private static BKLehrplanKatalog getLehrplaene() {
		final BKLehrplanKatalog katalog = new BKLehrplanKatalog();
		final File dir = new File("daten/json/bk/lehrplaene");
		if (dir.isDirectory()) {
			final File[] files = dir.listFiles();
			if (files != null) {
				for (final File json : files) {
					if (json.isFile()) {
						final BKLehrplanKatalogEintrag lehrplan = JsonReader.fromResource(json.toString(), BKLehrplanKatalogEintrag.class);
						katalog.version += lehrplan.version;
						katalog.lehrplaene.add(lehrplan);
					}
				}
			}
		}
		//for (final int index : indizes) {
			//final BerufskollegFachklassenKatalogIndex katIndex = JsonReader.fromResource("daten/json/fachklassen/Index" + index + ".json", BerufskollegFachklassenKatalogIndex.class);
			//katalog.version += katIndex.version;
			//katalog.indizes.add(katIndex);
		//}
		return katalog;
	}


	/** Der Core-Manager für die Fachklassen an berufsbildenden Schulformenen. */
	public static final BerufskollegLehrplanManager lehrplanManager = new BerufskollegLehrplanManager(getLehrplaene());

}
