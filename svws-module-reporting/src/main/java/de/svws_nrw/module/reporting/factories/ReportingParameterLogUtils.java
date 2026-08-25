package de.svws_nrw.module.reporting.factories;

import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;

/**
 * Formt die Kenndaten einer Reporting-Anfrage zu einer Zeile für das Log.
 * <p>Die Meldungen eines Abbruchs nennen keine technischen Werte. Der Entwickler braucht sie trotzdem, denn der Anwender meldet "der Druck geht nicht"
 * und nicht den Request. Diese Zeile hält die Kenndaten deshalb einmal am Eingang fest.</p>
 * <p>Die Formatierung liegt außerhalb der {@link ReportingFactory}, damit sie ohne Datenbankverbindung prüfbar ist.</p>
 * <p><b>Sie läuft vor jeder inhaltlichen Prüfung und arbeitet damit auf ungeprüften Daten aus dem Request.</b> Jedes Feld ist deshalb einzeln nullsicher,
 * auch dort, wo das Datenobjekt {@code @NotNull} zusagt: Diese Zusage ist Bean-Validation, und die Reporting-Endpunkte werten sie nicht aus. Scheiterte die
 * Zeile selbst, verdeckte ihr Fehler genau den Fehler, den sie dokumentieren soll.</p>
 * <p>Freier Text aus dem Request wird maskiert und gekürzt. Im Fehlerfall geht das Log über den globalen Logger auf die Server-Konsole und als Antwort an
 * den Client; ein Steuerzeichen im Vorlagennamen erzeugte dort eine zweite Zeile, die wie ein eigener Log-Eintrag aussieht.</p>
 */
final class ReportingParameterLogUtils {

	/** Die Höchstlänge des ausgegebenen Vorlagennamens, das Auslassungszeichen eingerechnet. */
	private static final int MAX_LAENGE_VORLAGENNAME = 120;

	/** Die Zahl der IDs, die eine Liste im Log ausschreibt. Was darüber hinausgeht, erscheint als Restanzahl. */
	private static final int MAX_AUSGEGEBENE_IDS = 10;

	/** Das Zeichen für einen gekürzten Text. Reines ASCII, denn der Zeichensatz der Server-Konsole ist nicht zugesichert. */
	private static final String AUSLASSUNG = "...";

