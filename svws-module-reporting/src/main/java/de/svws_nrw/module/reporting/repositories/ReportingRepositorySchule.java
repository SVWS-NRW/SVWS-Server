package de.svws_nrw.module.reporting.repositories;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.base.email.EmailJobContext;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.email.DataEmailJobs;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingSchule;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import jakarta.ws.rs.core.Response.Status;

/**
 * Domänen-Repository für Schuldaten und Schuljahresabschnitte.
 * Die Schulstammdaten und alle Schuljahresabschnitte werden bei der Initialisierung aus der Datenbank geladen.
 */
public class ReportingRepositorySchule {

	private final ReportingContext reportingContext;
	private final SchuleStammdaten schulstammdaten;
	private final String schullogoBase64;
	private final Long idAktuellerSchuljahresabschnitt;
	private final Long idAuswahlSchuljahresabschnitt;
	private final Map<Long, ReportingSchuljahresabschnitt> mapSchuljahresabschnitte = new HashMap<>();

	/**
	 * Cache der virtuellen Schuljahresabschnitte dieses Reports, indiziert nach ihrer negativen Pseudo-ID. Bewusst
	 * getrennt von {@link #mapSchuljahresabschnitte} gehalten, damit virtuelle Abschnitte nicht in den Listen der
	 * tatsächlich vorhandenen Abschnitte der Schule auftauchen. Siehe {@link #schuljahresabschnittOderVirtuell(int, int)}.
	 */
	private final Map<Long, ReportingSchuljahresabschnitt> mapVirtuelleSchuljahresabschnitte = new HashMap<>();

	/** Zwischenspeicher für das Reporting-Objekt der Schule, damit es nicht mehrfach instanziiert wird. */
	private ReportingSchule schule;

	/** Zwischenspeichert das Ergebnis der GOSt-Schul-Vorbedingung: {@code null} = noch nicht geprüft, {@code true}/{@code false} = geprüft. */
	private Boolean istSchuleMitGost = null;

