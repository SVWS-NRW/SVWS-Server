package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.UhrzeitConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Gost_Klausuren_Raeume_Stunden_Aufsichten.
 */
public class Tabelle_Gost_Klausuren_Raeume_Stunden_Aufsichten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klausuraufsicht (generiert)");

	/** Die Definition der Tabellenspalte KlausurRaumStunde_ID */
	public SchemaTabelleSpalte col_KlausurRaumStunde_ID = add("KlausurRaumStunde_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID der Klausur-Raumstunde");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des Lehrers");

	/** Die Definition der Tabellenspalte Startzeit */
	public SchemaTabelleSpalte col_Startzeit = add("Startzeit", SchemaDatentypen.TIME, false)
		.setConverter(UhrzeitConverter.class)
		.setJavaComment("Die Startzeit der Aufsicht");

	/** Die Definition der Tabellenspalte Endzeit */
	public SchemaTabelleSpalte col_Endzeit = add("Endzeit", SchemaDatentypen.TIME, false)
		.setConverter(UhrzeitConverter.class)
		.setJavaComment("Die Endzeit der Aufsicht");

	/** Die Definition der Tabellenspalte Bemerkungen */
	public SchemaTabelleSpalte col_Bemerkungen = add("Bemerkungen", SchemaDatentypen.TEXT, false)
		.setJavaComment("Text für Bemerkungen zur Aufsicht");

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Raeume_Stunden_Aufsichten_KlausurRaumStunde_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Raeume_Stunden_Aufsichten_KlausurRaumStunde_ID_FK = addForeignKey(
			"Gost_Klausuren_Raeume_Stunden_Aufsichten_KlausurRaumStunde_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_KlausurRaumStunde_ID, Schema.tab_Gost_Klausuren_Raeume_Stunden.col_ID)
		);

	/** Die Definition des Fremdschlüssels Gost_Klausuren_Raeume_Stunden_Aufsichten_Lehrer_ID_FK */
	public SchemaTabelleFremdschluessel fk_Gost_Klausuren_Raeume_Stunden_Aufsichten_Lehrer_ID_FK = addForeignKey(
			"Gost_Klausuren_Raeume_Stunden_Aufsichten_Lehrer_ID_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Gost_Klausuren_Raeume_Stunden_Aufsichten.
	 */
	public Tabelle_Gost_Klausuren_Raeume_Stunden_Aufsichten() {
		super("Gost_Klausuren_Raeume_Stunden_Aufsichten", SchemaRevisionen.REV_9);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("gost.klausurplanung");
		setJavaClassName("DTOGostKlausurenRaeumeStundenAufsichten");
		setJavaComment("Tabelle für die Definition von Aufsichten für Klausur-Raumstunden");
	}

}
