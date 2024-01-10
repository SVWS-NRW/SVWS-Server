package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Blockung.
 */
public class Tabelle_Gost_Blockung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Blockung (generiert)");

	/** Die Definition der Tabellenspalte Name */
	public SchemaTabelleSpalte col_Name = add("Name", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setDefault("Neue Blockung")
		.setNotNull()
		.setJavaComment("Textuelle Bezeichnung der Blockung");

	/** Die Definition der Tabellenspalte Abi_Jahrgang */
	public SchemaTabelleSpalte col_Abi_Jahrgang = add("Abi_Jahrgang", SchemaDatentypen.INT, false)
		.setNotNull()
		.setJavaComment("Der Abiturjahrgang, dem die Blockung zugeordnet ist");

	/** Die Definition der Tabellenspalte Halbjahr */
	public SchemaTabelleSpalte col_Halbjahr = add("Halbjahr", SchemaDatentypen.INT, false)
		.setNotNull()
		.setConverter(GOStHalbjahrConverter.class)
		.setJavaComment("Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)");

	/** Die Definition der Tabellenspalte IstAktiv */
	public SchemaTabelleSpalte col_IstAktiv = add("IstAktiv", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob die Blockung als aktive Blockung markiert wurde oder nicht: 1 - true, 0 - false.");

	/** Die Definition des Fremdschlüssels Gost_Blockung_Abi_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Blockung_Abi_Jahrgang_FK = addForeignKey(
			"Gost_Blockung_Abi_Jahrgang_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abi_Jahrgang, Schema.tab_Gost_Jahrgangsdaten.col_Abi_Jahrgang)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Blockung.
	 */
	public Tabelle_Gost_Blockung() {
		super("Gost_Blockung", SchemaRevisionen.REV_7);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.kursblockung");
		setJavaClassName("DTOGostBlockung");
		setJavaComment("Tabelle für die Definition einer Kursblockung der gymnasialen Oberstufe");
	}

}
