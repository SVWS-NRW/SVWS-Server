package de.svws_nrw.data.schule;

import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;
import java.util.Map;

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
		setAttributesRequiredOnCreation("bezeichnung", "schluessel", "personTyp");
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
		return daten;
	}


	@Override
	public List<Einwilligungsart> getAll() throws ApiOperationException {
		final List<DTOKatalogEinwilligungsart> katalog = conn.queryAll(DTOKatalogEinwilligungsart.class);
		if (katalog == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Einwilligungsarten gefunden.");

		return katalog.stream().map(this::map).toList();
	}


	@Override
	public Einwilligungsart getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Einwilligungsart mit der ID null ist unzulässig.");
		final DTOKatalogEinwilligungsart einwilligung = conn.queryByKey(DTOKatalogEinwilligungsart.class, id);
		if (einwilligung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Einwilligungsart mit der ID %d wurde nicht gefunden.".formatted(id));
		return map(einwilligung);
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


	private String validateBezeichnung(final Object value, final PersonTyp personTyp) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Datenschutz.col_Bezeichnung.datenlaenge());
		final List<DTOKatalogEinwilligungsart> bezeichnungenFiltered = conn.queryList(
				"SELECT e FROM DTOKatalogEinwilligungsart e WHERE e.Bezeichnung = ?1 AND e.personTyp = ?2", DTOKatalogEinwilligungsart.class,
				bezeichnung, personTyp);
		if (!bezeichnungenFiltered.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Bezeichnung '%s' wird bereits für eine andere Einwilligungsart des gleichen Personentyps ('%s') genutzt.".formatted(bezeichnung,
							personTyp.bezeichnung));
		return bezeichnung;
	}


	private String validateSchluessel(final Object value, final PersonTyp personTyp) throws ApiOperationException {
		final String schluessel = JSONMapper.convertToString(value, false, false, Schema.tab_K_Datenschutz.col_Schluessel.datenlaenge());
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
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (Long.compare(patch_id, dto.ID) != 0))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die angegebene ID %d ist null oder stimmt nicht mit der ID %d im DTO überein.".formatted(patch_id, dto.ID));
			}
			case "bezeichnung" -> dto.Bezeichnung = validateBezeichnung(value, personTyp);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false);
			case "schluessel" -> dto.Schluessel = validateSchluessel(value, personTyp);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false);
			case "beschreibung" -> dto.Beschreibung =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Datenschutz.col_Beschreibung.datenlaenge());
			case "personTyp" -> {
				final PersonTyp convertedValue = PersonTyp.getByID(JSONMapper.convertToInteger(value, false));
				validateBezeichnung(map.containsKey("bezeichnung") ? map.get("bezeichnung") : dto.Bezeichnung, convertedValue);
				validateSchluessel(map.containsKey("schluessel") ? map.get("schluessel") : dto.Schluessel, convertedValue);
				dto.personTyp = convertedValue;
				if (dto.personTyp == null)
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d ist für den Personentyp ungültig.".formatted(JSONMapper.convertToInteger(value, false)));
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}
}
