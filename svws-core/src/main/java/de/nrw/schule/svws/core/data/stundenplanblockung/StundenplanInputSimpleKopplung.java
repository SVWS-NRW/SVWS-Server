package de.nrw.schule.svws.core.data.stundenplanblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Kopplung bei
 * {@link StundenplanInputSimple}. Kurse der selben Kopplung dürfen in einer Klasse parallel stattfinden. */
@XmlRootElement(name = "StundenplanInputSimpleKopplung")
@Schema(name = "StundenplanInputSimpleKopplung", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Kopplung bei {@link StundenplanInputSimple}. Kurse der selben Kopplung dürfen in einer Klasse parallel stattfinden.")
@TranspilerDTO
public class StundenplanInputSimpleKopplung {

	/** Die ID der Kopplung. */
	public long id;

	/** Das Kürzel des Kopplung. Beispielsweise '5RE' oder 'Q1LK1'. */
	public @NotNull String kuerzel = "";

	/** Die Anzahl der Stunden der Kopplung. Muss mindestens so groß sein, wie der Kurs mit den meisten Stunden in
	 * dieser Kopplung. */
	public int stunden = -1;

}
