package de.svws_nrw.service.uv.lerngruppen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
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

import de.svws_nrw.core.data.uv.UvLerngruppenLehrerCreateRequest;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppenLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppenLehrerRepository;
import jakarta.servlet.http.HttpServletRequest;

/** Prüft die atomare Übernahme von Blockungsergebnissen. */
@ExtendWith(MockitoExtension.class)
class UvLerngruppenLehrerErsetzenTest {

	@Mock private UvLerngruppenLehrerRepository repository;
	@Mock private DBEntityManager conn;
	@Mock private HttpServletRequest http;
	private UvLerngruppenLehrerService service;

	@BeforeEach
	void setup() {
		ResteasyContext.pushContext(HttpServletRequest.class, http);
		when(http.getAttribute("connection")).thenReturn(conn);
		service = new UvLerngruppenLehrerService(repository);
	}

	@AfterEach
	void cleanup() {
		ResteasyContext.clearContextData();
	}

	@Test
	void fehlgeschlageneNeuanlageRolltDasLoeschenZurueck() {
		final var alt = List.of(dto(21));
		when(repository.getListByPlanungsabschnitt(11L)).thenReturn(alt);
		when(repository.getNextID()).thenReturn(22L);
		doThrow(new IllegalStateException("Anlage fehlgeschlagen")).when(repository).update(anyCollection());
		final var anfragen = List.of(anfrage(11));
		assertThrows(IllegalStateException.class, () -> service.replaceByPlanungsabschnitt(11, anfragen));
		final var reihenfolge = inOrder(repository);
		reihenfolge.verify(repository).getNextID();
		reihenfolge.verify(repository).getListByPlanungsabschnitt(11L);
		reihenfolge.verify(repository).delete(alt);
		verify(conn).transactionRollbackOrThrow();
		verify(conn, never()).transactionCommitOrThrow();
	}

	@Test
	void falscherPlanWirdVorDemLoeschenAbgewiesen() {
		final var anfragen = List.of(anfrage(12));
		assertThrows(ApiOperationException.class, () -> service.replaceByPlanungsabschnitt(11, anfragen));
		verify(repository, never()).delete(anyCollection());
		verify(conn).transactionRollbackOrThrow();
	}

	@Test
	void leeresErgebnisLoeschtDenBestandAtomar() {
		final var alt = List.of(dto(21));
		when(repository.getListByPlanungsabschnitt(11L)).thenReturn(alt);
		assertTrue(service.replaceByPlanungsabschnitt(11L, List.of()).isEmpty());
		verify(repository).delete(alt);
		verify(repository, never()).update(anyCollection());
		verify(conn).transactionCommitOrThrow();
	}

	@Test
	void erfolgreichesErsetzenVerwendetNeueIds() {
		when(repository.getNextID()).thenReturn(22L);
		when(repository.getListByPlanungsabschnitt(11L)).thenReturn(List.of(dto(21)));
		when(repository.findListByIds(anyCollection())).thenReturn(List.of(dto(22)));
		assertEquals(22, service.replaceByPlanungsabschnitt(11L, List.of(anfrage(11))).getFirst().id);
		verify(conn).transactionCommitOrThrow();
	}

	private static DTOUvLerngruppenLehrer dto(final long id) {
		return new DTOUvLerngruppenLehrer(id, 11L, 31L, 41L, 1, 2.0, 2.0);
	}

	private static UvLerngruppenLehrerCreateRequest anfrage(final long plan) {
		final var request = new UvLerngruppenLehrerCreateRequest();
		request.idPlanungsabschnitt = plan;
		request.idLerngruppe = 31L;
		request.idLehrer = 41L;
		request.reihenfolge = 1;
		request.wochenstunden = 2.0;
		request.wochenstundenAngerechnet = 2.0;
		return request;
	}
}
