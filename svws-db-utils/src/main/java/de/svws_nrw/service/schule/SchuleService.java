package de.svws_nrw.service.schule;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;

/**
 * Ein Service für den Zugriff auf die Schuldaten.
 */
public final class SchuleService {

	/** Das Repository für die Schuljahresabschnitte */
	private final EigeneSchuleRepository eigeneSchuleRepository;

	private final SchuljahresabschnitteRepository schulejahresabschnitteRepository;

	/**
	 * @param eigeneSchuleRepository {@link EigeneSchuleRepository}
	 * @param schulejahresabschnitteRepository {@link SchuljahresabschnitteRepository}
	 */
	public SchuleService(final EigeneSchuleRepository eigeneSchuleRepository, final SchuljahresabschnitteRepository schulejahresabschnitteRepository) {
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.schulejahresabschnitteRepository = schulejahresabschnitteRepository;
	}

	/**
	 * Liefert die Schulnummer der Schule
	 *
	 * @return die Schulnummer der Schule
	 */
	public int getSchulnummer() {
		return eigeneSchuleRepository.getSchulnummer();
	}

	/**
	 * Liefert das Schuljahr der Schule
	 *
	 * @return das Schuljahr der Schule
	 */
	public int getSchuljahr() {
		return schulejahresabschnitteRepository.getById(eigeneSchuleRepository.getIdSchuljahresabschnitt()).Jahr;
	}

	/**
	 * Liefert die Schulform der Schule
	 *
	 * @return die Schulform der Schule
	 */
	public Schulform getSchulform() {
		return eigeneSchuleRepository.getSchulform();
	}

}
