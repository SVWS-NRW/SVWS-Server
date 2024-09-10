package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 23.
 */
public final class Revision23Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 23.
	 */
	public Revision23Updates() {
		super(SchemaRevisionen.REV_23);
		add("Schuljahresabschnitte: Setzen der Abschnitts-ID des vorigen Abschnitts",
				"""
				UPDATE Schuljahresabschnitte s
				    JOIN Schuljahresabschnitte t
				        ON t.Jahr = CASE WHEN s.Abschnitt - 1 < 1 THEN s.Jahr - 1 ELSE s.Jahr END
				        AND t.Abschnitt = CASE WHEN s.Abschnitt - 1 < 1 THEN (SELECT AnzahlAbschnitte FROM EigeneSchule) ELSE s.Abschnitt - 1 END
				SET s.VorigerAbschnitt_ID = t.ID
				""",
				Schema.tab_Schuljahresabschnitte
		);
		add("Schuljahresabschnitte: Setzen der Folge-Abschnitts-ID",
				"""
				UPDATE Schuljahresabschnitte s
				    JOIN Schuljahresabschnitte t
				        ON t.Jahr = CASE WHEN s.Abschnitt + 1 > (SELECT AnzahlAbschnitte FROM EigeneSchule) THEN s.Jahr + 1 ELSE s.Jahr END
				        AND t.Abschnitt = CASE WHEN s.Abschnitt + 1 > (SELECT AnzahlAbschnitte FROM EigeneSchule) THEN 1 ELSE s.Abschnitt + 1 END
				SET s.FolgeAbschnitt_ID = t.ID
				""",
				Schema.tab_Schuljahresabschnitte
		);
	}

}
