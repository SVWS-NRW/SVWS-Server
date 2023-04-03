package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BenutzerTypConverter;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Benutzer.
 */
public class Tabelle_Benutzer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die ID des Benutzers");

	/** Die Definition der Tabellenspalte Typ */
	public SchemaTabelleSpalte col_Typ = add("Typ", SchemaDatentypen.SMALLINT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(BenutzerTypConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Der Typ des Benutzers (0 = Allgemeiner Benutzer, 1 = Lehrer bzw. Personal aus K_Lehrer, 2 = Schueler, 3 = Erzieher)");

	/** Die Definition der Tabellenspalte Allgemein_ID */
	public SchemaTabelleSpalte col_Allgemein_ID = add("Allgemein_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die ID des allgemeinen Benutzers, falls der Benutzer es sich um einen allgemeinen Benutzer handelt");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die Lehrer-ID des Benutzers, falls der Benutzer es sich um einen Lehrer handelt");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die Schüler-ID des Benutzers, falls der Benutzer es sich um einen Schüler handelt");

	/** Die Definition der Tabellenspalte Erzieher_ID */
	public SchemaTabelleSpalte col_Erzieher_ID = add("Erzieher_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die erzieher-ID des Benutzers, falls der Benutzer es sich um einen Erzieher handelt");

	/** Die Definition der Tabellenspalte IstAdmin */
	public SchemaTabelleSpalte col_IstAdmin = add("IstAdmin", SchemaDatentypen.INT, false)
		.setDefault("0")
		.setNotNull()
		.setConverter(Boolean01Converter.class)
		.setJavaComment("Gibt an, ob der Benutzer Administrator-Rechte hat (1) oder nicht (0)");


	/** Die Definition des Fremdschlüssels Benutzer_Allgemein_FK */
	public SchemaTabelleFremdschluessel fk_Benutzer_Allgemein_FK = addForeignKey(
			"Benutzer_Allgemein_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Allgemein_ID, Schema.tab_BenutzerAllgemein.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Benutzer_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_Benutzer_Lehrer_FK = addForeignKey(
			"Benutzer_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Benutzer_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_Benutzer_Schueler_FK = addForeignKey(
			"Benutzer_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Benutzer_Erzieher_FK */
	public SchemaTabelleFremdschluessel fk_Benutzer_Erzieher_FK = addForeignKey(
			"Benutzer_Erzieher_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Erzieher_ID, Schema.tab_SchuelerErzAdr.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Benutzer.
	 */
	public Tabelle_Benutzer() {
		super("Benutzer", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.benutzer");
		setJavaClassName("DTOBenutzer");
		setJavaComment("Die Benutzer, welchen sich am Server anmelden können (verweist auf allgemeine Benutzer, Lehrer bzw. Personal, Schüler und Erzieher). ");
	}

}
