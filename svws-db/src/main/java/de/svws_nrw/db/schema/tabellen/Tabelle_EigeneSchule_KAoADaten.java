package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_KAoADaten.
 */
public class Tabelle_EigeneSchule_KAoADaten extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des KAoA-Dateneintrags für die Schule");

	/** Die Definition der Tabellenspalte Curriculum */
	public SchemaTabelleSpalte col_Curriculum = add("Curriculum", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setNotNull()
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("KAOA Curriculumsangaben");

	/** Die Definition der Tabellenspalte Koordinator */
	public SchemaTabelleSpalte col_Koordinator = add("Koordinator", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setNotNull()
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("KAOA Koordinator");

	/** Die Definition der Tabellenspalte Berufsorientierungsbuero */
	public SchemaTabelleSpalte col_Berufsorientierungsbuero = add("Berufsorientierungsbuero", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setNotNull()
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Schule hat Beruforientierungsbüro");

	/** Die Definition der Tabellenspalte KooperationsvereinbarungAA */
	public SchemaTabelleSpalte col_KooperationsvereinbarungAA = add("KooperationsvereinbarungAA", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setNotNull()
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Kooperationsvereinbarung KAOA geschlossen");

	/** Die Definition der Tabellenspalte NutzungReflexionsworkshop */
	public SchemaTabelleSpalte col_NutzungReflexionsworkshop = add("NutzungReflexionsworkshop", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setNotNull()
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Reflexionsworkshops werden genutzt");

	/** Die Definition der Tabellenspalte NutzungEntscheidungskompetenzI */
	public SchemaTabelleSpalte col_NutzungEntscheidungskompetenzI = add("NutzungEntscheidungskompetenzI", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setNotNull()
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Nutzung der Entscheidungskompetenzen 1");

	/** Die Definition der Tabellenspalte NutzungEntscheidungskompetenzII */
	public SchemaTabelleSpalte col_NutzungEntscheidungskompetenzII = add("NutzungEntscheidungskompetenzII", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setNotNull()
		.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
		.setJavaComment("Nutzung der Entscheidungskompetenzen 2");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_KAoADaten.
	 */
	public Tabelle_EigeneSchule_KAoADaten() {
		super("EigeneSchule_KAoADaten", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOEigeneSchuleKAoADaten");
		setJavaComment("TODO: Tabelle zur Eintragung, welche KAOA-Merkmale an der Schule genutzt wreden");
	}

}
