package de.svws_nrw.service.enm;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.base.crypto.Passwords;
import de.svws_nrw.core.data.enm.ENMLehrerInitialKennwort;
import de.svws_nrw.core.data.enm.v2.ENMv2Daten;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulCredentials;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.ext.jbcrypt.BCrypt;
import de.svws_nrw.repo.enm.NotenmodulCredentialsRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Service für die Credentials für das externe Notenmodul.
 */
public class NotenmodulCredentialsService {

	private final NotenmodulCredentialsRepository notenmodulCredentialsRepository;
	private final LehrerRepository lehrerRepository;
	private final EnmV2GetService enmGetService;

	/**
	 * Erstellt einen neuen Service für die Notenmodul-Credentials
	 *
	 * @param notenmodulCredentialsRepository   das Repository für den Zugriff auf die Notenmodul-Credentials
	 * @param lehrerRepository                  das Repository für den Zugriff auf die Lehrer-Daten
	 * @param enmGetService                     der Service zum Einlesen der ENM-Daten aus der SVWS-Datenbank
	 */
	public NotenmodulCredentialsService(final NotenmodulCredentialsRepository notenmodulCredentialsRepository, final LehrerRepository lehrerRepository,
			final EnmV2GetService enmGetService) {
		this.notenmodulCredentialsRepository = notenmodulCredentialsRepository;
		this.lehrerRepository = lehrerRepository;
		this.enmGetService = enmGetService;
	}


	/**
	 * Erstellt ein neues Initialkennwort
	 *
	 * @return das neue Initialkennwort
	 */
	private static String createInitialkennwort() {
		return new String(Passwords.generateRandomPasswordWithoutSpecialChars());
	}


	/**
	 * Erstellt initiale Credentials für den Lehrer mit der übergebenen ID.
	 *
	 * @param idLehrer   die ID des Lehrers
	 * @param password   das zu setzende Kennwort, falls es vom Initialkennwort abweichen soll
	 * @param art2FA     die zu verwendende Methode für die Zwei-Faktor-Authentifizierung
	 *
	 * @return die neu erzeugten Credentials
	 */
	private DTONotenmodulCredentials createInitialCredentials(final long idLehrer, final String password, final int art2FA) {
		final String initial = createInitialkennwort();
		final String hash = BCrypt.hashpw(((password == null) || password.isBlank()) ? initial : password, BCrypt.gensalt());
		final DTONotenmodulCredentials cred = new DTONotenmodulCredentials(idLehrer, initial, hash, art2FA, true);
		cred.totpSecret = Passwords.generateTotpSecret();
		notenmodulCredentialsRepository.update(cred);
		return cred;
	}


	/**
	 * Gibt für alle Lehrer, welche bei den ENM-Daten vorkommen die Initialkennwörter zurück.
	 *
	 * @return die Liste der Initialkennwörter
	 */
	public List<ENMLehrerInitialKennwort> getInitialkennwoerter() {
		return transactional(() -> {
			// Erstelle zunächst Initialkennwörter, falls eine Lehrer noch keines hat
			generateMissingCredentials();
			// Erstelle die ENM-Daten, damit klar ist, für welche Lehrer die Initialkennwörter zurückgegeben werden müssen
			final ENMv2Daten enmdaten = enmGetService.get(null);
			// Bestimme die Menge der Lehrer-IDs und lese dann dafür die Initialkennwörter aus der Datenbank.
			final List<ENMLehrerInitialKennwort> daten = new ArrayList<>();
			final List<Long> idsLehrer = enmdaten.lehrer.stream().map(l -> l.id).toList();
			if (!idsLehrer.isEmpty()) {
				final List<DTONotenmodulCredentials> dtos = notenmodulCredentialsRepository.findListByIds(idsLehrer);
				for (final DTONotenmodulCredentials dto : dtos) {
					final ENMLehrerInitialKennwort cred = new ENMLehrerInitialKennwort();
					cred.id = dto.idLehrer;
					cred.initialKennwort = dto.initialkennwort;
					daten.add(cred);
				}
			}
			return daten;
		});
	}


