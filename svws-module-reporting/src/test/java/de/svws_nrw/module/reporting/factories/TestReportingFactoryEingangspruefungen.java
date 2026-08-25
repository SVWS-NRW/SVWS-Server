package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.utils.ReportingServerUtils;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft die Eingangsprüfungen des Konstruktors der {@link ReportingFactory}, die eine Anfrage abweisen, bevor Daten geladen werden.
 * <p>Gegenstand ist der Fehlervertrag: Jeder dieser Abbrüche trägt seinen Grund als Meldung der Exception, und aus dieser Meldung entsteht die Kopfzeile
 * der Fehlerantwort. Trägt ein Wurf keine Meldung, fällt die Kopfzeile auf einen Ersatztext zurück und nennt nur die Phase, in der die Erzeugung
 * stehenblieb. Geprüft wird das an der Struktur der Fehlerantwort und nicht am Wortlaut - so bleiben die Tests bei einer Textänderung gültig.</p>
 * <p>Geprüft werden die Prüfungen vor dem Aufbau des Reporting-Context: Bis dorthin kommt die Factory ohne echte Datenbankverbindung aus. Die Angaben zur
 * Server-Version stammen aus dem Manifest der Auslieferung und werden deshalb vorgegeben.</p>
 */
class TestReportingFactoryEingangspruefungen {

	/** Die Überschrift, mit der der Fehlerblock in den Stacktrace übergeht. Auf sie folgt keine Meldung mehr. */
	private static final String UEBERSCHRIFT_STACKTRACE = "### STACKTRACE:";


	/**
	 * Erzeugt die Parameter einer Anfrage mit gültigem Ausgabeformat und dem übergebenen Vorlagennamen.
	 *
	 * @param vorlagenname Der Name der Reportvorlage; darf {@code null} sein.
	 *
	 * @return Die Parameter der Anfrage.
	 */
	private static ReportingParameter parameterMitVorlage(final String vorlagenname) {
		final ReportingParameter parameter = new ReportingParameter();
		parameter.reportvorlage = vorlagenname;
		parameter.ausgabeformat = ReportingAusgabeformat.PDF.getId();
		return parameter;
	}

	/**
	 * Führt den übergebenen Aufbau aus und gibt die dabei geworfene Exception zurück. Die Angaben zur Server-Version werden vorgegeben: Sie stammen aus dem
	 * Manifest der Auslieferung, das im Test nicht vorliegt.
	 *
	 * @param aufbau Der Aufbau der Factory.
	 *
	 * @return Die geworfene Exception.
	 */
	private static ApiOperationException abbruchBeimAufbau(final Supplier<ReportingFactory> aufbau) {
		try (MockedStatic<ReportingServerUtils> serverUtils = mockStatic(ReportingServerUtils.class)) {
			serverUtils.when(ReportingServerUtils::serverversion).thenReturn("Test");
			serverUtils.when(ReportingServerUtils::servermodetext).thenReturn("Test");
			return assertThrows(ApiOperationException.class, aufbau::get);
		}
	}

	/**
	 * Lässt die Factory an der übergebenen Reportvorlage scheitern.
	 *
	 * @param vorlagenname Der Name der Reportvorlage; darf {@code null} sein.
	 *
	 * @return Die geworfene Exception.
	 */
	private static ApiOperationException abbruchMitVorlage(final String vorlagenname) {
		final DBEntityManager conn = mock(DBEntityManager.class);
		final ReportingParameter parameter = parameterMitVorlage(vorlagenname);
		return abbruchBeimAufbau(() -> new ReportingFactory(conn, parameter, ReportingAusgabeformat.PDF));
	}

