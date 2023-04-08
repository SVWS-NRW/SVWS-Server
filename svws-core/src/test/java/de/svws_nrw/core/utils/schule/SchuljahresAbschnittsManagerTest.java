package de.svws_nrw.core.utils.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.schule.Schuljahresabschnitt;

/**
 * Testklasse für den SchuljahresabschnittsManager
 *
 */
class SchuljahresAbschnittsManagerTest {

	private final SchuljahresAbschnittsManager manager0;
	private final SchuljahresAbschnittsManager manager1;
	private final SchuljahresAbschnittsManager manager2;
	private final SchuljahresAbschnittsManager manager3;
	private final SchuljahresAbschnittsManager manager4;
	private final SchuljahresAbschnittsManager manager5;
	private final Schuljahresabschnitt abschnitt;

	/**
	 * Konstruktor für die Testklasse, initialisiert den Schuljahresabschnitt und
	 * die zu testenden SchuljahresAbschnittsManager für 0 bis 5 Abschnitte im Jahr
	 */
	SchuljahresAbschnittsManagerTest() {
		abschnitt = new Schuljahresabschnitt();
		abschnitt.abschnitt = 1;
		abschnitt.schuljahr = 2022;
		manager0 = new SchuljahresAbschnittsManager(abschnitt, 0);
		manager1 = new SchuljahresAbschnittsManager(abschnitt, 1);
		manager2 = new SchuljahresAbschnittsManager(abschnitt, 2);
		manager3 = new SchuljahresAbschnittsManager(abschnitt, 3);
		manager4 = new SchuljahresAbschnittsManager(abschnitt, 4);
		manager5 = new SchuljahresAbschnittsManager(abschnitt, 5);
	}

	/**
	 * Test der Methode
	 * {@link SchuljahresAbschnittsManager#getSchuljahresAbschnittAsString()}
	 */
	@Test
	void testGetSchuljahresAbschnittAsString() {
		assertEquals("2022", manager0.getSchuljahresAbschnittAsString());
		assertEquals("2022", manager1.getSchuljahresAbschnittAsString());
		assertEquals("S1 2022", manager2.getSchuljahresAbschnittAsString());
		assertEquals("T1 2022", manager3.getSchuljahresAbschnittAsString());
		assertEquals("Q1 2022", manager4.getSchuljahresAbschnittAsString());
		assertEquals("1/5 2022", manager5.getSchuljahresAbschnittAsString());
	}

	/**
	 * Test der statischen Methode
	 * {@link SchuljahresAbschnittsManager#createSchuljahresAbschnittString(Schuljahresabschnitt, int)}
	 */
	@Test
	void testStaticGetSchuljahresAbschnittAsString() {
		assertEquals("2022", SchuljahresAbschnittsManager.createSchuljahresAbschnittString(abschnitt, 0));
		assertEquals("2022", SchuljahresAbschnittsManager.createSchuljahresAbschnittString(abschnitt, 1));
		assertEquals("S1 2022", SchuljahresAbschnittsManager.createSchuljahresAbschnittString(abschnitt, 2));
		assertEquals("T1 2022", SchuljahresAbschnittsManager.createSchuljahresAbschnittString(abschnitt, 3));
		assertEquals("Q1 2022", SchuljahresAbschnittsManager.createSchuljahresAbschnittString(abschnitt, 4));
		assertEquals("1/5 2022", SchuljahresAbschnittsManager.createSchuljahresAbschnittString(abschnitt, 5));
	}

	/**
	 * Test der Methode
	 * {@link SchuljahresAbschnittsManager#getRepresentationForAnzahlAbschnitte()}
	 */
	@Test
	void testGetRepresentationForAnzahlAbschnitte() {
		assertEquals("", manager0.getRepresentationForAnzahlAbschnitte());
		assertEquals("", manager1.getRepresentationForAnzahlAbschnitte());
		assertEquals("S", manager2.getRepresentationForAnzahlAbschnitte());
		assertEquals("T", manager3.getRepresentationForAnzahlAbschnitte());
		assertEquals("Q", manager4.getRepresentationForAnzahlAbschnitte());
		assertEquals("", manager5.getRepresentationForAnzahlAbschnitte());
	}

	/**
	 * Test der statischen Methode
	 * {@link SchuljahresAbschnittsManager#createRepresentationForAnzahlAbschnitte(int)}
	 */
	@Test
	void testStaticGetRepresentationForAnzahlAbschnitte() {
		assertEquals("", SchuljahresAbschnittsManager.createRepresentationForAnzahlAbschnitte(0));
		assertEquals("", SchuljahresAbschnittsManager.createRepresentationForAnzahlAbschnitte(1));
		assertEquals("S", SchuljahresAbschnittsManager.createRepresentationForAnzahlAbschnitte(2));
		assertEquals("T", SchuljahresAbschnittsManager.createRepresentationForAnzahlAbschnitte(3));
		assertEquals("Q", SchuljahresAbschnittsManager.createRepresentationForAnzahlAbschnitte(4));
		assertEquals("", SchuljahresAbschnittsManager.createRepresentationForAnzahlAbschnitte(5));

	}
}
