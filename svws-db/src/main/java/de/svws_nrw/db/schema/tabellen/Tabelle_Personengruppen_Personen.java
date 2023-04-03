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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Personengruppen_Personen.
 */
public class Tabelle_Personengruppen_Personen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Gruppe_ID */
	public SchemaTabelleSpalte col_Gruppe_ID = add("Gruppe_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("GruppenID des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte Person_ID */
	public SchemaTabelleSpalte col_Person_ID = add("Person_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("PersonenID des Personeneintrags zur Personengruppe wenn in DB vorhandne");

	/** Die Definition der Tabellenspalte PersonNr */
	public SchemaTabelleSpalte col_PersonNr = add("PersonNr", SchemaDatentypen.INT, false)
		.setJavaComment("Personennummer des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonArt */
	public SchemaTabelleSpalte col_PersonArt = add("PersonArt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("PersonenArt des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonName */
	public SchemaTabelleSpalte col_PersonName = add("PersonName", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setNotNull()
		.setJavaComment("Name des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonVorname */
	public SchemaTabelleSpalte col_PersonVorname = add("PersonVorname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setJavaComment("Vorname des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonPLZ */
	public SchemaTabelleSpalte col_PersonPLZ = add("PersonPLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("PLZ des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonOrt */
	public SchemaTabelleSpalte col_PersonOrt = add("PersonOrt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Ort des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonStrasse */
	public SchemaTabelleSpalte col_PersonStrasse = add("PersonStrasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Straße des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonStrassenname */
	public SchemaTabelleSpalte col_PersonStrassenname = add("PersonStrassenname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setJavaComment("Straßenname zur Person der Personengruppe");

	/** Die Definition der Tabellenspalte PersonHausNr */
	public SchemaTabelleSpalte col_PersonHausNr = add("PersonHausNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Hausnummer wenn getrennt gespeichert");

	/** Die Definition der Tabellenspalte PersonHausNrZusatz */
	public SchemaTabelleSpalte col_PersonHausNrZusatz = add("PersonHausNrZusatz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden");

	/** Die Definition der Tabellenspalte PersonTelefon */
	public SchemaTabelleSpalte col_PersonTelefon = add("PersonTelefon", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Telfonnummer des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonMobil */
	public SchemaTabelleSpalte col_PersonMobil = add("PersonMobil", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Mobilnummer des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonEmail */
	public SchemaTabelleSpalte col_PersonEmail = add("PersonEmail", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Email  des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte Bemerkung */
	public SchemaTabelleSpalte col_Bemerkung = add("Bemerkung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Bemerkung des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte Zusatzinfo */
	public SchemaTabelleSpalte col_Zusatzinfo = add("Zusatzinfo", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Zusatzinfo des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Sortierung des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonAnrede */
	public SchemaTabelleSpalte col_PersonAnrede = add("PersonAnrede", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Anrede des Personeneintrags zur Personengruppe");

	/** Die Definition der Tabellenspalte PersonAkadGrad */
	public SchemaTabelleSpalte col_PersonAkadGrad = add("PersonAkadGrad", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("DEPRECATED: Titel des Personeneintrags zur Personengruppe");


	/** Die Definition des Fremdschlüssels Personengruppen_Personen_Gruppe */
	public SchemaTabelleFremdschluessel fk_Personengruppen_Personen_Gruppe = addForeignKey(
			"Personengruppen_Personen_Gruppe",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Gruppe_ID, Schema.tab_Personengruppen.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Personengruppen_Personen.
	 */
	public Tabelle_Personengruppen_Personen() {
		super("Personengruppen_Personen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.personengruppen");
		setJavaClassName("DTOPersonengruppenPersonen");
		setJavaComment("Zuordnung von Personen zur den Einträgen in der Tabelle Personengruppen");
	}

}