	/**
	 * Prüft, dass die Fehlerantwort ihren Abbruchgrund aus der Meldung der Exception bildet, und gibt diese Meldung zurück.
	 * <p>Der Fehlerblock nennt zuerst den Vorgang, dann den Fehlertyp mit dem Statuscode und danach die Meldung. Fehlt die Meldung, folgt auf den Fehlertyp
	 * unmittelbar der Stacktrace, und die Kopfzeile trägt den Ersatztext des Vorgangs. Genau diese beiden Fälle unterscheidet die Prüfung - ohne den
	 * Wortlaut der Meldung zu kennen.</p>
	 *
	 * @param aoe Die Exception, die den Abbruch an den Aufrufer trägt.
	 *
	 * @return Die Meldung der Exception, wie sie im Fehlerblock steht.
	 */
	private static String abbruchgrund(final ApiOperationException aoe) {
		final SimpleOperationResponse antwort = assertInstanceOf(SimpleOperationResponse.class, aoe.getBody(),
				"Die Fehlerantwort trägt das Log als SimpleOperationResponse.");
		final List<String> log = antwort.log.stream().map(String::strip).toList();

		final int fehlertyp = log.indexOf("### FEHLER: Fehler vom Typ ApiOperationException - Code: %d".formatted(aoe.getStatus().getStatusCode()));
		assertTrue(fehlertyp >= 0, "Der Fehlerblock muss den Fehlertyp mit dem Statuscode des Abbruchs nennen: " + log);

		final String meldung = log.get(fehlertyp + 1);
		assertNotEquals(UEBERSCHRIFT_STACKTRACE, meldung,
				"Auf den Fehlertyp muss die Meldung der Exception folgen; ohne sie nennt die Kopfzeile nur die Phase des Abbruchs: " + log);
		assertEquals("ABBRUCH (Status %d): %s".formatted(aoe.getStatus().getStatusCode(), meldung.replaceFirst("^###\\s*", "")), log.getFirst(),
				"Die Kopfzeile muss den Abbruchgrund aus der Meldung der Exception tragen: " + log);
		return meldung;
	}


	// ##### Die Reportvorlage #####

	@Test
	void testEineFehlendeReportvorlageIstEinEingabefehler() {
		final ApiOperationException aoe = abbruchMitVorlage(null);

		assertEquals(Status.BAD_REQUEST, aoe.getStatus(), "Eine Anfrage, die der Aufrufer berichtigen kann, endet nicht mit einem Serverfehler.");
		final String grund = abbruchgrund(aoe);
		assertFalse(grund.contains("null"), "Eine fehlende Angabe wird nicht als Name 'null' ausgegeben: " + grund);
	}

	@Test
	void testEinLeererVorlagennameGiltAlsFehlendeAngabe() {
		// Für den Aufrufer bedeutet beides dasselbe: Seine Anfrage benennt keine Vorlage.
		assertEquals(abbruchgrund(abbruchMitVorlage(null)), abbruchgrund(abbruchMitVorlage("   ")));
	}

	@Test
	void testEinUnbekannterVorlagennameNenntDenAngefragtenNamen() {
		// Der Name ist die einzige Angabe, an der der Aufrufer seinen Fehler erkennt.
		assertTrue(abbruchgrund(abbruchMitVorlage("Gibt-Es-Nicht")).contains("'Gibt-Es-Nicht'"),
				"Die Meldung muss den angefragten Namen nennen: " + abbruchgrund(abbruchMitVorlage("Gibt-Es-Nicht")));
	}

	@Test
	void testEinVorlagennameMitSteuerzeichenErreichtDieKopfzeileMaskiert() {
		// Der Name stammt aus dem Request, und die Kopfzeile maskiert nicht selbst. Ein Umbruch erzeugte beim Aufrufer eine zweite Zeile, die wie ein
		// eigener Eintrag des Logs aussieht.
		final String grund = abbruchgrund(abbruchMitVorlage("Zeile1\nZeile2"));

		assertTrue(grund.contains("Zeile1\\nZeile2"), "Der Umbruch muss sichtbar ersetzt sein: " + grund);
		assertFalse(grund.contains("\n"), "Die Meldung darf keinen echten Umbruch enthalten: " + grund);
	}


	// ##### Die übrigen Eingangsprüfungen #####

	@Test
	void testFehlendeAngabenSindEinEingabefehler() {
		final DBEntityManager conn = mock(DBEntityManager.class);

		final ApiOperationException aoe = abbruchBeimAufbau(() -> new ReportingFactory(conn, null, ReportingAusgabeformat.PDF));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		abbruchgrund(aoe);
	}

	@Test
	void testEineFehlendeDatenbankverbindungIstEinServerfehler() {
		final ReportingParameter parameter = parameterMitVorlage("Gibt-Es-Nicht");

		final ApiOperationException aoe = abbruchBeimAufbau(() -> new ReportingFactory(null, parameter, ReportingAusgabeformat.PDF));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(), "Die Verbindung baut der Server selbst auf; ihr Fehlen ist kein Fehler des Aufrufers.");
		abbruchgrund(aoe);
	}

	@Test
	void testEinNichtPassendesAusgabeformatIstEinEingabefehler() {
		final ReportingParameter parameter = parameterMitVorlage("Gibt-Es-Nicht");
		final DBEntityManager conn = mock(DBEntityManager.class);

		final ApiOperationException aoe = abbruchBeimAufbau(() -> new ReportingFactory(conn, parameter, ReportingAusgabeformat.HTML));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		abbruchgrund(aoe);
	}

}
