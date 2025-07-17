package de.svws_nrw.db.schema.tabellen;

import java.util.Arrays;
import java.util.Collection;

import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleCoreType;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle EinschulungsartKatalog_Keys.
 */
public class Tabelle_EinschulungsartKatalog_Keys extends SchemaTabelle {

	/** Die Definition der Tabellenspalte Kuerzel */
	public SchemaTabelleSpalte col_Kuerzel = add("Kuerzel", SchemaDatentypen.VARCHAR, true).setDatenlaenge(10)
			.setNotNull()
			.setJavaComment("Das Kürzel der Einschulungsart");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle EinschulungsartKatalog_Keys.
	 */
	public Tabelle_EinschulungsartKatalog_Keys() {
		super("EinschulungsartKatalog_Keys", SchemaRevisionen.REV_0);
		setMigrate(false);
		setImportExport(false);
		setJavaSubPackage("schild.schule");
		setJavaClassName("DTOEinschulungsartenKatalogKeys");
		setJavaComment("Gültige Schlüsselwerte für Fremdschlüssel zu den Einschulungsarten");
		setCoreType(new SchemaTabelleCoreType(this, Einschulungsart.class, Einschulungsart.data().getVersion(),
				rev -> Arrays.stream(Einschulungsart.values())
					.map(s -> s.historie().stream().map(h -> "" + h.schluessel).toList()).flatMap(Collection::stream).distinct().toList()));
	}

}
