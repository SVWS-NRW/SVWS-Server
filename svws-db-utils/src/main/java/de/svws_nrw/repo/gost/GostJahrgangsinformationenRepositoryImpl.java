package de.svws_nrw.repo.gost;

import java.util.List;

import de.svws_nrw.db.DBEntityManager;

/**
 * Diese Repository-Klasse dient der Abfrage aggregierter Informationen zu den Jahrgängen der gymnasialen Oberstufe.
 */
public final class GostJahrgangsinformationenRepositoryImpl implements GostJahrgangsinformationenRepository {

	/** Die Jahrgänge der gymnasialen Oberstufe als Liste von Kürzeln für die SQL-Abfrage */
	private static final String SQL_JAHRGAENGE_OBERSTUFE = "(\"EF\", \"Q1\", \"Q2\")";

	/** Die Kursarten der gymnasialen Oberstufe als Liste von Kürzeln für die SQL-Abfrage */
	private static final String SQL_KURSARTEN_OBERSTUFE = "('LK','ZK','GK','PJK','VTF')";

	private static final String SQL_ALL = """
		SELECT
		    jg.id AS Jahrgang_ID,
		    jg.ASDJahrgang,
		    sja.ID AS Schuljahresabschnitts_ID,
		    if (kv.AnzahlKurse IS NULL, 0, kv.AnzahlKurse) AS AnzahlKurse,
		    if (nv.AnzahlNoten IS NULL, 0, nv.AnzahlNoten) AS AnzahlNoten
		FROM
		Schuljahresabschnitte sja
		JOIN EigeneSchule_Jahrgaenge jg ON jg.ASDJahrgang IN %1$s
		LEFT JOIN (
		    SELECT k.Jahrgang_ID, k.Schuljahresabschnitts_ID, count(k.ID) AS AnzahlKurse
		    FROM Kurse k WHERE k.ASDJahrgang IN %1$s AND k.KursartAllg IN %2$s
		    GROUP BY k.Jahrgang_ID, k.Schuljahresabschnitts_ID
		) AS kv ON kv.Jahrgang_ID = jg.ID AND kv.Schuljahresabschnitts_ID = sja.ID
		LEFT JOIN (
		    SELECT sla.Jahrgang_ID, sla.Schuljahresabschnitts_ID, count(sld.ID) AS AnzahlNoten
		    FROM SchuelerLernabschnittsdaten sla
		    JOIN Schueler s ON s.Geloescht = '-' AND sla.Schueler_ID = s.ID
		    JOIN SchuelerLeistungsdaten sld ON sld.Abschnitt_ID = sla.ID AND sld.KursartAllg IN %2$s
		        AND NOT (((sld.NotenKrz IS NULL) OR (sld.NotenKrz = '') OR (sld.NotenKrz = 'AT')) AND ((sld.NotenKrzQuartal IS NULL) OR (sld.NotenKrzQuartal = '') OR (sld.NotenKrzQuartal = 'AT')))
		    GROUP BY
		        sla.Jahrgang_ID, sla.Schuljahresabschnitts_ID
		) nv ON nv.Jahrgang_ID = jg.ID AND nv.Schuljahresabschnitts_ID = sja.ID
		""".formatted(SQL_JAHRGAENGE_OBERSTUFE, SQL_KURSARTEN_OBERSTUFE);

	private final DBEntityManager conn;

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public GostJahrgangsinformationenRepositoryImpl(final DBEntityManager conn) {
		this.conn = conn;
	}

	@Override
	public List<GostJahrgangsinformation> getAll() {
		final List<Object[]> result = conn.queryNative(SQL_ALL);
		return result.stream().map(GostJahrgangsinformationenRepositoryImpl::toInformation).toList();
	}

	/**
	 * Wandelt eine Ergebniszeile der Abfrage (Jahrgang_ID, ASDJahrgang, Schuljahresabschnitts_ID, AnzahlKurse, AnzahlNoten) um.
	 *
	 * @param zeile   die Ergebniszeile
	 *
	 * @return die Jahrgangsinformation
	 */
	private static GostJahrgangsinformation toInformation(final Object[] zeile) {
		final long idJahrgang = ((Number) zeile[0]).longValue();
		final String kuerzelASDJahrgang = (String) zeile[1];
		final long idSchuljahresabschnitt = ((Number) zeile[2]).longValue();
		final long anzahlKurse = ((Number) zeile[3]).longValue();
		final long anzahlNoten = ((Number) zeile[4]).longValue();
		return new GostJahrgangsinformation(idJahrgang, kuerzelASDJahrgang, idSchuljahresabschnitt, anzahlKurse, anzahlNoten);
	}

}
