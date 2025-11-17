package de.svws_nrw.data.enm;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import de.svws_nrw.base.crypto.TLSUtils;
import de.svws_nrw.core.data.TLSCertificate;
import de.svws_nrw.core.data.enm.ENMServerConnection;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulVerbindungen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse organisiert den Datenbank-Zugriff auf die Information eine Datenbank-Verbindung
 */
public final class DataENMServerConnection extends DataManagerRevised<Long, DTONotenmodulVerbindungen, ENMServerConnection> {

	/** Der Formatter für das Umwandeln der Daten, wann ein Zertifikat gültig ist in das ISO-Format */
	private static final DateTimeFormatter dateFormatter =
			new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter().withZone(ZoneId.systemDefault());


	/**
	 * Erstellt einen neuen Manager.
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	public DataENMServerConnection(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("url");
		setAttributesNotPatchable("serverTLSCert", "serverTLSCertIsKnown", "serverTLSCertChain");
	}


	@Override
	protected void initDTO(final DTONotenmodulVerbindungen dto, final Long id, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.id = id;
		dto.url = "";
		dto.clientID = "1";
		dto.clientSecret = "";
		dto.serverTLSCertIsKnown = false;
		dto.serverTLSCertIsTrusted = false;
	}


	@Override
	protected ENMServerConnection map(final DTONotenmodulVerbindungen dto) throws ApiOperationException {
		final ENMServerConnection daten = new ENMServerConnection();
		daten.id = dto.id;
		daten.url = dto.url;
		daten.clientID = dto.clientID;
		daten.clientSecret = dto.clientSecret;
		daten.bezeichnung = dto.bezeichnung;
		// Umwandeln des TLS-Zertifikates, so dass der Client dieses in lesbarer Form erhält
		try {
			if (dto.serverTLSCert != null) {
				final List<X509Certificate> chain = TLSUtils.decodeCertListJson(dto.serverTLSCert);
				for (final X509Certificate cert : chain) {
					final TLSCertificate c = new TLSCertificate();
					c.version = cert.getVersion();
					c.type = cert.getType();
					c.subject = cert.getSubjectX500Principal().getName();
					c.validSince = dateFormatter.format(cert.getNotBefore().toInstant());
					c.validUntil = dateFormatter.format(cert.getNotAfter().toInstant());
					c.issuer = cert.getIssuerX500Principal().getName();
					c.serialNumber = cert.getSerialNumber().toString(16);
					c.signatureAlgorithm = cert.getSigAlgName();
					c.signatureAlgorithmOID = cert.getSigAlgOID();
					c.signature = Base64.getEncoder().encodeToString(cert.getSignature());
					final PublicKey key = cert.getPublicKey();
					c.keyAlgorithm = key.getAlgorithm();
					c.keyFormat = key.getFormat();
					c.key = Base64.getEncoder().encodeToString(key.getEncoded());
					daten.serverTLSCertChain.add(c);
				}
			}
		} catch (final CertificateException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Einlesen der TLS-Zertifikatskette");
		}
		daten.serverTLSCert = dto.serverTLSCert;
		daten.serverTLSCertIsKnown = dto.serverTLSCertIsKnown;
		daten.serverTLSCertIsTrusted = dto.serverTLSCertIsTrusted;
		return daten;
	}


	@Override
	public List<ENMServerConnection> getList() throws ApiOperationException {
		final List<DTONotenmodulVerbindungen> daten = conn.queryAll(DTONotenmodulVerbindungen.class);
		final List<ENMServerConnection> result = new ArrayList<>();
		if (daten == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		for (final DTONotenmodulVerbindungen secret : daten)
			result.add(map(secret));
		return result;
	}


	@Override
	public ENMServerConnection getById(final Long id) throws ApiOperationException {
		final DTONotenmodulVerbindungen daten = conn.queryByKey(DTONotenmodulVerbindungen.class, id);
		if (daten == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es konnte keine Verbindung zu einem Web-Notenmodul-Server mit der angebenen ID gefunden werden.");
		return map(daten);
	}


	@Override
	protected void mapAttribute(final DTONotenmodulVerbindungen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.id))
					throw new ApiOperationException(Status.BAD_REQUEST, "Die ID darf für die Verbindung nicht angepasst werden.");
			}
			case "url" -> {
				try {
					final String url = JSONMapper.convertToString(value, false, false, 255);
					new URI(url).toURL();
					dto.url = url;
				} catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
					throw new ApiOperationException(Status.BAD_REQUEST, e);
				}
			}
			case "bezeichnung" -> dto.bezeichnung = JSONMapper.convertToString(value, false, true, null);
			case "clientID" -> dto.clientID = JSONMapper.convertToString(value, false, false, null);
			case "clientSecret" -> dto.clientSecret = JSONMapper.convertToString(value, false, true, null);
			case "serverTLSCertIsTrusted" -> dto.serverTLSCertIsTrusted = JSONMapper.convertToBoolean(value, false, "serverTLSCertIsTrusted");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

}
