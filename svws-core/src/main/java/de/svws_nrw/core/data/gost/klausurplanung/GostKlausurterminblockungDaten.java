package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO enthält die Daten für die Klausurtermin-Blockung
 */
@XmlRootElement
@Schema(description = "Informationen für eine Klausurtermin-Blockung der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostKlausurterminblockungDaten {

	/** Die Konfiguration für den Blockungs-Algorithmus */
	public @NotNull GostKlausurterminblockungKonfiguration konfiguration = new GostKlausurterminblockungKonfiguration();

	/** Die Kurs-Klausuren, für welche die Blockung durchgeführt werden soll. */
	public @NotNull List<@NotNull GostKursklausur> klausuren = new ArrayList<>();

}
