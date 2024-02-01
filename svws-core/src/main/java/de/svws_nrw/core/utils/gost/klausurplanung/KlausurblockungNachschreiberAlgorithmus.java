package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
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

	private boolean _regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen = false;

	// Könntest du noch config-optionen rein nehmen,
	// wie alle Klausuren derselben Kursart + Fachid am selben Termin
	// an die Fach-ID und Kursart kommst du mit
	// klausurManager.vorgabeBySchuelerklausurTermin(skt).idFach / kursart
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
	 * Aktiviert/Deaktiviert die Regel. Falls TRUE, werden NachschreiberInnen der selben Klausur auf den selben Termin geblockt.
	 *
	 * @param b falls TRUE, wird die Regel angewandt.
	 */
	public void set_regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen(final boolean b) {
		this._regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen = b;
	}

	/**
	 * @param nachschreiber   Alle "GostSchuelerklausurTermin", die auf die "GostKlausurtermin" verteilt werden sollen.
	 * @param termine         Alle "GostKlausurtermin", auf die potentiell Nachschreiber verteilt werden.
	 * @param klausurManager  Der Kursklausur-Manager.
	 * @param maxTimeMillis   Die maximal erlaubte Berechnungszeit (in Millisekunden).
	 *
	 * @return Eine Liste von Paaren: 1. Element = GostSchuelerklausurtermin (Nachschreiber), 2. Element = ID des Termins / der Schiene
	 */
	public @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> berechne(
					final @NotNull List<@NotNull GostSchuelerklausurTermin> nachschreiber,
					final @NotNull List<@NotNull GostKlausurtermin> termine,
					final @NotNull GostKursklausurManager klausurManager,
					final long maxTimeMillis) {

		// 1) Bilde Gruppen von Nachschreibern, falls dies bestimmte Kriterien/Regeln erfordern.
		final @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTermin>> nachschreiberGruppen = new ArrayList<>(); // Liste der Gruppen.

		for (final @NotNull GostSchuelerklausurTermin skt : nachschreiber) {
			final GostSchuelerklausur sk = klausurManager.schuelerklausurBySchuelerklausurtermin(skt);
			final long idSchueler = sk.idSchueler; // die ID des Schülers, die innerhalb jedes Klausurtermins / Schiene einzigartig sein muss
			final long idKurs = sk.idKursklausur; // die ID der Kursklausur als Gütekriterium, möglichst alle mit gleicher Kursklausur innerhalb eines Termins
			DeveloperNotificationException.ifTrue("Ungültige Schüler-ID = " + idSchueler, idSchueler < 0);
			DeveloperNotificationException.ifTrue("Ungültige Kurs-ID = " + idKurs, idKurs < 0);

			// Überprüfe, ob "skt" in eine bereits vorhandene Gruppe darf.
			boolean added = false;
			for (final @NotNull List<@NotNull GostSchuelerklausurTermin> gruppe : nachschreiberGruppen)
				if (_istHinzufuegenErlaubt(gruppe, skt, klausurManager)) {
					gruppe.add(skt);
					added = true;
					break;
				}

			// Erzeuge eine neue Gruppe für "skt".
			if (!added)
				nachschreiberGruppen.add(ListUtils.create1(skt));
		}

		// 2) Verwende derzeit nur Algorithmus1.
		final @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> ergebnis = _algorithmusProTerminGruppenVerteilen(termine, nachschreiberGruppen, klausurManager);
		return ergebnis;
	}

	private boolean _istHinzufuegenErlaubt(
					final @NotNull List<@NotNull GostSchuelerklausurTermin> gruppe,
					final @NotNull GostSchuelerklausurTermin skt1,
					final @NotNull GostKursklausurManager klausurManager) {

		final @NotNull GostSchuelerklausur sk1 = klausurManager.schuelerklausurBySchuelerklausurtermin(skt1);

		// Verbiete, dass ein Schüler doppelt in einer Gruppe ist.
		for (final @NotNull GostSchuelerklausurTermin skt2 : gruppe) {
			final @NotNull GostSchuelerklausur sk2 = klausurManager.schuelerklausurBySchuelerklausurtermin(skt2);
			if (sk1.idSchueler == sk2.idSchueler)
				return false;
		}

		// Sollen Schüler der selben Kurs-Klausur in der selben Gruppe landen?
		if (_regel_nachschreiber_der_selben_klausur_auf_selbe_termine_verteilen) {
			final @NotNull GostSchuelerklausurTermin skt2 = ListUtils.getNonNullElementAtOrException(gruppe, 0);
			final @NotNull GostSchuelerklausur sk2 = klausurManager.schuelerklausurBySchuelerklausurtermin(skt2);
			return (sk1.idKursklausur == sk2.idKursklausur);
		}

		// Ein Hinzufügen zu dieser Gruppe ist nicht erlaubt.
		return false;
	}

	private static @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> _algorithmusProTerminGruppenVerteilen(
					final @NotNull List<@NotNull GostKlausurtermin> termine,
					final @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTermin>> nachschreiberGruppen,
					final @NotNull GostKursklausurManager klausurManager) {

		// Zum Sammeln der Ergebnisse.
		final @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> ergebnis = new ArrayList<>();

		// Kopiere Gruppen, da die Liste schrittweise zerstört wird.
		final @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTermin>> gruppen = new ArrayList<>(nachschreiberGruppen);

		// Verteile pro Termin möglichst viele Gruppen.
		for (final @NotNull GostKlausurtermin termin : termine)
			_verteileMoeglichstVieleGruppenAufDenTermin(termin.id, klausurManager, gruppen, ergebnis);

		// Verteile pro Fake-Termin möglichst viele Gruppen.
		long fakeID = -1;
		while (!gruppen.isEmpty()) {
			_verteileMoeglichstVieleGruppenAufDenTermin(fakeID, klausurManager, gruppen, ergebnis);
			fakeID--;
		}

		return ergebnis;
	}

	private static void _verteileMoeglichstVieleGruppenAufDenTermin(final long idTermin,
			final @NotNull GostKursklausurManager klausurManager,
			final @NotNull List<@NotNull List<@NotNull GostSchuelerklausurTermin>> gruppen,
			final @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> ergebnis) {

		// Sammle Schüler_IDs des Termins.
		final HashSet<@NotNull Long> schuelerIDsDesTermin = new HashSet<>();
		if (idTermin >= 0) {
			for (final @NotNull GostSchuelerklausur sk : klausurManager.schuelerklausurGetMengeByTerminid(idTermin))
				schuelerIDsDesTermin.add(sk.idSchueler);
		}

		// Verteile die jeweilige Gruppe auf den Termin, falls es keine Kollision gibt.
		final Iterator<@NotNull List<@NotNull GostSchuelerklausurTermin>> i = gruppen.iterator();
		while (i.hasNext()) {
			final @NotNull List<@NotNull GostSchuelerklausurTermin> gruppe = i.next();
			boolean kollision = false;

			// SammleIDs
			final @NotNull List<@NotNull Long> schuelerIDsDerGruppe = new ArrayList<>();
			for (final @NotNull GostSchuelerklausurTermin skt : gruppe) {
				final @NotNull GostSchuelerklausur sk = klausurManager.schuelerklausurBySchuelerklausurtermin(skt);
				schuelerIDsDerGruppe.add(sk.idSchueler);
				kollision |= schuelerIDsDesTermin.contains(sk.idSchueler);
			}

			// Füge die Gruppe hinzu, falls es keine Kollision gab.
			if (!kollision) {
				// GostSchuelerklausurTermin --> TerminID
				for (final @NotNull GostSchuelerklausurTermin skt : gruppe)
					ergebnis.add(new Pair<>(skt, idTermin));
				// "schuelerIDsDerGruppe" der "schuelerIDsDesTermin" hinzufügen.
				schuelerIDsDesTermin.addAll(schuelerIDsDerGruppe);
				i.remove();
			}
		}

	}

}
