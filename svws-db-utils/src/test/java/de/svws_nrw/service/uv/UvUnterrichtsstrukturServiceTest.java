package de.svws_nrw.service.uv;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKlasseAnlageRequest;
import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.data.uv.UvKursunterrichtAnlageRequest;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.uv.klassen.UvKlasseService;
import de.svws_nrw.service.uv.kurse.UvKursService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppeService;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeSchuelerService;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeService;
import jakarta.servlet.http.HttpServletRequest;

/** Prüft Fachoperationen, Transaktionsgrenzen und die Wiederverwendung vorhandener Gruppen. */
@ExtendWith(MockitoExtension.class)
class UvUnterrichtsstrukturServiceTest {

	@Mock private UvKlasseService klassen;
	@Mock private UvKursService kurse;
	@Mock private UvLerngruppeService lerngruppen;
	@Mock private UvSchuelergruppeService gruppen;
	@Mock private UvSchuelergruppeSchuelerService mitglieder;
	@Mock private DBEntityManager conn;
	@Mock private HttpServletRequest http;
	private UvUnterrichtsstrukturService service;

	@BeforeEach
	void setup() {
		ResteasyContext.pushContext(HttpServletRequest.class, http);
		when(http.getAttribute("connection")).thenReturn(conn);
		service = new UvUnterrichtsstrukturService(klassen, kurse, lerngruppen, gruppen, mitglieder);
	}

	@AfterEach
	void cleanup() {
		ResteasyContext.clearContextData();
	}

	@Test
	void klasseUndNeueGruppeWerdenGemeinsamGespeichert() {
		final UvKlasseAnlageRequest anfrage = klassenAnfrage();
		anfrage.schuelergruppe = gruppenAnfrage();
		final UvSchuelergruppe gruppe = gruppe(21L, 11L);
		when(gruppen.createMultiple(anyCollection())).thenReturn(List.of(gruppe));
		when(klassen.createMultiple(anyCollection())).thenReturn(List.of(anfrage.klasse));
		final var result = service.createKlasseMitSchuelergruppe(anfrage);
		assertSame(gruppe, result.schuelergruppen.getFirst());
		verify(klassen).createMultiple(org.mockito.ArgumentMatchers.argThat(requests -> requests.iterator().next().idSchuelergruppe == 21L));
		verify(conn).transactionBegin();
		verify(conn).transactionCommitOrThrow();
	}

	@Test
	void sammelerstellungOrdnetGruppenAuchBeiAndererRepositoryReihenfolgeRichtigZu() {
		final UvKlasseAnlageRequest erste = klassenAnfrage();
		erste.schuelergruppe = gruppenAnfrage();
		final UvKlasseAnlageRequest zweite = klassenAnfrage();
		zweite.klasse.kuerzel = "9B";
		zweite.schuelergruppe = gruppenAnfrage();
		when(gruppen.createMultiple(anyCollection())).thenReturn(List.of(gruppe(22L, 11L), gruppe(21L, 11L)));
		service.createKlassenMitSchuelergruppen(List.of(erste, zweite));
		verify(klassen).createMultiple(org.mockito.ArgumentMatchers.argThat(requests -> {
			final var iterator = requests.iterator();
			return iterator.next().idSchuelergruppe == 21L && iterator.next().idSchuelergruppe == 22L;
		}));
	}

	@Test
	void fehlgeschlageneKlassenanlageRolltDieGruppenanlageZurueck() {
		final UvKlasseAnlageRequest anfrage = klassenAnfrage();
		anfrage.schuelergruppe = gruppenAnfrage();
		when(gruppen.createMultiple(anyCollection())).thenReturn(List.of(gruppe(21L, 11L)));
		when(klassen.createMultiple(anyCollection())).thenThrow(new IllegalStateException("Klasse fehlgeschlagen"));
		assertThrows(IllegalStateException.class, () -> service.createKlasseMitSchuelergruppe(anfrage));
		verify(conn).transactionRollbackOrThrow();
		verify(conn, never()).transactionCommitOrThrow();
	}

	@Test
	void bestehendeGruppeWirdNurImPassendenPlanungsabschnittVerwendet() {
		final UvKlasseAnlageRequest anfrage = klassenAnfrage();
		anfrage.klasse.idSchuelergruppe = 21L;
		when(gruppen.get(21L)).thenReturn(gruppe(21L, 12L));
		assertThrows(ApiOperationException.class, () -> service.createKlasseMitSchuelergruppe(anfrage));
		verify(klassen, never()).createMultiple(anyCollection());
		verify(gruppen, never()).createMultiple(anyCollection());
		verify(conn).transactionRollbackOrThrow();
	}

