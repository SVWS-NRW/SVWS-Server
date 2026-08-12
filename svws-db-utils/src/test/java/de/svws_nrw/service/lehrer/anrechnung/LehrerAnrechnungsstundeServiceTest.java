package de.svws_nrw.service.lehrer.anrechnung;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für LehrerAnrechnungsstundenService")
class LehrerAnrechnungsstundeServiceTest {

	@Mock
	private LehrerAnrechnungsstundeServiceKontext kontext;

	@InjectMocks
	private LehrerAnrechnungsstundeService service;

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
	void setupTransaction() {
		mockedTransaction = mockStatic(TransactionSupport.class);
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
			final var dto = new DTOLehrerAnrechnungsstunde(id, idAbschnitt);
			dto.AnrechnungStd = 1.5;
			dto.AnrechnungsgrundKrz = "310";

			when(kontext.fetch(List.of(id))).thenReturn(List.of(dto));
			when(kontext.getAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2024, 1));

			final var result = service.get(id);

			assertThat(result).isNotNull();
			assertThat(result.id).isEqualTo(id);
			assertThat(result.anzahl).isEqualTo(1.5);
			final var grundId = LehrerAnrechnungsgrund.data().getWertByKuerzel("310").daten(2024).id;
			assertThat(result.idGrund).isEqualTo(grundId);
		}

		@Test
		@DisplayName("Erfolg, aber: idGrund wird auf null gesetzt, wenn das Kürzel ungültig ist")
		void success_fix_grund_as_null() {
			final var dto = new DTOLehrerAnrechnungsstunde(id, idAbschnitt);
			dto.AnrechnungsgrundKrz = "INVALID_KRZ";

			when(kontext.fetch(List.of(id))).thenReturn(List.of(dto));
			when(kontext.getAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
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
						assertThat(apiEx.getStatus()).isEqualTo(Status.NOT_FOUND);
						assertThat(apiEx.getMessage()).contains("Es wurde kein Eintrag mit der ID 1 gefunden");
					});
		}
	}


	@Nested
	@DisplayName("Validierung von getListByLehrerabschnittsdatenId")
	class GetListByLehrerabschnittsdatenIdTests {

		private final long idAbschnitt = 100L;
		private final long idSchuljahresabschnitt = 200L;

		@Test
		@DisplayName("Erfolg: Lädt alle Einträge zu einem Abschnitt")
		void success() {
			final var dto1 = new DTOLehrerAnrechnungsstunde(1L, idAbschnitt);
			dto1.AnrechnungsgrundKrz = "310";
			final var dto2 = new DTOLehrerAnrechnungsstunde(2L, idAbschnitt);
			dto2.AnrechnungsgrundKrz = "320";

			when(kontext.fetchByLehrerabschnittsdatenId(idAbschnitt)).thenReturn(List.of(dto1, dto2));

			final var abschnitt = new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt);
			final var schuljahr = new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2024, 1);

			when(kontext.getAbschnitt(idAbschnitt)).thenReturn(abschnitt);
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(schuljahr);

			final var result = service.getListByLehrerabschnittsdatenId(idAbschnitt);

			assertThat(result).hasSize(2);
			assertThat(result.stream().map(r -> r.id)).containsExactlyInAnyOrder(1L, 2L);
			verify(kontext).fetchByLehrerabschnittsdatenId(idAbschnitt);
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
			final long idAnrechnungsgrund = 1L;
			final long idAbschnitt = 100L;
			final var patch = new LehrerAnrechnungsstundePatchRequest();
			patch.id = idAnrechnungsgrund;
			patch.anzahl = JsonNullable.of(3.5);

			final var entity = new DTOLehrerAnrechnungsstunde(idAnrechnungsgrund, idAbschnitt);
			entity.AnrechnungStd = 1.0;
			entity.AnrechnungsgrundKrz = "310";
			when(kontext.fetch(List.of(idAnrechnungsgrund))).thenReturn(List.of(entity));
			when(kontext.getAnrechnungsstunden(idAnrechnungsgrund)).thenReturn(entity);

			final var mapped = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
			mapped.id = idAnrechnungsgrund;
			when(kontext.getAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, 200L));
			when(kontext.getSchuljahresabschnitt(200L)).thenReturn(new DTOSchuljahresabschnitte(200L, 2024, 1));

			final var result = service.patchMultiple(List.of(patch));

			assertThat(result).hasSize(1);
			assertThat(entity.AnrechnungStd).isEqualTo(3.5);
			assertThat(entity.AnrechnungsgrundKrz).isEqualTo("310");
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
			final var patch = new LehrerAnrechnungsstundePatchRequest();
			patch.id = id;
			patch.idGrund = JsonNullable.of(999999L);
			final var patches = List.of(patch);

			when(kontext.fetch(List.of(id))).thenReturn(List.of(new DTOLehrerAnrechnungsstunde(id, 100L)));
			when(kontext.getAnrechnungsstunden(id)).thenReturn(new DTOLehrerAnrechnungsstunde(id, 100L));

			assertThatThrownBy(() -> service.patchMultiple(patches))
					.isExactlyInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST)
					.hasMessageContaining("nicht vorhanden");
		}

		@Test
		@DisplayName("Wirft eine Exception (400 - BAD_REQUEST), wenn idGrund in dem Schuljahr nicht gültig ist")
		void patchIdGrundNotValidInYear() {
			final long id = 1L;
			final long idAbschnitt = 100L;
			final long idSchuljahresabschnitt = 200L;
			final int schuljahr = 2025;
			final var patch = new LehrerAnrechnungsstundePatchRequest();
			patch.id = id;
			patch.idGrund = JsonNullable.of(315000L);
			final var patches = List.of(patch);

			when(kontext.fetch(List.of(id))).thenReturn(List.of(new DTOLehrerAnrechnungsstunde(id, idAbschnitt)));
			when(kontext.getAnrechnungsstunden(id)).thenReturn(new DTOLehrerAnrechnungsstunde(id, idAbschnitt));
			when(kontext.getAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, schuljahr, 1));

			assertThatThrownBy(() -> service.patchMultiple(patches))
					.isExactlyInstanceOf(ApiOperationException.class)
					.hasMessageContaining("nicht gültig");
		}

		@Test
		@DisplayName("Wirft eine Exception (400 - BAD_REQUEST), wenn eine ID nicht existiert")
		void patchNotFound() {
			final var patch1 = new LehrerAnrechnungsstundePatchRequest();
			patch1.id = 1L;
			final var patch2 = new LehrerAnrechnungsstundePatchRequest();
			patch2.id = 2L;

			when(kontext.fetch(List.of(1L, 2L))).thenReturn(List.of(new DTOLehrerAnrechnungsstunde(1L, 100L)));
			final var patches = List.of(patch1, patch2);

			assertThatThrownBy(() -> service.patchMultiple(patches))
					.isExactlyInstanceOf(ApiOperationException.class)
					.satisfies(e -> assertThat(((ApiOperationException) e).getStatus()).isEqualTo(Status.NOT_FOUND));
		}

		@Test
		@DisplayName("Erfolg: Patcht nur idGrund, anzahl bleibt unverändert (undefined)")
		void patchOnlyIdGrund() {
			final long idAnrechnungsstunde = 1L;
			final long idAbschnitt = 100L;
			final int jahr = 2024;

			final var patch = new LehrerAnrechnungsstundePatchRequest();
			patch.id = idAnrechnungsstunde;
			final var grund = LehrerAnrechnungsgrund.data().getWertByKuerzel("310");
			final long neueGrundId = grund.daten(jahr).id;
			patch.idGrund = JsonNullable.of(neueGrundId);

			final var entity = new DTOLehrerAnrechnungsstunde(idAnrechnungsstunde, idAbschnitt);
			entity.AnrechnungStd = 8.5;
			entity.AnrechnungsgrundKrz = "OLD";

			when(kontext.fetch(List.of(idAnrechnungsstunde))).thenReturn(List.of(entity));
			when(kontext.getAnrechnungsstunden(idAnrechnungsstunde)).thenReturn(entity);

			final var abschnitt = new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, 200L);
			final var schuljahr = new DTOSchuljahresabschnitte(200L, jahr, 1);
			when(kontext.getAbschnitt(idAbschnitt)).thenReturn(abschnitt);
			when(kontext.getSchuljahresabschnitt(200L)).thenReturn(schuljahr);

			service.patchMultiple(List.of(patch));

			assertThat(entity.AnrechnungsgrundKrz).isEqualTo("310");
			assertThat(entity.AnrechnungStd).isEqualTo(8.5);
		}

		@Test
		@DisplayName("Erfolg: Patch mit zwei undefined Feldern ändert nichts")
		void patchAllUndefined() {
			final long idAnrechnungsstunde = 1L;
			final long idAbschnitt = 100L;
			final long idSchuljahresabschnitt = 200L;
			final var patch = new LehrerAnrechnungsstundePatchRequest();
			patch.id = idAnrechnungsstunde;

			final var entity = new DTOLehrerAnrechnungsstunde(idAnrechnungsstunde, idAbschnitt);
			entity.AnrechnungStd = 5.0;
			entity.AnrechnungsgrundKrz = "310";

			when(kontext.fetch(List.of(idAnrechnungsstunde))).thenReturn(List.of(entity));
			when(kontext.getAnrechnungsstunden(idAnrechnungsstunde)).thenReturn(entity);
			when(kontext.getAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2024, 1));

			final var result = service.patchMultiple(List.of(patch));

			assertThat(result).hasSize(1);
			assertThat(entity.AnrechnungStd).isEqualTo(5.0);
			assertThat(entity.AnrechnungsgrundKrz).isEqualTo("310");
			verify(kontext).persist(List.of(entity));
		}

	}


	@Nested
	@DisplayName("Validierung von create und createMultiple")
	class CreateTests {

		@Test
		@DisplayName("createMultiple: Erstellt erfolgreich neue Einträge aus Patches")
		void createSuccess() {
			// GIVEN
			final long idAbschnitt = 100L;
			final long idSchuljahresabschnitt = 200L;
			final long neueId = 500L;

			final var patch = new LehrerAnrechnungsstundeCreateRequest();
			patch.idAbschnittsdaten = idAbschnitt;
			patch.idGrund = LehrerAnrechnungsgrund.data().getWertByKuerzel("310").daten(2024).id;
			patch.anzahl = 2.5;

			final var neueEntity = new DTOLehrerAnrechnungsstunde(neueId, idAbschnitt);

			when(kontext.create(anyCollection())).thenReturn(List.of(neueEntity));
			when(kontext.fetch(anyList())).thenReturn(List.of(neueEntity));

			when(kontext.getAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
			when(kontext.getSchuljahresabschnitt(idSchuljahresabschnitt)).thenReturn(new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2024, 1));

			final var result = service.createMultiple(List.of(patch));

			assertThat(result).hasSize(1);
			assertThat(result.get(0).id).isEqualTo(neueId);
			assertThat(neueEntity.AnrechnungStd).isEqualTo(2.5);
			assertThat(neueEntity.AnrechnungsgrundKrz).isEqualTo("310");

			verify(kontext).persist(List.of(neueEntity));
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

			final var patch = new LehrerAnrechnungsstundeCreateRequest();
			patch.idAbschnittsdaten = idAbschnitt;
			patch.idGrund = LehrerAnrechnungsgrund.data().getWertByKuerzel("310").daten(jahr).id;
			patch.anzahl = 1.0;

			final var neueEntity = new DTOLehrerAnrechnungsstunde(500L, idAbschnitt);
			when(kontext.create(anyCollection())).thenReturn(List.of(neueEntity));
			when(kontext.fetch(anyList())).thenReturn(List.of(neueEntity));
			when(kontext.getAbschnitt(idAbschnitt)).thenReturn(new DTOLehrerAbschnittsdaten(idAbschnitt, 10L, idSchuljahresabschnitt));
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
			final long sId = 200L;
			final var entity = new DTOLehrerAnrechnungsstunde(id, absId);

			when(kontext.fetch(List.of(id))).thenReturn(List.of(entity));
			when(kontext.getAbschnitt(absId)).thenReturn(new DTOLehrerAbschnittsdaten(absId, 10L, sId));
			when(kontext.getSchuljahresabschnitt(sId)).thenReturn(new DTOSchuljahresabschnitte(sId, 2024, 1));

			final var result = service.deleteMultiple(List.of(id));

			assertThat(result).hasSize(1);
			assertThat(result.get(0).id).isEqualTo(id);
			verify(kontext).fetch(List.of(id));
			verify(kontext).delete(List.of(entity));
		}

		@Test
		@DisplayName("deleteMultiple: Wirft eine Exception (400 - BAD_REQUEST), wenn die ID-Liste null ist")
		void deleteMultipleNull() {
			assertThatThrownBy(() -> service.deleteMultiple(null))
					.isExactlyInstanceOf(ApiOperationException.class)
					.satisfies(e -> assertThat(((ApiOperationException) e).getStatus()).isEqualTo(Status.BAD_REQUEST));
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
			final var entity = new DTOLehrerAnrechnungsstunde(id, 100L);

			when(kontext.fetch(List.of(id))).thenReturn(List.of(entity));
			when(kontext.getAbschnitt(100L)).thenReturn(new DTOLehrerAbschnittsdaten(100L, 10L, 200L));
			when(kontext.getSchuljahresabschnitt(200L)).thenReturn(new DTOSchuljahresabschnitte(200L, 2024, 1));

			final var result = service.delete(id);

			assertThat(result).isNotNull();
			assertThat(result.id).isEqualTo(id);
			verify(kontext).delete(List.of(entity));
		}
	}

}
