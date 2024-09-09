package de.svws_nrw.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKey;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESAlgo;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.lehrer.LehrerLeitungsfunktion;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.ext.jbcrypt.BCrypt;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse repräsentiert u.a. bei einem OpenAPI-Zugriff den angemeldeten Benutzer
 * bei einer SVWS-Datenbank. Für den Datenbankzugriff wird intern eine Instanz der Klasse
 * {@link DBEntityManager} verwendet.
 */
public final class Benutzer {

	/** Der Benutzername des angemeldeten Benutzers. */
	private String _username;

	/** Das Kennwort des angemeldeten Benutzers */
	private String _password;

	/** Das AES-Objekt, passend zum angemeldeten Benutzer. */
	private AES _aes;

	/** Enthält bei einem Open-API-Zugriff die Datenbank-ID des zugehörigen SVWS-Benutzers. */
	private Long _id = null;

	/** Die verwendete Datenbank-Konfiguration {@link DBConfig} */
	private final DBConfig _config;

	/** Der Manager für die Datenbank-Verbindungen */
	public final ConnectionManager connectionManager;

	/** Enthält bei einem Open-API-Zugriff die Datenbank-ID des zugehörigen Lehrers, falls der Benutzer ein Lehrer ist*/
	private Long _idLehrer = null;


	/**
	 * Enthält nach der Anmeldung die grundlegenden Informationen zur Schule und den dortigen Schuljahresabschnitten.
	 */
	private SchuleStammdaten _stammdaten = null;

	/**
	 * Eine Map für den schnellen Zugriff auf die Schuljahresabschnitte aus den Stammdaten der Schule
	 */
	private final @NotNull Map<Long, Schuljahresabschnitt> _mapSchuljahresabschnitte = new HashMap<>();

	/**
	 * Enthält die Information welche Kompetenzen der Benutzer in Bezug auf den Datenbankzugriff hat.
	 * Die Kompetenzen erlauben die Zuordnung von OpenAPI-Methode zu einzelnen Kompetenzbereichen, welche
	 * dann einem Datenbankbenutzer administrativ zugewiesen werden. Diese Zuordnung ist in der SVWS-Datenbank
	 * gespeichert.
	 */
	private List<BenutzerKompetenz> _kompetenzen = new ArrayList<>();

	/**
	 * Die Menge der bei der Operation verwendeten Benutzerkompetenzen, sofern nicht die Admin-Kompetenz verwendet
	 * wurde oder keine Kompetenz notwendig war.
	 */
	private final Set<BenutzerKompetenz> _kompetenzenVerwendet = new HashSet<>();

	/**
	 * Enthält die IDs zu den Klassen, bei welchen der Benutzer einen vollständigen Zugriff bei
	 * funktionsbezogenenen Operationen erhält. Dies kann z.B. durch die Tätigkeit als Klassenlehrer oder
	 * auch durch die Tätigkeit als Abteilungsleiter erfolgen.
	 */
	private final Set<Long> _idsKlassen = new HashSet<>();

	/**
	 * Enthält die aktuellen Leitungsfunktionen des Benutzers, falls es sich um einen Lehrer
	 * handelt. Diese sind normalerweise in Bezug auf das aktuelle Datum des Servers gesetzt.
	 */
	private final Set<LehrerLeitungsfunktion> _leitungsfunktionen = new HashSet<>();

	/**
	 * Enthält die Abiturjahrgänge, in welchen der Benutzer, wenn es sich um einen Lehrer handelt,
	 * als Beratungslehrer eingetragen ist.
	 */
	private final Set<Integer> _abiturjahrgaenge = new HashSet<>();


	/**
	 * Erzeugt einen neuen Datenbank-Benutzer, wobei die für den Datenbankzugriff zu
	 * verwendende {@link DBConfig} angegeben wird.
	 *
	 * @param config   die Datenbank-Konfiguration
	 *
	 * @throws DBException wenn die Authentifizierung fehlschlägt
	 */
	private Benutzer(final DBConfig config) throws DBException {
		this._username = "niemand";
		this._password = "keines";
		this._aes = null;
		this._config = config;
		this.connectionManager = ConnectionManager.get(config);
	}


