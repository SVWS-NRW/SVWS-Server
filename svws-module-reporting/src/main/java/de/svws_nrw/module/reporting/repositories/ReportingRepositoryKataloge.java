package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.data.schule.Telefonart;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.erzieher.DataErzieherarten;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.kataloge.DataOrte;
import de.svws_nrw.data.kataloge.DataOrtsteile;
import de.svws_nrw.data.schueler.DataKatalogSchuelerFoerderschwerpunkte;
import de.svws_nrw.data.schule.DataReligionen;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.data.schule.DataTelefonarten;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ProxyReportingErzieherArt;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.ws.rs.core.Response.Status;

/**
 * Domänen-Repository für Kataloge, Fächer, Jahrgänge und Erzieherarten.
 * Die Katalogdaten werden bei der Initialisierung vollständig aus der Datenbank geladen.
 */
public class ReportingRepositoryKataloge {

	private final ReportingContext reportingContext;

	private Map<Long, KatalogEntlassgrund> katalogEntlassgruende;
	private Map<Long, FoerderschwerpunktEintrag> katalogFoerderschwerpunkte;
	private Map<Long, OrtKatalogEintrag> katalogOrte;
	private Map<Long, OrtsteilKatalogEintrag> katalogOrtsteile;
	private Map<Long, ReligionEintrag> katalogReligionen;
	private Map<Long, SchulEintrag> katalogSchulen;
	private Map<Long, SchulformKatalogEintrag> katalogSchulformen;
	private Map<Long, Telefonart> katalogTelefonnummerArten;
	private Map<Long, DTOFach> mapFaecher;
	private Map<Long, JahrgangsDaten> mapJahrgaenge;
	private Map<Long, ReportingErzieherArt> mapErzieherarten;

	/**
	 * Erstellt ein neues ReportingKatalogRepository und initialisiert Kataloge, Fächer und Jahrgänge.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public ReportingRepositoryKataloge(final ReportingContext reportingContext) throws ApiOperationException {
		this.reportingContext = reportingContext;

		initKataloge();
		initFachdaten();
		initJahrgaenge();
	}

	private void initKataloge() throws ApiOperationException {
		try {
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Ermittle Katalogdaten.");

			this.katalogEntlassgruende =
					new DataKatalogEntlassgruende(this.reportingContext.conn()).getAll().stream().collect(Collectors.toMap(e -> e.id, e -> e));
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Katalog Entlassgründe geladen.");

			this.katalogFoerderschwerpunkte =
					new DataKatalogSchuelerFoerderschwerpunkte(this.reportingContext.conn()).getAll().stream().collect(Collectors.toMap(f -> f.id, f -> f));
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Katalog Förderschwerpunkte geladen.");

			final DataOrte dataOrte = new DataOrte(this.reportingContext.conn());
			this.katalogOrte = dataOrte.getAll().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Katalog Orte geladen.");

			this.katalogOrtsteile = new DataOrtsteile(this.reportingContext.conn(), dataOrte).getAll().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Katalog Ortsteile geladen.");

			this.katalogReligionen = new DataReligionen(this.reportingContext.conn()).getAll().stream().collect(Collectors.toMap(r -> r.id, r -> r));
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Katalog Religionen geladen.");

			this.katalogSchulen = new DataSchulen(this.reportingContext.conn()).getAll().stream().collect(Collectors.toMap(s -> s.id, s -> s));
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Katalog Schulen geladen.");

			final ArrayList<SchulformKatalogEintrag> schulformen = new ArrayList<>();
			for (final Schulform schulform : Schulform.values()) {
				schulformen.addAll(schulform.historie());
			}
			this.katalogSchulformen = schulformen.stream().collect(Collectors.toMap(sfke -> sfke.id, sfke -> sfke));
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Katalog Schulformen geladen.");

			this.katalogTelefonnummerArten = new DataTelefonarten(this.reportingContext.conn()).getAll().stream().collect(Collectors.toMap(ta -> ta.id, ta -> ta));
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Katalog TelefonnummerArten geladen.");

			this.mapErzieherarten = new DataErzieherarten(this.reportingContext.conn()).getAll().stream().collect(Collectors.toMap(a -> a.id,
					ProxyReportingErzieherArt::new));
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Liste der Erzieherarten geladen.");
		} catch (final Exception e) {
			this.reportingContext.logger().logLn(LogLevel.ERROR, 8, "FEHLER: Die Kataloge der Schule konnten nicht vollständig ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Kataloge der Schule konnten nicht vollständig ermittelt werden.");
		}
	}

	private void initFachdaten() throws ApiOperationException {
		try {
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Ermittle Fächer.");
			this.mapFaecher = this.reportingContext.conn().queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		} catch (final Exception e) {
			this.reportingContext.logger().logLn(LogLevel.ERROR, 8, "FEHLER: Die Fächer konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Daten der Fächer konnten nicht ermittelt werden.");
		}
	}

	private void initJahrgaenge() throws ApiOperationException {
		try {
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Ermittle die Jahrgangsdaten.");
			this.mapJahrgaenge = new DataJahrgangsdaten(this.reportingContext.conn()).getAll().stream().collect(Collectors.toMap(j -> j.id, j -> j));
		} catch (final Exception e) {
			this.reportingContext.logger().logLn(LogLevel.ERROR, 4, "FEHLER: Die Jahrgangsdaten konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Jahrgangsdaten. konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Gibt die Map der Entlassgründe zurück, indiziert nach ID.
	 *
	 * @return Map der Entlassgründe
	 */
	public Map<Long, KatalogEntlassgrund> entlassgruende() {
		return katalogEntlassgruende;
	}

