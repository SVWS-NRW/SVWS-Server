package de.svws_nrw.service.schule.teilstandort;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.core.data.schule.Teilstandort;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schule.teilstandort.TeilstandortMapper;
import de.svws_nrw.repo.schule.teilstandort.TeilstandortRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeilstandortServiceTest {

	@Mock
	private TeilstandortRepository repository;

	@Mock
	private TeilstandortMapper mapper;

	private TeilstandortService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOTeilstandorte entityA;
	private DTOTeilstandorte entityB;
	private Teilstandort apiModelA;
	private Teilstandort apiModelB;

	@BeforeEach
	void setUp() {
		entityA = new DTOTeilstandorte("A");
		entityB = new DTOTeilstandorte("B");

		apiModelA = new Teilstandort();
		apiModelA.adrMerkmal = "A";

		apiModelB = new Teilstandort();
		apiModelB.adrMerkmal = "B";

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		service = new TeilstandortService(repository, mapper);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private TeilstandortCreateRequest createRequest() {
		final var request = new TeilstandortCreateRequest();
		request.adrMerkmal = "A";
		request.plz = "53840";
		request.ort = "Troisdorf";
		return request;
	}

	@Nested
	@DisplayName("getAll")
	class GetAll {

		@Test
		@DisplayName("Gibt leere Liste zurück wenn keine Teilstandorte vorhanden")
		void getAll_leer() {
			when(repository.getAll()).thenReturn(List.of());
			when(repository.getReferencedIds(Set.of())).thenReturn(Set.of());

			assertThat(service.getAll()).isEmpty();
			verify(mapper, never()).toApi(any());
		}

		@Test
		@DisplayName("Gibt alle Teilstandorte gemappt zurück und setzt referenziertInAnderenTabellen korrekt")
		void getAll() {
			when(repository.getAll()).thenReturn(List.of(entityA, entityB));
			when(repository.getReferencedIds(Set.of("A", "B"))).thenReturn(Set.of("B"));
			when(mapper.toApi(entityA)).thenReturn(apiModelA);
			when(mapper.toApi(entityB)).thenReturn(apiModelB);

			final var result = service.getAll();

			assertThat(result).containsExactly(apiModelA, apiModelB);
			assertThat(apiModelA.referenziertInAnderenTabellen).isFalse();
			assertThat(apiModelB.referenziertInAnderenTabellen).isTrue();
		}

		@Test
		@DisplayName("Fragt die Referenzen nur einmal ab, unabhängig von der Anzahl der Teilstandorte")
		void getAll_einmaligeReferenzabfrage() {
			when(repository.getAll()).thenReturn(List.of(entityA, entityB));
			when(repository.getReferencedIds(Set.of("A", "B"))).thenReturn(Set.of());
			when(mapper.toApi(entityA)).thenReturn(apiModelA);
			when(mapper.toApi(entityB)).thenReturn(apiModelB);

			service.getAll();

			verify(repository, times(1)).getReferencedIds(anyCollection());
		}
	}

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("Legt Teilstandort korrekt an")
		void create() {
			final var request = createRequest();

			when(repository.existsById("A")).thenReturn(false);
			when(mapper.toDomain(request)).thenReturn(entityA);
			when(repository.create(entityA)).thenReturn(entityA);
			when(mapper.toApi(entityA)).thenReturn(apiModelA);

			final var result = service.create(request);

			assertThat(result).isEqualTo(apiModelA);
			verify(repository).create(entityA);
		}

		@Test
		@DisplayName("Prüft die Existenz, bevor der Teilstandort angelegt wird")
		void create_prueftExistenzVorDemAnlegen() {
			final var request = createRequest();

			when(repository.existsById("A")).thenReturn(false);
			when(mapper.toDomain(request)).thenReturn(entityA);
			when(repository.create(entityA)).thenReturn(entityA);
			when(mapper.toApi(entityA)).thenReturn(apiModelA);

			service.create(request);

			final var reihenfolge = inOrder(repository);
			reihenfolge.verify(repository).existsById("A");
			reihenfolge.verify(repository).create(entityA);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST mit vollständiger Meldung wenn das Adressmerkmal bereits vergeben ist")
		void create_adrMerkmalExistiertBereits() {
			final var request = createRequest();

			when(repository.existsById("A")).thenReturn(true);

			assertThatException()
					.isThrownBy(() -> service.create(request))
					.isInstanceOf(ApiOperationException.class)
					.withMessage("Ein Teilstandort mit dem AdrMerkmal A existiert bereits.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verify(repository, never()).create(any(DTOTeilstandorte.class));
			verify(mapper, never()).toDomain(any());
		}

		@Test
		@DisplayName("Setzt referenziertInAnderenTabellen bei einem neuen Teilstandort nicht")
		void create_neuerTeilstandortIstNichtReferenziert() {
			final var request = createRequest();

			when(repository.existsById("A")).thenReturn(false);
			when(mapper.toDomain(request)).thenReturn(entityA);
			when(repository.create(entityA)).thenReturn(entityA);
			when(mapper.toApi(entityA)).thenReturn(apiModelA);

			final var result = service.create(request);

			assertThat(result.referenziertInAnderenTabellen).isFalse();
		}
	}

	@Nested
	@DisplayName("patch")
	class Patch {

		private TeilstandortPatchRequest patchRequest() {
			final var request = new TeilstandortPatchRequest();
			request.plz = JsonNullable.of("40213");
			return request;
		}

		@Test
		@DisplayName("Patcht den Teilstandort und gibt ihn gemappt zurück")
		void patch() {
			final var request = patchRequest();

			when(repository.findById("A")).thenReturn(Optional.of(entityA));
			when(mapper.toApi(entityA)).thenReturn(apiModelA);

			final var result = service.patch("A", request);

			assertThat(result).isEqualTo(apiModelA);
			verify(mapper).patch(request, entityA);
		}

		@Test
		@DisplayName("Wendet den Patch an, bevor die Entity gemappt wird")
		void patch_mapptErstNachDemPatch() {
			final var request = patchRequest();

			when(repository.findById("A")).thenReturn(Optional.of(entityA));
			when(mapper.toApi(entityA)).thenReturn(apiModelA);

			service.patch("A", request);

			final var reihenfolge = inOrder(mapper);
			reihenfolge.verify(mapper).patch(request, entityA);
			reihenfolge.verify(mapper).toApi(entityA);
		}

		@Test
		@DisplayName("Wirft NOT_FOUND mit vollständiger Meldung bei unbekanntem Adressmerkmal")
		void patch_unbekanntesAdrMerkmal() {
			final var request = patchRequest();

			when(repository.findById("Z")).thenReturn(Optional.empty());

			assertThatException()
					.isThrownBy(() -> service.patch("Z", request))
					.isInstanceOf(ApiOperationException.class)
					.withMessage("Ein Teilstandort mit dem AdrMerkmal Z existiert nicht.")
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);

			verify(mapper, never()).patch(any(), any());
			verify(mapper, never()).toApi(any());
		}

		@Test
		@DisplayName("Setzt referenziertInAnderenTabellen beim Patchen nicht")
		void patch_setztReferenziertInAnderenTabellenNicht() {
			final var request = patchRequest();

			when(repository.findById("A")).thenReturn(Optional.of(entityA));
			when(mapper.toApi(entityA)).thenReturn(apiModelA);

			final var result = service.patch("A", request);

			assertThat(result.referenziertInAnderenTabellen).isFalse();
			verify(repository, never()).getReferencedIds(anyCollection());
		}

		@Test
		@DisplayName("Greift im Repository nur lesend per findById zu")
		void patch_greiftNurPerFindByIdAufDasRepositoryZu() {
			final var request = patchRequest();

			when(repository.findById("A")).thenReturn(Optional.of(entityA));
			when(mapper.toApi(entityA)).thenReturn(apiModelA);

			service.patch("A", request);

			verify(repository).findById("A");
			verifyNoMoreInteractions(repository);
		}
	}

	@Nested
	@DisplayName("deleteMultiple")
	class DeleteMultiple {

		@Test
		@DisplayName("Löscht nicht referenzierte Teilstandorte und meldet Erfolg")
		void deleteMultiple() {
			final var adrMerkmale = List.of("A", "B");

			when(repository.getReferencedIds(adrMerkmale)).thenReturn(Set.of());
			when(repository.findById("A")).thenReturn(Optional.of(entityA));
			when(repository.findById("B")).thenReturn(Optional.of(entityB));

			final var result = service.deleteMultiple(adrMerkmale);

			assertThat(result).hasSize(2);
			assertThat(result).allSatisfy(response -> {
				assertThat(response.success).isTrue();
				assertThat(response.log).isEmpty();
				assertThat(response.id).isNull();
			});
			verify(repository).delete(entityA);
			verify(repository).delete(entityB);
		}

		@Test
		@DisplayName("Löscht einen referenzierten Teilstandort nicht und meldet es im Log")
		void deleteMultiple_referenziert() {
			final var adrMerkmale = List.of("A");

			when(repository.getReferencedIds(adrMerkmale)).thenReturn(Set.of("A"));

			final var result = service.deleteMultiple(adrMerkmale);

			assertThat(result).hasSize(1);
			assertThat(result.get(0).success).isFalse();
			assertThat(result.get(0).log)
					.containsExactly("Der Teilstandort mit dem AdrMerkmal A kann nicht gelöscht werden, da er in einer Klasse referenziert wird.");
			verify(repository, never()).findById(any());
			verify(repository, never()).delete(any(DTOTeilstandorte.class));
		}

		@Test
		@DisplayName("Meldet einen unbekannten Teilstandort im Log, ohne einen Fehler zu werfen")
		void deleteMultiple_nichtGefunden() {
			final var adrMerkmale = List.of("Z");

			when(repository.getReferencedIds(adrMerkmale)).thenReturn(Set.of());
			when(repository.findById("Z")).thenReturn(Optional.empty());

			final var result = service.deleteMultiple(adrMerkmale);

			assertThat(result).hasSize(1);
			assertThat(result.get(0).success).isFalse();
			assertThat(result.get(0).log).containsExactly("Ein Teilstandort mit dem AdrMerkmal Z existiert nicht.");
			verify(repository, never()).delete(any(DTOTeilstandorte.class));
		}

		@Test
		@DisplayName("Behandelt jeden Eintrag einzeln und liefert die Antworten in der Reihenfolge der Eingabe")
		void deleteMultiple_gemischteListe() {
			final var adrMerkmale = List.of("A", "B", "Z");

			when(repository.getReferencedIds(adrMerkmale)).thenReturn(Set.of("A"));
			when(repository.findById("B")).thenReturn(Optional.of(entityB));
			when(repository.findById("Z")).thenReturn(Optional.empty());

			final var result = service.deleteMultiple(adrMerkmale);

			assertThat(result).extracting(response -> response.success).containsExactly(false, true, false);
			verify(repository).delete(entityB);
			verify(repository, never()).delete(entityA);
		}

		@Test
		@DisplayName("Fragt die Referenzen nur einmal ab, unabhängig von der Anzahl der Adressmerkmale")
		void deleteMultiple_einmaligeReferenzabfrage() {
			final var adrMerkmale = List.of("A", "B");

			when(repository.getReferencedIds(adrMerkmale)).thenReturn(Set.of());
			when(repository.findById("A")).thenReturn(Optional.of(entityA));
			when(repository.findById("B")).thenReturn(Optional.of(entityB));

			service.deleteMultiple(adrMerkmale);

			verify(repository, times(1)).getReferencedIds(anyCollection());
		}

		@Test
		@DisplayName("Gibt bei einer leeren Liste eine leere Liste zurück und greift nicht auf das Repository zu")
		void deleteMultiple_leereListe() {
			assertThat(service.deleteMultiple(List.of())).isEmpty();

			verifyNoInteractions(repository);
		}

		@Test
		@DisplayName("Wirft BAD_REQUEST mit vollständiger Meldung, wenn die Liste null ist")
		void deleteMultiple_null() {
			assertThatException()
					.isThrownBy(() -> service.deleteMultiple(null))
					.isInstanceOf(ApiOperationException.class)
					.withMessage("Für das Löschen müssen Adressmerkmale angegeben werden. Null ist nicht zulässig.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

			verifyNoInteractions(repository);
		}
	}
}
