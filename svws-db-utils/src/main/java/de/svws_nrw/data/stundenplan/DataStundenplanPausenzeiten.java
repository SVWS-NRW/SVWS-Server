package de.svws_nrw.data.stundenplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenzeit;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenzeitKlassenzuordnung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Der Daten-Manager für die Pausenzeiten eines Stundenplans:
 * {@link DTOStundenplanPausenzeit} und {@link StundenplanPausenzeit}.
 */
public final class DataStundenplanPausenzeiten extends DataManagerRevised<Long, DTOStundenplanPausenzeit, StundenplanPausenzeit> {

	private Long stundenplanID = null;

	/**
	 * Erstellt einen neuen Manager.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Pausenzeiten abgefragt werden
	 *
	 * @throws ApiOperationException   falls die Stundenplan-ID ungültig ist
	 */
	public DataStundenplanPausenzeiten(final DBEntityManager conn, final Long stundenplanID) throws ApiOperationException {
		super(conn);
		setAttributesRequiredOnCreation("wochentag", "beginn", "ende");
		setAttributesDelayedOnCreation("klassen");
		this.stundenplanID = stundenplanID;
		// Prüfe ggf. ob der Stundenplan existiert
		if (stundenplanID != null)
			DataStundenplan.getDTOStundenplan(conn, stundenplanID);
	}


	@Override
	protected void initDTO(final DTOStundenplanPausenzeit dto, final Long newId) throws ApiOperationException {
		dto.ID = newId;
		dto.Stundenplan_ID = this.stundenplanID;
	}


	@Override
	public StundenplanPausenzeit map(final DTOStundenplanPausenzeit dto) {
		final StundenplanPausenzeit daten = new StundenplanPausenzeit();
		daten.id = dto.ID;
		daten.wochentag = dto.Tag;
		daten.beginn = dto.Beginn;
		daten.ende = dto.Ende;
		daten.bezeichnung = dto.Bezeichnung;
		return daten;
	}


