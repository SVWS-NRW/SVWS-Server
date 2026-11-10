package de.svws_nrw.service.gost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholung;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.gost.GostJahrgangsdatenRepository;
import de.svws_nrw.repo.gost.GostSchuelerFachbelegungenRepository;
import de.svws_nrw.repo.gost.GostSchuelerRepository;
import de.svws_nrw.repo.gost.GostSchuelerWiederholungFachwahlenRepository;
import de.svws_nrw.repo.gost.GostSchuelerWiederholungRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.service.benutzer.BenutzerKompetenzService;

/**
 * Prüft, dass der Stand der Simulation der Abiturzulassung beim Übertragen einer Wiederholung in die Schülerdaten der Oberstufe übernommen wird.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für das Übertragen der Simulation der Abiturzulassung")
class GostSchuelerServiceZulassungSimulationTest {

	private static final long ID_SCHUELER = 1L;

	@Mock
	private BenutzerAllgemeinRepository benutzerRepository;

	@Mock
	private JahrgangRepository jahrgangRepository;

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private SchuelerLernabschnittRepository schuelerLernabschnittRepository;

	@Mock
	private GostSchuelerRepository gostSchuelerRepository;

	@Mock
	private GostSchuelerFachbelegungenRepository gostSchuelerFachbelegungenRepository;

	@Mock
	private GostSchuelerWiederholungRepository gostSchuelerWiederholungRepository;

	@Mock
	private GostSchuelerWiederholungFachwahlenRepository gostSchuelerWiederholungFachwahlenRepository;

	@Mock
	private GostJahrgangsdatenRepository gostJahrgangsdatenRepository;

	@Mock
	private BenutzerKompetenzService benutzerKompetenzService;

	private GostSchuelerService erzeugeService(final boolean aktuellePlanung) {
		return new GostSchuelerService(benutzerRepository, jahrgangRepository, schuelerRepository, schuelerLernabschnittRepository, gostSchuelerRepository,
				gostSchuelerFachbelegungenRepository, gostSchuelerWiederholungRepository, gostSchuelerWiederholungFachwahlenRepository,
				gostJahrgangsdatenRepository, benutzerKompetenzService, aktuellePlanung);
	}

	private static String erzeugeJson(final int notenpunkte) {
		return "{\"noten\":[{\"idFach\":42,\"halbjahr\":4,\"notenpunkte\":" + notenpunkte + "}],"
				+ "\"abiturfaecher\":[],\"besondereLernleistung\":\"K\",\"besondereLernleistungNotenpunkte\":null}";
	}

	private static DTOGostSchueler erzeugeGostSchueler(final String json) {
		final DTOGostSchueler gostSchueler = new DTOGostSchueler(ID_SCHUELER, false);
		gostSchueler.ZulassungSimulation = json;
		return gostSchueler;
	}

	private static DTOGostSchuelerWiederholung erzeugeWiederholung(final String json) {
		final DTOGostSchuelerWiederholung wiederholung = new DTOGostSchuelerWiederholung(ID_SCHUELER, 2029, GostHalbjahr.Q11);
		wiederholung.ZulassungSimulation = json;
		return wiederholung;
	}

	@Test
	@DisplayName("uebertrageWiederholung: Der Stand der Wiederholung ersetzt den Stand in den Schülerdaten der Oberstufe")
	void testUebertragen() {
		final DTOGostSchueler gostSchueler = erzeugeGostSchueler(erzeugeJson(1));
		mockUebertragen(erzeugeWiederholung(erzeugeJson(9)), gostSchueler);

		erzeugeService(false).uebertrageWiederholung(ID_SCHUELER);

		assertThat(gostSchueler.ZulassungSimulation).isEqualTo(erzeugeJson(9));
		verify(gostSchuelerRepository).update(gostSchueler);
	}

	@Test
	@DisplayName("uebertrageWiederholung: Auch ein leerer Stand der Wiederholung ersetzt den Stand in den Schülerdaten der Oberstufe")
	void testUebertragenLeer() {
		final DTOGostSchueler gostSchueler = erzeugeGostSchueler(erzeugeJson(1));
		mockUebertragen(erzeugeWiederholung(null), gostSchueler);

		erzeugeService(false).uebertrageWiederholung(ID_SCHUELER);

		assertThat(gostSchueler.ZulassungSimulation).isNull();
		verify(gostSchuelerRepository).update(gostSchueler);
	}

	private void mockUebertragen(final DTOGostSchuelerWiederholung wiederholung, final DTOGostSchueler gostSchueler) {
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(wiederholung));
		when(gostSchuelerFachbelegungenRepository.getMapBySchuelerID(anyCollection())).thenReturn(Map.of());
		when(gostSchuelerWiederholungFachwahlenRepository.getMapBySchuelerID(anyCollection())).thenReturn(Map.of());
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(gostSchueler));
	}

}
