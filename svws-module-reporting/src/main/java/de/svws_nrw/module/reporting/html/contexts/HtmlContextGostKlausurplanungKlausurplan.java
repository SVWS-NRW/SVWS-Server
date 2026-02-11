package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.svws_nrw.module.reporting.filterung.ReportingFilterDataType;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionHjData;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausuren;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import jakarta.ws.rs.core.Response;


/**
 * Ein Thymeleaf-html-Daten-Context zum Bereich "GostKlausurplanung", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextGostKlausurplanungKlausurplan extends HtmlContext<Object> {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Klausurplan dieses Contexts. */
	@JsonIgnore
	private ReportingGostKlausurplanungKlausurplan gostKlausurplan;

	/** Eine Liste von IDs, die die Ausgabe auf diese IDs beschränkt. Auf welchen Datentyp sich diese IDs beziehen, definiert der Wert der Eigenschaft
	 * idsFilterDataType. Ist die Liste leer, dann erfolgt keine Filterung. */
	private final List<Long> idsFilter;

	/** Der Typ von Daten, auf den sich die Filterung der IDs bezieht. */
	private final ReportingFilterDataType idsFilterDataType;

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param idsFilter             Eine Liste von IDs, die die Ausgabe auf diese IDs beschränkt. Auf welchen Datentyp sich diese IDs beziehen, definiert der
	 *                              Wert der Eigenschaft idsFilterDataType.
	 * @param idsFilterDataType     Der Typ von Daten, auf den sich die Filterung der IDs bezieht.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	public HtmlContextGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository, final List<Long> idsFilter,
			final ReportingFilterDataType idsFilterDataType) throws ApiOperationException {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		this.idsFilter = idsFilter;
		this.idsFilterDataType = idsFilterDataType;
		erzeugeContext();
	}

	/**
	 * Initialisiert einen neuen HtmlContext mit den übergebenen Daten.
	 *
	 * @param reportingRepository	Repository mit Parametern, Logger und Daten zum Reporting.
	 * @param gostKlausurplan		Ein GOSt-Klausurplan, auf dem dieser Kontext aufbauen soll.
	 * @param idsFilter             Eine Liste von IDs, die die Ausgabe auf diese IDs beschränkt. Auf welchen Datentyp sich diese IDs beziehen, definiert der
	 *                              Wert der Eigenschaft idsFilterDataType.
	 * @param idsFilterDataType     Der Typ von Daten, auf den sich die Filterung der IDs bezieht.
	 */
	public HtmlContextGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository, final ReportingGostKlausurplanungKlausurplan gostKlausurplan,
			final List<Long> idsFilter, final ReportingFilterDataType idsFilterDataType) {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		this.idsFilter = idsFilter;
		this.idsFilterDataType = idsFilterDataType;
		erzeugeContextFromKlausurplan(gostKlausurplan);
	}


	/**
	 * Erzeugt den Context zur GOSt-Klausurplanung.
	 *
	 * @throws ApiOperationException   	im Fehlerfall
	 */
	private void erzeugeContext() throws ApiOperationException {

		// In den idsHauptdaten der Reporting-Parameter werden im Wechsel das Abiturjahr und das GostHalbjahr (0 = EF.1 bis 5 = Q2.2) übergeben.
		// Hier werden die Daten NICHT validiert. Die Daten aus den Parametern müssen vorab validiert worden sein (ReportingValidierung).
		final List<Long> parameterDaten = reportingRepository.reportingParameter().idsHauptdaten().stream().filter(Objects::nonNull).toList();
		final List<GostKlausurenCollectionHjData> selection = new ArrayList<>();

		if (!parameterDaten.isEmpty()) {
			// Stelle die übergebenen Stufen und Halbjahre zusammen.
			for (int i = 0; i < parameterDaten.size(); i = i + 2) {
				selection.add(new GostKlausurenCollectionHjData(Math.toIntExact(parameterDaten.get(i)), Math.toIntExact(parameterDaten.get(i + 1))));
			}
		} else {
			// Es wurden keine Stufen übergeben. Erzeuge die Ausgabe für alle Stufen gemäß Schuljahresabschnitt im Client.
			// EF:
			selection.add(new GostKlausurenCollectionHjData(reportingRepository.auswahlSchuljahresabschnitt().schuljahr() + 3,
					reportingRepository.auswahlSchuljahresabschnitt().abschnitt() - 1));
			// Q1:
			selection.add(new GostKlausurenCollectionHjData(reportingRepository.auswahlSchuljahresabschnitt().schuljahr() + 2,
					reportingRepository.auswahlSchuljahresabschnitt().abschnitt() + 1));
			// Q2:
			selection.add(new GostKlausurenCollectionHjData(reportingRepository.auswahlSchuljahresabschnitt().schuljahr() + 1,
					reportingRepository.auswahlSchuljahresabschnitt().abschnitt() + 3));
		}

		try {
			final GostKlausurenCollectionAllData allData = DataGostKlausuren.getAllData(this.reportingRepository.conn(), selection);
			final GostKlausurplanManager gostKlausurManager =
					new GostKlausurplanManager(allData);

			this.gostKlausurplan = new ProxyReportingGostKlausurplanungKlausurplan(this.reportingRepository, gostKlausurManager, this.idsFilter, this.idsFilterDataType);

			// Daten-Context für Thymeleaf erzeugen.
			final Context context = new Context();
			context.setVariable("GostKlausurplan", this.gostKlausurplan);

			super.setContext(context);
		} catch (final ApiOperationException e) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, e,
					"FEHLER: Zu mindestens einer Stufe konnten keine Klausurplanungsdaten ermittelt werden. Es konnte kein html-Klausuren-Kontext erstellt werden.");
		}
	}


	/**
	 * Erzeugt den Context zur GOSt-Klausurplanung auf Basis des Klausurplan-Objektes.
	 *
	 * @param gostKlausurplan		Ein GOSt-Klausurplan, auf dem dieser Kontext aufbauen soll.
	 */
	private void erzeugeContextFromKlausurplan(final ReportingGostKlausurplanungKlausurplan gostKlausurplan) {
		this.gostKlausurplan = new ProxyReportingGostKlausurplanungKlausurplan(reportingRepository, gostKlausurplan.klausurtermine(), gostKlausurplan.kurse(),
				gostKlausurplan.kursklausuren(), gostKlausurplan.schueler(), gostKlausurplan.schuelerklausuren(), this.idsFilter, this.idsFilterDataType);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("GostKlausurplan", this.gostKlausurplan);

		super.setContext(context);
	}


	/**
	 * Teile diesen Context mit allen Schülern in eine Liste von Contexts auf, die jeweils einen Schüler enthalten. Damit können Ausgaben pro Schüler erzeugt
	 * werden.
	 *
	 * @return	Liste der Einzel-Contexts.
	 */
	public List<HtmlContextGostKlausurplanungKlausurplan> getEinzelContexts() {
		final List<HtmlContextGostKlausurplanungKlausurplan> result = new ArrayList<>();

		for (final Long idFilter : this.gostKlausurplan.idsGefiltert()) {
			final HtmlContextGostKlausurplanungKlausurplan htmlContextGostKlausurplanungKlausurplan =
					new HtmlContextGostKlausurplanungKlausurplan(this.reportingRepository, this.gostKlausurplan, List.of(idFilter), this.idsFilterDataType);
			result.add(htmlContextGostKlausurplanungKlausurplan);
		}

		return result;
	}

	/**
	 * Gibt eine Liste von IDs für die Ausgabe von Detaildaten zurück. Falls keine IDs definiert sind, wird eine leere Liste zurückgegeben.
	 *
	 * @return Eine Liste von Long-Werten, die die IDs der Detaildaten darstellen, oder eine leere Liste, wenn keine Detaildaten-IDs vorhanden sind.
	 */
	@Override
	public List<Long> getIds() {
		return this.gostKlausurplan.idsGefiltert();
	}
}
