package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse repräsentiert ein Update zu DTOs einer Klausurblockung.
 */

@XmlRootElement
@Schema(description = "Informationen zu einem Update einer Klausurblockung der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostKlausurenUpdate {

	/** Die zu entfernenden Regeln */
	public @NotNull List<@NotNull Long> listSchuelerklausurTermineRemoveIdTermin = new ArrayList<>();

	/** Die hinzuzufügenden Regeln */
	public @NotNull List<@NotNull Long> listKlausurtermineNachschreiberZugelassenFalse = new ArrayList<>();

}
