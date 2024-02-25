package de.svws_nrw.module.reporting.proxytypes.schueler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.data.gost.DBUtilsGostAbitur;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.module.reporting.proxytypes.schueler.gost.abitur.ProxyReportingSchuelerGostAbitur;
import de.svws_nrw.module.reporting.proxytypes.schueler.gost.laufbahnplanung.ProxyReportingSchuelerGostLaufbahnplanung;
import de.svws_nrw.module.reporting.proxytypes.schueler.lernabschnitte.ProxyReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.gost.abitur.ReportingSchuelerGostAbitur;
import de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung.ReportingSchuelerGostLaufbahnplanung;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import jakarta.ws.rs.WebApplicationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schüler und erweitert die Klasse {@link ReportingSchueler}.</p>
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
 * 		<li>Die Proxy-Klasse können zudem auf das Repository {@link ReportingRepository} zugreifen. Dieses
 * 			enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 * 			wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 * 			Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingSchueler extends ReportingSchueler {

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis eines Stammdaten-Objektes.
	 * @param reportingRepository Repository für die Reporting.
	 * @param schuelerStammdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchueler(final ReportingRepository reportingRepository, final SchuelerStammdaten schuelerStammdaten) {
		super(null,
			schuelerStammdaten.anmeldedatum,
			schuelerStammdaten.aufnahmedatum,
			schuelerStammdaten.bemerkungen,
			schuelerStammdaten.druckeKonfessionAufZeugnisse,
			schuelerStammdaten.emailPrivat,
			schuelerStammdaten.emailSchule,
			schuelerStammdaten.erhaeltMeisterBAFOEG,
			schuelerStammdaten.erhaeltSchuelerBAFOEG,
			schuelerStammdaten.externeSchulNr,
			schuelerStammdaten.fahrschuelerArtID,
			schuelerStammdaten.foto,
			schuelerStammdaten.geburtsdatum,
			schuelerStammdaten.geburtsland,
			schuelerStammdaten.geburtslandMutter,
			schuelerStammdaten.geburtslandVater,
			schuelerStammdaten.geburtsname,
			schuelerStammdaten.geburtsort,
			Geschlecht.fromValue(schuelerStammdaten.geschlecht),
			null,
			new ArrayList<>(),
			null,
			schuelerStammdaten.haltestelleID,
			schuelerStammdaten.hatMasernimpfnachweis,
			schuelerStammdaten.hatMigrationshintergrund,
			schuelerStammdaten.hausnummer,
			schuelerStammdaten.hausnummerZusatz,
			schuelerStammdaten.id,
			schuelerStammdaten.istBerufsschulpflichtErfuellt,
			schuelerStammdaten.istDuplikat,
			schuelerStammdaten.istSchulpflichtErfuellt,
			schuelerStammdaten.istVolljaehrig,
			schuelerStammdaten.keineAuskunftAnDritte,
			new ArrayList<>(),
			schuelerStammdaten.nachname,
			schuelerStammdaten.religionabmeldung,
			schuelerStammdaten.religionanmeldung,
			null,
			Nationalitaeten.getByDESTATIS(schuelerStammdaten.staatsangehoerigkeitID),
			Nationalitaeten.getByDESTATIS(schuelerStammdaten.staatsangehoerigkeit2ID),
			SchuelerStatus.fromID(schuelerStammdaten.status),
			schuelerStammdaten.strassenname,
			schuelerStammdaten.telefon,
			schuelerStammdaten.telefonMobil,
			schuelerStammdaten.verkehrspracheFamilie,
			schuelerStammdaten.vorname,
			schuelerStammdaten.alleVornamen,
			schuelerStammdaten.wohnortID != null ? reportingRepository.katalogOrte().get(schuelerStammdaten.wohnortID) : null,
			"",
			schuelerStammdaten.ortsteilID != null ? reportingRepository.katalogOrtsteile().get(schuelerStammdaten.ortsteilID) : null,
			"",
			schuelerStammdaten.zuzugsjahr);

		this.reportingRepository = reportingRepository;
		super.setReligion(this.reportingRepository.katalogReligionen().get(schuelerStammdaten.religionID));
		super.setWohnortname(super.wohnort() != null ? super.wohnort().ortsname : "");
		super.setWohnortsteilname(super.wohnortsteil() != null ? super.wohnortsteil().ortsteil : "");

		// Füge Stammdaten des Schülers für weitere Verwendung in der Map im Repository hinzu.
		reportingRepository.mapSchuelerStammdaten().putIfAbsent(super.id(), schuelerStammdaten);
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepositorySchule() {
		return reportingRepository;
	}



	/**
	 * Stellt die Daten des aktuellen Lernabschnitts des Schülers zur Verfügung.
	 * @return Daten des aktuellen Lernabschnitts, wenn dieser vorhanden ist.
	 */
	@Override
	public ReportingSchuelerLernabschnitt aktuellerLernabschnitt() {
		if (super.aktuellerLernabschnitt() == null) {
			if (this.reportingRepository.mapAktuelleLernabschnittsdaten().containsKey(this.id())) {
				super.setAktuellerLernabschnitt(new ProxyReportingSchuelerLernabschnitt(this.reportingRepository, this.reportingRepository.mapAktuelleLernabschnittsdaten().get(this.id())));
			} else {
				if (!lernabschnitte().isEmpty())
					if (lernabschnitte().size() == 1) {
						super.setAktuellerLernabschnitt(lernabschnitte().getFirst());
					} else {
						super.setAktuellerLernabschnitt(lernabschnitte().stream()
							.filter(a -> a.wechselNr() == 0)
							.filter(a -> a.schuljahresabschnitt().id() == this.reportingRepository.aktuellerSchuljahresabschnitt().id).toList()
							.getFirst());
					}
			}
		}
		return super.aktuellerLernabschnitt();
	}

	/**
	 * Stellt die Daten zum Abitur in der GOSt des Schülers zur Verfügung.
	 * @return Daten zum Abitur in der GOSt
	 */
	@Override
	public ReportingSchuelerGostAbitur gostAbitur() {
		if (super.gostAbitur() == null) {
			if (this.reportingRepository.mapGostSchuelerAbiturdaten().containsKey(this.id())) {
				super.setGostAbitur(new ProxyReportingSchuelerGostAbitur(this.reportingRepository, this.reportingRepository.mapGostSchuelerAbiturdaten().get(this.id())));
			} else {
				try {
					final Abiturdaten abiturdaten = DBUtilsGostAbitur.getAbiturdaten(this.reportingRepository.conn(), this.id());
					super.setGostAbitur(new ProxyReportingSchuelerGostAbitur(this.reportingRepository, abiturdaten));
				} catch (WebApplicationException ex) {
					throw OperationError.NOT_FOUND.exception("Es wurde eine Schüler-ID übergeben, für die keine Abiturdaten in der GOSt existieren.");
				}
			}
		}
		return super.gostAbitur();
	}

	/**
	 * Stellt die Daten der GOSt-Laufbahnplanung des Schülers zur Verfügung.
	 * @return Daten der GOSt-Laufbahnplanung
	 */
	@Override
	public ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung() {
		if (super.gostLaufbahnplanung() == null) {
			super.setGostLaufbahnplanung(new ProxyReportingSchuelerGostLaufbahnplanung(this.reportingRepository, this));
		}
		return super.gostLaufbahnplanung();
	}

	/**
	 * Stellt die Daten aller Lernabschnitte des Schülers in einer Liste zur Verfügung.
	 * @return Liste der Daten aller Lernabschnitte
	 */
	@Override
	public List<ReportingSchuelerLernabschnitt> lernabschnitte() {
		if (super.lernabschnitte().isEmpty()) {
			final List<SchuelerLernabschnittsdaten> schuelerLernabschnittsdaten = new DataSchuelerLernabschnittsdaten(this.reportingRepositorySchule().conn()).getListFromSchuelerIDs(new ArrayList<>(List.of(this.id())), true);
			// Wenn, wie bei einer Neuaufnahme, keine Lernabschnitte vorhanden sind, gebe die leere Liste zurück.
			if (schuelerLernabschnittsdaten.isEmpty())
				return super.lernabschnitte();
			super.setLernabschnitte(schuelerLernabschnittsdaten.stream()
				.map(a -> (ReportingSchuelerLernabschnitt) new ProxyReportingSchuelerLernabschnitt(this.reportingRepository, a))
				.sorted(Comparator
					.comparing((ReportingSchuelerLernabschnitt a) -> a.schuljahresabschnitt().schuljahr())
					.thenComparing((ReportingSchuelerLernabschnitt a) -> a.schuljahresabschnitt().abschnitt())
					.thenComparing(ReportingSchuelerLernabschnitt::wechselNr))
				.toList());
			// Aktuellen Lernabschnitt in der Map im Repository speichern. Gibt es nur einen Abschnitt, dann wird dieser gespeichert, andernfalls der mit WechselNr. 0 im aktuellen Schuljahresabschnitt.
			if (schuelerLernabschnittsdaten.size() == 1) {
				this.reportingRepository.mapAktuelleLernabschnittsdaten()
					.computeIfAbsent(super.id(), l -> schuelerLernabschnittsdaten.getFirst());
			} else {
				this.reportingRepository.mapAktuelleLernabschnittsdaten()
					.computeIfAbsent(super.id(), l -> schuelerLernabschnittsdaten.stream()
						.filter(a -> a.wechselNr == 0)
						.filter(a -> a.schuljahresabschnitt == this.reportingRepository.aktuellerSchuljahresabschnitt().id)
						.toList()
						.getFirst());
			}
		}
		return super.lernabschnitte();
	}
}
