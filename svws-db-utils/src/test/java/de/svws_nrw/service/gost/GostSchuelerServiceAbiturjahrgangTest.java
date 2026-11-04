package de.svws_nrw.service.gost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
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
 * Prüft die Regel, dass für einen Abiturjahrgang bei Schülern mit einer Wiederholung in einen anderen Jahrgang die aktuelle Planung
 * gilt und bei allen anderen die übliche Zuordnung. Beispiel ist der Abiturjahrgang 2028 mit einem Schüler ohne Wiederholung,
 * einem Schüler, der nach 2029 wechselt, und einem Schüler, der aus 2027 nach 2028 kommt.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für die Daten eines Abiturjahrgangs bei Wiederholungen")
class GostSchuelerServiceAbiturjahrgangTest {

	private static final long ID_OHNE_WIEDERHOLUNG = 1L;
	private static final long ID_WECHSELT_HINAUS = 2L;
	private static final long ID_WECHSELT_HINEIN = 3L;
	private static final List<Long> IDS = List.of(ID_OHNE_WIEDERHOLUNG, ID_WECHSELT_HINAUS, ID_WECHSELT_HINEIN);

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

	private GostSchuelerService erzeugeService() {
		return new GostSchuelerService(benutzerRepository, jahrgangRepository, schuelerRepository, schuelerLernabschnittRepository, gostSchuelerRepository,
				gostSchuelerFachbelegungenRepository, gostSchuelerWiederholungRepository, gostSchuelerWiederholungFachwahlenRepository,
				gostJahrgangsdatenRepository, benutzerKompetenzService, false);
	}

	private static DTOGostSchuelerWiederholung erzeugeWiederholung(final long idSchueler, final int abiturjahr, final long idVorgabe) {
		final DTOGostSchuelerWiederholung wiederholung = new DTOGostSchuelerWiederholung(idSchueler, abiturjahr, GostHalbjahr.Q11);
		wiederholung.GKL_EF_AF1_Klausurvorgabe_ID = idVorgabe;
		return wiederholung;
	}

	private static DTOGostSchueler erzeugeGostSchueler(final long idSchueler, final long idVorgabe) {
		final DTOGostSchueler gostSchueler = new DTOGostSchueler(idSchueler, false);
		gostSchueler.GKL_EF_AF1_Klausurvorgabe_ID = idVorgabe;
		return gostSchueler;
	}

	private void mockWiederholungen() {
		when(gostSchuelerWiederholungRepository.findMapByIds(anyCollection())).thenReturn(Map.of(
				ID_WECHSELT_HINAUS, erzeugeWiederholung(ID_WECHSELT_HINAUS, 2029, 202L),
				ID_WECHSELT_HINEIN, erzeugeWiederholung(ID_WECHSELT_HINEIN, 2028, 203L)));
	}

	@Test
	@DisplayName("getIdsWiederholungAndererJahrgang: Nur die Wiederholung in einen anderen Jahrgang zählt")
	void testIdsAndererJahrgang() {
		mockWiederholungen();
		assertThat(erzeugeService().getIdsWiederholungAndererJahrgang(IDS, 2028)).containsExactly(ID_WECHSELT_HINAUS);
	}

	@Test
	@DisplayName("getIdsWiederholungAndererJahrgang: Ohne vorgegebenen Jahrgang gilt für alle die Planung")
	void testIdsOhneSicht() {
		assertThat(erzeugeService().getIdsWiederholungAndererJahrgang(IDS, null)).isEmpty();
	}

	@Test
	@DisplayName("getMapGKLWahlen: Wer hinauswechselt, erhält die bisherigen Wahlen, wer hineinwechselt, die Wahlen der Wiederholung")
	void testGKLWahlenFuerAbiturjahrgang() {
		mockWiederholungen();
		when(gostSchuelerRepository.findMapByIds(anyCollection())).thenReturn(Map.of(
				ID_OHNE_WIEDERHOLUNG, erzeugeGostSchueler(ID_OHNE_WIEDERHOLUNG, 101L),
				ID_WECHSELT_HINAUS, erzeugeGostSchueler(ID_WECHSELT_HINAUS, 102L),
				ID_WECHSELT_HINEIN, erzeugeGostSchueler(ID_WECHSELT_HINEIN, 103L)));

		final Map<Long, GostSchuelerGKLWahl> wahlen = erzeugeService().getMapGKLWahlen(IDS, 2028);

		assertThat(wahlen).hasSize(3);
		assertThat(wahlen.get(ID_OHNE_WIEDERHOLUNG).idKlausurvorgabeEF_Sprachen).isEqualTo(101L);
		assertThat(wahlen.get(ID_WECHSELT_HINAUS).idKlausurvorgabeEF_Sprachen).isEqualTo(102L);
		assertThat(wahlen.get(ID_WECHSELT_HINEIN).idKlausurvorgabeEF_Sprachen).isEqualTo(203L);
	}

}
