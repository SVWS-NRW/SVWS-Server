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
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle UV_StundenplanErgebnisse_Unterricht_Raeume.
 */
public class Tabelle_UV_StundenplanErgebnisse_Unterricht_Raeume extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Ergebnis_ID */
	public final SchemaTabelleSpalte col_Ergebnis_ID = add("Ergebnis_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Fremdschlüssel auf das Stundenplanergebnis");

	/** Die Definition der Tabellenspalte Unterricht_ID */
	public final SchemaTabelleSpalte col_Unterricht_ID = add("Unterricht_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Fremdschlüssel auf den UV_Unterricht");

	/** Die Definition der Tabellenspalte Raum_ID */
	public final SchemaTabelleSpalte col_Raum_ID = add("Raum_ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Fremdschlüssel auf den Raum (Tabelle UV_Raeume)");


	/** Die Definition des Fremdschlüssels UVStundenplanErgUntRaeume_UVStundenplanErg_FK */
	public final SchemaTabelleFremdschluessel fk_UVStundenplanErgUntRaeume_UVStundenplanErg_FK = addForeignKey(
			"UVStundenplanErgeUntRaeume_UVStundenplanErg_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Ergebnis_ID, Schema.tab_UV_StundenplanErgebnisse.col_ID));

	/** Die Definition des Fremdschlüssels UVStundenplanErgUntRaeume_UVUnt_FK */
	public final SchemaTabelleFremdschluessel fk_UVStundenplanErgUntRaeume_UVUnt_FK = addForeignKey(
			"UVStundenplanErgUntRaeume_UVUnt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Unterricht_ID, Schema.tab_UV_Unterrichte.col_ID));

	/** Die Definition des Fremdschlüssels UVStundenplanErgUntRaeume_UVRaeume_FK */
	public final SchemaTabelleFremdschluessel fk_UVStundenplanErgUntRaeume_UVRaeume_FK = addForeignKey(
			"UVStundenplanErgUntRaeume_UVRaeume_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Raum_ID, Schema.tab_UV_Raeume.col_ID));


	/**
	 * Erstellt die Schema-Definition für die Tabelle UV_StundenplanErgebnisse_Unterricht_Raeume.
	 */
	public Tabelle_UV_StundenplanErgebnisse_Unterricht_Raeume() {
		super("UV_StundenplanErgebnisse_Unterricht_Raeume", SchemaRevisionen.REV_48);
		setMigrate(false);
		setImportExport(true);
		setJavaSubPackage("uv");
		setJavaClassName("DTOUvStundenplanErgebnisUnterrichtRaum");
		setJavaComment("Tabelle für die Zuteilung von Räumen zu Unterrichten in der UV-Stundenplanung");
	}

}
