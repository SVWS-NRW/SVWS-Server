package de.svws_nrw.repo.schule;

import de.svws_nrw.db.dto.current.schild.schule.DTOStatistikZusatzerhebungen;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository der SVWS-Datenbank zum Zugriff auf die Daten der Zusatzerhebungen
 * zur amtlichen Schulstatistik. Der Schlüssel ist die ID des Schuljahresabschnitts.
 */
public interface ZusatzerhebungenStatistikRepository extends RepositoryBase<DTOStatistikZusatzerhebungen, Long> {

	// Hier sollten ggf. weitere spezifische Funktionen ergänzt werden

}
