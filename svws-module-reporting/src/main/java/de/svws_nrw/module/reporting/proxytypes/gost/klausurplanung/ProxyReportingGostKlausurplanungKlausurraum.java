package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.DTOManagerMapper;
import de.svws_nrw.db.dto.current.gost.klausurplanung.DTOGostKlausurenRaeume;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;


/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurraum und erweitert die Klasse {@link ReportingGostKlausurplanungKlausurraum}.</p>
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
public class ProxyReportingGostKlausurplanungKlausurraum extends GostKlausurraum {

	private final GostKlausurplanManager manager;
	private final ReportingGostKlausurplanungKlausurplan plan;

	/**
	 * Erstellt ein neues Reporting-Objekt.
	 * @param manager		der Manager
	 * @param plan
	 */
	public ProxyReportingGostKlausurplanungKlausurraum(final GostKlausurplanManager manager, final ReportingGostKlausurplanungKlausurplan plan) {
		this.manager = manager;
		this.plan = plan;
	}

	/**
	 * Der Klausurtermin, dem dieser Raum mit seinen Aufischten zugeordnet wurde.
	 * @return Inhalt des Feldes klausurtermin
	 */
	public ReportingGostKlausurplanungKlausurtermin getKlausurtermin() {
		return new ReportingGostKlausurplanungKlausurtermin((ProxyReportingGostKlausurplanungKlausurtermin) manager.terminGetByIdOrException(idTermin));
	}

	/**
	 * Die Kapazität des Raumes (in Bezug auf die Anzahl der Schülerplätze).
	 * @return Inhalt des Feldes kapazitaet
	 */
	public int getKapazitaet() {
		final StundenplanRaum raum = manager.stundenplanraumGetByKlausurraumOrNull(this);
		return raum == null ? -1 : raum.groesse;
	}

	/**
	 * Das Kürzel des Raumes.
	 * @return Inhalt des Feldes kuerzel
	 */
	public String getKuerzel() {
		final StundenplanRaum raum = manager.stundenplanraumGetByKlausurraumOrNull(this);
		return raum == null ? null : raum.kuerzel;
	}

	/**
	 * Die Beschreibung des Raumes.
	 * @return Inhalt des Feldes beschreibung
	 */
	public String getBeschreibung() {
		final StundenplanRaum raum = manager.stundenplanraumGetByKlausurraumOrNull(this);
		return raum == null ? null : raum.beschreibung;
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
	public static final DTOManagerMapper<GostKlausurraum, ProxyReportingGostKlausurplanungKlausurraum, GostKlausurplanManager, ReportingGostKlausurplanungKlausurplan> dtoMapper = (final GostKlausurraum z, final GostKlausurplanManager m, final ReportingGostKlausurplanungKlausurplan p) -> {
		final ProxyReportingGostKlausurplanungKlausurraum daten = new ProxyReportingGostKlausurplanungKlausurraum(m, p);
		daten.id = z.id;
		daten.bemerkung = z.bemerkung;
		daten.idStundenplanRaum = z.idStundenplanRaum;
		daten.idTermin = z.idTermin;
		return daten;
	};
}
