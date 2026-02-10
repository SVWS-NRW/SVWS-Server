package de.svws_nrw.core.abschluss.bk.d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelFach;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturFachbelegungHalbjahr;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse verwaltet die Fachbelegungen eines Schülers im beruflichen Gymnasium
 */
public class BKGymFachbelegungZuStundentafelfachManager {

	/** Der Abiturdaten-Manager */
	private final @NotNull BKGymAbiturdatenManager abidatenManager;

	// Datenstrukturen zum schnellen Zugriff auf Fachbelegungen

	/** Eine HashMap2D für den schnelle Zugriff auf die Halbjahresbelegungen anhand des Halbjahres und der Fachbezeichnung */
	private final @NotNull HashMap2D<@NotNull String, @NotNull Integer, BKGymAbiturFachbelegungHalbjahr>
		mapBelegungByHalbjahrAndFachbezeichung;

	/** Eine HashMap2D für den schnelle Zugriff auf die Halbjahresbelegungen anhand des Halbjahres und der Fachbezeichnung */
	private final @NotNull HashMap2D<@NotNull String, @NotNull Integer, BKGymAbiturFachbelegungHalbjahr>
		mapUsedBelegungByHalbjahrAndFachbezeichung = new HashMap2D<>();

	/** Ein Array, das die belegten Fächer je Halbjahr der Qualifikationsphase speichert. */
	private final @NotNull int[] @NotNull [] belegteStundenByHalbjahrUndFachposition;


	/**
	 * Erstellt einen neuen Fachbelegungs-Manager für die Fachbelegungen eines Schülers im beruflichen Gymnasium
	 *
	 * @param fachbelegungManager                      der Manager für eine Stundentafelvariante
	 * @param mapBelegungByHalbjahrAndFachbezeichung   eine HashMap2D für den schnellen Zugriff auf die Halbjahresbelegungen
	 * @param abidatenManager
	 * @param maxFachposition
	 */
	public BKGymFachbelegungZuStundentafelfachManager(final @NotNull BKGymFachbelegungManager fachbelegungManager,
			final @NotNull HashMap2D<@NotNull String, @NotNull Integer, @NotNull BKGymAbiturFachbelegungHalbjahr> mapBelegungByHalbjahrAndFachbezeichung,
			final @NotNull BKGymAbiturdatenManager abidatenManager, final int maxFachposition) {
		this.abidatenManager = abidatenManager;
		this.mapBelegungByHalbjahrAndFachbezeichung = mapBelegungByHalbjahrAndFachbezeichung;
		this.belegteStundenByHalbjahrUndFachposition = new int[maxFachposition + 1][GostHalbjahr.maxHalbjahre];
	}


	/**
	 * Gibt die Anzahl der belegten Stunden für eine Fachposition und Halbjahr zurück
	 *
	 * @param hj     das Halbjahr
	 * @param fach   die Fach der Stundentafel
	 *
	 * @return 0, wenn nicht belegt sonst > 0, wenn belegt
	 */
	public int getBelegteStundenByHalbjahrAndFach(final @NotNull GostHalbjahr hj, final @NotNull BeruflichesGymnasiumStundentafelFach fach) {
		return belegteStundenByHalbjahrUndFachposition[fach.sortierung][hj.id];
	}


	/**
	 * Gibt zurück, ob ein Fach in einem Halbjahr schriftlich belegt wurde.
	 * Falls es nicht belegt wurde, wird trotzdem true geliefert, damit nicht
	 * zum Fehler des Faches noch die nicht erfüllte Schriftlichkeit ausgegeben wird.
	 *
	 * @param hj
	 * @param fach
	 *
	 * @return true, wenn schriftlich belegt oder keine Belegung vorliegt, sonst false
	 */
	public boolean getSchriftlichBelegt(final @NotNull GostHalbjahr hj, final @NotNull BeruflichesGymnasiumStundentafelFach fach) {
		BKGymAbiturFachbelegungHalbjahr belegungHj = mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
		if (belegungHj == null)
			belegungHj = mapUsedBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
		return (belegungHj == null) || belegungHj.schriftlich;
	}


