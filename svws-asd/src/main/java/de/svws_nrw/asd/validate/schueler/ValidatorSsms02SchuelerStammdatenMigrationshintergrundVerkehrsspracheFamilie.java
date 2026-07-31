package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob der eingetragene Wert für das Feld 'Verkehrssprache'
 * für das ausgewählte Schuljahr zeitlich gültig ist.
 */
public final class ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {

	/** Die ID der Verkehrssprache der Familie des Schülers */
	private final @NotNull Supplier<@AllowNull Long> _idVerkehrsspracheFamilie;

	private static final @NotNull String FEHLERTEXT =
			"Verkehrssprache: Der eingetragene Wert für das Feld 'Verkehrssprache' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idVerkehrsspracheFamilie  die ID der Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(
			final @NotNull Supplier<@AllowNull Long> idVerkehrsspracheFamilie,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idVerkehrsspracheFamilie = idVerkehrsspracheFamilie;

		_validatoren.add(
				new ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(idVerkehrsspracheFamilie, hatMigrationshintergrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idVerkehrsspracheFamilie = _idVerkehrsspracheFamilie.get();

		if (idVerkehrsspracheFamilie == null) {
			return true;
		}

		final int schuljahr = kontext().getSchuljahr();

		// Prüft über den CoreTypeDataManager, ob die ID im übergebenen Schuljahr eine gültige Historie hat
		if (!Verkehrssprache.data().isGueltig(idVerkehrsspracheFamilie, schuljahr)) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
