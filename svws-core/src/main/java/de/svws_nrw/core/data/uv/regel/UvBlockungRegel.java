package de.svws_nrw.core.data.uv.regel;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse repräsentiert eine Regel einer Uv-Blockung der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Informationen zu einer Regel einer Uv-Blockung der gymnasialen Oberstufe.")
@TranspilerDTO
@SuppressWarnings("java:S1104") // Sonar soll bei DTOs diese Regel nicht anwenden.
public class UvBlockungRegel {

	/** Die ID der Regel. */
	public long id = -1;

	/** Der Type der Regel - siehe {@link UvBlockungRegelTyp}. */
	public int typ = UvBlockungRegelTyp.UNDEFINIERT.nr;

	/** Die Priorität der - siehe {@link UvBlockungRegelPrioritaet}. */
	public int prioritaet = UvBlockungRegelPrioritaet.MITTEL.nr;

	/** Eine Liste der Regel-Parameter. */
	public @NotNull List<Long> parameter = new ArrayList<>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvBlockungRegel() {
		// leer
	}


}

