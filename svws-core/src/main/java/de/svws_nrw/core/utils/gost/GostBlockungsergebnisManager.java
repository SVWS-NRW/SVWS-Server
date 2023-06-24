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
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
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

	/** Kurs-ID --> Anzahl an Dummy-SuS */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_kursID_dummySuS = new HashMap<>();

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
		_map_kursID_dummySuS.clear();
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
			MapUtils.getOrCreateArrayList(_map_fachID_kurse, eKurs.fachID).add(eKurs);

			// Map: fachartID --> Kursliste
			final long fachartID = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			MapUtils.getOrCreateArrayList(_map_fachartID_kurse, fachartID).add(eKurs);

			// Map: fachartID --> Kursdifferenz
			if (!_map_fachartID_kursdifferenz.containsKey(fachartID)) {
				_map_fachartID_kursdifferenz.put(fachartID, 0);
				_ergebnis.bewertung.kursdifferenzHistogramm[0]++;
			}
		}

		// Fachwahlen zu denen es keinen Kurs gibt der Map '_map_fachartID_kurse' hinzufügen.
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen)
			MapUtils.getOrCreateArrayList(_map_fachartID_kurse, GostKursart.getFachartIDByFachwahl(gFachwahl));

		// Map: schienenID --> fachartID --> Kurse = Alle Kurse einer Fachart pro Schiene
		for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
			DeveloperNotificationException.ifMapPutOverwrites(_map_schienenID_fachartID_kurse, gSchiene.id, new HashMap<>());
			for (final @NotNull Long fachartID : _map_fachartID_kursdifferenz.keySet())
				getOfSchieneFachartKursmengeMap(gSchiene.id).put(fachartID, new ArrayList<>());
		}

		// Schüler kopieren und hinzufügen.
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler) {
			// Map: schuelerID --> Kurs-Set
			DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_kurse, gSchueler.id, new HashSet<@NotNull GostBlockungsergebnisKurs>());
			// Map: schuelerID --> Anzahl Kollisionen
			DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_kollisionen, gSchueler.id, 0);
			// Map: schuelerID --> (fachID --> Kurs)
			DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_fachID_kurs, gSchueler.id, new HashMap<>());
			// Map: schuelerID --> (schienenID --> Kurse)
			DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_schienenID_kurse, gSchueler.id, new HashMap<>());
		}

		// Map: schuelerID --> (FachID --> Kurs) = Fachwahlen kopieren und hinzufügen
		final String strErrorDoppeltesFach = "Schüler %d hat Fach %d doppelt!";
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen) {
			final @NotNull Map<@NotNull Long, GostBlockungsergebnisKurs> mapFachKurs = DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_fachID_kurs, gFachwahl.schuelerID);
			if (mapFachKurs.put(gFachwahl.fachID, null) != null)
				throw new DeveloperNotificationException(strErrorDoppeltesFach.formatted(gFachwahl.schuelerID, gFachwahl.fachID));
		}

		// Map: schuelerID --> (schienenID --> Kurse) = Alle Kurse des Schülers pro Schiene
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler)
			for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
				final HashSet<@NotNull GostBlockungsergebnisKurs> newSet = new HashSet<>();
				getOfSchuelerSchienenKursmengeMap(gSchueler.id).put(gSchiene.id, newSet);
			}

		// Zuordnungen kopieren (diese können leer sein).
		final HashSet<@NotNull Long> kursBearbeitet = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisSchiene schieneOld : pOld.schienen)
			for (final @NotNull GostBlockungsergebnisKurs kursOld : schieneOld.kurse) {
				setKursSchiene(kursOld.id, schieneOld.id, true);
				if (kursBearbeitet.add(kursOld.id)) // Bei Multikursen dürfen SuS nicht doppelt hinzugefügt werden.
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
		_map_kursID_dummySuS.clear();

		for (final @NotNull GostBlockungRegel r : _parent.getMengeOfRegeln()) {
			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);
			switch (typ) {
				case KURSART_SPERRE_SCHIENEN_VON_BIS: // 1
					stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(r, regelVerletzungen);
					break;
				case KURS_FIXIERE_IN_SCHIENE: // 2
					stateRegelvalidierung2_kurs_fixieren_in_schiene(r, regelVerletzungen);
					break;
				case KURS_SPERRE_IN_SCHIENE: // 3
					stateRegelvalidierung3_kurs_sperren_in_schiene(r, regelVerletzungen);
					break;
				case SCHUELER_FIXIEREN_IN_KURS: // 4
					stateRegelvalidierung4_schueler_fixieren_in_kurs(r, regelVerletzungen);
					break;
				case SCHUELER_VERBIETEN_IN_KURS: // 5
					stateRegelvalidierung5_schueler_verbieten_in_kurs(r, regelVerletzungen);
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
				case KURS_MIT_DUMMY_SUS_AUFFUELLEN: // 9
					stateRegelvalidierung9_kurs_mit_dummy_sus_auffuellen(r);
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
		final long idKurs1 = r.parameter.get(0);
		final long idKurs2 = r.parameter.get(1);
		for (final @NotNull GostBlockungsergebnisSchiene schiene1 : getOfKursSchienenmenge(idKurs1))
			for (final @NotNull GostBlockungsergebnisSchiene schiene2 : getOfKursSchienenmenge(idKurs2))
				if (schiene1 == schiene2)
					regelVerletzungen.add(r.id);
	}

	private void stateRegelvalidierung8_kurs_zusammen_mit_kurs(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen) {
		final long idKurs1 = r.parameter.get(0);
		final long idKurs2 = r.parameter.get(1);
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> set1 = getOfKursSchienenmenge(idKurs1);
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> set2 = getOfKursSchienenmenge(idKurs2);
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

	private void stateRegelvalidierung9_kurs_mit_dummy_sus_auffuellen(final @NotNull GostBlockungRegel r) {
		final long idKurs = r.parameter.get(0);
		final int anzahl = r.parameter.get(1).intValue();
		DeveloperNotificationException.ifTrue("Regel 9 DummySuS Wert = " + anzahl + " ist ungültig!", (anzahl < 1) || (anzahl > 99));
		DeveloperNotificationException.ifMapPutOverwrites(_map_kursID_dummySuS, idKurs, anzahl);
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
	 *          Stattdessen wird die ungültige Wahl in einer Map gespeichert.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 */
	private void stateSchuelerKursHinzufuegen(final long idSchueler, final long idKurs) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		final long fachID = kurs.fachID;

		// Ungültige Wahlen extra behandeln.
		if (!getOfSchuelerHatFachwahl(idSchueler, fachID, kurs.kursart)) {
			stateSchuelerKursUngueltigeWahlHinzufuegen(idSchueler, kurs);
			return;
		}

		// Wurde das Fach bereits einem anderen Kurs zugeordnet?
		if (getOfSchuelerOfFachZugeordneterKurs(idSchueler, fachID) != null) // wirft ggf. Exception
			return;

		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(idSchueler);
		final @NotNull Set<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(idKurs);
		final @NotNull Map<@NotNull Long, GostBlockungsergebnisKurs> mapSFK = getOfSchuelerFachIDKursMap(idSchueler);
		final long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		// Hinzufügen
		kurs.schueler.add(idSchueler); // Data-Objekt aktualisieren
		kurseOfSchueler.add(kurs);
		schuelerIDsOfKurs.add(idSchueler);
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		mapSFK.put(fachID, kurs);
		stateKursdifferenzUpdate(fachartID);
		for (final @NotNull Long schieneID : kurs.schienen)
			stateSchuelerSchieneHinzufuegen(idSchueler, schieneID, kurs);
		stateRegelvalidierung();
	}

	/**
	 * Entfernt den Schüler aus dem Kurs.<br>
	 * Hinweis: Ist die Wahl des Kurses für diesen Schüler ungültig, so wird der Schüler aus der zuvor gespeichert
	 *          Zuordnung aller ungültigen Wahlen gelöscht.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 */
	private void stateSchuelerKursEntfernen(final long idSchueler, final long idKurs) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		final long fachID = kurs.fachID;

		// Ungültige Wahlen extra behandeln.
		if (!getOfSchuelerHatFachwahl(idSchueler, fachID, kurs.kursart)) {
			stateSchuelerKursUngueltigeWahlEntfernen(idSchueler, kurs);
			return;
		}

		// Der Kurs ist derzeit gar nicht zugeordnet!
		if (getOfSchuelerOfFachZugeordneterKurs(idSchueler, fachID) != kurs) // wirft ggf. Exception
			return;

		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(idSchueler);
		final @NotNull Set<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(idKurs);
		final @NotNull Map<@NotNull Long, GostBlockungsergebnisKurs> mapSFK = getOfSchuelerFachIDKursMap(idSchueler);
		final long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		// Hinzufügen
		kurs.schueler.remove(idSchueler); // Data-Objekt aktualisieren
		kurseOfSchueler.remove(kurs);
		schuelerIDsOfKurs.remove(idSchueler);
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
		mapSFK.put(fachID, null);
		stateKursdifferenzUpdate(fachartID);
		for (final @NotNull Long schieneID : kurs.schienen)
			stateSchuelerSchieneEntfernen(idSchueler, schieneID, kurs);
		stateRegelvalidierung();
	}

	private void stateSchuelerKursUngueltigeWahlHinzufuegen(final long idSchueler, final @NotNull GostBlockungsergebnisKurs idKurs) {
		MapUtils.getOrCreateHashSet(_map_schuelerID_ungueltige_kurse, idSchueler).add(idKurs);
	}

	private void stateSchuelerKursUngueltigeWahlEntfernen(final long idSchueler, final @NotNull GostBlockungsergebnisKurs idKurs) {
	    final @NotNull Set<@NotNull GostBlockungsergebnisKurs> set = DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_ungueltige_kurse, idSchueler);
		set.remove(idKurs);
		if (set.isEmpty())
			_map_schuelerID_ungueltige_kurse.remove(idSchueler);
	}

	/**
	 * Fügt den Kurs der Schiene hinzu.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 */
	private void stateKursSchieneHinzufuegen(final long idKurs, final long idSchiene) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(idSchiene);
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> setSchienenOfKurs = getOfKursSchienenmenge(idKurs);
		final long idFach = kurs.fachID;
		final long idFachart = GostKursart.getFachartID(idFach, kurs.kursart);
		final @NotNull List<@NotNull GostBlockungsergebnisKurs> kursGruppe = getOfSchieneOfFachartKursmenge(idSchiene, idFachart);

		// Hinzufügen
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		DeveloperNotificationException.ifListAddsDuplicate("kurs.schienen", kurs.schienen, schiene.id); // Data-Objekt aktualisieren
		DeveloperNotificationException.ifListAddsDuplicate("schiene.kurse", schiene.kurse, kurs);       // Data-Objekt aktualisieren
		DeveloperNotificationException.ifSetAddsDuplicate("setSchienenOfKurs", setSchienenOfKurs, schiene);
		for (final @NotNull Long schuelerID : kurs.schueler)
			stateSchuelerSchieneHinzufuegen(schuelerID, schiene.id, kurs);
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		_ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene += kursGruppe.isEmpty() ? 0 : 1;
		DeveloperNotificationException.ifListAddsDuplicate("kursGruppe", kursGruppe, kurs);  // Muss nach der Bewertung passieren.

		stateRegelvalidierung();
	}

	/**
	 * Entfernt den Kurs aus der Schiene.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene Die Datenbank-ID der Schiene.
	 */
	private void stateKursSchieneEntfernen(final long idKurs, final long idSchiene) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(idSchiene);
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> setSchienenOfKurs = getOfKursSchienenmenge(idKurs);
		final long idFach = kurs.fachID;
		final long idFachart = GostKursart.getFachartID(idFach, kurs.kursart);
		final @NotNull List<@NotNull GostBlockungsergebnisKurs> kursGruppe = getOfSchieneOfFachartKursmenge(idSchiene, idFachart);

		// Entfernen
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		DeveloperNotificationException.ifListRemoveFailes("kurs.schienen", kurs.schienen, schiene.id); // Data-Objekt aktualisieren
		DeveloperNotificationException.ifListRemoveFailes("schiene.kurse", schiene.kurse, kurs);       // Data-Objekt aktualisieren
		DeveloperNotificationException.ifSetRemoveFailes("setSchienenOfKurs", setSchienenOfKurs, schiene);
		for (final @NotNull Long schuelerID : kurs.schueler)
			stateSchuelerSchieneEntfernen(schuelerID, schiene.id, kurs);
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		DeveloperNotificationException.ifListRemoveFailes("kursGruppe", kursGruppe, kurs); // Muss vor der Bewertung passieren.
		_ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene -= kursGruppe.isEmpty() ? 0 : 1;

		stateRegelvalidierung();
	}

	private void stateSchuelerSchieneHinzufuegen(final long idSchueler, final long idSchiene, final @NotNull GostBlockungsergebnisKurs kurs) {
		// Schiene --> Integer (erhöhen)
		final int schieneSchuelerzahl = getOfSchieneAnzahlSchueler(idSchiene);
		_map_schienenID_schuelerAnzahl.put(idSchiene, schieneSchuelerzahl + 1);

		// Schiene --> Schüler --> Kurse (erhöhen)
		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfSchuelerOfSchieneKursmenge(idSchueler, idSchiene);
		kursmenge.add(kurs);

		// Kollisionen erhöhen?
		if (kursmenge.size() > 1) {
			// Kollisionen der Schiene.
			final int schieneKollisionen = getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene);
			_map_schienenID_kollisionen.put(idSchiene, schieneKollisionen + 1);

			// Kollisionen des Schülers.
			final int schuelerKollisionen = getOfSchuelerAnzahlKollisionen(idSchueler);
			_map_schuelerID_kollisionen.put(idSchueler, schuelerKollisionen + 1);

			// Kollisionen insgesamt.
			_ergebnis.bewertung.anzahlSchuelerKollisionen++;
		}
	}

	private void stateSchuelerSchieneEntfernen(final long idSchueler, final long idSchiene, final @NotNull GostBlockungsergebnisKurs kurs) {
		// // Schiene --> Integer (verringern)
		final int schieneSchuelerzahl = getOfSchieneAnzahlSchueler(idSchiene);
		DeveloperNotificationException.ifTrue("schieneSchuelerzahl == 0 --> Entfernen unmöglich!", schieneSchuelerzahl == 0);
		_map_schienenID_schuelerAnzahl.put(idSchiene, schieneSchuelerzahl - 1);

		// Schiene --> Schüler --> Integer (verringern)
		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfSchuelerOfSchieneKursmenge(idSchueler, idSchiene);
		kursmenge.remove(kurs);

		// Kollisionen verringern?
		if (!kursmenge.isEmpty()) {
			// Kollisionen der Schiene.
			final int schieneKollisionen = getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene);
			DeveloperNotificationException.ifTrue("schieneKollisionen == 0 --> Entfernen unmöglich!", schieneKollisionen == 0);
			_map_schienenID_kollisionen.put(idSchiene, schieneKollisionen - 1);

			// Kollisionen des Schülers.
			final int schuelerKollisionen = getOfSchuelerAnzahlKollisionen(idSchueler);
			DeveloperNotificationException.ifTrue("schuelerKollisionen == 0 --> Entfernen unmöglich!", schuelerKollisionen == 0);
			_map_schuelerID_kollisionen.put(idSchueler, schuelerKollisionen - 1);

			// Kollisionen insgesamt.
			DeveloperNotificationException.ifTrue("Gesamtkollisionen == 0 --> Entfernen unmöglich!", _ergebnis.bewertung.anzahlSchuelerKollisionen == 0);
			_ergebnis.bewertung.anzahlSchuelerKollisionen--;
		}
	}

	private void stateKursdifferenzUpdate(final long idFachart) {
		// Den ersten Kurs der Fachart holen.
		final @NotNull List<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfFachartKursmenge(idFachart);
		final @NotNull GostBlockungsergebnisKurs kurs1 = DeveloperNotificationException.ifListGetFirstFailes("getOfFachartKursmenge", kursmenge);

		// Neue Kursdifferenz berechnen
		int min = kurs1.schueler.size();
		int max = min;
		for (final @NotNull GostBlockungsergebnisKurs kurs : kursmenge) {
			final int size = kurs.schueler.size() + getOfKursAnzahlSchuelerDummy(kurs.id);
			min = Math.min(min, size);
			max = Math.max(max, size);
		}
		final int newKD = max - min;

		// Kursdifferenz ändert sich nicht.
		final int oldKD = getOfFachartKursdifferenz(idFachart);
		if (newKD == oldKD)
			return;

		// Kursdifferenz-Map aktualisieren.
		_map_fachartID_kursdifferenz.put(idFachart, newKD);

		// Kursdifferenz-Histogramm aktualisieren.
		final int[] kursdifferenzen = _ergebnis.bewertung.kursdifferenzHistogramm;
		kursdifferenzen[oldKD]--;
		kursdifferenzen[newKD]++;

		// Kursdifferenz-Max aktualisieren.
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
	 * Liefert den zugehörigen Daten-Manager für diesen Ergebnis-Manager.
	 *
	 * @return den zugehörigen Daten-Manager für diesen Ergebnis-Manager.
	 */
	public GostBlockungsdatenManager getParent() {
		return _parent;
	}

	/**
	 * Liefert die Datenbank-ID der Blockungs. Das ist die ID des Elternteils.
	 *
	 * @return die Datenbank-ID der Blockungs. Das ist die ID des Elternteils.
	 */
	public long getBlockungsdatenID() {
		return _ergebnis.blockungID;
	}

	/**
	 * Liefert das Blockungsergebnis.
	 *
	 * @return das Blockungsergebnis.
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
	 * Liefert die Anzahl an Schülerkollisionen.<br>
	 * Ist ein Schüler x mal in einer Schiene und ist x > 1, dann wird dies als x-1 Kollisionen gezählt.
	 *
	 * @return die Anzahl an Schülerkollisionen.
	 */
	public int getOfBewertungAnzahlKollisionen() {
		return _ergebnis.bewertung.anzahlSchuelerKollisionen;
	}

	/**
	 * Liefert die Anzahl nicht vollständig verteilter Kurse.<br>
	 * Ein Multikurse der über mehrere Schienen geht und gar nicht zugeteilt wurde, wird mehrfach gezählt.
	 *
	 * @return die Anzahl nicht vollständig verteilter Kurse.
	 */
	public int getOfBewertungAnzahlNichtZugeordneterKurse() {
		return _ergebnis.bewertung.anzahlKurseNichtZugeordnet;
	}

	/**
	 * Liefert die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 *
	 * @return die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 */
	public int getOfBewertungAnzahlNichtzugeordneterFachwahlen() {
		return _ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
	}

	/**
	 * Ermittelt das {@link GostFach} für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param idFach Die Datenbank-ID des Faches.
	 *
	 * @return Das GostFach-Objekt.
	 * @throws DeveloperNotificationException im Falle, dass die ID nicht bekannt ist.
	 */
	public @NotNull GostFach getFach(final long idFach) throws DeveloperNotificationException {
		return _parent.faecherManager().getOrException(idFach);
	}

	/**
	 * Liefert die Menge aller Kurse mit dem angegebenen Fach-ID.<br>
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Menge aller Kurse mit dem angegebenen Fach-ID.
	 * @throws DeveloperNotificationException falls die Fach-ID unbekannt ist.
	 */
	public @NotNull List<@NotNull GostBlockungsergebnisKurs> getOfFachKursmenge(final long idFach) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachID_kurse, idFach);
	}

	/**
	 * Liefert die Kursmenge, die zur Fachart gehört.<br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @param  idFachart  Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return die Kursmenge, die zur Fachart gehört.
	 * @throws DeveloperNotificationException falls die Fachart-ID unbekannt ist.
	 */
	public @NotNull List<@NotNull GostBlockungsergebnisKurs> getOfFachartKursmenge(final long idFachart) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachartID_kurse, idFachart);
	}

	/**
	 * Liefert die Kursdifferenz der Fachart.<br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @param  idFachart  Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return die Kursdifferenz der Fachart.
	 * @throws DeveloperNotificationException Falls die Fachart-ID unbekannt ist.
	 */
	public int getOfFachartKursdifferenz(final long idFachart) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachartID_kursdifferenz, idFachart);
	}

	/**
	 * Liefert das {@link Schueler}-Objekt zur übergebenen ID.<br>
	 * Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return das {@link Schueler}-Objekt zur übergebenen ID.
	 * @throws DeveloperNotificationException falls die Schüler-ID unbekannt ist.
	 */
	public @NotNull Schueler getSchuelerG(final long idSchueler) throws DeveloperNotificationException {
		return _parent.getSchueler(idSchueler);
	}

	/**
	 * Liefert einen Schüler-String im Format: 'Nachname, Vorname'.
	 *
	 * @param  idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return einen Schüler-String im Format: 'Nachname, Vorname'.
	 */
	public @NotNull String getOfSchuelerNameVorname(final long idSchueler) {
		final @NotNull Schueler schueler = _parent.getSchueler(idSchueler);
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
	 * Liefert TRUE, falls der Schüler mindestens eine Nichtwahl hat. <br>
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler mindestens eine Nichtwahl hat.
	 */
	public boolean getOfSchuelerHatNichtwahl(final long idSchueler) {
		final @NotNull Map<@NotNull Long, GostBlockungsergebnisKurs> map = getOfSchuelerFachIDKursMap(idSchueler);

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
	 * Liefert TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 *
	 * @param idSchueler   Die Datenbank.ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 */
	public boolean getOfSchuelerHatFach(final long idSchueler, final long idFach) {
		return _parent.getOfSchuelerHatFach(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Kollision hat. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler mindestens eine Kollision hat.
	 */
	public boolean getOfSchuelerHatKollision(final long idSchueler) {
		return getOfSchuelerAnzahlKollisionen(idSchueler) > 0;
	}

	/**
	 * Liefert die Anzahl an Kollisionen des Schülers.<br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return die Anzahl an Kollisionen des Schülers.
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
	 * @return die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 */
	public @NotNull Map<@NotNull Long, GostBlockungsergebnisKurs> getOfSchuelerFachIDKursMap(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_fachID_kurs, idSchueler);
	}

	/**
	 * Liefert die Map des Schülers, die pro Schiene die belegten Kurse des Schülers zuordnet.
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return die Map des Schülers, die pro Schiene die belegten Kurse des Schülers zuordnet.
	 */
	public @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> getOfSchuelerSchienenKursmengeMap(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_schienenID_kurse, idSchueler);
	}

	/**
	 * Liefert die Menge der belegten Kurse des Schülers in der Schiene.
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge der belegten Kurse des Schülers in der Schiene.
	 */
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getOfSchuelerOfSchieneKursmenge(final long idSchueler, final long idSchiene) {
		return DeveloperNotificationException.ifMapGetIsNull(getOfSchuelerSchienenKursmengeMap(idSchueler), idSchiene);
	}

	/**
	 * Liefert die zu (idSchueler, idFach) die jeweilige Kursart. <br>
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 * @param idFach     Die Datenbank-ID des Faches.
	 *
	 * @return Die zu (idSchueler, idFach) die jeweilige Kursart.
	 */
	public @NotNull GostKursart getOfSchuelerOfFachKursart(final long idSchueler, final long idFach) {
		return _parent.getOfSchuelerOfFachKursart(idSchueler, idFach);
	}

	/**
	 * Liefert den zu (idSchueler, idFach) passenden Kurs.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idFach     Die Datenbank-ID des Faches.
	 *
	 * @return den zu (idSchueler, idFach) passenden Kurs.
	 */
	public GostBlockungsergebnisKurs getOfSchuelerOfFachZugeordneterKurs(final long idSchueler, final long idFach) {
		@NotNull final Map<@NotNull Long, GostBlockungsergebnisKurs> mapFachZuKurs = getOfSchuelerFachIDKursMap(idSchueler);
		DeveloperNotificationException.ifMapNotContains("mapFachZuKurs", mapFachZuKurs, idFach);
		return mapFachZuKurs.get(idFach);
	}

	/**
	 * Liefert TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 */
	public boolean getOfSchuelerOfKursIstZugeordnet(final long idSchueler, final long idKurs) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(idSchueler);
		return kurseOfSchueler.contains(kurs);
	}

	/**
	 * Liefert ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 */
	public @NotNull SchuelerblockungOutput getOfSchuelerNeuzuordnung(final long idSchueler) {
		final @NotNull SchuelerblockungInput input = new SchuelerblockungInput();

		// Aktuelle Anzahl an Schienen.
		input.schienen = this.getOfSchieneAnzahl();

		// Fachwahlen des Schülers.
		final @NotNull List<@NotNull GostFachwahl> fachwahlenDesSchuelers = _parent.getOfSchuelerFacharten(idSchueler);
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
				inKurs.istGesperrt = getOfSchuelerOfKursIstGesperrt(idSchueler, kurs.id);
				inKurs.istFixiert = getOfSchuelerOfKursIstFixiert(idSchueler, kurs.id);
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
	 * Liefert ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 *
	 * @param idSchueler           Die Datenbank-ID des Schülers.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 */
	public @NotNull SchuelerblockungOutput getOfSchuelerNeuzuordnungMitFixierung(final long idSchueler, final boolean fixiereBelegteKurse) {

		// Konstruiere die Eingabedaten "input".
		final @NotNull SchuelerblockungInput input = new SchuelerblockungInput();
		input.schienen = _parent.getSchienenAnzahl();

		// Sammle alle Facharten des Schülers...
		for (final @NotNull GostFachwahl fachwahl : _parent.getOfSchuelerFacharten(idSchueler)) {
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
				kursS.schienen = getOfKursSchienenNummern(idKurs);
				input.kurse.add(kursS);
			}
		}

		// Sonderfall: Der Schüler hat 0 Fachwahlen oder alle Fachwahlen haben 0 Kurse.
		if (input.kurse.isEmpty())
			return new SchuelerblockungOutput();

		// Berechne die Zuordnung und gib sie zurück.
		return new SchuelerblockungAlgorithmus().handle(input);
	}

	/**
	 * Liefert TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 */
	public boolean getOfSchuelerOfKursIstFixiert(final long idSchueler, final long idKurs) {
		for (final @NotNull GostBlockungRegel r : _parent.getMengeOfRegeln()) {
			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);

			if (typ == GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS) {
				final long schuelerID = r.parameter.get(0);
				final long kursID = r.parameter.get(1);
				if ((schuelerID == idSchueler) && (kursID == idKurs))
					return true;
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Schüler im Kurs via Regel gesperrt sein soll.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler im Kurs via Regel gesperrt sein soll.
	 */
	public boolean getOfSchuelerOfKursIstGesperrt(final long idSchueler, final long idKurs) {
		for (final @NotNull GostBlockungRegel r : _parent.getMengeOfRegeln()) {
			final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(r.typ);

			if (typ == GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS) {
				final long schuelerID = r.parameter.get(0);
				final long kursID = r.parameter.get(1);
				if ((schuelerID == idSchueler) && (kursID == idKurs))
					return true;
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Sub-String im Nachnamen oder im Vornamen des Schülers vorkommt (Groß- und Kleinschreibung wird dabei ignoriert).
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param subString   Der zu suchende Sub-String.
	 *
	 * @return TRUE, falls der Sub-String im Nachnamen oder im Vornamen des Schülers vorkommt (Groß- und Kleinschreibung wird dabei ignoriert).
	 */
	public boolean getOfSchuelerHatImNamenSubstring(final long idSchueler, final @NotNull String subString) {
		final @NotNull Schueler schueler = getSchuelerG(idSchueler);
		final @NotNull String text = subString.toLowerCase();
		return schueler.nachname.toLowerCase().contains(text) || schueler.vorname.toLowerCase().contains(text);
	}

	/**
	 * Liefert das Geschlecht des Schülers.<br>
	 * Wirft eine Exception, falls das Enum {@link Geschlecht} nicht definiert ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return das Geschlecht des Schülers.
	 * @throws DeveloperNotificationException falls das Enum {@link Geschlecht} nicht definiert ist.
	 */
	public @NotNull Geschlecht getOfSchuelerGeschlechtOrException(final long idSchueler) throws DeveloperNotificationException {
		final @NotNull Schueler schueler = getSchuelerG(idSchueler);
		final Geschlecht geschlecht = Geschlecht.fromValue(schueler.geschlecht);
		return DeveloperNotificationException.ifNull("Das Geschlecht des Schülers " + idSchueler + " ist nicht definiert!", geschlecht);
	}

	/**
	 * Liefert den {@link GostBlockungKurs} zur übergebenen ID.<br>
	 * Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Wirft eine DeveloperNotificationException, falls die ID unbekannt ist.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den {@link GostBlockungKurs} zur übergebenen ID.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public @NotNull GostBlockungKurs getKursG(final long idKurs) throws DeveloperNotificationException {
		return _parent.getKurs(idKurs);
	}

	/**
	 * Liefert den {@link GostBlockungsergebnisKurs} zur übergebenen ID.<br>
	 * Wirft eine DeveloperNotificationException, falls die ID unbekannt ist.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 *
	 * @return den {@link GostBlockungsergebnisKurs} zur übergebenen ID.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public @NotNull GostBlockungsergebnisKurs getKursE(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_kursID_kurs, idKurs);
	}

	/**
	 * Liefert den Namen des Kurses, erzeugt aus Fach, der Kursart und der Nummer, beispielsweise D-GK1.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den Namen des Kurses, erzeugt aus Fach, der Kursart und der Nummer, beispielsweise D-GK1.
	 */
	public @NotNull String getOfKursName(final long idKurs) {
		return _parent.getNameOfKurs(idKurs);
	}

	/**
	 * Liefert TRUE, falls der Kurs der Schiene zugeordnet ist.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs der Schiene zugeordnet ist.
	 */
	public boolean getOfKursOfSchieneIstZugeordnet(final long idKurs, final long idSchiene) {
		final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(idSchiene);
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(idKurs);
		return schienenOfKurs.contains(schiene);
	}

	/**
	 * Liefert zur Kurs-ID die zugehörige Menge aller Schüler-IDs.<br>
	 * Wirft eine Exception, falls der ID kein Kurs zugeordnet ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return zur Kurs-ID die zugehörige Menge aller Schüler-IDs.
	 * @throws DeveloperNotificationException falls der ID kein Kurs zugeordnet ist.
	 */
	public @NotNull Set<@NotNull Long> getOfKursSchuelerIDmenge(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_kursID_schuelerIDs, idKurs);
	}

	/**
	 * Liefert die Schienenmenge des Kurses.<br>
	 * Wirft eine Exception, falls der ID kein Kurs zugeordnet ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Schienenmenge des Kurses.
	 * @throws DeveloperNotificationException falls der ID kein Kurs zugeordnet ist.
	 */
	public @NotNull Set<@NotNull GostBlockungsergebnisSchiene> getOfKursSchienenmenge(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_kursID_schienen, idKurs);
	}

	/**
	 * Liefert ein Array aller Schienen-Nummern des Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return ein Array aller Schienen-Nummern des Kurses.
	 */
	public @NotNull int[] getOfKursSchienenNummern(final long idKurs) {
		final @NotNull List<@NotNull Long> schienenIDs = getKursE(idKurs).schienen;
		final int[] a = new int[schienenIDs.size()];
		for (int i = 0; i < a.length; i++) {
			final long schienenID = schienenIDs.get(i);
			a[i] = _parent.getSchiene(schienenID).nummer;
		}
		return a;
	}

	/**
	 * Liefert TRUE, falls der Kurs mindestens eine Kollision hat. <br>
	 * Definition: Ein Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs mindestens eine Kollision hat.
	 */
	public boolean getOfKursHatKollision(final long idKurs) {
		return getOfKursAnzahlKollisionen(idKurs) > 0;
	}

	/**
	 * Liefert die Anzahl an Schülern des Kurses mit Kollisionen.<br>
	 * Kollision: Der Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern des Kurses mit Kollisionen.
	 */
	public int getOfKursAnzahlKollisionen(final long idKurs) {
		return getOfKursSchuelermengeMitKollisionen(idKurs).size();
	}

	/**
	 * Liefert die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 *
	 * @param  idKursID  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 */
	public @NotNull Set<@NotNull Long> getOfKursSchuelermengeMitKollisionen(final long idKursID) {
		final @NotNull HashSet<@NotNull Long> set = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisSchiene schiene : getOfKursSchienenmenge(idKursID))
			for (final @NotNull Long schuelerID : getKursE(idKursID).schueler)
				if (getOfSchuelerOfSchieneKursmenge(schuelerID, schiene.id).size() > 1)
					set.add(schuelerID); // Set ist wichtig, da bei Multikursen ein Schüler mehrfach kollidieren kann.
		return set;
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind.
	 */
	public int getOfKursAnzahlSchueler(final long idKurs) {
		final @NotNull GostBlockungsergebnisKurs kursE = getKursE(idKurs);
		return kursE.schueler.size();
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS des Kurses.  Dummy-SuS werden durch die Regel mit dem
	 * Typ {@link GostKursblockungRegelTyp#KURS_MIT_DUMMY_SUS_AUFFUELLEN} einem Kurs zugeordnet.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Dummy-SuS des Kurses.
	 */
	public int getOfKursAnzahlSchuelerDummy(final long idKurs) {
		return MapUtils.getOrDefault(_map_kursID_dummySuS, idKurs, 0);
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn schriftlich belegt haben.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn schriftlich belegt haben.
	 */
	public int getOfKursAnzahlSchuelerSchriftlich(final long idKurs) {
		final @NotNull GostBlockungsergebnisKurs kursE = getKursE(idKurs);
		final long idFach = kursE.fachID;

		int summe = 0;
		for (final @NotNull Long idSchueler : kursE.schueler) {
			final @NotNull GostFachwahl fachwahl = _parent.getOfSchuelerOfFachFachwahl(idSchueler, idFach);
			DeveloperNotificationException.ifTrue("fachwahl.schuelerID != idSchueler", fachwahl.schuelerID != idSchueler);
			DeveloperNotificationException.ifTrue("fachwahl.kursartID != kursE.kursart", fachwahl.kursartID != kursE.kursart);
			DeveloperNotificationException.ifTrue("fachwahl.fachID != idFach", fachwahl.fachID != idFach);
			if (fachwahl.istSchriftlich)
				summe++;
		}

		return summe;
	}

	/**
	 * Liefert die Anzahl an Schienen in denen der Kurs gerade ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schienen in denen der Kurs gerade ist.
	 */
	public int getOfKursAnzahlSchienenIst(final long idKurs) {
		return getOfKursSchienenmenge(idKurs).size();
	}

	/**
	 * Liefert die Anzahl an Schienen, die der Kurs haben sollte.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schienen, die der Kurs haben sollte.
	 */
	public int getOfKursAnzahlSchienenSoll(final long idKurs) {
		return getKursE(idKurs).anzahlSchienen;
	}

	/**
	 * Liefert TRUE, falls der Kurs keine Schüler enthält und somit ein Löschen des Kurses erlaubt ist.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs keine Schüler enthält und somit ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public boolean getOfKursRemoveAllowed(final long idKurs) throws DeveloperNotificationException {
		return getOfKursAnzahlSchueler(idKurs) == 0;
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostBlockungSchiene}-Objekt.<br>
	 * Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.<br>
	 * Wirft eine DeveloperNotificationException, falls die ID unbekannt ist.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return das zur ID zugehörige {@link GostBlockungSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public @NotNull GostBlockungSchiene getSchieneG(final long idSchiene) throws DeveloperNotificationException {
		return _parent.getSchiene(idSchiene);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.<br>
	 * Wirft eine Exception, wenn der ID keine Schiene zugeordnet ist.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return das zur ID zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public @NotNull GostBlockungsergebnisSchiene getSchieneE(final long idSchiene) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenID_schiene, idSchiene);
	}

	/**
	 * Liefert das zur Nummer zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.<br>
	 * Wirft eine {@link DeveloperNotificationException} falls eine solche Schiene nicht existiert.
	 *
	 * @param nrSchiene Die Nummer der Schiene.
	 *
	 * @return das zur Nummer zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls eine solche Schiene nicht existiert.
	 */
	public @NotNull GostBlockungsergebnisSchiene getSchieneEmitNr(final int nrSchiene) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenNr_schiene, nrSchiene);
	}

	/**
	 * Liefert die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück.<br>
	 * Hinweis: Falls ein Schüler mehrfach in der Schiene ist, also mit Kollisionen, wird er mehrfach gezählt!
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück.
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
	 * Liefert TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 */
	public boolean getOfSchieneHatKollision(final long idSchiene) {
		return getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) > 0;
	}

	/**
	 * Liefert die Anzahl an Schüler-Kollisionen der Schiene.<br>
	 * Hinweis Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Schüler-Kollisionen der Schiene.
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
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public int getOfSchieneAnzahlKursmengeMitKollisionen(final long idSchiene) {
		return getOfSchieneKursmengeMitKollisionen(idSchiene).size();
	}

	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getOfSchieneKursmengeMitKollisionen(final long idSchiene) {
		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisKurs kurs : getSchieneE(idSchiene).kurse)
			if (getOfKursHatKollision(kurs.id))
				set.add(kurs);
		return set;
	}

	/**
	 * Liefert eine "FachartID --> Kurse" Map der Schiene.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 * @return Eine "FachartID --> Kurse" Map der Schiene.
	 */
	public @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostBlockungsergebnisKurs>> getOfSchieneFachartKursmengeMap(final long idSchiene) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenID_fachartID_kurse, idSchiene);
	}

	/**
	 * Liefert alle Kurse, die in einer bestimmten Schiene eine bestimmte Fachart haben.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 * @param idFachart  Die ID der Fachart.
	 *
	 * @return alle Kurse, die in einer bestimmten Schiene eine bestimmte Fachart haben.
	 */
	public @NotNull List<@NotNull GostBlockungsergebnisKurs> getOfSchieneOfFachartKursmenge(final long idSchiene, final long idFachart) {
		return DeveloperNotificationException.ifMapGetIsNull(getOfSchieneFachartKursmengeMap(idSchiene), idFachart);
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist.<br>
	 * Kriterium: Es dürfen keine Kurse der Schiene zugeordnet sein.
	 *
	 * @param  idShiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert.
	 */
	public boolean getOfSchieneRemoveAllowed(final long idShiene) throws DeveloperNotificationException {
		return getSchieneE(idShiene).kurse.isEmpty();
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
	 * Liefert eine nach Kriterien gefilterte Menge aller Schüler.
	 *
	 * @param  idKurs           falls > 0, werden Schüler des Kurses herausgefiltert.
	 * @param  idFach           falls > 0, werden Schüler mit diesem Fach herausgefiltert.
	 * @param  idKursart        falls > 0 und idFach > 0, werden Schüler mit dieser Fach/Kursart Kombination herausgefiltert.
	 * @param  konfliktTyp      falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param  subString        falls |pSubString| > 0 werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 *
	 * @return eine nach Kriterien gefilterte Menge aller Schüler.
	 */
	public @NotNull List<@NotNull Schueler> getMengeDerSchuelerGefiltert(final long idKurs, final long idFach, final int idKursart, final int konfliktTyp, final @NotNull String subString) {
		final @NotNull List<@NotNull Schueler> menge = new ArrayList<>();

		for (final @NotNull Schueler schueler : _parent.getMengeOfSchueler())
			if (getOfSchuelerErfuelltKriterien(schueler.id, idKurs, idFach, idKursart, konfliktTyp, subString, null, null))
				menge.add(schueler);

		return menge;
	}

	/**
	 * Liefert die Anzahl der Schüler, die den Filterkriterien entsprechen.
	 *
	 * @param  idKurs           falls > 0, werden Schüler des Kurses herausgefiltert.
	 * @param  idFach           falls > 0, werden Schüler mit diesem Fach herausgefiltert.
	 * @param  idKursart        falls > 0 und idFach > 0, werden Schüler mit dieser Fach/Kursart Kombination herausgefiltert.
	 * @param  konfliktTyp      falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param  subString        falls |pSubString| > 0 werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 * @param  geschlecht       falls != null, werden die Schüler mit diesem {@link Geschlecht} herausgefiltert.
	 * @param  schriftlichkeit  falls != null, werden die Schüler mit dieser {@link GostSchriftlichkeit} herausgefiltert (isKurs oder idFach/idKursart müssen definiert sein).
	 *
	 * @return die Anzahl der Schüler, die den Filterkriterien entsprechen.
	 */
	public int getCountDerSchuelerGefiltert(final long idKurs, final long idFach, final int idKursart, final int konfliktTyp, final @NotNull String subString, final Geschlecht geschlecht, final GostSchriftlichkeit schriftlichkeit) {
		int summe = 0;

		for (final @NotNull Schueler schueler : _parent.getMengeOfSchueler())
			if (getOfSchuelerErfuelltKriterien(schueler.id, idKurs, idFach, idKursart, konfliktTyp, subString, geschlecht, schriftlichkeit))
				summe++;

		return summe;
	}

	private boolean getOfSchuelerErfuelltKriterien(final long idSchueler, final long idKurs, final long idFach, final int idKursart, final int konfliktTyp, @NotNull final String subString, final Geschlecht geschlecht, final GostSchriftlichkeit schriftlichkeit) {

		if ((konfliktTyp == 1) && (!getOfSchuelerHatKollision(idSchueler)))
			return false;

		if ((konfliktTyp == 2) && (!getOfSchuelerHatNichtwahl(idSchueler)))
			return false;

		if ((konfliktTyp == 3) && ((!getOfSchuelerHatKollision(idSchueler)) && (!getOfSchuelerHatNichtwahl(idSchueler))))
			return false;

		if ((subString.length() > 0) && (!getOfSchuelerHatImNamenSubstring(idSchueler, subString)))
			return false;

		if ((geschlecht != null) && (getOfSchuelerGeschlechtOrException(idSchueler) != geschlecht))
			return false;

		// Kurs-Filter
		if (idKurs > 0) {
			if (!getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs))
				return false;
			// Schüler hat den Kurs. Stimmt die Schriftlichkeit ebenfalls?
			if ((schriftlichkeit != null) && (schriftlichkeit.getIstSchriftlichOrException() != getOfSchuelerFachwahlZuKurs(idSchueler, idKurs).istSchriftlich))
				return false;
		}

		if (idFach > 0) {
			if (idKursart > 0) {
				// Fach/Kursart-Filter
				if (!getOfSchuelerHatFachwahl(idSchueler, idFach, idKursart))
					return false;
			} else {
				// Fach-Filter
				if (!getOfSchuelerHatFach(idSchueler, idFach))
					return false;
			}
			// Schüler hat das Fach. Stimmt die Schriftlichkeit ebenfalls?
			if ((schriftlichkeit != null) && (schriftlichkeit.getIstSchriftlichOrException() != getOfSchuelerFachwahlZuFach(idSchueler, idFach).istSchriftlich))
				return false;
		}

		return true;
	}

	/**
	 * Liefert die Fachwahl des Schüler passend zu den Kurs.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Fachwahl des Schüler passend zu den Kurs.
	 */
	public @NotNull GostFachwahl getOfSchuelerFachwahlZuKurs(final long idSchueler, final long idKurs) {
		final long idFach = getKursE(idKurs).fachID;
		return _parent.getOfSchuelerOfFachFachwahl(idSchueler, idFach);
	}

	/**
	 * Liefert die Fachwahl des Schüler passend zum Fach.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches.
	 *
	 * @return die Fachwahl des Schüler passend zum Fach.
	 */
	public @NotNull GostFachwahl getOfSchuelerFachwahlZuFach(final long idSchueler, final long idFach) {
		return _parent.getOfSchuelerOfFachFachwahl(idSchueler, idFach);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public int getStatistikSchuelerMaennlich() {
		return getCountDerSchuelerGefiltert(0, 0, 0, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public int getStatistikSchuelerWeiblich() {
		return getCountDerSchuelerGefiltert(0, 0, 0, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public int getStatistikSchuelerDivers() {
		return getCountDerSchuelerGefiltert(0, 0, 0, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public int getStatistikSchuelerOhneAngabe() {
		return getCountDerSchuelerGefiltert(0, 0, 0, 0, "", Geschlecht.X, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public int getStatistikOfKursSchuelerMaennlich(final long idKurs) {
		return getCountDerSchuelerGefiltert(idKurs, 0, 0, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public int getStatistikOfKursSchuelerWeiblich(final long idKurs) {
		return getCountDerSchuelerGefiltert(idKurs, 0, 0, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public int getStatistikOfKursSchuelerDivers(final long idKurs) {
		return getCountDerSchuelerGefiltert(idKurs, 0, 0, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public int getStatistikOfKursSchuelerOhneAngabe(final long idKurs) {
		return getCountDerSchuelerGefiltert(idKurs, 0, 0, 0, "", Geschlecht.X, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public int getStatistikOfFachSchuelerMaennlich(final long idFach) {
		return getCountDerSchuelerGefiltert(0, idFach, 0, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public int getStatistikOfFachSchuelerWeiblich(final long idFach) {
		return getCountDerSchuelerGefiltert(0, idFach, 0, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public int getStatistikOfFachSchuelerDivers(final long idFach) {
		return getCountDerSchuelerGefiltert(0, idFach, 0, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public int getStatistikOfFachSchuelerOhneAngabe(final long idFach) {
		return getCountDerSchuelerGefiltert(0, idFach, 0, 0, "", Geschlecht.X, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public int getStatistikOfFachOfKursartSchuelerMaennlich(final long idFach, final int idKursart) {
		return getCountDerSchuelerGefiltert(0, idFach, idKursart, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public int getStatistikOfFachOfKursartSchuelerWeiblich(final long idFach, final int idKursart) {
		return getCountDerSchuelerGefiltert(0, idFach, idKursart, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public int getStatistikOfFachOfKursartSchuelerDivers(final long idFach, final int idKursart) {
		return getCountDerSchuelerGefiltert(0, idFach, idKursart, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public int getStatistikOfFachOfKursartSchuelerOhneAngabe(final long idFach, final int idKursart) {
		return getCountDerSchuelerGefiltert(0, idFach, idKursart, 0, "", Geschlecht.X, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler des Kurses mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 */
	public int getStatistikOfKursSchriftlich(final long idKurs) {
		return getCountDerSchuelerGefiltert(idKurs, 0, 0, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler des Kurses mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 */
	public int getStatistikOfKursMuendlich(final long idKurs) {
		return getCountDerSchuelerGefiltert(idKurs, 0, 0, 0, "", null, GostSchriftlichkeit.MUENDLICH);
	}

	/**
	 * Liefert die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 */
	public int getStatistikOfFachSchriftlich(final long idFach) {
		return getCountDerSchuelerGefiltert(0, idFach, 0, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH);
	}

	/**
	 * Liefert die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 */
	public int getStatistikOfFachMuendlich(final long idFach) {
		return getCountDerSchuelerGefiltert(0, idFach, 0, 0, "", null, GostSchriftlichkeit.MUENDLICH);
	}

	/**
	 * Liefert die Anzahl aller Schüler des übergebenen Faches und Kursart mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler des übergebenen Faches und Kursart mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 */
	public int getStatistikOfFachUndKursartSchriftlich(final long idFach, final int idKursart) {
		return getCountDerSchuelerGefiltert(0, idFach, idKursart, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH);
	}

	/**
	 * Liefert die Anzahl aller Schüler des übergebenen Faches und Kursart mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler des übergebenen Faches und Kursart mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 */
	public int getStatistikOfFachUndKursartMuendlich(final long idFach, final int idKursart) {
		return getCountDerSchuelerGefiltert(0, idFach, idKursart, 0, "", null, GostSchriftlichkeit.MUENDLICH);
	}

	// TODO BAR refactored until here
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
	 * @throws DeveloperNotificationException falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public void setKursSchiene(final long idKurs, final long idSchiene, final boolean hinzufuegenOderEntfernen) throws DeveloperNotificationException {
		if (hinzufuegenOderEntfernen)
			stateKursSchieneHinzufuegen(idKurs, idSchiene);
		else
			stateKursSchieneEntfernen(idKurs, idSchiene);
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
