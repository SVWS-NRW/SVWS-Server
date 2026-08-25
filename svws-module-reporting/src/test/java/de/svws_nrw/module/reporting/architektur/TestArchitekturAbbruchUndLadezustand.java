package de.svws_nrw.module.reporting.architektur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import de.svws_nrw.module.reporting.architektur.Reportingquelltexte.Catchblock;
import de.svws_nrw.module.reporting.architektur.Reportingquelltexte.Quelldatei;

/**
 * Zwei Regeln des Moduls, die kein einzelner Aufruf sichtbar macht: Ein Catch-Block, der einen Fehler mit {@code ERROR} protokolliert, verlässt den Block
 * danach nur noch werfend, und eine Repository-Map mit Fehler-Marker braucht einen dauerhaften Rückkanal für den Fehler.
 * <p>Die Prüfung findet beim Bauen statt und nicht im Betrieb: Eine Laufzeitprüfung des Logs kann einen Verstoß erst melden, wenn ein Anwender ihn auslöst,
 * und sie unterscheidet nicht zwischen einem gewollten Rückfallwert und einem vergessenen Wurf.</p>
 * <p>Die erste Regel greift nur, <b>wenn</b> ein Catch-Block {@code LogLevel.ERROR} enthält. Sie sagt nichts darüber, woher die Meldung eines Abbruchs
 * stammt, und steht dem Fehlervertrag - eine Meldungsquelle, protokolliert wird an der Abschlussgrenze - deshalb nicht entgegen. Ein Catch-Block mit einer
 * technischen {@code ERROR}-Zeile endet mit einem Wurf.</p>
 * <p><b>Grenzen:</b> Die erste Regel sieht nur Catch-Blöcke. Ein {@code ERROR}-Eintrag, der außerhalb davon entsteht und mit {@code return} statt einem Wurf
 * endet, bliebe unentdeckt und stünde im Log eines erfolgreichen Reports. Beide Regeln erkennen zudem nicht, wenn ein breiter Catch den Abbruch der
 * Meldefassade verschluckt, der als {@code ApiOperationException} reist.</p>
 */
class TestArchitekturAbbruchUndLadezustand {

	/**
	 * Der Beginn eines Wurf-Statements - auch der Wurf einer Ausnahme, die eine Hilfsmethode gebaut hat. Geprüft wird das letzte Statement des Rumpfs auf
	 * oberster Ebene: Ein {@code throw} irgendwo im Rumpf genügt nicht, weil ein bedingter Wurf einen Pfad offen ließe, der nach dem {@code ERROR} erfolgreich
	 * zurückkehrt. Die Wortgrenze hält Bezeichner wie {@code throwable} fern.
	 */
	private static final Pattern BEGINNT_MIT_WURF = Pattern.compile("^throw\\b");

	/** Ein einfacher Java-Bezeichner. Nur ein solcher kann ein Feld der Datei benennen; {@code new}-Ausdrücke und Aufrufe fallen durch. */
	private static final Pattern BEZEICHNER = Pattern.compile("[A-Za-z_$][A-Za-z0-9_$]*");

	/**
	 * Ein Ausstieg, der den Block ohne Wurf verlässt. Nach einem protokollierten {@code ERROR} ist er unzulässig: Der Pfad kehrte trotz des Eintrags
	 * erfolgreich zurück. Ein tolerierter Ausstieg gehört deshalb vor die Protokollierung - erst entscheiden, dann abbrechen. {@code yield} verlässt einen
	 * Switch-Ausdruck mit einem Wert; als kontextuelles Schlüsselwort zählt damit auch ein gleichnamiger Aufruf wie {@code Thread.yield()} - gewollte
	 * Strenge, die Umformung ist stets möglich.
	 */
	private static final Pattern AUSSTIEG = Pattern.compile("\\b(return|break|continue|yield)\\b");

	/** Der Protokollaufruf, um den sich die erste Regel dreht. */
	private static final String FEHLERPROTOKOLL = "LogLevel.ERROR";

	/**
	 * Die Mindestzahl an Catch-Blöcken mit einem Fehlerprotokoll. Zwei technische Zeilen tragen einen Zusammenhang, den die Fehlerquelle nicht kennt, und
	 * bleiben deshalb dauerhaft: der Dateiname des betroffenen Dokuments in der Sammelausgabe und die Zuordnung eines Anhangs zum Empfänger.
	 */
	private static final int SCHWELLE_FEHLERPROTOKOLL = 2;

	/** Der Aufruf, mit dem eine Meldung über die Fassade entsteht. Er ersetzt den Wurf, wo ein Befund hingenommen wird. */
	private static final String MELDUNG_UEBER_FASSADE = "meldeAusgabeproblem";

