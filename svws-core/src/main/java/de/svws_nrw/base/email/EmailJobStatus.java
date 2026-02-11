package de.svws_nrw.base.email;

/**
 * Diese Aufzählung beschreibt den Status eines Jobs zum asynchronen Versenden von Emails.
 */
public enum EmailJobStatus {

	/** Der Job ist in der Warteschlange und bereit zur Ausführung. */
	QUEUED,

	/** Der Job wird aktuell bearbeitet. */
	SENDING,

	/** Der Job wurde erfolgreich abgeschlossen. */
	COMPLETED_SUCCESSFULLY,

	/** Der Job wurde erfolgreich abgeschlossen. */
	COMPLETED_WITH_ERRORS,

	/** Der Job wurde abgebrochen. */
	CANCELED,

	/** Der Job ist fehlgeschlagen. */
	FAILED

}
