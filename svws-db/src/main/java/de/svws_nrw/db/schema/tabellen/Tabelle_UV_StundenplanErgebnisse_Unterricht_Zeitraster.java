package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_StundenplanErgebnisse_Unterricht_Zeitraster.
 */
public class Tabelle_UV_StundenplanErgebnisse_Unterricht_Zeitraster extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Ergebnis_ID */
	public final SchemaTabelleSpalte col_Ergebnis_ID = add("Ergebnis_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Fremdschlüssel auf das Stundenplanergebnis");

	/** Die Definition der Tabellenspalte Unterricht_ID */
	public final SchemaTabelleSpalte col_Unterricht_ID = add("Unterricht_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Fremdschlüssel auf den UV_Unterricht");

	/** Die Definition der Tabellenspalte ZeitrasterEintrag_ID */
	public final SchemaTabelleSpalte col_ZeitrasterEintrag_ID = add("ZeitrasterEintrag_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Fremdschlüssel auf den UV_ZeitrasterEintrag");


	/** Die Definition des Fremdschlüssels UVStundenplanErgUntZr_UVStundenplanErg_FK */
	public final SchemaTabelleFremdschluessel fk_UVStundenplanErgUntZr_UVStundenplanErg_FK = addForeignKey(
			"UVStundenplanErgUntZr_UVStundenplanErg_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Ergebnis_ID, Schema.tab_UV_StundenplanErgebnisse.col_ID));

	/** Die Definition des Fremdschlüssels UVStundenplanErgUntZr_UVUnt_FK */
	public final SchemaTabelleFremdschluessel fk_UVStundenplanErgUntZr_UVUnt_FK = addForeignKey(
			"UVStundenplanErgUntZr_UVUnterrichte_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Unterricht_ID, Schema.tab_UV_Unterrichte.col_ID));

	/** Die Definition des Fremdschlüssels UVStundenplanErgUntZr_UVZrEintraege_FK */
	public final SchemaTabelleFremdschluessel fk_UVStundenplanErgUntZr_UVZrEintraege_FK = addForeignKey(
			"UVStundenplanErgUntZr_UVZrEintraege_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.RESTRICT,
			new Pair<>(col_ZeitrasterEintrag_ID, Schema.tab_UV_Zeitraster_Eintraege.col_ID));


	/** Die Definition des Unique-Index UV_StundenplanErgebnisse_Unterricht_Zeitraster_UC1 */
	public final SchemaTabelleUniqueIndex unique_StundenplanErgebnisse_Unterricht_Zeitraster_UC1 = addUniqueIndex("UVStundenplanErgebnisse_Unterricht_Zeitraster_UC1",
			col_Ergebnis_ID, col_Unterricht_ID
	);


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_StundenplanErgebnisse_Unterricht_Zeitraster.
	 */
	public Tabelle_UV_StundenplanErgebnisse_Unterricht_Zeitraster() {
		super("UV_StundenplanErgebnisse_Unterricht_Zeitraster", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvStundenplanErgebnisUnterrichtZeitraster");
		setJavaComment("Tabelle für die Zuteilung von Zeitrastereinträgen zu Unterrichten in der UV-Stundenplanung");
	}

}
