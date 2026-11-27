package de.svws_nrw.service.uv.faecher;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.jboss.resteasy.core.ResteasyContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.faecher.UvFachRepository;
import jakarta.servlet.http.HttpServletRequest;

/** Prüft Gültigkeitskonflikte vor dem Speichern einzelner und mehrerer UV-Fächer. */
@ExtendWith(MockitoExtension.class)
class UvFachServiceTest {

	@Mock
	private UvFachRepository repository;
	@Mock
	private DBEntityManager conn;
	@Mock
	private HttpServletRequest request;
	private UvFachService service;

	@BeforeEach
	void setup() {
		ResteasyContext.pushContext(HttpServletRequest.class, request);
		when(request.getAttribute("connection")).thenReturn(conn);
		when(conn.hasActiveTransaction()).thenReturn(true);
		service = new UvFachService(repository);
	}

	@AfterEach
	void cleanup() {
		ResteasyContext.clearContextData();
	}

	@Test
	void verweigertUeberschneidungenInnerhalbEinerSammelanlage() {
		when(repository.getNextID()).thenReturn(1L);
		final var anfragen = List.of(createRequest("2026-08-01", "2027-07-31"), createRequest("2027-07-31", null));
		assertThrows(ApiOperationException.class, () -> service.createMultiple(anfragen));
		verify(repository, never()).update(anyCollection());
	}

	@Test
	void verweigertAnlageNebenUnbegrenztGueltigemBestand() {
		when(repository.getNextID()).thenReturn(2L);
		when(repository.getListByFachId(1L)).thenReturn(List.of(new DTOUvFach(1L, 1L, "2026-08-01")));
		final var anfrage = createRequest("2027-08-01", null);
		assertThrows(ApiOperationException.class, () -> service.create(anfrage));
		verify(repository, never()).update(anyCollection());
	}

	@Test
	void verweigertUeberschneidungBeiAenderung() {
		final DTOUvFach erstes = new DTOUvFach(1L, 1L, "2026-01-01");
		erstes.GueltigBis = "2026-07-31";
		final DTOUvFach zweites = new DTOUvFach(2L, 1L, "2026-08-01");
		when(repository.findListByIds(anyCollection())).thenReturn(List.of(erstes));
		when(repository.getById(1L)).thenReturn(erstes);
		when(repository.getListByFachId(1L)).thenReturn(List.of(erstes, zweites));
		final UvFachPatchRequest patch = new UvFachPatchRequest();
		patch.id = 1L;
		patch.gueltigBis = JsonNullable.of("2026-08-01");
		assertThrows(ApiOperationException.class, () -> service.patch(patch));
		verify(repository, never()).update(anyCollection());
	}

	@Test
	void prueftGemeinsamenEndstandBeimVerschiebenEinerIntervallgrenze() {
		final DTOUvFach erstes = new DTOUvFach(1L, 1L, "2026-01-01");
		erstes.GueltigBis = "2026-07-31";
		final DTOUvFach zweites = new DTOUvFach(2L, 1L, "2026-08-01");
		final List<DTOUvFach> bestand = List.of(erstes, zweites);
		when(repository.findListByIds(anyCollection())).thenReturn(bestand);
		when(repository.getById(1L)).thenReturn(erstes);
		when(repository.getById(2L)).thenReturn(zweites);
		when(repository.getListByFachId(1L)).thenReturn(bestand);
		final UvFachPatchRequest ende = new UvFachPatchRequest();
		ende.id = 1L;
		ende.gueltigBis = JsonNullable.of("2026-08-31");
		final UvFachPatchRequest beginn = new UvFachPatchRequest();
		beginn.id = 2L;
		beginn.gueltigVon = JsonNullable.of("2026-09-01");
		service.patchMultiple(List.of(ende, beginn));
		verify(repository).update(bestand);
	}

	private UvFachCreateRequest createRequest(final String von, final String bis) {
		final UvFachCreateRequest daten = new UvFachCreateRequest();
		daten.idFach = 1L;
		daten.gueltigVon = von;
		daten.gueltigBis = bis;
		return daten;
	}
}
