package de.svws_nrw.module.reporting.html.contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.filterung.ReportingFilterDataType;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ProxyReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import jakarta.ws.rs.core.Response;
import org.thymeleaf.context.Context;


/**
 * Ein Thymeleaf-Html-Daten-Context zum Bereich "GostKursplanung", um Thymeleaf-html-Templates mit Daten zu füllen.
 */
public final class HtmlContextGostKursplanungBlockungsergebnis extends HtmlContext<Object> {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Das Reporting-Objekt zum Blockungsergebnis. */
	@JsonIgnore
	private ReportingGostKursplanungBlockungsergebnis reportingGostKursplanungBlockungsergebnis = null;

	/** Blockungsergebnis dieses Contexts. */
	@JsonIgnore
	private GostBlockungsergebnis blockungsergebnis = null;

	/** Datenmanager dieses Contexts. */
	@JsonIgnore
	private GostBlockungsdatenManager datenManager = null;

	/** Eine Liste von IDs, die die Ausgabe auf diese IDs beschränkt. Auf welchen Datentyp sich diese IDs beziehen, definiert der Wert der Eigenschaft
	 * idsFilterDataType. Ist die Liste leer, dann erfolgt keine Filterung. */
	private List<Long> idsFilter;

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
	public HtmlContextGostKursplanungBlockungsergebnis(final ReportingRepository reportingRepository, final List<Long> idsFilter,
			final ReportingFilterDataType idsFilterDataType) throws ApiOperationException {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		this.idsFilter = idsFilter;
		this.idsFilterDataType = idsFilterDataType;
		erzeugeContext();
	}

	/**
	 * Konstruktor zur Erstellung eines HtmlContext für die Kursplanung eines Blockungsergebnisses in der gymnasialen Oberstufe.
	 *
	 * @param reportingRepository   Das Reporting-Repository, welches Parameter, Logger und Daten für das Reporting bereitstellt.
	 * @param blockungsergebnis     Das Blockungsergebnis der Kursplanung, für das der HtmlContext erstellt wird.
	 * @param datenManager          Der Datenmanager, der Hilfsfunktionen und grundlegende Daten für die Blockung bereitstellt.
	 * @param idsFilter             Eine Liste von IDs, die die Ausgabe auf diese IDs beschränkt. Auf welchen Datentyp sich diese IDs beziehen, definiert der
	 *                              Wert der Eigenschaft idsFilterDataType.
	 * @param idsFilterDataType     Der Typ von Daten, auf den sich die Filterung der IDs bezieht.
	 *
	 * @throws ApiOperationException Wird ausgelöst, wenn ein Fehler während der Erstellung des Kontextes auftritt.
	 */
	public HtmlContextGostKursplanungBlockungsergebnis(final ReportingRepository reportingRepository, final GostBlockungsergebnis blockungsergebnis,
			final GostBlockungsdatenManager datenManager, final List<Long> idsFilter, final ReportingFilterDataType idsFilterDataType)
			throws ApiOperationException {
		super(reportingRepository, true);
		this.reportingRepository = reportingRepository;
		this.blockungsergebnis = blockungsergebnis;
		this.datenManager = datenManager;
		this.idsFilter = idsFilter;
		this.idsFilterDataType = idsFilterDataType;
		erzeugeContext();
	}

	/**
	 * Erzeugt den Context zum Füllen eines HTML-Templates.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void erzeugeContext() throws ApiOperationException {

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeuge Context zu einem GostKursplanungBlockungsergebnis.");

		this.idsFilter = (this.idsFilter == null) ? new ArrayList<>() : this.idsFilter.stream().filter(Objects::nonNull).toList();

		if ((blockungsergebnis == null) || (this.datenManager == null)) {
			try {
				final Long idBlockungsergebnis = this.reportingRepository.reportingParameter().idsHauptdaten().getFirst();
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Die ID der Blockungsergebnisses wurde ermittelt: " + idBlockungsergebnis.toString());
				this.blockungsergebnis = DataGostBlockungsergebnisse.getErgebnisFromID(this.reportingRepository.conn(), idBlockungsergebnis);
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Das Blockungsergebnis wurde ermittelt.");
				this.datenManager = DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(this.reportingRepository.conn(), blockungsergebnis.blockungID);
				reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Der Datenmanager zum Blockungsergebnis wurde ermittelt.");
			} catch (final ApiOperationException e) {
				throw new ApiOperationException(Response.Status.NOT_FOUND, e,
						"FEHLER: Das Blockungsergebnis und der zugehörige Datenmanager konnten nicht ermittelt werden. " + e.getMessage());
			}
		}

		reportingGostKursplanungBlockungsergebnis = new ProxyReportingGostKursplanungBlockungsergebnis(this.reportingRepository, this.blockungsergebnis,
				datenManager, this.idsFilter, this.idsFilterDataType);

		// Daten-Context für Thymeleaf erzeugen.
		final Context context = new Context();
		context.setVariable("GostBlockungsergebnis", reportingGostKursplanungBlockungsergebnis);

		reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Erzeugung des Context zum GostKursplanungBlockungsergebnis abgeschlossen.");

		super.setContext(context);
	}

	/**
	 * Erstellt eine Liste von HTML-Kontexten für die Kursplanung eines Blockungsergebnisses in der gymnasialen Oberstufe. Dabei wird für jede übermittelte
	 * Detail-ID ein separater Kontext erzeugt, der die relevanten Daten enthält.
	 *
	 * @return Eine Liste von HtmlContextGostKursplanungBlockungsergebnis-Objekten, wobei jedes Objekt einen separaten Kontext für die angegebene Detail-ID
	 * darstellt. Wird keine Detail-ID übermittelt oder sind Daten unvollständig, wird eine leere Liste zurückgegeben.
	 */
	public List<HtmlContextGostKursplanungBlockungsergebnis> getEinzelContexts() {
		final List<HtmlContextGostKursplanungBlockungsergebnis> result = new ArrayList<>();

		if ((this.blockungsergebnis == null) || (this.datenManager == null))
			return result;

		try {
			for (final Long idFilter : this.reportingGostKursplanungBlockungsergebnis.idsGefiltert()) {
				final HtmlContextGostKursplanungBlockungsergebnis htmlContextGostKursplanungBlockungsergebnis =
						new HtmlContextGostKursplanungBlockungsergebnis(this.reportingRepository, this.blockungsergebnis, this.datenManager,
								List.of(idFilter), this.idsFilterDataType);
				result.add(htmlContextGostKursplanungBlockungsergebnis);
			}
		} catch (final ApiOperationException ignore) {
			// Da die Einzelausgabe nur bei schon bestehenden Datenmanagern erstellt werden kann, kann diese Exception hier ignoriert werden.
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
		return this.reportingGostKursplanungBlockungsergebnis.idsGefiltert();
	}
}
