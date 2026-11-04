package de.svws_nrw.service.enm;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulKonfigurationClientRepository;
import de.svws_nrw.repo.enm.NotenmodulKonfigurationServerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittKursartZuweisungenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.teilleistung.SchuelerTeilleistungRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für die Patch-Methoden des {@link NotenmodulLocalService}.
 */
@DisplayName("NotenmodulLocalService")
class NotenmodulLocalServiceTest {

	private MockedStatic<TransactionSupport> transactions;

	private SchuelerTeilleistungRepository schuelerTeilleistungRepository;
	private SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;
	private SchuelerAnkreuzkompetenzRepository schuelerAnkreuzkompetenzRepository;

	private NotenmodulLocalService service;


	@BeforeEach
	void setUp() {
		// Die Transaktion wird durchgereicht, da keine Datenbank-Verbindung existiert
		transactions = mockStatic(TransactionSupport.class);
		transactions.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(inv -> inv.getArgument(0, Supplier.class).get());
		transactions.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Runnable>any()))
				.thenAnswer(inv -> {
					inv.getArgument(0, Runnable.class).run();
					return null;
				});

		schuelerTeilleistungRepository = mock(SchuelerTeilleistungRepository.class);
		schuelerLeistungsdatenRepository = mock(SchuelerLeistungsdatenRepository.class);
		schuelerAnkreuzkompetenzRepository = mock(SchuelerAnkreuzkompetenzRepository.class);
		service = new NotenmodulLocalService(
				mock(NotenmodulKonfigurationClientRepository.class),
				mock(NotenmodulKonfigurationServerRepository.class),
				mock(SchuelerRepository.class),
				mock(SchuelerLernabschnittRepository.class),
				mock(SchuelerLernabschnittBemerkungRepository.class),
				mock(SchuelerLernabschnittKursartZuweisungenRepository.class),
				schuelerLeistungsdatenRepository,
				schuelerTeilleistungRepository,
				schuelerAnkreuzkompetenzRepository,
				mock(AnkreuzkompetenzRepository.class));
	}

	@AfterEach
	void tearDown() {
		transactions.close();
	}


	// -------------------------------------------------------------------------
	// Befund 6: Teilleistung ohne Teilleistungsart
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patchTeilleistung: Eine Teilleistung ohne Teilleistungsart wird mit BAD_REQUEST abgelehnt")
	void patchTeilleistungOhneArt() {
		final long idTeilleistung = 500L;
		final DTOSchuelerTeilleistung teilleistung = new DTOSchuelerTeilleistung(idTeilleistung, 200L);
		teilleistung.Art_ID = null;
		when(schuelerTeilleistungRepository.findById(idTeilleistung)).thenReturn(Optional.of(teilleistung));
		final NotenmodulLocalTeilleistungPatchRequest patch = new NotenmodulLocalTeilleistungPatchRequest();
		patch.id = idTeilleistung;
		patch.note = JsonNullable.of("2");

		assertThatThrownBy(() -> service.patchTeilleistung(patch, mock(Benutzer.class)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST)
				.hasMessageContaining("500")
				.hasMessageContaining("Teilleistungsart");
		verify(schuelerLeistungsdatenRepository, never()).findById(anyLong());
	}


	// -------------------------------------------------------------------------
	// Eingabevalidierung: Stufen der Ankreuzkompetenz
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patchAnkreuzkompetenz: Ein Array mit weniger als 5 Stufen wird mit BAD_REQUEST abgelehnt")
	void patchAnkreuzkompetenzZuWenigStufen() {
		final NotenmodulLocalAnkreuzkompetenzPatchRequest patch = new NotenmodulLocalAnkreuzkompetenzPatchRequest();
		patch.id = 600L;
		patch.stufen = new boolean[3];

		assertThatThrownBy(() -> service.patchAnkreuzkompetenz(patch, mock(Benutzer.class)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST)
				.hasMessageContaining("5");
		verify(schuelerAnkreuzkompetenzRepository, never()).findById(anyLong());
	}

	@Test
	@DisplayName("patchAnkreuzkompetenz: Ein Array mit mehr als 5 Stufen wird mit BAD_REQUEST abgelehnt")
	void patchAnkreuzkompetenzZuVieleStufen() {
		final NotenmodulLocalAnkreuzkompetenzPatchRequest patch = new NotenmodulLocalAnkreuzkompetenzPatchRequest();
		patch.id = 600L;
		patch.stufen = new boolean[6];

		assertThatThrownBy(() -> service.patchAnkreuzkompetenz(patch, mock(Benutzer.class)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		verify(schuelerAnkreuzkompetenzRepository, never()).findById(anyLong());
	}

	@Test
	@DisplayName("patchAnkreuzkompetenz: Fehlende Stufen (null) werden mit BAD_REQUEST abgelehnt")
	void patchAnkreuzkompetenzStufenNull() {
		final NotenmodulLocalAnkreuzkompetenzPatchRequest patch = new NotenmodulLocalAnkreuzkompetenzPatchRequest();
		patch.id = 600L;
		patch.stufen = null;

		assertThatThrownBy(() -> service.patchAnkreuzkompetenz(patch, mock(Benutzer.class)))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		verify(schuelerAnkreuzkompetenzRepository, never()).findById(anyLong());
	}

}
