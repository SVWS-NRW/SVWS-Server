package de.nrw.schule.svws.core.utils.gost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnis;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisSchiene;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import de.nrw.schule.svws.core.data.schueler.Schueler;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostBlockungsergebnis}. Hierbei werden auch Hilfsmethoden zur
 * Interpretation der Daten erzeugt. <br>
 * Nur Methoden, die mit "state" beginnen verändern den Zustand der Daten. <br>
 */
public class GostBlockungsergebnisManager {

	/** Der Blockungsdaten-Manager ist das Elternteil dieses Objektes. */
	private final @NotNull GostBlockungsdatenManager _parent;

	/** Das Blockungsergebnis ist das zugehörige Eltern-Datenobjekt. */
	private final @NotNull GostBlockungsergebnis _ergebnis;

	/** Schienen-Nummer --> GostBlockungsergebnisSchiene */
	private final @NotNull HashMap<@NotNull Integer, @NotNull GostBlockungsergebnisSchiene> _map_schienenNr_schiene = new HashMap<>();

	/** Schienen-ID --> GostBlockungSchiene */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungsergebnisSchiene> _map_schienenID_schiene = new HashMap<>();

	/** Schienen-ID --> Integer = Anzahl SuS */
	private final @NotNull HashMap<@NotNull Long, @NotNull Integer> _map_schienenID_schuelerAnzahl = new HashMap<>();

	/** Schienen-ID --> Anzahl Kollisionen */
	private final @NotNull HashMap<@NotNull Long, @NotNull Integer> _map_schienenID_kollisionen = new HashMap<>();

	/** Schienen-ID --> Fachart-ID --> Vector<Kurse> = Alle Kurse in der Schiene mit der Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostBlockungsergebnisKurs>>> _map_schienenID_fachartID_kurse = new HashMap<>();
	
	/** Schüler-ID --> Set<GostBlockungsergebnisKurs> */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisKurs>> _map_schuelerID_kurse = new HashMap<>();

//	/** Schüler-ID */
//	private final @NotNull HashSet<@NotNull Long> _set_schuelerID = new HashSet<>();

	/** Schüler-ID --> Integer (Kollisionen) */
	private final @NotNull HashMap<@NotNull Long, @NotNull Integer> _map_schuelerID_kollisionen = new HashMap<>();

	/** Schüler-ID --> Fach-ID --> GostBlockungsergebnisKurs */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull Long, GostBlockungsergebnisKurs>> _map_schuelerID_fachID_kurs = new HashMap<>();

	/** Schüler-ID --> Schienen-ID --> Set<GostBlockungsergebnisKurs> = Alle Kurse des Schülers in der Schiene.*/
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisKurs>>> _map_schuelerID_schienenID_kurse = new HashMap<>();

	/** Kurs-ID --> Set<GostBlockungsergebnisSchiene> */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene>> _map_kursID_schienen = new HashMap<>();

	/** Kurs-ID --> GostBlockungsergebnisKurs */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungsergebnisKurs> _map_kursID_kurs = new HashMap<>();

	/** Kurs-ID --> Set<SchuelerID> */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull Long>> _map_kursID_schuelerIDs = new HashMap<>();

	/** Fach-ID --> Vector<GostBlockungsergebnisKurs> */
	private final @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostBlockungsergebnisKurs>> _map_fachID_kurse = new HashMap<>();

	/** Fachart-ID --> Vector<GostBlockungsergebnisKurs> = Alle Kurse der selben Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostBlockungsergebnisKurs>> _map_fachartID_kurse = new HashMap<>();

	/** Fachart-ID --> Integer = Kursdifferenz der Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Integer> _map_fachartID_kursdifferenz = new HashMap<>();

	/** GostKursblockungRegelTyp --> Set<GostBlockungRegel> = Alle Regeln eines Typs. */
	private final @NotNull HashMap<@NotNull GostKursblockungRegelTyp, @NotNull HashSet<@NotNull GostBlockungRegel>> _map_regeltyp_regeln = new HashMap<>();

	
	/**
	 * Erstellt einen leeren GostBlockungsergebnisManager in Bezug auf GostBlockungsdatenManager. Die ID des leeren
	 * Ergebnisses ist -1 und muss noch gesetzt werden.
	 * 
	 * @param pParent                  Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der
	 *                                 Blockung)
	 * @param pGostBlockungsergebnisID Die ID des Blockungsergebnisses.
	 */
	public GostBlockungsergebnisManager(@NotNull GostBlockungsdatenManager pParent, long pGostBlockungsergebnisID) {
		_parent = pParent;
		_ergebnis = new GostBlockungsergebnis();
		_ergebnis.id = pGostBlockungsergebnisID;
		stateClear(_ergebnis);
	}

	/**
	 * Erstellt einen neuen Manager mit den Daten aus dem übergebenen Ergebnis.
	 * 
	 * @param pParent   Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der Blockung)
	 * @param pErgebnis Das Ergebnis, welches kopiert wird.
	 */
	public GostBlockungsergebnisManager(@NotNull GostBlockungsdatenManager pParent, @NotNull GostBlockungsergebnis pErgebnis) {
		_parent = pParent;
		_ergebnis = new GostBlockungsergebnis();
		stateClear(pErgebnis);
		// Idee1: Trennen in Maps erstellen und revalidate
		// "stateRevalidate": Alles neubewerten anhand der Daten von _ergebnis 
	}

//	private void stateRevalidate() {
//	}
	
