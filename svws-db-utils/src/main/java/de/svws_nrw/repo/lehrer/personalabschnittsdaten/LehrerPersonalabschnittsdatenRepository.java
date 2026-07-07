package de.svws_nrw.repo.lehrer.personalabschnittsdaten;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.repo.Repository;

public interface LehrerPersonalabschnittsdatenRepository extends Repository<DTOLehrerAbschnittsdaten> {

	/**
	 * Gibt die LehrerPersonalabschnittsdaten für den Lehrer mit der gegebenen ID zurück.
	 *
	 * @param idLehrer idLehrer
	 * @return {@link LehrerPersonalabschnittsdaten}
	 */
	List<DTOLehrerAbschnittsdaten> findByIdLehrer(long idLehrer);

}
