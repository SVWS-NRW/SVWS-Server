package de.svws_nrw.core.utils.gost;

import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.asd.types.Note;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostZulassungSimulation;
import de.svws_nrw.core.data.gost.GostZulassungSimulationAbiturfach;
import de.svws_nrw.core.data.gost.GostZulassungSimulationNote;
import de.svws_nrw.core.types.gost.GostBesondereLernleistung;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden zur Verfügung, um Abiturdaten mit den Eingaben einer Simulation der Abiturzulassung zu kombinieren
 * und damit die Zulassung und das Prüfungsergebnis zu berechnen. Die Methoden verändern die übergebenen Abiturdaten.
 */
public final class GostZulassungSimulationUtils {

	private GostZulassungSimulationUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	private static @NotNull Map<Long, AbiturFachbelegung> getMapFaecher(final @NotNull Abiturdaten daten) {
		final @NotNull Map<Long, AbiturFachbelegung> mapFaecher = new HashMap<>();
		for (final AbiturFachbelegung fach : daten.fachbelegungen) {
			mapFaecher.put(fach.fachID, fach);
		}
		return mapFaecher;
	}


	private static String getKuerzel(final Integer notenpunkte, final int schuljahr) {
		final @NotNull Note note = Note.fromNotenpunkte(notenpunkte);
		return (note == Note.KEINE) ? null : note.getNoteKuerzel(schuljahr);
	}


	/**
	 * Entfernt die leeren Belegungen, die der AbiturdatenManager beim Lesen der Fachwahlen für Halbjahre ohne Belegung anlegt.
	 * Sie würden sonst als Belegung gewertet.
	 *
	 * @param daten   die Abiturdaten
	 */
	private static void entferneLeereBelegungen(final @NotNull Abiturdaten daten) {
		for (final AbiturFachbelegung fach : daten.fachbelegungen) {
			for (int i = 0; i < fach.belegungen.length; i++) {
				final AbiturFachbelegungHalbjahr belegung = fach.belegungen[i];
				if ((belegung != null) && "".equals(belegung.kursartKuerzel)) {
					fach.belegungen[i] = null;
				}
			}
		}
	}


