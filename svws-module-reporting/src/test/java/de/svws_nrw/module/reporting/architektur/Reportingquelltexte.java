package de.svws_nrw.module.reporting.architektur;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Zugriff auf die Quelltexte des Moduls für die Architekturtests. Sie prüfen Regeln, die kein Aufruf sichtbar macht, und lesen den Code dafür als Text.
 * <p>Kommentare und Zeichenketten werden vor der Prüfung ausgeblendet: Ohne das meldete eine JavaDoc-Zeile, die eine Regel beschreibt, denselben Treffer wie
 * echter Code. Die Zeilenstruktur bleibt erhalten, damit Fundstellen mit ihrer Zeilennummer benannt werden können.</p>
 */
final class Reportingquelltexte {

	/** Das Verzeichnis der Produktivquellen des Moduls, ausgehend vom Arbeitsverzeichnis der Tests. */
	private static final Path QUELLVERZEICHNIS = Path.of("src", "main", "java", "de", "svws_nrw", "module", "reporting");

	/** Das Verzeichnis der unversionierten Arbeitsdokumente; es enthält keinen Quelltext. */
	private static final String VERZEICHNIS_DOKU = "doku";

	/**
	 * Ein Catch-Block samt beginnendem Rumpf. Das Wort allein genügt nicht als Muster: Ein Bezeichner, der mit "catch" beginnt, ergäbe sonst einen Block, der
	 * an einer beliebigen späteren Klammer beginnt. Der Parameterteil lässt eine Klammerebene zu, weil ein Catch eine Annotation mit Argument tragen darf
	 * ({@code catch (@SuppressWarnings("unused") final Exception ignore)}) - gerade diese still ignorierenden Blöcke dürfen dem Test nicht entgehen.
	 */
	private static final Pattern CATCH_MIT_RUMPF = Pattern.compile("\\bcatch\\s*\\((?:[^()]|\\([^()]*\\))*\\)\\s*\\{");

