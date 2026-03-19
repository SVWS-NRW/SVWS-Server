package de.svws_nrw.service.enm;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag;
import de.svws_nrw.asd.data.schule.FloskelgruppenartKatalogEintrag;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.schule.Floskelgruppenart;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.enm.ENMAnkreuzkompetenz;
import de.svws_nrw.core.data.enm.ENMDaten;
import de.svws_nrw.core.data.enm.ENMFach;
import de.svws_nrw.core.data.enm.ENMFloskel;
import de.svws_nrw.core.data.enm.ENMFloskelgruppe;
import de.svws_nrw.core.data.enm.ENMJahrgang;
import de.svws_nrw.core.data.enm.ENMKlasse;
import de.svws_nrw.core.data.enm.ENMLeistung;
import de.svws_nrw.core.data.enm.ENMLerngruppe;
import de.svws_nrw.core.data.enm.ENMSchueler;
import de.svws_nrw.core.data.enm.ENMTeilleistungsart;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelgruppen;
import de.svws_nrw.db.dto.current.katalog.DTOFloskeln;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulCredentials;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsNotenmodulCredentials;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerTeilleistungen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Zugriff auf die ENM-Daten
 */
public final class EnmV1GetService {

	/** Der Daten-Kontext für diesen Service */
	private final EnmV1GetServiceKontext kontext;


	/**
	 * Erstellt einen neuen Service mit dem angegebenen Daten-Kontext.
	 *
	 * @param kontext   der Daten-Kontext
	 */
	public EnmV1GetService(final EnmV1GetServiceKontext kontext) {
		this.kontext = kontext;
	}

	/**
	 * Füge Lehrer-Credentials zu den ENM-Daten hinzu, sofern die ENM-Daten diesen nicht bereits enthalten
	 *
	 * @param idLehrer   die ID des hinzuzufügenden Lehrers
	 */
	private void addLehrerIfNotExists(final long idLehrer) {
		// Prüfe, ob die Lehrer-Daten bereits enthalten sind.
		if (kontext.manager.getLehrer(idLehrer) != null) {
			return;
		}

		final DTOLehrer lehrer = kontext.getLehrer(idLehrer);
		if (lehrer == null) {
			return;
		}

		// Ermittle ggf. Credentials, lasse bei aus Lehrer eingeschränkten ENM-Daten aber die Credentials leer
		DTONotenmodulCredentials creds = null;
		DTOTimestampsNotenmodulCredentials tsCreds = null;
		if (!kontext.istLehrerSpezifisch() || (kontext.getLehrerSpezfisch().ID == lehrer.ID)) {
			creds = kontext.getCredentials(lehrer.ID);
			tsCreds = kontext.getTimestampCredentials(lehrer.ID);
		}

		// Füge den Lehrer hinzu
		kontext.manager.addLehrer(lehrer.ID, lehrer.Kuerzel, lehrer.Nachname, lehrer.Vorname, lehrer.Geschlecht, lehrer.eMailDienstlich,
				(creds == null) ? "" : creds.passwordHash, (tsCreds == null) ? null : tsCreds.tsPasswordHash);
	}


	/**
	 * Prüft, ob die ENM-Daten den Jahrgang aus dem Lernabschnitt bereits enthalten. Ist dies nicht der Fall,
	 * so wird der Jahrgang hinzugefügt.
	 * Liefert die ENM-Daten zur Klasse aus dem Manager. Fügt die Klasse aus dem Lernabschnitt zu den ENM-Daten hinzu,
	 * sofern sie nicht bereits in den Daten existiert
	 *
	 * @param idJahrgang   die ID des Jahrgangs
	 */
	private void addJahrgangIfNotExists(final Long idJahrgang) {
		// Ist die ID nicht vorhanden, so braucht kein Jahrgang geprüft werden
		if (idJahrgang == null) {
			return;
		}

		// Prüfe ob der Jahrgang bereits vorhanden ist.
		final ENMJahrgang enmJahrgang = kontext.manager.getJahrgang(idJahrgang);
		if (enmJahrgang != null) {
			return;
		}

		// Wenn nicht, dann füge ihn hinzu ...
		final DTOJahrgang dtoJahrgang = kontext.getJahrgang(idJahrgang);
		if (dtoJahrgang == null)
			throw new NullPointerException("Kein Jahrgang zu der Jahrgangs-ID gefunden.");
		kontext.manager.addJahrgang(dtoJahrgang.ID, dtoJahrgang.ASDJahrgang, dtoJahrgang.InternKrz,
				dtoJahrgang.ASDBezeichnung, dtoJahrgang.Sekundarstufe, dtoJahrgang.Sortierung);
	}


