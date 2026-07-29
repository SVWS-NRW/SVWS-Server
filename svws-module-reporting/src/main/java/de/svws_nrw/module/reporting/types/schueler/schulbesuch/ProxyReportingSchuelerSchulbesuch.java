package de.svws_nrw.module.reporting.types.schueler.schulbesuch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.schueler.UebergangsempfehlungKatalogEintrag;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingSchulkatalogEintragNRW;

/**
 * Proxy-Klasse für die Darstellung von Schulbesuchsdaten eines Schülers für das Reporting.
 * Diese Klasse erweitert die ReportingSchuelerSchulbesuch und wird über die Übergabe
 * eines SchuelerSchulbesuchsdaten-Objektes gefüllt.
 */
public class ProxyReportingSchuelerSchulbesuch extends ReportingSchuelerSchulbesuch {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt eine neue Proxy-Instanz aus einem SchuelerSchulbesuchsdaten-Objekt.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param schulbesuchsdaten Das SchuelerSchulbesuchsdaten-Objekt, aus dem die Proxy-Instanz erstellt wird
	 */
	public ProxyReportingSchuelerSchulbesuch(final ReportingContext reportingContext, final SchuelerSchulbesuchsdaten schulbesuchsdaten) {
		super(
				createReportingSchulkatalogEintragNRW(reportingContext, schulbesuchsdaten.idVorherigeSchule),
				ersetzeNullBlankTrim(schulbesuchsdaten.entlassdatumVorherigeSchule),
				ersetzeNullBlankTrim(schulbesuchsdaten.kuerzelEntlassjahrgangVorherigeSchule),
				ersetzeNullBlankTrim(schulbesuchsdaten.idHerkunftsartVersetzungVorherigeSchule),
				ersetzeNullBlankTrim(schulbesuchsdaten.bemerkungVorherigeSchule),
				createEndlassgrund(reportingContext, schulbesuchsdaten.idEntlassgrundVorherigeSchule),
				schulbesuchsdaten.schluesselAbschlussartBerufsbildendVorherigeSchule + schulbesuchsdaten.schluesselAbschlussartAllgemeinbildendVorherigeSchule,
				ersetzeNullBlankTrim(schulbesuchsdaten.entlassdatumDieseSchule),
				schulbesuchsdaten.idEntlassjahrgangDieseSchule,
				createEndlassgrund(reportingContext, schulbesuchsdaten.idEntlassgrundDieseSchule),
				schulbesuchsdaten.idAbschlussartDieseSchule,
				schulbesuchsdaten.schluesselHoechsterSchulabschluss,
				createReportingSchulkatalogEintragNRW(reportingContext, schulbesuchsdaten.idAufnehmendeSchule),
				ersetzeNullBlankTrim(schulbesuchsdaten.wechseldatumAufnehmendeSchule),
				schulbesuchsdaten.wechselBestaetigtAufnehmendeSchule,
				schulbesuchsdaten.einschulungsjahrGrundschule,
				schulbesuchsdaten.idEinschulungsartGrundschule,
				schulbesuchsdaten.idEingangsphaseGrundschule,
				uebergangsempfehlungKuerzel(schulbesuchsdaten.idUebergangsempfehlungGrundschule),
				uebergangsempfehlungText(schulbesuchsdaten.idUebergangsempfehlungGrundschule),
				schulbesuchsdaten.wechseljahrSekI,
				ersetzeNullBlankTrim(schulbesuchsdaten.kuerzelErsteSchulformSek1),
				schulbesuchsdaten.wechseljahrSekII,
				schulbesuchsdaten.idDauerKindergartenbesuch,
				schulbesuchsdaten.idKindergarten,
				schulbesuchsdaten.verpflichtungSprachfoerderkurs,
				schulbesuchsdaten.teilnahmeSprachfoerderkurs,
				convertAlleSchulen(reportingContext, schulbesuchsdaten.bisherBesuchteSchulen)
		);
		this.reportingContext = reportingContext;
	}

	private static ProxyReportingSchulkatalogEintragNRW createReportingSchulkatalogEintragNRW(final ReportingContext reportingContext,
			final Long idSchule) {
		if (idSchule == null) {
			return null;
		}

		final SchulEintrag schulEintrag = reportingContext.repositoryKataloge().schule(idSchule);
		if (schulEintrag == null) {
			return null;
		}

		return new ProxyReportingSchulkatalogEintragNRW(reportingContext, schulEintrag);
	}

	private static KatalogEntlassgrund createEndlassgrund(final ReportingContext reportingContext, final Long idEntlassgrund) {
		if (idEntlassgrund == null) {
			return null;
		}

		return reportingContext.repositoryKataloge().entlassgrund(idEntlassgrund);
	}

	private static String uebergangsempfehlungKuerzel(final Long idUebergangsempfehlung) {
		if (idUebergangsempfehlung == null) {
			return "";
		}
		final UebergangsempfehlungKatalogEintrag eintrag = Uebergangsempfehlung.data().getEintragByID(idUebergangsempfehlung);
		return (eintrag == null) ? "" : eintrag.kuerzel;
	}

	private static String uebergangsempfehlungText(final Long idUebergangsempfehlung) {
		if (idUebergangsempfehlung == null) {
			return "";
		}
		final UebergangsempfehlungKatalogEintrag eintrag = Uebergangsempfehlung.data().getEintragByID(idUebergangsempfehlung);
		return (eintrag == null) ? "" : eintrag.text;
	}

	/**
	 * Konvertiert die Liste der SchuelerSchulbesuchSchule-Objekte in ReportingSchuelerSchulbesuchSchule-Objekte.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param alleSchulen Die Liste der SchuelerSchulbesuchSchule-Objekte
	 * @return Eine Liste von ReportingSchuelerSchulbesuchSchule-Objekten
	 */
	private static List<ReportingSchuelerSchulbesuchSchule> convertAlleSchulen(final ReportingContext reportingContext,
			final List<SchuelerSchulbesuchSchule> alleSchulen) {
		if ((alleSchulen == null) || alleSchulen.isEmpty()) {
			return new ArrayList<>();
		}

		return alleSchulen.stream()
				.map(schule -> new ProxyReportingSchuelerSchulbesuchSchule(reportingContext, schule)).collect(Collectors.toList());
	}

	// ##### Getter #####

	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return reportingContext;
	}
}
