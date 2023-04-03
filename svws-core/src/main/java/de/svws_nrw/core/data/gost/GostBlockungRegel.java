package de.svws_nrw.core.data.gost;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert eine Regel einer Blockung der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Informationen zu einer Regel einer Blockung der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "typ", "parameter" })
@TranspilerDTO
public class GostBlockungRegel {

	/** Die ID der Regel */
	public long id = -1;

	/** Der Type der Regel - siehe {@link GostKursblockungRegelTyp} */
	public int typ = -1;

	/** Eine Liste der Regel-Parameter */
	public @NotNull Vector<@NotNull Long> parameter = new Vector<>();

}
