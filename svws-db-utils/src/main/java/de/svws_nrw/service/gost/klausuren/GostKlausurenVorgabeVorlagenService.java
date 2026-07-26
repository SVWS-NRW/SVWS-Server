package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.gost.klausuren.GostKlausurplanManager;
import de.svws_nrw.db.dto.current.gost.klausuren.DTOGostKlausurenVorgaben;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenVorgabeRepository;
import de.svws_nrw.service.gost.GostFaecherService;

import static de.svws_nrw.data.TransactionSupport.transactional;

/**
 * Ein Service für Vorlagenoperationen auf GOSt-Klausurvorgaben.
 */
public final class GostKlausurenVorgabeVorlagenService {

	private final GostKlausurenVorgabeRepository repository;
	private final GostFaecherService gostFaecherService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für GOSt-Klausurvorgaben
	 * @param gostFaecherService der Service für GOSt-Fächer
	 */
	public GostKlausurenVorgabeVorlagenService(final GostKlausurenVorgabeRepository repository, final GostFaecherService gostFaecherService) {
		this.repository = repository;
		this.gostFaecherService = gostFaecherService;
	}

	/**
	 * Kopiert die Klausurvorgaben aus der Vorlage in einen Abiturjahrgang.
	 *
	 * @param abiturjahr das Abiturjahr
	 * @param halbjahr das Halbjahr der gymnasialen Oberstufe
	 * @param quartal das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return die neu angelegten Klausurvorgaben
	 */
	public List<GostKlausurvorgabe> copyVorlagenToJahrgang(final int abiturjahr, final int halbjahr, final int quartal) {
		return copyVorlagenToJahrgang(abiturjahr, GostKlausurenValidationUtils.checkHalbjahr(halbjahr),
				GostKlausurenValidationUtils.checkQuartal(quartal));
	}

	/**
	 * Kopiert die Klausurvorgaben aus der Vorlage in einen Abiturjahrgang.
	 *
	 * @param abiturjahr das Abiturjahr
	 * @param halbjahr das Halbjahr der gymnasialen Oberstufe oder null für alle Halbjahre
	 * @param quartal das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return die neu angelegten Klausurvorgaben
	 */
	public List<GostKlausurvorgabe> copyVorlagenToJahrgang(final int abiturjahr, final GostHalbjahr halbjahr, final int quartal) {
		return transactional(() -> {
			GostKlausurenValidationUtils.checkQuartal(quartal);
			final List<DTOGostKlausurenVorgaben> vorgabenVorlage = repository.getListByAbiturjahr(-1);
			final List<DTOGostKlausurenVorgaben> vorgabenJg = repository.getListByAbiturjahr(abiturjahr);
			final List<DTOGostKlausurenVorgaben> vorgabenNeu = copyMissingVorgaben(abiturjahr, halbjahr, quartal, vorgabenVorlage, vorgabenJg);
			repository.create(vorgabenNeu);
			repository.flush();
			return GostKlausurenVorgabeService.toApiList(vorgabenNeu);
		});
	}

	/**
	 * Legt Klausurvorgaben laut APO-GOSt für die Vorlage an.
	 *
	 * @param halbjahr das Halbjahr der gymnasialen Oberstufe
	 * @param quartal das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return die neu angelegten Klausurvorgaben
	 */
	public List<GostKlausurvorgabe> createMissingVorlagen(final int halbjahr, final int quartal) {
		return createMissingVorlagen(GostKlausurenValidationUtils.checkHalbjahr(halbjahr), GostKlausurenValidationUtils.checkQuartal(quartal));
	}

	/**
	 * Legt Klausurvorgaben laut APO-GOSt für die Vorlage an.
	 *
	 * @param halbjahr das Halbjahr der gymnasialen Oberstufe
	 * @param quartal das Quartal, 0 für das gesamte Halbjahr
	 *
	 * @return die neu angelegten Klausurvorgaben
	 */
	public List<GostKlausurvorgabe> createMissingVorlagen(final GostHalbjahr halbjahr, final int quartal) {
		return transactional(() -> {
			final List<DTOGostKlausurenVorgaben> vorgabenVorlage = repository.getListByAbiturjahr(-1);
			final GostFaecherManager faecherManager = gostFaecherService.getGostFaecherManager(-1, false);
			final GostKlausurplanManager manager = new GostKlausurplanManager(
					GostKlausurenVorgabeService.toApiList(vorgabenVorlage.stream().filter(v -> v.Halbjahr == halbjahr).toList()));
			manager.setFaecherManager(-1, faecherManager);
			final List<DTOGostKlausurenVorgaben> neueVorgaben = createMissingVorlagen(halbjahr, quartal, faecherManager, manager);
			repository.create(neueVorgaben);
			repository.flush();
			return GostKlausurenVorgabeService.toApiList(neueVorgaben);
		});
	}

