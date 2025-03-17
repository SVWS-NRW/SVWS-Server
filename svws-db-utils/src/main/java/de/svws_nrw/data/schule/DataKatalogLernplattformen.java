package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO {@link Lernplattform}.
 */
public final class DataKatalogLernplattformen extends DataManagerRevised<Long, DTOLernplattformen, Lernplattform> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Lernplattform}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogLernplattformen(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	public Lernplattform map(final DTOLernplattformen dtoKatalogLernplattformen) {
		final Lernplattform daten = new Lernplattform();
		daten.id = dtoKatalogLernplattformen.ID;
		daten.bezeichnung = dtoKatalogLernplattformen.Bezeichnung;
		daten.benutzernameSuffixLehrer = dtoKatalogLernplattformen.BenutzernameSuffixLehrer;
		daten.benutzernameSuffixErzieher = dtoKatalogLernplattformen.BenutzernameSuffixErzieher;
		daten.benutzernameSuffixSchueler = dtoKatalogLernplattformen.BenutzernameSuffixSchueler;
		daten.konfiguration = dtoKatalogLernplattformen.Konfiguration;
		daten.anzahlEinwilligungen = 0;
		return daten;
	}

	/**
	 * Konvertiert ein DTOLernplattformen-Objekt in ein Lernplattform-Objekt und setzt die Anzahl der Einwilligungen.
	 *
	 * @param dtoLernplattformen Das DTOLernplattformen-Objekt, das konvertiert werden soll.
	 * @param anzahlEinwilligungen Die Anzahl der Einwilligungen, die gesetzt werden sollen.
	 *
	 * @return Ein Lernplattform-Objekt, das aus dem DTOLernplattformen-Objekt konvertiert und mit der Anzahl der Einwilligungen gesetzt wurde.
	 */
	public Lernplattform map(final DTOLernplattformen dtoLernplattformen, final int anzahlEinwilligungen) {
		final Lernplattform lp = map(dtoLernplattformen);
		lp.anzahlEinwilligungen = anzahlEinwilligungen;
		return lp;
	}

	//TODO Anzahl der Einwilligungen für alle Personentypen definieren
	@Override
	public List<Lernplattform> getAll() throws ApiOperationException {
		final List<DTOLernplattformen> mapLernplattform = conn.queryAll(DTOLernplattformen.class);
		return mapLernplattform.stream()
				.map(this::map).toList();
	}

	@Override
	public Lernplattform getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Lernplattform mit der ID null ist unzulässig.");
		final DTOLernplattformen lernplattform = conn.queryByKey(DTOLernplattformen.class, id);
		if (lernplattform == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Lernplattform mit der ID %d wurde nicht gefunden.".formatted(id));
		return map(lernplattform);
	}

	@Override
	protected void initDTO(final DTOLernplattformen dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Bezeichnung = "";
		dto.BenutzernameSuffixLehrer = "";
		dto.BenutzernameSuffixErzieher = "";
		dto.BenutzernameSuffixSchueler = "";
		dto.Konfiguration = "";
	}

	private String validateBezeichnung(final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, 255, name);
		final List<DTOLernplattformen> bezeichnungenFiltered = conn.queryList(
				"SELECT e FROM DTOLernplattformen e WHERE e.Bezeichnung = ?1", DTOLernplattformen.class, bezeichnung);
		if (!bezeichnungenFiltered.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Bezeichnung '%s' wird bereits für eine andere Lernplattform genutzt.".formatted(bezeichnung));
		return bezeichnung;
	}

	@Override
	protected void mapAttribute(final DTOLernplattformen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(id, dto.ID));
			}
			case "bezeichnung" -> dto.Bezeichnung = validateBezeichnung(value, name);
			case "benutzernameSuffixLehrer" -> dto.BenutzernameSuffixLehrer =
					JSONMapper.convertToString(value, true, true, 20, "benutzernameSuffixLehrer");
			case "benutzernameSuffixErzieher" -> dto.BenutzernameSuffixErzieher =
					JSONMapper.convertToString(value, true, true, 20, "benutzernameSuffixErzieher");
			case "benutzernameSuffixSchueler" -> dto.BenutzernameSuffixSchueler =
					JSONMapper.convertToString(value, true, true, 20, "benutzernameSuffixSchueler");
			case "konfiguration" -> dto.Konfiguration = JSONMapper.convertToString(value, true, true, 255, "konfiguration");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}
}
