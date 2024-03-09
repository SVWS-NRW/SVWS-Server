package de.svws_nrw.data.email;

import de.svws_nrw.core.data.email.SMTPServerKonfiguration;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schule.DTOSchuleEmail;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SMTPServerKonfiguration}.
 */
public final class DataEmailSMTPServerKonfiguration extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SMTPServerKonfiguration}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataEmailSMTPServerKonfiguration(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchuleEmail} in einen Core-DTO {@link SMTPServerKonfiguration}.
	 */
	private static final Function<DTOSchuleEmail, SMTPServerKonfiguration> dtoMapper = (final DTOSchuleEmail dto) -> {
    	final SMTPServerKonfiguration daten = new SMTPServerKonfiguration();
    	daten.id = dto.ID;
    	daten.host = dto.SMTPServer;
    	daten.port = dto.SMTPPort;
    	daten.useStartTLS = dto.SMTPStartTLS;
    	daten.useTLS = dto.SMTPUseTLS;
    	daten.trustTLSHost = dto.SMTPTrustTLSHost;
		return daten;
	};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}


	/**
	 * Gibt das Datenbank-DTO für die Email-Konfiguration dieser Schule zurück.
	 *
	 * @param conn   die Datenbank-Verbindung.
	 *
	 * @return das DTO
	 */
	private static DTOSchuleEmail getOrCreate(final DBEntityManager conn) {
		DTOSchuleEmail dto = conn.querySingle(DTOSchuleEmail.class);
		if (dto == null) {
			dto = new DTOSchuleEmail(1, "", 25, true, false);
			conn.transactionPersist(dto);
			conn.transactionFlush();
		}
		return dto;
	}


	/**
	 * Gibt die SMTP-Server-Konfiguration dieser Schule zurück.
	 *
	 * @param conn   die Datenbank-Verbindung.
	 *
	 * @return die SMTP-Server-Konfiguration
	 */
	public static SMTPServerKonfiguration getOrCreateSMTPServerKonfiguration(final DBEntityManager conn) {
		return dtoMapper.apply(getOrCreate(conn));
	}


	/**
	 * Ermittelt die SMTP-Server-Konfiguration der Schule
	 *
	 * @param conn   die Datenbank-Verbindung.
	 *
	 * @return die Response mit der SMTP-Server-Konfiguration
	 */
	public static Response get(final DBEntityManager conn) {
		final SMTPServerKonfiguration daten = getOrCreateSMTPServerKonfiguration(conn);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}


	private static final Map<String, DataBasicMapper<DTOSchuleEmail>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("host", (conn, dto, value, map) -> dto.SMTPServer = JSONMapper.convertToString(value, false, true, 256)),
		Map.entry("port", (conn, dto, value, map) -> dto.SMTPPort = JSONMapper.convertToIntegerInRange(value, false, 1, 65536)),
		Map.entry("useStartTLS", (conn, dto, value, map) -> dto.SMTPStartTLS = JSONMapper.convertToBoolean(value, false)),
		Map.entry("useTLS", (conn, dto, value, map) -> dto.SMTPUseTLS = JSONMapper.convertToBoolean(value, false)),
		Map.entry("trustTLSHost", (conn, dto, value, map) -> dto.SMTPTrustTLSHost = JSONMapper.convertToString(value, true, false, 256))
	);


	@Override
	public Response patch(final InputStream is) {
		final DTOSchuleEmail dto = getOrCreate(conn);
		applyPatchMappings(conn, dto, JSONMapper.toMap(is), patchMappings, null);
		conn.transactionPersist(dto);
		conn.transactionFlush();
		return Response.status(Status.NO_CONTENT).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
