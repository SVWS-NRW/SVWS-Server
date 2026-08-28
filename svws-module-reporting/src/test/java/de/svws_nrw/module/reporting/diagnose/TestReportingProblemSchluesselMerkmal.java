package de.svws_nrw.module.reporting.diagnose;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.module.reporting.types.schule.ReportingBild;

/**
 * Prüft das Merkmal des {@link ReportingProblemSchluessel}: Trifft ein Problem eine feste Ausprägung einer Objektart statt eines Datensatzes, so bezeichnet
 * das Merkmal die Ausprägung. Ohne diese Unterscheidung verschmölzen mehrere betroffene Ausprägungen zu einem Befund, und das Log nennte nur den ersten.
 */
class TestReportingProblemSchluesselMerkmal {

	/**
	 * Erzeugt einen Sammler, der in die übergebene Liste protokolliert.
	 *
	 * @param log Die Liste, die die Logeinträge aufnimmt.
	 *
	 * @return Der Sammler.
	 */
	private static ReportingProblemSammler sammler(final LogConsumerList log) {
		final Logger logger = new Logger();
		logger.addConsumer(log);
		return new ReportingProblemSammler(logger);
	}

	/**
	 * Meldet ein fehlendes Bild zu der übergebenen Bilddefinition.
	 *
	 * @param sammler        Der Sammler, der das Problem aufnimmt.
	 * @param bildDefinition Die betroffene Bilddefinition.
	 */
	private static void meldeFehlendesBild(final ReportingProblemSammler sammler, final ReportingBildDefinition bildDefinition) {
		sammler.melde(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
				ReportingProblemSchluessel.fuer(ReportingBild.class, bildDefinition), "In der Logoverwaltung ist kein Bild hinterlegt.", null);
	}


	@Test
	void testZweiAuspraegungenDerselbenObjektartSindZweiSchluessel() {
		assertNotEquals(ReportingProblemSchluessel.fuer(ReportingBild.class, ReportingBildDefinition.DIN5008_BRIEFKOPF),
				ReportingProblemSchluessel.fuer(ReportingBild.class, ReportingBildDefinition.SCHULLOGO_QUADRATISCH));
	}

	@Test
	void testDieselbeAuspraegungIstDerselbeSchluessel() {
		assertEquals(ReportingProblemSchluessel.fuer(ReportingBild.class, ReportingBildDefinition.DIN5008_BRIEFKOPF),
				ReportingProblemSchluessel.fuer(ReportingBild.class, ReportingBildDefinition.DIN5008_BRIEFKOPF));
	}

	@Test
	void testDieBeschreibungNenntDieAuspraegung() {
		assertEquals("ReportingBild DIN5008_BRIEFKOPF",
				ReportingProblemSchluessel.fuer(ReportingBild.class, ReportingBildDefinition.DIN5008_BRIEFKOPF).beschreibung());
	}

	@Test
	void testOhneMerkmalBleibtDieBeschreibungUnveraendert() {
		assertEquals("ReportingBild (gesamter Aufruf)", ReportingProblemSchluessel.fuer(ReportingBild.class).beschreibung());
		assertEquals("ReportingBild 42", ReportingProblemSchluessel.fuer(ReportingBild.class, 42L).beschreibung());
	}

	@Test
	void testJedeBetroffeneAuspraegungWirdEinmalGezaehltUndProtokolliert() {
		final LogConsumerList log = new LogConsumerList();
		final ReportingProblemSammler sammler = sammler(log);

		meldeFehlendesBild(sammler, ReportingBildDefinition.DIN5008_BRIEFKOPF);
		meldeFehlendesBild(sammler, ReportingBildDefinition.SCHULLOGO_QUADRATISCH);
		meldeFehlendesBild(sammler, ReportingBildDefinition.DIN5008_BRIEFKOPF);

		assertEquals(2, sammler.anzahl(), "Zwei betroffene Bilddefinitionen sind zwei Befunde, die Wiederholung der ersten zählt nicht erneut.");
		assertEquals(1, log.getStrings().stream().filter(eintrag -> eintrag.contains("ReportingBild DIN5008_BRIEFKOPF")).count());
		assertEquals(1, log.getStrings().stream().filter(eintrag -> eintrag.contains("ReportingBild SCHULLOGO_QUADRATISCH")).count());
	}

}
