package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.GeschlechtConverterFromString;
import de.svws_nrw.db.converter.current.NationalitaetenConverter;
import de.svws_nrw.db.converter.current.PersonalTypConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_Lehrer.
 */
public class Tabelle_K_Lehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eindeutige ID zur Kennzeichnung des Lehrer-Datensatzes");

	/** Die Definition der Tabellenspalte GU_ID */
	public SchemaTabelleSpalte col_GU_ID = add("GU_ID", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("Eindeutige ID Datenbank übergreifend. Wurde früher mal für Logineo genutzt, kann später mal zur Identifizierung genutzt werden.");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Lehrer-Kürzel für eine lesbare eindeutige Identifikation des Lehrers");

	/** Die Definition der Tabellenspalte LIDKrz */
	public SchemaTabelleSpalte col_LIDKrz = add("LIDKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(4)
		.setJavaName("kuerzelLID")
		.setJavaComment("Lehrer-Kürzel für eine eindeutige Identifikation des Lehrers – Verwendung für die Statistik - TODO lassen sich kuerzel und LIDKrz nicht sinnvoll zusammenfassen?");

	/** Die Definition der Tabellenspalte Nachname */
	public SchemaTabelleSpalte col_Nachname = add("Nachname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setNotNull()
		.setJavaComment("Der Nachname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte Vorname */
	public SchemaTabelleSpalte col_Vorname = add("Vorname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setJavaComment("Der Vorname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2. Wird im Client mit Rufname angezeigt.");

	/** Die Definition der Tabellenspalte PersonTyp */
	public SchemaTabelleSpalte col_PersonTyp = add("PersonTyp", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setDefault("LEHRKRAFT")
		.setConverter(PersonalTypConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Die Art der Person – wurde nachträglich hinzugefügt, damit auch Nicht-Lehrer in die Liste aufgenommen und unterschieden werden können");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Eine Nummer, die zur Sortierung der Lehrer-Datensätze verwendet werden kann.");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob der Lehrer-Datensatz in der Oberfläche sichtbar sein soll und bei einer Auswahl zur Verfügung steht. ");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob Änderungen am Lehrer-Datensatz erlaubt sind.");

	/** Die Definition der Tabellenspalte FuerExport */
	public SchemaTabelleSpalte col_FuerExport = add("FuerExport", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob der Lehrer-Datensatz für den Export in andere Software verwendet werden soll - TODO fuer welche(n) Zweck(e) wird dies gespeichert - gehört dies an diese Stelle? ");

	/** Die Definition der Tabellenspalte Statistik */
	public SchemaTabelleSpalte col_Statistik = add("Statistik", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setJavaName("statistikRelevant")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an, ob der Lehrer-Datensatz bei der Statistik berücksichtigt werden soll.");

	/** Die Definition der Tabellenspalte Strasse */
	public SchemaTabelleSpalte col_Strasse = add("Strasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Adressdaten des Lehrers: Strasse");

	/** Die Definition der Tabellenspalte Strassenname */
	public SchemaTabelleSpalte col_Strassenname = add("Strassenname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setJavaComment("Straßenname der Lehrkraft");

	/** Die Definition der Tabellenspalte HausNr */
	public SchemaTabelleSpalte col_HausNr = add("HausNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Hausnummer wenn getrennt gespeichert");

	/** Die Definition der Tabellenspalte HausNrZusatz */
	public SchemaTabelleSpalte col_HausNrZusatz = add("HausNrZusatz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden");

	/** Die Definition der Tabellenspalte Ort_ID */
	public SchemaTabelleSpalte col_Ort_ID = add("Ort_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Adressdaten des Lehrers: Fremdschlüssel auf die Katalog-Tabelle mit den Orten");

	/** Die Definition der Tabellenspalte PLZ */
	public SchemaTabelleSpalte col_PLZ = add("PLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_3)
		.setJavaComment("DEPRECATED: Adressdaten des Lehrers: Postleitzahl - Ort als Spalte in der Tabelle ergänzen, ähnlich wie bei Schüler, wichtig da ansonsten Probleme auftauchen, wenn ein PLZ ? Ortsname nicht eindeutig ist, was gelegentlich vorkommt");

	/** Die Definition der Tabellenspalte Ortsteil_ID */
	public SchemaTabelleSpalte col_Ortsteil_ID = add("Ortsteil_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Adressdaten des Lehrers: Fremdschlüssel auf die Katalog-Tabelle mit den Ortsteilen");

	/** Die Definition der Tabellenspalte Tel */
	public SchemaTabelleSpalte col_Tel = add("Tel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaName("telefon")
		.setJavaComment("Adressdaten des Lehrers: Telefonnummer");

	/** Die Definition der Tabellenspalte Handy */
	public SchemaTabelleSpalte col_Handy = add("Handy", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaName("telefonMobil")
		.setJavaComment("Adressdaten des Lehrers: Mobilnummer oder Faxnummer");

	/** Die Definition der Tabellenspalte Email */
	public SchemaTabelleSpalte col_Email = add("Email", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaName("eMailPrivat")
		.setJavaComment("Adressdaten des Lehrers: Private Email-Adresse");

	/** Die Definition der Tabellenspalte EmailDienstlich */
	public SchemaTabelleSpalte col_EmailDienstlich = add("EmailDienstlich", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaName("eMailDienstlich")
		.setJavaComment("Adressdaten des Lehrers: Dienstliche Email-Adresse");

	/** Die Definition der Tabellenspalte StaatKrz */
	public SchemaTabelleSpalte col_StaatKrz = add("StaatKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaName("staatsangehoerigkeit")
		.setConverter(NationalitaetenConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Die erste Staatsangehörigkeit des Lehrers");

	/** Die Definition der Tabellenspalte Geburtsdatum */
	public SchemaTabelleSpalte col_Geburtsdatum = add("Geburtsdatum", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Das Geburtsdatum des Lehrers");

	/** Die Definition der Tabellenspalte Geschlecht */
	public SchemaTabelleSpalte col_Geschlecht = add("Geschlecht", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setConverter(GeschlechtConverterFromString.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Das Geschlecht des Lehrers - TODO ist in der Datenbank als String und nicht als Integer (3/4) hinterlegt, dies sollte in allen Tabellen einheitlich sein");

	/** Die Definition der Tabellenspalte Anrede */
	public SchemaTabelleSpalte col_Anrede = add("Anrede", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Die Anrede für den Lehrer");

	/** Die Definition der Tabellenspalte Amtsbezeichnung */
	public SchemaTabelleSpalte col_Amtsbezeichnung = add("Amtsbezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Die Amtsbezeichnung des Lehrers");

	/** Die Definition der Tabellenspalte Titel */
	public SchemaTabelleSpalte col_Titel = add("Titel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Ggf. der Titel des Lehrers");

	/** Die Definition der Tabellenspalte Faecher */
	public SchemaTabelleSpalte col_Faecher = add("Faecher", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Die Fächer, die der Lehrer unterrichtet - TODO hat dieses Feld noch einen Zweck? Fächer sind dem Lehrer eigentlich anders zugeordnet...  ");

	/** Die Definition der Tabellenspalte IdentNr1 */
	public SchemaTabelleSpalte col_IdentNr1 = add("IdentNr1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaName("identNrTeil1")
		.setJavaComment("Der vordere Teil der NRW-weit eindeutigen Ident-Nummer - setzt sich normalerweise aus Geburtsdatum und Geschlecht (3/4) zusammen, kann in Einzelfällen aber von diesem Schema abweichen");

	/** Die Definition der Tabellenspalte SerNr */
	public SchemaTabelleSpalte col_SerNr = add("SerNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(5)
		.setJavaName("identNrTeil2SerNr")
		.setJavaComment("Der hintere Teil der Ident-Nummer – wird üblicherweise NRW-weit fortlaufend vergeben");

	/** Die Definition der Tabellenspalte PANr */
	public SchemaTabelleSpalte col_PANr = add("PANr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Personalakten-Nummer (wird von den Bezirksregierungen ggf genutzt)");

	/** Die Definition der Tabellenspalte LBVNr */
	public SchemaTabelleSpalte col_LBVNr = add("LBVNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaName("personalNrLBV")
		.setJavaComment("Die Personalnummer beim Landesamt für Besoldung und Versorgung (LBV)");

	/** Die Definition der Tabellenspalte VSchluessel */
	public SchemaTabelleSpalte col_VSchluessel = add("VSchluessel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaName("verguetungsSchluessel")
		.setJavaComment("Der Vergütungsschlüssel");

	/** Die Definition der Tabellenspalte DatumZugang */
	public SchemaTabelleSpalte col_DatumZugang = add("DatumZugang", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Das Datum, wann der Lehrer an die Schule gekommen ist.");

	/** Die Definition der Tabellenspalte GrundZugang */
	public SchemaTabelleSpalte col_GrundZugang = add("GrundZugang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Der Grund für den Zugang des Lehrers");

	/** Die Definition der Tabellenspalte DatumAbgang */
	public SchemaTabelleSpalte col_DatumAbgang = add("DatumAbgang", SchemaDatentypen.DATE, false)
		.setConverter(DatumConverter.class)
		.setJavaComment("Das Datum, wann der Lehrer die Schule verlassen hat.");

	/** Die Definition der Tabellenspalte GrundAbgang */
	public SchemaTabelleSpalte col_GrundAbgang = add("GrundAbgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Der Grund für das Verlassen der Schule durch den Lehrer");

	/** Die Definition der Tabellenspalte KennwortTools */
	public SchemaTabelleSpalte col_KennwortTools = add("KennwortTools", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Wird für das GS-Modul genutzt (gehashed).");

	/** Die Definition der Tabellenspalte KennwortToolsAktuell */
	public SchemaTabelleSpalte col_KennwortToolsAktuell = add("KennwortToolsAktuell", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setDefault("-;5")
		.setJavaComment("Gibt an ob das LPassword geändert wurde oder ob es noch das Initialkennwort ist");

	/** Die Definition der Tabellenspalte LPassword */
	public SchemaTabelleSpalte col_LPassword = add("LPassword", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("DEPRECATED: Wird für das GS-Modul genutzt (gehashed).")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte PWAktuell */
	public SchemaTabelleSpalte col_PWAktuell = add("PWAktuell", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setDefault("-;5")
		.setJavaComment("DEPRECATED: Gibt an ob das LPassword geändert wurde oder ob es noch das Initialkennwort ist")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte PflichtstdSoll */
	public SchemaTabelleSpalte col_PflichtstdSoll = add("PflichtstdSoll", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Das Pflichtstundensoll des Lehrers")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte Rechtsverhaeltnis */
	public SchemaTabelleSpalte col_Rechtsverhaeltnis = add("Rechtsverhaeltnis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit)")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte Beschaeftigungsart */
	public SchemaTabelleSpalte col_Beschaeftigungsart = add("Beschaeftigungsart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.)")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** [ASD] Die Definition der Tabellenspalte Einsatzstatus */
	public SchemaTabelleSpalte col_Einsatzstatus = add("Einsatzstatus", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("[ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig)")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte StammschulNr */
	public SchemaTabelleSpalte col_StammschulNr = add("StammschulNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("Die Schulnummer der Stammschule, sofern diese abweicht")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte UnterrichtsStd */
	public SchemaTabelleSpalte col_UnterrichtsStd = add("UnterrichtsStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Berechnetes Feld: Die Anzahl der unterrichteten Stunden")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte MehrleistungStd */
	public SchemaTabelleSpalte col_MehrleistungStd = add("MehrleistungStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Berechnetes Feld: Die Stunden für eine Mehrleistung")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte EntlastungStd */
	public SchemaTabelleSpalte col_EntlastungStd = add("EntlastungStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Berechnetes Feld: Die Stunden für eine Entlastung")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte AnrechnungStd */
	public SchemaTabelleSpalte col_AnrechnungStd = add("AnrechnungStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Berechnetes Feld: Die angerechneten Stunden für eine nichtunterrichtliche Tätigkeit")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte RestStd */
	public SchemaTabelleSpalte col_RestStd = add("RestStd", SchemaDatentypen.FLOAT, false)
		.setJavaComment("Berechnetes Feld: Reststunden")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte SchILDweb_FL */
	public SchemaTabelleSpalte col_SchILDweb_FL = add("SchILDweb_FL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("DEPRECATED: nicht mehr genutzt SchildWeb")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte SchILDweb_KL */
	public SchemaTabelleSpalte col_SchILDweb_KL = add("SchILDweb_KL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("DEPRECATED: nicht mehr genutzt SchildWeb")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte SchILDweb_Config */
	public SchemaTabelleSpalte col_SchILDweb_Config = add("SchILDweb_Config", SchemaDatentypen.TEXT, false)
		.setJavaComment("DEPRECATED: nicht mehr genutzt SchildWeb")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte Antwort1 */
	public SchemaTabelleSpalte col_Antwort1 = add("Antwort1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("DEPRECATED: wurde nie verwendet")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte Antwort2 */
	public SchemaTabelleSpalte col_Antwort2 = add("Antwort2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("DEPRECATED: wurde nie verwendet")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte XNMPassword */
	public SchemaTabelleSpalte col_XNMPassword = add("XNMPassword", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Passwort für das XNM-Tool ")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte XNMPassword2 */
	public SchemaTabelleSpalte col_XNMPassword2 = add("XNMPassword2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("zweites Passwort für das XNM-Tool ")
		.setVeraltet(SchemaRevisionen.REV_13);

	/** Die Definition der Tabellenspalte CredentialID */
	public SchemaTabelleSpalte col_CredentialID = add("CredentialID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die ID des Credential-Eintrags");


	/** Die Definition des Fremdschlüssels K_Lehrer_Credentials_FK */
	public SchemaTabelleFremdschluessel fk_K_Lehrer_Credentials_FK = addForeignKey(
			"K_Lehrer_Credentials_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_CredentialID, Schema.tab_Credentials.col_ID)
		);

	/** Die Definition des Fremdschlüssels K_Lehrer_Ort_FK */
	public SchemaTabelleFremdschluessel fk_K_Lehrer_Ort_FK = addForeignKey(
			"K_Lehrer_Ort_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Ort_ID, Schema.tab_K_Ort.col_ID)
		);

	/** Die Definition des Fremdschlüssels K_Lehrer_Ortsteil_FK */
	public SchemaTabelleFremdschluessel fk_K_Lehrer_Ortsteil_FK = addForeignKey(
			"K_Lehrer_Ortsteil_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Ortsteil_ID, Schema.tab_K_Ortsteil.col_ID)
		);

	/** Die Definition des Fremdschlüssels K_Lehrer_PersonTyp_FK */
	public SchemaTabelleFremdschluessel fk_K_Lehrer_PersonTyp_FK = addForeignKey(
			"K_Lehrer_PersonTyp_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_PersonTyp, Schema.tab_PersonalTypen.col_Kuerzel)
		);

	/** Die Definition des Fremdschlüssels K_Lehrer_Statkue_Nationalitaeten_FK */
	public SchemaTabelleFremdschluessel fk_K_Lehrer_Statkue_Nationalitaeten_FK = addForeignKey(
			"K_Lehrer_Statkue_Nationalitaeten_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_StaatKrz, Schema.tab_Nationalitaeten_Keys.col_DEStatisCode)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/** Die Definition des Unique-Index K_Lehrer_UC1 */
	public SchemaTabelleUniqueIndex unique_K_Lehrer_UC1 = addUniqueIndex("K_Lehrer_UC1",
			col_Kuerzel
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_Lehrer.
	 */
	public Tabelle_K_Lehrer() {
		super("K_Lehrer", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrer");
		setJavaComment("Tabelle für die Datensätze der Lehrkräfte");
	}

}
