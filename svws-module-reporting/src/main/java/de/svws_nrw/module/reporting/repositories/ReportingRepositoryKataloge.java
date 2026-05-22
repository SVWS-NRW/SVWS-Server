package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.core.data.schule.Ankreuzkompetenz;
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
import de.svws_nrw.data.schule.DataAnkreuzkompetenzJahrgangszuordnungen;
import de.svws_nrw.data.schule.DataAnkreuzkompetenzen;
import de.svws_nrw.data.schule.DataReligionen;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.data.schule.DataTelefonarten;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ProxyReportingErzieherArt;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Domänen-Repository für Kataloge, Fächer, Jahrgänge und Erzieherarten.
 * Die Katalogdaten werden erst beim ersten Zugriff (Lazy-Loading) aus der Datenbank geladen,
 * damit Reports, die einzelne Kataloge nicht benötigen, keinen unnötigen DB-Zugriff auslösen.
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
	private Map<Long, Ankreuzkompetenz> mapAnkreuzkompetenzen;

	/**
	 * Erstellt ein neues ReportingKatalogRepository. Die Katalogdaten werden erst beim ersten Zugriff geladen.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 */
	public ReportingRepositoryKataloge(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
	}

	/**
	 * Gibt die Map der Entlassgründe zurück, indiziert nach ID. Wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Map der Entlassgründe
	 */
	public Map<Long, KatalogEntlassgrund> entlassgruende() {
		if (katalogEntlassgruende == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Katalog Entlassgründe.");
				katalogEntlassgruende = new DataKatalogEntlassgruende(this.reportingContext.conn()).getAll().stream()
						.collect(Collectors.toMap(e -> e.id, e -> e));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Entlassgründe", e);
			}
		}
		return katalogEntlassgruende;
	}

	/**
	 * Gibt die Map der Förderschwerpunkt-Katalogeinträge zurück, indiziert nach ID. Wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Map der Förderschwerpunkt-Katalogeinträge
	 */
	public Map<Long, FoerderschwerpunktEintrag> foerderschwerpunkte() {
		if (katalogFoerderschwerpunkte == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Katalog Förderschwerpunkte.");
				katalogFoerderschwerpunkte = new DataKatalogSchuelerFoerderschwerpunkte(this.reportingContext.conn()).getAll().stream()
						.collect(Collectors.toMap(f -> f.id, f -> f));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Förderschwerpunkte", e);
			}
		}
		return katalogFoerderschwerpunkte;
	}

	/**
	 * Gibt die Map der Ort-Katalogeinträge zurück, indiziert nach ID. Wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Map der Ort-Katalogeinträge
	 */
	public Map<Long, OrtKatalogEintrag> orte() {
		if (katalogOrte == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Katalog Orte.");
				katalogOrte = new DataOrte(this.reportingContext.conn()).getAll().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Orte", e);
			}
		}
		return katalogOrte;
	}

	/**
	 * Gibt die Map der Ortsteil-Katalogeinträge zurück, indiziert nach ID. Wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Map der Ortsteil-Katalogeinträge
	 */
	public Map<Long, OrtsteilKatalogEintrag> ortsteile() {
		if (katalogOrtsteile == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Katalog Ortsteile.");
				final DataOrte dataOrte = new DataOrte(this.reportingContext.conn());
				katalogOrtsteile = new DataOrtsteile(this.reportingContext.conn(), dataOrte).getAll().stream()
						.collect(Collectors.toMap(o -> o.id, o -> o));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Ortsteile", e);
			}
		}
		return katalogOrtsteile;
	}

	/**
	 * Gibt die Map der Religions-Katalogeinträge zurück, indiziert nach ID. Wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Map der Religions-Katalogeinträge
	 */
	public Map<Long, ReligionEintrag> religionen() {
		if (katalogReligionen == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Katalog Religionen.");
				katalogReligionen = new DataReligionen(this.reportingContext.conn()).getAll().stream().collect(Collectors.toMap(r -> r.id, r -> r));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Religionen", e);
			}
		}
		return katalogReligionen;
	}

	/**
	 * Gibt die Map der Schul-Katalogeinträge zurück, indiziert nach ID. Wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Map der Schul-Katalogeinträge
	 */
	public Map<Long, SchulEintrag> schulen() {
		if (katalogSchulen == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Katalog Schulen.");
				katalogSchulen = new DataSchulen(this.reportingContext.conn()).getAll().stream().collect(Collectors.toMap(s -> s.id, s -> s));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Schulen", e);
			}
		}
		return katalogSchulen;
	}

	/**
	 * Gibt die Map der Schulform-Katalogeinträge zurück, indiziert nach ID. Wird beim ersten Zugriff aufgebaut.
	 *
	 * @return Map der Schulform-Katalogeinträge
	 */
	public Map<Long, SchulformKatalogEintrag> schulformen() {
		if (katalogSchulformen == null) {
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Katalog Schulformen.");
			final ArrayList<SchulformKatalogEintrag> schulformen = new ArrayList<>();
			for (final Schulform schulform : Schulform.values()) {
				schulformen.addAll(schulform.historie());
			}
			katalogSchulformen = schulformen.stream().collect(Collectors.toMap(sfke -> sfke.id, sfke -> sfke));
		}
		return katalogSchulformen;
	}

	/**
	 * Gibt die Map der Telefonnummer-Arten zurück, indiziert nach ID. Wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Map der Telefonnummer-Arten
	 */
	public Map<Long, Telefonart> telefonnummerArten() {
		if (katalogTelefonnummerArten == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Katalog TelefonnummerArten.");
				katalogTelefonnummerArten = new DataTelefonarten(this.reportingContext.conn()).getAll().stream()
						.collect(Collectors.toMap(ta -> ta.id, ta -> ta));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Telefonnummer-Arten", e);
			}
		}
		return katalogTelefonnummerArten;
	}

	/**
	 * Gibt die Map der Fächer-DTOs zurück, indiziert nach der ID des Faches. Wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Map der Fächer-DTOs
	 */
	public Map<Long, DTOFach> faecher() {
		if (mapFaecher == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Fächer.");
				mapFaecher = this.reportingContext.conn().queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Fächer", e);
			}
		}
		return mapFaecher;
	}

	/**
	 * Gibt die Map der Jahrgangsdaten zurück, indiziert nach Jahrgangs-ID. Wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Map der Jahrgangsdaten
	 */
	public Map<Long, JahrgangsDaten> jahrgaenge() {
		if (mapJahrgaenge == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Jahrgangsdaten.");
				mapJahrgaenge = new HashMap<>();
				new DataJahrgangsdaten(this.reportingContext.conn()).getAll().forEach(j -> mapJahrgaenge.put(j.id, j));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Jahrgangsdaten", e);
			}
		}
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
		final Map<Long, JahrgangsDaten> map = jahrgaenge();
		if (map.containsKey(idJahrgang)) {
			return map.get(idJahrgang);
		}
		try {
			final JahrgangsDaten jahrgangsDaten = new DataJahrgangsdaten(this.reportingContext.conn()).getById(idJahrgang);
			map.put(idJahrgang, jahrgangsDaten);
			return jahrgangsDaten;
		} catch (final ApiOperationException e) {
			ReportingExceptionUtils.logException(
					"FEHLER: Fehler bei der Ermittlung der Jahrgangsdaten zur ID %d aus der Datenbank im ReportingContext.".formatted(idJahrgang),
					e, this.reportingContext.logger(), LogLevel.ERROR, 0);
			map.put(idJahrgang, null);
			return null;
		}
	}

	/**
	 * Gibt die Map der Erzieherarten als Reporting-Objekte zurück, indiziert nach ID. Wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Map der Erzieherarten
	 */
	public Map<Long, ReportingErzieherArt> erzieherarten() {
		if (mapErzieherarten == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Erzieherarten.");
				mapErzieherarten = new DataErzieherarten(this.reportingContext.conn()).getAll().stream()
						.collect(Collectors.toMap(a -> a.id, ProxyReportingErzieherArt::new));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Erzieherarten", e);
			}
		}
		return mapErzieherarten;
	}

	/**
	 * Gibt die Map der Ankreuzkompetenzen zurück, indiziert nach ID. Wird beim ersten Zugriff aus der Datenbank geladen.
	 * Die in den Ankreuzkompetenzen enthaltenen Jahrgangszuordnungen werden ebenfalls mitgeladen.
	 *
	 * @return Map der Ankreuzkompetenzen
	 */
	public Map<Long, Ankreuzkompetenz> ankreuzkompetenzen() {
		if (mapAnkreuzkompetenzen == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Ankreuzkompetenzen.");
				final DataAnkreuzkompetenzJahrgangszuordnungen dataJahrgangszuordnungen =
						new DataAnkreuzkompetenzJahrgangszuordnungen(this.reportingContext.conn());
				mapAnkreuzkompetenzen = new DataAnkreuzkompetenzen(this.reportingContext.conn(), dataJahrgangszuordnungen).getList().stream()
						.collect(Collectors.toMap(a -> a.id, a -> a));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Ankreuzkompetenzen", e);
			}
		}
		return mapAnkreuzkompetenzen;
	}

	/**
	 * Loggt einen Fehler beim Nachladen von Katalogdaten über {@link ReportingExceptionUtils} und gibt eine
	 * {@link IllegalStateException} zurück, die vom Aufrufer geworfen werden soll, um den laufenden Report-Aufruf abzubrechen.
	 *
	 * <p>Die Exception ist bewusst unchecked, damit die Getter-Signaturen der Katalog-Methoden frei von {@code throws}-Klauseln
	 * bleiben können. Sie wird vom äußeren {@code try/catch} in {@link de.svws_nrw.module.reporting.factories.ReportingFactory}
	 * gefangen.</p>
	 *
	 * @param datentyp     Kurze Beschreibung des nicht ladbaren Katalogs wird in die Log- und Exception-Meldung übernommen.
	 * @param fehlerursache Die ursprüngliche Exception aus dem DB-Zugriff; wird sowohl geloggt als auch als {@code cause} mitgegeben.
	 *
	 * @return Die zu werfende {@link IllegalStateException} mit aufbereiteter Meldung und Ursache.
	 */
	private IllegalStateException fehlerKatalogdatenLaden(final String datentyp, final Exception fehlerursache) {
		final String meldung = "FEHLER: Katalogdaten vom Typ '%s' konnte nicht aus der Datenbank ermittelt werden.".formatted(datentyp);
		ReportingExceptionUtils.logException(meldung, fehlerursache, this.reportingContext.logger(), LogLevel.ERROR, 0);
		return new IllegalStateException(meldung, fehlerursache);
	}
}
