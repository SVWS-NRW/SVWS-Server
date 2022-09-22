package de.nrw.schule.svws.core.data.schule;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Fachklassen beim Berufskolleg.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Fachklassen beim Berufskolleg.")
@TranspilerDTO
public class BerufskollegFachklassenKatalogEintrag {


	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Der Index für die Verknüpfung von einem Bildungsgang des Berufskollegs mit Fachklassen */
	@Schema(required = true, description = "der Index für die Verknüpfung von einem Bildungsgang des Berufskollegs mit Fachklassen", example="100")
	public int index;
	
	/** Der Fachklassenschlüssel. */
	@Schema(required = true, description = "der Fachklassenschlüssel - Teil 1", example="620")
	public String schluessel;

	/** Der Fachklassenschlüssel - Teil 2. */
	@Schema(required = true, description = "der Fachklassenschlüssel - Teil 2", example="00")
	public String schluessel2;
	
	/** Gibt an, ob die Fachklassen ausgelaufen ist oder nicht */
	@Schema(required = true, description = "gibt an, ob die Fachklassen ausgelaufen ist oder nicht", example="false")
	public boolean istAusgelaufen = false;
	
	/** Die Gruppe des Berufsfeldes. */
	@Schema(required = true, description = "die Gruppe des Berufsfeldes", example="T")
	public String berufsfeldGruppe;
	
	/** Das Berufsfeld. */
	@Schema(required = true, description = "das Berufsfeld", example="MT")
	public String berufsfeld;

	/** Ebene 1 des Berufsfeldes */
	@Schema(required = true, description = "Ebene 1 des Berufsfeldes", example="TE")
	public String ebene1;

	/** Ebene 2 des Berufsfeldes */
	@Schema(required = true, description = "Ebene 2 des Berufsfeldes", example="ME")
	public String ebene2;

	/** Ebene 3 des Berufsfeldes */
	@Schema(required = true, description = "Ebene 3 des Berufsfeldes", example="")
	public String ebene3;

	/** Die Bezeichnung der Fachklasse */
	@Schema(required = true, description = "die Bezeichnung der Fachklasse", example="Metalltechnik")
	public @NotNull String bezeichnung = "";
	
	/** Die Bezeichnung der Fachklasse (männlich) */
	@Schema(required = true, description = "die Bezeichnung der Fachklasse (männlich)", example="Metalltechnik")
	public @NotNull String bezeichnungM = "";
	
	/** Die Bezeichnung der Fachklasse (weiblich) */
	@Schema(required = true, description = "die Bezeichnung der Fachklasse (weiblich)", example="Metalltechnik")
	public @NotNull String bezeichnungW = "";
	
	/** Gibt an, in welchem Schuljahr die Fachklasse einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr die Anlage einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Fachklasse gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr die Anlage gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Anlage-Eintrag mit Standardwerten
	 */
	public BerufskollegFachklassenKatalogEintrag() {
	}


	/**
	 * Erstellt einen Anlage-Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param index              der Index, welcher der Fachklasse für die Zuordnung zum Bildungsgang zugeordnet ist
	 * @param schluessel         der erste Teil des Fachklassenschlüssels
	 * @param schluessel2        der zweite Teil de Fachklassenschlüssels
	 * @param istAusgelaufen     gibt an, ob die Fachklasse ausgelaufen ist oder nicht
	 * @param berufsfeldGruppe   die Gruppe der Berufsfelder, welcher das Berufsfeld der Fachklasse
	 * @param berufsfeld         das Berufsfeld, welchem die Fachklasse zugeordnet ist
	 * @param ebene1             die Berufsebene 1
	 * @param ebene2             die Berufsebene 2
	 * @param ebene3             die Berufsebene 3
	 * @param bezeichnung        die Bezeichnung der Fachklasse
	 * @param bezeichnungM       die Bezeichnung der Fachklasse (Text in männlicher Form)
	 * @param bezeichnungW       die Bezeichnung der Fachklasse (Text in weiblicher Form)
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public BerufskollegFachklassenKatalogEintrag(long id, int index, String schluessel, String schluessel2, boolean istAusgelaufen,
			String berufsfeldGruppe, String berufsfeld, String ebene1, String ebene2, String ebene3,  
			@NotNull String bezeichnung, @NotNull String bezeichnungM, @NotNull String bezeichnungW, 
			Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.index = index;
		this.schluessel = schluessel;
		this.schluessel2 = schluessel2;
		this.istAusgelaufen = istAusgelaufen;
		this.berufsfeldGruppe = berufsfeldGruppe;
		this.berufsfeld = berufsfeld;
		this.ebene1 = ebene1;
		this.ebene2 = ebene2;
		this.ebene3 = ebene3;
		this.bezeichnung = bezeichnung;
		this.bezeichnungM = bezeichnungM;
		this.bezeichnungW = bezeichnungW;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	
	
}
