package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_PrfSemAbschl.
 */
public class Tabelle_Schildintern_PrfSemAbschl extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Nr */
	public SchemaTabelleSpalte col_Nr = add("Nr", SchemaDatentypen.VARCHAR, true).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Nummer des Versetzungsvermerk");

	/** Die Definition der Tabellenspalte Klartext */
	public SchemaTabelleSpalte col_Klartext = add("Klartext", SchemaDatentypen.VARCHAR, false).setDatenlaenge(50)
		.setJavaComment("Schildintern Tabelle: Klartext des Versetzungsvermerk");

	/** Die Definition der Tabellenspalte StatistikKrz */
	public SchemaTabelleSpalte col_StatistikKrz = add("StatistikKrz", SchemaDatentypen.VARCHAR, false).setDatenlaenge(1)
		.setJavaComment("Schildintern Tabelle: Statistikkürzel des Versetzungsvermerk (DEPRECATED)");

	/** Die Definition der Tabellenspalte Sortierung */
	public SchemaTabelleSpalte col_Sortierung = add("Sortierung", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Sortierung des Versetzungsvermerk");

	/** Die Definition der Tabellenspalte Schulform */
	public SchemaTabelleSpalte col_Schulform = add("Schulform", SchemaDatentypen.VARCHAR, true).setDatenlaenge(2)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: Schulform des Versetzungsvermerk");

	/** Die Definition der Tabellenspalte StatistikKrzNeu */
	public SchemaTabelleSpalte col_StatistikKrzNeu = add("StatistikKrzNeu", SchemaDatentypen.VARCHAR, false).setDatenlaenge(2)
		.setJavaComment("Schildintern Tabelle: Neue Statistikkürzel  des Versetzungsvermerk");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig ab Schuljahr");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig bis Schuljahr");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_PrfSemAbschl.
	 */
	public Tabelle_Schildintern_PrfSemAbschl() {
		super("Schildintern_PrfSemAbschl", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternPrfSemesterAbschluss");
		setJavaComment("Liste der Versetzungsvermerke Schulformbezogen");
	}

}
