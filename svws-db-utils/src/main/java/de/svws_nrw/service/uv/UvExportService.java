package de.svws_nrw.service.uv;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKlassenLehrer;
import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvGrunddatenBundle;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLehrerAnrechnungsstunden;
import de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle;
import de.svws_nrw.core.data.uv.UvRaum;
import de.svws_nrw.core.data.uv.UvRaumgruppe;
import de.svws_nrw.core.data.uv.UvSchiene;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvStundentafel;
import de.svws_nrw.core.data.uv.UvStundentafelFach;
import de.svws_nrw.core.data.uv.UvUnterricht;
import de.svws_nrw.core.data.uv.UvZeitraster;
import de.svws_nrw.core.data.uv.UvZeitrasterEintrag;
import de.svws_nrw.core.data.uv.export.UVv1Export;
import de.svws_nrw.core.data.uv.export.UVv1FachExport;
import de.svws_nrw.core.data.uv.export.UVv1GrunddatenExport;
import de.svws_nrw.core.data.uv.export.UVv1KlasseExport;
import de.svws_nrw.core.data.uv.export.UVv1KlassenLehrerExport;
import de.svws_nrw.core.data.uv.export.UVv1KursExport;
import de.svws_nrw.core.data.uv.export.UVv1LehrerAnrechnungsstundenExport;
import de.svws_nrw.core.data.uv.export.UVv1LehrerExport;
import de.svws_nrw.core.data.uv.export.UVv1LehrerPflichtstundensollExport;
import de.svws_nrw.core.data.uv.export.UVv1LehrerUnterrichtsfachExport;
import de.svws_nrw.core.data.uv.export.UVv1LerngruppeExport;
import de.svws_nrw.core.data.uv.export.UVv1LerngruppenLehrerExport;
import de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittExport;
import de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittSchuelerExport;
import de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittZeitrasterExport;
import de.svws_nrw.core.data.uv.export.UVv1RaumExport;
import de.svws_nrw.core.data.uv.export.UVv1RaumgruppeExport;
import de.svws_nrw.core.data.uv.export.UVv1SchieneExport;
import de.svws_nrw.core.data.uv.export.UVv1SchuelergruppeExport;
import de.svws_nrw.core.data.uv.export.UVv1StundentafelExport;
import de.svws_nrw.core.data.uv.export.UVv1StundentafelFachExport;
import de.svws_nrw.core.data.uv.export.UVv1UnterrichtExport;
import de.svws_nrw.core.data.uv.export.UVv1ZeitrasterEintragExport;
import de.svws_nrw.core.data.uv.export.UVv1ZeitrasterExport;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittService;

/**
 * Ein Service für versionierte Exporte der Unterrichtsverteilung.
 */
public final class UvExportService {

	private final UvGrunddatenBundleService uvGrunddatenBundleService;
	private final UvPlanungsabschnittService uvPlanungsabschnittService;
	private final UvPlanungsabschnittsdatenBundleService uvPlanungsabschnittsdatenBundleService;

	/**
	 * Erstellt einen neuen Service für UV-Exporte.
	 *
	 * @param uvGrunddatenBundleService der Service für die UV-Grunddaten
	 * @param uvPlanungsabschnittService der Service für die UV-Planungsabschnitte
	 * @param uvPlanungsabschnittsdatenBundleService der Service für die Daten eines UV-Planungsabschnitts
	 */
	public UvExportService(final UvGrunddatenBundleService uvGrunddatenBundleService,
			final UvPlanungsabschnittService uvPlanungsabschnittService,
			final UvPlanungsabschnittsdatenBundleService uvPlanungsabschnittsdatenBundleService) {
		this.uvGrunddatenBundleService = uvGrunddatenBundleService;
		this.uvPlanungsabschnittService = uvPlanungsabschnittService;
		this.uvPlanungsabschnittsdatenBundleService = uvPlanungsabschnittsdatenBundleService;
	}

