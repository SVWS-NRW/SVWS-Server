package de.svws_nrw.mapper.schueler.lernabschnittsdaten;

import java.util.List;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen;

public record SchuelerLernabschnittsdatenMappingContext(
		SchuelerLernabschnittBemerkungen bemerkung,
		List<SchuelerLeistungsdaten> leistungsdaten
) {
	/**
	 * @return empty {@link SchuelerLernabschnittsdatenMappingContext}
	 */
	public static SchuelerLernabschnittsdatenMappingContext empty() {
		return new SchuelerLernabschnittsdatenMappingContext(new SchuelerLernabschnittBemerkungen(), List.of());
	}
}
