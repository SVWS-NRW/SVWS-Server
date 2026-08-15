package de.svws_nrw.module.reporting.diagnose;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Überführt die gemeldeten Ausgabeprobleme in den öffentlichen Hinweisvertrag: den Header und den Text der Hinweisdatei.
 * <p>Nach außen gelangen nur Kategorie und Anzahl - keine IDs, Namen, Freitexte oder Stacktraces, denn Header und Datei begleiten Dokumente, die
 * weitergegeben werden. Gezählt werden die deduplizierten Probleme; die Kategorienzahlen zerlegen dieselbe Menge, ihre Summe ergibt stets {@code gesamt}.
 * Ob eine Antwort den Header trägt, entscheidet der Aufrufer anhand der Diagnose-Anbindung des Datenaufbaus, nicht dieser Serializer.</p>
 */
public final class ReportingHinweisSerializer {

	/** Der Name des Response-Headers, über den die Hinweise eine erfolgreiche Ausgabe begleiten. */
	public static final String HEADER_NAME = "SVWS-Reporting-Hinweise";

	/** Die Version des Vertrags. 0 ist die ausdrücklich vorläufige Fassung: Der Kern liegt fest, der Kategorienkatalog darf sich noch ändern. */
	public static final int VERTRAGSVERSION = 0;

	/** Der Name der Hinweisdatei in einer ZIP-Ausgabe. Die Großschreibung kennzeichnet sie als Beilage neben den PDF-Dateien. */
	public static final String DATEINAME_HINWEISE = "HINWEISE.txt";

	/** Die Überschrift der Hinweisdatei. */
	private static final String UEBERSCHRIFT = "Hinweise zu dieser Ausgabe";

