package de.svws_nrw.service.gost;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.gost.GostSchuelerWiederholung;
import de.svws_nrw.core.types.gost.GostHalbjahr;

/**
 * Prüft die Regel, dass eine Wiederholung für Halbjahre ab dem Einstieg und für die Planung maßgeblich ist, davor aber
 * der Jahrgang des Lernabschnitts gilt. Beispiel ist eine Wiederholung, die von Q1.2 im Abiturjahrgang 2028 nach Q1.1 im Jahrgang 2029 führt.
 */
@DisplayName("Tests für die Maßgeblichkeit einer Wiederholung")
class GostSchuelerServiceWiederholungTest {

	private static GostSchuelerWiederholung erzeugeWiederholung() {
		final GostSchuelerWiederholung wiederholung = new GostSchuelerWiederholung();
		wiederholung.idSchueler = 1;
		wiederholung.abiturjahr = 2029;
		wiederholung.gostHalbjahr = GostHalbjahr.Q11.id;
		return wiederholung;
	}

	@Test
	@DisplayName("Für die Planung ohne Halbjahr ist die Wiederholung maßgeblich")
	void testPlanung() {
		assertThat(GostSchuelerService.istWiederholungMassgeblich(erzeugeWiederholung(), 2028, null)).isTrue();
	}

	@Test
	@DisplayName("Im laufenden Halbjahr des bisherigen Jahrgangs ist die Wiederholung nicht maßgeblich")
	void testLaufendesHalbjahrBisherigerJahrgang() {
		assertThat(GostSchuelerService.istWiederholungMassgeblich(erzeugeWiederholung(), 2028, GostHalbjahr.Q12)).isFalse();
	}

	@Test
	@DisplayName("Ab dem Einstiegshalbjahr ist die Wiederholung maßgeblich, im bisherigen wie im neuen Jahrgang")
	void testAbEinstieg() {
		assertThat(GostSchuelerService.istWiederholungMassgeblich(erzeugeWiederholung(), 2028, GostHalbjahr.Q21)).isTrue();
		assertThat(GostSchuelerService.istWiederholungMassgeblich(erzeugeWiederholung(), 2029, GostHalbjahr.Q11)).isTrue();
	}

	@Test
	@DisplayName("Bei einer Wiederholung von Q2.2 nach Q2.1 gehört der Schüler im Abiturhalbjahr Q2.2 noch zum bisherigen Jahrgang")
	void testQ22NachQ21ImAbiturhalbjahr() {
		final GostSchuelerWiederholung wiederholung = erzeugeWiederholung();
		wiederholung.gostHalbjahr = GostHalbjahr.Q21.id;
		assertThat(GostSchuelerService.istWiederholungMassgeblich(wiederholung, 2028, GostHalbjahr.Q22)).isFalse();
		assertThat(GostSchuelerService.istWiederholungMassgeblich(wiederholung, 2029, GostHalbjahr.Q22)).isTrue();
	}

	@Test
	@DisplayName("Planung: Vor dem Einstieg entscheidet im bisherigen Jahrgang der Lernabschnitt, der Schüler gehört also noch dazu")
	void testPlanungBisherigerJahrgangVorDemEinstieg() {
		assertThat(GostSchuelerService.entscheidetWiederholung(erzeugeWiederholung(), 2028, null, 2026, 2)).isFalse();
	}

	@Test
	@DisplayName("Planung: Ab dem Einstieg gehört der Schüler nicht mehr zum bisherigen Jahrgang")
	void testPlanungBisherigerJahrgangAbEinstieg() {
		assertThat(GostSchuelerService.entscheidetWiederholung(erzeugeWiederholung(), 2028, null, 2027, 1)).isTrue();
	}

	@Test
	@DisplayName("Planung: Im neuen Jahrgang entscheidet immer die Wiederholung")
	void testPlanungNeuerJahrgang() {
		assertThat(GostSchuelerService.entscheidetWiederholung(erzeugeWiederholung(), 2029, null, 2026, 2)).isTrue();
	}

	@Test
	@DisplayName("Mit Halbjahr entscheidet die Wiederholung ab dem Einstieg, unabhängig vom Abschnitt der Schule")
	void testMitHalbjahr() {
		assertThat(GostSchuelerService.entscheidetWiederholung(erzeugeWiederholung(), 2028, GostHalbjahr.Q12, 2026, 2)).isFalse();
		assertThat(GostSchuelerService.entscheidetWiederholung(erzeugeWiederholung(), 2028, GostHalbjahr.Q21, 2026, 2)).isTrue();
	}

	@Test
	@DisplayName("Je Halbjahr gehört der Schüler vor dem Einstieg zum bisherigen und ab dem Einstieg zum neuen Jahrgang")
	void testGehoertImHalbjahrZumJahrgang() {
		assertThat(GostSchuelerService.gehoertImHalbjahrZumJahrgang(erzeugeWiederholung(), 2028, GostHalbjahr.Q12)).isTrue();
		assertThat(GostSchuelerService.gehoertImHalbjahrZumJahrgang(erzeugeWiederholung(), 2028, GostHalbjahr.Q21)).isFalse();
		assertThat(GostSchuelerService.gehoertImHalbjahrZumJahrgang(erzeugeWiederholung(), 2029, GostHalbjahr.EF2)).isFalse();
		assertThat(GostSchuelerService.gehoertImHalbjahrZumJahrgang(erzeugeWiederholung(), 2029, GostHalbjahr.Q11)).isTrue();
	}

	@Test
	@DisplayName("Ein Halbjahr des neuen Jahrgangs vor dem Einstieg zählt noch nicht")
	void testNeuerJahrgangVorDemEinstieg() {
		assertThat(GostSchuelerService.istWiederholungMassgeblich(erzeugeWiederholung(), 2029, GostHalbjahr.EF2)).isFalse();
	}

}
