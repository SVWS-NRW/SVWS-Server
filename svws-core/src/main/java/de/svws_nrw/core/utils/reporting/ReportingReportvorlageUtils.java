package de.svws_nrw.core.utils.reporting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.data.reporting.ReportingEMailDaten;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinition;
import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.data.reporting.ReportingFilterEintrag;
import de.svws_nrw.core.data.reporting.ReportingFilterKriterium;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinition;
import de.svws_nrw.core.data.reporting.ReportingSortierungDefinitionGruppe;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.reporting.ReportingAusgabeformat;
import de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp;
import de.svws_nrw.core.types.reporting.ReportingFilterVerknuepfung;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.core.types.reporting.ReportingUIKomponentenTyp;
import jakarta.validation.constraints.NotNull;

public final class ReportingReportvorlageUtils {

	private ReportingReportvorlageUtils() {
		// Utility-Klasse
	}

	/**
	 * Wandelt einen {@link ServerMode} in den zu speichernden Text-Wert um. Der Modus {@link ServerMode#STABLE} (und null) wird als leerer String abgebildet,
	 * da er semantisch identisch zu "in allen Modi verfügbar" ist.
	 *
	 * @param serverMode der ServerMode
	 *
	 * @return der Text-Wert des ServerMode oder ein leerer String bei STABLE
	 */
	private static @NotNull String serverModeText(final ServerMode serverMode) {
		return ((serverMode == null) || (serverMode == ServerMode.STABLE)) ? "" : serverMode.text;
	}

	/**
	 * Wandelt eine Liste von {@link BenutzerKompetenz} in eine Liste ihrer IDs um. Ein null-Wert wird als leere Liste (= keine Kompetenz erforderlich)
	 * interpretiert.
	 *
	 * @param kompetenzen die Liste der Benutzerkompetenzen oder null
	 *
	 * @return die Liste der Kompetenz-IDs (ggf. leer)
	 */
	private static @NotNull List<Long> kompetenzIds(final List<BenutzerKompetenz> kompetenzen) {
		final List<Long> ids = new ArrayList<>();
		if (kompetenzen != null) {
			for (final BenutzerKompetenz kompetenz : kompetenzen) {
				if (kompetenz != null) {
					ids.add(kompetenz.daten.id);
				}
			}
		}
		return ids;
	}

	/**
	 * Erstellt ein ReportingParameter-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param ausgabeformatOptionen                 Liste von Ausgabeformat-Optionen. Wenn null oder leer, wird standardmäßig PDF verwendet.
	 * @param reportvorlageParameterGruppen         Liste von ReportingReportvorlageParameterGruppen. Wenn null, wird eine leere Liste erstellt.
	 * @param eMailDaten                            Die Einstellungen und Informationen zum E-Mail-Versand. Wenn null, werden Standardwerte initialisiert,
	 *                                              die aber keinen E-Mail-Versand ermöglichen.
	 * @param sortierungDefinitionenGruppen         Liste von ReportingSortierungDefinitionGruppen, die die Sortierungsdefinitionen enthalten. Wenn null, wird
	 *                                              eine leere Liste erstellt.
	 * @param filterDefinitionenGruppen             Liste von ReportingFilterDefinitionGruppen, die die Filterdefinitionen enthalten. Wenn null, wird eine
	 *                                              leere Liste erstellt.
	 * @param uiIstSichtbarEinzelausgabeDaten       Gibt an, ob die Option "Einzelausgabe" in der UI sichtbar sein soll.
	 * @param uiIstSichtbarDuplexdruck              Gibt an, ob die Option "Duplexdruck" in der UI sichtbar sein soll.
	 *
	 * @return Ein konfiguriertes ReportingParameter-Objekt mit den angegebenen Eigenschaften.
	 */
	public static @NotNull ReportingParameter erzeugeReportingParameter(final List<Integer> ausgabeformatOptionen,
			final List<ReportingReportvorlageParameterGruppe> reportvorlageParameterGruppen,
			final ReportingEMailDaten eMailDaten,
			final List<ReportingSortierungDefinitionGruppe> sortierungDefinitionenGruppen,
			final List<ReportingFilterDefinitionGruppe> filterDefinitionenGruppen,
			final @NotNull boolean uiIstSichtbarEinzelausgabeDaten,
			final @NotNull boolean uiIstSichtbarDuplexdruck) {

		final ReportingParameter reportingParameter = new ReportingParameter();
		reportingParameter.ausgabeformatOptionen = new ArrayList<>(
				((ausgabeformatOptionen == null) || ausgabeformatOptionen.isEmpty()) ? List.of(ReportingAusgabeformat.PDF.getId()) : ausgabeformatOptionen);

		// Standardausgabeoptionen-Parameter-Gruppe erstellen
		String ausgabeoptionenBeschreibung = (uiIstSichtbarEinzelausgabeDaten)
				? "Die Option 'Daten in einzelne Dateien ausgeben' ermöglicht es, bei der Ausgabe pro Datensatz in eine einzelne Datei zu erzeugen. "
				: "";
		ausgabeoptionenBeschreibung += (uiIstSichtbarDuplexdruck)
				? ("Wird Duplexdruck aktiviert, so kann die erzeuget Datei später auf einem Drucker mit der Option 'beidseitiger Druck (Duplexdruck)' gedruckt "
						+ "werden.")
				: "";

		final ReportingReportvorlageParameterGruppe standardausgabeoptionenGruppe =
				erzeugeReportingvorlageParameterGruppe("Ausgabeoptionen", ausgabeoptionenBeschreibung, true, 3,
						Arrays.asList(
								erzeugeVorlageParameter("einzelausgabeDaten", "Daten in einzelne Dateien ausgeben", ReportingReportvorlageParameterTyp.BOOLEAN,
										"" + false, uiIstSichtbarEinzelausgabeDaten, ReportingUIKomponentenTyp.CHECKBOX, 1),
								erzeugeVorlageParameter("duplexdruck", "Duplexdruck", ReportingReportvorlageParameterTyp.BOOLEAN, "" + false,
										uiIstSichtbarDuplexdruck, ReportingUIKomponentenTyp.CHECKBOX, 1)));

		// Die Parameter-Gruppen der Vorlage zuweisen, sowohl spezielle als auch allgemeine Gruppen.
		reportingParameter.reportvorlageParameterGruppen = new ArrayList<>(
				(reportvorlageParameterGruppen == null) ? new ArrayList<>() : reportvorlageParameterGruppen);
		reportingParameter.reportvorlageParameterGruppen.add(standardausgabeoptionenGruppe);

		// E-Mail-Daten zuweisen
		reportingParameter.eMailDaten = (eMailDaten == null) ? new ReportingEMailDaten() : eMailDaten;

		// Sortier- und Filterdefinitionen zuweisen
		reportingParameter.sortierungDefinitionenGruppen = new ArrayList<>(
				(sortierungDefinitionenGruppen == null) ? new ArrayList<>() : sortierungDefinitionenGruppen);
		reportingParameter.filterDefinitionenGruppen = new ArrayList<>(
				(filterDefinitionenGruppen == null) ? new ArrayList<>() : filterDefinitionenGruppen);

		return reportingParameter;
	}

