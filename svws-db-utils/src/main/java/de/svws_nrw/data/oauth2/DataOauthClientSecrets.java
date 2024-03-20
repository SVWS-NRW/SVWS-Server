package de.svws_nrw.data.oauth2;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.data.oauth2.OAuth2ClientSecret;
import de.svws_nrw.core.types.oauth2.OAuth2ServerTyp;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link OAuth2ClientSecret}.
 */
public final class DataOauthClientSecrets extends DataManager<Long> {

	/** Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchuleOAuthSecrets} in einen Core-DTO {@link OAuth2ClientSecret}. */
	private static final Function<DTOSchuleOAuthSecrets, OAuth2ClientSecret> dtoMapper = (final DTOSchuleOAuthSecrets secrets) -> {
		final OAuth2ClientSecret daten = new OAuth2ClientSecret();
		daten.id = secrets.ID;
		daten.authServer = secrets.AuthServer;
		daten.clientID = secrets.ClientID;
		daten.clientSecret = secrets.ClientSecret;
		return daten;
	};


	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link OAuth2ClientSecret}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataOauthClientSecrets(final DBEntityManager conn) {
		super(conn);
	}


	@Override
	public Response getAll() {
		return this.getList();
	}


	@Override
	public Response getList() {
		final List<DTOSchuleOAuthSecrets> daten = conn.queryNamed("DTOSchuleOAuthSecrets.all", DTOSchuleOAuthSecrets.class).getResultList();
		final List<OAuth2ClientSecret> result = new ArrayList<>();
		if (daten == null)
			throw OperationError.NOT_FOUND.exception();
		for (final DTOSchuleOAuthSecrets secret : daten)
			result.add(dtoMapper.apply(secret));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}


	@Override
	public Response get(final Long id) {
		// Bestimme den Typ des OAuth2-Servers, dessen Secrets aus der Darenbank ermittelt werden sollen
		final OAuth2ServerTyp typ = OAuth2ServerTyp.getByID(id);
		final DTOSchuleOAuthSecrets daten = getDto(typ);
		if (daten == null)
			throw OperationError.NOT_FOUND.exception();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(dtoMapper.apply(daten)).build();
	}


	private DTOSchuleOAuthSecrets getDto(final OAuth2ServerTyp typ) {
		final List<DTOSchuleOAuthSecrets> dtoList = conn.queryNamed("DTOSchuleOAuthSecrets.id", typ.getId(), DTOSchuleOAuthSecrets.class);
		if ((dtoList == null) || (dtoList.size() != 1))
			return null;
		return dtoList.get(0);
	}


	private static final Map<String, DataBasicMapper<DTOSchuleOAuthSecrets>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID)
					|| OAuth2ServerTyp.getByID(patch_id.longValue()) == null)
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("authServer", (conn, dto, value, map) -> {
			try {
				final String authServer = JSONMapper.convertToString(value, false, false, 255);
				new URI(authServer).toURL();
				dto.AuthServer = authServer;
			} catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
				throw OperationError.BAD_REQUEST.exception(e);
			}
		}),
		Map.entry("clientID", (conn, dto, value, map) -> dto.ClientID = JSONMapper.convertToString(value, false, false, null)),
		Map.entry("clientSecret", (conn, dto, value, map) -> dto.ClientSecret = JSONMapper.convertToString(value, false, false, null))
	);


	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOSchuleOAuthSecrets.class, patchMappings);
	}


	/**
	 * Löscht ein OAuth2 Client Secret
	 *
	 * @param id die ID des {@link DTOSchuleOAuthSecrets}
	 *
	 * @return die HTTP-Response, welche den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
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
	 */
	public Response add(final InputStream is) {
		try {
			final OAuth2ClientSecret data =  JSONMapper.mapper.readValue(is, OAuth2ClientSecret.class);
			if (getDto(OAuth2ServerTyp.getByID(data.id)) != null)
				throw OperationError.CONFLICT.exception();
			final DTOSchuleOAuthSecrets toPersist = new DTOSchuleOAuthSecrets(data.id, data.authServer, data.clientID, data.clientSecret);
			// Persistiere das DTO in der Datenbank
			if (!conn.transactionPersist(toPersist)) {
				conn.transactionRollback();
				throw OperationError.INTERNAL_SERVER_ERROR.exception();
			}
			conn.transactionFlush();
			return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).entity(dtoMapper.apply(toPersist)).build();
		} catch (final IOException e) {
			conn.transactionRollback();
			throw OperationError.BAD_REQUEST.exception(e);
		}
	}

}
