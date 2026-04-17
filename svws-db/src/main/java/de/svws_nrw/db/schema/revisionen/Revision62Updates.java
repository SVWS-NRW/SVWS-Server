package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 62.
 */
public class Revision62Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 62.
	 */
	public Revision62Updates() {
		super(SchemaRevisionen.REV_62);
		migrateSchullogoInLogoTabelle();
	}

	private void migrateSchullogoInLogoTabelle() {
		add("Migriert das Base64-kodierte Schullogo aus EigeneSchule_Logo in die neue Tabelle Logo",
				"""
				INSERT INTO %s (kennung, logoBase64, hinzugefuegtAm)
				SELECT 'SCHULLOGO_SCHILD', LogoBase64, CURDATE()
				FROM %s
				WHERE (LogoBase64 IS NOT NULL)
				    AND (LogoBase64 <> '')
				    AND NOT EXISTS (
				        SELECT 1 FROM %s WHERE (kennung = 'SCHULLOGO_SCHILD')
				    );
				"""
						.formatted(
								Schema.tab_Logo.name(),
								Schema.tab_EigeneSchule_Logo.name(),
								Schema.tab_Logo.name()
						),
				Schema.tab_Logo, Schema.tab_EigeneSchule_Logo
		);
	}

}
