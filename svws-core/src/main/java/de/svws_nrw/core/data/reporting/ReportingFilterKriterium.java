package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Ein einzelnes Filterkriterium für einen Reporting-Datentyp.
 * Diese Klasse kann sowohl einfache Einträge (mit Attribut, Operation, Wert) aufnehmen als auch weitere Unterkriterien.
 * ALle Elemente werden mit der angegebenen Verknüpfung miteinander verbunden.
 */
@XmlRootElement
@Schema(description = "Ein einzelnes Filterkriterium für einen Reporting-Datentyp. "
		+ "Diese Klasse kann sowohl einfache Einträge (mit Attribut, Operation, Wert) aufnehmen als auch weitere Unterkriterien. "
		+ "ALle Elemente werden mit der angegebenen Verknüpfung miteinander verbunden.")
@TranspilerDTO
public class ReportingFilterKriterium {

	/** Die Art der logischen Verknüpfung für die Listen 'eintraege' und 'unterkriterien'. */
	@Schema(description = "Die Art der logischen Verknüpfung für die Listen 'eintraege' und 'unterkriterien'.",
			example = "1")
	public int verknuepfung = ReportingFilterVerknuepfung.AND.getId();

	/** Liste von konkreten Filter-Einträgen (Attributvergleiche), die gemäß 'verknuepfung' verbunden werden. */
	@Schema(description = "Liste von konkreten Filter-Einträgen (Attributvergleiche), die gemäß 'verknuepfung' verbunden werden.")
	public @NotNull List<ReportingFilterEintrag> eintraege = new ArrayList<>();

	/** Liste von Unterkriterien, die gemäß 'verknuepfung' mit den Einträgen verbunden werden. */
	@Schema(description = "Liste von Unterkriterien, die gemäß 'verknuepfung' mit den Einträgen verbunden werden.")
	public @NotNull List<ReportingFilterKriterium> unterkriterien = new ArrayList<>();

	/** Gibt an, ob das Gesamtergebnis dieser Gruppe negiert werden soll (NICHT). */
	@Schema(description = "Gibt an, ob das Gesamtergebnis dieser Gruppe negiert werden soll (NICHT).", example = "false")
	public boolean nicht = false;
}
