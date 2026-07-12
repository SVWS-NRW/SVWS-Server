package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse definiert das Speicherformat der benutzerweiten Reporting-Einstellungen eines Benutzers in der
 * Client-Konfiguration. Gespeichert werden ausschließlich die Werte der Parameter — die zugehörigen UI-Metadaten
 * (Beschriftung, Komponententyp, Kompetenzen) kommen stets frisch aus dem Katalog der benutzerweiten Parameter.
 */
@XmlRootElement
@Schema(description = "Die benutzerweiten Reporting-Einstellungen eines Benutzers.")
@TranspilerDTO
public class ReportingEinstellungenBenutzerVorlagen {

	/** Die Version des Speicherformats, um künftige Format-Migrationen zu ermöglichen. */
	@Schema(description = "Die Version des Speicherformats, um künftige Format-Migrationen zu ermöglichen.", example = "1")
	public int version = 1;

	/** Die gespeicherten Werte der benutzerweiten Parameter. Fehlt ein Katalog-Parameter, so gilt sein Katalog-Default. */
	@Schema(description = "Die gespeicherten Werte der benutzerweiten Parameter. Fehlt ein Katalog-Parameter, so gilt sein Katalog-Default.")
	public @NotNull List<ReportingEinstellungenBenutzerVorlagenParameterWert> parameterWerte = new ArrayList<>();


	/**
	 * Diese Klasse definiert das Speicherformat der benutzerweiten Reporting-Einstellungen eines Benutzers in der
	 * Client-Konfiguration.
	 */
	public ReportingEinstellungenBenutzerVorlagen() {
		super();
	}
}