	/**
	 * Erstellt ein ReportingParameter-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param name                            Name der Parametergruppe
	 * @param beschreibung                    Beschreibung der Parametergruppe
	 * @param uiIstSichtbar                   Gibt an, ob die Parametergruppe in der UI sichtbar sein soll
	 * @param uiAnzahlSpalten                 Anzahl der Spalten für die Parametergruppe in der UI
	 * @param reportingReportvorlageParameter Liste der ReportingReportvorlageParameter, die in der Parametergruppe enthalten sind
	 *
	 * @return Ein ReportingReportvorlageParameterGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static @NotNull ReportingReportvorlageParameterGruppe erzeugeReportingvorlageParameterGruppe(
			final @NotNull String name, final @NotNull String beschreibung, final @NotNull boolean uiIstSichtbar, final int uiAnzahlSpalten,
			final @NotNull List<ReportingReportvorlageParameter> reportingReportvorlageParameter) {
		return erzeugeReportingvorlageParameterGruppe(name, beschreibung, uiIstSichtbar, uiAnzahlSpalten, ServerMode.STABLE, List.of(),
				reportingReportvorlageParameter);
	}

	/**
	 * Erstellt ein ReportingReportvorlageParameterGruppe-Objekt basierend auf den angegebenen Parametern, inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen.
	 *
	 * @param name                            Name der Parametergruppe
	 * @param beschreibung                    Beschreibung der Parametergruppe
	 * @param uiIstSichtbar                   Gibt an, ob die Parametergruppe in der UI sichtbar sein soll
	 * @param uiAnzahlSpalten                 Anzahl der Spalten für die Parametergruppe in der UI
	 * @param uiErforderlicherServerMode      Der mindestens erforderliche ServerMode, damit die Gruppe verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen      Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 * @param reportingReportvorlageParameter Liste der ReportingReportvorlageParameter, die in der Parametergruppe enthalten sind
	 *
	 * @return Ein ReportingReportvorlageParameterGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static @NotNull ReportingReportvorlageParameterGruppe erzeugeReportingvorlageParameterGruppe(
			final @NotNull String name, final @NotNull String beschreibung, final @NotNull boolean uiIstSichtbar, final int uiAnzahlSpalten,
			final @NotNull ServerMode uiErforderlicherServerMode, final @NotNull List<BenutzerKompetenz> uiErforderlicheKompetenzen,
			final @NotNull List<ReportingReportvorlageParameter> reportingReportvorlageParameter) {
		final ReportingReportvorlageParameterGruppe gruppe = new ReportingReportvorlageParameterGruppe();
		gruppe.name = name;
		gruppe.beschreibung = beschreibung;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.uiAnzahlSpalten = uiAnzahlSpalten;
		gruppe.reportvorlageParameter = reportingReportvorlageParameter;
		gruppe.uiErforderlicherServerMode = serverModeText(uiErforderlicherServerMode);
		gruppe.uiErforderlicheKompetenzen = kompetenzIds(uiErforderlicheKompetenzen);
		return gruppe;
	}

	/**
	 * Erstellt ein ReportingReportvorlageParameter-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param name              Name des Parameters
	 * @param bezeichnung       Bezeichnung des Parameters
	 * @param typ               Typ des Parameters
	 * @param wert              Wert des Parameters
	 * @param uiIstSichtbar     Gibt an, ob der Parameter in der UI sichtbar sein soll
	 * @param uiKomponentenTyp  Typ der UI-Komponente für den Parameter
	 * @param uiAnzahlSpalten   Anzahl der Spalten für die UI-Komponente
	 *
	 * @return Ein ReportingReportvorlageParameter-Objekt mit den angegebenen Eigenschaften
	 */
	public static @NotNull ReportingReportvorlageParameter erzeugeVorlageParameter(
			final @NotNull String name, final @NotNull String bezeichnung, final @NotNull ReportingReportvorlageParameterTyp typ,
			final @NotNull String wert, final @NotNull boolean uiIstSichtbar, final @NotNull ReportingUIKomponentenTyp uiKomponentenTyp,
			final int uiAnzahlSpalten) {
		return erzeugeVorlageParameter(name, bezeichnung, typ, wert, uiIstSichtbar, uiKomponentenTyp, uiAnzahlSpalten, ServerMode.STABLE, List.of());
	}

