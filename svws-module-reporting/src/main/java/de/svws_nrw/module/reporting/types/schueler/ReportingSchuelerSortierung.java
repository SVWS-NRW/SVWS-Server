package de.svws_nrw.module.reporting.types.schueler;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.FunktionBuilder;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingSchueler}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingSchueler#SORTIERUNG} verwendet.
 */
public final class ReportingSchuelerSortierung {

	/** Die Sortierkonfiguration für {@link ReportingSchueler}. */
	public static final ReportingSortierung<ReportingSchueler> SORTIERUNG =
			ReportingSortierung.<ReportingSchueler>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingSchueler::nachname),
							ReportingTypesUtils.methodeToString(ReportingSchueler::vorname),
							ReportingTypesUtils.methodeToString(ReportingSchueler::vornamen),
							ReportingTypesUtils.methodeToString(ReportingSchueler::geburtsdatum),
							ReportingTypesUtils.methodeToString(ReportingSchueler::id)))
					.build();

	private ReportingSchuelerSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingSchueler. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingSchueler> buildRegistry() {
		final SortierungRegistry<ReportingSchueler> reg = new SortierungRegistry<>();

		// Registrierung grundlegender Personenattribute (aus ReportingPerson)
		reg.registiereString(ReportingSchueler::nachname);
		reg.registiereString(ReportingSchueler::vorname);
		reg.registiereString(ReportingSchueler::vornamen);
		reg.registiereString(ReportingSchueler::anrede);
		reg.registiereString(ReportingSchueler::titel);
		reg.registiereString(ReportingSchueler::geburtsdatum);
		reg.registiereString(ReportingSchueler::geburtsname);
		reg.registiereString(ReportingSchueler::geburtsort);
		reg.registiereComparable(ReportingSchueler::geschlecht);
		reg.registiereString(ReportingSchueler::emailPrivat);
		reg.registiereString(ReportingSchueler::emailSchule);
		reg.registiereString(ReportingSchueler::telefonPrivat);
		reg.registiereString(ReportingSchueler::telefonPrivatMobil);
		reg.registiereString(ReportingSchueler::strassenname);
		reg.registiereString(ReportingSchueler::hausnummer);
		reg.registiereString(ReportingSchueler::hausnummerZusatz);
		reg.registiereString(ReportingSchueler::wohnortname);
		reg.registiereString(ReportingSchueler::wohnortsteilname);

		// Registrierung schul-/schülerspezifischer Attribute (aus ReportingSchueler)
		reg.registiereComparable(ReportingSchueler::id);
		reg.registiereComparable(ReportingSchueler::status);
		reg.registiereComparable(ReportingSchueler::haltestelleID);
		reg.registiereComparable(ReportingSchueler::fahrschuelerArtID);
		reg.registiereComparable(ReportingSchueler::zuzugsjahr);
		reg.registiereString(ReportingSchueler::religionanmeldung);
		reg.registiereString(ReportingSchueler::religionabmeldung);
		reg.registiereString(ReportingSchueler::verkehrspracheFamilie);
		reg.registiereComparable(ReportingSchueler::erhaeltSchuelerBAFOEG);
		reg.registiereComparable(ReportingSchueler::erhaeltMeisterBAFOEG);
		reg.registiereComparable(ReportingSchueler::hatMigrationshintergrund);
		reg.registiereComparable(ReportingSchueler::hatMasernimpfnachweis);
		reg.registiereComparable(ReportingSchueler::istDuplikat);
		reg.registiereComparable(ReportingSchueler::keineAuskunftAnDritte);
		reg.registiereComparable(ReportingSchueler::istVolljaehrig);
		reg.registiereComparable(ReportingSchueler::istSchulpflichtErfuellt);
		reg.registiereComparable(ReportingSchueler::istBerufsschulpflichtErfuellt);

		// Importiere alle Klasse-Attribute unter dem Prefix "auswahlLernabschnitt.klasse"
		reg.importiereRegistryEintraege(
				ReportingTypesUtils.methodeToString(ReportingSchueler::auswahlLernabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuelerLernabschnitt::klasse) + ".",
				ReportingKlasse.SORTIERUNG.registry(),
				FunktionBuilder.start(ReportingSchueler::auswahlLernabschnitt).then(ReportingSchuelerLernabschnitt::klasse).toFunction());

		// Verschachtelte Attribute über den ausgewählten Lernabschnitt
		reg.registiereString(
				ReportingTypesUtils.methodeToString(ReportingSchueler::auswahlLernabschnitt) + "."
						+ ReportingTypesUtils.methodeToString(ReportingSchuelerLernabschnitt::jahrgang) + "."
						+ ReportingTypesUtils.methodeToString(ReportingJahrgang::kuerzel),
				FunktionBuilder.start(ReportingSchueler::auswahlLernabschnitt)
						.then(ReportingSchuelerLernabschnitt::jahrgang)
						.then(ReportingJahrgang::kuerzel)
						.toFunction());

		return reg;
	}
}
