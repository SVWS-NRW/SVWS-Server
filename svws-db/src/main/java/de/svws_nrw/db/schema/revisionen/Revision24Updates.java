package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 24.
 */
public final class Revision24Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 24.
	 */
	public Revision24Updates() {
		super(SchemaRevisionen.REV_24);
		add("SchuelerLernabschnittsdaten: Setzen einer fehlenden ASDSchulgliederung mithilfe der Schulgliederung aus der Tabelle EigeneSchule_Jahrgaenge",
				"""
				UPDATE %s sla SET sla.ASDSchulgliederung = (
				    SELECT jg.SGL FROM %s jg WHERE jg.ID = sla.Jahrgang_ID
				) WHERE sla.ASDSchulgliederung IS NULL AND sla.Jahrgang_ID IS NOT NULL
				""".formatted(Schema.tab_SchuelerLernabschnittsdaten.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()),
				Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule_Jahrgaenge
		);
		add("SchuelerLernabschnittsdaten: Setzen einer fehlenden ASDSchulgliederung mithilfe der Schulgliederung aus der Tabelle EigeneSchule_Fachklassen",
				"""
				UPDATE %s sla SET sla.ASDSchulgliederung = (
				    SELECT fk.BKIndexTyp FROM %s fk WHERE fk.ID = sla.Fachklasse_ID
				) WHERE sla.ASDSchulgliederung IS NULL AND sla.Jahrgang_ID IS NULL AND sla.Fachklasse_ID IS NOT NULL
				""".formatted(Schema.tab_SchuelerLernabschnittsdaten.name(), Schema.tab_EigeneSchule_Fachklassen.name()),
				Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule_Fachklassen
		);
		add("SchuelerLernabschnittsdaten: (nur Schulformen BK, SB) Entfernen von Datensätzen bei den die ASDSchulgliederung fehlt",
				"""
				DELETE FROM %s WHERE ASDSchulgliederung IS NULL AND Jahrgang_ID IS NULL AND Fachklasse_ID IS NULL
				    AND (SELECT SchulformKrz FROM %s) IN ('BK', 'SB')
				""".formatted(Schema.tab_SchuelerLernabschnittsdaten.name(), Schema.tab_EigeneSchule.name()),
				Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_EigeneSchule
		);
	}

}
