import { ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland } from '../../../asd/validate/schueler/ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland extends Validator {

	/**
	 * Geburtsland
	 */
	private readonly _idGeburtsland: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtsland             die ID des Geburtslands
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idGeburtsland: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtsland = idGeburtsland;
		this._validatoren.add(new ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland(idGeburtsland, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland>('de.svws_nrw.asd.validate.schueler.ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland(obj: unknown): ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland {
	return obj as ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland;
}
