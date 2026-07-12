package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse enthält die gespeicherte Auswahl einer einzelnen Sortier- oder Filtergruppe. Gespeichert werden nur
 * die {@code bezeichnung}en der ausgewählten Definitionen aus den Katalog-Optionen der Gruppe (z. B.
 * {@link ReportingSortierungDefinitionGruppe#sortierungDefinitionenOptionen} bzw.
 * {@link ReportingFilterDefinitionGruppe#filterDefinitionenOptionen}), nicht deren Attribute bzw. Kriterien, damit
 * die Auswahl robust gegen künftige Katalog-Änderungen bleibt. Die Liste bildet sowohl die heutige Einfachauswahl bei
 * der Sortierung als auch die Mehrfachauswahl bei der Filterung ab; die Reihenfolge der Einträge entspricht der
 * gespeicherten Auswahlreihenfolge.
 */
@XmlRootElement
@Schema(description = "Die gespeicherte Auswahl einer einzelnen Sortier- oder Filtergruppe.")
@TranspilerDTO
public class ReportingEinstellungenBenutzerVorlageGruppe {

	/** Die Bezeichnung der Sortier- oder Filtergruppe. */
	@Schema(description = "Die Bezeichnung der Sortier- oder Filtergruppe.", example = "Schülersortierung")
	public @NotNull String gruppe = "";

	/** Die Bezeichnungen der ausgewählten Definitionen aus den Katalog-Optionen der Gruppe, in Auswahlreihenfolge. */
	@Schema(description = "Die Bezeichnungen der ausgewählten Definitionen, in Auswahlreihenfolge.", example = "[\"Klasse, Name, Rufname\"]")
	public @NotNull List<String> bezeichnungen = new ArrayList<>();


	/**
	 * Diese Klasse enthält die gespeicherte Auswahl einer einzelnen Sortier- oder Filtergruppe.
	 */
	public ReportingEinstellungenBenutzerVorlageGruppe() {
		super();
	}
}
