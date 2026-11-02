package de.svws_nrw.repo.gost;

import java.util.List;

/**
 * Das Interface für ein Repository zur Abfrage aggregierter Informationen zu den Jahrgängen der gymnasialen Oberstufe.
 */
public interface GostJahrgangsinformationenRepository {

	/**
	 * Ermittelt die Informationen zu den Oberstufenjahrgängen (EF, Q1, Q2) zu allen Schuljahresabschnitten in der SVWS-Datenbank.
	 *
	 * @return die Liste mit den Informationen
	 */
	List<GostJahrgangsinformation> getAll();

}
