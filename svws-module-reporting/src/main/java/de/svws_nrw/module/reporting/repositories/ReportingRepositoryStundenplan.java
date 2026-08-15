package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.data.stundenplan.DataStundenplanPausenaufsichten;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterricht;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterrichtsverteilung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.types.stundenplanung.ProxyReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.ws.rs.core.Response.Status;

/**
 * Domänen-Repository für Stundenpläne (Definitionen, Manager und Reporting-Objekte).
 * Die Stundenplandefinitionen werden bei der Initialisierung geladen; Manager und Reporting-Objekte werden bei Bedarf erzeugt und gecacht.
 * <p>Der Reporting-Context erzeugt dieses Repository für jeden Report, auch für solche ohne Stundenplanbezug. Ein Fehler beim Laden der Definitionen wird
 * deshalb nur festgehalten und erst von dem Zugriff bewertet, der die Daten benötigt - siehe {@link #stundenplan(String)} und {@link #stundenplan(long)}.</p>
 */
public class ReportingRepositoryStundenplan {

	private final ReportingContext reportingContext;

	/** Die Definitionen der Stundenpläne. Schlägt das Laden fehl, bleibt die Liste leer, damit die vorhandenen Zugriffe nicht ins Leere laufen. */
	private final List<StundenplanListeEintrag> stundenplandefinitionen = new ArrayList<>();

	/**
	 * Der Fehler beim Laden der Definitionen oder null. Festgehalten wird die Exception und kein Kennzeichen, damit beim Zugriff nicht nur bekannt ist,
	 * <i>dass</i> das Laden fehlschlug, sondern die Ursache als {@code cause} weitergegeben werden kann.
	 */
	private final ApiOperationException ladefehlerDefinitionen;

	/** Gibt an, ob der optionale Zugriff den Ladefehler bereits als Warnung protokolliert hat. */
	private boolean ladefehlerAlsWarnungProtokolliert = false;

	private final Map<Long, StundenplanManager> mapStundenplanManager = new HashMap<>();
	private final Map<Long, ReportingStundenplanungStundenplan> mapStundenplaene = new HashMap<>();

	/**
	 * Die Fehler je Stundenplan, dessen Daten sich nicht laden ließen; der Wert ist {@code null}, wenn der Zugriff ohne Ausnahme unvollständig blieb. Der
	 * strikte Zugriff gibt den festgehaltenen Fehler als Ursache seines Abbruchs mit.
	 */
	private final Map<Long, Exception> ladefehlerStundenplaene = new HashMap<>();

	/**
	 * Erstellt ein neues ReportingStundenplanRepository und initialisiert die Stundenplandefinition.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 */
	public ReportingRepositoryStundenplan(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;

		this.ladefehlerDefinitionen = initStundenplanDefinitionen();
	}

