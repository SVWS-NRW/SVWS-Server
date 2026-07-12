package de.svws_nrw.core.types.reporting.reportvorlagekonfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;

class TestReportingReportvorlageKonfigurationBenutzerweit {

	/** Erlaubtes Muster: nur ASCII-Buchstaben, Ziffern, Unterstriche und Bindestriche – keine Umlaute, kein ß. */
	private static final Pattern PATTERN_ERLAUBTE_ZEICHEN = Pattern.compile("^[a-zA-Z0-9_-]+$");

	@Test
	void testKatalogParameterNamenSindEindeutigUndGueltig() {
		final Map<String, List<String>> parameterNamen = new LinkedHashMap<>();
		final List<String> fehler = new ArrayList<>();

		for (final ReportingReportvorlageParameterGruppe gruppe : ReportingReportvorlageKonfigurationBenutzerweit.getBenutzerweiteParameterGruppen()) {
			assertNotNull(gruppe, "Katalog-Gruppe ist null");
			if ((gruppe.name == null) || gruppe.name.isBlank()) {
				fehler.add("Gruppenname ist leer/null");
			}
			assertNotNull(gruppe.reportvorlageParameter, "Parameterliste der Gruppe '" + gruppe.name + "' ist null");

			for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
				assertNotNull(parameter, "Parameter in Gruppe '" + gruppe.name + "' ist null");
				if ((parameter.name == null) || parameter.name.isBlank()) {
					fehler.add("Parametername ist leer/null (Gruppe: '" + gruppe.name + "')");
					continue;
				}
				if (!PATTERN_ERLAUBTE_ZEICHEN.matcher(parameter.name).matches()) {
					fehler.add("Parametername '" + parameter.name + "' enthält unerlaubte Zeichen (erlaubt: a-zA-Z, 0-9, _, -)");
				}
				parameterNamen.computeIfAbsent(parameter.name.trim().toLowerCase(Locale.ROOT), k -> new ArrayList<>())
						.add(parameter.name + " (Gruppe: " + gruppe.name + ")");
			}
		}

		for (final Map.Entry<String, List<String>> entry : parameterNamen.entrySet()) {
			if (entry.getValue().size() > 1) {
				fehler.add("Parametername '" + entry.getKey() + "' über alle Katalog-Gruppen mehrfach: " + entry.getValue());
			}
		}

		if (!fehler.isEmpty()) {
			fail("Ungültige Katalog-Definition:\n" + String.join("\n", fehler));
		}
	}

	private static Map<String, String> katalogParameter() {
		final Map<String, String> defaults = new LinkedHashMap<>();
		for (final ReportingReportvorlageParameterGruppe gruppe : ReportingReportvorlageKonfigurationBenutzerweit.getBenutzerweiteParameterGruppen()) {
			for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
				defaults.put(parameter.name, parameter.wert);
			}
		}
		return defaults;
	}
}