	/**
	 * Erstellt basierend auf der übergebenen Datenbank-Konfiguration {@link DBConfig}
	 * einen neuen Datenbank-{@link Benutzer}.
	 *
	 * @param config   die Datenbank-Konfiguration.
	 *
	 * @return die neue Instanz des {@link Benutzer}
	 *
	 * @throws DBException wenn die Authentifizierung fehlschlägt
	 */
	public static Benutzer create(final DBConfig config) throws DBException {
		return new Benutzer(config);
	}


	/**
	 * Erstellt eine neue Verbindung mit den gleichen Verbindungsinformationen, aber zu
	 * einem anderen Schema.
	 * Anmerkung: ein identischer Schema-Name wird als Sonderfall auch zugelassen.
	 *
	 * @param schema   der name des Schemas für die neue Verbindung
	 *
	 * @return der Benutzer für den Datenbankzugriff, oder null im Fehlerfall
	 *
	 * @throws DBException wenn die Authentifizierung fehlschlägt
	 */
	public Benutzer connectTo(final String schema) throws DBException {
		return create(_config.switchSchema(schema));
	}


	/**
	 * Erstellt zu dem angegeben Kennwort einen Passwort-Hash mit dem
	 * BCrypt-Algorithmus. Dabei wird ein zufälliger Wert für den Salt verwendet.
	 *
	 * @param password   das Kennwort im Klartext.
	 *
	 * @return der Hash des Kennwortes
	 */
	public static String erstellePasswortHash(final String password) {
		if ((password == null) || ("".equals(password)))
			return null;
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}


	/**
	 * Prüft, ob dem Benutzer eine der übergeben Kompetenzen eingeräumt wurde oder nicht.
	 * Hierbei findet keine Prüfung auf einen Admin-Benutzer mit vollen Rechten statt.
	 *
	 * @param kompetenzen   die zu prüfenden Kompetenzen oder BenutzerKompetenz.KEINE
	 *
	 * @return true, im Falle von BenutzerKompetenz.KEINE als zu prüfende Kompetenz
	 *         oder wenn der Benutzer einer der übergebenen Kompetenzen besitzt. Anonsten
	 *         wird false zurückgegeben.
	 */
	private boolean hatKompetenz(final Set<BenutzerKompetenz> kompetenzen) {
		if (kompetenzen == null)
			return false;
		if (kompetenzen.contains(BenutzerKompetenz.KEINE))
			return true;
		_kompetenzenVerwendet.clear();
		for (final BenutzerKompetenz kompetenz : kompetenzen)
			if (this._kompetenzen.contains(kompetenz))
				_kompetenzenVerwendet.add(kompetenz);
		return !_kompetenzenVerwendet.isEmpty();
	}


	/**
	 * Prüft, ob die übergebene Kompetenz bei der Anmeldung verwendet wurde oder nicht.
	 *
	 * @param kompetenz   die auf Verwendung zu prüfende Kompetenz
	 *
	 * @return true, wenn die Kompetenz verwendet wurde und ansonsten false
	 */
	public boolean hatVerwendeteKompetenz(final BenutzerKompetenz kompetenz) {
		return _kompetenzenVerwendet.contains(kompetenz);
	}


	/**
	 * Prüft, ob es sich bei dem Benutzer um einen Admin-Benutzer mit
	 * allen Kompetenzen handelt.
	 *
	 * @return true, falls es sich um einen Admin-Benutzer mit allen Kompetenzen
	 *         handelt und ansonsten false
	 */
	public boolean istAdmin() {
		return (_kompetenzen != null) && _kompetenzen.contains(BenutzerKompetenz.ADMIN);
	}


	/**
	 * Prüft, ob der Benutzer eine der übergeben Kompetenz eingeräumt wurde oder nicht.
	 * Hierbei findet zunächst eine Prüfung auf einen Admin-Benutzer mit vollen Rechten
	 * statt. Ist dies ein Admin-Benutzer, so hat er die Kompetenz ohne dass weiter
	 * geprüft werden muss.
	 *
	 * @param kompetenzen   die zu prüfenden Kompetenzen oder BenutzerKompetenz.KEINE
	 *
	 * @return true, im Falle von BenutzerKompetenz.KEINE als zu prüfende Kompetenz
	 *         oder wenn der Benutzer die übergebene Kompetenz besitzt. Anonsten
	 *         wird false zurückgegeben.
	 */
	public boolean pruefeKompetenz(final BenutzerKompetenz... kompetenzen) {
		final Set<BenutzerKompetenz> tmp = new HashSet<>(Arrays.asList(kompetenzen));
		return istAdmin() || hatKompetenz(tmp);
	}


