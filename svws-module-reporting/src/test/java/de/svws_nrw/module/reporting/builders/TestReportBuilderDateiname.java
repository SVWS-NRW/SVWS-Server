package de.svws_nrw.module.reporting.builders;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLNonTransientConnectionException;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Dateinamensbildung aus einer Namensvorlage, mit Schwerpunkt auf einer Ausgabe <b>ohne Datensätze</b>.
 * <p>Seit eine Auswahl bewusst leer bleiben darf, ist der Leerfall ein regulärer Ausgabeweg und keine Ausnahme mehr. Die Vorlagen des Moduls behandeln ihn
 * heute selbst - jede kennt einen Zweig für null Datensätze. Darauf allein darf sich die Ausgabe aber nicht stützen: Eine neue oder geänderte Vorlage, die den
 * Zweig vergisst, greift auf {@code daten[0]} zu und lässt die Auswertung scheitern. Geprüft wird deshalb die Rückfallkette, die den Dateinamen in jedem Fall
 * liefert.</p>
 */
class TestReportBuilderDateiname {

	/** Der statische Dateiname, den die Reportvorlage vorgibt und auf den der Builder zurückfällt. */
	private static final String STATISCHER_NAME = "Schulbescheinigung";

	/**
	 * Ein minimaler Context, der eine Liste von Daten unter einem Variablennamen bereitstellt. Er ersetzt die produktiven Contexts, die für ihren Aufbau
	 * Repositories und damit eine Datenbankverbindung benötigen.
	 *
	 * @param <T> Der Typ der Daten, die der Context bereitstellt.
	 */
	private static final class TestHtmlContext<T> extends HtmlContext<T> {

		/**
		 * Erzeugt den Context mit den übergebenen Daten.
		 *
		 * @param variablenname Der Name der Thymeleaf-Variablen.
		 * @param daten         Die Daten, die unter diesem Namen bereitstehen.
		 */
		private TestHtmlContext(final String variablenname, final List<T> daten) {
			super(null);
			erzeugeContext(variablenname, daten);
		}
	}

	/**
	 * Ein Datensatz, dessen Werte erst beim Zugriff aus der Datenbank nachgeladen werden - hier scheitert das Nachladen an einer abgerissenen Verbindung. Er
	 * tritt an die Stelle eines Reporting-Objekts, dessen Getter die Vorlage während ihrer Auswertung anspricht.
	 */
	private static final class DatensatzMitVerbindungsabbruch extends HashMap<String, String> {

		/** Die Version für die Serialisierung; sie erbt die Schnittstelle von der Oberklasse. */
		private static final long serialVersionUID = 1L;

		@Override
		public String get(final Object schluessel) {
			throw new IllegalStateException("Der Zugriff ist gescheitert.",
					new SQLNonTransientConnectionException("Verbindung zur Datenbank verloren."));
		}
	}

	/**
	 * Gibt die Contexts mit der Variablen "Daten" und den übergebenen Einträgen zurück.
	 *
	 * @param daten Die Einträge der Variablen.
	 *
	 * @return Die Contexts für die Auswertung der Namensvorlage.
	 */
	private static List<HtmlContext<?>> contexts(final List<String> daten) {
		return List.of(new TestHtmlContext<>("Daten", daten));
	}

	/**
	 * Gibt die Contexts mit einem einzelnen Datensatz zurück, dessen Zugriff an einer abgerissenen Verbindung scheitert.
	 *
	 * @return Die Contexts für die Auswertung der Namensvorlage.
	 */
	private static List<HtmlContext<?>> contextsMitVerbindungsabbruch() {
		return List.of(new TestHtmlContext<>("Daten", List.of(new DatensatzMitVerbindungsabbruch())));
	}

