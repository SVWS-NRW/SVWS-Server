package de.svws_nrw.service.uv;

import de.svws_nrw.core.data.uv.UvGrunddatenBundle;
import de.svws_nrw.service.lehrer.unterrichtsfach.LehrerUnterrichtsfachService;
import de.svws_nrw.service.uv.faecher.UvFachService;
import de.svws_nrw.service.uv.lehrer.UvLehrerAnrechnungsstundenService;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollService;
import de.svws_nrw.service.uv.lehrer.UvLehrerService;
import de.svws_nrw.service.uv.lehrer.UvLehrerUnterrichtsfachService;
import de.svws_nrw.service.uv.raeume.UvRaumService;
import de.svws_nrw.service.uv.raeume.UvRaumgruppeService;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachService;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelService;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterEintragService;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterService;

/**
 * Ein Service für den Zugriff auf die UV-Zeitraster.
 */
public final class UvGrunddatenBundleService {

	private final UvRaumgruppeService uvRaumgruppeService;
	private final UvRaumService uvRaumService;
	private final UvStundentafelService uvStundentafelService;
	private final UvStundentafelFachService uvStundentafelFachService;
	private final UvFachService uvFachService;
	private final UvLehrerService uvLehrerService;
	private final UvZeitrasterService uvZeitrasterService;
	private final UvZeitrasterEintragService uvZeitrasterEintragService;
	private final UvLehrerAnrechnungsstundenService uvLehrerAnrechnungsstundenService;
	private final UvLehrerPflichtstundensollService uvLehrerPflichtstundensollService;
	private final UvLehrerUnterrichtsfachService uvLehrerUnterrichtsfachService;
	private final LehrerUnterrichtsfachService lehrerUnterrichtsfachService;

	/**
	 * Erstellt einen neuen Service zum Bündeln der UV-Grunddaten.
	 *
	 * @param uvRaumgruppeService der Service für die UV-Raumgruppen
	 * @param uvRaumService der Service für die UV-Räume
	 * @param uvStundentafelService der Service für die UV-Stundentafeln
	 * @param uvStundentafelFachService der Service für die UV-Stundentafel-Fächer
	 * @param uvFachService der Service für die UV-Fächer
	 * @param uvLehrerService der Service für die UV-Lehrer
	 * @param uvZeitrasterService der Service für die UV-Zeitraster
	 * @param uvZeitrasterEintragService der Service für die UV-Zeitraster-Einträge
	 * @param uvLehrerAnrechnungsstundenService der Service für die UV-Lehrer-Anrechnungsstunden
	 * @param uvLehrerPflichtstundensollService der Service für die UV-Lehrer-Pflichtstundensoll-Datensätze
	 * @param uvLehrerUnterrichtsfachService der Service für UV-Lehrer-Unterrichtsfächer
	 * @param lehrerUnterrichtsfachService der Service für Schild-Lehrer-Unterrichtsfächer
	 */
	public UvGrunddatenBundleService(final UvRaumgruppeService uvRaumgruppeService,
			final UvRaumService uvRaumService,
			final UvStundentafelService uvStundentafelService,
			final UvStundentafelFachService uvStundentafelFachService,
			final UvFachService uvFachService,
			final UvLehrerService uvLehrerService,
			final UvZeitrasterService uvZeitrasterService,
			final UvZeitrasterEintragService uvZeitrasterEintragService,
			final UvLehrerAnrechnungsstundenService uvLehrerAnrechnungsstundenService,
			final UvLehrerPflichtstundensollService uvLehrerPflichtstundensollService,
			final UvLehrerUnterrichtsfachService uvLehrerUnterrichtsfachService,
			final LehrerUnterrichtsfachService lehrerUnterrichtsfachService) {
		this.uvRaumgruppeService = uvRaumgruppeService;
		this.uvRaumService = uvRaumService;
		this.uvStundentafelService = uvStundentafelService;
		this.uvStundentafelFachService = uvStundentafelFachService;
		this.uvFachService = uvFachService;
		this.uvLehrerService = uvLehrerService;
		this.uvZeitrasterService = uvZeitrasterService;
		this.uvZeitrasterEintragService = uvZeitrasterEintragService;
		this.uvLehrerAnrechnungsstundenService = uvLehrerAnrechnungsstundenService;
		this.uvLehrerPflichtstundensollService = uvLehrerPflichtstundensollService;
		this.uvLehrerUnterrichtsfachService = uvLehrerUnterrichtsfachService;
		this.lehrerUnterrichtsfachService = lehrerUnterrichtsfachService;
	}

	/**
	 * Ermittelt ein vollständiges Bundle der UV-Grunddaten.
	 *
	 * @return das Bundle mit Raumgruppen, Räumen, Stundentafeln, Fächern, Lehrern,
	 *         Lehrer-Unterrichtsfächern, Zeitrastern sowie Lehrer-Anrechnungsstunden
	 *         und Pflichtstundensoll-Datensätzen
	 */
	public UvGrunddatenBundle getGrunddatenBundle() {
		final UvGrunddatenBundle result = new UvGrunddatenBundle();
		result.raumgruppen = uvRaumgruppeService.getAll();
		result.raeume = uvRaumService.getAll();
		result.stundentafeln = uvStundentafelService.getAll();
		result.stundentafelfaecher =
				uvStundentafelFachService.getListByStundentafelIds(result.stundentafeln.stream().map(s -> s.id).toList());
		result.faecher = uvFachService.getAll();
		result.lehrer = uvLehrerService.getAll();
		result.lehrerUnterrichtsfaecher.addAll(lehrerUnterrichtsfachService
				.getListByLehrerIds(result.lehrer.stream().filter(l -> l.idKLehrer != null).map(l -> l.idKLehrer).toList()));
		result.lehrerUnterrichtsfaecher.addAll(uvLehrerUnterrichtsfachService
				.getListByLehrerIds(result.lehrer.stream().filter(l -> l.idKLehrer == null).map(l -> l.id).toList()));
		result.zeitraster = uvZeitrasterService.getAll();
		result.zeitrastereintraege = uvZeitrasterEintragService.getListByZeitrasterIds(result.zeitraster.stream().map(z -> z.id).toList());
		result.lehrerAnrechnungsstunden = uvLehrerAnrechnungsstundenService
				.getListByLehrerIds(result.lehrer.stream().map(l -> l.id).toList());
		result.lehrerPflichtstundensoll = uvLehrerPflichtstundensollService
				.getListByLehrerIds(result.lehrer.stream().map(l -> l.id).toList());
		return result;
	}
}
