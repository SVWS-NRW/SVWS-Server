package de.svws_nrw.service.uv.regeln;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.jboss.resteasy.core.ResteasyContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnitt;
import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanKonfiguration;
import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanRegel;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.faecher.UvFachRepository;
import de.svws_nrw.repo.uv.klassen.UvKlasseRepository;
import de.svws_nrw.repo.uv.lehrer.UvPlanungsabschnittLehrerRepository;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppeRepository;
import de.svws_nrw.repo.uv.planungsabschnitte.UvPlanungsabschnittRepository;
import de.svws_nrw.repo.uv.regeln.UvStundenplanKonfigurationRepository;
import de.svws_nrw.repo.uv.regeln.UvStundenplanRegelRepository;
import de.svws_nrw.repo.uv.regeln.UvStundenplanRegelparameterRepository;
import jakarta.servlet.http.HttpServletRequest;

/** Prüft, dass zusammengehörige Regeländerungen gemeinsam gespeichert oder zurückgerollt werden. */
@ExtendWith(MockitoExtension.class)
class UvRegelSammelaenderungTest {

	@Mock private UvPlanungsabschnittRepository plaene;
	@Mock private UvStundenplanKonfigurationRepository konfigurationen;
	@Mock private UvStundenplanRegelRepository regeln;
	@Mock private UvStundenplanRegelparameterRepository parameter;
	@Mock private UvPlanungsabschnittLehrerRepository lehrer;
	@Mock private UvKlasseRepository klassen;
	@Mock private UvLerngruppeRepository lerngruppen;
	@Mock private UvFachRepository faecher;
	@Mock private DBEntityManager conn;
	@Mock private HttpServletRequest http;
	private UvRegelService service;

	@BeforeEach
	void setup() {
		ResteasyContext.pushContext(HttpServletRequest.class, http);
		when(http.getAttribute("connection")).thenReturn(conn);
		service = new UvRegelService(plaene, konfigurationen, regeln, parameter, lehrer, klassen, lerngruppen, faecher);
	}

	@AfterEach
	void cleanup() {
		ResteasyContext.clearContextData();
	}

	@Test
	void scheitertEineRegelWirdDieGesamteAenderungZurueckgerollt() {
		when(conn.hasActiveTransaction()).thenReturn(false, true);
		when(plaene.findById(11L)).thenReturn(Optional.of(new DTOUvPlanungsabschnitt(11L, 2026, false, "2026-08-01")));
		when(konfigurationen.getListByPlanungsabschnitt(11L)).thenReturn(List.of(new DTOUvStundenplanKonfiguration(12L, 11L)));
		final DTOUvStundenplanRegel erste = new DTOUvStundenplanRegel(21L, 12L, 1);
		final DTOUvStundenplanRegel zweite = new DTOUvStundenplanRegel(22L, 12L, 1);
		when(regeln.findById(21L)).thenReturn(Optional.of(erste));
		when(regeln.findById(22L)).thenReturn(Optional.of(zweite));
		final IllegalStateException fehler = new IllegalStateException("Zweite Änderung fehlgeschlagen");
		doAnswer(aufruf -> {
			if (aufruf.getArgument(0) == zweite) {
				throw fehler;
			}
			return null;
		}).when(regeln).update(any(DTOUvStundenplanRegel.class));
		final var anfragen = List.of(anfrage(21L), anfrage(22L));
		assertSame(fehler, assertThrows(IllegalStateException.class, () -> service.updateMultiple(11L, anfragen)));
		verify(regeln).update(erste);
		verify(regeln).update(zweite);
		verify(conn).transactionBegin();
		verify(conn).transactionRollbackOrThrow();
		verify(conn, never()).transactionCommitOrThrow();
	}

	@Test
	void doppelteRegelnWerdenVorDemSchreibenAbgewiesen() {
		when(plaene.findById(11L)).thenReturn(Optional.of(new DTOUvPlanungsabschnitt(11L, 2026, false, "2026-08-01")));
		when(konfigurationen.getListByPlanungsabschnitt(11L)).thenReturn(List.of(new DTOUvStundenplanKonfiguration(12L, 11L)));
		when(regeln.findById(21L)).thenReturn(Optional.of(new DTOUvStundenplanRegel(21L, 12L, 1)));
		final var anfragen = List.of(anfrage(21L), anfrage(21L));
		assertThrows(ApiOperationException.class, () -> service.updateMultiple(11L, anfragen));
		verify(regeln, never()).update(any(DTOUvStundenplanRegel.class));
		verify(conn).transactionRollbackOrThrow();
	}

	private static UvBlockungRegel anfrage(final long id) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.id = id;
		regel.typ = 1;
		regel.prioritaet = 5;
		regel.parameter.add(1L);
		return regel;
	}
}
