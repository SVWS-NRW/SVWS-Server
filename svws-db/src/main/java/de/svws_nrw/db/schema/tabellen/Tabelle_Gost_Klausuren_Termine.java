package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.UhrzeitConverter;
import de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Termine.
 */
public class Tabelle_Gost_Klausuren_Termine extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Klausurtermins (generiert)");

	/** Die Definition der Tabellenspalte Schuljahresabschnitt_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitt_ID = add("Schuljahresabschnitt_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("ID des Schuljahresabschnittes")
			.setRevision(SchemaRevisionen.REV_40);

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, false)
			.setNotNull()
			.setJavaComment("Der Abiturjahrgang, dem die Klausurvorgabe zugeordnet ist");

	/** Die Definition der Tabellenspalte Halbjahr */
	public SchemaTabelleSpalte col_Halbjahr = add("Halbjahr", SchemaDatentypen.INT, false)
			.setNotNull()
			.setConverter(GOStHalbjahrConverter.class)
			.setJavaComment("Das Halbjahr, welchem die Klausurvorgabe zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)");

	/** Die Definition der Tabellenspalte Quartal */
	public SchemaTabelleSpalte col_Quartal = add("Quartal", SchemaDatentypen.INT, false)
			.setNotNull()
			.setJavaComment("Das Quartal, in dem die Klausur geschrieben wird.");

	/** Die Definition der Tabellenspalte Datum */
	public SchemaTabelleSpalte col_Datum = add("Datum", SchemaDatentypen.DATE, false)
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum des Klausurtermins");

	/** Die Definition der Tabellenspalte Startzeit */
	public SchemaTabelleSpalte col_Startzeit = add("Startzeit", SchemaDatentypen.TIME, false)
			.setConverter(UhrzeitConverter.class)
			.setJavaComment("Die Startzeit des Klausurtermins");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.TEXT, false)
			.setJavaComment("Text für Bezeichnung des Klausurtermins");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
			.setJavaComment("Text für Bemerkungen des Klausurtermins");

	/** Die Definition der Tabellenspalte IstHaupttermin */
	public SchemaTabelleSpalte col_IstHaupttermin = add("IstHaupttermin", SchemaDatentypen.INT, false)
			.setDefault("1")
			.setNotNull()
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob es sich bei dem Termin um den Haupttermin (1) handelt oder einen Nachschreibtermin (0).");

	/** Die Definition der Tabellenspalte NachschreiberZugelassen */
	public SchemaTabelleSpalte col_NachschreiberZugelassen = add("NachschreiberZugelassen", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setNotNull()
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob bei einem Haupttermin Nachschreibklausuren zugelassen sind (1) oder nicht (0).");

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Termine_Schuljahresabschnitt_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Termine_Schuljahresabschnitt_ID_FK = addForeignKey(
			"Gost_Klausuren_Termine_Schuljahresabschnitt_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schuljahresabschnitt_ID, Schema.tab_Schuljahresabschnitte.col_ID)
	).setRevision(SchemaRevisionen.REV_40);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Termine_Abi_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Termine_Abi_Jahrgang_FK = addForeignKey(
			"Gost_Klausuren_Termine_Abi_Jahrgang_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abi_Jahrgang, Schema.tab_Gost_Jahrgangsdaten.col_Abi_Jahrgang)
	);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_Termine_IDX_Schuljahresabschnitt_ID */
	public SchemaTabelleIndex index_Gost_Klausuren_Termine_IDX_Schuljahresabschnitt_ID = addIndex("Gost_Klausuren_Termine_IDX_Schuljahresabschnitt_ID",
			col_Schuljahresabschnitt_ID
	).setRevision(SchemaRevisionen.REV_40);

	/** Die Definition des Non-Unique-Index Gost_Klausuren_Termine_IDX_Abi_Jahrgang */
	public SchemaTabelleIndex index_Gost_Klausuren_Termine_IDX_Abi_Jahrgang = addIndex("Gost_Klausuren_Termine_IDX_Abi_Jahrgang",
			col_Abi_Jahrgang
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Termine.
	 */
	public Tabelle_Gost_Klausuren_Termine() {
		super("Gost_Klausuren_Termine", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenTermine");
		setJavaComment("Tabelle für die Definition von Terminen für Klausuren");
	}

}
