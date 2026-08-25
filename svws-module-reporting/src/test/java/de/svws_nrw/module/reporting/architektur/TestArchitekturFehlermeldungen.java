package de.svws_nrw.module.reporting.architektur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import de.svws_nrw.module.reporting.architektur.Reportingquelltexte.Quelldatei;

/**
 * Prüft die Form der Fehlermeldungen am Quelltext, nicht an einzelnen Wortlauten.
 * <p>Ein Test, der einen konkreten Satz festschreibt, sichert den erreichten Stand. Er sagt nichts über die Meldung, die jemand nächstes Jahr schreibt.
 * Diese Regeln gelten dagegen für jede künftige Meldung: Sie lesen alle Quelldateien und erfassen damit auch Klassen, die es heute nicht gibt.</p>
 * <p>Gelesen wird über {@link Reportingquelltexte#alleMitTexten()}, denn der Marker steht in genau der Zeichenkette, die der übliche Leseweg entfernt.
 * Kommentare fallen weiterhin weg: Ein JavaDoc, das eine Meldung zitiert, ist keine Meldung.</p>
 * <p>Jede Regel ist eine Methode über eine einzelne Quelldatei. So lässt sie sich auch gegen einen synthetischen Verstoß prüfen - erst diese Proben zeigen,
 * dass eine Regel anschlägt; die Zahl der Fundstellen im Modul beweist das nicht.</p>
 * <p>Die Regeln lesen Text. Sie finden deshalb nicht, was erst ein Vergleich zeigt: eine Protokollzeile, die den Meldungstext ohne Marker wiederholt,
 * oder einen Catch, der die gefangene Exception nicht als Ursache mitgibt. Das sichern die Verhaltenstests an der jeweiligen Stelle.</p>
 */
class TestArchitekturFehlermeldungen {

	/** Der Marker, den jede Meldung an den Anwender trägt. Der Client erkennt daran die Zeilen, die er hervorhebt. */
	private static final String MARKER = "### FEHLER: ";

	/** Die Mindestzahl an Meldungen, die der Test im Modul finden muss, damit der Leseweg als intakt gilt. */
	private static final int SCHWELLE_MELDUNGEN = 60;

	/** Zeichen, mit denen eine Meldung enden darf. */
	private static final String SATZZEICHEN = ".!?";

	/** Eine Zeichenkette im Quelltext, mit ihren maskierten Zeichen. */
	private static final Pattern ZEICHENKETTE = Pattern.compile("\"((?:[^\"\\\\]|\\\\.)*)\"");

	/**
	 * Ein Bezeichner in Großbuchstaben innerhalb der Wurfklammer: eine Konstante, deren Text in derselben Datei steht. Ein qualifizierter Name wie
	 * {@code Status.BAD_REQUEST} zählt nicht. Eine Konstante aus einer anderen Datei bleibt unaufgelöst - diese Grenze ist bekannt.
	 */
	private static final Pattern KONSTANTE = Pattern.compile("(?<![.\\w])([A-Z][A-Z0-9_]{2,})(?!\\w)");

	/**
	 * Zeichenketten und Konstanten in der Reihenfolge ihres Auftretens. Eine Meldung kann aus einer Konstante und einem Zusatz bestehen; dann trägt die
	 * Konstante den Marker, und der Zusatz entscheidet über das Satzzeichen. Gruppe 1 ist der Text einer Zeichenkette, Gruppe 2 der Name einer Konstante.
	 */
	private static final Pattern ZEICHENKETTE_ODER_KONSTANTE = Pattern.compile(ZEICHENKETTE.pattern() + "|" + KONSTANTE.pattern());

	/** Der Beginn eines Wurfs, dessen Meldung geprüft wird. */
	private static final Pattern WURF = Pattern.compile("new\\s+ApiOperationException\\s*\\(");

	/** Der Beginn einer Protokollzeile, mit oder ohne Zeilenumbruch am Ende. Wie sie ihr Level erhält, spielt keine Rolle. */
	private static final Pattern PROTOKOLLZEILE = Pattern.compile("\\blog(?:Ln)?\\s*\\(");

