package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.NationalitaetenKatalogEintrag;
import de.svws_nrw.asd.data.schule.VerkehrsspracheKatalogEintrag;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Prüfung auf den Migrationshintergrund der Schülerstammdaten aus.
 */
public final class ValidatorSsmkSchuelerStammdatenMigrationshintergrundKombination extends Validator {

 	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtsland             die ID des Geburtslandes
	 * @param idGeburtslandMutter       die ID des Geburtslandes der Mutter
	 * @param idGeburtslandVater        die ID des Geburtslandes des Vaters
	 * @param idVerkehrsspracheFamilie  die Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Gibt an, ob ein Migrationshintergrund vorhanden ist
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsmkSchuelerStammdatenMigrationshintergrundKombination(
			final @NotNull Supplier<@AllowNull Long> idGeburtsland,
			final @NotNull Supplier<@AllowNull Long> idGeburtslandMutter,
			final @NotNull Supplier<@AllowNull Long> idGeburtslandVater,
			final @NotNull Supplier<@AllowNull Long> idVerkehrsspracheFamilie,
			final @NotNull Supplier<@NotNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

//		String geburtsland = Nationalitaeten.data().getEintragByID(idGeburtsland.get()) != null ? Nationalitaeten.data().getEintragByID(idGeburtsland.get()).kuerzel : "DEU";
		@NotNull String geburtsland;
		@AllowNull NationalitaetenKatalogEintrag nationalitaetEintrag = Nationalitaeten.data().getEintragByID(idGeburtslandVater.get());
		if (nationalitaetEintrag != null) {
			geburtsland = nationalitaetEintrag.kuerzel;
		} else {
			geburtsland = "DEU";
		}

//		String geburtslandMutter = Nationalitaeten.data().getEintragByID(idGeburtslandMutter.get()) != null ? Nationalitaeten.data().getEintragByID(idGeburtslandMutter.get()).kuerzel : "DEU";
		@NotNull String geburtslandMutter;
		nationalitaetEintrag = Nationalitaeten.data().getEintragByID(idGeburtslandVater.get());
		if (nationalitaetEintrag != null) {
			geburtslandMutter = nationalitaetEintrag.kuerzel;
		} else {
			geburtslandMutter = "DEU";
		}

//		String geburtslandVater = Nationalitaeten.data().getEintragByID(idGeburtslandVater.get()) != null ? Nationalitaeten.data().getEintragByID(idGeburtslandVater.get()).kuerzel : "DEU";
		@NotNull String geburtslandVater;
		nationalitaetEintrag = Nationalitaeten.data().getEintragByID(idGeburtslandVater.get());
		if (nationalitaetEintrag != null) {
			geburtslandVater = nationalitaetEintrag.kuerzel;
		} else {
			geburtslandVater = "DEU";
		}

//		String verkehrsspracheFamilie = Verkehrssprache.data().getEintragByID(idVerkehrsspracheFamilie.get()) != null ? Verkehrssprache.data().getEintragByID(idVerkehrsspracheFamilie.get()).kuerzel : "de";
		@NotNull String verkehrsspracheFamilie;
		@AllowNull VerkehrsspracheKatalogEintrag verkehrsspracheEintrag = Verkehrssprache.data().getEintragByID(idVerkehrsspracheFamilie.get());
		if (verkehrsspracheEintrag != null) {
			verkehrsspracheFamilie = verkehrsspracheEintrag.kuerzel;
		} else {
			verkehrsspracheFamilie = "de";
		}

		_validatoren.add(new ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination(() -> geburtsland, () -> geburtslandMutter, () -> geburtslandVater, () -> verkehrsspracheFamilie, hatMigrationshintergrund, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}
}
