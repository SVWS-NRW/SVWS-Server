package de.svws_nrw.core.abschluss.gost.belegpruefung;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.abschluss.gost.GostFachManager;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der 
 * EF1 bzw. für die Gesamtprüfungen, welche im Bereich der Gesellschaftwissenschaften 
 * und Religion durchgeführt werden
 */
public class GesellschaftswissenschaftenUndReligion extends GostBelegpruefung {

	/// Die Belegungen für alle Fächer der Gesellschaftswissenschaften 
	private List<@NotNull AbiturFachbelegung> gesellschaftswissenschaften;

	/// Die Belegung des Faches Geschichte - oder null, falls es nicht belegt wurde
	private List<@NotNull AbiturFachbelegung> geschichte;
	
	/// Die Belegung des Faches Sozialwissenschaften - oder null, falls es nicht belegt wurde
	private List<@NotNull AbiturFachbelegung> sozialwissenschaften;

	/// Die Belegung des Faches Philosophie - oder null, falls es nicht belegt wurde
	private AbiturFachbelegung philosophie;
	
	/// Die Belegungen für die Fächer Gesellschaftswissenschaften außer Geschichte, Sozialwissenschaften und Philosophie
	private List<@NotNull AbiturFachbelegung> sonstige_gesellschaftswissenschaften;
	
	/// Die Belegung des Faches Religion - oder null, falls es nicht belegt wurde
	private List<@NotNull AbiturFachbelegung> religion;
	
	/// Alle Fachbelegungen bei denen Zusatzkurse in zwei aufeinanderfolgenden Halbjahren in der Qualifikationsphase belegt wurden.
	private Vector<@NotNull AbiturFachbelegung> zusatzkursFachbelegungen;
	
	
	