	/**
	 * Erstellt ein neues ReportingSchuleRepository und initialisiert Schulstammdaten und Schuljahresabschnitte. Der übergebene Schuljahresabschnitt wird hier
	 * geprüft, weil ein unbekannter erst beim Auflösen als {@code null} auffiele und den Report dann mit einem Serverfehler beendete - obwohl der Aufrufer ihn
	 * benannt hat.
	 *
	 * @param reportingContext           Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 * @param idAuswahlSchuljahresabschnitt Die ID des ausgewählten Schuljahresabschnitts.
	 *
	 * @throws ApiOperationException Mit Status 500, wenn die Schuldaten nicht ermittelt werden können; mit Status 400, wenn der übergebene
	 *                               Schuljahresabschnitt nicht zu dieser Schule gehört.
	 */
	public ReportingRepositorySchule(final ReportingContext reportingContext, final long idAuswahlSchuljahresabschnitt) throws ApiOperationException {
		this.reportingContext = reportingContext;
		this.reportingContext.logger().logLn(LogLevel.DEBUG, 8, "Ermittle Stammdaten und Abschnitte der Schule.");
		try {
			final DataSchuleStammdaten dataSchuleStammdaten = new DataSchuleStammdaten(this.reportingContext.conn());
			this.schulstammdaten = DataSchuleStammdaten.getStammdaten(this.reportingContext.conn());
			this.schullogoBase64 = dataSchuleStammdaten.getSchullogoBase64();

			final List<Schuljahresabschnitt> datenSchuljahresabschnitte = this.reportingContext.conn().getUser().schuleGetStammdaten().abschnitte;
			for (final Schuljahresabschnitt datenSchuljahresabschnitt : datenSchuljahresabschnitte) {
				mapSchuljahresabschnitte.putIfAbsent(datenSchuljahresabschnitt.id,
						new ProxyReportingSchuljahresabschnitt(this.reportingContext, datenSchuljahresabschnitt));
			}

			this.idAktuellerSchuljahresabschnitt = this.schulstammdaten.idSchuljahresabschnitt;
		} catch (final Exception e) {
			// Protokolliert wird hier nicht, damit der eine ERROR-Eintrag an der Abschlussgrenze entsteht.
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: Die Stamm- oder Abschnittsdaten der Schule konnten nicht ermittelt werden.");
		}

		if (!mapSchuljahresabschnitte.containsKey(idAuswahlSchuljahresabschnitt)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "### FEHLER: Der gewählte Schuljahresabschnitt gehört nicht zu dieser Schule.");
		}
		this.idAuswahlSchuljahresabschnitt = idAuswahlSchuljahresabschnitt;
	}


	// ##### Schulstammdaten und Schullogo #####

	/**
	 * Gibt die Stammdaten der Schule zurück.
	 *
	 * @return Die Stammdaten der Schule.
	 */
	public SchuleStammdaten stammdaten() {
		return schulstammdaten;
	}

	/**
	 * Gibt das Schullogo der Schule im Base64-Format zurück.
	 *
	 * @return Das Schullogo im Base64-Format.
	 */
	public String schullogoBase64() {
		return schullogoBase64;
	}

	/**
	 * Gibt das Reporting-Objekt der Schule zurück. Wird bei erstmaligem Zugriff erzeugt und danach zwischengespeichert.
	 *
	 * @return Das Reporting-Objekt der Schule.
	 */
	public ReportingSchule schule() {
		if (schule == null) {
			schule = new ProxyReportingSchule(this.reportingContext);
		}
		return schule;
	}


	// ##### Schuljahresabschnitte #####

	/**
	 * Gibt alle Schuljahresabschnitte der Schule als sortierte Liste zurück (nach Schuljahr und Abschnitt).
	 * Virtuelle Abschnitte sind nicht enthalten, da sie keine Abschnitte der Schule sind.
	 *
	 * @return Alle Schuljahresabschnitte der Schule.
	 */
	public List<ReportingSchuljahresabschnitt> schuljahresabschnitte() {
		return mapSchuljahresabschnitte.values().stream().sorted(
				Comparator.comparing(ReportingSchuljahresabschnitt::schuljahr)
						.thenComparing(ReportingSchuljahresabschnitt::abschnitt))
				.toList();
	}

	/**
	 * Gibt den Schuljahresabschnitt zur übergebenen ID zurück. Berücksichtigt neben den Abschnitten der Schule auch die
	 * in diesem Report erzeugten virtuellen Abschnitte, sodass auch deren negative Pseudo-IDs auflösbar bleiben.
	 *
	 * @param id Die ID des angeforderten Schuljahresabschnitts.
	 *
	 * @return Der Schuljahresabschnitt zur ID oder null, falls keiner existiert.
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt(final long id) {
		final ReportingSchuljahresabschnitt schuljahresabschnitt = mapSchuljahresabschnitte.get(id);
		if (schuljahresabschnitt != null) {
			return schuljahresabschnitt;
		}
		return mapVirtuelleSchuljahresabschnitte.get(id);
	}

	/**
	 * Gibt den Schuljahresabschnitt zum angegebenen Schuljahr und Abschnitt zurück. Es werden ausschließlich die in der
	 * Datenbank vorhandenen Abschnitte der Schule betrachtet; wird auch für einen nicht vorhandenen Abschnitt ein
	 * Reporting-Objekt benötigt, so ist {@link #schuljahresabschnittOderVirtuell(int, int)} zu verwenden.
	 *
	 * @param schuljahr Das Schuljahr.
	 * @param abschnitt Der Abschnitt.
	 *
	 * @return Der Schuljahresabschnitt zu den Angaben oder null, falls keiner existiert.
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt(final int schuljahr, final int abschnitt) {
		final List<ReportingSchuljahresabschnitt> result =
				mapSchuljahresabschnitte.values().stream().filter(a -> (a.schuljahr() == schuljahr) && (a.abschnitt() == abschnitt)).toList();
		if (result.isEmpty()) {
			return null;
		}
		return result.getFirst();
	}

	/**
	 * Gibt den Schuljahresabschnitt zum angegebenen Schuljahr und Abschnitt zurück und erzeugt ihn als virtuellen
	 * Abschnitt, falls die Schule ihn in der Datenbank nicht besitzt. Der Rückgabewert ist daher nie {@code null}.
	 *
	 * <p>Zu verwenden überall dort, wo Schuljahr und Abschnitt aus fachlichen Daten abgeleitet werden und deshalb auch
	 * in der Zukunft liegen können — etwa bei einer GOSt-Blockung oder einer Fachwahlstatistik für einen Jahrgang, für
	 * den die betroffenen Abschnitte noch nicht angelegt sind. Was ein virtueller Abschnitt leisten kann und was nicht,
	 * beschreibt {@link ReportingSchuljahresabschnitt#istVirtuell()}.</p>
	 *
	 * <p>Virtuelle Abschnitte werden pro Report zwischengespeichert, sodass für dieselbe Kombination aus Schuljahr und
	 * Abschnitt stets dasselbe Objekt geliefert wird. Sie erscheinen bewusst nicht in {@link #schuljahresabschnitte()},
	 * da sie keine Abschnitte der Schule sind.</p>
	 *
	 * @param schuljahr Das Schuljahr.
	 * @param abschnitt Der Abschnitt.
	 *
	 * @return Der vorhandene Schuljahresabschnitt der Schule oder andernfalls ein virtueller Schuljahresabschnitt.
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnittOderVirtuell(final int schuljahr, final int abschnitt) {
		final ReportingSchuljahresabschnitt vorhandenerAbschnitt = schuljahresabschnitt(schuljahr, abschnitt);
		if (vorhandenerAbschnitt != null) {
			return vorhandenerAbschnitt;
		}
		// Die Pseudo-ID wird negativ vergeben, um Kollisionen mit den stets positiven Datenbank-IDs auszuschließen.
		// Der Abschnitt ist stets einstellig (die Schule kennt maximal vier Abschnitte), daher ist er als letzte Stelle eindeutig.
		final long idVirtuellerAbschnitt = -((schuljahr * 10L) + abschnitt);
		return mapVirtuelleSchuljahresabschnitte.computeIfAbsent(idVirtuellerAbschnitt, id -> {
			this.reportingContext.logger().logLn(LogLevel.DEBUG, 8,
					"Zum Schuljahr %d und Abschnitt %d existiert kein Schuljahresabschnitt der Schule. Es wird ein virtueller Abschnitt verwendet."
							.formatted(schuljahr, abschnitt));
			return new ProxyReportingSchuljahresabschnitt(this.reportingContext, id, schuljahr, abschnitt);
		});
	}

	/**
	 * Gibt den aktuellen Schuljahresabschnitt der Schule zurück.
	 *
	 * @return Der aktuelle Schuljahresabschnitt der Schule.
	 */
	public ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt() {
		return this.mapSchuljahresabschnitte.get(idAktuellerSchuljahresabschnitt);
	}

	/**
	 * Gibt den für den Druck ausgewählten Schuljahresabschnitt zurück.
	 *
	 * @return Der Schuljahresabschnitt der Auswahl für den Druck.
	 */
	public ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt() {
		return this.mapSchuljahresabschnitte.get(idAuswahlSchuljahresabschnitt);
	}

	/**
	 * Gibt an, ob die Schule eine gymnasiale Oberstufe (GOSt) besitzt. Gefragt wird direkt die Schulform: Die GOSt-Prüfung von {@code DBUtilsGost} meldet auch
	 * ein Schema ohne Schule mit derselben Ausnahme, und deren Deutung machte aus diesem Serverfehler die fachliche Aussage "keine gymnasiale Oberstufe".
	 *
	 * @return true, wenn die Schule über eine gymnasiale Oberstufe verfügt; false, wenn nicht.
	 */
	public boolean istSchuleMitGost() {
		if (istSchuleMitGost == null) {
			istSchuleMitGost = this.reportingContext.conn().getUser().schuleHatGymOb();
		}
		return istSchuleMitGost;
	}

	// ##### Schul-/Schema-weite E-Mail-Konfiguration #####

	/**
	 * Gibt einen neuen, schemaweit gültigen Default-{@link EmailJobContext} der Schule zurück (SMTP-Konfiguration
	 * etc.), welcher als Snapshot beim Erzeugen eines {@link de.svws_nrw.base.email.EmailJob} verwendet wird.
	 * Die Daten werden bei jedem Aufruf frisch aus der Datenbank gelesen, sodass jeder E-Mail-Job seinen eigenen,
	 * unabhängigen Kontext erhält.
	 *
	 * @return Der Default-Kontext für einen E-Mail-Job.
	 *
	 * @throws ApiOperationException Falls die Default-Konfiguration nicht ermittelt werden kann.
	 */
	public EmailJobContext defaultEmailJobContext() throws ApiOperationException {
		return DataEmailJobs.createDefaultJobContext(this.reportingContext.conn());
	}


}