	/**
	 * Gibt für den angegebenen Lehrer das Initialkennwort zurück.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return das Initialkennwort
	 */
	public String getInitialkennwort(final long idLehrer) {
		return transactional(() -> {
			// Prüfe, ob ein Lehrer mit der ID in der Datenbank existiert
			lehrerRepository.findById(idLehrer)
					.orElseThrow(
							() -> new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d konnte nicht gefunden werden.".formatted(idLehrer)));
			final Optional<DTONotenmodulCredentials> foundCred = notenmodulCredentialsRepository.findById(idLehrer);
			final DTONotenmodulCredentials cred = foundCred.isEmpty()
					? createInitialCredentials(idLehrer, null, 0)
					: foundCred.get();
			return cred.initialkennwort;
		});
	}


	/**
	 * Erstellt für alle Lehrer initiale Credentials, sofern ein Lehrer nicht bereits welche besitzt.
	 */
	public void generateMissingCredentials() {
		transactional(() -> {
			// Prüfe zunächst die existierenden Credentials auf Vollständigkeit
			final List<DTONotenmodulCredentials> existing = notenmodulCredentialsRepository.getAll();
			for (final DTONotenmodulCredentials cred : existing) {
				final boolean hasInitial = (cred.initialkennwort != null) && (!cred.initialkennwort.isBlank());
				final boolean hasHash = (cred.passwordHash != null) && (!cred.passwordHash.isBlank());
				final boolean hasTotp = (cred.totpSecret != null) && (!cred.totpSecret.isBlank());
				if (hasInitial && hasTotp && hasHash) {
					continue;
				}
				if (!hasInitial) {
					cred.initialkennwort = createInitialkennwort();
				}
				if (!hasHash) {
					cred.passwordHash = BCrypt.hashpw(cred.initialkennwort, BCrypt.gensalt());
				}
				if (!hasTotp) {
					cred.totpSecret = Passwords.generateTotpSecret();
					cred.istErstanmeldung = true;
				}
				notenmodulCredentialsRepository.update(cred);
			}
			// Erstelle dann die noch fehlenden Credentials
			final Set<Long> idsExisting = existing.stream().map(c -> c.idLehrer).collect(Collectors.toUnmodifiableSet());
			final List<Long> ids = lehrerRepository.getAll().stream().map(l -> l.ID).filter(l -> !idsExisting.contains(l)).toList();
			for (final long id : ids) {
				createInitialCredentials(id, null, 0);
			}
			notenmodulCredentialsRepository.flush();
		});
	}


	/**
	 * Setzt das Kennwort des Lehrers auf das Initialkennwort zurück. Ist kein Initialkennwort vorhanden,
	 * so wird ein neues generiert.
	 *
	 * @param idLehrer   die ID des Lehrers
	 */
	public void resetPassword(final long idLehrer) {
		transactional(() -> {
			// Prüfe, ob ein Lehrer mit der ID in der Datenbank existiert
			lehrerRepository.findById(idLehrer)
					.orElseThrow(
							() -> new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d konnte nicht gefunden werden.".formatted(idLehrer)));
			// Setze die Credentials neu
			final Optional<DTONotenmodulCredentials> foundCred = notenmodulCredentialsRepository.findById(idLehrer);
			if (foundCred.isEmpty()) {
				createInitialCredentials(idLehrer, null, 0);
			} else {
				final DTONotenmodulCredentials cred = foundCred.get();
				final boolean hasInitial = (cred.initialkennwort != null) && (!cred.initialkennwort.isBlank());
				if (!hasInitial) {
					cred.initialkennwort = createInitialkennwort();
				}
				cred.passwordHash = BCrypt.hashpw(cred.initialkennwort, BCrypt.gensalt());
				notenmodulCredentialsRepository.update(cred);
			}
		});
	}


	/**
	 * Generiert für einen Lehrers ein neues Initialkennwort und gib dieses zurück.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return das neue Initialkennwort
	 */
	public String generateInitialPassword(final long idLehrer) {
		return transactional(() -> {
			// Prüfe, ob ein Lehrer mit der ID in der Datenbank existiert
			lehrerRepository.findById(idLehrer)
					.orElseThrow(
							() -> new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d konnte nicht gefunden werden.".formatted(idLehrer)));
			final Optional<DTONotenmodulCredentials> foundCred = notenmodulCredentialsRepository.findById(idLehrer);
			final DTONotenmodulCredentials cred;
			if (foundCred.isEmpty()) {
				// Erzeuge neue Credentials
				cred = createInitialCredentials(idLehrer, null, 0);
			} else {
				// Setze das Initialkennwort neu
				cred = foundCred.get();
				final boolean istInitialPassword = (cred.initialkennwort != null) && (cred.passwordHash != null)
						&& BCrypt.checkpw(cred.initialkennwort, cred.passwordHash);
				cred.initialkennwort = createInitialkennwort();
				if ((cred.passwordHash == null) || istInitialPassword) {
					cred.passwordHash = BCrypt.hashpw(cred.initialkennwort, BCrypt.gensalt());
				}
				notenmodulCredentialsRepository.update(cred);
			}
			return cred.initialkennwort;
		});
	}


	/**
	 * Ersetzt ein vorhandenes TOTP-Secret durch ein neues TOTP-Secret oder erzuegt ggf. ein neues. Danach
	 * wird erneut von einer Erstanmeldung für die 2FA ausgegangen.
	 *
	 * @param idLehrer   die ID des Lehrers
	 */
	public void resetTotpSecret(final long idLehrer) {
		transactional(() -> {
			// Prüfe, ob ein Lehrer mit der ID in der Datenbank existiert
			lehrerRepository.findById(idLehrer)
					.orElseThrow(
							() -> new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d konnte nicht gefunden werden.".formatted(idLehrer)));
			// Setze die Credentials neu
			final Optional<DTONotenmodulCredentials> foundCred = notenmodulCredentialsRepository.findById(idLehrer);
			if (foundCred.isEmpty()) {
				createInitialCredentials(idLehrer, null, 0);
			} else {
				final DTONotenmodulCredentials cred = foundCred.get();
				cred.totpSecret = Passwords.generateTotpSecret();
				cred.istErstanmeldung = true;
				notenmodulCredentialsRepository.update(cred);
			}
		});
	}

	/**
	 * Setzt die Methode für die Zwei-Faktor-Authentifizierung für einen Lehrer.
	 *
	 * @param idLehrer   die ID des Lehrers
	 * @param art2FA     die zu verwendende Methode für die Zwei-Faktor-Authentifizierung
	 */
	public void setArt2FA(final long idLehrer, final Integer art2FA) {
		transactional(() -> {
			// Prüfe, ob die Methode gültig ist oder nicht
			if (art2FA == null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Eine Zwei-Faktor-Methode muss angebenen werden");
			}
			if ((art2FA < 0) || (art2FA > 1)) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Eine Zwei-Faktor-Methode %d wird nicht unterstützt".formatted(art2FA));
			}
			// Prüfe, ob ein Lehrer mit der ID in der Datenbank existiert
			lehrerRepository.findById(idLehrer)
					.orElseThrow(
							() -> new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d konnte nicht gefunden werden.".formatted(idLehrer)));
			// Setze die Methode der Zwei-Faktor-Authentifizierung bei den Credentials
			final Optional<DTONotenmodulCredentials> foundCred = notenmodulCredentialsRepository.findById(idLehrer);
			if (foundCred.isEmpty()) {
				createInitialCredentials(idLehrer, null, art2FA);
			} else {
				final DTONotenmodulCredentials cred = foundCred.get();
				cred.art2FA = art2FA;
				if (cred.totpSecret == null) {
					cred.totpSecret = Passwords.generateTotpSecret();
					cred.istErstanmeldung = true;
				}
				notenmodulCredentialsRepository.update(cred);
			}
		});
	}
}