	/**
	 * Liefert die ENM-Daten zur Klasse aus dem Manager. Fügt die Klasse aus dem Lernabschnitt zu den ENM-Daten hinzu,
	 * sofern sie nicht bereits in den Daten existiert
	 *
	 * @param lernabschnitt   der zu integrierende Lernabschnitt
	 *
	 * @return die Daten der ENM-Klasse
	 */
	private ENMKlasse addKlasseIfNotExists(final DTOSchuelerLernabschnittsdaten lernabschnitt) {
		// Lese die Klasse aus dem Kontext aus. Dort muss sie vorliegen...
		final DTOKlassen dtoKlasse = kontext.getKlasse(lernabschnitt.Klassen_ID);
		if (dtoKlasse == null)
			throw new NullPointerException();

		// Prüfe, ob die Klasse bereits zu den ENM-Daten hinzugefügt wurde
		ENMKlasse enmKlasse = kontext.manager.getKlasse(dtoKlasse.ID);
		if (enmKlasse != null) {
			return enmKlasse;
		}

		// Sie existiert noch nicht in den ENM-Daten. Füge sie hinzu...
		addJahrgangIfNotExists(dtoKlasse.Jahrgang_ID);

		kontext.manager.addKlasse(dtoKlasse.ID, dtoKlasse.ASDKlasse, dtoKlasse.Klasse, dtoKlasse.Jahrgang_ID, dtoKlasse.Sortierung);
		enmKlasse = kontext.manager.getKlasse(dtoKlasse.ID);
		final List<Long> idsKlassenleitungen = kontext.getKlassenleitungen(dtoKlasse.ID);
		if (idsKlassenleitungen != null) {
			for (final Long idKlassenleitung : idsKlassenleitungen) {
				if (kontext.manager.getLehrer(idKlassenleitung) == null) {
					addLehrerIfNotExists(idKlassenleitung);
				}
				enmKlasse.klassenlehrer.add(idKlassenleitung);
			}
		}
		return enmKlasse;
	}


