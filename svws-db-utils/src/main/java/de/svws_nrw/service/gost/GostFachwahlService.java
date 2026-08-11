package de.svws_nrw.service.gost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostSchuelerFachwahl;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungenPK;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenVorgaben;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.gost.GostJahrgangFachbelegungenRepository;
import de.svws_nrw.repo.gost.GostJahrgangsdatenRepository;
import de.svws_nrw.repo.gost.GostSchuelerFachbelegungenRepository;
import de.svws_nrw.repo.gost.GostSchuelerRepository;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenVorgabeRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für die Fachwahlen des Gymnasialen Oberstufe
 */
public class GostFachwahlService {

	private final BenutzerAllgemeinRepository benutzerRepository;
	private final SchuelerRepository schuelerRepository;
	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;
	private final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;
	private final JahrgangRepository jahrgangRepository;
	private final FachRepository fachRepository;
	private final GostSchuelerRepository gostSchuelerRepository;
	private final GostSchuelerFachbelegungenRepository gostSchuelerFachbelegungenRepository;
	private final GostJahrgangsdatenRepository gostJahrgangsdatenRepository;
	private final GostJahrgangFachbelegungenRepository gostJahrgangFachbelegungenRepository;
	private final GostAbiturdatenService gostAbiturdatenService;
	private final GostSchuelerService gostSchuelerService;
	private final GostKlausurenVorgabeRepository gostKlausurenVorgabeRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository                     das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param schuelerRepository                     das Repository für den Zugriff auf Schülerdaten
	 * @param schuelerLernabschnittRepository        das Repository für den Zugriff auf die Schüler-Lernabschnittsdaten
	 * @param schuelerLeistungsdatenRepository       das Repository für den Zugriff auf die Schüler-Leistungsdaten
	 * @param jahrgangRepository                   das Repository für den Zugriff auf Jahrgänge
	 * @param fachRepository                         das Repository für den Zugriff auf Fächerdaten
	 * @param gostSchuelerRepository                 das Repository für den Zugriff auf die Schüler-Informationen zur Gymnasialen Oberstufe
	 * @param gostSchuelerFachbelegungenRepository   das Repository für den Zugriff auf die Schüler-Fachbelegungen
	 * @param gostJahrgangsdatenRepository           das Repository für den Zugriff auf die Jahrgangsdaten der gymnasialen Oberstufe
	 * @param gostJahrgangFachbelegungenRepository   das Repository für den Zugriff auf die Vorlage-Fachbelegungen von Abiturjahrgängen
	 * @param gostKlausurenVorgabeRepository         das Repository für den Zugriff auf die Klausurvorgaben der gymnasialen Oberstufe
	 * @param gostAbiturdatenService                 der Service für den Zugriff auf die Abiturdaten der gymnasialen Oberstufe
	 * @param gostSchuelerService                    der Service für den Zugriff auf die Schülerdaten der gymnasialen Oberstufe
	 */
	public GostFachwahlService(final BenutzerAllgemeinRepository benutzerRepository,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final JahrgangRepository jahrgangRepository,
			final FachRepository fachRepository,
			final GostSchuelerRepository gostSchuelerRepository,
			final GostSchuelerFachbelegungenRepository gostSchuelerFachbelegungenRepository,
			final GostJahrgangsdatenRepository gostJahrgangsdatenRepository,
			final GostJahrgangFachbelegungenRepository gostJahrgangFachbelegungenRepository,
			final GostKlausurenVorgabeRepository gostKlausurenVorgabeRepository,
			final GostAbiturdatenService gostAbiturdatenService,
			final GostSchuelerService gostSchuelerService) {
		this.benutzerRepository = benutzerRepository;
		this.schuelerRepository = schuelerRepository;
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
		this.schuelerLeistungsdatenRepository = schuelerLeistungsdatenRepository;
		this.jahrgangRepository = jahrgangRepository;
		this.fachRepository = fachRepository;
		this.gostSchuelerRepository = gostSchuelerRepository;
		this.gostSchuelerFachbelegungenRepository = gostSchuelerFachbelegungenRepository;
		this.gostJahrgangsdatenRepository = gostJahrgangsdatenRepository;
		this.gostJahrgangFachbelegungenRepository = gostJahrgangFachbelegungenRepository;
		this.gostKlausurenVorgabeRepository = gostKlausurenVorgabeRepository;
		this.gostAbiturdatenService = gostAbiturdatenService;
		this.gostSchuelerService = gostSchuelerService;
	}


	private void pruefeGymOb() {
		// Prüfe, ob die aktuelle Schule eine Gymnasiale Oberstufe hat oder nicht
		if (!benutzerRepository.getAktuellerBenutzer().schuleHatGymOb()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Schulform der Schule hat keine gymnasiale Oberstufe.");
		}
	}


