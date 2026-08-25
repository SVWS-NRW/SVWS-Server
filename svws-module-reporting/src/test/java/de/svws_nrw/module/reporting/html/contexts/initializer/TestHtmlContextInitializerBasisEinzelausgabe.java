package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Einzelausgabe-Entscheidung in {@link HtmlContextInitializerBasis}. Die Zusage des Datenaufbaus,
 * {@link HtmlContextAufbau#unterstuetztEinzelausgabe()}, entscheidet, ob der Initializer den Schlüssel des Haupt-Contexts herausgibt oder den Client-Fehler
 * wirft. Die Basisklasse ist der einzige Leser dieser Zusage; geprüft wird sie deshalb über einem Stub-Aufbau und nicht je Datenaufbau.
 */
class TestHtmlContextInitializerBasisEinzelausgabe {

	/** Der Schlüssel, den der Stub-Aufbau als Haupt-Context nennt. */
	private static final String SCHLUESSEL = "Probe";

	/** Die Reportvorlage, deren Bezeichnung die Meldung des Client-Fehlers nennt. */
	private static final ReportingReportvorlage VORLAGE = ReportingReportvorlage.GOST_LAUFBAHNPLANUNG_ABITURJAHRGANG_V_FACHWAHLSTATISTIKEN;


	/**
	 * Ein Datenaufbau, der allein seine Zusage zur Einzelausgabe trägt.
	 *
	 * @param einzelausgabe Die Zusage.
	 */
	private record StubAufbau(boolean einzelausgabe) implements HtmlContextAufbau {

		@Override
		public String contextSchluessel() {
			return SCHLUESSEL;
		}

		@Override
		public boolean unterstuetztEinzelausgabe() {
			return einzelausgabe;
		}

		@Override
		public HtmlContextInitializer initializer(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts) {
			return new StubInitializer(reportingContext, mapHtmlContexts, this);
		}
	}

	/** Ein Initializer ohne eigenen Datenaufbau; er erbt allein die Entscheidung der Basisklasse. */
	private static final class StubInitializer extends HtmlContextInitializerBasis {

		/**
		 * Erzeugt den Initializer über dem Stub-Aufbau.
		 *
		 * @param reportingContext Der gemockte Reporting-Context.
		 * @param mapHtmlContexts  Die Context-Map.
		 * @param aufbau           Der Stub-Aufbau mit seiner Zusage.
		 */
		StubInitializer(final ReportingContext reportingContext, final Map<String, HtmlContext<?>> mapHtmlContexts, final HtmlContextAufbau aufbau) {
			super(reportingContext, mapHtmlContexts, aufbau);
		}

		@Override
		public void init() {
			// Kein Datenaufbau nötig: Geprüft wird allein die Entscheidung über die Einzelausgabe.
		}

		@Override
		public boolean meldetAusgabeumfangImContextAufbau() {
			return false;
		}
	}


	/**
	 * Baut einen Initializer über einem Stub-Aufbau mit der übergebenen Zusage.
	 *
	 * @param einzelausgabe Die Zusage des Stub-Aufbaus.
	 *
	 * @return Der Initializer.
	 */
	private static HtmlContextInitializer initializer(final boolean einzelausgabe) {
		final ReportingParameterTypisiert parameter = mock(ReportingParameterTypisiert.class);
		when(parameter.reportVorlage()).thenReturn(VORLAGE);
		final ReportingContext reportingContext = mock(ReportingContext.class);
		when(reportingContext.reportingParameter()).thenReturn(parameter);
		return new StubAufbau(einzelausgabe).initializer(reportingContext, new HashMap<>());
	}


	@Test
	void testOhneZusageWeistDieBasisklasseDieEinzelausgabeAlsClientFehlerAb() {
		// Der Anwender hat für diese Vorlage eine Ausgabe verlangt, die es nicht gibt. Das ist seine Anfrage, kein Serverfehler.
		final HtmlContextInitializer initializer = initializer(false);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, initializer::einzelContextBezeichnung);

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		final String meldung = assertInstanceOf(String.class, aoe.getBody(), "Ohne String-Body bliebe die Kopfzeile der Fehlerantwort ohne Abbruchgrund.");
		assertTrue(meldung.contains(VORLAGE.getBezeichnung()), "Die Meldung nennt die Vorlage, die der Anwender gewählt hat: " + meldung);
	}

	@Test
	void testMitZusageGibtDieBasisklasseDenSchluesselDesHauptContextsHeraus() throws ApiOperationException {
		assertEquals(SCHLUESSEL, initializer(true).einzelContextBezeichnung());
	}

}
