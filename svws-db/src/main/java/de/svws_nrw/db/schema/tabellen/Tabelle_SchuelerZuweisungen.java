package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerZuweisungen.
 */
public class Tabelle_SchuelerZuweisungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Abschnitt_ID */
	public SchemaTabelleSpalte col_Abschnitt_ID = add("Abschnitt_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("LernabschnittsID  der Zuweisung (E G Kurse GE und PS SK)");

	/** Die Definition der Tabellenspalte Fach_ID */
	public SchemaTabelleSpalte col_Fach_ID = add("Fach_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("FachID der Zuweisung");

	/** Die Definition der Tabellenspalte Kursart */
	public SchemaTabelleSpalte col_Kursart = add("Kursart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kursart der Zuweisung");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels SchuelerZuweisungen_Abschnitt_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerZuweisungen_Abschnitt_FK = addForeignKey(
			"SchuelerZuweisungen_Abschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Abschnitt_ID, Schema.tab_SchuelerLernabschnittsdaten.col_ID)
		);

	/** Die Definition des Fremdschlüssels SchuelerZuweisungen_Fach_FK */
	public SchemaTabelleFremdschluessel fk_SchuelerZuweisungen_Fach_FK = addForeignKey(
			"SchuelerZuweisungen_Fach_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Fach_ID, Schema.tab_EigeneSchule_Faecher.col_ID)
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerZuweisungen.
	 */
	public Tabelle_SchuelerZuweisungen() {
		super("SchuelerZuweisungen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.berufskolleg");
		setJavaClassName("DTOSchuelerZuweisung");
		setJavaComment("Lernabscnittsbezogene Zuweisungen für Gesamt- und Sekundarschulen");
	}

}
