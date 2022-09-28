package de.nrw.schule.svws.core.data.gost;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Core-DTO für ein Ergebnis einer Kursblockung
 */
@XmlRootElement
@Schema(description="Informationen zu dem Ergebnis einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "blockungID", "name", "gostHalbjahr", "anzahlUmwaehler", "anzahlKollisionen", "anzahlSchienenMitKollisionen", "bewertung", "istMarkiert", "schienen" })
@TranspilerDTO
public class GostBlockungsergebnis {

	/** Die ID des Zwischenergebnisses der Blockung */
	public long id = -1;
	
	/** Die ID der Blockung */
	public long blockungID = -1;
	
	/** Der Name der Blockung */
	public @NotNull String name = "Blockung";

	/** Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	public int gostHalbjahr = 0;
	
	/** Die Anzahl der nötigen Umwähler bei diesem Blockungsergebnis */
	public int anzahlUmwaehler = 0;
	
	/** Die Anzahl der Kollisionen in diesem Blockungsergebnis */
	public int anzahlKollisionen = 0;
	
	/** Die Anzahl der Schienen mit Kollisionen in diesem Blockungsergebnis */
	public int anzahlSchienenMitKollisionen = 0;
	
	/** Die Bewertung dieser Blockung als numerischer Wert, welche eine automatisierte Qualitätsbewertung des Ergebnisses darstellt. */
	public long bewertung = -1;
	
	/** Gibt an, ob dieses Ergebnis markiert wurde. Dies kann verwendet werden, um besonders geeignete Blockungsergebnisse hervorzuheben. */
	public boolean istMarkiert = false;
	
	/** Gibt an, ob dieses Ergebnis ein Duplikat aus einer anderen Blockung ist und als Grundlage für diese Blockungsdefinition dienen soll. */
	public boolean istDuplikat = false; 

	/** Eine Liste der Schienen, welche zugeordnete Kurse beinhalten. */
	public final @NotNull Vector<@NotNull GostBlockungsergebnisSchiene> schienen = new Vector<>();
	
}