	/** Das Wort, mit dem der Code seine Schlüssel benennt. Der Anwender wählt Klassen aus, keine Klassen-IDs. */
	private static final Pattern INTERNE_ID = Pattern.compile("\\bIDs?\\b");

	/**
	 * Die Klasse, die die Exception-Blöcke schreibt - auf ERROR für den Abbruch an der Abschlussgrenze, auf WARNING oder INFO für einen hingenommenen
	 * Fehler. Ihre Zeilen tragen den Marker zu Recht: Er kennzeichnet dort die Exception, nicht das Level und keinen Abbruch. Jede andere Protokollzeile
	 * mit Marker ist eine zweite Meldung an denselben Leser.
	 */
	private static final Set<String> FORMATIERER_DER_EXCEPTIONBLOECKE = Set.of("ReportingExceptionUtils.java");

	/**
	 * Wörter, die im Quelltext ihren Platz haben, aber nicht in einer Meldung an den Anwender. Er kennt weder die Klassen des Servers noch die Begriffe,
	 * mit denen der Code seine Bestandteile benennt.
	 */
	private static final List<String> INTERNE_BEGRIFFE = List.of(
			"Context", "Builder", "Repository", "Factory", "Initializer", "Manager", "Renderer",
			"null", "Exception", "NullPointer", "MIME", "Input");


	// ##### Lesen der Meldungen #####

	/**
	 * Sammelt die Meldungen einer Quelldatei: je Wurf die Texte innerhalb der Klammer. Ein Wurf, dessen Meldung aus einer Variablen oder einer Konstante
	 * einer anderen Datei stammt, liefert keine Meldung - diese Grenze ist bekannt.
	 *
	 * @param datei Die Quelldatei mit erhaltenen Zeichenketten.
	 *
	 * @return Die Meldungen mit ihrer Fundstelle.
	 */
	private static List<Meldung> meldungen(final Quelldatei datei) {
		final List<Meldung> gefunden = new ArrayList<>();
		final Matcher wurf = WURF.matcher(datei.inhalt());
		while (wurf.find()) {
			final Meldung meldung = texteInDerKlammer(datei, wurf.start(), wurf.end() - 1);
			if (!meldung.teile().isEmpty()) {
				gefunden.add(meldung);
			}
		}
		return gefunden;
	}

	/**
	 * Liest die Texte innerhalb einer Klammer - eines Wurfs oder einer Protokollzeile: Zeichenketten und aufgelöste Konstanten in ihrer Reihenfolge. Wurf
	 * und Protokollzeile lesen über denselben Weg, damit eine Konstante der einen Regel nicht entgeht, die die andere längst auflöst.
	 *
	 * @param datei    Die Quelldatei, in der Konstanten aufgelöst werden.
	 * @param position Die Position des Aufrufs, für die Fundstelle.
	 * @param klammer  Die Position der öffnenden Klammer.
	 *
	 * @return Die Texte als Meldung; ohne Teile, wenn die Klammer keinen Text enthält.
	 */
	private static Meldung texteInDerKlammer(final Quelldatei datei, final int position, final int klammer) {
		final String argumente = datei.inhalt().substring(klammer, endeDerArgumente(datei.inhalt(), klammer));
		final Matcher teil = ZEICHENKETTE_ODER_KONSTANTE.matcher(argumente);
		final List<String> teile = new ArrayList<>();
		boolean ausKonstante = false;
		while (teil.find()) {
			if (teil.group(1) != null) {
				teile.add(teil.group(1));
			} else {
				final List<String> aufgeloest = literaleDerKonstante(datei.inhalt(), teil.group(2));
				ausKonstante |= !aufgeloest.isEmpty();
				teile.addAll(aufgeloest);
			}
		}
		return new Meldung(datei.fundstelle(position), teile, ausKonstante);
	}

