package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
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

	/** Der Zeitstempel mit den letzten Änderungen zu Bemerkungen zum Arbeits- und Sozialverhalten */
	@Schema(required = false, description = "Der Zeitstempel mit den letzten Änderungen zu Bemerkungen zum Arbeits- und Sozialverhalten.", example="2013-11-14 13:12:48.774")
	public String tsASV;
	
    /** Informationen zu dem Außerunterrichtlichen Engagement (AUE) */
	@Schema(required = true, description = "Informationen zu dem Außerunterrichtlichen Engagement (AUE).", example="Text zum AUE")
    public String AUE;

	/** Der Zeitstempel mit den letzten Änderungen zu Bemerkungen zum Außerunterrichtlichen Engagement (AUE) */
	@Schema(required = false, description = "Der Zeitstempel mit den letzten Änderungen zu Bemerkungen zum Außerunterrichtlichen Engagement (AUE).", example="2013-11-14 13:12:48.774")
	public String tsAUE;
	
    /** Zeugnisbemerkungen */
	@Schema(required = false, description = "Zeugnisbemerkungen.", example="Text der Zeugnisbemerkung")
    public String ZB;

	/** Der Zeitstempel mit den letzten Änderungen zu Zeugnis-Bemerkungen */
	@Schema(required = false, description = "Der Zeitstempel mit den letzten Änderungen zu Zeugnis-Bemerkungen.", example="2013-11-14 13:12:48.774")
	public String tsZB;

    /** Bemerkungen zur Lern und Leistungsentwicklung (LELS) in den Fächern */
	@Schema(required = false, description = "Bemerkungen zur Lern und Leistungsentwicklung (LELS) in den Fächern.", example="Text zum LELS")
    public String LELS;

    /** Schulform-Empfehlungen */
	@Schema(required = false, description = "Schulform-Empfehlungen.", example="R")
    public String schulformEmpf;

    /** Individuelle Bemerkungen zur Versetzung */
	@Schema(required = false, description = "Individuelle Bemerkungen zur Versetzung.", example="Text zur Versetzung")
    public String individuelleVersetzungsbemerkungen;

	/** Der Zeitstempel mit den letzten Änderungen zu individuellen Bemerkungen zur Versetzung */
	@Schema(required = false, description = "Der Zeitstempel mit den letzten Änderungen zu individuellen Bemerkungen zur Versetzung.", example="2013-11-14 13:12:48.774")
	public String tsIndividuelleVersetzungsbemerkungen;

    /** Förderbemerkungen */
	@Schema(required = false, description = "Förderbemerkungen.", example="Text zum Förderschwerpunkt")
    public String foerderbemerkungen;	

}