	private void stateClear(GostBlockungsergebnis pErgebnis) {
		_ergebnis.id = (pErgebnis == null) ? -1 : pErgebnis.id;
		_ergebnis.blockungID = _parent.getID();
		_ergebnis.name = (pErgebnis == null) ? "Ergebnis (ID noch nicht initialisiert)" : pErgebnis.name;
		_ergebnis.gostHalbjahr = _parent.daten().gostHalbjahr;
		_ergebnis.istMarkiert = (pErgebnis == null) ? false : pErgebnis.istMarkiert;
		_ergebnis.istVorlage = (pErgebnis == null) ? false : pErgebnis.istVorlage;

		// Bewertungskriterium 3a und 3b (Kursdifferenzen)
		_ergebnis.bewertung.kursdifferenzMax = 0;
		_ergebnis.bewertung.kursdifferenzHistogramm = new int[_parent.getSchuelerAnzahl() + 1];

		// Bewertungskriterium 2a (Nicht zugeordnete Fachwahlen)
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet += _parent.daten().fachwahlen.size();

		// Regeln kopieren und hinzufügen.
		for (@NotNull GostKursblockungRegelTyp gRegelTyp : GostKursblockungRegelTyp.getCollection())
			_map_regeltyp_regeln.put(gRegelTyp, new HashSet<>());
		for (@NotNull GostBlockungRegel gRegel : _parent.daten().regeln)
			getOfRegeltypRegelmenge(GostKursblockungRegelTyp.fromTyp(gRegel.typ)).add(gRegel);
			
		// Schienen kopieren und hinzufügen.
		for (@NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
			// GostBlockungSchiene --> GostBlockungsergebnisSchiene
			@NotNull GostBlockungsergebnisSchiene eSchiene = new GostBlockungsergebnisSchiene();
			eSchiene.id = gSchiene.id;
			
			// Hinzufügen.
			_ergebnis.schienen.add(eSchiene);
			if (_map_schienenNr_schiene.put(gSchiene.nummer, eSchiene) != null)
				throw new NullPointerException("Schienen NR " + gSchiene.nummer + " doppelt!");
			if (_map_schienenID_schiene.put(gSchiene.id, eSchiene) != null)
				throw new NullPointerException("Schienen ID " + gSchiene.id + " doppelt!");
			if (_map_schienenID_schuelerAnzahl.put(gSchiene.id, 0) != null)
				throw new NullPointerException("Schienen ID " + gSchiene.id + " doppelt!");
			if (_map_schienenID_kollisionen.put(gSchiene.id, 0) != null)
				throw new NullPointerException("Schienen ID " + gSchiene.id + " doppelt!");
		}

		// Kurse kopieren und hinzufügen. Fachart-IDs erzeugen.
		for (@NotNull GostBlockungKurs gKurs : _parent.daten().kurse) {
			// GostBlockungKurs --> GostBlockungsergebnisKurs
			@NotNull GostBlockungsergebnisKurs eKurs = new GostBlockungsergebnisKurs();
			eKurs.id = gKurs.id;
			eKurs.fachID = gKurs.fach_id;
			eKurs.kursart = gKurs.kursart;
			eKurs.anzahlSchienen = gKurs.anzahlSchienen;
			
			// Hinzufügen.
			_ergebnis.bewertung.anzahlKurseNichtZugeordnet += eKurs.anzahlSchienen;
			if (_map_kursID_kurs.put(eKurs.id, eKurs) != null)
				throw new NullPointerException("Kurs-ID " + eKurs.id + " doppelt!");
			if (_map_kursID_schienen.put(eKurs.id, new HashSet<>()) != null)
				throw new NullPointerException("Kurs-ID " + eKurs.id + " doppelt!");
			if (_map_kursID_schuelerIDs.put(eKurs.id, new HashSet<>()) != null)
				throw new NullPointerException("Kurs-ID " + eKurs.id + " doppelt!");

			Vector<@NotNull GostBlockungsergebnisKurs> fachgruppe = _map_fachID_kurse.get(eKurs.fachID);
			if (fachgruppe == null) {
				fachgruppe = new Vector<>();
				_map_fachID_kurse.put(eKurs.fachID, fachgruppe);
			}
			fachgruppe.add(eKurs);

			long fachartID = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			Vector<@NotNull GostBlockungsergebnisKurs> fachartgruppe = _map_fachartID_kurse.get(fachartID);
			if (fachartgruppe == null) {
				fachartgruppe = new Vector<>();
				_map_fachartID_kurse.put(fachartID, fachartgruppe);
				_map_fachartID_kursdifferenz.put(fachartID, 0);
				_ergebnis.bewertung.kursdifferenzHistogramm[0]++;
			}
			fachartgruppe.add(eKurs);

		}

		// _map_schienenID_fachartID_kurse
		for (@NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
			_map_schienenID_fachartID_kurse.put(gSchiene.id, new HashMap<>());
			for (@NotNull Long fachartID : _map_fachartID_kursdifferenz.keySet()) 
				getOfSchieneFachartKursmengeMap(gSchiene.id).put(fachartID, new Vector<>());
		}
		
		// Schüler kopieren und hinzufügen.
		for (@NotNull Schueler gSchueler : _parent.daten().schueler) {
			// Schueler --> GostBlockungsergebnisKurs
			@NotNull Long eSchuelerID = gSchueler.id;
			
			// Hinzufügen.
//			if (!_set_schuelerID.add(eSchuelerID))
//				throw new NullPointerException("Schüler ID " + eSchuelerID + " doppelt!");
			if (_map_schuelerID_kurse.put(eSchuelerID, new HashSet<>()) != null)
				throw new NullPointerException("Schüler ID " + eSchuelerID + " doppelt!");
			if (_map_schuelerID_kollisionen.put(eSchuelerID, 0) != null)
				throw new NullPointerException("Schüler ID " + eSchuelerID + " doppelt!");
		}

		// Fachwahlen kopieren und hinzufügen.
		for (@NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen) {
			HashMap<@NotNull Long, GostBlockungsergebnisKurs> mapFachKurs = _map_schuelerID_fachID_kurs.get(gFachwahl.schuelerID);
			if (mapFachKurs == null) {
				mapFachKurs = new HashMap<>();
				_map_schuelerID_fachID_kurs.put(gFachwahl.schuelerID, mapFachKurs);
			}
			if (mapFachKurs.put(gFachwahl.fachID, null) != null)
				throw new NullPointerException("Schüler " + gFachwahl.schuelerID + " hat Fach " + gFachwahl.fachID + " doppelt!");
		}

		// _map_schuelerID_schienenID_kurse
		for (@NotNull Schueler gSchueler : _parent.daten().schueler) {
			_map_schuelerID_schienenID_kurse.put(gSchueler.id, new HashMap<>());
			for (@NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen)
				getOfSchuelerSchienenKursmengeMap(gSchueler.id).put(gSchiene.id, new HashSet<>());
		}
		
		// Die übergebenen Zuordnungen des GostBlockungsergebnis kopieren?
		if (pErgebnis != null) {
			HashSet<@NotNull Long> kursBearbeitet = new HashSet<>();
			for (@NotNull GostBlockungsergebnisSchiene schiene : pErgebnis.schienen)
				for (@NotNull GostBlockungsergebnisKurs kurs : schiene.kurse) {
					setKursSchiene(kurs.id, schiene.id, true);
					if (kursBearbeitet.add(kurs.id))
						for (@NotNull Long schuelerID : kurs.schueler)
							setSchuelerKurs(schuelerID, kurs.id, true);
				}
		}
		
		// Regelverletzungen überprüfen.
		stateRegelvalidierung();
	}

	private void stateRegelvalidierung() {
		// Clear
		@NotNull Vector<@NotNull Long> regelVerletzungen = _ergebnis.bewertung.regelVerletzungen;
		regelVerletzungen.clear();
		
		// 4: SCHUELER_FIXIEREN_IN_KURS
		for (@NotNull GostBlockungRegel r : getOfRegeltypRegelmenge(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS)) {
			long schuelerID = r.parameter.get(0);
			long kursID = r.parameter.get(1);
			if (!getOfSchuelerOfKursIstZugeordnet(schuelerID, kursID))
				regelVerletzungen.add(r.id);
		}
	
		// 5: SCHUELER_VERBIETEN_IN_KURS
		for (@NotNull GostBlockungRegel r : getOfRegeltypRegelmenge(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS)) {
			long schuelerID = r.parameter.get(0);
			long kursID = r.parameter.get(1);
			if (getOfSchuelerOfKursIstZugeordnet(schuelerID, kursID))
				regelVerletzungen.add(r.id);
		}

		// 2: KURS_FIXIERE_IN_SCHIENE
		for (@NotNull GostBlockungRegel r : getOfRegeltypRegelmenge(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE)) {
			long kursID = r.parameter.get(0);
			int schienenNr = r.parameter.get(1).intValue();
			GostBlockungsergebnisSchiene schiene = getSchieneEmitNr(schienenNr);
			if (!getOfKursSchienenmenge(kursID).contains(schiene))
				regelVerletzungen.add(r.id);
		}
	
		// 3: KURS_SPERRE_IN_SCHIENE
		for (@NotNull GostBlockungRegel r : getOfRegeltypRegelmenge(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE)) {
			long kursID = r.parameter.get(0);
			int schienenNr = r.parameter.get(1).intValue();
			GostBlockungsergebnisSchiene schiene = getSchieneEmitNr(schienenNr);
			if (getOfKursSchienenmenge(kursID).contains(schiene))
				regelVerletzungen.add(r.id);
		}
		
		// 1: KURSART_SPERRE_SCHIENEN_VON_BIS
		for (@NotNull GostBlockungRegel r : getOfRegeltypRegelmenge(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS)) {
			int kursart = r.parameter.get(0).intValue();
			int schienenNrVon = r.parameter.get(1).intValue();
			int schienenNrBis = r.parameter.get(2).intValue();
			for (int schienenNr = schienenNrVon;  schienenNr <=  schienenNrBis; schienenNr++) 
				for (GostBlockungsergebnisKurs eKurs : getSchieneEmitNr(schienenNr).kurse) 
					if (eKurs.kursart == kursart)
						regelVerletzungen.add(r.id);
		}
		
		// 6: KURSART_ALLEIN_IN_SCHIENEN_VON_BIS
		for (@NotNull GostBlockungRegel r : getOfRegeltypRegelmenge(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)) {
			int kursart = r.parameter.get(0).intValue();
			int schienenNrVon = r.parameter.get(1).intValue();
			int schienenNrBis = r.parameter.get(2).intValue();
			for (GostBlockungsergebnisKurs eKurs : _map_kursID_kurs.values()) 
				for (@NotNull Long eSchieneID : eKurs.schienen) {
					int nr  = getSchieneG(eSchieneID).nummer;
					boolean b1 = eKurs.kursart == kursart;
					boolean b2 = (schienenNrVon <= nr) && (nr <= schienenNrBis);
					if (b1 != b2)
						regelVerletzungen.add(r.id);
				}
		}
	
	}

