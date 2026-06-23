package de.svws_nrw.data.lehrer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.lehrer.LehrerFunktion;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOLeitungsfunktion;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerFunktion;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

public final class DataLehrerPersonalabschnittsdatenLehrerfunktionen extends DataManagerRevised<Long, DTOLehrerFunktion, LehrerFunktion> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn		die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataLehrerPersonalabschnittsdatenLehrerfunktionen(final DBEntityManager conn) {
		super(conn);
		// Eine Änderung der ID oder eine Neu-Zuweisung zu einem anderen Abschnitt eines ggf. anderen Lehrers ist nicht erlaubt
		setAttributesNotPatchable("id", "idAbschnittsdaten");
		// Außer der ID sind alle Attribute beim Erzeugen eines neuen Grundes korrekt zu setzen
		setAttributesRequiredOnCreation("idAbschnittsdaten", "idFunktion");
	}

	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		// Prüfe vor dem Erstellen, ob bereits ein entsprechende Funktion für den Abschnitt in der Datenbank eingetragen ist.
		final long idAbschnittsdaten = JSONMapper.convertToLong(initAttributes.get("idAbschnittsdaten"), false);
		final long idFunktion = JSONMapper.convertToLong(initAttributes.get("idFunktion"), false);
		final List<DTOLehrerFunktion> funktionen = conn.queryList(
					"SELECT p FROM DTOLehrerFunktion p WHERE p.idAbschnitt = ?1 AND p.idFunktion = ?2", DTOLehrerFunktion.class,
					idAbschnittsdaten, idFunktion);
		// Wenn ja, dann ist das Anlegen eines neuen Eintrages unzulässig
		if (!funktionen.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Lehrerfunktion mit der ID %s im Abschnitt mit der ID %s existiert bereits.".formatted(idFunktion, idAbschnittsdaten));
		}
	}

	@Override
	public void checkBeforePersist(final DTOLehrerFunktion dto, final Map<String, Object> patchedAttributes) throws ApiOperationException {
		// Wenn idAbschnittsdaten oder idGrund nicht bereits in das DTO eingetragen sind, dann liegt hier ein Fehler vor. Ein Patch muss hier bereits angewendet sein.
		if (dto.idAbschnitt < 0) {
			throw new ApiOperationException(Status.BAD_REQUEST,
							"Eine negative ID %d für den Abschnitt des Lehrers ist nicht zulässig.".formatted(dto.idAbschnitt));
		}
		if (Long.valueOf(dto.idFunktion) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist für die Funktion nicht zulässig.");
		}

		// Überprüfe, ob ein anderes DTO-Objekt mit dem Abschnitt und der Funktion in der DB bereits existiert.
		final List<DTOLehrerFunktion> funktionen = conn.queryList(
				"SELECT p FROM DTOLehrerFunktion p WHERE p.idAbschnitt = ?1 AND p.idFunktion = ?2 AND p.id != ?3", DTOLehrerFunktion.class,
				dto.idAbschnitt, dto.idFunktion, dto.id);
		// Wenn ja, dann ist das Anlegen eines neuen Eintrages unzulässig.
		if (!funktionen.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Lehrerfunktion mit der ID %s im Abschnitt mit der ID %s existiert bereits.".formatted(dto.idFunktion, dto.idAbschnitt));
		}
	}

	@Override
	protected void initDTO(final DTOLehrerFunktion dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.id = newID;
	}
	protected static LehrerFunktion mapInternal(final DTOLehrerFunktion dto, final DBEntityManager conn) throws ApiOperationException {
		final LehrerFunktion daten = new LehrerFunktion();
		daten.id = dto.id;
		daten.idAbschnittsdaten = dto.idAbschnitt;
		daten.idFunktion = dto.idFunktion;
		return daten;
	}

	@Override
	protected LehrerFunktion map(final DTOLehrerFunktion dto) throws ApiOperationException {
		return mapInternal(dto, conn);
	}

	@Override
	protected void mapAttribute(final DTOLehrerFunktion dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idAbschnittsdaten" -> updateAbschnittID(dto,  JSONMapper.convertToLong(value, false));
			case "idFunktion" -> dto.idFunktion = updateIdFunktion(JSONMapper.convertToLong(value, false));
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	public LehrerFunktion getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Mehrleistungsstunden mit der ID %d gefunden.".formatted(id));
		}
		final DTOLehrerFunktion dto = conn.queryByKey(DTOLehrerFunktion.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}
		return map(dto);
	}

	/**
	 * Ermittelt die Lehrerfunktionen für die Lehrerabschnittsdaten mit der angegebenen ID und und gibt diese zurück.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idLehrerabschnittsdaten   die ID der Lehrerabschnittsdaten
	 *
	 * @return die Liste mit den Lehrerfunktionen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<LehrerFunktion> getByLehrerabschnittsdatenId(final DBEntityManager conn,
			final long idLehrerabschnittsdaten) throws ApiOperationException {
		final List<LehrerFunktion> result = new ArrayList<>();
		// Bestimme die Anrechungen für die Lehrerabschnittsdaten
		final List<DTOLehrerFunktion> dtos = conn.queryList(DTOLehrerFunktion.QUERY_BY_IDABSCHNITT,
				DTOLehrerFunktion.class, idLehrerabschnittsdaten);
		if (dtos == null) {
			return result;
		}
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerFunktion dto : dtos) {
			result.add(mapInternal(dto, conn));
		}
		return result;
	}

	private void updateAbschnittID(final DTOLehrerFunktion dto, final Long abschnittID) throws ApiOperationException {
		if ((abschnittID != null) && ((abschnittID < 0 || (conn.queryByKey(DTOLehrerAbschnittsdaten.class, abschnittID)) == null))) {
			throw new ApiOperationException(Status.CONFLICT, "AbschnittID %d ungültig.".formatted(abschnittID));
		}
		if (abschnittID == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "AbschnittID darf nicht null sein.");
		}
		dto.idAbschnitt = abschnittID;
	}

	private long updateIdFunktion(final Long idFunktion) throws ApiOperationException {
		final DTOLeitungsfunktion f = conn.queryByKey(DTOLeitungsfunktion.class, idFunktion);
		if (f == null) {
			throw new ApiOperationException(Status.NOT_FOUND,  "Keine Funktion mit der ID  %d gefunden.".formatted(idFunktion));
		}
		return f.ID;
	}

}
