package de.nrw.schule.svws.db.schema.tabellen;

import java.util.Arrays;
import java.util.Collection;

import de.nrw.schule.svws.core.types.kaoa.KAOAZusatzmerkmal;
import de.nrw.schule.svws.db.schema.SchemaDatentypen;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleCoreType;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle KAoA_Zusatzmerkmal_Keys.
 */
public class Tabelle_KAoA_Zusatzmerkmal_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
		.setNotNull()
		.setJavaComment("Die eindeutige ID des Zusatzmerkmals");

	/**
	 * Erstellt die Schema-Defintion für die Tabelle KAoA_Zusatzmerkmal_Keys.
	 */
	public Tabelle_KAoA_Zusatzmerkmal_Keys() {
		super("KAoA_Zusatzmerkmal_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schule");
		setJavaClassName("DTOKAoAZusatzmerkmalKeys");
		setJavaComment("Gültige Schlüsselwerte für Fremdschlüssel zu den KAOA-Zusatzmerkmalen");
        setCoreType(new SchemaTabelleCoreType(this, KAOAZusatzmerkmal.class, KAOAZusatzmerkmal.VERSION, (rev) -> Arrays
            .stream(KAOAZusatzmerkmal.values())
            .map(a -> Arrays.stream(a.historie)
                .map(h -> "" + h.id)
                .toList()
            ).flatMap(Collection::stream).toList()));
	}

}
