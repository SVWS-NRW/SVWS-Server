package de.svws_nrw.core.types.reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

class TestReportingReportvorlage {

	/** Erlaubtes Muster: nur ASCII-Buchstaben, Ziffern, Unterstriche und Bindestriche – keine Umlaute, kein ß. */
	private static final Pattern PATTERN_ERLAUBTE_ZEICHEN = Pattern.compile("^[a-zA-Z0-9_-]+$");

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	void testBezeichnungenSindEindeutigCaseInsensitive() {
		final Map<String, List<String>> bezeichnungToEnumNamen = new LinkedHashMap<>();

		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			final String key = normalize(reportvorlage.getBezeichnung());
			bezeichnungToEnumNamen.computeIfAbsent(key, k -> new ArrayList<>()).add(reportvorlage.name());
		}

		final List<String> konflikte = new ArrayList<>();
		for (final Map.Entry<String, List<String>> entry : bezeichnungToEnumNamen.entrySet()) {
			if (entry.getValue().size() > 1) {
				konflikte.add("Bezeichnung '" + entry.getKey() + "' mehrfach verwendet von: " + entry.getValue());
			}
		}

		if (!konflikte.isEmpty()) {
			fail("Nicht eindeutige Reportvorlagen-Bezeichnungen (case-insensitive):\n" + String.join("\n", konflikte));
		}
	}

	@Test
	void testGruppenUndParameterProEnumWertEindeutigCaseInsensitive() {
		final List<String> konflikte = new ArrayList<>();

		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			final Map<String, List<String>> gruppenNamen = new LinkedHashMap<>();
			final Map<String, List<String>> parameterNamen = new LinkedHashMap<>();

			for (final ReportingReportvorlageParameterGruppe gruppe : reportvorlage.getReportingParameter().reportvorlageParameterGruppen) {
				if (gruppe == null) {
					continue;
				}

				final String gruppenNameOriginal = (gruppe.name == null) ? "" : gruppe.name;
				final String gruppenNameNorm = normalize(gruppenNameOriginal);
				gruppenNamen.computeIfAbsent(gruppenNameNorm, k -> new ArrayList<>()).add(gruppenNameOriginal);

				if (gruppe.reportvorlageParameter == null) {
					continue;
				}

				for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
					if (parameter == null) {
						continue;
					}
					final String parameterNameOriginal = (parameter.name == null) ? "" : parameter.name;
					final String parameterNameNorm = normalize(parameterNameOriginal);
					parameterNamen.computeIfAbsent(parameterNameNorm, k -> new ArrayList<>())
							.add(parameterNameOriginal + " (Gruppe: " + gruppenNameOriginal + ")");
				}
			}

			for (final Map.Entry<String, List<String>> entry : gruppenNamen.entrySet()) {
				if (entry.getValue().size() > 1) {
					konflikte.add("[" + reportvorlage.name() + "] Gruppenname '" + entry.getKey() + "' mehrfach: " + entry.getValue());
				}
			}

			for (final Map.Entry<String, List<String>> entry : parameterNamen.entrySet()) {
				if (entry.getValue().size() > 1) {
					konflikte.add("[" + reportvorlage.name() + "] Parametername '" + entry.getKey()
							+ "' über alle Gruppen mehrfach: " + entry.getValue());
				}
			}
		}

		if (!konflikte.isEmpty()) {
			fail("Nicht eindeutige Gruppen-/Parameternamen pro Enum-Wert (case-insensitive):\n" + String.join("\n", konflikte));
		}
	}

	@Test
	void testGruppenUndParameternamenSindNichtLeer() {
		final List<String> fehler = new ArrayList<>();

		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			for (final ReportingReportvorlageParameterGruppe gruppe : reportvorlage.getReportingParameter().reportvorlageParameterGruppen) {
				if (gruppe == null) {
					fehler.add("[" + reportvorlage.name() + "] Gruppe ist null");
					continue;
				}

				if ((gruppe.name == null) || gruppe.name.isBlank()) {
					fehler.add("[" + reportvorlage.name() + "] Gruppenname ist leer/null");
				}

				if (gruppe.reportvorlageParameter == null) {
					fehler.add("[" + reportvorlage.name() + "] Parameterliste der Gruppe '" + gruppe.name + "' ist null");
					continue;
				}

				for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
					if (parameter == null) {
						fehler.add("[" + reportvorlage.name() + "] Parameter in Gruppe '" + gruppe.name + "' ist null");
						continue;
					}
					if ((parameter.name == null) || parameter.name.isBlank()) {
						fehler.add("[" + reportvorlage.name() + "] Parametername ist leer/null (Gruppe: '" + gruppe.name + "')");
					}
				}
			}
		}

		if (!fehler.isEmpty()) {
			fail("Leere/ungültige Gruppen- oder Parameternamen gefunden:\n" + String.join("\n", fehler));
		}
	}

	@Test
	void testBezeichnungenEnthaltenNurErlaubteZeichen() {
		final List<String> fehler = new ArrayList<>();

		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			final String bezeichnung = reportvorlage.getBezeichnung();

			if (bezeichnung.contains(" ")) {
				fehler.add("[" + reportvorlage.name() + "] Bezeichnung '" + bezeichnung + "' enthält Leerzeichen");
			}

			if (!bezeichnung.isEmpty() && !PATTERN_ERLAUBTE_ZEICHEN.matcher(bezeichnung).matches()) {
				fehler.add("[" + reportvorlage.name() + "] Bezeichnung '" + bezeichnung
						+ "' enthält unerlaubte Zeichen (erlaubt: a-zA-Z, 0-9, _, -)");
			}
		}

		if (!fehler.isEmpty()) {
			fail("Bezeichnungen mit unerlaubten Zeichen gefunden:\n" + String.join("\n", fehler));
		}
	}

	@Test
	void testGruppennamenEnthaltenNurErlaubteZeichen() {
		final List<String> fehler = new ArrayList<>();

		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			for (final ReportingReportvorlageParameterGruppe gruppe : reportvorlage.getReportingParameter().reportvorlageParameterGruppen) {
				if ((gruppe == null) || (gruppe.name == null) || gruppe.name.isBlank()) {
					continue;
				}

				if (gruppe.name.contains(" ")) {
					fehler.add("[" + reportvorlage.name() + "] Gruppenname '" + gruppe.name + "' enthält Leerzeichen");
				}

				if (!PATTERN_ERLAUBTE_ZEICHEN.matcher(gruppe.name).matches()) {
					fehler.add("[" + reportvorlage.name() + "] Gruppenname '" + gruppe.name
							+ "' enthält unerlaubte Zeichen (erlaubt: a-zA-Z, 0-9, _, -)");
				}
			}
		}

		if (!fehler.isEmpty()) {
			fail("Gruppennamen mit unerlaubten Zeichen gefunden:\n" + String.join("\n", fehler));
		}
	}

	@Test
	void testParameternamenEnthaltenNurErlaubteZeichen() {
		final List<String> fehler = new ArrayList<>();

		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			for (final ReportingReportvorlageParameterGruppe gruppe : reportvorlage.getReportingParameter().reportvorlageParameterGruppen) {
				if ((gruppe == null) || (gruppe.reportvorlageParameter == null)) {
					continue;
				}

				final String gruppenName = (gruppe.name == null) ? "<unbekannt>" : gruppe.name;

				for (final ReportingReportvorlageParameter parameter : gruppe.reportvorlageParameter) {
					if ((parameter == null) || (parameter.name == null) || parameter.name.isBlank()) {
						continue;
					}

					if (parameter.name.contains(" ")) {
						fehler.add("[" + reportvorlage.name() + "] Parametername '" + parameter.name
								+ "' (Gruppe: '" + gruppenName + "') enthält Leerzeichen");
					}

					if (!PATTERN_ERLAUBTE_ZEICHEN.matcher(parameter.name).matches()) {
						fehler.add("[" + reportvorlage.name() + "] Parametername '" + parameter.name
								+ "' (Gruppe: '" + gruppenName + "') enthält unerlaubte Zeichen (erlaubt: a-zA-Z, 0-9, _, -)");
					}
				}
			}
		}

		if (!fehler.isEmpty()) {
			fail("Parameternamen mit unerlaubten Zeichen gefunden:\n" + String.join("\n", fehler));
		}
	}

	/** Nennt eine Vorlage keine Schulform, so steht sie jeder Schule offen - auch ohne bekannte Schulform. */
	@Test
	void testEineVorlageOhneSchulformenGiltUeberall() {
		final ReportingReportvorlage reportvorlage = ersteVorlageMitSchulformen(false);

		for (final Schulform schulform : Schulform.values()) {
			assertTrue(reportvorlage.giltFuerSchulform(schulform), schulform.name());
		}
		assertTrue(reportvorlage.giltFuerSchulform(null));
	}

	/** Nennt eine Vorlage Schulformen, so gilt sie an diesen und sonst nirgends. */
	@Test
	void testEineVorlageMitSchulformenGiltNurAnDiesen() {
		final ReportingReportvorlage reportvorlage = ersteVorlageMitSchulformen(true);
		final List<Schulform> genannte = reportvorlage.getSchulformen();

		for (final Schulform schulform : genannte) {
			assertTrue(reportvorlage.giltFuerSchulform(schulform), schulform.name());
		}
		for (final Schulform schulform : Schulform.values()) {
			if (!genannte.contains(schulform)) {
				assertFalse(reportvorlage.giltFuerSchulform(schulform), schulform.name());
			}
		}
		assertFalse(reportvorlage.giltFuerSchulform(null));
	}

	/**
	 * Bindet die im Quelltext genannten Schulformen an den Katalog: Jede Vorlage, die Schulformen nennt, ist eine GOSt-Vorlage und muss die Schulformen
	 * führen, für die {@code SchulformKatalogEintrag.hatGymOb} gilt. Kommt eine Vorlage mit anderer Bindung hinzu, ist der Test zu erweitern.
	 */
	@Test
	void testDieGenanntenSchulformenEntsprechenDemKatalog() {
		final Set<Schulform> mitGymnasialerOberstufe = Set.copyOf(Schulform.getListAllMitGymOb());

		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			final List<Schulform> schulformen = reportvorlage.getSchulformen();
			if (!schulformen.isEmpty()) {
				assertEquals(mitGymnasialerOberstufe, Set.copyOf(schulformen), reportvorlage.name());
			}
		}
	}

	/**
	 * Liefert eine Vorlage mit oder ohne genannte Schulformen. Die Auswahl über diese Eigenschaft statt über den Namen hält die Tests unabhängig davon,
	 * welche Schulformen eine einzelne Vorlage nennt.
	 *
	 * @param mitSchulformen true für eine Vorlage mit genannten Schulformen, false für eine ohne.
	 *
	 * @return Die erste passende Vorlage.
	 */
	private static ReportingReportvorlage ersteVorlageMitSchulformen(final boolean mitSchulformen) {
		for (final ReportingReportvorlage reportvorlage : ReportingReportvorlage.values()) {
			if (reportvorlage.getSchulformen().isEmpty() != mitSchulformen) {
				return reportvorlage;
			}
		}
		return fail("Keine Reportvorlage " + (mitSchulformen ? "mit" : "ohne") + " genannte Schulformen vorhanden");
	}

	private static String normalize(final String value) {
		return value.trim().toLowerCase(Locale.ROOT);
	}
}
