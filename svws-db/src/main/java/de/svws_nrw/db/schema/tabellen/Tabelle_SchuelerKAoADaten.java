package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerKAoADaten.
 */
public class Tabelle_SchuelerKAoADaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaName("id")
			.setJavaComment("ID des KAOA-Eintrags beim Schüler");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("DEPRECATED: Schüler-ID des KAOA-Eintrags beim Schüler, in Abschnitt_ID enthalten");

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaName("idLernabschnitt")
			.setJavaComment("ID der zugehörigen Schülerlernabschnittsdaten");

	/** Die Definition der Tabellenspalte Jahrgang */
	public SchemaTabelleSpalte col_Jahrgang = add("Jahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
			.setJavaName("jahrgang")
			.setJavaComment("Jahrgang des KAOA-Eintrags beim Schüler");

	/** Die Definition der Tabellenspalte KategorieID */
	public SchemaTabelleSpalte col_KategorieID = add("KategorieID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaName("idKategorie")
			.setJavaComment("ID der Kategorie des KAOA-Eintrags beim Schüler FK");

	/** Die Definition der Tabellenspalte MerkmalID */
	public SchemaTabelleSpalte col_MerkmalID = add("MerkmalID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idMerkmal")
			.setJavaComment("ID des Merkmal des KAOA-Eintrags beim Schüler FK");

	/** Die Definition der Tabellenspalte ZusatzmerkmalID */
	public SchemaTabelleSpalte col_ZusatzmerkmalID = add("ZusatzmerkmalID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idZusatzmerkmal")
			.setJavaComment("ID des Zusatzmerkmal des KAOA-Eintrags beim Schüler FK");

	/** Die Definition der Tabellenspalte AnschlussoptionID */
	public SchemaTabelleSpalte col_AnschlussoptionID = add("AnschlussoptionID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idAnschlussoption")
			.setJavaComment("ID der Anschlussoption des KAOA-Eintrags beim Schüler FK");

	/** Die Definition der Tabellenspalte BerufsfeldID */
	public SchemaTabelleSpalte col_BerufsfeldID = add("BerufsfeldID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idBerufsfeld")
			.setJavaComment("ID des Berufsfeld des KAOA-Eintrags beim Schüler FK");

	/** Die Definition der Tabellenspalte SBO_Ebene4ID */
	public SchemaTabelleSpalte col_SBO_Ebene4ID = add("SBO_Ebene4ID", SchemaDatentypen.BIGINT, false)
			.setJavaName("idEbene4")
			.setJavaComment("ID der Ebene4 des KAOA-Eintrags beim Schüler FK");

	/** Die Definition der Tabellenspalte Bemerkung */
	public SchemaTabelleSpalte col_Bemerkung = add("Bemerkung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setJavaName("bemerkung")
			.setJavaComment("Bemerkung des KAOA-Eintrags beim Schüler");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
			.setNotNull()
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank"
					+ " gespeichert werden");

	/** Die Definition der Tabellenspalte Jahr */
	public SchemaTabelleSpalte col_Jahr = add("Jahr", SchemaDatentypen.INT, false)
			.setNotNull()
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("Schuljahr des KAOA-Eintrags beim Schüler");

	/** Die Definition der Tabellenspalte Abschnitt */
	public SchemaTabelleSpalte col_Abschnitt = add("Abschnitt", SchemaDatentypen.INT, false)
			.setNotNull()
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment("Abschnitt des KAOA-Eintrags beim Schüler");


	/** Die Definition des Fremdschlüssels SchuelerKAoADaten_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerKAoADaten_Abschnitt_FK = addForeignKey(
			"SchuelerKAoADaten_Abschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abschnitt_ID, Schema.tab_SchuelerLernabschnittsdaten.col_ID)
	);

	/** Die Definition des Fremdschlüssels SchuelerKAoADaten_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerKAoADaten_Schueler_FK = addForeignKey(
			"SchuelerKAoADaten_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	).setVeraltet(SchemaRevisionen.REV_1);

	/** Die Definition des Fremdschlüssels SchuelerKAoADaten_Kategorie_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerKAoADaten_Kategorie_FK = addForeignKey(
			"SchuelerKAoADaten_Kategorie_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_KategorieID, Schema.tab_KAoA_Kategorie_Keys.col_ID)
	);

	/** Die Definition des Fremdschlüssels SchuelerKAoADaten_Merkmal_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerKAoADaten_Merkmal_FK = addForeignKey(
			"SchuelerKAoADaten_Merkmal_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_MerkmalID, Schema.tab_KAoA_Merkmal_Keys.col_ID)
	).setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerKAoADaten_Zusatzmerkmal_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerKAoADaten_Zusatzmerkmal_FK = addForeignKey(
			"SchuelerKAoADaten_Zusatzmerkmal_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_ZusatzmerkmalID, Schema.tab_KAoA_Zusatzmerkmal_Keys.col_ID)
	).setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerKAoADaten_Anschlussoption_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerKAoADaten_Anschlussoption_FK = addForeignKey(
			"SchuelerKAoADaten_Anschlussoption_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_AnschlussoptionID, Schema.tab_KAoA_Anschlussoption_Keys.col_ID)
	).setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerKAoADaten_Berufsfeld_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerKAoADaten_Berufsfeld_FK = addForeignKey(
			"SchuelerKAoADaten_Berufsfeld_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_BerufsfeldID, Schema.tab_KAoA_Berufsfeld_Keys.col_ID)
	).setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels SchuelerKAoADaten_SBO_Ebene4_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerKAoADaten_SBO_Ebene4_FK = addForeignKey(
			"SchuelerKAoADaten_SBO_Ebene4_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_SBO_Ebene4ID, Schema.tab_KAoA_SBO_Ebene4_Keys.col_ID)
	).setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerKAoADaten.
	 */
	public Tabelle_SchuelerKAoADaten() {
		super("SchuelerKAoADaten", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schueler");
		setJavaClassName("DTOSchuelerKAoADaten");
		setJavaComment("Einträge beim Schüler zu den KAOA-Defaut-Werten");
	}

}
