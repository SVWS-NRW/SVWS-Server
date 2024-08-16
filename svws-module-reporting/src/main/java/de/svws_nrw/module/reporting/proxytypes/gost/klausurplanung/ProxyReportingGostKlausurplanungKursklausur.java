package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.DTOManagerMapper;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeume;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;


/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKursklausur und erweitert die Klasse {@link ReportingGostKlausurplanungKursklausur}.</p>
 *
 *  <p>In diesem Kontext besitzt die Proxy-Klasse ausschließlich die gleichen Methoden wie die zugehörige Reporting-Super-Klasse.
 *  Während die Super-Klasse aber als reiner Datentyp konzipiert ist, d. h. ohne Anbindung an die Datenbank,
 *  greift die Proxy-Klassen an verschiedenen Stellen auf die Datenbank zu.</p>
 *
 *  <ul>
 *      <li>Die Proxy-Klasse stellt in der Regel einen zusätzlichen Constructor zur Verfügung, um Reporting-Objekte
 *  		aus Stammdatenobjekten (aus dem Package core.data) erstellen zu können. Darin werden Felder, die Reporting-Objekte
 *  		zurückgegeben und nicht im Stammdatenobjekt enthalten sind, mit null initialisiert.</li>
 * 		<li>Die Proxy-Klasse überschreibt einzelne Getter der Super-Klasse (beispielsweise bei Felder, die mit null initialisiert wurden)
 *  		und lädt dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
 *  		halten.</li>
 *  	<li>Die Proxy-Klasse können zudem auf den Klausurplan {@link ReportingGostKlausurplanungKlausurplan} zugreifen. Drin ist wieder der
 *  		Zugriff auf das Repository {@link ReportingRepository} möglich.
 *  		Im ersteren kann auf Klausurmanager zugegriffen werden, um darüber Daten nachladen zu können.
 *  		Das zweite enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 *    		wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 *    		Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingGostKlausurplanungKursklausur extends GostKursklausur {

	private final GostKlausurplanManager manager;
	private final ReportingGostKlausurplanungKlausurplan plan;

	/**
	 * Erstellt ein neues Reporting-Objekt.
	 * @param manager		Der GostKlausurplanManager
	 * @param plan der Plan
	 */
	public ProxyReportingGostKlausurplanungKursklausur(final GostKlausurplanManager manager, final ReportingGostKlausurplanungKlausurplan plan) {
		this.manager = manager;
		this.plan = plan;
	}

	/**
	 * Liefert die Vorgabe zu dieser Klausur
	 *
	 * @return die Vorgabe zu dieser Kursklausur
	 */
	public GostKlausurvorgabe getVorgabe() {
		return manager.vorgabeByKursklausur(this);
	}

	/**
	 * Liefert den Termin zu dieser Klausur
	 *
	 * @return der Termin zu dieser Kursklausur
	 */
	public ReportingGostKlausurplanungKlausurtermin getTermin() {
		return new ReportingGostKlausurplanungKlausurtermin((ProxyReportingGostKlausurplanungKlausurtermin) manager.terminOrExceptionByKursklausur(this));
	}

	/**
	 * Der Kurs, indem die Klausur geschrieben wird, mit seinen Daten.
	 * @return Inhalt des Feldes kurs
	 */
	public ReportingKurs getKurs() {
		return plan.kurs(idKurs);
	}

	/**
	 * Die Liste der Schüler aus dem Kurs, die diese Klausur schreiben.
	 * @return Inhalt des Feldes klausurschreiber
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> getSchuelerklausuren() {
		return manager.schuelerklausurterminGetMengeByKursklausur(this).stream().map(sk -> new ReportingGostKlausurplanungSchuelerklausur((ProxyReportingGostKlausurplanungSchuelerklausur) sk)).toList();
	}

	/**
	 * Die Startuhrzeit der Kursklausur, falls schon gesetzt.
	 * @return Die Uhrzeitangabe der Startzeit.
	 */
	public String getStartuhrzeit() {
		Integer startzeit = manager.startzeitByKursklausurOrNull(this);
		return startzeit == null ? "" : DateUtils.gibZeitStringOfMinuten(startzeit);
	}

	/**
	 * Die Liste der Räume, in denen die Schüler des Kurses ihre Klausur schreiben.
	 * @return Die Liste der Räume der Kursklausur.
	 */
	public List<ReportingGostKlausurplanungKlausurraum> raeume() {
		return manager.raumGetMengeByTerminAndKursklausur(manager.terminGetByIdOrException(idTermin), this).stream().map(r -> new ReportingGostKlausurplanungKlausurraum((ProxyReportingGostKlausurplanungKlausurraum) r)).toList();
	}

	/**
	 * Die Unterrichtsstunden, in denen die Schüler des Kurses ihre Klausur schreiben.
	 * @return Die Unterrichtsstunden der Klausur.
	 */
	public List<Integer> stunden() {
		final List<GostKlausurraum> raeume = manager.raumGetMengeByTerminAndKursklausur(manager.terminGetByIdOrException(idTermin), this);
		if (raeume.isEmpty())
			return new ArrayList<>();
		final GostKlausurraum raum = raeume.getFirst();
		return manager.zeitrasterGetMengeByRaum(raum).stream().map(z -> z.unterrichtstunde).toList();
	}

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return super.equals(another);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenRaeume} in einen Core-DTO
	 * {@link GostKlausurraum}.
	 */
	public static final DTOManagerMapper<GostKursklausur, ProxyReportingGostKlausurplanungKursklausur, GostKlausurplanManager, ReportingGostKlausurplanungKlausurplan> dtoMapper = (final GostKursklausur z, final GostKlausurplanManager m, final ReportingGostKlausurplanungKlausurplan p) -> {
		final ProxyReportingGostKlausurplanungKursklausur daten = new ProxyReportingGostKlausurplanungKursklausur(m, p);
		daten.id = z.id;
		daten.idTermin = z.idTermin;
		daten.bemerkung = z.bemerkung;
		daten.idKurs = z.idKurs;
		daten.idVorgabe = z.idVorgabe;
		daten.startzeit = z.startzeit;
		return daten;
	};
}
