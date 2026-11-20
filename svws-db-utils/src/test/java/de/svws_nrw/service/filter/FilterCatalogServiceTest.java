package de.svws_nrw.service.filter;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.filter.FilterCatalogValue;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FilterCatalogService")
class FilterCatalogServiceTest {

	@Mock
	private FilterCatalogSource orte;

	@Test
	@DisplayName("Konstruktor | wirft NullPointerException bei null als Quellen")
	void constructorNull() {
		assertThatThrownBy(() -> new FilterCatalogService(null))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Katalogquellen");
	}

	@Test
	@DisplayName("getByName | liefert die Werte der Quelle des Katalogs")
	void getByNameFound() {
		final var value = new FilterCatalogValue();
		value.id = 1L;
		value.bezeichnung = "53840 Troisdorf";
		when(orte.loadValues()).thenReturn(List.of(value));
		final var cut = new FilterCatalogService(Map.of(FilterCatalog.ORTE, orte));

		final var result = cut.getByName("Orte");

		assertThat(result).containsExactly(value);
	}

	@Test
	@DisplayName("getByName | wirft NOT_FOUND, wenn es keinen Katalog mit dem Namen gibt")
	void getByNameUnknown() {
		final var cut = new FilterCatalogService(Map.of(FilterCatalog.ORTE, orte));

		assertThatThrownBy(() -> cut.getByName("Unbekannt"))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND))
				.hasMessageContaining("Unbekannt");
		verify(orte, never()).loadValues();
	}

	@Test
	@DisplayName("getByName | findet den Katalog unabhängig von der Groß- und Kleinschreibung")
	void getByNameIgnoresCase() {
		when(orte.loadValues()).thenReturn(List.of());
		final var cut = new FilterCatalogService(Map.of(FilterCatalog.ORTE, orte));

		assertThat(cut.getByName("orte")).isEmpty();
		assertThat(cut.getByName("ORTE")).isEmpty();
	}

	@Test
	@DisplayName("getByName | wirft NOT_FOUND bei null")
	void getByNameNull() {
		final var cut = new FilterCatalogService(Map.of(FilterCatalog.ORTE, orte));

		assertThatThrownBy(() -> cut.getByName(null))
				.isInstanceOfSatisfying(ApiOperationException.class, e -> assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND));
	}

	@Test
	@DisplayName("getByName | lädt die Werte pro Aufruf genau einmal und liefert eine leere Liste, wenn die Quelle keine Werte hat")
	void getByNameEmpty() {
		when(orte.loadValues()).thenReturn(List.of());
		final var cut = new FilterCatalogService(Map.of(FilterCatalog.ORTE, orte));

		assertThat(cut.getByName("Orte")).isEmpty();
		verify(orte, times(1)).loadValues();
	}

	@Test
	@DisplayName("getByName | reicht eine Exception der Quelle unverändert weiter")
	void getByNamePropagatesSourceError() {
		final var error = new IllegalStateException("Datenbank nicht erreichbar");
		when(orte.loadValues()).thenThrow(error);
		final var cut = new FilterCatalogService(Map.of(FilterCatalog.ORTE, orte));

		assertThatThrownBy(() -> cut.getByName("Orte")).isSameAs(error);
	}
}
