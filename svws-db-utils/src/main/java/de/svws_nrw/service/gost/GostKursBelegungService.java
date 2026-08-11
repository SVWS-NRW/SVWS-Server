package de.svws_nrw.service.gost;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;

/**
 * Service für GOSt-Belegungen zu Kursen.
 */
public final class GostKursBelegungService {

	private static final Set<ZulaessigeKursart> SCHRIFTLICHE_GK_KURSARTEN_BIS_Q21 = Set.of(
			ZulaessigeKursart.AB3, ZulaessigeKursart.AB4, ZulaessigeKursart.GKS);
	private static final Set<ZulaessigeKursart> SCHRIFTLICHE_GK_KURSARTEN_Q22 = Set.of(ZulaessigeKursart.AB3);
	private static final Set<GostHalbjahr> GOST_HALBJAHRE_BIS_Q21 = EnumSet.of(
			GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);

	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;
	private final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;
	private final SchuelerRepository schuelerRepository;
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	/**
	 * Erstellt einen neuen Service.
	 * TODO: Auf Basis-Services umstellen, sobald es DTO-nahe Services für die hier verwendeten Repositories
	 * SchuelerLernabschnittRepository, SchuelerLeistungsdatenRepository, SchuelerRepository und
	 * SchuljahresabschnitteRepository gibt. Aktuell existieren dafür keine passenden Services mit den benötigten
	 * performanten Bulk-Abfragen.
	 *
	 * @param schuelerLernabschnittRepository das Repository für Schüler-Lernabschnittsdaten
	 * @param schuelerLeistungsdatenRepository das Repository für Schüler-Leistungsdaten
	 * @param schuelerRepository das Repository für Schülerdaten
	 * @param schuljahresabschnitteRepository das Repository für Schuljahresabschnitte
	 */
	public GostKursBelegungService(final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final SchuelerRepository schuelerRepository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository) {
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
		this.schuelerLeistungsdatenRepository = schuelerLeistungsdatenRepository;
		this.schuelerRepository = schuelerRepository;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
	}

	/**
	 * Ermittelt aktive schriftliche GOSt-Schüler-Lernabschnittsdaten zu einem Kurs.
	 *
	 * @param schuljahr das Schuljahr
	 * @param idKurs die ID des Kurses
	 *
	 * @return die aktiven schriftlichen GOSt-Schüler-Lernabschnittsdaten
	 */
	public List<DTOSchuelerLernabschnittsdaten> getAktiveSchriftlicheGostSchuelerLernabschnittsdatenByKurs(final int schuljahr, final long idKurs) {
		final List<DTOSchuelerLeistungsdaten> leistungsdaten = schuelerLeistungsdatenRepository.findListByKurs(idKurs);
		if (leistungsdaten.isEmpty()) {
			return List.of();
		}

		final Map<Long, DTOSchuelerLernabschnittsdaten> lernabschnitte = schuelerLernabschnittRepository
				.findMapByIds(leistungsdaten.stream().map(l -> l.Abschnitt_ID).distinct().toList());
		final Map<Long, DTOSchueler> schueler = schuelerRepository
				.findMapByIds(lernabschnitte.values().stream().map(l -> l.Schueler_ID).distinct().toList());
		final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitte = schuljahresabschnitteRepository
				.findMapByIds(lernabschnitte.values().stream().map(l -> l.Schuljahresabschnitts_ID).distinct().toList());

		final Set<Integer> validStatus = Set.of(
				Integer.parseInt(SchuelerStatus.AKTIV.daten(schuljahr).kuerzel),
				Integer.parseInt(SchuelerStatus.EXTERN.daten(schuljahr).kuerzel));
		final Map<Long, DTOSchuelerLernabschnittsdaten> result = new LinkedHashMap<>();
		for (final DTOSchuelerLeistungsdaten leistung : leistungsdaten) {
			final DTOSchuelerLernabschnittsdaten lernabschnitt = lernabschnitte.get(leistung.Abschnitt_ID);
			if (lernabschnitt == null) {
				continue;
			}
			final DTOSchueler dtoSchueler = schueler.get(lernabschnitt.Schueler_ID);
			final DTOSchuljahresabschnitte schuljahresabschnitt = schuljahresabschnitte.get(lernabschnitt.Schuljahresabschnitts_ID);
			if (istAktiveSchriftlicheGostBelegung(leistung, lernabschnitt, dtoSchueler, schuljahresabschnitt, validStatus)) {
				result.putIfAbsent(lernabschnitt.ID, lernabschnitt);
			}
		}
		return List.copyOf(result.values());
	}

	private static boolean istAktiveSchriftlicheGostBelegung(final DTOSchuelerLeistungsdaten leistung,
			final DTOSchuelerLernabschnittsdaten lernabschnitt, final DTOSchueler schueler,
			final DTOSchuljahresabschnitte schuljahresabschnitt, final Set<Integer> validStatus) {
		return (schueler != null)
				&& (schuljahresabschnitt != null)
				&& !schueler.Geloescht
				&& validStatus.contains(schueler.idStatus)
				&& istKursartSchriftlich(leistung.Kursart, lernabschnitt.ASDJahrgang, schuljahresabschnitt.Abschnitt);
	}

	private static boolean istKursartSchriftlich(final String kursart, final String jahrgang, final int abschnitt) {
		final ZulaessigeKursart zulKursart = ZulaessigeKursart.data().getWertByKuerzel(kursart);
		if (zulKursart == null) {
			return false;
		}
		final GostKursart gostKursart = GostKursart.fromKursart(zulKursart);
		if (gostKursart == GostKursart.LK) {
			return true;
		}
		if (gostKursart != GostKursart.GK) {
			return false;
		}
		final GostHalbjahr halbjahr = GostHalbjahr.fromJahrgangUndHalbjahr(jahrgang, abschnitt);
		final boolean erstesBisQ21 = GOST_HALBJAHRE_BIS_Q21.contains(halbjahr) && SCHRIFTLICHE_GK_KURSARTEN_BIS_Q21.contains(zulKursart);
		final boolean q22 = (halbjahr == GostHalbjahr.Q22) && SCHRIFTLICHE_GK_KURSARTEN_Q22.contains(zulKursart);
		return erstesBisQ21 || q22;
	}

}
