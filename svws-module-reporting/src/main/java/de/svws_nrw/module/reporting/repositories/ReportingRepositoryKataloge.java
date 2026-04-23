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
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.erzieher.DataErzieherarten;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.kataloge.DataOrte;
import de.svws_nrw.data.kataloge.DataOrtsteile;
import de.svws_nrw.data.schueler.DataKatalogSchuelerFoerderschwerpunkte;
import de.svws_nrw.data.schule.DataReligionen;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.data.schule.DataTelefonarten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ProxyReportingErzieherArt;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArt;
import jakarta.ws.rs.core.Response.Status;

/**
 * Domänen-Repository für Kataloge, Fächer, Jahrgänge und Erzieherarten.
 * Die Katalogdaten werden bei der Initialisierung vollständig aus der Datenbank geladen.
 */
public class ReportingRepositoryKataloge {

	private final DBEntityManager conn;
	private final Logger logger;

	private Map<Long, KatalogEntlassgrund> katalogEntlassgruende;
	private Map<Long, FoerderschwerpunktEintrag> katalogFoerderschwerpunkte;
	private Map<Long, OrtKatalogEintrag> katalogOrte;
	private Map<Long, OrtsteilKatalogEintrag> katalogOrtsteile;
	private Map<Long, ReligionEintrag> katalogReligionen;
	private Map<Long, SchulEintrag> katalogSchulen;
	private Map<Long, SchulformKatalogEintrag> katalogSchulformen;
	private Map<Long, Telefonart> katalogTelefonnummerArten;
	private Map<Long, DTOFach> mapFachdaten;
	private Map<Long, JahrgangsDaten> mapJahrgaenge;
	private Map<Long, ReportingErzieherArt> mapErzieherarten;

	/**
	 * Erstellt ein neues ReportingKatalogRepository und initialisiert Kataloge, Fächer und Jahrgänge.
	 *
	 * @param conn   Die Datenbankverbindung.
	 * @param logger Der Logger.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public ReportingRepositoryKataloge(final DBEntityManager conn, final Logger logger)
			throws ApiOperationException {
		this.conn = conn;
		this.logger = logger;

		initKataloge();
		initFachdaten();
		initJahrgaenge();
	}

	private void initKataloge() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle Katalogdaten.");

			this.katalogEntlassgruende =
					new DataKatalogEntlassgruende(this.conn).getAll().stream().collect(Collectors.toMap(e -> e.id, e -> e));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Entlassgründe geladen.");

			this.katalogFoerderschwerpunkte =
					new DataKatalogSchuelerFoerderschwerpunkte(this.conn).getAll().stream().collect(Collectors.toMap(f -> f.id, f -> f));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Förderschwerpunkte geladen.");

			final DataOrte dataOrte = new DataOrte(this.conn);
			this.katalogOrte = dataOrte.getAll().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Orte geladen.");

			this.katalogOrtsteile = new DataOrtsteile(this.conn, dataOrte).getAll().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Ortsteile geladen.");

			this.katalogReligionen = new DataReligionen(this.conn).getAll().stream().collect(Collectors.toMap(r -> r.id, r -> r));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Religionen geladen.");

			this.katalogSchulen = new DataSchulen(this.conn).getAll().stream().collect(Collectors.toMap(s -> s.id, s -> s));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Schulen geladen.");

			final ArrayList<SchulformKatalogEintrag> schulformen = new ArrayList<>();
			for (final Schulform schulform : Schulform.values()) {
				schulformen.addAll(schulform.historie());
			}
			this.katalogSchulformen = schulformen.stream().collect(Collectors.toMap(sfke -> sfke.id, sfke -> sfke));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Schulformen geladen.");

			this.katalogTelefonnummerArten = new DataTelefonarten(this.conn).getAll().stream().collect(Collectors.toMap(ta -> ta.id, ta -> ta));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog TelefonnummerArten geladen.");

			this.mapErzieherarten = new DataErzieherarten(this.conn).getAll().stream().collect(Collectors.toMap(a -> a.id,
					ProxyReportingErzieherArt::new));
			this.logger.logLn(LogLevel.DEBUG, 8, "Liste der Erzieherarten geladen.");
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 8, "FEHLER: Die Kataloge der Schule konnten nicht vollständig ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Kataloge der Schule konnten nicht vollständig ermittelt werden.");
		}
	}

	private void initFachdaten() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle Fächer.");
			this.mapFachdaten = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 8, "FEHLER: Die Fächer konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Daten der Fächer konnten nicht ermittelt werden.");
		}
	}

	private void initJahrgaenge() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle die Jahrgangsdaten.");
			this.mapJahrgaenge = new DataJahrgangsdaten(this.conn).getAll().stream().collect(Collectors.toMap(j -> j.id, j -> j));
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Die Jahrgangsdaten konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Jahrgangsdaten. konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Gibt die Map der Entlassgründe zurück, indiziert nach ID.
	 *
	 * @return Map der Entlassgründe
	 */
	public Map<Long, KatalogEntlassgrund> katalogEntlassgruende() {
		return katalogEntlassgruende;
	}

	/**
	 * Gibt die Map der Förderschwerpunkt-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Förderschwerpunkt-Katalogeinträge
	 */
	public Map<Long, FoerderschwerpunktEintrag> katalogFoerderschwerpunkte() {
		return katalogFoerderschwerpunkte;
	}

	/**
	 * Gibt die Map der Ort-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Ort-Katalogeinträge
	 */
	public Map<Long, OrtKatalogEintrag> katalogOrte() {
		return katalogOrte;
	}

	/**
	 * Gibt die Map der Ortsteil-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Ortsteil-Katalogeinträge
	 */
	public Map<Long, OrtsteilKatalogEintrag> katalogOrtsteile() {
		return katalogOrtsteile;
	}

	/**
	 * Gibt die Map der Religions-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Religions-Katalogeinträge
	 */
	public Map<Long, ReligionEintrag> katalogReligionen() {
		return katalogReligionen;
	}

	/**
	 * Gibt die Map der Schul-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Schul-Katalogeinträge
	 */
	public Map<Long, SchulEintrag> katalogSchulen() {
		return katalogSchulen;
	}

	/**
	 * Gibt die Map der Schulform-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Schulform-Katalogeinträge
	 */
	public Map<Long, SchulformKatalogEintrag> katalogSchulformen() {
		return katalogSchulformen;
	}

	/**
	 * Gibt die Map der Telefonnummer-Arten zurück, indiziert nach ID.
	 *
	 * @return Map der Telefonnummer-Arten
	 */
	public Map<Long, Telefonart> katalogTelefonnummerArten() {
		return katalogTelefonnummerArten;
	}

	/**
	 * Gibt die Map der Fächer-DTOs zurück, indiziert nach der ID des Faches.
	 *
	 * @return Map der Fächer-DTOs
	 */
	public Map<Long, DTOFach> mapFachdaten() {
		return mapFachdaten;
	}

	/**
	 * Gibt die Map der Jahrgangsdaten zurück, indiziert nach Jahrgangs-ID.
	 *
	 * @return Map der Jahrgangsdaten
	 */
	public Map<Long, JahrgangsDaten> mapJahrgaenge() {
		return mapJahrgaenge;
	}

	/**
	 * Gibt die Map der Erzieherarten als Reporting-Objekte zurück, indiziert nach ID.
	 *
	 * @return Map der Erzieherarten
	 */
	public Map<Long, ReportingErzieherArt> mapErzieherarten() {
		return mapErzieherarten;
	}
}
