package de.svws_nrw.service.bk;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.bk.abi.BKGymAbiturdaten;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeService;


/**
 * Diese Klasse stellt einen Daten-Kontext für den Service {@link LehrerAnrechnungsstundeService} bereit.
 */
public final class BKGymAbiturdatenServiceKontext {

	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private final SchuelerRepository schuelerRepository;


	private BKGymAbiturdatenServiceKontext(
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final SchuelerRepository schuelerRepository) {
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.schuelerRepository = schuelerRepository;
	}

	/**
	 * Erstellt einen neuen Service-Kontext.
	 *
	 * @param schuljahresabschnitteRepository   das Repository für die Schuljahresabschnitte
	 * @param schuelerRepository                das Repository für die Schüler
	 *
	 * @return der neue Service-Kontext.
	 */
	public static BKGymAbiturdatenServiceKontext of(final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final SchuelerRepository schuelerRepository) {
		return new BKGymAbiturdatenServiceKontext(schuljahresabschnitteRepository, schuelerRepository);
	}


	/**
	 * Lädt die grundlegenden Daten für die Erstellung der BKGymAbiturdaten aus der Datenbank.
	 * Die Reihenfolge der Repository-Aufrufe ist so gestaltet, dass die Zugriffe möglichst gebündelt stattfinden.
	 *
	 * @param idsSchueler   die Schueler-IDs
	 */
	public void fetchData(final Collection<Long> idsSchueler) {
		// TODO implementieren, wenn die Daten für die Anrechnungsstunden definiert sind
	}


	/**
	 * Führt eine Anfrage auf das Repository der Lehrer-Anrechnungsstunden mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs
	 *
	 * @return die gesuchten Entitäten, sofern entweder im Cache oder aus der Datenbank geladen
	 */
	public List<BKGymAbiturdaten> getAbiturdaten(final Collection<Long> ids) {
		// TODO Auto-generated method stub
		return List.of();
	}

}
