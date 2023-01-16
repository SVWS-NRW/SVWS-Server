package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.gost.GOStHalbjahrConverter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Vorgaben.
 */
public class Tabelle_Gost_Klausuren_Vorgaben extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klausurvorgaben (generiert)");

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Der Abiturjahrgang, dem die Klausurvorgabe zugeordnet ist");

	/** Die Definition der Tabellenspalte Halbjahr */
	public SchemaTabelleSpalte col_Halbjahr = add("Halbjahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setConverter(GOStHalbjahrConverter.class)
		.setJavaComment("Das Halbjahr, welchem die Klausurvorgabe zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)");
	
	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("Fach_ID der Klausurvorgaben");
	
	/** Die Definition der Tabellenspalte KursartAllg */
	public SchemaTabelleSpalte col_KursartAllg = add("KursartAllg", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("Allgemeine Kursart des Klausur-Kurses");

	/** Die Definition der Tabellenspalte Quartal */
	public SchemaTabelleSpalte col_Quartal = add("Quartal", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Das Quartal, in dem die Klausur geschrieben wird.");

	/** Die Definition der Tabellenspalte Dauer */
	public SchemaTabelleSpalte col_Dauer = add("Dauer", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Das Dauer der Klausur/Prüfung in Minuten");

	/** Die Definition der Tabellenspalte Auswahlzeit */
	public SchemaTabelleSpalte col_Auswahlzeit = add("Auswahlzeit", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Das Dauer der Auswahlzeit in Minuten");
	
	/** Die Definition der Tabellenspalte IstMdlPruefung */
	public SchemaTabelleSpalte col_IstMdlPruefung = add("IstMdlPruefung", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob es sich um eine mündliche Prüfunge handelt oder nicht: 1 - true, 0 - false.");

	/** Die Definition der Tabellenspalte IstAudioNotwendig */
	public SchemaTabelleSpalte col_IstAudioNotwendig = add("IstAudioNotwendig", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob es sich um eine Klausur mit Hörverstehen handelt oder nicht: 1 - true, 0 - false.");

	/** Die Definition der Tabellenspalte IstVideoNotwendig */
	public SchemaTabelleSpalte col_IstVideoNotwendig = add("IstVideoNotwendig", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob es sich um eine Klausur handelt, in der ein Video gezeigt werden muss oder nicht: 1 - true, 0 - false.");
	
	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Bemerkungen zur Klausurvorlage");

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Vorgaben_Abi_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Vorgaben_Abi_Jahrgang_FK = addForeignKey(
			"Gost_Klausuren_Vorgaben_Abi_Jahrgang_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Abi_Jahrgang, Schema.tab_Gost_Jahrgangsdaten.col_Abi_Jahrgang)
		);
	
	/** Die Definition des Fremdschlüssels Gost_Klausuren_Vorgaben_Fach_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Vorgaben_Fach_FK = addForeignKey(
			"Gost_Klausuren_Vorgaben_Fach_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);

	/** Die Definition des Unique-Index Gost_Klausuren_Vorgaben_UC1 */
	public SchemaTabelleUniqueIndex unique_Gost_Klausuren_Vorgaben_UC1 = addUniqueIndex("Gost_Klausuren_Vorgaben_UC1", 
			col_Abi_Jahrgang, col_Fach_ID, col_Halbjahr, col_KursartAllg, col_Quartal
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Vorgaben.
	 */
	public Tabelle_Gost_Klausuren_Vorgaben() {
		super("Gost_Klausuren_Vorgaben", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenVorgaben");
		setJavaComment("Tabelle für die Definition von Vorgaben für Klausuren");
	}

}
