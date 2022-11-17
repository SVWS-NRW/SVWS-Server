package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schild_Verwaltung.
 */
public class Tabelle_Schild_Verwaltung extends SchemaTabelle {

	/** Die Definition der Tabellenspalte BackupDatum */
	public SchemaTabelleSpalte col_BackupDatum = add("BackupDatum", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Wird das Dateum des letzten Backupo eingetragen.");

	/** Die Definition der Tabellenspalte AutoBerechnung */
	public SchemaTabelleSpalte col_AutoBerechnung = add("AutoBerechnung", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Wurden die täglichen automatischen Prozesse schon durchgeführt?");

	/** Die Definition der Tabellenspalte DatumStatkue */
	public SchemaTabelleSpalte col_DatumStatkue = add("DatumStatkue", SchemaDatentypen.DATETIME, false)
		.setJavaComment("DEPRECATED Datum der Statkue wird nicht benutzt.");

	/** Die Definition der Tabellenspalte DatumSchildIntern */
	public SchemaTabelleSpalte col_DatumSchildIntern = add("DatumSchildIntern", SchemaDatentypen.DATETIME, false)
		.setJavaComment("DEPRECATED Datum der Schildintern wird nicht benutzt.");

	/** Die Definition der Tabellenspalte Bescheinigung */
	public SchemaTabelleSpalte col_Bescheinigung = add("Bescheinigung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Pfad zu der ausgewählten Reportvorlage");

	/** Die Definition der Tabellenspalte Stammblatt */
	public SchemaTabelleSpalte col_Stammblatt = add("Stammblatt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Pfad zu der ausgewählten Reportvorlage");

	/** Die Definition der Tabellenspalte DatenGeprueft */
	public SchemaTabelleSpalte col_DatenGeprueft = add("DatenGeprueft", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Stößt eine Datenprüfung nach großen Importen an");

	/** Die Definition der Tabellenspalte Version */
	public SchemaTabelleSpalte col_Version = add("Version", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Versionsdatum (wird zur Erkennung für Updates genutzt)");

	/** Die Definition der Tabellenspalte GU_ID */
	public SchemaTabelleSpalte col_GU_ID = add("GU_ID", SchemaDatentypen.VARCHAR, true).setDatenlaenge(40)
		.setNotNull()
		.setJavaComment("Stellt eine GU_ID für die Datenbank zur Verfügung damit bei Kurs42 erkannt werden kann ob verschiedene DBs verwendet wurden.");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte DatumLoeschfristHinweisDeaktiviert */
	public SchemaTabelleSpalte col_DatumLoeschfristHinweisDeaktiviert = add("DatumLoeschfristHinweisDeaktiviert", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Gibt an ob der User den Hiweis zu den Löschfristen deaktiviert hat.");

	/** Die Definition der Tabellenspalte DatumLoeschfristHinweisDeaktiviertUserID */
	public SchemaTabelleSpalte col_DatumLoeschfristHinweisDeaktiviertUserID = add("DatumLoeschfristHinweisDeaktiviertUserID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Gibt an welcher User den Hiweis deaktiviert hat.");

	/** Die Definition der Tabellenspalte DatumDatenGeloescht */
	public SchemaTabelleSpalte col_DatumDatenGeloescht = add("DatumDatenGeloescht", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Gibt an wann der Löschvorgang zuletzt gelaufen ist.");


	/** Die Definition des Fremdschlüssels Schild_Verwaltung_Loeschfrist_deaktiviert_durch_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_Schild_Verwaltung_Loeschfrist_deaktiviert_durch_Benutzer_FK = addForeignKey(
			"Schild_Verwaltung_Loeschfrist_deaktiviert_durch_Benutzer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL, 
			new Pair<>(col_DatumLoeschfristHinweisDeaktiviertUserID, Schema.tab_Benutzer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schild_Verwaltung.
	 */
	public Tabelle_Schild_Verwaltung() {
		super("Schild_Verwaltung", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild");
		setJavaClassName("DTOSchildVerwaltung");
		setJavaComment("Verwaltung von Versionsnummern, Datum, teilweise veraltet oder nie verwendet");
	}

}
