package de.svws_nrw.service.schule;

import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Zugriff auf die Schuldaten.
 */
public final class EigeneSchuleService {

	/** Das Repository für die Schuljahresabschnitte */
	private final EigeneSchuleRepository eigeneSchuleRepository;

	private final SchuljahresabschnitteRepository schulejahresabschnitteRepository;

	/**
	 * @param eigeneSchuleRepository {@link EigeneSchuleRepository}
	 * @param schulejahresabschnitteRepository {@link SchuljahresabschnitteRepository}
	 */
	public EigeneSchuleService(final EigeneSchuleRepository eigeneSchuleRepository, final SchuljahresabschnitteRepository schulejahresabschnitteRepository) {
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
	 * Liefert die ID des aktuellen Schuljahresabschnitts der Schule
	 *
	 * @return die ID des aktuellen Schuljahresabschnitts der Schule
	 */
	public long getIdSchuljahresabschnitt() {
		return eigeneSchuleRepository.getIdSchuljahresabschnitt();
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

	/**
	 * Gibt zurück, ob die Schulform der Schule im aktuellen Schuljahr eine gymnasiale Oberstufe erlaubt.
	 *
	 * @return {@code true}, wenn die Schule eine gymnasiale Oberstufe haben kann, sonst {@code false}
	 *
	 * @throws ApiOperationException   wenn der Katalog-Eintrag der Schulform für das aktuelle Schuljahr nicht bestimmt werden kann
	 */
	public boolean hatGymnasialeOberstufe() {
		final int schuljahr = getSchuljahr();
		final SchulformKatalogEintrag eintrag = getSchulform().daten(schuljahr);
		if (eintrag == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Schulform-Katalog-Eintrag für die Schulform der Schule konnte für das aktuelle Schuljahr %d nicht bestimmt werden.".formatted(schuljahr));
		}
		return eintrag.hatGymOb;
	}

}