	/**
	 * Erstellt ein ReportingReportvorlageParameter-Objekt basierend auf den angegebenen Parametern, inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen.
	 *
	 * @param name                        Name des Parameters
	 * @param bezeichnung                 Bezeichnung des Parameters
	 * @param typ                         Typ des Parameters
	 * @param wert                        Wert des Parameters
	 * @param uiIstSichtbar               Gibt an, ob der Parameter in der UI sichtbar sein soll
	 * @param uiKomponentenTyp            Typ der UI-Komponente für den Parameter
	 * @param uiAnzahlSpalten             Anzahl der Spalten für die UI-Komponente
	 * @param uiErforderlicherServerMode  Der mindestens erforderliche ServerMode, damit der Parameter verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen  Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 *
	 * @return Ein ReportingReportvorlageParameter-Objekt mit den angegebenen Eigenschaften
	 */
	// SONARQUBE WARNUNG: Es sollen max. 7 Parameter an eine Methode übergeben werden. Hier sind es neun, sie sind aber alle gut nachvollziehbar.
	@SuppressWarnings("java:S107")
	public static @NotNull ReportingReportvorlageParameter erzeugeVorlageParameter(
			final @NotNull String name, final @NotNull String bezeichnung, final @NotNull ReportingReportvorlageParameterTyp typ,
			final @NotNull String wert, final @NotNull boolean uiIstSichtbar, final @NotNull ReportingUIKomponentenTyp uiKomponentenTyp,
			final int uiAnzahlSpalten, final @NotNull ServerMode uiErforderlicherServerMode,
			final @NotNull List<BenutzerKompetenz> uiErforderlicheKompetenzen) {
		final ReportingReportvorlageParameter parameter = new ReportingReportvorlageParameter();
		parameter.name = name;
		parameter.bezeichnung = bezeichnung;
		parameter.typ = typ.getId();
		parameter.wert = wert;
		parameter.uiIstSichtbar = uiIstSichtbar;
		parameter.uiKomponentenTyp = uiKomponentenTyp.getId();
		parameter.uiAnzahlSpalten = uiAnzahlSpalten;
		parameter.uiErforderlicherServerMode = serverModeText(uiErforderlicherServerMode);
		parameter.uiErforderlicheKompetenzen = kompetenzIds(uiErforderlicheKompetenzen);
		return parameter;
	}

	/**
	 * Erstellt ein ReportingEMailDaten-Objekt basierend auf den angegebenen Eigenschaften.
	 *
	 * @param eMailEmpfaengerTyp             Typ des Empfängers für die E-Mail
	 * @param istPrivateEmailAlternative     Gibt an, ob es sich um eine private E-Mail-Alternative handelt
	 * @param betreff                        Betreff der E-Mail
	 * @param text                           Textinhalt der E-Mail
	 *
	 * @return Ein ReportingEMailDaten-Objekt mit den angegebenen Eigenschaften
	 */
	public static @NotNull ReportingEMailDaten erzeugeEmailParameter(final @NotNull ReportingEMailEmpfaengerTyp eMailEmpfaengerTyp,
			final boolean istPrivateEmailAlternative, final @NotNull String betreff, final @NotNull String text) {

		final ReportingEMailDaten daten = new ReportingEMailDaten();
		daten.empfaengerTyp = eMailEmpfaengerTyp.getId();
		daten.istPrivateEmailAlternative = istPrivateEmailAlternative;
		daten.betreff = betreff;
		daten.text = text;
		return daten;
	}

