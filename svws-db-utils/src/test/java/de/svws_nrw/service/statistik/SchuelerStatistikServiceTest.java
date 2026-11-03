package de.svws_nrw.service.statistik;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.mapper.schueler.schulbesuch.SchulbesuchMapper;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.schueler.abitur.SchuelerAbiturFachRepository;
import de.svws_nrw.repo.schueler.abitur.SchuelerAbiturRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepository;

/**
 * Testet den Service für den Zugriff auf die Schülerdaten für die Statistik.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SchuelerStatistikServiceTest {

	private static final long ID_ABSCHNITT = 14L;

	private static final long ID_ABSCHNITT_VORHER = 13L;

	@Mock
	private BenutzerAllgemeinRepository benutzerRepository;

	@Mock
	private Benutzer benutzer;

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private SchuelerLernabschnittRepository lernabschnittRepository;

	@Mock
	private SchuelerLeistungsdatenRepository leistungsdatenRepository;

	@Mock
	private SchuelerAbiturRepository abiturRepository;

	@Mock
	private SchuelerAbiturFachRepository abiturFachRepository;

	@Mock
	private FachRepository fachRepository;

	@Mock
	private SchuleRepository schuleRepository;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	private SchuelerStatistikService createService(final long idBetreuung, final Long idBetreuungVorher) {
		final var abschnitt = new Schuljahresabschnitt();
		abschnitt.id = ID_ABSCHNITT;
		abschnitt.schuljahr = 2026;
		abschnitt.abschnitt = 1;
		when(benutzerRepository.getAktuellerBenutzer()).thenReturn(benutzer);
		when(benutzer.schuleGetSchuljahresabschnitt()).thenReturn(abschnitt);
		when(benutzer.schuleGetSchuljahresabschnittByIdOrDefault(anyLong())).thenReturn(abschnitt);
		when(benutzer.schuleGetSchulform()).thenReturn(Schulform.GY);

		final var schueler = new DTOSchueler(1L, "GU-1", false);
		schueler.Geschlecht = Geschlecht.M;
		schueler.Schuljahresabschnitts_ID = ID_ABSCHNITT;
		schueler.idStatus = 2;
		final var lernabschnitt = new DTOSchuelerLernabschnittsdaten(10L, 1L, ID_ABSCHNITT, true, false, idBetreuung);
		lernabschnitt.Klassenart = "PK";
		when(schuelerRepository.getMapByStatusAndSchuljahresabschnitt(anyLong(), anyCollection())).thenReturn(Map.of(1L, schueler));
		when(lernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(anyCollection(), eq(ID_ABSCHNITT))).thenReturn(Map.of(1L, lernabschnitt));
		if (idBetreuungVorher != null) {
			final var abschnittVorher = new Schuljahresabschnitt();
			abschnittVorher.id = ID_ABSCHNITT_VORHER;
			abschnittVorher.schuljahr = 2025;
			abschnittVorher.abschnitt = 2;
			final var lernabschnittVorher = new DTOSchuelerLernabschnittsdaten(9L, 1L, ID_ABSCHNITT_VORHER, true, false, idBetreuungVorher);
			lernabschnittVorher.Klassenart = "PK";
			when(benutzer.schuleGetAbschnittBySchuljahrUndHalbjahr(2025, 2)).thenReturn(abschnittVorher);
			when(lernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(anyCollection(), eq(ID_ABSCHNITT_VORHER)))
					.thenReturn(Map.of(1L, lernabschnittVorher));
		}
		when(leistungsdatenRepository.getMapListByLernabschnittsIds(anyCollection())).thenReturn(Map.of(10L, List.of()));
		when(schuleRepository.getSchulenBySchulnummer()).thenReturn(new HashMap<>());
		when(abiturRepository.getListBySchuelerIds(anyCollection())).thenReturn(List.of());
		when(abiturFachRepository.getListBySchuelerIdsNurPruefungsfaecher(anyCollection())).thenReturn(List.of());
		when(fachRepository.findListByIds(anyCollection())).thenReturn(List.of());
		return new SchuelerStatistikService(new SchuelerStatistikRepositories(benutzerRepository, schuelerRepository, lernabschnittRepository,
				leistungsdatenRepository, abiturRepository, abiturFachRepository, fachRepository, schuleRepository), SchulbesuchMapper.INSTANCE);
	}

	@Test
	@DisplayName("getList: Die Betreuungsart wird aus dem Lernabschnitt übernommen")
	void testGetListUebernimmtIdBetreuung() {
		final var service = createService(4L, null);

		final var result = service.getList();

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().lernabschnitte).hasSize(1);
		assertThat(result.getFirst().lernabschnitte.getFirst().idBetreuung).isEqualTo(4L);
	}

	@Test
	@DisplayName("getList: Auch der Lernabschnitt der letzten Versetzung übernimmt seine eigene Betreuungsart")
	void testGetListUebernimmtIdBetreuungAuchFuerVorherigenLernabschnitt() {
		final var service = createService(4L, 2L);

		final var result = service.getList();

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().lernabschnitte).hasSize(2);
		assertThat(result.getFirst().lernabschnitte.get(0).idBetreuung).isEqualTo(2L);
		assertThat(result.getFirst().lernabschnitte.get(1).idBetreuung).isEqualTo(4L);
	}

	@Test
	@DisplayName("getList: Die Betreuungsart 1 (keine Teilnahme) wird unverändert übernommen")
	void testGetListUebernimmtStandardBetreuungsart() {
		final var service = createService(1L, null);

		final var result = service.getList();

		assertThat(result.getFirst().lernabschnitte.getFirst().idBetreuung).isEqualTo(1L);
	}
}
