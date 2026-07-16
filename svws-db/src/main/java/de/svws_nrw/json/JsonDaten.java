package de.svws_nrw.json;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.FachklasseKatalogEintrag;
import de.svws_nrw.asd.types.schule.Fachklasse;
import de.svws_nrw.core.data.bk.BKBildungsplanKatalog;
import de.svws_nrw.core.data.bk.BKBildungsplanKatalogEintrag;
import de.svws_nrw.core.data.schule.AbgangsartKatalog;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalog;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogDaten;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogEintrag;
import de.svws_nrw.core.data.schule.BerufskollegFachklassenKatalogIndex;
import de.svws_nrw.core.utils.schule.AbgangsartenManager;
import de.svws_nrw.core.utils.schule.BerufskollegBildungsplanManager;
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



	/** Der Core-Manager für die Fachklassen an berufsbildenden Schulformenen. */
	public static final BerufskollegFachklassenManager fachklassenManager = new BerufskollegFachklassenManager(getFachklassenFromCoreType());


	/**
	 * Liest den Katalog der berufsbezogenen Lehrpläne an berufsbildenden Schulformen ein.
	 *
	 * @return der Katalog der berufsbezogenen Lehrpläne
	 */
	/* Einlesen der BK-JSONs in folgendem Ablauf
	 * - Die Daten sind nach Gliederungsindizes in Unterverzeichnissen gruppiert
	 * - in jedem dieser Unterverzeichnisse ist eine fachklassen_<id>.json Datei enthalten, die die Fachklassen des Gliederungsindex enthält
	 * - es sind eine Reihe von Lehrplänen enthalten, die die Namensgebung Bildungsplan_<index>_<fachklasse>.json haben, die
	 *   mittels sweep durch das Verzeichnis automatisch eingelesen werden sollen.
	 */

	private static BKBildungsplanKatalog getLehrplaene() {
		final BKBildungsplanKatalog katalog = new BKBildungsplanKatalog();
		final File dir = new File("daten/json/bk/lehrplaene");
		if (dir.isDirectory()) {
			final File[] files = dir.listFiles();
			if (files != null) {
				for (final File json : files) {
					if (json.isFile()) {
						final BKBildungsplanKatalogEintrag bildungsplan = JsonReader.fromResource(json.toString(), BKBildungsplanKatalogEintrag.class);
						katalog.version += bildungsplan.version;
						katalog.lehrplaene.add(bildungsplan);
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
	public static final BerufskollegBildungsplanManager bildungsplanManager = new BerufskollegBildungsplanManager(getLehrplaene());


	/**
	 * Gibt den Katalog der Fachklassen zurück, der aus den Core-Types generiert wird.
	 *
	 * @return der Katalog der Fachklassen
	 */
	private static BerufskollegFachklassenKatalog getFachklassenFromCoreType() {
		final BerufskollegFachklassenKatalog katalog = new BerufskollegFachklassenKatalog();
		katalog.version = Fachklasse.data().getVersion();

		// Map bkIndex → BerufskollegFachklassenKatalogIndex
		final Map<Integer, BerufskollegFachklassenKatalogIndex> mapIndex = new HashMap<>();

		for (final Fachklasse fachklasse : Fachklasse.data().getWerte()) {
			final List<FachklasseKatalogEintrag> historie = Fachklasse.data().getHistorieByWert(fachklasse);
			if (historie.isEmpty()) {
				continue;
			}

			// Schlüssel und bkIndex aus dem ersten Historieneintrag ermitteln
			final int bkIndex = historie.get(0).bkIndex;
			final String schluessel = historie.get(0).fkSchluessel;
			final String schluessel2 = historie.get(0).fkSchluessel2;

			// Katalogeintrag anlegen
			final BerufskollegFachklassenKatalogEintrag eintrag = new BerufskollegFachklassenKatalogEintrag();
			eintrag.schluessel = (schluessel != null) ? schluessel : "";
			eintrag.schluessel2 = (schluessel2 != null) ? schluessel2 : "";

			for (final FachklasseKatalogEintrag he : historie) {
				final BerufskollegFachklassenKatalogDaten daten = new BerufskollegFachklassenKatalogDaten();
				daten.id = he.id;
				daten.istAusgelaufen = he.istAusgelaufen;
				daten.berufsfeldGruppe = he.berufsfeldGruppe;
				daten.berufsfeld = he.berufsfeld;
				daten.ebene1 = he.ebene1;
				daten.ebene2 = he.ebene2;
				daten.ebene3 = he.ebene3;
				daten.bezeichnung = he.text;
				daten.bezeichnungM = he.bezeichnungM;
				daten.bezeichnungW = he.bezeichnungW;
				daten.gueltigVon = he.gueltigVon;
				daten.gueltigBis = he.gueltigBis;
				eintrag.historie.add(daten);
			}

			// Index-Gruppe ermitteln oder neu anlegen
			BerufskollegFachklassenKatalogIndex katIndex = mapIndex.get(bkIndex);
			if (katIndex == null) {
				katIndex = new BerufskollegFachklassenKatalogIndex();
				katIndex.index = bkIndex;
				katIndex.version = katalog.version;
				mapIndex.put(bkIndex, katIndex);
			}
			katIndex.fachklassen.add(eintrag);
		}

		// Indizes aufsteigend sortiert dem Katalog hinzufügen
		mapIndex.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(e -> katalog.indizes.add(e.getValue()));

		return katalog;
	}

}
