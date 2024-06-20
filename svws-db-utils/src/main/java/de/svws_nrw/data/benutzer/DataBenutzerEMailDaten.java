package de.svws_nrw.data.benutzer;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.core.data.benutzer.BenutzerDaten;
import de.svws_nrw.core.data.benutzer.BenutzerEMailDaten;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerMail;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerEMailDaten}.
 */
public final class DataBenutzerEMailDaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link BenutzerEMailDaten}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBenutzerEMailDaten(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOViewBenutzerdetails} in einen Core-DTO {@link BenutzerDaten}.
	 */
	private final Function<DTOBenutzerMail, BenutzerEMailDaten> dtoMapper = (final DTOBenutzerMail b) -> {
		final BenutzerEMailDaten daten = new BenutzerEMailDaten();
		daten.id = b.Benutzer_ID;
		daten.name = (b.EmailName == null) ? "" : b.EmailName;
		daten.address = (b.Email == null) ? "" : b.Email;
		daten.usernameSMTP = (b.SMTPUsername == null) ? "" : b.SMTPUsername;
		daten.passwordSMTP = (b.SMTPPassword == null) ? "" : b.SMTPPassword;
		daten.signatur = (b.EMailSignature == null) ? "" : b.EMailSignature;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	private static @NotNull DTOBenutzerMail getOrCreateDTO(final DBEntityManager conn, final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde keine ID angegeben.");
		// Prüfe, ob die ID mit der ID des Benutzers der Verbindung
		if (!Objects.equals(id, conn.getUser().getId()))
			throw new ApiOperationException(Status.FORBIDDEN, "Nur der angemeldete Benutzer darf seine SMTP-Verbindungsdaten auslesen.");
		DTOBenutzerMail dto = conn.queryByKey(DTOBenutzerMail.class, id);
		if (dto == null) {
			dto = new DTOBenutzerMail(id, "", "");
			dto.SMTPUsername = "";
			dto.SMTPPassword = "";
			dto.EMailSignature = "";
			conn.transactionPersist(dto);
			conn.transactionFlush();
		}
		return dto;
	}

	/**
	 * Gib das DB-DTO mit den Benutzer-spezifischen EMail-Daten zurück.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return das DB-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static DTOBenutzerMail getOrCreateDTO(final DBEntityManager conn) throws ApiOperationException {
		return getOrCreateDTO(conn, conn.getUser().getId());
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		final @NotNull DTOBenutzerMail dto = getOrCreateDTO(conn, id);
		final BenutzerEMailDaten daten = dtoMapper.apply(dto);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private static final Map<String, DataBasicMapper<DTOBenutzerMail>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.Benutzer_ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}),
			Map.entry("name", (conn, dto, value, map) -> dto.EmailName = JSONMapper.convertToString(value, true, false, 255)),
			Map.entry("address", (conn, dto, value, map) -> dto.Email = JSONMapper.convertToString(value, true, false, 255)),
			Map.entry("usernameSMTP", (conn, dto, value, map) -> dto.SMTPUsername = JSONMapper.convertToString(value, true, false, 255)),
			Map.entry("passwordSMTP", (conn, dto, value, map) -> {
				final String password = JSONMapper.convertToString(value, true, false, 127);
				final AES aes = conn.getUser().getAES();
				if (aes == null)
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Konnte kein AES-Verschlüsselungsobject für den Benutzer finden.");
				try {
					dto.SMTPPassword = aes.encryptBase64(password.getBytes());
				} catch (@SuppressWarnings("unused") final AESException e) {
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Verschlüsseln des SMTP-Kennwortes");
				}
			}),
			Map.entry("signatur", (conn, dto, value, map) -> dto.EMailSignature = JSONMapper.convertToString(value, true, false, 2047)));


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		final DTOBenutzerMail dto = getOrCreateDTO(conn, id);
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "In dem Patch sind keine Daten enthalten.");
		applyPatchMappings(conn, dto, map, patchMappings, Collections.emptySet(), null);
		if (!conn.transactionPersist(dto))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		conn.transactionFlush();
		return Response.status(Status.NO_CONTENT).build();
	}

}
