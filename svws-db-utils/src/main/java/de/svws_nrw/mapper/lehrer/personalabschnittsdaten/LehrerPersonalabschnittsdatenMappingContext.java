package de.svws_nrw.mapper.lehrer.personalabschnittsdaten;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerFunktion;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;

public record LehrerPersonalabschnittsdatenMappingContext(
		List<LehrerPersonalabschnittsdatenAnrechnungsstunden> anrechnungen,
		List<LehrerPersonalabschnittsdatenAnrechnungsstunden> mehrleistung,
		List<LehrerPersonalabschnittsdatenAnrechnungsstunden> minderleistung,
		List<LehrerFunktion> funktionen
) {
	/**
	 * @return empty {@link LehrerPersonalabschnittsdatenMappingContext}
	 */
	public static LehrerPersonalabschnittsdatenMappingContext empty() {
		return new LehrerPersonalabschnittsdatenMappingContext(List.of(), List.of(), List.of(), List.of());
	}
}
