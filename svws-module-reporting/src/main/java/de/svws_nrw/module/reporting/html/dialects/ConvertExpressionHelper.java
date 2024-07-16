package de.svws_nrw.module.reporting.html.dialects;

import de.svws_nrw.core.utils.DateUtils;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


/**
 * Die Klasse stellt Hilfsmethoden zur Datumsverarbeitung zur Verfügung, die über einen Thymeleaf-Dialect
 * und dessen ExpressionFactory in HTML-Templates verwendet werden können.
 */
public class ConvertExpressionHelper {

	/**
	 * Erstellt einen neuen SVWSDateExpressionHelper
	 */
	public ConvertExpressionHelper() {
		// leerer Konstruktor, um diesen mit einem JavaDoc-Kommentar versehen zu können.
	}

	/**
	 * Gibt ein als String im ISO-Format vorliegendes Datum als Objekt vom Typ Date zurück.
	 * @param dateISO8601		Der String, der das im ISO-Format yyyy-MM-dd vorliegende Datum enthält.
	 * @return					Das übergebene Datum als Date-Objekt.
	 */
	public Date toDateObject(final String dateISO8601) {
		final @NotNull int[] info = DateUtils.extractFromDateISO8601(dateISO8601);
		return Date.from(LocalDate.of(info[0], info[1], info[2]).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Gibt ein als String im ISO-Format vorliegendes Datum als String im deutschen Datumsformat zurück.
	 * @param dateISO8601		Der String, der das im ISO-Format yyyy-MM-dd vorliegende Datum enthält.
	 * @return					Das übergebene Datum im deutschen Format.
	 */
	public String toDateDE(final String dateISO8601) {
		if ((dateISO8601 == null) || dateISO8601.isEmpty())
			return "";
		try {
			return DateUtils.gibDatumGermanFormat(dateISO8601);
		} catch (final Exception ignore) {
			return "";
		}
	}

	/**
	 * Gibt ein als String im ISO-Format vorliegendes Datum als String im deutschen Datumsformat mit ausgeschriebenem Monat zurück.
	 * @param dateISO8601		Der String, der das im ISO-Format yyyy-MM-dd vorliegende Datum enthält.
	 * @return					Das übergebene Datum im deutschen Format mit ausgeschriebenem Monat.
	 */
	public String toDateDELong(final String dateISO8601) {
		if ((dateISO8601 == null) || dateISO8601.isEmpty())
			return "";
		try {
			return DateUtils.gibDatumGermanFormatAusgeschrieben(dateISO8601);
		} catch (final Exception ignore) {
			return "";
		}
	}

	/**
	 * Gibt ein als String im ISO-Format vorliegendes Datum als String im deutschen Datumsformat mit ausgeschriebenem Monat zurück.
	 * @param dateISO8601		Der String, der das im ISO-Format yyyy-MM-dd vorliegende Datum enthält.
	 * @return					Das übergebene Datum im deutschen Format mit ausgeschriebenem Monat.
	 */
	public String toDateDEWochentag(final String dateISO8601) {
		if ((dateISO8601 == null) || dateISO8601.isEmpty())
			return "";
		try {
			final String[] wochentage =  new String[] {"", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
			return wochentage[DateUtils.gibWochentagDesDatumsISO8601(dateISO8601)];
		} catch (final Exception ignore) {
			return "";
		}
	}

}
