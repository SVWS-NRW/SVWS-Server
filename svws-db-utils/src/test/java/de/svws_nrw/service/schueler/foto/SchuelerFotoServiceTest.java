package de.svws_nrw.service.schueler.foto;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.svws_nrw.mapper.schueler.foto.SchuelerFotoMapper;
import de.svws_nrw.repo.schueler.foto.SchuelerFotoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuelerFotoServiceTest {

	@Mock
	private SchuelerFotoRepository repo;

	@Mock
	private SchuelerFotoMapper mapper;

	private SchuelerFotoService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	/** Wiederverwendbare Test-Entity mit idSchueler=1 */
	private DTOSchuelerFoto entity;

	/** Wiederverwendbares Domain-Record mit idSchueler=1 */
	private SchuelerFoto domain;

	@BeforeEach
	void setUp() {
		entity = new DTOSchuelerFoto(1L);
		entity.fotoBase64 = "abc123";

		domain = new SchuelerFoto(1L, "abc123");

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		service = new SchuelerFotoService(repo, mapper);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	// -------------------------------------------------------------------------
	// getByIdSchueler
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getByIdSchueler")
	class GetByIdSchueler {

		@Test
		@DisplayName("Gibt gemapptes Domain-Objekt zurück")
		void getByIdSchueler_gibtDomainZurueck() {
			when(repo.findById(1L)).thenReturn(Optional.of(entity));
			when(mapper.toDomain(entity)).thenReturn(domain);

			final var result = service.findByIdSchueler(1L);

			assertThat(result).contains(domain);
			verify(repo).findById(1L);
			verify(mapper).toDomain(entity);
		}

		@Test
		@DisplayName("Gibt leeres Optional zurück wenn kein Foto vorhanden")
		void getByIdSchueler_keinFoto() {
			when(repo.findById(1L)).thenReturn(Optional.empty());

			assertThat(service.findByIdSchueler(1L)).isEmpty();
		}

	}

	// -------------------------------------------------------------------------
	// getBySchuelerIds
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("getBySchuelerIds")
	class GetBySchuelerIds {

		@Test
		@DisplayName("Gibt leere Liste zurück wenn keine Entitäten gefunden")
		void getBySchuelerIds_leer() {
			when(repo.findListByIds(List.of(99L))).thenReturn(List.of());

			assertThat(service.getBySchuelerIds(List.of(99L))).isEmpty();
		}

		@Test
		@DisplayName("Gibt alle gefundenen Fotos gemappt zurück")
		void getBySchuelerIds_mehrere() {
			final var entity2 = new DTOSchuelerFoto(2L);
			entity2.fotoBase64 = "xyz";
			final var domain2 = new SchuelerFoto(2L, "xyz");

			when(repo.findListByIds(List.of(1L, 2L))).thenReturn(List.of(entity, entity2));
			when(mapper.toDomain(entity)).thenReturn(domain);
			when(mapper.toDomain(entity2)).thenReturn(domain2);

			final var result = service.getBySchuelerIds(List.of(1L, 2L));

			assertThat(result).containsExactly(domain, domain2);
		}
	}

	// -------------------------------------------------------------------------
	// upsertOrDelete
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("upsertOrDelete")
	class UpsertOrDelete {

		@Test
		@DisplayName("Löscht Foto wenn vorhanden und newFotoBase64 == null")
		void upsertOrDelete_loeschtFoto() {
			when(repo.findById(1L)).thenReturn(Optional.of(entity));

			service.upsertOrDelete(1L, null);

			verify(repo).delete(entity);
			verify(repo, never()).create(any(DTOSchuelerFoto.class));
		}

		@Test
		@DisplayName("Keine Aktion wenn kein Foto vorhanden und newFotoBase64 == null")
		void upsertOrDelete_keineAktionBeiNullUndKeinFoto() {
			when(repo.findById(1L)).thenReturn(Optional.empty());

			service.upsertOrDelete(1L, null);

			verify(repo, never()).delete(any(DTOSchuelerFoto.class));
			verify(repo, never()).create(any(DTOSchuelerFoto.class));
		}

		@Test
		@DisplayName("Legt neues Foto an wenn keines vorhanden und newFotoBase64 != null")
		void upsertOrDelete_legtNeuAn() {
			when(repo.findById(1L)).thenReturn(Optional.empty());

			service.upsertOrDelete(1L, "neuesFoto");

			final var captor = ArgumentCaptor.forClass(DTOSchuelerFoto.class);
			verify(repo).create(captor.capture());
			assertThat(captor.getValue().idSchueler).isEqualTo(1L);
			assertThat(captor.getValue().fotoBase64).isEqualTo("neuesFoto");
			verify(repo, never()).delete(any(DTOSchuelerFoto.class));
		}

		@Test
		@DisplayName("Aktualisiert Foto wenn vorhanden und Inhalt geändert")
		void upsertOrDelete_aktualisiertFoto() {
			when(repo.findById(1L)).thenReturn(Optional.of(entity));

			service.upsertOrDelete(1L, "geaendertesFoto");

			assertThat(entity.fotoBase64).isEqualTo("geaendertesFoto");
			verify(repo, never()).delete(any(DTOSchuelerFoto.class));
			verify(repo, never()).create(any(DTOSchuelerFoto.class));
		}

		@Test
		@DisplayName("Keine Aktion wenn Foto vorhanden und Inhalt unverändert")
		void upsertOrDelete_keineAktionBeiGleichemInhalt() {
			when(repo.findById(1L)).thenReturn(Optional.of(entity));

			service.upsertOrDelete(1L, "abc123");

			verify(repo, never()).delete(any(DTOSchuelerFoto.class));
			verify(repo, never()).create(any(DTOSchuelerFoto.class));
			assertThat(entity.fotoBase64).isEqualTo("abc123");
		}
	}
}
