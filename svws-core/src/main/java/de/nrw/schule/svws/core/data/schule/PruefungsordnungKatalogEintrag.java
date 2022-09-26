package de.nrw.schule.svws.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Ausbildungs- und/oder Prüfungsordnungen.
 * Diese ist so gestaltet, dass er alte Verordnungen nur zum Teil unterstützt, sofern
 * die zur Kompatibilität mit Schild-NRW nötig ist..  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Ausbildungs- und/oder Prüfungsordnungen.")
@TranspilerDTO
public class PruefungsordnungKatalogEintrag {


	/** Die ID des Eintrags. */
	@Schema(required = true, description = "die ID des Eintrags", example="4711")
	public long id;

	/** Das Kürzel der Ausbildungs und/oder Prüfungsordnung */
	@Schema(required = true, description = "das Kürzel der Verordnung", example="APO-GOSt")
	public @NotNull String kuerzel = "";
	
	/** Das Kürzel der Ausbildungs und/oder Prüfungsordnung, wie es in Schild NRW verwendet wird */
	@Schema(required = false, description = "das Kürzel der Ausbildungs und/oder Prüfungsordnung, wie es in Schild NRW verwendet wird", example="APO-GOSt(B)10")
	public String kuerzelSchild = "";
	
	/** Die Bezeichnung der Verordnung. */
	@Schema(required = true, description = "die Bezeichnung der Verordnung", example="Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe (APO-GOSt)")
	public @NotNull String bezeichnung = "";

	/** Gesetz- und Verordnungsblatt: Das Jahr in dem die Verordnung veröffentlich wurde */
	@Schema(required = false, description = "Gesetz- und Verordnungsblatt: Das Jahr in dem die Verordnung veröffentlich wurde", example="1998")
	public Integer gvJahr = null;
	
	/** Gesetz- und Verordnungsblatt: Die Nummer im Jahr der Veröffentlichung */
	@Schema(required = false, description = "Gesetz- und Verordnungsblatt: Die Nummer im Jahr der Veröffentlichung", example="43")
	public @NotNull String gvNr = "";

	/** Gesetz- und Verordnungsblatt: ggf. die Seitenangaben im Jahr der Veröffentlichung */
	@Schema(required = false, description = "Gesetz- und Verordnungsblatt: ggf. die Seitennummer im Jahr der Veröffentlichung", example="593-608")
	public @NotNull String gvSeiten = "";
	
	/** ggf. ein Link zu einer Version der Verordnung */
	@Schema(required = false, description = "ggf. ein Link zu einer Version der Verordnung", example="https://bass.schul-welt.de/9607.htm")
	public @NotNull String link = "";	

	/** Gibt an, in welchem Schuljahr die Verordnung einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr die Verordnung einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="1999")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Verordnung gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr die Verordnung gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public PruefungsordnungKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel 
	 * @param kuerzelSchild      das Kürzel der Ausbildungs und/oder Prüfungsordnung, wie es in Schild NRW verwendet wird
	 * @param bezeichnung        die Bezeichnung
	 * @param gvJahr             Gesetz- und Verordnungsblatt: Das Jahr in dem die Verordnung veröffentlich wurde
	 * @param gvNr               Gesetz- und Verordnungsblatt: Die Nummer im Jahr der Veröffentlichung
	 * @param gvSeiten           Gesetz- und Verordnungsblatt: ggf. die Seitenangaben im Jahr der Veröffentlichung
	 * @param link               ggf. ein Link zu einer Version der Verordnung
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public PruefungsordnungKatalogEintrag(long id, @NotNull String kuerzel, String kuerzelSchild, @NotNull String bezeichnung, 
			Integer gvJahr, @NotNull String gvNr, @NotNull String gvSeiten, @NotNull String link,
			Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.kuerzelSchild = kuerzelSchild;
		this.bezeichnung = bezeichnung;
		this.gvJahr = gvJahr;
		this.gvNr = gvNr;
		this.gvSeiten = gvSeiten;
		this.link = link;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	
	
}
