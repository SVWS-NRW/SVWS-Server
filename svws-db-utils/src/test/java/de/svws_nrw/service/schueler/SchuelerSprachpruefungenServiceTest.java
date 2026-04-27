package de.svws_nrw.service.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.utils.CoreTypeRessource;
import de.svws_nrw.asd.data.schueler.Sprachpruefung;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import de.svws_nrw.data.schueler.DataSchuelerSprachpruefung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;
import de.svws_nrw.repo.schueler.SchuelerSprachpruefungenRepository;

@ExtendWith(MockitoExtension.class)
class SchuelerSprachpruefungenServiceTest {

	@BeforeAll
	static void initCoreTypes() {
		CoreTypeRessource.initAll();
	}

	@Mock
	private SchuelerSprachpruefungenRepository schuelerSprachpruefungenRepository;

	private final SchuelerSprachpruefungenService service = new SchuelerSprachpruefungenService();

	@Test
	@DisplayName("Test: pruefe, ob toApi die DTO-Daten korrekt in Sprachpruefung konvertiert.")
	void testToApi() {
		final DTOSchuelerSprachpruefungen dto = new DTOSchuelerSprachpruefungen(10L, 100L, "IM");
		dto.ASDJahrgang = "09";
		dto.Pruefungsdatum = "2024-06-15";
		dto.IstHSUPruefung = true;
		dto.IstFeststellungspruefung = false;
		dto.KannErstePflichtfremdspracheErsetzen = true;
		dto.KannZweitePflichtfremdspracheErsetzen = false;
		dto.KannWahlpflichtfremdspracheErsetzen = true;
		dto.KannBelegungAlsFortgefuehrteSpracheErlauben = false;
		dto.Referenzniveau = null;
		dto.NotePruefung = null;
		dto.Zeugnisbezeichnung = "Englisch";

		final Schuljahresabschnitt abschnitt = new Schuljahresabschnitt();
		abschnitt.schuljahr = 2024;

		final Sprachpruefung result = service.toApi(dto, abschnitt);

		assertNotNull(result);
		assertEquals(dto.Sprache, result.sprache);
		assertEquals(dto.ASDJahrgang, result.jahrgang);
		assertEquals(dto.Pruefungsdatum, result.pruefungsdatum);
		assertEquals(SprachendatenUtils.getErsetzeSprache(dto.Sprache), result.ersetzteSprache);
		assertTrue(result.istHSUPruefung);
		assertFalse(result.istFeststellungspruefung);
		assertTrue(result.kannErstePflichtfremdspracheErsetzen);
		assertFalse(result.kannZweitePflichtfremdspracheErsetzen);
		assertTrue(result.kannWahlpflichtfremdspracheErsetzen);
		assertFalse(result.kannBelegungAlsFortgefuehrteSpracheErlauben);
		assertNull(result.referenzniveau);
		assertNull(result.note);
		assertEquals(DataSchuelerSprachpruefung.mapZeugnisbezeichnung(dto.Zeugnisbezeichnung, dto.Sprache), result.zeugnisbezeichnung);
	}

	@Test
	@DisplayName("Test: pruefe, ob fetchData/getSprachpruefungen die Daten über das Repository lädt und korrekt konvertiert.")
	void testFetchDataAndGetSprachpruefungen() {
		final SchuelerSprachpruefungenService repositoryBackedService = new SchuelerSprachpruefungenService(schuelerSprachpruefungenRepository);
		final DTOSchuelerSprachpruefungen dto = new DTOSchuelerSprachpruefungen(10L, 100L, "IM");
		dto.ASDJahrgang = "09";
		dto.Pruefungsdatum = "2024-06-15";

		when(schuelerSprachpruefungenRepository.getMapBySchuelerIDs(List.of(100L)))
				.thenReturn(Map.of(100L, List.of(dto)));

		final Schuljahresabschnitt abschnitt = new Schuljahresabschnitt();
		abschnitt.schuljahr = 2024;

		repositoryBackedService.fetchData(List.of(100L));
		final List<Sprachpruefung> result = repositoryBackedService.getSprachpruefungen(100L, abschnitt);

		assertEquals(1, result.size());
		assertEquals("IM", result.get(0).sprache);
		assertEquals("09", result.get(0).jahrgang);
	}

	@Test
	@DisplayName("Test: prüfe, ob fetchData ohne Repository eine IllegalStateException wirft.")
	void testFetchDataWithoutRepositoryThrows() {
		assertThrows(IllegalStateException.class, () -> service.fetchData(List.of(100L)));
	}
}
