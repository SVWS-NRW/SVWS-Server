package de.svws_nrw.data.klassen;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.klassen.KlassenDatenMinimal;
import de.svws_nrw.asd.data.klassen.KlassenListeEintrag;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dieser DataManager stellt Funktionalität zum Laden von {@link KlassenListeEintrag} Objekten bereit.
 */
public final class DataKlassenliste extends DataManagerRevised<Long, DTOKlassen, KlassenListeEintrag> {

	/**
	 * Default Konstruktor
	 *
	 * @param conn DBEntityManager
	 */
	public DataKlassenliste(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Methode liefert eine Liste von {@link KlassenListeEintrag} zur angegebenen SchuljahresabschnittID.
	 *
	 * @param idSchuljahresabschnitt ID des Schuljahresabschnittes
	 *
	 * @return Liste von KlassenListeEintrag Objekten
	 */
	public Response getListBySchuljahresabschnittIDAsResponse(final Long idSchuljahresabschnitt) {
		final Map<Long, Integer> anzahlSchuelerByIdKlasse = getAnzahlSchuelerByIdKlasse(idSchuljahresabschnitt);
		final List<DTOKlassen> klassen = getKlassenByIdSchuljahresabschnitt(idSchuljahresabschnitt);
		final var idsKlassen = klassen.stream()
				.map(k -> k.ID)
				.toList();
		final Map<Long, List<Long>> idsLehrerByIdKlasse = conn
				.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, idsKlassen).stream()
				.collect(Collectors.groupingBy(
						dto -> dto.Klassen_ID,
						Collectors.mapping(dto -> dto.Lehrer_ID, Collectors.toList())
				));

		final List<KlassenListeEintrag> klassenliste = klassen.stream()
				.map(k -> mapWithAnzahlSchueler(k, anzahlSchuelerByIdKlasse, idsLehrerByIdKlasse))
				.toList();

		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(klassenliste).build();
	}

	/**
	 * Methode liefert eine Liste von {@link KlassenDatenMinimal} zur angegebenen SchuljahresabschnittID.
	 *
	 * @param idSchuljahresabschnitt ID des Schuljahresabschnittes
	 *
	 * @return Liste von KlassenDatenMinimal Objekten
	 */
	public Response getKlassenDatenMinimalByIdSchuljahresabschnitt(final Long idSchuljahresabschnitt) {
		final List<DTOKlassen> klassen = getKlassenByIdSchuljahresabschnitt(idSchuljahresabschnitt);
		final List<KlassenDatenMinimal> klassenliste = klassen.stream()
				.map(this::mapKlassenDatenMinimal)
				.toList();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(klassenliste).build();
	}

	@Override
	protected KlassenListeEintrag map(final DTOKlassen entity) {
		final KlassenListeEintrag dto = new KlassenListeEintrag();
		dto.id = entity.ID;
		dto.idSchuljahresabschnitt = entity.Schuljahresabschnitts_ID;
		dto.kuerzel = entity.Klasse;
		dto.idJahrgang = entity.Jahrgang_ID;
		dto.parallelitaet = getParallelitaet(entity);
		dto.beschreibung = Objects.toString(entity.Bezeichnung, "");
		dto.sortierung = Objects.requireNonNullElse(entity.Sortierung, 32000);
		return dto;
	}


	private KlassenDatenMinimal mapKlassenDatenMinimal(final DTOKlassen entity) {
		final var dto = new KlassenDatenMinimal();
		dto.id = entity.ID;
		dto.idSchuljahresabschnitt = entity.Schuljahresabschnitts_ID;
		dto.kuerzel = entity.Klasse;
		dto.idJahrgang = entity.Jahrgang_ID;
		dto.beschreibung = entity.Bezeichnung;
		dto.parallelitaet = getParallelitaet(entity);
		return dto;
	}

	private Map<Long, Integer> getAnzahlSchuelerByIdKlasse(final Long idSchuljahresabschnitt) {

		final var querySchueler = """
				SELECT Klassen_ID, COUNT(*)
				FROM SchuelerLernabschnittsdaten
				WHERE Klassen_ID IS NOT NULL
				  AND WechselNr = 0
				  AND Schuljahresabschnitts_ID = ?1
				GROUP BY Klassen_ID
				""";

		return conn.queryNativeWithParameters(querySchueler, idSchuljahresabschnitt)
				.stream()
				.collect(Collectors.toMap(
						row -> ((Number) row[0]).longValue(),
						row -> ((Number) row[1]).intValue()
				));
	}

	private KlassenListeEintrag mapWithAnzahlSchueler(final DTOKlassen dto, final Map<Long, Integer> anzahlZugehoerigerSchuelerByIdKlasse, final Map<Long, List<Long>> idsLehrerByIdKlasse) {
		final KlassenListeEintrag klassenListeEintrag = map(dto);
		klassenListeEintrag.anzahlZugeordneteSchueler = anzahlZugehoerigerSchuelerByIdKlasse.getOrDefault(dto.ID, 0);
		klassenListeEintrag.idsKlassenleitungen = idsLehrerByIdKlasse.getOrDefault(dto.ID, Collections.emptyList());
		return klassenListeEintrag;
	}

	private List<DTOKlassen> getKlassenByIdSchuljahresabschnitt(final Long idSchuljahresabschnitt) {
		if (idSchuljahresabschnitt == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Schuljahresabschnitt darf nicht null sein.");
		}
		return conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt);
	}

	private static String getParallelitaet(final DTOKlassen klasse) {
		return ((klasse.ASDKlasse == null) || (klasse.ASDKlasse.length() < 3)) ? null : klasse.ASDKlasse.substring(2);
	}

}
