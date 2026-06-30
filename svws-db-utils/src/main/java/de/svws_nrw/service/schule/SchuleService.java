package de.svws_nrw.service.schule;

import de.svws_nrw.repo.schule.SchuleRepository;

/**
 * Ein Service für den Zugriff auf die Schuldaten.
 */
public final class SchuleService {

	/** Das Repository für die Schuljahresabschnitte */
	private final SchuleRepository schuleRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuleRepository   das Repository für die Schuljahresabschnitte
	 */
	public SchuleService(final SchuleRepository schuleRepository) {
		this.schuleRepository = schuleRepository;
	}

	/**
	 * Liefert die Schulnummer der Schule
	 *
	 * @return die Schulnummer der Schule
	 */
	public int getSchulnummer() {
		return schuleRepository.getSchulnummer();
	}

}
