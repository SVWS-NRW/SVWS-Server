package de.svws_nrw.module.reporting.utils;

/**
 * Runtime-Exception für Fehler beim Nachladen / Lazy-Loading von Reporting-Daten.
 * Wird genutzt, wenn innerhalb von Lambdas keine checked Exceptions geworfen werden können.
 */
public class ReportingDataLoadException extends RuntimeException {

	/**
	 * Konstruktor für ReportingDataLoadException, der die Ursache der Exception übernimmt.
	 *
	 * @param cause Die Ursache der Exception, die weitergegeben werden soll.
	 */
	public ReportingDataLoadException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Konstruktor für ReportingDataLoadException, der eine Nachricht und die Ursache
	 * der Exception übernimmt.
	 *
	 * @param message Die Nachricht, die die Ausnahme beschreibt.
	 * @param cause Die Ursache der Exception, die weitergegeben werden soll.
	 */
	public ReportingDataLoadException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
