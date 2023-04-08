package de.svws_nrw.core.utils.gost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungInput;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungInputKurs;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungOutput;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungOutputFachwahlZuKurs;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.kursblockung.SchuelerblockungAlgorithmus;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
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
	private @NotNull GostBlockungsergebnis _ergebnis = new GostBlockungsergebnis();

	/** Schienen-Nummer --> GostBlockungsergebnisSchiene */
	private final @NotNull HashMap<@NotNull Integer, @NotNull GostBlockungsergebnisSchiene> _map_schienenNr_schiene = new HashMap<>();

	/** Schienen-ID --> GostBlockungSchiene */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungsergebnisSchiene> _map_schienenID_schiene = new HashMap<>();

	/** Schienen-ID --> Integer = Anzahl SuS */
	private final @NotNull HashMap<@NotNull Long, @NotNull Integer> _map_schienenID_schuelerAnzahl = new HashMap<>();

	/** Schienen-ID --> Anzahl Kollisionen */
	private final @NotNull HashMap<@NotNull Long, @NotNull Integer> _map_schienenID_kollisionen = new HashMap<>();

	/** Schienen-ID --> Fachart-ID --> ArrayList<Kurse> = Alle Kurse in der Schiene mit der Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull Long, @NotNull ArrayList<@NotNull GostBlockungsergebnisKurs>>> _map_schienenID_fachartID_kurse = new HashMap<>();

	/** Schüler-ID --> Set<GostBlockungsergebnisKurs> */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisKurs>> _map_schuelerID_kurse = new HashMap<>();

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

	/** Fach-ID --> ArrayList<GostBlockungsergebnisKurs> */
	private final @NotNull HashMap<@NotNull Long, @NotNull ArrayList<@NotNull GostBlockungsergebnisKurs>> _map_fachID_kurse = new HashMap<>();

	/** Fachart-ID --> ArrayList<GostBlockungsergebnisKurs> = Alle Kurse der selben Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull ArrayList<@NotNull GostBlockungsergebnisKurs>> _map_fachartID_kurse = new HashMap<>();

	/** Fachart-ID --> Integer = Kursdifferenz der Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Integer> _map_fachartID_kursdifferenz = new HashMap<>();

	/**
	 * Erstellt einen leeren GostBlockungsergebnisManager in Bezug auf GostBlockungsdatenManager. Die ID des leeren
	 * Ergebnisses ist -1 und muss noch gesetzt werden.
	 *
	 * @param pParent                  Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der
	 *                                 Blockung)
	 * @param pGostBlockungsergebnisID Die ID des Blockungsergebnisses.
	 */
	public GostBlockungsergebnisManager(final @NotNull GostBlockungsdatenManager pParent, final long pGostBlockungsergebnisID) {
		_parent = pParent;
		stateClear(new GostBlockungsergebnis(), pGostBlockungsergebnisID);
	}

	/**
	 * Erstellt einen neuen Manager mit den Daten aus dem übergebenen Ergebnis.
	 *
	 * @param pParent   Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der Blockung)
	 * @param pErgebnis Das Ergebnis, welches kopiert wird.
	 */
	public GostBlockungsergebnisManager(final @NotNull GostBlockungsdatenManager pParent, final @NotNull GostBlockungsergebnis pErgebnis) {
		_parent = pParent;
		stateClear(pErgebnis, pErgebnis.id);
	}

	/**
	 * Baut alle Datenstrukturen neu auf.
	 */
	private void stateRevalidateEverything() {
		stateClear(_ergebnis, _ergebnis.id);
	}

	private void stateClear(final @NotNull GostBlockungsergebnis pOld, final long pGostBlockungsergebnisID) {
		// clear maps
		_map_schienenNr_schiene.clear();
		_map_schienenID_schiene.clear();
		_map_schienenID_schuelerAnzahl.clear();
		_map_schienenID_kollisionen.clear();
		_map_schienenID_fachartID_kurse.clear();
		_map_schuelerID_kurse.clear();
		_map_schuelerID_kollisionen.clear();
		_map_schuelerID_fachID_kurs.clear();
		_map_schuelerID_schienenID_kurse.clear();
		_map_kursID_schienen.clear();
		_map_kursID_kurs.clear();
		_map_kursID_schuelerIDs.clear();
		_map_fachID_kurse.clear();
		_map_fachartID_kurse.clear();
		_map_fachartID_kursdifferenz.clear();

		// copy attributes
		_ergebnis = new GostBlockungsergebnis();
		_ergebnis.id = pGostBlockungsergebnisID;
		_ergebnis.blockungID = _parent.getID();
		_ergebnis.name = pOld.name;
		_ergebnis.gostHalbjahr = _parent.daten().gostHalbjahr;
		_ergebnis.istMarkiert = pOld.istMarkiert;
		_ergebnis.istVorlage = pOld.istVorlage;

		// Bewertungskriterium 3a und 3b (Kursdifferenzen)
		_ergebnis.bewertung.kursdifferenzMax = 0;
		_ergebnis.bewertung.kursdifferenzHistogramm = new int[_parent.getSchuelerAnzahl() + 1];

		// Bewertungskriterium 2a (Nicht zugeordnete Fachwahlen)
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet += _parent.daten().fachwahlen.size();

		// Schienen von '_parent' kopieren und hinzufügen.
		for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
			// GostBlockungSchiene --> GostBlockungsergebnisSchiene
			final @NotNull GostBlockungsergebnisSchiene eSchiene = new GostBlockungsergebnisSchiene();
			eSchiene.id = gSchiene.id;

			// Hinzufügen.
			_ergebnis.schienen.add(eSchiene);
			if (_map_schienenNr_schiene.put(gSchiene.nummer, eSchiene) != null)
				throw new DeveloperNotificationException("Schienen NR " + gSchiene.nummer + " doppelt!");
			if (_map_schienenID_schiene.put(gSchiene.id, eSchiene) != null)
				throw new DeveloperNotificationException("Schienen ID " + gSchiene.id + " doppelt!");
			if (_map_schienenID_schuelerAnzahl.put(gSchiene.id, 0) != null)
				throw new DeveloperNotificationException("Schienen ID " + gSchiene.id + " doppelt!");
			if (_map_schienenID_kollisionen.put(gSchiene.id, 0) != null)
				throw new DeveloperNotificationException("Schienen ID " + gSchiene.id + " doppelt!");
		}

		// Kurse von '_parent' kopieren und hinzufügen. Fachart-IDs erzeugen.
		for (final @NotNull GostBlockungKurs gKurs : _parent.daten().kurse) {
			// GostBlockungKurs --> GostBlockungsergebnisKurs
			final @NotNull GostBlockungsergebnisKurs eKurs = new GostBlockungsergebnisKurs();
			eKurs.id = gKurs.id;
			eKurs.fachID = gKurs.fach_id;
			eKurs.kursart = gKurs.kursart;
			eKurs.anzahlSchienen = gKurs.anzahlSchienen;

			// Hinzufügen.
			_ergebnis.bewertung.anzahlKurseNichtZugeordnet += eKurs.anzahlSchienen;
			if (_map_kursID_kurs.put(eKurs.id, eKurs) != null)
				throw new DeveloperNotificationException("Kurs-ID " + eKurs.id + " doppelt!");
			if (_map_kursID_schienen.put(eKurs.id, new HashSet<>()) != null)
				throw new DeveloperNotificationException("Kurs-ID " + eKurs.id + " doppelt!");
			if (_map_kursID_schuelerIDs.put(eKurs.id, new HashSet<>()) != null)
				throw new DeveloperNotificationException("Kurs-ID " + eKurs.id + " doppelt!");

			ArrayList<@NotNull GostBlockungsergebnisKurs> fachgruppe = _map_fachID_kurse.get(eKurs.fachID);
			if (fachgruppe == null) {
				fachgruppe = new ArrayList<>();
				_map_fachID_kurse.put(eKurs.fachID, fachgruppe);
			}
			fachgruppe.add(eKurs);

			final long fachartID = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			ArrayList<@NotNull GostBlockungsergebnisKurs> fachartgruppe = _map_fachartID_kurse.get(fachartID);
			if (fachartgruppe == null) {
				fachartgruppe = new ArrayList<>();
				_map_fachartID_kurse.put(fachartID, fachartgruppe);
				_map_fachartID_kursdifferenz.put(fachartID, 0);
				_ergebnis.bewertung.kursdifferenzHistogramm[0]++;
			}
			fachartgruppe.add(eKurs);

		}

		// Fachwahlen zu denen es keinen Kurs gibt der Map '_map_fachartID_kurse' hinzufügen.
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen) {
			final long fachartID = GostKursart.getFachartIDByFachwahl(gFachwahl);
			if (!_map_fachartID_kurse.containsKey(fachartID))
				_map_fachartID_kurse.put(fachartID, new ArrayList<>());
		}

		// _map_schienenID_fachartID_kurse
		for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
			_map_schienenID_fachartID_kurse.put(gSchiene.id, new HashMap<>());
			for (final @NotNull Long fachartID : _map_fachartID_kursdifferenz.keySet())
				getOfSchieneFachartKursmengeMap(gSchiene.id).put(fachartID, new ArrayList<>());
		}

		// Schüler kopieren und hinzufügen.
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler) {
			// Schueler --> GostBlockungsergebnisKurs
			final @NotNull Long eSchuelerID = gSchueler.id;

			// Hinzufügen.
			if (_map_schuelerID_kurse.put(eSchuelerID, new HashSet<>()) != null)
				throw new DeveloperNotificationException("Schüler ID " + eSchuelerID + " doppelt!");
			if (_map_schuelerID_kollisionen.put(eSchuelerID, 0) != null)
				throw new DeveloperNotificationException("Schüler ID " + eSchuelerID + " doppelt!");
		}

		// Fachwahlen kopieren und hinzufügen.
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler)
			_map_schuelerID_fachID_kurs.put(gSchueler.id, new HashMap<>());
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen) {
			final HashMap<@NotNull Long, GostBlockungsergebnisKurs> mapFachKurs = _map_schuelerID_fachID_kurs.get(gFachwahl.schuelerID);
			if (mapFachKurs == null)
				throw new DeveloperNotificationException("Schüler " + gFachwahl.schuelerID + " hat eine Fachwahl ist aber unbekannt!");
			if (mapFachKurs.put(gFachwahl.fachID, null) != null)
				throw new DeveloperNotificationException("Schüler " + gFachwahl.schuelerID + " hat Fach " + gFachwahl.fachID + " doppelt!");
		}

		// _map_schuelerID_schienenID_kurse
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler) {
			_map_schuelerID_schienenID_kurse.put(gSchueler.id, new HashMap<>());
			for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen)
				getOfSchuelerSchienenKursmengeMap(gSchueler.id).put(gSchiene.id, new HashSet<>());
		}

		// Zuordnungen kopieren (diese können leer sein).
		final HashSet<@NotNull Long> kursBearbeitet = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisSchiene schieneOld : pOld.schienen)
			for (final @NotNull GostBlockungsergebnisKurs kursOld : schieneOld.kurse) {
				setKursSchiene(kursOld.id, schieneOld.id, true);
				if (kursBearbeitet.add(kursOld.id))
					for (final @NotNull Long schuelerID : kursOld.schueler)
						setSchuelerKurs(schuelerID, kursOld.id, true);
			}

		// Regelverletzungen überprüfen.
		stateRegelvalidierung();
	}

	private void stateRegelvalidierung() {
		// Clear
		final @NotNull ArrayList<@NotNull Long> regelVerletzungen = _ergebnis.bewertung.regelVerletzungen;
		regelVerletzungen.clear();

		for (final @NotNull GostBlockungRegel r : _parent.getMengeOfRegeln()) {
			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);

			switch (typ) {
				case SCHUELER_FIXIEREN_IN_KURS: // 4
					if (!getOfSchuelerOfKursIstZugeordnet(r.parameter.get(0), r.parameter.get(1)))
						regelVerletzungen.add(r.id);
					break;
				case SCHUELER_VERBIETEN_IN_KURS: // 5
					if (getOfSchuelerOfKursIstZugeordnet(r.parameter.get(0), r.parameter.get(1)))
						regelVerletzungen.add(r.id);
					break;
				case KURS_FIXIERE_IN_SCHIENE: // 2
					if (!getOfKursSchienenmenge(r.parameter.get(0)).contains(getSchieneEmitNr(r.parameter.get(1).intValue())))
						regelVerletzungen.add(r.id);
					break;
				case KURS_SPERRE_IN_SCHIENE: // 3
					if (getOfKursSchienenmenge(r.parameter.get(0)).contains(getSchieneEmitNr(r.parameter.get(1).intValue())))
						regelVerletzungen.add(r.id);
					break;
				case KURSART_SPERRE_SCHIENEN_VON_BIS: // 1
					for (int schienenNr = r.parameter.get(1).intValue(); schienenNr <= r.parameter.get(2).intValue(); schienenNr++)
						for (final GostBlockungsergebnisKurs eKurs : getSchieneEmitNr(schienenNr).kurse)
							if (eKurs.kursart == r.parameter.get(0).intValue())
								regelVerletzungen.add(r.id);
					break;
				case KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: // 6
					for (final GostBlockungsergebnisKurs eKurs : _map_kursID_kurs.values())
						for (final @NotNull Long eSchieneID : eKurs.schienen) {
							final int nr  = getSchieneG(eSchieneID).nummer;
							final boolean b1 = eKurs.kursart == r.parameter.get(0).intValue();
							final boolean b2 = (r.parameter.get(1).intValue() <= nr) && (nr <= r.parameter.get(2).intValue());
							if (b1 != b2)
								regelVerletzungen.add(r.id);
						}
					break;
				case KURS_VERBIETEN_MIT_KURS: // 7
					for (final @NotNull GostBlockungsergebnisSchiene schiene1 : getOfKursSchienenmenge(r.parameter.get(0)))
						for (final @NotNull GostBlockungsergebnisSchiene schiene2 : getOfKursSchienenmenge(r.parameter.get(1)))
							if (schiene1 == schiene2)
								regelVerletzungen.add(r.id);
					break;
				case KURS_ZUSAMMEN_MIT_KURS: // 8
					final @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> set1 = getOfKursSchienenmenge(r.parameter.get(0));
					final @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> set2 = getOfKursSchienenmenge(r.parameter.get(1));
					if (set1.size() < set2.size()) { // "set1" muss in "set2" enthalten sein.
						for (final @NotNull GostBlockungsergebnisSchiene schiene1 : set1)
							if (!set2.contains(schiene1))
								regelVerletzungen.add(r.id);
					} else { // "set2" muss in "set1" enthalten sein.
						for (final @NotNull GostBlockungsergebnisSchiene schiene2 : set2)
							if (!set1.contains(schiene2))
								regelVerletzungen.add(r.id);
					}
					break;
				case LEHRKRAFT_BEACHTEN: // 9
					final boolean externBeachten = r.parameter.get(0) == 1L;
					for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _map_schienenID_schiene.values())
						for (final @NotNull GostBlockungsergebnisKurs eKurs1 : eSchiene.kurse)
							for (final @NotNull GostBlockungsergebnisKurs eKurs2 : eSchiene.kurse)
								if (eKurs1.id < eKurs2.id)
									for (final @NotNull GostBlockungKursLehrer gLehr1 : getKursG(eKurs1.id).lehrer)
										for (final @NotNull GostBlockungKursLehrer gLehr2 : getKursG(eKurs2.id).lehrer)
											if ((gLehr1.id == gLehr2.id) && ((externBeachten) || (!gLehr1.istExtern)))
												regelVerletzungen.add(r.id);
					break;
				case LEHRKRAEFTE_BEACHTEN: // 10
					for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _map_schienenID_schiene.values())
						for (final @NotNull GostBlockungsergebnisKurs eKurs1 : eSchiene.kurse)
							for (final @NotNull GostBlockungsergebnisKurs eKurs2 : eSchiene.kurse)
								if (eKurs1.id < eKurs2.id)
									for (final @NotNull GostBlockungKursLehrer gLehr1 : getKursG(eKurs1.id).lehrer)
										for (final @NotNull GostBlockungKursLehrer gLehr2 : getKursG(eKurs2.id).lehrer)
											if (gLehr1.id == gLehr2.id)
												regelVerletzungen.add(r.id);
					break;
				default:
					throw new IllegalStateException("Der Regel-Typ ist unbekannt: " + typ);
			}
		}

		// Die Bewertung im DatenManager aktualisieren.
		_parent.updateErgebnisBewertung(_ergebnis);
	}

	/**
	 * Fügt den Schüler dem Kurs hinzu.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 *
	 * @return             FALSE, falls der Schüler bereits zugeordnet war, sonst TRUE.
	 */
	private boolean stateSchuelerKursHinzufuegen(final long pSchuelerID, final long pKursID) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		final long fachID = kurs.fachID;
		if (getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) != null) // wirft ggf. Exception
			return false; // Das Fach wurde bereits einem anderen Kurs zugeordnet!

		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(pSchuelerID);
		final @NotNull HashSet<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(pKursID);
		final @NotNull HashMap<@NotNull Long, GostBlockungsergebnisKurs> mapSFK = getOfSchuelerFachIDKursMap(pSchuelerID);
		final long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		// Hinzufügen
		kurs.schueler.add(pSchuelerID); // Data-Objekt aktualisieren
		kurseOfSchueler.add(kurs);
		schuelerIDsOfKurs.add(pSchuelerID);
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		mapSFK.put(fachID, kurs);
		stateKursdifferenzUpdate(fachartID);
		for (final @NotNull Long schieneID : kurs.schienen)
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
	private boolean stateSchuelerKursEntfernen(final long pSchuelerID, final long pKursID) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		final long fachID = kurs.fachID;
		if (getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) != kurs) // wirft ggf. Exception
			return false; // Der Kurs ist derzeit gar nicht zugeordnet!

		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(pSchuelerID);
		final @NotNull HashSet<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(pKursID);
		final @NotNull HashMap<@NotNull Long, GostBlockungsergebnisKurs> mapSFK = getOfSchuelerFachIDKursMap(pSchuelerID);
		final long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		// Hinzufügen
		kurs.schueler.remove(pSchuelerID); // Data-Objekt aktualisieren
		kurseOfSchueler.remove(kurs);
		schuelerIDsOfKurs.remove(pSchuelerID);
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
		mapSFK.put(fachID, null);
		stateKursdifferenzUpdate(fachartID);
		for (final @NotNull Long schieneID : kurs.schienen)
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
	private boolean stateKursSchieneHinzufuegen(final long pKursID, final long pSchienenID) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(pSchienenID);
		final @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(pKursID);
		final long fachID = kurs.fachID;
		final long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		if (schienenOfKurs.contains(schiene))
			return false;

		final ArrayList<@NotNull GostBlockungsergebnisKurs> kursGruppe = getOfSchieneFachartKursmengeMap(pSchienenID).get(fachartID);
		if (kursGruppe == null)
			throw new DeveloperNotificationException("SchieneID=" + pSchienenID + " --> FachartID=" + fachartID + " --> NULL");

		// Hinzufügen
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kurs.schienen.add(schiene.id);   // Data-Objekt aktualisieren
		schiene.kurse.add(kurs);      // Data-Objekt aktualisieren
		schienenOfKurs.add(schiene);
		for (final @NotNull Long schuelerID : kurs.schueler)
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
	private boolean stateKursSchieneEntfernen(final long pKursID, final long pSchienenID) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(pSchienenID);
		final @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(pKursID);
		final long fachID = kurs.fachID;
		final long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		if (!schienenOfKurs.contains(schiene))
			return false;

		final ArrayList<@NotNull GostBlockungsergebnisKurs> kursGruppe = getOfSchieneFachartKursmengeMap(pSchienenID).get(fachartID);
		if (kursGruppe == null)
			throw new DeveloperNotificationException("SchieneID=" + pSchienenID + " --> FachartID=" + fachartID + " --> NULL");

		// Entfernen
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kurs.schienen.remove(schiene.id);   // Data-Objekt aktualisieren
		schiene.kurse.remove(kurs);      // Data-Objekt aktualisieren
		schienenOfKurs.remove(schiene);
		for (final @NotNull Long schuelerID : kurs.schueler)
			stateSchuelerSchieneEntfernen(schuelerID, schiene.id, kurs);
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kursGruppe.remove(kurs); // Muss vor der Bewertung passieren.
		_ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene -= kursGruppe.isEmpty() ? 0 : 1;
		stateRegelvalidierung();

		return true;
	}

	private void stateSchuelerSchieneHinzufuegen(final long pSchuelerID, final long pSchienenID, final @NotNull GostBlockungsergebnisKurs pKurs) {
		// Schiene --> Integer (erhöhen)
		final int schieneSchuelerzahl = getOfSchieneAnzahlSchueler(pSchienenID);
		_map_schienenID_schuelerAnzahl.put(pSchienenID, schieneSchuelerzahl + 1);

		// Schiene --> Schüler --> Kurse (erhöhen)
		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
		kursmenge.add(pKurs);

		// Kollisionen erhöhen?
		if (kursmenge.size() > 1) {
			// Kollisionen der Schiene.
			final int schieneKollisionen = getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID);
			_map_schienenID_kollisionen.put(pSchienenID, schieneKollisionen + 1);

			// Kollisionen des Schülers.
			final int schuelerKollisionen = getOfSchuelerAnzahlKollisionen(pSchuelerID);
			_map_schuelerID_kollisionen.put(pSchuelerID, schuelerKollisionen + 1);

			// Kollisionen insgesamt.
			_ergebnis.bewertung.anzahlSchuelerKollisionen++;
		}
	}

	private void stateSchuelerSchieneEntfernen(final long pSchuelerID, final long pSchienenID, final @NotNull GostBlockungsergebnisKurs pKurs) {
		// // Schiene --> Integer (verringern)
		final int schieneSchuelerzahl = getOfSchieneAnzahlSchueler(pSchienenID);
		if (schieneSchuelerzahl == 0)
			throw new DeveloperNotificationException("schieneSchuelerzahl == 0 --> Entfernen unmöglich!");
		_map_schienenID_schuelerAnzahl.put(pSchienenID, schieneSchuelerzahl - 1);

		// Schiene --> Schüler --> Integer (verringern)
		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
		kursmenge.remove(pKurs);

		// Kollisionen verringern?
		if (!kursmenge.isEmpty()) {
			// Kollisionen der Schiene.
			final int schieneKollisionen = getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID);
			if (schieneKollisionen == 0)
				throw new DeveloperNotificationException("schieneKollisionen == 0 --> Entfernen unmöglich!");
			_map_schienenID_kollisionen.put(pSchienenID, schieneKollisionen - 1);

			// Kollisionen des Schülers.
			final int schuelerKollisionen = getOfSchuelerAnzahlKollisionen(pSchuelerID);
			if (schuelerKollisionen == 0)
				throw new DeveloperNotificationException("schuelerKollisionen == 0 --> Entfernen unmöglich!");
			_map_schuelerID_kollisionen.put(pSchuelerID, schuelerKollisionen - 1);

			// Kollisionen insgesamt.
			if (_ergebnis.bewertung.anzahlSchuelerKollisionen == 0)
				throw new DeveloperNotificationException("Gesamtkollisionen == 0 --> Entfernen unmöglich!");
			_ergebnis.bewertung.anzahlSchuelerKollisionen--;
		}
	}

	private void stateKursdifferenzUpdate(final long pFachartID) {
		// Alte Kursdifferenz ist noch gespeichert.
		final int oldKD = getOfFachartKursdifferenz(pFachartID);

		// Neue Kursdifferenz wird berechnet
		final GostBlockungsergebnisKurs kurs1 = getOfFachartKursmenge(pFachartID).get(0);
		if (kurs1 == null)
			throw new DeveloperNotificationException("Fachart-ID " + pFachartID + " ohne Kurs!");
		int min = kurs1.schueler.size();
		int max = min;
		for (final @NotNull GostBlockungsergebnisKurs kurs : getOfFachartKursmenge(pFachartID)) {
			final int size = kurs.schueler.size();
			min = Math.min(min, size);
			max = Math.max(max, size);
		}
		final int newKD = max - min;

		// Kursdifferenz ändert sich nicht.
		if (newKD == oldKD)
			return;

		// Kursdifferenz ändert sich.
		_map_fachartID_kursdifferenz.put(pFachartID, newKD);

		final int[] kursdifferenzen = _ergebnis.bewertung.kursdifferenzHistogramm;
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
	 * Liefert den zugehörigen Daten-Manager für diesen Ergebnis-Manager zurück.
	 *
	 * @return Der Daten-Manager, der zu diesem Ergebnis-Manager gehört.
	 */
	public GostBlockungsdatenManager getParent() {
		return _parent;
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
		final double summe = getOfBewertung1Wert();
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
		final double summe = getOfBewertung2Wert();
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
		final int wert = getOfBewertung4Wert();
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
	 * Ermittelt das Fach für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return Das GostFach-Objekt.
	 * @throws DeveloperNotificationException im Falle, dass die ID nicht bekannt ist.
	 */
	public @NotNull GostFach getFach(final long pFachID) throws DeveloperNotificationException {
		return _parent.faecherManager().getOrException(pFachID);
	}

	/**
	 * Liefert die Menge aller Kurse mit dem angegebenen Fach. <br>
	 * Wirft eine Exception, wenn der ID kein Kurs zugeordnet ist.
	 *
	 * @param pFachID  Die Datenbank-ID des Faches.
	 * @return         Die Menge aller Kurse mit dem angegebenen Fach.
	 */
	public @NotNull ArrayList<@NotNull GostBlockungsergebnisKurs> getOfFachKursmenge(final long pFachID) {
		final ArrayList<@NotNull GostBlockungsergebnisKurs> kurseOfFach = _map_fachID_kurse.get(pFachID);
		if (kurseOfFach == null)
			throw new DeveloperNotificationException("Fach-ID " + pFachID + " unbekannt!");
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
	 * @throws DeveloperNotificationException Falls die Fachart-ID unbekannt ist.
	 */
	public @NotNull ArrayList<@NotNull GostBlockungsergebnisKurs> getOfFachartKursmenge(final long pFachartID) throws DeveloperNotificationException {
		final ArrayList<@NotNull GostBlockungsergebnisKurs> fachartGruppe = _map_fachartID_kurse.get(pFachartID);
		if (fachartGruppe == null)
			throw new DeveloperNotificationException("Fachart-ID " + pFachartID + " unbekannt!");
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
	 * @throws DeveloperNotificationException Falls die Fachart-ID unbekannt ist.
	 */
	public int getOfFachartKursdifferenz(final long pFachartID) throws DeveloperNotificationException {
		final Integer kursdifferenz = _map_fachartID_kursdifferenz.get(pFachartID);
		if (kursdifferenz == null)
			throw new DeveloperNotificationException("Fachart-ID " + pFachartID + " unbekannt!");
		return kursdifferenz;
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Das Schueler-Objekt.
	 * @throws     DeveloperNotificationException im Falle, dass die ID nicht bekannt ist
	 */
	public @NotNull Schueler getSchuelerG(final long pSchuelerID) throws DeveloperNotificationException {
		return _parent.getSchueler(pSchuelerID);
	}

	/**
	 * Liefert einen Schüler-String im Format: 'Nachname, Vorname'.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 *
	 * @return             Einen Schüler-String im Format: 'Nachname, Vorname'.
	 */
	public @NotNull String getOfSchuelerNameVorname(final long pSchuelerID) {
		final @NotNull Schueler schueler = _parent.getSchueler(pSchuelerID);
		return schueler.nachname + ", " + schueler.vorname;
	}


	/**
	 * Liefert die Menge aller Kurse, die dem Schüler zugeordnet sind. <br>
	 * Wirft eine Exception, wenn der ID kein Schüler zugeordnet ist.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller Kurse, die dem Schüler zugeordnet sind.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getOfSchuelerKursmenge(final long pSchuelerID) {
		final HashSet<@NotNull GostBlockungsergebnisKurs> kursIDs = _map_schuelerID_kurse.get(pSchuelerID);
		if (kursIDs == null)
			throw new DeveloperNotificationException("Schüler-ID " + pSchuelerID + " unbekannt!");
		return kursIDs;
	}

	/**
	 * Liefert die Menge aller Kurse des Schülers mit Kollisionen.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller Kurse des Schülers mit Kollisionen.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getOfSchuelerKursmengeMitKollisionen(final long pSchuelerID) {
		final @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisKurs>> mapSchieneKurse = getOfSchuelerSchienenKursmengeMap(pSchuelerID);

		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();
		for (final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kurseDerSchiene : mapSchieneKurse.values())
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
	public boolean getOfSchuelerHatKollision(final long pSchuelerID) {
		return getOfSchuelerAnzahlKollisionen(pSchuelerID) > 0;
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Nichtwahl hat. <br>
	 *
	 * @param pSchuelerID  Die Datenbank-ID des Schülers.
	 * @return             TRUE, falls der Schüler mindestens eine Nichtwahl hat.
	 */
	public boolean getOfSchuelerHatNichtwahl(final long pSchuelerID) {
		final @NotNull HashMap<@NotNull Long, GostBlockungsergebnisKurs> map = getOfSchuelerFachIDKursMap(pSchuelerID);

		for (final @NotNull Long fachID : map.keySet())
			if (map.get(fachID) == null)
				return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 *
	 * @param pSchuelerID   Die Datenbank.ID des Schülers.
	 * @param pFach         Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param pKursart      Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 * @return              TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 */
	public boolean getOfSchuelerHatFachwahl(final long pSchuelerID, final long pFach, final long pKursart) {
		return _parent.getOfSchuelerHatFachart(pSchuelerID, pFach, pKursart);
	}

	/**
	 * Liefert die Anzahl an Kollisionen des Schülers. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Anzahl an Kollisionen des Schülers.
	 */
	public int getOfSchuelerAnzahlKollisionen(final long pSchuelerID) {
		final Integer anzahl = _map_schuelerID_kollisionen.get(pSchuelerID);
		if (anzahl == null)
			throw new DeveloperNotificationException("Schüler-ID " + pSchuelerID + " unbekannt!");
		return anzahl;
	}

	/**
	 * Liefert TRUE, falls alle Schüler-Fachwahlen noch nicht zugeordnet sind.
	 *
	 * @return TRUE, falls alle Schüler-Fachwahlen noch nicht zugeordnet sind.
	 */
	public boolean getOfSchuelerAlleFachwahlenNichtZugeordnet() {
		return _ergebnis.bewertung.anzahlSchuelerNichtZugeordnet == _parent.daten().fachwahlen.size();
	}

	/**
	 * Liefert die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 */
	public @NotNull HashMap<@NotNull Long, GostBlockungsergebnisKurs> getOfSchuelerFachIDKursMap(final long pSchuelerID) {
		final HashMap<@NotNull Long, GostBlockungsergebnisKurs> mapFachKurs = _map_schuelerID_fachID_kurs.get(pSchuelerID);
		if (mapFachKurs == null)
			throw new DeveloperNotificationException("Schüler-ID " + pSchuelerID + " unbekannt!");
		return mapFachKurs;
	}

	/**
	 * Liefert die Map des Schülers, die einer Schiene die Kurse des Schülers zuordnet.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Map des Schülers, die der Schiene die Kurse des Schülers zuordnet.
	 */
	public @NotNull HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisKurs>> getOfSchuelerSchienenKursmengeMap(final long pSchuelerID) {
		final HashMap<@NotNull Long, @NotNull HashSet<@NotNull GostBlockungsergebnisKurs>> mapSchuelerKurse = _map_schuelerID_schienenID_kurse.get(pSchuelerID);
		if (mapSchuelerKurse == null)
			throw new DeveloperNotificationException("Schüler-ID " + pSchuelerID + " unbekannt!");
		return mapSchuelerKurse;
	}

	/**
	 * Liefert die Kursmenge des Schülers in der Schiene.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Kursmenge des Schülers in der Schiene.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getOfSchuelerOfSchieneKursmenge(final long pSchuelerID, final long pSchienenID) {
		final HashSet<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfSchuelerSchienenKursmengeMap(pSchuelerID).get(pSchienenID);
		if (kursmenge == null)
			throw new DeveloperNotificationException("Schüler-ID=" + pSchuelerID + ", Schienen-ID=" + pSchienenID +  " unbekannt!");
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
	public @NotNull GostKursart getOfSchuelerOfFachKursart(final long pSchuelerID, final long pFachID) {
		return _parent.getOfSchuelerOfFachKursart(pSchuelerID, pFachID);
	}

	/**
	 * Ermittelt für den Schüler mit einem Fach den zugeordneten Kurs.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pFachID     Die Datenbank-ID des Faches.
	 * @return             Der zugeordnete Kurs oder null, falls es keine Zuordnung gibt.
	 */
	public GostBlockungsergebnisKurs getOfSchuelerOfFachZugeordneterKurs(final long pSchuelerID, final long pFachID) {
		if (!getOfSchuelerFachIDKursMap(pSchuelerID).containsKey(pFachID))
			throw new DeveloperNotificationException("Schüler " + pSchuelerID + " hat das Fach " + pFachID + " nicht gewählt!");
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
	public boolean getOfSchuelerOfKursIstZugeordnet(final long pSchuelerID, final long pKursID) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(pSchuelerID);
		return kurseOfSchueler.contains(kurs);
	}

	/**
	 * Liefert für den übergebenen Schüler einen Vorschlag für eine Neuzuordnung der Kurse.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return             Die Neuzuordnung der Kurse im {@link SchuelerblockungOutput}-Objekt.
	 */
	public @NotNull SchuelerblockungOutput getOfSchuelerNeuzuordnung(final long pSchuelerID) {
		final @NotNull SchuelerblockungInput input = new SchuelerblockungInput();

		// Aktuelle Anzahl an Schienen.
		input.schienen = this.getOfSchieneAnzahl();

		// Fachwahlen des Schülers.
		final @NotNull List<@NotNull GostFachwahl> fachwahlenDesSchuelers = _parent.getOfSchuelerFacharten(pSchuelerID);
		input.fachwahlen.addAll(fachwahlenDesSchuelers);

		for (final @NotNull GostFachwahl fachwahl : fachwahlenDesSchuelers) {
			final @NotNull String representation = _parent.getNameOfFachwahl(fachwahl);
			input.fachwahlenText.add(representation);
		}

		// Alle für den Schüler wählbaren Kurse.
		for (final GostFachwahl fachwahl : fachwahlenDesSchuelers) {
			final long fachartID = GostKursart.getFachartIDByFachwahl(fachwahl);
			final @NotNull ArrayList<@NotNull GostBlockungsergebnisKurs> kurse = getOfFachartKursmenge(fachartID);
			for (final @NotNull GostBlockungsergebnisKurs kurs : kurse) {
				final @NotNull SchuelerblockungInputKurs inKurs = new SchuelerblockungInputKurs();
				inKurs.id = kurs.id;
				inKurs.fach = kurs.fachID;
				inKurs.kursart = kurs.kursart;
				inKurs.istGesperrt = getOfSchuelerOfKursIstGesperrt(pSchuelerID, kurs.id);
				inKurs.istFixiert = getOfSchuelerOfKursIstFixiert(pSchuelerID, kurs.id);
				inKurs.anzahlSuS = getOfKursAnzahlSchueler(kurs.id);
				inKurs.schienen = getOfKursSchienenNummern(kurs.id);
				input.kurse.add(inKurs);
			}
		}

		// Sonderfall: Der Schüler hat 0 Fachwahlen oder alle Fachwahlen haben 0 Kurse.
		if (input.kurse.isEmpty())
			return new SchuelerblockungOutput();

		// Mit dem Algorithmus die Zuordnung berechnen.
		final @NotNull SchuelerblockungAlgorithmus algorithmus = new SchuelerblockungAlgorithmus();
		final @NotNull SchuelerblockungOutput output = algorithmus.handle(input);
		return output;
	}

	/**
	 * Liefert TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 *
	 * @param pSchuelerID  Die Datenbank-ID des Schülers.
	 * @param pKursID      Die Datenbank-ID des Kurses.
	 * @return             TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 */
	public boolean getOfSchuelerOfKursIstFixiert(final long pSchuelerID, final long pKursID) {
		for (final @NotNull GostBlockungRegel r : _parent.getMengeOfRegeln()) {
			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);

			if (typ == GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS) {
				final long schuelerID = r.parameter.get(0);
				final long kursID = r.parameter.get(1);
				if ((schuelerID == pSchuelerID) && (kursID == pKursID))
					return true;
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Schüler im Kurs via Regel gesperrt sein soll.
	 *
	 * @param pSchuelerID  Die Datenbank-ID des Schülers.
	 * @param pKursID      Die Datenbank-ID des Kurses.
	 * @return             TRUE, falls der Schüler im Kurs via Regel gesperrt sein soll.
	 */
	public boolean getOfSchuelerOfKursIstGesperrt(final long pSchuelerID, final long pKursID) {
		for (final @NotNull GostBlockungRegel r : _parent.getMengeOfRegeln()) {
			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);

			if (typ == GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS) {
				final long schuelerID = r.parameter.get(0);
				final long kursID = r.parameter.get(1);
				if ((schuelerID == pSchuelerID) && (kursID == pKursID))
					return true;
			}
		}
		return false;
	}

	/**
	 * Ermittelt den Kurs für die angegebene ID. Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param  pKursID Die ID des Kurses.
	 * @return Das GostBlockungKurs-Objekt.
	 * @throws DeveloperNotificationException im Falle, dass die ID nicht bekannt ist.
	 */
	public @NotNull GostBlockungKurs getKursG(final long pKursID) throws DeveloperNotificationException {
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
	public @NotNull GostBlockungsergebnisKurs getKursE(final long pKursID) {
		final GostBlockungsergebnisKurs kurs = _map_kursID_kurs.get(pKursID);
		if (kurs == null)
			throw new DeveloperNotificationException("Kurs-ID " + pKursID + " unbekannt!");
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
	public @NotNull String getOfKursName(final long pKursID) {
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
	public boolean getOfKursOfSchieneIstZugeordnet(final long pKursID, final long pSchienenID) {
		final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(pSchienenID);
		final @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(pKursID);
		return schienenOfKurs.contains(schiene);
	}

	/**
	 * Liefert die Menge aller Schüler-IDs, die dem Kurs zugeordnet sind. <br>
	 * Wirft eine Exception, wenn der ID kein Kurs zugeordnet ist.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return Die Menge aller Schüler-IDs, die dem Kurs zugeordnet sind.
	 */
	public @NotNull HashSet<@NotNull Long> getOfKursSchuelerIDmenge(final long pKursID) {
		final HashSet<@NotNull Long> schuelerIDs = _map_kursID_schuelerIDs.get(pKursID);
		if (schuelerIDs == null)
			throw new DeveloperNotificationException("Kurs-ID " + pKursID + " unbekannt!");
		return schuelerIDs;
	}

	/**
	 * Liefert die Schienenmenge des Kurses.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return Die Schienenmenge des Kurses.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> getOfKursSchienenmenge(final long pKursID) {
		final HashSet<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = _map_kursID_schienen.get(pKursID);
		if (schienenOfKurs == null)
			throw new DeveloperNotificationException("Kurs-ID " + pKursID + " unbekannt!");
		return schienenOfKurs;
	}

	/**
	 * Liefert ein Array aller Schienen-Nummern des Kurses.
	 *
	 * @param pKursID  Die Datenbank-ID des Kurses.
	 * @return         Ein Array aller Schienen-Nummern des Kurses.
	 */
	public @NotNull int[] getOfKursSchienenNummern(final long pKursID) {
		final @NotNull ArrayList<@NotNull Long> schienenIDs = getKursE(pKursID).schienen;
		final int[] a = new int[schienenIDs.size()];
		for (int i = 0; i < a.length; i++) {
			final long schienenID = schienenIDs.get(i);
			a[i] = _parent.getSchiene(schienenID).nummer;
		}
		return a;
	}

	/**
	 * Liefert TRUE, falls der Kurs mindestens eine Kollision hat. <br>
	 * Kollision: Der Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 *
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return TRUE, falls der Kurs mindestens eine Kollision hat.
	 */
	public boolean getOfKursHatKollision(final long pKursID) {
		for (final @NotNull GostBlockungsergebnisSchiene schiene :  getOfKursSchienenmenge(pKursID))
			for (final @NotNull Long schuelerID : getKursE(pKursID).schueler)
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
	public int getOfKursAnzahlKollisionen(final long pKursID) {
		int summe = 0;
		for (final @NotNull GostBlockungsergebnisSchiene schiene :  getOfKursSchienenmenge(pKursID))
			for (final @NotNull Long schuelerID : getKursE(pKursID).schueler)
				if (getOfSchuelerOfSchieneKursmenge(schuelerID, schiene.id).size() > 1)
					summe++;
		return summe;
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet.
	 *
	 * @param  pKursID  Die Datenbank-ID des Kurses.
	 * @return          Die Anzahl an Schülern die dem Kurs zugeordnet.
	 */
	public int getOfKursAnzahlSchueler(final long pKursID) {
		final @NotNull GostBlockungsergebnisKurs kursE = getKursE(pKursID);
		return kursE.schueler.size();
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn schriftlich belegt haben.
	 *
	 * @param  pKursID  Die Datenbank-ID des Kurses.
	 * @return          Die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn schriftlich belegt haben.
	 */
	public int getOfKursAnzahlSchuelerSchriftlich(final long pKursID) {
		final @NotNull GostBlockungsergebnisKurs kursE = getKursE(pKursID);
		final long fachID = kursE.fachID;

		int summe = 0;
		for (final @NotNull Long schuelerID : kursE.schueler) {
			final @NotNull GostFachwahl fachwahl = _parent.getOfSchuelerOfFachFachwahl(schuelerID, fachID);
			if (fachwahl.istSchriftlich)
				summe++;
		}

		return summe;
	}

	/**
	 * Liefert die Anzahl an Schienen in denen der Kurs gerade ist.
	 *
	 * @param kursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schienen in denen der Kurs gerade ist.
	 */
	public int getOfKursAnzahlSchienenIst(final long kursID) {
		return getOfKursSchienenmenge(kursID).size();
	}

	/**
	 * Liefert die Anzahl an Schienen, die der Kurs haben sollte.
	 *
	 * @param kursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schienen, die der Kurs haben sollte.
	 */
	public int getOfKursAnzahlSchienenSoll(final long kursID) {
		return getKursE(kursID).anzahlSchienen;
	}

	/**
	 * Liefert die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 *
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return Die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 */
	public @NotNull HashSet<@NotNull Long> getOfKursSchuelermengeMitKollisionen(final long pKursID) {
		final @NotNull HashSet<@NotNull Long> set = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisSchiene schiene :  getOfKursSchienenmenge(pKursID))
			for (final @NotNull Long schuelerID : getKursE(pKursID).schueler)
				if (getOfSchuelerOfSchieneKursmenge(schuelerID, schiene.id).size() > 1)
					set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Es dürfen keine Schüler dem Kurs zugeordnet sein.
	 *
	 * @param  pKursID               Die Datenbank-ID des Kurses.
	 * @return                       TRUE, falls ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException  Falls der Kurs nicht existiert.
	 */
	public boolean getOfKursRemoveAllowed(final long pKursID) throws DeveloperNotificationException {
		return getKursE(pKursID).schueler.isEmpty();
	}

	/**
	 * Ermittelt die Schiene für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes
	 * {@link GostBlockungsdatenManager}. <br>
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param pSchienenID Die ID der Schiene
	 * @return Das GostBlockungSchiene-Objekt.
	 * @throws DeveloperNotificationException im Falle, dass die ID nicht bekannt ist.
	 */
	public @NotNull GostBlockungSchiene getSchieneG(final long pSchienenID) throws DeveloperNotificationException {
		return _parent.getSchiene(pSchienenID);
	}

	/**
	 * Liefert die Schiene  zur übergebenen ID. <br>
	 * Wirft eine Exception, wenn der ID keine Schiene zugeordnet ist.
	 *
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Schiene (des Blockungsergebnisses) zur übergebenen ID.
	 */
	public @NotNull GostBlockungsergebnisSchiene getSchieneE(final long pSchienenID) {
		final GostBlockungsergebnisSchiene schiene = _map_schienenID_schiene.get(pSchienenID);
		if (schiene == null)
			throw new DeveloperNotificationException("Schienen-ID " + pSchienenID + " unbekannt!");
		return schiene;
	}

	/**
	 * Liefert die Schiene mit der übergebenen Schienen-NR. <br>
	 * Wirft eine Exception, wenn eine Schiene mit NR nicht existiert.
	 *
	 * @param pSchienenNr Die Nummer der Schiene.
	 * @return Die Schiene mit der übergebenen Schienen-NR.
	 */
	public @NotNull GostBlockungsergebnisSchiene getSchieneEmitNr(final int pSchienenNr) {
		final GostBlockungsergebnisSchiene schiene = _map_schienenNr_schiene.get(pSchienenNr);
		if (schiene == null)
			throw new DeveloperNotificationException("Schienen-NR " + pSchienenNr + " unbekannt!");
		return schiene;
	}

	/**
	 * Liefert die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück. <br>
	 * Falls ein Schüler mehrfach in der Schiene ist, wird er mehrfach gezählt!
	 *
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Anzahl an Schülern in der Schiene.
	 */
	public int getOfSchieneAnzahlSchueler(final long pSchienenID) {
		final Integer anzahl = _map_schienenID_schuelerAnzahl.get(pSchienenID);
		if (anzahl == null)
			throw new DeveloperNotificationException("Schienen-ID " + pSchienenID + " unbekannt!");
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
	public boolean getOfSchieneHatKollision(final long pSchienenID) {
		return getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID) > 0;
	}

	/**
	 * Liefert die Anzahl an Schüler-Kollisionen der Schiene zurück. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Anzahl an Schüler-Kollisionen in der Schiene.
	 */
	public int getOfSchieneAnzahlSchuelerMitKollisionen(final long pSchienenID) {
		final Integer anzahl = _map_schienenID_kollisionen.get(pSchienenID);
		if (anzahl == null)
			throw new DeveloperNotificationException("Schienen-ID " + pSchienenID + " unbekannt!");
		return anzahl;
	}

	/**
	 * Liefert die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 *
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 */
	public @NotNull HashSet<@NotNull Long> getOfSchieneSchuelermengeMitKollisionen(final long pSchienenID) {
		final @NotNull HashSet<@NotNull Long> set = new HashSet<>();
		for (final @NotNull Long schuelerID : _map_schuelerID_kollisionen.keySet())
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
	public int getOfSchieneAnzahlKursmengeMitKollisionen(final long pSchienenID) {
		int summe = 0;
		for (final @NotNull GostBlockungsergebnisKurs kurs : getSchieneE(pSchienenID).kurse)
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
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getOfSchieneKursmengeMitKollisionen(final long pSchienenID) {
		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisKurs kurs : getSchieneE(pSchienenID).kurse)
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
	public @NotNull HashMap<@NotNull Long, @NotNull ArrayList<@NotNull GostBlockungsergebnisKurs>> getOfSchieneFachartKursmengeMap(final long pSchienenID) {
		final HashMap<@NotNull Long, @NotNull ArrayList<@NotNull GostBlockungsergebnisKurs>> map = _map_schienenID_fachartID_kurse.get(pSchienenID);
		if (map == null)
			throw new DeveloperNotificationException("Die Schienen-ID " + pSchienenID + " ist unbekannt!");
		return map;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist. <br>
	 * Kriterium: Es dürfen keine Kurse der Schiene zugeordnet sein.
	 *
	 * @param  pSchienenID          Die Datenbank-ID der Schiene.
	 * @return                      TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert.
	 */
	public boolean getOfSchieneRemoveAllowed(final long pSchienenID) throws DeveloperNotificationException {
		return getSchieneE(pSchienenID).kurse.isEmpty();
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
		final @NotNull HashSet<@NotNull Long> set = new HashSet<>();

		for (final @NotNull Long schuelerID : _map_schuelerID_kollisionen.keySet())
			if (getOfSchuelerHatKollision(schuelerID))
				set.add(schuelerID);

		return set;
	}

	/**
	 * Liefert eine Menge aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 *
	 * @return Eine Menge aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 */
	public @NotNull HashSet<@NotNull Long> getMengeDerSchuelerMitKollisionenOderNichtwahlen() {
		final @NotNull HashSet<@NotNull Long> set = new HashSet<>();

		for (final @NotNull Long schuelerID : _map_schuelerID_kollisionen.keySet())
			if (getOfSchuelerHatKollision(schuelerID) || getOfSchuelerHatNichtwahl(schuelerID))
				set.add(schuelerID);

		return set;
	}

	/**
	 * Liefert eine gefilterte Menge aller Schüler.
	 *
	 * @param  pKursID      Falls > 0, werden Schüler des Kurses herausgefiltert.
	 * @param  pFachID      Falls > 0 und
	 * @param  pKursartID   falls > 0, werden Schüler mit dieser Fachwahl herausgefiltert.
	 * @param  pKonfliktTyp Falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param  pSubString   Falls |pSubString| > 0 werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 *
	 * @return eine gefilterte Menge aller Schüler.
	 */
	public @NotNull ArrayList<@NotNull Schueler> getMengeDerSchuelerGefiltert(final int pKursID, final int pFachID, final int pKursartID, final int pKonfliktTyp, final @NotNull String pSubString) {
		final @NotNull ArrayList<@NotNull Schueler> menge = new ArrayList<>();

		for (final @NotNull Schueler schueler : _parent.getMengeOfSchueler()) {
			final long pSchuelerID = schueler.id;
			if ((pKonfliktTyp == 1) && (!getOfSchuelerHatKollision(pSchuelerID)))
				continue;
			if ((pKonfliktTyp == 2) && (!getOfSchuelerHatNichtwahl(pSchuelerID)))
				continue;
			if ((pKonfliktTyp == 3) && ((!getOfSchuelerHatKollision(pSchuelerID)) && (!getOfSchuelerHatNichtwahl(pSchuelerID))))
				continue;
			if ((pSubString.length() > 0) && (!getOfSchuelerHatImNamenSubstring(pSchuelerID, pSubString)))
				continue;
			if ((pKursID > 0) && (!getOfSchuelerOfKursIstZugeordnet(pSchuelerID, pKursID)))
				continue;
			if (((pFachID > 0) && (pKursartID > 0)) &&  (!getOfSchuelerHatFachwahl(pSchuelerID, pFachID, pKursartID)))
				continue;
			// Der Schüler entspricht allen Filterkriterien.
			menge.add(schueler);
		}

		return menge;
	}


	/**
	 * Liefert TRUE, falls der Sub-String im Nachnamen oder im Vornamen des Schülers vorkommt.
	 * Groß- und Kleinschreibung wird dabei ignoriert.
	 *
	 * @param pSchuelerID  Die Datenbank-ID des Schülers.
	 * @param pSubString   Der zu suchende Sub-String.
	 * @return             TRUE, falls der Sub-String im Nachnamen oder im Vornamen des Schülers vorkommt.
	 */
	public boolean getOfSchuelerHatImNamenSubstring(final long pSchuelerID, final @NotNull String pSubString) {
		final @NotNull Schueler schueler = getSchuelerG(pSchuelerID);
		final @NotNull String text = pSubString.toLowerCase();
		if (schueler.nachname.toLowerCase().contains(text))
			return true;
		return (schueler.vorname.toLowerCase().contains(text));
	}

	/**
	 * Liefert die Anzahl aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 *
	 * @return Die Anzahl aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 */
	public int getAnzahlDerSchuelerMitKollisionenOderNichtwahlen() {
		return getMengeDerSchuelerMitKollisionenOderNichtwahlen().size();
	}

	/**
	 * Liefert eine Menge aller Kurse mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Kurse mit mindestens einer Kollision.
	 */
	public @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> getMengeDerKurseMitKollisionen() {
		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisKurs kurs : _map_kursID_kurs.values())
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
		final @NotNull HashSet<@NotNull GostBlockungsergebnisSchiene> set = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisSchiene schiene : _map_schienenID_schiene.values())
			if (getOfSchieneHatKollision(schiene.id))
				set.add(schiene);
		return set;
	}

	/**
	 * Liefert die Menge aller Schienen.
	 *
	 * @return Die Menge aller Schienen.
	 */
	public @NotNull ArrayList<@NotNull GostBlockungsergebnisSchiene> getMengeAllerSchienen() {
		return _ergebnis.schienen;
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene. Die Schiene wird anhand ihrer Nummer (nicht die Datenbank-ID)
	 * identifiziert.
	 *
	 * @param  pKursID              Die Datenbank-ID des Kurses.
	 * @param  pSchienenNr          Die Nummer der Schiene (nicht die Datenbank-ID).
	 *
	 * @throws DeveloperNotificationException Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public void setKursSchienenNr(final long pKursID, final int pSchienenNr) throws DeveloperNotificationException {
		final GostBlockungsergebnisSchiene eSchiene = _map_schienenNr_schiene.get(pSchienenNr);
		if (eSchiene == null)
			throw new DeveloperNotificationException("Schienen-Nr. " + pSchienenNr + " unbekannt!");
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
	 * @throws DeveloperNotificationException      Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public boolean setKursSchiene(final long pKursID, final long pSchienenID, final boolean pHinzufuegenOderEntfernen) throws DeveloperNotificationException {
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
	 * @throws DeveloperNotificationException      Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public boolean setSchuelerKurs(final long pSchuelerID, final long pKursID, final boolean pHinzufuegenOderEntfernen) throws DeveloperNotificationException {
		if (pHinzufuegenOderEntfernen)
			return stateSchuelerKursHinzufuegen(pSchuelerID, pKursID);
		return stateSchuelerKursEntfernen(pSchuelerID, pKursID);
	}

	/**
	 * Geht die übergebenen Zuordnungen (Fach --> Kurs) durch und setzt
	 * bei Veränderung Kurse des übergebenen Schülers neu.
	 *
	 * @param schuelerID  Die Datenbank-ID des Schülers.
	 * @param pZuordnung  Die gewünschte Zuordnung.
	 */
	public void setSchuelerNeuzuordnung(final long schuelerID, final @NotNull SchuelerblockungOutput pZuordnung) {

		for (final @NotNull SchuelerblockungOutputFachwahlZuKurs z : pZuordnung.fachwahlenZuKurs) {
			// Kurs des Faches 'vorher'.
			final GostBlockungsergebnisKurs kursV = getOfSchuelerOfFachZugeordneterKurs(schuelerID, z.fachID);
			// Kurs des Faches 'nachher'.
			final GostBlockungsergebnisKurs kursN = z.kursID < 0 ? null : getKursE(z.kursID);
			// Bei Ungleichheit wird der Kurs gewechselt.
			if (kursV != kursN) {
				if (kursV != null)
					setSchuelerKurs(schuelerID, kursV.id, false);
				if (kursN != null)
					setSchuelerKurs(schuelerID, kursN.id, true);
			}
		}

	}

	/**
	 * Fügt die übergebene Schiene hinzu.
	 *
	 * @param  pSchienenID           Die Datenbank-ID der Schiene.
	 * @throws DeveloperNotificationException  Falls die Schiene nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public void setAddSchieneByID(final long pSchienenID) throws DeveloperNotificationException {
		if (!_parent.getSchieneExistiert(pSchienenID))
			throw new DeveloperNotificationException("Die Schiene " + pSchienenID + " muss erst beim Datenmanager hinzugefügt werden!");

		stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Schiene.
	 *
	 * @param  pSchienenID           Die Datenbank-ID der Schiene.
	 * @throws DeveloperNotificationException  Falls die Schiene nicht zuerst beim Datenmanager entfernt wurde, oder
	 *                               falls die Schiene noch Kurszuordnungen hat.
	 */
	public void setRemoveSchieneByID(final long pSchienenID) throws DeveloperNotificationException {
		if (_parent.getSchieneExistiert(pSchienenID))
			throw new DeveloperNotificationException("Die Schiene " + pSchienenID + " muss erst beim Datenmanager entfernt werden!");

		final int nKurse = getSchieneE(pSchienenID).kurse.size();
		if (nKurse > 0)
			throw new DeveloperNotificationException("Entfernen unmöglich: Schiene " + pSchienenID + " hat noch " + nKurse + " Kurse!");

		stateRevalidateEverything();
	}

	/**
	 * Fügt die übergebene Regel hinzu.
	 *
	 * @param  pRegelID              Die Datenbank-ID der Regel.
	 * @throws DeveloperNotificationException  Falls die Regel nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public void setAddRegelByID(final long pRegelID) throws DeveloperNotificationException {
		if (!_parent.getRegelExistiert(pRegelID))
			throw new DeveloperNotificationException("Die Regel " + pRegelID + " muss erst beim Datenmanager hinzugefügt werden!");

		stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Regel.
	 *
	 * @param  pRegelID              Die Datenbank-ID der Regel.
	 * @throws DeveloperNotificationException  Falls die Regel nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public void setRemoveRegelByID(final long pRegelID) throws DeveloperNotificationException {
		if (_parent.getRegelExistiert(pRegelID))
			throw new DeveloperNotificationException("Die Regel " + pRegelID + " muss erst beim Datenmanager entfernt werden!");

		stateRevalidateEverything();
	}

	/**
	 * Fügt den übergebenen Kurs hinzu.
	 *
	 * @param  pKursID               Die Datenbank-ID des Kurses.
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager hinzugefügt wurde.
	 */
	public void setAddKursByID(final long pKursID) throws DeveloperNotificationException {
		if (!_parent.getKursExistiert(pKursID))
			throw new DeveloperNotificationException("Der Kurs " + pKursID + " muss erst beim Datenmanager hinzugefügt werden!");

		final @NotNull GostBlockungKurs kurs = _parent.getKurs(pKursID);
		final int nSchienen = _parent.getSchienenAnzahl();
		if (nSchienen < kurs.anzahlSchienen)
			throw new DeveloperNotificationException("Es gibt " + nSchienen + " Schienen, da passt ein Kurs mit " + kurs.anzahlSchienen + " nicht hinein!");

		stateRevalidateEverything(); // Muss vor 'setKursSchienenNr' aufgerufen werden.

		// Kurs automatisch in die ersten 'kurs.anzahlSchienen' Schienen hinzufügen.
		for (int nr = 1; nr <= kurs.anzahlSchienen; nr++)
			setKursSchienenNr(pKursID, nr);
	}

	/**
	 * Löscht den übergebenen Kurs.
	 *
	 * @param  pKursID               Die Datenbank-ID des Kurses.
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager entfernt wurde, oder
	 *                               falls der Kurs noch Schülerzuordnungen hat.
	 */
	public void setRemoveKursByID(final long pKursID) throws DeveloperNotificationException {
		if (_parent.getKursExistiert(pKursID))
			throw new DeveloperNotificationException("Der Kurs " + pKursID + " muss erst beim Datenmanager entfernt werden!");

		final int nSchueler = getKursE(pKursID).schueler.size();
		if (nSchueler > 0)
			throw new DeveloperNotificationException("Entfernen unmöglich: Kurs " + pKursID + " hat noch " + nSchueler + " Schüler!");

		// Kurs aus den Daten löschen, sonst wird die Kurs-Schienen-Zuordnung kopiert.
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		for (final @NotNull Long schienenID : kurs.schienen)
			getSchieneE(schienenID).kurse.remove(kurs);
		kurs.schienen.clear();

		stateRevalidateEverything();
	}

	/**
	 * Verändert die Schienenanzahl eines Kurses. Dies ist nur bei einer Blockungsvorlage erlaubt.
	 *
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @param  pAnzahlSchienenNeu Die neue Schienenanzahl des Kurses.
	 * @throws DeveloperNotificationException Falls ein unerwarteter Fehler passiert.
	 */
	public void patchOfKursSchienenAnzahl(final long pKursID, final int pAnzahlSchienenNeu) throws DeveloperNotificationException {
		// Daten holen.
		final @NotNull GostBlockungKurs kursG = getKursG(pKursID);
		final @NotNull GostBlockungsergebnisKurs kursE = getKursE(pKursID);
		final int nSchienen = _parent.getSchienenAnzahl();

		// DeveloperNotificationException
		if (!_parent.getIstBlockungsVorlage())
			throw new DeveloperNotificationException("Die Schienenanzahl eines Kurses darf nur bei der Blockungsvorlage verändert werden!");
		if (kursE.anzahlSchienen != kursG.anzahlSchienen)
			throw new DeveloperNotificationException("Der GostBlockungKurs hat " + kursG.anzahlSchienen + " Schienen, der GostBlockungsergebnisKurs hat hingegen " + kursE.anzahlSchienen + " Schienen!");
		if (nSchienen == 0)
			throw new DeveloperNotificationException("Die Blockung hat 0 Schienen. Das darf nicht passieren!");
		if (pAnzahlSchienenNeu <= 0)
			throw new DeveloperNotificationException("Ein Kurs muss mindestens einer Schiene zugeordnet sein, statt " + pAnzahlSchienenNeu + " Schienen!");
		if (pAnzahlSchienenNeu > nSchienen)
			throw new DeveloperNotificationException("Es gibt nur " + nSchienen + " Schienen, der Kurs kann nicht " + pAnzahlSchienenNeu + " Schienen zugeordnet werden!");

		// Die Schienenanzahl erhöhen, ggf. mehrfach.
		while (pAnzahlSchienenNeu > kursG.anzahlSchienen) {
			boolean hinzugefuegt = false;
			for (int nr = 1; (nr <= _map_schienenNr_schiene.size()) && (!hinzugefuegt); nr++) {
				final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneEmitNr(nr);
				if (!kursE.schienen.contains(schiene.id)) {
					hinzugefuegt = true;
					kursG.anzahlSchienen++;
					kursE.anzahlSchienen++;
					setKursSchiene(pKursID, schiene.id, true);
				}
			}
			if (!hinzugefuegt)
				throw new DeveloperNotificationException("Es wurde keine freie Schiene für den Kurs " + pKursID + " gefunden!");
		}

		// Die Schienenanzahl verringern, ggf. mehrfach.
		while (pAnzahlSchienenNeu < kursG.anzahlSchienen) {
			boolean entfernt = false;
			for (int nr = _map_schienenNr_schiene.size(); (nr >= 1) && (!entfernt); nr--) {
				final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneEmitNr(nr);
				if (kursE.schienen.contains(schiene.id)) {
					entfernt = true;
					kursG.anzahlSchienen--;
					kursE.anzahlSchienen--;
					setKursSchiene(pKursID, schiene.id, false);
				}
			}
			if (!entfernt)
				throw new DeveloperNotificationException("Es wurde keine belegte Schiene von Kurs " + pKursID + " gefunden!");
		}

	}

	/**
	 * Nur für Debug-Zwecke.
	 */
	public void debug() {
		//  TODO BAR Weitere Bewertungskriterien überprüfen (auch nach Entfernen).
		System.out.println("----- Kurse sortiert nach Fachart -----");
		for (final @NotNull Long fachartID : _map_fachartID_kurse.keySet()) {
			System.out.println("FachartID = " + fachartID + " (KD = " + getOfFachartKursdifferenz(fachartID) + ")");
			for (final @NotNull GostBlockungsergebnisKurs kurs : getOfFachartKursmenge(fachartID)) {
				System.out.println("    " + getOfKursName(kurs.id) + " : " + kurs.schueler.size() + " SuS");
			}
		}
		System.out.println("KursdifferenzMax = " + _ergebnis.bewertung.kursdifferenzMax);
		System.out.println("KursdifferenzHistogramm = " + Arrays.toString(_ergebnis.bewertung.kursdifferenzHistogramm));
	}


}