	private ReportingHinweisSerializer() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}


	/**
	 * Bildet den Wert des Hinweis-Headers nach RFC 9651, etwa {@code v=0, gesamt=3, leer=?0, datensaetze=2, angaben=1}.
	 * Kategorien ohne Befund fehlen im Wert; die Reihenfolge ist fest, damit derselbe Sachverhalt denselben Wert ergibt.
	 *
	 * @param probleme    Die gemeldeten, bereits deduplizierten Ausgabeprobleme; bei problemfreier Ausgabe eine leere Sammlung.
	 * @param bewusstLeer true, wenn Datensätze angefordert waren und keiner übrig blieb.
	 *
	 * @return Der Wert des Headers.
	 *
	 * @throws NullPointerException Wenn {@code null} übergeben wird - ein fehlender Diagnosebestand ist keine geprüfte problemfreie Ausgabe.
	 */
	public static String headerwert(final Collection<ReportingProblem> probleme, final boolean bewusstLeer) {
		final Map<ReportingHinweisKategorie, Integer> anzahlen = anzahlenJeKategorie(probleme);

		final StringJoiner wert = new StringJoiner(", ");
		wert.add("v=" + VERTRAGSVERSION);
		wert.add("gesamt=" + probleme.size());
		// Ein Boolean wird nach RFC 9651 als ?0 oder ?1 geschrieben und nicht als true oder false.
		wert.add("leer=?" + (bewusstLeer ? 1 : 0));
		for (final ReportingHinweisKategorie kategorie : ReportingHinweisKategorie.values()) {
			final int anzahl = anzahlen.getOrDefault(kategorie, 0);
			if (anzahl > 0) {
				wert.add(kategorie.schluessel() + "=" + anzahl);
			}
		}
		return wert.toString();
	}

	/**
	 * Gibt an, ob eine ZIP-Ausgabe eine Hinweisdatei erhält: bei mindestens einem Hinweis oder bei einer Auswahl ohne verbliebenen Datensatz.
	 * Anders als der Header hängt die Datei nicht an der Diagnose-Anbindung - sie erklärt, was bekannt ist, und behauptet keine Vollständigkeit.
	 *
	 * @param probleme    Die gemeldeten Ausgabeprobleme; {@code null} ist unzulässig.
	 * @param bewusstLeer true, wenn Datensätze angefordert waren und keiner übrig blieb.
	 *
	 * @return true, wenn die Ausgabe eine Hinweisdatei erhält, sonst false.
	 */
	public static boolean brauchtHinweisdatei(final Collection<ReportingProblem> probleme, final boolean bewusstLeer) {
		Objects.requireNonNull(probleme, "Ein fehlender Diagnosebestand ist keine problemfreie Ausgabe; eine solche übergibt eine leere Sammlung.");
		return bewusstLeer || !probleme.isEmpty();
	}

	/**
	 * Bildet den Inhalt der Hinweisdatei. Der Text richtet sich an den Empfänger des Archivs: Anzahlen und lesbare Kategorien, keine IDs, Namen oder
	 * Fehlermeldungen. Der reine Filter-Leerfall bleibt ohne Fehlerbehauptung - wer alles ausfiltert, hat nichts falsch gemacht.
	 *
	 * @param probleme    Die gemeldeten, bereits deduplizierten Ausgabeprobleme; {@code null} ist unzulässig.
	 * @param bewusstLeer true, wenn Datensätze angefordert waren und keiner übrig blieb.
	 *
	 * @return Der Inhalt der Hinweisdatei.
	 */
	public static String hinweisdatei(final Collection<ReportingProblem> probleme, final boolean bewusstLeer) {
		final Map<ReportingHinweisKategorie, Integer> anzahlen = anzahlenJeKategorie(probleme);

		final StringBuilder text = new StringBuilder(UEBERSCHRIFT).append(System.lineSeparator())
				.append("=".repeat(UEBERSCHRIFT.length())).append(System.lineSeparator()).append(System.lineSeparator());

		if (bewusstLeer) {
			// In der ZIP-Ausgabe bedeutet eine leere Auswahl zwingend, dass keine PDF-Datei entstand: Ein Archiv entsteht bei mehreren Dokumenten oder bei
			// keinem, und bei einem einzelnen Dokument wird dieses unmittelbar geliefert.
			text.append(probleme.isEmpty()
					? "Zu den angeforderten Daten blieb nach der Auswahl kein Datensatz übrig. Es wurde deshalb keine PDF-Datei erzeugt."
					: "Zu den angeforderten Daten blieb nach der Auswahl kein Datensatz übrig. Es wurde deshalb keine PDF-Datei erzeugt. "
							+ "Beim Zusammenstellen der Daten sind Hinweise aufgetreten.")
					.append(System.lineSeparator());
		} else {
			text.append("Die Ausgabe wurde erstellt, ist aber unvollständig.").append(System.lineSeparator());
		}

		if (!probleme.isEmpty()) {
			text.append(System.lineSeparator()).append("Hinweise insgesamt: ").append(probleme.size()).append(System.lineSeparator());
			for (final ReportingHinweisKategorie kategorie : ReportingHinweisKategorie.values()) {
				final int anzahl = anzahlen.getOrDefault(kategorie, 0);
				if (anzahl > 0) {
					text.append("- ").append(kategorie.bezeichnung()).append(": ").append(anzahl).append(System.lineSeparator());
				}
			}
			text.append(System.lineSeparator())
					.append("Die Anzahl bezeichnet die festgestellten Hinweise und nicht die Anzahl fehlender Datensätze oder Dateien.")
					.append(System.lineSeparator());
		}

		return text.toString();
	}

	/**
	 * Zählt die übergebenen Ausgabeprobleme je öffentlicher Kategorie.
	 *
	 * @param probleme Die gemeldeten Ausgabeprobleme; eine problemfreie Ausgabe übergibt eine leere Sammlung, {@code null} ist unzulässig.
	 *
	 * @return Die Anzahl je Kategorie; Kategorien ohne Befund fehlen.
	 *
	 * @throws NullPointerException Wenn kein Diagnosebestand übergeben wurde.
	 */
	public static Map<ReportingHinweisKategorie, Integer> anzahlenJeKategorie(final Collection<ReportingProblem> probleme) {
		Objects.requireNonNull(probleme, "Ein fehlender Diagnosebestand ist keine problemfreie Ausgabe; eine solche übergibt eine leere Sammlung.");

		final Map<ReportingHinweisKategorie, Integer> anzahlen = new EnumMap<>(ReportingHinweisKategorie.class);
		for (final ReportingProblem problem : probleme) {
			anzahlen.merge(ReportingHinweisKategorie.fuer(problem), 1, Integer::sum);
		}
		return anzahlen;
	}

}
