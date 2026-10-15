package de.svws_nrw.service.schulwechsel.erfolgreiche_bewerbung;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.core.data.schule.SchulwechselDokument;
import de.svws_nrw.core.data.schule.SchulwechselErfolgreicheBewerbung;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.mapper.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungMapper;
import de.svws_nrw.repo.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungRepository;
import de.svws_nrw.service.schulwechsel.client.ErfolgreicheBewerbungResponse;
import de.svws_nrw.service.schulwechsel.client.ErfolgreicheBewerbungenClient;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentCreateRequest;
import de.svws_nrw.service.schulwechsel.dokument.SchulwechselDokumentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchulwechselErfolgreicheBewerbungServiceTest {

	@Mock
	private SchulwechselErfolgreicheBewerbungRepository repository;

	@Mock
	private SchulwechselErfolgreicheBewerbungMapper mapper;

	@Mock
	private SchulwechselDokumentService dokumentService;

	@Mock
	private ErfolgreicheBewerbungenClient erfolgreicheBewerbungenClient;

	private SchulwechselErfolgreicheBewerbungService service;

	private MockedStatic<TransactionSupport> transactionSupportMock;

	@BeforeEach
	void setUp() {
		service = new SchulwechselErfolgreicheBewerbungService(repository, mapper, dokumentService, erfolgreicheBewerbungenClient);
		transactionSupportMock = mockStatic(TransactionSupport.class);
		transactionSupportMock.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<?>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@AfterEach
	void tearDown() {
		transactionSupportMock.close();
	}

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private static SchulwechselDokument createDokument(final long id) {
		final var dokument = new SchulwechselDokument();
		dokument.id = id;
		return dokument;
	}

	private void setupCreateAll() {
		when(repository.create(anyList())).thenAnswer(inv -> inv.getArgument(0));
		when(mapper.toApi(any())).thenReturn(new SchulwechselErfolgreicheBewerbung());
	}

	// -------------------------------------------------------------------------
	// sync
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("sync | Dokument-Requests werden in Eingabe-Reihenfolge an createAll übergeben")
	void sync_dokumentRequestsWerdenInEingabeReihenfolgeUebergeben() {
		when(erfolgreicheBewerbungenClient.getAllBewerbungen()).thenReturn(List.of(
				new ErfolgreicheBewerbungResponse("schulkind-A", null, "neu"),
				new ErfolgreicheBewerbungResponse("schulkind-B", null, "neu"),
				new ErfolgreicheBewerbungResponse("schulkind-C", null, "neu")
		));
		when(erfolgreicheBewerbungenClient.getXSchule(anyString())).thenReturn("<xml/>");
		when(repository.findAllIdSchulkindSchulbewerbung()).thenReturn(Set.of());
		when(dokumentService.createAll(anyList())).thenReturn(List.of(
				createDokument(10L), createDokument(11L), createDokument(12L)
		));
		setupCreateAll();

		service.sync();

		final ArgumentCaptor<List<SchulwechselDokumentCreateRequest>> captor = ArgumentCaptor.forClass(List.class);
		verify(dokumentService).createAll(captor.capture());

		assertThat(captor.getValue())
				.hasSize(3)
				.satisfies(requests -> {
					assertThat(requests.get(0).fileName).contains("schulkind-A");
					assertThat(requests.get(1).fileName).contains("schulkind-B");
					assertThat(requests.get(2).fileName).contains("schulkind-C");
				});
	}

	@Test
	@DisplayName("sync | Jede Bewerbung wird mit dem Dokument an gleicher Indexposition verknüpft")
	void sync_bewerbungenWerdenMitDokumentAnGleicherIndexpositionVerknuepft() {
		when(erfolgreicheBewerbungenClient.getAllBewerbungen()).thenReturn(List.of(
				new ErfolgreicheBewerbungResponse("schulkind-A", null, "neu"),
				new ErfolgreicheBewerbungResponse("schulkind-B", null, "neu"),
				new ErfolgreicheBewerbungResponse("schulkind-C", null, "neu")
		));
		when(erfolgreicheBewerbungenClient.getXSchule(anyString())).thenReturn("<xml/>");
		when(repository.findAllIdSchulkindSchulbewerbung()).thenReturn(Set.of());
		when(dokumentService.createAll(anyList())).thenReturn(List.of(
				createDokument(10L), createDokument(11L), createDokument(12L)
		));
		setupCreateAll();

		service.sync();

		final ArgumentCaptor<SchulwechselErfolgreicheBewerbungCreateRequest> captor =
				ArgumentCaptor.forClass(SchulwechselErfolgreicheBewerbungCreateRequest.class);
		verify(mapper, times(3)).toDomain(captor.capture());

		final var requests = captor.getAllValues();
		assertThat(requests.get(0).idSchulkindSchulbewerbung).isEqualTo("schulkind-A");
		assertThat(requests.get(0).idDocument).isEqualTo(10L);
		assertThat(requests.get(1).idSchulkindSchulbewerbung).isEqualTo("schulkind-B");
		assertThat(requests.get(1).idDocument).isEqualTo(11L);
		assertThat(requests.get(2).idSchulkindSchulbewerbung).isEqualTo("schulkind-C");
		assertThat(requests.get(2).idDocument).isEqualTo(12L);
	}

	@Test
	@DisplayName("sync | Bereits bekannte Einträge werden übersprungen")
	void sync_bereitsBekannteBewerbungenWerdenUebersprungen() {
		when(erfolgreicheBewerbungenClient.getAllBewerbungen()).thenReturn(List.of(
				new ErfolgreicheBewerbungResponse("schulkind-A", null, "neu"),
				new ErfolgreicheBewerbungResponse("schulkind-B", null, "neu"),
				new ErfolgreicheBewerbungResponse("schulkind-C", null, "neu")
		));
		when(erfolgreicheBewerbungenClient.getXSchule(anyString())).thenReturn("<xml/>");
		when(repository.findAllIdSchulkindSchulbewerbung()).thenReturn(Set.of("schulkind-B"));
		when(dokumentService.createAll(anyList())).thenReturn(List.of(
				createDokument(10L), createDokument(11L)
		));
		setupCreateAll();

		final var ergebnis = service.sync();

		assertThat(ergebnis.erstellt()).hasSize(2);
		assertThat(ergebnis.uebersprungen()).containsExactly("schulkind-B");
	}

	@Test
	@DisplayName("sync | Leere Remote-Liste ergibt leeres SyncErgebnis")
	void sync_leereRemoteListe_ergebnisLeer() {
		when(erfolgreicheBewerbungenClient.getAllBewerbungen()).thenReturn(List.of());
		when(repository.findAllIdSchulkindSchulbewerbung()).thenReturn(Set.of());

		final var ergebnis = service.sync();

		assertThat(ergebnis.erstellt()).isEmpty();
		assertThat(ergebnis.uebersprungen()).isEmpty();
	}
}