	/**
	 * Gibt die Position der schließenden Klammer zum Aufruf zurück, der an der übergebenen Position beginnt.
	 *
	 * @param inhalt  Der Inhalt der Quelldatei.
	 * @param beginn  Die Position der öffnenden Klammer.
	 *
	 * @return Die Position der schließenden Klammer, oder das Ende des Inhalts.
	 */
	private static int endeDerArgumente(final String inhalt, final int beginn) {
		int tiefe = 0;
		for (int i = beginn; i < inhalt.length(); i++) {
			if (inhalt.charAt(i) == '(') {
				tiefe++;
			} else if (inhalt.charAt(i) == ')') {
				tiefe--;
				if (tiefe == 0) {
					return i;
				}
			}
		}
		return inhalt.length();
	}

	/**
	 * Gibt die Zeichenketten zurück, aus denen die Konstante mit dem übergebenen Namen besteht - bei einer verketteten Meldung mehrere.
	 *
	 * @param inhalt Der Inhalt der Quelldatei.
	 * @param name   Der Name der Konstante.
	 *
	 * @return Die Zeichenketten der Zuweisung in ihrer Reihenfolge; leer, wenn die Datei die Konstante nicht als String deklariert.
	 */
	private static List<String> literaleDerKonstante(final String inhalt, final String name) {
		final Matcher deklaration = Pattern.compile("\\bString\\s+" + Pattern.quote(name) + "\\s*=").matcher(inhalt);
		if (!deklaration.find()) {
			return List.of();
		}
		final List<String> teile = new ArrayList<>();
		final Matcher text = ZEICHENKETTE.matcher(inhalt);
		int cursor = deklaration.end();
		while (text.find(cursor) && !inhalt.substring(cursor, text.start()).contains(";")) {
			teile.add(text.group(1));
			cursor = text.end();
		}
		return teile;
	}

	/**
	 * Gibt an, ob die Datei die Exception-Blöcke formatiert und deshalb Protokollzeilen mit Marker schreiben darf.
	 *
	 * @param pfad Der Pfad der Quelldatei.
	 *
	 * @return true, wenn die Datei der Formatierer ist.
	 */
	private static boolean formatiererDerExceptionbloecke(final Path pfad) {
		return FORMATIERER_DER_EXCEPTIONBLOECKE.contains(pfad.getFileName().toString());
	}


	// ##### Die Regeln, je über eine Quelldatei #####

	/**
	 * Regel: Jede Meldung beginnt mit dem Marker. Ein Text, der ihn anders schreibt oder weglässt, geht in der Ausgabe des Clients unter.
	 *
	 * @param datei Die Quelldatei.
	 *
	 * @return Die Verstöße mit Fundstelle.
	 */
	private static List<String> ohneMarker(final Quelldatei datei) {
		return meldungen(datei).stream().filter(meldung -> !meldung.anfang().startsWith(MARKER))
				.map(meldung -> "%s: %s".formatted(meldung.fundstelle(), meldung.anfang())).toList();
	}

	/**
	 * Regel: Jede Meldung endet mit einem Satzzeichen. Der Client reiht sie mit anderen Zeilen; ohne Schlusszeichen laufen zwei Meldungen ineinander.
	 *
	 * @param datei Die Quelldatei.
	 *
	 * @return Die Verstöße mit Fundstelle.
	 */
	private static List<String> ohneSatzzeichen(final Quelldatei datei) {
		return meldungen(datei).stream().filter(meldung -> !endetMitSatzzeichen(meldung.schluss()))
				.map(meldung -> "%s: %s".formatted(meldung.fundstelle(), meldung.schluss())).toList();
	}

	/**
	 * Regel: Keine Meldung nennt einen internen Begriff. Ein Klassenname sagt dem Anwender nichts und verdeckt, was er tun kann.
	 *
	 * @param datei Die Quelldatei.
	 *
	 * @return Die Verstöße mit Fundstelle und dem gefundenen Begriff.
	 */
	private static List<String> mitInternemBegriff(final Quelldatei datei) {
		final List<String> verstoesse = new ArrayList<>();
		for (final Meldung meldung : meldungen(datei)) {
			INTERNE_BEGRIFFE.stream().filter(meldung.ganz()::contains).findFirst()
					.ifPresent(begriff -> verstoesse.add("%s nennt '%s': %s".formatted(meldung.fundstelle(), begriff, meldung.ganz())));
		}
		return verstoesse;
	}

