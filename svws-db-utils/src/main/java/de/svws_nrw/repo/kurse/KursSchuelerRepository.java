package de.svws_nrw.repo.kurse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchuelerPK;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Kurs-Schüler-Zuordnung (Tabelle Kurs_Schueler) der SVWS-Datenbank
 */
public interface KursSchuelerRepository extends RepositoryBase<DTOKursSchueler, DTOKursSchuelerPK> {

	/**
	 * Bestimmt die Zuordnung der Einträge der Kurs-Schüler-Zuordnung im aktuellen Lernabschnitt
	 * (LernabschnittWechselNr = 0) zu den Kursen mit den übergebenen IDs. Jeder übergebenen Kurs-ID
	 * wird mindestens eine leere Liste zugeordnet.
	 *
	 * @param idsKurse   die IDs der Kurse
	 *
	 * @return die Zuordnung der Kurs-ID zu den Einträgen
	 */
	Map<Long, List<DTOKursSchueler>> getMapByKurse(Collection<Long> idsKurse);

	/**
	 * Bestimmt die Zuordnung der Einträge der Kurs-Schüler-Zuordnung im aktuellen Lernabschnitt
	 * (LernabschnittWechselNr = 0) zu den Schülern mit den übergebenen IDs. Jeder übergebenen
	 * Schüler-ID wird mindestens eine leere Liste zugeordnet.
	 *
	 * @param idsSchueler            die IDs der Schüler
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts, auf dessen Kurse die Einträge eingeschränkt werden,
	 *                                 oder null, falls die Einträge nicht eingeschränkt werden sollen
	 *
	 * @return die Zuordnung der Schüler-ID zu den Einträgen
	 */
	Map<Long, List<DTOKursSchueler>> getMapBySchueler(Collection<Long> idsSchueler, Long idSchuljahresabschnitt);

}
