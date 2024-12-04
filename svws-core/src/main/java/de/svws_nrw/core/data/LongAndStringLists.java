package de.svws_nrw.core.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist die ein allgemeines Core-DTO für zwei Listen aus Long-Werten und String-Werten.
 */
@XmlRootElement
@Schema(description = "Long-Werte und String-Werte in Listen.")
@TranspilerDTO
public class LongAndStringLists {

	/** Die Lehrer, die diesem Kurs bereits fest zugeordnet sind. */
	public @NotNull List<Long> numbers = new ArrayList<>();

	/** Die Lehrer, die diesem Kurs bereits fest zugeordnet sind. */
	public @NotNull List<String> strings = new ArrayList<>();

}
