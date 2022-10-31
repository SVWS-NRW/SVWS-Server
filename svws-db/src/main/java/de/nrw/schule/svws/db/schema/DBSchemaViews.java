package de.nrw.schule.svws.db.schema;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält alle Definition von Views für alle Revisionen der SVWS-DB 
 */
public class DBSchemaViews {
	
	/** Die Singleton-Instanz der View-Definitionen.*/
	private static DBSchemaViews instance;
	
	/** Eine Liste aller Views */
	private Vector<View> allViews = new Vector<>();
	
	/** Eine HashMap mit allen Views, welche den Revisionen der Datenbank zugeordnet sind. */
	private @NotNull HashMap<@NotNull Long, List<View>> views = new HashMap<>();

	/** Eine HashMap mit allen Views, welche der Revision der Datenbank als veraltet zugeordnet sind. */
	private @NotNull HashMap<@NotNull Long, List<View>> viewsDeprecated = new HashMap<>();
	

	/**
	 * Erstellt die Übersicht über alle View-Definitionen in der Datenbank 
	 */
	private DBSchemaViews() {
		// Revision 0
		add_Schildintern_Berufsebene();
		add_Schildintern_K_Schulnote();
		add_Schulver_Schulformen();
		add_Statkue_Schulformen();
		add_Statkue_Herkunftsschulform();
		add_Statkue_Herkunftsart();
		add_Statkue_ZulJahrgaenge();
		add_Statkue_ZulKuArt();
		add_Statkue_ZulFaecher();
		add_Statkue_SVWS_ZulaessigeFaecher();
		add_Statkue_AllgMerkmale();
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
	private void addView(@NotNull View view) {
		if (view == null)
			throw new NullPointerException("Kann null nicht zu der Liste der Views hinzufügen.");
		allViews.add(view);
		int rev = view.revision;
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
	public List<View> getViewsCreated(long revision) {
		List<View> v = views.get(revision);
		if (v == null) {
			v = new Vector<>();
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
	public List<View> getViewsDeprecated(long revision) {
		List<View> v = viewsDeprecated.get(revision);
		if (v == null) {
			v = new Vector<>();
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
	public List<View> getViewsActive(long revision) {
		Vector<View> views = new Vector<>();
		for (View v : allViews)
			if ((revision >= v.revision) && ((v.veraltet == null) || (revision < v.veraltet)))
				views.add(v);
		return views;
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


	private void add_Schildintern_Berufsebene() {
		View view = new View(
				"Schildintern_Berufsebene", "views.schildintern", "DTOSchildInternBerufsebenen", 
				"View zur Simulation einer Schildintern-Tabelle: Berufsebenen für die Fachklassen an BK – Zeugnisdruck",
				0, null,
                """
				((SELECT *, 1 AS Berufsebene FROM Berufskolleg_Berufsebenen1) UNION
				 (SELECT *, 2 AS Berufsebene FROM Berufskolleg_Berufsebenen2) UNION
				 (SELECT *, 3 AS Berufsebene FROM Berufskolleg_Berufsebenen3)) bbe
                """
		).add("Berufsebene", "Die Berufsebene für Fachklassen", "Integer", "Berufsebene", null, true)
		 .add("Kuerzel", "Das Kürzel der Berufsebene", "String", "Kuerzel", null, true)
		 .add("Klartext", "Die Bezeichnung der Berufsebene", "String", "Bezeichnung", null, false)
		 .add("gueltigVon", "Gibt das Schuljahr an, ab dem die Berufsebene verwendet werden kann oder null, falls es keine Einschränkung gibt", "Integer", "gueltigVon", null, false)
		 .add("gueltigBis", "Gibt das Schuljahr an, bis zu welchem die Berufsebene verwendet werden kann oder null, falls es keine Einschränkung gibt", "Integer", "gueltigBis", null, false);
		addView(view);
	}

	private void add_Schildintern_K_Schulnote() {
		View view = new View(
				"Schildintern_K_Schulnote", "views.schildintern", "DTOSchildInternNote", 
				"View zur Simulation einer Schildintern-Tabelle: Liste der möglichen Noteneinträge in den Leistungsdaten",
				0, null,
                """
                Noten
                """
		).add("ID", "Die ID der Schulnote", "Long", "ID", null, true)
		 .add("Krz", "Das Notenkürzel (z.B. 5+)", "String", "Kuerzel", null, false)
		 .add("Art", "Die Art der Note (N - allgemeine Note, Z - mit Tendenz)", "String", "CASE WHEN IstTendenznote > 0 THEN 'Z' ELSE 'N' END", null, false)
		 .add("Bezeichnung", "Die ausführliche Noten-Bezeichnung (z.B. ausreichend (plus))", "String", "Text", null, false)
		 .add("Zeugnisnotenbez", "Die Bezeichnung auf einem Zeugnis ohne Tendenzen (z.B. ausreichend)", "String", """
                                           CASE
                                             WHEN Kuerzel = '5-' OR Kuerzel = '5+' THEN 'mangelhaft'
                                             WHEN Kuerzel = '4-' OR Kuerzel = '4+' THEN 'ausreichend'
                                             WHEN Kuerzel = '3-' OR Kuerzel = '3+' THEN 'befriedigend'
                                             WHEN Kuerzel = '2-' OR Kuerzel = '2+' THEN 'gut'
                                             WHEN Kuerzel = '1-' OR Kuerzel = '1+' THEN 'sehr gut'
                                             WHEN Kuerzel IN ('AM', 'AT', 'NT', 'NB', 'NE', 'LM') THEN '--------------------'
                                             ELSE Text
                                           END								 		
                                           """, null, false)
		 .add("Punkte", "Die Bezeichnung der Note in einer Laufbahn der Sekundarstufe II", "String", "CASE WHEN TextLaufbahnSII IS NULL THEN '--' ELSE TextLaufbahnSII END", null, false)
		 .add("Sortierung", "Ein Zahlwert, welcher die Sortierreihenfolge der einzelnen Noten spezifiziert", "Integer", "Sortierung", null, false)
		 .add("Sichtbar", "Gibt an, ob der Noteneintrag sichtbar ist", "Boolean", "'+'", "BooleanPlusMinusDefaultPlusConverter", false)
		 .add("Aenderbar", "Gibt an, ob der Noteneintrag änderbar ist", "Boolean", "'+'", "BooleanPlusMinusDefaultPlusConverter", false)
		 .add("gueltigVon", "Gibt das Schuljahr an, ab dem die Note verwendet werden kann oder null, falls es keine Einschränkung gibt", "Integer", "gueltigVon", null, false)
		 .add("gueltigBis", "Gibt das Schuljahr an, bis zu welchem die Note verwendet werden kann oder null, falls es keine Einschränkung gibt", "Integer", "gueltigBis", null, false);
		addView(view);
	}


	private void add_Schulver_Schulformen() {
		View view = new View(
				"Schulver_Schulformen", "views.schulver", "DTOSchulverSchulformen", 
				"View zur Simulation einer Schulver-Tabelle: Schulformen",
				0, null,
                """
                Schulformen WHERE Nummer IS NOT NULL
                """
		).add("Schulform", "Eindeutige Nummer der Schulform", "String", "Nummer", null, true)
		 .add("SF", "Das Kürzel der Schulform", "String", "Kuerzel", null, false)
		 .add("Bezeichnung", "Die Bezeichnung der Schulform", "String", "Bezeichnung", null, false)
		 .add("Flag", "Flag zur Kompatibilität zur Schulver", "String", "'1'", null, false)
		 .add("geaendert", "Das Datum der letzten Änderung", "String", "null", null, false)
		 .add("gueltigVon", "Gibt das Schuljahr an, ab dem die Schulform verwendet werden kann oder null, falls es keine Einschränkung gibt", "Integer", "gueltigVon", null, false)
		 .add("gueltigBis", "Gibt das Schuljahr an, bis zu welchem die Schulform verwendet werden kann oder null, falls es keine Einschränkung gibt", "Integer", "gueltigBis", null, false);
		addView(view);
	}


	private void add_Statkue_Schulformen() {
		View view = new View(
				"Statkue_Schulformen", "views.statkue", "DTOStatkueSchulformen", 
				"View zur Simulation einer Statkue-Tabelle: Schulformen",
				0, null,
                """
                Schulformen
                """
		).add("Schulform", "Eindeutige Nummer der Schulform", "String", "Nummer", null, false)
		 .add("SF", "Das Kürzel der Schulform", "String", "Kuerzel", null, true)
		 .add("Bezeichnung", "Die Bezeichnung der Schulform", "String", "Bezeichnung", null, true)
		 .add("Flag", "Flag zur Kompatibilität zur Schulver", "String", "'1'", null, false)
		 .add("geaendert", "Das Datumd er letzten Änderung", "String", "null", null, false)
		 .add("Sortierung", "Gibt die Sortierreihenfolge an", "Integer", "0", null, false)
		 .add("gueltigVon", "Gibt das Schuljahr an, ab dem die Schulform verwendet werden kann oder null, falls es keine Einschränkung gibt", "Integer", "gueltigVon", null, false)
		 .add("gueltigBis", "Gibt das Schuljahr an, bis zu welchem die Schulform verwendet werden kann oder null, falls es keine Einschränkung gibt", "Integer", "gueltigBis", null, false);
		addView(view);
	}

	private void add_Statkue_Herkunftsschulform() {
		View view = new View(
				"Statkue_Herkunftsschulform", "views.statkue", "DTOStatkueHerkunftsschulform", 
				"View zur Simulation einer Statkue-Tabelle: Herkunftsschulform",
				0, null,
                """
                Herkunft h 
                JOIN Herkunft_Schulformen hs ON h.ID = hs.Herkunft_ID WHERE h.gueltigBis IS NULL
                """
		).add("SF", "Zulässige Schulform für die Herkunft", "String", "hs.Schulform_Kuerzel", null, true)
		 .add("HSF", "Das Kürzel für die Herkunft/Herkunftsschulform", "String", "h.Kuerzel", null, true)
		 .add("Beschreibung", "Textuelle Beschreibung der Herkunft/Herkunftsschulform", "String", "h.Beschreibung", null, false)
		 .add("Flag", "Ein Flag (hier zur Kompatibilität vorhanden)", "String", "'1'", null, false)
		 .add("geaendert", "Das Datum der letzten Änderung (hier zur Kompatibilität vorhanden)", "String", "NULL", null, false)
		 .add("gueltigVon", "Gibt die Gültigkeit ab welchem Schuljahr an", "String", "h.gueltigVon", null, false)
		 .add("gueltigBis", "Gibt die Gültigkeit bis zu welchem Schuljahr an", "String", "h.gueltigBis", null, false);
		addView(view);
	}

	private void add_Statkue_Herkunftsart() {
		View view = new View(
				"Statkue_Herkunftsart", "views.statkue", "DTOStatkueHerkunftsart", 
				"View zur Simulation einer Statkue-Tabelle: Herkunftsart",
				0, null,
                """
			    Herkunftsart h 
			    JOIN Herkunftsart_Schulformen hs ON h.ID = hs.Herkunftsart_ID WHERE h.gueltigBis IS NULL
                """
		).add("SF", "Zulässige Schulform für die Herkunft", "String", "hs.Schulform_Kuerzel", null, true)
		 .add("Art", "Das Kürzel für die Herkunftsart", "String", "h.Kuerzel", null, true)
		 .add("Beschreibung", "Textuelle Beschreibung der Herkunftsart", "String", "hs.Bezeichnung", null, false)
		 .add("Flag", "Ein Flag (hier zur Kompatibilität vorhanden)", "String", "'1'", null, false)
		 .add("Sortierung", "Die Sortierung (hier zur Kompatibilität vorhanden)", "Integer", "0", null, false)
		 .add("geaendert", "Das Datum der letzten Änderung (hier zur Kompatibilität vorhanden)", "String", "NULL", null, false)
		 .add("gueltigVon", "Gibt die Gültigkeit ab welchem Schuljahr an", "String", "h.gueltigVon", null, false)
		 .add("gueltigBis", "Gibt die Gültigkeit bis zu welchem Schuljahr an", "String", "h.gueltigBis", null, false);
		addView(view);
	}

	private void add_Statkue_ZulJahrgaenge() {
		View view = new View(
				"Statkue_ZulJahrgaenge", "views.statkue", "DTOStatkueZulaessigerJahrgang", 
				"View zur Simulation einer Statkue-Tabelle: Zulässige Jahrgänge",
				0, null,
                """
                Jahrgaenge jg JOIN Jahrgaenge_Bezeichnungen jgb ON jg.ID = Jahrgang_ID WHERE jg.gueltigBis IS NULL
                """
		).add("ID", "Eine eindeutige ID des Jahrgangs", "Long", "jg.ID", null, true)
		 .add("Schulform", "Die Schulform, für welche der Jahrgang definiert ist", "String", "jgb.Schulform_Kuerzel", null, false)
		 .add("SNR", "Die Schulnummer, für welche der Jahrgang definiert wurde, falls die Definition auf eine schule eingeschränkt ist.", "String", "CASE WHEN jgb.Schulform_Kuerzel = 'HI' THEN 164264 ELSE NULL END", null, false)
		 .add("FSP", "Der Förderschwerpunkt, fallse die Jahrgangsdefintion nur auf einen FSP eingeschränkt ist", "String", "NULL", null, false)
		 .add("Jahrgang", "Das zweistellige Kürzel des Jahrgangs", "String", "jg.Kuerzel", null, false)
		 .add("KZ_Bereich", "Statkue - Feld KZ_Bereich", "Integer", "0", null, false)
		 .add("Beschreibung", "die textuelle Beschreibung des Jahrgangs", "String", "jgb.Bezeichnung", null, false)
		 .add("geaendert", "Datum der letzten Änderung (hier zur Kompatibilität vorhanden)", "String", "NULL", null, false)
		 .add("gueltigVon", "Gibt die Gültigkeit ab welchem Schuljahr an", "String", "jg.gueltigVon", null, false)
		 .add("gueltigBis", "Gibt die Gültigkeit bis zu welchem Schuljahr an", "String", "jg.gueltigBis", null, false);
		addView(view);		
	}


	private void add_Statkue_ZulKuArt() {
		View view = new View(
				"Statkue_ZulKuArt", "views.statkue", "DTOStatkueZulaessigeKursart", 
				"View zur Simulation einer Statkue-Tabelle: Zulässige Kursarten",
				0, null,
                """
                KursartenKatalog k JOIN KursartenKatalog_Schulformen ks ON k.ID = ks.Kursart_ID AND k.gueltigBis IS NULL
                """
		).add("SF", "Die Schulform bei der die Kursart zulässig ist.", "String", "ks.Schulform_Kuerzel", null, true)
		 .add("FSP", "Eine Einschränkung der Zulässigkeit der Kusart auf einen Förderschwerpunkt (hier nur Kompatibilität angegeben)", "String", "'**'", null, true)
		 .add("BG", "Eine Einschränkung der Zulässigkeit der Kursart auf einen Bildungsgang", "String", "CASE WHEN ks.Schulgliederung_Kuerzel = '' THEN '**' ELSE ks.Schulgliederung_Kuerzel END", null, true)
		 .add("Kursart", "Der numerische Schlüssel für die amtliche Schulstatistik", "String", "k.Nummer", null, true)
		 .add("Kursart2", "Das Kürzel der Kursart", "String", "k.Kuerzel", null, true)
		 .add("Bezeichnung", "Die Bezeichnung der Kursart", "String", "k.Bezeichnung", null, true)		 
		 .add("SGLBereich", "Zur Kompatibilität vorhanden", "Integer", "0", null, true)
		 .add("Flag", "Flag - zur Kompatibilität (hier 1)", "String", "'1'", null, true)
		 .add("geaendert", "Datum der letzten Änderung (hier zur Kompatibilität vorhanden)", "String", "NULL", null, false)
		 .add("gueltigVon", "Gibt die Gültigkeit ab welchem Schuljahr an", "String", "k.gueltigVon", null, false)
		 .add("gueltigBis", "Gibt die Gültigkeit bis zu welchem Schuljahr an", "String", "k.gueltigBis", null, false);
		addView(view);
	}


	private void add_Statkue_ZulFaecher() {
		View view = new View(
				"Statkue_ZulFaecher", "views.statkue", "DTOStatkueZulaessigesFach", 
				"View zur Simulation einer Statkue-Tabelle: Zulässige Fächer",
				0, null,
                """
                FachKatalog f JOIN FachKatalog_Schulformen fs ON f.ID = fs.Fach_ID AND f.gueltigBis IS NULL
                """
		).add("Schulform", "Die Schulform bei der das Fach zulässig ist.", "String", "fs.Schulform_Kuerzel", null, true)
		 .add("FSP", "Eine Einschränkung der Zulässigkeit des Faches auf einen Förderschwerpunkt (hier nur Kompatibilität angegeben)", "String", "NULL", null, false)
		 .add("BG", "Eine Einschränkung der Zulässigkeit des Faches auf einen Bildungsgang", "String", "CASE WHEN fs.Schulgliederung_Kuerzel = '' THEN '***' ELSE fs.Schulgliederung_Kuerzel END", null, true)
		 .add("Fach", "Das Kürzel für die amtliche Schulstatistik", "String", "f.KuerzelASD", null, true)
		 .add("Bezeichnung", "Die Bezeichnung des Faches", "String", "f.Bezeichnung", null, true)		 
		 .add("KZ_Bereich", "Zur Kompatibilität vorhanden", "Integer", "0", null, false)
		 .add("Flag", "Flag - zur Kompatibilität (hier 1)", "String", "'1'", null, true)
		 .add("Sortierung", "Zur Kompatibilität vorhanden", "Integer", "1", null, false)
		 .add("Sortierung2", "Zur Kompatibilität vorhanden", "Integer", "1", null, false)
		 .add("geaendert", "Datum der letzten Änderung (hier zur Kompatibilität vorhanden)", "String", "NULL", null, false)
		 .add("gueltigVon", "Gibt die Gültigkeit ab welchem Schuljahr an", "String", "f.gueltigVon", null, false)
		 .add("gueltigBis", "Gibt die Gültigkeit bis zu welchem Schuljahr an", "String", "f.gueltigBis", null, false);
		addView(view);
	}


	private void add_Statkue_SVWS_ZulaessigeFaecher() {
		View view = new View(
				"Statkue_SVWS_ZulaessigeFaecher", "views.statkue", "DTOSVWSZulaessigeFaecherMapping", 
				"View zur Simulation einer Statkue-Tabelle: SVWS Zulässige Fächer",
				0, null,
                """
                FachKatalog f 
                JOIN FachKatalog_Schulformen fs ON f.ID = fs.Fach_ID AND f.gueltigBis IS NULL
                LEFT JOIN Fachgruppen fg ON f.Fachgruppe = fg.FG_Kuerzel
                GROUP BY f.id
                """
		).add("Fach", "Fachkürzel aus der Tabelle StatkueZulaessigeFaecher von ITNRW.", "String", "f.KuerzelASD", null, true)
		 .add("Bezeichnung", "Die Bezeichnung des Faches", "String", "f.Bezeichnung", null, false)		 
		 .add("FachkuerzelAtomar", "atomarisiertes Fachkürzel", "String", "f.Kuerzel", null, false)
		 .add("Kurzbezeichnung", "kürzere Bezeichnung falls gewünscht", "String", "f.Bezeichnung", null, false)		 
		 .add("Aufgabenfeld", "das Aufgabenfeld, welchem das Fach zugeordnet ist (1, 2 oder 3)", "Integer", "f.Aufgabenfeld", null, false)
		 .add("Fachgruppe_ID", "Die ID der zugeordneten Fachgruppe", "Integer", "fg.ID", null, false)
		 .add("SchulformenUndGliederungen", "Gibt die Schulformen an in denen das Fach erlaubt ist (R,H,2020) gibt an dass ein Fach an R nur in H ab 2020 erlaubt ist", "String", "group_concat(fs.Schulform_Kuerzel)", null, false)		 
		 .add("SchulformenAusgelaufen", "zur Kompatibilität", "String", "NULL", null, false)
		 .add("AusgelaufenInSchuljahr", "zur Kompatibilität", "String", "NULL", null, false)
		 .add("AbJahrgang", "Jahrgang ab dem das Fach unterrichtet werden darf", "String", "f.JahrgangAb", null, false)		
		 .add("IstFremdsprache", "Boolscher Wert ob es eine Fremdsprache ist", "Boolean", "f.IstFremdsprache", "Boolean01Converter", false)
		 .add("IstHKFS", "Boolscher Wert ob es eine HerkunftsFremdsprache ist", "Boolean", "f.IstHKFS", "Boolean01Converter", false)
		 .add("IstAusRegUFach", "Boolscher Wert ob es eine reguläres Unterrichtsfach ist", "Boolean", "f.IstAusRegUFach", "Boolean01Converter", false)
		 .add("IstErsatzPflichtFS", "Boolscher Wert ob es eine Ersatzpflichfremdsprache ist", "Boolean", "f.IstErsatzPflichtFS", "Boolean01Converter", false)
		 .add("IstKonfKoop", "Boolscher Wert ob es eine eine Religionslehre im KOOP ist", "Boolean", "f.IstKonfKoop", "Boolean01Converter", false)
		 .add("NurSII", "Boolscher Wert ob das Fach nur in der SII unterrichtet werden darf", "Boolean", "f.NurSII", "Boolean01Converter", false)
		 .add("ExportASD", "Boolscher Wert ob das Fach für den ASDExport vorgesehen ist", "Boolean", "f.ExportASD", "Boolean01Converter", false)
		 .add("gueltigVon", "Gibt die Gültigkeit ab welchem Schuljahr an", "String", "f.gueltigVon", null, false)
		 .add("gueltigBis", "Gibt die Gültigkeit bis zu welchem Schuljahr an", "String", "f.gueltigBis", null, false);
		addView(view);
	}


	private void add_Statkue_AllgMerkmale() {
		View view = new View(
				"Statkue_AllgMerkmale", "views.statkue", "DTOStatkueAllgMerkmale", 
				"View zur Simulation einer Statkue-Tabelle: Allgemeine Merkmale bei Schulen und Schülern",
				0, null,
                """
                AllgemeineMerkmaleKatalog am 
                JOIN AllgemeineMerkmaleKatalog_Schulformen as2 ON am.ID = as2.Merkmal_ID AND am.GueltigBis IS NULL
                JOIN Schulformen s ON as2.Schulform_Kuerzel = s.Kuerzel AND s.gueltigBis IS NULL
                ORDER BY am.ID, s.Kuerzel, s.Nummer 
                """
		).add("ID", "Die ID des Merkmals.", "String", "CAST(s.Nummer AS INTEGER) + am.ID * 100", null, true)
		 .add("SF", "Die Schulform für welche das Merkmal zur Verfügung steht", "String", "as2.Schulform_Kuerzel", null, false)
		 .add("Kurztext", "Das Kürzel des Merkmals", "String", "am.Kuerzel", null, false)
		 .add("StatistikKuerzel", "Ggf. das Statistikkürzel des Merkmals", "String", "am.KuerzelASD", null, false)
		 .add("Langtext", "Die Beschreibung des Merkmals", "String", "am.Bezeichnung", null, false)
		 .add("Schule", "Gibt an, ob das Merkmal bei einer Schule gesetzt werden kann", "Integer", "CASE WHEN am.beiSchule THEN 1 ELSE 0 END", null, false)
		 .add("Schueler", "Gibt an, ob das Merkmal bei einem Schüler gesetzt werden kann", "Integer", "CASE WHEN am.beiSchueler THEN 1 ELSE 0 END", null, false)
		 .add("Beginn", "Zur Kompatibilität vorhanden", "String", "NULL", null, false)
		 .add("Ende", "Zur Kompatibilität vorhanden", "String", "NULL", null, false)
		 .add("Sort", "Zur Kompatibilität vorhanden", "Integer", "am.ID", null, false)
		 .add("gueltigVon", "Gibt die Gültigkeit ab welchem Schuljahr an", "String", "am.gueltigVon", null, false)
		 .add("gueltigBis", "Gibt die Gültigkeit bis zu welchem Schuljahr an", "String", "am.gueltigBis", null, false);
		addView(view);
	}


	private void add_V_Benutzer() {
		View view = new View(
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
		      SELECT Benutzer.ID, CASE WHEN max(Benutzer.IstAdmin) = 1 OR max(Benutzergruppen.IstAdmin) = 1 THEN 1 ELSE 0 END AS IstAdmin FROM Benutzer LEFT JOIN BenutzergruppenMitglieder ON Benutzer.ID = BenutzergruppenMitglieder.Benutzer_ID JOIN Benutzergruppen ON Benutzergruppen.ID = BenutzergruppenMitglieder.Gruppe_ID
		      GROUP BY Benutzer.ID
		    ) admins ON creds.ID = admins.ID
			"""
		).add("ID", "Die eindeutige ID des Benutzers", "Long", "creds.ID", null, true)
		 .add("AnzeigeName", "Der Anzeige-Name des Benutzers (z.B. Max Mustermann)", "String", "creds.AnzeigeName", null, false)
		 .add("Benutzername", "Der Anmeldename des Benutzers (z.B. max.mustermann)", "String", "creds.Benutzername", null, false)
		 .add("PasswordHash", "Der bcrypt-Password-Hash zur Überprüfung des Benutzer-Kennwortes", "String", "creds.PasswordHash", null, false)
		 .add("IstAdmin", "Gibt an, ob es sich um einen administrativen Benutzer handelt oder nicht", "Boolean", "admins.IstAdmin", "Boolean01Converter", false);
		addView(view);
	}
	
	private void add_V_Benutzerkompetenzen() {
		View view = new View(
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
		View view = new View(
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
                  SELECT Benutzer.ID, CASE WHEN max(Benutzer.IstAdmin) = 1 OR max(Benutzergruppen.IstAdmin) = 1 THEN 1 ELSE 0 END AS IstAdmin FROM Benutzer LEFT JOIN BenutzergruppenMitglieder ON Benutzer.ID = BenutzergruppenMitglieder.Benutzer_ID JOIN Benutzergruppen ON Benutzergruppen.ID = BenutzergruppenMitglieder.Gruppe_ID
                  GROUP BY Benutzer.ID
                ) admins ON creds.ID = admins.ID
                """
		).add("ID", "Die eindeutige ID des Benutzers", "Long", "creds.ID", null, true)
		 .add("credentialID", "Die ID der Benutzer-Credentials", "Long", "creds.credentialID", null, false)
		 .add("Typ", "Der Typ des Benutzers", "BenutzerTyp", "creds.Typ", "BenutzerTypConverter", false)
		 .add("TypID", "Die ID des Benutzers in der Benutzer-Typ-spezifischen Tabelle (z.B. eine Schüler-ID)", "Long", "creds.TypID", null, false)
		 .add("AnzeigeName", "Der Anzeige-Name des Benutzers (z.B. Max Mustermann)", "String", "creds.AnzeigeName", null, false)
		 .add("Benutzername", "Der Anmeldename des Benutzers (z.B. max.mustermann)", "String", "creds.Benutzername", null, false)
		 .add("PasswordHash", "Der bcrypt-Password-Hash zur Überprüfung des Benutzer-Kennwortes", "String", "creds.PasswordHash", null, false)
		 .add("IstAdmin", "Gibt an, ob es sich um einen administrativen Benutzer handelt oder nicht", "Boolean", "admins.IstAdmin", "Boolean01Converter", false);
		addView(view);
	}
	
	private void add_V_Gost_Schueler_Abiturjahrgang() {
		View view = new View(
				"V_Gost_Schueler_Abiturjahrgang", "views.gost", "DTOViewGostSchuelerAbiturjahrgang",
				"Eine Schülerliste mit der Zuordnung Abiturjahrgängen für die gymnasiale Oberstufe. Die View sollte nur bei Schulformen mit gymnasialer Oberstufe genutzt werden",
				6, null,
                """
                Schueler 
                JOIN Schuljahresabschnitte ON Schueler.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID
                JOIN SchuelerLernabschnittsdaten ON Schueler.Schuljahresabschnitts_ID = SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID
                  AND Schueler.ID = SchuelerLernabschnittsdaten.Schueler_ID
                JOIN EigeneSchule_Jahrgaenge ON SchuelerLernabschnittsdaten.Jahrgang_ID = EigeneSchule_Jahrgaenge.ID
                JOIN EigeneSchule
                """
		).add("ID", "Die ID des Schülers", "Long", "Schueler.ID", null, true)
		 .add("Status", "Der Status des Schülers", "SchuelerStatus", "Schueler.Status", "SchuelerStatusConverter", false)
		 .add("Schulform", "Die Schulform des Schülers", "Schulform", "EigeneSchule.SchulformKrz", "SchulformKuerzelConverter", false)
		 .add("Schulgliederung", "Die Schulgliederung des Schülers", "Schulgliederung", "SchuelerLernabschnittsdaten.ASDSchulgliederung", "SchulgliederungKuerzelConverter", false)
		 .add("Abiturjahr", "Das Abiturjahr des Schülers", "Long", "(EigeneSchule_Jahrgaenge.Restabschnitte / EigeneSchule.AnzahlAbschnitte) + Schuljahresabschnitte.Jahr", null, false);
		addView(view);
	}

}
