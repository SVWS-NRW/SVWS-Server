package de.svws_nrw.service.enm;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.net.URI;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.base.crypto.TLSUtils;
import de.svws_nrw.core.data.TLSCertificate;
import de.svws_nrw.core.data.enm.ENMServerConnection;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulVerbindungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulVerbindungenRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Zugriff auf die Notenmodul-Verbindungen
 */
public final class NotenmodulVerbindungenService {

	/** Der Formatter für das Umwandeln der Daten, wann ein Zertifikat gültig ist in das ISO-Format */
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
			.withZone(ZoneId.systemDefault());

	/** Das Repository für den Zugriff auf die Notenmodul-Verbindungen */
	private final NotenmodulVerbindungenRepository repository;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository   das Repository für den Zugriff auf die Notenmodul-Verbindungen
	 */
	public NotenmodulVerbindungenService(final NotenmodulVerbindungenRepository repository) {
		this.repository = repository;
	}


	private ENMServerConnection toApi(final DTONotenmodulVerbindungen dto) {
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


	/**
	 * Ermittelt die Notenmodul-Verbindung anhand der übergebenen ID.
	 *
	 * @param id   die ID der Notenmodul-Verbindung
	 *
	 * @return die Notenmodul-Verbindung
	 */
	public ENMServerConnection get(final long id) {
		final var list = getList(List.of(id));
		if (list.isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Eintrag mit der ID %d gefunden.".formatted(id));
		}
		return list.get(0);
	}


	/**
	 * Ermittelt die Notenmodul-Verbindungen anhand der übergebenen IDs.
	 *
	 * @param ids   die IDs der Notenmodul-Verbindungen
	 *
	 * @return die Liste mit den Notenmodul-Verbindungen
	 */
	public List<ENMServerConnection> getList(final Collection<Long> ids) {
		final List<DTONotenmodulVerbindungen> entities = repository.findListByIds(ids);
		if (entities.size() != ids.size()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden nicht alle Notenmodul-Verbindungen zu den IDs gefunden (%d von %d).".formatted(entities.size(), ids.size()));
		}
		return entities.stream().map(this::toApi).toList();
	}


	/**
	 * Ermittelt alle Notenmodul-Verbindungen.
	 *
	 * @return die Liste mit den Notenmodul-Verbindungen
	 */
	public List<ENMServerConnection> getAll() {
		final List<DTONotenmodulVerbindungen> entities = repository.getAll();
		return entities.stream().map(this::toApi).toList();
	}


	/**
	 * Führt einen Patch für das Core-DTO mit der angebenen ID aus
	 *
	 * @param id      die ID
	 * @param patch   der Patch
	 *
	 * @return das gepatchte Core-DTO
	 */
	public ENMServerConnection patch(final long id, final NotenmodulVerbindungenPatchRequest patch) {
		return patchMultiple(Map.of(id, patch)).getFirst();
	}


	/**
	 * Führt mehrere Patches auf mehrere Core-DTOs aus.
	 *
	 * @param patches   eine Map mit den Patches, welche jeweils ihren IDs zugeordnet werden.
	 *
	 * @return die Liste mit den gepatchten Core-DTOs
	 */
	public List<ENMServerConnection> patchMultiple(final Map<Long, NotenmodulVerbindungenPatchRequest> patches) {
		if (patches.isEmpty()) {
			return new ArrayList<>();
		}

		return transactional(() -> {
			// Bestimme die Entitäten aus der Datenbank
			final var listVerbindungen = repository.findListByIds(patches.keySet());
			if (listVerbindungen.size() != patches.size()) {
				throw new ApiOperationException(Status.NOT_FOUND,
						"Es wurden nicht alle Verbindungen zu den IDs gefunden (%d von %d).".formatted(listVerbindungen.size(), patches.size()));
			}
			final var mapVerbindungen = listVerbindungen.stream().collect(Collectors.toMap(e -> e.id, e -> e));

			// Führe die Patches aus
			for (final var entry : patches.entrySet()) {
				final var patch = entry.getValue();
				final var entity = mapVerbindungen.get(entry.getKey());
				applyPatch(entity, patch);
			}

			// Persistiere das Ergebnis und gebe die Core-DTOs zurück
			repository.update(listVerbindungen);
			return getList(patches.keySet());
		});
	}


	private static void validateUrl(final String url) {
		try {
			new URI(url).toURL();
		} catch (final Exception e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Ungültiges URL-Format: " + url);
		}
	}


	private static void applyPatch(final DTONotenmodulVerbindungen daten, final NotenmodulVerbindungenPatchRequest patch) {
		patch.url.ifPresent(val -> {
			validateUrl(val);
			daten.url = val;
		});
		patch.bezeichnung.ifPresent(val -> daten.bezeichnung = val);
		patch.clientID.ifPresent(val -> daten.clientID = val);
		patch.clientSecret.ifPresent(val -> daten.clientSecret = val);
		patch.serverTLSCertIsTrusted.ifPresent(val -> daten.serverTLSCertIsTrusted = val);
		if (patch.url.isPresent() || patch.clientID.isPresent() || patch.clientSecret.isPresent() || patch.serverTLSCertIsTrusted.isPresent()) {
			daten.tokenTimestamp = null;
			daten.token = null;
			daten.tokenExpiresIn = null;
		}
	}


	/**
	 * Erstellt eine neues Core-DTO mit einer neuen ID und mithilfe
	 * des Create-Patches.
	 *
	 * @param patch   der Create-Patch
	 *
	 * @return das neue Core-DTO
	 */
	public ENMServerConnection create(final NotenmodulVerbindungenCreateRequest patch) {
		return createMultiple(List.of(patch)).getFirst();
	}

	/**
	 * Erstellt neue Core-DTOs mit neuen IDs und mithilfe der Create-Patches.
	 *
	 * @param requests   die Create-Patches
	 *
	 * @return die neuen Core-DTOs
	 */
	public List<ENMServerConnection> createMultiple(final Collection<NotenmodulVerbindungenCreateRequest> requests) {
		if (requests.isEmpty()) {
			return new ArrayList<>();
		}

		return transactional(() -> {
			// Erstelle die neuen Entitäten als Grundlage für den Patch-Vorgang
			long nextId = repository.getNextID();
			final List<DTONotenmodulVerbindungen> entities = new ArrayList<>();
			for (final NotenmodulVerbindungenCreateRequest request : requests) {
				validateUrl(request.url);
				final var neu = new DTONotenmodulVerbindungen(nextId++, request.url, request.clientID, request.clientSecret);
				neu.bezeichnung = request.bezeichnung;
				neu.serverTLSCertIsKnown = false;
				neu.serverTLSCertIsTrusted = false;
				entities.add(neu);
			}

			// Persistiere das Ergebnis und gebe die Core-DTOs zurück
			repository.update(entities);
			repository.flush();
			final var ids = entities.stream().map(e -> e.id).toList();
			return getList(ids);
		});
	}


	/**
	 * Löscht das Core-DTO mit der angebenen ID aus der Datenbank.
	 *
	 * @param id   die ID
	 *
	 * @return das entfernte Core-DTO
	 */
	public ENMServerConnection delete(final long id) {
		final List<ENMServerConnection> result = deleteMultiple(List.of(id));
		return result.getFirst();
	}


	/**
	 * Löscht mehrere Core-DTO mit der angebenen ID aus der Datenbank.
	 *
	 * @param ids   die IDs
	 *
	 * @return die entfernten Core-DTOs
	 */
	public List<ENMServerConnection> deleteMultiple(final Collection<Long> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}
		return transactional(() -> {
			final List<DTONotenmodulVerbindungen> entities = repository.findListByIds(ids);
			if (entities.size() != ids.size()) {
				throw new ApiOperationException(Status.NOT_FOUND,
						"Es wurden nicht alle Notenmodul-Verbindungen zu den IDs gefunden (%d von %d).".formatted(entities.size(), ids.size()));
			}
			final var result = entities.stream().map(this::toApi).toList();
			repository.delete(entities);
			return result;
		});
	}

}