	/**
	 * Regel: Keine Meldung nennt eine ID. Ein veränderlicher Wert gehört hinein, soweit der Anwender ihn kennt - den Schlüssel der Datenbank kennt er nicht.
	 *
	 * @param datei Die Quelldatei.
	 *
	 * @return Die Verstöße mit Fundstelle.
	 */
	private static List<String> mitInternerId(final Quelldatei datei) {
		return meldungen(datei).stream().filter(meldung -> INTERNE_ID.matcher(meldung.ganz()).find())
				.map(meldung -> "%s: %s".formatted(meldung.fundstelle(), meldung.ganz())).toList();
	}

	/**
	 * Regel: Keine Protokollzeile trägt den Marker. Ein Abbruch hat eine Meldungsquelle - die Meldung der Exception. Eine Zeile neben dem Wurf darf allein
	 * eine technische Angabe nennen, die sonst niemand trägt; mit Marker wäre sie eine zweite Meldung - gleich wie sie ihr Level erhält und ob ihr Text als
	 * Zeichenkette oder als Konstante ankommt.
	 *
	 * @param datei Die Quelldatei.
	 *
	 * @return Die Verstöße mit Fundstelle.
	 */
	private static List<String> protokollzeilenMitMarker(final Quelldatei datei) {
		final List<String> verstoesse = new ArrayList<>();
		final Matcher zeile = PROTOKOLLZEILE.matcher(datei.inhalt());
		while (zeile.find()) {
			final Meldung texte = texteInDerKlammer(datei, zeile.start(), zeile.end() - 1);
			texte.teile().stream().filter(teil -> teil.contains(MARKER.strip()))
					.forEach(teil -> verstoesse.add("%s: %s".formatted(texte.fundstelle(), teil)));
		}
		return verstoesse;
	}

	/**
	 * Wendet eine Regel auf alle Quelldateien des Moduls an.
	 *
	 * @param regel Die Regel über eine einzelne Quelldatei.
	 *
	 * @return Alle Verstöße im Modul.
	 */
	private static List<String> verstoesseImModul(final Function<Quelldatei, List<String>> regel) {
		return verstoesseImModul(regel, pfad -> false);
	}

	/**
	 * Wendet eine Regel auf alle Quelldateien des Moduls an, die nicht ausgenommen sind.
	 *
	 * @param regel       Die Regel über eine einzelne Quelldatei.
	 * @param ausgenommen Die Dateien, für die die Regel nicht gilt.
	 *
	 * @return Alle Verstöße im Modul.
	 */
	private static List<String> verstoesseImModul(final Function<Quelldatei, List<String>> regel, final Predicate<Path> ausgenommen) {
		return Reportingquelltexte.alleMitTexten().stream().filter(datei -> !ausgenommen.test(datei.pfad())).map(regel).flatMap(List::stream).toList();
	}


	// ##### Die Regeln über das Modul #####

	@Test
	void testJedeMeldungTraegtDenMarker() {
		assertEquals(List.of(), verstoesseImModul(TestArchitekturFehlermeldungen::ohneMarker),
				"Diese Meldungen beginnen nicht mit '" + MARKER + "'.");
	}

	@Test
	void testJedeMeldungEndetMitEinemSatzzeichen() {
		assertEquals(List.of(), verstoesseImModul(TestArchitekturFehlermeldungen::ohneSatzzeichen),
				"Diese Meldungen enden ohne Satzzeichen.");
	}

	@Test
	void testKeineMeldungNenntEinenInternenBegriff() {
		assertEquals(List.of(), verstoesseImModul(TestArchitekturFehlermeldungen::mitInternemBegriff),
				"Diese Meldungen nennen einen internen Begriff.");
	}

