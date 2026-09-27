package de.svws_nrw.service.schule.katalog.ankreuzkompetenz;

import de.svws_nrw.core.data.kataloge.AnkreuzkompetenzKonfiguration;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzdaten;
import de.svws_nrw.mapper.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzKonfigurationMapper;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzKonfigurationRepository;

/**
 * Der Service für den Zugriff auf die Konfiguration der Ankreuzkompetenzen
 */
public class AnkreuzkompetenzKonfigurationService {

	private final AnkreuzkompetenzKonfigurationRepository repo;
	private final AnkreuzkompetenzKonfigurationMapper mapper;

	/**
	 * Erstellt einen neuen Service
	 *
	 * @param repo    das Repository für den Zugriff auf die Konfiguration {@link AnkreuzkompetenzKonfigurationRepository}
	 * @param mapper  der Mapper für die Konfigurationsdaten {@link AnkreuzkompetenzKonfigurationMapper}
	 */
	public AnkreuzkompetenzKonfigurationService(final AnkreuzkompetenzKonfigurationRepository repo, final AnkreuzkompetenzKonfigurationMapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}

	private DTOAnkreuzdaten getFromDB() {
		return TransactionSupport.transactional(() -> repo.findFirst().orElseGet(() -> {
			final DTOAnkreuzdaten result = new DTOAnkreuzdaten(1);
			repo.create(result);
			return result;
		}));
	}

	/**
	 * Liefert die Konfiguration zu den Ankreuzkompetenzen zurück. Ist noch keine Konfiguration
	 * in der Datenbank vorhanden, so wird diese automatisch neu erstellt.
	 *
	 * @return  die Konfiguration zu den Ankreuzkompetenzen
	 */
	public AnkreuzkompetenzKonfiguration get() {
		return mapper.toApi(getFromDB());
	}

	/**
	 * Führt einen Patch auf die Konfiguration der Ankreuzkompetenzen aus und gibz das gepatchte Ergebnis zurück.
	 *
	 * @param patch   der Patch-Request
	 *
	 * @return das Ergebnis nach dem Patch
	 */
	public AnkreuzkompetenzKonfiguration patch(final AnkreuzkompetenzKonfigurationPatchRequest patch) {
		return TransactionSupport.transactional(() -> {
			final var entity = getFromDB();
			mapper.patch(patch, entity);
			repo.update(entity);
			return mapper.toApi(entity);
		});
	}

}
