package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Klassen.
 */
public class Tabelle_Klassen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID der Klasse in der Klassen- Versetzuungstabelle");

	/** Die Definition der Tabellenspalte Schuljahresabschnitts_ID */
	public SchemaTabelleSpalte col_Schuljahresabschnitts_ID = add("Schuljahresabschnitts_ID", SchemaDatentypen.BIGINT, false)
		.setNotNull()
		.setJavaComment("ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte");

	/** Die Definition der Tabellenspalte Bezeichnung */
	public SchemaTabelleSpalte col_Bezeichnung = add("Bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(150)
		.setJavaComment("Bezeichnender Text für die Klasse");

	/** Die Definition der Tabellenspalte ASDKlasse */
	public SchemaTabelleSpalte col_ASDKlasse = add("ASDKlasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaComment("ASD-Jahrgang der Klasse");

	/** Die Definition der Tabellenspalte Klasse */
	public SchemaTabelleSpalte col_Klasse = add("Klasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setNotNull()
		.setJavaComment("Kürzel der Klasse");

	/** Die Definition der Tabellenspalte Jahrgang_ID */
	public SchemaTabelleSpalte col_Jahrgang_ID = add("Jahrgang_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("ID des ASD-Jahrgangs");

	/** Die Definition der Tabellenspalte FKlasse */
	public SchemaTabelleSpalte col_FKlasse = add("FKlasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Folgeklasse");

	/** Die Definition der Tabellenspalte VKlasse */
	public SchemaTabelleSpalte col_VKlasse = add("VKlasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(15)
		.setJavaComment("Vorgängerklasse");

	/** Die Definition der Tabellenspalte OrgFormKrz */
	public SchemaTabelleSpalte col_OrgFormKrz = add("OrgFormKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Organisationsform der Klasse Kürzel IT.NRW");

	/** Die Definition der Tabellenspalte ASDSchulformNr */
	public SchemaTabelleSpalte col_ASDSchulformNr = add("ASDSchulformNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaComment("Schulgliederung in der Klasse");

	/** Die Definition der Tabellenspalte Fachklasse_ID */
	public SchemaTabelleSpalte col_Fachklasse_ID = add("Fachklasse_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("FID des Fachklasse nur BK SBK");

	/** Die Definition der Tabellenspalte PruefOrdnung */
	public SchemaTabelleSpalte col_PruefOrdnung = add("PruefOrdnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Prüfungsordnung für die Klasse");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an ob eine Klasse sichtbar ist");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Sortierungnummer der Klasse");

	/** Die Definition der Tabellenspalte Klassenart */
	public SchemaTabelleSpalte col_Klassenart = add("Klassenart", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Klassenart");

	/** Die Definition der Tabellenspalte SommerSem */
	public SchemaTabelleSpalte col_SommerSem = add("SommerSem", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Beginn im Sommersemester nur WBK");

	/** Die Definition der Tabellenspalte NotenGesperrt */
	public SchemaTabelleSpalte col_NotenGesperrt = add("NotenGesperrt", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Noteneingabe für die Klasse gesperrt");

	/** Die Definition der Tabellenspalte AdrMerkmal */
	public SchemaTabelleSpalte col_AdrMerkmal = add("AdrMerkmal", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("A")
		.setJavaComment("Adressmerkmal des Teilstandorts für die Klasse");

	/** Die Definition der Tabellenspalte KoopKlasse */
	public SchemaTabelleSpalte col_KoopKlasse = add("KoopKlasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob die Klasse eine KOOP-Klasse ist");

	/** Die Definition der Tabellenspalte Ankreuzzeugnisse */
	public SchemaTabelleSpalte col_Ankreuzzeugnisse = add("Ankreuzzeugnisse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("-")
		.setConverter(BooleanPlusMinusDefaultMinusConverter.class)
		.setJavaComment("Gibt an ob in der Klasse Ankreuzeugnisse (GS) oder Kompentenzschreiben (andere) verwendet werden");


	/** Die Definition des Fremdschlüssels Klassen_Schuljahresabschnitt_FK */
	public SchemaTabelleFremdschluessel fk_Klassen_Schuljahresabschnitt_FK = addForeignKey(
		"Klassen_Schuljahresabschnitt_FK",
		/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
		/* OnDelete: */ SchemaFremdschluesselAktionen.CASCADE,
		new Pair<>(col_Schuljahresabschnitts_ID, Schema.tab_Schuljahresabschnitte.col_ID)
	);

	/** Die Definition des Fremdschlüssels Klassen_Fachklasse_FK */
	public SchemaTabelleFremdschluessel fk_Klassen_Fachklasse_FK = addForeignKey(
			"Klassen_Fachklasse_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Fachklasse_ID, Schema.tab_EigeneSchule_Fachklassen.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);

	/** Die Definition des Fremdschlüssels Klassen_Jahrgang_FK */
	public SchemaTabelleFremdschluessel fk_Klassen_Jahrgang_FK = addForeignKey(
			"Klassen_Jahrgang_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_Jahrgang_ID, Schema.tab_EigeneSchule_Jahrgaenge.col_ID)
		)
		.setRevision(SchemaRevisionen.REV_2);


	/** Die Definition des Unique-Index Klassen_UC1 */
	public SchemaTabelleUniqueIndex unique_Klassen_UC1 = addUniqueIndex("Klassen_UC1",
			col_Schuljahresabschnitts_ID,
			col_Klasse
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Klassen.
	 */
	public Tabelle_Klassen() {
		super("Klassen", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.klassen");
		setJavaClassName("DTOKlassen");
		setJavaComment("Tabelle für die Schuljahresabschnitts-spezifische Klassen-/Versetzungstabelle");
	}

}
