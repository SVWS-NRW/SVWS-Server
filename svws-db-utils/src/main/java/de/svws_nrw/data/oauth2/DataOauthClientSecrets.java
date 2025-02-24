package de.svws_nrw.data.oauth2;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.oauth2.OAuth2ClientConnection;
import de.svws_nrw.core.types.oauth2.OAuth2ServerTyp;
import de.svws_nrw.data.DTOMapper;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link OAuth2ClientConnection}.
 */
public final class DataOauthClientSecrets extends DataManager<Long> {

	/** Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchuleOAuthSecrets} in einen Core-DTO {@link OAuth2ClientConnection}. */
	private static final DTOMapper<DTOSchuleOAuthSecrets, OAuth2ClientConnection> dtoMapper = (final DTOSchuleOAuthSecrets secrets) -> {
		final OAuth2ClientConnection daten = new OAuth2ClientConnection();
		daten.id = secrets.ID;
		daten.authServer = secrets.AuthServer;
		daten.clientID = secrets.ClientID;
		daten.clientSecret = secrets.ClientSecret;
		daten.tlsCert = secrets.TLSCert;
		daten.tlsCertIsKnown = secrets.TLSCertIsKnown;
		daten.tlsCertIsTrusted = secrets.TLSCertIsTrusted;
		return daten;
	};


	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link OAuth2ClientConnection}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataOauthClientSecrets(final DBEntityManager conn) {
		super(conn);
	}


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}


	@Override
	public Response getList() throws ApiOperationException {
		final List<DTOSchuleOAuthSecrets> daten = conn.queryAll(DTOSchuleOAuthSecrets.class);
		final List<OAuth2ClientConnection> result = new ArrayList<>();
		if (daten == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		for (final DTOSchuleOAuthSecrets secret : daten)
			result.add(dtoMapper.apply(secret));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		// Bestimme den Typ des OAuth2-Servers, dessen Secrets aus der Darenbank ermittelt werden sollen
		final OAuth2ServerTyp typ = OAuth2ServerTyp.getByID(id);
		final DTOSchuleOAuthSecrets daten = getDto(typ);
		if (daten == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(dtoMapper.apply(daten)).build();
	}


	/**
	 * Liefert das Datenbank-DTO zu den OAuth2-Secrets des angegebenen Server-Typs
	 *
	 * @param typ   der Server-Typ
	 *
	 * @return das DB-DTO zu den OAuth2-Secrets
	 */
	public DTOSchuleOAuthSecrets getDto(final OAuth2ServerTyp typ) {
		return conn.queryByKey(DTOSchuleOAuthSecrets.class, typ.getId());
	}


	private static final Map<String, DataBasicMapper<DTOSchuleOAuthSecrets>> patchMappings = Map.ofEntries(
			Map.entry("id", (conn, dto, value, map) -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID) || (OAuth2ServerTyp.getByID(patch_id.longValue()) == null))
					throw new ApiOperationException(Status.BAD_REQUEST, "Die ID bzw. der Servertyp darf für das OAuth2-Secret nicht angepasst werden.");
			}),
			Map.entry("authServer", (conn, dto, value, map) -> {
				try {
					final String authServer = JSONMapper.convertToString(value, false, false, 255);
					new URI(authServer).toURL();
					dto.AuthServer = authServer;
				} catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
					throw new ApiOperationException(Status.BAD_REQUEST, e);
				}
			}),
			Map.entry("clientID", (conn, dto, value, map) -> dto.ClientID = JSONMapper.convertToString(value, false, false, null)),
			Map.entry("clientSecret", (conn, dto, value, map) -> dto.ClientSecret = JSONMapper.convertToString(value, false, true, null)),
			Map.entry("tlsCertIsTrusted", (conn, dto, value, map) -> dto.TLSCertIsTrusted = JSONMapper.convertToBoolean(value, false, "tlsCertIsTrusted")));


	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		return super.patchBasic(id, is, DTOSchuleOAuthSecrets.class, patchMappings);
	}


	/**
	 * Löscht ein OAuth2 Client Secret
	 *
	 * @param id die ID des {@link DTOSchuleOAuthSecrets}
	 *
	 * @return die HTTP-Response, welche den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es muss eine ID angegeben werden. Null ist nicht zulässig.");
		final DTOSchuleOAuthSecrets dto = conn.queryByKey(DTOSchuleOAuthSecrets.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein DTO mit der ID %s gefunden.".formatted(id));
		return super.deleteBasic(id, DTOSchuleOAuthSecrets.class, dtoMapper);
	}


	/**
	 * Fügt ein {@link DTOSchuleOAuthSecrets} mit den übergebenen JSON-Daten der
	 * Datenbank hinzu und gibt das zugehörige CoreDTO zurück. Falls ein Fehler
	 * auftritt wird ein entsprechender Response-Code zurückgegeben.
	 *
	 * @param is der InputStream mit den JSON-Daten
	 *
	 * @return die Response mit den Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response add(final InputStream is) throws ApiOperationException {
		try {
			final OAuth2ClientConnection data = JSONMapper.mapper.readValue(is, OAuth2ClientConnection.class);
			final OAuth2ServerTyp serverTyp = OAuth2ServerTyp.getByID(data.id);
			if (serverTyp == null)
				throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein OAuth2-Servertyp mit der ID %d.".formatted(data.id));
			if (getDto(serverTyp) != null)
				throw new ApiOperationException(Status.CONFLICT, "Es existiert bereits ein Datensatz für den OAuth2-Servertyp %s."
						.formatted(serverTyp.toString()));
			final DTOSchuleOAuthSecrets toPersist = new DTOSchuleOAuthSecrets(data.id, data.authServer, data.clientID, data.clientSecret);
			// Persistiere das DTO in der Datenbank
			if (!conn.transactionPersist(toPersist)) {
				conn.transactionRollback();
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
			}
			conn.transactionFlush();
			return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(dtoMapper.apply(toPersist)).build();
		} catch (final IOException e) {
			conn.transactionRollback();
			throw new ApiOperationException(Status.BAD_REQUEST, e);
		}
	}

}