	/**
	 * Erstellt ein ReportingSortierungDefinitionGruppe-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param bezeichnung                    Bezeichnung der Sortierung-Definition-Gruppe
	 * @param typ                            Typ der Sortierung-Definition-Gruppe
	 * @param uiIstSichtbar                  Gibt an, ob die Sortierung-Definition-Gruppe in der UI sichtbar sein soll
	 * @param sortierungDefinitionenOptionen Liste der ReportingSortierungDefinition-Objekte, die in der Sortierung-Definition-Gruppe enthalten sind
	 *
	 * @return Ein ReportingSortierungDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static @NotNull ReportingSortierungDefinitionGruppe erzeugeSortierungDefinitionGruppe(
			final @NotNull String bezeichnung, final @NotNull String typ, final boolean uiIstSichtbar,
			final @NotNull List<ReportingSortierungDefinition> sortierungDefinitionenOptionen) {
		return erzeugeSortierungDefinitionGruppe(bezeichnung, typ, uiIstSichtbar, ServerMode.STABLE, List.of(), sortierungDefinitionenOptionen);
	}

	/**
	 * Erstellt ein ReportingSortierungDefinitionGruppe-Objekt basierend auf den angegebenen Parametern, inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen.
	 *
	 * @param bezeichnung                    Bezeichnung der Sortierung-Definition-Gruppe
	 * @param typ                            Typ der Sortierung-Definition-Gruppe
	 * @param uiIstSichtbar                  Gibt an, ob die Sortierung-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiErforderlicherServerMode     Der mindestens erforderliche ServerMode, damit die Gruppe verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen     Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 * @param sortierungDefinitionenOptionen Liste der ReportingSortierungDefinition-Objekte, die in der Sortierung-Definition-Gruppe enthalten sind
	 *
	 * @return Ein ReportingSortierungDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static @NotNull ReportingSortierungDefinitionGruppe erzeugeSortierungDefinitionGruppe(
			final @NotNull String bezeichnung, final @NotNull String typ, final boolean uiIstSichtbar,
			final @NotNull ServerMode uiErforderlicherServerMode, final @NotNull List<BenutzerKompetenz> uiErforderlicheKompetenzen,
			final @NotNull List<ReportingSortierungDefinition> sortierungDefinitionenOptionen) {
		final ReportingSortierungDefinitionGruppe gruppe = new ReportingSortierungDefinitionGruppe();
		gruppe.bezeichnung = bezeichnung;
		gruppe.typ = typ;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.sortierungDefinitionenOptionen = new ArrayList<>(sortierungDefinitionenOptionen);
		gruppe.uiErforderlicherServerMode = serverModeText(uiErforderlicherServerMode);
		gruppe.uiErforderlicheKompetenzen = kompetenzIds(uiErforderlicheKompetenzen);
		return gruppe;
	}

	/**
	 * Erstellt ein ReportingFilterDefinitionGruppe-Objekt basierend auf den angegebenen Parametern.
	 *
	 * @param bezeichnung                     Bezeichnung der Filter-Definition-Gruppe
	 * @param typ                             Typ der Filter-Definition-Gruppe
	 * @param uiIstSichtbar                   Gibt an, ob die Filter-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiIstFilterMultiselect          Gibt an, ob die Filter-Definition-Gruppe als Multiselect in der UI angezeigt werden soll
	 * @param uiFilterMultiselectVerknuepfung Verknüpfung für Multiselect-Filter-Definitionen
	 * @param filterDefinitionenOptionen      Liste der ReportingFilterDefinition-Objekte, die in der Filter-Definition-Gruppe enthalten sind
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static @NotNull ReportingFilterDefinitionGruppe erzeugeFilterDefinitionGruppe(
			final @NotNull String bezeichnung, final @NotNull String typ, final boolean uiIstSichtbar, final boolean uiIstFilterMultiselect,
			final @NotNull ReportingFilterVerknuepfung uiFilterMultiselectVerknuepfung,
			final @NotNull List<ReportingFilterDefinition> filterDefinitionenOptionen) {
		return erzeugeFilterDefinitionGruppe(bezeichnung, typ, uiIstSichtbar, uiIstFilterMultiselect, uiFilterMultiselectVerknuepfung, ServerMode.STABLE,
				List.of(), filterDefinitionenOptionen);
	}

	/**
	 * Erstellt ein ReportingFilterDefinitionGruppe-Objekt basierend auf den angegebenen Parametern, inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen.
	 *
	 * @param bezeichnung                     Bezeichnung der Filter-Definition-Gruppe
	 * @param typ                             Typ der Filter-Definition-Gruppe
	 * @param uiIstSichtbar                   Gibt an, ob die Filter-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiIstFilterMultiselect          Gibt an, ob die Filter-Definition-Gruppe als Multiselect in der UI angezeigt werden soll
	 * @param uiFilterMultiselectVerknuepfung Verknüpfung für Multiselect-Filter-Definitionen
	 * @param uiErforderlicherServerMode      Der mindestens erforderliche ServerMode, damit die Gruppe verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen      Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 * @param filterDefinitionenOptionen      Liste der ReportingFilterDefinition-Objekte, die in der Filter-Definition-Gruppe enthalten sind
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	// SONARQUBE WARNUNG: Es sollen max. 7 Parameter an eine Methode übergeben werden. Hier sind es acht, sie sind aber alle gut nachvollziehbar.
	@SuppressWarnings("java:S107")
	public static @NotNull ReportingFilterDefinitionGruppe erzeugeFilterDefinitionGruppe(
			final @NotNull String bezeichnung, final @NotNull String typ, final boolean uiIstSichtbar, final boolean uiIstFilterMultiselect,
			final @NotNull ReportingFilterVerknuepfung uiFilterMultiselectVerknuepfung,
			final @NotNull ServerMode uiErforderlicherServerMode, final @NotNull List<BenutzerKompetenz> uiErforderlicheKompetenzen,
			final @NotNull List<ReportingFilterDefinition> filterDefinitionenOptionen) {
		final ReportingFilterDefinitionGruppe gruppe = new ReportingFilterDefinitionGruppe();
		gruppe.bezeichnung = bezeichnung;
		gruppe.typ = typ;
		gruppe.uiIstSichtbar = uiIstSichtbar;
		gruppe.uiIstFilterMultiselect = uiIstFilterMultiselect;
		gruppe.uiFilterMultiselectVerknuepfung = uiFilterMultiselectVerknuepfung.getId();
		gruppe.filterDefinitionenOptionen = new ArrayList<>(filterDefinitionenOptionen);
		gruppe.uiErforderlicherServerMode = serverModeText(uiErforderlicherServerMode);
		gruppe.uiErforderlicheKompetenzen = kompetenzIds(uiErforderlicheKompetenzen);
		return gruppe;
	}

	/**
	 * Erstellt ein ReportingFilterDefinitionGruppe-Objekt mit einer Vorauswahl an Filterdefinitionen.
	 *
	 * <p>Wichtig: Die Einträge in {@code filterDefinitionenVorauswahl} müssen dieselben Objektinstanzen sein wie die entsprechenden Einträge in
	 * {@code filterDefinitionenOptionen}, da die UI die Vorauswahl über die Objektidentität ermittelt.</p>
	 *
	 * @param bezeichnung                    Bezeichnung der Filter-Definition-Gruppe
	 * @param typ                            Typ der Filter-Definition-Gruppe
	 * @param uiIstSichtbar                  Gibt an, ob die Filter-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiIstFilterMultiselect          Gibt an, ob die Filter-Definition-Gruppe als Multiselect in der UI angezeigt werden soll
	 * @param uiFilterMultiselectVerknuepfung Verknüpfung für Multiselect-Filter-Definitionen
	 * @param filterDefinitionenOptionen      Liste der ReportingFilterDefinition-Objekte, die in der Filter-Definition-Gruppe zur Verfügung stehen
	 * @param filterDefinitionenVorauswahl    Liste der ReportingFilterDefinition-Objekte, die in der Gruppe vorausgewählt sein sollen
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	public static @NotNull ReportingFilterDefinitionGruppe erzeugeFilterDefinitionGruppe(
			final @NotNull String bezeichnung, final @NotNull String typ, final boolean uiIstSichtbar, final boolean uiIstFilterMultiselect,
			final @NotNull ReportingFilterVerknuepfung uiFilterMultiselectVerknuepfung,
			final @NotNull List<ReportingFilterDefinition> filterDefinitionenOptionen,
			final @NotNull List<ReportingFilterDefinition> filterDefinitionenVorauswahl) {
		return erzeugeFilterDefinitionGruppe(bezeichnung, typ, uiIstSichtbar, uiIstFilterMultiselect, uiFilterMultiselectVerknuepfung, ServerMode.STABLE,
				List.of(), filterDefinitionenOptionen, filterDefinitionenVorauswahl);
	}

	/**
	 * Erstellt ein ReportingFilterDefinitionGruppe-Objekt mit einer Vorauswahl an Filterdefinitionen, inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen.
	 *
	 * <p>Wichtig: Die Einträge in {@code filterDefinitionenVorauswahl} müssen dieselben Objektinstanzen sein wie die entsprechenden Einträge in
	 * {@code filterDefinitionenOptionen}, da die UI die Vorauswahl über die Objektidentität ermittelt.</p>
	 *
	 * @param bezeichnung                     Bezeichnung der Filter-Definition-Gruppe
	 * @param typ                             Typ der Filter-Definition-Gruppe
	 * @param uiIstSichtbar                   Gibt an, ob die Filter-Definition-Gruppe in der UI sichtbar sein soll
	 * @param uiIstFilterMultiselect          Gibt an, ob die Filter-Definition-Gruppe als Multiselect in der UI angezeigt werden soll
	 * @param uiFilterMultiselectVerknuepfung Verknüpfung für Multiselect-Filter-Definitionen
	 * @param uiErforderlicherServerMode      Der mindestens erforderliche ServerMode, damit die Gruppe verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen      Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 * @param filterDefinitionenOptionen      Liste der ReportingFilterDefinition-Objekte, die in der Filter-Definition-Gruppe zur Verfügung stehen
	 * @param filterDefinitionenVorauswahl    Liste der ReportingFilterDefinition-Objekte, die in der Gruppe vorausgewählt sein sollen
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt mit den angegebenen Eigenschaften
	 */
	// SONARQUBE WARNUNG: Es sollen max. 7 Parameter an eine Methode übergeben werden. Hier sind es neun, sie sind aber alle gut nachvollziehbar.
	@SuppressWarnings("java:S107")
	public static @NotNull ReportingFilterDefinitionGruppe erzeugeFilterDefinitionGruppe(
			final @NotNull String bezeichnung, final @NotNull String typ, final boolean uiIstSichtbar, final boolean uiIstFilterMultiselect,
			final @NotNull ReportingFilterVerknuepfung uiFilterMultiselectVerknuepfung,
			final @NotNull ServerMode uiErforderlicherServerMode, final @NotNull List<BenutzerKompetenz> uiErforderlicheKompetenzen,
			final @NotNull List<ReportingFilterDefinition> filterDefinitionenOptionen,
			final @NotNull List<ReportingFilterDefinition> filterDefinitionenVorauswahl) {
		final ReportingFilterDefinitionGruppe gruppe = erzeugeFilterDefinitionGruppe(bezeichnung, typ, uiIstSichtbar, uiIstFilterMultiselect,
				uiFilterMultiselectVerknuepfung, uiErforderlicherServerMode, uiErforderlicheKompetenzen, filterDefinitionenOptionen);
		gruppe.filterDefinitionen = new ArrayList<>(filterDefinitionenVorauswahl);
		return gruppe;
	}

