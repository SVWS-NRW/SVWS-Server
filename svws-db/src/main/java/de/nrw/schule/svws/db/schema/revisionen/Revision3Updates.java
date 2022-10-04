package de.nrw.schule.svws.db.schema.revisionen;

import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaRevisionUpdateSQL;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 3.
 */
public class Revision3Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates 
	 * für Revision 3.
	 */
	public Revision3Updates() {
		super(SchemaRevisionen.REV_3);
		updateFremdschluessel_K_Ort();
	}

	private void updateFremdschluessel_K_Ort() {
		add("Fremdschlüssel auf K_Ort - Tabelle K_AllgAdresse", 
			"UPDATE K_AllgAdresse SET AllgAdrOrt_ID = (SELECT K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_AllgAdresse.AllgAdrPLZ LIMIT 1)",
			DBDriver.MSSQL, "UPDATE K_AllgAdresse SET AllgAdrOrt_ID = (SELECT TOP 1 K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_AllgAdresse.AllgAdrPLZ)",
			Schema.tab_K_Ort, Schema.tab_K_AllgAdresse
		);
		add("Fremdschlüssel auf K_Ort - Tabelle K_Lehrer", 
			"UPDATE K_Lehrer SET Ort_ID = (SELECT K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_Lehrer.PLZ LIMIT 1)",
			DBDriver.MSSQL, "UPDATE K_Lehrer SET Ort_ID = (SELECT TOP 1 K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_Lehrer.PLZ)",
			Schema.tab_K_Ort, Schema.tab_K_Lehrer
		);
		add("Fremdschlüssel auf K_Ort - Tabelle Schueler", 
			"UPDATE Schueler SET Ort_ID = (SELECT K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=Schueler.PLZ LIMIT 1)",
			DBDriver.MSSQL, "UPDATE Schueler SET Ort_ID = (SELECT TOP 1 K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=Schueler.PLZ)",
			Schema.tab_K_Ort, Schema.tab_Schueler
		);
		add("Fremdschlüssel auf K_Ort - Tabelle SchuelerErzAdr", 
			"UPDATE SchuelerErzAdr SET ErzOrt_ID = (SELECT K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=SchuelerErzAdr.ErzPLZ LIMIT 1)",
			DBDriver.MSSQL, "UPDATE SchuelerErzAdr SET ErzOrt_ID = (SELECT TOP 1 K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=SchuelerErzAdr.ErzPLZ)",
			Schema.tab_K_Ort, Schema.tab_SchuelerErzAdr
		);
		add("Fremdschlüssel auf K_Ort - Tabelle K_Ortsteil", 
			"UPDATE K_Ortsteil SET Ort_ID = (SELECT K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_Ortsteil.PLZ LIMIT 1)",
			DBDriver.MSSQL, "UPDATE K_Ortsteil SET Ort_ID = (SELECT TOP 1 K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_Ortsteil.PLZ)",
			Schema.tab_K_Ort, Schema.tab_K_Ortsteil
		);
	}
	
}