	/**
	 * Fügt den Schüler dem Kurs hinzu.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * 
	 * @return             FALSE, falls der Schüler bereits zugeordnet war, sonst TRUE.
	 */
	private boolean stateSchuelerKursHinzufuegen(long pSchuelerID, long pKursID) {
		@NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		long fachID = kurs.fachID;
		if (getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) != null) // wirft ggf. Exception
			return false; // Das Fach wurde bereits einem anderen Kurs zugeordnet!
			
		@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(pSchuelerID);
		@NotNull HashSet<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(pKursID);
		@NotNull HashMap<@NotNull Long, GostBlockungsergebnisKurs> mapSFK = getOfSchuelerFachIDKursMap(pSchuelerID);
		long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		// Hinzufügen
		kurs.schueler.add(pSchuelerID); // Data-Objekt aktualisieren
		kurseOfSchueler.add(kurs);
		schuelerIDsOfKurs.add(pSchuelerID);
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		mapSFK.put(fachID, kurs);
		stateKursdifferenzUpdate(fachartID);
		for (@NotNull Long schieneID : kurs.schienen)
			stateSchuelerSchieneHinzufuegen(pSchuelerID, schieneID, kurs);
		stateRegelvalidierung();

		return true;
	}

	/**
	 * Entfernt den Schüler aus dem Kurs.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * 
	 * @return             FALSE, falls der Schüler bereits nicht zugeordnet war, sonst TRUE.
	 */
	private boolean stateSchuelerKursEntfernen(long pSchuelerID, long pKursID) {
		@NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		long fachID = kurs.fachID;
		if (getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) != kurs) // wirft ggf. Exception
			return false; // Der Kurs ist derzeit gar nicht zugeordnet!
			
		@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(pSchuelerID);
		@NotNull HashSet<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(pKursID);
		@NotNull HashMap<@NotNull Long, GostBlockungsergebnisKurs> mapSFK = getOfSchuelerFachIDKursMap(pSchuelerID);
		long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		// Hinzufügen
		kurs.schueler.remove(pSchuelerID); // Data-Objekt aktualisieren
		kurseOfSchueler.remove(kurs);
		schuelerIDsOfKurs.remove(pSchuelerID);
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
		mapSFK.put(fachID, null);
		stateKursdifferenzUpdate(fachartID);
		for (@NotNull Long schieneID : kurs.schienen)
			stateSchuelerSchieneEntfernen(pSchuelerID, schieneID, kurs);
		stateRegelvalidierung();

		return true;
	}

	/**
	 * Fügt den Kurs der Schiene hinzu.
	 * 
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 * 
	 * @return             FALSE, falls der Kurs bereits in der Schiene war, sonst TRUE.
	 */
	private boolean stateKursSchieneHinzufuegen(long pKursID, long pSchienenID) {
		@NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		@NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(pSchienenID);
		@NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(pKursID);
		long fachID = kurs.fachID;
		long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		if (schienenOfKurs.contains(schiene))
			return false;
		
		Vector<@NotNull GostBlockungsergebnisKurs> kursGruppe = getOfSchieneFachartKursmengeMap(pSchienenID).get(fachartID);
		if (kursGruppe == null)
			throw new NullPointerException("SchieneID=" + pSchienenID + " --> FachartID=" + fachartID + " --> NULL");

		// Hinzufügen
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kurs.schienen.add(schiene.id);   // Data-Objekt aktualisieren
		schiene.kurse.add(kurs);      // Data-Objekt aktualisieren
		schienenOfKurs.add(schiene);
		for (@NotNull Long schuelerID : kurs.schueler)
			stateSchuelerSchieneHinzufuegen(schuelerID, schiene.id, kurs);
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		_ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene += kursGruppe.isEmpty() ? 0 : 1;
		kursGruppe.add(kurs); // Muss nach der Bewertung passieren.
		stateRegelvalidierung();

		return true;
	}

	/**
	 * Entfernt den Kurs aus der Schiene.
	 * 
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 * 
	 * @return             FALSE, falls der Kurs bereits nicht in der Schiene war, sonst TRUE.
	 */
	private boolean stateKursSchieneEntfernen(long pKursID, long pSchienenID) {
		@NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		@NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(pSchienenID);
		@NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(pKursID);
		long fachID = kurs.fachID;
		long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		if (!schienenOfKurs.contains(schiene))
			return false;

		Vector<@NotNull GostBlockungsergebnisKurs> kursGruppe = getOfSchieneFachartKursmengeMap(pSchienenID).get(fachartID);
		if (kursGruppe == null)
			throw new NullPointerException("SchieneID="+pSchienenID+" --> FachartID="+fachartID+" --> NULL");
		
		// Entfernen
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kurs.schienen.remove(schiene.id);   // Data-Objekt aktualisieren
		schiene.kurse.remove(kurs);      // Data-Objekt aktualisieren
		schienenOfKurs.remove(schiene);
		for (@NotNull Long schuelerID : kurs.schueler)
			stateSchuelerSchieneEntfernen(schuelerID, schiene.id, kurs);
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kursGruppe.remove(kurs); // Muss vor der Bewertung passieren.
		_ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene -= kursGruppe.isEmpty() ? 0 : 1;
		stateRegelvalidierung();

		return true;
	}

	private void stateSchuelerSchieneHinzufuegen(long pSchuelerID, long pSchienenID, @NotNull GostBlockungsergebnisKurs pKurs) {
		// Schiene --> Integer (erhöhen)
		int schieneSchuelerzahl = getOfSchieneAnzahlSchueler(pSchienenID);
		_map_schienenID_schuelerAnzahl.put(pSchienenID, schieneSchuelerzahl + 1);

		// Schiene --> Schüler --> Kurse (erhöhen)
		@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
		kursmenge.add(pKurs);

		// Kollisionen erhöhen?
		if (kursmenge.size() > 1) {
			// Kollisionen der Schiene.
			int schieneKollisionen = getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID);
			_map_schienenID_kollisionen.put(pSchienenID, schieneKollisionen + 1);

			// Kollisionen des Schülers.
			int schuelerKollisionen = getOfSchuelerAnzahlKollisionen(pSchuelerID);
			_map_schuelerID_kollisionen.put(pSchuelerID, schuelerKollisionen + 1);
			
			// Kollisionen insgesamt. 
			_ergebnis.bewertung.anzahlSchuelerKollisionen++;
		}
	}

	private void stateSchuelerSchieneEntfernen(long pSchuelerID, long pSchienenID, @NotNull GostBlockungsergebnisKurs pKurs) {
		// // Schiene --> Integer (verringern)
		int schieneSchuelerzahl = getOfSchieneAnzahlSchueler(pSchienenID);
		if (schieneSchuelerzahl == 0)
			throw new NullPointerException("schieneSchuelerzahl == 0 --> Entfernen unmöglich!");
		_map_schienenID_schuelerAnzahl.put(pSchienenID, schieneSchuelerzahl - 1);

		// Schiene --> Schüler --> Integer (verringern)
		@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
		kursmenge.remove(pKurs);

		// Kollisionen verringern?
		if (kursmenge.size() > 0) {
			// Kollisionen der Schiene.
			int schieneKollisionen = getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID);
			if (schieneKollisionen == 0)
				throw new NullPointerException("schieneKollisionen == 0 --> Entfernen unmöglich!");
			_map_schienenID_kollisionen.put(pSchienenID, schieneKollisionen - 1);

			// Kollisionen des Schülers.
			int schuelerKollisionen = getOfSchuelerAnzahlKollisionen(pSchuelerID);
			if (schuelerKollisionen == 0)
				throw new NullPointerException("schuelerKollisionen == 0 --> Entfernen unmöglich!");
			_map_schuelerID_kollisionen.put(pSchuelerID, schuelerKollisionen - 1);

			// Kollisionen insgesamt. 
			if (_ergebnis.bewertung.anzahlSchuelerKollisionen == 0)
				throw new NullPointerException("Gesamtkollisionen == 0 --> Entfernen unmöglich!");
			_ergebnis.bewertung.anzahlSchuelerKollisionen--;
		}
	}

	private void stateKursdifferenzUpdate(long pFachartID) {
		// Alte Kursdifferenz ist noch gespeichert.
		int oldKD = getOfFachartKursdifferenz(pFachartID);

		// Neue Kursdifferenz wird berechnet
		GostBlockungsergebnisKurs kurs1 = getOfFachartKursmenge(pFachartID).firstElement();
		if (kurs1 == null)
			throw new NullPointerException("Fachart-ID " + pFachartID + " ohne Kurs!");
		int min = kurs1.schueler.size();
		int max = min;
		for (@NotNull GostBlockungsergebnisKurs kurs : getOfFachartKursmenge(pFachartID)) {
			int size = kurs.schueler.size();
			min = Math.min(min, size);
			max = Math.max(max, size);
		}
		int newKD = max - min;

		// Kursdifferenz ändert sich nicht.
		if (newKD == oldKD)
			return;

		// Kursdifferenz ändert sich.
		_map_fachartID_kursdifferenz.put(pFachartID, newKD);

		int[] kursdifferenzen = _ergebnis.bewertung.kursdifferenzHistogramm;
		kursdifferenzen[oldKD]--;
		kursdifferenzen[newKD]++;

		if (oldKD == _ergebnis.bewertung.kursdifferenzMax)
			if (newKD > oldKD) {
				// Neues Maximum
				_ergebnis.bewertung.kursdifferenzMax = newKD;
			} else {
				// Neues Minimum
				if (kursdifferenzen[oldKD] == 0)
					_ergebnis.bewertung.kursdifferenzMax = newKD;
			}
	}

	/**
	 * Liefert die Blockungs-ID. Das ist die ID des Elternteils.
	 * 
	 * @return Liefert die Blockungs-ID. Das ist die ID des Elternteils.
	 */
	public long getBlockungsdatenID() {
		return _ergebnis.blockungID;
	}

	/**
	 * Liefert das Blockungsergebnis zurück.
	 * 
	 * @return das Blockungsergebnis
	 */
	public @NotNull GostBlockungsergebnis getErgebnis() {
		return _ergebnis;
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * 
	 * @return Den Wert des 1. Bewertungskriteriums.
	 */
	public int getOfBewertung1Wert() {
		int summe = 0;
		summe += _ergebnis.bewertung.anzahlKurseNichtZugeordnet;
		summe += _ergebnis.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * 
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung1Farbcode() {
		double summe = getOfBewertung1Wert();
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 * 
	 * @return Den Wert des 2. Bewertungskriteriums.
	 */
	public int getOfBewertung2Wert() {
		int summe = 0;
		summe += _ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += _ergebnis.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 * 
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung2Farbcode() {
		double summe = getOfBewertung2Wert();
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 * 
	 * @return Den Wert des 3. Bewertungskriteriums.
	 */
	public int getOfBewertung3Wert() {
		return _ergebnis.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 * 
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung3Farbcode() {
		int wert = getOfBewertung3Wert();
		if (wert > 0)
			wert--; // Jede Kursdifferenz wird um 1 reduziert, außer die 0.
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 * 
	 * @return Den Wert des 4. Bewertungskriteriums.
	 */
	public int getOfBewertung4Wert() {
		return _ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 * 
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung4Farbcode() {
		int wert = getOfBewertung4Wert();
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 * Liefert die Anzahl an Schülerkollisionen. Ist ein Schüler x mal in einer Schiene und ist x > 1, dann wird dies
	 * als x-1 Kollisionen gezählt.
	 * 
	 * @return Die Gesamtzahl der Kollisionen zurück.
	 */
	public int getOfBewertungAnzahlKollisionen() {
		return _ergebnis.bewertung.anzahlSchuelerKollisionen;
	}

	/**
	 * Liefert die Anzahl nicht vollständig verteilter Kurse. Ein Multikurse der über zwei Schienen geht und gar nicht
	 * zugeteilt wurde, wird doppelt gezählt.
	 * 
	 * @return Liefert die Anzahl nicht vollständig verteilter Kurse.
	 */
	public int getOfBewertungAnzahlNichtZugeordneterKurse() {
		return _ergebnis.bewertung.anzahlKurseNichtZugeordnet;
	}

	/**
	 * Liefert die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 * 
	 * @return Die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 */
	public int getOfBewertungAnzahlNichtzugeordneterFachwahlen() {
		return _ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
	}

	/**
	 * Liefert die Menge der Regeln des angegebenen Typs. <br>
	 * Wenn es zu dem Typ keine Regel gibt, ist die Menge leer.
	 * 
	 * @param pTyp Der Typ der Regel.
	 * @return Die Menge der Regeln des angegebenen Typs.
	 */
	public @NotNull HashSet<@NotNull GostBlockungRegel> getOfRegeltypRegelmenge(@NotNull GostKursblockungRegelTyp pTyp) {
		HashSet<@NotNull GostBlockungRegel> set = _map_regeltyp_regeln.get(pTyp);
		if (set == null)
			return new HashSet<>();
		return set;
	}

	/**
	 * Ermittelt das Fach für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.
	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return Das GostFach-Objekt.
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist.
	 */
	public @NotNull GostFach getFach(long pFachID) throws NullPointerException {
		return _parent.faecherManager().getOrException(pFachID);
	}

	/**
	 * Liefert die Menge aller Kurse mit dem angegebenen Fach. <br>
	 * Wirft eine Exception, wenn der ID kein Fach zugeordnet ist.
	 * 
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return Die Menge aller Kurse mit dem angegebenen Fach.
	 */
	public @NotNull Vector<@NotNull GostBlockungsergebnisKurs> getOfFachKursmenge(long pFachID) {
		Vector<@NotNull GostBlockungsergebnisKurs> kurseOfFach = _map_fachID_kurse.get(pFachID);
		if (kurseOfFach == null)
			throw new NullPointerException("Fach-ID " + pFachID + " unbekannt!");
		return kurseOfFach;
	}

	/**
	 * Liefert die Kursmenge, die zur Fachart gehört. <br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 * 
	 * @param  pFachartID           Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 * 
	 * @return                      Die Kursmenge, die zur Fachart gehört.
	 * 
	 * @throws NullPointerException Falls die Fachart-ID unbekannt ist.
	 */
	public @NotNull Vector<@NotNull GostBlockungsergebnisKurs> getOfFachartKursmenge(long pFachartID)
			throws NullPointerException {
		Vector<@NotNull GostBlockungsergebnisKurs> fachartGruppe = _map_fachartID_kurse.get(pFachartID);
		if (fachartGruppe == null)
			throw new NullPointerException("Fachart-ID " + pFachartID + " unbekannt!");
		return fachartGruppe;
	}

	/**
	 * Liefert die Kursdifferenz der Fachart. <br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 * 
	 * @param  pFachartID           Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 * 
	 * @return                      Die Kursdifferenz der Fachart.
	 * 
	 * @throws NullPointerException Falls die Fachart-ID unbekannt ist.
	 */
	public int getOfFachartKursdifferenz(long pFachartID) throws NullPointerException {
		Integer kursdifferenz = _map_fachartID_kursdifferenz.get(pFachartID);
		if (kursdifferenz == null)
			throw new NullPointerException("Fachart-ID " + pFachartID + " unbekannt!");
		return kursdifferenz;
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Das Schueler-Objekt.
	 * @throws     NullPointerException im Falle, dass die ID nicht bekannt ist
	 */
	public @NotNull Schueler getSchuelerG(long pSchuelerID) throws NullPointerException {
		return _parent.getSchueler(pSchuelerID);
	}

// Diese Methode ist nicht nötig, da nicht mehr das Objekt zurückgegeben wird. 
//	/**
//	 * Liefert die Schüler-ID (des Blockungsergebnisses) zur übergebenen Schüler-ID. <br>
//	 * Wirft eine Exception, wenn der ID keine Schüler-ID zugeordnet ist.
//	 * 
//	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
//	 * 
//	 * @return             Die Schüler-ID (des Blockungsergebnisses) zur übergebenen Schüler-ID.
//	 */
//	public long getSchuelerE(long pSchuelerID) {
//		if (!_set_schuelerID.contains(pSchuelerID))
//			throw new NullPointerException("Schüler-ID " + pSchuelerID + " unbekannt!");
//		return pSchuelerID;
//	}

	/**
	 * Liefert einen Schüler-String im Format: 'Nachname, Vorname'.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * 
	 * @return             Einen Schüler-String im Format: 'Nachname, Vorname'.
	 */
	public @NotNull String getOfSchuelerNameVorname(long pSchuelerID) {
		@NotNull Schueler schueler = _parent.getSchueler(pSchuelerID);
		return schueler.nachname + ", " + schueler.vorname;
	}


	/**
	 * Liefert die Menge aller Kurse, die dem Schüler zugeordnet sind. <br>
	 * Wirft eine Exception, wenn der ID kein Schüler zugeordnet ist.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller Kurse, die dem Schüler zugeordnet sind.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getOfSchuelerKursmenge(long pSchuelerID) {
		HashSet<@NotNull GostBlockungsergebnisKurs> kursIDs = _map_schuelerID_kurse.get(pSchuelerID);
		if (kursIDs == null)
			throw new NullPointerException("Schüler-ID " + pSchuelerID + " unbekannt!");
		return kursIDs;
	}

	/**
	 * Liefert die Menge aller Kurse des Schülers mit Kollisionen.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller Kurse des Schülers mit Kollisionen.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getOfSchuelerKursmengeMitKollisionen(long pSchuelerID) {
		@NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisKurs>> mapSchieneKurse = getOfSchuelerSchienenKursmengeMap(pSchuelerID);
		
		@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();
		for (@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kurseDerSchiene : mapSchieneKurse.values()) 
			if (kurseDerSchiene.size() > 1)
				set.addAll(kurseDerSchiene);
		
		return set;
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Kollision hat. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return TRUE, falls der Schüler mindestens eine Kollision hat.
	 */
	public boolean getOfSchuelerHatKollision(long pSchuelerID) {
		return getOfSchuelerAnzahlKollisionen(pSchuelerID) > 0;
	}
	
	/**
	 * Liefert die Anzahl an Kollisionen des Schülers. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Anzahl an Kollisionen des Schülers.
	 */
	public int getOfSchuelerAnzahlKollisionen(long pSchuelerID) {
		Integer anzahl = _map_schuelerID_kollisionen.get(pSchuelerID);
		if (anzahl == null)
			throw new NullPointerException("Schüler-ID " + pSchuelerID + " unbekannt!");
		return anzahl;
	}

	/**
	 * Liefert die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 */
	public @NotNull HashMap<@NotNull Long, GostBlockungsergebnisKurs> getOfSchuelerFachIDKursMap(long pSchuelerID) {
		HashMap<@NotNull Long, GostBlockungsergebnisKurs> mapFachKurs = _map_schuelerID_fachID_kurs.get(pSchuelerID);
		if (mapFachKurs == null)
			throw new NullPointerException("Schüler-ID " + pSchuelerID + " unbekannt!");
		return mapFachKurs;
	}

	/**
	 * Liefert die Map des Schülers, die einer Schiene die Kurse des Schülers zuordnet.
	 *   
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Map des Schülers, die der Schiene die Kurse des Schülers zuordnet.
	 */
	public @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisKurs>> getOfSchuelerSchienenKursmengeMap(long pSchuelerID) {
		HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisKurs>> mapSchuelerKurse = _map_schuelerID_schienenID_kurse.get(pSchuelerID);
		if (mapSchuelerKurse == null)
			throw new NullPointerException("Schüler-ID " + pSchuelerID + " unbekannt!");
		return mapSchuelerKurse;
	}
	
	/**
	 * Liefert die Kursmenge des Schülers in der Schiene.
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Kursmenge des Schülers in der Schiene.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getOfSchuelerOfSchieneKursmenge(long pSchuelerID, long pSchienenID) {
		HashSet<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfSchuelerSchienenKursmengeMap(pSchuelerID).get(pSchienenID);
		if (kursmenge == null)
			throw new NullPointerException("Schüler-ID=" + pSchuelerID +", Schienen-ID=" + pSchienenID +  " unbekannt!");
		return kursmenge;
	}

	
	/**
	 * Liefert die zu (Schüler, Fach) die jeweilige Kursart. <br>
	 * Liefert eine Exception, falls (Schüler, Fach) nicht existiert.
	 * 
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pFachID     Die Datenbank-ID des Faches.
	 * 
	 * @return Die zu (Schüler, Fach) die jeweilige Kursart.
	 */
	public @NotNull GostKursart getOfSchuelerOfFachKursart(long pSchuelerID, long pFachID) {
		return _parent.getOfSchuelerOfFachKursart(pSchuelerID, pFachID);
	}

	/**
	 * Ermittelt für den Schüler mit einem Fach den zugeordneten Kurs.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pFachID     Die Datenbank-ID des Faches.
	 * @return             Der zugeordnete Kurs oder null, falls es keine Zuordnung gibt.
	 */
	public GostBlockungsergebnisKurs getOfSchuelerOfFachZugeordneterKurs(long pSchuelerID, long pFachID) {
		if (getOfSchuelerFachIDKursMap(pSchuelerID).containsKey(pFachID) == false)
			throw new NullPointerException("Schüler " + pSchuelerID + " hat das Fach " + pFachID + " nicht gewählt!");
		return getOfSchuelerFachIDKursMap(pSchuelerID).get(pFachID);
	}

	/**
	 * Liefert TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 * 
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * 
	 * @return             TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 */
	public boolean getOfSchuelerOfKursIstZugeordnet(long pSchuelerID, long pKursID) {
		@NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(pSchuelerID);
		return kurseOfSchueler.contains(kurs);
	}

	/**
	 * Ermittelt den Kurs für die angegebene ID. Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param  pKursID Die ID des Kurses.
	 * @return Das GostBlockungKurs-Objekt.
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist.
	 */
	public @NotNull GostBlockungKurs getKursG(long pKursID) throws NullPointerException {
		return _parent.getKurs(pKursID);
	}

	/**
	 * Liefert den Kurs (des Blockungsergebnisses) zur übergebenen ID. <br>
	 * Wirft eine Exception, wenn der ID kein Kurs zugeordnet ist.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * 
	 * @return         Den Kurs (des Blockungsergebnisses) zur übergebenen ID.
	 */
	public @NotNull GostBlockungsergebnisKurs getKursE(long pKursID) {
		GostBlockungsergebnisKurs kurs = _map_kursID_kurs.get(pKursID);
		if (kurs == null)
			throw new NullPointerException("Kurs-ID " + pKursID + " unbekannt!");
		return kurs;
	}

	/**
	 * Liefert den Namen des Kurses. Der Name wird automatisch erzeugt aus dem Fach, der Kursart und der Nummer,
	 * beispielsweise D-GK1.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * 
	 * @return         Die Datenbank-ID des Kurses.
	 */
	public @NotNull String getOfKursName(long pKursID) {
		return _parent.getNameOfKurs(pKursID);
	}

	/**
	 * Liefert TRUE, falls der Kurs der Schiene zugeordnet ist.
	 * 
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 * 
	 * @return             TRUE, falls der Kurs der Schiene zugeordnet ist.
	 */
	public boolean getOfKursOfSchieneIstZugeordnet(long pKursID, long pSchienenID) {
		@NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(pSchienenID);
		@NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(pKursID);
		return schienenOfKurs.contains(schiene);
	}

	/**
	 * Liefert die Menge aller Schüler-IDs, die dem Kurs zugeordnet sind. <br>
	 * Wirft eine Exception, wenn der ID kein Kurs zugeordnet ist.
	 * 
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return Die Menge aller Schüler-IDs, die dem Kurs zugeordnet sind.
	 */
	public @NotNull HashSet<@NotNull Long> getOfKursSchuelerIDmenge(long pKursID) {
		HashSet<@NotNull Long> schuelerIDs = _map_kursID_schuelerIDs.get(pKursID);
		if (schuelerIDs == null)
			throw new NullPointerException("Kurs-ID " + pKursID + " unbekannt!");
		return schuelerIDs;
	}

	/**
	 * Liefert die Schienenmenge des Kurses.
	 * 
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return Die Schienenmenge des Kurses.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> getOfKursSchienenmenge(long pKursID) {
		HashSet<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = _map_kursID_schienen.get(pKursID);
		if (schienenOfKurs == null)
			throw new NullPointerException("Kurs-ID " + pKursID + " unbekannt!");
		return schienenOfKurs;
	}
	
	/**
	 * Liefert TRUE, falls der Kurs mindestens eine Kollision hat. <br>
	 * Kollision: Der Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return TRUE, falls der Kurs mindestens eine Kollision hat.
	 */
	public boolean getOfKursHatKollision(long pKursID) {
		for (@NotNull GostBlockungsergebnisSchiene schiene :  getOfKursSchienenmenge(pKursID)) 
			for (@NotNull Long schuelerID : getKursE(pKursID).schueler) 
				if (getOfSchuelerOfSchieneKursmenge(schuelerID, schiene.id).size() > 1)
					return true;
		return false;
	}

	/**
	 * Liefert die Anzahl an Schülern des Kurses mit Kollisionen. <br>
	 * Kollision: Der Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schülern des Kurses mit Kollisionen.
	 */
	public int getOfKursAnzahlKollisionen(long pKursID) {
		int summe = 0;
		for (@NotNull GostBlockungsergebnisSchiene schiene :  getOfKursSchienenmenge(pKursID)) 
			for (@NotNull Long schuelerID : getKursE(pKursID).schueler) 
				if (getOfSchuelerOfSchieneKursmenge(schuelerID, schiene.id).size() > 1)
					summe++;
		return summe;
	}
	
	/**
	 * Liefert die Anzahl an Schienen in denen der Kurs gerade ist.
	 * 
	 * @param kursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schienen in denen der Kurs gerade ist.
	 */
	public int getOfKursAnzahlSchienenIst(long kursID) {
		return getOfKursSchienenmenge(kursID).size();
	}

	/**
	 * Liefert die Anzahl an Schienen, die der Kurs haben sollte.
	 * 
	 * @param kursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schienen, die der Kurs haben sollte.
	 */
	public int getOfKursAnzahlSchienenSoll(long kursID) {
		return getKursE(kursID).anzahlSchienen;
	}

	/**
	 * Liefert die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 * 
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return Die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 */
	public @NotNull HashSet<@NotNull Long> getOfKursSchuelermengeMitKollisionen(long pKursID) {
		@NotNull HashSet<@NotNull Long> set = new HashSet<>();
		for (@NotNull GostBlockungsergebnisSchiene schiene :  getOfKursSchienenmenge(pKursID)) 
			for (@NotNull Long schuelerID : getKursE(pKursID).schueler) 
				if (getOfSchuelerOfSchieneKursmenge(schuelerID, schiene.id).size() > 1)
					set.add(schuelerID);
		return set;
	}

	/**
	 * Ermittelt die Schiene für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes
	 * {@link GostBlockungsdatenManager}. <br>
	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
	 * 
	 * @param pSchienenID Die ID der Schiene
	 * @return Das GostBlockungSchiene-Objekt.
	 * @throws NullPointerException im Falle, dass die ID nicht bekannt ist.
	 */
	public @NotNull GostBlockungSchiene getSchieneG(long pSchienenID) throws NullPointerException {
		return _parent.getSchiene(pSchienenID);
	}

	/**
	 * Liefert die Schiene  zur übergebenen ID. <br>
	 * Wirft eine Exception, wenn der ID keine Schiene zugeordnet ist.
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Schiene (des Blockungsergebnisses) zur übergebenen ID.
	 */
	public @NotNull GostBlockungsergebnisSchiene getSchieneE(long pSchienenID) {
		GostBlockungsergebnisSchiene schiene = _map_schienenID_schiene.get(pSchienenID);
		if (schiene == null)
			throw new NullPointerException("Schienen-ID " + pSchienenID + " unbekannt!");
		return schiene;
	}

	/**
	 * Liefert die Schiene mit der übergebenen Schienen-NR. <br>
	 * Wirft eine Exception, wenn eine Schiene mit NR nicht existiert.
	 * 
	 * @param pSchienenNr Die Nummer der Schiene.
	 * @return Die Schiene mit der übergebenen Schienen-NR.
	 */
	public @NotNull GostBlockungsergebnisSchiene getSchieneEmitNr(int pSchienenNr) {
		GostBlockungsergebnisSchiene schiene = _map_schienenNr_schiene.get(pSchienenNr);
		if (schiene == null)
			throw new NullPointerException("Schienen-NR " + pSchienenNr + " unbekannt!");
		return schiene;
	}
	
	/**
	 * Liefert die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück. <br>
	 * Falls ein Schüler mehrfach in der Schiene ist, wird er mehrfach gezählt!
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Anzahl an Schülern in der Schiene.
	 */
	public int getOfSchieneAnzahlSchueler(long pSchienenID) {
		Integer anzahl = _map_schienenID_schuelerAnzahl.get(pSchienenID);
		if (anzahl == null)
			throw new NullPointerException("Schienen-ID " + pSchienenID + " unbekannt!");
		return anzahl;
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 * 
	 * @return Die Anzahl an Schienen.
	 */
	public int getOfSchieneAnzahl() {
		return _map_schienenID_schiene.size();
	}
	
	/**
	 * TRUE, falls die Schiene mindestens eine Schüler-Kollision hat. 
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 */
	public boolean getOfSchieneHatKollision(long pSchienenID) {
		return getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID) > 0;
	}

	/**
	 * Liefert die Anzahl an Schüler-Kollisionen der Schiene zurück. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Anzahl an Schüler-Kollisionen in der Schiene.
	 */
	public int getOfSchieneAnzahlSchuelerMitKollisionen(long pSchienenID) {
		Integer anzahl = _map_schienenID_kollisionen.get(pSchienenID);
		if (anzahl == null)
			throw new NullPointerException("Schienen-ID " + pSchienenID + " unbekannt!");
		return anzahl;
	}

	/**
	 * Liefert die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 */
	public @NotNull HashSet<@NotNull Long> getOfSchieneSchuelermengeMitKollisionen(long pSchienenID) {
		@NotNull HashSet<@NotNull Long> set = new HashSet<>();
		for (@NotNull Long schuelerID : _map_schuelerID_kollisionen.keySet()) 
			if (getOfSchuelerOfSchieneKursmenge(schuelerID, pSchienenID).size() > 1)
				set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public int getOfSchieneAnzahlKursmengeMitKollisionen(long pSchienenID) {
		int summe = 0;
		for (@NotNull GostBlockungsergebnisKurs kurs : getSchieneE(pSchienenID).kurse) 
			if (getOfKursHatKollision(kurs.id))
				summe++;
		return summe;
	}
	
	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 * 
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getOfSchieneKursmengeMitKollisionen(long pSchienenID) {
		@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();
		for (@NotNull GostBlockungsergebnisKurs kurs : getSchieneE(pSchienenID).kurse) 
			if (getOfKursHatKollision(kurs.id))
				set.add(kurs);
		return set;
	}

	/**
	 * Liefert eine "FachartID --> Kurse" Map der Schiene.
	 *   
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Eine "FachartID --> Kurse" Map der Schiene.
	 */
	public @NotNull HashMap<@NotNull Long, @NotNull Vector<@NotNull GostBlockungsergebnisKurs>> getOfSchieneFachartKursmengeMap(long pSchienenID) {
		HashMap<@NotNull Long, @NotNull Vector<@NotNull GostBlockungsergebnisKurs>> map = _map_schienenID_fachartID_kurse.get(pSchienenID);
		if (map == null)
			throw new NullPointerException("Die Schienen-ID "+pSchienenID+" ist unbekannt!"); 
		return map;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schülermenge zuordnet.
	 * 
	 * @return Die Map, welche jedem Kurs seine Schülermenge zuordnet.
	 */
	public @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull Long>> getMappingKursIDSchuelerIDs() {
		return _map_kursID_schuelerIDs;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 * 
	 * @return Die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 */
	public @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene>> getMappingKursIDSchienenmenge() {
		return _map_kursID_schienen;
	}

	/**
	 * Liefert eine Menge aller Schüler-IDs mit mindestens einer Kollision.
	 * 
	 * @return Eine Menge aller Schüler-IDs mit mindestens einer Kollision.
	 */
	public @NotNull HashSet<@NotNull Long> getMengeDerSchuelerMitKollisionen() {
		@NotNull HashSet<@NotNull Long> set = new HashSet<>();
		for (@NotNull Long schuelerID : _map_schuelerID_kollisionen.keySet()) 
			if (getOfSchuelerHatKollision(schuelerID))
				set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert eine Menge aller Kurse mit mindestens einer Kollision.
	 * 
	 * @return Eine Menge aller Kurse mit mindestens einer Kollision.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getMengeDerKurseMitKollisionen() {
		@NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();
		for (@NotNull GostBlockungsergebnisKurs kurs : _map_kursID_kurs.values()) 
			if (getOfKursHatKollision(kurs.id))
				set.add(kurs);
		return set;
	}
	
	/**
	 * Liefert eine Menge aller Schienen mit mindestens einer Kollision.
	 * 
	 * @return Eine Menge aller Schienen mit mindestens einer Kollision.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> getMengeDerSchienenMitKollisionen() {
		@NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> set = new HashSet<>();
		for (@NotNull GostBlockungsergebnisSchiene schiene : _map_schienenID_schiene.values()) 
			if (getOfSchieneHatKollision(schiene.id))
				set.add(schiene);
		return set;
	}
        
	/**
	 * Liefert die Menge aller Schienen.
	 * 
	 * @return Die Menge aller Schienen.
	 */
	public @NotNull Vector<@NotNull GostBlockungsergebnisSchiene> getMengeAllerSchienen() {
		return _ergebnis.schienen;
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene. Die Schiene wird anhand ihrer Nummer (nicht die Datenbank-ID)
	 * identifiziert.
	 * 
	 * @param  pKursID              Die Datenbank-ID des Kurses.
	 * @param  pSchienenNr          Die Nummer der Schiene (nicht die Datenbank-ID).
	 * 
	 * @throws NullPointerException Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public void setKursSchienenNr(long pKursID, int pSchienenNr) throws NullPointerException {
		GostBlockungsergebnisSchiene eSchiene = _map_schienenNr_schiene.get(pSchienenNr);
		if (eSchiene == null)
			throw new NullPointerException("SchienenNr. " + pSchienenNr + " unbekannt!");
		stateKursSchieneHinzufuegen(pKursID, eSchiene.id);
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene oder hebt die Verknüpfung auf.
	 * 
	 * @param  pKursID                   Die Datenbank-ID des Kurses.
	 * @param  pSchienenID               Die Datenbank-ID der Schiene.
	 * @param  pHinzufuegenOderEntfernen TRUE=Hinzufügen, FALSE=Entfernen
	 * 
	 * @return                           TRUE, falls die jeweilige Operation erfolgreich war.
	 * 
	 * @throws NullPointerException      Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public boolean setKursSchiene(long pKursID, long pSchienenID, boolean pHinzufuegenOderEntfernen)
			throws NullPointerException {
		if (pHinzufuegenOderEntfernen)
			return stateKursSchieneHinzufuegen(pKursID, pSchienenID);
		return stateKursSchieneEntfernen(pKursID, pSchienenID);
	}

	/**
	 * Verknüpft einen Schüler mit einem Kurs oder hebt die Verknüpfung auf.
	 * 
	 * @param  pSchuelerID               Die Datenbank-ID des Schülers.
	 * @param  pKursID                   Die Datenbank-ID des Kurses.
	 * @param  pHinzufuegenOderEntfernen TRUE=Hinzufügen, FALSE=Entfernen
	 * 
	 * @return                           TRUE, falls die jeweilige Operation erfolgreich war.
	 * 
	 * @throws NullPointerException      Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public boolean setSchuelerKurs(long pSchuelerID, long pKursID, boolean pHinzufuegenOderEntfernen)
			throws NullPointerException {
		if (pHinzufuegenOderEntfernen)
			return stateSchuelerKursHinzufuegen(pSchuelerID, pKursID);
		return stateSchuelerKursEntfernen(pSchuelerID, pKursID);
	}

	/**
	 * Nur für Debug-Zwecke.
	 */
	public void debug() {
		//  TODO BAR Weitere Bewertungskriterien überprüfen (auch nach Entfernen).
		System.out.println("----- Kurse sortiert nach Fachart -----");
		for (@NotNull Long fachartID : _map_fachartID_kurse.keySet()) {
			System.out.println("FachartID = " + fachartID + " (KD = " + getOfFachartKursdifferenz(fachartID) + ")");
			for (@NotNull GostBlockungsergebnisKurs kurs : getOfFachartKursmenge(fachartID)) {
				System.out.println("    " + getOfKursName(kurs.id) + " : " + kurs.schueler.size() + " SuS");
			}
		}
		System.out.println("KursdifferenzMax = " + _ergebnis.bewertung.kursdifferenzMax);
		System.out.println("KursdifferenzHistogramm = " + Arrays.toString(_ergebnis.bewertung.kursdifferenzHistogramm));
	}

//	/**
//	 * Ermittelt die Kurs-Schüler-Zuordnungen für den Kurs mit der angegebenen ID. <br>
//	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
//	 * 
//	 * @deprecated                      Bitte {@link #getKursE(long)} verwenden!
//	 * 
//	 * @param      pKursID              die ID des Kurses
//	 * 
//	 * @return                          die Kurs-Schüler-Zuordnungen des Kurses
//	 * 
//	 * @throws     NullPointerException im Falle, dass die ID nicht bekannt ist
//	 */
//	public @NotNull GostBlockungsergebnisKurs getKursSchuelerZuordnung(long pKursID) throws NullPointerException {
//		return getKursE(pKursID);
//	}
//
//	/**
//	 * Ordnet einen Kurs einer Schiene zu. <br>
//	 * Ist die ID der Schiene null, so wird eine vorherige Zuordnung aufgehoben.
//	 * 
//	 * @deprecated           Diese Methode kann nicht mehr verwendet werden. Das Problem liegt darin, dass bei
//	 *                       Multikursen nicht klar ist, welche Schienenzuordnung aufgehoben werden soll. Bitte
//	 *                       {@link #setKursSchiene(long, long, boolean)} verwenden.
//	 * 
//	 * @param      idKurs    die ID des Kurses
//	 * @param      idSchiene die ID der Schiene
//	 * 
//	 * @return               true, falls der Kurs der Schiene zugeordnet wurde
//	 */
//	public boolean assignKursSchiene(long idKurs, Long idSchiene) {
//		if (idSchiene == null)
//			throw new NullPointerException("Funktioniert nicht mit Multikurses. ");
//		return setKursSchiene(idKurs, idSchiene, true);
//	}
//
//	/**
//	 * Ordnet einen Schüler einem Kurs zu.
//	 * 
//	 * @deprecated            Bitte nicht mehr verwenden, stattdessen {@link #setSchuelerKurs(long, long, boolean)} -->
//	 *                        Vorsicht der boolean-Wert ist genau anders herum gemeint!
//	 * 
//	 * @param      idSchueler die ID des Schüler
//	 * @param      idKurs     die ID des Kurses
//	 * @param      undo       normally false, but true if the assignment should be removed
//	 * 
//	 * @return                true, falls der Schüler dem Kurs zugeordnet wurde
//	 */
//	public boolean assignSchuelerKurs(long idSchueler, long idKurs, boolean undo) {
//		return setSchuelerKurs(idSchueler, idKurs, !undo);
//	}
//
//	/**
//	 * Ermittelt die Schienen-Zuordnung des Kurses mit der angegebenen ID. Liegt keine Zuordnung vor, so wird null
//	 * zurückgegeben.
//	 * 
//	 * @deprecated         Diese Methode sollte auf gar keinen Fall mehr benutzt werden, da ein Kurs (Multikurs) in mehr
//	 *                     als in einer Schiene liegen kann! --> {@link #getOfKursSchienenmenge(long)}
//	 * 
//	 * @param      pKursID die ID des Kurses
//	 * 
//	 * @return             die Kurs-Schienen-Zuordnungen der Schiene, zu welcher der Kurs zugeordnet ist
//	 */
//	public GostBlockungsergebnisSchiene getKursSchienenZuordnung(long pKursID) {
//		HashSet<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(pKursID);
//		for (@NotNull GostBlockungsergebnisSchiene schiene : schienenOfKurs)
//			return schiene;
//		return null;
//	}
//
//	/**
//	 * Ermittelt die Kurs-Schülermenge für den Kurs mit der angegebenen ID. <br>
//	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
//	 * 
//	 * @deprecated                      Bitte {@link #getOfKursSchuelerIDmenge(long)} verwenden.
//	 * 
//	 * @param      pKursID              die ID des Kurses
//	 * 
//	 * @return                          die Kurs-Schülermenge des Kurses
//	 * 
//	 * @throws     NullPointerException im Falle, dass die ID nicht bekannt ist
//	 */
//	public @NotNull HashSet<@NotNull Long> getKursSchuelermenge(long pKursID) throws NullPointerException {
//		@NotNull HashSet<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(pKursID);
//		return schuelerIDsOfKurs;
//	}
//
//	/**
//	 * Ermittelt die Kurs-Schüler-Zuordnungen für die Kurse mit der angegebenen Fach-ID. <br>
//	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
//	 * 
//	 * @deprecated                      Bitte {@link #getOfFachKursmenge(long)} verwenden.
//	 * 
//	 * @param      pFachID              die ID des Faches
//	 * 
//	 * @return                          die Kurs-Schüler-Zuordnungen der Kurse mit der angegebenen Fach-ID
//	 * 
//	 * @throws     NullPointerException im Falle, dass die ID nicht bekannt ist
//	 */
//	public @NotNull Vector<@NotNull GostBlockungsergebnisKurs> getKursSchuelerZuordnungenFuerFach(long pFachID)
//			throws NullPointerException {
//		return getOfFachKursmenge(pFachID);
//	}
//
//	/**
//	 * Ermittelt die Schüler-Kurs-Zuordnungen für den Schüler mit der angegebenen ID. <br>
//	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
//	 * 
//	 * @deprecated                      Bitte {@link #getOfSchuelerKursmenge(long)} verwenden.
//	 * 
//	 * @param      pSchuelerID          die ID des Schülers
//	 * 
//	 * @return                          die Schüler-Kurs-Zuordnungen des Schülers
//	 * 
//	 * @throws     NullPointerException im Falle, dass die ID nicht bekannt ist
//	 */
//	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getSchuelerKursZuordnung(long pSchuelerID)
//			throws NullPointerException {
//		return getOfSchuelerKursmenge(pSchuelerID);
//	}
//
//	/**
//	 * Ermittelt die Schienen-Kurs-Zuordnungen für die Schiene mit der angegebenen ID. <br>
//	 * Erzeugt eine NullPointerException im Fehlerfall, dass die ID nicht bekannt ist.
//	 * 
//	 * @deprecated                      Bitte {@link #getSchieneE(long) verwenden.}
//	 * 
//	 * @param      pSchienenID          die ID der Schiene
//	 * 
//	 * @return                          die Schienen-Kurs-Zuordnungen zu der Schiene
//	 * 
//	 * @throws     NullPointerException im Falle, dass die ID nicht bekannt ist
//	 */
//	public @NotNull GostBlockungsergebnisSchiene getSchienenKursZuordnung(long pSchienenID)
//			throws NullPointerException {
//		return getSchieneE(pSchienenID);
//	}
//
}
