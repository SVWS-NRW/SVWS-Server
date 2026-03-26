package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle LehrerUnterrichtsfaecher.
 */
public class Tabelle_LehrerUnterrichtsfaecher extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID für den Eintrag für das Unterrichtsfach");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public final SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Lehrers");

	/** Die Definition der Tabellenspalte Fach_ID */
	public final SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, false)
			.setNotNull()
			.setJavaComment("Die ID des Fachs");

	/** Die Definition der Tabellenspalte IstSek1 */
	public final SchemaTabelleSpalte col_IstSek1 = add("IstSek1", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setNotNull()
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf");

	/** Die Definition der Tabellenspalte IstSek2 */
	public final SchemaTabelleSpalte col_IstSek2 = add("IstSek2", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setNotNull()
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf");

	/** Die Definition der Tabellenspalte Bemerkung */
	public final SchemaTabelleSpalte col_Bemerkung = add("Bemerkung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setJavaComment("Bemerkung zum Unterrichtsfach");

	/** Die Definition der Tabellenspalte GueltigVon */
	public final SchemaTabelleSpalte col_GueltigVon = add("GueltigVon", SchemaDatentypen.DATE, false)
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum, ab dem die Lehrkraft das Fach unterrichtet");

	/** Die Definition der Tabellenspalte GueltigBis */
	public final SchemaTabelleSpalte col_GueltigBis = add("GueltigBis", SchemaDatentypen.DATE, false)
			.setConverter(DatumConverter.class)
			.setJavaComment("Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet");


	/** Die Definition des Fremdschlüssels LehrerUnterrichtsfaecher_Lehrer_FK */
	public final SchemaTabelleFremdschluessel fk_LehrerUnterrichtsfaecher_Lehrer_FK = addForeignKey(
			"LehrerUnterrichtsfaecher_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
	);

	/** Die Definition des Fremdschlüssels LehrerUnterrichtsfaecher_Fach_FK */
	public final SchemaTabelleFremdschluessel fk_LehrerUnterrichtsfaecher_Fach_FK = addForeignKey(
			"LehrerUnterrichtsfaecher_Fach_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
	);


	/** Die Definition des Unique-Index LehrerUnterrichtsfaecher_UC1 */
	public final SchemaTabelleUniqueIndex unique_LehrerUnterrichtsfaecher_UC1 = addUniqueIndex("LehrerUnterrichtsfaecher_UC1",
			col_Lehrer_ID,
			col_Fach_ID
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle LehrerUnterrichtsfaecher.
	 */
	public Tabelle_LehrerUnterrichtsfaecher() {
		super("LehrerUnterrichtsfaecher", SchemaRevisionen.REV_59);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.lehrer");
		setJavaClassName("DTOLehrerUnterrichtsfach");
		setJavaComment("Fächer, die eine Lehrkraft an der Schule unterrichtet");
	}

}
