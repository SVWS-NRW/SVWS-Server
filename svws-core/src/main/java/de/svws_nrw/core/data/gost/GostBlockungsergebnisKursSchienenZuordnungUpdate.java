package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist die Core-DTO für ein Update der Kurs-Schienen-Zuordnungen eines Ergebnisses einer Kursblockung
 */
@XmlRootElement
@Schema(description = "Informationen zu einem Update der Kurs-Schienen-Zuordnung eines Ergebnisses einer Blockung der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostBlockungsergebnisKursSchienenZuordnungUpdate {

	/** Die zu entfernenden Zuordnungen */
	public @NotNull List<@NotNull GostBlockungsergebnisKursSchienenZuordnung> listEntfernen = new ArrayList<>();

	/** Die hinzuzufügenden Zuordnungen */
	public @NotNull List<@NotNull GostBlockungsergebnisKursSchienenZuordnung> listHinzuzufuegen = new ArrayList<>();

	/** Die Blockungs-Regeln, die dabei angepasst werden sollen. Das ist nur zulässig, wenn nur ein Blockungsergebnis für die Blockung vorhanden ist. */
	public @NotNull GostBlockungRegelUpdate regelUpdates = new GostBlockungRegelUpdate();

}
