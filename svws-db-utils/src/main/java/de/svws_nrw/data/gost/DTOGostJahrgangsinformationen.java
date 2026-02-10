package de.svws_nrw.data.gost;

import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;

/**
 * Dieses DTO enthält aggregierte Daten zu ein Jahrgang der gymnasialen Oberstufe.
 */
public class DTOGostJahrgangsinformationen {

	/** Die ID des Jahrgangs */
	public long idJahrgang;

	/** Der ASD-Jahrgang */
	public @NotNull String kuerzelASDJahrgang;

	/** Die ID des Schuljahresabschnittes */
	public long idSchuljahresabschnitts;

	/** Die Anzahl der Oberstufen-Kurse, welche für den Jahrgang in dem Schuljahresabschnittes bereits erstellt wurden */
	public long anzahlKurse;

	/** Die Anzahl der Leistungsdaten, welche für den Jahrgang in dem Schuljahresabschnittes bereits mit Noten versehen wurden */
	public long anzahlNoten;


	/**
	 * Erstellt ein neues DTO.
	 *
	 * @param idJahrgang                die ID des Jahrgangs
	 * @param kuerzelASDJahrgang        das Statistik-Kürzel des Jahrgangs
	 * @param idSchuljahresabschnitts   die ID des Schuljahresabchnittes
	 * @param anzahlKurse               die Anzahl der Oberstufen-Kurse, welche für den Jahrgang in dem Schuljahresabschnittes bereits erstellt wurden
	 * @param anzahlNoten               die Anzahl der Leistungsdaten, welche für den Jahrgang in dem Schuljahresabschnittes bereits mit Noten versehen wurden
	 */
	public DTOGostJahrgangsinformationen(final long idJahrgang, final @NotNull String kuerzelASDJahrgang, final long idSchuljahresabschnitts,
			final long anzahlKurse, final long anzahlNoten) {
		this.idJahrgang = idJahrgang;
		this.kuerzelASDJahrgang = kuerzelASDJahrgang;
		this.idSchuljahresabschnitts = idSchuljahresabschnitts;
		this.anzahlKurse = anzahlKurse;
		this.anzahlNoten = anzahlNoten;
	}


	/**
	 * Ermittelt die Informationen zu den Oberstufenjahrgängen zu allen Schuljahresabschnitten in der SVWS-Datenbank
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste
	 */
	public static @NotNull List<DTOGostJahrgangsinformationen> queryAll(final @NotNull DBEntityManager conn) {
		final String sql = """
			SELECT
			    jg.id AS Jahrgang_ID,
			    jg.ASDJahrgang,
			    sja.ID AS Schuljahresabschnitts_ID,
			    if (kv.AnzahlKurse IS NULL, 0, kv.AnzahlKurse) AS AnzahlKurse,
			    if (nv.AnzahlNoten IS NULL, 0, nv.AnzahlNoten) AS AnzahlNoten
			FROM
			Schuljahresabschnitte sja
			JOIN EigeneSchule_Jahrgaenge jg ON jg.ASDJahrgang IN ("EF", "Q1", "Q2")
			LEFT JOIN (
			    SELECT k.Jahrgang_ID, k.Schuljahresabschnitts_ID, count(k.ID) AS AnzahlKurse
			    FROM Kurse k WHERE k.ASDJahrgang IN ("EF", "Q1", "Q2") AND k.KursartAllg IN ('LK','ZK','GK','PJK','VTF')
			    GROUP BY k.Jahrgang_ID, k.Schuljahresabschnitts_ID
			) AS kv ON kv.Jahrgang_ID = jg.ID AND kv.Schuljahresabschnitts_ID = sja.ID
			LEFT JOIN (
			    SELECT sla.Jahrgang_ID, sla.Schuljahresabschnitts_ID, count(sld.ID) AS AnzahlNoten
			    FROM SchuelerLernabschnittsdaten sla
			    JOIN Schueler s ON s.Geloescht = '-' AND sla.Schueler_ID = s.ID
			    JOIN SchuelerLeistungsdaten sld ON sld.Abschnitt_ID = sla.ID AND sld.KursartAllg IN ('LK','ZK','GK','PJK','VTF')
			        AND NOT (((sld.NotenKrz IS NULL) OR (sld.NotenKrz = '') OR (sld.NotenKrz = 'AT')) AND ((sld.NotenKrzQuartal IS NULL) OR (sld.NotenKrzQuartal = '') OR (sld.NotenKrzQuartal = 'AT')))
			    GROUP BY
			        sla.Jahrgang_ID, sla.Schuljahresabschnitts_ID
			) nv ON nv.Jahrgang_ID = jg.ID AND nv.Schuljahresabschnitts_ID = sja.ID
			""";
		final List<Object[]> result = conn.queryNative(sql);
		return result.stream().map(a -> {
			final long idJahrgang = ((Number) a[0]).longValue();
			final @NotNull String kuerzelASDJahrgang = (String) a[1];
			final long idSchuljahresabschnitt = ((Number) a[2]).longValue();
			final long anzahlKurse = ((Number) a[3]).longValue();
			final long anzahlNoten = ((Number) a[4]).longValue();
			return new DTOGostJahrgangsinformationen(idJahrgang, kuerzelASDJahrgang, idSchuljahresabschnitt, anzahlKurse, anzahlNoten);
		}).toList();
	}


	/**
	 * Ermittelt die Informationen zu den Oberstufenjahrgängen zu allen Schuljahresabschnitten in der SVWS-Datenbank und
	 * gibt diese in Form einer HashMap2D (idSchuljahresabschnitt, kuerzelASDJahrgang) -> DTOGostJahrgangsinformationen zurück.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die HashMap2D
	 */
	public static HashMap2D<Long, String, DTOGostJahrgangsinformationen> getMapJahrgangsinformationen(final @NotNull DBEntityManager conn) {
		final @NotNull List<DTOGostJahrgangsinformationen> list = queryAll(conn);
		final @NotNull HashMap2D<Long, String, DTOGostJahrgangsinformationen> map = new HashMap2D<>();
		for (final @NotNull DTOGostJahrgangsinformationen info : list)
			map.put(info.idSchuljahresabschnitts, info.kuerzelASDJahrgang, info);
		return map;
	}

}
