package de.svws_nrw.data.kurse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KursDaten}.
 */
public final class DataKursliste extends DataManager<Long> {

	private final Long abschnitt;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KursDaten}.
	 *
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abschnitt   der Lernabschnitt für die Liste der Kurse
	 */
	public DataKursliste(final DBEntityManager conn, final Long abschnitt) {
		super(conn);
		this.abschnitt = abschnitt;
	}

	/**
	 * Bestimmt die Liste der Kurse für den angegeben Abschnitt. Ist dieser Abschnitt null, so werden die Kurse
	 * aller Abschnitte zurückgegeben.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param mitSchuelerInfo          gibt an, ob die KurslistenEinträge die Information zu Schülern beinhalten soll
	 *
	 * @return die Liste der Kurse
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static @NotNull List<KursDaten> getKursListenFuerAbschnitt(final DBEntityManager conn,
			final Long idSchuljahresabschnitt, final boolean mitSchuelerInfo) throws ApiOperationException {
		final @NotNull List<DTOKurs> kurse = (idSchuljahresabschnitt == null)
				? conn.queryAll(DTOKurs.class)
				: conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, idSchuljahresabschnitt);
		if (kurse.isEmpty())
			return new ArrayList<>();
		// Erstelle die Liste der Kurse
		final @NotNull List<KursDaten> daten = new ArrayList<>();
		for (final @NotNull DTOKurs dtoKurs : kurse)
			daten.add(DataKursdaten.dtoMapper.apply(dtoKurs));
		daten.sort((a, b) -> Long.compare(a.sortierung, b.sortierung));
		if (!mitSchuelerInfo)
			return daten;
		// Ergänze die Liste der Schüler in den Kursen
		final List<Long> kursIDs = daten.stream().map(k -> k.id).toList();
		final List<DTOKursSchueler> listKursSchueler = conn.queryList(
				"SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class, kursIDs);
		final List<Long> schuelerIDs = listKursSchueler.stream().map(ks -> ks.Schueler_ID).toList();
		final Map<Long, DTOSchueler> mapSchueler = conn.queryByKeyList(DTOSchueler.class, schuelerIDs).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		final HashMap<Long, List<Schueler>> mapKursSchueler = new HashMap<>();
		for (final DTOKursSchueler ks : listKursSchueler) {
			final DTOSchueler dtoSchueler = mapSchueler.get(ks.Schueler_ID);
			if (dtoSchueler == null)
				continue;
			List<Schueler> listSchueler = mapKursSchueler.get(ks.Kurs_ID);
			if (listSchueler == null) {
				listSchueler = new ArrayList<>();
				mapKursSchueler.put(ks.Kurs_ID, listSchueler);
			}
			listSchueler.add(DataSchuelerliste.mapToSchueler.apply(dtoSchueler));
		}
		for (final KursDaten eintrag : daten) {
			final List<Schueler> listSchueler = mapKursSchueler.get(eintrag.id);
			if (listSchueler != null)
				eintrag.schueler.addAll(listSchueler);
		}
		return daten;
	}


	@Override
	public Response getAll() throws ApiOperationException {
		final @NotNull List<@NotNull KursDaten> daten = getKursListenFuerAbschnitt(conn, abschnitt, true);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response getList() throws ApiOperationException {
		return this.getAll();
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
