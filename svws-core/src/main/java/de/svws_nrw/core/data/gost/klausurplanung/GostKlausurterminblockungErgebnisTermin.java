package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO enthält einen Termin von einem Ergebnis einer Klausurtermin-Blockung
 */
@XmlRootElement
@Schema(description = "Ein Termin von einem Ergebnis einer Klausurtermin-Blockung der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostKlausurterminblockungErgebnisTermin {

	/** Eine Liste der, dem Termin zugeordneten, Kurs-Klausur-IDs */
	public @NotNull List<@NotNull Long> kursklausuren = new ArrayList<>();

}