	/**
	 * Prüft, ob der Benutzer eine der übergeben Kompetenz eingeräumt wurde oder nicht.
	 * Hierbei findet zunächst eine Prüfung auf einen Admin-Benutzer mit vollen Rechten
	 * statt. Ist dies ein Admin-Benutzer, so hat er die Kompetenz ohne dass weiter
	 * geprüft werden muss.
	 *
	 * @param kompetenzen   die zu prüfenden Kompetenzen oder BenutzerKompetenz.KEINE
	 *
	 * @return true, im Falle von BenutzerKompetenz.KEINE als zu prüfende Kompetenz
	 *         oder wenn der Benutzer die übergebene Kompetenz besitzt. Anonsten
	 *         wird false zurückgegeben.
	 */
	public boolean pruefeKompetenz(final Set<BenutzerKompetenz> kompetenzen) {
		return istAdmin() || hatKompetenz(kompetenzen);
	}


	/**
	 * Gibt im Falle eines Open-API-Zugriffs die Datenbank-ID des angemeldeten
	 * SVWS-Benutzers zurück.
	 *
	 * @return die Datenbank-ID des Benutzer oder null
	 */
	public Long getId() {
		return _id;
	}


	/**
	 * Setzt die Datenbank-ID des angemeldeten SVWS-Benutzers.
	 *
	 * @param id   die ID des SVWS-Benutzers
	 */
	public void setId(final Long id) {
		this._id = id;
	}


	/**
	 * Gibt den Benutzernamen zurück.
	 *
	 * @return der Benutzername
	 */
	public String getUsername() {
		return _username;
	}


	/**
	 * Setzt den Benutzernamen diese Benutzers.
	 *
	 * @param username   der zu setzende Benutzername
	 */
	public void setUsername(final String username) {
		this._username = username;
	}


	/**
	 * Gibt das Kennwort dieses Benutzers zurück.
	 *
	 * @return das Kennwort
	 */
	public String getPassword() {
		return _password;
	}


	/**
	 * Setzt das Kennwort dieses Benutzers.
	 *
	 * @param password   das zu setzende Kennwort
	 */
	public void setPassword(final String password) {
		this._password = password;
	}


	/**
	 * Gibt das AES-Objekt zur Verschlüsselung von Benutzerspezifischen
	 * Daten zurück.
	 *
	 * @return das AES-Verschlüsselungsobjekt
	 */
	public AES getAES() {
		return _aes;
	}


	/**
	 * Setzt das AES-Objekt zur Verschlüsselung von Benuterspezifischen Daten
	 * in Bezug auf den aktuellen Benutzernamen und seinem Kennwort.
	 */
	public void setAES() {
		this._aes = getAESInstance(_username, _password);
	}


	/**
	 * Erzeugt eine Instanz für die AES-Verschlüsselung basierend auf dem Benutzernamen und dem
	 * Kennwort des angemeldeten Benutzers.
	 *
	 * @param username   der Benutzername
	 * @param password   das Anmeldekennwort
	 *
	 * @return das AES-Verschlüsselungsobjekt
	 */
	public static AES getAESInstance(final String username, final String password) {
		try {
			final SecretKey key = AES.getKey256(password, username);
			return new AES(AESAlgo.CBC_PKCS5PADDING, key);
		} catch (@SuppressWarnings("unused") final AESException e) {
			return null;
		}
	}


	/**
	 * Gibt die Liste der zugeorndeten Komptenzen zurück.
	 *
	 * @return die Liste der Benutzer-Kompetenzen
	 */
	public List<BenutzerKompetenz> getKompetenzen() {
		return _kompetenzen;
	}


	/**
	 * Ordnet die übergebenen Kompetenzen diesem Benutzer zu.
	 *
	 * @param kompetenzen   die Kompetenzen, die diesem Benutzer zugeordnet werden.
	 */
	public void setKompetenzen(final List<BenutzerKompetenz> kompetenzen) {
		this._kompetenzen = kompetenzen;
	}


