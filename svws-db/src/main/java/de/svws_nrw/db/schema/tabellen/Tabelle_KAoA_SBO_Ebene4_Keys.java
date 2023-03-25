package de.svws_nrw.db.schema.tabellen;

import java.util.Arrays;
import java.util.Collection;

import de.svws_nrw.core.types.kaoa.KAOAEbene4;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleCoreType;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle KAoA_SBO_Ebene4_Keys.
 */
public class Tabelle_KAoA_SBO_Ebene4_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Merkmals der SBO-Ebene 4");

	/**
	 * Erstellt die Schema-Defintion für die Tabelle KAoA_SBO_Ebene4_Keys.
	 */
	public Tabelle_KAoA_SBO_Ebene4_Keys() {
		super("KAoA_SBO_Ebene4_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schule");
		setJavaClassName("DTOKAoASBOEB4Keys");
		setJavaComment("Gültige Schlüsselwerte für Fremdschlüssel zu den KAOA-Merkmalen der SBO-Ebene 4");
        setCoreType(new SchemaTabelleCoreType(this, KAOAEbene4.class, KAOAEbene4.VERSION, (rev) -> Arrays
                .stream(KAOAEbene4.values())
                .map(a -> Arrays.stream(a.historie)
                    .map(h -> "" + h.id)
                    .toList()
                ).flatMap(Collection::stream).toList()));
	}

}
