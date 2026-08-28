package de.svws_nrw.module.reporting.diagnose;

import java.util.Objects;

/**
 * Die Identität eines Ausgabeproblems: Objektart und - wenn ein einzelner Datensatz betroffen ist - dessen ID. Über sie wird dasselbe Problem je Aufruf nur
 * einmal gezählt und protokolliert. Die Objektart ist eine Klasse statt eines Texts, damit keine zwei Schreibweisen derselben Art entstehen; eine
 * Proxy-Klasse wird still auf ihren Reporting-Typ zurückgeführt, denn ein Wurf im Diagnosepfad brächte die Ausgabe zu Fall, die er beschreiben soll.
 * Der Schlüssel bleibt modulintern - zum Client gelangen nur Kategorie und Anzahl.
 * <p>Trifft ein Problem statt eines Datensatzes eine feste Ausprägung einer Objektart - etwa eine einzelne Bilddefinition der Logoverwaltung -, so
 * unterscheidet das Merkmal die Fälle. Es ist aus demselben Grund eine Aufzählung und kein Text wie die Objektart eine Klasse ist.</p>
 *
 * @param objektart Die Art des betroffenen Objekts, etwa {@code ReportingSchueler.class}.
 * @param id        Die ID des betroffenen Datensatzes oder {@code null}, wenn das Problem nicht an einem einzelnen Datensatz hängt.
 * @param merkmal   Die betroffene Ausprägung der Objektart oder {@code null}, wenn die Objektart allein den Fall bezeichnet.
 */
public record ReportingProblemSchluessel(Class<?> objektart, Long id, Enum<?> merkmal) {

	/**
	 * Das Namenspräfix der Proxy-Implementierungen der Reporting-Typen.
	 * <p>Es enthält bewusst auch {@code Reporting}: Ein bloßes {@code Proxy} träfe jede fremde Klasse mit diesem Namensanfang - etwa
	 * {@link java.lang.reflect.Proxy} - und ersetzte sie durch ihre Oberklasse.</p>
	 */
	private static final String PRAEFIX_PROXY = "ProxyReporting";

	/**
	 * Prüft die Objektart und führt sie auf den Reporting-Typ zurück. Ohne Objektart wäre die Identität nicht eindeutig, und ohne die Rückführung zählte
	 * derselbe Datensatz je nach Aufrufstelle einmal als Typ und einmal als Proxy.
	 *
	 * @param objektart Die Art des betroffenen Objekts.
	 * @param id        Die ID des betroffenen Datensatzes oder null.
	 * @param merkmal   Die betroffene Ausprägung der Objektart oder null.
	 */
	public ReportingProblemSchluessel {
		Objects.requireNonNull(objektart, "Ein Schlüssel ohne Objektart lässt sich keinem Datenbestand zuordnen.");
		objektart = reportingTyp(objektart);
	}

	/**
	 * Führt eine Proxy-Implementierung auf den Reporting-Typ zurück, von dem sie erbt.
	 * <p>Die Schleife steigt so lange auf, wie der einfache Name mit {@code ProxyReporting} beginnt: Sie erreicht damit auch eine Klasse, die von einem Proxy
	 * erbt. Klassen außerhalb dieser Namenskonvention bleiben unberührt - der Schlüssel steht auch für Datentypen offen, die keinen Reporting-Typ besitzen.</p>
	 *
	 * @param objektart Die übergebene Objektart.
	 *
	 * @return Die Objektart selbst oder die erste Oberklasse, die keine Proxy-Implementierung ist.
	 */
	private static Class<?> reportingTyp(final Class<?> objektart) {
		Class<?> typ = objektart;
		while ((typ.getSuperclass() != null) && typ.getSimpleName().startsWith(PRAEFIX_PROXY)) {
			typ = typ.getSuperclass();
		}
		return typ;
	}

	/**
	 * Erzeugt den Schlüssel für ein Problem an einem einzelnen Datensatz.
	 *
	 * @param objektart Die Art des betroffenen Objekts, etwa {@code ReportingSchueler.class}.
	 * @param id        Die ID des betroffenen Datensatzes.
	 *
	 * @return Der Schlüssel.
	 */
	public static ReportingProblemSchluessel fuer(final Class<?> objektart, final long id) {
		return new ReportingProblemSchluessel(objektart, id, null);
	}

	/**
	 * Erzeugt den Schlüssel für ein Problem, das nicht an einem einzelnen Datensatz hängt - etwa nicht ladbare Stundenplandefinitionen, die eine ganze
	 * Ausgabe betreffen. Er ist damit für den gesamten Reporting-Aufruf eindeutig und zählt einmal.
	 *
	 * @param objektart Die Art der betroffenen Objekte.
	 *
	 * @return Der Schlüssel ohne ID.
	 */
	public static ReportingProblemSchluessel fuer(final Class<?> objektart) {
		return new ReportingProblemSchluessel(objektart, null, null);
	}

	/**
	 * Erzeugt den Schlüssel für ein Problem an einer festen Ausprägung einer Objektart, etwa an einer einzelnen Bilddefinition der Logoverwaltung.
	 * Je Ausprägung entsteht ein eigener Schlüssel, so dass mehrere betroffene Ausprägungen nicht zu einem Befund verschmelzen.
	 *
	 * @param objektart Die Art der betroffenen Objekte.
	 * @param merkmal   Die betroffene Ausprägung.
	 *
	 * @return Der Schlüssel mit dem Merkmal.
	 */
	public static ReportingProblemSchluessel fuer(final Class<?> objektart, final Enum<?> merkmal) {
		return new ReportingProblemSchluessel(objektart, null, merkmal);
	}

	/**
	 * Gibt den Schlüssel in einer für das Log lesbaren Form zurück.
	 *
	 * @return Die Objektart mit der ID des betroffenen Datensatzes, mit der betroffenen Ausprägung oder mit dem Vermerk, dass das Problem den gesamten
	 *         Aufruf betrifft.
	 */
	public String beschreibung() {
		if (merkmal != null) {
			return "%s %s".formatted(objektart.getSimpleName(), merkmal.name());
		}
		if (id == null) {
			return "%s (gesamter Aufruf)".formatted(objektart.getSimpleName());
		}
		return "%s %d".formatted(objektart.getSimpleName(), id);
	}

}