	/** Die Meldestelle der Repositories; sie meldet über die Fassade und bricht bei einer Infrastrukturstörung dort ab. */
	private static final String MELDUNG_TEILDATEN = "meldeTeildatenLadefehler";

	/**
	 * Die Aufrufe der gemeinsamen Lademethoden, die einer nicht ladbaren ID einen Ersatzwert in die Repository-Map schreiben: den Fehler-Marker
	 * {@code put(id, null)} bei Einzelwerten, die leere Liste bei Listen. Beide Male ist der Fehler ohne Rückkanal verloren.
	 */
	private static final Pattern LADEAUFRUF_MIT_FEHLERMARKER = Pattern.compile("ladeFehlende(Werte|Listen)InRepositoryMap\\s*\\(");

	/**
	 * Die Datei, die die Lademethode definiert. Ihre Signaturen sind keine Aufrufe; die Generics ihrer Parameter machten die Zählung der Argumente zudem
	 * unbrauchbar.
	 */
	private static final String DEFINITION_DER_LADEMETHODE = "ReportingRepositoryUtils.java";

	/**
	 * Die Dateien, die die Lademethode ohne Fehler-Rückkanal aufrufen dürfen. Zulässig ist das nur, wenn der Ladepfad den Fehler nicht verschluckt - etwa
	 * weil er ihn wirft oder an seiner Stelle meldet. Die Liste ist bewusst leer: Zurzeit führt jeder Aufruf den Rückkanal.
	 */
	private static final Set<String> ERLAUBT_OHNE_FEHLERRUECKKANAL = Set.of();


	/**
	 * Gibt das sechste Argument des Aufrufs zurück, der an der übergebenen Position beginnt, oder {@code null}, falls der Aufruf weniger Argumente hat.
	 * Ein Komma innerhalb spitzer Klammern verschiebt die Zählung, weil nur runde, geschweifte und eckige Klammern die Tiefe bilden; ein so zerteiltes
	 * Argument ist aber kein einfacher Bezeichner mehr und fällt bei der Feldprüfung durch.
	 *
	 * @param inhalt         Der bereinigte Quelltext.
	 * @param klammerBeginn  Die Position der öffnenden Klammer des Aufrufs.
	 *
	 * @return Das sechste Argument auf oberster Ebene oder {@code null}.
	 */
	private static String sechstesArgument(final String inhalt, final int klammerBeginn) {
		int tiefe = 0;
		int argument = 1;
		int beginn = klammerBeginn + 1;
		for (int i = klammerBeginn; i < inhalt.length(); i++) {
			final char zeichen = inhalt.charAt(i);
			if ((zeichen == '(') || (zeichen == '{') || (zeichen == '[')) {
				tiefe++;
			} else if ((zeichen == ')') || (zeichen == '}') || (zeichen == ']')) {
				tiefe--;
				if (tiefe == 0) {
					return (argument == 6) ? inhalt.substring(beginn, i) : null;
				}
			} else if ((zeichen == ',') && (tiefe == 1)) {
				if (argument == 6) {
					return inhalt.substring(beginn, i);
				}
				argument++;
				beginn = i + 1;
			}
		}
		return null;
	}

	/**
	 * Gibt an, ob der Rumpf auf oberster Ebene mit einem Wurf-Statement endet. Ein Rumpf, der mit einer Verzweigung endet, gilt auch dann nicht als werfend,
	 * wenn alle ihre Zweige werfen: Die Umformung auf einen unbedingten Wurf am Ende ist stets möglich und hält die Regel einfach.
	 *
	 * @param rumpf Der Rumpf des Catch-Blocks.
	 *
	 * @return true, wenn das letzte Statement der obersten Ebene ein Wurf ist, sonst false.
	 */
	private static boolean endetMitWurf(final String rumpf) {
		int tiefe = 0;
		int beginn = 0;
		String letztesStatement = "";
		for (int i = 0; i < rumpf.length(); i++) {
			final char zeichen = rumpf.charAt(i);
			if ((zeichen == '(') || (zeichen == '{') || (zeichen == '[')) {
				tiefe++;
			} else if ((zeichen == ')') || (zeichen == '}') || (zeichen == ']')) {
				tiefe--;
				if ((tiefe == 0) && (zeichen == '}')) {
					// Ein Blockende schließt ein Statement ab, ohne dass ein Semikolon folgt - etwa eine Verzweigung.
					beginn = i + 1;
				}
			} else if ((zeichen == ';') && (tiefe == 0)) {
				final String statement = rumpf.substring(beginn, i).strip();
				if (!statement.isEmpty()) {
					letztesStatement = statement;
				}
				beginn = i + 1;
			}
		}
		return BEGINNT_MIT_WURF.matcher(letztesStatement).find();
	}

