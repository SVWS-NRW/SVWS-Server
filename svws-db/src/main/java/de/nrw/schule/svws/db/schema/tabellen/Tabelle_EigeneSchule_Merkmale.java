package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Merkmale.
 */
public class Tabelle_EigeneSchule_Merkmale extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("ID des Merkmals das an der Schule vorhanden ist");

	/** Die Definition der Tabellenspalte Schule */
	public SchemaTabelleSpalte col_Schule = add("Schule", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Merkmal kann der Schule zugewiesen werden");

	/** Die Definition der Tabellenspalte Schueler */
	public SchemaTabelleSpalte col_Schueler = add("Schueler", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setDefault("+")
		.setConverter("BooleanPlusMinusDefaultPlusConverter")
		.setJavaComment("Merkmal kann auch einem einzelnen Schüler auf Individualdaten II zugewiesen werden");

	/** Die Definition der Tabellenspalte Kurztext */
	public SchemaTabelleSpalte col_Kurztext = add("Kurztext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Kurstext des Merkmals zB OGS");

	/** Die Definition der Tabellenspalte Langtext */
	public SchemaTabelleSpalte col_Langtext = add("Langtext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
		.setJavaComment("Langtext des Merkmal zB offener Ganztag");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Merkmale.
	 */
	public Tabelle_EigeneSchule_Merkmale() {
		super("EigeneSchule_Merkmale", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOMerkmale");
		setJavaComment("Katalog der Merkmale (Schule bearbeiten > Merkmale), die dem Schüler dann auf Individualdaten zugewiesen werden können (IT.NRW-Statistik)");
	}

}