	/**
	 * Erzeugt den Export der Unterrichtsverteilung im Format v1.
	 *
	 * @return der UV-Export
	 */
	public UVv1Export getExportV1() {
		final UVv1Export result = new UVv1Export();
		final UvGrunddatenBundle grunddatenBundle = uvGrunddatenBundleService.getGrunddatenBundle();
		result.grunddaten = mapGrunddaten(grunddatenBundle);
		result.planungsabschnitte = uvPlanungsabschnittService.getAll().stream()
				.map(planungsabschnitt -> uvPlanungsabschnittsdatenBundleService.getPlanungsabschnittsdatenBundle(planungsabschnitt.id))
				.map(UvExportService::mapPlanungsabschnitt)
				.toList();
		return result;
	}

	private static UVv1GrunddatenExport mapGrunddaten(final UvGrunddatenBundle bundle) {
		final UVv1GrunddatenExport result = new UVv1GrunddatenExport();
		final Map<Long, List<LehrerUnterrichtsfach>> unterrichtsfaecherByLehrerId = bundle.lehrerUnterrichtsfaecher.stream()
				.collect(Collectors.groupingBy(fach -> fach.idLehrer));
		final Map<Long, List<UvLehrerAnrechnungsstunden>> anrechnungsstundenByLehrerId = bundle.lehrerAnrechnungsstunden.stream()
				.collect(Collectors.groupingBy(stunden -> stunden.idLehrer));
		final Map<Long, List<UvLehrerPflichtstundensoll>> pflichtstundensollByLehrerId = bundle.lehrerPflichtstundensoll.stream()
				.collect(Collectors.groupingBy(soll -> soll.idLehrer));

		result.lehrer = bundle.lehrer.stream()
				.map(lehrer -> mapLehrer(lehrer, unterrichtsfaecherByLehrerId, anrechnungsstundenByLehrerId, pflichtstundensollByLehrerId))
				.toList();
		result.raeume = bundle.raeume.stream().map(UvExportService::mapRaum).toList();
		result.raumgruppen = bundle.raumgruppen.stream().map(UvExportService::mapRaumgruppe).toList();
		result.stundentafeln = mapStundentafeln(bundle.stundentafeln, bundle.stundentafelfaecher);
		result.zeitraster = mapZeitraster(bundle.zeitraster, bundle.zeitrastereintraege);
		result.faecher = bundle.faecher.stream().map(UvExportService::mapFach).toList();
		return result;
	}

	private static UVv1LehrerExport mapLehrer(final UvLehrer lehrer,
			final Map<Long, List<LehrerUnterrichtsfach>> unterrichtsfaecherByLehrerId,
			final Map<Long, List<UvLehrerAnrechnungsstunden>> anrechnungsstundenByLehrerId,
			final Map<Long, List<UvLehrerPflichtstundensoll>> pflichtstundensollByLehrerId) {
		final UVv1LehrerExport result = new UVv1LehrerExport();
		result.uvId = lehrer.id;
		result.idLehrer = lehrer.idKLehrer;
		result.kuerzel = lehrer.kuerzel;
		result.nachname = lehrer.nachname;
		result.vorname = lehrer.vorname;

		final long fachKey = (lehrer.idKLehrer != null) ? lehrer.idKLehrer.longValue() : lehrer.id;
		result.unterrichtsfaecher = unterrichtsfaecherByLehrerId.getOrDefault(fachKey, List.of()).stream()
				.map(UvExportService::mapLehrerUnterrichtsfach)
				.toList();
		result.anrechnungsstunden = anrechnungsstundenByLehrerId.getOrDefault(lehrer.id, List.of()).stream()
				.map(UvExportService::mapAnrechnungsstunde)
				.toList();
		result.pflichtstundensoll = pflichtstundensollByLehrerId.getOrDefault(lehrer.id, List.of()).stream()
				.map(UvExportService::mapPflichtstundensoll)
				.toList();
		return result;
	}

