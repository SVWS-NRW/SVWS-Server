package de.svws_nrw.module.reporting.proxytypes.schueler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.data.erzieher.DataErzieherStammdaten;
import de.svws_nrw.data.gost.DBUtilsGostAbitur;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schueler.DataSchuelerSprachbelegung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.schueler.erzieher.ProxyReportingErzieher;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchuelerDatenstatus;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieher;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArtGruppe;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.proxytypes.schueler.gost.abitur.ProxyReportingSchuelerGostAbitur;
import de.svws_nrw.module.reporting.proxytypes.schueler.gost.laufbahnplanung.ProxyReportingSchuelerGostLaufbahnplanung;
import de.svws_nrw.module.reporting.proxytypes.schueler.lernabschnitte.ProxyReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.proxytypes.schueler.sprachen.ProxyReportingSchuelerSprachbelegung;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.gost.abitur.ReportingSchuelerGostAbitur;
import de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung.ReportingSchuelerGostLaufbahnplanung;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schueler.sprachen.ReportingSchuelerSprachbelegung;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schüler und erweitert die Klasse {@link ReportingSchueler}.
 */
public class ProxyReportingSchueler extends ReportingSchueler {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchueler}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param schuelerStammdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchueler(final ReportingRepository reportingRepository, final SchuelerStammdaten schuelerStammdaten) {
		super(null,
				ersetzeNullBlankTrim(schuelerStammdaten.anmeldedatum),
				"",
				ersetzeNullBlankTrim(schuelerStammdaten.aufnahmedatum),
				null,
				EnumSet.noneOf(ReportingSchuelerDatenstatus.class),
				schuelerStammdaten.druckeKonfessionAufZeugnisse,
				ersetzeNullBlankTrim(schuelerStammdaten.emailPrivat),
				ersetzeNullBlankTrim(schuelerStammdaten.emailSchule),
				schuelerStammdaten.erhaeltMeisterBAFOEG,
				schuelerStammdaten.erhaeltSchuelerBAFOEG,
				new ArrayList<>(),
				new ArrayList<>(),
				ersetzeNullBlankTrim(schuelerStammdaten.externeSchulNr),
				schuelerStammdaten.fahrschuelerArtID,
				schuelerStammdaten.foto,
				ersetzeNullBlankTrim(schuelerStammdaten.geburtsdatum),
				ersetzeNullBlankTrim(schuelerStammdaten.geburtsland),
				ersetzeNullBlankTrim(schuelerStammdaten.geburtslandMutter),
				ersetzeNullBlankTrim(schuelerStammdaten.geburtslandVater),
				ersetzeNullBlankTrim(schuelerStammdaten.geburtsname),
				ersetzeNullBlankTrim(schuelerStammdaten.geburtsort),
				Geschlecht.fromValue(schuelerStammdaten.geschlecht),
				null,
				new ArrayList<>(),
				new ArrayList<>(),
				null,
				schuelerStammdaten.haltestelleID,
				schuelerStammdaten.hatMasernimpfnachweis,
				schuelerStammdaten.hatMigrationshintergrund,
				ersetzeNullBlankTrim(schuelerStammdaten.hausnummer),
				ersetzeNullBlankTrim(schuelerStammdaten.hausnummerZusatz),
				schuelerStammdaten.id,
				schuelerStammdaten.istBerufsschulpflichtErfuellt,
				schuelerStammdaten.istDuplikat,
				schuelerStammdaten.istSchulpflichtErfuellt,
				schuelerStammdaten.istVolljaehrig,
				schuelerStammdaten.keineAuskunftAnDritte,
				null,
				ersetzeNullBlankTrim(schuelerStammdaten.nachname),
				ersetzeNullBlankTrim(schuelerStammdaten.religionabmeldung),
				ersetzeNullBlankTrim(schuelerStammdaten.religionanmeldung),
				null,
				new ArrayList<>(),
				Nationalitaeten.getByDESTATIS(schuelerStammdaten.staatsangehoerigkeitID),
				Nationalitaeten.getByDESTATIS(schuelerStammdaten.staatsangehoerigkeit2ID),
				SchuelerStatus.data().getWertByKuerzel("" + schuelerStammdaten.status),
				ersetzeNullBlankTrim(schuelerStammdaten.strassenname),
				ersetzeNullBlankTrim(schuelerStammdaten.telefon),
				ersetzeNullBlankTrim(schuelerStammdaten.telefonMobil),
				"",
				ersetzeNullBlankTrim(schuelerStammdaten.verkehrspracheFamilie),
				ersetzeNullBlankTrim(schuelerStammdaten.vorname),
				ersetzeNullBlankTrim(schuelerStammdaten.alleVornamen),
				(schuelerStammdaten.wohnortID != null) ? reportingRepository.katalogOrte().get(schuelerStammdaten.wohnortID) : null,
				(schuelerStammdaten.ortsteilID != null) ? reportingRepository.katalogOrtsteile().get(schuelerStammdaten.ortsteilID) : null,
				schuelerStammdaten.zuzugsjahr);

