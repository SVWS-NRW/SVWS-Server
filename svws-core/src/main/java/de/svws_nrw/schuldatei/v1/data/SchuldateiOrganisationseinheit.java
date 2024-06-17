package de.svws_nrw.schuldatei.v1.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt eine Organisationseinheit im Rahmen der Schuldatei.
 * Den Schulen als eine Art von Organisationseinheiten sind i.A. Schulnummern
 * zwichen 100000 und 199999 zugeordnet.
 */
@XmlRootElement
@Schema(description = "eine Organisationseinheit der Schuldatei.")
@TranspilerDTO
public class SchuldateiOrganisationseinheit {

	/** Die Schulnummer der Organisationseinheit. */
	@Schema(description = "die Schulnummer der Organisationseinheit", example = "100001")
	public int schulnummer = 0;

	/** Die Bundeslandkennung (NRW) */
	@Schema(description = "die Bundeslandkennung", example = "NRW")
	public String bundeslandkennung;

	/** Die eindeutige Identifier für das XSCHULE-Format */
	@Schema(description = "der Identifier der Schule für XSCHULE", example = "NRW_123456")
	public String xscid;

	/** Die Art der Organisationseinheit */
	@Schema(description = "die Art der Organisationseinheit", example = "1")
	public String oeart;

	/** Die Amtsbezeichnung der Organisationseinheit */
	@Schema(description = "die amtliche Bezeichnung der Organisationseinheit", example = "Städt. Gymnasium ....")
	public @NotNull String amtsbez = "";

	/** Das Errichtungsdatum der Schule. */
	@Schema(description = "das Errichtungsdatum der Schule", example = "01.08.1973")
	public String errichtung;

	/** Das Aufloesungsdatum der Schule. */
	@Schema(description = "das Auflösungsdatum der Schule", example = "01.08.1973")
	public String aufloesung;

	/** Die Grunddaten der Organisationseinheit (zeitl. Verlaufsliste)*/
	@ArraySchema(schema = @Schema(implementation = SchuldateiOrganisationseinheitGrunddaten.class))
	public final @NotNull List<@NotNull SchuldateiOrganisationseinheitGrunddaten> grunddaten = new ArrayList<>();

	/** Die Adressen der Organisationseinheit (zeitl. Verlaufsliste)*/
	@ArraySchema(schema = @Schema(implementation = SchuldateiOrganisationseinheitAdresse.class))
	public final @NotNull List<@NotNull SchuldateiOrganisationseinheitAdresse> adressen = new ArrayList<>();

	/** Die Merkmale der Organisationseinheit (zeitl. Verlaufsliste)*/
	@ArraySchema(schema = @Schema(implementation = SchuldateiOrganisationseinheitMerkmal.class))
	public final @NotNull List<@NotNull SchuldateiOrganisationseinheitMerkmal> merkmal = new ArrayList<>();

	/** Die Erreichbarkeiten der Organisationseinheit (zeitl. Verlaufsliste)*/
	@ArraySchema(schema = @Schema(implementation = SchuldateiOrganisationseinheitErreichbarkeit.class))
	public final @NotNull List<@NotNull SchuldateiOrganisationseinheitErreichbarkeit> erreichbarkeiten = new ArrayList<>();

	/** Die Eigenschaften der Organisationseinheit */
	@ArraySchema(schema = @Schema(implementation = SchuldateiOrganisationseinheitEigenschaft.class))
	public final @NotNull List<@NotNull SchuldateiOrganisationseinheitEigenschaft> oe_eigenschaften = new ArrayList<>();

	/** Die Gliederungen der Organisationseinheit-Schule (zeitl. Verlaufsliste)*/
	@ArraySchema(schema = @Schema(implementation = SchuldateiOrganisationseinheitGliederung.class))
	public final @NotNull List<@NotNull SchuldateiOrganisationseinheitGliederung> gliederung = new ArrayList<>();


	/**
	 * Erstellt eine neue Organiationseinheit für die Schuldatei
	 */
	public SchuldateiOrganisationseinheit() {
		// Die Initialisierung mit Defaults erfolgt direkt über die Attribute
	}

}