	/**
	 * Gibt eine neue Instanz des {@link DBEntityManager} zurück, die diesem Benutzer
	 * für den Datenbank-Zugriff zugeordnet ist. Diese repräsentiert gleichzeitig
	 * eine neue Verbindung.
	 *
	 * @return die Instanz des {@link DBEntityManager}
	 */
	public DBEntityManager getEntityManager() {
		try {
			return new DBEntityManager(this, _config);
		} catch (@SuppressWarnings("unused") final IllegalStateException e) {
			// TODO error handling
			return null;
		}
	}


	/**
	 * Gibt die Stammdaten der Schule zurück.
	 *
	 * @return die Stammdaten der Schule
	 */
	public @NotNull SchuleStammdaten schuleGetStammdaten() {
		if (_stammdaten == null)
			throw new DeveloperNotificationException("Es wurde auf die Stammdaten der Schule zugegriffen, obwohl diese nicht geladen sind.");
		return _stammdaten;
	}


	/**
	 * Setzt die Stammdaten der Schule und initialisiert die Map für den Zugriff
	 * auf die Schuljahresabschnitte.
	 *
	 * @param stammdaten   die neuen Stammdaten
	 */
	public void schuleSetStammdaten(final SchuleStammdaten stammdaten) {
		_stammdaten = stammdaten;
		this._mapSchuljahresabschnitte.clear();
		if (_stammdaten != null)
			for (final @NotNull Schuljahresabschnitt abschnitt : _stammdaten.abschnitte)
				_mapSchuljahresabschnitte.put(abschnitt.id, abschnitt);
	}


	/**
	 * Gibt den Schuljahresabschnitt zu der ID zurück oder null, wenn keiner für die ID existiert.
	 *
	 * @param id   die ID des Schuljahresabschnitts
	 *
	 * @return der Schuljahresabschnitt oder null
	 */
	public Schuljahresabschnitt schuleGetAbschnittById(final long id) {
		return _mapSchuljahresabschnitte.get(id);
	}


	/**
	 * Bestimmt den aktuellen Schuljahresabschnitt der Schule
	 *
	 * @return der aktuelle Schuljahresabschnitt der Schule
	 */
	public @NotNull Schuljahresabschnitt schuleGetSchuljahresabschnitt() {
		final Schuljahresabschnitt result = _mapSchuljahresabschnitte.get(schuleGetStammdaten().idSchuljahresabschnitt);
		if (result == null)
			throw new DeveloperNotificationException("Der aktuelle Schuljahresabschnitt der Schule konnte nicht bestimmt werden.");
		return result;
	}


	/**
	 * Bestimmt den Schuljahresabschnitt für die angebene ID oder als alternative den der Schule
	 *
	 * @param id   die ID des Schuljahresabschnitts
	 *
	 * @return der Schuljahresabschnitt für ID oder der der Schule, falls die ID ungültig ist
	 */
	public @NotNull Schuljahresabschnitt schuleGetSchuljahresabschnittByIdOrDefault(final long id) {
		final Schuljahresabschnitt result = _mapSchuljahresabschnitte.get(id);
		if (result != null)
			return result;
		return schuleGetSchuljahresabschnitt();
	}


	/**
	 * Gibt das aktuelle Schuljahr der Schule zurück.
	 *
	 * @return das aktuelle Schuljahr der Schule
	 */
	public int schuleGetSchuljahr() {
		return schuleGetSchuljahresabschnitt().schuljahr;
	}


	/**
	 * Gibt den aktuellen Abschnitt im Schuljahr der Schule zurück.
	 *
	 * @return der aktuelle Abschnitt
	 */
	public int schuleGetAbschnitt() {
		return schuleGetSchuljahresabschnitt().abschnitt;
	}


	/**
	 * Gibt die Schulform der Schule zurück.
	 *
	 * @return die Schulform der Schule
	 */
	public @NotNull Schulform schuleGetSchulform() {
		final Schulform result = Schulform.data().getWertByKuerzel(schuleGetStammdaten().schulform);
		if (result == null)
			throw new DeveloperNotificationException("Die Schulform der Schule konnte nicht bestimmt werden.");
		return result;
	}