	private static List<UVv1StundentafelExport> mapStundentafeln(final List<UvStundentafel> stundentafeln,
			final List<UvStundentafelFach> stundentafelFaecher) {
		final Map<Long, List<UvStundentafelFach>> faecherByStundentafelId = stundentafelFaecher.stream()
				.collect(Collectors.groupingBy(fach -> fach.idStundentafel));
		return stundentafeln.stream().map(stundentafel -> {
			final UVv1StundentafelExport result = new UVv1StundentafelExport();
			result.uvId = stundentafel.id;
			result.idJahrgang = stundentafel.idJahrgang;
			result.bezeichnung = stundentafel.bezeichnung;
			result.gueltigVon = stundentafel.gueltigVon;
			result.gueltigBis = stundentafel.gueltigBis;
			result.beschreibung = stundentafel.beschreibung;
			result.stundentafelfaecher = faecherByStundentafelId.getOrDefault(stundentafel.id, List.of()).stream()
					.map(UvExportService::mapStundentafelFach)
					.toList();
			return result;
		}).toList();
	}

	private static List<UVv1ZeitrasterExport> mapZeitraster(final List<UvZeitraster> zeitraster,
			final List<UvZeitrasterEintrag> zeitrastereintraege) {
		final Map<Long, List<UvZeitrasterEintrag>> eintraegeByZeitrasterId = zeitrastereintraege.stream()
				.collect(Collectors.groupingBy(eintrag -> eintrag.idZeitraster));
		return zeitraster.stream().map(raster -> {
			final UVv1ZeitrasterExport result = new UVv1ZeitrasterExport();
			result.uvId = raster.id;
			result.gueltigVon = raster.gueltigVon;
			result.gueltigBis = raster.gueltigBis;
			result.bezeichnung = raster.bezeichnung;
			result.eintraege = eintraegeByZeitrasterId.getOrDefault(raster.id, List.of()).stream()
					.map(UvExportService::mapZeitrasterEintrag)
					.toList();
			return result;
		}).toList();
	}

	private static UVv1PlanungsabschnittExport mapPlanungsabschnitt(final UvPlanungsabschnittsdatenBundle bundle) {
		final UVv1PlanungsabschnittExport result = new UVv1PlanungsabschnittExport();
		final UvPlanungsabschnitt planungsabschnitt = bundle.planungsabschnitt;
		final Map<Long, List<UvKlassenLehrer>> klassenlehrerByKlasseId = bundle.klassenlehrer.stream()
				.collect(Collectors.groupingBy(zuordnung -> zuordnung.idKlasse));
		final Map<Long, List<Long>> schuelerIdsBySchuelergruppeId = bundle.schuelergruppenschueler.stream()
				.collect(Collectors.groupingBy(zuordnung -> zuordnung.idSchuelergruppe,
						Collectors.mapping(zuordnung -> zuordnung.idSchueler, Collectors.toList())));
		final Map<Long, List<UvLerngruppenLehrer>> lerngruppenlehrerByLerngruppeId = bundle.lerngruppenlehrer.stream()
				.collect(Collectors.groupingBy(zuordnung -> zuordnung.idLerngruppe));
		final Map<Long, List<Long>> schienenIdsByLerngruppeId = bundle.lerngruppenschienen.stream()
				.collect(Collectors.groupingBy(zuordnung -> zuordnung.idLerngruppe,
						Collectors.mapping(zuordnung -> zuordnung.idSchiene, Collectors.toList())));
		final Map<Long, List<Long>> raumIdsByUnterrichtId = bundle.unterrichtraeume.stream()
				.collect(Collectors.groupingBy(zuordnung -> zuordnung.idUnterricht,
						Collectors.mapping(zuordnung -> zuordnung.idRaum, Collectors.toList())));
		final Map<Long, List<Long>> lerngruppenlehrerIdsByUnterrichtId = bundle.unterrichtlerngruppenlehrer.stream()
				.collect(Collectors.groupingBy(zuordnung -> zuordnung.idUnterricht,
						Collectors.mapping(zuordnung -> zuordnung.idLerngruppenLehrer, Collectors.toList())));
		result.uvId = planungsabschnitt.id;
		result.schuljahr = planungsabschnitt.schuljahr;
		result.aktiv = planungsabschnitt.aktiv;
		result.gueltigVon = planungsabschnitt.gueltigVon;
		result.gueltigBis = planungsabschnitt.gueltigBis;
		result.beschreibung = planungsabschnitt.beschreibung;
		result.lehrerUvIds = bundle.planungsabschnittlehrer.stream().map(l -> l.idLehrer).toList();
		result.schueler = bundle.planungsabschnittschueler.stream().map(UvExportService::mapPlanungsabschnittSchueler).toList();
		result.zeitraster = bundle.planungsabschnittzeitraster.stream().map(UvExportService::mapPlanungsabschnittZeitraster).toList();
		result.klassen = bundle.klassen.stream()
				.map(klasse -> mapKlasse(klasse, klassenlehrerByKlasseId))
				.toList();
		result.kurse = bundle.kurse.stream().map(UvExportService::mapKurs).toList();
		result.schuelergruppen = bundle.schuelergruppen.stream()
				.map(schuelergruppe -> mapSchuelergruppe(schuelergruppe, schuelerIdsBySchuelergruppeId))
				.toList();
		result.schienen = bundle.schienen.stream().map(UvExportService::mapSchiene).toList();
		result.lerngruppen = bundle.lerngruppen.stream()
				.map(lerngruppe -> mapLerngruppe(lerngruppe, lerngruppenlehrerByLerngruppeId, schienenIdsByLerngruppeId))
				.toList();
		result.unterrichte = bundle.unterrichte.stream()
				.map(unterricht -> mapUnterricht(unterricht, raumIdsByUnterrichtId, lerngruppenlehrerIdsByUnterrichtId))
				.toList();
		return result;
	}

