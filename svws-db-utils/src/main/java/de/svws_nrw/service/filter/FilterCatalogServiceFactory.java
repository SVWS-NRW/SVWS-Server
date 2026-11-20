package de.svws_nrw.service.filter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.mapper.schule.katalog.ort.OrtMapper;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;

/**
 * Factory zur Erzeugung des {@link FilterCatalogService}.
 */
public final class FilterCatalogServiceFactory {

	private final KatalogRepositoryFactory katalogRepositoryFactory;

	private FilterCatalogServiceFactory(final KatalogRepositoryFactory katalogRepositoryFactory) {
		this.katalogRepositoryFactory = katalogRepositoryFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der {@link FilterCatalogServiceFactory}.
	 *
	 * @param katalogRepositoryFactory die Factory für die Repositories der Kataloge
	 *
	 * @return eine neue Instanz der {@link FilterCatalogServiceFactory}
	 *
	 * @throws NullPointerException wenn die Factory {@code null} ist
	 */
	public static FilterCatalogServiceFactory getNewInstance(final KatalogRepositoryFactory katalogRepositoryFactory) {
		return new FilterCatalogServiceFactory(
				Objects.requireNonNull(katalogRepositoryFactory, "Die KatalogRepositoryFactory darf nicht null sein."));
	}

	/**
	 * Erzeugt eine neue Instanz des {@link FilterCatalogService} mit allen angebundenen Katalogen.
	 *
	 * @return eine neue Instanz des {@link FilterCatalogService}
	 */
	public FilterCatalogService getService() {
		return new FilterCatalogService(indexByCatalog(createSources()));
	}

	private List<FilterCatalogSource> createSources() {
		return List.of(
				new OrtFilterCatalogSource(katalogRepositoryFactory.getOrtRepository(), OrtMapper.INSTANCE)
		);
	}

	private static Map<FilterCatalog, FilterCatalogSource> indexByCatalog(final List<FilterCatalogSource> sources) {
		return sources.stream()
				.collect(Collectors.toMap(FilterCatalogSource::getCatalog, Function.identity()));
	}

}
