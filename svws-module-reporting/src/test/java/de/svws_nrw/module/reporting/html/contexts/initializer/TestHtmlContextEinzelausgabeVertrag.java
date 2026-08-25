package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.reporting.ReportingReportvorlageDatenContext;
import de.svws_nrw.module.reporting.html.contexts.HtmlContextAufteilbar;

/**
 * Prüft die Zusage der Registry zur Einzelausgabe: Wer sie meldet, muss einen Haupt-Context eintragen, der sich aufteilen lässt.
 * <p>Die {@code HtmlFactory} verlässt sich darauf. Sie holt den Context unter dem Schlüssel des Datenaufbaus und bricht mit einem Serverfehler ab, wenn er
 * {@link HtmlContextAufteilbar} nicht erfüllt - ein Programmfehler, den kein Aufrufer herbeiführen kann.</p>
 * <p>Weder der Compiler noch ein anderer Test hält diese Zusage: Die Erzeuger sind auf Oberklassen gebunden, die die Schnittstelle nicht fordern, und die
 * Zuordnung von Datenaufbau zu Context-Klasse trägt allein die Tabelle der Registry. Ein Eintrag mit der falschen Klasse fiele erst im Betrieb auf.</p>
 * <p>Geprüft wird die eingetragene Klasse und nicht ein erzeugtes Objekt: Die Erzeuger der GOSt-Sichtweisen laden beim Aufruf Daten nach und sind ohne
 * Datenbank nicht aufrufbar. Die Klasse steht dagegen im Quelltext der Registry und lässt sich dort ablesen.</p>
 */
class TestHtmlContextEinzelausgabeVertrag {

	/** Die Registry, deren Tabelle die Zuordnung von Datenaufbau zu Context-Klasse trägt. */
	private static final Path REGISTRY =
			Path.of("src/main/java/de/svws_nrw/module/reporting/html/contexts/initializer/HtmlContextInitializerRegistry.java");

	/** Das Paket, in dem die Daten-Contexts liegen. */
	private static final String PAKET_CONTEXTS = "de.svws_nrw.module.reporting.html.contexts.";

	/** Die Konstruktor-Referenz, mit der ein Eintrag der Registry seinen Haupt-Context benennt. */
	private static final Pattern CONTEXT_ERZEUGER = Pattern.compile("(HtmlContext\\w+)::new");

	/** Die Mindestzahl an Einträgen mit Einzelausgabe. Sie belegt, dass die Suche im Quelltext überhaupt greift. */
	private static final int MINDESTZAHL_EINTRAEGE = 10;


	/**
	 * Gibt den Quelltext der Registry zurück.
	 *
	 * @return Der Quelltext.
	 */
	private static String registryQuelltext() {
		try {
			return Files.readString(REGISTRY, StandardCharsets.UTF_8);
		} catch (final IOException e) {
			throw new UncheckedIOException("Der Quelltext der Registry ist nicht lesbar: " + REGISTRY.toAbsolutePath(), e);
		}
	}

	/**
	 * Gibt die Klasse des Haupt-Contexts zurück, die die Registry für den übergebenen Datenaufbau einträgt.
	 * <p>Gesucht wird die Konstruktor-Referenz innerhalb des Tabelleneintrags. Der Eintrag reicht von der Nennung des Datenaufbaus bis zu seiner
	 * schließenden Klammer; die Auswahl-Lambdas darin tragen keine solche Referenz.</p>
	 *
	 * @param quelltext    Der Quelltext der Registry.
	 * @param datenContext Der Datenaufbau.
	 *
	 * @return Der einfache Name der Context-Klasse oder {@code null}, wenn der Eintrag keine Konstruktor-Referenz führt.
	 */
	private static String eingetrageneContextKlasse(final String quelltext, final ReportingReportvorlageDatenContext datenContext) {
		final int beginn = quelltext.indexOf("ReportingReportvorlageDatenContext." + datenContext.name() + ",");
		if (beginn < 0) {
			return null;
		}
		int tiefe = 0;
		for (int i = beginn; i < quelltext.length(); i++) {
			final char zeichen = quelltext.charAt(i);
			if (zeichen == '(') {
				tiefe++;
			} else if (zeichen == ')') {
				if (tiefe == 0) {
					final Matcher treffer = CONTEXT_ERZEUGER.matcher(quelltext.substring(beginn, i));
					return treffer.find() ? treffer.group(1) : null;
				}
				tiefe--;
			}
		}
		return null;
	}