	/**
	 * Ermittelt die aus der Vorlage in den Jahrgang zu kopierenden Vorgaben.
	 *
	 * @param abiturjahr das Ziel-Abiturjahr
	 * @param halbjahr das Halbjahr oder null für alle Halbjahre
	 * @param quartal das Quartal oder 0 für alle Quartale
	 * @param vorgabenVorlage die Vorgaben des Vorlagenjahrgangs
	 * @param vorgabenJg die bereits vorhandenen Vorgaben des Zieljahrgangs
	 *
	 * @return die neu zu persistierenden Vorgaben
	 */
	private List<DTOGostKlausurenVorgaben> copyMissingVorgaben(final int abiturjahr, final GostHalbjahr halbjahr, final int quartal,
			final List<DTOGostKlausurenVorgaben> vorgabenVorlage, final List<DTOGostKlausurenVorgaben> vorgabenJg) {
		final Set<VorgabeKey> existingKeys = toExistingVorgabeKeys(vorgabenJg);
		final List<DTOGostKlausurenVorgaben> vorgabenNeu = new ArrayList<>();
		for (final DTOGostKlausurenVorgaben vorgabe : vorgabenVorlage) {
			addCopyIfMissing(vorgabenNeu, existingKeys, abiturjahr, halbjahr, quartal, vorgabe);
		}
		return vorgabenNeu;
	}

	/**
	 * Erstellt die Vergleichsschlüssel für vorhandene Vorgaben.
	 *
	 * @param vorgaben die Vorgaben
	 *
	 * @return die Vergleichsschlüssel
	 */
	private static Set<VorgabeKey> toExistingVorgabeKeys(final List<DTOGostKlausurenVorgaben> vorgaben) {
		final Set<VorgabeKey> result = new HashSet<>();
		for (final DTOGostKlausurenVorgaben vorgabe : vorgaben) {
			result.add(VorgabeKey.of(vorgabe));
		}
		return result;
	}

	/**
	 * Fügt eine Kopie der Vorlage hinzu, falls sie zum Filter passt und im Zieljahrgang noch fehlt.
	 *
	 * @param vorgabenNeu die Liste der neuen Vorgaben
	 * @param existingKeys die Schlüssel der bereits vorhandenen Vorgaben
	 * @param abiturjahr das Ziel-Abiturjahr
	 * @param halbjahr das Halbjahr oder null für alle Halbjahre
	 * @param quartal das Quartal oder 0 für alle Quartale
	 * @param vorgabe die Vorlage
	 */
	private static void addCopyIfMissing(final List<DTOGostKlausurenVorgaben> vorgabenNeu, final Set<VorgabeKey> existingKeys, final int abiturjahr,
			final GostHalbjahr halbjahr, final int quartal, final DTOGostKlausurenVorgaben vorgabe) {
		if (!isMatchingFilter(vorgabe, halbjahr, quartal) || existingKeys.contains(VorgabeKey.of(vorgabe))) {
			return;
		}
		vorgabenNeu.add(copyVorgabe(abiturjahr, vorgabe));
	}

	/**
	 * Prüft, ob eine Vorgabe zum Halbjahres- und Quartalsfilter passt.
	 *
	 * @param vorgabe die Vorgabe
	 * @param halbjahr das Halbjahr oder null für alle Halbjahre
	 * @param quartal das Quartal oder 0 für alle Quartale
	 *
	 * @return true, falls die Vorgabe zum Filter passt
	 */
	private static boolean isMatchingFilter(final DTOGostKlausurenVorgaben vorgabe, final GostHalbjahr halbjahr, final int quartal) {
		return ((halbjahr == null) || (vorgabe.Halbjahr == halbjahr)) && ((quartal <= 0) || (quartal == vorgabe.Quartal));
	}

