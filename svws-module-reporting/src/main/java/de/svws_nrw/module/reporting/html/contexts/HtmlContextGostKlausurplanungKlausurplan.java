package de.svws_nrw.module.reporting.html.contexts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.thymeleaf.context.Context;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanListUtils;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausuren;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import jakarta.ws.rs.core.Response;


/**
 * Ein ThymeLeaf-Html-Daten-Context zum Bereich "GostKlausurplanung", um ThymeLeaf-html-Templates mit Daten zu füllen und daraus PDF-Dateien zu erstellen.
 */
public final class HtmlContextGostKlausurplanungKlausurplan extends HtmlContext {

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public HtmlContextGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository) throws ApiOperationException {
		erzeugeContext(reportingRepository);
	}

	/**
	 * Erzeugt den Context zum Füllen eines html-Templates.
	 *
	 * @param reportingRepository	Das Repository mit Daten zum Reporting.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void erzeugeContext(final ReportingRepository reportingRepository) throws ApiOperationException {

		// In den idsHauptdaten der Reporting-Parameter werden im Wechsel das Abiturjahr und des GostHlabjahr (0 = EF.1 bis 5 = Q2.2) übergeben.
		// Hier werden die Daten NICHT valdiert. Die Daten aus den Paramatern müssen vorab validiert worden sein (ReportingValidierung).
		final List<Long> parameterDaten = reportingRepository.reportingParameter().idsHauptdaten.stream().filter(Objects::nonNull).toList();
		final List<Pair<Integer, Integer>> selection = new ArrayList<>();

		if (!parameterDaten.isEmpty()) {
			// Stelle die übergebenen Stufen und Halbjahre zusammen.
			for (int i = 0; i < parameterDaten.size(); i = i + 2) {
				selection.add(new Pair<>(Math.toIntExact(parameterDaten.get(i)), Math.toIntExact(parameterDaten.get(i + 1))));
			}
		} else {
			// Es wurden keine Stufen übergeben. Erzeuge die Ausgabe für alle Stufen gemäß Schuljahresabschnitt im Client.
			// EF:
			selection.add(new Pair<>(reportingRepository.auswahlSchuljahresabschnitt().schuljahr + 3,
					reportingRepository.auswahlSchuljahresabschnitt().abschnitt - 1));
			// Q1:
			selection.add(new Pair<>(reportingRepository.auswahlSchuljahresabschnitt().schuljahr + 2,
					reportingRepository.auswahlSchuljahresabschnitt().abschnitt + 1));
			// Q2:
			selection.add(new Pair<>(reportingRepository.auswahlSchuljahresabschnitt().schuljahr + 1,
					reportingRepository.auswahlSchuljahresabschnitt().abschnitt + 3));
		}

		try {
			final GostKlausurenCollectionAllData allData = DataGostKlausuren.getAllData(reportingRepository.conn(), selection);
			final GostKlausurplanManager gostKlausurManager = new GostKlausurplanManager();
			final ReportingGostKlausurplanungKlausurplan gostKlausurplan =
					new ReportingGostKlausurplanungKlausurplan(reportingRepository, gostKlausurManager);
			final GostKlausurenCollectionAllData allProxiedData = coreDtoToReportingProxy(allData, gostKlausurManager, gostKlausurplan);
			gostKlausurManager.addAllData(allProxiedData);
			gostKlausurplan.init_all();


			final List<StundenplanListeEintrag> sl = DataStundenplanListe.getStundenplaene(reportingRepository.conn(), reportingRepository.auswahlSchuljahresabschnitt().id);
			if (sl.isEmpty())
				throw new DeveloperNotificationException("Keine Stundenplandaten gefunden");

			Date date = new Date();
			String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
			final StundenplanListeEintrag slEntry = StundenplanListUtils.get(sl, modifiedDate);
			if (slEntry == null)
				throw new DeveloperNotificationException("Es konnte kein aktiver Stundenplan gefunden werden.");



			final Stundenplan stundenplan = DataStundenplan.getStundenplan(reportingRepository.conn(), slEntry.id);
			final StundenplanManager stundenplanManager = new StundenplanManager(stundenplan, new ArrayList(), new ArrayList(), null);
			gostKlausurManager.setStundenplanManager(stundenplanManager);


			// Daten-Context für Thymeleaf erzeugen.
			final Context context = new Context();
			context.setVariable("GostKlausurplan", gostKlausurplan);

			super.setContext(context);
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, e,
					"FEHLER: Zu mindestens einer Stufe konnten keine Klausurplanungsdaten ermittelt werden. Es konnte kein html-Klausuren-Kontext erstellt werden.");
		}
	}

	private static GostKlausurenCollectionAllData coreDtoToReportingProxy(final GostKlausurenCollectionAllData coreCollection, final GostKlausurplanManager manager, final ReportingGostKlausurplanungKlausurplan plan) {
		final GostKlausurenCollectionAllData reportingCollection = new GostKlausurenCollectionAllData();
		reportingCollection.kursklausuren.addAll(coreCollection.kursklausuren.stream().map(k -> ProxyReportingGostKlausurplanungKursklausur.dtoMapper.apply(k, manager, plan)).toList());
		reportingCollection.metadata = coreCollection.metadata;
		reportingCollection.schuelerklausuren = coreCollection.schuelerklausuren;
		reportingCollection.schuelerklausurtermine.addAll(coreCollection.schuelerklausurtermine.stream().map(k -> ProxyReportingGostKlausurplanungSchuelerklausur.dtoMapper.apply(k, manager, plan)).toList());
		reportingCollection.termine.addAll(coreCollection.termine.stream().map(k -> ProxyReportingGostKlausurplanungKlausurtermin.dtoMapper.apply(k, manager, plan)).toList());
		reportingCollection.vorgaben = coreCollection.vorgaben;
		reportingCollection.raumdata.raeume.addAll(coreCollection.raumdata.raeume.stream().map(k -> ProxyReportingGostKlausurplanungKlausurraum.dtoMapper.apply(k, manager, plan)).toList());
		reportingCollection.raumdata.raumstunden = coreCollection.raumdata.raumstunden;
		reportingCollection.raumdata.sktRaumstunden = coreCollection.raumdata.sktRaumstunden;
		return reportingCollection;
	}
}