	/**
	 * Erstellt eine neue Belegprüfung für den Bereich der Gesellschaftswissenschaften und Religion.
	 * 
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public GesellschaftswissenschaftenUndReligion(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungs_art) {
		super(manager, pruefungs_art);
	}
	
	
	@Override
	protected void init() {
		gesellschaftswissenschaften = manager.getFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH);
		geschichte = manager.getFachbelegungen(GostFachbereich.GESCHICHTE);
		sozialwissenschaften = manager.getFachbelegungen(GostFachbereich.SOZIALWISSENSCHAFTEN);
		philosophie = manager.getFachbelegung(GostFachbereich.PHILOSOPHIE);
		sonstige_gesellschaftswissenschaften = manager.getFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE);
		religion = manager.getFachbelegungen(GostFachbereich.RELIGION);
		zusatzkursFachbelegungen = new Vector<>();
	}


	@Override
	protected void pruefeEF1() {
		// Prüfung EF1
		pruefeGesellschaftswissenschaftenEF1();
		pruefeReligionEF1();
	}


	/**
	 * EF1-Prüfung Punkte 8-10: 
	 * Prüfe, ob eine Gesellschaftswissenschaft in EF.1 schriftlich belegt wurde und durchgängig belegbar ist
	 *    und ob Geschichte belegt wurde
	 *    und ob Sozialwissenschaften belegt wurde 
	 */
	private void pruefeGesellschaftswissenschaftenEF1() {
		// Wurde eine durchgehend belegbare Gesellschaftswissenschaft in EF.1 belegt?
		if (!manager.pruefeBelegungDurchgehendBelegbarExistiert(gesellschaftswissenschaften, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.GW_10);
		// Wurde eine Gesellschaftswissenschaft in EF.1 schriftlich belegt?
		if (!manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(gesellschaftswissenschaften, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.GW_11);
		// Wurde Geschichte in EF.1 belegt? Wenn nicht, so müssen in der Qualifikationsphase zwei Zusatzkurse belegt werden.
		if (manager.zaehleBelegungInHalbjahren(geschichte, GostHalbjahr.EF1) <= 0)
			addFehler(GostBelegungsfehler.GE_1_INFO);
		// Wurde Sozialwissenschaften in EF.1 belegt? Wenn nicht, so müssen in der Qualifikationsphase zwei Zusatzkurse belegt werden.
		if (manager.zaehleBelegungInHalbjahren(sozialwissenschaften, GostHalbjahr.EF1) <= 0)
			addFehler(GostBelegungsfehler.SW_1_INFO);
	}
	
	
	/**
	 * EF1-Prüfung Punkt 11:
	 * Prüfe, ob Religion in EF.1 belegt wurde oder ob Philosophie und eine weitere durchgehend belegbare Gesellschaftswissenschaft belegt wurde.
	 * Falls Philosophie als Ersatz für Religion gewählt wurde, zählt es nicht als durchgehend belegte Gesellschaftswissenschaft.   
	 */
	private void pruefeReligionEF1() {
		// Wurde Religion in der EF.1 belegt?
		if (manager.pruefeBelegungExistiert(religion, GostHalbjahr.EF1))
			return;
		
		// Falls nicht: Philosophie und eine weitere durchgängig belegbare Gesellschaftswissenschaft müssen belegt sein
		if (!manager.pruefeBelegung(philosophie, GostHalbjahr.EF1)) {
			// Philosophie wurde nicht belegt
			addFehler(GostBelegungsfehler.RE_10);
		} else if ((!manager.pruefeBelegungDurchgehendBelegbarExistiert(geschichte, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1))
				&& (!manager.pruefeBelegungDurchgehendBelegbarExistiert(sozialwissenschaften, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1))
				&& (!manager.pruefeBelegungDurchgehendBelegbarExistiert(sonstige_gesellschaftswissenschaften, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1))) {
			// Philosophie wurde belegt - aber keine weitere Gesellschaftswissenschaft, die durchgängig belegbar ist
			addFehler(GostBelegungsfehler.RE_10);
		}
	}


	

	@Override
	protected void pruefeGesamt() {
		// Gesamtprüfung für die gesamte Oberstufe
		pruefeSchriftlichkeitEF();
		pruefeDurchgaengigeBelegung();
		pruefeDurchgaengigeBelegungUndSchriftlich();
		pruefeZusatzkurs(geschichte);
		pruefeBelegungGeschichte();
		pruefeZusatzkurs(sozialwissenschaften);
		pruefeBelegungSozialwissenschaften();
		pruefeReligionEF();
		pruefeReligionQ1();
		pruefeReligionKontinuitaet();
	}


	/**
	 * Gesamtprüfung Punkt 39:
	 * Prüfe, on in EF.1 und EF.2 jeweils ein Fach der Gesellschaftswissenschaften schriftlich belegt wurde
	 */
	private void pruefeSchriftlichkeitEF() {
		if ((!manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(gesellschaftswissenschaften, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) ||
			(!manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(gesellschaftswissenschaften, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF2)))
			addFehler(GostBelegungsfehler.GW_11);
	}
	
	
	/**
	 * Gesamtprüfung Punkt 38:
	 * Prüfe, ob ein Fach der Gesellschaftswissenschaften von EF.1 bis Q2.2 durchgängig belegt wurde 
	 * - Zusatzkurse zählen hier nicht als Belegung
	 */
	private void pruefeDurchgaengigeBelegung() {
		if (!manager.pruefeBelegungExistiert(gesellschaftswissenschaften, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			addFehler(GostBelegungsfehler.GW_10);
	}

	
	/**
	 * Gesamtprüfung Punkt 40:
	 * Prüfe, ob ein Fach der Gesellschaftswissenschaften oder Religionslehre von EF.1 bis Q2.2 belegt 
	 * und von Q1.1 bis Q2.1 schriftlich belegt wurde, damit es als potentielles Abiturfach zur Verfügung steht.
	 * - Zusatzkurse zählen hier nicht als Belegung
	 */
	private void pruefeDurchgaengigeBelegungUndSchriftlich() {
		if (manager.pruefeBelegungExistiertDurchgehendSchriftlich(gesellschaftswissenschaften))
			return;
		if (manager.pruefeBelegungExistiertDurchgehendSchriftlich(religion))
			return;
		addFehler(GostBelegungsfehler.GW_12);
	}
	
	
	/**
	 * Gesamtprüfung Punkte 27, 29 und 30:
	 * Prüft, ob der Zusatzkurs genau zwei mal belegt wurde 
	 *    und ob ein Zusatzkurs belegt wurde, obwohl im Halbjahr zuvor ein Geschichtskurs belegt wurde.
	 * 
	 * @param fachbelegungen   die Fachbelegung für Geschichte oder Sozialwissenschaften
	 */
	private void pruefeZusatzkurs(final List<@NotNull AbiturFachbelegung> fachbelegungen) {
		// Prüfe zunächst, ob das Fach überhaupt belegt wurde
		if ((fachbelegungen == null) || (fachbelegungen.size() == 0))
			return;
		// ob die Kursart "Zusatzkurs" überhaupt in einem Halbjahr gewählt wurde
		final List<@NotNull AbiturFachbelegung> fachbelegungenZK = manager.filterBelegungKursartExistiert(fachbelegungen, GostKursart.ZK);
		if (fachbelegungenZK.size() == 0)
			return;

		// Prüfe, ob mehr als eine Belegung des Zusatzkurses vorliegt (z.B. durch eine Zweitbelegung in einer bilingualen Variante des Faches)
		if (fachbelegungenZK.size() > 1)
			addFehler(GostBelegungsfehler.ZK_13);
		
		// Prüfe die gefilterten Belegungen
		for (final AbiturFachbelegung fachbelegung : fachbelegungenZK) {
			// Prüfe, ob die Belegung für den Zusatzkurs bilingual ist
			final GostFach fach = manager.getFach(fachbelegung);
			if ((fach == null) || (GostFachManager.istBilingual(fach)))
				addFehler(GostBelegungsfehler.ZK_13);
			
			// Bestimme die Halbjahre des Zusatzkurses
			final @NotNull List<@NotNull GostHalbjahr> halbjahre = manager.getHalbjahreKursart(fachbelegung, GostKursart.ZK);
			// Prüfe, ob zwei Zusatzkurse in aufeinanderfolgenden Halbjahren belegt wurden
			if (halbjahre.size() == 2) {
				if (((manager.pruefeBelegungMitKursart(fachbelegung, GostKursart.ZK, GostHalbjahr.Q11, GostHalbjahr.Q12)) 
					|| (manager.pruefeBelegungMitKursart(fachbelegung, GostKursart.ZK, GostHalbjahr.Q12, GostHalbjahr.Q21)) 
					|| (manager.pruefeBelegungMitKursart(fachbelegung, GostKursart.ZK, GostHalbjahr.Q21, GostHalbjahr.Q22)))) {
					if (zusatzkursFachbelegungen != null)
						zusatzkursFachbelegungen.add(fachbelegung);
				}
		    // Prüfe, ob mehr als zwei Zusatzkurse belegt wurden oder die Belegung in nicht aufeinander folgenden Halbjahren ist
			} else if (halbjahre.size() > 1) { 
				addFehler(GostBelegungsfehler.ZK_12);
			}
			// Prüfe, ob bei den Halbjahren des Zusatzkurses im Halbjahr davor eine Belegung vorliegt - beim ersten Halbjahr darf dies nicht der Fall sein!
			if (halbjahre.size() > 0) {
				final GostHalbjahr prevHalbjahr = halbjahre.get(0).previous();
				if ((prevHalbjahr != null) && (manager.pruefeBelegung(fachbelegung, prevHalbjahr)))
					addFehler(GostBelegungsfehler.ZK_10);
			}
		}
	}

	
	/**
	 * Gesamtprüfung Punkt 41:
	 * Prüft, ob Geschichte korrekt belegt wurde (mind. von EF.1 bis Q1.2 oder als Zusatzkurs)
	 */
	private void pruefeBelegungGeschichte() {
		// Prüfe, ob überhaupt eine Belegung für Geschichte existiert
		if ((geschichte == null) || (geschichte.size() <= 0)) {
			addFehler(GostBelegungsfehler.GE_10);
			return;
		}
		
		// Prüfe, on eine Belegung von Geschichte in der EF und Q1 in jedem Halbjahr bis Q1.2 existiert. Wenn ja, dann ist die Belegungsverpflichtung erfüllt
		if (manager.pruefeBelegungExistiert(geschichte, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12))
			return;
		
		// Prüfe, ob eine Zusatzkurs-Belegung mit Geschichte existiert. Wenn ja, dann ist die Belegungsverpflichtung erfüllt
		if (zusatzkursFachbelegungen != null)
			for (final AbiturFachbelegung zkBelegung : zusatzkursFachbelegungen)
				if (geschichte.contains(zkBelegung))
					return;
		
		// Keine ausreichende Belegung von Geschichte gefunden -> Belegungsfehler 
		addFehler(GostBelegungsfehler.GE_10);
	}
	
	
	/**
	 * Gesamtprüfung Punkt 42:
	 * Prüft, ob Sozialwissenschaften korrekt belegt wurde (mind. von EF.1 bis Q1.2 oder als Zusatzkurs)
	 */
	private void pruefeBelegungSozialwissenschaften() {
		// Prüfe, ob überhaupt eine Belegung für Sozialwissenschaften existiert
		if ((sozialwissenschaften == null) || (sozialwissenschaften.size() <= 0)) {
			addFehler(GostBelegungsfehler.SW_10);
			return;
		}
		
		// Prüfe, on eine Belegung von Sozialwissenschaften in der EF und Q1 in jedem Halbjahr bis Q1.2 existiert. Wenn ja, dann ist die Belegungsverpflichtung erfüllt
		if (manager.pruefeBelegungExistiert(sozialwissenschaften, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12))
			return;
		
		// Prüfe, ob eine Zusatzkurs-Belegung mit Sozialwissenschaften existiert. Wenn ja, dann ist die Belegungsverpflichtung erfüllt
		if (zusatzkursFachbelegungen != null)
			for (final AbiturFachbelegung zkBelegung : zusatzkursFachbelegungen)
				if (sozialwissenschaften.contains(zkBelegung))
					return;
		
		// Keine ausreichende Belegung von Sozialwissenschaften gefunden -> Belegungsfehler 
		addFehler(GostBelegungsfehler.SW_10);
	}

	
	/**
	 * Gesamtprüfung Punkte 43:
	 * Prüft die Belegung von Religion und Philosophie in der EF. Wird Philosophie als Ersatz belegt, so wird auch geprüft, 
	 * ob eine weitere Gesellschaftswissenschaft belegt wurde.
	 */
	private void pruefeReligionEF() {
		// Prüfe nacheinander für die Halbjahre EF.1 und EF.2 
		for (final GostHalbjahr halbjahr : GostHalbjahr.getEinfuehrungsphase()) {
			// Wurde Religion in dem Halbjahr belegt?
			if (manager.pruefeBelegungExistiertEinzeln(religion, halbjahr))
				continue;
			
			// Wurde weder Religion (s.o.) noch Philosophie in dem Halbjahr belegt?
			// Oder wurde Philosophie sogar durchgängig belegt, aber keine zweite Gesellschaftswissenschaft belegt, 
			// welche in der Qualifikationsphase durchgängig belegt werden kann?
			if ((!manager.pruefeBelegung(philosophie, halbjahr))
					|| (manager.pruefeDurchgaengigkeit(philosophie) && (manager.zaehleBelegungInHalbjahren(gesellschaftswissenschaften, halbjahr) <= 1))) {
				addFehler(GostBelegungsfehler.RE_10);
				break;						
			}					
		}
	}
	

	/**
	 * Gesamtprüfung Punkt 44:
	 * Prüft die Belegung von Religion und Philosophie. Wird Philosophie als Ersatz belegt, so wird auch geprüft, ob eine weitere
	 * Gesellschaftswissenschaft belegt wurde.
	 */
	private void pruefeReligionQ1() {
		for (final GostHalbjahr halbjahr : GostHalbjahr.getHalbjahreFromJahrgang("Q1")) {
			// Wurde Religion in dem Halbjahr belegt?
			if (manager.pruefeBelegungExistiertEinzeln(religion, halbjahr))
				continue;
			
			// Wurde auch Philosophie in dem Halbjahr nicht belegt?
			if (!manager.pruefeBelegung(philosophie, halbjahr)) {
				addFehler(GostBelegungsfehler.RE_10);
				return;
			}
			
			// Prüfe, ob Philosophie als Ersatz dienen kann, wenn es durchgängig belegt wurde. 
			// -> in diesem Fall müsste eine zweite Gesellschaftswissenschaft durchgängig belegt sein!
			if (manager.pruefeDurchgaengigkeit(philosophie) && 
		       (manager.zaehleDurchgaengigeBelegungen(gesellschaftswissenschaften) > 1))
				continue;

			// Prüfe bei einer nicht durchgängigen Belegung von Philosophie, ob eine durchgängige 
			// Belegung eines anderen Faches vorliegt.
			// -> in diesem Fach kann Philosophie als Ersatz dienen
			if (!manager.pruefeDurchgaengigkeit(philosophie) &&
			   (manager.zaehleDurchgaengigeBelegungen(gesellschaftswissenschaften) > 0))
				continue;

			// Prüfe nun zunächst ob eine der sonstigen Gesellschaftswissenschaften (außer GE und SW)  
			// als Ersatz dienen können. Es muss immer ein(!) Fach als Ersatz dienen, was durch diese 
			// Prüfung indirekt sichergestellt ist, obwohl Halbjahre geprüft werden. 
			if (manager.zaehleBelegungInHalbjahren(sonstige_gesellschaftswissenschaften, halbjahr) > 0)
				continue;

			// Prüfe, ob Geschichte als Ausgleich genutzt werden kann. Dies ist dann der Fall, wenn mehr 
			// als zwei GE-Kurse in der Qualifikationsphase belegt wurden - also 3 Kurse (bei vieren wäre 
			// zuvor eine durchgängige Belegung erkannt worden). Durch die Nutzung des Halbjahreskurses 
			// als Ersatz für Religion müssen zwei zusammenhängende Kurse in Geschichte übrig bleiben.
			// Somit kann Geschichte nur als Ersatz für Religion in der Q1.1 dienen, sofern es - wie hier 
			// der Fall - nicht durchgängig belegt wurde.
			// Analog: Sozialwissenschaften 
			if ((halbjahr == GostHalbjahr.Q11) && 
					(manager.pruefeBelegungExistiertEinzeln(geschichte, GostHalbjahr.Q11) || 
					 manager.pruefeBelegungExistiertEinzeln(sozialwissenschaften, GostHalbjahr.Q11)))
				continue;
			
			// Es wurde kein Ersatz für Religion gefunden!
			addFehler(GostBelegungsfehler.RE_10);
			break;					
		}
	}
	

	/**
	 * Gesamtprüfung Punkt 55:
	 * Prüft, ob Fehler bei der Kontinuität bei Philosophie und Religion nur durch die Ersatzfachregelung bei Religion
	 * zustandekommen und damit zulässig sind.
	 */
	private void pruefeReligionKontinuitaet() {
		// Wurde Philosophie nicht belegt, so kann diese Prüfung entfallen
		if (philosophie == null)
			return; 

		// Prüfe ob die Belegungen in den einzelnen Halbjahren und in deren Vorgänger-Halbjahren korrekt sind oder Lücken aufweisen
		for (final AbiturFachbelegungHalbjahr belegung : philosophie.belegungen) {
			if (belegung == null)
				continue;
			final GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel);
			if (halbjahr == null)
				continue;
			// In EF.1 darf neu gewählt werden
			final GostHalbjahr prevHalbjahr = halbjahr.previous();
			if (prevHalbjahr == null)
				continue;
			// Wurde Philosophie in dem Halbjahr weiterbelegt, so liegt kein Belegungsfehler vor
			if (manager.pruefeBelegung(philosophie, prevHalbjahr))
				continue;    
			// Philosophie wurde nicht weiterbelegt! 
			// Wurde Philosophie überhaupt als Ersatz für Religion gewählt? -> Wenn nicht, dann liegt eine Lücke in der Belegung von Philosophie vor 
			if (manager.pruefeBelegungExistiertEinzeln(religion, halbjahr))
				addFehler(GostBelegungsfehler.E1BEL_10);
			// Wurde Religion im vorigen Halbjahr belegt? -> Wenn nicht, dann liegt ebenfalls eine Lücke bei der Belegung von Philosophie vor (bzw. bei Religion)
			if (!manager.pruefeBelegungExistiertEinzeln(religion, prevHalbjahr))
				addFehler(GostBelegungsfehler.E1BEL_10);
		}
	}
	
}
