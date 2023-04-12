package de.svws_nrw.core.data.stundenplanblockung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Regel bei {@link StundenplanblockungInput}.
 *
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "StundenplanblockungRegel")
@Schema(name = "StundenplanblockungRegel", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Regel bei {@link StundenplanblockungInput}.")
@TranspilerDTO
public class StundenplanblockungRegel {

	/** Die Datenbank-ID der Regel. */
	public long id;

	/** Der Type der Regel - siehe {@link GostKursblockungRegelTyp} */
	public int typ = -1;

	/** Eine Liste der Regel-Parameter */
	public @NotNull List<@NotNull Long> parameter = new ArrayList<>();

}
