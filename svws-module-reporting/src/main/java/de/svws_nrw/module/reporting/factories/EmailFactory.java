package de.svws_nrw.module.reporting.factories;

import de.svws_nrw.base.email.EmailJob;
import de.svws_nrw.base.email.EmailJobAttachment;
import de.svws_nrw.base.email.EmailJobManagerContext;
import de.svws_nrw.base.email.EmailJobManagerFactory;
import de.svws_nrw.base.email.EmailJobStatus;
import de.svws_nrw.base.email.EmailJobRecipient;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.benutzer.BenutzerEMailDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp;
import de.svws_nrw.data.benutzer.DataBenutzerEMailDaten;
import de.svws_nrw.data.email.DataEmailJobs;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderPdf;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.person.ReportingPerson;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Versendet Report-Ergebnisse in Form von PDF-Dateien per E-Mail an die zugehörigen Personen.
 */
public final class EmailFactory {

	/**
	 * Blacklist mit E-Mail-Domains, an die kein Versand stattfinden soll. Die Domains werden in Kleinbuchstaben eingetragen.
	 * Die Domains smail.de und lmail.de sind real existierende Domains, die vom Anonymisierung-Tool für SchILD-NRW verwendet wurden und damit in vielen
	 * Beispieldatenbanken vorhanden sind.
	 */
	private static final Set<String> BLOCKED_EMAIL_DOMAINS = Set.of("smail.de", "lmail.de");

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingRepository reportingRepository;

	/**
	 * Erstelle eine neue Instanz von EmailFactory.
	 *
	 * @param reportingRepository das Repository für Reporting-Daten
	 */
	public EmailFactory(final ReportingRepository reportingRepository) {
		this.reportingRepository = reportingRepository;
	}

	/**
	 * Startet den E-Mail-Versand asynchron als Hintergrundjob. Es wird eine Job-ID zurückgegeben,
	 * mit der der Status und das Log später abgefragt werden können. Der eigentliche Versand
	 * erfolgt durch einen Hintergrund-Worker.
	 *
	 * @param pdfFactory Eine Instanz von PdfFactory, die die zu versendenden PDF-Dokumente bereitstellt.
	 *
	 * @return Response mit Informationen zum gestarteten Job (HTTP 202 Accepted)
	 *
	 * @throws ApiOperationException Falls ein schwerwiegender Fehler bei der Job-Initialisierung auftritt.
	 */
	public Response sendEmails(final PdfFactory pdfFactory) throws ApiOperationException {
		try {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 0, ">>> Beginne den Job für den asynchronen E-Mail-Versand zu starten.");

			final ReportingParameterTypisiert parameter = pruefeParameter();
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Parameter wurden geprüft und erfolgreich ermittelt.");

			final String subject = buildEMailBetreff(parameter);
			final String body = buildEMailHTMLBody(parameter);

			final @NotNull EmailJobManagerContext context = DataEmailJobs.getDefaultJobManagerContext(reportingRepository.conn());
			// E-Mail-Context Einstellungen abseits der Standardwerte setzen: Keine E-Mails ohne Anhang versenden.
			context.withFilterMailsWithoutAttachments(true);
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "EmailJobManagerContext mit SMTP Sitzung erstellt.");

			final String absenderEmail = ermittleAbsenderEmail();
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Absender-E-Mail-Adresse ermittelt.");

			final ReportingEMailEmpfaengerTyp empfaengerTyp = ermittleEmpfaengerTyp();
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Empfänger-Typ der E-Mail ermittelt.");