	private static void setzeBlock1Zurueck(final @NotNull Abiturdaten daten) {
		for (final AbiturFachbelegung fach : daten.fachbelegungen) {
			for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
				final AbiturFachbelegungHalbjahr belegung = fach.belegungen[halbjahr.id];
				if (belegung != null) {
					belegung.block1gewertet = false;
				}
			}
		}
		daten.block1AnzahlKurse = null;
		daten.block1DefiziteGesamt = null;
		daten.block1DefiziteLK = null;
		daten.block1PunktSummeGK = null;
		daten.block1PunktSummeLK = null;
		daten.block1PunktSummeNormiert = null;
		daten.block1NotenpunkteDurchschnitt = null;
		daten.block1Zulassung = null;
	}


	/**
	 * Gibt zurück, ob die Note einer Halbjahresbelegung nicht durch eine Eingabe der Simulation verändert werden darf. Dies gilt immer
	 * für Vertiefungskurse, da sie keine Notenpunkte haben, und für Pseudonoten, damit diese erhalten bleiben. Andere vorhandene Noten
	 * sind nur gesperrt, wenn sie fixiert sind.
	 *
	 * @param faecher        der Manager für die Fächer des Abiturjahrgangs
	 * @param idFach         die ID des Faches
	 * @param belegung       die Halbjahresbelegung
	 * @param schuljahr      das Schuljahr, in welchem die Abiturprüfung stattfindet
	 * @param fixiereNoten   gibt an, ob vorhandene Noten Vorrang vor den Eingaben haben
	 *
	 * @return true, wenn die Note nicht verändert werden darf, und ansonsten false
	 */
	public static boolean istEingabeGesperrt(final @NotNull GostFaecherManager faecher, final long idFach, final @NotNull AbiturFachbelegungHalbjahr belegung,
			final int schuljahr, final boolean fixiereNoten) {
		if (faecher.fachIstVertiefungskurs(idFach)) {
			return true;
		}
		final @NotNull Note note = Note.fromKuerzel(belegung.notenkuerzel);
		return (note != Note.KEINE) && (fixiereNoten || !note.istNote(schuljahr));
	}


	/**
	 * Wendet die Eingaben der Simulation auf die Abiturdaten an. Alle Halbjahre der Qualifikationsphase gelten danach als bewertet,
	 * damit die Zulassung berechnet wird. Einträge ohne passende Belegung, auf gesperrten Noten und Noten außerhalb des Katalogs bleiben
	 * unberücksichtigt.
	 * Vorhandene Prüfungsdaten werden zurückgesetzt: Die besondere Lernleistung wird immer durch die Eingabe ersetzt, ebenso alle
	 * Prüfungswerte eines Faches, zu dem ein Eintrag vorliegt.
	 *
	 * @param daten          die Abiturdaten, welche verändert werden
	 * @param simulation     die Eingaben der Simulation
	 * @param faecher        der Manager für die Fächer des Abiturjahrgangs
	 * @param fixiereNoten   gibt an, ob vorhandene Noten Vorrang vor den Eingaben haben
	 */
	public static void combine(final @NotNull Abiturdaten daten, final @NotNull GostZulassungSimulation simulation, final @NotNull GostFaecherManager faecher,
			final boolean fixiereNoten) {
		entferneLeereBelegungen(daten);
		final @NotNull Map<Long, AbiturFachbelegung> mapFaecher = getMapFaecher(daten);
		for (final @NotNull GostHalbjahr halbjahr : GostHalbjahr.getQualifikationsphase()) {
			daten.bewertetesHalbjahr[halbjahr.id] = true;
		}
		for (final @NotNull GostZulassungSimulationNote eintrag : simulation.noten) {
			final GostHalbjahr halbjahr = GostHalbjahr.fromID(eintrag.halbjahr);
			final AbiturFachbelegung fach = mapFaecher.get(eintrag.idFach);
			if ((halbjahr == null) || !halbjahr.istQualifikationsphase() || (fach == null)) {
				continue;
			}
			// Das bewertete Halbjahr reicht für die Fixierung nicht aus, da auch das laufende Halbjahr meist als bewertet gilt, obwohl die Noten noch fehlen
			final AbiturFachbelegungHalbjahr belegung = fach.belegungen[halbjahr.id];
			if ((belegung == null) || istEingabeGesperrt(faecher, fach.fachID, belegung, daten.schuljahrAbitur, fixiereNoten)) {
				continue;
			}
			final String kuerzel = getKuerzel(eintrag.notenpunkte, daten.schuljahrAbitur);
			if ((kuerzel != null) || (eintrag.notenpunkte == null)) {
				belegung.notenkuerzel = kuerzel;
			}
		}
		for (final @NotNull GostZulassungSimulationAbiturfach eintrag : simulation.abiturfaecher) {
			final AbiturFachbelegung fach = mapFaecher.get(eintrag.idFach);
			if ((fach == null) || (fach.abiturFach == null)) {
				continue;
			}
			fach.block2NotenKuerzelPruefung = getKuerzel(eintrag.pruefungNotenpunkte, daten.schuljahrAbitur);
			fach.block2MuendlichePruefungBestehen = eintrag.muendlichBestehen;
			fach.block2MuendlichePruefungFreiwillig = eintrag.muendlichFreiwillig;
			fach.block2MuendlichePruefungReihenfolge = eintrag.muendlichReihenfolge;
			fach.block2MuendlichePruefungNotenKuerzel = getKuerzel(eintrag.muendlichNotenpunkte, daten.schuljahrAbitur);
		}
		daten.besondereLernleistung = GostBesondereLernleistung.fromKuerzel(simulation.besondereLernleistung).kuerzel;
		daten.besondereLernleistungNotenKuerzel = getKuerzel(simulation.besondereLernleistungNotenpunkte, daten.schuljahrAbitur);
	}


	/**
	 * Berechnet Zulassung und Prüfungsergebnis für Abiturdaten, auf welche die Eingaben bereits angewendet wurden. Markierungen aus
	 * der Planung werden verworfen und neu bestimmt. Bilinguale Belegungen werden dabei zusammengefasst, daher werden die Abiturdaten
	 * vom Manager verändert.
	 *
	 * @param daten                      die Abiturdaten, welche verändert werden
	 * @param jahrgangsdaten             die Daten des Abiturjahrgangs
	 * @param faecher                    der Manager für die Fächer des Abiturjahrgangs
	 * @param refreshPflichtpruefungen   gibt an, ob die mündlichen Pflichtprüfungen neu bestimmt werden. Dies überschreibt die Eingaben dazu.
	 *
	 * @return der Manager mit dem Ergebnis
	 */
	public static @NotNull AbiturdatenManager berechne(final @NotNull Abiturdaten daten, final GostJahrgangsdaten jahrgangsdaten,
			final @NotNull GostFaecherManager faecher, final boolean refreshPflichtpruefungen) {
		setzeBlock1Zurueck(daten);
		final @NotNull AbiturdatenManager manager = new AbiturdatenManager(daten, jahrgangsdaten, faecher, GostBelegpruefungsArt.GESAMT);
		manager.kombiniereFachbelegungenEinesFaches();
		manager.applyErgebnisMarkierungsalgorithmus();
		AbiturdatenManager.berechnePruefungsergebnis(daten, refreshPflichtpruefungen);
		return manager;
	}

}