	/**
	 * Gibt an, ob im Rumpf nach dem ersten protokollierten {@code ERROR} noch ein Ausstieg ohne Wurf folgt. Geprüft wird die textuelle Reihenfolge: Ein
	 * Ausstieg zählt auch dann, wenn er auf einem anderen Pfad liegt oder in einem Lambda oder Switch steht - die Umformung, den tolerierten Ausstieg vor die
	 * Protokollierung zu ziehen, ist stets möglich.
	 *
	 * @param rumpf Der Rumpf des Catch-Blocks.
	 *
	 * @return true, wenn nach dem ersten {@code ERROR} ein {@code return}, {@code break}, {@code continue} oder {@code yield} folgt, sonst false.
	 */
	private static boolean ausstiegNachFehlerprotokoll(final String rumpf) {
		final int fehlerprotokoll = rumpf.indexOf(FEHLERPROTOKOLL);
		return (fehlerprotokoll >= 0) && AUSSTIEG.matcher(rumpf).find(fehlerprotokoll);
	}

	/**
	 * Gibt an, ob das Argument einen dauerhaften Rückkanal benennt: einen Bezeichner - wahlweise mit {@code this.} -, den dieselbe Datei als
	 * {@code private final Map<Long, Exception>}-Feld deklariert. {@code null}, ein {@code new}-Ausdruck und eine methodenlokale Map fallen durch: Sie
	 * überleben den Aufruf nicht beziehungsweise nicht die Lebensdauer der Repository-Map.
	 *
	 * @param datei    Die Quelldatei des Aufrufs.
	 * @param argument Das sechste Argument des Aufrufs.
	 *
	 * @return true, wenn das Argument ein Fehler-Rückkanal-Feld der Datei benennt, sonst false.
	 */
	private static boolean istRueckkanalFeld(final Quelldatei datei, final String argument) {
		final String name = argument.strip().replaceFirst("^this\\s*\\.\\s*", "");
		if (!BEZEICHNER.matcher(name).matches()) {
			return false;
		}
		final Pattern deklaration = Pattern.compile("private\\s+final\\s+Map<Long,\\s*Exception>\\s+" + Pattern.quote(name) + "\\b");
		return deklaration.matcher(datei.inhalt()).find();
	}


	@Test
	void testKeinCatchBlockProtokolliertEinenFehlerOhneZuWerfen() {
		// ERROR ist dem Abbruch vorbehalten. Ein Catch-Block, der ihn schreibt und weiterläuft, hinterlässt im Log einen Fehler ohne Folge - und im Log eines
		// erfolgreichen Reports hat er nichts zu suchen. Zwei Prüfungen zusammen sichern das: Der Rumpf endet mit einem unbedingten Wurf, und nach dem ERROR
		// folgt kein Ausstieg mehr - sonst genügte ein bedingter return zwischen Protokollierung und Wurf, und der Pfad kehrte erfolgreich zurück.
		final List<String> verstoesse = new ArrayList<>();
		for (final Quelldatei datei : Reportingquelltexte.alle()) {
			for (final Catchblock block : Reportingquelltexte.catchbloecke(datei)) {
				if (block.rumpf().contains(FEHLERPROTOKOLL) && (!endetMitWurf(block.rumpf()) || ausstiegNachFehlerprotokoll(block.rumpf()))) {
					verstoesse.add(datei.fundstelle(block.position()));
				}
			}
		}

		assertEquals(List.of(), verstoesse,
				"Diese Catch-Blöcke protokollieren ERROR und verlassen den Block danach nicht ausschließlich werfend. Wer den Fehler hinnehmen will, meldet "
						+ "ihn über %s oder %s mit WARNING und steigt vor der Protokollierung aus; ".formatted(MELDUNG_UEBER_FASSADE, MELDUNG_TEILDATEN)
						+ "wer ihn nicht hinnehmen kann, endet mit einem unbedingten Wurf: " + verstoesse);
	}

