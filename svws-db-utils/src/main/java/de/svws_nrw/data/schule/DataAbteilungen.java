package de.svws_nrw.data.schule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Abteilung;
import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungsKlassen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link Abteilung}.
 */
public final class DataAbteilungen extends DataManager<Long> {

	/** Die ID des Schuljahresabschnittes */
	private final Long idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Abteilung}.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes, auf den sich die Anfragen beziehen
	 */
	public DataAbteilungen(final DBEntityManager conn, final Long idSchuljahresabschnitt) {
		super(conn);
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOAbteilungen} in einen Core-DTO {@link Abteilung}.
	 */
	public static final DTOMapper<DTOAbteilungen, Abteilung> dtoMapper = (final DTOAbteilungen dto) -> {
		final Abteilung daten = new Abteilung();
		daten.id = dto.ID;
		daten.bezeichnung = dto.Bezeichnung;
		daten.idSchuljahresabschnitts = dto.Schuljahresabschnitts_ID;
		daten.idAbteilungsleiter = dto.AbteilungsLeiter_ID;
		daten.raum = dto.Raum;
		daten.email = dto.Email;
		daten.durchwahl = dto.Durchwahl;
		daten.sortierung = (dto.Sortierung == null) ? 32000 : dto.Sortierung;
		return daten;
	};


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}


	/**
	 * Gibt die Liste der Abteilungen für den Schuljahresabschnitt mit der angegebenen ID zurück.
	 * Ist die ID null, so werden die Daten für alle Schuljahresabschnitte zurückgegeben.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes oder null
	 *
	 * @return die Liste der Abteilungen
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<Abteilung> getAbteilungen(final @NotNull DBEntityManager conn, final Long idSchuljahresabschnitt) throws ApiOperationException {
		// Ermittle die Abteilungen ...
		final List<DTOAbteilungen> abteilungen = (idSchuljahresabschnitt == null) ? conn.queryAll(DTOAbteilungen.class)
				: conn.queryList(DTOAbteilungen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOAbteilungen.class, idSchuljahresabschnitt);
		if (abteilungen.isEmpty())
			return new ArrayList<>();
		final List<Abteilung> result = new ArrayList<>();
		for (final DTOAbteilungen abteilung : abteilungen)
			result.add(dtoMapper.apply(abteilung));
		// ... ermittle die Klassenzuordnungen ...
		final List<Long> idsAbteilungen = abteilungen.stream().map(a -> a.ID).toList();
		final List<DTOAbteilungsKlassen> dtosZuordnungen = conn.queryList(DTOAbteilungsKlassen.QUERY_LIST_BY_ABTEILUNG_ID, DTOAbteilungsKlassen.class, idsAbteilungen);
		final List<AbteilungKlassenzuordnung> zuordnungen = new ArrayList<>();
		for (final DTOAbteilungsKlassen dtoZuordnung : dtosZuordnungen)
			zuordnungen.add(DataAbteilungenKlassenzuordnungen.dtoMapper.apply(dtoZuordnung));
		final Map<Long, List<AbteilungKlassenzuordnung>> mapZuordnungen = zuordnungen.stream().collect(Collectors.groupingBy(z -> z.idAbteilung));
		for (final Abteilung abteilung : result)
			if (mapZuordnungen.containsKey(abteilung.id))
				abteilung.klassen.addAll(mapZuordnungen.get(abteilung.id));
		// ... und gib die Abteilungsinformationen zurück.
		return result;
	}


	/**
	 * Gibt die Abteilung für die übergebene ID zurück.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param id     die ID der Abteilung
	 *
	 * @return die Informationen zu der Abteilung
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Abteilung getAbteilung(final @NotNull DBEntityManager conn, final long id) throws ApiOperationException {
		final DTOAbteilungen abteilung = conn.queryByKey(DTOAbteilungen.class, id);
		if (abteilung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Eine Abteilung mit der ID %d konnte nicht gefunden werden.".formatted(id));
		final Abteilung result = dtoMapper.apply(abteilung);
		result.klassen.addAll(DataAbteilungenKlassenzuordnungen.getZuordnungen(conn, id));
		return result;
	}


	@Override
	public Response getList() throws ApiOperationException {
		final List<Abteilung> daten = getAbteilungen(conn, this.idSchuljahresabschnitt);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final Abteilung daten = getAbteilung(conn, id);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		// Umsetzung mit patchBasic
		throw new UnsupportedOperationException();
	}

	// TODO add

	// TODO addMultiple

	// TODO delete

	// TODO deleteMultiple

}
