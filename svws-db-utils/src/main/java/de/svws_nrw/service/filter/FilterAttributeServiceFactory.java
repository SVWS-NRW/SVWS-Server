package de.svws_nrw.service.filter;

import de.svws_nrw.mapper.filter.FilterAttributeMapper;

/**
 * Factory zur Erzeugung des {@link FilterAttributeService}.
 */
public final class FilterAttributeServiceFactory {

	/**
	 * Konstruktor für die {@link FilterAttributeServiceFactory}.
	 */
	private FilterAttributeServiceFactory() {
	}

	/**
	 * Erzeugt eine neue Instanz der {@link FilterAttributeServiceFactory}.
	 *
	 * @return eine neue Instanz der {@link FilterAttributeServiceFactory}
	 */
	public static FilterAttributeServiceFactory getNewInstance() {
		return new FilterAttributeServiceFactory();
	}

	/**
	 * Erzeugt eine neue Instanz des {@link FilterAttributeService} mit der Default-{@link FilterRegistry} und dem {@link FilterAttributeMapper}.
	 *
	 * @return eine neue Instanz des {@link FilterAttributeService}
	 */
	public FilterAttributeService getService() {
		return new FilterAttributeService(FilterRegistry.defaults(), FilterAttributeMapper.INSTANCE);
	}
}
