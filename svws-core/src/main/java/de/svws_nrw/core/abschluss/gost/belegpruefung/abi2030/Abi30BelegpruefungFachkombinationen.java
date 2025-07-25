package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp;
import jakarta.validation.constraints.NotNull;

/*
 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
 * umgesetzt werden.
 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
 * erlaubt.
 */
/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche in Bezug auf die schulspezifischen Fachkombinationen durchgeführt werden.
 */
public final class Abi30BelegpruefungFachkombinationen extends GostBelegpruefung {

	/**
	 * Erstellt eine neue Belegprüfung für die schulspezifischen Fachkombinationen.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Abi30BelegpruefungFachkombinationen(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		// mache nichts...
	}


	private static boolean pruefeHatBelegungFach2InHalbjahr(final @NotNull GostJahrgangFachkombination kombi, final AbiturFachbelegung belegung2,
			final @NotNull GostHalbjahr halbjahr) {
		if (belegung2 == null)
			return false;
		final AbiturFachbelegungHalbjahr belegung2Halbjahr = belegung2.belegungen[halbjahr.id];
		return ((belegung2Halbjahr != null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegung2Halbjahr))
				&& ((kombi.kursart2 == null) || (GostKursart.fromKuerzel(belegung2Halbjahr.kursartKuerzel) == GostKursart.fromKuerzel(kombi.kursart2))));
	}


	private void pruefeHatFachkombination(final @NotNull GostJahrgangFachkombination kombi, final @NotNull GostHalbjahr... halbjahre) {
		// Prüfe, ob das erste fach überhaupt belegt ist und die Fachkombination "aktivieren" kann
		final AbiturFachbelegung belegung1 = manager.getFachbelegungByID(kombi.fachID1);
		if (belegung1 == null)
			return;
		// Prüfe nun über alle Halbjahre, ob eine Halbjahresbelegung mit der Kursart die Fachkombination aktiviert und diese ggf. zu einem Fehler führt
		final AbiturFachbelegung belegung2 = manager.getFachbelegungByID(kombi.fachID2);
		for (final @NotNull GostHalbjahr halbjahr : halbjahre) {
			// Prüfe, ob die Regel in dem Halbjahr überhaupt gültig ist
			if (!kombi.gueltigInHalbjahr[halbjahr.id])
				continue;
			// Prüfe die Belegungen
			final AbiturFachbelegungHalbjahr belegung1Halbjahr = belegung1.belegungen[halbjahr.id];
			if ((belegung1Halbjahr == null) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegung1Halbjahr)))
				continue;
			if ((kombi.kursart1 == null) || (GostKursart.fromKuerzel(belegung1Halbjahr.kursartKuerzel) == GostKursart.fromKuerzel(kombi.kursart1))) {
				// Die Fachkombinations-Regel wurde durch dir Fachbelegung des ersten Faches aktiviert - Prüfe nun auf eine Regelverletzung
				if ((kombi.typ == GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue()) && pruefeHatBelegungFach2InHalbjahr(kombi, belegung2, halbjahr)) {
					addFehler(GostBelegungsfehler.KOMBI_1);
					return;
				} else if ((kombi.typ == GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue())
						&& !pruefeHatBelegungFach2InHalbjahr(kombi, belegung2, halbjahr)) {
					addFehler(GostBelegungsfehler.KOMBI_2);
					return;
				}
			}
		}
	}


	@Override
	protected void pruefeEF1() {
		for (final @NotNull GostJahrgangFachkombination kombi : manager.getFachkombinationenEF1())
			pruefeHatFachkombination(kombi, GostHalbjahr.EF1);
	}

	@Override
	protected void pruefeGesamt() {
		for (final @NotNull GostJahrgangFachkombination kombi : manager.getFachkombinationenGesamt())
			pruefeHatFachkombination(kombi, GostHalbjahr.values());
	}

}
