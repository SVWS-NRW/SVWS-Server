package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaFremdschluesselAktionen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerWiedervorlage.
 */
public class Tabelle_SchuelerWiedervorlage extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Wiedervorlageeitrags beim Schüler");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("SchülerID des Wiedervorlageeintrags");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte Bemerkung */
	public SchemaTabelleSpalte col_Bemerkung = add("Bemerkung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
		.setJavaComment("Bemerkung des Wiedervorlageeintrags");

	/** Die Definition der Tabellenspalte AngelegtAm */
	public SchemaTabelleSpalte col_AngelegtAm = add("AngelegtAm", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Datum Anlage des Wiedervorlageeintrags");

	/** Die Definition der Tabellenspalte WiedervorlageAm */
	public SchemaTabelleSpalte col_WiedervorlageAm = add("WiedervorlageAm", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Datum Wiedervorlage am");

	/** Die Definition der Tabellenspalte ErledigtAm */
	public SchemaTabelleSpalte col_ErledigtAm = add("ErledigtAm", SchemaDatentypen.DATETIME, false)
		.setJavaComment("Datum Erledigung  des Wiedervorlageeintrags");

	/** Die Definition der Tabellenspalte User_ID */
	public SchemaTabelleSpalte col_User_ID = add("User_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("UserID  des Wiedervorlageeintrags");

	/** Die Definition der Tabellenspalte Sekretariat */
	public SchemaTabelleSpalte col_Sekretariat = add("Sekretariat", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Steuert die Sichtbarkeit für den User Sekretariat");

	/** Die Definition der Tabellenspalte Typ */
	public SchemaTabelleSpalte col_Typ = add("Typ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Typ des Wiedervorlageeintrags");

	/** Die Definition der Tabellenspalte NichtLoeschen */
	public SchemaTabelleSpalte col_NichtLoeschen = add("NichtLoeschen", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter("BooleanPlusMinusDefaultMinusConverter")
		.setJavaComment("Steuert die automatische Löschung der Einträge");


	/** Die Definition des Fremdschlüssels SchuelerWiedervorlage_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerWiedervorlage_Schueler_FK = addForeignKey(
			"SchuelerWiedervorlage_Schueler_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerWiedervorlage_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerWiedervorlage_Benutzer_FK = addForeignKey(
			"SchuelerWiedervorlage_Benutzer_FK", 
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE, 
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE, 
			new Pair<>(col_User_ID, Schema.tab_Benutzer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_1);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerWiedervorlage.
	 */
	public Tabelle_SchuelerWiedervorlage() {
		super("SchuelerWiedervorlage", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild");
		setJavaClassName("DTOSchuelerWiedervorlage");
		setJavaComment("Einträge zur Wiedervorlage zum Schüler");
	}

}