	private Reportingquelltexte() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}


	/**
	 * Eine Java-Quelldatei des Moduls mit ihrem um Kommentare und Zeichenketten bereinigten Inhalt.
	 *
	 * @param pfad    Der Pfad der Datei, für Fundstellen in den Fehlermeldungen.
	 * @param inhalt  Der bereinigte Inhalt.
	 */
	record Quelldatei(Path pfad, String inhalt) {

		/**
		 * Gibt die Zeilennummer zur übergebenen Position im Inhalt zurück.
		 *
		 * @param position Die Position im bereinigten Inhalt.
		 *
		 * @return Die Zeilennummer, beginnend bei 1.
		 */
		int zeile(final int position) {
			return (int) inhalt.substring(0, position).chars().filter(zeichen -> zeichen == '\n').count() + 1;
		}

		/**
		 * Gibt die Fundstelle in der Form "Datei:Zeile" zurück.
		 *
		 * @param position Die Position im bereinigten Inhalt.
		 *
		 * @return Die Fundstelle für eine Fehlermeldung.
		 */
		String fundstelle(final int position) {
			return "%s:%d".formatted(pfad.getFileName(), zeile(position));
		}
	}


	/**
	 * Liest alle Produktivquellen des Moduls ein.
	 *
	 * @return Die Quelldateien mit bereinigtem Inhalt.
	 */
	static List<Quelldatei> alle() {
		if (!Files.isDirectory(QUELLVERZEICHNIS)) {
			throw new IllegalStateException("Die Quellen des Moduls wurden nicht gefunden: " + QUELLVERZEICHNIS.toAbsolutePath());
		}
		try (Stream<Path> pfade = Files.walk(QUELLVERZEICHNIS)) {
			return pfade.filter(pfad -> pfad.toString().endsWith(".java"))
					.filter(pfad -> !pfad.toString().contains(VERZEICHNIS_DOKU))
					.map(Reportingquelltexte::lies)
					.toList();
		} catch (final IOException e) {
			throw new UncheckedIOException("Die Quellen des Moduls konnten nicht gelesen werden.", e);
		}
	}

	/**
	 * Liest eine Quelldatei und blendet Kommentare und Zeichenketten aus.
	 *
	 * @param pfad Der Pfad der Datei.
	 *
	 * @return Die eingelesene Quelldatei.
	 */
	private static Quelldatei lies(final Path pfad) {
		try {
			return new Quelldatei(pfad, ohneKommentareUndTexte(Files.readString(pfad, StandardCharsets.UTF_8)));
		} catch (final IOException e) {
			throw new UncheckedIOException("Die Quelldatei %s konnte nicht gelesen werden.".formatted(pfad), e);
		}
	}

	/**
	 * Ersetzt Kommentare und Zeichenketten durch Leerzeichen und behält dabei alle Zeilenumbrüche.
	 *
	 * @param quelltext Der ursprüngliche Quelltext.
	 *
	 * @return Der bereinigte Quelltext gleicher Länge.
	 */
	private static String ohneKommentareUndTexte(final String quelltext) {
		final StringBuilder ergebnis = new StringBuilder(quelltext.length());
		int i = 0;
		while (i < quelltext.length()) {
			final int start = i;
			if (quelltext.startsWith("//", i)) {
				i = endeZeilenkommentar(quelltext, i);
			} else if (quelltext.startsWith("/*", i)) {
				i = endeBlockkommentar(quelltext, i);
			} else if ((quelltext.charAt(i) == '"') || (quelltext.charAt(i) == '\'')) {
				i = endeText(quelltext, i);
			} else {
				ergebnis.append(quelltext.charAt(i));
				i++;
				continue;
			}
			ausblenden(ergebnis, quelltext.substring(start, i));
		}
		return ergebnis.toString();
	}

	/**
	 * Hängt den übergebenen Abschnitt als Leerzeichen an und behält dabei die Zeilenumbrüche.
	 *
	 * @param ergebnis  Der Puffer des bereinigten Quelltextes.
	 * @param abschnitt Der auszublendende Abschnitt.
	 */
	private static void ausblenden(final StringBuilder ergebnis, final String abschnitt) {
		for (int i = 0; i < abschnitt.length(); i++) {
			ergebnis.append((abschnitt.charAt(i) == '\n') ? '\n' : ' ');
		}
	}

	/**
	 * Gibt das Ende eines Zeilenkommentars zurück.
	 *
	 * @param quelltext Der Quelltext.
	 * @param start     Die Position der einleitenden Zeichen.
	 *
	 * @return Die Position hinter dem Kommentar.
	 */
	private static int endeZeilenkommentar(final String quelltext, final int start) {
		final int ende = quelltext.indexOf('\n', start);
		return (ende < 0) ? quelltext.length() : ende;
	}

	/**
	 * Gibt das Ende eines Blockkommentars zurück.
	 *
	 * @param quelltext Der Quelltext.
	 * @param start     Die Position der einleitenden Zeichen.
	 *
	 * @return Die Position hinter dem Kommentar.
	 */
	private static int endeBlockkommentar(final String quelltext, final int start) {
		final int ende = quelltext.indexOf("*/", start + 2);
		return (ende < 0) ? quelltext.length() : (ende + 2);
	}

	/**
	 * Gibt das Ende einer Zeichenkette oder eines Zeichenliterals zurück. Ein maskiertes Anführungszeichen beendet sie nicht.
	 *
	 * @param quelltext Der Quelltext.
	 * @param start     Die Position des öffnenden Anführungszeichens.
	 *
	 * @return Die Position hinter der Zeichenkette.
	 */
	private static int endeText(final String quelltext, final int start) {
		final char begrenzer = quelltext.charAt(start);
		int i = start + 1;
		while (i < quelltext.length()) {
			final char zeichen = quelltext.charAt(i);
			if (zeichen == '\\') {
				i += 2;
			} else if ((zeichen == begrenzer) || (zeichen == '\n')) {
				return i + 1;
			} else {
				i++;
			}
		}
		return quelltext.length();
	}


	/**
	 * Gibt die Rümpfe aller Catch-Blöcke der Quelldatei mit ihrer Startposition zurück.
	 *
	 * @param datei Die zu untersuchende Quelldatei.
	 *
	 * @return Die Catch-Blöcke als Paare aus Startposition und Rumpf.
	 */
	static List<Catchblock> catchbloecke(final Quelldatei datei) {
		final List<Catchblock> bloecke = new ArrayList<>();
		final String inhalt = datei.inhalt();
		final Matcher treffer = CATCH_MIT_RUMPF.matcher(inhalt);
		while (treffer.find()) {
			final int rumpfBeginn = treffer.end() - 1;
			bloecke.add(new Catchblock(treffer.start(), inhalt.substring(rumpfBeginn + 1, endeBlock(inhalt, rumpfBeginn))));
		}
		return bloecke;
	}

	/**
	 * Gibt die Position der schließenden Klammer zum Block zurück, der an der übergebenen Position beginnt.
	 *
	 * @param inhalt      Der bereinigte Quelltext.
	 * @param blockBeginn Die Position der öffnenden Klammer.
	 *
	 * @return Die Position der zugehörigen schließenden Klammer.
	 */
	private static int endeBlock(final String inhalt, final int blockBeginn) {
		int tiefe = 0;
		for (int i = blockBeginn; i < inhalt.length(); i++) {
			if (inhalt.charAt(i) == '{') {
				tiefe++;
			} else if (inhalt.charAt(i) == '}') {
				tiefe--;
				if (tiefe == 0) {
					return i;
				}
			}
		}
		return inhalt.length();
	}


	/**
	 * Ein Catch-Block mit seiner Position in der Quelldatei.
	 *
	 * @param position Die Position des Schlüsselwortes {@code catch}.
	 * @param rumpf    Der Inhalt des Blocks.
	 */
	record Catchblock(int position, String rumpf) {
		// Nur Träger der Fundstelle.
	}

}