	/**
	 * Lädt die Stundenplandefinitionen und gibt einen dabei aufgetretenen Fehler zurück, statt ihn zu werfen oder zu protokollieren.
	 * <p>Zum Zeitpunkt der Initialisierung ist die Bedeutung des Fehlers unbekannt: Ein Report ohne Stundenplanbezug berührt die Definitionen nie. Ein
	 * Log-Eintrag mit {@link LogLevel#ERROR} wäre hier zudem verfrüht, denn er ist dem Abbruch vorbehalten.</p>
	 *
	 * @return Der Fehler beim Laden der Definitionen oder null, wenn sie geladen werden konnten.
	 */
	private ApiOperationException initStundenplanDefinitionen() {
		try {
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Ermittle alle Stundenplan-Definitionen der Schule.");
			this.stundenplandefinitionen.addAll(DataStundenplanListe.getStundenplaeneAktiv(this.reportingContext.conn(), null));
			this.stundenplandefinitionen.sort(Comparator.comparing((StundenplanListeEintrag sle) -> sle.gueltigAb).reversed());
			return null;
		} catch (final ApiOperationException aoe) {
			// Der bereits klassifizierte Fehler bleibt erhalten; der generische Zweig darunter überschriebe seinen Status.
			this.stundenplandefinitionen.clear();
			return aoe;
		} catch (final Exception e) {
			this.stundenplandefinitionen.clear();
			return new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "FEHLER: Die Daten der Stundenpläne konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Protokolliert den Ladefehler der Definitionen als Warnung, jedoch höchstens einmal je Repository-Instanz und damit je Reporting-Aufruf.
	 * <p>Der optionale Zugriff erfolgt je Klausurraum erneut. Ohne die Begrenzung stünde derselbe Fehler samt Stacktrace bei dreißig Klausurräumen dreißigmal
	 * im Log. Da der Zugriff nicht wirft, ist diese Ausgabe zugleich die einzige Gelegenheit, Ursache und Stacktrace festzuhalten.</p>
	 */
	private void protokolliereLadefehlerAlsWarnung() {
		if (this.ladefehlerAlsWarnungProtokolliert) {
			return;
		}
		this.ladefehlerAlsWarnungProtokolliert = true;
		ReportingExceptionUtils.logException(
				"WARNUNG: Die Stundenplandefinitionen konnten nicht ermittelt werden. Angaben aus dem Stundenplan bleiben in dieser Ausgabe leer.",
				this.ladefehlerDefinitionen, this.reportingContext.logger(), LogLevel.WARNING, 8);
	}

	/**
	 * Gibt den Stundenplan zurück, der am übergebenen Datum gültig ist.
	 * <p><b>Optionaler Zugriff:</b> Der Stundenplan ist hier Beiwerk einer anderen Ausgabe - etwa die Raumangabe eines Klausurtermins. Ein fehlender oder
	 * nicht ladbarer Stundenplan wird als Lücke dargestellt und bricht die Ausgabe nicht ab: Die Methode wirft nicht und protokolliert einen Ladefehler
	 * höchstens als Warnung. Wer den Stundenplan als Hauptdatum benötigt, verwendet {@link #stundenplan(long)}.</p>
	 *
	 * @param datum Das Datum im Format yyyy-mm-dd.
	 *
	 * @return Der Stundenplan zum Datum oder null, falls keiner existiert oder die Definitionen nicht geladen werden konnten.
	 */
	public ReportingStundenplanungStundenplan stundenplan(final String datum) {
		// Ohne verwertbares Datum werden keine Stundenplandaten benötigt - etwa bei einem Klausurtermin ohne Datum. Ein Ladefehler bleibt dann folgenlos und
		// wird auch nicht gemeldet.
		if ((datum == null) || (datum.length() != 10)) {
			return null;
		}

		if (this.ladefehlerDefinitionen != null) {
			protokolliereLadefehlerAlsWarnung();
			return null;
		}

		if (stundenplandefinitionen.isEmpty()) {
			return null;
		}

		final StundenplanListeEintrag stundenplandefinitionZuDatum = stundenplandefinitionen.stream()
				.filter(d -> ((d.gueltigAb != null) && (d.gueltigBis != null) && (datum.compareTo(d.gueltigAb) >= 0) && (datum.compareTo(d.gueltigBis) <= 0)))
				.findFirst().orElse(null);

		if (stundenplandefinitionZuDatum != null) {
			return ermittleStundenplan(stundenplandefinitionZuDatum.id);
		} else {
			return null;
		}
	}

	/**
	 * Gibt den Stundenplan zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank erzeugt.
	 * <p><b>Strikter Zugriff:</b> Der Stundenplan ist hier das angeforderte Hauptdatum, und maßgeblich ist die Definitionsliste dieses Aufrufs: Führt sie die
	 * ID und entsteht dennoch kein Stundenplan, ist das ein Serverfehler - auch dann, wenn der Einzelzugriff den Plan nicht mehr findet, etwa nach einem
	 * parallelen Löschen. Eine ID, zu der es keine Definition gibt, ergibt dagegen null - der Aufrufer macht daraus ein {@code NOT_FOUND}. Für den optionalen
	 * Zugriff auf einen Stundenplan als Beiwerk gilt {@link #stundenplan(String)}.</p>
	 *
	 * @param idStundenplan Die ID des Stundenplans.
	 *
	 * @return Der Stundenplan zur ID oder null, falls keiner existiert.
	 *
	 * @throws ApiOperationException Mit Status 500, wenn die Stundenplandefinitionen oder die Daten des vorhandenen Stundenplans nicht geladen werden konnten.
	 */
	public ReportingStundenplanungStundenplan stundenplan(final long idStundenplan) throws ApiOperationException {
		if (this.ladefehlerDefinitionen != null) {
			// Nur die Quellmeldung: Den strukturierten Block aus Typ, Ursachenkette und Stacktrace gibt die oberste Ebene aus, die diesen Wurf behandelt.
			this.reportingContext.logger().logLn(LogLevel.ERROR, 8,
					"FEHLER: Der angeforderte Stundenplan ist nicht ermittelbar, da die Stundenplandefinitionen nicht geladen werden konnten.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, this.ladefehlerDefinitionen,
					"FEHLER: Der angeforderte Stundenplan ist nicht ermittelbar, da die Stundenplandefinitionen nicht geladen werden konnten.");
		}

		final ReportingStundenplanungStundenplan stundenplan = ermittleStundenplan(idStundenplan);
		if ((stundenplan == null) && stundenplandefinitionen.stream().anyMatch(d -> d.id == idStundenplan)) {
			// Die Definitionsliste dieses Aufrufs führt den Stundenplan; dass er dennoch fehlt, ist ein Serverproblem. Als NOT_FOUND wäre das die falsche
			// Auskunft, der Anwender suchte nach einem Datensatz, den die Schule führt.
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, this.ladefehlerStundenplaene.get(idStundenplan),
					"FEHLER: Der Stundenplan %d ist in den Stundenplandefinitionen vorhanden, seine Daten konnten aber nicht geladen werden."
							.formatted(idStundenplan));
		}
		return stundenplan;
	}

	/**
	 * Ermittelt den Stundenplan zur übergebenen ID aus dem Cache oder aus der Datenbank, ohne den Ladefehler der Definitionen zu bewerten.
	 * <p>Beide {@code stundenplan}-Methoden teilen sich diese Ermittlung; sie unterscheiden sich allein darin, wie sie einen Ladefehler der Definitionen
	 * behandeln.</p>
	 *
	 * @param idStundenplan Die ID des Stundenplans.
	 *
	 * @return Der Stundenplan zur ID oder null, falls keiner existiert.
	 */
	private ReportingStundenplanungStundenplan ermittleStundenplan(final long idStundenplan) {
		if (stundenplandefinitionen.stream().noneMatch(d -> d.id == idStundenplan)) {
			return null;
		}

		if (mapStundenplaene.containsKey(idStundenplan)) {
			return mapStundenplaene.get(idStundenplan);
		}

		if (mapStundenplanManager.containsKey(idStundenplan)) {
			mapStundenplaene.computeIfAbsent(idStundenplan,
					key -> new ProxyReportingStundenplanungStundenplan(this.reportingContext, mapStundenplanManager.get(key)));
			return mapStundenplaene.get(idStundenplan);
		}

		try {
			final StundenplanManager manager = manager(idStundenplan);
			if (manager == null) {
				return null;
			}
			mapStundenplanManager.put(idStundenplan, manager);
			mapStundenplaene.put(idStundenplan, new ProxyReportingStundenplanungStundenplan(this.reportingContext, manager));
			return mapStundenplaene.get(idStundenplan);
		} catch (final Exception e) {
			// Hier scheitert der Aufbau des Reporting-Objekts an Daten, die nicht zusammenpassen - etwa einer Aufsicht ohne ihren Aufsichtsbereich. Der
			// Stundenplan entfällt dann, wird aber gemeldet statt still verschluckt; eine Infrastrukturstörung bricht in der Meldung ab.
			meldeStundenplanLadefehler(idStundenplan, e);
			return null;
		}
	}

	/**
	 * Gibt den StundenplanManager zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank erzeugt.
	 * <p>Beide Fälle des Fehlens ergeben {@code null}, werden aber getrennt behandelt: Einen Stundenplan, den es nicht gibt, meldet der Zugriff nicht - er hat
	 * einwandfrei gearbeitet. Ein gescheiterter oder unvollständiger Zugriff wird dagegen gemeldet, sonst bliebe er beim Aufrufer von "gibt es nicht"
	 * ununterscheidbar.</p>
	 *
	 * @param idStundenplan Die ID des Stundenplans.
	 *
	 * @return Der StundenplanManager zur ID oder null, falls der Stundenplan nicht existiert oder nicht geladen werden konnte.
	 */
	public StundenplanManager manager(final long idStundenplan) {
		mapStundenplanManager.computeIfAbsent(idStundenplan, this::erzeugeManager);

		return this.mapStundenplanManager.get(idStundenplan);
	}

	/**
	 * Erzeugt den StundenplanManager zur übergebenen ID aus der Datenbank.
	 * <p>Allein der Zugriff auf den Stundenplan selbst darf {@code NOT_FOUND} als "gibt es nicht" lesen. Derselbe Status aus dem Nachladen von Unterricht,
	 * Aufsichten oder Unterrichtsverteilung bezeichnet dagegen fehlende Teildaten eines vorhandenen Stundenplans und wird gemeldet.</p>
	 *
	 * @param idStundenplan Die ID des Stundenplans.
	 *
	 * @return Der Manager oder null, falls der Stundenplan nicht existiert oder nicht geladen werden konnte.
	 */
	private StundenplanManager erzeugeManager(final long idStundenplan) {
		final Stundenplan stundenplan;
		try {
			stundenplan = new DataStundenplan(this.reportingContext.conn()).getById(idStundenplan);
		} catch (final ApiOperationException e) {
			if (e.getStatus() == Status.NOT_FOUND) {
				// Für den direkten Zugriff bleibt das die fachliche Auskunft "gibt es nicht" und wird nicht gemeldet. Festgehalten wird der Fehler trotzdem:
				// Führt die Definitionsliste die ID, gibt der strikte Zugriff ihn als Ursache seines Abbruchs mit.
				this.ladefehlerStundenplaene.put(idStundenplan, e);
				return null;
			}
			meldeStundenplanLadefehler(idStundenplan, e);
			return null;
		}

		if (stundenplan == null) {
			// Den Stundenplan gibt es, seine Daten sind aber unvollständig - etwa ohne den Schuljahresabschnitt, auf den er sich bezieht.
			meldeStundenplanLadefehler(idStundenplan, null);
			return null;
		}

		try {
			final List<StundenplanUnterricht> unterrichte = DataStundenplanUnterricht.getUnterrichte(this.reportingContext.conn(), idStundenplan);
			final List<StundenplanPausenaufsicht> aufsichten = DataStundenplanPausenaufsichten.getAufsichten(this.reportingContext.conn(), idStundenplan);
			final StundenplanUnterrichtsverteilung unterrichtsverteilung =
					DataStundenplanUnterrichtsverteilung.getUnterrichtsverteilung(this.reportingContext.conn(), idStundenplan);
			return new StundenplanManager(stundenplan, unterrichte, aufsichten, unterrichtsverteilung);
		} catch (final ApiOperationException e) {
			meldeStundenplanLadefehler(idStundenplan, e);
			return null;
		}
	}

	/**
	 * Hält den Ladefehler eines Stundenplans fest und meldet ihn als Ausgabeproblem. Festgehalten wird er, damit der strikte Zugriff ihn als Ursache seines
	 * Abbruchs mitgeben kann.
	 *
	 * @param idStundenplan Die ID des betroffenen Stundenplans.
	 * @param fehler        Der Fehler des Zugriffs oder {@code null}, wenn der Zugriff ohne Ausnahme unvollständig blieb.
	 */
	private void meldeStundenplanLadefehler(final long idStundenplan, final Exception fehler) {
		this.ladefehlerStundenplaene.put(idStundenplan, fehler);
		ReportingRepositoryUtils.meldeTeildatenLadefehler(this.reportingContext,
				ReportingProblemSchluessel.fuer(ReportingStundenplanungStundenplan.class, idStundenplan),
				"Die Daten des Stundenplans %d".formatted(idStundenplan), fehler);
	}

}
