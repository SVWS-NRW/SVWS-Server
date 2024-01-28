package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.kursblockung.KursblockungMatrix;
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
				if (istHinzufuegenErlaubt(gruppe, skt, klausurManager)) {
					gruppe.add(skt);
					added = true;
					break;
				}

			// Erzeuge eine neue Gruppe für "skt".
			if (!added)
				nachschreiberGruppen.add(ListUtils.create1(skt));
		}


		// 2) Matrix für den Algorithmus erzeugen (Gruppe r wird Termin c zugeordnet).
		final int rows = nachschreiberGruppen.size();
		final int cols = termine.size();
		final @NotNull KursblockungMatrix matrix = new KursblockungMatrix(_random, rows, cols);
		final @NotNull long @NotNull [][] data = matrix.getMatrix();
		for (int r = 0; r < nachschreiberGruppen.size(); r++)
			for (int c = 0; c < termine.size(); c++)
				data[r][c] = gibBewertung(nachschreiberGruppen.get(r), termine.get(c), klausurManager); // Darf Gruppe r dem Termin c zugeordnet werden?


		// 3) Optimale Zuordnung berechnen.
		final int[] r2c = matrix.gibMaximalesBipartitesMatching(true);


		// 4) Ergebnis erzeugen.
		final @NotNull List<@NotNull Pair<@NotNull GostSchuelerklausurTermin, @NotNull Long>> ergebnis = new ArrayList<>();

		long dummyTerminID = -1;
		for (int r = 0; r < nachschreiberGruppen.size(); r++) {
			// Bestimme, ob die ID von einem echten Termin kommt, oder eine Dummy-ID verwendet wird.
			long idTermin = dummyTerminID;

			final int c = r2c[r];
			if (c >= 0) {
				idTermin = termine.get(c).id;
			} else {
				dummyTerminID--;
			}

			// Verteile alle "GostSchuelerklausurTermin" der Gruppe auf den Termin.
			for (final @NotNull GostSchuelerklausurTermin nachschreiberDerGruppe : nachschreiberGruppen.get(r))
				ergebnis.add(new Pair<>(nachschreiberDerGruppe, idTermin));
		}

		return ergebnis;
	}

	private static long gibBewertung(
					final @NotNull List<@NotNull GostSchuelerklausurTermin> nachschreiberGruppe,
					final @NotNull GostKlausurtermin termin,
					final @NotNull GostKursklausurManager klausurManager) {

		for (final @NotNull GostSchuelerklausur sk1 : klausurManager.schuelerklausurGetMengeByTerminid(termin.id)) { // Alle Schüler des Termins...
			for (final @NotNull GostSchuelerklausurTermin skt : nachschreiberGruppe) { // Alle Schüler der Gruppe ...
				final GostSchuelerklausur sk2 = klausurManager.schuelerklausurBySchuelerklausurtermin(skt);
				if (sk1.idSchueler == sk2.idSchueler)
					return 0; // Die Gruppe darf nicht an dem Termin liegen.
			}
		}

		return 1;  // Die Gruppe darf an dem Termin liegen.
	}

	private boolean istHinzufuegenErlaubt(
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

}
