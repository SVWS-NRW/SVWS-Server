package de.svws_nrw.service.filter;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FilterCatalog")
class FilterCatalogTest {

	@ParameterizedTest
	@EnumSource(FilterCatalog.class)
	@DisplayName("getByName | liefert zu dem Namen jedes Katalogs genau diesen Katalog")
	void getByNameFindsEachCatalog(final FilterCatalog catalog) {
		assertThat(FilterCatalog.getByName(catalog.getName())).isSameAs(catalog);
	}

	@Test
	@DisplayName("getName | die Namen der Kataloge sind eindeutig und nicht leer")
	void namesAreUniqueAndNotBlank() {
		assertThat(Arrays.stream(FilterCatalog.values()).map(FilterCatalog::getName))
				.doesNotHaveDuplicates()
				.allSatisfy(name -> assertThat(name).isNotBlank());
	}

	@Test
	@DisplayName("getName | Orte: der Name lautet Orte")
	void orte() {
		assertThat(FilterCatalog.ORTE.getName()).isEqualTo("Orte");
	}

	@Test
	@DisplayName("getByName | beachtet die Groß- und Kleinschreibung des Namens nicht")
	void getByNameIgnoresCase() {
		assertThat(FilterCatalog.getByName("orte")).isSameAs(FilterCatalog.ORTE);
		assertThat(FilterCatalog.getByName("ORTE")).isSameAs(FilterCatalog.ORTE);
	}

	@Test
	@DisplayName("getByName | liefert null bei unbekanntem Namen und bei null")
	void getByNameUnknown() {
		assertThat(FilterCatalog.getByName("Unbekannt")).isNull();
		assertThat(FilterCatalog.getByName(null)).isNull();
	}
}
