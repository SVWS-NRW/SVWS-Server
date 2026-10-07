package de.svws_nrw.mapper.lehrer.lehramt;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;

public record LehrerLehramtMappingContext(
		List<LehrerFachrichtungEintrag> fachrichtungen,
		List<LehrerLehrbefaehigungEintrag> lehrbefaehigungen
) {
	/**
	 * @return empty {@link LehrerLehramtMappingContext}
	 */
	public static LehrerLehramtMappingContext empty() {
		return new LehrerLehramtMappingContext(List.of(), List.of());
	}
}
