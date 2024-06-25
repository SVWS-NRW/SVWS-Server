package de.svws_nrw.db.schema.tabellen;

import java.util.Arrays;
import java.util.Collection;

import de.svws_nrw.core.types.kaoa.KAOAMerkmal;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleCoreType;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle KAoA_Merkmal_Keys.
 */
public class Tabelle_KAoA_Merkmal_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die eindeutige ID des Merkmals");

	/**
	 * Erstellt die Schema-Defintion für die Tabelle KAoA_Merkmal_Keys.
	 */
	public Tabelle_KAoA_Merkmal_Keys() {
		super("KAoA_Merkmal_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schule");
		setJavaClassName("DTOKAoAMerkmalKeys");
		setJavaComment("Gültige Schlüsselwerte für Fremdschlüssel zu den KAOA-Merkmalen");
		setCoreType(new SchemaTabelleCoreType(this, KAOAMerkmal.class, KAOAMerkmal.VERSION, rev -> Arrays
				.stream(KAOAMerkmal.values())
				.map(a -> Arrays.stream(a.historie)
						.map(h -> "" + h.id)
						.toList()
				).flatMap(Collection::stream).toList()));
	}

}