	/**
	 * Liefert die ENM-Daten zur Klasse aus dem Manager. Fügt die Klasse aus dem Lernabschnitt zu den ENM-Daten hinzu,
	 * sofern sie nicht bereits in den Daten existiert
	 *
	 * @param lernabschnitt   der zu integrierende Lernabschnitt
	 * @param enmKlasse       die Klassendaten aus den ENM-Daten
	 *
	 * @return die Daten des ENM-Schülers
	 */
	private ENMSchueler addSchuelerIfNotExists(final DTOSchuelerLernabschnittsdaten lernabschnitt, final ENMKlasse enmKlasse) {
		ENMSchueler enmSchueler = kontext.manager.getSchueler(lernabschnitt.Schueler_ID);
		if (enmSchueler != null) {
			return enmSchueler;
		}

		addJahrgangIfNotExists(lernabschnitt.Jahrgang_ID);

		final var dtoSchueler = kontext.getSchueler(lernabschnitt.Schueler_ID);
		kontext.manager.addSchueler(dtoSchueler.ID, lernabschnitt.Jahrgang_ID, enmKlasse.id, dtoSchueler.Nachname, dtoSchueler.Vorname,
				dtoSchueler.Geschlecht, lernabschnitt.BilingualerZweig, lernabschnitt.ZieldifferentesLernen,
				dtoSchueler.Lernstandsbericht);  // Deutsch als Fremdsprache liegt vor, wenn für den Schüler Lernstandsberichte geschrieben werden...

		final DTOSchuelerPSFachBemerkungen bemerkungen = kontext.getBemerkungen(lernabschnitt.ID);
		final DTOTimestampsSchuelerLernabschnittsdaten tsLernabschnitt = kontext.getLernabschnittTimestamps(lernabschnitt.ID);
		enmSchueler = kontext.manager.getSchueler(lernabschnitt.Schueler_ID);
		enmSchueler.lernabschnitt.id = lernabschnitt.ID;
		enmSchueler.lernabschnitt.fehlstundenGesamt = lernabschnitt.SumFehlStd;
		enmSchueler.lernabschnitt.tsFehlstundenGesamt = tsLernabschnitt.tsSumFehlStd;
		enmSchueler.lernabschnitt.fehlstundenGesamtUnentschuldigt = lernabschnitt.SumFehlStdU;
		enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt = tsLernabschnitt.tsSumFehlStdU;
		enmSchueler.lernabschnitt.pruefungsordnung = lernabschnitt.PruefOrdnung;
		final Note noteLB1 = Note.fromNoteSekI(lernabschnitt.Gesamtnote_GS);
		final Note noteLB2 = Note.fromNoteSekI(lernabschnitt.Gesamtnote_NW);
		enmSchueler.lernabschnitt.lernbereich1note = (noteLB1 == null) ? null : noteLB1.daten(kontext.getSchuljahr()).kuerzel;
		enmSchueler.lernabschnitt.lernbereich2note = (noteLB2 == null) ? null : noteLB2.daten(kontext.getSchuljahr()).kuerzel;
		enmSchueler.lernabschnitt.foerderschwerpunkt1 = kontext.getFoerderschwerpunktKuerzel(lernabschnitt.Foerderschwerpunkt_ID);
		enmSchueler.lernabschnitt.foerderschwerpunkt2 = kontext.getFoerderschwerpunktKuerzel(lernabschnitt.Foerderschwerpunkt2_ID);
		enmSchueler.bemerkungen.ASV = (bemerkungen == null) ? null : bemerkungen.ASV;
		enmSchueler.bemerkungen.tsASV = tsLernabschnitt.tsASV;
		enmSchueler.bemerkungen.AUE = (bemerkungen == null) ? null : bemerkungen.AUE;
		enmSchueler.bemerkungen.tsAUE = tsLernabschnitt.tsAUE;
		enmSchueler.bemerkungen.ZB = lernabschnitt.ZeugnisBem;
		enmSchueler.bemerkungen.tsZB = tsLernabschnitt.tsZeugnisBem;
		enmSchueler.bemerkungen.LELS = (bemerkungen == null) ? null : bemerkungen.LELS;
		enmSchueler.bemerkungen.tsLELS = tsLernabschnitt.tsLELS;
		enmSchueler.bemerkungen.schulformEmpf = (bemerkungen == null) ? null : bemerkungen.ESF;
		enmSchueler.bemerkungen.tsSchulformEmpf = tsLernabschnitt.tsESF;
		enmSchueler.bemerkungen.individuelleVersetzungsbemerkungen = (bemerkungen == null) ? null : bemerkungen.BemerkungVersetzung;
		enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen = tsLernabschnitt.tsBemerkungVersetzung;
		enmSchueler.bemerkungen.foerderbemerkungen = (bemerkungen == null) ? null : bemerkungen.BemerkungFSP;
		enmSchueler.bemerkungen.tsFoerderbemerkungen = tsLernabschnitt.tsBemerkungFSP;
		return enmSchueler;
	}


	/**
	 * Hilfsmethode zum Bestimmen der allgemeinen Kursart für die übergeben Kursart
	 *
	 * @param kursart   die Kursart
	 *
	 * @return die allgemeine Kursart
	 */
	private String getKursartAllg(final ZulaessigeKursart kursart) {
		if (kursart == null) {
			return null;
		}
		final ZulaessigeKursartKatalogEintrag kursartEintrag = kursart.daten(kontext.getSchuljahr());
		if ((kursartEintrag.kuerzelAllg == null) || "".equals(kursartEintrag.kuerzelAllg)) {
			return kursartEintrag.kuerzel;
		}
		return kursartEintrag.kuerzelAllg;
	}


