package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.CollectionUtils;
import de.svws_nrw.core.utils.MapUtils;
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
	private final @NotNull Map<@NotNull Integer, @NotNull GostBlockungsergebnisSchiene> _map_schienenNr_schiene = new HashMap<>();

	/** Schienen-ID --> GostBlockungSchiene */
	private final @NotNull Map<@NotNull Long, @NotNull GostBlockungsergebnisSchiene> _map_schienenID_schiene = new HashMap<>();

	/** Schienen-ID --> Integer = Anzahl SuS */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_schienenID_schuelerAnzahl = new HashMap<>();

	/** Schienen-ID --> Anzahl Kollisionen */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_schienenID_kollisionen = new HashMap<>();

	/** Schienen-ID --> Fachart-ID --> ArrayList<Kurse> = Alle Kurse in der Schiene mit der Fachart. */
	private final @NotNull Map<@NotNull Long, @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostBlockungsergebnisKurs>>> _map_schienenID_fachartID_kurse = new HashMap<>();

	/** Schüler-ID --> Set<GostBlockungsergebnisKurs> */
	private final @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> _map_schuelerID_kurse = new HashMap<>();

	/** Schüler-ID --> Set<GostBlockungsergebnisKurs> */
	private final @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> _map_schuelerID_ungueltige_kurse = new HashMap<>();

	/** Schüler-ID --> Integer (Kollisionen) */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_schuelerID_kollisionen = new HashMap<>();

	/** Schüler-ID --> Fach-ID --> GostBlockungsergebnisKurs */
	private final @NotNull Map<@NotNull Long, @NotNull Map<@NotNull Long, GostBlockungsergebnisKurs>> _map_schuelerID_fachID_kurs = new HashMap<>();

	/** Schüler-ID --> Schienen-ID --> Set<GostBlockungsergebnisKurs> = Alle Kurse des Schülers in der Schiene.*/
	private final @NotNull Map<@NotNull Long, @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>>> _map_schuelerID_schienenID_kurse = new HashMap<>();

	/** Kurs-ID --> Set<GostBlockungsergebnisSchiene> */
	private final @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisSchiene>> _map_kursID_schienen = new HashMap<>();

	/** Kurs-ID --> GostBlockungsergebnisKurs */
	private final @NotNull Map<@NotNull Long, @NotNull GostBlockungsergebnisKurs> _map_kursID_kurs = new HashMap<>();

	/** Kurs-ID --> Set<SchuelerID> */
	private final @NotNull Map<@NotNull Long, @NotNull Set<@NotNull Long>> _map_kursID_schuelerIDs = new HashMap<>();

	/** Fach-ID --> ArrayList<GostBlockungsergebnisKurs> */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostBlockungsergebnisKurs>> _map_fachID_kurse = new HashMap<>();

	/** Fachart-ID --> ArrayList<GostBlockungsergebnisKurs> = Alle Kurse der selben Fachart. */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostBlockungsergebnisKurs>> _map_fachartID_kurse = new HashMap<>();

	/** Fachart-ID --> Integer = Kursdifferenz der Fachart. */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_fachartID_kursdifferenz = new HashMap<>();

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
		_map_schuelerID_ungueltige_kurse.clear();
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
			DeveloperNotificationException.ifMapPutOverwrites(_map_schienenNr_schiene, gSchiene.nummer, eSchiene);
			DeveloperNotificationException.ifMapPutOverwrites(_map_schienenID_schiene, gSchiene.id, eSchiene);
			DeveloperNotificationException.ifMapPutOverwrites(_map_schienenID_schuelerAnzahl, gSchiene.id, 0);
			DeveloperNotificationException.ifMapPutOverwrites(_map_schienenID_kollisionen, gSchiene.id, 0);
		}

		// Kurse von '_parent' kopieren und hinzufügen. Fachart-IDs erzeugen.
		for (final @NotNull GostBlockungKurs gKurs : _parent.daten().kurse) {
			// GostBlockungKurs --> GostBlockungsergebnisKurs
			final @NotNull GostBlockungsergebnisKurs eKurs = new GostBlockungsergebnisKurs();
			eKurs.id = gKurs.id;
			eKurs.fachID = gKurs.fach_id;
			eKurs.kursart = gKurs.kursart;
			eKurs.anzahlSchienen = gKurs.anzahlSchienen;
			_ergebnis.bewertung.anzahlKurseNichtZugeordnet += eKurs.anzahlSchienen;

			// Map: kursID --> Kurs-Objekt
			DeveloperNotificationException.ifMapPutOverwrites(_map_kursID_kurs, eKurs.id, eKurs);

			// Map: kursID --> Schienen des Kurses
			DeveloperNotificationException.ifMapPutOverwrites(_map_kursID_schienen, eKurs.id, new HashSet<@NotNull GostBlockungsergebnisSchiene>());

			// Map: kursID --> Schüler des Kurses
			DeveloperNotificationException.ifMapPutOverwrites(_map_kursID_schuelerIDs, eKurs.id, new HashSet<@NotNull Long>());

			// Map: fachID --> Kursliste
			final @NotNull List<@NotNull GostBlockungsergebnisKurs> fachKursliste = MapUtils.getOrCreateArrayList(_map_fachID_kurse, eKurs.fachID);
			fachKursliste.add(eKurs);

			// Map: fachartID --> Kursliste
			final long fachartID = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			final @NotNull List<@NotNull GostBlockungsergebnisKurs> facharKursliste = MapUtils.getOrCreateArrayList(_map_fachartID_kurse, fachartID);
			facharKursliste.add(eKurs);

			// Map: fachartID --> Kursdifferenz
			if (!_map_fachartID_kursdifferenz.containsKey(fachartID)) {
				_map_fachartID_kursdifferenz.put(fachartID, 0);
				_ergebnis.bewertung.kursdifferenzHistogramm[0]++;
			}
		}

		// Fachwahlen zu denen es keinen Kurs gibt der Map '_map_fachartID_kurse' hinzufügen.
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen)
			MapUtils.getOrCreateArrayList(_map_fachartID_kurse, GostKursart.getFachartIDByFachwahl(gFachwahl));

		// _map_schienenID_fachartID_kurse
		for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
			_map_schienenID_fachartID_kurse.put(gSchiene.id, new HashMap<>());
			for (final @NotNull Long fachartID : _map_fachartID_kursdifferenz.keySet())
				getOfSchieneFachartKursmengeMap(gSchiene.id).put(fachartID, new ArrayList<>());
		}

		// Schüler kopieren und hinzufügen.
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler) {
			// Map: schuelerID --> Kurs-Set
			DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_kurse, gSchueler.id, new HashSet<@NotNull GostBlockungsergebnisKurs>());
			// Map: schuelerID --> Anzahl Kollisionen
			DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_kollisionen, gSchueler.id, 0);
		}

		// Fachwahlen kopieren und hinzufügen.
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler)
			_map_schuelerID_fachID_kurs.put(gSchueler.id, new HashMap<>());
		final String strErrorUnbekannteFachwahl = "Schüler %d hat eine Fachwahl ist aber unbekannt!";
		final String strErrorDoppeltesFach = "Schüler %d hat Fach %d doppelt!";
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen) {
			final Map<@NotNull Long, GostBlockungsergebnisKurs> mapFachKurs = _map_schuelerID_fachID_kurs.get(gFachwahl.schuelerID);
			if (mapFachKurs == null)
				throw new DeveloperNotificationException(strErrorUnbekannteFachwahl.formatted(gFachwahl.schuelerID));
			if (mapFachKurs.put(gFachwahl.fachID, null) != null)
				throw new DeveloperNotificationException(strErrorDoppeltesFach.formatted(gFachwahl.schuelerID, gFachwahl.fachID));
		}

		// _map_schuelerID_schienenID_kurse
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler) {
			_map_schuelerID_schienenID_kurse.put(gSchueler.id, new HashMap<>());
			for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
				final HashSet<@NotNull GostBlockungsergebnisKurs> newSet = new HashSet<>();
				getOfSchuelerSchienenKursmengeMap(gSchueler.id).put(gSchiene.id, newSet);
			}
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
		final @NotNull List<@NotNull Long> regelVerletzungen = _ergebnis.bewertung.regelVerletzungen;
		regelVerletzungen.clear();

		for (final @NotNull GostBlockungRegel r : _parent.getMengeOfRegeln()) {
			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);
			switch (typ) {
				case SCHUELER_FIXIEREN_IN_KURS: // 4
					stateRegelvalidierung4_schueler_fixieren_in_kurs(r, regelVerletzungen);
					break;
				case SCHUELER_VERBIETEN_IN_KURS: // 5
					stateRegelvalidierung5_schueler_verbieten_in_kurs(r, regelVerletzungen);
					break;
				case KURS_FIXIERE_IN_SCHIENE: // 2
					stateRegelvalidierung2_kurs_fixieren_in_schiene(r, regelVerletzungen);
					break;
				case KURS_SPERRE_IN_SCHIENE: // 3
					stateRegelvalidierung3_kurs_sperren_in_schiene(r, regelVerletzungen);
					break;
				case KURSART_SPERRE_SCHIENEN_VON_BIS: // 1
					stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(r, regelVerletzungen);
					break;
				case KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: // 6
					stateRegelvalidierung6_kursart_allein_in_schiene_von_bis(r, regelVerletzungen);
					break;
				case KURS_VERBIETEN_MIT_KURS: // 7
					stateRegelvalidierung7_kurs_verbieten_mit_kurs(r, regelVerletzungen);
					break;
				case KURS_ZUSAMMEN_MIT_KURS: // 8
					stateRegelvalidierung8_kurs_zusammen_mit_kurs(r, regelVerletzungen);
					break;
				case LEHRKRAFT_BEACHTEN: // 9
					stateRegelvalidierung9_lehrkraft_beachten(r, regelVerletzungen);
					break;
				case LEHRKRAEFTE_BEACHTEN: // 10
					stateRegelvalidierung10_lehrkraefte_beachten(r, regelVerletzungen);
					break;
				default:
					throw new IllegalStateException("Der Regel-Typ ist unbekannt: " + typ);
			}
		}

		// Die Bewertung im DatenManager aktualisieren.
		_parent.updateErgebnisBewertung(_ergebnis);
	}

	private void stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen) {
		for (int schienenNr = r.parameter.get(1).intValue(); schienenNr <= r.parameter.get(2).intValue(); schienenNr++)
			for (final GostBlockungsergebnisKurs eKurs : getSchieneEmitNr(schienenNr).kurse)
				if (eKurs.kursart == r.parameter.get(0).intValue())
					regelVerletzungen.add(r.id);
	}

	private void stateRegelvalidierung2_kurs_fixieren_in_schiene(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen) {
		if (!getOfKursSchienenmenge(r.parameter.get(0)).contains(getSchieneEmitNr(r.parameter.get(1).intValue())))
			regelVerletzungen.add(r.id);
	}

	private void stateRegelvalidierung3_kurs_sperren_in_schiene(final @NotNull GostBlockungRegel r, final  @NotNull List<@NotNull Long> regelVerletzungen) {
		if (getOfKursSchienenmenge(r.parameter.get(0)).contains(getSchieneEmitNr(r.parameter.get(1).intValue())))
			regelVerletzungen.add(r.id);
	}

	private void stateRegelvalidierung4_schueler_fixieren_in_kurs(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen) {
		if (!getOfSchuelerOfKursIstZugeordnet(r.parameter.get(0), r.parameter.get(1)))
			regelVerletzungen.add(r.id);
	}

	private void stateRegelvalidierung5_schueler_verbieten_in_kurs(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen) {
		if (getOfSchuelerOfKursIstZugeordnet(r.parameter.get(0), r.parameter.get(1)))
			regelVerletzungen.add(r.id);
	}

	private void stateRegelvalidierung6_kursart_allein_in_schiene_von_bis(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen) {
		for (final GostBlockungsergebnisKurs eKurs : _map_kursID_kurs.values())
			for (final @NotNull Long eSchieneID : eKurs.schienen) {
				final int nr  = getSchieneG(eSchieneID).nummer;
				final boolean b1 = eKurs.kursart == r.parameter.get(0).intValue();
				final boolean b2 = (r.parameter.get(1).intValue() <= nr) && (nr <= r.parameter.get(2).intValue());
				if (b1 != b2)
					regelVerletzungen.add(r.id);
			}
	}

	private void stateRegelvalidierung7_kurs_verbieten_mit_kurs(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen) {
		for (final @NotNull GostBlockungsergebnisSchiene schiene1 : getOfKursSchienenmenge(r.parameter.get(0)))
			for (final @NotNull GostBlockungsergebnisSchiene schiene2 : getOfKursSchienenmenge(r.parameter.get(1)))
				if (schiene1 == schiene2)
					regelVerletzungen.add(r.id);
	}

	private void stateRegelvalidierung8_kurs_zusammen_mit_kurs(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen) {
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> set1 = getOfKursSchienenmenge(r.parameter.get(0));
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> set2 = getOfKursSchienenmenge(r.parameter.get(1));
		if (set1.size() < set2.size()) {
			// "set1" muss in "set2" enthalten sein.
			for (final @NotNull GostBlockungsergebnisSchiene schiene1 : set1)
				if (!set2.contains(schiene1))
					regelVerletzungen.add(r.id);
		} else {
			// "set2" muss in "set1" enthalten sein.
			for (final @NotNull GostBlockungsergebnisSchiene schiene2 : set2)
				if (!set1.contains(schiene2))
					regelVerletzungen.add(r.id);
		}
	}

	private void stateRegelvalidierung9_lehrkraft_beachten(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen) {
		final boolean externBeachten = r.parameter.get(0) == 1L;
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _map_schienenID_schiene.values())
			for (final @NotNull GostBlockungsergebnisKurs eKurs1 : eSchiene.kurse)
				for (final @NotNull GostBlockungsergebnisKurs eKurs2 : eSchiene.kurse)
					if (eKurs1.id < eKurs2.id)
						for (final @NotNull GostBlockungKursLehrer gLehr1 : getKursG(eKurs1.id).lehrer)
							for (final @NotNull GostBlockungKursLehrer gLehr2 : getKursG(eKurs2.id).lehrer)
								if ((gLehr1.id == gLehr2.id) && ((externBeachten) || (!gLehr1.istExtern)))
									regelVerletzungen.add(r.id);
	}

	private void stateRegelvalidierung10_lehrkraefte_beachten(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen) {
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _map_schienenID_schiene.values())
			for (final @NotNull GostBlockungsergebnisKurs eKurs1 : eSchiene.kurse)
				for (final @NotNull GostBlockungsergebnisKurs eKurs2 : eSchiene.kurse)
					if (eKurs1.id < eKurs2.id)
						for (final @NotNull GostBlockungKursLehrer gLehr1 : getKursG(eKurs1.id).lehrer)
							for (final @NotNull GostBlockungKursLehrer gLehr2 : getKursG(eKurs2.id).lehrer)
								if (gLehr1.id == gLehr2.id)
									regelVerletzungen.add(r.id);
	}

	/**
	 * Fügt den Schüler dem Kurs hinzu.<br>
	 * Hinweis: Ist die Wahl des Kurses für diesen Schüler ungültig, wird der Schüler nicht hinzugefügt.
	 *          Stattdessen wird diese ungültige Wahl gespeichert.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 */
	private void stateSchuelerKursHinzufuegen(final long pSchuelerID, final long pKursID) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		final long fachID = kurs.fachID;

		// Ungültige Wahlen extra behandeln.
		if (!getOfSchuelerHatFachwahl(pSchuelerID, fachID, kurs.kursart)) {
			stateSchuelerKursUngueltigeWahlHinzufuegen(pSchuelerID, kurs);
			return;
		}

		// Wurde das Fach bereits einem anderen Kurs zugeordnet?
		if (getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) != null) // wirft ggf. Exception
			return;

		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(pSchuelerID);
		final @NotNull Set<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(pKursID);
		final @NotNull Map<@NotNull Long, GostBlockungsergebnisKurs> mapSFK = getOfSchuelerFachIDKursMap(pSchuelerID);
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
	}

	/**
	 * Entfernt den Schüler aus dem Kurs.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 */
	private void stateSchuelerKursEntfernen(final long pSchuelerID, final long pKursID) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(pKursID);
		final long fachID = kurs.fachID;

		// Ungültige Wahlen extra behandeln.
		if (!getOfSchuelerHatFachwahl(pSchuelerID, fachID, kurs.kursart)) {
			stateSchuelerKursUngueltigeWahlEntfernen(pSchuelerID, kurs);
			return;
		}

		// Der Kurs ist derzeit gar nicht zugeordnet!
		if (getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) != kurs) // wirft ggf. Exception
			return;

		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(pSchuelerID);
		final @NotNull Set<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(pKursID);
		final @NotNull Map<@NotNull Long, GostBlockungsergebnisKurs> mapSFK = getOfSchuelerFachIDKursMap(pSchuelerID);
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
	}

	private void stateSchuelerKursUngueltigeWahlHinzufuegen(final long pSchuelerID, final @NotNull GostBlockungsergebnisKurs pKurs) {
		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> set = MapUtils.getOrCreateHashSet(_map_schuelerID_ungueltige_kurse, pSchuelerID);
		set.add(pKurs);
	}

	private void stateSchuelerKursUngueltigeWahlEntfernen(final long pSchuelerID, final @NotNull GostBlockungsergebnisKurs pKurs) {
	    final @NotNull Set<@NotNull GostBlockungsergebnisKurs> set = DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_ungueltige_kurse, pSchuelerID);
		set.remove(pKurs);
		if (set.isEmpty())
			_map_schuelerID_ungueltige_kurse.remove(pSchuelerID);
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
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(pKursID);
		final long fachID = kurs.fachID;
		final long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		if (schienenOfKurs.contains(schiene))
			return false;

		final List<@NotNull GostBlockungsergebnisKurs> kursGruppe = getOfSchieneFachartKursmengeMap(pSchienenID).get(fachartID);
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
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(pKursID);
		final long fachID = kurs.fachID;
		final long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		if (!schienenOfKurs.contains(schiene))
			return false;

		final List<@NotNull GostBlockungsergebnisKurs> kursGruppe = getOfSchieneFachartKursmengeMap(pSchienenID).get(fachartID);
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
		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
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
		DeveloperNotificationException.ifTrue("schieneSchuelerzahl == 0 --> Entfernen unmöglich!", schieneSchuelerzahl == 0);
		_map_schienenID_schuelerAnzahl.put(pSchienenID, schieneSchuelerzahl - 1);

		// Schiene --> Schüler --> Integer (verringern)
		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
		kursmenge.remove(pKurs);

		// Kollisionen verringern?
		if (!kursmenge.isEmpty()) {
			// Kollisionen der Schiene.
			final int schieneKollisionen = getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID);
			DeveloperNotificationException.ifTrue("schieneKollisionen == 0 --> Entfernen unmöglich!", schieneKollisionen == 0);
			_map_schienenID_kollisionen.put(pSchienenID, schieneKollisionen - 1);

			// Kollisionen des Schülers.
			final int schuelerKollisionen = getOfSchuelerAnzahlKollisionen(pSchuelerID);
			DeveloperNotificationException.ifTrue("schuelerKollisionen == 0 --> Entfernen unmöglich!", schuelerKollisionen == 0);
			_map_schuelerID_kollisionen.put(pSchuelerID, schuelerKollisionen - 1);

			// Kollisionen insgesamt.
			DeveloperNotificationException.ifTrue("Gesamtkollisionen == 0 --> Entfernen unmöglich!", _ergebnis.bewertung.anzahlSchuelerKollisionen == 0);
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

		if (oldKD == _ergebnis.bewertung.kursdifferenzMax) {
			if (newKD > oldKD) {
				// Neues Maximum
				_ergebnis.bewertung.kursdifferenzMax = newKD;
			} else {
				// Neues Minimum
				if (kursdifferenzen[oldKD] == 0)
					_ergebnis.bewertung.kursdifferenzMax = newKD;
			}
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
	public @NotNull List<@NotNull GostBlockungsergebnisKurs> getOfFachKursmenge(final long pFachID) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachID_kurse, pFachID);
	}

	/**
	 * Liefert die Kursmenge, die zur Fachart gehört. <br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @param  idFachart  Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return die Kursmenge, die zur Fachart gehört.
	 * @throws DeveloperNotificationException Falls die Fachart-ID unbekannt ist.
	 */
	public @NotNull List<@NotNull GostBlockungsergebnisKurs> getOfFachartKursmenge(final long idFachart) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachartID_kurse, idFachart);
	}

	/**
	 * Liefert die Kursdifferenz der Fachart. <br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @param  pFachartID           Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return                      Die Kursdifferenz der Fachart.
	 * @throws DeveloperNotificationException Falls die Fachart-ID unbekannt ist.
	 */
	public int getOfFachartKursdifferenz(final long pFachartID) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachartID_kursdifferenz, pFachartID);
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 *
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
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Menge aller Kurse, die dem Schüler zugeordnet sind.
	 */
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getOfSchuelerKursmenge(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_kurse, idSchueler);
	}

	/**
	 * Liefert die Menge aller Kurse des Schülers mit Kollisionen.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Menge aller Kurse des Schülers mit Kollisionen.
	 */
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getOfSchuelerKursmengeMitKollisionen(final long idSchueler) {
		final @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> mapSchieneKurse = getOfSchuelerSchienenKursmengeMap(idSchueler);

		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();
		for (final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseDerSchiene : mapSchieneKurse.values())
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
		final @NotNull Map<@NotNull Long, GostBlockungsergebnisKurs> map = getOfSchuelerFachIDKursMap(pSchuelerID);

		for (@NotNull final Entry<@NotNull Long, @NotNull GostBlockungsergebnisKurs> e : map.entrySet())
			if (e.getValue() == null)
				return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 *
	 * @param idSchueler   Die Datenbank.ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param idKursart    Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 */
	public boolean getOfSchuelerHatFachwahl(final long idSchueler, final long idFach, final long idKursart) {
		return _parent.getOfSchuelerHatFachart(idSchueler, idFach, idKursart);
	}

	/**
	 * Liefert die Anzahl an Kollisionen des Schülers. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Anzahl an Kollisionen des Schülers.
	 */
	public int getOfSchuelerAnzahlKollisionen(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_kollisionen, idSchueler);
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
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 */
	public @NotNull Map<@NotNull Long, GostBlockungsergebnisKurs> getOfSchuelerFachIDKursMap(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_fachID_kurs, idSchueler);
	}

	/**
	 * Liefert die Map des Schülers, die einer Schiene die Kurse des Schülers zuordnet.
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Map des Schülers, die der Schiene die Kurse des Schülers zuordnet.
	 */
	public @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> getOfSchuelerSchienenKursmengeMap(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_schienenID_kurse, idSchueler);
	}

	/**
	 * Liefert die Kursmenge des Schülers in der Schiene.
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return Die Kursmenge des Schülers in der Schiene.
	 */
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getOfSchuelerOfSchieneKursmenge(final long idSchueler, final long idSchiene) {
		return DeveloperNotificationException.ifMapGetIsNull(getOfSchuelerSchienenKursmengeMap(idSchueler), idSchiene);
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
		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(pSchuelerID);
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
			final @NotNull List<@NotNull GostBlockungsergebnisKurs> kurse = getOfFachartKursmenge(fachartID);
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
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(pKursID);
		return schienenOfKurs.contains(schiene);
	}

	/**
	 * Liefert die Menge aller Schüler-IDs, die dem Kurs zugeordnet sind. <br>
	 * Wirft eine Exception, wenn der ID kein Kurs zugeordnet ist.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return Die Menge aller Schüler-IDs, die dem Kurs zugeordnet sind.
	 */
	public @NotNull Set<@NotNull Long> getOfKursSchuelerIDmenge(final long pKursID) {
		final Set<@NotNull Long> schuelerIDs = _map_kursID_schuelerIDs.get(pKursID);
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
	public @NotNull Set<@NotNull GostBlockungsergebnisSchiene> getOfKursSchienenmenge(final long pKursID) {
		final Set<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = _map_kursID_schienen.get(pKursID);
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
		final @NotNull List<@NotNull Long> schienenIDs = getKursE(pKursID).schienen;
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
	public @NotNull Set<@NotNull Long> getOfKursSchuelermengeMitKollisionen(final long pKursID) {
		final @NotNull HashSet<@NotNull Long> set = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisSchiene schiene : getOfKursSchienenmenge(pKursID))
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
	 * Liefert die Schiene zur übergebenen ID. <br>
	 * Wirft eine Exception, wenn der ID keine Schiene zugeordnet ist.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Schiene zur übergebenen ID.
	 */
	public @NotNull GostBlockungsergebnisSchiene getSchieneE(final long idSchiene) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenID_schiene, idSchiene);
	}

	/**
	 * Liefert die Schiene mit der übergebenen Nummer. <br>
	 * Wirft eine {@link DeveloperNotificationException} wenn eine solche Schiene nicht existiert.
	 *
	 * @param nrSchiene Die Nummer der Schiene.
	 *
	 * @return Die Schiene mit der übergebenen Schienen-NR.
	 */
	public @NotNull GostBlockungsergebnisSchiene getSchieneEmitNr(final int nrSchiene) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenNr_schiene, nrSchiene);
	}

	/**
	 * Liefert die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück. <br>
	 * Falls ein Schüler mehrfach in der Schiene ist, wird er mehrfach gezählt!
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Anzahl an Schülern in der Schiene.
	 */
	public int getOfSchieneAnzahlSchueler(final long idSchiene) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenID_schuelerAnzahl, idSchiene);
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
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 */
	public boolean getOfSchieneHatKollision(final long idSchiene) {
		return getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) > 0;
	}

	/**
	 * Liefert die Anzahl an Schüler-Kollisionen der Schiene zurück. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Anzahl an Schüler-Kollisionen in der Schiene.
	 */
	public int getOfSchieneAnzahlSchuelerMitKollisionen(final long idSchiene) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenID_kollisionen, idSchiene);
	}

	/**
	 * Liefert die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 */
	public @NotNull Set<@NotNull Long> getOfSchieneSchuelermengeMitKollisionen(final long idSchiene) {
		final @NotNull HashSet<@NotNull Long> set = new HashSet<>();
		for (final @NotNull Long schuelerID : _map_schuelerID_kollisionen.keySet())
			if (getOfSchuelerOfSchieneKursmenge(schuelerID, idSchiene).size() > 1)
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
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getOfSchieneKursmengeMitKollisionen(final long pSchienenID) {
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
	public @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostBlockungsergebnisKurs>> getOfSchieneFachartKursmengeMap(final long pSchienenID) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenID_fachartID_kurse, pSchienenID);
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
	public @NotNull Map<@NotNull Long, @NotNull Set<@NotNull Long>> getMappingKursIDSchuelerIDs() {
		return _map_kursID_schuelerIDs;
	}

	/**
	 * Liefert die Map, welche einer Schüler-ID die Menge aller ungültigen Kurse zuordnet. <br>
	 * Hinweis 1: Hat ein Schüler keine ungültige Kurse, dann gibt es die ID nicht. <br>
	 * Hinweis 2: Gibt es keine ungültigen Wahlen, so ist die Map leer. <br>
	 *
	 * @return Die Map, welche einer Schüler-ID die Menge aller ungültigen Kurse zuordnet.
	 */
	public @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>>  getMappingSchuelerIDzuUngueltigeKurse() {
		return _map_schuelerID_ungueltige_kurse;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 *
	 * @return Die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 */
	public @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisSchiene>> getMappingKursIDSchienenmenge() {
		return _map_kursID_schienen;
	}

	/**
	 * Liefert eine Menge aller Schüler-IDs mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Schüler-IDs mit mindestens einer Kollision.
	 */
	public @NotNull Set<@NotNull Long> getMengeDerSchuelerMitKollisionen() {
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
	public @NotNull Set<@NotNull Long> getMengeDerSchuelerMitKollisionenOderNichtwahlen() {
		final @NotNull HashSet<@NotNull Long> set = new HashSet<>();
		for (final @NotNull Long schuelerID : _map_schuelerID_kollisionen.keySet())
			if (getOfSchuelerHatKollision(schuelerID) || getOfSchuelerHatNichtwahl(schuelerID))
				set.add(schuelerID);

		return set;
	}

	/**
	 * Liefert eine gefilterte Menge aller Schüler.
	 *
	 * @param  idKurs      Falls > 0, werden Schüler des Kurses herausgefiltert.
	 * @param  idFach      Falls > 0 und
	 * @param  idKursart   falls > 0, werden Schüler mit dieser Fachwahl herausgefiltert.
	 * @param  konfliktTyp Falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param  subString   Falls |pSubString| > 0 werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 *
	 * @return eine gefilterte Menge aller Schüler.
	 */
	public @NotNull List<@NotNull Schueler> getMengeDerSchuelerGefiltert(final int idKurs, final int idFach, final int idKursart, final int konfliktTyp, final @NotNull String subString) {
		final @NotNull List<@NotNull Schueler> menge = new ArrayList<>();

		for (final @NotNull Schueler schueler : _parent.getMengeOfSchueler()) {
			final long idSchueler = schueler.id;
			if ((konfliktTyp == 1) && (!getOfSchuelerHatKollision(idSchueler)))
				continue;
			if ((konfliktTyp == 2) && (!getOfSchuelerHatNichtwahl(idSchueler)))
				continue;
			if ((konfliktTyp == 3) && ((!getOfSchuelerHatKollision(idSchueler)) && (!getOfSchuelerHatNichtwahl(idSchueler))))
				continue;
			if ((subString.length() > 0) && (!getOfSchuelerHatImNamenSubstring(idSchueler, subString)))
				continue;
			if ((idKurs > 0) && (!getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)))
				continue;
			if (((idFach > 0) && (idKursart > 0)) &&  (!getOfSchuelerHatFachwahl(idSchueler, idFach, idKursart)))
				continue;
			// Der Schüler entspricht allen Filterkriterien.
			menge.add(schueler);
		}

		return menge;
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
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getMengeDerKurseMitKollisionen() {
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
    public @NotNull Set<@NotNull GostBlockungsergebnisSchiene> getMengeDerSchienenMitKollisionen() {
        return CollectionUtils.toFilteredHashSet(_map_schienenID_schiene.values(), (@NotNull final GostBlockungsergebnisSchiene s) -> getOfSchieneHatKollision(s.id));
    }


	/**
	 * Liefert die Menge aller Schienen.
	 *
	 * @return Die Menge aller Schienen.
	 */
	public @NotNull List<@NotNull GostBlockungsergebnisSchiene> getMengeAllerSchienen() {
		return _ergebnis.schienen;
	}

	/**
	 * Liefert die Menge aller Schienen.
	 *
	 * @param idSchueler           Die Datenbank-ID des Schülers.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return Die Menge aller Schienen.
	 */
	public @NotNull SchuelerblockungOutput getSchuelerblockungOutput(final long idSchueler, final boolean fixiereBelegteKurse) {

		// Konstruiere die Eingabedaten "input".
		final @NotNull SchuelerblockungInput input = new SchuelerblockungInput();
		input.schienen = _parent.getSchienenAnzahl();

		// Sammle alle Facharten des Schülers...
		for (@NotNull final GostFachwahl fachwahl : _parent.getOfSchuelerFacharten(idSchueler)) {
			input.fachwahlen.add(fachwahl);
			input.fachwahlenText.add(_parent.getNameOfFachwahl(fachwahl));
			final long fachartID = GostKursart.getFachartIDByFachwahl(fachwahl);

			// Sammle alle potentiellen Kurse der Fachart des Schülers...
			for (final @NotNull GostBlockungsergebnisKurs kursE : getOfFachartKursmenge(fachartID)) {
				final @NotNull SchuelerblockungInputKurs kursS = new SchuelerblockungInputKurs();
				final long idKurs = kursE.id;
				kursS.id = idKurs;
				kursS.fach = kursE.fachID;
				kursS.kursart = kursE.kursart;
				kursS.istGesperrt = getOfSchuelerOfKursIstGesperrt(idSchueler, idKurs);
				kursS.istFixiert = getOfSchuelerOfKursIstFixiert(idSchueler, idKurs)
                                   || (fixiereBelegteKurse && getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs));
				DeveloperNotificationException.ifTrue("kursS.istGesperrt && kursS.istFixiert", kursS.istGesperrt && kursS.istFixiert);
				kursS.anzahlSuS = getOfKursAnzahlSchueler(idKurs);
				kursS.schienen = new int[kursE.schienen.size()];
				for (int i = 0; i < kursS.schienen.length; i++)
					kursS.schienen[i] = _parent.getSchiene(kursE.schienen.get(i)).nummer;
				input.kurse.add(kursS);
			}
		}

		// Berechne die Zuordnung und gib sie zurück.
		return new SchuelerblockungAlgorithmus().handle(input);
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene.
	 * Die Schiene wird anhand ihrer Nummer (nicht anhand der Datenbank-ID) identifiziert.
	 *
	 * @param  kursID      Die Datenbank-ID des Kurses.
	 * @param  schienenNr  Die Nummer der Schiene (nicht die Datenbank-ID).
	 *
	 * @throws DeveloperNotificationException falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public void setKursSchienenNr(final long kursID, final int schienenNr) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnisSchiene eSchiene = DeveloperNotificationException.ifMapGetIsNull(_map_schienenNr_schiene, schienenNr);
		stateKursSchieneHinzufuegen(kursID, eSchiene.id);
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene oder hebt die Verknüpfung auf.
	 *
	 * @param  idKurs                    Die Datenbank-ID des Kurses.
	 * @param  idSchiene                 Die Datenbank-ID der Schiene.
	 * @param  hinzufuegenOderEntfernen  TRUE=Hinzufügen, FALSE=Entfernen
	 *
	 * @return  TRUE, falls die jeweilige Operation erfolgreich war.
	 *
	 * @throws DeveloperNotificationException  falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public boolean setKursSchiene(final long idKurs, final long idSchiene, final boolean hinzufuegenOderEntfernen) throws DeveloperNotificationException {
		if (hinzufuegenOderEntfernen)
			return stateKursSchieneHinzufuegen(idKurs, idSchiene);
		return stateKursSchieneEntfernen(idKurs, idSchiene);
	}

	/**
	 * Verknüpft einen Schüler mit einem Kurs oder hebt die Verknüpfung auf.
	 *
	 * @param  idSchueler                Die Datenbank-ID des Schülers.
	 * @param  idKurs                    Die Datenbank-ID des Kurses.
	 * @param  hinzufuegenOderEntfernen  TRUE=Hinzufügen, FALSE=Entfernen
	 *
	 * @throws DeveloperNotificationException  falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public void setSchuelerKurs(final long idSchueler, final long idKurs, final boolean hinzufuegenOderEntfernen) throws DeveloperNotificationException {
		if (hinzufuegenOderEntfernen)
			stateSchuelerKursHinzufuegen(idSchueler, idKurs);
		else
			stateSchuelerKursEntfernen(idSchueler, idKurs);
	}

	/**
	 * Geht die übergebene Fach-Zuordnungen (Fach --> Kurs) eines Schülers durch und
	 * setzt aktualisiert Veränderung die Kurs-Schüler-Zuordnung.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param zuordnung   Die gewünschte Zuordnung.
	 */
	public void setSchuelerNeuzuordnung(final long idSchueler, final @NotNull SchuelerblockungOutput zuordnung) {

		for (final @NotNull SchuelerblockungOutputFachwahlZuKurs z : zuordnung.fachwahlenZuKurs) {
			// Kurs des Faches 'vorher'.
			final GostBlockungsergebnisKurs kursV = getOfSchuelerOfFachZugeordneterKurs(idSchueler, z.fachID);
			// Kurs des Faches 'nachher'.
			final GostBlockungsergebnisKurs kursN = z.kursID < 0 ? null : getKursE(z.kursID);
			// Bei Ungleichheit wird der Kurs gewechselt.
			if (kursV != kursN) {
				if (kursV != null)
					setSchuelerKurs(idSchueler, kursV.id, false);
				if (kursN != null)
					setSchuelerKurs(idSchueler, kursN.id, true);
			}
		}

	}

	/**
	 * Fügt die übergebene Schiene hinzu.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @throws DeveloperNotificationException  falls die Schiene nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public void setAddSchieneByID(final long idSchiene) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Schiene " + idSchiene + " muss erst beim Datenmanager hinzugefügt werden!", !_parent.getSchieneExistiert(idSchiene));

		// Bewertungen aktualisieren.
		stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Schiene.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @throws DeveloperNotificationException  falls die Schiene nicht zuerst beim Datenmanager entfernt wurde, oder
	 *                                         falls die Schiene noch Kurszuordnungen hat.
	 */
	public void setRemoveSchieneByID(final long idSchiene) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Schiene " + idSchiene + " muss erst beim Datenmanager entfernt werden!", _parent.getSchieneExistiert(idSchiene));
		final int nKurse = getSchieneE(idSchiene).kurse.size();
		DeveloperNotificationException.ifTrue("Entfernen unmöglich: Schiene " + idSchiene + " hat noch " + nKurse + " Kurse!", nKurse > 0);

		// Bewertungen aktualisieren.
		stateRevalidateEverything();
	}

	/**
	 * Fügt die übergebene Regel hinzu.
	 *
	 * @param  idRegel  Die Datenbank-ID der Regel.
	 *
	 * @throws DeveloperNotificationException  falls die Regel nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public void setAddRegelByID(final long idRegel) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Regel " + idRegel + " muss erst beim Datenmanager hinzugefügt werden!", !_parent.getRegelExistiert(idRegel));

		// Bewertungen aktualisieren
		stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Regel.
	 *
	 * @param  idRegel  Die Datenbank-ID der Regel.
	 *
	 * @throws DeveloperNotificationException  falls die Regel nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public void setRemoveRegelByID(final long idRegel) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Regel " + idRegel + " muss erst beim Datenmanager entfernt werden!", _parent.getRegelExistiert(idRegel));

		// Bewertungen aktualisieren
		stateRevalidateEverything();
	}

	/**
	 * Fügt den übergebenen Kurs hinzu.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager hinzugefügt wurde.
	 */
	public void setAddKursByID(final long idKurs) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Der Kurs " + idKurs + " muss erst beim Datenmanager hinzugefügt werden!", !_parent.getKursExistiert(idKurs));
		final @NotNull GostBlockungKurs kurs = _parent.getKurs(idKurs);
		final int nSchienen = _parent.getSchienenAnzahl();
		DeveloperNotificationException.ifTrue("Es gibt " + nSchienen + " Schienen, da passt ein Kurs mit " + kurs.anzahlSchienen + " nicht hinein!", nSchienen < kurs.anzahlSchienen);

		// Muss vor 'setKursSchienenNr' aufgerufen werden.
		stateRevalidateEverything();

		// Kurs automatisch in die ersten 'kurs.anzahlSchienen' Schienen hinzufügen.
		for (int nr = 1; nr <= kurs.anzahlSchienen; nr++)
			setKursSchienenNr(idKurs, nr);
	}

	/**
	 * Löscht den übergebenen Kurs.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 *
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager entfernt wurde, oder
	 *                                         falls der Kurs noch Schülerzuordnungen hat.
	 */
	public void setRemoveKursByID(final long idKurs) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Der Kurs " + idKurs + " muss erst beim Datenmanager entfernt werden!", _parent.getKursExistiert(idKurs));
		final int nSchueler = getKursE(idKurs).schueler.size();
		DeveloperNotificationException.ifTrue("Entfernen unmöglich: Kurs " + idKurs + " hat noch " + nSchueler + " Schüler!", nSchueler > 0);

		// Kurs aus den Daten löschen, sonst wird die Kurs-Schienen-Zuordnung kopiert.
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		for (final @NotNull Long schienenID : kurs.schienen)
			getSchieneE(schienenID).kurse.remove(kurs);
		kurs.schienen.clear();

		stateRevalidateEverything();
	}

	/**
	 * Verschiebt alles SuS von pKursID2delete nach pKursID1keep und
	 * löscht dann den Kurs mit der ID beim {@link GostBlockungsdatenManager},
	 * anschließend in diesem Manager.
	 *
	 * @param  idKursID1keep    Die Datenbank-ID des Kurses, der erhalten bleibt.
	 * @param  idKursID2delete  Die Datenbank-ID des Kurses, der gelöscht wird.
	 */
	public void setMergeKurseByID(final long idKursID1keep, final long idKursID2delete) {
		// 1) Verschieben der SuS von Kurs2 nach Kurs1 (in diesem Manager).
		@NotNull final GostBlockungsergebnisKurs kurs2 = getKursE(idKursID2delete);
		for (final @NotNull Long schuelerID : new ArrayList<>(kurs2.schueler)) {
			stateSchuelerKursEntfernen(schuelerID, idKursID2delete);
			stateSchuelerKursHinzufuegen(schuelerID, idKursID1keep);
		}

		// 2) Kurs2 löschen (beim Parent-Manager).
		_parent.removeKursByID(idKursID2delete);

		// 3) Kurs2 löschen (in diesem Manager).
		setRemoveKursByID(idKursID2delete);
	}

	/**
	 * Erzeugt einen neuen Kurs2 beim {@link GostBlockungsdatenManager},
	 * dann bei diesem Manager und
	 * verschiebt alle SuS des übergebenen Arrays von Kurs1 nach Kurs2.
	 *
	 * @param  kurs1alt     Der Kurs, der gesplittet wird.
	 * @param  kurs2neu     Der Kurs, der neu erzeugt wird.
	 * @param  susVon1nach2 Die Datenbank-IDs der Schüler, die verschoben werden sollen.
	 */
	public void setSplitKurs(final @NotNull GostBlockungKurs kurs1alt, final @NotNull GostBlockungKurs kurs2neu, final @NotNull long[] susVon1nach2) {
		// 1) Kurs2 erzeugen (beim Parent-Manager).
		_parent.addKurs(kurs2neu);

		// 2) Kurs2 erzeugen (in diesem Manager).
		setAddKursByID(kurs2neu.id);

		// 3) Verschieben der SuS von Kurs1 nach Kurs2 (in diesem Manager).
		for (final long schuelerID : susVon1nach2) {
			stateSchuelerKursEntfernen(schuelerID, kurs1alt.id);
			stateSchuelerKursHinzufuegen(schuelerID, kurs2neu.id);
		}
	}

	/**
	 * Verändert die Schienenanzahl eines Kurses. Dies ist nur bei einer Blockungsvorlage erlaubt.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 * @param  anzahlSchienenNeu Die neue Schienenanzahl des Kurses.
	 *
	 * @throws DeveloperNotificationException Falls ein unerwarteter Fehler passiert.
	 */
	public void patchOfKursSchienenAnzahl(final long idKurs, final int anzahlSchienenNeu) throws DeveloperNotificationException {
		// Daten holen.
		final @NotNull GostBlockungKurs kursG = getKursG(idKurs);
		final @NotNull GostBlockungsergebnisKurs kursE = getKursE(idKurs);
		final int nSchienen = _parent.getSchienenAnzahl();

		// DeveloperNotificationException
		DeveloperNotificationException.ifTrue("Die Schienenanzahl eines Kurses darf nur bei der Blockungsvorlage verändert werden!", !_parent.getIstBlockungsVorlage());
		DeveloperNotificationException.ifTrue("Der GostBlockungKurs hat " + kursG.anzahlSchienen + " Schienen, der GostBlockungsergebnisKurs hat hingegen " + kursE.anzahlSchienen + " Schienen!", kursE.anzahlSchienen != kursG.anzahlSchienen);
		DeveloperNotificationException.ifTrue("Die Blockung hat 0 Schienen. Das darf nicht passieren!", nSchienen == 0);
		DeveloperNotificationException.ifTrue("Ein Kurs muss mindestens einer Schiene zugeordnet sein, statt " + anzahlSchienenNeu + " Schienen!", anzahlSchienenNeu <= 0);
		DeveloperNotificationException.ifTrue("Es gibt nur " + nSchienen + " Schienen, der Kurs kann nicht " + anzahlSchienenNeu + " Schienen zugeordnet werden!", anzahlSchienenNeu > nSchienen);

		// Die Schienenanzahl erhöhen, ggf. mehrfach.
		while (anzahlSchienenNeu > kursG.anzahlSchienen) {
			boolean hinzugefuegt = false;
			for (int nr = 1; (nr <= _map_schienenNr_schiene.size()) && (!hinzugefuegt); nr++) {
				final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneEmitNr(nr);
				if (!kursE.schienen.contains(schiene.id)) {
					hinzugefuegt = true;
					kursG.anzahlSchienen++;
					kursE.anzahlSchienen++;
					setKursSchiene(idKurs, schiene.id, true);
				}
			}
			DeveloperNotificationException.ifTrue("Es wurde keine freie Schiene für den Kurs " + idKurs + " gefunden!", !hinzugefuegt);
		}

		// Die Schienenanzahl verringern, ggf. mehrfach.
		while (anzahlSchienenNeu < kursG.anzahlSchienen) {
			boolean entfernt = false;
			for (int nr = _map_schienenNr_schiene.size(); (nr >= 1) && (!entfernt); nr--) {
				final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneEmitNr(nr);
				if (kursE.schienen.contains(schiene.id)) {
					entfernt = true;
					kursG.anzahlSchienen--;
					kursE.anzahlSchienen--;
					setKursSchiene(idKurs, schiene.id, false);
				}
			}
			DeveloperNotificationException.ifTrue("Es wurde keine belegte Schiene von Kurs " + idKurs + " gefunden!", !entfernt);
		}

	}

	/**
	 * Informiert den Manager, dass sich bei mindestens einem Kurs die Lehrkraft geändert hat.
	 * Führt zu einer Revalidierung der Bewertung des Ergebnisses.
	 */
	public void patchOfKursLehrkaefteChanged() {
		stateRevalidateEverything();
	}

	/**
	 * Eine Logger-Ausgabe für Debug-Zwecke.
	 *
	 * @param logger Ein Logger für Debug-Zwecke.
	 */
	public void debug(final @NotNull Logger logger) {
		logger.modifyIndent(+4);
		logger.logLn("----- Kurse sortiert nach Fachart -----");
		for (final @NotNull Long fachartID : _map_fachartID_kurse.keySet()) {
			logger.logLn("FachartID = " + fachartID + " (KD = " + getOfFachartKursdifferenz(fachartID) + ")");
			for (final @NotNull GostBlockungsergebnisKurs kurs : getOfFachartKursmenge(fachartID)) {
				logger.logLn("    " + getOfKursName(kurs.id) + " : " + kurs.schueler.size() + " SuS");
			}
		}
		logger.logLn("KursdifferenzMax = " + _ergebnis.bewertung.kursdifferenzMax);
		logger.logLn("KursdifferenzHistogramm = " + Arrays.toString(_ergebnis.bewertung.kursdifferenzHistogramm));
		logger.modifyIndent(-4);
	}


}