	/**
	 * Erstellt eine Kopie der Vorlage für den angegebenen Abiturjahrgang.
	 *
	 * @param abiturjahr das Ziel-Abiturjahr
	 * @param vorgabe die zu kopierende Vorlage
	 *
	 * @return die neue Vorgabe ohne vergebene ID
	 */
	private static DTOGostKlausurenVorgaben copyVorgabe(final int abiturjahr, final DTOGostKlausurenVorgaben vorgabe) {
		final DTOGostKlausurenVorgaben result =
				new DTOGostKlausurenVorgaben(0, abiturjahr, vorgabe.Halbjahr, vorgabe.Quartal, vorgabe.Fach_ID, vorgabe.Kursart, vorgabe.Dauer,
						vorgabe.Auswahlzeit, vorgabe.IstGklMoeglich, vorgabe.IstMdlPruefung, vorgabe.IstAudioNotwendig, vorgabe.IstVideoNotwendig);
		result.Bemerkungen = vorgabe.Bemerkungen;
		return result;
	}

	/**
	 * Ermittelt die fehlenden Vorlagen für alle schriftlich möglichen Fächer.
	 *
	 * @param halbjahr das Halbjahr
	 * @param quartal das Quartal oder 0 für alle Quartale
	 * @param faecherManager der Fächer-Manager des Vorlagenjahrgangs
	 * @param manager die vorhandenen Vorlagen des Halbjahres
	 *
	 * @return die neu zu persistierenden Vorlagen
	 */
	private List<DTOGostKlausurenVorgaben> createMissingVorlagen(final GostHalbjahr halbjahr, final int quartal,
			final GostFaecherManager faecherManager, final GostKlausurplanManager manager) {
		final List<DTOGostKlausurenVorgaben> result = new ArrayList<>();
		for (final GostFach fach : faecherManager.getFaecherSchriftlichMoeglich()) {
			result.addAll(createMissingVorlagenForFach(halbjahr, quartal, manager, fach));
		}
		return result;
	}

	/**
	 * Ermittelt die fehlenden Vorlagen für ein Fach.
	 *
	 * @param halbjahr das Halbjahr
	 * @param quartal das Quartal oder 0 für alle Quartale
	 * @param manager die vorhandenen Vorlagen des Halbjahres
	 * @param fach das Fach
	 *
	 * @return die neu zu persistierenden Vorlagen
	 */
	private static List<DTOGostKlausurenVorgaben> createMissingVorlagenForFach(final GostHalbjahr halbjahr, final int quartal,
			final GostKlausurplanManager manager, final GostFach fach) {
		final List<DTOGostKlausurenVorgaben> result = new ArrayList<>();
		for (final GostKursart kursart : getKursarten(halbjahr)) {
			result.addAll(createMissingVorlagenForKursart(halbjahr, quartal, manager, fach, kursart));
		}
		return result;
	}

	/**
	 * Ermittelt die fehlenden Vorlagen für ein Fach und eine Kursart.
	 *
	 * @param halbjahr das Halbjahr
	 * @param quartal das Quartal oder 0 für alle Quartale
	 * @param manager die vorhandenen Vorlagen des Halbjahres
	 * @param fach das Fach
	 * @param kursart die Kursart
	 *
	 * @return die neu zu persistierenden Vorlagen
	 */
	private static List<DTOGostKlausurenVorgaben> createMissingVorlagenForKursart(final GostHalbjahr halbjahr, final int quartal,
			final GostKlausurplanManager manager, final GostFach fach, final GostKursart kursart) {
		final List<DTOGostKlausurenVorgaben> result = new ArrayList<>();
		if (!isKursartAllowed(halbjahr, fach, kursart)) {
			return result;
		}
		for (final int q : getQuartale(quartal)) {
			addVorlageIfMissing(result, manager, halbjahr, q, fach, kursart);
		}
		return result;
	}

