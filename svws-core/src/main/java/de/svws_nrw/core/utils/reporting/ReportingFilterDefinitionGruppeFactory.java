package de.svws_nrw.core.utils.reporting;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import de.svws_nrw.core.data.reporting.ReportingFilterDefinition;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;

/**
 * Factory zum Erzeugen von {@link ReportingFilterDefinitionGruppe}-Instanzen für die Übergabe
 * konkret ausgewählter Filter in den Reporting-Parametern.
 */
public final class ReportingFilterDefinitionGruppeFactory {

	private ReportingFilterDefinitionGruppeFactory() {
		// utility class
	}

	/**
	 * Erzeugt eine {@link ReportingFilterDefinitionGruppe} mit den übergebenen Filterdefinitionen.
	 *
	 * @param bezeichnung    die Bezeichnung der Gruppe (UI-Text)
	 * @param typ            der Typname des zu filternden Reporting-Datentyps (z. B. {@code "ReportingSchueler"})
	 * @param uiIstSichtbar  gibt an, ob die Gruppe in der UI sichtbar sein soll
	 * @param definitionen   die der Gruppe zugeordneten, aktiven Filterdefinitionen
	 *
	 * @return die neu erzeugte Gruppe mit gesetzten {@code filterDefinitionen}
	 */
	public static @NotNull ReportingFilterDefinitionGruppe gruppe(final @NotNull String bezeichnung, final @NotNull String typ,
			final boolean uiIstSichtbar, final @NotNull ReportingFilterDefinition... definitionen) {

		final ReportingFilterDefinitionGruppe gruppe = new ReportingFilterDefinitionGruppe();
		gruppe.bezeichnung = bezeichnung;
		gruppe.typ = typ;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.filterDefinitionen.addAll(Arrays.asList(definitionen));
		return gruppe;
	}

	/**
	 * Erzeugt eine {@link ReportingFilterDefinitionGruppe} aus einer Liste von IDs. Die IDs werden in einen
	 * einzelnen {@code IN}-Filtereintrag auf dem Attribut {@code "id"} überführt und in eine entsprechende
	 * Filterdefinition mit AND-Verknüpfung verpackt.
	 *
	 * @param bezeichnung    die Bezeichnung der Gruppe (UI-Text)
	 * @param typ            der Typname des zu filternden Reporting-Datentyps (z. B. {@code "ReportingSchueler"})
	 * @param uiIstSichtbar  gibt an, ob die Gruppe in der UI sichtbar sein soll
	 * @param idsListe       die IDs, die in einem {@code IN}-Filter auf {@code id} verwendet werden
	 *
	 * @return die neu erzeugte Gruppe mit einer einzelnen Filterdefinition für {@code id IN (idsListe)}
	 */
	public static @NotNull ReportingFilterDefinitionGruppe gruppeAusIds(final @NotNull String bezeichnung, final @NotNull String typ,
			final boolean uiIstSichtbar, final @NotNull List<Long> idsListe) {

		final @NotNull String @NotNull [] werte = new String[idsListe.size()];
		for (int i = 0; i < idsListe.size(); i++) {
			werte[i] = "" + idsListe.get(i);
		}
		final @NotNull ReportingFilterDefinition definition = ReportingFilterDefinitionFactory.definition(bezeichnung, typ,
				ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.in("id", werte)));
		return gruppe(bezeichnung, typ, uiIstSichtbar, definition);
	}
}
