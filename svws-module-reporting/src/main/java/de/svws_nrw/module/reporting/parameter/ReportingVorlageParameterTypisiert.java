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
	 * Erstellt eine Instanz und wandelt den Wert des übergebenen ReportingVorlageParameter in den Zieltyp T um. Lässt sich der Wert nicht in den Typ
	 * des Vorlage-Parameters wandeln, wird eine ApiOperationException geworfen.
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
	private static <T> T getTypisiertenWert(final ReportingReportvorlageParameter reportingReportVorlageParameter) throws ApiOperationException {
		return getTypisiertenWert(reportingReportVorlageParameter.typ, reportingReportVorlageParameter.wert);
	}

	/**
	 * Konvertiert den übergebenen Wert in den entsprechenden Zieltyp T basierend auf dem übergebenen Parameter-Typ.
	 *
	 * @param <T>   Der Zieltyp, in den der Wert konvertiert werden soll
	 * @param typ   Der Typ des Parameters gemäß {@link ReportingReportvorlageParameterTyp}
	 * @param wert  Der zu konvertierende Wert als Zeichenkette
	 *
	 * @return Der in den Zieltyp T konvertierte Wert
	 *
	 * @throws ApiOperationException Wenn die Konvertierung fehlschlägt oder ein ungültiger Typ angegeben wurde
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getTypisiertenWert(final int typ, final String wert) throws ApiOperationException {
		final String s = (wert == null) ? "" : wert.trim();
		try {
			return switch (ReportingReportvorlageParameterTyp.getByID(typ)) {
				case BOOLEAN -> (T) Boolean.valueOf(Boolean.parseBoolean(s));
				case INTEGER -> (T) Integer.valueOf(s.isEmpty() ? -1 : Integer.parseInt(s));
				case LONG -> (T) Long.valueOf(s.isEmpty() ? -1L : Long.parseLong(s));
				case DECIMAL -> (T) Double.valueOf(s.isEmpty() ? -1.0 : Double.parseDouble(s));
				case STRING, UNDEFINED -> (T) s;
			};
		} catch (final Exception e) {
			// Der Wert stammt aus der Vorlagendefinition auf dem Server; Werte aus Request und Konfiguration prüft der ParameterBuilder vorher auf Typkonformität.
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e, "### FEHLER: Ein Vorlagenparameter des Reports ist auf dem Server ungültig.");
		}
	}

	/**
	 * Prüft vorab, ob der übergebene Wert für den angegebenen Parameter-Typ gültig ist und somit ohne Fehler und ohne stillen Bedeutungsverlust
	 * konvertiert werden kann. Für BOOLEAN sind nur "true" und "false" (unabhängig von Groß-/Kleinschreibung) gültig, da Boolean.parseBoolean alle
	 * anderen Zeichenketten still als false interpretiert. Für die Zahlentypen wird die zentrale Konvertierung selbst als Prüfung verwendet, wobei
	 * der leere String gültig ist und bei der Konvertierung als -1 interpretiert wird. Zeichenketten-Typen sind immer gültig.
	 *
	 * @param typ   Der Typ des Parameters gemäß {@link ReportingReportvorlageParameterTyp}
	 * @param wert  Der zu prüfende Wert als Zeichenkette
	 *
	 * @return true, wenn der Wert typkonform konvertiert werden kann, ansonsten false
	 */
	public static boolean istWertTypkonform(final int typ, final String wert) {
		final String s = (wert == null) ? "" : wert.trim();
		if (ReportingReportvorlageParameterTyp.getByID(typ) == ReportingReportvorlageParameterTyp.BOOLEAN) {
			return "true".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s);
		}
		try {
			getTypisiertenWert(typ, s);
			return true;
		} catch (final ApiOperationException e) {
			return false;
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
