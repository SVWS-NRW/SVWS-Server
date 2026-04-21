package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Merkmale.
 */
public class Tabelle_EigeneSchule_Merkmale extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public final SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaName("id")
			.setJavaComment("ID des Merkmals das an der Schule vorhanden ist");

	/** Die Definition der Tabellenspalte Schule */
	public final SchemaTabelleSpalte col_Schule = add("Schule", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("+")
			.setJavaName("istSchulmerkmal")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Merkmal kann der Schule zugewiesen werden");

	/** Die Definition der Tabellenspalte Schueler */
	public final SchemaTabelleSpalte col_Schueler = add("Schueler", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
			.setDefault("+")
			.setJavaName("istSchuelermerkmal")
			.setConverter(BooleanPlusMinusDefaultPlusConverter.class)
			.setJavaComment("Merkmal kann auch einem einzelnen Schüler auf Individualdaten II zugewiesen werden");

	/** Die Definition der Tabellenspalte Kurztext */
	public final SchemaTabelleSpalte col_Kurztext = add("Kurztext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
			.setJavaName("kuerzel")
			.setJavaComment("Kurztext des Merkmals zB OGS");

	/** Die Definition der Tabellenspalte Langtext */
	public final SchemaTabelleSpalte col_Langtext = add("Langtext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(100)
			.setJavaName("bezeichnung")
			.setJavaComment("Langtext des Merkmal zB offener Ganztag");

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public final SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
			.setVeraltet(SchemaRevisionen.REV_1)
			.setJavaComment(
					"Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");


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
		setJavaComment("Katalog der Merkmale (Schule bearbeiten > Merkmale), die dem Schüler dann auf Individualdaten zugewiesen werden können"
				+ " (IT.NRW-Statistik)");
	}

}
