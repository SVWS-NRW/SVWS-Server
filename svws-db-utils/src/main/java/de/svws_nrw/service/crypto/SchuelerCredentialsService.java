package de.svws_nrw.service.crypto;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESAlgo;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.base.crypto.Passwords;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentials;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.CredentialsRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für die Verwaltung von Schüler-Credentials
 */
public class SchuelerCredentialsService {

	private final CredentialsRepository credentialsRepository;
	private final SchuelerRepository schuelerRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param credentialsRepository   das Repository für den Zugriff auf die Credentials
	 * @param schuelerRepository      das Repository für den Zugriff auf die Schülerdaten
	 */
	public SchuelerCredentialsService(final CredentialsRepository credentialsRepository,
			final SchuelerRepository schuelerRepository) {
		this.credentialsRepository = credentialsRepository;
		this.schuelerRepository = schuelerRepository;
	}

	private static String nameToAscii(final String name) {
		final String keineUmlaute = name.trim().replace("Ä", "Ae").replace("Ö", "Oe").replace("Ü", "Ue").replace("ä", "ae").replace("ö", "oe")
				.replace("ü", "ue").replace("ß", "ss");
		final String normalized = Normalizer.normalize(keineUmlaute, Normalizer.Form.NFD).replace('\u0141', 'L').replace('\u0142', 'l');
		final Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		final String noAccents = pattern.matcher(normalized).replaceAll("");
		final String asciiOnly = noAccents.replaceAll("[^\\x20-\\x7E]", "");
		return asciiOnly.replaceAll("\\s+", "").replace("-", "").toLowerCase();
	}

	private static String determineUsername(final String nachname, final String vorname, final int maxlen, final Set<String> existingUsernames) {
		final String vn = nameToAscii(vorname);
		String nn = nameToAscii(nachname);
		if (nn.length() > (maxlen - 2)) {
			nn = nn.substring(0, maxlen - 2);
		}
		// Erster Versuch mit vorname.nachname
		String username = vn + "." + nn;
		if ((username.length() <= maxlen) && (!existingUsernames.contains(username))) {
			return username;
		}
		// Zweiter Versuch mit v.nachname
		final String vn1 = vn.substring(0, 1);
		username = vn1 + "." + nn;
		if ((username.length() <= maxlen) && (!existingUsernames.contains(username))) {
			return username;
		}
		// Dritter Versuch mit vo.nachname
		if (vn.length() > 1) {
			username = vn.substring(0, 2) + "." + nn;
			if ((username.length() <= maxlen) && (!existingUsernames.contains(username))) {
				return username;
			}
		}
		// Und dann Versuch mit vX.nachname, wobei X hochgezählt wird und nachname ggf. gekürzt wird.
		long value = 1;
		while (value > 0) {
			final String nummer = "" + value;
			if (nn.length() > (maxlen - (2 + nummer.length()))) {
				nn = nn.substring(0, (maxlen - (2 + nummer.length())));
			}
			username = vn1 + nummer + "." + nn;
			if ((username.length() <= maxlen) && (!existingUsernames.contains(username))) {
				return username;
			}
			value++;
		}
		throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Kann keinen Benutzernamen ermitteln.");
	}