	/**
	 * Erstellt die Filter-Definition-Gruppe "Statusfilter" für den Reporting-Typ "ReportingSchueler". Als Optionen werden alle Werte des
	 * {@link SchuelerStatus} angeboten (Multiselect, OR-Verknüpfung); vorausgewählt sind die Status AKTIV und EXTERN.
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt für die Filterung nach dem Schülerstatus
	 */
	public static @NotNull ReportingFilterDefinitionGruppe erzeugeSchuelerStatusfilterGruppe() {
		return erzeugeSchuelerStatusfilterGruppe(ServerMode.STABLE, List.of());
	}

	/**
	 * Erstellt die Filter-Definition-Gruppe "Statusfilter" für den Reporting-Typ "ReportingSchueler", inklusive der Angaben zu erforderlichem ServerMode und
	 * erforderlichen Benutzerkompetenzen. Als Optionen werden alle Werte des {@link SchuelerStatus} angeboten (Multiselect, OR-Verknüpfung); vorausgewählt sind
	 * die Status AKTIV und EXTERN.
	 *
	 * @param uiErforderlicherServerMode Der mindestens erforderliche ServerMode, damit die Gruppe verfügbar ist (STABLE = in allen Modi verfügbar)
	 * @param uiErforderlicheKompetenzen Die erforderlichen Benutzerkompetenzen (OR-verknüpft; leer = keine Kompetenz erforderlich)
	 *
	 * @return Ein ReportingFilterDefinitionGruppe-Objekt für die Filterung nach dem Schülerstatus
	 */
	public static @NotNull ReportingFilterDefinitionGruppe erzeugeSchuelerStatusfilterGruppe(
			final @NotNull ServerMode uiErforderlicherServerMode, final @NotNull List<BenutzerKompetenz> uiErforderlicheKompetenzen) {
		final List<ReportingFilterDefinition> optionen = new ArrayList<>();
		final List<ReportingFilterDefinition> vorauswahl = new ArrayList<>();
		for (final SchuelerStatus status : SchuelerStatus.values()) {
			final ReportingFilterDefinition definition = ReportingFilterDefinitionFactory.definition(
					normalisiereSchuelerStatusBezeichnung(status.name()), "ReportingSchueler",
					ReportingFilterDefinitionFactory.and(ReportingFilterDefinitionFactory.eq("status", status.name())));
			optionen.add(definition);
			if ((status == SchuelerStatus.AKTIV) || (status == SchuelerStatus.EXTERN)) {
				vorauswahl.add(definition);
			}
		}
		return erzeugeFilterDefinitionGruppe("Schülerstatus", "ReportingSchueler", true, true, ReportingFilterVerknuepfung.OR,
				uiErforderlicherServerMode, uiErforderlicheKompetenzen, optionen, vorauswahl);
	}

