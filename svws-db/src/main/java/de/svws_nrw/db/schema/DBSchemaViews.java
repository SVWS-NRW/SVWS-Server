package de.svws_nrw.db.schema;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.db.converter.current.BenutzerTypConverter;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.SchuelerStatusConverter;
import de.svws_nrw.db.converter.current.statkue.SchulformKuerzelConverter;
import de.svws_nrw.db.converter.current.statkue.SchulgliederungKuerzelConverter;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält alle Definition von Views für alle Revisionen der SVWS-DB
 */
public final class DBSchemaViews {

	/** Die Singleton-Instanz der View-Definitionen.*/
	private static DBSchemaViews instance;

	/** Eine Liste aller Views */
	private final ArrayList<View> allViews = new ArrayList<>();

	/** Eine HashMap mit allen Views, welche den Revisionen der Datenbank zugeordnet sind. */
	private final @NotNull HashMap<@NotNull Long, List<View>> views = new HashMap<>();

	/** Eine HashMap mit allen Views, welche der Revision der Datenbank als veraltet zugeordnet sind. */
	private final @NotNull HashMap<@NotNull Long, List<View>> viewsDeprecated = new HashMap<>();


	/**
	 * Erstellt die Übersicht über alle View-Definitionen in der Datenbank
	 */
	private DBSchemaViews() {
		// Revision 6
		add_V_Benutzer();
		add_V_Benutzerkompetenzen();
		add_V_BenutzerDetails();
		add_V_Gost_Schueler_Abiturjahrgang();
	}


	/**
	 * Fügt die übergebene View in die Liste aller Views ein.
	 *
	 * @param view   die hinzuzufügende View
	 */
	private void addView(@NotNull final View view) {
		if (view == null)
			throw new NullPointerException("Kann null nicht zu der Liste der Views hinzufügen.");
		allViews.add(view);
		final int rev = view.revision;
		if (rev < 0)
			throw new IllegalArgumentException("Negative Revisionen dürfen bei Views nicht angegeben werden");
		getViewsCreated(rev).add(view);
		if ((view.veraltet != null) && (view.veraltet >= 0))
			getViewsDeprecated(view.veraltet).add(view);
	}


	/**
	 * Gibt alle Views zurück, welche in der angegebenen Revision erstellt werden.
	 *
	 * @param revision   die Revision
	 *
	 * @return die Liste der Views, welche in der angegebenen Revision erstellt werden.
	 */
	public List<View> getViewsCreated(final long revision) {
		List<View> v = views.get(revision);
		if (v == null) {
			v = new ArrayList<>();
			views.put(revision, v);
		}
		return v;
	}


	/**
	 * Gibt alle Views zurück, welche in der angegebenen Revision als veraltet gesetzt sind.
	 *
	 * @param revision   die Revision
	 *
	 * @return die Liste der Views, welche in der angegebenen Revision als veraltet gesetzt sind.
	 */
	public List<View> getViewsDeprecated(final long revision) {
		List<View> v = viewsDeprecated.get(revision);
		if (v == null) {
			v = new ArrayList<>();
			viewsDeprecated.put(revision, v);
		}
		return v;
	}


	/**
	 * Gibt alle Views zurück, welche in der angegebenen Revision aktiv sind.
	 *
	 * @param revision   die Revision
	 *
	 * @return die Liste der Views, welche in der angegebenen Revision aktiv sind.
	 */
	public List<View> getViewsActive(final long revision) {
		final ArrayList<View> result = new ArrayList<>();
		for (final View v : allViews)
			if ((revision >= v.revision) && ((v.veraltet == null) || (revision < v.veraltet)))
				result.add(v);
		return result;
	}


	/**
	 * Gibt die Instanz der Klasse {@link DBSchemaViews} zurück. Ist keine Instanz vorhanden, so
	 * wird sie zunächst mit dem privaten Konstruktor erzeugt.
	 *
	 * @return die Instanz der Klasse {@link DBSchemaViews} mit den Informationen des SVWS-Datenbank-Schemas
	 */
	public static DBSchemaViews getInstance() {
		if (instance == null) {
			instance = new DBSchemaViews();
		}
		return instance;
	}

