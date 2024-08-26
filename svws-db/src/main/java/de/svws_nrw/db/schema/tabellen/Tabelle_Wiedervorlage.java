package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.PersonTypNullableConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Wiedervorlage.
 */
public class Tabelle_Wiedervorlage extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("ID des Eintrags für die Wiedervorlage");

	/** Die Definition der Tabellenspalte PersonTyp */
	public SchemaTabelleSpalte col_PersonTyp = add("PersonTyp", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setConverter(PersonTypNullableConverter.class)
			.setJavaName("personTyp")
			.setJavaComment("Der Typ der Person, welche der Wiedevorlage zugeordnet ist (S=Schueler L=Lehrer E=Erzieher)");

	/** Die Definition der Tabellenspalte Lehrer_ID */
	public SchemaTabelleSpalte col_Lehrer_ID = add("Lehrer_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die Lehrer-ID des Benutzers, falls PersonTyp sich auf einen Lehrer bezieht");

	/** Die Definition der Tabellenspalte Schueler_ID */
	public SchemaTabelleSpalte col_Schueler_ID = add("Schueler_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die Schüler-ID des Benutzers, falls PersonTyp sich auf einen Lehrer bezieht");

	/** Die Definition der Tabellenspalte Erzieher_ID */
	public SchemaTabelleSpalte col_Erzieher_ID = add("Erzieher_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die Erzieher-ID des Benutzers, falls PersonTyp sich auf einen Lehrer bezieht");

	/** Die Definition der Tabellenspalte Bemerkung */
	public SchemaTabelleSpalte col_Bemerkung = add("Bemerkung", SchemaDatentypen.TEXT, false)
			.setNotNull()
			.setDefault("")
			.setJavaComment("Die Bemerkung des Eintrags für die Wiedervorlage");

	/** Die Definition der Tabellenspalte tsAngelegt */
	public SchemaTabelleSpalte col_tsAngelegt = add("tsAngelegt", SchemaDatentypen.DATETIME, false)
			.setJavaComment("Der Zeitpunkt, wann der Eintrag für die Wiedervorlage angelegt wurde");

	/** Die Definition der Tabellenspalte tsWiedervorlage */
	public SchemaTabelleSpalte col_tsWiedervorlage = add("tsWiedervorlage", SchemaDatentypen.DATETIME, false)
			.setJavaComment("Der Zeitpunkt, ab wann der Eintrag zur Wiedervorlage angezeigt werden soll");

	/** Die Definition der Tabellenspalte tsErledigt */
	public SchemaTabelleSpalte col_tsErledigt = add("tsErledigt", SchemaDatentypen.DATETIME, false)
			.setJavaComment("Der Zeitpunkt, wann der Eintrag als erledigt markiert wurde");

	/** Die Definition der Tabellenspalte Benutzer_ID */
	public SchemaTabelleSpalte col_Benutzer_ID = add("Benutzer_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die ID des Benutzers, welcher den Eintrag zur Wiedervorlage angelegt hat und dem er auch angezeigt wird.");

	/** Die Definition der Tabellenspalte Benutzer_ID_Erledigt */
	public SchemaTabelleSpalte col_Benutzer_ID_Erledigt = add("Benutzer_ID_Erledigt", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die ID des Benutzers, welcher den Eintrag erledigt hat");

	/** Die Definition der Tabellenspalte Benutzergruppe_ID */
	public SchemaTabelleSpalte col_Benutzergruppe_ID = add("Benutzergruppe_ID", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Die ID der Benutzergruppe, welcher der Eintrag für die Wiedervorlage zur Bearbeitung angezeigt werden soll oder null.");

	/** Die Definition der Tabellenspalte AutomatischErledigt */
	public SchemaTabelleSpalte col_AutomatischErledigt = add("AutomatischErledigt", SchemaDatentypen.INT, false)
			.setDefault("0")
			.setNotNull()
			.setConverter(Boolean01Converter.class)
			.setJavaComment(
					"Gibt an, dass der Eintrag automatisch als erledigt markiert werden soll, wenn er einem Benutzer nach dem Wiedervorlage-Zeitpunkt angezeigt wurde.");


	/** Die Definition des Fremdschlüssels Wiedervorlage_Lehrer_FK */
	public SchemaTabelleFremdschluessel fk_Wiedervorlage_Lehrer_FK = addForeignKey(
			"Wiedervorlage_Lehrer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Lehrer_ID, Schema.tab_K_Lehrer.col_ID)
	);

	/** Die Definition des Fremdschlüssels Wiedervorlage_Schueler_FK */
	public SchemaTabelleFremdschluessel fk_Wiedervorlage_Schueler_FK = addForeignKey(
			"Wiedervorlage_Schueler_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schueler_ID, Schema.tab_Schueler.col_ID)
	);

	/** Die Definition des Fremdschlüssels Wiedervorlage_Erzieher_FK */
	public SchemaTabelleFremdschluessel fk_Wiedervorlage_Erzieher_FK = addForeignKey(
			"Wiedervorlage_Erzieher_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Erzieher_ID, Schema.tab_SchuelerErzAdr.col_ID)
	);

	/** Die Definition des Fremdschlüssels Wiedervorlage_Benutzer_FK */
	public SchemaTabelleFremdschluessel fk_Wiedervorlage_Benutzer_FK = addForeignKey(
			"Wiedervorlage_Benutzer_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Benutzer_ID, Schema.tab_Benutzer.col_ID)
	);

	/** Die Definition des Fremdschlüssels Wiedervorlage_Benutzer_Erledigt_FK */
	public SchemaTabelleFremdschluessel fk_Wiedervorlage_Benutzer_Erledigt_FK = addForeignKey(
			"Wiedervorlage_Benutzer_Erledigt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Benutzer_ID_Erledigt, Schema.tab_Benutzer.col_ID)
	);

	/** Die Definition des Fremdschlüssels Wiedervorlage_Benutzergruppe_FK */
	public SchemaTabelleFremdschluessel fk_Wiedervorlage_Benutzergruppe_FK = addForeignKey(
			"Wiedervorlage_Benutzergruppe_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Benutzergruppe_ID, Schema.tab_Benutzergruppen.col_ID)
	);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Wiedervorlage.
	 */
	public Tabelle_Wiedervorlage() {
		super("Wiedervorlage", SchemaRevisionen.REV_21);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schule");
		setJavaClassName("DTOWiedervorlage");
		setJavaComment("Einträge zur Wiedervorlage");
	}

}
