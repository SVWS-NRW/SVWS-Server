package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.gost.DataGostFaecher;
import de.svws_nrw.data.gost.DataGostJahrgangSchuelerliste;
import de.svws_nrw.data.gost.DataGostJahrgangsliste;
import de.svws_nrw.data.kurse.DataKursliste;
import de.svws_nrw.data.lehrer.DataLehrerliste;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Data-Manager für die Klausuren der gymnasialen Oberstufe
 */
public final class DataGostKlausuren extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurtermin}.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 */
	public DataGostKlausuren(final DBEntityManager conn, final int abiturjahr) {
		super(conn);
	}

	/**
	 * Gibt die Blockungsdaten für die Blockung mit der angegebenen ID als GZip-Json
	 * zurück.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param selection das Jahr, in welchem der Jahrgang Abitur machen wird
//	 * @param halbjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 *
	 * @return die DataCollection
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response getAllDataGZip(final DBEntityManager conn, final List<Pair<Integer, Integer>> selection) throws ApiOperationException {
		final GostKlausurenCollectionAllData coll = getAllData(conn, selection);
		return JSONMapper.gzipFileResponseFromObject(coll, "klausurdaten.json.gz");
	}

	/**
	 * Sammelt alle Daten, die für die Klausurplanung der gesamten Oberstufe nötig sind.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param selection das Jahr, in welchem der Jahrgang Abitur machen wird
	 *
	 * @return die DataCollection
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenCollectionAllData getAllData(final DBEntityManager conn, final List<Pair<Integer, Integer>> selection)
			throws ApiOperationException {


		final List<Pair<Integer, Integer>> jgs = new ArrayList<>();
		if (selection == null) {
			for (final GostJahrgang jg : DataGostJahrgangsliste.getGostJahrgangsliste(conn)) {
				jgs.add(new Pair<>(jg.abiturjahr, -1));
			}
		} else {
			jgs.addAll(selection);
		}

		final GostKlausurenCollectionAllData data = new GostKlausurenCollectionAllData();
		for (final Pair<Integer, Integer> jg : jgs) {
			final GostKlausurenCollectionAllData klausuren = DataGostKlausurenKursklausur.getKlausurDataCollection(conn, jg.a, jg.b, true);
			data.kursklausuren.addAll(klausuren.kursklausuren);
			data.schuelerklausuren.addAll(klausuren.schuelerklausuren);
			data.schuelerklausurtermine.addAll(klausuren.schuelerklausurtermine);
			final Set<GostKlausurtermin> terminMenge = new HashSet<>(data.termine);
			terminMenge.addAll(klausuren.termine);
			data.termine = new ArrayList<>(terminMenge);
			data.vorgaben.addAll(klausuren.vorgaben);
			data.metadata.faecher.addAll(DataGostFaecher.getFaecherManager(conn, jg.a).faecher());
			data.metadata.schueler.addAll(new DataGostJahrgangSchuelerliste(conn, jg.a).getAllSchueler());

			for (final GostHalbjahr gj : GostHalbjahr.values()) {
				final Schuljahresabschnitt sja =
						DataSchuljahresabschnitte.getFromSchuljahrUndAbschnitt(conn, gj.getSchuljahrFromAbiturjahr(jg.a), gj.halbjahr);
				if (sja != null)
					data.metadata.kurse.addAll(DataKursliste.getKursListenFuerAbschnitt(conn, sja.id, true));
			}

		}

		data.metadata.lehrer.addAll(DataLehrerliste.getLehrerListe(conn));


		data.raumdata = DataGostKlausurenSchuelerklausurraumstunde.getSchuelerklausurraumstundenByTerminids(conn, data.termine.stream().map(t -> t.id).toList());

		return data;
	}



	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}



}
