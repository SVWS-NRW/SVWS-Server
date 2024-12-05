package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 30.
 */
public class Revision30Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 30.
	 */
	public Revision30Updates() {
		super(SchemaRevisionen.REV_30);
		add("GGf. Entfernen von nicht mehr genutzten Credentials in %s.".formatted(Schema.tab_Credentials.name()),
				"""
				DELETE FROM %1$s WHERE ID NOT IN (SELECT %2$s FROM %3$s WHERE %2$s IS NOT NULL UNION SELECT %4$s FROM %5$s WHERE %4$s IS NOT NULL UNION
				SELECT %6$s FROM %7$s WHERE %6$s IS NOT NULL UNION SELECT %8$s FROM %9$s WHERE %8$s IS NOT NULL)
				""".formatted(Schema.tab_Credentials.name(), Schema.tab_BenutzerAllgemein.col_CredentialID.name(), Schema.tab_BenutzerAllgemein.name(),
						Schema.tab_K_Lehrer.col_CredentialID.name(), Schema.tab_K_Lehrer.name(),
						Schema.tab_Schueler.col_CredentialID.name(), Schema.tab_Schueler.name(),
						Schema.tab_SchuelerErzAdr.col_CredentialID.name(), Schema.tab_SchuelerErzAdr.name()),
				Schema.tab_Credentials, Schema.tab_BenutzerAllgemein, Schema.tab_K_Lehrer, Schema.tab_Schueler, Schema.tab_SchuelerErzAdr
		);
		add("Entfernen von Einträgen in der Tabelle %s, die sich auf doppelte Benutzernamen beziehen.".formatted(Schema.tab_Benutzer.name()),
				"""
				DELETE FROM %1$s WHERE %5$s IN (SELECT %6$s FROM %7$s WHERE %8$s IN (SELECT c1.%2$s FROM %3$s c1 JOIN %3$s c2 ON LOWER(c1.%4$s) = LOWER(c2.%4$s) AND c1.%2$s > c2.%2$s))
				OR %9$s IN (SELECT %10$s FROM %11$s WHERE %12$s IN (SELECT c1.%2$s FROM %3$s c1 JOIN %3$s c2 ON LOWER(c1.%4$s) = LOWER(c2.%4$s) AND c1.%2$s > c2.%2$s))
				OR %13$s IN (SELECT %14$s FROM %15$s WHERE %16$s IN (SELECT c1.%2$s FROM %3$s c1 JOIN %3$s c2 ON LOWER(c1.%4$s) = LOWER(c2.%4$s) AND c1.%2$s > c2.%2$s))
				OR %17$s IN (SELECT %18$s FROM %19$s WHERE %20$s IN (SELECT c1.%2$s FROM %3$s c1 JOIN %3$s c2 ON LOWER(c1.%4$s) = LOWER(c2.%4$s) AND c1.%2$s > c2.%2$s))
				""".formatted(Schema.tab_Benutzer.name(), Schema.tab_Credentials.col_ID.name(), Schema.tab_Credentials.name(),
						Schema.tab_Credentials.col_Benutzername.name(), Schema.tab_Benutzer.col_Allgemein_ID.name(), Schema.tab_BenutzerAllgemein.col_ID.name(),
						Schema.tab_BenutzerAllgemein.name(), Schema.tab_BenutzerAllgemein.col_CredentialID.name(), Schema.tab_Benutzer.col_Lehrer_ID.name(),
						Schema.tab_K_Lehrer.col_ID.name(), Schema.tab_K_Lehrer.name(), Schema.tab_K_Lehrer.col_CredentialID.name(),
						Schema.tab_Benutzer.col_Schueler_ID.name(), Schema.tab_Schueler.col_ID.name(), Schema.tab_Schueler.name(),
						Schema.tab_Schueler.col_CredentialID.name(), Schema.tab_Benutzer.col_Erzieher_ID.name(), Schema.tab_SchuelerErzAdr.col_ID.name(),
						Schema.tab_SchuelerErzAdr.name(), Schema.tab_SchuelerErzAdr.col_CredentialID.name()),
				Schema.tab_Benutzer, Schema.tab_Credentials, Schema.tab_BenutzerAllgemein, Schema.tab_K_Lehrer, Schema.tab_Schueler, Schema.tab_SchuelerErzAdr
		);
		add("Entfernen von Einträgen in der Tabelle %s, die sich auf doppelte Benutzernamen beziehen.".formatted(Schema.tab_BenutzerAllgemein.name()),
				"DELETE FROM %1$s WHERE %2$s IN (SELECT c1.%3$s FROM %4$s c1 JOIN %4$s c2 ON LOWER(c1.%5$s) = LOWER(c2.%5$s) AND c1.%3$s > c2.%3$s)".formatted(
						Schema.tab_BenutzerAllgemein.name(), Schema.tab_BenutzerAllgemein.col_CredentialID.name(),
						Schema.tab_Credentials.col_ID.name(), Schema.tab_Credentials.name(), Schema.tab_Credentials.col_Benutzername.name()),
				Schema.tab_Credentials, Schema.tab_BenutzerAllgemein
		);
		add("Entfernen von Einträgen in der Tabelle %s, die doppelte Benutzernamen haben.".formatted(Schema.tab_Credentials.name()),
				"DELETE FROM %1$s WHERE %2$s IN (SELECT c1.%2$s FROM %1$s c1 JOIN %1$s c2 ON LOWER(c1.%3$s) = LOWER(c2.%3$s) AND c1.%2$s > c2.%2$s)".formatted(
						Schema.tab_Credentials.name(), Schema.tab_Credentials.col_ID.name(), Schema.tab_Credentials.col_Benutzername.name()),
				Schema.tab_Credentials
		);
	}

}
