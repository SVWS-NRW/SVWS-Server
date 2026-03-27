package de.svws_nrw.module.reporting.parameter;


import java.util.Objects;

import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;


/**
 * Repräsentiert einen typisierten Vorlage-Parameter für Berichtsvorlagen, der sowohl einen Namen als auch
 * einen Wert des Typs T enthält. Diese Klasse ermöglicht die Nutzung und Konvertierung von Vorlage-Parametern
 * innerhalb des Reportings.
 *
 * @param <T> der Typ des Wertes des Vorlage-Parameters
 */
public class ReportingVorlageParameterTypisiert<T> {

	/** Der Name des Vorlage-Parameters, wie er später im HTML-Template verwendet wird. */
	protected String name = "";

	/** Der Wert des Vorlage-Parameters. */
	protected T wert;

	/**
	 * Der Konstruktor für die Klasse ReportingTypisierterVorlageParameter, der den Namen und den Wert eines Vorlage-Parameters setzt.
	 *
	 * @param name der Name des Vorlage-Parameters, wie er im HTML-Template verwendet wird
	 * @param wert der Wert des Vorlage-Parameters
	 */
	public ReportingVorlageParameterTypisiert(final String name, final T wert) {
		this.name = name;
		this.wert = wert;
	}

	/**
	 * Erstellt eine Instanz und wandelt den Wert des übergebenen ReportingVorlageParameter in den Zieltyp T um,
	 * sofern T mit dem Typ des Vorlage-Parameters kompatibel ist. Andernfalls wird eine IllegalArgumentException geworfen.
	 *
	 * @param reportingReportVorlageParameter der Vorlage-Parameter
	 * @throws ApiOperationException falls der Zieltyp nicht mit dem Parameter-Typ kompatibel ist oder die Konvertierung fehlschlägt
	 */
	public ReportingVorlageParameterTypisiert(final ReportingReportvorlageParameter reportingReportVorlageParameter) throws ApiOperationException {
		this.name = reportingReportVorlageParameter.name;
		this.wert = getTypisiertenWert(reportingReportVorlageParameter);
	}

	/**
	 * Überprüft, ob das angegebene Objekt gleich der aktuellen Instanz ist.
	 *
	 * @param o das Objekt, das mit der aktuellen Instanz verglichen werden soll
	 *
	 * @return true, wenn das angegebene Objekt gleich der aktuellen Instanz ist, andernfalls false
	 */
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (getClass() != o.getClass())) {
			return false;
		}
		final ReportingVorlageParameterTypisiert<?> that = (ReportingVorlageParameterTypisiert<?>) o;
		return Objects.equals(this.name, that.name);
	}

	/**
	 * Berechnet den Hashcode für die aktuelle Instanz basierend auf dem Attribut "name".
	 *
	 * @return der berechnete Hashcode-Wert der Instanz
	 */
	public int hashCode() {
		return Objects.hash(this.name);
	}

	/**
	 * Konvertiert den Wert eines übergebenen ReportingVorlageParameter in den entsprechenden Zieltyp T basierend auf dem Typ des Parameters.
	 *
	 * @param <T> Der Zieltyp, in den der Wert konvertiert werden soll
	 * @param reportingReportVorlageParameter Das Objekt, das den zu konvertierenden Wert sowie dessen Typ angibt
	 *
	 * @return Der in den Zieltyp T konvertierte Wert
	 *
	 * @throws ApiOperationException Wenn die Konvertierung fehlschlägt oder ein ungültiger Typ angegeben wurde
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getTypisiertenWert(final ReportingReportvorlageParameter reportingReportVorlageParameter) throws ApiOperationException {
		final String s = (reportingReportVorlageParameter.wert == null) ? "" : reportingReportVorlageParameter.wert.trim();
		try {
			return switch (ReportingReportvorlageParameterTyp.getByID(reportingReportVorlageParameter.typ)) {
				case BOOLEAN -> (T) Boolean.valueOf(Boolean.parseBoolean(s));
				case INTEGER -> (T) Integer.valueOf(s.isEmpty() ? -1 : Integer.parseInt(s));
				case LONG -> (T) Long.valueOf(s.isEmpty() ? -1L : Long.parseLong(s));
				case DECIMAL -> (T) Double.valueOf(s.isEmpty() ? -1.0 : Double.parseDouble(s));
				case STRING, UNDEFINED -> (T) s;
			};
		} catch (final Exception e) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e,
					"Konvertierung des Werts " + s + " für die Vorlagenparameter " + reportingReportVorlageParameter.name + " fehlgeschlagen.");
		}
	}


	/**
	 * @return Name des Parameters
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return typisierter Wert
	 */
	public T getWert() {
		return wert;
	}

}
