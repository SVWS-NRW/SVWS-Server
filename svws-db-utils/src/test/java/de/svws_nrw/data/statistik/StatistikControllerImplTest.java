package de.svws_nrw.data.statistik;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@ExtendWith(MockitoExtension.class)
class StatistikControllerImplTest {

	@Mock
	private StatistikService serviceMock;

	@InjectMocks
	private StatistikControllerImpl controller;

	@Test
	@DisplayName("Test: getStatistikGesamt ruft den Service auf gibt eine OK-Response zurück")
	void testGetStatistikGesamtSuccess() {
		// Erzeugen von Testdaten
		final KlassenStatistikGesamt klasse1 = new KlassenStatistikGesamt();
		klasse1.kuerzel = "ABC";
		final KlassenStatistikGesamt klasse2 = new KlassenStatistikGesamt();
		klasse2.kuerzel = "DEF";

		// Mocking des Services
		final StatistikGesamt daten = new StatistikGesamt();
		daten.klassen = List.of(klasse1, klasse2);
		when(serviceMock.get()).thenReturn(daten);

		// ... Aufruf der Controller-Methode ...
		final Response response = controller.getStatistikGesamt();

		// ... und Überprüfung der Response
		assertNotNull(response, "Die gelieferte Response darf nicht null sein.");
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertThat(response.getEntity()).asInstanceOf(InstanceOfAssertFactories.type(StatistikGesamt.class))
				.extracting("klassen").asInstanceOf(InstanceOfAssertFactories.list(KlassenStatistikGesamt.class))
				.containsExactlyInAnyOrder(klasse1, klasse2);
		verify(serviceMock).get();
	}

	@Test
	@DisplayName("Test: getStatistikGesamt ruft den Service auf und es tritt einen Fehler auf")
	void testGetStatistikGesamtFailure() {
		// Mocking des Services mit einem unerwarteten Fehler als RuntimeException
		final RuntimeException exception = new RuntimeException("Fehler bei Statistik-Aggregation, z.B. ein Datenbank-Fehler");
		when(serviceMock.get()).thenThrow(exception);

		// Der Aufruf des Controller muss genau diese Exception werfen
		final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			controller.getStatistikGesamt();
		});

		assertEquals("Fehler bei Statistik-Aggregation, z.B. ein Datenbank-Fehler", thrown.getMessage());
		verify(serviceMock).get();
	}

}
