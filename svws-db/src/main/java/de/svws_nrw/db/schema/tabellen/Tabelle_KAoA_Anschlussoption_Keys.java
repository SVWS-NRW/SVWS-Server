package de.svws_nrw.db.schema.tabellen;

import java.util.Arrays;
import java.util.Collection;

import de.svws_nrw.asd.types.kaoa.KAOAAnschlussoptionen;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleCoreType;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle KAoA_Anschlussoption_Keys.
 */
public class Tabelle_KAoA_Anschlussoption_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte ID */
	public SchemaTabelleSpalte col_ID = add("ID", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die eindeutige ID der Anschlussoption");

	/**
	 * Erstellt die Schema-Defintion für die Tabelle KAoA_Anschlussoption_Keys.
	 */
	public Tabelle_KAoA_Anschlussoption_Keys() {
		super("KAoA_Anschlussoption_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schule");
		setJavaClassName("DTOKAoAAnschlussoptionKeys");
		setJavaComment("Gültige Schlüsselwerte für Fremdschlüssel zu den KAOA-Anschlussoptionen");
		setCoreType(new SchemaTabelleCoreType(this, KAOAAnschlussoptionen.class, KAOAAnschlussoptionen.data().getVersion(), rev -> Arrays
				.stream(KAOAAnschlussoptionen.values())
				.map(a -> a.historie().stream().map(h -> "" + h.id).toList()).flatMap(Collection::stream).toList()));
	}

}
