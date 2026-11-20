package de.svws_nrw.service.filter;

import java.util.Arrays;
import java.util.List;

import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("FilterCatalogServiceFactory")
class FilterCatalogServiceFactoryTest {

	@Test
	@DisplayName("getNewInstance | wirft NullPointerException, wenn die KatalogRepositoryFactory null ist")
	void getNewInstanceNull() {
		assertThatThrownBy(() -> FilterCatalogServiceFactory.getNewInstance(null))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("KatalogRepositoryFactory");
	}

	@Test
	@DisplayName("getService | liefert bei jedem Aufruf einen neuen Service")
	void getServiceReturnsNewInstance() {
		final var repositoryFactory = mock(KatalogRepositoryFactory.class);
		final OrtRepository ortRepo = mock(OrtRepository.class);
		when(repositoryFactory.getOrtRepository()).thenReturn(ortRepo);
		final var cut = FilterCatalogServiceFactory.getNewInstance(repositoryFactory);

		final var first = cut.getService();
		final var second = cut.getService();

		assertThat(first).isNotNull();
		assertThat(second).isNotNull().isNotSameAs(first);
	}

	@Test
	@DisplayName("getService | bindet jeden Katalog des Enums an")
	void getServiceCoversAllCatalogs() {
		final var repositoryFactory = mock(KatalogRepositoryFactory.class);
		final OrtRepository ortRepo = mock(OrtRepository.class);
		when(repositoryFactory.getOrtRepository()).thenReturn(ortRepo);
		final var service = FilterCatalogServiceFactory.getNewInstance(repositoryFactory).getService();

		Arrays.stream(FilterCatalog.values())
				.forEach(catalog -> assertThatCode(() -> service.getByName(catalog.getName())).doesNotThrowAnyException());
	}

	@Test
	@DisplayName("getService | liefert die Orte aus dem Repository als Werte mit Bezeichnung aus PLZ und Ortsname, sortiert")
	void getServiceLoadsOrteFromRepository() {
		final var repositoryFactory = mock(KatalogRepositoryFactory.class);
		final var ortRepository = mock(OrtRepository.class);
		when(repositoryFactory.getOrtRepository()).thenReturn(ortRepository);
		when(ortRepository.getAll()).thenReturn(List.of(new DTOOrt(2L, "53840", "Troisdorf"), new DTOOrt(1L, "44649", "Herne")));
		final var service = FilterCatalogServiceFactory.getNewInstance(repositoryFactory).getService();

		final var result = service.getByName("Orte");

		assertThat(result)
				.extracting(value -> value.id, value -> value.bezeichnung)
				.containsExactly(tuple(1L, "44649 Herne"), tuple(2L, "53840 Troisdorf"));
	}

	@Test
	@DisplayName("getService | wirft NOT_FOUND für einen unbekannten Katalog")
	void getServiceUnknownCatalog() {
		final var repositoryFactory = mock(KatalogRepositoryFactory.class);
		final OrtRepository ortRepo = mock(OrtRepository.class);
		when(repositoryFactory.getOrtRepository()).thenReturn(ortRepo);
		final var service = FilterCatalogServiceFactory.getNewInstance(repositoryFactory).getService();

		assertThatThrownBy(() -> service.getByName("Unbekannt"))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("Unbekannt");
	}
}