	@Test
	void testJedeRepositoryMapMitFehlerMarkerFuehrtEinenFehlerRueckkanal() {
		// Die Lademethode hinterlässt für eine nicht ladbare ID den Marker put(id, null) und verhindert damit jeden weiteren Ladeversuch. Ohne den Rückkanal
		// wüsste die Stelle, die daraus einen Befund meldet, nur noch, dass das Laden scheiterte - nicht mehr woran.
		final List<String> verstoesse = new ArrayList<>();
		for (final Quelldatei datei : Reportingquelltexte.alle()) {
			final String dateiname = datei.pfad().getFileName().toString();
			if (DEFINITION_DER_LADEMETHODE.equals(dateiname) || ERLAUBT_OHNE_FEHLERRUECKKANAL.contains(dateiname)) {
				continue;
			}
			final Matcher treffer = LADEAUFRUF_MIT_FEHLERMARKER.matcher(datei.inhalt());
			while (treffer.find()) {
				// Der Rückkanal ist das sechste Argument und muss ein Feld der Datei sein: null, ein new-Ausdruck oder eine methodenlokale Map bestünden die
				// bloße Zählung, hielten den Fehler aber nicht über den Aufruf hinaus fest.
				final String rueckkanal = sechstesArgument(datei.inhalt(), treffer.end() - 1);
				if ((rueckkanal == null) || !istRueckkanalFeld(datei, rueckkanal)) {
					verstoesse.add(datei.fundstelle(treffer.start()));
				}
			}
		}

		assertEquals(List.of(), verstoesse,
				"Diese Ladeaufrufe hinterlassen einen Fehler-Marker in der Repository-Map, ohne den Fehler in einem dauerhaften Rückkanal festzuhalten "
						+ "(das sechste Argument fehlt oder ist kein private-final-Feld der Datei): " + verstoesse);
	}

	@Test
	void testDieRegelnGreifenAufDenGeprueftenQuellen() {
		// Gegenprobe zu beiden Regeln: Ein Suchmuster, das nichts findet, wäre stets grün. Die Quellen müssen also überhaupt Catch-Blöcke und Ladeaufrufe
		// enthalten - und die Definition der Lademethode selbst zählt als Ladeaufruf mit.
		final List<Quelldatei> quellen = Reportingquelltexte.alle();
		final long catchbloecke = quellen.stream().mapToLong(datei -> Reportingquelltexte.catchbloecke(datei).size()).sum();
		final long ladeaufrufe = quellen.stream().mapToLong(datei -> LADEAUFRUF_MIT_FEHLERMARKER.matcher(datei.inhalt()).results().count()).sum();
		// Auch die Enderkennung braucht eine Gegenprobe: Erkennte sie jedes Rumpfende als Wurf, wäre die erste Regel still grün. Die stillen
		// Rückfall-Catches des Moduls enden nicht mit einem Wurf und müssen zahlreich gefunden werden.
		final long catchbloeckeOhneWurfAmEnde = quellen.stream()
				.flatMap(datei -> Reportingquelltexte.catchbloecke(datei).stream())
				.filter(block -> !endetMitWurf(block.rumpf()))
				.count();
		// Dieselbe Gegenprobe für die Ausstiegserkennung: Die stillen Rückfall-Catches steigen mit return aus und müssen zahlreich gefunden werden - fände
		// das Ausstiegsmuster nichts, wäre die Reihenfolge-Prüfung still grün.
		final long catchbloeckeMitAusstieg = quellen.stream()
				.flatMap(datei -> Reportingquelltexte.catchbloecke(datei).stream())
				.filter(block -> AUSSTIEG.matcher(block.rumpf()).find())
				.count();
		// Auch die Voraussetzung der ersten Regel braucht eine Gegenprobe: Sie betrachtet einen Catch-Block nur, wenn er ERROR protokolliert. Findet das
		// Suchwort keinen einzigen solchen Block, prüft die Regel nichts mehr.
		final long catchbloeckeMitFehlerprotokoll = quellen.stream()
				.flatMap(datei -> Reportingquelltexte.catchbloecke(datei).stream())
				.filter(block -> block.rumpf().contains(FEHLERPROTOKOLL))
				.count();

		assertFalse(quellen.isEmpty(), "Ohne eingelesene Quellen prüfen die Architekturtests nichts.");
		assertFalse(catchbloecke < 10, "Es wurden zu wenige Catch-Blöcke gefunden; das Suchmuster greift nicht: " + catchbloecke);
		assertFalse(ladeaufrufe < 3, "Es wurden zu wenige Ladeaufrufe gefunden; das Suchmuster greift nicht: " + ladeaufrufe);
		assertFalse(catchbloeckeOhneWurfAmEnde < 10,
				"Es wurden zu wenige Catch-Blöcke ohne Wurf am Ende gefunden; die Enderkennung greift nicht: " + catchbloeckeOhneWurfAmEnde);
		assertFalse(catchbloeckeMitAusstieg < 10,
				"Es wurden zu wenige Catch-Blöcke mit einem Ausstieg gefunden; das Ausstiegsmuster greift nicht: " + catchbloeckeMitAusstieg);
		assertFalse(catchbloeckeMitFehlerprotokoll < SCHWELLE_FEHLERPROTOKOLL,
				"Es wurden zu wenige Catch-Blöcke mit einem Fehlerprotokoll gefunden; die erste Regel betrachtet dann keinen Block mehr: "
						+ catchbloeckeMitFehlerprotokoll);
	}

}
