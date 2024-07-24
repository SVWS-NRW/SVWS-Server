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
		daten.sortierung = dtoKatalogEinwilligungsart.Sortierung;
		daten.personTyp = dtoKatalogEinwilligungsart.personTyp.id;
		daten.schluessel = (dtoKatalogEinwilligungsart.Schluessel == null) ? "" : dtoKatalogEinwilligungsart.Schluessel;
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
	protected void initDTO(final DTOKatalogEinwilligungsart dto, final Long id) {
		dto.ID = id;
		dto.Bezeichnung = "";
		dto.Sortierung = 32000;
		dto.personTyp = PersonTyp.SCHUELER;
		dto.Schluessel = "";
	}


	@Override
	protected void mapAttribute(final DBEntityManager conn, final DTOKatalogEinwilligungsart dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (Long.compare(patch_id, dto.ID) != 0))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die angegebene ID %d ist null oder stimmt nicht mit der ID %d im DTO überein.".formatted(patch_id, dto.ID));
			}
			case "bezeichnung" -> {
				final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Datenschutz.col_Bezeichnung.datenlaenge());
				final List<DTOKatalogEinwilligungsart> bezeichnungen =
						conn.queryList(DTOKatalogEinwilligungsart.QUERY_BY_BEZEICHNUNG, DTOKatalogEinwilligungsart.class, bezeichnung);
				if (!bezeichnungen.isEmpty())
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die Bezeichnung '%s' wird bereits für eine andere Einwilligungsart genutzt.".formatted(bezeichnung));
				dto.Bezeichnung = bezeichnung;
			}
			case "personTyp" -> {
				final int idPersonTyp = JSONMapper.convertToInteger(value, false);
				dto.personTyp = PersonTyp.getByID(idPersonTyp);
				if (dto.personTyp == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d ist für den Personentyp ungültig.".formatted(idPersonTyp));
			}
			case "schluessel" -> dto.Schluessel =
					JSONMapper.convertToString(value, false, false, Schema.tab_K_Datenschutz.col_Schluessel.datenlaenge());
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}

}