	/**
	 * Ermittelt die Fachwahl für die gymnasiale Oberstufe zu einem Fach von dem angegebenen Schüler.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param idFach       die ID des Faches
	 *
	 * @return Die Fachwahl des Schülers
	 */
	public GostSchuelerFachwahl get(final Long idSchueler, final Long idFach) {
		return transactional(() -> {
			pruefeGymOb();
			if (schuelerRepository.findById(idSchueler).isEmpty()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler));
			}
			final DTOFach fach = fachRepository.findById(idFach)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Fach mit der ID %d gefunden.".formatted(idFach)));
			if (!Boolean.TRUE.equals(fach.IstOberstufenFach)) {
				throw new ApiOperationException(Status.NOT_FOUND);
			}
			final Optional<DTOGostSchuelerFachbelegungen> fachbelegung =
					gostSchuelerFachbelegungenRepository.findById(new DTOGostSchuelerFachbelegungenPK(idSchueler, idFach));
			final GostSchuelerFachwahl fachwahl = new GostSchuelerFachwahl();
			fachbelegung.ifPresentOrElse(fb -> {
				fachwahl.halbjahre[0] = fb.EF1_Kursart;
				fachwahl.halbjahre[1] = fb.EF2_Kursart;
				fachwahl.halbjahre[2] = fb.Q11_Kursart;
				fachwahl.halbjahre[3] = fb.Q12_Kursart;
				fachwahl.halbjahre[4] = fb.Q21_Kursart;
				fachwahl.halbjahre[5] = fb.Q22_Kursart;
				fachwahl.abiturFach = fb.AbiturFach;
				fachwahl.idReferenzfach = fb.Referenzfach_ID;
			}, () -> {
				fachwahl.halbjahre[0] = null;
				fachwahl.halbjahre[1] = null;
				fachwahl.halbjahre[2] = null;
				fachwahl.halbjahre[3] = null;
				fachwahl.halbjahre[4] = null;
				fachwahl.halbjahre[5] = null;
				fachwahl.abiturFach = null;
				fachwahl.idReferenzfach = null;
			});
			return fachwahl;
		});
	}




	/**
	 * Prüft, ob die Fachwahl in dem Halbjahr zu den Leistungsdaten passt. Ist dies nicht der Fall, so wird eine Exception generiert.
	 *
	 * @param leistungen    die Leistungen die geprüft werden
	 * @param halbjahr      das Halbjahr, auf welches sich der Patch bezieht
	 * @param istSP         gibt an, ob das Fach für die Leistungsdaten Sport ist
	 * @param fw            der Wert für die Fachwahl
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static void patchFachwahlHalbjahrCheckLeistungen(final List<DTOSchuelerLeistungsdaten> leistungen, final GostHalbjahr halbjahr, final boolean istSP,
			final String fw) throws ApiOperationException {
		for (final DTOSchuelerLeistungsdaten leistung : leistungen) {
			final ZulaessigeKursart zulkursart = ZulaessigeKursart.data().getWertByKuerzel(leistung.Kursart);
			final GostKursart kursart = GostKursart.fromKursart(zulkursart);
			if (kursart == null) {
				continue;
			}
			// Keine Fachwahl -> Konflikt, da Leistungsdaten vorhanden sind
			if (fw == null) {
				throw new ApiOperationException(Status.CONFLICT);
			}
			// Prüfe, ob Fachwahl mündlich passt
			if (("M".equals(fw)) && ((kursart == GostKursart.PJK) || (kursart == GostKursart.VTF)
					|| ((kursart == GostKursart.GK) && ((zulkursart == ZulaessigeKursart.GKM)
							|| ((zulkursart == ZulaessigeKursart.AB4) && (halbjahr == GostHalbjahr.Q22)))))) {
				return;
			}
			// Prüfe, ob Fachwahl schriftlich passt
			if (("S".equals(fw)) && ((kursart == GostKursart.GK) && ((zulkursart == ZulaessigeKursart.GKS) || (zulkursart == ZulaessigeKursart.AB3)
					|| ((zulkursart == ZulaessigeKursart.AB4) && (halbjahr != GostHalbjahr.Q22))))) {
				return;
			}
			// Prüfe, ob Fachwahl Leistungskurs passt
			if (("LK".equals(fw)) && (kursart == GostKursart.LK)) {
				return;
			}
			// Prüfe, ob Fachwahl Zusatzkurs passt
			if (("ZK".equals(fw)) && (kursart == GostKursart.ZK)) {
				return;
			}
			// Prüfe, ob ein Sportattest passt
			if (("AT".equals(fw)) && (istSP && (Note.data().getWertByKuerzel(leistung.NotenKrz) == Note.ATTEST))) {
				return;
			}
		}
		if (fw != null) {
			throw new ApiOperationException(Status.CONFLICT);
		}
	}

	/**
	 * Prüft, ob die Fachwahl in dem Halbjahr zu den Leistungsdaten passt. Ist dies nicht der Fall, so wird eine Exception generiert.
	 *
	 * @param leistungen    die Leistungen die geprüft werden
	 * @param halbjahr      das Halbjahr, auf welches sich der Patch bezieht
	 * @param istSP         gibt an, ob das Fach für die Leistungsdaten Sport ist
	 * @param fw            der Wert für die Fachwahl
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private static void patchFachwahlHalbjahrCheckLeistungenAbi2030(final List<DTOSchuelerLeistungsdaten> leistungen, final GostHalbjahr halbjahr,
			final boolean istSP,
			final String fw) throws ApiOperationException {
		for (final DTOSchuelerLeistungsdaten leistung : leistungen) {
			final ZulaessigeKursart zulkursart = ZulaessigeKursart.data().getWertByKuerzel(leistung.Kursart);
			final GostKursart kursart = GostKursart.fromKursart(zulkursart);
			if (kursart == null) {
				continue;
			}
			// Keine Fachwahl -> Konflikt, da Leistungsdaten vorhanden sind
			if (fw == null) {
				throw new ApiOperationException(Status.CONFLICT);
			}
			// Prüfe, ob Fachwahl mündlich passt
			if (("M".equals(fw)) && ((kursart == GostKursart.VTF) || ((kursart == GostKursart.GK) && ((zulkursart == ZulaessigeKursart.GKM)
					|| ((zulkursart == ZulaessigeKursart.AB4) && (halbjahr == GostHalbjahr.Q22)))))) {
				return;
			}
			// Prüfe, ob Fachwahl schriftlich passt
			if (("S".equals(fw)) && ((kursart == GostKursart.PJK)
					|| ((kursart == GostKursart.GK) && ((zulkursart == ZulaessigeKursart.GKS) || (zulkursart == ZulaessigeKursart.AB3)
							|| ((zulkursart == ZulaessigeKursart.AB4) && (halbjahr != GostHalbjahr.Q22)))))) {
				return;
			}
			// Prüfe, ob Fachwahl Leistungskurs passt
			if (("LK".equals(fw)) && (kursart == GostKursart.LK)) {
				return;
			}
			// Prüfe, ob Fachwahl Zusatzkurs passt
			if (("ZK".equals(fw)) && (kursart == GostKursart.ZK)) {
				return;
			}
			// Prüfe, ob ein Sportattest passt
			if (("AT".equals(fw)) && (istSP && (Note.data().getWertByKuerzel(leistung.NotenKrz) == Note.ATTEST))) {
				return;
			}
		}
		if (fw != null) {
			throw new ApiOperationException(Status.CONFLICT);
		}
	}

	/**
	 * Prüft, ob die Fachwahl in dem Halbjahr zu den Leistungsdaten in den Lernabschnitten passt.
	 * Ist dies nicht der Fall, so wird eine Exception generiert.
	 *
	 * @param schueler      der Schüler, für welchen die Fachwahl angepasst wird
	 * @param abiturjahr    das Jahr des Abiturjahrgangs des Schülers
	 * @param halbjahr      das Halbjahr, auf welches sich der Patch bezieht
	 * @param fach          das Fach, für welches die Fachwahl angepasst werden soll
	 * @param fw            der Wert für die Fachwahl
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private void patchFachwahlHalbjahrCheckLernabschnitt(final DTOSchueler schueler, final int abiturjahr, final GostHalbjahr halbjahr, final DTOFach fach,
			final String fw) throws ApiOperationException {
		// Prüfe, ob die eingebene Fachwahl den Leistungsdaten entspricht
		final List<DTOSchuelerLernabschnittsdaten> lernabschnitte =
				schuelerLernabschnittRepository.getGewerteteAbschnittInASDJahrgang(schueler.ID, halbjahr.jahrgang);
		for (final DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
			final Schuljahresabschnitt schuljahresabschnitt =
					benutzerRepository.getAktuellerBenutzer().schuleGetSchuljahresabschnittByIdOrDefault(lernabschnitt.Schuljahresabschnitts_ID);
			if (halbjahr.halbjahr != schuljahresabschnitt.abschnitt) {
				continue;
			}
			final List<DTOSchuelerLeistungsdaten> leistungen =
					schuelerLeistungsdatenRepository.findListByLernabschnittAndFach(List.of(lernabschnitt.ID), List.of(fach.ID));
			if (leistungen.isEmpty()) {
				final boolean valid = (fw == null)
						|| ("M".equals(fw)) || ("S".equals(fw))
						|| ((("LK".equals(fw)) || ("ZK".equals(fw))) && (!halbjahr.istEinfuehrungsphase()))
						|| (("AT".equals(fw)) && ("SP".equals(fach.StatistikKuerzel)));
				if (!valid) {
					throw new ApiOperationException(Status.CONFLICT);
				}
				return;
			}
			if (AbiturdatenManager.istAbitur2030(abiturjahr)) {
				patchFachwahlHalbjahrCheckLeistungenAbi2030(leistungen, halbjahr, "SP".equals(fach.StatistikKuerzel), fw);
			} else {
				patchFachwahlHalbjahrCheckLeistungen(leistungen, halbjahr, "SP".equals(fach.StatistikKuerzel), fw);
			}
			return;
		}
		if (fw != null) {
			throw new ApiOperationException(Status.CONFLICT, "Es konnte keine Fachwahl für die Leistungsdaten gefunden werden.");
		}
	}


	/**
	 * Führt den Fachwahl-Patch für das angegebene Halbjahr aus, sofern dieser gültig ist und in dem
	 * angegebenen Halbjahr erlaubt ist. Ein Patch ist nicht erlaubt, wenn dieser in das aktuelle
	 * Halbjahr oder ein Halbjahr davor fällt, da hier bereits eine Kursblockung stattgefunden hat
	 * und Anpassungen über die Kurswahlen bzw. die Leistungsdaten erfolgen sollten.
	 *
	 * @param schueler      der Schüler, für welchen die Fachwahl angepasst wird
	 * @param abiturjahr    das Jahr des Abiturjahrgangs des Schülers
	 * @param fwDB          der Wert für die Fachwahl aus der DB
	 * @param halbjahr      das Halbjahr, auf welches sich der Patch bezieht
	 * @param aktHalbjahr   das Halbjahr, in welchem sich der Schüler befindet
	 * @param fach          das Fach, für welches die Fachwahl angepasst werden soll
	 * @param fw            der Wert für die Fachwahl
	 *
	 * @return der zu übertragende Wert
	 *
	 * @throws ApiOperationException (CONFLICT) falls die Fachwahl ungültig ist
	 */
	private String patchFachwahlHalbjahr(final DTOSchueler schueler, final int abiturjahr, final String fwDB, final GostHalbjahr halbjahr,
			final GostHalbjahr aktHalbjahr, final DTOFach fach, final String fw) throws ApiOperationException {
		if ("".equals(fw)) {
			return null;
		}
		if (((fw == null) && (fwDB == null)) || ((fw != null) && (fw.equals(fwDB)))) {
			return fwDB;
		}
		final boolean valid = (fw == null)
				|| ("M".equals(fw)) || ("S".equals(fw))
				|| ("LK".equals(fw) && !halbjahr.istEinfuehrungsphase()
						&& !GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hatKuerzel(fach.StatistikKuerzel))
				|| ("ZK".equals(fw) && !halbjahr.istEinfuehrungsphase())
				|| ("AT".equals(fw) && "SP".equals(fach.StatistikKuerzel));
		if (!valid) {
			throw new ApiOperationException(Status.CONFLICT, "Die angegebene Fachwahl ist ungültig.");
		}
		// prüfe, ob eine Änderung bei diesem Schüler überhaupt erlaubt ist oder in das aktuelle Halbjahr des Schülers oder früher fällt...
		if ((aktHalbjahr != null) && (aktHalbjahr.compareTo(halbjahr) >= 0)) {
			patchFachwahlHalbjahrCheckLernabschnitt(schueler, abiturjahr, halbjahr, fach, fw);
			return fw;
		}
		return fw;
	}


	/**
	 * Passt die Fachwahl für die gymnasiale Oberstufe zu einem Fach von dem angegebenen Schüler an.
	 *
	 * @param idSchueler   die ID des Schülers
	 * @param idFach       die ID des Faches
	 * @param is           der {@link InputStream} mit dem JSON-Patch für die Fachwahl
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public void patch(final Long idSchueler, final Long idFach, final InputStream is) throws ApiOperationException {
		transactional(() -> {
			pruefeGymOb();

			final Map<String, Object> map = JSONMapper.toMap(is);
			if (map.isEmpty()) {
				return;
			}

			final DTOSchueler schueler = schuelerRepository.findById(idSchueler)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schüler mit der ID %d gefunden.".formatted(idSchueler)));

			final DTOFach fach = fachRepository.findById(idFach)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Fach mit der ID %d gefunden.".formatted(idFach)));
			if (!Boolean.TRUE.equals(fach.IstOberstufenFach)) {
				throw new ApiOperationException(Status.NOT_FOUND);
			}
			final Optional<DTOGostSchuelerFachbelegungen> optFachbelegung =
					gostSchuelerFachbelegungenRepository.findById(new DTOGostSchuelerFachbelegungenPK(idSchueler, idFach));


			final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
			final Schulform schulform = benutzer.schuleGetSchulform();
			final Schuljahresabschnitt schuljahresabschnitt = benutzer.schuleGetSchuljahresabschnittByIdOrDefault(schueler.Schuljahresabschnitts_ID);

			// Ermittle den aktuellen Schüler-Lernabschnitt
			final DTOSchuelerLernabschnittsdaten lernabschnitt = schuelerLernabschnittRepository.findAktuellBySchuelerID(idSchueler)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Es konnte kein aktueller Lernabschnitt für den schüler bestimmt werden."));
			final DTOJahrgang dtoJahrgang = jahrgangRepository.findById(lernabschnitt.Jahrgang_ID)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Der Jahrgang des aktuellen Lernabschnittes des Schülers ist ungültig."));
			if (dtoJahrgang.ASDJahrgang == null) {
				throw new ApiOperationException(Status.NOT_FOUND, "Der Jahrgang des aktuellen Lernabschnittes des Schülers ist nicht korrekt gesetzt.");
			}
			final GostHalbjahr aktHalbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(dtoJahrgang.ASDJahrgang, schuljahresabschnitt.abschnitt);
			final Schulgliederung schulgliederung = (lernabschnitt.Schulgliederung == null)
					? Schulgliederung.getDefault(schulform)
					: Schulgliederung.data().getWertByKuerzel(lernabschnitt.Schulgliederung);
			final Jahrgaenge jahrgang = Jahrgaenge.data().getWertBySchluessel(dtoJahrgang.ASDJahrgang);
			final Integer abiturjahr = DBUtilsGost.getAbiturjahr(schulform, schulgliederung, schuljahresabschnitt.schuljahr, jahrgang);
			if (abiturjahr == null) {
				throw new ApiOperationException(Status.NOT_FOUND, "Das Abiturjahr konnte für den Schüler nicht ermittelt werden.");
			}

			// Bestimme die Fachbelegungen in der DB. Liegen keine vor, so erstelle eine neue Fachbelegung in der DB, um den Patch zu speichern
			final DTOGostSchuelerFachbelegungen fachbelegung = optFachbelegung.orElse(new DTOGostSchuelerFachbelegungen(schueler.ID, fach.ID));
			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
					case "halbjahre" -> {
						final String[] wahlen = JSONMapper.convertToStringArray(value, true, 6);
						if ((wahlen == null) || ((wahlen.length != 0) && (wahlen.length != 6))) {
							throw new ApiOperationException(Status.CONFLICT);
						}
						if (wahlen.length == 0) {
							fachbelegung.EF1_Kursart = null;
							fachbelegung.EF2_Kursart = null;
							fachbelegung.Q11_Kursart = null;
							fachbelegung.Q12_Kursart = null;
							fachbelegung.Q21_Kursart = null;
							fachbelegung.Q22_Kursart = null;
						} else {
							fachbelegung.EF1_Kursart =
									patchFachwahlHalbjahr(schueler, abiturjahr, fachbelegung.EF1_Kursart, GostHalbjahr.EF1, aktHalbjahr, fach, wahlen[0]);
							fachbelegung.EF2_Kursart =
									patchFachwahlHalbjahr(schueler, abiturjahr, fachbelegung.EF2_Kursart, GostHalbjahr.EF2, aktHalbjahr, fach, wahlen[1]);
							fachbelegung.Q11_Kursart =
									patchFachwahlHalbjahr(schueler, abiturjahr, fachbelegung.Q11_Kursart, GostHalbjahr.Q11, aktHalbjahr, fach, wahlen[2]);
							fachbelegung.Q12_Kursart =
									patchFachwahlHalbjahr(schueler, abiturjahr, fachbelegung.Q12_Kursart, GostHalbjahr.Q12, aktHalbjahr, fach, wahlen[3]);
							fachbelegung.Q21_Kursart =
									patchFachwahlHalbjahr(schueler, abiturjahr, fachbelegung.Q21_Kursart, GostHalbjahr.Q21, aktHalbjahr, fach, wahlen[4]);
							fachbelegung.Q22_Kursart =
									patchFachwahlHalbjahr(schueler, abiturjahr, fachbelegung.Q22_Kursart, GostHalbjahr.Q22, aktHalbjahr, fach, wahlen[5]);
						}
					}
					case "abiturFach" -> {
						// experimenteller Code für das 5. Abiturfach
						final int maxAbifach = AbiturdatenManager.istAbitur2030(abiturjahr)
								? 5 : 4;
						fachbelegung.AbiturFach = JSONMapper.convertToIntegerInRange(value, true, 1, maxAbifach + 1);
					}
					case "idReferenzfach" -> {
						final Long idReferenzfach = JSONMapper.convertToLong(value, true, "idReferenzfach");
						if (idReferenzfach == null) {
							fachbelegung.Referenzfach_ID = null;
						} else {
							fachRepository.findById(idReferenzfach).orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Wahl des Referentfaches ist ungültig."));
							fachbelegung.Referenzfach_ID = idReferenzfach;
						}
					}
					default -> throw new ApiOperationException(Status.BAD_REQUEST);
				}
			}
			gostSchuelerFachbelegungenRepository.update(fachbelegung);
		});
	}


	/**
	 * Setzt die Fachwahlen der angegebenen Schüler mit den Vorgabe-Fachwahlen des Abiturjahrgangs zurück.
	 * Es werden die existierenden Fachwahlen entfernt und die Fachwahlen aus dem Abiturjahrgang übernommen.
	 *
	 * @param abijahr      der Abiturjahrgang
	 * @param idsSchueler   die IDs der Schüler
	 */
	private void resetAbijahrgangSchuelerInternal(final Integer abijahr, final Collection<Long> idsSchueler) {
		if (abijahr == null) {
			return;
		}

		// Entferne auch die GKLs, sofern sie nicht in bewerteten Abschnitten liegen
		if (AbiturdatenManager.istAbitur2030(abijahr)) {
			final Map<Long, Abiturdaten> mapAbidaten = gostAbiturdatenService.getMap(idsSchueler);
			final Map<Long, DTOGostSchueler> mapGostSchueler = gostSchuelerRepository.findMapByIds(idsSchueler);
			final Map<Long, DTOGostKlausurenVorgaben> mapKlausurvorgaben = getMapKlausurvorgabenByGKLWahlen(mapGostSchueler.values());
			for (final long idSchueler : idsSchueler) {
				final DTOGostSchueler gostSchueler = mapGostSchueler.get(idSchueler);
				final Abiturdaten abidaten = mapAbidaten.get(idSchueler);
				loescheGKLsBeiNichtBewertetenHalbjahren(gostSchueler, abidaten.bewertetesHalbjahr, mapKlausurvorgaben);
			}
			gostSchuelerRepository.flush();
		}

		gostSchuelerFachbelegungenRepository.deleteMultipleBySchuelerID(idsSchueler);
		gostSchuelerFachbelegungenRepository.flush();

		final Map<Long, DTOGostJahrgangFachbelegungen> mapJahrgangFachwahlen =
				gostJahrgangFachbelegungenRepository.getMap2DByAbiturjahrgangAndFachID(List.of(abijahr)).getSubMapOrNull(abijahr);
		if (mapJahrgangFachwahlen == null) {
			return;
		}

		for (final DTOGostJahrgangFachbelegungen dto : mapJahrgangFachwahlen.values()) {
			for (final long idSchueler : idsSchueler) {
				final DTOGostSchuelerFachbelegungen fw = new DTOGostSchuelerFachbelegungen(idSchueler, dto.Fach_ID);
				fw.EF1_Kursart = dto.EF1_Kursart;
				fw.EF2_Kursart = dto.EF2_Kursart;
				fw.Q11_Kursart = dto.Q11_Kursart;
				fw.Q12_Kursart = dto.Q11_Kursart;
				fw.Q21_Kursart = dto.Q21_Kursart;
				fw.Q22_Kursart = dto.Q22_Kursart;
				fw.AbiturFach = dto.AbiturFach;
				fw.Bemerkungen = dto.Bemerkungen;
				gostSchuelerFachbelegungenRepository.create(fw);
			}
		}
	}


	/**
	 * Setzt die Fachwahlen für den angegebenen Schüler zurück.
	 * Liegen bereits bewertete Halbjahre vor, so werden die zukünftigen Fachwahlen entfernt.
	 * Ansonsten wir die Vorlage für die Fachwahlen des Abiturjahrgangs übernommen.
	 *
	 * @param idSchueler   die ID des Schülers
	 */
	public void reset(final long idSchueler) {
		transactional(() -> {
			pruefeGymOb();
			final Abiturdaten abidaten = gostAbiturdatenService.get(idSchueler);
			final DTOGostJahrgangsdaten jahrgang = gostJahrgangsdatenRepository.findById(abidaten.abiturjahr)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Der Abiturjahrgang konnte nicht gefunden werden."));
			final DTOGostSchueler gostSchueler = gostSchuelerRepository.getById(idSchueler);
			final Map<Long, DTOGostKlausurenVorgaben> mapKlausurvorgaben = getMapKlausurvorgabenByGKLWahlen(List.of(gostSchueler));

			if (!abidaten.bewertetesHalbjahr[GostHalbjahr.EF1.id]) {
				resetAbijahrgangSchuelerInternal(jahrgang.Abi_Jahrgang, List.of(idSchueler));
				return;
			}

			// Entferne auch die Informationen zu den GKL-Wahlen, sofern diese nicht im Bereich eines bewerteten Halbjahres liegen
			loescheGKLsBeiNichtBewertetenHalbjahren(gostSchueler, abidaten.bewertetesHalbjahr, mapKlausurvorgaben);

			final Map<Long, DTOGostSchuelerFachbelegungen> mapFachwahlen =
					gostSchuelerFachbelegungenRepository.getMap2DBySchuelerIDAndFachID(List.of(idSchueler)).getSubMapOrNull(idSchueler);
			for (final DTOGostSchuelerFachbelegungen fw : mapFachwahlen.values()) {
				fw.AbiturFach = null;
				if (!abidaten.bewertetesHalbjahr[GostHalbjahr.EF1.id]) {
					fw.EF1_Kursart = null;
				}
				if (!abidaten.bewertetesHalbjahr[GostHalbjahr.EF2.id]) {
					fw.EF2_Kursart = null;
				}
				if (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q11.id]) {
					fw.Q11_Kursart = null;
				}
				if (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q12.id]) {
					fw.Q12_Kursart = null;
				}
				if (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q21.id]) {
					fw.Q21_Kursart = null;
				}
				if (!abidaten.bewertetesHalbjahr[GostHalbjahr.Q22.id]) {
					fw.Q22_Kursart = null;
				}
			}
			gostSchuelerFachbelegungenRepository.update(mapFachwahlen.values());

			gostSchuelerFachbelegungenRepository.flush();
		});
	}



	/**
	 * Setzt die Fachwahlen bei allen (!) Schülern des angegebenen Abiturjahrgangs zurück.
	 *
	 * @param abijahr   der Abiturjahrgang
	 */
	public void resetAbiturjahrgang(final Integer abijahr) {
		transactional(() -> {
			pruefeGymOb();
			if (abijahr == null) {
				return;
			}
			gostJahrgangsdatenRepository.findById(abijahr)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Der Abiturjahrgang konnte nicht gefunden werden."));
			final List<DTOSchueler> listSchueler = gostSchuelerService.getByAbiturjahrgang(abijahr);
			final List<Long> idsSchueler = listSchueler.stream().map(s -> s.ID).distinct().toList();
			resetAbijahrgangSchuelerInternal(abijahr, idsSchueler);
		});
	}


	private Map<Long, DTOGostKlausurenVorgaben> getMapKlausurvorgabenByGKLWahlen(final Collection<DTOGostSchueler> gostSchueler) {
		final List<Long> idsKlausurvorgaben = gostSchueler.stream().flatMap(s -> {
			final List<Long> result = new ArrayList<>();
			if (s.GKL_EF_AF1_Klausurvorgabe_ID != null) {
				result.add(s.GKL_EF_AF1_Klausurvorgabe_ID);
			}
			if (s.GKL_EF_AF2_Klausurvorgabe_ID != null) {
				result.add(s.GKL_EF_AF2_Klausurvorgabe_ID);
			}
			if (s.GKL_EF_AF3_Klausurvorgabe_ID != null) {
				result.add(s.GKL_EF_AF3_Klausurvorgabe_ID);
			}
			if (s.GKL_Q_AF1_Klausurvorgabe_ID != null) {
				result.add(s.GKL_Q_AF1_Klausurvorgabe_ID);
			}
			if (s.GKL_Q_AF2_Klausurvorgabe_ID != null) {
				result.add(s.GKL_Q_AF2_Klausurvorgabe_ID);
			}
			if (s.GKL_Q_AF3_Klausurvorgabe_ID != null) {
				result.add(s.GKL_Q_AF3_Klausurvorgabe_ID);
			}
			return result.stream();
		}).distinct().toList();
		return gostKlausurenVorgabeRepository.findMapByIds(idsKlausurvorgaben);
	}


	private static Long getGKLNachLoeschenBeiNichtBewertetenHalbjahren(final Long idKlausurvorgabe, final boolean[] bewertetesHalbjahr,
			final Map<Long, DTOGostKlausurenVorgaben> mapKlausurvorgaben) {
		if (idKlausurvorgabe == null) {
			return null;
		}
		final DTOGostKlausurenVorgaben vorgabe = mapKlausurvorgaben.get(idKlausurvorgabe);
		if (vorgabe == null) {
			return null;
		}
		return bewertetesHalbjahr[vorgabe.Halbjahr.id] ? idKlausurvorgabe : null;
	}


	private void loescheGKLsBeiNichtBewertetenHalbjahren(final DTOGostSchueler gostSchueler, final boolean[] bewertetesHalbjahr,
			final Map<Long, DTOGostKlausurenVorgaben> mapKlausurvorgaben) {
		gostSchueler.GKL_EF_AF1_Klausurvorgabe_ID = getGKLNachLoeschenBeiNichtBewertetenHalbjahren(gostSchueler.GKL_EF_AF1_Klausurvorgabe_ID, bewertetesHalbjahr, mapKlausurvorgaben);
		gostSchueler.GKL_EF_AF2_Klausurvorgabe_ID = getGKLNachLoeschenBeiNichtBewertetenHalbjahren(gostSchueler.GKL_EF_AF2_Klausurvorgabe_ID, bewertetesHalbjahr, mapKlausurvorgaben);
		gostSchueler.GKL_EF_AF3_Klausurvorgabe_ID = getGKLNachLoeschenBeiNichtBewertetenHalbjahren(gostSchueler.GKL_EF_AF3_Klausurvorgabe_ID, bewertetesHalbjahr, mapKlausurvorgaben);
		gostSchueler.GKL_Q_AF1_Klausurvorgabe_ID = getGKLNachLoeschenBeiNichtBewertetenHalbjahren(gostSchueler.GKL_Q_AF1_Klausurvorgabe_ID, bewertetesHalbjahr, mapKlausurvorgaben);
		gostSchueler.GKL_Q_AF2_Klausurvorgabe_ID = getGKLNachLoeschenBeiNichtBewertetenHalbjahren(gostSchueler.GKL_Q_AF2_Klausurvorgabe_ID, bewertetesHalbjahr, mapKlausurvorgaben);
		gostSchueler.GKL_Q_AF3_Klausurvorgabe_ID = getGKLNachLoeschenBeiNichtBewertetenHalbjahren(gostSchueler.GKL_Q_AF3_Klausurvorgabe_ID, bewertetesHalbjahr, mapKlausurvorgaben);
		gostSchuelerRepository.update(gostSchueler);
	}


	/**
	 * Löscht die Fachwahlen für die angegebenen Schüler. Liegen bereits bewertete Halbjahre vor, so
	 * werden nur die Informationen nach dem letzten bewerteten Halbjahr gelöscht. Dabei werden
	 * die Informationen zum Abitur, wie dem Abiturfach, der Q22 zugerechnet.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 */
	public void delete(final List<Long> idsSchueler) {
		transactional(() -> {
			pruefeGymOb();

			final Map<Long, Abiturdaten> mapAbidaten = gostAbiturdatenService.getMap(idsSchueler);
			final Map<Long, List<DTOGostSchuelerFachbelegungen>> mapFachwahlen = gostSchuelerFachbelegungenRepository.getMapBySchuelerID(idsSchueler);

			final Map<Long, DTOGostSchueler> mapGostSchueler = gostSchuelerRepository.findMapByIds(idsSchueler);
			final Map<Long, DTOGostKlausurenVorgaben> mapKlausurvorgaben = getMapKlausurvorgabenByGKLWahlen(mapGostSchueler.values());

			for (final long idSchueler : idsSchueler) {
				final DTOGostSchueler gostSchueler = mapGostSchueler.get(idSchueler);
				if (gostSchueler == null) {
					throw new ApiOperationException(Status.NOT_FOUND,
							"Die Informationen zur Gymnasialen Oberstufe für den Schüler mit der ID %d konnten nicht bestimmt werden.".formatted(idSchueler));
				}

				final Abiturdaten abidaten = mapAbidaten.get(idSchueler);
				if (abidaten == null) {
					throw new ApiOperationException(Status.NOT_FOUND,
							"Die Abiturdaten für den Schüler mit der ID %d konnten nicht bestimmt werden.".formatted(idSchueler));
				}
				final List<DTOGostSchuelerFachbelegungen> fachwahlen = mapFachwahlen.get(idSchueler);
				if (fachwahlen == null) {
					continue;
				}

				// Prüfe, bis zu welchem Halbjahr es Bewertungen gibt
				GostHalbjahr halbjahr = null;
				for (final GostHalbjahr tmpHalbjahr : GostHalbjahr.values()) {
					if (abidaten.bewertetesHalbjahr[tmpHalbjahr.id]) {
						halbjahr = tmpHalbjahr;
					}
				}

				// Entferne auch die Informationen zu den GKL-Wahlen, sofern diese nicht im Bereich eines bewerteten Halbjahres liegen
				loescheGKLsBeiNichtBewertetenHalbjahren(gostSchueler, abidaten.bewertetesHalbjahr, mapKlausurvorgaben);

				// Wenn es keine Bewertung gibt, dann können einfach alle Fachwahlen des Schülers komplett gelöscht werden
				if (halbjahr == null) {
					gostSchuelerFachbelegungenRepository.delete(fachwahlen);
					continue;
				}
				// Wenn auch die Q22 bewertet ist, dann muss nichts gelöscht werden
				if (halbjahr == GostHalbjahr.Q22) {
					continue;
				}
				// Die Fachwahlen müssen einzeln bearbeitet werden ...
				for (final DTOGostSchuelerFachbelegungen fachwahl : fachwahlen) {
					if (halbjahr.compareTo(GostHalbjahr.EF2) < 0) {
						fachwahl.EF2_Kursart = null;
						fachwahl.EF2_Punkte = null;
					}
					if (halbjahr.compareTo(GostHalbjahr.Q11) < 0) {
						fachwahl.Q11_Kursart = null;
						fachwahl.Q11_Punkte = null;
						fachwahl.Markiert_Q1 = false;
					}
					if (halbjahr.compareTo(GostHalbjahr.Q12) < 0) {
						fachwahl.Q12_Kursart = null;
						fachwahl.Q12_Punkte = null;
						fachwahl.Markiert_Q2 = false;
					}
					if (halbjahr.compareTo(GostHalbjahr.Q21) < 0) {
						fachwahl.Q21_Kursart = null;
						fachwahl.Q21_Punkte = null;
						fachwahl.Markiert_Q3 = false;
					}
					if (halbjahr.compareTo(GostHalbjahr.Q22) < 0) {
						fachwahl.Q22_Kursart = null;
						fachwahl.Q22_Punkte = null;
						fachwahl.Markiert_Q4 = false;
						fachwahl.AbiturFach = null;
						fachwahl.Bemerkungen = null;
						fachwahl.ergebnisAbiturpruefung = null;
						fachwahl.ergebnisMuendlichePruefung = null;
						fachwahl.hatMuendlichePflichtpruefung = null;
					}
					gostSchuelerFachbelegungenRepository.update(fachwahl);
				}
			}
			gostSchuelerFachbelegungenRepository.flush();
		});
	}

}
