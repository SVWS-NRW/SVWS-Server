package de.nrw.schule.svws.db.schema.tabellen;

import java.util.Arrays;
import java.util.Collection;

import de.nrw.schule.svws.core.types.kaoa.KAOAKategorie;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleCoreType;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle KAoA_Kategorie_Keys.
 */
public class Tabelle_KAoA_Kategorie_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID der Kategorie");

	/**
	 * Erstellt die Schema-Defintion für die Tabelle KAoA_Kategorie_Keys.
	 */
	public Tabelle_KAoA_Kategorie_Keys() {
		super("KAoA_Kategorie_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schule");
		setJavaClassName("DTOKAoAKategorieKeys");
		setJavaComment("Gültige Schlüsselwerte für Fremdschlüssel zu den KAOA-Kategorien");
        setCoreType(new SchemaTabelleCoreType(this, KAOAKategorie.class, KAOAKategorie.VERSION, (rev) -> Arrays
            .stream(KAOAKategorie.values())
            .map(a -> Arrays.stream(a.historie)
                .map(h -> "" + h.id)
                .toList()
            ).flatMap(Collection::stream).toList()));
	}

}
