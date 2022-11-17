package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle K_AllgAdresse.
 */
public class Tabelle_K_AllgAdresse extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der weiteren Adresse (Betriebe)");

	/** Die Definition der Tabellenspalte AdressArt_ID */
	public SchemaTabelleSpalte col_AdressArt_ID = add("AdressArt_ID", SchemaDatentypen.BIGINT, false)
		.setRevision(SchemaRevisionen.REV_1)
		.setJavaName("adressArt")
		.setJavaComment("Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart");

	/** Die Definition der Tabellenspalte AllgAdrAdressArt */
	public SchemaTabelleSpalte col_AllgAdrAdressArt = add("AllgAdrAdressArt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED Die Bezeichnung der Adressart des Betriebs");

	/** Die Definition der Tabellenspalte AllgAdrName1 */
	public SchemaTabelleSpalte col_AllgAdrName1 = add("AllgAdrName1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaName("name1")
		.setJavaComment("Name1 des Betriebs");

	/** Die Definition der Tabellenspalte AllgAdrName2 */
	public SchemaTabelleSpalte col_AllgAdrName2 = add("AllgAdrName2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaName("name2")
		.setJavaComment("Name2 des Betriebs");

	/** Die Definition der Tabellenspalte AllgAdrStrasse */
	public SchemaTabelleSpalte col_AllgAdrStrasse = add("AllgAdrStrasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaName("strasse")
		.setJavaComment("Straße des Betriebs");

	/** Die Definition der Tabellenspalte AllgAdrStrassenname */
	public SchemaTabelleSpalte col_AllgAdrStrassenname = add("AllgAdrStrassenname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setJavaName("strassenname")
		.setJavaComment("Straßenname des Betriebsdatensatz");

	/** Die Definition der Tabellenspalte AllgAdrHausNr */
	public SchemaTabelleSpalte col_AllgAdrHausNr = add("AllgAdrHausNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaName("hausnr")
		.setJavaComment("Hausnummer wenn getrennt gespeichert");

	/** Die Definition der Tabellenspalte AllgAdrHausNrZusatz */
	public SchemaTabelleSpalte col_AllgAdrHausNrZusatz = add("AllgAdrHausNrZusatz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaName("hausnrzusatz")
		.setJavaComment("Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden");

	/** Die Definition der Tabellenspalte AllgAdrOrt_ID */
	public SchemaTabelleSpalte col_AllgAdrOrt_ID = add("AllgAdrOrt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaName("ort_id")
		.setJavaComment("OrtID des Betriebs");

	/** Die Definition der Tabellenspalte AllgAdrPLZ */
	public SchemaTabelleSpalte col_AllgAdrPLZ = add("AllgAdrPLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_3)
		.setJavaName("plz")
		.setJavaComment("DEPRECATED: PLZ des Betriebs");

	/** Die Definition der Tabellenspalte AllgAdrTelefon1 */
	public SchemaTabelleSpalte col_AllgAdrTelefon1 = add("AllgAdrTelefon1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaName("telefon1")
		.setJavaComment("Telefonnummer1 des Betriebs");

	/** Die Definition der Tabellenspalte AllgAdrTelefon2 */
	public SchemaTabelleSpalte col_AllgAdrTelefon2 = add("AllgAdrTelefon2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaName("telefon2")
		.setJavaComment("Telefonnummer2 des Betriebs");

	/** Die Definition der Tabellenspalte AllgAdrFax */
	public SchemaTabelleSpalte col_AllgAdrFax = add("AllgAdrFax", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaName("fax")
		.setJavaComment("Faxnummer des Betriebs");

	/** Die Definition der Tabellenspalte AllgAdrEmail */
	public SchemaTabelleSpalte col_AllgAdrEmail = add("AllgAdrEmail", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaName("email")
		.setJavaComment("E-MailAdresse des Betriebes");

	/** Die Definition der Tabellenspalte AllgAdrBemerkungen */
	public SchemaTabelleSpalte col_AllgAdrBemerkungen = add("AllgAdrBemerkungen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaName("bemerkungen")
		.setJavaComment("Bemerkung zum Betrieb");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaName("sortierung")
		.setJavaComment("Sortierung des Betriebsdatensatz");

	/** Die Definition der Tabellenspalte AllgAdrAusbildungsBetrieb */
	public SchemaTabelleSpalte col_AllgAdrAusbildungsBetrieb = add("AllgAdrAusbildungsBetrieb", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("ausbildungsbetrieb")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob der Betrieb ausbildet Ja Nein");

	/** Die Definition der Tabellenspalte AllgAdrBietetPraktika */
	public SchemaTabelleSpalte col_AllgAdrBietetPraktika = add("AllgAdrBietetPraktika", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setJavaName("bietetPraktika")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob der Betrieb Praktikumsplätze bietet Ja Nein");

	/** Die Definition der Tabellenspalte AllgAdrBranche */
	public SchemaTabelleSpalte col_AllgAdrBranche = add("AllgAdrBranche", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaName("branche")
		.setJavaComment("Brache des Betriebs");

	/** Die Definition der Tabellenspalte AllgAdrZusatz1 */
	public SchemaTabelleSpalte col_AllgAdrZusatz1 = add("AllgAdrZusatz1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaName("zusatz1")
		.setJavaComment("Adresszusatz zum Betrieb");

	/** Die Definition der Tabellenspalte AllgAdrZusatz2 */
	public SchemaTabelleSpalte col_AllgAdrZusatz2 = add("AllgAdrZusatz2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaName("zusatz2")
		.setJavaComment("Adresszusatz2 zum Betrieb");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Sichtbarkeit des Datensatzes");

	/** Die Definition der Tabellenspalte Aenderbar */
	public SchemaTabelleSpalte col_Aenderbar = add("Aenderbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Datensatz ist änderbar Ja Nein");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Massnahmentraeger */
	public SchemaTabelleSpalte col_Massnahmentraeger = add("Massnahmentraeger", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Bezeichnung des Maßnahmenträgers");

	/** Die Definition der Tabellenspalte BelehrungISG */
	public SchemaTabelleSpalte col_BelehrungISG = add("BelehrungISG", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Belehrung nach Infektionsschutzgesetz notwendig Ja Nein");

	/** Die Definition der Tabellenspalte GU_ID */
	public SchemaTabelleSpalte col_GU_ID = add("GU_ID", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("GU_ID des Betriebsdatensatzes (für Import zur Erkennung)");

	/** Die Definition der Tabellenspalte ErwFuehrungszeugnis */
	public SchemaTabelleSpalte col_ErwFuehrungszeugnis = add("ErwFuehrungszeugnis", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt?");

	/** Die Definition der Tabellenspalte ExtID */
	public SchemaTabelleSpalte col_ExtID = add("ExtID", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Externe ID des Betriebsdatensatzes");


	/** Die Definition des Fremdschlüssels K_AllgAdresse_K_Adressart_FK */
	public SchemaTabelleFremdschluessel fk_K_AllgAdresse_K_Adressart_FK = addForeignKey(
			"K_AllgAdresse_K_Adressart_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_AdressArt_ID, Schema.tab_K_Adressart.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels K_AllgAdresse_Ort_FK */
	public SchemaTabelleFremdschluessel fk_K_AllgAdresse_Ort_FK = addForeignKey(
			"K_AllgAdresse_Ort_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_AllgAdrOrt_ID, Schema.tab_K_Ort.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle K_AllgAdresse.
	 */
	public Tabelle_K_AllgAdresse() {
		super("K_AllgAdresse", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOKatalogAllgemeineAdresse");
		setJavaComment("Katalog der weiteren Adressen und Betriebe");
	}

}
