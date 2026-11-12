package de.svws_nrw.service.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.RepositoryException;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import jakarta.ws.rs.core.Response;

/**
 * Test für die Klasse {@link KlassenleitungService}
 */
@ExtendWith(MockitoExtension.class)
class KlassenleitungServiceTest {

	private static final long ID_KLASSE = 7L;
	private static final long ID_LEHRER_1 = 101L;
	private static final long ID_LEHRER_2 = 102L;
	private static final long ID_LEHRER_3 = 103L;

	@Mock
	private KlassenRepository klassenRepository;

	@Mock
	private KlassenleitungenRepository klassenleitungenRepository;

	@Mock
	private LehrerRepository lehrerRepository;

	private KlassenleitungService service;

	@BeforeEach
	void setUp() {
		service = new KlassenleitungService(klassenRepository, klassenleitungenRepository, lehrerRepository);
	}

	private static DTOKlassenLeitung createKlassenleitung(final long idLehrer, final int reihenfolge) {
		return new DTOKlassenLeitung(ID_KLASSE, idLehrer, reihenfolge);
	}

	private void stubKlasseUndBisherige(final DTOKlassenLeitung... bisherige) {
		when(klassenRepository.existsById(ID_KLASSE)).thenReturn(true);
		when(klassenleitungenRepository.findListByIdKlasse(ID_KLASSE)).thenReturn(new ArrayList<>(Arrays.asList(bisherige)));
	}

	private void stubLehrerExistieren(final Long... idsLehrer) {
		when(lehrerRepository.existsByIds(Arrays.asList(idsLehrer))).thenReturn(Set.of(idsLehrer));
	}