	/**
	 * Gibt an, ob die Klasse mit dem übergebenen Namen aufteilbar ist.
	 *
	 * @param klassenname Der einfache Name der Context-Klasse.
	 *
	 * @return true, wenn die Klasse {@link HtmlContextAufteilbar} erfüllt, sonst false.
	 */
	private static boolean istAufteilbar(final String klassenname) {
		try {
			return HtmlContextAufteilbar.class.isAssignableFrom(Class.forName(PAKET_CONTEXTS + klassenname));
		} catch (final ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * Gibt die Datenaufbauten zurück, die die Einzelausgabe melden.
	 *
	 * @return Die Datenaufbauten mit Einzelausgabe.
	 */
	private static List<ReportingReportvorlageDatenContext> mitEinzelausgabe() {
		final List<ReportingReportvorlageDatenContext> aufbauten = new ArrayList<>();
		for (final ReportingReportvorlageDatenContext datenContext : ReportingReportvorlageDatenContext.values()) {
			final HtmlContextAufbau aufbau = HtmlContextInitializerRegistry.aufbauOderNull(datenContext);
			if ((aufbau != null) && aufbau.unterstuetztEinzelausgabe()) {
				aufbauten.add(datenContext);
			}
		}
		return aufbauten;
	}


	@Test
	void testJederDatenaufbauMitEinzelausgabeTraegtEinenAufteilbarenContext() {
		final String quelltext = registryQuelltext();
		final List<String> verstoesse = new ArrayList<>();

		for (final ReportingReportvorlageDatenContext datenContext : mitEinzelausgabe()) {
			final String klassenname = eingetrageneContextKlasse(quelltext, datenContext);
			if ((klassenname == null) || !istAufteilbar(klassenname)) {
				verstoesse.add("%s (%s) -> %s".formatted(datenContext.name(),
						HtmlContextInitializerRegistry.aufbauOderNull(datenContext).contextSchluessel(),
						(klassenname == null) ? "keine Context-Klasse gefunden" : klassenname));
			}
		}

		assertEquals(List.of(), verstoesse,
				"Diese Datenaufbauten melden die Einzelausgabe, tragen aber einen Haupt-Context ohne HtmlContextAufteilbar ein. Die HtmlFactory bricht "
						+ "dafür mit einem Serverfehler ab: " + verstoesse);
	}

	@Test
	void testDieRegelGreiftAufDerGeprueftenRegistry() {
		// Gegenprobe zur Suche: Findet sie keine Context-Klasse, prüft die Regel still nichts. Die Sichtweisen mit Einzelausgabe müssen zahlreich
		// gefunden werden.
		final String quelltext = registryQuelltext();
		final List<String> gefunden = new ArrayList<>();
		for (final ReportingReportvorlageDatenContext datenContext : mitEinzelausgabe()) {
			if (eingetrageneContextKlasse(quelltext, datenContext) != null) {
				gefunden.add(datenContext.name());
			}
		}

		assertFalse(gefunden.size() < MINDESTZAHL_EINTRAEGE, "Es wurden zu wenige Einträge mit Einzelausgabe gefunden; die Suche greift nicht: " + gefunden);
	}

	@Test
	void testDieTyppruefungErkenntEinenNichtAufteilbarenContext() {
		// Gegenprobe zur Typprüfung: Erkennte sie jede Klasse als aufteilbar, wäre die Regel still grün. Die Oberklasse der Klausurplan-Sichtweisen trägt
		// die Schnittstelle nicht, nur die Sichtweisen selbst tun es.
		assertFalse(istAufteilbar("HtmlContextGostKlausurplanungKlausurplan"), "Die Oberklasse darf nicht als aufteilbar gelten.");
		assertFalse(istAufteilbar("HtmlContextGibtEsNicht"), "Eine unbekannte Klasse darf nicht als aufteilbar gelten.");
	}

}