	private void add_V_Benutzer() {
		final View view = new View(
			"V_Benutzer", "views.benutzer", "DTOViewBenutzer",
			"Eine Benutzerliste mit Benutzernamen und Password-Hash von allen Benutzern, die eine Berechtigung zum Zugang zum SVWS-Server haben",
			6, null,
			"""
			(
			  SELECT Benutzer.ID, BenutzerAllgemein.AnzeigeName AS AnzeigeName, Credentials.Benutzername, Credentials.PasswordHash FROM Benutzer JOIN BenutzerAllgemein ON Benutzer.Allgemein_ID = BenutzerAllgemein.ID JOIN Credentials ON BenutzerAllgemein.CredentialID = Credentials.ID
			  UNION
			  SELECT Benutzer.ID, Concat(K_Lehrer.Vorname, ' ', K_Lehrer.Nachname) AS AnzeigeName, Credentials.Benutzername, Credentials.PasswordHash FROM Benutzer JOIN K_Lehrer ON Benutzer.Lehrer_ID = K_Lehrer.ID JOIN Credentials ON K_Lehrer.CredentialID = Credentials.ID
			  UNION
			  SELECT Benutzer.ID, Concat(Schueler.Vorname, ' ', Schueler.Name) AS AnzeigeName, Credentials.Benutzername, Credentials.PasswordHash FROM Benutzer JOIN Schueler ON Benutzer.Schueler_ID = Schueler.ID JOIN Credentials ON Schueler.CredentialID = Credentials.ID
			  UNION
			  SELECT Benutzer.ID, Concat(SchuelerErzAdr.Vorname1, ' ', SchuelerErzAdr.Name1) AS AnzeigeName, Credentials.Benutzername, Credentials.PasswordHash FROM Benutzer JOIN SchuelerErzAdr ON Benutzer.Erzieher_ID = SchuelerErzAdr.ID JOIN Credentials ON SchuelerErzAdr.CredentialID = Credentials.ID
			) creds JOIN (
			  SELECT Benutzer.ID, CASE WHEN max(Benutzer.IstAdmin) = 1 OR max(Benutzergruppen.IstAdmin) = 1 THEN 1 ELSE 0 END AS IstAdmin FROM Benutzer LEFT JOIN BenutzergruppenMitglieder ON Benutzer.ID = BenutzergruppenMitglieder.Benutzer_ID LEFT JOIN Benutzergruppen ON Benutzergruppen.ID = BenutzergruppenMitglieder.Gruppe_ID
			  GROUP BY Benutzer.ID
			) admins ON creds.ID = admins.ID
			"""
		).add("ID", "Die eindeutige ID des Benutzers", "Long", "creds.ID", null, true)
		 .add("AnzeigeName", "Der Anzeige-Name des Benutzers (z.B. Max Mustermann)", "String", "creds.AnzeigeName", null, false)
		 .add("Benutzername", "Der Anmeldename des Benutzers (z.B. max.mustermann)", "String", "creds.Benutzername", null, false)
		 .add("PasswordHash", "Der bcrypt-Password-Hash zur Überprüfung des Benutzer-Kennwortes", "String", "creds.PasswordHash", null, false)
		 .add("IstAdmin", "Gibt an, ob es sich um einen administrativen Benutzer handelt oder nicht", "Boolean", "admins.IstAdmin", Boolean01Converter.class, false);
		addView(view);
	}

	private void add_V_Benutzerkompetenzen() {
		final View view = new View(
				"V_Benutzerkompetenzen", "views.benutzer", "DTOViewBenutzerKompetenz",
				"Eine Liste von den effektiven Kompetenzen, die den Benutzern entweder direkt oder über Benutzergruppen zugewiesen wurden",
				6, null,
                """
                (
                  SELECT Benutzer.ID AS Benutzer_ID, Kompetenz_ID FROM Benutzer JOIN BenutzerKompetenzen ON Benutzer.ID = BenutzerKompetenzen.Benutzer_ID
                  UNION
                  SELECT Benutzer.ID AS Benutzer_ID, Kompetenz_ID FROM Benutzer
                  JOIN BenutzergruppenMitglieder ON Benutzer.ID = BenutzergruppenMitglieder.Benutzer_ID
                  JOIN BenutzergruppenKompetenzen ON BenutzergruppenMitglieder.Gruppe_ID = BenutzergruppenKompetenzen.Gruppe_ID
                ) effkomp
                """
		).add("Benutzer_ID", "Die ID des Benutzers", "Long", "Benutzer_ID", null, true)
		 .add("Kompetenz_ID", "Die ID der Benutzer-Kompetenz", "Long", "Kompetenz_ID", null, true);
		addView(view);
	}

