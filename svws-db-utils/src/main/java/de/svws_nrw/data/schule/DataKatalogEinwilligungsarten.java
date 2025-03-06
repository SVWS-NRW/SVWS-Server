package de.svws_nrw.data.schule;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schueler.DataSchuelerEinwilligungen;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link Einwilligungsart}.
 */
public final class DataKatalogEinwilligungsarten extends DataManagerRevised<Long, DTOKatalogEinwilligungsart, Einwilligungsart> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Einwilligungsart}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogEinwilligungsarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung", "personTyp");
	}


	@Override
	public Einwilligungsart map(final DTOKatalogEinwilligungsart dtoKatalogEinwilligungsart) {
		final Einwilligungsart daten = new Einwilligungsart();
		daten.id = dtoKatalogEinwilligungsart.ID;
		daten.bezeichnung = (dtoKatalogEinwilligungsart.Bezeichnung == null) ? "" : dtoKatalogEinwilligungsart.Bezeichnung;
		daten.sichtbar = (dtoKatalogEinwilligungsart.Sichtbar == null) || dtoKatalogEinwilligungsart.Sichtbar;
		daten.schluessel = (dtoKatalogEinwilligungsart.Schluessel == null) ? "" : dtoKatalogEinwilligungsart.Schluessel;
		daten.sortierung = dtoKatalogEinwilligungsart.Sortierung;
		daten.beschreibung = (dtoKatalogEinwilligungsart.Beschreibung == null) ? "" : dtoKatalogEinwilligungsart.Beschreibung;
		daten.personTyp = dtoKatalogEinwilligungsart.personTyp.id;
		daten.anzahlEinwilligungen = 0;
		return daten;
	}

	/**
	 * Konvertiert ein DTOKatalogEinwilligungsart-Objekt in ein EinwilligungsartEintrag-Objekt und setzt die Anzahl der Einwilligungen.
	 *
	 * @param dtoKatalogEinwilligungsart Das DTOEinwilligungArt-Objekt, das konvertiert werden soll.
	 * @param anzahlEinwilligungen Die Anzahl der Einwilligungen, die gesetzt werden sollen.
	 *
	 * @return Ein EinwilligungskartEintrag-Objekt, das aus dem DTOEinwilligungArt-Objekt konvertiert und mit der Anzahl der Einwilligungen gesetzt wurde.
	 */
	public Einwilligungsart map(final DTOKatalogEinwilligungsart dtoKatalogEinwilligungsart, final int anzahlEinwilligungen) {
		final Einwilligungsart ea = map(dtoKatalogEinwilligungsart);
		ea.anzahlEinwilligungen = anzahlEinwilligungen;
		return ea;
	}


	@Override
	public List<Einwilligungsart> getAll() throws ApiOperationException {
		final List<DTOKatalogEinwilligungsart> katalog = conn.queryAll(DTOKatalogEinwilligungsart.class);
		final Map<Long, Long> mapAnzahlEinwilligungenByEinwilligungsart = conn.queryList(DTOSchuelerDatenschutz.QUERY_ALL.concat(" WHERE e.Datenschutz_ID IS NOT NULL"),
				DTOSchuelerDatenschutz.class).stream().collect(Collectors.groupingBy(s -> s.Datenschutz_ID, Collectors.counting()));
		return katalog.stream().map(ea -> map(ea, mapAnzahlEinwilligungenByEinwilligungsart.computeIfAbsent(ea.ID, k -> 0L).intValue())).toList();
	}


	@Override
	public Einwilligungsart getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Einwilligungsart mit der ID null ist unzulässig.");
		final DTOKatalogEinwilligungsart einwilligung = conn.queryByKey(DTOKatalogEinwilligungsart.class, id);
		if (einwilligung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Einwilligungsart mit der ID %d wurde nicht gefunden.".formatted(id));
		final int anzahlEinwilligungen = conn.queryList(DTOSchuelerDatenschutz.QUERY_BY_DATENSCHUTZ_ID.replace("SELECT e ", "SELECT COUNT(e) "),
				DTOSchuelerDatenschutz.class, einwilligung.ID).size();
		return map(einwilligung, anzahlEinwilligungen);
	}


	@Override
	protected void initDTO(final DTOKatalogEinwilligungsart dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Bezeichnung = "";
		dto.Sichtbar = true;
		dto.Schluessel = "";
		dto.Sortierung = 32000;
		dto.Beschreibung = "";
		dto.personTyp = PersonTyp.SCHUELER;
	}

	@Override
	protected Einwilligungsart addBasic(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Einwilligungsart neueEinwilligungsart = super.addBasic(newID, initAttributes);
		if (neueEinwilligungsart.personTyp == PersonTyp.LEHRER.id) {
			final List<DTOLehrerDatenschutz> lehrerEinwilligungen = new ArrayList<>();
			final List<Long> lehrerIds = conn.queryList("SELECT e.ID FROM DTOLehrer e", Long.class);
			for (final Long lehrerId : lehrerIds) {
				final DTOLehrerDatenschutz dto = new DTOLehrerDatenschutz(lehrerId, neueEinwilligungsart.id, false, false);
				lehrerEinwilligungen.add(dto);
			}
		conn.transactionPersistAll(lehrerEinwilligungen);
		} else if (neueEinwilligungsart.personTyp == PersonTyp.SCHUELER.id) {
			final List<Long> schuelerIds = conn.queryList("SELECT e.ID FROM DTOSchueler e", Long.class);
			final List<DTOSchuelerDatenschutz> schuelerEinwilligungen = new ArrayList<>();
			for (final Long schuelerId : schuelerIds) {
				final DTOSchuelerDatenschutz dto = new DTOSchuelerDatenschutz(schuelerId, neueEinwilligungsart.id, false, false);
				schuelerEinwilligungen.add(dto);
			}
			conn.transactionPersistAll(schuelerEinwilligungen);
		} else {
			throw new ApiOperationException(Status.BAD_REQUEST, "Unbekannter Personentyp: " + neueEinwilligungsart.personTyp);
		}
		return neueEinwilligungsart;
	}


	private String validateBezeichnung(final Object value, final PersonTyp personTyp, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Datenschutz.col_Bezeichnung.datenlaenge(), name);
		final List<DTOKatalogEinwilligungsart> bezeichnungenFiltered = conn.queryList(
				"SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Bezeichnung = ?1 AND e.personTyp = ?2", DTOKatalogEinwilligungsart.class,
				bezeichnung, personTyp);
		if (!bezeichnungenFiltered.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Bezeichnung '%s' wird bereits für eine andere Einwilligungsart des gleichen Personentyps ('%s') genutzt.".formatted(bezeichnung,
							personTyp.bezeichnung));
		return bezeichnung;
	}


	private String validateSchluessel(final Object value, final PersonTyp personTyp, final String name) throws ApiOperationException {
		final String schluessel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Datenschutz.col_Schluessel.datenlaenge(), name);
		if (schluessel.isEmpty()) {
			return schluessel;
		}
		final List<DTOKatalogEinwilligungsart> schluesselFiltered = conn.queryList(
				"SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Schluessel = ?1 AND e.personTyp = ?2", DTOKatalogEinwilligungsart.class,
				schluessel, personTyp);
		if (!schluesselFiltered.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Schlüssel '%s' wird bereits für eine andere Einwilligungsart des gleichen Personentyps ('%s') genutzt.".formatted(schluessel,
							personTyp.bezeichnung));
		return schluessel;
	}


	@Override
	protected void mapAttribute(final DTOKatalogEinwilligungsart dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		final PersonTyp personTyp = (map.containsKey("personTyp")) ? PersonTyp.getByID(JSONMapper.convertToInteger(map.get("personTyp"), true)) : dto.personTyp;
		switch (name) {
			case "id" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true, name);
				if ((patch_id == null) || (Long.compare(patch_id, dto.ID) != 0))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die angegebene ID %d ist null oder stimmt nicht mit der ID %d im DTO überein.".formatted(patch_id, dto.ID));
			}
			case "bezeichnung" -> dto.Bezeichnung = validateBezeichnung(value, personTyp, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "schluessel" -> dto.Schluessel = validateSchluessel(value, personTyp, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "beschreibung" -> dto.Beschreibung =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Datenschutz.col_Beschreibung.datenlaenge(), name);
			case "personTyp" -> {
				final PersonTyp convertedValue = PersonTyp.getByID(JSONMapper.convertToInteger(value, false, name));
				validateBezeichnung(map.containsKey("bezeichnung") ? map.get("bezeichnung") : dto.Bezeichnung, convertedValue, name);
				validateSchluessel(map.containsKey("schluessel") ? map.get("schluessel") : dto.Schluessel, convertedValue, name);
				dto.personTyp = convertedValue;
				if (dto.personTyp == null)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d ist für den Personentyp ungültig.".formatted(JSONMapper.convertToInteger(value, false, name)));
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}

	/**
	 * Löscht mehrere Einwilligungsarten und gibt das Ergebnis der Lösch-Operationen als Liste von {@link SimpleOperationResponse} zurück.
	 *
	 * @param ids   die IDs der zu löschenden Einwilligungsarten
	 *
	 * @return die Response mit einer Liste von {@link SimpleOperationResponse} zu den angefragten Lösch-Operationen.
	 */
	@Override
	public Response deleteMultipleAsResponse(final List<Long> ids) {
		// Bestimme die Datenbank-DTOs der Einwilligungsarten
		final List<DTOKatalogEinwilligungsart> einwilligungsArten = this.conn.queryByKeyList(DTOKatalogEinwilligungsart.class, ids).stream().toList();

		// Prüfe, ob das Löschen der Einwilligungsarten erlaubt ist
		final Map<Long, SimpleOperationResponse> mapResponses = einwilligungsArten.stream()
				.collect(Collectors.toMap(r -> r.ID, this::checkDeletePreConditions));

		// Lösche die Einwilligungsarten und gib den Erfolg in der Response zurück
		for (final DTOKatalogEinwilligungsart dtoEinwilligungsArt : einwilligungsArten) {
			final SimpleOperationResponse operationResponse = mapResponses.get(dtoEinwilligungsArt.ID);
			if (operationResponse == null)
				throw new DeveloperNotificationException("Das SimpleOperationResponse Objekt zu der ID %d existiert nicht.".formatted(dtoEinwilligungsArt.ID));

			if (operationResponse.log.isEmpty())
				operationResponse.success = this.conn.transactionRemove(dtoEinwilligungsArt);
		}

		return Response.ok().entity(mapResponses.values()).build();
	}

	/**
	 * Diese Methode prüft, ob alle Vorbedingungen zum Löschen einer Einwilligungsart erfüllt sind.
	 * Es wird eine {@link SimpleOperationResponse} zurückgegeben.
	 *
	 * @param dtoEinwilligungsArt   das DTO der Einwilligungsart, die gelöscht werden soll
	 *
	 * @return Liefert eine Response mit dem Log der Vorbedingungsprüfung zurück.
	 */
	private SimpleOperationResponse checkDeletePreConditions(final @NotNull DTOKatalogEinwilligungsart dtoEinwilligungsArt) {
		final SimpleOperationResponse operationResponse = new SimpleOperationResponse();
		operationResponse.id = dtoEinwilligungsArt.ID;

		// Kein Schüler darf Einwilligungen dieser Einwilligungsart haben
		final List<Long> einwilligungIds = new DataSchuelerEinwilligungen(conn, 1L).getIDsByEinwilligungsartId(dtoEinwilligungsArt.ID);
		if (!einwilligungIds.isEmpty())
			operationResponse.log.add(
					"Einwilligungsart %s (ID: %d) hat noch %d verknüpfte(n) Einwilligungen.".formatted(dtoEinwilligungsArt.Bezeichnung, dtoEinwilligungsArt.ID, einwilligungIds.size()));

		return operationResponse;
	}
}
