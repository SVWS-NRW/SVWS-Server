package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;


import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse repräsentiert ein Update zu Regen einer Blockung der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Informationen zu einem Update zu Regen einer Blockung der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostBlockungRegelUpdate {

	/** Die zu entfernenden Regeln */
	public @NotNull List<@NotNull GostBlockungRegel> listEntfernen = new ArrayList<>();

	/** Die hinzuzufügenden Regeln */
	public @NotNull List<@NotNull GostBlockungRegel> listHinzuzufuegen = new ArrayList<>();

}
