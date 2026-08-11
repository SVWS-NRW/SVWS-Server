package de.svws_nrw.service.schueler.schulbesuch;

import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.mapper.schueler.schulbesuch.BisherigeSchuleMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerMerkmalMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchulbesuchMapper;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;

public final class SchulbesuchServiceFactory {

	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final SchuelerMerkmalServiceFactory schuelerMerkmalServiceFactory;
	private final BisherigeSchuleServiceFactory bisherigeSchuleServiceFactory;
	private final SchulbesuchMapper mapper;

	private SchulbesuchServiceFactory(final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final SchuelerMerkmalServiceFactory schuelerMerkmalServiceFactory,
			final BisherigeSchuleServiceFactory bisherigeSchuleServiceFactory,
			final SchulbesuchMapper mapper) {
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.schuelerMerkmalServiceFactory = schuelerMerkmalServiceFactory;
		this.bisherigeSchuleServiceFactory = bisherigeSchuleServiceFactory;
		this.mapper = mapper;
	}

	/**
	 * Constructor
	 *
	 * @param schuelerRepositoryFactory schuelerRepositoryFactory
	 * @param schuelerMerkmalServiceFactory schuelerMerkmalServiceFactory
	 * @param bisherigeSchuleServiceFactory bisherigeSchuleServiceFactory
	 * @param schulbesuchMapper schulbesuchMapper
	 * @return SchulbesuchServiceFactory
	 */
	public static SchulbesuchServiceFactory getNewInstance(final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final SchuelerMerkmalServiceFactory schuelerMerkmalServiceFactory,
			final BisherigeSchuleServiceFactory bisherigeSchuleServiceFactory,
			final SchulbesuchMapper schulbesuchMapper) {
		return new SchulbesuchServiceFactory(
				schuelerRepositoryFactory,
				schuelerMerkmalServiceFactory,
				bisherigeSchuleServiceFactory,
				schulbesuchMapper
		);
	}

	/**
	 * constructor
	 *
	 * @return SchulbesuchServiceFactory
	 */
	public static SchulbesuchServiceFactory getNewInstance() {
		final var schuelerRepositoryFactory = SchuelerRepositoryFactory.getNewInstance();
		final var katalogRepositoryFactory = KatalogRepositoryFactory.getNewInstance();
		final var schuelerMerkmalServiceFactory = SchuelerMerkmalServiceFactory.getNewInstance(
				schuelerRepositoryFactory,
				katalogRepositoryFactory,
				SchuelerMerkmalMapper.INSTANCE
		);
		final var bisherigeSchuleServiceFactory = BisherigeSchuleServiceFactory.getNewInstance(
				schuelerRepositoryFactory,
				BisherigeSchuleMapper.INSTANCE
		);
		return SchulbesuchServiceFactory.getNewInstance(
				schuelerRepositoryFactory,
				schuelerMerkmalServiceFactory,
				bisherigeSchuleServiceFactory,
				SchulbesuchMapper.INSTANCE
		);
	}

	/**
	 * Erzeugt eine neue Instanz
	 *
	 * @return SchulbesuchService
	 */
	public SchulbesuchService getSchulbesuchService() {
		return new SchulbesuchService(
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerMerkmalServiceFactory.getSchuelerMerkmalService(),
				bisherigeSchuleServiceFactory.getBisherigeSchuleService(),
				new DataKatalogEntlassgruende(DbConnectionProvider.getConnection()),
				new DataSchulen(DbConnectionProvider.getConnection()),
				mapper
		);
	}

}
