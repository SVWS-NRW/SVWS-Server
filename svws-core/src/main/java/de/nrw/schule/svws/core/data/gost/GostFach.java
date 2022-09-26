package de.nrw.schule.svws.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse stellt die Core-Types für 
 * Eigenschaften einen Oberstufenfaches für die Abiturberechnung zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
@XmlRootElement
@Schema(description="Informationen zu einem Fach der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "kuerzel", "kuerzelAnzeige", "bezeichnung", "sortierung", "istFremdsprache",
	"istFremdSpracheNeuEinsetzendMoeglich", "biliSprache",  // TODO istFremdSpracheNeuEinsetzendMoeglich --> kein Attribut 
	"istMoeglichEF1", "istMoeglichEF2", "istMoeglichQ11", "istMoeglichQ12", 
	"istMoeglichQ21", "istMoeglichQ22", "istMoeglichAbiLK", "istMoeglichAbiGK", 
	"wochenstundenEF1", "wochenstundenEF2", "wochenstundenQualifikationsphase", 
	"mussSchriftlichEF1", "mussSchriftlichEF2", "projektKursLeitfach1ID", 
	"projektKursLeitfach1Kuerzel", "projektKursLeitfach2ID", "projektKursLeitfach2Kuerzel" })
@TranspilerDTO
public class GostFach {

	/** Die ID des Faches */
	public long id = -1;
	
	/** Das Statistik-Kürzel des Faches */
	public @NotNull String kuerzel = "";
	
	/** Das Fach-Kürzel, welches zur Anzeige verwendet wird. */
	public String kuerzelAnzeige = null;
	
	/** Die Bezeichnung des Faches */
	public String bezeichnung = null;
	
	/** Die Nummer, welche die Sortierung der Fächer angibt. */
	public int sortierung = 32000;
	
	
	/** Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht */
	public boolean istFremdsprache = false;
	
	/** Gibt an, ob das Fache eine neu einsetzende Fremdsprache ist. */
	public boolean istFremdSpracheNeuEinsetzend = false;
	
	/** Gibt im Falle eines bilingualen Sachfaches das einstellige Fremdsprachenkürzel an. */
	public String biliSprache = null;
	
	
	/** Gibt an, ob das Fach als Leistungskurs im Abitur gewählt werden kann. */
	public boolean istMoeglichAbiLK = false;
	
	/** Gibt an, ob das Fach als Grundkurs im Abitur gewählt werden kann. */
	public boolean istMoeglichAbiGK = false;
	
	/** Gibt an, ob das Fach in der EF.1 gewählt werden kann. */
	public boolean istMoeglichEF1 = false;
	
	/** Gibt an, ob das Fach in der EF.2 gewählt werden kann. */
	public boolean istMoeglichEF2 = false;
	
	/** Gibt an, ob das Fach in der Q1.1 gewählt werden kann. */
	public boolean istMoeglichQ11 = false;
	
	/** Gibt an, ob das Fach in der Q1.2 gewählt werden kann. */
	public boolean istMoeglichQ12 = false;
	
	/** Gibt an, ob das Fach in der Q2.1 gewählt werden kann. */
	public boolean istMoeglichQ21 = false;

	/** Gibt an, ob das Fach in der Q2.2 gewählt werden kann. */
	public boolean istMoeglichQ22 = false;
	
	/** Die Wochenstundenzahl des Faches in der EF.1 */
	public int wochenstundenEF1 = 3;

	/** Die Wochenstundenzahl des Faches in der EF.2 */
	public int wochenstundenEF2 = 3;

	/** Die Wochenstundenzahl des Faches in der Qualifikationsphase */
	public int wochenstundenQualifikationsphase = 3;

	/** Gibt an, ob das Fach in der EF.1 schriftlich belegt werden muss */
	public boolean mussSchriftlichEF1 = false;

	/** Gibt an, ob das Fach in der EF.2 schriftlich belegt werden muss */
	public boolean mussSchriftlichEF2 = false;


	/** Die Fach-ID des Leitfaches eines Projektkurses oder Vertiefungsfaches */
	public Long projektKursLeitfach1ID = null;
	
	/** Das Fach-Kürzel des Leitfaches eines Projektkurses oder Vertiefungsfaches */
	public String projektKursLeitfach1Kuerzel = null;
	
	/** Die Fach-ID des zweiten Leitfaches eines Projektkurses */
	public Long projektKursLeitfach2ID = null;
	
	/** Die Fach-Kürzel des zweiten Leitfaches eines Projektkurses */
	public String projektKursLeitfach2Kuerzel = null;

}
