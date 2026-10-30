package de.svws_nrw.service.schueler.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.service.schueler.einwilligung.SchuelerEinwilligungService;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittService;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformService;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchService;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenService;
import jakarta.ws.rs.core.Response;

/**
 * Tests für den {@link SchuelerCreateService}.
 */
@ExtendWith(MockitoExtension.class)
class SchuelerCreateServiceTest {

	private static final long ID_SCHUELER = 100L;
	private static final long ID_SCHULJAHRESABSCHNITT = 8L;

	@Mock
	private SchuelerStammdatenService schuelerStammdatenService;

	@Mock
	private SchuelerLernabschnittService schuelerLernabschnittService;

	@Mock
	private SchuelerSchulbesuchService schuelerSchulbesuchService;

	@Mock
	private SchuelerLernplattformService schuelerLernplattformService;

	@Mock
	private SchuelerEinwilligungService schuelerEinwilligungService;

	@Mock
	private DBEntityManager conn;

	private MockedStatic<DbConnectionProvider> dbConnectionProviderMock;

	private SchuelerCreateService service;

	@BeforeEach
	void setUp() {
		// Die echte TransactionSupport-Logik läuft gegen eine gemockte Connection, damit Commit und Rollback prüfbar sind
		dbConnectionProviderMock = mockStatic(DbConnectionProvider.class);
		dbConnectionProviderMock.when(DbConnectionProvider::getConnection).thenReturn(conn);
		service = new SchuelerCreateService(schuelerStammdatenService, schuelerLernabschnittService, schuelerSchulbesuchService,
				schuelerLernplattformService, schuelerEinwilligungService);
	}

	@AfterEach
	void tearDown() {
		dbConnectionProviderMock.close();
	}

	private static SchuelerCreateRequest createRequest() {
		final var request = new SchuelerCreateRequest();
		request.nachname = "Test";
		request.vorname = "Max";
		request.geschlecht = 3;
		request.geburtsdatum = "2000-01-01";
		request.status = 2;
		request.idSchuljahresabschnitt = ID_SCHULJAHRESABSCHNITT;
		return request;
	}

	private static SchuelerStammdaten createdSchueler() {
		final var created = new SchuelerStammdaten();
		created.id = ID_SCHUELER;
		return created;
	}

	@Test
	@DisplayName("create | Alle Schritte werden in der richtigen Reihenfolge in einer Transaktion ausgeführt")
	void create_alleFelder() {
		final var request = createRequest();
		request.idJahrgang = 5L;
		request.idKlasse = 7L;
		request.idGrundschuleEinschulungsart = 51L;
		final var created = createdSchueler();
		when(schuelerStammdatenService.create(request)).thenReturn(created);

		final var result = service.create(request);

		assertThat(result).isSameAs(created);
		final var order = inOrder(conn, schuelerStammdatenService, schuelerLernabschnittService, schuelerSchulbesuchService,
				schuelerEinwilligungService, schuelerLernplattformService);
		order.verify(conn).transactionBegin();
		order.verify(schuelerStammdatenService).create(request);
		order.verify(schuelerLernabschnittService).create(argThat(lernabschnitt -> lernabschnitt.schuelerID.equals(ID_SCHUELER)
				&& lernabschnitt.schuljahresabschnitt.equals(ID_SCHULJAHRESABSCHNITT)
				&& lernabschnitt.klassenID.equals(JsonNullable.of(7L))
				&& lernabschnitt.jahrgangID.equals(JsonNullable.of(5L))));
		order.verify(schuelerSchulbesuchService).patch(anyLong(), argThat(patch -> patch.idEinschulungsartGrundschule.equals(JsonNullable.of(51L))));
		order.verify(schuelerEinwilligungService).addForSchueler(ID_SCHUELER);
		order.verify(schuelerLernplattformService).addForSchueler(ID_SCHUELER);
		order.verify(conn).transactionCommitOrThrow();
	}

	@Test
	@DisplayName("create | Ohne Jahrgang, Klasse und Einschulungsart werden diese nicht gesetzt und kein Schulbesuch gepatcht")
	void create_nurPflichtfelder() {
		final var request = createRequest();
		when(schuelerStammdatenService.create(request)).thenReturn(createdSchueler());

		service.create(request);

		verify(schuelerLernabschnittService).create(argThat(lernabschnitt -> lernabschnitt.schuelerID.equals(ID_SCHUELER)
				&& lernabschnitt.schuljahresabschnitt.equals(ID_SCHULJAHRESABSCHNITT)
				&& !lernabschnitt.klassenID.isPresent()
				&& !lernabschnitt.jahrgangID.isPresent()));
		verify(schuelerSchulbesuchService, never()).patch(anyLong(), any());
		verify(schuelerEinwilligungService).addForSchueler(ID_SCHUELER);
		verify(schuelerLernplattformService).addForSchueler(ID_SCHUELER);
	}

	@Test
	@DisplayName("create | Ist bereits eine Transaktion offen, wird keine neue gestartet")
	void create_offeneTransaktion() {
		final var request = createRequest();
		when(conn.hasActiveTransaction()).thenReturn(true);
		when(schuelerStammdatenService.create(request)).thenReturn(createdSchueler());

		service.create(request);

		verify(conn, never()).transactionBegin();
		verify(conn, never()).transactionCommitOrThrow();
		verify(schuelerLernabschnittService, times(1)).create(any());
	}

	@Test
	@DisplayName("create | Fehler im Stammdaten-Service: keine weiteren Schritte, Rollback")
	void create_fehlerBeiStammdaten() {
		final var request = createRequest();
		when(schuelerStammdatenService.create(request)).thenThrow(new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Status zur ID 2 gefunden"));

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

		verifyNoInteractions(schuelerLernabschnittService, schuelerSchulbesuchService, schuelerEinwilligungService, schuelerLernplattformService);
		verify(conn).transactionRollbackOrThrow();
		verify(conn, never()).transactionCommitOrThrow();
	}

	@Test
	@DisplayName("create | Fehler im Lernabschnitt-Service: Rollback ohne Commit und keine weiteren Schritte")
	void create_fehlerBeiLernabschnitt() {
		final var request = createRequest();
		request.idGrundschuleEinschulungsart = 51L;
		when(schuelerStammdatenService.create(request)).thenReturn(createdSchueler());
		when(schuelerLernabschnittService.create(any())).thenThrow(new ApiOperationException(Response.Status.BAD_REQUEST, "Schuljahresabschnitt nicht gefunden"));

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Schuljahresabschnitt nicht gefunden");

		verifyNoInteractions(schuelerSchulbesuchService, schuelerEinwilligungService, schuelerLernplattformService);
		verify(conn).transactionRollbackOrThrow();
		verify(conn, never()).transactionCommitOrThrow();
	}

	@Test
	@DisplayName("create | Fehler beim Anlegen der Lernplattformen: Rollback ohne Commit")
	void create_fehlerBeiLernplattform() {
		final var request = createRequest();
		when(schuelerStammdatenService.create(request)).thenReturn(createdSchueler());
		when(schuelerLernplattformService.addForSchueler(ID_SCHUELER))
				.thenThrow(new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler"));

		assertThatThrownBy(() -> service.create(request)).isInstanceOf(ApiOperationException.class);

		verify(conn).transactionRollbackOrThrow();
		verify(conn, never()).transactionCommitOrThrow();
	}

}