	@Override
	protected void mapAttribute(final DBEntityManager conn, final DTOStundenplanPausenzeit dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST, "Die IDs der Daten des Patches stimmen nicht mit der ID des Aufrufs überein.");
			}
			case "wochentag" -> dto.Tag = JSONMapper.convertToIntegerInRange(value, false, 1, 8);
			case "beginn" -> dto.Beginn = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
			case "ende" -> dto.Ende = JSONMapper.convertToIntegerInRange(value, true, 0, 1440);
			case "bezeichnung" -> dto.Bezeichnung = JSONMapper.convertToString(value, false, false, 40);
			case "klassen" -> {
				// Prüfe zunächst die übergebenen IDs, ob diese auch IDs von Klassen des Schuljahresabschnittes des Stundenplans sind
				final DTOStundenplan dtoStundenplan = conn.queryByKey(DTOStundenplan.class, dto.Stundenplan_ID);
				final List<Long> idsKlassen = JSONMapper.convertToListOfLong(value, false);
				final List<DTOKlassen> klassen = idsKlassen.isEmpty() ? new ArrayList<>() : conn.queryByKeyList(DTOKlassen.class, idsKlassen);
				if (idsKlassen.size() != klassen.size())
					throw new ApiOperationException(Status.BAD_REQUEST, "Nicht alle angegebenen Klassen-IDs lassen sich Klassen zuordnen");
				final List<DTOKlassen> klassenVonStundenplan =
						klassen.stream().filter(k -> k.Schuljahresabschnitts_ID == dtoStundenplan.Schuljahresabschnitts_ID).toList();
				if (klassen.size() != klassenVonStundenplan.size())
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Nicht alle angegebenen Klassen-IDs gehören zu Klassen des Schuljahresabschnittes des Stundenplans");
				// Bestimme die bereits existierenden Klasseneinträge und vergleiche diese mit den geforderten
				final List<DTOStundenplanPausenzeitKlassenzuordnung> existing = conn.queryList(DTOStundenplanPausenzeitKlassenzuordnung.QUERY_BY_PAUSENZEIT_ID,
						DTOStundenplanPausenzeitKlassenzuordnung.class, dto.ID);
				final Map<Long, DTOStundenplanPausenzeitKlassenzuordnung> mapExisting =
						existing.stream().collect(Collectors.toMap(pkl -> pkl.Klassen_ID, pkl -> pkl));
				final Set<Long> idsKlassenNeu = new HashSet<>();
				final Set<Long> idsKlassenVorhanden = new HashSet<>();
				for (final long idKlasse : idsKlassen) {
					if (mapExisting.keySet().contains(idKlasse))
						idsKlassenVorhanden.add(idKlasse);
					else
						idsKlassenNeu.add(idKlasse);
				}
				// Entferne die alten Klassenzuordnungen
				mapExisting.keySet().removeAll(idsKlassenVorhanden);
				for (final DTOStundenplanPausenzeitKlassenzuordnung pkl : mapExisting.values())
					conn.transactionRemove(pkl);
				// Erzeuge die neuen Klassenzuordnungen
				long idNext = conn.transactionGetNextID(DTOStundenplanPausenzeitKlassenzuordnung.class);
				for (final long idKlasse : idsKlassenNeu)
					conn.transactionPersist(new DTOStundenplanPausenzeitKlassenzuordnung(idNext++, dto.ID, idKlasse));
				conn.transactionFlush();
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}


	/**
	 * Gibt die Pausenzeiten des Stundenplans zurück.
	 *
	 * @return die Liste der Pausenzeiten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@Override
	public List<StundenplanPausenzeit> getList() throws ApiOperationException {
		final List<DTOStundenplanPausenzeit> pausenzeiten = conn.queryList(DTOStundenplanPausenzeit.QUERY_BY_STUNDENPLAN_ID,
				DTOStundenplanPausenzeit.class, this.stundenplanID);
		final List<Long> idsPausenzeiten = pausenzeiten.stream().map(p -> p.ID).toList();
		final Map<Long, List<Long>> mapKlassen = idsPausenzeiten.isEmpty() ? new HashMap<>()
				: conn.queryList(DTOStundenplanPausenzeitKlassenzuordnung.QUERY_LIST_BY_PAUSENZEIT_ID, DTOStundenplanPausenzeitKlassenzuordnung.class,
						idsPausenzeiten).stream()
						.collect(Collectors.groupingBy(pkz -> pkz.Pausenzeit_ID, Collectors.mapping(pkz -> pkz.Klassen_ID, Collectors.toUnmodifiableList())));
		final ArrayList<StundenplanPausenzeit> daten = new ArrayList<>();
		for (final DTOStundenplanPausenzeit p : pausenzeiten) {
			final StundenplanPausenzeit mapped = map(p);
			final List<Long> idsKlassen = mapKlassen.get(p.ID);
			if (idsKlassen != null)
				mapped.klassen.addAll(idsKlassen);
			daten.add(mapped);
		}
		return daten;
	}

	@Override
	public StundenplanPausenzeit getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Pausenzeit eines Stundenplans mit der ID null ist unzulässig.");
		final DTOStundenplanPausenzeit pausenzeit = conn.queryByKey(DTOStundenplanPausenzeit.class, id);
		if (pausenzeit == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Pausenzeit eines Stundenplans mit der ID %d gefunden.".formatted(id));
		final List<Long> klassen = conn.queryList(DTOStundenplanPausenzeitKlassenzuordnung.QUERY_BY_PAUSENZEIT_ID,
				DTOStundenplanPausenzeitKlassenzuordnung.class, id).stream().map(pkz -> pkz.Klassen_ID).toList();
		final StundenplanPausenzeit daten = map(pausenzeit);
		daten.klassen.addAll(klassen);
		return daten;
	}


	/**
	 * Prüft vor dem Löschen von Pausenzeiten, ob diese alle zu dem Stundenplan gehören.
	 *
	 * @param dtos    die zu löschenden DTOs
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@Override
	public void checkBeforeDeletion(final List<DTOStundenplanPausenzeit> dtos) throws ApiOperationException {
		for (final DTOStundenplanPausenzeit dto : dtos)
			if (dto.Stundenplan_ID != this.stundenplanID)
				throw new ApiOperationException(Status.BAD_REQUEST, "Der Pausenzeit-Eintrag gehört nicht zu dem angegebenen Stundenplan.");
	}

}
