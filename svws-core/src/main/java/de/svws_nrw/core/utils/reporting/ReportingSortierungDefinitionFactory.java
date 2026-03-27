package de.svws_nrw.core.utils.reporting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import de.svws_nrw.core.data.reporting.ReportingSortierungDefinition;

/**
 * Factory zum Erzeugen von {@link ReportingSortierungDefinition}en,
 * beispielsweise gedacht für die Definition der "sortierungDefinitionenOptionen" in {@code ReportingReportvorlage}.
 */
public final class ReportingSortierungDefinitionFactory {

	private ReportingSortierungDefinitionFactory() {
		// utility class
	}

	/**
	 * Erzeugt eine {@link ReportingSortierungDefinition}.
	 *
	 * @param bezeichnung                die Bezeichnung der Sortierdefinition (UI-Text)
	 * @param typ                        der Typname des zu sortierenden Reporting-Datentyps (z. B. {@code "ReportingSchueler"})
	 * @param verwendeStandardsortierung {@code true}, falls die Standardsortierung für diesen Typ verwendet werden soll; sonst {@code false}
	 * @param attribute                  die Sortierattribute für eine benutzerdefinierte Sortierung (z. B. {@code "Nachname"} oder {@code "-Geburtsdatum"} für absteigend)
	 *
	 * @return die neu erzeugte Sortierdefinition
	 */
	public static @NotNull ReportingSortierungDefinition definition(final @NotNull String bezeichnung, final @NotNull String typ,
			final boolean verwendeStandardsortierung, final @NotNull List<String> attribute) {

		final ReportingSortierungDefinition d = new ReportingSortierungDefinition();
		d.bezeichnung = bezeichnung;
		d.typ = typ;
		d.verwendeStandardsortierung = verwendeStandardsortierung;
		d.attribute = new ArrayList<>(attribute);
		return d;
	}

	/**
	 * Erzeugt eine {@link ReportingSortierungDefinition}.
	 *
	 * @param bezeichnung                die Bezeichnung der Sortierdefinition (UI-Text)
	 * @param typ                        die Klasse des zu sortierenden Reporting-Datentyps
	 * @param verwendeStandardsortierung {@code true}, falls die Standardsortierung für diesen Typ verwendet werden soll; sonst {@code false}
	 * @param attribute                  die Sortierattribute für eine benutzerdefinierte Sortierung (z. B. {@code "Nachname"} oder {@code "-Geburtsdatum"} für absteigend)
	 *
	 * @return die neu erzeugte Sortierdefinition
	 */
	public static @NotNull ReportingSortierungDefinition definition(final @NotNull String bezeichnung, final @NotNull Class<?> typ,
			final boolean verwendeStandardsortierung, final @NotNull List<String> attribute) {
		return definition(bezeichnung, typ.getSimpleName(), verwendeStandardsortierung, attribute);
	}

	/**
	 * Erzeugt eine {@link ReportingSortierungDefinition}, die die Standardsortierung des Typs verwendet.
	 *
	 * @param bezeichnung    die Bezeichnung der Sortierdefinition (UI-Text)
	 * @param typ            die Klasse des zu sortierenden Reporting-Datentyps
	 *
	 * @return die neu erzeugte Sortierdefinition mit aktivierter Standardsortierung
	 */
	public static @NotNull ReportingSortierungDefinition standard(final @NotNull String bezeichnung, final @NotNull String typ) {
		return definition(bezeichnung, typ, true, List.of());
	}

	/**
	 * Erzeugt eine {@link ReportingSortierungDefinition}, die die Standardsortierung des Typs verwendet.
	 *
	 * @param bezeichnung    die Bezeichnung der Sortierdefinition (UI-Text)
	 * @param typ            die Klasse des zu sortierenden Reporting-Datentyps
	 *
	 * @return die neu erzeugte Sortierdefinition mit aktivierter Standardsortierung
	 */
	public static @NotNull ReportingSortierungDefinition standard(final @NotNull String bezeichnung, final @NotNull Class<?> typ) {
		return definition(bezeichnung, typ, true, List.of());
	}

	/**
	 * Erzeugt eine {@link ReportingSortierungDefinition} mit benutzerdefinierten Sortierattributen.
	 *
	 * @param bezeichnung    die Bezeichnung der Sortierdefinition (UI-Text)
	 * @param typ            der Typname des zu sortierenden Reporting-Datentyps (z. B. {@code "ReportingSchueler"})
	 * @param typBezeichnung die Bezeichnung des Typs, die auch in der UI verwendet werden kann, z. B. 'Schülersortierung'
	 * @param attribute      die Sortierattribute (z. B. {@code "Nachname"} oder {@code "-Geburtsdatum"} für absteigend)
	 *
	 * @return die neu erzeugte Sortierdefinition mit benutzerdefinierten Attributen
	 */
	public static @NotNull ReportingSortierungDefinition benutzerdefiniert(final @NotNull String bezeichnung, final @NotNull String typ,
			final @NotNull String typBezeichnung, final @NotNull List<String> attribute) {
		return definition(bezeichnung, typ, false, attribute);
	}

	/**
	 * Methode zum Erzeugen der Liste von Sortierdefinitionen.
	 *
	 * @param definitionen die anzubietenden Sortierdefinitionen
	 *
	 * @return eine veränderbare Liste mit den übergebenen Sortierdefinitionen
	 */
	public static @NotNull List<ReportingSortierungDefinition> definitionen(final @NotNull ReportingSortierungDefinition... definitionen) {
		return new ArrayList<>(Arrays.asList(definitionen));
	}

	/**
	 * Liefert eine leere, veränderbare Liste für {@code sortierungDefinitionenOptionen}.
	 *
	 * @return leere Liste von Sortierdefinitionen
	 */
	public static @NotNull List<ReportingSortierungDefinition> keineDefinitionen() {
		return new ArrayList<>();
	}


	// ##### Attribute-Helfer (String-Konvention: "-" = absteigend) #####

	/**
	 * Normalisiert ein Sortierattribut auf aufsteigende Sortierung (entfernt ggf. ein führendes {@code '-'}).
	 *
	 * @param attribut der Attributname (mit oder ohne führendes {@code '-'})
	 *
	 * @return der Attributname für aufsteigende Sortierung (ohne führendes {@code '-'})
	 */
	public static @NotNull String asc(final @NotNull String attribut) {
		return attribut.startsWith("-") ? attribut.substring(1) : attribut;
	}

	/**
	 * Normalisiert ein Sortierattribut auf absteigende Sortierung (stellt ein führendes {@code '-'} sicher).
	 *
	 * @param attribut der Attributname (mit oder ohne führendes {@code '-'})
	 *
	 * @return der Attributname für absteigende Sortierung (mit führendem {@code '-'})
	 */
	public static @NotNull String desc(final @NotNull String attribut) {
		final String clean = attribut.startsWith("-") ? attribut.substring(1) : attribut;
		return "-" + clean;
	}
}
