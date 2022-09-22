package de.nrw.schule.svws.core.data.enm;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu Schüler-spezifischen
 * Bemerkungen eines Schüler in Bezug auf den Lernabschnitt für das 
 * Externe-Noten-Modul ENM. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu Schüler-spezifischen Bemerkungen eines Schüler in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM. ")
@TranspilerDTO
public class ENMLeistungBemerkungen {

	/** Informationen zum Arbeits- und Sozialverhalten */
	@Schema(required = false, description = "Informationen zum Arbeits- und Sozialverhalten.", example="Text zum ASV")
    public String ASV;

    /** Informationen zu dem Außerunterrichtlichen Engagement (AUE) */
	@Schema(required = true, description = "Informationen zu dem Außerunterrichtlichen Engagement (AUE).", example="Text zum AUE")
    public String AUE;

    /** Zeugnisbemerkungen */
	@Schema(required = false, description = "Zeugnisbemerkungen.", example="Text der Zeugnisbemerkung")
    public String ZB;

    /** Bemerkungen zur Lern und Leistungsentwicklung (LELS) in den Fächern */
	@Schema(required = false, description = "Bemerkungen zur Lern und Leistungsentwicklung (LELS) in den Fächern.", example="Text zum LELS")
    public String LELS;

    /** Schulform-Empfehlungen */
	@Schema(required = false, description = "Schulform-Empfehlungen.", example="R")
    public String schulformEmpf;

    /** Individuelle Bemerkungen zur Versetzung */
	@Schema(required = false, description = "Individuelle Bemerkungen zur Versetzung.", example="Text zur Versetzung")
    public String individuelleVersetzungsbemerkungen;

    /** Förderbemerkungen */
	@Schema(required = false, description = "Förderbemerkungen.", example="Text zum Förderschwerpunkt")
    public String foerderbemerkungen;	

}
