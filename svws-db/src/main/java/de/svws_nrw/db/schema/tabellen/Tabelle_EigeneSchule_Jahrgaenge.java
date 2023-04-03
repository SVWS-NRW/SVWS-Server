package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.statkue.SchulgliederungKuerzelConverter;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaFremdschluesselAktionen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Jahrgaenge.
 */
public class Tabelle_EigeneSchule_Jahrgaenge extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Eindeutige ID zur Kennzeichnung des Jahrgangs-Datensatzes");

	/** Die Definition der Tabellenspalte InternKrz */
	public SchemaTabelleSpalte col_InternKrz = add("InternKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(20)
		.setJavaComment("Ein Kürzel für den Jahrgang, welches bei der Darstellung genutzt wird");

	/** Die Definition der Tabellenspalte GueltigVon */
	public SchemaTabelleSpalte col_GueltigVon = add("GueltigVon", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), NULL bedeutet von dem ersten Abschnitt an");

	/** Die Definition der Tabellenspalte GueltigBis */
	public SchemaTabelleSpalte col_GueltigBis = add("GueltigBis", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), NULL bedeutet bis zum letzten Abschnitt, Ende offen");

	/** Die Definition der Tabellenspalte ASDJahrgang */
	public SchemaTabelleSpalte col_ASDJahrgang = add("ASDJahrgang", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Ein Kürzel für den Jahrgang, welches bei der Statistik verwendet wird");

	/** Die Definition der Tabellenspalte ASDBezeichnung */
	public SchemaTabelleSpalte col_ASDBezeichnung = add("ASDBezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Die Bezeichnung für den Jahrgang, welche bei der Statistik verwendet wird");

	/** Die Definition der Tabellenspalte Sichtbar */
	public SchemaTabelleSpalte col_Sichtbar = add("Sichtbar", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("true, falls der Jahrgang bei Auswahlen angezeigt werden soll oder nicht.");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setDefault("32000")
		.setJavaComment("Ein Zahlwert, welcher bei für eine Sortierung der Jahrgänge bei der Darstellung verwendet wird.");

	/** Die Definition der Tabellenspalte IstChronologisch */
	public SchemaTabelleSpalte col_IstChronologisch = add("IstChronologisch", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Gibt an ob ein Jahrgang zu einer chronologischen Reihenfolge gehört");

	/** Die Definition der Tabellenspalte Spaltentitel */
	public SchemaTabelleSpalte col_Spaltentitel = add("Spaltentitel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaName("Kurzbezeichnung")
		.setJavaComment("Wird in der Übersicht benutzt um den Spaltentitel anzuzeigen. (Kurzbezeichnung in Jahrgangstabelle)");

	/** Die Definition der Tabellenspalte SekStufe */
	public SchemaTabelleSpalte col_SekStufe = add("SekStufe", SchemaDatentypen.VARCHAR, false).setDatenlaenge(6)
		.setJavaName("Sekundarstufe")
		.setJavaComment("Gibt die Primar- bzw. Sekundarstufe des Jahrgang an (Pr, SI, SII-1, SII-2, SII-3)");

	/** Die Definition der Tabellenspalte SGL */
	public SchemaTabelleSpalte col_SGL = add("SGL", SchemaDatentypen.VARCHAR, false).setDatenlaenge(3)
		.setJavaName("Gliederung")
		.setConverter(SchulgliederungKuerzelConverter.class)
		.setConverterRevision(SchemaRevisionen.REV_1)
		.setJavaComment("Schulgliederung des Jahrgangs");

	/** Die Definition der Tabellenspalte Restabschnitte */
	public SchemaTabelleSpalte col_Restabschnitte = add("Restabschnitte", SchemaDatentypen.INT, false)
		.setJavaName("AnzahlRestabschnitte")
		.setJavaComment("Gibt die Anzahl der Restabschnitte an, die für den Verbleib an dieser Schulform üblich ist.");

	/** Die Definition der Tabellenspalte Folgejahrgang_ID */
	public SchemaTabelleSpalte col_Folgejahrgang_ID = add("Folgejahrgang_ID", SchemaDatentypen.BIGINT, false)
		.setJavaComment("Die eindeutige ID des Jahrgangs, welcher auf diesen folgt – verweist auf diese Tabelle oder NULL, falls es sich um den letzten Jahrgang handelt");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/** Die Definition des Fremdschlüssels EigeneSchule_Jahrgaenge_GueltigVon_FK */
	public SchemaTabelleFremdschluessel fk_EigeneSchule_Jahrgaenge_GueltigVon_FK = addForeignKey(
			"EigeneSchule_Jahrgaenge_GueltigVon_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_GueltigVon, Schema.tab_Schuljahresabschnitte.col_ID)
		);

	/** Die Definition des Fremdschlüssels EigeneSchule_Jahrgaenge_GueltigBis_FK */
	public SchemaTabelleFremdschluessel fk_EigeneSchule_Jahrgaenge_GueltigBis_FK = addForeignKey(
			"EigeneSchule_Jahrgaenge_GueltigBis_FK",
			/* OnUpdate: */ SchemaFremdschluesselAktionen.CASCADE,
			/* OnDelete: */ SchemaFremdschluesselAktionen.SET_NULL,
			new Pair<>(col_GueltigBis, Schema.tab_Schuljahresabschnitte.col_ID)
		);


	/** Die Definition des Unique-Index EigeneSchule_Jahrgaenge_UC1 */
	public SchemaTabelleUniqueIndex unique_EigeneSchule_Jahrgaenge_UC1 = addUniqueIndex("EigeneSchule_Jahrgaenge_UC1",
			col_InternKrz
		);


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Jahrgaenge.
	 */
	public Tabelle_EigeneSchule_Jahrgaenge() {
		super("EigeneSchule_Jahrgaenge", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOJahrgang");
		setJavaComment("vorhandene Jahrgänge mit deren ASD-Zuordnungen");
	}

}