	/**
	 * Ermittelt die zu berücksichtigenden Quartale.
	 *
	 * @param quartal das Quartal oder 0 für beide Quartale
	 *
	 * @return die zu berücksichtigenden Quartale
	 */
	private static List<Integer> getQuartale(final int quartal) {
		return (quartal == 0) ? List.of(1, 2) : List.of(quartal);
	}

	/**
	 * Ermittelt die Kursarten, für die im Halbjahr Vorgaben erzeugt werden.
	 *
	 * @param halbjahr das Halbjahr
	 *
	 * @return die Kursarten
	 */
	private static GostKursart[] getKursarten(final GostHalbjahr halbjahr) {
		return halbjahr.istEinfuehrungsphase() ? new GostKursart[] { GostKursart.GK } : new GostKursart[] { GostKursart.GK, GostKursart.LK };
	}

	/**
	 * Prüft, ob für Fach, Kursart und Halbjahr eine Vorlage erzeugt werden darf.
	 *
	 * @param halbjahr das Halbjahr
	 * @param fach das Fach
	 * @param kursart die Kursart
	 *
	 * @return true, falls die Kursart erlaubt ist
	 */
	private static boolean isKursartAllowed(final GostHalbjahr halbjahr, final GostFach fach, final GostKursart kursart) {
		return !(((kursart == GostKursart.LK) && !fach.istMoeglichAbiLK)
				|| ((halbjahr == GostHalbjahr.Q22) && !(fach.istMoeglichAbiGK || fach.istMoeglichAbiLK)));
	}

	/**
	 * Erstellt eine Vorlage ohne vergebene ID.
	 *
	 * @param halbjahr das Halbjahr
	 * @param quartal das Quartal
	 * @param fach das Fach
	 * @param kursart die Kursart
	 *
	 * @return die neue Vorlage
	 */
	private static DTOGostKlausurenVorgaben createVorlage(final GostHalbjahr halbjahr, final int quartal, final GostFach fach,
			final GostKursart kursart) {
		return new DTOGostKlausurenVorgaben(0, -1, halbjahr, quartal, fach.id, kursart,
				GostKlausurplanManager.berechneGostKlausurdauerByHalbjahrAndKursartAndFach(halbjahr, kursart, fach, -1), 0, false, false, false, false);
	}

	/**
	 * Fügt eine Vorlage hinzu, falls sie noch nicht existiert.
	 *
	 * @param result die Liste der neuen Vorgaben
	 * @param manager die vorhandenen Vorlagen des Halbjahres
	 * @param halbjahr das Halbjahr
	 * @param quartal das Quartal
	 * @param fach das Fach
	 * @param kursart die Kursart
	 */
	private static void addVorlageIfMissing(final List<DTOGostKlausurenVorgaben> result,
			final GostKlausurplanManager manager, final GostHalbjahr halbjahr, final int quartal,
			final GostFach fach, final GostKursart kursart) {
		if (!vorgabeExists(manager, halbjahr, quartal, fach.id, kursart)) {
			result.add(createVorlage(halbjahr, quartal, fach, kursart));
		}
	}

	/**
	 * Prüft, ob eine Vorgabe in den Vorlagen existiert.
	 *
	 * @param manager die vorhandenen Vorlagen des Halbjahres
	 * @param halbjahr das Halbjahr
	 * @param quartal das Quartal
	 * @param idFach die Fach-ID
	 * @param kursart die Kursart
	 *
	 * @return true, falls eine passende Vorgabe existiert
	 */
	private static boolean vorgabeExists(final GostKlausurplanManager manager, final GostHalbjahr halbjahr, final int quartal,
			final long idFach, final GostKursart kursart) {
		return manager.vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(-1, halbjahr, quartal, kursart, idFach) != null;
	}

	/**
	 * Vergleichsschlüssel für fachlich gleiche Klausurvorgaben.
	 *
	 * @param halbjahr das Halbjahr
	 * @param quartal das Quartal
	 * @param fachId die Fach-ID
	 * @param kursart die Kursart
	 */
	private record VorgabeKey(int halbjahr, int quartal, long fachId, GostKursart kursart) {

		private static VorgabeKey of(final DTOGostKlausurenVorgaben vorgabe) {
			return new VorgabeKey(vorgabe.Halbjahr.id, vorgabe.Quartal, vorgabe.Fach_ID, vorgabe.Kursart);
		}

	}

}