			final Map<Long, List<ReportBuilderPdf>> mapGruppiertePdfs = pdfFactory.getPdfBuildersById();
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "PDF-Builder in gruppierter Form erhalten.");

			final List<String> listUebersprungen = new ArrayList<>();
			final List<EmailJobRecipient> mapEmpfaengerEmailAnhaenge =
					sammleEmpfaengerUndAnhaenge(parameter, empfaengerTyp, mapGruppiertePdfs, listUebersprungen);
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4, "Empfänger und ihre Anhänge wurden zusammengestellt.");

			final var manager = EmailJobManagerFactory.getInstance().getManager(context);
			final @NotNull EmailJob job = new EmailJob(absenderEmail)
					.withSubject(subject)
					.withBody(body)
					.addRecipients(mapEmpfaengerEmailAnhaenge);
			job.logSkipped.addAll(listUebersprungen);
			final long jobId = manager.enqueue(job);

			final SimpleOperationResponse simple = new SimpleOperationResponse();
			simple.id = jobId;
			simple.success = true;
			simple.log.add("Der E-Mail-Versand wurde als Hintergrundjob gestartet mit Job-ID " + jobId);
			reportingRepository.logger().logLn(LogLevel.DEBUG, 0, "<<< Job für den asynchronen E-Mail-Versand wurde gestartet. Job-ID: " + jobId);

			return Response.status(Status.ACCEPTED).type(MediaType.APPLICATION_JSON).entity(simple).build();
		} catch (final Exception e) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4, "### FEHLER: Der E-Mail-Versand konnte nicht als Job gestartet werden.");
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "### FEHLER: Fehler beim Starten des E-Mail-Jobs.");
		}
	}


	/**
	 * Prüft, ob die notwendigen Parameter und E-Mail-Daten für den E-Mail-Versand vorhanden und korrekt gesetzt sind, und liest diese aus. Sollte eine der
	 * notwendigen Informationen fehlen oder nicht valide sein, wird eine Ausnahme ausgelöst.
	 *
	 * @return Das ReportingParameter-Objekt, das die nötigen Parameter und E-Mail-Daten enthält
	 *
	 * @throws ApiOperationException Fehler werfen, falls die benötigten Parameter oder E-Mail-Daten nicht vorhanden sind, unvollständig oder ungültig sind.
	 */
	private ReportingParameterTypisiert pruefeParameter() throws ApiOperationException {
		if ((this.reportingRepository.reportingParameter() == null) || (this.reportingRepository.reportingParameter().eMailDaten() == null)) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4,
					"### FEHLER: Der E-Mail-Versand wurde abgebrochen, da die notwendigen Parameter und E-Mail-Daten nicht übergeben wurden.");
			throw new ApiOperationException(Status.BAD_REQUEST, null, null, MediaType.APPLICATION_JSON);
		}
		final ReportingParameterTypisiert parameter = this.reportingRepository.reportingParameter();
		if ((parameter.eMailDaten().betreff == null) || parameter.eMailDaten().betreff.isBlank()
				|| (parameter.eMailDaten().text == null) || parameter.eMailDaten().text.isBlank()) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4,
					"### FEHLER: Der E-Mail-Versand wurde abgebrochen, da kein Betreff oder Text für die E-Mail angegeben wurde.");
			throw new ApiOperationException(Status.BAD_REQUEST, null, null, MediaType.APPLICATION_JSON);
		}

		return parameter;
	}

	/**
	 * Ermittelt die E-Mail-Adresse des aktuellen Benutzers. Falls keine gültige E-Mail-Adresse vorliegt oder ein Fehler auftritt, wird eine Ausnahme
	 * ausgelöst und der Prozess abgebrochen.
	 *
	 * @return Die ermittelte, gültige E-Mail-Adresse des aktuellen Benutzers
	 *
	 * @throws ApiOperationException Falls keine gültige E-Mail-Adresse gefunden wird oder ein Fehler beim Abrufen der Benutzerdaten auftritt.
	 */
	private String ermittleAbsenderEmail() throws ApiOperationException {
		String emailAdresse = "";
		final BenutzerEMailDaten benutzerEMailDaten;
		// E-Mail-Daten des aktuellen Benutzers ermitteln. Tritt dabei ein Fehler auf, so wird eine leere Adresse zurückgegeben.
		try {
			benutzerEMailDaten = new DataBenutzerEMailDaten(reportingRepository.conn()).getById(reportingRepository.conn().getUser().getId());
			if ((benutzerEMailDaten != null) && (benutzerEMailDaten.address != null) && !benutzerEMailDaten.address.isBlank())
				emailAdresse = benutzerEMailDaten.address.trim();
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			emailAdresse = "";
		}
		emailAdresse = validatedEmailOrEmpty(emailAdresse);

		if (emailAdresse.isBlank()) {
			reportingRepository.logger().logLn(LogLevel.ERROR, 4,
					"### FEHLER: Der E-Mail-Versand wurde abgebrochen, da für den aktuellen Benutzer keine gültige E-Mail-Adresse "
							+ "ermittelt werden konnte. Bitte überprüfen Sie die E-Mail-Einstellungen des Benutzers.");
			throw new ApiOperationException(Status.BAD_REQUEST, null, null, MediaType.APPLICATION_JSON);
		}
		return emailAdresse;
	}

	/**
	 * Ermittelt den aktuellen E-Mail-Empfänger-Typ aus den Reporting-Parametern.
	 * Falls kein gültiger Empfängertyp definiert ist, wird eine ApiOperationException ausgelöst.
	 *
	 * @return Der ermittelte Empfänger-Typ vom Typ {@link ReportingEMailEmpfaengerTyp}.
	 *
	 * @throws ApiOperationException Wenn kein gültiger Empfängertyp definiert ist, wird ein Fehler geworfen.
	 */
	private ReportingEMailEmpfaengerTyp ermittleEmpfaengerTyp() throws ApiOperationException {
		final ReportingParameterTypisiert parameter = this.reportingRepository.reportingParameter();
		if ((parameter != null) && (parameter.eMailDaten() != null)
				&& (ReportingEMailEmpfaengerTyp.getByID(parameter.eMailDaten().empfaengerTyp) != ReportingEMailEmpfaengerTyp.UNDEFINED)) {
			reportingRepository.logger().logLn(LogLevel.DEBUG, 4,
					"Der E-Mail-Empfänger-Typ wurde ermittelt: " + ReportingEMailEmpfaengerTyp.getByID(parameter.eMailDaten().empfaengerTyp).name());
			return ReportingEMailEmpfaengerTyp.getByID(parameter.eMailDaten().empfaengerTyp);
		}
		reportingRepository.logger().logLn(LogLevel.ERROR, 4, "### FEHLER: Es wurde kein gültiger Empfängertyp festgelegt");
		throw new ApiOperationException(Status.BAD_REQUEST, "### FEHLER: Es wurde kein gültiger Empfängertyp festgelegt.");
	}


	/**
	 * Sammelt E-Mail-Empfänger und ihre zugehörigen Anhänge anhand der bereitgestellten Parameter. Diese Methode verarbeitet eine Gruppierung von PDFs und
	 * weist sie den jeweiligen Empfängern zu, um so die vollständigen Daten für den E-Mail-Versand vorzubereiten. Nicht zuordenbare oder ungültige Einträge
	 * werden in einer separaten Liste aufgeführt.
	 *
	 * @param parameter Die Reporting-Parameter, die unter anderem Informationen zu E-Mail-Daten enthalten.
	 * @param empfaengerTyp Der Typ der Empfänger, der für die Ermittlung der E-Mail-Adressen benutzt wird.
	 * @param mapGruppiertePdfs Eine Zuordnung von IDs zu einer Liste von PdfBuilder-Objekten, die die PDFs repräsentieren.
	 * @param listUebersprungen Die Liste, in der Informationen über übersprungene Datensätze gesammelt werden.
	 *
	 * @return Eine Zuordnung von E-Mail-Adressen zu Empfänger-Objekten und deren zugehörigen Anhängen.
	 *
	 * @throws ApiOperationException Wenn beim Prozessieren der Daten ein Fehler auftritt, wird diese Exception geworfen.
	 */
	private List<EmailJobRecipient> sammleEmpfaengerUndAnhaenge(final ReportingParameterTypisiert parameter,
			final ReportingEMailEmpfaengerTyp empfaengerTyp, final Map<Long, List<ReportBuilderPdf>> mapGruppiertePdfs, final List<String> listUebersprungen)
			throws ApiOperationException {

		final Map<String, EmailJobRecipient> mapEmpfaengerEmailAnhaenge = new HashMap<>();

		for (final Map.Entry<Long, List<ReportBuilderPdf>> entry : mapGruppiertePdfs.entrySet()) {

			final Long id = entry.getKey();
			final List<ReportBuilderPdf> pdfBuilders = entry.getValue();

			if (id == null)
				continue;

			if (id < 0) {
				listUebersprungen.add("- HINWEIS: Es gab PDF-Dateien, die keinem Empfänger zugeordnet werden konnten. Diese werden nicht versendet.");
				continue;
			}

			if ((pdfBuilders == null) || pdfBuilders.isEmpty()) {
				listUebersprungen.add("- Für die ID " + id + " konnten keine PDFs gefunden werden und damit kein E-Mail-Versand erfolgen.");
				continue;
			}

			final List<ReportingPerson> empfaengerPersonen = ermittleEmpfaengerPersonen(id, empfaengerTyp);
			if (empfaengerPersonen.isEmpty()) {
				listUebersprungen.add("- Für die ID " + id + " konnten keine Empfänger ermittelt werden. Sie wird beim E-Mail-Versand übersprungen.");
				continue;
			}

			final List<EmailJobAttachment> attachments = new ArrayList<>();

			for (final ReportBuilderPdf pdfBuilder : pdfBuilders) {
				try {
					attachments.add(new EmailJobAttachment(pdfBuilder.getDateinameMitEndung(), pdfBuilder.generate(), "application/pdf"));
				} catch (final Exception e) {
					reportingRepository.logger().logLn(LogLevel.ERROR, 4,
							"### FEHLER: PDF-Datei '" + pdfBuilder.getDateiname() + "' für ID " + id + " konnte nicht generiert werden: "
									+ e.getMessage());
					throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
							"### FEHLER: PDF-Datei '" + pdfBuilder.getDateiname() + "' für ID " + id + " konnte nicht generiert werden: "
									+ e.getMessage());
				}
			}

			sammleEmpfaengerUndAnhaengeFuerAttachments(id, empfaengerPersonen, parameter.eMailDaten().istPrivateEmailAlternative, attachments,
					mapEmpfaengerEmailAnhaenge, listUebersprungen);
		}

		return new ArrayList<>(mapEmpfaengerEmailAnhaenge.values());
	}


	/**
	 * Hilfsmethode zu sammleEmpfaengerUndAnhaenge, welche für die Anhänge eines PDF-Builders die E-Mail-Empfänger bestimmt und diesen zuordnet.
	 * Die Zuordnung erfolgt über die in dem Parameter mapEmpfaengerEmailAnhaenge übergebene Map, welche im Allgemeinen schon durch vorige
	 * Aufrufe Zuordnungen von anderen Anhängen beinhaltet.
	 *
	 * @param id                            die ID
	 * @param empfaengerPersonen            die Personen-Objekte für den Empfänger
	 * @param nutzeAlternativPrivateEmail   gibt an, ob von der Person auch die private E-Mail-Adresse genutzt werden kann
	 * @param attachments                   die Anhänge, die den Empfänger-Personen zugeordnet werden können
	 * @param mapEmpfaengerEmailAnhaenge    die Map, welche mit dieser Methode schrittweise aufgebaut wird und die Zuordnung der Anhänge beinhaltet
	 * @param listUebersprungen             die Liste, in der Informationen über übersprungene Datensätze gesammelt werden
	 */
	private static void sammleEmpfaengerUndAnhaengeFuerAttachments(final long id, final List<ReportingPerson> empfaengerPersonen,
			final boolean nutzeAlternativPrivateEmail, final List<EmailJobAttachment> attachments,
			final Map<String, EmailJobRecipient> mapEmpfaengerEmailAnhaenge, final List<String> listUebersprungen) {
		for (final ReportingPerson empfaengerPerson : empfaengerPersonen) {
			final String empfaengerEmail = ermittleEMailAdresseZurPerson(empfaengerPerson, nutzeAlternativPrivateEmail);

			if (empfaengerEmail.isBlank()) {
				listUebersprungen.add("- Für die ID " + id + " konnte keine gültige E-Mail-Adresse des Empfängers ermittelt werden. Sie wird beim "
						+ "E-Mail-Versand übersprungen.");
				continue;
			}

			// Überprüfung der E-Mail-Domain.
			final int atIndex = empfaengerEmail.lastIndexOf('@');
			if ((atIndex <= 0) || (atIndex == (empfaengerEmail.length() - 1))) {
				listUebersprungen.add("- Die E-Mail-Adresse '" + empfaengerEmail + "' für ID " + id + " ist ungültig (fehlende oder fehlerhafte Domain).");
				continue;
			}
			final String domain = empfaengerEmail.substring(atIndex + 1).toLowerCase(Locale.ROOT);
			if (BLOCKED_EMAIL_DOMAINS.contains(domain)) {
				listUebersprungen.add("- Die E-Mail an " + empfaengerEmail + " konnte nicht versendet werden, da die Domain als unzulässig markiert wurde.");
				continue;
			}

			final EmailJobRecipient empfaengerEmailAnhaenge =
					mapEmpfaengerEmailAnhaenge.computeIfAbsent(empfaengerEmail, k -> new EmailJobRecipient(empfaengerEmail));
			empfaengerEmailAnhaenge.attachments.addAll(attachments);
		}
	}

	/**
	 * Ermittelt eine Liste von Empfänger-Personen basierend auf der angegebenen ID und dem spezifizierten Typ.
	 * Die Methode verarbeitet verschiedene Modi und gibt die entsprechenden Daten aus dem Reporting-Repository zurück.
	 *
	 * @param id Die ID, anhand der die zu ermittelnden Personen identifiziert werden
	 * @param typ Der Typ, der den Typ der zu ermittelnden Empfänger vorgibt.
	 *
	 * @return Eine Liste von ReportingPerson-Objekten, die die ermittelten Empfänger darstellen.
	 */
	private List<ReportingPerson> ermittleEmpfaengerPersonen(final long id, final ReportingEMailEmpfaengerTyp typ) throws ApiOperationException {
		return switch (typ) {
			case SCHUELER -> {
				final ReportingSchueler schueler = reportingRepository.schueler(id);
				yield (schueler == null) ? new ArrayList<>() : List.of(schueler);
			}
			case LEHRER -> {
				final ReportingLehrer lehrer = reportingRepository.lehrer(id);
				yield (lehrer == null) ? new ArrayList<>() : List.of(lehrer);
			}
			case KLASSENLEHRER -> {
				final ReportingKlasse klasse = reportingRepository.klasse(id);
				if (klasse == null)
					yield new ArrayList<>();
				final List<ReportingLehrer> lehrer = (klasse.klassenlehrer() == null) ? new ArrayList<>() : klasse.klassenlehrer();
				yield new ArrayList<>(lehrer);
			}
			case KURSLEHRER -> {
				final ReportingKurs kurs = reportingRepository.mapKurse().get(id);
				if (kurs == null)
					yield new ArrayList<>();
				final List<ReportingLehrer> lehrer = (kurs.kurslehrer() == null) ? new ArrayList<>() : kurs.kurslehrer();
				yield new ArrayList<>(lehrer);
			}
			case GOSTKURSPLANUNG_KURSLEHRER -> {
				final ReportingGostKursplanungKurs kurs = reportingRepository.mapGostKursplanungKurse().get(id);
				if (kurs == null)
					yield new ArrayList<>();
				final List<ReportingLehrer> lehrer = (kurs.lehrkraefte() == null) ? new ArrayList<>() : kurs.lehrkraefte();
				yield new ArrayList<>(lehrer);
			}
			default -> {
				reportingRepository.logger().logLn(LogLevel.ERROR, 4,
						"### FEHLER: Es wurde kein gültiger Empfängertyp festgelegt. Der Versand der E-Mails wurde abgebrochen.");
				throw new ApiOperationException(Status.NOT_FOUND, null, null, MediaType.APPLICATION_JSON);
			}
		};
	}


	/**
	 * Bricht einen laufenden oder geplanten E-Mail-Job ab.
	 *
	 * @param idJob   die Job-ID
	 *
	 * @return Response mit Ergebnis der Abbruch-Anforderung
	 */
	public Response cancelEmailJob(final long idJob) {
		final var manager =
				EmailJobManagerFactory.getInstance().getManagerByUser(reportingRepository.conn().getDBSchema(), reportingRepository.conn().getUser().getId());
		if (manager == null) {
			final SimpleOperationResponse notFound = new SimpleOperationResponse();
			notFound.success = false;
			notFound.log.add("E-Mail-Job-Manager nicht gefunden für: " + reportingRepository.conn().getUser().getId() + " ("
					+ reportingRepository.conn().getDBSchema() + ")");
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(notFound).build();
		}
		final EmailJob job = manager.getJob(idJob);
		if (job == null) {
			final SimpleOperationResponse notFound = new SimpleOperationResponse();
			notFound.success = false;
			notFound.log.add("Job nicht gefunden: " + idJob);
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(notFound).build();
		}
		final boolean ok = manager.cancelJob(idJob);
		final SimpleOperationResponse simple = new SimpleOperationResponse();
		simple.success = ok;
		if (!ok) {
			simple.log.add("Abbruch fehlgeschlagen für Job: " + idJob);
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(simple).build();
		}
		if ((job.getStatus() == EmailJobStatus.COMPLETED_SUCCESSFULLY) || (job.getStatus() == EmailJobStatus.COMPLETED_WITH_ERRORS)
				|| (job.getStatus() == EmailJobStatus.FAILED) || (job.getStatus() == EmailJobStatus.CANCELED))
			simple.log.add("Job war bereits beendet (Status=" + job.getStatus() + ").");
		else
			simple.log.add("Abbruch wurde veranlasst.");
		return Response.ok(simple).type(MediaType.APPLICATION_JSON).build();
	}



	// ##### Hilfsmethoden #####

	/**
	 * Liefert die E-Mail-Adresse einer angegebenen Person. Falls die Schul-E-Mail-Adresse nicht vorhanden oder ungültig ist, wird die private E-Mail-Adresse
	 * verwendet, sofern diese als Alternative erlaubt ist. Gibt einen leeren String zurück, wenn keine gültige E-Mail-Adresse gefunden wurde.
	 *
	 * @param reportingPerson Die Person, von der die E-Mail-Adresse abgerufen werden soll.
	 * @param istPrivateEmailAlternative Gibt an, ob auch die private E-Mail-Adresse der Peron berücksichtigt werden soll.
	 *
	 * @return Die gültige E-Mail-Adresse der Person oder ein leerer String, wenn keine gültige E-Mail-Adresse gefunden wurde.
	 */
	private static String ermittleEMailAdresseZurPerson(final ReportingPerson reportingPerson, final boolean istPrivateEmailAlternative) {
		if (reportingPerson == null)
			return "";

		String eMailAddress = validatedEmailOrEmpty(reportingPerson.emailSchule());

		if (eMailAddress.isBlank() && istPrivateEmailAlternative)
			eMailAddress = validatedEmailOrEmpty(reportingPerson.emailPrivat());

		return eMailAddress;
	}

	/**
	 * Validiert die angegebene E-Mail-Adresse und gibt sie in bereinigter Form zurück, wenn sie gültig ist. Andernfalls wird ein leerer String zurückgegeben.
	 *
	 * @param emailAddress Die zu validierende E-Mail-Adresse.
	 *
	 * @return Die bereinigte und validierte E-Mail-Adresse in Kleinbuchstaben, oder ein leerer String, wenn die Eingabe ungültig ist.
	 */
	private static String validatedEmailOrEmpty(final String emailAddress) {
		if ((emailAddress == null) || emailAddress.isBlank())
			return "";
		// E-Mail-Adresse bearbeiten ...
		final String resultEmailAddress = emailAddress.trim().toLowerCase(Locale.ROOT);
		// ... und dann validieren.
		if (isValidEmail(resultEmailAddress))
			return resultEmailAddress;
		return "";
	}

	/**
	 * Überprüft, ob die angegebene E-Mail-Adresse gültig ist.
	 *
	 * @param emailAddress   die E-Mail-Adresse, die überprüft werden soll.
	 *
	 * @return true, wenn die E-Mail-Adresse gültig ist, andernfalls false.
	 */
	private static boolean isValidEmail(final String emailAddress) {
		if ((emailAddress == null) || emailAddress.isBlank())
			return false;
		try {
			final InternetAddress address = new InternetAddress(emailAddress);
			address.validate();
			return true;
		} catch (@SuppressWarnings("unused") final AddressException addressException) {
			return false;
		}
	}

	/**
	 * Erstellt den Betreff einer E-Mail basierend auf den übergebenen Reporting-Parametern und validiert, ob ein gültiger Betreff vorliegt. Falls kein
	 * Betreff angegeben ist, wird ein Fehler geloggt und eine ApiOperationException ausgelöst.
	 *
	 * @param parameter Die ReportingParameter, die die relevanten Daten für die E-Mail enthalten. Insbesondere wird das Feld {@code eMailDaten.betreff}
	 *                  verwendet.
	 *
	 * @return Der generierte Betreff der E-Mail als String.
	 *
	 * @throws ApiOperationException Falls kein gültiger Betreff definiert ist.
	 */
	private String buildEMailBetreff(final ReportingParameterTypisiert parameter) throws ApiOperationException {
		if ((parameter != null) && (parameter.eMailDaten() != null) && (parameter.eMailDaten().betreff != null) && (!parameter.eMailDaten().betreff.isBlank())) {
			return parameter.eMailDaten().betreff;
		}
		reportingRepository.logger().logLn(LogLevel.ERROR, 4,
				"### FEHLER: Der E-Mail-Versand wurde abgebrochen, da kein Betreff für die E-Mail angegeben wurde.");
		throw new ApiOperationException(Status.BAD_REQUEST, null, null, MediaType.APPLICATION_JSON);
	}

	/**
	 * Erzeugt den HTML-Body für die E-Mail basierend auf den übergebenen Parametern.
	 * Der Text wird aus einem Plain-Text-Feld entnommen, normalisiert, in HTML umgewandelt und strukturiert.
	 * Leerzeilen und Zeilenumbrüche werden dabei entsprechend formatiert.
	 *
	 * @param parameter Die Parameter, die die E-Mail-Daten, einschließlich des Textes, enthalten
	 *
	 * @return der generierte E-Mail-Text als HTML-String
	 *
	 * @throws ApiOperationException Falls die übergebenen Parameter ungültig sind oder der Text fehlt
	 */
	private String buildEMailHTMLBody(final ReportingParameterTypisiert parameter) throws ApiOperationException {
		if ((parameter != null) && (parameter.eMailDaten() != null) && (parameter.eMailDaten().text != null) && (!parameter.eMailDaten().text.isBlank())) {
			// Der Text kommt als Plain-Text aus dem Client. Normalisiere zunächst den übergebenen Text.
			final String emailBodyPlainText = normalisierterPlainText(parameter.eMailDaten().text);

			// Plain-Text in HTML transformieren. Dazu HTML-escaped	Text erstellen.
			final String escaped = htmlEscape(emailBodyPlainText);

			// Einen StringBuilder für den HTML-Code erzeugen und ein minimales HTML-Grundgerüst einfügen, damit Clients korrekt als HTML rendern.
			final StringBuilder html = new StringBuilder(escaped.length() + 128);

			html.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head><body>");

			// Teile den Text in Absätze. Doppel-CRLF werden als Absatz (<p>) behandelt, Einzel-CRLF als Zeilenumbruch.
			final String[] absaetze = escaped.split("\\r\\n\\r\\n");
			for (final String absatz : absaetze) {
				// Ersetze verbleibende Einzelzeilenumbrüche im Absatz durch <br>
				final String absatzMitBr = absatz.replace("\r\n", "<br>");
				// Leere Absätze als echte Leerzeile darstellen
				if (absatzMitBr.isBlank()) {
					html.append("<p><br></p>");
				} else {
					html.append("<p>").append(absatzMitBr).append("</p>");
				}
			}

			html.append("</body></html>");

			return html.toString();
		}
		reportingRepository.logger().logLn(LogLevel.ERROR, 4,
				"### FEHLER: Der E-Mail-Versand wurde abgebrochen, da kein Text für die E-Mail angegeben wurde.");
		throw new ApiOperationException(Status.BAD_REQUEST, null, null, MediaType.APPLICATION_JSON);
	}

	/**
	 * Normalisiert den gegebenen Text, indem Zeilenumbrüche vereinheitlicht, überflüssige Leerzeilen reduziert,
	 * Tabs durch Leerzeichen ersetzt und abschließende Leerzeilen sichergestellt werden.
	 * Der Rückgabewert enthält abschließend Zeilenumbrüche im CRLF-Format zur maximalen Kompatibilität.
	 *
	 * @param plainText Der ursprüngliche Text, der normalisiert werden soll.
	 *
	 * @return Der normalisierte Text mit angepassten Zeilenumbrüchen und Leerzeilen.
	 */
	private static String normalisierterPlainText(final String plainText) {
		// 1) Normalisiere Zeilenumbrüche auf \n
		String result = plainText.replace("\r\n", "\n").replace("\r", "\n");

		// 2) Trimme führende/abschließende Spaces pro Zeile und reduziere doppelte Leerzeilen
		final String[] zeilen = result.split("\n", -1);
		final StringBuilder normalisierterText = new StringBuilder(result.length());
		boolean warLetzteZeileLeer = false;
		for (final String zeile : zeilen) {
			// Leerzeichen und white spaces entfernen.
			final String neueZeile = (zeile == null) ? "" : zeile.strip();
			if (neueZeile.isEmpty()) {
				// Reduziere mehrere Leerzeilen auf eine Leerzeile.
				if (!warLetzteZeileLeer) {
					normalisierterText.append('\n');
					warLetzteZeileLeer = true;
				}
			} else {
				// Ersetze Tabs durch 4 Spaces
				final String zeileOhneTabs = neueZeile.replace("\t", "    ");
				normalisierterText.append(zeileOhneTabs).append('\n');
				warLetzteZeileLeer = false;
			}
		}
		result = normalisierterText.toString();

		// 3) Sicherstellen, dass eine abschließende Leerzeile existiert.
		if (!result.endsWith("\n\n")) {
			if (result.endsWith("\n")) {
				result = result + "\n";
			} else {
				result = result + "\n\n";
			}
		}

		// 4) Konvertiere die Zeilenumbrüche final auf CRLF für maximale Client-Kompatibilität
		result = result.replace("\n", "\r\n");
		return result;
	}

	/**
	 * Wandelt ein übergebenes Text-String-Objekt in einen HTML-escaped String um. Zeichen wie `&`, `<`, `>` und `"` werden durch HTML-Entities ersetzt.
	 *
	 * @param text Der Eingabetext, der HTML-maskiert werden soll. Wenn der Text null oder leer ist, wird ein leerer String zurückgegeben.
	 *
	 * @return Der HTML-escaped String, der für die Verwendung in HTML-Inhalten geeignet ist.
	 */
	private static String htmlEscape(final String text) {
		if ((text == null) || text.isEmpty())
			return "";
		final StringBuilder stringBuilder = new StringBuilder(text.length());
		for (int i = 0; i < text.length(); i++) {
			final char c = text.charAt(i);
			switch (c) {
				case '&' -> stringBuilder.append("&amp;");
				case '<' -> stringBuilder.append("&lt;");
				case '>' -> stringBuilder.append("&gt;");
				case '"' -> stringBuilder.append("&quot;");
				case '\'' -> stringBuilder.append("&#39;");
				default -> stringBuilder.append(c);
			}
		}
		return stringBuilder.toString();
	}

}
