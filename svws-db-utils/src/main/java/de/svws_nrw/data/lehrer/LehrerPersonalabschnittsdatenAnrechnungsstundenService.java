package de.svws_nrw.data.lehrer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.lehrer.LehrerAnrechnungsgrundKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerMehrleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerMinderleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.lehrer.LehrerAnrechnungRepository;
import de.svws_nrw.repo.lehrer.LehrerMehrleistungRepository;
import de.svws_nrw.repo.lehrer.LehrerMinderleistungRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Anrechnungsstunden bei Personalabschnittsdaten
 */
public final class LehrerPersonalabschnittsdatenAnrechnungsstundenService {

	/** Das Repository für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnitteRepository repoSchuljahresabschnitte;

	/** Das Repository für den Zugriff auf die Mehrleistungen */
	private final LehrerMehrleistungRepository lehrerMehrleistungenRepository;

	/** Das Repository für den Zugriff auf die Minderleistungen */
	private final LehrerMinderleistungRepository lehrerMinderleistungenRepository;

	/** Das Repository für den Zugriff auf die Anrechnungsstunden */
	private final LehrerAnrechnungRepository lehrerAnrechnungRepository;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuljahresabschnitteRepository   das Repository für die Schuljahresabschnitte
	 * @param lehrerMehrleistungenRepository          das Repository für die Mehrleistungen
	 * @param lehrerMinderleistungenRepository        das Repository für die Minderleistungen
	 * @param lehrerAnrechnungRepository            das Repository für die Anrechnungsstunden
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstundenService(
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final LehrerMehrleistungRepository lehrerMehrleistungenRepository,
			final LehrerMinderleistungRepository lehrerMinderleistungenRepository,
			final LehrerAnrechnungRepository lehrerAnrechnungRepository) {
		this.repoSchuljahresabschnitte = schuljahresabschnitteRepository;
		this.lehrerMehrleistungenRepository = lehrerMehrleistungenRepository;
		this.lehrerMinderleistungenRepository = lehrerMinderleistungenRepository;
		this.lehrerAnrechnungRepository = lehrerAnrechnungRepository;
	}


	private LehrerPersonalabschnittsdatenAnrechnungsstunden mapMehrleistung(final DTOLehrerAbschnittsdaten dtoAbschnitt, final DTOLehrerMehrleistung dto) {
		final DTOSchuljahresabschnitte abschnitt = repoSchuljahresabschnitte.getById(dtoAbschnitt.Schuljahresabschnitts_ID);

		final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idAbschnittsdaten = dto.Abschnitt_ID;

		// Ermittle die Art des Grundes. Ist dieser nicht gültig für das Halbjahr, so wird keine Fehlermeldung ausgegeben, sondern der Grund auf null gesetzt.
		final LehrerMehrleistungsarten art = LehrerMehrleistungsarten.data().getWertByKuerzel(dto.MehrleistungsgrundKrz);
		final LehrerMehrleistungsartKatalogEintrag artEintrag = (art == null) ? null : art.daten(abschnitt.Jahr);
		daten.idGrund = (artEintrag == null) ? null : artEintrag.id;

		daten.anzahl = (dto.MehrleistungStd == null) ? 0.0 : dto.MehrleistungStd;
		return daten;
	}

	private LehrerPersonalabschnittsdatenAnrechnungsstunden mapMinderleistung(final DTOLehrerAbschnittsdaten dtoAbschnitt,
			final DTOLehrerEntlastungsstunde dto) {
		final DTOSchuljahresabschnitte abschnitt = repoSchuljahresabschnitte.getById(dtoAbschnitt.Schuljahresabschnitts_ID);

		final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idAbschnittsdaten = dto.Abschnitt_ID;

		// Ermittle die Art des Grundes. Ist dieser nicht gültig für das Halbjahr, so wird keine Fehlermeldung ausgegeben, sondern der Grund auf null gesetzt.
		final LehrerMinderleistungsarten art = LehrerMinderleistungsarten.data().getWertByKuerzel(dto.EntlastungsgrundKrz);
		final LehrerMinderleistungsartKatalogEintrag artEintrag = (art == null) ? null : art.daten(abschnitt.Jahr);
		daten.idGrund = (artEintrag == null) ? null : artEintrag.id;

		daten.anzahl = (dto.EntlastungStd == null) ? 0.0 : dto.EntlastungStd;
		return daten;
	}

