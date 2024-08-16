package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.util.List;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.DTOManagerMapper;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeume;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurtermin und erweitert die Klasse {@link ReportingGostKlausurplanungKlausurtermin}.</p>
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
public class ProxyReportingGostKlausurplanungKlausurtermin extends GostKlausurtermin {

	private final GostKlausurplanManager manager;
	private final ReportingGostKlausurplanungKlausurplan plan;

	/**
	 * Erstellt ein neues Reporting-Objekt. Dabei werden Kurs- und Schülerklausuren dieses Termins und dessen Räume als LEERE Liste initialisiert.
	 * Das Hinzufügen dieser Elemente erfolgt in der Proxy-Klassen bei ihrer Erzeugung.
	 * @param manager	Der GostKlausurtermin mit den Daten zum Klausurtermin.
	 * @param plan Der Plan
	 */
	public ProxyReportingGostKlausurplanungKlausurtermin(final GostKlausurplanManager manager, final ReportingGostKlausurplanungKlausurplan plan) {
		this.manager = manager;
		this.plan = plan;
	}

	/**
	 * Die Liste von Kursklausuren zu diesem Klausurtermin.
	 * @return Inhalt des Feldes kursklausuren
	 */
	public List<ReportingGostKlausurplanungKursklausur> getKursklausuren() {
		return manager.kursklausurGetMengeByTermin(this).stream().map(kk -> new ReportingGostKlausurplanungKursklausur((ProxyReportingGostKlausurplanungKursklausur) kk)).toList();
	}

	/**
	 * Die Liste der Schüler aus dem Kurs, die diese Klausur schreiben.
	 * @return Inhalt des Feldes klausurschreiber
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> getSchuelerklausuren() {
		return manager.schuelerklausurterminGetMengeByTermin(this).stream().map(sk -> new ReportingGostKlausurplanungSchuelerklausur((ProxyReportingGostKlausurplanungSchuelerklausur) sk)).toList();
	}

	/**
	 * Die Liste der Schülerklausuren zum Termin ihres Kurses.
	 * @return Die Schülerklausuren zum Kurstermin.
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> getSchuelerklausurenKurstermin() {
		return manager.schuelerklausurterminAktuellHtGetMengeByTermin(this).stream().map(sk -> new ReportingGostKlausurplanungSchuelerklausur((ProxyReportingGostKlausurplanungSchuelerklausur) sk)).toList();
	}

	/**
	 * Die Liste der Schülerklausuren zum Termin die Nachschreibklausuren darstellen.
	 * @return Die Schülerklausuren als Nachschreibklausuren.
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> getSchuelerklausurenNachschreibtermin() {
		return manager.schuelerklausurterminAktuellNtGetMengeByTermin(this).stream().map(sk -> new ReportingGostKlausurplanungSchuelerklausur((ProxyReportingGostKlausurplanungSchuelerklausur) sk)).toList();
	}

	/**
	 * Die Klausurräume dieses Termines, inkluse der Aufsichten für die Unterrichtsstunden der Klausur.
	 * @return Inhalt des Feldes klausurraeume
	 */
	public List<ReportingGostKlausurplanungKlausurraum> getKlausurraeume() {
		return manager.raumGetMengeByTermin(this).stream().map(kr -> new ReportingGostKlausurplanungKlausurraum((ProxyReportingGostKlausurplanungKlausurraum) kr)).toList();
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
	public static final DTOManagerMapper<GostKlausurtermin, ProxyReportingGostKlausurplanungKlausurtermin, GostKlausurplanManager, ReportingGostKlausurplanungKlausurplan> dtoMapper = (final GostKlausurtermin z, final GostKlausurplanManager m, final ReportingGostKlausurplanungKlausurplan p) -> {
		final ProxyReportingGostKlausurplanungKlausurtermin daten = new ProxyReportingGostKlausurplanungKlausurtermin(m, p);
		daten.id = z.id;
		daten.abijahr = z.abijahr;
		daten.bemerkung = z.bemerkung;
		daten.bezeichnung = z.bezeichnung;
		daten.datum = z.datum;
		daten.halbjahr = z.halbjahr;
		daten.istHaupttermin = z.istHaupttermin;
		daten.nachschreiberZugelassen = z.nachschreiberZugelassen;
		daten.quartal = z.quartal;
		daten.startzeit = z.startzeit;
		return daten;
	};

}