	private static UVv1FachExport mapFach(final UvFach fach) {
		final UVv1FachExport result = new UVv1FachExport();
		result.uvId = fach.id;
		result.idFach = fach.idFach;
		result.gueltigVon = fach.gueltigVon;
		result.gueltigBis = fach.gueltigBis;
		return result;
	}

	private static UVv1LehrerUnterrichtsfachExport mapLehrerUnterrichtsfach(final LehrerUnterrichtsfach fach) {
		final UVv1LehrerUnterrichtsfachExport result = new UVv1LehrerUnterrichtsfachExport();
		result.idFach = fach.idFach;
		result.istSek1 = fach.istSek1;
		result.istSek2 = fach.istSek2;
		result.bemerkung = fach.bemerkung;
		result.gueltigVon = fach.gueltigVon;
		result.gueltigBis = fach.gueltigBis;
		return result;
	}

	private static UVv1LehrerAnrechnungsstundenExport mapAnrechnungsstunde(final UvLehrerAnrechnungsstunden stunde) {
		final UVv1LehrerAnrechnungsstundenExport result = new UVv1LehrerAnrechnungsstundenExport();
		result.anrechnungsgrundKrz = stunde.anrechnungsgrundKrz;
		result.anzahlStunden = stunde.anzahlStunden;
		result.gueltigVon = stunde.gueltigVon;
		result.gueltigBis = stunde.gueltigBis;
		return result;
	}

	private static UVv1LehrerPflichtstundensollExport mapPflichtstundensoll(final UvLehrerPflichtstundensoll soll) {
		final UVv1LehrerPflichtstundensollExport result = new UVv1LehrerPflichtstundensollExport();
		result.pflichtstdSoll = soll.pflichtstdSoll;
		result.gueltigVon = soll.gueltigVon;
		result.gueltigBis = soll.gueltigBis;
		return result;
	}

