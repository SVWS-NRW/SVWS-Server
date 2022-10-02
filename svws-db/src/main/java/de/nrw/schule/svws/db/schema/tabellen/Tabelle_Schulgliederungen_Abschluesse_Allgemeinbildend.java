package de.nrw.schule.svws.db.schema.tabellen;

import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schulgliederungen_Abschluesse_Allgemeinbildend.
 */
public class Tabelle_Schulgliederungen_Abschluesse_Allgemeinbildend extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Schulgliederung_ID */
	public SchemaTabelleSpalte col_Schulgliederung_ID = add("Schulgliederung_ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("die ID der Schulgliederung");

	/** Die Definition der Tabellenspalte Abschluss_Kuerzel */
	public SchemaTabelleSpalte col_Abschluss_Kuerzel = add("Abschluss_Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
		.setNotNull()
		.setJavaComment("die ID der Art des allgemeinbildenden Abschlusses");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schulgliederungen_Abschluesse_Allgemeinbildend.
	 */
	public Tabelle_Schulgliederungen_Abschluesse_Allgemeinbildend() {
		super("Schulgliederungen_Abschluesse_Allgemeinbildend", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOAlleSchulgliederungenAbschluesseAllgmeinbildend");
		setJavaComment("Tabelle mit der Zuordnungen der erlaubten allgmeinbildenden Abschlüsse bei Schulgliederungen");
	}

}
