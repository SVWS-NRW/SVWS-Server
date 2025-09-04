package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Beschreibt eine typenbasierte Sortierdefinition für einen Reporting-Datentyp.
 * Ein Beispiel wäre "ReportingSchueler".
 */
@XmlRootElement
@Schema(description = "Typenbasierte Sortierdefinition für einen Reporting-Datentyp.")
@TranspilerDTO
public class ReportingSortierungDefinition {

	/** Der Typname des zu sortierenden Reporting-Datentyps, z. B. 'ReportingSchueler' oder 'ReportingKlasse'. */
	@Schema(description = "Der Typname des zu sortierenden Reporting-Datentyps, z. B. 'ReportingSchueler'.", example = "ReportingSchueler")
	public @NotNull String typ = "";

	/** Die Angabe legt fest, ob die definierte Standardsortierung für diesen Typ verwendet werden soll. */
	@Schema(description = "Die Angabe legt fest, ob die definierte Standardsortierung für diesen Typ verwendet werden soll.", example = "true")
	public Boolean verwendeStandardsortierung = true;

	/** Liste von Attributnamen für eine benutzerdefinierte Sortierung dieses Typs. */
	@Schema(description = "Liste von Sortierattributen für diesen Typ, z. B. ['Nachname', 'Vorname' oder '-Geburtsdatum'].",
			example = "[\"Nachname\",\"Vorname\"]")
	public @NotNull List<String> attribute = new ArrayList<>();

	/**
	 * Der Konstruktor der Klasse ReportingSortierungDefinition.
	 * Erzeugt eine Instanz der Klasse und initialisiert sie mit Standardwerten.
	 * Die Klasse beschreibt eine typenbasierte Sortierdefinition für einen Reporting-Datentyp.
	 */
	public ReportingSortierungDefinition() {
		super();
	}
}
