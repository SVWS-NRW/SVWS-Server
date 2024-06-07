package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.List;

import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenDataCollection;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenMetaDataCollection;
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
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 *
	 * @return die DataCollection
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response getAllDataGZip(final DBEntityManager conn, final int abiturjahr, final GostHalbjahr halbjahr) throws ApiOperationException {
		final GostKlausurenMetaDataCollection coll = getAllData(conn, abiturjahr, halbjahr);
		return JSONMapper.gzipFileResponseFromObject(coll, "klausurdaten_%d_%d.json.gz".formatted(abiturjahr, halbjahr.id));
	}

	/**
	 * Sammelt alle Daten, die für die Klausurplanung der gesamten Oberstufe nötig sind.
	 *
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 *
	 * @return die DataCollection
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static GostKlausurenMetaDataCollection getAllData(final DBEntityManager conn, final int abiturjahr, final GostHalbjahr halbjahr)
			throws ApiOperationException {

		final List<GostJahrgang> jahrgaenge = DataGostJahrgangsliste.getGostJahrgangsliste(conn);

		final GostKlausurenMetaDataCollection data = new GostKlausurenMetaDataCollection();
		for (final GostJahrgang jg : jahrgaenge) {
			final GostKlausurenDataCollection klausuren = DataGostKlausurenKursklausur.getKlausurDataCollection(conn, jg.abiturjahr, -1, true);
			data.klausurdata.kursklausuren.addAll(klausuren.kursklausuren);
			data.klausurdata.schuelerklausuren.addAll(klausuren.schuelerklausuren);
			data.klausurdata.schuelerklausurtermine.addAll(klausuren.schuelerklausurtermine);
			data.klausurdata.termine.addAll(klausuren.termine);
			data.klausurdata.vorgaben.addAll(klausuren.vorgaben);
			data.faecher.addAll(DataGostFaecher.getFaecherManager(conn, jg.abiturjahr).faecher());
			data.schueler.addAll(new DataGostJahrgangSchuelerliste(conn, jg.abiturjahr).getAllSchueler());

			for (final GostHalbjahr gj : GostHalbjahr.values()) {
				final Schuljahresabschnitt sja =
						DataSchuljahresabschnitte.getFromSchuljahrUndAbschnitt(conn, gj.getSchuljahrFromAbiturjahr(jg.abiturjahr), gj.halbjahr);
				if (sja != null)
					data.kurse.addAll(DataKursliste.getKursListenFuerAbschnitt(conn, sja.id, true));
			}

		}

		data.lehrer.addAll(DataLehrerliste.getLehrerListe(conn));
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