	/**
	 * Normalisiert den technischen Namen eines {@link SchuelerStatus} für die Anzeige, indem der erste Buchstabe groß und die übrigen Buchstaben klein
	 * geschrieben werden (z. B. "AKTIV" wird zu "Aktiv").
	 *
	 * @param name Der technische Name (Enum-Name) des Status
	 *
	 * @return Die normalisierte Bezeichnung für die Anzeige
	 */
	private static @NotNull String normalisiereSchuelerStatusBezeichnung(final @NotNull String name) {
		if (name.isEmpty()) {
			return name;
		}
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	/**
	 * Wählt aus den Katalog-Optionen einer Sortier- oder Filtergruppe genau die Einträge aus, deren Bezeichnung in der Liste der gespeicherten Bezeichnungen
	 * enthalten ist. Für jede gespeicherte Bezeichnung wird die passende Options-Instanz herausgesucht (nicht kopiert), damit die UI die Auswahl über die
	 * Objektidentität erkennt. Unbekannte Bezeichnungen (z. B. weil sich der Katalog geändert hat) werden übersprungen; die Reihenfolge der gespeicherten
	 * Bezeichnungen bleibt erhalten. Die Methode wird sowohl serverseitig (Anwenden der gespeicherten Einstellungen im ReportingParameterBuilder) als auch
	 * clientseitig (Vorbefüllen der Auswahl-Komponenten aus den gespeicherten Einstellungen) verwendet.
	 *
	 * @param <T>                       der Options-Typ, z. B. {@link ReportingSortierungDefinition} oder {@link ReportingFilterDefinition}
	 * @param optionen                  die Katalog-Optionen der Gruppe
	 * @param bezeichnungExtractor      Funktion, die die Bezeichnung eines Options-Objekts liefert
	 * @param gespeicherteBezeichnungen die gespeicherten Bezeichnungen in Auswahlreihenfolge
	 *
	 * @return die ausgewählten Options-Instanzen (dieselben Instanzen wie in {@code optionen}), ggf. leer
	 */
	public static <T> @NotNull List<T> waehleGespeicherteAuswahl(final @NotNull List<T> optionen,
			final @NotNull Function<T, String> bezeichnungExtractor, final @NotNull List<String> gespeicherteBezeichnungen) {
		final List<T> ergebnis = new ArrayList<>();
		for (final String bezeichnung : gespeicherteBezeichnungen) {
			if (bezeichnung == null) {
				continue;
			}
			for (final T option : optionen) {
				if ((option != null) && bezeichnung.equals(bezeichnungExtractor.apply(option))) {
					ergebnis.add(option);
					break;
				}
			}
		}
		return ergebnis;
	}

	/**
	 * Normalisiert den Eingabestring für Schlüsselangaben, indem er Leerzeichen entfernt und in Kleinbuchstaben umgewandelt wird.
	 *
	 * @param input Der Eingabestring, der normalisiert werden soll
	 *
	 * @return Der normalisierte Eingabestring
	 */
	public static @NotNull String normalizeKeyInput(final String input) {
		return (input == null) ? "" : input.trim().toLowerCase();
	}

	/**
	 * Erstellt eine Kopie eines ReportingParameter-Objekts.
	 *
	 * @param source Das zu kopierende ReportingParameter-Objekt
	 *
	 * @return Eine Kopie des ReportingParameter-Objekts
	 */
	public static @NotNull ReportingParameter cloneReportingParameter(final @NotNull ReportingParameter source) {
		final ReportingParameter copy = new ReportingParameter();

		copy.idSchuljahresabschnitt = source.idSchuljahresabschnitt;
		copy.ausgabeformat = source.ausgabeformat;
		copy.reportvorlage = source.reportvorlage;
		copy.idHauptdatenObjekt = source.idHauptdatenObjekt;
		copy.ausgabeformatOptionen.addAll(source.ausgabeformatOptionen);
		copy.idsHauptdaten.addAll(source.idsHauptdaten);
		copy.idsDetaildaten.addAll(source.idsDetaildaten);

		if (source.eMailDaten != null) {
			copy.eMailDaten = new ReportingEMailDaten();
			copy.eMailDaten.empfaengerTyp = source.eMailDaten.empfaengerTyp;
			copy.eMailDaten.istPrivateEmailAlternative = source.eMailDaten.istPrivateEmailAlternative;
			copy.eMailDaten.betreff = source.eMailDaten.betreff;
			copy.eMailDaten.text = source.eMailDaten.text;
		} else {
			copy.eMailDaten = null;
		}

		copy.reportvorlageParameterGruppen.addAll(cloneVorlageParameterGruppen(source.reportvorlageParameterGruppen));
		copy.sortierungDefinitionenGruppen.addAll(cloneSortierungDefinitionGruppen(source.sortierungDefinitionenGruppen));
		copy.filterDefinitionenGruppen.addAll(cloneFilterDefinitionGruppen(source.filterDefinitionenGruppen));

		return copy;
	}

	private static @NotNull List<ReportingReportvorlageParameterGruppe> cloneVorlageParameterGruppen(final List<ReportingReportvorlageParameterGruppe> source) {
		final List<ReportingReportvorlageParameterGruppe> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingReportvorlageParameterGruppe vpg : source) {
			if (vpg == null) {
				continue;
			}
			final ReportingReportvorlageParameterGruppe vpgCopy = new ReportingReportvorlageParameterGruppe();
			vpgCopy.name = vpg.name;
			vpgCopy.beschreibung = vpg.beschreibung;
			vpgCopy.uiIstSichtbar = vpg.uiIstSichtbar;
			vpgCopy.uiAnzahlSpalten = vpg.uiAnzahlSpalten;
			vpgCopy.uiErforderlicherServerMode = vpg.uiErforderlicherServerMode;
			if (vpg.uiErforderlicheKompetenzen != null) {
				vpgCopy.uiErforderlicheKompetenzen = new ArrayList<>(vpg.uiErforderlicheKompetenzen);
			}
			if (vpg.reportvorlageParameter != null) {
				vpgCopy.reportvorlageParameter.addAll(cloneVorlageParameter(vpg.reportvorlageParameter));
			}
			result.add(vpgCopy);
		}
		return result;
	}

