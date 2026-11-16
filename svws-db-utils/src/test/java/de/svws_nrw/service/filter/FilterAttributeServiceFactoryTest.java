package de.svws_nrw.service.filter;

import de.svws_nrw.core.types.filter.FilterDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FilterAttributeServiceFactory")
class FilterAttributeServiceFactoryTest {

	@Test
	@DisplayName("getNewInstance | liefert bei jedem Aufruf eine neue Factory")
	void getNewInstance() {
		final var first = FilterAttributeServiceFactory.getNewInstance();
		final var second = FilterAttributeServiceFactory.getNewInstance();

		assertThat(first).isNotNull();
		assertThat(second).isNotNull().isNotSameAs(first);
	}

	@Test
	@DisplayName("getService | liefert bei jedem Aufruf einen neuen Service")
	void getServiceReturnsNewInstance() {
		final var cut = FilterAttributeServiceFactory.getNewInstance();

		final var first = cut.getService();
		final var second = cut.getService();

		assertThat(first).isNotNull();
		assertThat(second).isNotNull().isNotSameAs(first);
	}

	@Test
	@DisplayName("getService | Service ist mit der Default-Registry verdrahtet")
	void getServiceUsesDefaultRegistry() {
		final var service = FilterAttributeServiceFactory.getNewInstance().getService();

		assertThat(service.getByDomain(FilterDomain.SCHUELER))
				.extracting(attribute -> attribute.key)
				.containsExactly(
						FilterAttributeKeys.SCHUELER_STATUS.value(),
						FilterAttributeKeys.SCHUELER_GESCHLECHT.value(),
						FilterAttributeKeys.SCHUELER_WOHNORT.value());
	}
}
