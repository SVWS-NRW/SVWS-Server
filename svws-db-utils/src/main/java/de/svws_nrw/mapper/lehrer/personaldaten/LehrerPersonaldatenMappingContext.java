package de.svws_nrw.mapper.lehrer.personaldaten;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;

/**
 * LehrerPersonaldatenMappingContext
 *
 * @param personalabschnittsdaten Liste der Personalabschnittsdaten
 * @param lehraemter Liste der Lehrämter
 */
public record LehrerPersonaldatenMappingContext(
		List<LehrerPersonalabschnittsdaten> personalabschnittsdaten,
		List<LehrerLehramtEintrag> lehraemter
) {
	/**
	 * @return empty {@link LehrerPersonaldatenMappingContext}
	 */
	public static LehrerPersonaldatenMappingContext empty() {
		return new LehrerPersonaldatenMappingContext(List.of(), List.of());
	}
}
