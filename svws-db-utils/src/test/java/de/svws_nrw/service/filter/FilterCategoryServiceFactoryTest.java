package de.svws_nrw.service.filter;

import de.svws_nrw.core.types.filter.FilterDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FilterCategoryServiceFactory")
class FilterCategoryServiceFactoryTest {

	@Test
	@DisplayName("getNewInstance | liefert bei jedem Aufruf eine neue Factory")
	void getNewInstance() {
		final FilterCategoryServiceFactory first = FilterCategoryServiceFactory.getNewInstance();
		final FilterCategoryServiceFactory second = FilterCategoryServiceFactory.getNewInstance();

		assertThat(first).isNotNull();
		assertThat(second).isNotNull().isNotSameAs(first);
	}

	@Test
	@DisplayName("getService | liefert bei jedem Aufruf einen neuen Service")
	void getServiceReturnsNewInstance() {
		final FilterCategoryServiceFactory cut = FilterCategoryServiceFactory.getNewInstance();

		final FilterCategoryService first = cut.getService();
		final FilterCategoryService second = cut.getService();

		assertThat(first).isNotNull();
		assertThat(second).isNotNull().isNotSameAs(first);
	}

	@Test
	@DisplayName("getService | Service ist mit der Default-Registry verdrahtet")
	void getServiceUsesDefaultRegistry() {
		final FilterCategoryService service = FilterCategoryServiceFactory.getNewInstance().getService();

		assertThat(service.getByDomain(FilterDomain.SCHUELER))
				.extracting(category -> category.key)
				.containsExactly(FilterCategoryKeys.SCHUELER_INDIVIDUALDATEN.value());
	}
}
