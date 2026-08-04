package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates auf Revision 72.
 */
public final class Revision72Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 72.
	 */
	public Revision72Updates() {
		super(SchemaRevisionen.REV_72);
		add("Herkunftbildungsgang ersetzt HerkunftbildungsgangTyp - daher: Umschlüsseln der WBK-Schlüssel AG, AR, KL aus HerkunftbildungsgangTyp "
						+ "auf G02, R02, K02 in Herkunftbildungsgang und nullen der BK-Schlüssel BF, BS, BY, F0, FS",
				"""
				UPDATE Schueler
				SET LSSchulformSim = CASE LSSchulformSim
				 WHEN 'AG' THEN 'G02'
				 WHEN 'AR' THEN 'R02'
				 WHEN 'KL' THEN 'K02'
				 WHEN 'BF' THEN NULL
				 WHEN 'BS' THEN NULL
				 WHEN 'BY' THEN NULL
				 WHEN 'F0' THEN NULL
				 WHEN 'FS' THEN NULL
				 ELSE LSSchulformSim
				 END
				WHERE LSSchulformSim IN ('AG', 'AR', 'KL', 'BF', 'BS', 'BY', 'F0', 'FS');
				""",
				Schema.tab_Schueler);
	}
}