	private static UVv1RaumExport mapRaum(final UvRaum raum) {
		final UVv1RaumExport result = new UVv1RaumExport();
		result.uvId = raum.id;
		result.kuerzel = raum.kuerzel;
		result.beschreibung = raum.beschreibung;
		result.groesse = raum.groesse;
		result.raumgruppeUvId = raum.idRaumgruppe;
		result.gueltigVon = raum.gueltigVon;
		result.gueltigBis = raum.gueltigBis;
		return result;
	}

	private static UVv1RaumgruppeExport mapRaumgruppe(final UvRaumgruppe raumgruppe) {
		final UVv1RaumgruppeExport result = new UVv1RaumgruppeExport();
		result.uvId = raumgruppe.id;
		result.bezeichnung = raumgruppe.bezeichnung;
		result.gueltigVon = raumgruppe.gueltigVon;
		result.gueltigBis = raumgruppe.gueltigBis;
		result.beschreibung = raumgruppe.beschreibung;
		return result;
	}

	private static UVv1StundentafelFachExport mapStundentafelFach(final UvStundentafelFach fach) {
		final UVv1StundentafelFachExport result = new UVv1StundentafelFachExport();
		result.abschnitt = fach.abschnitt;
		result.fachUvId = fach.idFach;
		result.wochenstunden = fach.wochenstunden;
		result.davonErgaenzungsstunden = fach.davonErgaenzungsstunden;
		return result;
	}

	private static UVv1ZeitrasterEintragExport mapZeitrasterEintrag(final UvZeitrasterEintrag eintrag) {
		final UVv1ZeitrasterEintragExport result = new UVv1ZeitrasterEintragExport();
		result.uvId = eintrag.id;
		result.wochentag = eintrag.wochentag;
		result.stunde = eintrag.stunde;
		result.beginn = eintrag.beginn;
		result.ende = eintrag.ende;
		return result;
	}

	private static UVv1PlanungsabschnittSchuelerExport mapPlanungsabschnittSchueler(final UvPlanungsabschnittSchueler schueler) {
		final UVv1PlanungsabschnittSchuelerExport result = new UVv1PlanungsabschnittSchuelerExport();
		result.idSchueler = schueler.idSchueler;
		result.idJahrgang = schueler.idJahrgang;
		result.klasseUvId = schueler.idKlasse;
		return result;
	}

	private static UVv1PlanungsabschnittZeitrasterExport mapPlanungsabschnittZeitraster(final de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster zeitraster) {
		final UVv1PlanungsabschnittZeitrasterExport result = new UVv1PlanungsabschnittZeitrasterExport();
		result.zeitrasterUvId = zeitraster.idZeitraster;
		result.idsJahrgaenge = zeitraster.idsJahrgaenge;
		return result;
	}

	private static UVv1KlasseExport mapKlasse(final UvKlasse klasse, final Map<Long, List<UvKlassenLehrer>> klassenlehrerByKlasseId) {
		final UVv1KlasseExport result = new UVv1KlasseExport();
		result.uvId = klasse.id;
		result.idSchuljahresabschnitt = klasse.idSchuljahresabschnitt;
		result.bezeichnung = klasse.bezeichnung;
		result.kuerzel = klasse.kuerzel;
		result.parallelitaet = klasse.parallelitaet;
		result.idStundentafel = klasse.idStundentafel;
		result.schuelergruppeUvId = klasse.idSchuelergruppe;
		result.orgFormKrz = klasse.orgFormKrz;
		result.idFachklasse = klasse.idFachklasse;
		result.asdSchulformNr = klasse.asdSchulformNr;
		result.klassenlehrer = klassenlehrerByKlasseId.getOrDefault(klasse.id, List.of()).stream()
				.map(UvExportService::mapKlassenLehrer)
				.toList();
		return result;
	}

	private static UVv1KlassenLehrerExport mapKlassenLehrer(final UvKlassenLehrer klassenlehrer) {
		final UVv1KlassenLehrerExport result = new UVv1KlassenLehrerExport();
		result.idLehrer = klassenlehrer.idLehrer;
		result.reihenfolge = klassenlehrer.reihenfolge;
		return result;
	}

