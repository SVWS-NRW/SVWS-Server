package de.svws_nrw.data.gost.klausurplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenDataCollection;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenMetaDataCollection;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.gost.DataGostFaecher;
import de.svws_nrw.data.gost.DataGostJahrgangSchuelerliste;
import de.svws_nrw.data.kurse.DataKursliste;
import de.svws_nrw.data.lehrer.DataLehrerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
	public static GostKlausurenMetaDataCollection getAllData(final DBEntityManager conn, final int abiturjahr, final GostHalbjahr halbjahr) throws ApiOperationException {
		final List<Integer> jahrgaenge = new ArrayList<>();
		jahrgaenge.add(abiturjahr);
		switch (halbjahr) {
			case GostHalbjahr.EF1, GostHalbjahr.EF2 -> {
				jahrgaenge.add(abiturjahr - 1);
				jahrgaenge.add(abiturjahr - 2);
			}
			case GostHalbjahr.Q11, GostHalbjahr.Q12 -> {
				jahrgaenge.add(abiturjahr - 1);
				jahrgaenge.add(abiturjahr + 1);
			}
			case GostHalbjahr.Q21, GostHalbjahr.Q22 -> {
				jahrgaenge.add(abiturjahr + 1);
				jahrgaenge.add(abiturjahr + 2);
			}
		}

		final int schuljahr = halbjahr.getSchuljahrFromAbiturjahr(abiturjahr);
		final List<DTOSchuljahresabschnitte> abschnitte = conn.query("SELECT sja FROM DTOSchuljahresabschnitte sja WHERE sja.Jahr = :jahr AND sja.Abschnitt = :abschnitt", DTOSchuljahresabschnitte.class)
				.setParameter("jahr", schuljahr)
				.setParameter("abschnitt", halbjahr.halbjahr)
				.getResultList();
		if (abschnitte.size() != 1)
			throw new ApiOperationException(Status.NOT_FOUND, "Schuljahresabschnitt nicht gefunden.");

		final GostKlausurenMetaDataCollection data = new GostKlausurenMetaDataCollection();
		for (final int jg : jahrgaenge) {
			final GostKlausurenDataCollection klausuren = DataGostKlausurenKursklausur.getKlausurDataCollection(conn, jg, GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(jg, schuljahr, halbjahr.halbjahr).id, true);
			data.klausurdata.kursklausuren.addAll(klausuren.kursklausuren);
			data.klausurdata.schuelerklausuren.addAll(klausuren.schuelerklausuren);
			data.klausurdata.schuelerklausurtermine.addAll(klausuren.schuelerklausurtermine);
			data.klausurdata.termine.addAll(klausuren.termine);
			data.klausurdata.vorgaben.addAll(klausuren.vorgaben);
			data.faecher.addAll(DataGostFaecher.getFaecherManager(conn, jg).faecher());
			data.schueler.addAll(new DataGostJahrgangSchuelerliste(conn, abiturjahr).getAllSchueler());
		}
		data.kurse.addAll(DataKursliste.getKursListenFuerAbschnitt(conn, abschnitte.getFirst().ID, true));
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
