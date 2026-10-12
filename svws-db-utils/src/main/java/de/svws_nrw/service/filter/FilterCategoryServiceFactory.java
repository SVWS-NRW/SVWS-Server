package de.svws_nrw.service.filter;

import de.svws_nrw.mapper.filter.FilterCategoryMapper;

/**
 * Factory zur Erzeugung des {@link FilterCategoryService}.
 */
public final class FilterCategoryServiceFactory {

	/**
	 * Konstruktor für die {@link FilterCategoryServiceFactory}.
	 */
	private FilterCategoryServiceFactory() {
	}

	/**
	 * Erzeugt eine neue Instanz der {@link FilterCategoryServiceFactory}.
	 *
	 * @return eine neue Instanz der {@link FilterCategoryServiceFactory}
	 */
	public static FilterCategoryServiceFactory getNewInstance() {
		return new FilterCategoryServiceFactory();
	}

	/**
	 * Erzeugt eine neue Instanz des {@link FilterCategoryService} mit der Default-{@link FilterRegistry} und dem {@link FilterCategoryMapper}.
	 *
	 * @return eine neue Instanz des {@link FilterCategoryService}
	 */
	public FilterCategoryService getService() {
		return new FilterCategoryService(FilterRegistry.defaults(), FilterCategoryMapper.INSTANCE);
	}
}
