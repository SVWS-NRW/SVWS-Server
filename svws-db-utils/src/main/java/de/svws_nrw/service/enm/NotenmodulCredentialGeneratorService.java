package de.svws_nrw.service.enm;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.base.crypto.Passwords;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulCredentials;
import de.svws_nrw.ext.jbcrypt.BCrypt;
import de.svws_nrw.repo.enm.NotenmodulCredentialsRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;

/**
 * Service für die Credentials für das externe Notenmodul.
 */
public class NotenmodulCredentialGeneratorService {

	private final NotenmodulCredentialsRepository notenmodulCredentialsRepository;
	private final LehrerRepository lehrerRepository;

	/**
	 * Erstellt einen neuen Service für die Notenmodul-Credentials
	 *
	 * @param notenmodulCredentialsRepository   das Repository für den Zugriff auf die Notenmodul-Credentials
	 * @param lehrerRepository                  das Repository für den Zugriff auf die Lehrer-Daten
	 */
	public NotenmodulCredentialGeneratorService(final NotenmodulCredentialsRepository notenmodulCredentialsRepository, final LehrerRepository lehrerRepository) {
		this.notenmodulCredentialsRepository = notenmodulCredentialsRepository;
		this.lehrerRepository = lehrerRepository;
	}


	/**
	 * Erstellt ein neues Initialkennwort
	 *
	 * @return das neue Initialkennwort
	 */
	public String createInitialkennwort() {
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
	public DTONotenmodulCredentials createInitialCredentials(final long idLehrer, final String password, final int art2FA) {
		final String initial = createInitialkennwort();
		final String hash = BCrypt.hashpw(((password == null) || password.isBlank()) ? initial : password, BCrypt.gensalt());
		final DTONotenmodulCredentials cred = new DTONotenmodulCredentials(idLehrer, initial, hash, art2FA, true);
		cred.totpSecret = Passwords.generateTotpSecret();
		notenmodulCredentialsRepository.update(cred);
		return cred;
	}



	/**
	 * Erstellt für alle Lehrer initiale Credentials, sofern ein Lehrer nicht bereits welche besitzt.
	 *
	 * @return die Map mit den Credentials, welche ein Initialkennwort, einen Hash und TOTP-Einträge haben
	 *         und der ID des Lehrers zugeordnet sind
	 */
	public Map<Long, DTONotenmodulCredentials> generateMissingCredentials() {
		return transactional(() -> {
			// Prüfe zunächst die existierenden Credentials auf Vollständigkeit
			final Map<Long, DTONotenmodulCredentials> result = new HashMap<>();
			final List<DTONotenmodulCredentials> existing = notenmodulCredentialsRepository.getAll();
			for (final DTONotenmodulCredentials cred : existing) {
				result.put(cred.idLehrer, cred);
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
				final DTONotenmodulCredentials cred = createInitialCredentials(id, null, 0);
				result.put(cred.idLehrer, cred);
			}
			notenmodulCredentialsRepository.flush();
			return result;
		});
	}

}
