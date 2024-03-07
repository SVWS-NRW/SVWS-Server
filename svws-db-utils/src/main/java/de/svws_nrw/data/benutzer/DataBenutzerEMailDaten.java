package de.svws_nrw.data.benutzer;

import java.io.InputStream;
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
import de.svws_nrw.db.utils.OperationError;
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
        daten.name = b.EmailName == null ? "" : b.EmailName;
        daten.address = b.Email == null ? "" : b.Email;
        daten.usernameSMTP = b.SMTPUsername == null ? "" : b.SMTPUsername;
        daten.passwordSMTP = b.SMTPPassword == null ? "" : b.SMTPPassword;
        daten.signatur = b.EMailSignature == null ? "" : b.EMailSignature;
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

    private @NotNull DTOBenutzerMail getOrCreateDTO(final Long id) {
    	if (id == null)
    		throw OperationError.BAD_REQUEST.exception("Es wurde keine ID angegeben.");
    	// Prüfe, ob die ID mit der ID des Benutzers der Verbindung
    	if (!Objects.equals(id, this.conn.getUser().getId()))
    		throw OperationError.FORBIDDEN.exception("Nur der angemeldete Benutzer darf seine SMTP-Verbindungsdaten auslesen.");
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

    @Override
    public Response get(final Long id) {
    	final @NotNull DTOBenutzerMail dto = getOrCreateDTO(id);
    	final BenutzerEMailDaten daten = dtoMapper.apply(dto);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

	private static final Map<String, DataBasicMapper<DTOBenutzerMail>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.Benutzer_ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("name", (conn, dto, value, map) -> dto.EmailName = JSONMapper.convertToString(value, true, false, 255)),
		Map.entry("address", (conn, dto, value, map) -> dto.Email = JSONMapper.convertToString(value, true, false, 255)),
		Map.entry("usernameSMTP", (conn, dto, value, map) -> dto.SMTPUsername = JSONMapper.convertToString(value, true, false, 255)),
		Map.entry("passwordSMTP", (conn, dto, value, map) -> {
			final String password = JSONMapper.convertToString(value, true, false, 127);
			final AES aes = conn.getUser().getAES();
			if (aes == null)
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Konnte kein AES-Verschlüsselungsobject für den Benutzer finden.");
			try {
				dto.SMTPPassword = aes.encryptBase64(password.getBytes());
			} catch (@SuppressWarnings("unused") final AESException e) {
				throw OperationError.INTERNAL_SERVER_ERROR.exception("Fehler beim Verschlüsseln des SMTP-Kennwortes");
			}
		}),
		Map.entry("signatur", (conn, dto, value, map) -> dto.EMailSignature = JSONMapper.convertToString(value, true, false, 2047))
	);


    @Override
    public Response patch(final Long id, final InputStream is) {
    	final DTOBenutzerMail dto = getOrCreateDTO(id);
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.isEmpty())
			return OperationError.NOT_FOUND.getResponse("In dem Patch sind keine Daten enthalten.");
		applyPatchMappings(conn, dto, map, patchMappings, null);
		if (!conn.transactionPersist(dto))
			throw OperationError.INTERNAL_SERVER_ERROR.exception();
		conn.transactionFlush();
		return Response.status(Status.NO_CONTENT).build();
    }

}