	private void add_V_BenutzerDetails() {
		final View view = new View(
				"V_BenutzerDetails", "views.benutzer", "DTOViewBenutzerdetails",
				"Eine Benutzerliste von allen Benutzern, die eine Berechtigung zum Zugang zum SVWS-Server haben, auch mit der CredentialID, dem Typ des Benutzers und der zugehörigen ID",
				6, null,
                """
                (
                  SELECT Benutzer.ID, Credentials.ID AS credentialID, Benutzer.Typ, Benutzer.Allgemein_ID AS TypID, BenutzerAllgemein.AnzeigeName AS AnzeigeName, Credentials.Benutzername, Credentials.PasswordHash FROM Benutzer JOIN BenutzerAllgemein ON Benutzer.Allgemein_ID = BenutzerAllgemein.ID JOIN Credentials ON BenutzerAllgemein.CredentialID = Credentials.ID
                  UNION
                  SELECT Benutzer.ID, Credentials.ID AS credentialID, Benutzer.Typ, Benutzer.Lehrer_ID AS TypID, Concat(K_Lehrer.Vorname, ' ', K_Lehrer.Nachname) AS AnzeigeName, Credentials.Benutzername, Credentials.PasswordHash FROM Benutzer JOIN K_Lehrer ON Benutzer.Lehrer_ID = K_Lehrer.ID JOIN Credentials ON K_Lehrer.CredentialID = Credentials.ID
                  UNION
                  SELECT Benutzer.ID, Credentials.ID AS credentialID, Benutzer.Typ, Benutzer.Schueler_ID AS TypID, Concat(Schueler.Vorname, ' ', Schueler.Name) AS AnzeigeName, Credentials.Benutzername, Credentials.PasswordHash FROM Benutzer JOIN Schueler ON Benutzer.Schueler_ID = Schueler.ID JOIN Credentials ON Schueler.CredentialID = Credentials.ID
                  UNION
                  SELECT Benutzer.ID, Credentials.ID AS credentialID, Benutzer.Typ, Benutzer.Erzieher_ID AS TypID, Concat(SchuelerErzAdr.Vorname1, ' ', SchuelerErzAdr.Name1) AS AnzeigeName, Credentials.Benutzername, Credentials.PasswordHash FROM Benutzer JOIN SchuelerErzAdr ON Benutzer.Erzieher_ID = SchuelerErzAdr.ID JOIN Credentials ON SchuelerErzAdr.CredentialID = Credentials.ID
                ) creds JOIN (
                  SELECT Benutzer.ID, CASE WHEN max(Benutzer.IstAdmin) = 1 OR max(Benutzergruppen.IstAdmin) = 1 THEN 1 ELSE 0 END AS IstAdmin
                  FROM Benutzer
                    LEFT JOIN BenutzergruppenMitglieder ON Benutzer.ID = BenutzergruppenMitglieder.Benutzer_ID
                    LEFT JOIN Benutzergruppen ON Benutzergruppen.ID = BenutzergruppenMitglieder.Gruppe_ID
                  GROUP BY Benutzer.ID
                ) admins ON creds.ID = admins.ID
                """
		).add("ID", "Die eindeutige ID des Benutzers", "Long", "creds.ID", null, true)
		 .add("credentialID", "Die ID der Benutzer-Credentials", "Long", "creds.credentialID", null, false)
		 .add("Typ", "Der Typ des Benutzers", "BenutzerTyp", "creds.Typ", BenutzerTypConverter.class, false)
		 .add("TypID", "Die ID des Benutzers in der Benutzer-Typ-spezifischen Tabelle (z.B. eine Schüler-ID)", "Long", "creds.TypID", null, false)
		 .add("AnzeigeName", "Der Anzeige-Name des Benutzers (z.B. Max Mustermann)", "String", "creds.AnzeigeName", null, false)
		 .add("Benutzername", "Der Anmeldename des Benutzers (z.B. max.mustermann)", "String", "creds.Benutzername", null, false)
		 .add("PasswordHash", "Der bcrypt-Password-Hash zur Überprüfung des Benutzer-Kennwortes", "String", "creds.PasswordHash", null, false)
		 .add("IstAdmin", "Gibt an, ob es sich um einen administrativen Benutzer handelt oder nicht", "Boolean", "admins.IstAdmin", Boolean01Converter.class, false);
		addView(view);
	}

	private void add_V_Gost_Schueler_Abiturjahrgang() {
		final View view = new View(
				"V_Gost_Schueler_Abiturjahrgang", "views.gost", "DTOViewGostSchuelerAbiturjahrgang",
				"Eine Schülerliste mit der Zuordnung Abiturjahrgängen für die gymnasiale Oberstufe. Die View sollte nur bei Schulformen mit gymnasialer Oberstufe genutzt werden",
				6, null,
                """
                Schueler
                JOIN Schuljahresabschnitte ON Schueler.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schueler.Geloescht <> '+'
                JOIN SchuelerLernabschnittsdaten ON Schueler.Schuljahresabschnitts_ID = SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID
                  AND Schueler.ID = SchuelerLernabschnittsdaten.Schueler_ID AND SchuelerLernabschnittsdaten.WechselNr IS NULL
                JOIN EigeneSchule_Jahrgaenge ON SchuelerLernabschnittsdaten.Jahrgang_ID = EigeneSchule_Jahrgaenge.ID
                JOIN EigeneSchule
                LEFT JOIN EigeneSchule_Jahrgaenge jg_abgang ON Schueler.Entlassjahrgang_ID = jg_abgang.ID
                WHERE Schueler.Status <> 8 OR jg_abgang.ASDJahrgang IN ('EF', 'Q1', 'Q2')
                """
		).add("ID", "Die ID des Schülers", "Long", "Schueler.ID", null, true)
		 .add("Status", "Der Status des Schülers", "SchuelerStatus", "Schueler.Status", SchuelerStatusConverter.class, false)
		 .add("Schulform", "Die Schulform des Schülers", "Schulform", "EigeneSchule.SchulformKrz", SchulformKuerzelConverter.class, false)
		 .add("Schulgliederung", "Die Schulgliederung des Schülers", "Schulgliederung", "SchuelerLernabschnittsdaten.ASDSchulgliederung", SchulgliederungKuerzelConverter.class, false)
		 .add("Abiturjahr", "Das Abiturjahr des Schülers", "Long", "(EigeneSchule_Jahrgaenge.Restabschnitte / EigeneSchule.AnzahlAbschnitte) + Schuljahresabschnitte.Jahr", null, false);
		addView(view);
	}

}