		this.reportingRepository = reportingRepository;

		super.religion = this.reportingRepository.katalogReligionen().get(schuelerStammdaten.religionID);

		// Füge Stammdaten des Schülers für weitere Verwendung in der Map im Repository hinzu.
		this.reportingRepository.mapSchuelerStammdaten().put(super.id(), schuelerStammdaten);
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}


	/**
	 * Stellt die Daten des aktuellen Lernabschnitts des Schülers zur Verfügung.
	 *
	 * @return Daten des aktuellen Lernabschnitts, wenn dieser vorhanden ist.
	 */
	@Override
	public ReportingSchuelerLernabschnitt aktuellerLernabschnitt() {
		if (super.aktuellerLernabschnitt == null)
			lernabschnitte();
		return super.aktuellerLernabschnitt;
	}

	/**
	 * Stellt die Daten des ausgewählten Lernabschnitts des Schülers zur Verfügung.
	 *
	 * @return Daten des ausgewählten Lernabschnitts, wenn dieser vorhanden ist.
	 */
	@Override
	public ReportingSchuelerLernabschnitt auswahlLernabschnitt() {
		if (super.auswahlLernabschnitt == null)
			lernabschnitte();
		return super.auswahlLernabschnitt;
	}

	/**
	 * Stellt die Erzieher des Schülers zur Verfügung.
	 *
	 * @return Daten der Erzieher, wenn diese vorhanden sind.
	 */
	@Override
	public List<ReportingErzieher> erzieher() {
		getErzieher();
		return super.erzieher;
	}

	/**
	 * Stellt die Erzieher des Schülers gruppiert nach ihrer Art zur Verfügung.
	 *
	 * @return Daten der Erzieher, wenn diese vorhanden sind, gruppiert nach Erzieher-Art.
	 */
	@Override
	public List<ReportingErzieherArtGruppe> erzieherArtGruppen() {
		getErzieher();
		return super.erzieherArtGruppen;
	}

	/**
	 * Hilfsfunktion, die die Erzieherdaten ggf. nachlädt und die Erzieher ggf. gruppiert.
	 */
	private void getErzieher() {
		if (!super.datenstatus.contains(ReportingSchuelerDatenstatus.ERZIEHER)) {
			if (!this.reportingRepository.mapErzieherStammdaten().containsKey(this.id())) {
				try {
					// Keine passenden Erzieherdaten gefunden. Lade nun alle Erzieherdaten aus der DB zu Schülern, bei denen noch keine Erzieher vorhanden sind.
					final List<Long> idsSchuelerOhneErzieher = this.reportingRepository.mapSchueler().values().stream()
							.filter(s -> (!s.datenstatus().contains(ReportingSchuelerDatenstatus.ERZIEHER))).map(ReportingSchueler::id).toList();
					final List<ErzieherStammdaten> erzieherStammdaten =
							new DataErzieherStammdaten(this.reportingRepository.conn()).getFromIds(idsSchuelerOhneErzieher);
					if (!erzieherStammdaten.isEmpty())
						this.reportingRepository.mapErzieherStammdaten().putAll(erzieherStammdaten.stream().collect(Collectors.groupingBy(e -> e.idSchueler)));
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Erzieher der bereits eingelesenen Schülerstammdaten.",
							e, reportingRepository.logger(), LogLevel.INFO, 0);
					return;
				}
			}

			final List<ErzieherStammdaten> thisErzieherStammdaten = this.reportingRepository.mapErzieherStammdaten().get(this.id());
			super.erzieher = new ArrayList<>();
			if ((thisErzieherStammdaten == null) || thisErzieherStammdaten.isEmpty()) {
				return;
			}
			super.erzieher.addAll(thisErzieherStammdaten.stream().map(e -> new ProxyReportingErzieher(this.reportingRepository, e, this)).toList());

			// Erstelle die Gruppen der Erzieherarten.
			final List<ReportingErzieher> erzieherMitArt = super.erzieher.stream().filter(re -> re.art() != null).toList();
			final List<Long> idsArt = erzieherMitArt.stream().map(re -> re.art().id()).distinct().toList();

			for (final Long idArt : idsArt) {
				final List<ReportingErzieher> erzieherInGruppe = erzieherMitArt.stream().filter(re -> re.art().id() == idArt)
						.sorted(Comparator.comparing(ReportingErzieher::anrede)
								.thenComparing(ReportingErzieher::nachname)
								.thenComparing(ReportingErzieher::vorname))
						.toList();
				super.erzieherArtGruppen.add(new ReportingErzieherArtGruppe(erzieherInGruppe.getFirst().art().bezeichnung(), erzieherInGruppe, idArt, this,
						erzieherInGruppe.getFirst().art().sortierung()));
			}

			// Sonderfall, wenn bei Erziehern keine Art gesetzt wurde.
			final List<ReportingErzieher> erzieherInGruppe = super.erzieher.stream().filter(re -> re.art() == null)
					.sorted(Comparator.comparing(ReportingErzieher::anrede)
							.thenComparing(ReportingErzieher::nachname)
							.thenComparing(ReportingErzieher::vorname))
					.toList();
			if (!erzieherInGruppe.isEmpty())
				super.erzieherArtGruppen.add(new ReportingErzieherArtGruppe("---", erzieherInGruppe, -1, this, -1));

			super.erzieherArtGruppen.sort(Comparator.comparing(ReportingErzieherArtGruppe::sortierung).thenComparing(ReportingErzieherArtGruppe::bezeichnung));

			super.datenstatus.add(ReportingSchuelerDatenstatus.ERZIEHER);
		}
	}


	/**
	 * Stellt die Daten zum Abitur in der GOSt des Schülers zur Verfügung.
	 *
	 * @return Daten zum Abitur in der GOSt
	 */
	@Override
	public ReportingSchuelerGostAbitur gostAbitur() {
		if (super.gostAbitur() == null) {
			if (this.reportingRepository.mapGostSchuelerAbiturdaten().containsKey(this.id())) {
				super.gostAbitur =
						new ProxyReportingSchuelerGostAbitur(this.reportingRepository, this.reportingRepository.mapGostSchuelerAbiturdaten().get(this.id()));
			} else {
				try {
					final Abiturdaten abiturdaten = DBUtilsGostAbitur.getAbiturdaten(this.reportingRepository.conn(), this.id());
					super.gostAbitur = new ProxyReportingSchuelerGostAbitur(this.reportingRepository, abiturdaten);
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der GOSt-Abiturdaten eines Schülers.", e,
							reportingRepository.logger(), LogLevel.INFO, 0);
					return null;
				}
			}
		}
		return super.gostAbitur;
	}

	/**
	 * Stellt die Daten der GOSt-Laufbahnplanung des Schülers zur Verfügung.
	 *
	 * @return Daten der GOSt-Laufbahnplanung
	 */
	@Override
	public ReportingSchuelerGostLaufbahnplanung gostLaufbahnplanung() {
		if (super.gostLaufbahnplanung() == null) {
			super.gostLaufbahnplanung = new ProxyReportingSchuelerGostLaufbahnplanung(this.reportingRepository, this);
		}
		return super.gostLaufbahnplanung;
	}

	/**
	 * Stellt die Daten aller Lernabschnitte des Schülers in einer Liste zur Verfügung.
	 *
	 * @return Liste der Daten aller Lernabschnitte
	 */
	@Override
	public List<ReportingSchuelerLernabschnitt> lernabschnitte() {
		if (super.lernabschnitte == null) {
			if (!this.reportingRepository.mapAlleLernabschnittsdaten().containsKey1(this.id())) {
				// Wenn zum Schüler keine Lernabschnitte aus der DB gefunden wurden, dann müssen diese nachträglich geladen worden sein oder der Schüler hat keine
				// Lernabschnitte. Prüfe auf Differenzen und lade nach.
				getLernabschnitte();
			}

			// Die Lernabschnitte aller Schüler der Stammdatenabschnitte liegen nun vor. Filtere alle Lernabschnitte des Schülers heraus.
			final List<SchuelerLernabschnittsdaten> schuelerLernabschnittsdaten =
					new ArrayList<>(this.reportingRepository.mapAlleLernabschnittsdaten().get1(this.id()));

			// Wenn, wie bei einer Neuaufnahme, keine Lernabschnitte vorhanden sind, gebe die leere Liste zurück.
			if (schuelerLernabschnittsdaten.isEmpty()) {
				super.lernabschnitte = new ArrayList<>();
				super.aktuellerLernabschnitt = null;
				super.auswahlLernabschnitt = null;
				return super.lernabschnitte();
			}

			// Sortiere die Lernabschnitte dieses Schülers und fülle damit seine Liste von Lernabschnitten.
			super.lernabschnitte = schuelerLernabschnittsdaten.stream()
					.map(a -> (ReportingSchuelerLernabschnitt) new ProxyReportingSchuelerLernabschnitt(this.reportingRepository, a))
					.sorted(Comparator
							.comparing((final ReportingSchuelerLernabschnitt a) -> a.schuljahresabschnitt().schuljahr())
							.thenComparing((final ReportingSchuelerLernabschnitt a) -> a.schuljahresabschnitt().abschnitt())
							.thenComparing(ReportingSchuelerLernabschnitt::wechselNr))
					.toList();

			final List<SchuelerLernabschnittsdaten> aktuelleAbschnitte =
					this.reportingRepository.mapAlleLernabschnittsdaten().get123(super.id, this.reportingRepository.aktuellerSchuljahresabschnitt().id(), 0);
			if (!aktuelleAbschnitte.isEmpty())
				super.aktuellerLernabschnitt = new ProxyReportingSchuelerLernabschnitt(this.reportingRepository, aktuelleAbschnitte.getFirst());
			else
				super.aktuellerLernabschnitt = null;

			final List<SchuelerLernabschnittsdaten> auswahlAbschnitte =
					this.reportingRepository.mapAlleLernabschnittsdaten().get123(super.id, this.reportingRepository.auswahlSchuljahresabschnitt().id(), 0);
			if (!auswahlAbschnitte.isEmpty())
				super.auswahlLernabschnitt = new ProxyReportingSchuelerLernabschnitt(this.reportingRepository, auswahlAbschnitte.getFirst());
			else
				super.auswahlLernabschnitt = null;
		}
		return super.lernabschnitte;
	}

	private void getLernabschnitte() {
		final List<Long> idsSchuelerOhneLernabschnitte = new ArrayList<>(this.reportingRepository.mapSchueler().size());

		for (final long key : this.reportingRepository.mapSchueler().keySet())
			if (!this.reportingRepository.mapAlleLernabschnittsdaten().containsKey1(key))
				idsSchuelerOhneLernabschnitte.add(key);

		if (!idsSchuelerOhneLernabschnitte.isEmpty()) {
			final List<SchuelerLernabschnittsdaten> schuelerGesamteLernabschnittsdaten = new ArrayList<>();
			try {
				schuelerGesamteLernabschnittsdaten.addAll(new DataSchuelerLernabschnittsdaten(this.reportingRepository().conn())
						.getListFromSchuelerIDs(idsSchuelerOhneLernabschnitte, false, false));
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Lernabschnitte eines Schülers.", e,
						reportingRepository.logger(), LogLevel.INFO, 0);
			}
			// Lege die Lernabschnittsdaten in den entsprechenden Maps des Repositories ab.
			if (!schuelerGesamteLernabschnittsdaten.isEmpty()) {
				for (final SchuelerLernabschnittsdaten la : schuelerGesamteLernabschnittsdaten) {
					this.reportingRepository.mapAlleLernabschnittsdaten().add(la.schuelerID, la.schuljahresabschnitt, la.wechselNr, la.id, la);
				}
			} else {
				this.reportingRepository.mapAlleLernabschnittsdaten().addEmpty(super.id, -1, -1, -1);
			}
		}
	}


	/**
	 * Stellt die Daten aller Sprachbelegungen des Schülers in einer Liste zur Verfügung.
	 *
	 * @return Liste der Daten aller Sprachbelegungen
	 */
	@Override
	public List<ReportingSchuelerSprachbelegung> sprachbelegungen() {
		if (!super.datenstatus.contains(ReportingSchuelerDatenstatus.SPRACHBELEGUNGEN)) {
			if (!this.reportingRepository.mapSchuelerSprachbelegungen().containsKey(this.id())) {
				// Wenn zum Schüler keine Sprachbelegungen aus der DB gefunden wurden, dann müssen diese nachträglich geladen worden sein oder der Schüler hat keine
				// Sprachbelegungen. Prüfe auf Differenzen und lade nach.
				final List<Long> idsSchuelerOhneSprachbelegungen = new ArrayList<>(this.reportingRepository.mapSchueler().values().stream()
						.filter(s -> (!s.datenstatus().contains(ReportingSchuelerDatenstatus.SPRACHBELEGUNGEN))).map(ReportingSchueler::id).toList());
				if (!idsSchuelerOhneSprachbelegungen.isEmpty()) {
					this.reportingRepository.mapSchuelerSprachbelegungen()
							.putAll(DataSchuelerSprachbelegung.getMapBySchuelerIDs(this.reportingRepository.conn(), idsSchuelerOhneSprachbelegungen));
				}
			}
			if (this.reportingRepository.mapSchuelerSprachbelegungen().containsKey(this.id()))
				super.sprachbelegungen = this.reportingRepository.mapSchuelerSprachbelegungen().get(this.id()).stream()
						.map(sb -> ((ReportingSchuelerSprachbelegung) new ProxyReportingSchuelerSprachbelegung(reportingRepository, sb))).toList();
			else
				super.sprachbelegungen = new ArrayList<>();

			super.datenstatus.add(ReportingSchuelerDatenstatus.SPRACHBELEGUNGEN);
		}
		return super.sprachbelegungen();
	}
}
