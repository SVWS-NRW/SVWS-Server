package de.svws_nrw.db.schema.revisionen;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungKatalogEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtungAnerkennung;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigungAnerkennung;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 45.
 */
public final class Revision45Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 45.
	 */
	public Revision45Updates() {
		super(SchemaRevisionen.REV_45);
		add("LehrerLehramtLehrbef: Korrektur: Setze das Lehramt-Kürzel bei Einträgen, wo dieses nicht gesetzt ist anhand der Lehramts-Tabelle",
				"""
				UPDATE LehrerLehramtLehrbef d
				SET LehramtKrz = (SELECT LehramtKrz FROM LehrerLehramt l GROUP BY l.Lehrer_ID HAVING l.Lehrer_ID = d.Lehrer_ID LIMIT 1)
				WHERE TRIM(COALESCE(d.LehramtKrz, '')) = ''
				    AND (SELECT LehramtKrz FROM LehrerLehramt l GROUP BY l.Lehrer_ID HAVING l.Lehrer_ID = d.Lehrer_ID LIMIT 1) IS NOT NULL
				""",
				Schema.tab_LehrerLehramtLehrbef
		);
		add("LehrerLehramtLehrbef: Korrektur: Entferne Einträge, wo kein gültiges Lehramt-Kürzel ermittelt werden konnte, da kein Lehramtseintrag bei dem Lehrer vorliegt",
				"""
				DELETE FROM LehrerLehramtLehrbef WHERE TRIM(COALESCE(LehramtKrz, '')) = ''
				""",
				Schema.tab_LehrerLehramtLehrbef
		);
		add("LehrerLehramtFachr: Korrektur: Setze das Lehramt-Kürzel bei Einträgen, wo dieses nicht gesetzt ist anhand der Lehramts-Tabelle",
				"""
				UPDATE LehrerLehramtFachr d
				SET LehramtKrz = (SELECT LehramtKrz FROM LehrerLehramt l GROUP BY l.Lehrer_ID HAVING l.Lehrer_ID = d.Lehrer_ID LIMIT 1)
				WHERE TRIM(COALESCE(d.LehramtKrz, '')) = ''
				    AND (SELECT LehramtKrz FROM LehrerLehramt l GROUP BY l.Lehrer_ID HAVING l.Lehrer_ID = d.Lehrer_ID LIMIT 1) IS NOT NULL
				""",
				Schema.tab_LehrerLehramtFachr
		);
		add("LehrerLehramtFachr: Korrektur: Entferne Einträge, wo kein gültiges Lehramt-Kürzel ermittelt werden konnte, da kein Lehramtseintrag bei dem Lehrer vorliegt",
				"""
				DELETE FROM LehrerLehramtFachr WHERE TRIM(COALESCE(LehramtKrz, '')) = ''
				""",
				Schema.tab_LehrerLehramtFachr
		);
	}

	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}

		// Bestimme das aktuelle Schuljahr der Schule
		final List<Object[]> listEigeneSchule = conn.queryNative("SELECT ID, Schuljahresabschnitts_ID FROM EigeneSchule");
		if (listEigeneSchule.isEmpty() || (listEigeneSchule.size() != 1)) {
			logger.logLn("Fehler beim Bestimmen der ID des aktuellen Schuljahresabschnittes der Schule.");
			return false;
		}
		final Long idSchuljahresabschnitt = (Long) listEigeneSchule.getFirst()[1];
		if (idSchuljahresabschnitt == null) {
			logger.logLn("Der Schule ist aktuell keine gültige ID für den Schuljahresabschnitt zugeordnet.");
			return false;
		}
		final List<Object[]> listSchuljahre =
				conn.queryNative("SELECT Jahr, Abschnitt FROM Schuljahresabschnitte WHERE ID = %d".formatted(idSchuljahresabschnitt));
		if (listSchuljahre.isEmpty() || (listSchuljahre.size() != 1)) {
			logger.logLn("Die ID des aktuellen Schuljahresabschnittes der Schule ist nicht gültig.");
			return false;
		}
		final Integer schuljahr = (Integer) listSchuljahre.getFirst()[0];
		if (schuljahr == null) {
			logger.logLn("Das Schuljahr zum aktuellen Schuljahresabschnittes der Schule ist null.");
			return false;
		}

		// Kopiere die Einträge aus der Tabelle LehrerLehramt in die Tabelle LehrerPersonaldatenLehramt
		final HashMap2D<Long, String, Long> mapLehraemter = new HashMap2D<>();
		final List<Object[]> listLehraemter = conn.queryNative("SELECT Lehrer_ID, LehramtKrz, LehramtAnerkennungKrz FROM LehrerLehramt");
		long idNext = conn.transactionGetNextIDByTablename(Schema.tab_LehrerPersonaldatenLehramt.name());
		final List<Object[]> listResultLehraemter = new ArrayList<>();
		for (final Object[] tmpLehramt : listLehraemter) {
			final long idLehrer = (Long) tmpLehramt[0];
			final String krzLehramt = (String) tmpLehramt[1];
			final String krzLehramtAnerkennung = (String) tmpLehramt[2];
			// Prüfe, ob das Lehramt überhaupt gültig ist oder war - nur solche Einträge werden übernommen
			final LehrerLehramt lehramt = LehrerLehramt.data().getWertBySchluessel(krzLehramt);
			if (lehramt == null)
				continue;
			// Bestimme den aktuellen Eintrag in dem Katalog der Lehrämter anhand des aktuellen Schuljahresabschnitt der Schule
			LehrerLehramtKatalogEintrag eintragLehramt = LehrerLehramt.data().getEintragBySchuljahrUndSchluessel(schuljahr, krzLehramt);
			if (eintragLehramt == null) // Existiert kein Eintrag in der Historie, so übernehme den letzten gültigen Historieneintrag
				eintragLehramt = lehramt.historie().getLast();
			// Prüfe den Anerkennungsgrund für das Lehramt
			final LehrerLehramtAnerkennung lehramtAnerkennung = LehrerLehramtAnerkennung.data().getWertBySchluessel(krzLehramtAnerkennung);
			LehrerLehramtAnerkennungKatalogEintrag eintragLehramtAnerkennung =
					LehrerLehramtAnerkennung.data().getEintragBySchuljahrUndSchluessel(schuljahr, krzLehramtAnerkennung);
			if (eintragLehramtAnerkennung == null)
				eintragLehramtAnerkennung = (lehramtAnerkennung == null) ? null : lehramtAnerkennung.historie().getLast();
			// Schreibe den Eintrag für das Lehramt
			final long id = idNext++;
			final Object[] entry = new Object[4];
			entry[0] = id;
			entry[1] = idLehrer;
			entry[2] = eintragLehramt.id;
			entry[3] = (eintragLehramtAnerkennung == null) ? null : eintragLehramtAnerkennung.id;
			listResultLehraemter.add(entry);
			mapLehraemter.put(idLehrer, krzLehramt, id);
		}
		conn.transactionInsertRangeNative(Schema.tab_LehrerPersonaldatenLehramt.name(),
				List.of("ID", "Lehrer_ID", "Lehramt_Katalog_ID", "LehramtAnerkennung_Katalog_ID"),
				listResultLehraemter, 0, listResultLehraemter.size() - 1);
		conn.transactionFlush();

		// Kopiere die Einträge aus der Tabelle LehrerLehramtLehrbef in die Tabelle LehrerPersonaldatenLehramtLehrbefaehigung
		final List<Object[]> listLehrbefaehigungen = conn.queryNative("SELECT Lehrer_ID, LehramtKrz, LehrbefKrz, LehrbefAnerkennungKrz FROM LehrerLehramtLehrbef");
		idNext = conn.transactionGetNextIDByTablename(Schema.tab_LehrerPersonaldatenLehramtLehrbefaehigung.name());
		final List<Object[]> listResultLehrbefaehigungen = new ArrayList<>();
		for (final Object[] tmpLehrbefaehigung : listLehrbefaehigungen) {
			final long idLehrer = (Long) tmpLehrbefaehigung[0];
			final String krzLehramt = (String) tmpLehrbefaehigung[1];
			final String krzLehrbefaehigung = (String) tmpLehrbefaehigung[2];
			final String krzLehrbefaehigungAnerkennung = (String) tmpLehrbefaehigung[3];
			// Bestimme die ID des zugehörigen Lehrarmtseintrages, ignoriere Einträge ohne gültigen Bezug zu einem Eintrag in der Lahramtstabelle
			final Long idLehramt = mapLehraemter.getOrNull(idLehrer, krzLehramt);
			if (idLehramt == null)
				continue;
			// Prüfe, ob die Lehrbefägigung überhaupt gültig ist oder war - nur solche Einträge werden übernommen
			final LehrerLehrbefaehigung lehrbefaehigung = LehrerLehrbefaehigung.data().getWertBySchluessel(krzLehrbefaehigung);
			if (lehrbefaehigung == null)
				continue;
			// Bestimme den aktuellen Eintrag in dem Katalog der Lehrbefägigungen anhand des aktuellen Schuljahresabschnitt der Schule
			LehrerLehrbefaehigungKatalogEintrag eintragLehrbefaehigung =
					LehrerLehrbefaehigung.data().getEintragBySchuljahrUndSchluessel(schuljahr, krzLehrbefaehigung);
			if (eintragLehrbefaehigung == null) // Existiert kein Eintrag in der Historie, so übernehme den letzten gültigen Historieneintrag
				eintragLehrbefaehigung = lehrbefaehigung.historie().getLast();
			// Prüfe den Anerkennungsgrund für die Lehrbefähigung
			final LehrerLehrbefaehigungAnerkennung lehrbefaehigungAnerkennung =
					LehrerLehrbefaehigungAnerkennung.data().getWertBySchluessel(krzLehrbefaehigungAnerkennung);
			LehrerLehrbefaehigungAnerkennungKatalogEintrag eintragLehrbefaehigungAnerkennung =
					LehrerLehrbefaehigungAnerkennung.data().getEintragBySchuljahrUndSchluessel(schuljahr, krzLehrbefaehigungAnerkennung);
			if (eintragLehrbefaehigungAnerkennung == null)
				eintragLehrbefaehigungAnerkennung = (lehrbefaehigungAnerkennung == null) ? null : lehrbefaehigungAnerkennung.historie().getLast();
			// Schreibe den Eintrag für die Lehrbefähigung
			final long id = idNext++;
			final Object[] entry = new Object[4];
			entry[0] = id;
			entry[1] = idLehramt;
			entry[2] = eintragLehrbefaehigung.id;
			entry[3] = (eintragLehrbefaehigungAnerkennung == null) ? null : eintragLehrbefaehigungAnerkennung.id;
			listResultLehrbefaehigungen.add(entry);
		}
		conn.transactionInsertRangeNative(Schema.tab_LehrerPersonaldatenLehramtLehrbefaehigung.name(),
				List.of("ID", "Lehreramt_ID", "Lehrbefaehigung_Katalog_ID", "LehrbefaehigungAnerkennung_Katalog_ID"),
				listResultLehrbefaehigungen, 0, listResultLehrbefaehigungen.size() - 1);
		conn.transactionFlush();

		// Kopiere die Einträge aus der Tabelle LehrerLehramtFachr in die Tabelle LehrerPersonaldatenLehramtFachrichtung
		final List<Object[]> listFachrichtungen = conn.queryNative("SELECT Lehrer_ID, LehramtKrz, FachrKrz, FachrAnerkennungKrz FROM LehrerLehramtFachr");
		idNext = conn.transactionGetNextIDByTablename(Schema.tab_LehrerPersonaldatenLehramtFachrichtung.name());
		final List<Object[]> listResultFachrichtungen = new ArrayList<>();
		for (final Object[] tmpFachrichtung : listFachrichtungen) {
			final long idLehrer = (Long) tmpFachrichtung[0];
			final String krzLehramt = (String) tmpFachrichtung[1];
			final String krzFachrichtung = (String) tmpFachrichtung[2];
			final String krzFachrichtungAnerkennung = (String) tmpFachrichtung[3];
			// Bestimme die ID des zugehörigen Lehrarmtseintrages, ignoriere Einträge ohne gültigen Bezug zu einem Eintrag in der Lahramtstabelle
			final Long idLehramt = mapLehraemter.getOrNull(idLehrer, krzLehramt);
			if (idLehramt == null)
				continue;
			// Prüfe, ob die Fachrichtung überhaupt gültig ist oder war - nur solche Einträge werden übernommen
			final LehrerFachrichtung fachrichtung = LehrerFachrichtung.data().getWertBySchluessel(krzFachrichtung);
			if (fachrichtung == null)
				continue;
			// Bestimme den aktuellen Eintrag in dem Katalog der Fachrichtungen anhand des aktuellen Schuljahresabschnitt der Schule
			LehrerFachrichtungKatalogEintrag eintragFachrichtung =
					LehrerFachrichtung.data().getEintragBySchuljahrUndSchluessel(schuljahr, krzFachrichtung);
			if (eintragFachrichtung == null) // Existiert kein Eintrag in der Historie, so übernehme den letzten gültigen Historieneintrag
				eintragFachrichtung = fachrichtung.historie().getLast();
			// Prüfe den Anerkennungsgrund für die Fachrichtung
			final LehrerFachrichtungAnerkennung fachrichtungAnerkennung =
					LehrerFachrichtungAnerkennung.data().getWertBySchluessel(krzFachrichtungAnerkennung);
			LehrerFachrichtungAnerkennungKatalogEintrag eintragFachrichtungAnerkennung =
					LehrerFachrichtungAnerkennung.data().getEintragBySchuljahrUndSchluessel(schuljahr, krzFachrichtungAnerkennung);
			if (eintragFachrichtungAnerkennung == null)
				eintragFachrichtungAnerkennung = (fachrichtungAnerkennung == null) ? null : fachrichtungAnerkennung.historie().getLast();
			// Schreibe den Eintrag für die Fachrichtung
			final long id = idNext++;
			final Object[] entry = new Object[4];
			entry[0] = id;
			entry[1] = idLehramt;
			entry[2] = eintragFachrichtung.id;
			entry[3] = (eintragFachrichtungAnerkennung == null) ? null : eintragFachrichtungAnerkennung.id;
			listResultFachrichtungen.add(entry);
		}
		conn.transactionInsertRangeNative(Schema.tab_LehrerPersonaldatenLehramtFachrichtung.name(),
				List.of("ID", "Lehreramt_ID", "Fachrichtung_Katalog_ID", "FachrichtungAnerkennung_Katalog_ID"),
				listResultFachrichtungen, 0, listResultFachrichtungen.size() - 1);
		conn.transactionFlush();
		return true;
	}

}
