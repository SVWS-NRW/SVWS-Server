package de.svws_nrw.service.schueler.schulbesuch;

import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.mapper.schueler.schulbesuch.BisherigeSchuleMapper;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerBisherigeSchuleRepository;

public final class BisherigeSchuleServiceFactory {

	private final SchuelerRepositoryFactory repoFactory;
	private final BisherigeSchuleMapper mapper;

	private BisherigeSchuleServiceFactory(final SchuelerRepositoryFactory repoFactory, final BisherigeSchuleMapper mapper) {
		this.repoFactory = repoFactory;
		this.mapper = mapper;
	}

	/**
	 * Erstellt eine neue Instanz der {@code BisherigeSchulenServiceFactory}.
	 *
	 * @param repoFactory das Repository-Factory für {@link SchuelerBisherigeSchuleRepository}-Instanzen
	 * @param mapper      der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @return eine neue {@code BisherigeSchulenServiceFactory}
	 */
	public static BisherigeSchuleServiceFactory getNewInstance(final SchuelerRepositoryFactory repoFactory, final BisherigeSchuleMapper mapper) {
		return new BisherigeSchuleServiceFactory(repoFactory, mapper);
	}

	/**
	 * Erstellt eine neue Instanz des {@link BisherigeSchuleService}.
	 * <p>
	 * Schulen- und Entlassgrundkataloge werden über die aktuelle Datenbankverbindung geladen.
	 * </p>
	 *
	 * @return ein neuer {@code BisherigeSchulenService} mit allen erforderlichen Abhängigkeiten
	 */
	public BisherigeSchuleService getBisherigeSchuleService() {
		final var dataSchulen = new DataSchulen(DbConnectionProvider.getConnection());
		final var dataEntlassgruende = new DataKatalogEntlassgruende(DbConnectionProvider.getConnection());
		return new BisherigeSchuleService(repoFactory.getSchuelerBisherigeSchuleRepository(), mapper, dataSchulen, dataEntlassgruende);
	}

}
