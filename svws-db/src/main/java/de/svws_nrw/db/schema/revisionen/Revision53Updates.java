package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 53.
 */
public final class Revision53Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 53.
	 */
	public Revision53Updates() {
		super(SchemaRevisionen.REV_53);
		add("Notenmodul_Credentials: Verschieben der Credentials für das Notenmodul von Tabelle LehrerNotenmodulCredentials zu Tabelle Notenmodul_Credentials.",
				"""
				INSERT INTO Notenmodul_Credentials(idLehrer, initialkennwort, passwordHash)
				SELECT
				    Lehrer_ID AS idLehrer,
				    Initialkennwort AS initialkennwort,
				    PasswordHash AS passwordHash
				FROM LehrerNotenmodulCredentials
				""",
				Schema.tab_LehrerNotenmodulCredentials, Schema.tab_Notenmodul_Credentials
		);
		add("Notenmodul_Verbindungen: Kopieren der OAuth2-Verbindunginformationen für die Notenmodul-Server aus SchuleOAuthSecrets.",
				"""
				INSERT INTO Notenmodul_Verbindungen(id, url, clientID, clientSecret, tokenTimestamp, tokenExpiresIn, token, serverTLSCert, serverTLSCertIsKnown, serverTLSCertIsTrusted)
				SELECT
				    ID AS id,
				    AuthServer AS url,
				    ClientID AS clientID,
				    ClientSecret AS clientSecret,
				    TokenTimestamp AS tokenTimestamp,
				    TokenExpiresIn AS tokenExpiresIn,
				    Token AS token,
				    TLSCert AS serverTLSCert,
				    TLSCertIsKnown AS serverTLSCertIsKnown,
				    TLSCertIsTrusted AS serverTLSCertIsTrusted
				FROM SchuleOAuthSecrets
				WHERE ID = 1
				""",
				Schema.tab_Notenmodul_Verbindungen, Schema.tab_SchuleOAuthSecrets
		);
		add("Notenmodul_Verbindungen: Entfernen der OAuth2-Verbindunginformationen für die Notenmodul-Server aus der SchuleOAuthSecrets.",
				"""
				DELETE FROM SchuleOAuthSecrets WHERE ID = 1
				""",
				Schema.tab_SchuleOAuthSecrets
		);
	}

}