	@Test
	void importTransaktionWirdNichtVorzeitigAbgeschlossen() {
		when(conn.hasActiveTransaction()).thenReturn(true);
		final UvKlasseAnlageRequest anfrage = klassenAnfrage();
		anfrage.klasse.idSchuelergruppe = 21L;
		when(gruppen.get(21L)).thenReturn(gruppe(21L, 11L));
		when(klassen.createMultiple(anyCollection())).thenReturn(List.of(anfrage.klasse));
		service.createKlasseMitSchuelergruppe(anfrage);
		verify(conn, never()).transactionBegin();
		verify(conn, never()).transactionCommitOrThrow();
		verify(conn, never()).transactionRollbackOrThrow();
	}

	@Test
	void kursanlageMitFehlenderLerngruppeRolltAllesZurueck() {
		final UvKursunterrichtAnlageRequest anfrage = kursAnfrage();
		anfrage.schuelergruppe = gruppenAnfrage();
		when(gruppen.create(any())).thenReturn(gruppe(21L, 11L));
		when(kurse.create(any())).thenReturn(kurs());
		when(lerngruppen.create(any())).thenThrow(new IllegalStateException("Lerngruppe fehlgeschlagen"));
		assertThrows(IllegalStateException.class, () -> service.createKursMitLerngruppe(anfrage));
		verify(conn).transactionRollbackOrThrow();
		verify(conn, never()).transactionCommitOrThrow();
	}

	@Test
	void kursanlageErgaenztNurNeueMitgliederOhneDuplikate() {
		final UvKursunterrichtAnlageRequest anfrage = kursAnfrage();
		anfrage.idsSchueler.addAll(List.of(31L, 32L, 32L));
		when(gruppen.get(21L)).thenReturn(gruppe(21L, 11L));
		when(kurse.create(any())).thenReturn(kurs());
		when(lerngruppen.create(any())).thenReturn(new UvLerngruppe());
		final UvSchuelergruppeSchueler vorhanden = new UvSchuelergruppeSchueler();
		vorhanden.idSchuelergruppe = 21L;
		vorhanden.idSchueler = 31L;
		when(mitglieder.getListByPlanungsabschnitt(11L)).thenReturn(List.of(vorhanden));
		service.createKursMitLerngruppe(anfrage);
		verify(mitglieder).createMultiple(org.mockito.ArgumentMatchers.argThat(requests -> requests.size() == 1
				&& requests.iterator().next().idSchueler == 32L));
		verify(gruppen, never()).create(any());
		verify(conn).transactionCommitOrThrow();
	}

	@Test
	void gruppenzuordnungAnBestehendeKlasseIstAtomar() {
		final UvKlasse klasse = klassenAnfrage().klasse;
		when(klassen.get(41L)).thenReturn(klasse);
		when(gruppen.create(any())).thenReturn(gruppe(21L, 11L));
		when(klassen.patch(any())).thenThrow(new IllegalStateException("Zuordnung fehlgeschlagen"));
		final var anfrage = gruppenAnfrage();
		assertThrows(IllegalStateException.class, () -> service.createSchuelergruppeFuerKlasse(41L, anfrage));
		verify(conn).transactionRollbackOrThrow();
		verify(conn, never()).transactionCommitOrThrow();
	}

	private static UvKlasseAnlageRequest klassenAnfrage() {
		final UvKlasseAnlageRequest request = new UvKlasseAnlageRequest();
		request.klasse.kuerzel = "9A";
		request.klasse.idPlanungsabschnitt = 11;
		request.klasse.idSchuljahresabschnitt = 1;
		return request;
	}

	private static UvSchuelergruppeCreateRequest gruppenAnfrage() {
		final UvSchuelergruppeCreateRequest request = new UvSchuelergruppeCreateRequest();
		request.idPlanungsabschnitt = 11L;
		request.bezeichnung = "Gruppe";
		return request;
	}

	private static UvSchuelergruppe gruppe(final long id, final long plan) {
		final UvSchuelergruppe gruppe = new UvSchuelergruppe();
		gruppe.id = id;
		gruppe.idPlanungsabschnitt = plan;
		return gruppe;
	}

	private static UvKurs kurs() {
		final UvKurs kurs = new UvKurs();
		kurs.id = 51;
		kurs.idPlanungsabschnitt = 11;
		return kurs;
	}

	private static UvKursunterrichtAnlageRequest kursAnfrage() {
		final UvKursunterrichtAnlageRequest request = new UvKursunterrichtAnlageRequest();
		request.kurs.idPlanungsabschnitt = 11L;
		request.kurs.idSchuljahresabschnitt = 1L;
		request.kurs.idFach = 1L;
		request.kurs.kursart = "GK";
		request.kurs.kursnummer = 1;
		request.kurs.idSchuelergruppe = 21L;
		request.lerngruppe.idPlanungsabschnitt = 11L;
		return request;
	}
}
