package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schildintern_DQR_Niveaus.
 */
public class Tabelle_Schildintern_DQR_Niveaus extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Gliederung */
	public SchemaTabelleSpalte col_Gliederung = add("Gliederung", SchemaDatentypen.VARCHAR, true).setDatenlaenge(4)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: DQR-Niveau für Gliederung");

	/** Die Definition der Tabellenspalte FKS */
	public SchemaTabelleSpalte col_FKS = add("FKS", SchemaDatentypen.VARCHAR, true).setDatenlaenge(8)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: DQR-Niveau für die FAchklasse");

	/** Die Definition der Tabellenspalte DQR_Niveau */
	public SchemaTabelleSpalte col_DQR_Niveau = add("DQR_Niveau", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("Schildintern Tabelle: DQR-Niveau als Nummer");

	/** Die Definition der Tabellenspalte gueltigVon */
	public SchemaTabelleSpalte col_gueltigVon = add("gueltigVon", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig ab Schuljahr");

	/** Die Definition der Tabellenspalte gueltigBis */
	public SchemaTabelleSpalte col_gueltigBis = add("gueltigBis", SchemaDatentypen.INT, false)
		.setJavaComment("Schildintern Tabelle: Gültig bis Schuljahr");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schildintern_DQR_Niveaus.
	 */
	public Tabelle_Schildintern_DQR_Niveaus() {
		super("Schildintern_DQR_Niveaus", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.intern");
		setJavaClassName("DTOInternDQRNiveaus");
		setJavaComment("DQR Referenzniveaus für Fachklassen am BK (Zeugnisdruck)");
	}

}
