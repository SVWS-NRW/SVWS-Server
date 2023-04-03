package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Abteilungen.
 */
public class Tabelle_EigeneSchule_Abteilungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Abteilung");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setNotNull()
		.setJavaComment("Text für die Bezeichnung der Abteilung");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setDefault("-1")
		.setNotNull()
		.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte AbteilungsLeiter_ID */
	public SchemaTabelleSpalte col_AbteilungsLeiter_ID = add("AbteilungsLeiter_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Lehrer-ID für den Abteilungsleiter");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("steuert die Sichtbarkeit der Abteilung");

	/** Die Definition der Tabellenspalte Raum */
	public SchemaTabelleSpalte col_Raum = add("Raum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Bezeichnung für den Raum des Abteilungsleiter z.B. für Briefköpfe");

	/** Die Definition der Tabellenspalte Email */
	public SchemaTabelleSpalte col_Email = add("Email", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Email des Abteilungsleiters");

	/** Die Definition der Tabellenspalte Durchwahl */
	public SchemaTabelleSpalte col_Durchwahl = add("Durchwahl", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Telefonische Durchwahl des Abteilungsleiters");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Sortierung des Datensatzes");

	/** Die Definition der Tabellenspalte AbteilungsLeiter */
	public SchemaTabelleSpalte col_AbteilungsLeiter = add("AbteilungsLeiter", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Lehrer-Kürzel für den Abteilungsleiter");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("DEPRECATED: Schulnummer zu der der Datensatz gehört");


	/** Die Definition des Fremdschlüssels EigeneSchule_Abteilungen_Leiter_FK */
	public SchemaTabelleFremdschluessel fk_EigeneSchule_Abteilungen_Leiter_FK = addForeignKey(
			"EigeneSchule_Abteilungen_Leiter_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_AbteilungsLeiter_ID, Schema.tab_K_Lehrer.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels EigeneSchule_Abteilungen_Schuljahreabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_EigeneSchule_Abteilungen_Schuljahreabschnitt_FK = addForeignKey(
			"EigeneSchule_Abteilungen_Schuljahreabschnitt_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
			new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Abteilungen.
	 */
	public Tabelle_EigeneSchule_Abteilungen() {
		super("EigeneSchule_Abteilungen", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAbteilungen");
		setJavaComment("Katalog der Abteilungen, denen dann ein Abteilungsleiter zugewiesen werden kann. Die Tabelle wird schuljahresspezifisch verwaltet.  ");
	}

}
