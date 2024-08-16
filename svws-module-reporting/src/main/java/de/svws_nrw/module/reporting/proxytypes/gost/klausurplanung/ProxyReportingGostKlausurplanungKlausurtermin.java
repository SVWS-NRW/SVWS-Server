package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
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
	private List<ReportingGostKlausurplanungKursklausur> kursklausuren;
	private List<ReportingGostKlausurplanungSchuelerklausur> schuelerklausuren;
	private List<ReportingGostKlausurplanungKlausurraum> raeume;

	/**
	 * Erstellt ein neues Reporting-Objekt. Dabei werden Kurs- und Schülerklausuren dieses Termins und dessen Räume als LEERE Liste initialisiert.
	 * Das Hinzufügen dieser Elemente erfolgt in der Proxy-Klassen bei ihrer Erzeugung.
	 * @param manager	Der GostKlausurtermin mit den Daten zum Klausurtermin.
	 */
	public ProxyReportingGostKlausurplanungKlausurtermin(final GostKlausurplanManager manager) {
		this.manager = manager;
	}

	/**
	 * Die Liste von Kursklausuren zu diesem Klausurtermin.
	 * @return Inhalt des Feldes kursklausuren
	 */
	public List<ReportingGostKlausurplanungKursklausur> getKursklausuren() {
		if (kursklausuren == null) {
			kursklausuren = new ArrayList<>();
			for (final GostKursklausur kk : manager.kursklausurGetMengeByTermin(this))
				kursklausuren.add(new ReportingGostKlausurplanungKursklausur((ProxyReportingGostKlausurplanungKursklausur) kk));
		}
		return kursklausuren;
	}

	/**
	 * Die Liste der Schüler aus dem Kurs, die diese Klausur schreiben.
	 * @return Inhalt des Feldes klausurschreiber
	 */
	public List<ReportingGostKlausurplanungSchuelerklausur> getSchuelerklausuren() {
		if (schuelerklausuren == null) {
			schuelerklausuren = new ArrayList<>();
			for (final GostSchuelerklausur sk : manager.schuelerklausurGetMengeByTermin(this))
				schuelerklausuren.add(new ReportingGostKlausurplanungSchuelerklausur((ProxyReportingGostKlausurplanungSchuelerklausur) sk));
		}
		return schuelerklausuren;
	}

	/**
	 * Die Klausurräume dieses Termines, inkluse der Aufsichten für die Unterrichtsstunden der Klausur.
	 * @return Inhalt des Feldes klausurraeume
	 */
	public List<ReportingGostKlausurplanungKlausurraum> getKlausurraeume() {
		if (raeume == null) {
			raeume = new ArrayList<>();
			for (final GostKlausurraum sk : manager.raumGetMengeByTermin(this))
				raeume.add(new ReportingGostKlausurplanungKlausurraum((ProxyReportingGostKlausurplanungKlausurraum) sk));
		}
		return raeume;
	}

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another != null) && (another instanceof GostKlausurtermin) && (this.id == ((GostKlausurtermin) another).id);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOGostKlausurenRaeume} in einen Core-DTO
	 * {@link GostKlausurraum}.
	 */
	public static final DTOManagerMapper<GostKlausurtermin, ProxyReportingGostKlausurplanungKlausurtermin, GostKlausurplanManager> dtoMapper = (final GostKlausurtermin z, final GostKlausurplanManager m) -> {
		final ProxyReportingGostKlausurplanungKlausurtermin daten = new ProxyReportingGostKlausurplanungKlausurtermin(m);
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