	/**
	 * Gibt den Katalog-Eintrag für die Schulform und das aktuelle Schuljahr der Schule zurück.
	 *
	 * @return der Schulform-Katalog-Eintrag
	 */
	public @NotNull SchulformKatalogEintrag schuleGetSchulformKatalogEintrag() {
		final SchulformKatalogEintrag result = schuleGetSchulform().daten(schuleGetSchuljahresabschnitt().schuljahr);
		if (result == null)
			throw new DeveloperNotificationException(
					"Der Schulform-Katalog-Eintrag für die Schulform der Schule konnte für das aktuelle Schuljahr %d nicht bestimmt werden."
							.formatted(schuleGetSchuljahresabschnitt().schuljahr));
		return result;
	}


	/**
	 * Gibt zurück, ob die Schulform dieser Schule eine gymnasiale Oberstufe erlaubt oder nicht.
	 *
	 * @return true, wenn die Schule eine gymnasiale Oberstufe hat, und false, wenn nicht.
	 */
	public boolean schuleHatGymOb() {
		return schuleGetSchulformKatalogEintrag().hatGymOb;
	}


	/**
	 * Gibt die Lehrer-ID des angemeldeten Benutzers zurück, sofern
	 * ein Lehrer angemeldet ist.
	 *
	 * @return die Lehrer-ID oder null, falls der angemeldete Benutzer kein Lehrer ist
	 */
	public Long getIdLehrer() {
		return _idLehrer;
	}


	/**
	 * Setzt die Lehrer-ID des angemeldeten Benutzers
	 *
	 * @param idLehrer   die Lehrer-ID oder null, falls der angemeldete Benutzer kein Lehrer ist
	 */
	public void setIdLehrer(final Long idLehrer) {
		this._idLehrer = idLehrer;
	}


	/**
	 * Setzt die IDs der Klassen, bei welchen der Benutzer einen vollständigen Zugriff bei
	 * funktionsbezogenenen Operationen erhält. Dies kann z.B. durch die Tätigkeit als Klassenlehrer
	 * oder auch durch die Tätigkeit als Abteilungsleiter erfolgen.
	 *
	 * @param idsKlassen   die IDs der Klassen
	 */
	public void setKlassenIDs(final Collection<Long> idsKlassen) {
		this._idsKlassen.clear();
		this._idsKlassen.addAll(idsKlassen);
	}


	/**
	 * Gibt die IDs der Klasse zurück, bei welchen der Benutzer einen vollständigen Zugriff bei
	 * funktionsbezogenenen Operationen erhält. Dies kann z.B. durch die Tätigkeit als Klassenlehrer
	 * oder auch durch die Tätigkeit als Abteilungsleiter erfolgen.
	 *
	 * @return die IDs der Klassen
	 */
	public Set<Long> getKlassenIDs() {
		return new HashSet<>(this._idsKlassen);
	}


	/**
	 * Setzt die aktuellen Leitungsfunktionen, welche dem Benutzer zugeordnet sind.
	 *
	 * @param funktionen   die Leitungsfunktionen
	 */
	public void setLeitungsfunktionen(final Collection<LehrerLeitungsfunktion> funktionen) {
		this._leitungsfunktionen.clear();
		this._leitungsfunktionen.addAll(funktionen);
	}


	/**
	 * Gibt die aktuellen Leitungsfunktionen zurück, welche dem Benutzer zugeordnet sind.
	 *
	 * @return die Leitungsfunktionen
	 */
	public Set<LehrerLeitungsfunktion> getLeitungsfunktionen() {
		return new HashSet<>(this._leitungsfunktionen);
	}


	/**
	 * Setzt die Abiturjahrgänge, in welchen der Benutzer Beratungslehrer ist.
	 *
	 * @param abijahrgaenge   die Abiturjahrgänge
	 */
	public void setAbiturjahrgaenge(final Collection<Integer> abijahrgaenge) {
		this._abiturjahrgaenge.clear();
		this._abiturjahrgaenge.addAll(abijahrgaenge);
	}


	/**
	 * Gibt die Abiturjahrgänge, in welchen der Benutzer Beratungslehrer ist.
	 *
	 * @return die Abiturjahrgänge
	 */
	public Set<Integer> getAbiturjahrgaenge() {
		return new HashSet<>(this._abiturjahrgaenge);
	}

}