	/**
	 * Liefert die ENM-Daten zur Lerngruppe aus dem Manager. Fügt die Lerngruppe zu den ENM-Daten hinzu,
	 * sofern sie nicht bereits in den Daten existiert
	 *
	 * @param lernabschnitt   der aktuelle Lernabschnitt
	 * @param leistung        die aktuellen Leistungsdaten
	 * @param kursart         die Kursart zu den aktuellen Leistungsdaten
	 * @param enmKlasse       die Klassendaten aus den ENM-Daten
	 *
	 * @return die Daten der ENM-Lerngruppe
	 */
	private ENMLerngruppe addLerngruppeIfNotExists(final DTOSchuelerLernabschnittsdaten lernabschnitt, final DTOSchuelerLeistungsdaten leistung,
			final ZulaessigeKursart kursart, final ENMKlasse enmKlasse) {

		// Erstelle eine temporäre LerngruppenID: Dient zur Kurs- und Klassenübergreifenden Identifikation der Lerngruppe
		final String strLerngruppenID = (leistung.Kurs_ID == null)
				? ("Klasse:" + lernabschnitt.Klassen_ID + "/" + leistung.Fach_ID)
				: ("Kurs:" + leistung.Kurs_ID);

		// Prüfe, ob die Lerngruppe bereits vorhanden ist.
		ENMLerngruppe lerngruppe = kontext.manager.getLerngruppe(strLerngruppenID);
		if (lerngruppe != null) {
			return lerngruppe;
		}

		// Ermittle das Fach und füge es ggf. zu den ENM-Daten hinzu
		final DTOFach fach = kontext.getFach(leistung.Fach_ID);
		if (kontext.manager.getFach(fach.ID) == null) {
			kontext.manager.addFach(fach.ID, fach.StatistikKuerzel, fach.Kuerzel, fach.SortierungAllg, fach.IstFremdsprache);
		}

		// Unterscheidung zwischen den beiden Lerngruppen-Typen und füge die Lerngruppe hinzu...
		final String kursartAllg = getKursartAllg(kursart);
		if (leistung.Kurs_ID == null) {  // es ist eine Klasse
			kontext.manager.addLerngruppe(strLerngruppenID, enmKlasse.id, leistung.Fach_ID, null, fach.Kuerzel, kursartAllg,
					fach.Unterrichtssprache, (leistung.Wochenstunden == null) ? 0 : leistung.Wochenstunden);
		} else {  // es ist ein Kurs
			final DTOKurs kurs = kontext.getKurs(leistung.Kurs_ID);
			kontext.manager.addLerngruppe(strLerngruppenID, leistung.Kurs_ID, leistung.Fach_ID,
					(kursart == null) ? -1 : Integer.parseInt(kursart.daten(kontext.getSchuljahr()).nummer), kurs.KurzBez, kursartAllg,
					fach.Unterrichtssprache, kurs.WochenStd);
		}
		lerngruppe = kontext.manager.getLerngruppe(strLerngruppenID);
		lerngruppe.lehrerID.add(leistung.Fachlehrer_ID);
		if (kontext.manager.getLehrer(leistung.Fachlehrer_ID) == null) {
			addLehrerIfNotExists(leistung.Fachlehrer_ID);
		}
		return lerngruppe;
	}


	/**
	 * Prüft, ob die ENM-Daten die Teilleistungart bereits enthält. Ist dies nicht der Fall, so wird sie hinzugefügt.
	 *
	 * @param idArt     die ID der Teilleistungart
	 */
	private void addTeilleistungsartIfNotExists(final long idArt) {
		final ENMTeilleistungsart enmTeilleistungsart = kontext.manager.getTeilleistungsart(idArt);
		if (enmTeilleistungsart == null) {
			final DTOTeilleistungsarten dtoArt = kontext.getTeilleistungsart(idArt);
			if (dtoArt == null) { // DB-Error -> should not happen
				throw new NullPointerException();
			}
			kontext.manager.addTeilleistungsart(dtoArt.ID, dtoArt.Bezeichnung,
					(dtoArt.Sortierung == null) ? 32000 : dtoArt.Sortierung,
					(dtoArt.Gewichtung == null) ? 1.0 : dtoArt.Gewichtung);
		}
	}

