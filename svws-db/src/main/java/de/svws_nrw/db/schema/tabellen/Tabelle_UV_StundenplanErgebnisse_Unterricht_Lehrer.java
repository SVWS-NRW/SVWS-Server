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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_StundenplanErgebnisse_Unterricht_Lehrer.
 */
public class Tabelle_UV_StundenplanErgebnisse_Unterricht_Lehrer extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Ergebnis_ID */
	public final SchemaTabelleSpalte col_Ergebnis_ID = add("Ergebnis_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Fremdschlüssel auf das Stundenplanergebnis");

	/** Die Definition der Tabellenspalte Unterricht_ID */
	public final SchemaTabelleSpalte col_Unterricht_ID = add("Unterricht_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Fremdschlüssel auf den UV_Unterricht");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public final SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Fremdschlüssel auf den UV_Lerngruppen_Lehrer");


	/** Die Definition des Fremdschlüssels UVStundenplanErgUntLehrer_UVStundenplanErg_FK */
	public final SchemaTabelleFremdschluessel fk_UVStundenplanErgUntLehrer_UVStundenplanErg_FK = addForeignKey(
			"UVStundenplanErgeUntLehrer_UVStundenplanErg_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Ergebnis_ID, Schema.tab_UV_StundenplanErgebnisse.col_ID));

	/** Die Definition des Fremdschlüssels UVStundenplanErgUntLehrer_UVUnt_FK */
	public final SchemaTabelleFremdschluessel fk_UVStundenplanErgUntLehrer_UVUnt_FK = addForeignKey(
			"UVStundenplanErgUntLehrer_UVUnt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Unterricht_ID, Schema.tab_UV_Unterrichte.col_ID));

	/** Die Definition des Fremdschlüssels UVStundenplanErgUntLehrer_UVLerngrLehrer_FK */
	public final SchemaTabelleFremdschluessel fk_UVStundenplanErgUntLehrer_UVLerngrLehrer_FK = addForeignKey(
			"UVStundenplanErgUntLehrer_UVLerngrLehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_UV_Lerngruppen_Lehrer.col_ID));


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_StundenplanErgebnisse_Unterricht_Lehrer.
	 */
	public Tabelle_UV_StundenplanErgebnisse_Unterricht_Lehrer() {
		super("UV_StundenplanErgebnisse_Unterricht_Lehrer", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvStundenplanErgebnisUnterrichtLehrer");
		setJavaComment("Tabelle für die Zuteilung von Lerngruppen-Lehrern zu Unterrichten in der UV-Stundenplanung");
	}

}