	private LehrerPersonalabschnittsdatenAnrechnungsstunden mapAnrechnung(final DTOLehrerAbschnittsdaten dtoAbschnitt, final DTOLehrerAnrechnungsstunde dto) {
		final DTOSchuljahresabschnitte abschnitt = repoSchuljahresabschnitte.getById(dtoAbschnitt.Schuljahresabschnitts_ID);

		final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idAbschnittsdaten = dto.Abschnitt_ID;

		// Ermittle die Art des Grundes. Ist dieser nicht gültig für das Halbjahr, so wird keine Fehlermeldung ausgegeben, sondern der Grund auf null gesetzt.
		final LehrerAnrechnungsgrund art = LehrerAnrechnungsgrund.data().getWertByKuerzel(dto.AnrechnungsgrundKrz);
		final LehrerAnrechnungsgrundKatalogEintrag artEintrag = (art == null) ? null : art.daten(abschnitt.Jahr);
		daten.idGrund = (artEintrag == null) ? null : artEintrag.id;

		daten.anzahl = (dto.AnrechnungStd == null) ? 0.0 : dto.AnrechnungStd;
		return daten;
	}


	/**
	 * Gibt eine Map mit der Zuordnung der Mehrleistungen zu den IDs der übergebenen Lehrer-Abschnittsdaten zurück.
	 *
	 * @param abschnitte   die Lehrer-Abschnittsdaten
	 *
	 * @return die Zuordnung
	 */
	public @NotNull Map<Long, List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> getMapMehrleistungen(
			final Collection<DTOLehrerAbschnittsdaten> abschnitte) {
		final var idsAbschnitte = abschnitte.stream().map(a -> a.ID).toList();
		final var mapMehrleistungen = lehrerMehrleistungenRepository.getMapByAbschnitt(idsAbschnitte);
		return abschnitte.stream().collect(Collectors.toMap(a -> a.ID,
				a -> mapMehrleistungen.getOrDefault(a.ID, Collections.emptyList()).stream().map(b -> mapMehrleistung(a, b)).toList()));
	}

	/**
	 * Gibt eine Map mit der Zuordnung der Minderleistungen zu den IDs der übergebenen Lehrer-Abschnittsdaten zurück.
	 *
	 * @param abschnitte   die Lehrer-Abschnittsdaten
	 *
	 * @return die Zuordnung
	 */
	public @NotNull Map<Long, List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> getMapMinderleistungen(
			final Collection<DTOLehrerAbschnittsdaten> abschnitte) {
		final var idsAbschnitte = abschnitte.stream().map(a -> a.ID).toList();
		final var mapMinderleistungen = lehrerMinderleistungenRepository.getMapByAbschnitt(idsAbschnitte);
		return abschnitte.stream().collect(Collectors.toMap(a -> a.ID,
				a -> mapMinderleistungen.getOrDefault(a.ID, Collections.emptyList()).stream().map(b -> mapMinderleistung(a, b)).toList()));
	}

	/**
	 * Gibt eine Map mit der Zuordnung der Anrechnungen zu den IDs der übergebenen Lehrer-Abschnittsdaten zurück.
	 *
	 * @param abschnitte   die Lehrer-Abschnittsdaten
	 *
	 * @return die Zuordnung
	 */
	public @NotNull Map<Long, List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> getMapAnrechungen(final Collection<DTOLehrerAbschnittsdaten> abschnitte) {
		final var idsAbschnitte = abschnitte.stream().map(a -> a.ID).toList();
		final var mapAnrechnungen = lehrerAnrechnungRepository.getMapByAbschnitt(idsAbschnitte);
		return abschnitte.stream().collect(Collectors.toMap(a -> a.ID,
				a -> mapAnrechnungen.getOrDefault(a.ID, Collections.emptyList()).stream().map(b -> mapAnrechnung(a, b)).toList()));
	}

}
