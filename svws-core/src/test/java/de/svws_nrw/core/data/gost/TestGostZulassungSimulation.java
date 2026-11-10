package de.svws_nrw.core.data.gost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Prüft, dass der Stand der Simulation der Abiturzulassung beim Ablegen als JSON-Text erhalten bleibt.
 */
@DisplayName("Teste die JSON-Darstellung der Simulation der Abiturzulassung")
class TestGostZulassungSimulation {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * Ein neuer Stand ist leer und hat keine besondere Lernleistung.
	 */
	@Test
	@DisplayName("Ein neuer Stand hat die erwarteten Vorgabewerte")
	void testVorgabewerte() {
		final GostZulassungSimulation simulation = new GostZulassungSimulation();
		assertTrue(simulation.noten.isEmpty());
		assertTrue(simulation.abiturfaecher.isEmpty());
		assertEquals("K", simulation.besondereLernleistung);
		assertNull(simulation.besondereLernleistungNotenpunkte);
	}


	/**
	 * Der gespeicherte Stand enthält weder eine Version noch die Fixierung der vorhandenen Noten, welche nur eine Einstellung der Ansicht ist.
	 *
	 * @throws JsonProcessingException   wenn das Schreiben fehlschlägt
	 */
	@Test
	@DisplayName("Weder eine Version noch die Fixierung der Noten werden gespeichert")
	void testNurEingabenImStand() throws JsonProcessingException {
		final String json = MAPPER.writeValueAsString(new GostZulassungSimulation());
		assertFalse(json.contains("version"));
		assertFalse(json.contains("fixiere"));
	}


	/**
	 * Alle Eingaben überstehen das Schreiben und Lesen als JSON-Text.
	 *
	 * @throws JsonProcessingException   wenn das Schreiben oder Lesen fehlschlägt
	 */
	@Test
	@DisplayName("Ein vollständiger Stand bleibt beim Schreiben und Lesen erhalten")
	void testRoundtrip() throws JsonProcessingException {
		final GostZulassungSimulation simulation = new GostZulassungSimulation();
		simulation.besondereLernleistung = "E";
		simulation.besondereLernleistungNotenpunkte = 11;
		final GostZulassungSimulationNote note = new GostZulassungSimulationNote();
		note.idFach = 42;
		note.halbjahr = 4;
		note.notenpunkte = 0;
		simulation.noten.add(note);
		final GostZulassungSimulationAbiturfach abiturfach = new GostZulassungSimulationAbiturfach();
		abiturfach.idFach = 43;
		abiturfach.pruefungNotenpunkte = 15;
		abiturfach.muendlichBestehen = true;
		abiturfach.muendlichFreiwillig = false;
		abiturfach.muendlichReihenfolge = 2;
		abiturfach.muendlichNotenpunkte = 3;
		simulation.abiturfaecher.add(abiturfach);

		final GostZulassungSimulation gelesen = MAPPER.readValue(MAPPER.writeValueAsString(simulation), GostZulassungSimulation.class);

		assertEquals("E", gelesen.besondereLernleistung);
		assertEquals(11, gelesen.besondereLernleistungNotenpunkte);
		assertEquals(1, gelesen.noten.size());
		final GostZulassungSimulationNote noteGelesen = gelesen.noten.get(0);
		assertEquals(42, noteGelesen.idFach);
		assertEquals(4, noteGelesen.halbjahr);
		assertEquals(0, noteGelesen.notenpunkte);
		assertEquals(1, gelesen.abiturfaecher.size());
		final GostZulassungSimulationAbiturfach abiturfachGelesen = gelesen.abiturfaecher.get(0);
		assertEquals(43, abiturfachGelesen.idFach);
		assertEquals(15, abiturfachGelesen.pruefungNotenpunkte);
		assertEquals(Boolean.TRUE, abiturfachGelesen.muendlichBestehen);
		assertEquals(Boolean.FALSE, abiturfachGelesen.muendlichFreiwillig);
		assertEquals(2, abiturfachGelesen.muendlichReihenfolge);
		assertEquals(3, abiturfachGelesen.muendlichNotenpunkte);
	}


	/**
	 * Nicht gesetzte Eingaben bleiben unterscheidbar von einer eingegebenen 0.
	 *
	 * @throws JsonProcessingException   wenn das Schreiben oder Lesen fehlschlägt
	 */
	@Test
	@DisplayName("Fehlende Eingaben bleiben beim Schreiben und Lesen null")
	void testRoundtripOhneEingaben() throws JsonProcessingException {
		final GostZulassungSimulation simulation = new GostZulassungSimulation();
		simulation.noten.add(new GostZulassungSimulationNote());
		simulation.abiturfaecher.add(new GostZulassungSimulationAbiturfach());

		final GostZulassungSimulation gelesen = MAPPER.readValue(MAPPER.writeValueAsString(simulation), GostZulassungSimulation.class);

		assertNull(gelesen.besondereLernleistungNotenpunkte);
		assertNull(gelesen.noten.get(0).notenpunkte);
		final GostZulassungSimulationAbiturfach abiturfach = gelesen.abiturfaecher.get(0);
		assertNull(abiturfach.pruefungNotenpunkte);
		assertNull(abiturfach.muendlichBestehen);
		assertNull(abiturfach.muendlichFreiwillig);
		assertNull(abiturfach.muendlichReihenfolge);
		assertNull(abiturfach.muendlichNotenpunkte);
	}

}
