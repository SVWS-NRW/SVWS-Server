package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
	private Map<String, SchulEintrag> katalogSchulenNachSchulnummer;
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
	 * Gibt den Entlassgrund zur übergebenen ID zurück. Der Katalog wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @param idEntlassgrund Die ID des Entlassgrundes; {@code null} ist zulässig.
	 *
	 * @return Der Entlassgrund oder null, falls keine ID übergeben wurde oder zur ID kein Eintrag vorliegt.
	 */
	public KatalogEntlassgrund entlassgrund(final Long idEntlassgrund) {
		return (idEntlassgrund == null) ? null : ladeEntlassgruende().get(idEntlassgrund);
	}

	private Map<Long, KatalogEntlassgrund> ladeEntlassgruende() {
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
	 * Gibt den Förderschwerpunkt-Katalogeintrag zur übergebenen ID zurück. Der Katalog wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @param idFoerderschwerpunkt Die ID des Förderschwerpunktes; {@code null} ist zulässig.
	 *
	 * @return Der Förderschwerpunkt oder null, falls keine ID übergeben wurde oder zur ID kein Eintrag vorliegt.
	 */
	public FoerderschwerpunktEintrag foerderschwerpunkt(final Long idFoerderschwerpunkt) {
		return (idFoerderschwerpunkt == null) ? null : ladeFoerderschwerpunkte().get(idFoerderschwerpunkt);
	}

	private Map<Long, FoerderschwerpunktEintrag> ladeFoerderschwerpunkte() {
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
	 * Gibt den Ort-Katalogeintrag zur übergebenen ID zurück. Der Katalog wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @param idOrt Die ID des Ortes; {@code null} ist zulässig.
	 *
	 * @return Der Ort oder null, falls keine ID übergeben wurde oder zur ID kein Eintrag vorliegt.
	 */
	public OrtKatalogEintrag ort(final Long idOrt) {
		return (idOrt == null) ? null : ladeOrte().get(idOrt);
	}

	private Map<Long, OrtKatalogEintrag> ladeOrte() {
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
	 * Gibt den Ortsteil-Katalogeintrag zur übergebenen ID zurück. Der Katalog wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @param idOrtsteil Die ID des Ortsteils; {@code null} ist zulässig.
	 *
	 * @return Der Ortsteil oder null, falls keine ID übergeben wurde oder zur ID kein Eintrag vorliegt.
	 */
	public OrtsteilKatalogEintrag ortsteil(final Long idOrtsteil) {
		return (idOrtsteil == null) ? null : ladeOrtsteile().get(idOrtsteil);
	}

	private Map<Long, OrtsteilKatalogEintrag> ladeOrtsteile() {
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
	 * Gibt den Religions-Katalogeintrag zur übergebenen ID zurück. Der Katalog wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @param idReligion Die ID der Religion; {@code null} ist zulässig.
	 *
	 * @return Die Religion oder null, falls keine ID übergeben wurde oder zur ID kein Eintrag vorliegt.
	 */
	public ReligionEintrag religion(final Long idReligion) {
		return (idReligion == null) ? null : ladeReligionen().get(idReligion);
	}

	private Map<Long, ReligionEintrag> ladeReligionen() {
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
	 * Gibt den Schul-Katalogeintrag zur übergebenen ID zurück. Der Katalog wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @param idSchule Die ID der Schule im Katalog; {@code null} ist zulässig.
	 *
	 * @return Die Schule oder null, falls keine ID übergeben wurde oder zur ID kein Eintrag vorliegt.
	 */
	public SchulEintrag schule(final Long idSchule) {
		return (idSchule == null) ? null : ladeSchulen().get(idSchule);
	}

	private Map<Long, SchulEintrag> ladeSchulen() {
		if (katalogSchulen == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Katalog Schulen, indiziert nach IDs.");
				katalogSchulen = new DataSchulen(this.reportingContext.conn()).getAll().stream()
						.collect(Collectors.toMap(s -> s.id, s -> s));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Schulen (nach IDs)", e);
			}
		}
		return katalogSchulen;
	}

	/**
	 * Gibt den Schul-Katalogeintrag zur übergebenen Schulnummer zurück. Sofern eine Schulnummer mehrfach vergeben wurde,
	 * wird nur der erste Eintrag berücksichtigt. Der Katalog wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @param schulnummer Die statistische Schulnummer; {@code null} ist zulässig.
	 *
	 * @return Die Schule oder null, falls keine Schulnummer übergeben wurde oder zu ihr kein Eintrag vorliegt.
	 */
	public SchulEintrag schuleNachSchulnummer(final String schulnummer) {
		return (schulnummer == null) ? null : ladeSchulenNachSchulnummer().get(schulnummer);
	}

	private Map<String, SchulEintrag> ladeSchulenNachSchulnummer() {
		if (katalogSchulenNachSchulnummer == null) {
			try {
				this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Lade Katalog Schulen, indiziert nach Schulnummern.");
				katalogSchulenNachSchulnummer = new DataSchulen(this.reportingContext.conn()).getAll().stream()
						.filter(s -> (s.schulnummerStatistik != null) && (!s.schulnummerStatistik.isEmpty()))
						.collect(Collectors.toMap(s -> s.schulnummerStatistik, s -> s, (s1, s2) -> s1));
			} catch (final Exception e) {
				throw fehlerKatalogdatenLaden("Schulen (nach Schulnummer)", e);
			}
		}
		return katalogSchulenNachSchulnummer;
	}

	/**
	 * Gibt den Schulform-Katalogeintrag zur übergebenen ID zurück. Der Katalog wird beim ersten Zugriff aufgebaut.
	 *
	 * @param idSchulform Die ID der Schulform; {@code null} ist zulässig.
	 *
	 * @return Die Schulform oder null, falls keine ID übergeben wurde oder zur ID kein Eintrag vorliegt.
	 */
	public SchulformKatalogEintrag schulform(final Long idSchulform) {
		return (idSchulform == null) ? null : ladeSchulformen().get(idSchulform);
	}

	/**
	 * Gibt alle Schulform-Katalogeinträge zurück. Der Katalog wird beim ersten Zugriff aufgebaut.
	 *
	 * @return Unveränderliche Liste der Schulform-Katalogeinträge.
	 */
	public List<SchulformKatalogEintrag> schulformen() {
		return List.copyOf(ladeSchulformen().values());
	}

	private Map<Long, SchulformKatalogEintrag> ladeSchulformen() {
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
	 * Gibt die Telefonnummer-Art zur übergebenen ID zurück. Der Katalog wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @param idTelefonart Die ID der Telefonnummer-Art; {@code null} ist zulässig.
	 *
	 * @return Die Telefonnummer-Art oder null, falls keine ID übergeben wurde oder zur ID kein Eintrag vorliegt.
	 */
	public Telefonart telefonnummerArt(final Long idTelefonart) {
		return (idTelefonart == null) ? null : ladeTelefonnummerArten().get(idTelefonart);
	}

	private Map<Long, Telefonart> ladeTelefonnummerArten() {
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
	 * Gibt alle Fächer-DTOs zurück. Sie werden beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @return Unveränderliche Liste der Fächer-DTOs.
	 */
	public List<DTOFach> faecher() {
		return List.copyOf(ladeFaecher().values());
	}

	private Map<Long, DTOFach> ladeFaecher() {
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
	 * Gibt alle Jahrgangsdaten zurück. Sie werden beim ersten Zugriff aus der Datenbank geladen.
	 * Negativ-Einträge, die {@link #jahrgang(long)} für nicht ermittelbare Jahrgänge hinterlegt, sind nicht enthalten.
	 *
	 * @return Unveränderliche Liste der Jahrgangsdaten.
	 */
	public List<JahrgangsDaten> jahrgaenge() {
		return ladeJahrgaenge().values().stream().filter(Objects::nonNull).toList();
	}

	private Map<Long, JahrgangsDaten> ladeJahrgaenge() {
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
		final Map<Long, JahrgangsDaten> map = ladeJahrgaenge();
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
	 * Gibt die Erzieherart zur übergebenen ID zurück. Der Katalog wird beim ersten Zugriff aus der Datenbank geladen.
	 *
	 * @param idErzieherArt Die ID der Erzieherart; {@code null} ist zulässig.
	 *
	 * @return Die Erzieherart oder null, falls keine ID übergeben wurde oder zur ID kein Eintrag vorliegt.
	 */
	public ReportingErzieherArt erzieherart(final Long idErzieherArt) {
		return (idErzieherArt == null) ? null : ladeErzieherarten().get(idErzieherArt);
	}

	private Map<Long, ReportingErzieherArt> ladeErzieherarten() {
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
	 * Gibt alle Ankreuzkompetenzen zurück. Sie werden beim ersten Zugriff aus der Datenbank geladen; die darin enthaltenen
	 * Jahrgangszuordnungen werden ebenfalls mitgeladen.
	 *
	 * @return Unveränderliche Liste der Ankreuzkompetenzen.
	 */
	public List<Ankreuzkompetenz> ankreuzkompetenzen() {
		return List.copyOf(ladeAnkreuzkompetenzen().values());
	}

	private Map<Long, Ankreuzkompetenz> ladeAnkreuzkompetenzen() {
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