	/**
	 * Durchwandert die Teilleistungen und ergänzt die ENM-Daten um fehlende Einträge.
	 *
	 * @param enmLeistung     die Leistungsdaten aus den ENM-Daten
	 */
	private void processSchuelerTeilleistungen(final ENMLeistung enmLeistung) {

		// Ermittle die Teilleistungen zu den Leistungsdaten
		final List<DTOSchuelerTeilleistung> teilleistungen = kontext.getTeilleistungen(enmLeistung.id);
		if (teilleistungen == null) {
			return;
		}

		for (final DTOSchuelerTeilleistung teilleistung : teilleistungen) {
			if (teilleistung.Art_ID == null)
				continue;

			addTeilleistungsartIfNotExists(teilleistung.Art_ID);

			// Füge die Teilleistung hinzu
			final DTOTimestampsSchuelerTeilleistungen teilleistungTimestamps = kontext.getTeilleistungTimestamps(teilleistung.ID);
			if (teilleistungTimestamps == null) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Es konnten keine Zeitstempel für die Teilleistungen ausgelesen werden. Dies deutet auf einen Fehler in der Datenbank hin.");
			}
			kontext.manager.addSchuelerTeilleistung(enmLeistung, teilleistung.ID, teilleistung.Art_ID, teilleistungTimestamps.tsArt_ID,
					teilleistung.Datum, teilleistungTimestamps.tsDatum, teilleistung.Bemerkung, teilleistungTimestamps.tsBemerkung,
					teilleistung.NotenKrz, teilleistungTimestamps.tsNotenKrz);
		}
	}


	/**
	 * Prüft die Schriftlichkeit eines Kurses an der
	 *
	 * @param idKurs    die ID des Kurses oder null, wenn es sich nicht um einen Kurs handelt
	 * @param kursart   die Kursart des Kurses
	 * @param klasse    das ASD-Kürzel der Klasse
	 * @param halbjahr  das Halbjahr des Kurses
	 *
	 * @return true, wenn der Kurs sicher schriftlich ist.
	 */
	private static boolean pruefeKursSchriftlichkeit(final Long idKurs, final ZulaessigeKursart kursart, final String klasse, final int halbjahr) {
		// Wenn es sich bei den Leistungsdaten nicht um eine Kurs handelt, so wird die Schriftlichkeit hier nicht geprüft
		if ((idKurs == null) || (kursart == null))
			return false;

		// Ein LK und das 1.-3. Abiturfach sind in der Qualifikationsphase immer schriftlich
		if (Set.of(ZulaessigeKursart.LK1, ZulaessigeKursart.LK2, ZulaessigeKursart.GKS, ZulaessigeKursart.AB3).contains(kursart)) {
			return true;
		}

		// Das 4. Abiturfach ist schriftlich, außer im letzten Halbjahr der Q2 (mündliche Prüfung)
		if (kursart == ZulaessigeKursart.AB4) {
			return !("Q2".equals(klasse) && halbjahr == 2);
		}

		return false;
	}


	/**
	 * Fügt die ENM-Leistungsdaten hinzu.
	 *
	 * @param leistung        die aktuellen Leistungsdaten
	 * @param kursart         die Kursart zu den aktuellen Leistungsdaten
	 * @param enmKlasse       die Klasse aus den ENM-Daten
	 * @param enmSchueler     der Schüler aus den ENM-Daten
	 * @param enmLerngruppe   die Lerngruppe aus den ENM-Daten
	 *
	 * @return die erstellten Leistungsdaten der ENM-Daten
	 */
	private ENMLeistung addSchuelerLeistungsdaten(final DTOSchuelerLeistungsdaten leistung,
			final ZulaessigeKursart kursart, final ENMKlasse enmKlasse, final ENMSchueler enmSchueler, final ENMLerngruppe enmLerngruppe) {
		final boolean istSchriftlich = pruefeKursSchriftlichkeit(leistung.Kurs_ID, kursart, enmKlasse.kuerzel, kontext.getHalbjahr());

		final Integer abiFach = Optional.ofNullable(leistung.AbiFach).map(Integer::valueOf).filter(v -> (v >= 1) && (v <= 5)).orElse(null);
		final boolean istGemahnt = (leistung.Warnung != null) && leistung.Warnung;
		final String mahndatum = leistung.Warndatum;
		final DTOTimestampsSchuelerLeistungsdaten tsLeistung = kontext.getLeistungsdatenTimestamps(leistung.ID);
		return kontext.manager.addSchuelerLeistungsdaten(enmSchueler,
				leistung.ID, enmLerngruppe.id, leistung.NotenKrz, tsLeistung.tsNotenKrz,
				leistung.NotenKrzQuartal, tsLeistung.tsNotenKrzQuartal, istSchriftlich, abiFach,
				leistung.FehlStd, tsLeistung.tsFehlStd, leistung.uFehlStd, tsLeistung.tsuFehlStd,
				leistung.Lernentw, tsLeistung.tsLernentw, null, istGemahnt, tsLeistung.tsWarnung, mahndatum);
	}


	/**
	 * Durchwandert die Leistungsdaten und ergänzt die ENM-Daten um fehlende Einträge.
	 *
	 * @param lernabschnitt   der zu integrierende Lernabschnitt
	 * @param enmKlasse       die Daten der aktuellen Klasse aus den ENM-Daten
	 * @param enmSchueler     die Daten des aktuellen Schülers aus den ENM-Daten
	 *
	 * @return gibt die Liste der Fach-IDs zurück, die bei den Schülerleistungsdaten vorkommen, um diese bei den Ankreuzkompetenzen zu kennen,
	 *         wenn die ENM-Daten Lehrer-Spezifisch erstellt werden.
	 */
	private Set<Long> processSchuelerLeistungen(final DTOSchuelerLernabschnittsdaten lernabschnitt, final ENMKlasse enmKlasse,
			final ENMSchueler enmSchueler) {
		final Set<Long> leistungenFachIDs = new HashSet<>();
		final List<DTOSchuelerLeistungsdaten> leistungen = kontext.getLeistungsdaten(lernabschnitt.ID);
		if (leistungen == null) {
			return leistungenFachIDs;
		}

		for (final DTOSchuelerLeistungsdaten leistung : leistungen) {
			if (leistung.Fachlehrer_ID == null)
				continue;

			final ZulaessigeKursart kursart = (leistung.Kurs_ID == null) ? null : ZulaessigeKursart.data().getWertByKuerzel(leistung.Kursart);
			final ENMLerngruppe lerngruppe = addLerngruppeIfNotExists(lernabschnitt, leistung, kursart, enmKlasse);

			if (kontext.istLehrerSpezifisch() && (leistung.Fachlehrer_ID == kontext.getLehrerSpezfisch().ID)) {
				leistungenFachIDs.add(lerngruppe.fachID);
			}

			final ENMLeistung enmLeistung = addSchuelerLeistungsdaten(leistung, kursart, enmKlasse, enmSchueler, lerngruppe);

			processSchuelerTeilleistungen(enmLeistung);

			// TODO check and add ZP10 - Data
			// TODO check and add BKAbschluss - Data
		}
		return leistungenFachIDs;
	}


	/**
	 * Ermittelt den ASD-Jahrgang einer Jahrgangseinschränkung bei einer Ankreuzkompetenz in Bezug auf eine Klasse. Liegt keine Einschränkung
	 * vor oder beziehen sich die Einschränkungen nicht auf diese Klasse, so wird ein leerer String zurückgegeben.
	 *
	 * @param idAnkreuzkompetenz        die ID der Ankreuzkompetenz
	 * @param idKlasse                  die ID der Klasse
	 *
	 * @return das ASD-Kürzel oder ein leerer String
	 */
	private String getJahrgangOfAnkreuzkompetenz(final long idAnkreuzkompetenz, final Long idKlasse) {
		// Prüfe, ob die Klasse zugeordnet ist und der Klasse ein Jahrgang
		final DTOKlassen klasse = (idKlasse == null) ? null : kontext.getKlasse(idKlasse);
		if ((klasse == null) || (klasse.Jahrgang_ID == null)) {
			return "";
		}

		// Prüfe, ob der Ankreuzkompetenz der Jahrgang der Klasse zugeordnet ist
		final long idJahrgang = klasse.Jahrgang_ID;
		final boolean exists = kontext.getAnkreuzkompetenzJahrgaenge(idJahrgang).stream().anyMatch(dto -> dto.idAnkreuzkompetenz == idAnkreuzkompetenz);
		if (!exists) {
			return "";
		}
		return Optional.ofNullable(kontext.getJahrgang(idJahrgang)).map(dto -> dto.ASDJahrgang).orElse("");
	}


	/**
	 * Durchwandert die Ankreuzkompetenzen eine Schüler-Lernabschnittes und ergänzt die ENM-Daten um fehlende Einträge.
	 *
	 * @param lernabschnitt   der zu integrierende Lernabschnitt
	 * @param enmKlasse       die Daten der aktuellen Klasse aus den ENM-Daten
	 * @param enmSchueler     die Daten des aktuellen Schülers aus den ENM-Daten
	 * @param fachIDs         die Fach-IDs aus den Leistungsdaten, um erkennen zu können bei welchen Fächern der Lehrer unterrichtet
	 */
	private void processSchuelerAnkreuzkompetenzenOfLernabschnitt(final DTOSchuelerLernabschnittsdaten lernabschnitt, final ENMKlasse enmKlasse,
			final ENMSchueler enmSchueler, final Set<Long> fachIDs) {
		final List<DTOSchuelerAnkreuzfloskeln> ankreuzkompetenzen = kontext.getSchuelerAnkreuzkompetenzen(lernabschnitt.ID);
		if (ankreuzkompetenzen == null) {
			return;
		}
		for (final DTOSchuelerAnkreuzfloskeln ankreuzkompetenz : ankreuzkompetenzen) {
			final DTOAnkreuzfloskeln dtoAnkreuzkompetenz = kontext.getKatalogeintragAnkreuzkompetenz(ankreuzkompetenz.Floskel_ID);
			if (dtoAnkreuzkompetenz == null) // DB-Error -> should not happen
				throw new NullPointerException();

			// Überspringe bei Lehrer-Spezifischen Daten die Ankreuzkompetezen, falls keine Fachlehrer-Zuordnung vorliegt bzw. der Lehrer kein Klassenlehrer ist
			if ((kontext.istLehrerSpezifisch())
					&& (((dtoAnkreuzkompetenz.Fach_ID == null) && !enmKlasse.klassenlehrer.contains(kontext.getLehrerSpezfisch().ID))
							|| ((dtoAnkreuzkompetenz.Fach_ID != null) && !fachIDs.contains(dtoAnkreuzkompetenz.Fach_ID)))) {
				continue;
			}

			// Prüfe die Ankreuzfloskel und ergänze sie ggf.
			final String jahrgang = getJahrgangOfAnkreuzkompetenz(ankreuzkompetenz.ID, lernabschnitt.Fachklasse_ID);
			final ENMAnkreuzkompetenz enmAnkreuzkompetenz = kontext.manager.getAnkreuzkompetenz(ankreuzkompetenz.Floskel_ID);
			if (enmAnkreuzkompetenz == null)
				kontext.manager.addAnkreuzkompetenz(dtoAnkreuzkompetenz.ID, (dtoAnkreuzkompetenz.IstASV == 0), dtoAnkreuzkompetenz.Fach_ID,
						jahrgang, dtoAnkreuzkompetenz.FloskelText, dtoAnkreuzkompetenz.Sortierung);
			// Füge die Schueler-Ankreuzkompetenz hinzu
			final var ankreuzkompetenzTimestamps = kontext.getSchuelerAnkreuzkompetenzenTimestamps(ankreuzkompetenz.ID);
			if (ankreuzkompetenzTimestamps == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Es konnten keine Zeitstempel für die Ankreuzkompetenzen ausgelesen werden. Dies deutet auf einen Fehler in der Datenbank hin.");
			final boolean[] stufen = { ankreuzkompetenz.Stufe1, ankreuzkompetenz.Stufe2, ankreuzkompetenz.Stufe3, ankreuzkompetenz.Stufe4,
					ankreuzkompetenz.Stufe5 };
			kontext.manager.addSchuelerAnkreuzkompetenz(enmSchueler, ankreuzkompetenz.ID, ankreuzkompetenz.Floskel_ID, stufen,
					ankreuzkompetenzTimestamps.tsStufe);
		}
	}


	/**
	 * Integriert die Daten des Schüler-Lernabschnitts in die ENM-Daten
	 *
	 * @param lernabschnitt   der zu integrierende Lernabschnitt
	 */
	private void processSchuelerLernabschnitt(final DTOSchuelerLernabschnittsdaten lernabschnitt) {
		if ((lernabschnitt.Klassen_ID == null) || (lernabschnitt.Jahrgang_ID == null))
			return;

		final ENMKlasse enmKlasse = addKlasseIfNotExists(lernabschnitt);
		final ENMSchueler enmSchueler = addSchuelerIfNotExists(lernabschnitt, enmKlasse);
		final Set<Long> idsFaecher = processSchuelerLeistungen(lernabschnitt, enmKlasse, enmSchueler);
		processSchuelerAnkreuzkompetenzenOfLernabschnitt(lernabschnitt, enmKlasse, enmSchueler, idsFaecher);
	}


	/**
	 * Ermittelt die ENM-Daten des aktuellen Schuljahresabschnitts zu dem Lehrer mit der angegebenen ID.
	 * Ist die ID null so werden die ENM-Daten für alle Lehrer generiert.
	 *
	 * @param id     die ID des Lehrers oder null
	 *
	 * @return die ENMDaten
	 */
	public ENMDaten get(final Long id) {
		return transactional(() -> {
			// Allgemeine ENM-Daten (Kontextdaten -  Schule, Kataloge, Lehrer, etc.) laden
			kontext.fetchData(id);

			// Erstelle einen ENM-Daten-Manager und füge ggf. den Lehrer hinzu für welchen die ENM-Daten erzeugt werden
			if (kontext.istLehrerSpezifisch()) {
				addLehrerIfNotExists(kontext.getLehrerSpezfisch().ID);
			}
			initManager();

			// Durchwandere die Lernabschnitte der Schüler...
			for (final DTOSchuelerLernabschnittsdaten lernabschnitt : kontext.getLernabschnitte()) {
				processSchuelerLernabschnitt(lernabschnitt);
			}

			// Ergänze den Floskelkatalog
			processFloskeln();

			return kontext.manager.daten;
		});
	}


	private void initManager() {
		// Setze die grundlegenden Schuldaten
		final var schule = kontext.getSchuldaten();
		kontext.manager.setSchuldaten(schule.SchulNr, kontext.getSchuljahr(), schule.AnzahlAbschnitte, kontext.getHalbjahr(), null, true, false, true,
				schule.SchulformKuerzel, null);
		// Setze die Informationen zu den Stufen der Ankreuzkompetenzen
		kontext.getKonfigurationAnkreuzkompetenzen().ifPresentOrElse(
				cfg -> kontext.manager.setAnkreuzkompetenzenStufen(cfg.TextStufe1, cfg.TextStufe2, cfg.TextStufe3, cfg.TextStufe4, cfg.TextStufe5,
						cfg.BezeichnungSONST),
				() -> kontext.manager.setAnkreuzkompetenzenStufen(null, null, null, null, null, null));
		// Kopiere den Noten-Katalog aus dem Core-type in die ENM-Daten
		kontext.manager.addNoten(kontext.getSchuljahr());
		// Kopiere den Förderschwerpunkt-Katalog aus dem Core-type in die ENM-Daten
		kontext.manager.addFoerderschwerpunkte(kontext.getSchuljahr(), Schulform.data().getWertByKuerzel(schule.SchulformKuerzel));
	}


	private HashMap<Long, ENMFloskelgruppe> addFloskelgruppen() {
		final HashMap<Long, ENMFloskelgruppe> map = new HashMap<>();
		for (final DTOFloskelgruppen dto : kontext.getFloskelgruppen()) {
			final ENMFloskelgruppe enmFG = new ENMFloskelgruppe();
			enmFG.kuerzel = dto.Kuerzel;
			enmFG.bezeichnung = dto.Bezeichnung;
			final FloskelgruppenartKatalogEintrag eintrag = Floskelgruppenart.data().getEintragByID(dto.Hauptgruppe_ID);
			enmFG.hauptgruppe = (eintrag == null) ? null : eintrag.schluessel;
			map.put(dto.ID, enmFG);
			kontext.manager.daten.floskelgruppen.add(enmFG);
		}
		return map;
	}


	private static void addFloskel(final DTOFloskeln floskel, final ENMFloskelgruppe enmFloskelGruppe, final Long idJahrgang,
			final ENMFach fach, final Long niveau) {
		final ENMFloskel enmFl = new ENMFloskel();
		enmFl.kuerzel = floskel.Kuerzel;
		enmFl.text = floskel.Text;
		enmFl.fachID = (fach == null) ? null : fach.id;
		enmFl.niveau = niveau;
		enmFl.jahrgangID = idJahrgang;
		enmFloskelGruppe.floskeln.add(enmFl);
	}


	/**
	 * Füllt die Datenstruktur für die Floskelgruppen des ENM mit den in der SVWS-DB hinterlegten
	 * Floskelgruppen und den zugehörigen Floskeln.
	 */
	private void processFloskeln() {
		// Ergänze zuerst die Floskelgruppen bei den ENM-Daten
		final HashMap<Long, ENMFloskelgruppe> mapFloskelgruppen = addFloskelgruppen();

		// Durchwandere die Floskel und füge diese den jeweiligen Floskelgruppen zu
		for (final DTOFloskeln floskel : kontext.getFloskeln()) {
			// Prüfe, ob die Floskel eine Gruppe zugeordnet hat - Wenn keine zugeordnet ist, wird die Floskel ignoriert
			final ENMFloskelgruppe enmFloskelGruppe = mapFloskelgruppen.get(floskel.Gruppe_ID);
			if (enmFloskelGruppe == null) {
				continue;
			}

			// Bestimme Fach und Niveau der Floskel
			final ENMFach fach = (floskel.Fach_ID == null) ? null : kontext.manager.getFach(floskel.Fach_ID);
			final Long niveau = (floskel.Niveau == null) ? null : floskel.Niveau.longValue();

			// Füge die Floskel entweder allgemein (idJahrgang = null) oder für alle Jahrgänge hinzu
			final List<Long> idsJahrgaenge = kontext.getFloskelJahrgaenge(floskel.ID);
			if (idsJahrgaenge.isEmpty()) {
				addFloskel(floskel, enmFloskelGruppe, null, fach, niveau);
			} else {
				for (final Long idJahrgang : idsJahrgaenge) {
					addFloskel(floskel, enmFloskelGruppe, idJahrgang, fach, niveau);
				}
			}
		}
	}

}
