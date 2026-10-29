package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Migrationshintergrund eines Schülers aus.
 */
public final class ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination extends Validator {

//	/** Geburtsland */
	private final @NotNull Supplier<@NotNull String> _geburtsland;
//	/** geburtslandMutter */
	private final @NotNull Supplier<@NotNull String> _geburtslandMutter;
//	/** geburtslandVater */
	private final @NotNull Supplier<@NotNull String> _geburtslandVater;
//	/** verkehrssprache */
	private final @NotNull Supplier<@NotNull String> _verkehrsspracheFamilie;
	/** hat Migrationshintergrund */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geburtsland               das Geburtsland
	 * @param geburtslandMutter         das Geburtsland der Mutter
	 * @param geburtslandVater          das Geburtsland des Vaters
	 * @param verkehrsspracheFamilie    die Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsmk10SchuelerStammdatenMigrationshintergrundKombination(
			final @NotNull Supplier<@NotNull String> geburtsland,
			final @NotNull Supplier<@NotNull String> geburtslandMutter,
			final @NotNull Supplier<@NotNull String> geburtslandVater,
			final @NotNull Supplier<@NotNull String> verkehrsspracheFamilie,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_geburtsland = geburtsland;
		_geburtslandMutter = geburtslandMutter;
		_geburtslandVater = geburtslandVater;
		_verkehrsspracheFamilie = verkehrsspracheFamilie;
		_hatMigrationshintergrund = hatMigrationshintergrund;
	}


	@Override
	protected boolean pruefe() {
		final @AllowNull Boolean hatMigrationshintergrundZwisch = _hatMigrationshintergrund.get();
		final @NotNull Boolean hatMigrationshintergrund = hatMigrationshintergrundZwisch == null ? false : hatMigrationshintergrundZwisch;

		if (hatMigrationshintergrund) {
			if ("DEU".equalsIgnoreCase(_geburtsland.get()) && "DEU".equalsIgnoreCase(_geburtslandMutter.get()) && "DEU".equalsIgnoreCase(_geburtslandVater.get()) && "de".equalsIgnoreCase(_verkehrsspracheFamilie.get())) {
				addFehler(0, "Migrationshintergrund des Schülers: Wenn ein Migrationshintergrund vorhanden ist, dürfen die Felder 'Geburtsland', 'Geburtsland Mutter', 'Geburtsland Vater' und 'Verkehrssprache' nicht allesamt mit Deutschland bzw. Deutsch belegt sein.");
				return false;
			}
		}
		return true;
	}
}
