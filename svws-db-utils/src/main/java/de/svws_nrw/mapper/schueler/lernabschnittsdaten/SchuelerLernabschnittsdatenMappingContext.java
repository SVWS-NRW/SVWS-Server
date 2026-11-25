package de.svws_nrw.mapper.schueler.lernabschnittsdaten;

import java.util.List;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen;

/**
 * Die Zusatzdaten für das Mapping eines Lernabschnitts, die nicht in der Entity der Lernabschnittsdaten selbst stehen.
 *
 * @param bemerkung           die Bemerkungen zum Lernabschnitt
 * @param leistungsdaten      die Leistungsdaten des Lernabschnitts
 * @param kennungFachklasse   die Kennung der Fachklasse des Lernabschnitts aus dem schulinternen Katalog oder {@code null}
 */
public record SchuelerLernabschnittsdatenMappingContext(
		SchuelerLernabschnittBemerkungen bemerkung,
		List<SchuelerLeistungsdaten> leistungsdaten,
		String kennungFachklasse
) {
	/**
	 * @return empty {@link SchuelerLernabschnittsdatenMappingContext}
	 */
	public static SchuelerLernabschnittsdatenMappingContext empty() {
		return new SchuelerLernabschnittsdatenMappingContext(new SchuelerLernabschnittBemerkungen(), List.of(), null);
	}
}