	@Test
	void testKeineMeldungNenntEineInterneId() {
		assertEquals(List.of(), verstoesseImModul(TestArchitekturFehlermeldungen::mitInternerId),
				"Diese Meldungen nennen eine ID.");
	}

	@Test
	void testKeineProtokollzeileTraegtDenMarker() {
		assertEquals(List.of(),
				verstoesseImModul(TestArchitekturFehlermeldungen::protokollzeilenMitMarker, TestArchitekturFehlermeldungen::formatiererDerExceptionbloecke),
				"Diese Protokollzeilen tragen den Meldungsmarker und wiederholen damit eine Exception-Meldung. Eine technische Zeile nennt allein die Angabe, "
						+ "die sonst niemand trägt; den Exception-Block schreibt allein ReportingExceptionUtils.");
	}


	// ##### Gegenproben: Der Leseweg ist intakt, und die Regeln schlagen an #####

	@Test
	void testDerLesewegFindetDieMeldungenDesModuls() {
		// Ohne diese Gegenprobe liefen die Regeln leer, sobald der Leseweg die Zeichenketten doch entfernt oder das Wurfmuster nicht mehr passt.
		final List<Meldung> alle = Reportingquelltexte.alleMitTexten().stream().map(TestArchitekturFehlermeldungen::meldungen).flatMap(List::stream)
				.toList();
		assertFalse(alle.size() < SCHWELLE_MELDUNGEN,
				"Es wurden nur %d Meldungen gefunden. Erwartet werden mindestens %d - der Leseweg oder das Wurfmuster stimmt nicht mehr."
						.formatted(alle.size(), SCHWELLE_MELDUNGEN));
		assertTrue(alle.stream().anyMatch(Meldung::ausKonstante),
				"Keine Meldung stammt aus einer Konstante. Das Modul wirft solche - die Auflösung der Konstanten greift nicht mehr.");
	}

	@Test
	void testDieFormregelnErkennenEineMeldungOhneMarker() {
		// Eine Meldung ohne Marker darf keiner Regel entgehen - auch nicht den Regeln, die sich auf den Text hinter dem Marker beziehen.
		final Quelldatei probe = probe("""
				throw new ApiOperationException(Status.BAD_REQUEST, "Builder ID fehlt");
				""");
		assertEquals(1, ohneMarker(probe).size(), "Der fehlende Marker muss auffallen.");
		assertEquals(1, ohneSatzzeichen(probe).size(), "Das fehlende Satzzeichen muss auffallen.");
		assertEquals(1, mitInternemBegriff(probe).size(), "Der interne Begriff muss auffallen.");
		assertEquals(1, mitInternerId(probe).size(), "Die ID muss auffallen.");

		final Quelldatei gegenprobe = probe("""
				throw new ApiOperationException(Status.BAD_REQUEST, "### FEHLER: Es wurden keine Klassen ausgewählt.");
				""");
		assertEquals(List.of(), ohneMarker(gegenprobe));
		assertEquals(List.of(), ohneSatzzeichen(gegenprobe));
		assertEquals(List.of(), mitInternemBegriff(gegenprobe));
		assertEquals(List.of(), mitInternerId(gegenprobe));
	}

	@Test
	void testDieFormregelnLesenEineKonstanteMitZusatz() {
		// Die Konstante trägt den Marker, der Zusatz entscheidet über das Satzzeichen. Beides muss der Test in dieser Reihenfolge sehen.
		final Quelldatei ohnePunkt = probe("""
				private static final String FEHLER_BASIS = "### FEHLER: Die Basis";
				throw new ApiOperationException(Status.BAD_REQUEST, FEHLER_BASIS + " und ein Zusatz");
				""");
		assertEquals(List.of(), ohneMarker(ohnePunkt), "Der Marker steht in der Konstante.");
		assertEquals(1, ohneSatzzeichen(ohnePunkt).size(), "Das fehlende Satzzeichen im Zusatz muss auffallen.");

		final Quelldatei mitPunkt = probe("""
				private static final String FEHLER_BASIS = "### FEHLER: Die Basis";
				throw new ApiOperationException(Status.BAD_REQUEST, FEHLER_BASIS + " und ein Zusatz.");
				""");
		assertEquals(List.of(), ohneSatzzeichen(mitPunkt));

		final Quelldatei nurKonstante = probe("""
				private static final String FEHLER_X = "Der Wert ist ungültig.";
				throw new ApiOperationException(Status.BAD_REQUEST, FEHLER_X);
				""");
		assertEquals(1, ohneMarker(nurKonstante).size(), "Der fehlende Marker in der Konstante muss auffallen.");
	}

