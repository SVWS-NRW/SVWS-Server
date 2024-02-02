package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostNachschreibterminblockungKonfiguration;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.ListUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Algorithmus zur Blockung von Klausuren auf eine minimale Anzahl von Schienen
 * (ergo Klausurtage).
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurblockungNachschreiberAlgorithmus {

	private static final @NotNull Random _random = new Random();

	/** Ein Logger für Debug-Zwecke. */
	private final @NotNull Logger _logger;

	/**
	 * Der Konstruktor.
	 */
	public KlausurblockungNachschreiberAlgorithmus() {
		_logger = new Logger();
	}

	/**
	 * Der Konstruktor.
	 *
	 * @param pLogger  Ein Logger für Debug-Zwecke.
	 */
	public KlausurblockungNachschreiberAlgorithmus(final @NotNull Logger pLogger) {
		_logger = pLogger;
	}

	/**
	 * @param config   		  Die Konfiguration
	 * @param klausurManager  Der Kursklausur-Manager.
	 * @param maxTimeMillis   Die maximal erlaubte Berechnungszeit (in Millisekunden).
	 *
	 * @return Eine Liste von Paaren: 1. Element = GostSchuelerklausurtermin (Nachschreiber), 2. Element = ID des Termins / der Schiene
	 */
	public @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> berechne(
					final @NotNull GostNachschreibterminblockungKonfiguration config,
					final @NotNull GostKursklausurManager klausurManager,
					final long maxTimeMillis) {

		// 1) Bilde Gruppen von Nachschreibern, falls dies bestimmte Kriterien/Regeln erfordern.
		final @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTermin>> nachschreiberGruppen = new ArrayList<>(); // Liste der Gruppen.

		for (final @NotNull GostSchuelerklausurTermin skt : config.schuelerklausurtermine) {
			final GostSchuelerklausur sk = klausurManager.schuelerklausurBySchuelerklausurtermin(skt);
			final long idSchueler = sk.idSchueler; // die ID des Schülers, die innerhalb jedes Klausurtermins / Schiene einzigartig sein muss
			final long idKurs = sk.idKursklausur; // die ID der Kursklausur als Gütekriterium, möglichst alle mit gleicher Kursklausur innerhalb eines Termins
			DeveloperNotificationException.ifTrue("Ungültige Schüler-ID = " + idSchueler, idSchueler < 0);
			DeveloperNotificationException.ifTrue("Ungültige Kurs-ID = " + idKurs, idKurs < 0);

			// Überprüfe, ob "skt" in eine bereits vorhandene Gruppe darf.
			boolean added = false;
			for (final @NotNull List<@NotNull GostSchuelerklausurTermin> gruppe : nachschreiberGruppen)
				if (_istHinzufuegenErlaubt(gruppe, skt, config, klausurManager)) {
					gruppe.add(skt);
					added = true;
					break;
				}

			// Erzeuge eine neue Gruppe für "skt".
			if (!added)
				nachschreiberGruppen.add(ListUtils.create1(skt));
		}

		final long zeitEnde = System.currentTimeMillis() + config.maxTimeMillis;

		// 2) Berechne das erste Ergebnis und speichere seine Bewertung.
		@NotNull KlausurblockungNachschreiberAlgorithmusBewertung bestBewertung = new KlausurblockungNachschreiberAlgorithmusBewertung();
		@NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> bestErgebnis =
				_algorithmusProTerminZufaelligGruppenVerteilenZufaellig(bestBewertung, config.termine, nachschreiberGruppen, klausurManager);

		// 3) Solange noch Zeit ist, berechne weitere Ergebnisse.
		int c = 1;
		while (System.currentTimeMillis() < zeitEnde) {
			final KlausurblockungNachschreiberAlgorithmusBewertung bewertung = new KlausurblockungNachschreiberAlgorithmusBewertung();
			final @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> ergebnis =
					_algorithmusProTerminZufaelligGruppenVerteilenZufaellig(bewertung, config.termine, nachschreiberGruppen, klausurManager);

			if (bewertung.compare(bestBewertung) < 0) {
				bestBewertung = bewertung;
				bestErgebnis = ergebnis;
			}

			c++;
		}

		System.out.println("In " + config.maxTimeMillis + " wurden " + c + " Blockungen ausprobiert.");
		System.out.println("bestBewertung.anzahl_termine = " + bestBewertung.anzahl_termine);
		System.out.println("bestBewertung.anzahl_zusatztermine = " + bestBewertung.anzahl_zusatztermine);

		return bestErgebnis;
	}

	private static boolean _istHinzufuegenErlaubt(
					final @NotNull List<@NotNull GostSchuelerklausurTermin> gruppe,
					final @NotNull GostSchuelerklausurTermin skt1, final @NotNull GostNachschreibterminblockungKonfiguration config,
					final @NotNull GostKursklausurManager klausurManager) {

		// Integrität überprüfen.
		DeveloperNotificationException.ifTrue("Die Gruppe muss mindestens ein Element enthalten!", gruppe.isEmpty());

		final @NotNull GostSchuelerklausur sk1 = klausurManager.schuelerklausurBySchuelerklausurtermin(skt1);
		final long idFach = klausurManager.vorgabeBySchuelerklausurTermin(skt1).idFach;
		final @NotNull String kursart = klausurManager.vorgabeBySchuelerklausurTermin(skt1).kursart;

		// Verbiete, dass ein Schüler doppelt in einer Gruppe ist.
		for (final @NotNull GostSchuelerklausurTermin skt2 : gruppe) {
			final @NotNull GostSchuelerklausur sk2 = klausurManager.schuelerklausurBySchuelerklausurtermin(skt2);
			if (sk1.idSchueler == sk2.idSchueler)
				return false;
		}

		// Sollen Schüler-Nachschreibklausuren der selben Kurs-Klausur in der selben Gruppe landen?
		if (config._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen) {
			final @NotNull GostSchuelerklausurTermin first = ListUtils.getNonNullElementAtOrException(gruppe, 0);
			final @NotNull GostSchuelerklausur sk2 = klausurManager.schuelerklausurBySchuelerklausurtermin(first);
			return (sk1.idKursklausur == sk2.idKursklausur);
		}

		// Sollen Schüler-Nachschreibklausuren der selben Fachart in der selben Gruppe landen?
		if (config._regel_gleiche_fachart_auf_selbe_termine_verteilen) {
			final @NotNull GostSchuelerklausurTermin first = ListUtils.getNonNullElementAtOrException(gruppe, 0);
			final boolean fachGleich = klausurManager.vorgabeBySchuelerklausurTermin(first).idFach == idFach;
			final boolean kursartGleich = klausurManager.vorgabeBySchuelerklausurTermin(first).kursart.equals(kursart);
			return fachGleich && kursartGleich;
		}

		// Ein Hinzufügen zu dieser Gruppe ist nicht erlaubt.
		return false;
	}

	private static @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> _algorithmusProTerminZufaelligGruppenVerteilenZufaellig(
					final @NotNull KlausurblockungNachschreiberAlgorithmusBewertung bewertung,
					final @NotNull List<@NotNull GostKlausurtermin> termine,
					final @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTermin>> nachschreiberGruppen,
					final @NotNull GostKursklausurManager klausurManager) {

		// Zum Sammeln der Ergebnisse.
		final @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> ergebnis = new ArrayList<>();

		// Kopiere Gruppen, da die Liste schrittweise zerstört wird.
		final @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTermin>> gruppen = new ArrayList<>(nachschreiberGruppen);

		// Verteile pro Termin möglichst viele Gruppen.
		for (final @NotNull GostKlausurtermin termin : ListUtils.getCopyPermuted(termine, _random)) {
			final int gruppenanzahl = gruppen.size();
			_verteileMoeglichstVieleGruppenZufaelligAufDenTermin(termin.id, klausurManager, gruppen, ergebnis);
			if (gruppen.size() < gruppenanzahl)
				bewertung.anzahl_termine++;
		}

		// Verteile pro Fake-Termin möglichst viele Gruppen.
		long fakeID = -1;
		while (!gruppen.isEmpty()) {
			_verteileMoeglichstVieleGruppenZufaelligAufDenTermin(fakeID, klausurManager, gruppen, ergebnis);
			fakeID--;
			bewertung.anzahl_zusatztermine++;
		}

		return ergebnis;
	}

	private static void _verteileMoeglichstVieleGruppenZufaelligAufDenTermin(
			final long idTermin,
			final @NotNull GostKursklausurManager klausurManager,
			final @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTermin>> gruppen,
			final @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> ergebnis) {

		// Sammle Schüler-IDs des Termins.
		final HashSet<@NotNull Long> schuelerIDsDesTermin = new HashSet<>();
		if (idTermin >= 0) {
			for (final @NotNull GostSchuelerklausur sk : klausurManager.schuelerklausurGetMengeByTerminid(idTermin))
				schuelerIDsDesTermin.add(sk.idSchueler);
		}

		// Gehe die Gruppen in zufälliger Reihenfolge durch und versuche die Gruppe auf den den Termin zu verteilen.
		for (final @NotNull List<@NotNull GostSchuelerklausurTermin> gruppe : ListUtils.getCopyPermuted(gruppen, _random)) {
			// Sammle Schüler-IDs der Gruppe.
			boolean kollision = false;
			final @NotNull List<@NotNull Long> schuelerIDsDerGruppe = new ArrayList<>();
			for (final @NotNull GostSchuelerklausurTermin skt : gruppe) {
				final @NotNull GostSchuelerklausur sk = klausurManager.schuelerklausurBySchuelerklausurtermin(skt);
				schuelerIDsDerGruppe.add(sk.idSchueler);
				kollision |= schuelerIDsDesTermin.contains(sk.idSchueler);
			}

			// Füge die Gruppe hinzu, falls es keine Kollision gab.
			if (!kollision) {
				// Ergebnis erzeugen
				for (final @NotNull GostSchuelerklausurTermin skt : gruppe)
					ergebnis.add(new Pair<>(skt, idTermin));

				// Schüler-IDs des Termins ergänzen
				schuelerIDsDesTermin.addAll(schuelerIDsDerGruppe);

				// Gruppe aus Gruppen entfernen.
				gruppen.remove(gruppe);
			}
		}

	}

}