	private static void assertFehler(final Runnable aufruf, final Response.Status status, final String meldung) {
		assertThatThrownBy(aufruf::run)
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(status);
					assertThat(e.getBody()).isEqualTo(meldung);
				});
	}

	@Test
	@DisplayName("replace legt neue Klassenleitungen in der Reihenfolge der Liste an")
	void replaceLegtNeueKlassenleitungenAn() {
		stubKlasseUndBisherige();
		stubLehrerExistieren(ID_LEHRER_2, ID_LEHRER_1);

		service.replace(ID_KLASSE, List.of(ID_LEHRER_2, ID_LEHRER_1));

		final var captor = ArgumentCaptor.forClass(DTOKlassenLeitung.class);
		verify(klassenleitungenRepository, times(2)).create(captor.capture());
		assertThat(captor.getAllValues())
				.extracting(kl -> kl.Klassen_ID, kl -> kl.Lehrer_ID, kl -> kl.Reihenfolge)
				.containsExactly(
						tuple(ID_KLASSE, ID_LEHRER_2, 1),
						tuple(ID_KLASSE, ID_LEHRER_1, 2));
		verify(klassenleitungenRepository, never()).delete(any(DTOKlassenLeitung.class));
		verify(klassenleitungenRepository).flush();
	}

	@Test
	@DisplayName("replace aktualisiert bei geänderter Reihenfolge nur die Reihenfolge der bestehenden Einträge")
	void replaceAktualisiertReihenfolge() {
		final var lehrer1 = createKlassenleitung(ID_LEHRER_1, 1);
		final var lehrer2 = createKlassenleitung(ID_LEHRER_2, 2);
		stubKlasseUndBisherige(lehrer1, lehrer2);
		stubLehrerExistieren(ID_LEHRER_2, ID_LEHRER_1);

		service.replace(ID_KLASSE, List.of(ID_LEHRER_2, ID_LEHRER_1));

		assertThat(lehrer1.Reihenfolge).isEqualTo(2);
		assertThat(lehrer2.Reihenfolge).isEqualTo(1);
		verify(klassenleitungenRepository).update(lehrer1);
		verify(klassenleitungenRepository).update(lehrer2);
		verify(klassenleitungenRepository, never()).create(any(DTOKlassenLeitung.class));
		verify(klassenleitungenRepository, never()).delete(any(DTOKlassenLeitung.class));
	}

	@Test
	@DisplayName("replace löscht nicht mehr enthaltene Klassenleitungen und lässt unveränderte unberührt")
	void replaceLoeschtNichtMehrEnthaltene() {
		final var lehrer1 = createKlassenleitung(ID_LEHRER_1, 1);
		final var lehrer2 = createKlassenleitung(ID_LEHRER_2, 2);
		stubKlasseUndBisherige(lehrer1, lehrer2);
		stubLehrerExistieren(ID_LEHRER_1);

		service.replace(ID_KLASSE, List.of(ID_LEHRER_1));

		verify(klassenleitungenRepository).delete(lehrer2);
		verify(klassenleitungenRepository, never()).delete(lehrer1);
		verify(klassenleitungenRepository, never()).update(any(DTOKlassenLeitung.class));
		verify(klassenleitungenRepository, never()).create(any(DTOKlassenLeitung.class));
	}

	@Test
	@DisplayName("replace entfernt bei einer leeren Liste alle Klassenleitungen")
	void replaceMitLeererListeEntferntAlle() {
		final var lehrer1 = createKlassenleitung(ID_LEHRER_1, 1);
		final var lehrer2 = createKlassenleitung(ID_LEHRER_2, 2);
		stubKlasseUndBisherige(lehrer1, lehrer2);
		when(lehrerRepository.existsByIds(List.of())).thenReturn(Set.of());

		service.replace(ID_KLASSE, List.of());

		verify(klassenleitungenRepository).delete(lehrer1);
		verify(klassenleitungenRepository).delete(lehrer2);
		verify(klassenleitungenRepository, never()).create(any(DTOKlassenLeitung.class));
	}

	@Test
	@DisplayName("replace ändert nichts, wenn die Klassenleitungen unverändert sind")
	void replaceOhneAenderung() {
		stubKlasseUndBisherige(createKlassenleitung(ID_LEHRER_1, 1), createKlassenleitung(ID_LEHRER_2, 2));
		stubLehrerExistieren(ID_LEHRER_1, ID_LEHRER_2);

		service.replace(ID_KLASSE, List.of(ID_LEHRER_1, ID_LEHRER_2));

		verify(klassenleitungenRepository).findListByIdKlasse(ID_KLASSE);
		verify(klassenleitungenRepository).flush();
		verifyNoMoreInteractions(klassenleitungenRepository);
	}

	@Test
	@DisplayName("replace legt neue Klassenleitungen an, entfernt alte und behält die übrigen")
	void replaceKombination() {
		final var lehrer1 = createKlassenleitung(ID_LEHRER_1, 1);
		final var lehrer2 = createKlassenleitung(ID_LEHRER_2, 2);
		stubKlasseUndBisherige(lehrer1, lehrer2);
		stubLehrerExistieren(ID_LEHRER_1, ID_LEHRER_3);

		service.replace(ID_KLASSE, List.of(ID_LEHRER_1, ID_LEHRER_3));

		verify(klassenleitungenRepository).delete(lehrer2);
		final var captor = ArgumentCaptor.forClass(DTOKlassenLeitung.class);
		verify(klassenleitungenRepository).create(captor.capture());
		assertThat(captor.getValue())
				.satisfies(kl -> assertThat(kl.Lehrer_ID).isEqualTo(ID_LEHRER_3))
				.satisfies(kl -> assertThat(kl.Reihenfolge).isEqualTo(2));
		verify(klassenleitungenRepository, never()).update(any(DTOKlassenLeitung.class));
	}

	@Test
	@DisplayName("replace lehnt eine Liste mit doppelten Lehrer-IDs mit 400 ab")
	void replaceMitDuplikaten() {
		when(klassenRepository.existsById(ID_KLASSE)).thenReturn(true);

		assertFehler(() -> service.replace(ID_KLASSE, List.of(ID_LEHRER_1, ID_LEHRER_1)), Response.Status.BAD_REQUEST,
				"Es dürfen keine Lehrer-IDs doppelt in der Klassenleitungs-Liste vorhanden sein");
		verify(klassenleitungenRepository, never()).create(any(DTOKlassenLeitung.class));
	}

	@Test
	@DisplayName("replace lehnt unbekannte Lehrkräfte mit 400 ab")
	void replaceMitUnbekannterLehrkraft() {
		when(klassenRepository.existsById(ID_KLASSE)).thenReturn(true);
		when(lehrerRepository.existsByIds(List.of(ID_LEHRER_1, ID_LEHRER_2))).thenReturn(Set.of(ID_LEHRER_1));

		assertFehler(() -> service.replace(ID_KLASSE, List.of(ID_LEHRER_1, ID_LEHRER_2)), Response.Status.BAD_REQUEST,
				"Mindestens zu einer der angegebenen Lehrer-IDs ist keine Lehrkraft bekannt");
		verify(klassenleitungenRepository, never()).create(any(DTOKlassenLeitung.class));
	}

	@Test
	@DisplayName("replace lehnt eine unbekannte Klasse mit 404 ab")
	void replaceMitUnbekannterKlasse() {
		when(klassenRepository.existsById(ID_KLASSE)).thenReturn(false);

		assertFehler(() -> service.replace(ID_KLASSE, List.of(ID_LEHRER_1)), Response.Status.NOT_FOUND, "Keine Klasse zur ID 7 gefunden.");
		verify(klassenleitungenRepository, never()).findListByIdKlasse(ID_KLASSE);
	}

	@Test
	@DisplayName("replace lehnt null und null-Einträge in der Liste mit 400 ab")
	void replaceMitUngueltigerListe() {
		final var meldung = "Die Liste der Lehrer-IDs der Klassenleitungen darf nicht null sein und keine null-Einträge enthalten.";

		assertFehler(() -> service.replace(ID_KLASSE, null), Response.Status.BAD_REQUEST, meldung);
		assertFehler(() -> service.replace(ID_KLASSE, Arrays.asList(ID_LEHRER_1, null)), Response.Status.BAD_REQUEST, meldung);
	}

	@Test
	@DisplayName("replace meldet einen Fehler beim Löschen mit 500")
	void replaceFehlerBeimLoeschen() {
		final var lehrer1 = createKlassenleitung(ID_LEHRER_1, 1);
		stubKlasseUndBisherige(lehrer1);
		when(lehrerRepository.existsByIds(List.of())).thenReturn(Set.of());
		doThrow(new RepositoryException("Fehler")).when(klassenleitungenRepository).delete(lehrer1);

		assertFehler(() -> service.replace(ID_KLASSE, List.of()), Response.Status.INTERNAL_SERVER_ERROR,
				"Die Klassenleitung mit der ID 101 konnte bei der Klasse mit der ID 7 nicht entfernt werden.");
	}

	@Test
	@DisplayName("replace meldet einen Fehler beim Anlegen mit 500")
	void replaceFehlerBeimAnlegen() {
		stubKlasseUndBisherige();
		stubLehrerExistieren(ID_LEHRER_1);
		doThrow(new RepositoryException("Fehler")).when(klassenleitungenRepository).create(any(DTOKlassenLeitung.class));

		assertFehler(() -> service.replace(ID_KLASSE, List.of(ID_LEHRER_1)), Response.Status.INTERNAL_SERVER_ERROR,
				"Klassenleitung konnte nicht persistiert werden");
	}

	@Test
	@DisplayName("replace meldet einen Fehler beim Aktualisieren mit 500")
	void replaceFehlerBeimAktualisieren() {
		final var lehrer1 = createKlassenleitung(ID_LEHRER_1, 2);
		stubKlasseUndBisherige(lehrer1);
		stubLehrerExistieren(ID_LEHRER_1);
		doThrow(new RepositoryException("Fehler")).when(klassenleitungenRepository).update(lehrer1);

		assertFehler(() -> service.replace(ID_KLASSE, List.of(ID_LEHRER_1)), Response.Status.INTERNAL_SERVER_ERROR,
				"Klassenleitung konnte nicht aktualisiert werden");
	}

}