	private static String determinePseudonym(final String praefix, final long id, final Set<String> existingPseudonyms) {
		final String pseudonym = praefix + id;
		if (existingPseudonyms.contains(pseudonym)) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Kann kein Pseudonym für den Benutzer erstellen.");
		}
		return pseudonym;
	}

	/**
	 * Ermittelt das AES-Verschlüsselungsobjekt für den Schüler mit der übergebenen ID aus der Datenbank.
	 * Liegen noch kein Credentials-Eintrag für den Schüler vor, so wird ein neuer generiert.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return das AES-Verschlüsselungsobjekt
	 */
	public AES getOrCreate(final long idSchueler) {
		final var map = getOrCreateMap(List.of(idSchueler));
		if (map.isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND,
					"Es konnte kein AES-Verschlüsselungsobjekt für einen Schüler mit der ID %d gefunden werden.".formatted(idSchueler));
		}
		return map.values().iterator().next();
	}

	/**
	 * Ermittelt die AES-Verschlüsselungsobjekte für die Schüler mit der übergebenen IDs aus der Datenbank.
	 * Liegen noch keine Credentials-Einträge für die Schüler vor, so werden neue generiert.
	 *
	 * @param ids   die IDs der Schüler
	 *
	 * @return eine Map mit den AES-Verschlüsselungsobjekten zugeordnet zu deren zugehörigen Schüler-IDs
	 */
	public Map<Long, AES> getOrCreateMap(final Collection<Long> ids) {
		return transactional(() -> {
			// Erstelle die Rückgabe-Map
			final Map<Long, AES> result = new HashMap<>();

			// Prüfe, ob überhaupt Daten angefragt wurden
			final List<Long> idsSchueler = ids.stream().filter(Objects::nonNull).toList();
			if (idsSchueler.isEmpty()) {
				return result;
			}

			// Bestimme zunächst alle Schüler
			final List<DTOSchueler> listSchueler = schuelerRepository.findListByIds(idsSchueler);
			if (listSchueler.size() != idsSchueler.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Es konnten nicht für alle Schüler-IDs Schülerdaten aus der Datenbank bestimmt werden.");
			}

			// Bestimme die bereits existierenden Credentials für die Schüler
			final List<Long> idsCredentials = listSchueler.stream().map(s -> s.CredentialID).filter(Objects::nonNull).toList();
			final Map<Long, DTOCredentials> mapCredentials = credentialsRepository.findMapByIds(idsCredentials);
			credentialsRepository.flush();

			// Erzeuge ggf. noch fehlende Credentials bei den Schülern
			final List<DTOSchueler> listSchuelerOhneCreds = listSchueler.stream().filter(s -> s.CredentialID == null).toList();
			Map<DTOSchueler, DTOCredentials> mapNeueCreds = new HashMap<>();
			if (!listSchuelerOhneCreds.isEmpty()) {
				mapNeueCreds = prepareNewSchuelerCredentials(listSchuelerOhneCreds);
				for (final DTOCredentials cred : mapNeueCreds.values()) {
					mapCredentials.put(cred.ID, cred);
				}
			}

			// Prüfe, ob bei den Credentials noch AES-Schlüssel fehlen
			updateCredentialsWithNewAESKey(mapCredentials.values());

			// Aktualisiere die Schüler mit ihren neuen Credential-Einträge nach dem Erzeugen der Credentials wegen der Foreign-Key-Constraints
			if (!mapNeueCreds.isEmpty()) {
				for (final var entry : mapNeueCreds.entrySet()) {
					final DTOSchueler schueler = entry.getKey();
					final DTOCredentials cred = entry.getValue();
					schueler.CredentialID = cred.ID;
				}
				schuelerRepository.update(mapNeueCreds.keySet());
			}
			schuelerRepository.flush();

			// Durchwandere die Schüler-Daten und lege die AES-Schlüssel in die Rückgabe-Map
			for (final DTOSchueler schueler : listSchueler) {
				final DTOCredentials cred = mapCredentials.get(schueler.CredentialID);
				final AES aes = new AES(AESAlgo.CBC_PKCS5PADDING, AES.getKeyFromByteArray(Base64.getDecoder().decode(cred.AES)));
				result.put(schueler.ID, aes);
			}
			return result;
		});
	}


	private void updateCredentialsWithNewAESKey(final Collection<DTOCredentials> creds) {
		try {
			final List<DTOCredentials> updatedCredentials = new ArrayList<>();
			for (final DTOCredentials cred : creds) {
				if (cred.AES == null) {
					cred.AES = Base64.getEncoder().encodeToString(AES.getRandomKey256().getEncoded());
					updatedCredentials.add(cred);
				}
			}
			credentialsRepository.update(updatedCredentials);
			credentialsRepository.flush();
		} catch (final AESException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Erstellen der AES-Schlüssel.");
		}
	}


	private Map<DTOSchueler, DTOCredentials> prepareNewSchuelerCredentials(final Collection<DTOSchueler> listSchuelerOhneCreds) {
		// Bestimme zur Erzeugung die Liste der bereits existierenden Benutzernamen und Pseudonyme
		final List<DTOCredentials> allCreds = credentialsRepository.getAll();
		final Set<String> allUsernames = allCreds.stream().map(c -> c.Benutzername).collect(Collectors.toSet());
		final Set<String> allUserPseudonyms = allCreds.stream().map(c -> c.BenutzernamePseudonym).collect(Collectors.toSet());

		// Durchwandere die Schüler-DTOs und erzeuge die Credentials für diese
		long credNextId = credentialsRepository.getNextID();
		final Map<DTOSchueler, DTOCredentials> result = new HashMap<>();
		for (final DTOSchueler schueler: listSchuelerOhneCreds) {
			// Erstelle den nächsten Credentials-Eintrag
			final DTOCredentials cred = new DTOCredentials(credNextId++, determineUsername(schueler.Nachname, schueler.Vorname, 16, allUsernames));
			cred.BenutzernamePseudonym = determinePseudonym("s", cred.ID, allUserPseudonyms);
			cred.Initialkennwort = Passwords.generateRandomPassword(12);
			cred.PasswordHash = Benutzer.erstellePasswortHash(cred.Initialkennwort);
			cred.RSAPublicKey = null;
			cred.RSAPrivateKey = null;
			cred.AES = null;
			result.put(schueler, cred);

			// Aktualisiere noch die Listen mit bereits existierenden Benutzernamen und Pseudonyme für den nächsten Schüler
			allUsernames.add(cred.Benutzername);
			allUserPseudonyms.add(cred.BenutzernamePseudonym);
		}
		return result;
	}

}