	private static @NotNull List<ReportingReportvorlageParameter> cloneVorlageParameter(final List<ReportingReportvorlageParameter> source) {
		final List<ReportingReportvorlageParameter> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingReportvorlageParameter vp : source) {
			if (vp == null) {
				continue;
			}
			final ReportingReportvorlageParameter vpCopy = new ReportingReportvorlageParameter();
			vpCopy.name = vp.name;
			vpCopy.bezeichnung = vp.bezeichnung;
			vpCopy.typ = vp.typ;
			vpCopy.wert = vp.wert;
			vpCopy.uiIstSichtbar = vp.uiIstSichtbar;
			vpCopy.uiKomponentenTyp = vp.uiKomponentenTyp;
			vpCopy.uiAnzahlSpalten = vp.uiAnzahlSpalten;
			vpCopy.uiErforderlicherServerMode = vp.uiErforderlicherServerMode;
			if (vp.uiErforderlicheKompetenzen != null) {
				vpCopy.uiErforderlicheKompetenzen = new ArrayList<>(vp.uiErforderlicheKompetenzen);
			}
			result.add(vpCopy);
		}
		return result;
	}

	private static @NotNull List<ReportingSortierungDefinitionGruppe> cloneSortierungDefinitionGruppen(final List<ReportingSortierungDefinitionGruppe> source) {
		final List<ReportingSortierungDefinitionGruppe> result = new ArrayList<>();
		if (source == null) {
			return result;
		}
		for (final ReportingSortierungDefinitionGruppe sdg : source) {
			if (sdg.sortierungDefinitionenOptionen == null) {
				continue;
			}
			final ReportingSortierungDefinitionGruppe sdgCopy = new ReportingSortierungDefinitionGruppe();
			sdgCopy.bezeichnung = sdg.bezeichnung;
			sdgCopy.typ = sdg.typ;
			sdgCopy.uiIstSichtbar = sdg.uiIstSichtbar;
			sdgCopy.uiErforderlicherServerMode = sdg.uiErforderlicherServerMode;
			if (sdg.uiErforderlicheKompetenzen != null) {
				sdgCopy.uiErforderlicheKompetenzen = new ArrayList<>(sdg.uiErforderlicheKompetenzen);
			}
			if (sdg.sortierungDefinitionenOptionen != null) {
				sdgCopy.sortierungDefinitionenOptionen.addAll(cloneSortierungDefinitionen(sdg.sortierungDefinitionenOptionen));
			}
			result.add(sdgCopy);
		}
		return result;
	}

	private static @NotNull List<ReportingSortierungDefinition> cloneSortierungDefinitionen(final List<ReportingSortierungDefinition> source) {
		final List<ReportingSortierungDefinition> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingSortierungDefinition sd : source) {
			if (sd == null) {
				continue;
			}
			final ReportingSortierungDefinition sdCopy = new ReportingSortierungDefinition();
			sdCopy.bezeichnung = sd.bezeichnung;
			sdCopy.typ = sd.typ;
			sdCopy.verwendeStandardsortierung = sd.verwendeStandardsortierung;
			sdCopy.attribute.addAll(sd.attribute);
			result.add(sdCopy);
		}
		return result;
	}

	private static @NotNull List<ReportingFilterDefinitionGruppe> cloneFilterDefinitionGruppen(final List<ReportingFilterDefinitionGruppe> source) {
		final List<ReportingFilterDefinitionGruppe> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingFilterDefinitionGruppe fdg : source) {
			if (fdg == null) {
				continue;
			}
			final ReportingFilterDefinitionGruppe fdgCopy = new ReportingFilterDefinitionGruppe();
			fdgCopy.bezeichnung = fdg.bezeichnung;
			fdgCopy.typ = fdg.typ;
			fdgCopy.uiIstSichtbar = fdg.uiIstSichtbar;
			fdgCopy.uiIstFilterMultiselect = fdg.uiIstFilterMultiselect;
			fdgCopy.uiFilterMultiselectVerknuepfung = fdg.uiFilterMultiselectVerknuepfung;
			fdgCopy.uiErforderlicherServerMode = fdg.uiErforderlicherServerMode;
			if (fdg.uiErforderlicheKompetenzen != null) {
				fdgCopy.uiErforderlicheKompetenzen = new ArrayList<>(fdg.uiErforderlicheKompetenzen);
			}
			if (fdg.filterDefinitionenOptionen != null) {
				final List<ReportingFilterDefinition> optionenKopie = cloneFilterDefinitionen(fdg.filterDefinitionenOptionen);
				fdgCopy.filterDefinitionenOptionen.addAll(optionenKopie);
				// Die Vorauswahl muss auf die geklonten Options-Instanzen abgebildet werden, damit die UI sie über die Objektidentität als ausgewählt erkennt.
				uebertrageFilterVorauswahl(fdg, optionenKopie, fdgCopy);
			}
			result.add(fdgCopy);
		}
		return result;
	}

	private static void uebertrageFilterVorauswahl(final @NotNull ReportingFilterDefinitionGruppe fdg,
			final @NotNull List<ReportingFilterDefinition> optionenKopie, final @NotNull ReportingFilterDefinitionGruppe fdgCopy) {
		if (fdg.filterDefinitionen != null) {
			for (final ReportingFilterDefinition vorauswahl : fdg.filterDefinitionen) {
				final int index = fdg.filterDefinitionenOptionen.indexOf(vorauswahl);
				if ((index >= 0) && (index < optionenKopie.size())) {
					fdgCopy.filterDefinitionen.add(optionenKopie.get(index));
				}
			}
		}
	}

	private static @NotNull List<ReportingFilterDefinition> cloneFilterDefinitionen(final List<ReportingFilterDefinition> source) {
		final List<ReportingFilterDefinition> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingFilterDefinition fd : source) {
			if (fd == null) {
				continue;
			}
			final ReportingFilterDefinition fdCopy = new ReportingFilterDefinition();
			fdCopy.bezeichnung = fd.bezeichnung;
			fdCopy.typ = fd.typ;
			fdCopy.kriterien.addAll(cloneFilterKriterien(fd.kriterien));
			result.add(fdCopy);
		}
		return result;
	}

	private static @NotNull List<ReportingFilterKriterium> cloneFilterKriterien(final List<ReportingFilterKriterium> source) {
		final List<ReportingFilterKriterium> result = new ArrayList<>();
		if ((source == null) || source.isEmpty()) {
			return result;
		}
		for (final ReportingFilterKriterium k : source) {
			if (k == null) {
				continue;
			}
			final ReportingFilterKriterium kCopy = new ReportingFilterKriterium();
			kCopy.verknuepfung = k.verknuepfung;
			kCopy.nicht = k.nicht;
			if (k.eintraege != null) {
				for (final ReportingFilterEintrag e : k.eintraege) {
					final ReportingFilterEintrag eCopy = new ReportingFilterEintrag();
					eCopy.attribut = e.attribut;
					eCopy.operation = e.operation;
					eCopy.werte.addAll(e.werte);
					kCopy.eintraege.add(eCopy);
				}
			}
			if (k.unterkriterien != null) {
				kCopy.unterkriterien.addAll(cloneFilterKriterien(k.unterkriterien));
			}
			result.add(kCopy);
		}
		return result;
	}
}
