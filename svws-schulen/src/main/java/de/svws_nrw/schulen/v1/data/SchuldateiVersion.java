package de.svws_nrw.schulen.v1.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Version der Schuldatei.
 */
@XmlRootElement
@Schema(description = "die Version der Schuldatei.")
@TranspilerDTO
public class SchuldateiVersion {

	/** Die Versionsnummer der Schuldatei. */
	@Schema(description = "die Versionsnummer", example = "2.0")
	public @NotNull String version = "";

	/** Der Name des API-Endpunktes der Schuldatei-Anwendung. */
	@Schema(description = "der Name", example = "Offizielle Schuldatei WebService")
	public @NotNull String name = "";

	/** Die URL des API-Endpunktes. */
	@Schema(description = "die URL", example = "https://schulverwaltung-test.it.nrw.de/SchuldateiDatenService-test/export/{konten|katalog|aenderungen}")
	public @NotNull String url = "";

	/** Das Ablaufdatum der Version. */
	@Schema(description = "das Ablaufdatum", example = "31.12.9999")
	public @NotNull String gueltigbis = "";

	/** Eine Bemerkung zu der Datei. */
	@Schema(description = "die Bemerkung", example = "Version 1.0 nur noch bis 31.07.2025 gültig")
	public @NotNull String bemerkung = "";

	/**
	 * Erstellt eine Schuldatei
	 */
	public SchuldateiVersion() {
		// Die Initialisierung mit Defaults erfolgt direkt über die Attribute
	}

}
