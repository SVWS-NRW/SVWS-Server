package de.nrw.schule.svws.core.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse liefert die allgemeinen textuellen Bemerkungen zu dem Lernabschnitt eines Schülers zurück.  
 * Siehe auch {@link SchuelerLernabschnittsdaten}.  
 */
@XmlRootElement
@Schema(description="Die allgemeinen textuellen Bemerkungen zu dem Lernabschnitt eines Schülers.")
@TranspilerDTO
public class SchuelerLernabschnittBemerkungen {

	/** Der Text für allgemeine Zeugnisbemerkungen. */
	@Schema(required = true, description = "der Text für allgemeine Zeugnisbemerkungen", example="")	
	public @NotNull String zeugnisAllgemein = ""; 

	/** Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten. */
	@Schema(required = true, description = "der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten", example="")	
	public @NotNull String zeugnisASV = ""; 
	
	/** Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen. */
	@Schema(required = true, description = "der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen", example="")	
	public @NotNull String zeugnisLELS = ""; 

	/** Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement. */
	@Schema(required = true, description = "der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement", example="")	
	public @NotNull String zeugnisAUE = ""; 

	/** Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I. */
	@Schema(required = true, description = "der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I", example="")	
	public @NotNull String uebergangESF = ""; 

	/** Eine Bemerkung zum Förderschwerpunkt. */
	@Schema(required = true, description = "eine Bemerkung zum Förderschwerpunkt", example="")	
	public @NotNull String foerderschwerpunkt = ""; 

	/** Eine Bemerkung zur Versetzungsentscheidung. */
	@Schema(required = true, description = "eine Bemerkung zur Versetzungsentscheidung", example="")	
	public @NotNull String versetzungsentscheidung = ""; 

}
