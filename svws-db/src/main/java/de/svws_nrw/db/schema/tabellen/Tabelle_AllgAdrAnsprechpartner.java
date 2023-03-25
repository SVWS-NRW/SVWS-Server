package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle AllgAdrAnsprechpartner.
 */
public class Tabelle_AllgAdrAnsprechpartner extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Ansprechpartners der Tabelle AllgAdresse (Betriebe)");

	/** Die Definition der Tabellenspalte Adresse_ID */
	public SchemaTabelleSpalte col_Adresse_ID = add("Adresse_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Betriebs (der Adresse) aus der Tabelle AllgAdresse");

	/** Die Definition der Tabellenspalte Name */
	public SchemaTabelleSpalte col_Name = add("Name", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setJavaComment("Name des Ansprechpartners im Betrieb PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte Vorname */
	public SchemaTabelleSpalte col_Vorname = add("Vorname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setJavaComment("Vorname des Ansprechpartners im Betrieb PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte Anrede */
	public SchemaTabelleSpalte col_Anrede = add("Anrede", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Anrede des Ansprechpartners im Betrieb");

	/** Die Definition der Tabellenspalte Telefon */
	public SchemaTabelleSpalte col_Telefon = add("Telefon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Telefonnummer des Ansprechpartners im Betrieb");

	/** Die Definition der Tabellenspalte Email */
	public SchemaTabelleSpalte col_Email = add("Email", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Email-Adresse des Ansprechpartners im Betrieb");

	/** Die Definition der Tabellenspalte Abteilung */
	public SchemaTabelleSpalte col_Abteilung = add("Abteilung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("ggf Abteilung des Ansprechpartners im Betrieb");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Titel */
	public SchemaTabelleSpalte col_Titel = add("Titel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Titel des Ansprechpartners im Betrieb");

	/** Die Definition der Tabellenspalte GU_ID */
	public SchemaTabelleSpalte col_GU_ID = add("GU_ID", SchemaDatentypen.VARCHAR, false).setDatenlaenge(40)
		.setJavaComment("GU_ID des Ansprechpartners im Betrieb");


	/** Die Definition des Fremdschlüssels AllgAdrAnsprechpartner_Adr_FK */
	public SchemaTabelleFremdschluessel fk_AllgAdrAnsprechpartner_Adr_FK = addForeignKey(
			"AllgAdrAnsprechpartner_Adr_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Adresse_ID, Schema.tab_K_AllgAdresse.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle AllgAdrAnsprechpartner.
	 */
	public Tabelle_AllgAdrAnsprechpartner() {
		super("AllgAdrAnsprechpartner", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.katalog");
		setJavaClassName("DTOAnsprechpartnerAllgemeineAdresse");
		setJavaComment("Ansprechpartner-Daten die einem Betrieb/Adresse in K_AllgAdresse zugeordnet werden können");
	}

}
