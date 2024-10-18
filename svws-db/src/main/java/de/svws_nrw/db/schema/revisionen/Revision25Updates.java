package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 25.
 */
public class Revision25Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 25.
	 */
	public Revision25Updates() {
		super(SchemaRevisionen.REV_25);
		updateZeitstempel();
	}

	private void updateZeitstempel() {
		add("Kopiere die Zeitstempel für die ENM-Leistungsdaten",
				"INSERT INTO %s(ID, tsNotenKrz, tsNotenKrzQuartal, tsFehlStd, tsuFehlStd, tsLernentw, tsWarnung) SELECT * FROM %s;"
						.formatted(Schema.tab_TimestampsSchuelerLeistungsdaten.name(), Schema.tab_EnmLeistungsdaten.name()),
				Schema.tab_EnmLeistungsdaten, Schema.tab_TimestampsSchuelerLeistungsdaten);
		add("Kopiere die Zeitstempel für die ENM-Lernabschnittsdaten",
				"INSERT INTO %s(ID, tsSumFehlStd, tsSumFehlStdU, tsZeugnisBem, tsASV, tsAUE, tsBemerkungVersetzung) SELECT * FROM %s;"
						.formatted(Schema.tab_TimestampsSchuelerLernabschnittsdaten.name(), Schema.tab_EnmLernabschnittsdaten.name()),
				Schema.tab_TimestampsSchuelerLernabschnittsdaten, Schema.tab_SchuelerLernabschnittsdaten);
		add("Kopiere die Zeitstempel für die ENM-Teilleistungen",
				"INSERT INTO %s(ID, tsDatum, tsLehrer_ID, tsArt_ID, tsBemerkung, tsNotenKrz) SELECT * FROM %s;"
						.formatted(Schema.tab_TimestampsSchuelerTeilleistungen.name(), Schema.tab_EnmTeilleistungen.name()),
				Schema.tab_TimestampsSchuelerTeilleistungen, Schema.tab_SchuelerEinzelleistungen);
		add("Setze/Aktualisisere die Zeitstempel für die Ankreuzkompetenzen der Schüler",
				"INSERT INTO %1$s(ID, tsStufe) SELECT ID, CURTIME(3) FROM %2$s WHERE ID NOT IN (SELECT ID FROM %1$s);"
					.formatted(Schema.tab_TimestampsSchuelerAnkreuzkompetenzen.name(), Schema.tab_SchuelerAnkreuzfloskeln.name()),
				Schema.tab_TimestampsSchuelerAnkreuzkompetenzen, Schema.tab_SchuelerAnkreuzfloskeln
		);
	}

}