	/**
	 * Gibt die Map der Förderschwerpunkt-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Förderschwerpunkt-Katalogeinträge
	 */
	public Map<Long, FoerderschwerpunktEintrag> foerderschwerpunkte() {
		return katalogFoerderschwerpunkte;
	}

	/**
	 * Gibt die Map der Ort-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Ort-Katalogeinträge
	 */
	public Map<Long, OrtKatalogEintrag> orte() {
		return katalogOrte;
	}

	/**
	 * Gibt die Map der Ortsteil-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Ortsteil-Katalogeinträge
	 */
	public Map<Long, OrtsteilKatalogEintrag> ortsteile() {
		return katalogOrtsteile;
	}

	/**
	 * Gibt die Map der Religions-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Religions-Katalogeinträge
	 */
	public Map<Long, ReligionEintrag> religionen() {
		return katalogReligionen;
	}

	/**
	 * Gibt die Map der Schul-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Schul-Katalogeinträge
	 */
	public Map<Long, SchulEintrag> schulen() {
		return katalogSchulen;
	}

	/**
	 * Gibt die Map der Schulform-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Schulform-Katalogeinträge
	 */
	public Map<Long, SchulformKatalogEintrag> schulformen() {
		return katalogSchulformen;
	}

	/**
	 * Gibt die Map der Telefonnummer-Arten zurück, indiziert nach ID.
	 *
	 * @return Map der Telefonnummer-Arten
	 */
	public Map<Long, Telefonart> telefonnummerArten() {
		return katalogTelefonnummerArten;
	}

	/**
	 * Gibt die Map der Fächer-DTOs zurück, indiziert nach der ID des Faches.
	 *
	 * @return Map der Fächer-DTOs
	 */
	public Map<Long, DTOFach> faecher() {
		return mapFaecher;
	}

	/**
	 * Gibt die Map der Jahrgangsdaten zurück, indiziert nach Jahrgangs-ID.
	 *
	 * @return Map der Jahrgangsdaten
	 */
	public Map<Long, JahrgangsDaten> jahrgaenge() {
		return mapJahrgaenge;
	}

	/**
	 * Gibt die Jahrgangsdaten zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank nachgeladen.
	 * Schlägt das Nachladen fehl, wird {@code null} zurückgegeben und im Cache als Negativ-Eintrag vermerkt.
	 *
	 * @param idJahrgang Die ID des Jahrgangs.
	 *
	 * @return Die Jahrgangsdaten oder {@code null}, falls der Jahrgang nicht ermittelt werden konnte.
	 */
	public JahrgangsDaten jahrgang(final long idJahrgang) {
		if (idJahrgang < 0) {
			return null;
		}
		if (mapJahrgaenge.containsKey(idJahrgang)) {
			return mapJahrgaenge.get(idJahrgang);
		}
		try {
			final JahrgangsDaten jahrgangsDaten = new DataJahrgangsdaten(this.reportingContext.conn()).getById(idJahrgang);
			mapJahrgaenge.put(idJahrgang, jahrgangsDaten);
			return jahrgangsDaten;
		} catch (final ApiOperationException e) {
			ReportingExceptionUtils.logException(
					"FEHLER: Fehler bei der Ermittlung der Jahrgangsdaten zur ID %d aus der Datenbank im ReportingContext.".formatted(idJahrgang),
					e, this.reportingContext.logger(), LogLevel.ERROR, 0);
			mapJahrgaenge.put(idJahrgang, null);
			return null;
		}
	}

	/**
	 * Gibt die Map der Erzieherarten als Reporting-Objekte zurück, indiziert nach ID.
	 *
	 * @return Map der Erzieherarten
	 */
	public Map<Long, ReportingErzieherArt> erzieherarten() {
		return mapErzieherarten;
	}
}