	/**
	 * Gibt an, ob die Ursachenkette des übergebenen Fehlers den injizierten Verbindungsabbruch führt.
	 *
	 * @param fehler Der geworfene Fehler.
	 *
	 * @return true, wenn die Kette den Verbindungsabbruch enthält, sonst false.
	 */
	private static boolean enthaeltVerbindungsabbruch(final Throwable fehler) {
		for (Throwable glied = fehler; glied != null; glied = glied.getCause()) {
			if (glied instanceof SQLNonTransientConnectionException) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Erzeugt einen HTML-Builder mit der übergebenen Namensvorlage und den übergebenen Daten. Der statische Dateiname ist dabei stets derselbe: Er ist der
	 * Rückfallwert, den die Vorlage bei Erfolg ersetzt.
	 *
	 * @param dateinamensvorlage Die Namensvorlage des Reports.
	 * @param daten              Die Daten, die der Vorlage unter "Daten" bereitstehen.
	 *
	 * @return Der Builder, dessen Dateiname geprüft wird.
	 *
	 * @throws ApiOperationException Wenn der Builder die übergebenen Daten ablehnt.
	 */
	private static ReportBuilderHtml htmlBuilder(final String dateinamensvorlage, final List<String> daten) throws ApiOperationException {
		return new ReportBuilderHtml(new ReportBuilderContextHtml()
				.withHtmlTemplate("<html><body>Inhalt</body></html>")
				.addHtmlContexts(contexts(daten))
				.withDateinamensvorlage(dateinamensvorlage)
				.withStatischerDateiname(STATISCHER_NAME)
				.withRootPfad("de/svws_nrw/module/reporting/")
				.withLogger(new Logger()));
	}


	@Test
	void testDerBuilderFaelltBeiUngeeigneterVorlageAufDenStatischenNamenZurueck() {
		// Der eigentliche Nachweis der Rückfallkette: Die Vorlage greift auf das erste Element einer leeren Liste zu und liefert damit keinen Namen. Der
		// Builder muss dann den statischen Namen führen - ohne ihn hätte das Dokument keinen Dateinamen, und die Ausgabe scheiterte an ihrem Namen statt an
		// ihren Daten.
		final ReportBuilderHtml builder = assertDoesNotThrow(() -> htmlBuilder("Bescheinigung_[(${Daten[0]})]", List.of()));

		assertEquals(STATISCHER_NAME, builder.getDateiname());
		assertEquals(STATISCHER_NAME + ".html", builder.getDateinameMitEndung());
	}

	@Test
	void testDerBuilderUebernimmtDenNamenAusDerVorlageWennSieEinenLiefert() {
		// Gegenprobe: Der statische Name ist der Rückfall und nicht der Regelfall. Ohne sie bliebe der Test auch grün, wenn die Vorlage nie ausgewertet würde.
		final ReportBuilderHtml builder = assertDoesNotThrow(() -> htmlBuilder("Bescheinigung_[(${Daten[0]})]", List.of("Meier")));

		assertEquals("Bescheinigung_Meier", builder.getDateiname());
	}

	@Test
	void testDerBuilderFaelltOhneVorlageAufDenStatischenNamenZurueck() {
		final ReportBuilderHtml builder = assertDoesNotThrow(() -> htmlBuilder("", List.of("Meier")));

		assertEquals(STATISCHER_NAME, builder.getDateiname());
	}


	@Test
	void testEineVorlageMitEigenemLeerzweigLiefertDessenNamen() {
		// So sind die Namensvorlagen des Moduls gebaut: Sie unterscheiden null, einen und mehrere Datensätze.
		final String vorlage = """
				[# th:with="anzahl = ${#lists.size(Daten)}"]
					[# th:if="${anzahl == 0}"]Sammelausgabe[/]
					[# th:if="${anzahl == 1}"]Einzelfall_[(${Daten[0]})][/]
				[/]""";

		assertEquals("Sammelausgabe", ReportBuilderUtils.generiereDateinameAusVorlage(vorlage, contexts(List.of())));
		assertEquals("Einzelfall_Meier", ReportBuilderUtils.generiereDateinameAusVorlage(vorlage, contexts(List.of("Meier"))));
	}

	@Test
	void testEineVorlageOhneLeerzweigLiefertBeiLeerenDatenKeinenNamen() {
		// Der Zugriff auf das erste Element scheitert an der leeren Liste. Der leere Rückgabewert ist die Bedingung dafür, dass der Builder auf den statischen
		// Namen zurückfällt - eine Exception nach außen ließe die gesamte Ausgabe an ihrem Dateinamen scheitern.
		final String vorlage = "Bescheinigung_[(${Daten[0]})]";

		assertEquals("", ReportBuilderUtils.generiereDateinameAusVorlage(vorlage, contexts(List.of())));
	}

	@Test
	void testEineVorlageOhneVariableImContextLiefertKeinenNamen() {
		// Denselben Weg nimmt eine Vorlage, die eine Variable erwartet, die der Datenaufbau nicht bereitstellt.
		assertEquals("", ReportBuilderUtils.generiereDateinameAusVorlage("Bescheinigung_[(${Unbekannt.name()})]", contexts(List.of("Meier"))));
	}

	@Test
	void testEineInfrastrukturstoerungBeendetDieNamensbildungStattZurueckzufallen() {
		// Der Rückfall auf den statischen Namen gilt Vorlagen, die keinen Namen hergeben. Eine abgerissene Verbindung ist kein solcher Fall: Sie beendet die
		// Ausgabe. Ohne diese Unterscheidung liefe die Störung hier ins Leere und meldete sich erst beim Rendern mit einer unpräziseren Ursache.
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> ReportBuilderUtils.generiereDateinameAusVorlage("Bescheinigung_[(${Daten[0].nachname})]", contextsMitVerbindungsabbruch()));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		// Die direkte Ursache ist der Fehler, in den Thymeleaf die Auswertung verpackt; der Verbindungsabbruch liegt tiefer. Ohne die erhaltene Kette stünde
		// im Log ein Abbruch ohne den Grund, an dem er liegt - und die Klassifikation griffe an dieser Stelle ins Leere.
		assertTrue(enthaeltVerbindungsabbruch(aoe), "Die ursprüngliche Ursachenkette gehört in den Abbruch: " + aoe.getCause());
	}

	@Test
	void testEinNameAusLeerenPlatzhalternGiltNichtAlsName() {
		// Die Vorlage wertet fehlerfrei aus, liefert aber nur Leerraum. Ein Dateiname aus Leerzeichen ist keiner; auch hier greift der statische Name.
		assertEquals("", ReportBuilderUtils.generiereDateinameAusVorlage("[# th:if=\"${#lists.size(Daten) > 0}\"]Bescheinigung[/]", contexts(List.of())));
	}

}