	@Test
	void testDieProtokollregelErkenntJedeFormDesLevels() {
		// Der Marker in einer Protokollzeile ist eine zweite Meldung - gleich, ob das Level als Literal, als Variable oder statisch importiert ankommt,
		// und gleich, ob der Text als Zeichenkette oder als Konstante steht.
		final Quelldatei probe = probe("""
				private static final String FEHLER_X = "### FEHLER: Der Datensatz fehlt.";
				logger.logLn(LogLevel.WARNING, 4, "### FEHLER: Der Datensatz fehlt.");
				logger.logLn(level, 4, "### FEHLER: Der Datensatz fehlt.");
				logger.log(ERROR, 4, "### FEHLER: Der Datensatz fehlt.");
				logger.logLn(level, 4, FEHLER_X);
				logger.logLn(LogLevel.ERROR, 4, "Beanstandete Stufe: " + kombinierteId);
				""");
		assertEquals(4, protokollzeilenMitMarker(probe).size(), "Vier Zeilen tragen den Marker - drei als Zeichenkette, eine als Konstante -, die "
				+ "technische Zeile nicht.");
	}


	/**
	 * Gibt an, ob die Meldung ordentlich schließt.
	 *
	 * @param text Der letzte Teil des Meldungstextes.
	 *
	 * @return true, wenn die Meldung mit einem Satzzeichen endet.
	 */
	private static boolean endetMitSatzzeichen(final String text) {
		final String geschlossen = text.strip();
		return !geschlossen.isEmpty() && (SATZZEICHEN.indexOf(geschlossen.charAt(geschlossen.length() - 1)) >= 0);
	}

	/**
	 * Baut eine synthetische Quelldatei für die Gegenproben. Sie enthält keine Kommentare; der Leseweg des Moduls würde sie sonst entfernen.
	 *
	 * @param quelltext Der Quelltext der Probe.
	 *
	 * @return Die Probe als Quelldatei.
	 */
	private static Quelldatei probe(final String quelltext) {
		return new Quelldatei(Path.of("Probe.java"), quelltext);
	}

	/**
	 * Die Texte innerhalb einer Klammer - eines Wurfs oder einer Protokollzeile - mit ihrer Fundstelle.
	 *
	 * @param fundstelle   Datei und Zeile des Aufrufs.
	 * @param teile        Die Zeichenketten des Wurfs in ihrer Reihenfolge; eine verkettete Meldung besteht aus mehreren.
	 * @param ausKonstante true, wenn ein Teil aus einer Konstante derselben Datei stammt.
	 */
	private record Meldung(String fundstelle, List<String> teile, boolean ausKonstante) {

		/**
		 * Gibt den Anfang der Meldung zurück - dort steht der Marker.
		 *
		 * @return Die erste Zeichenkette des Wurfs.
		 */
		String anfang() {
			return teile.getFirst();
		}

		/**
		 * Gibt den Schluss der Meldung zurück. Bei einer verketteten Meldung trägt erst der letzte Teil das Satzzeichen.
		 *
		 * @return Die letzte Zeichenkette des Wurfs.
		 */
		String schluss() {
			return teile.getLast();
		}

		/**
		 * Gibt die ganze Meldung zurück, wie sie der Anwender liest.
		 *
		 * @return Alle Teile, durch ein Leerzeichen verbunden.
		 */
		String ganz() {
			return String.join(" ", teile);
		}
	}

}
