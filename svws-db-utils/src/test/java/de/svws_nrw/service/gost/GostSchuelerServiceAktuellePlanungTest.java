package de.svws_nrw.service.gost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
 * Prüft die Regel, dass die GKL-Wahlen in der aktuellen Planung aus den Schülerdaten der Oberstufe kommen und dorthin geschrieben
 * werden, auch wenn eine Wiederholung besteht. Außerhalb der aktuellen Planung gehören sie zur Wiederholung.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für die GKL-Wahlen in der aktuellen Planung")
class GostSchuelerServiceAktuellePlanungTest {

	private static final long ID_SCHUELER = 1L;
	private static final long ID_VORGABE_BISHER = 100L;
	private static final long ID_VORGABE_WIEDERHOLUNG = 200L;

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

	private static DTOGostSchueler erzeugeGostSchueler() {
		final DTOGostSchueler gostSchueler = new DTOGostSchueler(ID_SCHUELER, false);
		gostSchueler.GKL_EF_AF1_Klausurvorgabe_ID = ID_VORGABE_BISHER;
		return gostSchueler;
	}

	private static DTOGostSchuelerWiederholung erzeugeWiederholung() {
		final DTOGostSchuelerWiederholung wiederholung = new DTOGostSchuelerWiederholung(ID_SCHUELER, 2029, GostHalbjahr.Q11);
		wiederholung.GKL_EF_AF1_Klausurvorgabe_ID = ID_VORGABE_WIEDERHOLUNG;
		return wiederholung;
	}

	private static GostSchuelerGKLWahl erzeugeWahl() {
		final GostSchuelerGKLWahl wahl = new GostSchuelerGKLWahl();
		wahl.idSchueler = ID_SCHUELER;
		wahl.idKlausurvorgabeEF_Sprachen = 300L;
		return wahl;
	}

	@Test
	@DisplayName("getMapGKLWahlen: In der aktuellen Planung gelten die Wahlen aus den Schülerdaten der Oberstufe")
	void testGetAktuellePlanung() {
		when(gostSchuelerRepository.findMapByIds(List.of(ID_SCHUELER))).thenReturn(Map.of(ID_SCHUELER, erzeugeGostSchueler()));

		final GostSchuelerGKLWahl wahl = erzeugeService(true).getMapGKLWahlen(List.of(ID_SCHUELER)).get(ID_SCHUELER);

		assertThat(wahl.idKlausurvorgabeEF_Sprachen).isEqualTo(ID_VORGABE_BISHER);
		verifyNoInteractions(gostSchuelerWiederholungRepository);
	}

	@Test
	@DisplayName("getMapGKLWahlen: Außerhalb der aktuellen Planung gelten die Wahlen der Wiederholung")
	void testGetWiederholung() {
		when(gostSchuelerWiederholungRepository.findMapByIds(List.of(ID_SCHUELER))).thenReturn(Map.of(ID_SCHUELER, erzeugeWiederholung()));
		when(gostSchuelerRepository.findMapByIds(List.of(ID_SCHUELER))).thenReturn(Map.of(ID_SCHUELER, erzeugeGostSchueler()));

		final GostSchuelerGKLWahl wahl = erzeugeService(false).getMapGKLWahlen(List.of(ID_SCHUELER)).get(ID_SCHUELER);

		assertThat(wahl.idKlausurvorgabeEF_Sprachen).isEqualTo(ID_VORGABE_WIEDERHOLUNG);
	}

	@Test
	@DisplayName("setGKLWahl: In der aktuellen Planung werden die Schülerdaten der Oberstufe geschrieben, nicht die Wiederholung")
	void testSetAktuellePlanung() {
		when(gostSchuelerRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(erzeugeGostSchueler()));

		erzeugeService(true).setGKLWahl(erzeugeWahl());

		final ArgumentCaptor<DTOGostSchueler> captor = ArgumentCaptor.forClass(DTOGostSchueler.class);
		verify(gostSchuelerRepository).update(captor.capture());
		assertThat(captor.getValue().GKL_EF_AF1_Klausurvorgabe_ID).isEqualTo(300L);
		verifyNoInteractions(gostSchuelerWiederholungRepository);
	}

	@Test
	@DisplayName("setGKLWahl: Außerhalb der aktuellen Planung wird die Wiederholung geschrieben")
	void testSetWiederholung() {
		when(gostSchuelerWiederholungRepository.findById(ID_SCHUELER)).thenReturn(Optional.of(erzeugeWiederholung()));

		erzeugeService(false).setGKLWahl(erzeugeWahl());

		final ArgumentCaptor<DTOGostSchuelerWiederholung> captor = ArgumentCaptor.forClass(DTOGostSchuelerWiederholung.class);
		verify(gostSchuelerWiederholungRepository).update(captor.capture());
		assertThat(captor.getValue().GKL_EF_AF1_Klausurvorgabe_ID).isEqualTo(300L);
		verify(gostSchuelerRepository, never()).update(any(DTOGostSchueler.class));
	}

}