	private ReportingParameterLogUtils() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}


	/**
	 * Gibt die Kenndaten der Anfrage als eine Zeile zurück.
	 * <p>Die Werte sind roh, wie sie ankamen - vor dem Entfernen von {@code null}-Einträgen und Dubletten. Die Anzahl der IDs weicht deshalb von den
	 * späteren Meldungen ab, und genau das ist der Zweck: Die Zeile zeigt die Anfrage und nicht das Ergebnis ihrer Bereinigung.</p>
	 * <p>Ausgeschlossen sind Betreff und Text der E-Mail, die freien Vorlagenparameter sowie die Filter- und Sortierwerte. Sie können personenbezogene
	 * Angaben tragen, und das Log erreicht im Fehlerfall den Client.</p>
	 *
	 * @param reportingParameter      Die Parameter der Anfrage; darf {@code null} sein.
	 * @param reportingAusgabeformat  Das Ausgabeformat des API-Aufrufs; darf {@code null} sein, denn geprüft wird es erst danach.
	 *
	 * @return Die Zeile mit den Kenndaten der Anfrage.
	 */
	static String kenndaten(final ReportingParameter reportingParameter, final ReportingAusgabeformat reportingAusgabeformat) {
		if (reportingParameter == null) {
			return "Anfrage: keine Reporting-Parameter übergeben.";
		}
		return ("Anfrage (Rohwerte): Vorlage '%s', Schuljahresabschnitt %d, Ausgabeformat der Anfrage %d, Ausgabeformat des API-Aufrufs %s, "
				+ "Hauptdatenobjekt %d, Hauptdaten %s, Detaildaten %s, E-Mail-Empfängertyp %s").formatted(
						maskiert(reportingParameter.reportvorlage),
						reportingParameter.idSchuljahresabschnitt,
						reportingParameter.ausgabeformat,
						(reportingAusgabeformat == null) ? "null" : reportingAusgabeformat.name(),
						reportingParameter.idHauptdatenObjekt,
						idListe(reportingParameter.idsHauptdaten),
						idListe(reportingParameter.idsDetaildaten),
						empfaengerTyp(reportingParameter));
	}

	/**
	 * Gibt den Empfängertyp der E-Mail als Zahl zurück. Nur die Zahl: Ihr Name sagt dem Leser des Logs nichts, was die Zahl nicht schon trägt.
	 *
	 * @param reportingParameter Die Parameter der Anfrage.
	 *
	 * @return Die Zahl oder "null", wenn die Anfrage keine E-Mail-Daten trägt.
	 */
	private static String empfaengerTyp(final ReportingParameter reportingParameter) {
		return (reportingParameter.eMailDaten == null) ? "null" : Integer.toString(reportingParameter.eMailDaten.empfaengerTyp);
	}

	/**
	 * Gibt eine ID-Liste als Anzahl und Auszug zurück, etwa {@code 3 IDs [1001, 1002, 1003]}.
	 * <p>Ab dem elften Eintrag stehen die ersten zehn und die Zahl der übrigen. Die Restanzahl entsteht dabei direkt aus {@code size()}: Eine Liste mit
	 * mehreren tausend IDs zuerst vollständig in Text zu wandeln, erzeugte genau den Aufwand, den die Kürzung vermeidet.</p>
	 * <p>Eine fehlende Liste bleibt von einer leeren unterscheidbar. Der Unterschied sagt, ob der Client nichts geschickt oder ausdrücklich nichts
	 * ausgewählt hat.</p>
	 *
	 * @param ids Die IDs aus der Anfrage; darf {@code null} sein und {@code null}-Einträge enthalten.
	 *
	 * @return Die Anzahl mit dem Auszug oder "null".
	 */
	private static String idListe(final List<Long> ids) {
		if (ids == null) {
			return "null";
		}
		final String auszug = ids.stream().limit(MAX_AUSGEGEBENE_IDS).map(String::valueOf).collect(Collectors.joining(", "));
		final int rest = ids.size() - MAX_AUSGEGEBENE_IDS;
		if (rest <= 0) {
			return "%d IDs [%s]".formatted(ids.size(), auszug);
		}
		return "%d IDs [%s, %d weitere]".formatted(ids.size(), auszug, rest);
	}

	/**
	 * Gibt einen Text aus dem Request so zurück, dass er das Log nicht beschädigt: ohne Steuerzeichen und auf {@link #MAX_LAENGE_VORLAGENNAME} Zeichen
	 * gekürzt.
	 * <p>Die Reihenfolge ist verbindlich: erst {@code null} abfangen, dann die Steuerzeichen ersetzen, dann kürzen. Wer zuerst kürzt, ruft {@code length()}
	 * auf einem {@code null}-Wert auf - und die Ersetzung verlängert den Text, die Grenze hielte danach nicht mehr.</p>
	 * <p>Auch eine Meldung, die einen Namen aus dem Request nennt, führt ihn hierdurch: Die Kopfzeile der Fehlerantwort maskiert nicht selbst, und der
	 * Client gibt jede Zeile des Logs einzeln aus.</p>
	 *
	 * @param text Der Text aus dem Request; darf {@code null} sein.
	 *
	 * @return Der maskierte und gekürzte Text.
	 */
	static String maskiert(final String text) {
		if (text == null) {
			return "null";
		}
		return gekuerzt(ohneSteuerzeichen(text));
	}

	/**
	 * Ersetzt jedes Steuerzeichen durch eine sichtbare Schreibweise.
	 * <p>Umbruch, Wagenrücklauf und Tabulator bekommen ihre gewohnte Form, jedes weitere Steuerzeichen seine Nummer. Ein Umbruch teilte die Konsolenausgabe
	 * in zwei Zeilen, von denen die zweite ohne Zeitstempel und Level steht und damit wie ein eigener Eintrag aussieht; ein Wagenrücklauf überschriebe die
	 * bereits ausgegebene Zeile, und eine Escape-Folge löste dort Steuersequenzen aus.</p>
	 *
	 * @param text Der Text aus dem Request.
	 *
	 * @return Der Text ohne Steuerzeichen.
	 */
	private static String ohneSteuerzeichen(final String text) {
		final StringBuilder ergebnis = new StringBuilder(text.length());
		for (int i = 0; i < text.length(); i++) {
			final char zeichen = text.charAt(i);
			if (!Character.isISOControl(zeichen)) {
				ergebnis.append(zeichen);
			} else if (zeichen == '\n') {
				ergebnis.append("\\n");
			} else if (zeichen == '\r') {
				ergebnis.append("\\r");
			} else if (zeichen == '\t') {
				ergebnis.append("\\t");
			} else {
				ergebnis.append("\\u%04x".formatted((int) zeichen));
			}
		}
		return ergebnis.toString();
	}

	/**
	 * Kürzt einen Text auf {@link #MAX_LAENGE_VORLAGENNAME} Zeichen, das Auslassungszeichen eingerechnet.
	 * <p>Ein Zeichen aus zwei Codeeinheiten wird dabei nicht zerschnitten. Die verbliebene Hälfte erschiene in der Ausgabe als Ersatzzeichen und machte
	 * den Namen an seinem Ende unlesbar.</p>
	 *
	 * @param text Der bereits maskierte Text.
	 *
	 * @return Der Text, höchstens so lang wie erlaubt.
	 */
	private static String gekuerzt(final String text) {
		if (text.length() <= MAX_LAENGE_VORLAGENNAME) {
			return text;
		}
		int ende = MAX_LAENGE_VORLAGENNAME - AUSLASSUNG.length();
		if (Character.isHighSurrogate(text.charAt(ende - 1))) {
			ende--;
		}
		return text.substring(0, ende) + AUSLASSUNG;
	}

}
