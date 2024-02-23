package de.svws_nrw.core.data.gost;

import java.util.List;
import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse ist die Core-DTO für die Daten eine Kursblockung
 */
@XmlRootElement
@Schema(description = "Informationen zu einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "name", "gostHalbjahr", "schienen", "regeln" })
@TranspilerDTO
public class GostBlockungsdaten {

	/** Die ID der Blockung */
	public long id = -1;

	/** Der Name der Blockung */
	public @NotNull String name = "Neue Blockung";

	/** Der Abiturjahrgang, dem die Kursblockung zugeordnet ist */
	public int abijahrgang = -1;

	/** Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	public int gostHalbjahr = GostHalbjahr.EF1.id;

	/** Gibt an, ob diese Blockung als aktiv markiert wurde. */
	public boolean istAktiv = false;

	/** Die Definition der Schienen */
	public @NotNull List<@NotNull GostBlockungSchiene> schienen = new ArrayList<>();

	/** Die Definition der Regeln */
	public @NotNull List<@NotNull GostBlockungRegel> regeln = new ArrayList<>();

	/** Die für die Blockung angelegten Kurse */
	public @NotNull List<@NotNull GostBlockungKurs> kurse = new ArrayList<>();

	/** Die SchülerInnen für die Blockung. */
	public @NotNull List<@NotNull Schueler> schueler = new ArrayList<>();

	/** Die Fachwahlen für die Blockung */
	public @NotNull List<@NotNull GostFachwahl> fachwahlen = new ArrayList<>();

	/** Eine Liste der Ergebnisse, die der Blockungsdefinition zugeordnet sind.  */
	public final @NotNull List<@NotNull GostBlockungsergebnis> ergebnisse = new ArrayList<>();

}
