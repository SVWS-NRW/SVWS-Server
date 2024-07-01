package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;


import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse repräsentiert ein Update zu den Zuordnungen von Pausenaufsichten zu Aufsichtsbereichen in einem Stundenplan.
 */
@XmlRootElement
@Schema(description = "Informationen zu einem Update zu den Zuordnungen von Pausenaufsichten zu Aufsichtsbereichen in einem Stundenplan.")
@TranspilerDTO
public class StundenplanPausenaufsichtBereichUpdate {

	/** Die zu entfernenden Zuordnungen */
	public @NotNull List<StundenplanPausenaufsichtBereich> listEntfernen = new ArrayList<>();

	/** Die hinzuzufügenden Zuordnungen */
	public @NotNull List<StundenplanPausenaufsichtBereich> listHinzuzufuegen = new ArrayList<>();

}
