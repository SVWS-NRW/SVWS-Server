package de.nrw.schule.svws.db.schema.tabellen;

import java.util.Arrays;

import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleCoreType;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle SchuelerStatus_Keys.
 */
public class Tabelle_SchuelerStatus_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.INT, true)
		.setNotNull()
		.setJavaComment("ID des Schüler-Status");



	/**
	 * Erstellt die Schema-Defintion für die Tabelle SchuelerStatus.
	 */
	public Tabelle_SchuelerStatus_Keys() {
		super("SchuelerStatus_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schueler");
		setJavaClassName("DTOSchuelerStatus");
		setJavaComment("Tabelle für die Schlüsselwerte des Core-Types SchuelerStatus");
        setCoreType(new SchemaTabelleCoreType(this, SchuelerStatus.class, SchuelerStatus.VERSION, (rev) ->
            Arrays.stream(SchuelerStatus.values()).map(s -> "" + s.id).toList()));
	}

}
