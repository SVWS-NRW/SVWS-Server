package de.svws_nrw.service.schule;

import de.svws_nrw.repo.schule.SchuleRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;

/**
 * Ein Service für den Zugriff auf die Schuldaten.
 */
public final class SchuleService {

	/** Das Repository für die Schuljahresabschnitte */
	private final SchuleRepository schuleRepository;

	private final SchuljahresabschnitteRepository schulejahresabschnitteRepository;

	/**
	 * @param schuleRepository {@link SchuleRepository}
	 * @param schulejahresabschnitteRepository {@link SchuljahresabschnitteRepository}
	 */
	public SchuleService(final SchuleRepository schuleRepository, final SchuljahresabschnitteRepository schulejahresabschnitteRepository) {
		this.schuleRepository = schuleRepository;
		this.schulejahresabschnitteRepository = schulejahresabschnitteRepository;
	}

	/**
	 * Liefert die Schulnummer der Schule
	 *
	 * @return die Schulnummer der Schule
	 */
	public int getSchulnummer() {
		return schuleRepository.getSchulnummer();
	}

	/**
	 * Liefert das Schuljahr der Schule
	 *
	 * @return das Schuljahr der Schule
	 */
	public int getSchuljahr() {
		return schulejahresabschnitteRepository.getById(schuleRepository.getIdSchuljahresabschnitt()).Jahr;
	}

}