	private static UVv1KursExport mapKurs(final UvKurs kurs) {
		final UVv1KursExport result = new UVv1KursExport();
		result.uvId = kurs.id;
		result.idSchuljahresabschnitt = kurs.idSchuljahresabschnitt;
		result.fachUvId = kurs.idFach;
		result.kursart = kurs.kursart;
		result.kursnummer = kurs.kursnummer;
		result.schuelergruppeUvId = kurs.idSchuelergruppe;
		return result;
	}

	private static UVv1SchuelergruppeExport mapSchuelergruppe(final UvSchuelergruppe schuelergruppe, final Map<Long, List<Long>> schuelerIdsBySchuelergruppeId) {
		final UVv1SchuelergruppeExport result = new UVv1SchuelergruppeExport();
		result.uvId = schuelergruppe.id;
		result.bezeichnung = schuelergruppe.bezeichnung;
		result.schuelerIds = schuelerIdsBySchuelergruppeId.getOrDefault(schuelergruppe.id, List.of());
		return result;
	}

	private static UVv1SchieneExport mapSchiene(final UvSchiene schiene) {
		final UVv1SchieneExport result = new UVv1SchieneExport();
		result.uvId = schiene.id;
		result.nummer = schiene.nummer;
		result.bezeichnung = schiene.bezeichnung;
		return result;
	}

	private static UVv1LerngruppeExport mapLerngruppe(final UvLerngruppe lerngruppe,
			final Map<Long, List<UvLerngruppenLehrer>> lerngruppenlehrerByLerngruppeId,
			final Map<Long, List<Long>> schienenIdsByLerngruppeId) {
		final UVv1LerngruppeExport result = new UVv1LerngruppeExport();
		result.uvId = lerngruppe.id;
		result.klasseUvId = lerngruppe.idKlasse;
		result.fachUvId = lerngruppe.idFach;
		result.kursUvId = lerngruppe.idKurs;
		result.wochenstunden = lerngruppe.wochenstunden;
		result.wochenstundenUnterrichtet = lerngruppe.wochenstundenUnterrichtet;
		result.koopSchulNr = lerngruppe.koopSchulNr;
		result.koopAnzahlExterne = lerngruppe.koopAnzahlExterne;
		result.lehrer = lerngruppenlehrerByLerngruppeId.getOrDefault(lerngruppe.id, List.of()).stream()
				.map(UvExportService::mapLerngruppenLehrer)
				.toList();
		result.schienenUvIds = schienenIdsByLerngruppeId.getOrDefault(lerngruppe.id, List.of());
		return result;
	}

	private static UVv1LerngruppenLehrerExport mapLerngruppenLehrer(final UvLerngruppenLehrer lerngruppenlehrer) {
		final UVv1LerngruppenLehrerExport result = new UVv1LerngruppenLehrerExport();
		result.lehrerUvId = lerngruppenlehrer.idLehrer;
		result.reihenfolge = lerngruppenlehrer.reihenfolge;
		result.wochenstunden = lerngruppenlehrer.wochenstunden;
		result.wochenstundenAngerechnet = lerngruppenlehrer.wochenstundenAngerechnet;
		return result;
	}

	private static UVv1UnterrichtExport mapUnterricht(final UvUnterricht unterricht,
			final Map<Long, List<Long>> raumIdsByUnterrichtId,
			final Map<Long, List<Long>> lerngruppenlehrerIdsByUnterrichtId) {
		final UVv1UnterrichtExport result = new UVv1UnterrichtExport();
		result.uvId = unterricht.id;
		result.zeitrasterEintragUvId = unterricht.idZeitrasterEintrag;
		result.lerngruppeUvId = unterricht.idLerngruppe;
		result.raumUvIds = raumIdsByUnterrichtId.getOrDefault(unterricht.id, List.of());
		result.lerngruppenlehrerUvIds = lerngruppenlehrerIdsByUnterrichtId.getOrDefault(unterricht.id, List.of());
		return result;
	}

}
