package de.svws_nrw.data.schule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Testet die Klasse {@link DataSchuleStammdaten} für die Statistik-Felder der Schule.
 */
@DisplayName("Diese Klasse testet die Klasse DataSchuleStammdaten")
@ExtendWith(MockitoExtension.class)
class DataSchuleStammdatenTest {

	@Mock
	private DBEntityManager conn;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	private static DTOEigeneSchule createSchule() {
		final var schule = new DTOEigeneSchule(1L, 1L, 3L, false, 1L, false, false, false, 1L, 0L);
		schule.SchulNr = 123456;
		schule.Schuljahresabschnitts_ID = 14L;
		schule.AnzahlAbschnitte = 2;
		return schule;
	}

	private Response patch(final String json) {
		return new DataSchuleStammdaten(conn).patch(1L, new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	@DisplayName("get: Die Statistik-Felder werden aus der Tabelle EigeneSchule übernommen")
	void testGetUebernimmtStatistikFelder() {
		final var schule = createSchule();
		schule.idGebundenerGanztag = 4L;
		schule.idFormOffenerGanztag = 2L;
		schule.istOffenerGanztag = true;
		schule.JVAZeigen = true;
		schule.idBilingualerUnterricht = 3L;
		schule.hatRealschuleHauptschulbildungsgang = true;
		schule.hatInternationaleKontakte = true;
		schule.hatKonfessionelleKooperation = true;
		schule.idTalentschule = 2L;
		schule.idReformpaedagogik = 3000L;
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(schule);
		final var abschnitt = mock(DTOSchuljahresabschnitte.class);
		when(conn.queryByKey(DTOSchuljahresabschnitte.class, 14L)).thenReturn(abschnitt);

		final SchuleStammdaten daten = DataSchuleStammdaten.getStammdaten(conn);

		assertThat(daten.idGebundenerGanztag).isEqualTo(4L);
		assertThat(daten.idFormOffenerGanztag).isEqualTo(2L);
		assertThat(daten.istOffenerGanztag).isTrue();
		assertThat(daten.istJva).isTrue();
		assertThat(daten.idBilingualerUnterricht).isEqualTo(3L);
		assertThat(daten.hatRealschuleHauptschulbildungsgang).isTrue();
		assertThat(daten.hatInternationaleKontakte).isTrue();
		assertThat(daten.hatKonfessionelleKooperation).isTrue();
		assertThat(daten.idTalentschule).isEqualTo(2L);
		assertThat(daten.idReformpaedagogik).isEqualTo(3000L);
	}

	@Test
	@DisplayName("get: Fehlende Boolean-Werte (null) werden als false übernommen")
	void testGetNullBooleanWirdFalse() {
		final var schule = createSchule();
		schule.istOffenerGanztag = null;
		schule.JVAZeigen = null;
		schule.hatRealschuleHauptschulbildungsgang = null;
		schule.hatInternationaleKontakte = null;
		schule.hatKonfessionelleKooperation = null;
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(schule);
		final var abschnitt = mock(DTOSchuljahresabschnitte.class);
		when(conn.queryByKey(DTOSchuljahresabschnitte.class, 14L)).thenReturn(abschnitt);

		final SchuleStammdaten daten = DataSchuleStammdaten.getStammdaten(conn);

		assertThat(daten.istOffenerGanztag).isFalse();
		assertThat(daten.istJva).isFalse();
		assertThat(daten.hatRealschuleHauptschulbildungsgang).isFalse();
		assertThat(daten.hatInternationaleKontakte).isFalse();
		assertThat(daten.hatKonfessionelleKooperation).isFalse();
	}

	@Test
	@DisplayName("patch: Gültige Werte werden in die Tabelle EigeneSchule übernommen")
	void testPatchGueltigeWerte() {
		final var schule = createSchule();
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(schule);
		when(conn.transactionPersist(any())).thenReturn(true);

		final Response response = patch("""
				{"idGebundenerGanztag": 4, "idFormOffenerGanztag": 2, "istOffenerGanztag": true, "istJva": true,
				 "idBilingualerUnterricht": 3, "hatRealschuleHauptschulbildungsgang": true, "hatInternationaleKontakte": true,
				 "hatKonfessionelleKooperation": true, "idTalentschule": 2, "idReformpaedagogik": 3000}
				""");

		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		assertThat(schule.idGebundenerGanztag).isEqualTo(4L);
		assertThat(schule.idFormOffenerGanztag).isEqualTo(2L);
		assertThat(schule.istOffenerGanztag).isTrue();
		assertThat(schule.JVAZeigen).isTrue();
		assertThat(schule.idBilingualerUnterricht).isEqualTo(3L);
		assertThat(schule.hatRealschuleHauptschulbildungsgang).isTrue();
		assertThat(schule.hatInternationaleKontakte).isTrue();
		assertThat(schule.hatKonfessionelleKooperation).isTrue();
		assertThat(schule.idTalentschule).isEqualTo(2L);
		assertThat(schule.idReformpaedagogik).isEqualTo(3000L);
		verify(conn).transactionPersist(schule);
	}

	@Test
	@DisplayName("patch: Ungültige Katalog-IDs führen zu BAD_REQUEST und werden nicht gespeichert")
	void testPatchUngueltigeKatalogIds() {
		for (final String attribut : new String[] { "idGebundenerGanztag", "idFormOffenerGanztag", "idBilingualerUnterricht", "idTalentschule",
				"idReformpaedagogik" }) {
			final var schule = createSchule();
			when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(schule);

			assertThatThrownBy(() -> patch("{\"" + attribut + "\": 99999}"))
					.as(attribut)
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		}
		verify(conn, never()).transactionPersist(any());
	}

	@Test
	@DisplayName("patch: Ein einzelnes Attribut ändert nur dieses Attribut")
	void testPatchAendertNurDasAngegebeneAttribut() {
		final var schule = createSchule();
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(schule);
		when(conn.transactionPersist(any())).thenReturn(true);

		patch("{\"idTalentschule\": 2}");

		assertThat(schule.idTalentschule).isEqualTo(2L);
		assertThat(schule.idGebundenerGanztag).isEqualTo(1L);
		assertThat(schule.idFormOffenerGanztag).isEqualTo(3L);
		assertThat(schule.idBilingualerUnterricht).isEqualTo(1L);
		assertThat(schule.idReformpaedagogik).isZero();
		assertThat(schule.istOffenerGanztag).isFalse();
		assertThat(schule.JVAZeigen).isNull();
	}

	@ParameterizedTest(name = "{0} = {1} ist gültig")
	@CsvSource({
		"idGebundenerGanztag, 1", "idGebundenerGanztag, 4",
		"idFormOffenerGanztag, 1", "idFormOffenerGanztag, 3",
		"idBilingualerUnterricht, 1", "idBilingualerUnterricht, 4",
		"idTalentschule, 1", "idTalentschule, 3",
		"idReformpaedagogik, 0", "idReformpaedagogik, 1000", "idReformpaedagogik, 5000"
	})
	@DisplayName("patch: Gültige Katalog-IDs an den Rändern der Kataloge werden akzeptiert")
	void testPatchGueltigeKatalogIdsAnDenRaendern(final String attribut, final long id) {
		final var schule = createSchule();
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(schule);
		when(conn.transactionPersist(any())).thenReturn(true);

		final Response response = patch("{\"" + attribut + "\": " + id + "}");

		assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		verify(conn).transactionPersist(schule);
	}

	@ParameterizedTest(name = "{0} = null ist nicht erlaubt")
	@ValueSource(strings = { "idGebundenerGanztag", "idFormOffenerGanztag", "idBilingualerUnterricht", "idTalentschule", "idReformpaedagogik",
			"istOffenerGanztag", "istJva", "hatRealschuleHauptschulbildungsgang", "hatInternationaleKontakte", "hatKonfessionelleKooperation" })
	@DisplayName("patch: Der Wert null wird für die neuen Attribute abgelehnt (BAD_REQUEST)")
	void testPatchNullWirdAbgelehnt(final String attribut) {
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(createSchule());

		assertThatThrownBy(() -> patch("{\"" + attribut + "\": null}"))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		verify(conn, never()).transactionPersist(any());
	}

	@ParameterizedTest(name = "{0} mit einem Text statt Boolean wird abgelehnt")
	@ValueSource(strings = { "istOffenerGanztag", "istJva", "hatRealschuleHauptschulbildungsgang", "hatInternationaleKontakte",
			"hatKonfessionelleKooperation" })
	@DisplayName("patch: Ein Wert, der kein Boolean ist, wird für die Boolean-Attribute abgelehnt (BAD_REQUEST)")
	void testPatchBooleanMitUngueltigemTyp(final String attribut) {
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(createSchule());

		assertThatThrownBy(() -> patch("{\"" + attribut + "\": \"vielleicht\"}"))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		verify(conn, never()).transactionPersist(any());
	}

	@Test
	@DisplayName("patch: Bei einem ungültigen Attribut in einer Anfrage mit mehreren Attributen wird nichts gespeichert")
	void testPatchUngueltigesAttributVerhindertSpeichern() {
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(createSchule());

		assertThatThrownBy(() -> patch("{\"idTalentschule\": 2, \"idReformpaedagogik\": 99999}"))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		verify(conn, never()).transactionPersist(any());
	}
}