	/**
	 * Belegt das angegebene Fach mit den vorhandenen Belegungen in der Qualifikationsphase.
	 *
	 * @param fach   die Fach der Stundentafel
	 */
	public void belegeFach(final @NotNull BeruflichesGymnasiumStundentafelFach fach) {
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
			final BKGymAbiturFachbelegungHalbjahr belegungHj = mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
			if (belegungHj != null) {
				final @NotNull Note note = Note.fromKuerzel(belegungHj.notenkuerzel);
				belegteStundenByHalbjahrUndFachposition[fach.sortierung][hj.id] = giltNoteFuerBelegung(note) ? belegungHj.wochenstunden : -1;
				mapBelegungByHalbjahrAndFachbezeichung.removeOrException(fach.fachbezeichnung, hj.id);
				mapUsedBelegungByHalbjahrAndFachbezeichung.put(fach.fachbezeichnung, hj.id, belegungHj);
			}
		}
	}


	/**
	 * Prüft ob die eingetragene Note zur Belegung führt.
	 * Eine Belegung mit ungenügend wird auch eingetragen.
	 * Das wird in der Markierung geprüft.
	 *
	 * @param note   die zu prüfende Note
	 *
	 * @return true, wenn die Note zu einer Belegung führt.
	 */
	private static boolean giltNoteFuerBelegung(final @NotNull Note note) {
		return switch (note) {
			case KEINE,
				 ABGEMELDET,
				 NICHT_ERTEILT,
				 NICHT_TEILGENOMMEN -> false;
			default -> true;
		};
	}


	/**
	 * Belegt ein Ersatzfach. Es wird das Fach nur dann belegt, wenn der Stundenumfang auch abgedeckt wird.
	 * Für den Fall, dass ein Halbjahr schon mit dem originalen Fach belegt ist, wird es nicht erneut belegt.
	 *
	 * @param fach   das zu belegende Fach
	 *
	 * @return true, wenn das Fach in den freien Halbjahren mit komplettem Umfang belegt wird.
	 */
	public boolean belegeErsatzfach(final @NotNull BeruflichesGymnasiumStundentafelFach fach) {
		// prüfe, ob vollständig Belegung möglich
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
			final BKGymAbiturFachbelegungHalbjahr belegungHj = mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
			if ((getBelegteStundenByHalbjahrAndFach(hj, fach) <= 0)
					&& ((belegungHj == null) || (belegungHj.wochenstunden < fach.stundenumfang[hj.id]) || !giltNoteFuerBelegung(Note.fromKuerzel(belegungHj.notenkuerzel))))
				return false;
		}
		// Belegung der fehlenden Halbjahre
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase()) {
			final BKGymAbiturFachbelegungHalbjahr belegungHj = mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
			if ((getBelegteStundenByHalbjahrAndFach(hj, fach) <= 0) && (belegungHj != null)) {
					belegteStundenByHalbjahrUndFachposition[fach.sortierung][hj.id] = belegungHj.wochenstunden;
					mapBelegungByHalbjahrAndFachbezeichung.removeOrException(fach.fachbezeichnung, hj.id);
					mapUsedBelegungByHalbjahrAndFachbezeichung.put(fach.fachbezeichnung, hj.id, belegungHj);
			}
		}
		return true;
	}


	/**
	 * Belegt ein Ersatzfach. Es wird das Fach nur dann belegt, wenn der Stundenumfang auch abgedeckt wird.
	 * Für den Fall, dass ein Halbjahr schon mit dem originalen Fach belegt ist, wird es nicht erneut belegt.
	 * Hinzu kommt, dass nur dann belegt wird, wenn nicht noch eine Belegung des regulären Faches folgt.
	 *
	 * @param fach   das zu belegende Fach
	 *
	 * @return true, wenn das Fach in den freien Halbjahren mit komplettem Umfang belegt wird.
	 */
	public boolean belegeErsatzfachVomEndeHer(final @NotNull BeruflichesGymnasiumStundentafelFach fach) {
		final @NotNull List<GostHalbjahr> hjeReversed = Arrays.asList(GostHalbjahr.getQualifikationsphase());
		Collections.reverse(hjeReversed);
		// prüfe, ob vollständig Belegung möglich
		for (final @NotNull GostHalbjahr hj : hjeReversed) {
			final BKGymAbiturFachbelegungHalbjahr belegungHj = mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
			if (getBelegteStundenByHalbjahrAndFach(hj, fach) > 0)
				break;
			if ((belegungHj == null) || (belegungHj.wochenstunden < fach.stundenumfang[hj.id]) || !giltNoteFuerBelegung(Note.fromKuerzel(belegungHj.notenkuerzel)))
				return false;
		}
		// Belegung der fehlenden Halbjahre
		for (final @NotNull GostHalbjahr hj : hjeReversed) {
			final BKGymAbiturFachbelegungHalbjahr belegungHj = mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fach.fachbezeichnung, hj.id);
			if (getBelegteStundenByHalbjahrAndFach(hj, fach) > 0)
				break;
			if (belegungHj != null) {
				belegteStundenByHalbjahrUndFachposition[fach.sortierung][hj.id] = belegungHj.wochenstunden;
				mapBelegungByHalbjahrAndFachbezeichung.removeOrException(fach.fachbezeichnung, hj.id);
				mapUsedBelegungByHalbjahrAndFachbezeichung.put(fach.fachbezeichnung, hj.id, belegungHj);
			}
		}
		return true;
	}


	/**
	 * Belegt ein beliebiges Fach für ein Halbjahr.
	 * Die Bezeichnung des Fachs ist dabei egal. Es wird der Position zugeordet.
	 * Die Methode wird für die Belegung des Wahlfachs verwendet
	 *
	 * @param hj     das Halbjahr
	 * @param fach   die Fach der Stundentafel der zweiten Fremdsprache
	 */
	public void belegeBeliebigesFachFuerHalbjahr(final @NotNull GostHalbjahr hj, final @NotNull BeruflichesGymnasiumStundentafelFach fach) {
		for (final @NotNull String fachbezeichnung : getFachbezeichnungenFreierBelegungen()) {
			final BKGymAbiturFachbelegungHalbjahr belegungHj = mapBelegungByHalbjahrAndFachbezeichung.getOrNull(fachbezeichnung, hj.id);
			if (belegungHj != null) {
				final @NotNull Note note = Note.fromKuerzel(belegungHj.notenkuerzel);
				if (giltNoteFuerBelegung(note) && (fach.stundenumfang[hj.id] <= belegungHj.wochenstunden)) {
					belegteStundenByHalbjahrUndFachposition[fach.sortierung][hj.id] = belegungHj.wochenstunden;
					mapBelegungByHalbjahrAndFachbezeichung.removeOrException(fachbezeichnung, hj.id);
					mapUsedBelegungByHalbjahrAndFachbezeichung.put(fach.fachbezeichnung, hj.id, belegungHj);
					return;
				}
			}
		}
	}


	/**
	 * Prüft ob ein Fach voll belegt ist in der Qualifikationsphase
	 *
	 * @param fach   das zu prüfende Fach
	 *
	 * @return true, wenn die Belegung vollständig auch im Stundenumfang ist.
	 */
	public boolean istVollbelegt(final @NotNull BeruflichesGymnasiumStundentafelFach fach) {
		for (final @NotNull GostHalbjahr hj : GostHalbjahr.getQualifikationsphase())
			if (getBelegteStundenByHalbjahrAndFach(hj, fach) < fach.stundenumfang[hj.id])
				return false;
		return true;
	}


	/**
	 * Liefert alle noch verbliebenen Fachbezeichnungen
	 *
	 * @return die verbleibenden Fachbezeichnungen
	 */
	public @NotNull List<String> getFachbezeichnungenFreierBelegungen() {
		return new ArrayList<>(mapBelegungByHalbjahrAndFachbezeichung.getKeySet());
	}
}
