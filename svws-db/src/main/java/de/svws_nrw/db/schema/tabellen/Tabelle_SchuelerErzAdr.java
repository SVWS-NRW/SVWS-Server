package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.NationalitaetenConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerErzAdr.
 */
public class Tabelle_SchuelerErzAdr extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Erzieherdatensatzes");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("SchülerID zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte ErzieherArt_ID */
	public SchemaTabelleSpalte col_ErzieherArt_ID = add("ErzieherArt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ErziherARTID zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte Anrede1 */
	public SchemaTabelleSpalte col_Anrede1 = add("Anrede1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Anrede1 zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte Titel1 */
	public SchemaTabelleSpalte col_Titel1 = add("Titel1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Titel1 zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte Name1 */
	public SchemaTabelleSpalte col_Name1 = add("Name1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setJavaComment("Nachname1 zum Erzieherdatensatz PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte Vorname1 */
	public SchemaTabelleSpalte col_Vorname1 = add("Vorname1", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setJavaComment("Vorname1 zum Erzieherdatensatz PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte Anrede2 */
	public SchemaTabelleSpalte col_Anrede2 = add("Anrede2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Anrede2 zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte Titel2 */
	public SchemaTabelleSpalte col_Titel2 = add("Titel2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Titel2 zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte Name2 */
	public SchemaTabelleSpalte col_Name2 = add("Name2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(120)
		.setJavaComment("Nachname2 zum Erzieherdatensatz PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte Vorname2 */
	public SchemaTabelleSpalte col_Vorname2 = add("Vorname2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(80)
		.setJavaComment("Vorname2 zum Erzieherdatensatz PAuswG vom 21.6.2019 §5 Abs. 2");

	/** Die Definition der Tabellenspalte ErzStrasse */
	public SchemaTabelleSpalte col_ErzStrasse = add("ErzStrasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Straße zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte ErzOrt_ID */
	public SchemaTabelleSpalte col_ErzOrt_ID = add("ErzOrt_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("OrtID zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte ErzStrassenname */
	public SchemaTabelleSpalte col_ErzStrassenname = add("ErzStrassenname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setJavaComment("Straßenname des Erzieherdatensatzes");

	/** Die Definition der Tabellenspalte ErzPLZ */
	public SchemaTabelleSpalte col_ErzPLZ = add("ErzPLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_3)
		.setJavaComment("DEPRECATED: PLZ zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte ErzHausNr */
	public SchemaTabelleSpalte col_ErzHausNr = add("ErzHausNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Hausnummer wenn getrennt gespeichert");

	/** Die Definition der Tabellenspalte ErzOrtsteil_ID */
	public SchemaTabelleSpalte col_ErzOrtsteil_ID = add("ErzOrtsteil_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("OrtsteilID zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte ErzHausNrZusatz */
	public SchemaTabelleSpalte col_ErzHausNrZusatz = add("ErzHausNrZusatz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden");

	/** Die Definition der Tabellenspalte ErzAnschreiben */
	public SchemaTabelleSpalte col_ErzAnschreiben = add("ErzAnschreiben", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Erhältanschreiben Ja Nein zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Sortierung zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte ErzEmail */
	public SchemaTabelleSpalte col_ErzEmail = add("ErzEmail", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Email1 zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte ErzAdrZusatz */
	public SchemaTabelleSpalte col_ErzAdrZusatz = add("ErzAdrZusatz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Aresszusatz zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Erz1StaatKrz */
	public SchemaTabelleSpalte col_Erz1StaatKrz = add("Erz1StaatKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setConverter(NationalitaetenConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Staatangehörigkeit1 zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte Erz2StaatKrz */
	public SchemaTabelleSpalte col_Erz2StaatKrz = add("Erz2StaatKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setConverter(NationalitaetenConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Staatangehörigkeit2 zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte ErzEmail2 */
	public SchemaTabelleSpalte col_ErzEmail2 = add("ErzEmail2", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Email2 zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte Erz1ZusatzNachname */
	public SchemaTabelleSpalte col_Erz1ZusatzNachname = add("Erz1ZusatzNachname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Zusatznachname1 zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte Erz2ZusatzNachname */
	public SchemaTabelleSpalte col_Erz2ZusatzNachname = add("Erz2ZusatzNachname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Zusatznachname2 zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("Memofeld Bemerkungen zum Erzieherdatensatz");

	/** Die Definition der Tabellenspalte CredentialID */
	public SchemaTabelleSpalte col_CredentialID = add("CredentialID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die ID des Credential-Eintrags");


	/** Die Definition des Fremdschlüssels SchuelerErzAdr_Credentials_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerErzAdr_Credentials_FK = addForeignKey(
			"SchuelerErzAdr_Credentials_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_CredentialID, Schema.tab_Credentials.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerErzAdr_ErzieherArt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerErzAdr_ErzieherArt_FK = addForeignKey(
			"SchuelerErzAdr_ErzieherArt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_ErzieherArt_ID, Schema.tab_K_ErzieherArt.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerErzAdr_Ort_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerErzAdr_Ort_FK = addForeignKey(
			"SchuelerErzAdr_Ort_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_ErzOrt_ID, Schema.tab_K_Ort.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerErzAdr_Ortsteil_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerErzAdr_Ortsteil_FK = addForeignKey(
			"SchuelerErzAdr_Ortsteil_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_ErzOrtsteil_ID, Schema.tab_K_Ortsteil.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerErzAdr_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerErzAdr_Schueler_FK = addForeignKey(
			"SchuelerErzAdr_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerErzAdr_Staat1_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerErzAdr_Staat1_FK = addForeignKey(
			"SchuelerErzAdr_Staat1_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Erz1StaatKrz, Schema.tab_Nationalitaeten_Keys.col_DEStatisCode)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerErzAdr_Staat2_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerErzAdr_Staat2_FK = addForeignKey(
			"SchuelerErzAdr_Staat2_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Erz2StaatKrz, Schema.tab_Nationalitaeten_Keys.col_DEStatisCode)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/** Die Definition des Non-Unique-Index SchuelerErzAdr_IDX1 */
	public SchemaTabelleIndex index_SchuelerErzAdr_IDX1 = addIndex("SchuelerErzAdr_IDX1",
			col_Schueler_ID
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerErzAdr.
	 */
	public Tabelle_SchuelerErzAdr() {
		super("SchuelerErzAdr", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.erzieher");
		setJavaClassName("DTOSchuelerErzieherAdresse");
		setJavaComment("Erziehereinträge zum Schüler");
	}

}
