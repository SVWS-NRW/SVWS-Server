package de.nrw.schule.svws.core.abschluss.gost.belegpruefung;

import java.util.List;

import de.nrw.schule.svws.core.abschluss.gost.AbiturdatenManager;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungsArt;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegungsfehler;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegung;
import de.nrw.schule.svws.core.types.gost.GostFachbereich;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw. 
 * für die Gesamtprüfungen, welche im Bereich des literarisch-künstlerischen Bereichs 
 * durchgeführt werden.
 */
public class LiterarischKuenstlerisch extends GostBelegpruefung {

	/// Die Belegungen für Kunst und Musik 
	private List<@NotNull AbiturFachbelegung> kunst_musik;
	
	/// Die Belegungen für die Ersatzfächer aus dem literarisch-künstlerischen Bereich 
	private List<@NotNull AbiturFachbelegung> kunst_musik_ersatz;

	
	/**
	 * Erstellt eine neue Belegprüfung für den literarisch-künstlerischen Bereich.
	 * 
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public LiterarischKuenstlerisch(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungs_art) {
		super(manager, pruefungs_art);
	}
	
	
	@Override
	protected void init() {
		kunst_musik = manager.getFachbelegungen(GostFachbereich.KUNST_MUSIK);
		kunst_musik_ersatz = manager.getFachbelegungen(GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ);
	}


	@Override
	protected void pruefeEF1() {
		// EF1-Prüfung Punkt 3: Prüfe, ob ein Kurs in Kunst oder Musik in EF.1 belegt wurde
		if (manager.zaehleBelegungInHalbjahren(kunst_musik, GostHalbjahr.EF1) == 0)
			addFehler(GostBelegungsfehler.KU_MU_10);
	}


	/**
	 * Gesamtprüfung Punkte 26-28:
	 * Prüfe, ob ein Kurs in Kunst oder Musik mindestens von EF.1 bis Q1.2 belegt wurde 
	 *   oder ob ein Ersatzfach (Literatur, vokal- oder instrumentalpraktischer Grundkurs) in der 
	 *           Qualifikationsphase gültig belegt wurde
	 */
	@Override
	protected void pruefeGesamt() {
		// Prüfe, ob ein Ersatzfach für Kunst oder Musik belegt wurde 
		//    und ob dieses Ersatzfach in genau zwei aufeinander folgenden Halbjahren der Qualifikationsphase belegt wurde
		boolean hatKuMuErsatz = false;
		if (kunst_musik_ersatz != null) {
			for (final AbiturFachbelegung fach : kunst_musik_ersatz) {
				if ((manager.zaehleBelegung(fach) == 2) && (manager.pruefeBelegung(fach, GostHalbjahr.Q11, GostHalbjahr.Q12)
												 	    || manager.pruefeBelegung(fach, GostHalbjahr.Q12, GostHalbjahr.Q21)
													    || manager.pruefeBelegung(fach, GostHalbjahr.Q21, GostHalbjahr.Q22))) {
					hatKuMuErsatz = true;
				} else if (manager.zaehleBelegung(fach) > 0) {
					addFehler(GostBelegungsfehler.LI_IV_10);
				}
			}
	
			// Prüfe, ob mehrere Ersatzfächer gewählt wurden. Dies ist nicht zulässig.
			if (kunst_musik_ersatz.size() > 1)
				addFehler(GostBelegungsfehler.LI_IV_11);
		}
		
		// Prüfe, ob Kunst oder Musik bis Ende Q1.2 belegt wurde oder zumindest bis Ende EF.2, dann aber in Kombination mit der Wahl eines Ersatzfaches
		final boolean hatKuMuBisQ12 = manager.pruefeBelegungExistiert(kunst_musik, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12);
		final boolean hatKuMuBisEF2 = manager.pruefeBelegungExistiert(kunst_musik, GostHalbjahr.EF1, GostHalbjahr.EF2); 
		if ((!hatKuMuBisEF2) || (hatKuMuBisEF2 && (!hatKuMuBisQ12) && (!hatKuMuErsatz)))
			addFehler(GostBelegungsfehler.KU_MU_10);
	}


}
