package de.svws_nrw.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse enthält die Informationen zu der Fachbelegung eines Schülers für das Abitur der 
 * gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description="enthält die Informationen zu der Fachbelegung eines Schülers für das Abitur der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "fachID", "letzteKursart", "abiturFach", "istFSNeu", "block1PunktSumme", "block1NotenpunkteDurchschnitt",
    "block2NotenKuerzelPruefung", "block2PunkteZwischenstand", "block2MuendlichePruefungAbweichung", "block2MuendlichePruefungBestehen",
    "block2MuendlichePruefungFreiwillig", "block2MuendlichePruefungReihenfolge", "block2MuendlichePruefungNotenKuerzel", "block2Punkte",
    "block2Pruefer", "belegungen"})
@TranspilerDTO
public class AbiturFachbelegung {
	
	/** Die ID des Faches der Gymnasialen Oberstufe, welches belegt wurde. */
	public long fachID = -1;
	
	/** Die letzte Kursart der Gymnasialen Oberstufe (LK, GK, ZK, PJK, VTF), mit welcher das Fach belegt wurde */
	public String letzteKursart = null;
	
	/** Gibt an, als welches Abiturfach das Fach belegt wurde (1,2,3,4 oder null) */
	public Integer abiturFach = null;
	
	/** Gibt an, ob es sich um die Belegung einer neuen Fremdsprache handelt */
	public boolean istFSNeu = false; // pruefe immer durch Lesen aus den Leistungsdaten: ist Neu, darf neu reicht nicht --> Sprachenfolgen prüfen!
    // TODO sollte hier eine Diskrepanz zwischen istFSNeu und DarfFSNeu bestehen, so liegt ein Fehler vor - inkonsistente Daten...
    
	
	/** Die Punktsumme im Block I des Abiturs für die Fachbelegung */
	public Integer block1PunktSumme = null;
	
	/** Der Durchschnitt der Notenpunkte im Block I des Abiturs für die Fachbelegung */
	public Double block1NotenpunkteDurchschnitt = null;
	
	
	/** Das Notenkürzel der Abiturprüfungsnote, sofern dies die Belegung eines Abiturfaches ist. */
	public String block2NotenKuerzelPruefung = null;
	
	/** Der Zwischenstand der erreichten Punkte im Abitur vor einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist. */
	public Integer block2PunkteZwischenstand = null;
	
	/** Gibt an, ob eine mündliche Abweichungsprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist (nur bis Abiturjahrgang 2019, ab 2020 gibt es keine Abweichungsprüfungen mehr). */
	public Boolean block2MuendlichePruefungAbweichung = null;
	
	/** Gibt an, ob eine mündliche Bestehensprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist. */
	public Boolean block2MuendlichePruefungBestehen = null;
	
	/** Gibt an, ob eine freiwillige mündliche Prüfung angesetzt wurde, sofern dies die Belegung eines schriftlichen Abiturfaches ist. */
	public Boolean block2MuendlichePruefungFreiwillig = null;
	
	/** Gibt die Reihenfolge bei den angesetzten mündlichen Prüfungen an, sofern dies die Belegung eines schriftlichen Abiturfaches ist. */
	public Integer block2MuendlichePruefungReihenfolge = null;
	
	/** Das Notenkürzel der mündlichen Abiturprüfung, sofern es sich um eine mündliche Prüfung bei einer Belegung eines schriftlichen Abiturfaches handelt. */
	public String block2MuendlichePruefungNotenKuerzel = null;
	
	/** Die erreichten Punkte im Abitur nach einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist. */
	public Integer block2Punkte = null;
	
	/** Die Lehrer-ID des Prüfers im Rahmen der Abiturprüfung. */
	public Long block2Pruefer = null;
	
	/** Die Einzelbelegungen des Faches in den einzelnen Halbjahren im Block I des Abiturs */
	public final @NotNull AbiturFachbelegungHalbjahr[] belegungen = new AbiturFachbelegungHalbjahr[GostHalbjahr.maxHalbjahre];

	
}
