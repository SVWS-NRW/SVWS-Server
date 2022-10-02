package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EigeneSchule_Teilstandorte.
 */
public class Tabelle_EigeneSchule_Teilstandorte extends SchemaTabelle {

	/** Die Definition der Tabellenspalte SchulnrEigner */
	public SchemaTabelleSpalte col_SchulnrEigner = add("SchulnrEigner", SchemaDatentypen.INT, false)
		.setNotNull()
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden");

	/** Die Definition der Tabellenspalte AdrMerkmal */
	public SchemaTabelleSpalte col_AdrMerkmal = add("AdrMerkmal", SchemaDatentypen.VARCHAR, true).setDatenlaenge(1)
		.setNotNull()
		.setJavaComment("Adressmerkmal des Teilstandortes (A...Z)");

	/** Die Definition der Tabellenspalte PLZ */
	public SchemaTabelleSpalte col_PLZ = add("PLZ", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Postleitzahl des Teilstandortes");

	/** Die Definition der Tabellenspalte Ort */
	public SchemaTabelleSpalte col_Ort = add("Ort", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Ort des Teilstandortes");

	/** Die Definition der Tabellenspalte Strasse */
	public SchemaTabelleSpalte col_Strasse = add("Strasse", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setVeraltet(SchemaRevisionen.REV_1)
		.setJavaComment("Straße des Teilstandortes");

	/** Die Definition der Tabellenspalte Strassenname */
	public SchemaTabelleSpalte col_Strassenname = add("Strassenname", SchemaDatentypen.VARCHAR, false).setDatenlaenge(55)
		.setJavaComment("Straßenname des Teilstandortes");

	/** Die Definition der Tabellenspalte HausNr */
	public SchemaTabelleSpalte col_HausNr = add("HausNr", SchemaDatentypen.VARCHAR, false).setDatenlaenge(10)
		.setJavaComment("Hausnummer des Teilstandortes");

	/** Die Definition der Tabellenspalte HausNrZusatz */
	public SchemaTabelleSpalte col_HausNrZusatz = add("HausNrZusatz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Hausnummerzusatz des Teilstandortes");

	/** Die Definition der Tabellenspalte Bemerkung */
	public SchemaTabelleSpalte col_Bemerkung = add("Bemerkung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Bemerkung zum Teilstandort (Text)");

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, false).setDatenlaenge(30)
		.setJavaComment("Kürzel des Teilstandortes");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EigeneSchule_Teilstandorte.
	 */
	public Tabelle_EigeneSchule_Teilstandorte() {
		super("EigeneSchule_Teilstandorte", SchemaRevisionen.REV_0);
		setMigrate(true);
		setImportExport(true);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOTeilstandorte");
		setJavaComment("Liste der Teilstandorte Schule bearbeiten > Teilstandorte > (It.NRW benutzt dieses Merkmal zu Bildung von Teilklassen in der sim.txt)");
	}

}
