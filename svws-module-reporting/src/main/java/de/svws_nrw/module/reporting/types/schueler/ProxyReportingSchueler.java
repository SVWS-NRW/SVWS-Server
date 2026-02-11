package de.svws_nrw.module.reporting.types.schueler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.data.schueler.SchuelerTelefon;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.data.erzieher.DataErzieherStammdaten;
import de.svws_nrw.data.gost.DataGostAbiturdaten;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schueler.DataSchuelerSchulbesuchsdaten;
import de.svws_nrw.data.schueler.DataSchuelerSprachbelegung;
import de.svws_nrw.data.schueler.DataSchuelerTelefon;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ProxyReportingErzieher;
import de.svws_nrw.module.reporting.types.schueler.schulbesuch.ProxyReportingSchuelerSchulbesuch;
import de.svws_nrw.module.reporting.types.schueler.telefon.ProxyReportingSchuelerTelefonkontakt;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieher;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArtGruppe;
import de.svws_nrw.module.reporting.types.schueler.schulbesuch.ReportingSchuelerSchulbesuch;
import de.svws_nrw.module.reporting.types.schueler.telefon.ReportingSchuelerTelefonkontakt;
import de.svws_nrw.module.reporting.utils.ReportingDataLoadException;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.types.schueler.gost.abitur.ProxyReportingSchuelerGostAbitur;
import de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung.ProxyReportingSchuelerGostLaufbahnplanung;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schueler.sprachen.ProxyReportingSchuelerSprachbelegung;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
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
				null,
				new ArrayList<>(),
				getNationalitaet(schuelerStammdaten.staatsangehoerigkeitID),
				getNationalitaet(schuelerStammdaten.staatsangehoerigkeit2ID),
				SchuelerStatus.data().getWertByKuerzel("" + schuelerStammdaten.status),
				ersetzeNullBlankTrim(schuelerStammdaten.strassenname),
				new ArrayList<>(),
				ersetzeNullBlankTrim(schuelerStammdaten.telefon),
				ersetzeNullBlankTrim(schuelerStammdaten.telefonMobil),
				"",
				ersetzeNullBlankTrim(schuelerStammdaten.verkehrspracheFamilie),
				ersetzeNullBlankTrim(schuelerStammdaten.vorname),
				ersetzeNullBlankTrim(schuelerStammdaten.alleVornamen),
				null,
				null,
				schuelerStammdaten.zuzugsjahr);

		this.reportingRepository = reportingRepository;

		super.religion = (schuelerStammdaten.religionID != null) ? this.reportingRepository.katalogReligionen().get(schuelerStammdaten.religionID) : null;
		super.wohnort = (schuelerStammdaten.wohnortID != null) ? this.reportingRepository.katalogOrte().get(schuelerStammdaten.wohnortID) : null;
		super.wohnortsteil = (schuelerStammdaten.ortsteilID != null) ? this.reportingRepository.katalogOrtsteile().get(schuelerStammdaten.ortsteilID) : null;

		// Füge Stammdaten des Schülers für weitere Verwendung in der Map im Repository hinzu.
		this.reportingRepository.mapSchuelerStammdaten().put(super.id(), schuelerStammdaten);
	}


	private static Nationalitaeten getNationalitaet(final String nationalitaetId) {
		if ((nationalitaetId == null) || nationalitaetId.isBlank())
			return null;

		Nationalitaeten result = Nationalitaeten.getByISO3(nationalitaetId);
		if (result != null)
			return result;

		result = Nationalitaeten.getByISO2(nationalitaetId);
		if (result != null)
			return result;

		result = Nationalitaeten.getByDESTATIS(nationalitaetId);

		return result;
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
	 * Eine Hilfsfunktion, die die Erzieher-Daten ggf. nachlädt und die Erzieher ggf. gruppiert.
	 */
	private void getErzieher() {
		ladeListeInRepositoryMap(
				this.reportingRepository.mapErzieherStammdaten(),
				ids -> {
					try {
						final List<ErzieherStammdaten> stammdaten = new DataErzieherStammdaten(this.reportingRepository.conn()).getListBySchuelerIds(ids);
						return stammdaten.stream().collect(Collectors.groupingBy(e -> e.idSchueler));
					} catch (final ApiOperationException e) {
						throw new ReportingDataLoadException(e);
					}
				},
				"Erziehern");

		// Übertrage Daten aus dem Repository
		if (super.erzieher.isEmpty()) {
			final List<ErzieherStammdaten> thisErzieherStammdaten = this.reportingRepository.mapErzieherStammdaten().get(this.id());

			if ((thisErzieherStammdaten != null) && !thisErzieherStammdaten.isEmpty()) {
				super.erzieher.addAll(thisErzieherStammdaten.stream().map(e -> new ProxyReportingErzieher(this.reportingRepository, e, this)).toList());
				erzieherGruppieren();
			}
		}
	}

	/**
	 * Erstellt die Gruppen der Erzieher-Arten aus der bereits gefüllten Erzieher-Liste.
	 */
	private void erzieherGruppieren() {
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
	}


	/**
	 * Stellt die Daten zum Abitur in der GOSt des Schülers zur Verfügung.
	 *
	 * @return Daten zum Abitur in der GOSt
	 */
	@Override
	public ReportingSchuelerGostAbitur gostAbitur() {
		if (super.gostAbitur == null) {
			ladeObjektInRepositoryMap(
					this.reportingRepository.mapGostSchuelerAbiturdaten(),
					ids -> {
						try {
							return new DataGostAbiturdaten(this.reportingRepository.conn(), null).getMapAbiturdatenFromIDs(ids);
						} catch (final ApiOperationException e) {
							throw new ReportingDataLoadException(e);
						}
					},
					"GOSt-Abiturdaten");

			// Übertrage Daten aus dem Repository
			final Abiturdaten repoDaten = this.reportingRepository.mapGostSchuelerAbiturdaten().get(this.id());
			if (repoDaten == null)
				return null;

			super.gostAbitur = new ProxyReportingSchuelerGostAbitur(this.reportingRepository, repoDaten);
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
		if (super.lernabschnitte() == null) {
			if (!this.reportingRepository.mapAlleLernabschnittsdaten().containsKey1(this.id())) {
				// Wenn keine Lernabschnitte zum Schüler aus der DB gefunden wurden, müssen diese nachträglich geladen worden sein oder der Schüler hat keine
				// Lernabschnitte. Prüfe auf Differenzen und lade nach.
				getLernabschnitte();
			}

			// Die Lernabschnitte aller Schüler der Stammdatenabschnitte liegen nun vor. Filtere alle Lernabschnitte des Schülers heraus.
			final List<SchuelerLernabschnittsdaten> schuelerLernabschnittsdaten =
					new ArrayList<>(this.reportingRepository.mapAlleLernabschnittsdaten().get1(this.id()));

			// Wenn, wie bei einer Neuaufnahme, keine Lernabschnitte vorhanden sind, so wird die leere Liste zurückgegeben.
			if (schuelerLernabschnittsdaten.isEmpty()) {
				super.setLernabschnitte(new ArrayList<>());
				super.aktuellerLernabschnitt = null;
				super.auswahlLernabschnitt = null;
				return super.lernabschnitte();
			}

			// Sortiere die Lernabschnitte dieses Schülers und fülle damit seine Liste von Lernabschnitten.
			super.setLernabschnitte(schuelerLernabschnittsdaten.stream()
					.map(a -> (ReportingSchuelerLernabschnitt) new ProxyReportingSchuelerLernabschnitt(this.reportingRepository, a))
					.sorted(Comparator
							.comparing((final ReportingSchuelerLernabschnitt a) -> a.schuljahresabschnitt().schuljahr())
							.thenComparing((final ReportingSchuelerLernabschnitt a) -> a.schuljahresabschnitt().abschnitt())
							.thenComparing(ReportingSchuelerLernabschnitt::wechselNr))
					.toList());

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
		return super.lernabschnitte();
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
				ReportingExceptionUtils.logException(
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
	 * Stellt die Daten des bisherigen und zukünftigen Schulbesuches des Schülers zur Verfügung.
	 *
	 * @return Daten zum Schulbesuch
	 */
	@Override
	public ReportingSchuelerSchulbesuch schulbesuch() {
		if (super.schulbesuch == null) {
			ladeObjektInRepositoryMap(
					this.reportingRepository.mapSchuelerSchulbesuchsdaten(),
					ids -> {
						try {
							return new DataSchuelerSchulbesuchsdaten(this.reportingRepository.conn())
									.getListByIds(ids).stream()
									.collect(Collectors.toMap(sb -> sb.id, sb -> sb));
						} catch (final ApiOperationException e) {
							throw new ReportingDataLoadException(e);
						}
					},
					"Schulbesuchsdaten");

			if (this.reportingRepository.mapSchuelerSchulbesuchsdaten().containsKey(this.id()))
				super.schulbesuch = new ProxyReportingSchuelerSchulbesuch(reportingRepository,
						this.reportingRepository.mapSchuelerSchulbesuchsdaten().get(this.id()));
		}
		return super.schulbesuch;
	}


	/**
	 * Stellt die Daten aller Sprachbelegungen des Schülers in einer Liste zur Verfügung.
	 *
	 * @return Liste der Daten aller Sprachbelegungen
	 */
	@Override
	public List<ReportingSchuelerSprachbelegung> sprachbelegungen() {
		ladeListeInRepositoryMap(
				this.reportingRepository.mapSchuelerSprachbelegungen(),
				ids -> DataSchuelerSprachbelegung.getMapBySchuelerIDs(this.reportingRepository.conn(), ids),
				"Sprachbelegungen");

		// Übertrage Daten aus dem Repository
		if (super.sprachbelegungen.isEmpty()) {
			final List<Sprachbelegung> repoDaten = this.reportingRepository.mapSchuelerSprachbelegungen().get(this.id());
			if ((repoDaten != null) && !repoDaten.isEmpty()) {
				super.sprachbelegungen.addAll(repoDaten.stream()
						.map(sb -> ((ReportingSchuelerSprachbelegung) new ProxyReportingSchuelerSprachbelegung(reportingRepository, sb)))
						.sorted(Comparator.comparing(ReportingSchuelerSprachbelegung::belegungVonJahrgang, Comparator.nullsLast(String::compareTo))
								.thenComparing(ReportingSchuelerSprachbelegung::reihenfolge, Comparator.nullsLast(Integer::compareTo)))
						.toList());
			}
		}
		return super.sprachbelegungen();
	}

	/**
	 * Stellt die verfügbaren Telefonkontakte eines Schülers bereit.
	 *
	 * @return Eine Liste von {@link ReportingSchuelerTelefonkontakt}, die die Telefonkontakte des Schülers enthält.
	 */
	@Override
	public List<ReportingSchuelerTelefonkontakt> telefonKontakte() {
		ladeListeInRepositoryMap(
				this.reportingRepository.mapSchuelerTelefonkontakte(),
				ids -> {
					try {
						final List<SchuelerTelefon> schuelerTelefone =
								new DataSchuelerTelefon(this.reportingRepository.conn()).getListFromSchuelerIDs(ids);
						return schuelerTelefone.stream()
								.collect(Collectors.groupingBy(
										dto -> dto.idSchueler,
										Collectors.collectingAndThen(
												Collectors.mapping(
														t -> (ReportingSchuelerTelefonkontakt) new ProxyReportingSchuelerTelefonkontakt(
																this.reportingRepository, t),
														Collectors.toList()),
												list -> list.stream()
														.sorted(Comparator.comparing(ReportingSchuelerTelefonkontakt::sortierung))
														.toList())));
					} catch (final Exception e) {
						throw new ReportingDataLoadException(e);
					}
				},
				"Telefonkontakten");

		// Sync local
		if (super.telefonKontakte.isEmpty()) {
			final List<ReportingSchuelerTelefonkontakt> repoDaten = this.reportingRepository.mapSchuelerTelefonkontakte().get(this.id());
			if ((repoDaten != null) && !repoDaten.isEmpty()) {
				super.telefonKontakte.addAll(repoDaten);
			}
		}
		return super.telefonKontakte;
	}


	// ##### Generische Hilfsmethode für Lazy-Loading mit Bulk-Load und Single-Fallback #####

	/**
	 * Generische Methode zum Laden von Listen mit Daten in eine Map des Repositories als gesammeltes Laden mit Fallback.
	 * Prüft, ob für den aktuellen Schüler bereits Daten im Repository vorhanden sind.
	 * Falls nicht, werden die IDs aller Schüler ermittelt, deren Daten noch nicht im Repository sind, und gesammelt nachgeladen.
	 * Schlägt dies fehl, wird nur für den aktuellen Schüler versucht, die Daten zu laden.
	 * In jedem Fall wird sichergestellt, dass nach dem Aufruf ein Eintrag in der Map existiert (ggf. eine leere Liste).
	 *
	 * @param <T>                      Der Typ der Daten in der Map.
	 * @param repositoryMap            Die Map aus dem Repository (Key: Schüler-ID, Value: Liste der Daten).
	 * @param funktionGesammeltesLaden Die Funktion, die eine Liste von Schüler-IDs entgegennimmt und eine Map mit Ergebnissen zurückgibt.
	 * @param datenbezeichnung         Eine Bezeichnung der Daten für Log-Meldungen (z. B. "Erzieher", "Sprachbelegungen").
	 */
	private <T> void ladeListeInRepositoryMap(final Map<Long, List<T>> repositoryMap, final Function<List<Long>, Map<Long, List<T>>> funktionGesammeltesLaden,
			final String datenbezeichnung) {
		if (repositoryMap.containsKey(this.id()))
			return;

		final List<Long> fehlendeIds = this.reportingRepository.mapSchueler().keySet().stream()
				.filter(id -> !repositoryMap.containsKey(id))
				.toList();

		if (fehlendeIds.isEmpty()) {
			repositoryMap.putIfAbsent(this.id(), new ArrayList<>());
			return;
		}

		try {
			final Map<Long, List<T>> geladeneDaten = funktionGesammeltesLaden.apply(fehlendeIds);
			repositoryMap.putAll(geladeneDaten);
			// Leere Listen für alle angefragten IDs ohne Treffer
			for (final Long id : fehlendeIds)
				repositoryMap.putIfAbsent(id, new ArrayList<>());
		} catch (final Exception e) {
			// Gesammeltes Laden fehlgeschlagen – Fallback: einzelnes Laden für aktuellen Schüler
			ReportingExceptionUtils.logException(
					"INFO: Fehler beim gesammelten Laden von " + datenbezeichnung + ". Versuche einzelnes Laden für Schüler ID " + this.id(),
					e, reportingRepository.logger(), LogLevel.INFO, 0);
			try {
				final Map<Long, List<T>> singleResult = funktionGesammeltesLaden.apply(List.of(this.id()));
				final List<T> daten = singleResult.get(this.id());
				repositoryMap.put(this.id(), (daten != null) ? daten : new ArrayList<>());
			} catch (final Exception exSingle) {
				ReportingExceptionUtils.logException(
						"INFO: Fehler beim einzelnen Laden von " + datenbezeichnung + " für Schüler ID " + this.id() + ". Setze leere Liste.",
						exSingle, reportingRepository.logger(), LogLevel.INFO, 0);
				repositoryMap.put(this.id(), new ArrayList<>());
			}
		}
	}

	/**
	 * Generische Methode zum Laden von Einzelobjekten in eine Map des Repositories als gesammeltes Laden mit Fallback.
	 * Prüft, ob für den aktuellen Schüler bereits Daten im Repository vorhanden sind.
	 * Falls nicht, werden die IDs aller Schüler ermittelt, deren Daten noch nicht im Repository sind, und gesammelt nachgeladen.
	 * Schlägt dies fehl, wird nur für den aktuellen Schüler versucht, die Daten zu laden.
	 * In jedem Fall wird sichergestellt, dass nach dem Aufruf ein Eintrag in der Map existiert.
	 *
	 * @param <T>                      Der Typ der Daten in der Map.
	 * @param repositoryMap            Die Map aus dem Repository (Key: Schüler-ID, Value: Einzelobjekt).
	 * @param funktionGesammeltesLaden Die Funktion, die eine Liste von Schüler-IDs entgegennimmt und eine Map mit Ergebnissen zurückgibt.
	 * @param datenbezeichnung         Eine Bezeichnung der Daten für Log-Meldungen (z. B. "Schulbesuchsdaten").
	 */
	private <T> void ladeObjektInRepositoryMap(final Map<Long, T> repositoryMap, final Function<List<Long>, Map<Long, T>> funktionGesammeltesLaden,
			final String datenbezeichnung) {
		if (repositoryMap.containsKey(this.id()))
			return;

		final List<Long> fehlendeIds = this.reportingRepository.mapSchueler().keySet().stream()
				.filter(id -> !repositoryMap.containsKey(id))
				.toList();

		if (fehlendeIds.isEmpty())
			return;

		try {
			final Map<Long, T> geladeneDaten = funktionGesammeltesLaden.apply(fehlendeIds);
			repositoryMap.putAll(geladeneDaten);
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"INFO: Fehler beim gesammelten Laden von " + datenbezeichnung + ". Versuche einzelnes Laden für Schüler ID " + this.id(),
					e, reportingRepository.logger(), LogLevel.INFO, 0);
			try {
				final Map<Long, T> singleResult = funktionGesammeltesLaden.apply(List.of(this.id()));
				repositoryMap.putAll(singleResult);
			} catch (final Exception exSingle) {
				ReportingExceptionUtils.logException(
						"INFO: Fehler beim einzelnen Laden von " + datenbezeichnung + " für Schüler ID " + this.id() + ".",
						exSingle, reportingRepository.logger(), LogLevel.INFO, 0);
				repositoryMap.put(this.id(), null);
			}
		}
	}

}
