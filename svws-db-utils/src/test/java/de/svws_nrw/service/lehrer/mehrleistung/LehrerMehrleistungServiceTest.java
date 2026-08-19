package de.svws_nrw.service.lehrer.mehrleistung;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.mehrleistung.LehrerMehrleistungMapper;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für LehrerMehrleistungService")
class LehrerMehrleistungServiceTest {

	@Mock
	private LehrerMehrleistungServiceKontext kontext;

	private LehrerMehrleistungService service;

	private MockedStatic<TransactionSupport> mockedTransaction;

	/**
	 * Initialisiert die Core-Types, damit die Tests ausgeführt werden können.
	 * Beim Laden der Core-Type-Daten werden die JSON-Dateien auf Plausibilität
	 * geprüft.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setupService() {
		service = new LehrerMehrleistungService(kontext, LehrerMehrleistungMapper.INSTANCE);
	}

	@BeforeEach
	void setupTransaction() {
		mockedTransaction = Mockito.mockStatic(TransactionSupport.class);
		mockedTransaction.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> {
					final java.util.function.Supplier<?> s = invocation.getArgument(0);
					return s.get();
				});
	}

	@AfterEach
	void tearDownTransaction() {
		if (mockedTransaction != null) {
			mockedTransaction.close();
		}
	}

	@Nested
	@DisplayName("Validierung von get")
	class GetTests {

		private final long id = 1L;
		private final long idAbschnitt = 100L;
		private final long idSchuljahresabschnitt = 200L;

		@Test
		@DisplayName("Erfolg: Es wird korrekt gemappt")
		void success() {
			final var dto = new DTOLehrerMehrleistung(id, idAbschnitt, "");
			dto.anzahl = 1.5;
			dto.idGrund = "110";

			when(kontext.fetch(List.of(id))).thenReturn(List.of(dto));
			when(kontext.getLehrerAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2024, 1));

			final var result = service.get(id);
			assertThat(result).isNotNull();
			assertThat(result.id).isEqualTo(id);
			assertThat(result.anzahl).isEqualTo(1.5);
			final var grundId = LehrerMehrleistungsarten.data().getWertByKuerzel("110").daten(2024).id;
			assertThat(result.idGrund).isEqualTo(grundId);
		}

		@Test
		@DisplayName("Erfolg, aber: idGrund wird auf null gesetzt, wenn das Kürzel ungültig ist")
		void successFixGrundAsNull() {
			final var dto = new DTOLehrerMehrleistung(id, idAbschnitt, "");
			dto.idGrund = "INVALID_KRZ";

			when(kontext.fetch(List.of(id))).thenReturn(List.of(dto));
			when(kontext.getLehrerAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2024, 1));

			final var result = service.get(id);

			assertThat(result.idGrund).isNull();
		}

		@Test
		@DisplayName("Wirft eine Exception (404 - NOT_FOUND), wenn die ID nicht existiert")
		void failureNotFound() {
			when(kontext.fetch(List.of(id))).thenReturn(List.of());
			assertThatThrownBy(() -> service.get(id))
					.isExactlyInstanceOf(ApiOperationException.class)
					.satisfies(e -> {
						final var apiEx = (ApiOperationException) e;
						assertThat(apiEx.getStatus()).isEqualTo(Response.Status.NOT_FOUND);
						assertThat(apiEx.getMessage()).contains("Es wurde kein Eintrag mit der ID 1 gefunden");
					});
		}
	}

	@Nested
	@DisplayName("Validierung von getListByLehrerabschnittsdatenId")
	class GetListByLehrerabschnittsdatenIdTests {
		private final long idAbschnitt = 100L;

		@Test
		@DisplayName("Erfolg: Lädt alle Einträge zu einem Abschnitt")
		void success() {
			final var dto1 = new DTOLehrerMehrleistung(1L, idAbschnitt, "");
			dto1.idGrund = "110";
			final var dto2 = new DTOLehrerMehrleistung(2L, idAbschnitt, "");
			dto2.idGrund = "150";

			when(kontext.fetchByLehrerabschnittsdatenId(idAbschnitt)).thenReturn(List.of(dto1, dto2));

			final var abschnitt = new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, 200L);
			final var schuljahr = new DTOSchuljahresabschnitte(200L, 2024, 1);

			when(kontext.getLehrerAbschnitt(idAbschnitt)).thenReturn(abschnitt);
			when(kontext.getSchuljahresabschnitt(200L)).thenReturn(schuljahr);

			final var result = service.getListByLehrerabschnittsdatenId(idAbschnitt);

			assertThat(result).hasSize(2);
			assertThat(result.stream().map(r -> r.id)).containsExactlyInAnyOrder(1L, 2L);
			verify(kontext).fetchByLehrerabschnittsdatenId(idAbschnitt);
		}

		@Test
		@DisplayName("getListByIdLehrerAbschnittsdaten: gruppiert und mappt je Abschnitt")
		void getListByIdLehrerAbschnittsdaten_success() {
			final var ids = List.of(100L, 200L);
			final long sj1 = 300L;
			final long sj2 = 301L;

			final var dtoA1 = new DTOLehrerMehrleistung(1L, 100L, "110");
			final var dtoA2 = new DTOLehrerMehrleistung(2L, 100L, "150");
			final var dtoB1 = new DTOLehrerMehrleistung(3L, 200L, "110");

			when(kontext.fetchMapByAbschnittIds(ids)).thenReturn(Map.of(
					100L, List.of(dtoA1, dtoA2),
					200L, List.of(dtoB1)
			));

			when(kontext.getLehrerAbschnitt(100L)).thenReturn(new DTOLehrerAbschnittsdaten(100L, 10L, sj1));
			when(kontext.getLehrerAbschnitt(200L)).thenReturn(new DTOLehrerAbschnittsdaten(200L, 10L, sj2));
			when(kontext.getSchuljahresabschnitt(sj1)).thenReturn(new DTOSchuljahresabschnitte(sj1, 2024, 1));
			when(kontext.getSchuljahresabschnitt(sj2)).thenReturn(new DTOSchuljahresabschnitte(sj2, 2024, 1));

			final var result = service.getListByIdLehrerAbschnittsdaten(ids);

			assertThat(result).hasSize(2);
			assertThat(result.get(100L)).hasSize(2);
			assertThat(result.get(200L)).hasSize(1);
			assertThat(result.get(100L).stream().map(r -> r.id)).containsExactly(1L, 2L);
			assertThat(result.get(200L).stream().map(r -> r.id)).containsExactly(3L);
		}

		@Test
		@DisplayName("Erfolg: Gibt eine leere Collection zurück, wenn keine Einträge vorhanden sind")
		void emptyList() {
			when(kontext.fetchByLehrerabschnittsdatenId(idAbschnitt)).thenReturn(List.of());

			final var result = service.getListByLehrerabschnittsdatenId(idAbschnitt);

			assertThat(result).isEmpty();
			verify(kontext).fetchByLehrerabschnittsdatenId(idAbschnitt);
		}
	}

	@Nested
	@DisplayName("Validierung von patch und patchMultiple")
	class PatchTests {

		@Test
		@DisplayName("Erfolg: Patcht ein Feld und lässt das andere unverändert")
		void patchSuccess() {
			final long idMehrleistung = 1L;
			final long idAbschnitt = 100L;
			final var patch = new LehrerMehrleistungPatchRequest();
			patch.id = idMehrleistung;
			patch.anzahl = JsonNullable.of(3.5);

			final var entity = new DTOLehrerMehrleistung(idMehrleistung, idAbschnitt, "");
			entity.idGrund = "110";
			entity.anzahl = 1.0;

			when(kontext.fetch(List.of(idMehrleistung))).thenReturn(List.of(entity));
			when(kontext.getMehrleistung(patch.id)).thenReturn(entity);

			when(kontext.getLehrerAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, 200L));
			when(kontext.getSchuljahresabschnitt(200L)).thenReturn(new DTOSchuljahresabschnitte(200L, 2024, 1));

			final var result = service.patchMultiple(List.of(patch));

			assertThat(result).hasSize(1);
			assertThat(entity.anzahl).isEqualTo(3.5);
			assertThat(entity.idGrund).isEqualTo("110");
			verify(kontext).persist(List.of(entity));
		}

		@Test
		@DisplayName("patchMultiple: Gibt leere Liste zurück, wenn keine Patches übergeben werden")
		void patchEmpty() {
			final var result = service.patchMultiple(List.of());
			assertThat(result).isEmpty();
			verifyNoInteractions(kontext);
		}

		@Test
		@DisplayName("Wirft eine Exception (400 - BAD_REQUEST), wenn idGrund nicht existiert")
		void patchIdGrundNotInKatalog() {
			final long id = 1L;
			final var patch = new LehrerMehrleistungPatchRequest();
			patch.id = id;
			patch.idGrund = JsonNullable.of(999999L);
			final var patches = List.of(patch);

			when(kontext.fetch(List.of(id))).thenReturn(List.of(new DTOLehrerMehrleistung(id, 100L, "")));
			when(kontext.getMehrleistung(id)).thenReturn(new DTOLehrerMehrleistung(id, 100L, ""));

			assertThatThrownBy(() -> service.patchMultiple(patches))
					.isExactlyInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("nicht vorhanden");
		}

		@Test
		@DisplayName("Wirft eine Exception (400 - BAD_REQUEST), wenn idGrund in dem Schuljahr nicht gültig ist")
		void patchIdGrundNotValidInYear() {
			final long id = 1L;
			final long idAbschnitt = 100L;
			final long idSchuljahresabschnitt = 200L;
			final int schuljahr = 2025;
			final var patch = new LehrerMehrleistungPatchRequest();
			patch.id = id;
			// ID 5 = LehrerMehrleistungsarten "Mehrarbeit allg.", nur bis einschließlich Schuljahr 2024 gültig
			patch.idGrund = JsonNullable.of(5L);
			final var patches = List.of(patch);

			when(kontext.fetch(List.of(id))).thenReturn(List.of(new DTOLehrerMehrleistung(id, idAbschnitt, "")));
			when(kontext.getMehrleistung(id)).thenReturn(new DTOLehrerMehrleistung(id, idAbschnitt, ""));
			when(kontext.getLehrerAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, schuljahr, 1));

			assertThatThrownBy(() -> service.patchMultiple(patches))
					.isExactlyInstanceOf(ApiOperationException.class)
					.hasMessageContaining("nicht gültig");
		}

		@Test
		@DisplayName("Wirft eine Exception (404 - NOT_FOUND), wenn eine ID nicht existiert")
		void patchNotFound() {
			final var patch1 = new LehrerMehrleistungPatchRequest();
			patch1.id = 1L;
			final var patch2 = new LehrerMehrleistungPatchRequest();
			patch2.id = 2L;

			when(kontext.fetch(List.of(1L, 2L))).thenReturn(List.of(new DTOLehrerMehrleistung(1L, 100L, "")));
			final var patches = List.of(patch1, patch2);

			assertThatThrownBy(() -> service.patchMultiple(patches))
					.isExactlyInstanceOf(ApiOperationException.class)
					.satisfies(e -> assertThat(((ApiOperationException) e).getStatus()).isEqualTo(Response.Status.NOT_FOUND));
		}

		@Test
		@DisplayName("Erfolg: Patcht nur idGrund, Anzahl bleibt unverändert (undefined)")
		void patchOnlyIdGrund() {
			final long idMehrleistung = 1L;
			final long idAbschnitt = 100L;
			final int jahr = 2024;

			final var patch = new LehrerMehrleistungPatchRequest();
			patch.id = idMehrleistung;
			final var neugrundId = LehrerMehrleistungsarten.data().getWertByKuerzel("110").daten(jahr).id;
			patch.idGrund = JsonNullable.of(neugrundId);

			final var entity = new DTOLehrerMehrleistung(idMehrleistung, idAbschnitt, "");
			entity.anzahl = 8.5;
			entity.idGrund = "ALT";

			when(kontext.fetch(List.of(idMehrleistung))).thenReturn(List.of(entity));
			when(kontext.getMehrleistung(idMehrleistung)).thenReturn(entity);

			final var abschnitt = new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, 200L);
			final var schuljahr = new DTOSchuljahresabschnitte(200L, jahr, 1);
			when(kontext.getLehrerAbschnitt(idAbschnitt)).thenReturn(abschnitt);
			when(kontext.getSchuljahresabschnitt(200L)).thenReturn(schuljahr);

			service.patchMultiple(List.of(patch));

			assertThat(entity.idGrund).isEqualTo("110");
			assertThat(entity.anzahl).isEqualTo(8.5);
		}

		@Test
		@DisplayName("Erfolg: Patch mit zwei undefined Feldern ändert nichts")
		void patchAllUndefined() {
			final long idMehrleistung = 1L;
			final long idAbschnitt = 100L;
			final long idSchuljahresabschnitt = 200L;
			final var patch = new LehrerMehrleistungPatchRequest();
			patch.id = idMehrleistung;

			final var entity = new DTOLehrerMehrleistung(idMehrleistung, idAbschnitt, "");
			entity.anzahl = 5.0;
			entity.idGrund = "110";

			when(kontext.fetch(List.of(idMehrleistung))).thenReturn(List.of(entity));
			when(kontext.getMehrleistung(idMehrleistung)).thenReturn(entity);
			when(kontext.getLehrerAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2024, 1));

			final var result = service.patchMultiple(List.of(patch));

			assertThat(result).hasSize(1);
			assertThat(entity.anzahl).isEqualTo(5.0);
			assertThat(entity.idGrund).isEqualTo("110");
			verify(kontext).persist(List.of(entity));
		}

	}

	@Nested
	@DisplayName("Validierung von create und createMultiple")
	class CreateTests {

		@Test
		@DisplayName("createMultiple: Erstellt erfolgreich neue Einträge aus Patches")
		void createSuccess() {
			final long idAbschnitt = 100L;
			final long idSchuljahresabschnitt = 200L;
			final long neueId = 500L;

			final var patch = new LehrerMehrleistungCreateRequest();
			patch.idAbschnittsdaten = idAbschnitt;
			patch.idGrund = LehrerMehrleistungsarten.data().getWertByKuerzel("110").daten(2024).id;
			patch.anzahl = 2.5;

			final var neueEntity = new DTOLehrerMehrleistung(neueId, idAbschnitt, "");

			when(kontext.create(anyCollection())).thenReturn(List.of(neueEntity));
			when(kontext.fetch(anyList())).thenReturn(List.of(neueEntity));

			when(kontext.getLehrerAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2024, 1));

			final var result = service.createMultiple(List.of(patch));

			assertThat(result).hasSize(1);
			assertThat(result.getFirst().id).isEqualTo(neueId);
			assertThat(neueEntity.anzahl).isEqualTo(2.5);
			assertThat(neueEntity.idGrund).isEqualTo("110");

			verify(kontext).persist(List.of(neueEntity));
		}

		@Test
		@DisplayName("Wirft eine Exception (400 - BAD_REQUEST), wenn idGrund beim Erstellen nicht im Katalog existiert")
		void createIdGrundNotInKatalog() {
			final long idAbschnitt = 100L;

			final var request = new LehrerMehrleistungCreateRequest();
			request.idAbschnittsdaten = idAbschnitt;
			request.idGrund = 999999L;
			request.anzahl = 1.0;

			final var neueEntity = new DTOLehrerMehrleistung(500L, idAbschnitt, "");
			when(kontext.create(anyCollection())).thenReturn(List.of(neueEntity));
			final var requests = List.of(request);

			assertThatThrownBy(() -> service.createMultiple(requests))
					.isExactlyInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("nicht vorhanden");
		}

		@Test
		@DisplayName("Wirft eine Exception (400 - BAD_REQUEST), wenn idGrund beim Erstellen im Schuljahr nicht gültig ist")
		void createIdGrundNotValidInYear() {
			final long idAbschnitt = 100L;
			final long idSchuljahresabschnitt = 200L;

			final var request = new LehrerMehrleistungCreateRequest();
			request.idAbschnittsdaten = idAbschnitt;
			// ID 5 = LehrerMehrleistungsarten "Mehrarbeit allg.", nur bis einschließlich Schuljahr 2024 gültig
			request.idGrund = 5L;
			request.anzahl = 1.0;

			final var neueEntity = new DTOLehrerMehrleistung(500L, idAbschnitt, "");
			when(kontext.create(anyCollection())).thenReturn(List.of(neueEntity));
			when(kontext.getLehrerAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2025, 1));
			final var requests = List.of(request);

			assertThatThrownBy(() -> service.createMultiple(requests))
					.isExactlyInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessageContaining("nicht gültig");
		}

		@Test
		@DisplayName("createMultiple: Gibt leere Liste zurück bei leeren Eingabedaten")
		void createEmpty() {
			final var result = service.createMultiple(List.of());
			assertThat(result).isEmpty();
			verifyNoInteractions(kontext);
		}

		@Test
		@DisplayName("create: Delegiert an createMultiple")
		void createSingle() {
			final long idAbschnitt = 100L;
			final long idSchuljahresabschnitt = 200L;
			final int jahr = 2024;

			final var patch = new LehrerMehrleistungCreateRequest();
			patch.idAbschnittsdaten = idAbschnitt;
			patch.idGrund = LehrerMehrleistungsarten.data().getWertByKuerzel("110").daten(jahr).id;
			patch.anzahl = 1.0;

			final var neueEntity = new DTOLehrerMehrleistung(500L, idAbschnitt, "");
			when(kontext.create(anyCollection())).thenReturn(List.of(neueEntity));
			when(kontext.fetch(anyList())).thenReturn(List.of(neueEntity));
			when(kontext.getLehrerAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, jahr, 1));

			final var result = service.create(patch);

			assertThat(result).isNotNull();
			assertThat(result.id).isEqualTo(500L);
			verify(kontext).persist(List.of(neueEntity));
		}

	}

	@Nested
	@DisplayName("Validierung von delete und deleteMultiple")
	class DeleteTests {

		@Test
		@DisplayName("deleteMultiple: Löscht mehrere Entitäten und gibt diese zurück")
		void deleteMultipleSuccess() {
			final long id = 1L;
			final long absId = 100L;
			final var entity = new DTOLehrerMehrleistung(id, absId, "");

			when(kontext.fetch(List.of(id))).thenReturn(List.of(entity));

			final var result = service.deleteMultiple(List.of(id));

			assertThat(result).hasSize(1);
			assertThat(result.getFirst().id).isEqualTo(id);
			verify(kontext).fetch(List.of(id));
			verify(kontext).delete(List.of(entity));
		}

		@Test
		@DisplayName("deleteMultiple: Wirft eine Exception (400 - BAD_REQUEST), wenn die ID-Liste null ist")
		void deleteMultipleNull() {
			assertThatThrownBy(() -> service.deleteMultiple(null))
					.isExactlyInstanceOf(ApiOperationException.class)
					.satisfies(e -> assertThat(((ApiOperationException) e).getStatus()).isEqualTo(Response.Status.BAD_REQUEST));
		}

		@Test
		@DisplayName("deleteMultiple: Gibt leere Liste zurück, wenn ID-Liste leer ist")
		void deleteMultipleEmpty() {
			final var result = service.deleteMultiple(List.of());
			assertThat(result).isEmpty();
			verify(kontext).fetch(List.of());
			verify(kontext).delete(List.of());
		}

		@Test
		@DisplayName("delete: Delegiert an deleteMultiple")
		void deleteSingle() {
			final long id = 1L;
			final var entity = new DTOLehrerMehrleistung(id, 100L, "");

			when(kontext.fetch(List.of(id))).thenReturn(List.of(entity));

			final var result = service.delete(id);

			assertThat(result).isNotNull();
			assertThat(result.id).isEqualTo(id);
			verify(kontext).delete(List.of(entity));
		}
	}

}
