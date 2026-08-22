package de.svws_nrw.module.reporting.types.schueler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.signing.SchulbescheinigungQrDaten;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ProxyReportingErzieher;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieher;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArtGruppe;
import de.svws_nrw.module.reporting.types.schueler.gost.abitur.ProxyReportingSchuelerGostAbitur;
import de.svws_nrw.module.reporting.types.schueler.gost.abitur.ReportingSchuelerGostAbitur;
import de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung.ProxyReportingSchuelerGostLaufbahnplanung;
import de.svws_nrw.module.reporting.types.schueler.gost.laufbahnplanung.ReportingSchuelerGostLaufbahnplanung;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schueler.schulbesuch.ProxyReportingSchuelerSchulbesuch;
import de.svws_nrw.module.reporting.types.schueler.schulbesuch.ReportingSchuelerSchulbesuch;
import de.svws_nrw.module.reporting.types.schueler.sprachen.ProxyReportingSchuelerSprachbelegung;
import de.svws_nrw.module.reporting.types.schueler.sprachen.ReportingSchuelerSprachbelegung;
import de.svws_nrw.module.reporting.types.schueler.telefon.ReportingSchuelerTelefonkontakt;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schüler und erweitert die Klasse {@link ReportingSchueler}.
 */
public class ProxyReportingSchueler extends ReportingSchueler {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchueler}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param schuelerStammdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchueler(final ReportingContext reportingContext, final SchuelerStammdaten schuelerStammdaten) {
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
				"",
				schuelerStammdaten.fahrschuelerArtID,
				schuelerStammdaten.foto,
				ersetzeNullBlankTrim(schuelerStammdaten.geburtsdatum),
				geburtslandBezeichnung(schuelerStammdaten.idGeburtsland),
				geburtslandBezeichnung(schuelerStammdaten.idGeburtslandMutter),
				geburtslandBezeichnung(schuelerStammdaten.idGeburtslandVater),
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
				Nationalitaeten.data().getWertByIDOrNull(schuelerStammdaten.idStaatsangehoerigkeit),
				Nationalitaeten.data().getWertByIDOrNull(schuelerStammdaten.idStaatsangehoerigkeit2),
				SchuelerStatus.data().getWertByKuerzel("" + schuelerStammdaten.status),
				ersetzeNullBlankTrim(schuelerStammdaten.strassenname),
				new ArrayList<>(),
				ersetzeNullBlankTrim(schuelerStammdaten.telefon),
				ersetzeNullBlankTrim(schuelerStammdaten.telefonMobil),
				"",
				(Verkehrssprache.data().getWertByIDOrNull(schuelerStammdaten.idVerkehrspracheFamilie) != null)
						? Verkehrssprache.data().getWertByIDOrNull(schuelerStammdaten.idVerkehrspracheFamilie).historie().getLast().iso3
						: "",
				ersetzeNullBlankTrim(schuelerStammdaten.vorname),
				ersetzeNullBlankTrim(schuelerStammdaten.alleVornamen),
				null,
				null,
				schuelerStammdaten.zuzugsjahr);

		this.reportingContext = reportingContext;

		super.religion = this.reportingContext.repositoryKataloge().religion(schuelerStammdaten.religionID);
		super.wohnort = this.reportingContext.repositoryKataloge().ort(schuelerStammdaten.wohnortID);
		super.wohnortsteil = this.reportingContext.repositoryKataloge().ortsteil(schuelerStammdaten.ortsteilID);

		// Registriere die Stammdaten des Schülers für die weitere Verwendung im Repository.
		this.reportingContext.repositorySchueler().registriereStammdaten(super.id(), schuelerStammdaten);
	}

	/**
	 * Ermittelt zur ID eines Geburtslandes dessen Bezeichnung aus dem Nationalitäten-Katalog.
	 *
	 * @param idGeburtsland Die ID des Geburtslandes oder null.
	 *
	 * @return Die Bezeichnung des Geburtslandes oder ein leerer String, wenn kein Eintrag vorliegt.
	 */
	private static String geburtslandBezeichnung(final Long idGeburtsland) {
		final Nationalitaeten nationalitaet = Nationalitaeten.data().getWertByIDOrNull(idGeburtsland);
		return (nationalitaet != null) ? nationalitaet.historie().getLast().bezeichnung : "";
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
	public ReportingContext reportingContext() {
		return this.reportingContext;
	}


	/**
	 * Stellt die Daten des aktuellen Lernabschnitts des Schülers zur Verfügung.
	 *
	 * @return Daten des aktuellen Lernabschnitts, wenn dieser vorhanden ist.
	 */
	@Override
	public ReportingSchuelerLernabschnitt aktuellerLernabschnitt() {
		if (super.aktuellerLernabschnitt == null) {
			lernabschnitte();
		}
		return super.aktuellerLernabschnitt;
	}

	/**
	 * Stellt die Daten des ausgewählten Lernabschnitts des Schülers zur Verfügung.
	 *
	 * @return Daten des ausgewählten Lernabschnitts, wenn dieser vorhanden ist.
	 */
	@Override
	public ReportingSchuelerLernabschnitt auswahlLernabschnitt() {
		if (super.auswahlLernabschnitt == null) {
			lernabschnitte();
		}
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
		if (!super.erzieher.isEmpty()) {
			return;
		}
		final List<ErzieherStammdaten> listeErzieherStammdaten = this.reportingContext.repositorySchueler().erzieherStammdaten(id());
		if (!listeErzieherStammdaten.isEmpty()) {
			super.erzieher.addAll(listeErzieherStammdaten.stream().map(e -> new ProxyReportingErzieher(this.reportingContext, e, this)).toList());
			erzieherGruppieren();
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
		if (!erzieherInGruppe.isEmpty()) {
			super.erzieherArtGruppen.add(new ReportingErzieherArtGruppe("---", erzieherInGruppe, -1, this, -1));
		}

		super.erzieherArtGruppen.sort(Comparator.comparing(ReportingErzieherArtGruppe::sortierung).thenComparing(ReportingErzieherArtGruppe::bezeichnung));
	}


	/**
	 * Das Kürzel der externen Schule bei einem externen Schüler, sofern dieses im Schulkatalog hinterlegt ist.
	 *
	 * @return Inhalt des Feldes externesSchulKuerzel; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	@Override
	public String externesSchulKuerzel() {
		if ((super.externeSchulNr() != null) && !super.externeSchulNr().isEmpty() && super.externesSchulKuerzel.isEmpty()) {
			final SchulEintrag schule = this.reportingContext.repositoryKataloge().schuleNachSchulnummer(super.externeSchulNr());
			if ((schule != null) && (schule.kuerzel != null) && !schule.kuerzel.isEmpty()) {
				super.externesSchulKuerzel = schule.kuerzel;
			} else {
				super.externesSchulKuerzel = "";
			}
		}
		return super.externesSchulKuerzel;
	}

	/**
	 * Stellt die Daten zum Abitur in der GOSt des Schülers zur Verfügung.
	 * <p>Der Rückgabewert {@code null} unterscheidet nicht, ob keine Abiturdaten vorliegen oder ob ihr Laden gescheitert ist. Auf dem Abitur-Datenaufbau ist
	 * beides vorab gedeckt: Die Auswahl meldet betroffene Schüler und lässt sie aus, bevor dieser Getter läuft; die Vorlagen prüfen zusätzlich auf
	 * {@code null}.</p>
	 *
	 * @return Daten zum Abitur in der GOSt; kann {@code null} sein, wenn zum Schüler keine Abiturdaten vorliegen.
	 */
	@Override
	public ReportingSchuelerGostAbitur gostAbitur() {
		if (super.gostAbitur == null) {
			final Abiturdaten abiturdaten = this.reportingContext.repositoryGost().schuelerAbiturdaten(id());
			if (abiturdaten == null) {
				return null;
			}
			super.gostAbitur = new ProxyReportingSchuelerGostAbitur(this.reportingContext, abiturdaten);
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
			super.gostLaufbahnplanung = new ProxyReportingSchuelerGostLaufbahnplanung(this.reportingContext, this);
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
			final List<SchuelerLernabschnittsdaten> schuelerLernabschnittsdaten =
					this.reportingContext.repositorySchueler().lernabschnitte(id());

			// Wenn, wie bei einer Neuaufnahme, keine Lernabschnitte vorhanden sind, so wird die leere Liste zurückgegeben.
			if (schuelerLernabschnittsdaten.isEmpty()) {
				super.setLernabschnitte(new ArrayList<>());
				super.aktuellerLernabschnitt = null;
				super.auswahlLernabschnitt = null;
				return super.lernabschnitte();
			}

			// Sortiere die Lernabschnitte dieses Schülers und fülle damit seine Liste von Lernabschnitten.
			super.setLernabschnitte(schuelerLernabschnittsdaten.stream()
					.map(a -> (ReportingSchuelerLernabschnitt) new ProxyReportingSchuelerLernabschnitt(this.reportingContext, a))
					.sorted(ReportingSchuelerLernabschnitt.SORTIERUNG.comparatorStandard())
					.toList());

			final List<SchuelerLernabschnittsdaten> aktuelleAbschnitte =
					this.reportingContext.repositorySchueler().lernabschnittsdaten().get123(super.id,
							this.reportingContext.repositorySchule().aktuellerSchuljahresabschnitt().id(), 0);
			if (!aktuelleAbschnitte.isEmpty()) {
				super.aktuellerLernabschnitt = new ProxyReportingSchuelerLernabschnitt(this.reportingContext, aktuelleAbschnitte.getFirst());
			} else {
				super.aktuellerLernabschnitt = null;
			}

			final List<SchuelerLernabschnittsdaten> auswahlAbschnitte =
					this.reportingContext.repositorySchueler().lernabschnittsdaten().get123(super.id,
							this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().id(), 0);
			if (!auswahlAbschnitte.isEmpty()) {
				super.auswahlLernabschnitt = new ProxyReportingSchuelerLernabschnitt(this.reportingContext, auswahlAbschnitte.getFirst());
			} else {
				super.auswahlLernabschnitt = null;
			}
		}
		return super.lernabschnitte();
	}


	/**
	 * Stellt die Daten des bisherigen und zukünftigen Schulbesuches des Schülers zur Verfügung.
	 *
	 * @return Daten zum Schulbesuch
	 */
	@Override
	public ReportingSchuelerSchulbesuch schulbesuch() {
		if (super.schulbesuch == null) {
			final SchuelerSchulbesuchsdaten schulbesuchsdaten = this.reportingContext.repositorySchueler().schulbesuchsdaten(id());
			if (schulbesuchsdaten != null) {
				super.schulbesuch = new ProxyReportingSchuelerSchulbesuch(this.reportingContext, schulbesuchsdaten);
			}
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
		if (super.sprachbelegungen.isEmpty()) {
			final List<Sprachbelegung> listeSprachbelegungen = this.reportingContext.repositorySchueler().sprachbelegungen(id());
			if (!listeSprachbelegungen.isEmpty()) {
				super.sprachbelegungen.addAll(listeSprachbelegungen.stream()
						.map(sb -> ((ReportingSchuelerSprachbelegung) new ProxyReportingSchuelerSprachbelegung(this.reportingContext, sb)))
						.sorted(ReportingSchuelerSprachbelegung.SORTIERUNG.comparatorStandard())
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
		if (super.telefonKontakte.isEmpty()) {
			final List<ReportingSchuelerTelefonkontakt> telefonkontakte = this.reportingContext.repositorySchueler().telefonkontakte(id());
			if (!telefonkontakte.isEmpty()) {
				super.telefonKontakte.addAll(telefonkontakte);
			}
		}
		return super.telefonKontakte;
	}

	/**
	 * Stellt die gerenderten QR-Codes der signierten Schulbescheinigung des Schülers zur Verfügung. Beim ersten Zugriff
	 * werden die Daten über das Repository in einem einzigen Signier-Batch für alle Schüler erzeugt und zwischengespeichert.
	 *
	 * @return Die QR-Daten der Schulbescheinigung.
	 */
	@Override
	public SchulbescheinigungQrDaten schulbescheinigungQrDaten() {
		if (super.schulbescheinigungQrDaten == null) {
			super.schulbescheinigungQrDaten = this.reportingContext.repositorySchueler().schulbescheinigungQrDaten(this.id());
		}
		return super.schulbescheinigungQrDaten;
	}


}
