package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 61.
 */
public class Revision61Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 61.
	 */
	public Revision61Updates() {
		super(SchemaRevisionen.REV_61);
		korrigiereKlassenZuordnung();
	}

	private void korrigiereKlassenZuordnung() {
		add("Korrigiere ggf. fehlerhafter Klassenzuordnungen bei Schüler-Lernabschnitten",
				"""
				UPDATE %1$s sla
				SET sla.Klassen_ID = (SELECT kn.ID FROM %2$s ka JOIN %2$s kn ON ka.ID = sla.Klassen_ID AND ka.Klasse = kn.Klasse AND kn.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID)
				WHERE sla.Klassen_ID NOT IN (SELECT ID FROM %2$s k WHERE k.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID);
				"""
				.formatted(Schema.tab_SchuelerLernabschnittsdaten.name(), Schema.tab_Klassen.name()),
				Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_Klassen
		);
		add("Ergänze ggf fehlende Einträge in ",
				"""
				INSERT INTO %2$s (idLehrer, tsPasswordHash, tsArt2FA, tsTotpSecret, tsIstErstanmeldung)
				SELECT nc.idLehrer, CURTIME(3), CURTIME(3), CURTIME(3), CURTIME(3)
				FROM %1$s AS nc
				LEFT JOIN %2$s AS tsnc ON nc.idLehrer = tsnc.idLehrer
				WHERE tsnc.idLehrer IS NULL;
				"""
				.formatted(Schema.tab_Notenmodul_Credentials.name(), Schema.tab_TimestampsNotenmodulCredentials.name()),
				Schema.tab_Notenmodul_Credentials, Schema.tab_TimestampsNotenmodulCredentials
		);
	}

}
