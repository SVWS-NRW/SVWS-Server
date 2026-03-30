package de.svws_nrw.service.enm;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.base.crypto.Passwords;
import de.svws_nrw.core.data.enm.ENMLehrerInitialKennwort;
import de.svws_nrw.core.data.enm.v1.ENMv1Daten;
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
	private final EnmV1GetService enmGetService;

	/**
	 * Erstellt einen neuen Service für die Notenmodul-Credentials
	 *
	 * @param notenmodulCredentialsRepository   das Repository für den Zugriff auf die Notenmodul-Credentials
	 * @param lehrerRepository                  das Repository für den Zugriff auf die Lehrer-Daten
	 * @param enmGetService                     der Service zum Einlesen der ENM-Daten aus der SVWS-Datenbank
	 */
	public NotenmodulCredentialsService(final NotenmodulCredentialsRepository notenmodulCredentialsRepository, final LehrerRepository lehrerRepository,
			final EnmV1GetService enmGetService) {
		this.notenmodulCredentialsRepository = notenmodulCredentialsRepository;
		this.lehrerRepository = lehrerRepository;
		this.enmGetService = enmGetService;
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
			final ENMv1Daten enmdaten = enmGetService.get(null);
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
	 * Erstellt für alle Lehrer initiale Credentials, sofern ein Lehrer nicht bereits welche besitzt.
	 */
	public void generateMissingCredentials() {
		transactional(() -> {
			// Prüfe zunächst die existierenden Credentials auf Vollständigkeit
			final List<DTONotenmodulCredentials> existing = notenmodulCredentialsRepository.getAll();
			for (final DTONotenmodulCredentials cred : existing) {
				final boolean hasInitial = (cred.initialkennwort != null) && (!cred.initialkennwort.isBlank());
				final boolean hasHash = (cred.passwordHash != null) && (!cred.passwordHash.isBlank());
				if (hasInitial && hasHash) {
					continue;
				}
				if (!hasInitial) {
					cred.initialkennwort = Passwords.generateRandomPasswordWithoutSpecialChars(10);
				}
				if (!hasHash) {
					cred.passwordHash = BCrypt.hashpw(cred.initialkennwort, BCrypt.gensalt());
				}
				notenmodulCredentialsRepository.update(cred);
			}
			// Erstelle dann die noch fehlenden Credentials
			final Set<Long> idsExisting = existing.stream().map(c -> c.idLehrer).collect(Collectors.toUnmodifiableSet());
			final List<Long> ids = lehrerRepository.getAll().stream().map(l -> l.ID).filter(l -> !idsExisting.contains(l)).toList();
			for (final long id : ids) {
				final String initial = Passwords.generateRandomPasswordWithoutSpecialChars(10);
				final String hash = BCrypt.hashpw(initial, BCrypt.gensalt());
				notenmodulCredentialsRepository.update(new DTONotenmodulCredentials(id, initial, hash));
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
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d konnte nicht gefunden werden.".formatted(idLehrer)));
			// Setze die Credentials neu
			final Optional<DTONotenmodulCredentials> foundCred = notenmodulCredentialsRepository.findById(idLehrer);
			if (foundCred.isEmpty()) {
				final String initial = Passwords.generateRandomPasswordWithoutSpecialChars(10);
				final String hash = BCrypt.hashpw(initial, BCrypt.gensalt());
				notenmodulCredentialsRepository.update(new DTONotenmodulCredentials(idLehrer, initial, hash));
			} else {
				final DTONotenmodulCredentials cred = foundCred.get();
				final boolean hasInitial = (cred.initialkennwort != null) && (!cred.initialkennwort.isBlank());
				if (!hasInitial) {
					cred.initialkennwort = Passwords.generateRandomPasswordWithoutSpecialChars(10);
				}
				cred.passwordHash = BCrypt.hashpw(cred.initialkennwort, BCrypt.gensalt());
				notenmodulCredentialsRepository.update(cred);
			}
		});
	}


	/**
	 * Setzt das Kennwort des Lehrers auf das übergebene Kennwort. Das Initialkennwort bleibt dabei
	 * bestehen oder wird durch ein generiertes gesetzt, wenn der Lehrer vorher kein Initialkennwort hatte.
	 *
	 * @param idLehrer   die ID des Lehrers
	 * @param password   das neu zu setzende Kennwort
	 */
	public void setPassword(final long idLehrer, final String password) {
		transactional(() -> {
			// TODO geeignetere Kriterien festlegen und in Passwords.java als Methode zum Prüfen implementieren
			if ((password == null) || (password.isBlank()) || (password.length() < 6)) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Ein neues Kennwort darf nicht leer sein und muss mindestens 6 Zeichen enthalten.");
			}

			// Prüfe, ob ein Lehrer mit der ID in der Datenbank existiert
			lehrerRepository.findById(idLehrer)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Ein Lehrer mit der ID %d konnte nicht gefunden werden.".formatted(idLehrer)));

			// Lese ggf. vorhandene Credentials aus der Datenbank ein und erzeuge des neuen Password-Hash
			final Optional<DTONotenmodulCredentials> foundCred = notenmodulCredentialsRepository.findById(idLehrer);
			final String hash = BCrypt.hashpw(password, BCrypt.gensalt());

			// Wenn noch keine Credentials vorhanden sind, dann erstelle neu
			if (foundCred.isEmpty()) {
				final String initial = Passwords.generateRandomPasswordWithoutSpecialChars(10);
				final DTONotenmodulCredentials cred = new DTONotenmodulCredentials(idLehrer, initial, hash);
				notenmodulCredentialsRepository.update(cred);
				return;
			}

			// Aktualisiere vorhandene Credentials
			final DTONotenmodulCredentials cred = foundCred.get();
			final boolean hasInitial = (cred.initialkennwort != null) && (!cred.initialkennwort.isBlank());
			if (!hasInitial) {
				cred.initialkennwort = Passwords.generateRandomPasswordWithoutSpecialChars(10);
			}
			cred.passwordHash = hash;
			notenmodulCredentialsRepository.update(cred);
		});
	}

}
