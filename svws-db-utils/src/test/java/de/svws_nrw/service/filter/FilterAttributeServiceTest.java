package de.svws_nrw.service.filter;

import java.util.List;

import de.svws_nrw.core.data.filter.FilterAttributeResponse;
import de.svws_nrw.core.types.filter.FilterDomain;
import de.svws_nrw.mapper.filter.FilterAttributeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FilterAttributeService")
class FilterAttributeServiceTest {

	@Mock
	private FilterRegistry filterRegistry;

	private final FilterAttributeMapper mapper = FilterAttributeMapper.INSTANCE;

	private FilterAttributeService cut;

	@BeforeEach
	void setUp() {
		cut = new FilterAttributeService(filterRegistry, mapper);
	}

	@Nested
	@DisplayName("Konstruktor")
	class Constructor {

		@Test
		@DisplayName("wirft NullPointerException, wenn die FilterRegistry null ist")
		void registryNull() {
			assertThatThrownBy(() -> new FilterAttributeService(null, mapper))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("FilterRegistry");
		}

		@Test
		@DisplayName("wirft NullPointerException, wenn der Mapper null ist")
		void mapperNull() {
			assertThatThrownBy(() -> new FilterAttributeService(filterRegistry, null))
					.isInstanceOf(NullPointerException.class)
					.hasMessageContaining("FilterAttributeMapper");
		}
	}

	@Nested
	@DisplayName("getByDomain")
	class GetByDomain {

		@Test
		@DisplayName("mappt alle Attribute der Registry vollständig und in der Reihenfolge der Registry")
		void mapsAllAttributesInOrder() {
			final var category = new FilterCategoryKey("schueler.test");
			final FilterAttribute first = new EnumFilterAttribute(new FilterAttributeKey("schueler.erstes"), "Erstes", "ErsterEnum", category,
					AttributeTarget.of("Entity", "erstes"));
			final FilterAttribute second = new CatalogFilterAttribute(new FilterAttributeKey("schueler.zweites"), "Zweites", "Orte", category,
					AttributeTarget.of("Entity", "zweites"));
			when(filterRegistry.getAttributesByDomain(FilterDomain.SCHUELER)).thenReturn(List.of(first, second));

			final var result = cut.getByDomain(FilterDomain.SCHUELER);

			assertThat(result)
					.extracting(response -> response.key, response -> response.label, response -> response.keyCategory, response -> response.type,
							response -> response.source)
					.containsExactly(
							tuple("schueler.erstes", "Erstes", "schueler.test", "enum", "ErsterEnum"),
							tuple("schueler.zweites", "Zweites", "schueler.test", "catalog", "Orte"));
		}

		@Test
		@DisplayName("liefert eine leere Liste, wenn die Domäne keine Attribute hat")
		void emptyRegistryResult() {
			when(filterRegistry.getAttributesByDomain(FilterDomain.SCHUELER)).thenReturn(List.of());

			assertThat(cut.getByDomain(FilterDomain.SCHUELER)).isEmpty();
		}

		@Test
		@DisplayName("fragt die Registry mit der übergebenen Domäne ab")
		void delegatesDomainToRegistry() {
			when(filterRegistry.getAttributesByDomain(FilterDomain.SCHUELER)).thenReturn(List.of());

			cut.getByDomain(FilterDomain.SCHUELER);

			verify(filterRegistry).getAttributesByDomain(FilterDomain.SCHUELER);
		}

		@Test
		@DisplayName("liefert mit der Default-Registry die Attribute der Kategorie Individualdaten")
		void integrationWithDefaults() {
			final var service = new FilterAttributeService(FilterRegistry.defaults(), mapper);

			final List<FilterAttributeResponse> result = service.getByDomain(FilterDomain.SCHUELER);

			assertThat(result)
					.extracting(response -> response.key, response -> response.label, response -> response.keyCategory, response -> response.type,
							response -> response.source)
					.containsExactly(
							tuple("schueler.status", "Status", "schueler.individualdaten", "coretype", "SchuelerStatus"),
							tuple("schueler.geschlecht", "Geschlecht", "schueler.individualdaten", "enum", "Geschlecht"),
							tuple("schueler.wohnort", "Ort", "schueler.individualdaten", "catalog", "Orte"));
		}
	}
}
