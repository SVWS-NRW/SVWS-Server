package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

import java.util.List;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
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
 * für die Gesamtprüfungen, welche im Bereich des literarisch-künstlerischen Bereichs
 * durchgeführt werden.
 */
public final class Abi30BelegpruefungLiterarischKuenstlerisch extends GostBelegpruefung {

	/** Die Belegungen für Kunst und Musik. */
	private List<AbiturFachbelegung> kunst_musik;

	/** Die Belegungen für die Ersatzfächer aus dem literarisch-künstlerischen Bereich. */
	private List<AbiturFachbelegung> literatur;


	/**
	 * Erstellt eine neue Belegprüfung für den literarisch-künstlerischen Bereich.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Abi30BelegpruefungLiterarischKuenstlerisch(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		kunst_musik = manager.getRelevanteFachbelegungen(GostFachbereich.KUNST_MUSIK);
		literatur = manager.getRelevanteFachbelegungen(GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ);
	}


	@Override
	protected void pruefeEF1() {
		// EF1-Prüfung Punkt 3: Prüfe, ob ein Kurs in Kunst oder Musik in EF.1 belegt wurde
		if (manager.zaehleBelegungInHalbjahren(kunst_musik, GostHalbjahr.EF1) == 0) {
			addFehler(GostBelegungsfehler.GOST30_KU_MU_10);
		}
	}


	/**
	 * Gesamtprüfung Punkte 26-28:
	 * Prüfe, ob ein Kurs in Kunst oder Musik mindestens von EF.1 bis Q1.2 belegt wurde
	 *   oder ob das Ersatzfach Literatur in der Qualifikationsphase gültig belegt wurde
	 */
	@Override
	protected void pruefeGesamt() {
		// Prüfe, ob ein Ersatzfach Literatur für Kunst oder Musik belegt wurde
		//    und ob dieses Ersatzfach Literatur in genau zwei aufeinander folgenden Halbjahren der Qualifikationsphase belegt wurde
		boolean hatLi = false;
		if (literatur != null) {
			for (final AbiturFachbelegung fach : literatur) {
				hatLi = hatLi || (manager.pruefeBelegung(fach, GostHalbjahr.Q11, GostHalbjahr.Q12)
						|| manager.pruefeBelegung(fach, GostHalbjahr.Q12, GostHalbjahr.Q21)
						|| manager.pruefeBelegung(fach, GostHalbjahr.Q21, GostHalbjahr.Q22));
			}
		}

		// Prüfe, ob Kunst oder Musik bis Ende Q1.2 belegt wurde oder zumindest bis Ende EF.2, dann aber in Kombination mit der Wahl des Ersatzfaches Literatur
		final boolean hatKuMuBisQ12 = manager.pruefeBelegungExistiert(kunst_musik, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12);
		final boolean hatKuMuBisEF2 = manager.pruefeBelegungExistiert(kunst_musik, GostHalbjahr.EF1, GostHalbjahr.EF2);
		if ((!hatKuMuBisEF2) || ((!hatKuMuBisQ12) && (!hatLi))) {
			addFehler(GostBelegungsfehler.GOST30_KU_MU_10);
		}
	}


}
