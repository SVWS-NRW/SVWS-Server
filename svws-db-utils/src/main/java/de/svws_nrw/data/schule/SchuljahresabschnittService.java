package de.svws_nrw.data.schule;

import java.util.List;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;

/**
 * Ein Service für den Zugriff auf die Schuljahresabschnitte der Schule
 */
public final class SchuljahresabschnittService {

	/** Das Repository für die Schuljahresabschnitte */
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuljahresabschnitteRepository   das Repository für die Schuljahresabschnitte
	 */
	public SchuljahresabschnittService(final SchuljahresabschnitteRepository schuljahresabschnitteRepository) {
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
	}


	private static Schuljahresabschnitt map(final DTOSchuljahresabschnitte dto) {
		final var daten = new Schuljahresabschnitt();
		daten.id = dto.ID;
		daten.schuljahr = dto.Jahr;
		daten.abschnitt = dto.Abschnitt;
		daten.idVorigerAbschnitt = dto.VorigerAbschnitt_ID;
		daten.idFolgeAbschnitt = dto.FolgeAbschnitt_ID;
		return daten;
	}


	/**
	 * Bestimmt den Schuljahresabschnitt anhand der ID
	 *
	 * @param id   die ID des Schuljahresabschnittes
	 *
	 * @return das Schuljahresabschnitt-DTO
	 */
	public Schuljahresabschnitt getById(final Long id) {
		final var dto = schuljahresabschnitteRepository.getById(id);
		return map(dto);
	}

	/**
	 * Gibt eine Liste aller Schuljahresabschnitte zurück.
	 *
	 * @return die Liste mit den Schuljahresabschnitt-DB-DTOs
	 */
	public List<Schuljahresabschnitt> getList() {
		return schuljahresabschnitteRepository.getAll().stream().map(s -> map(s)).toList();
	}

}
