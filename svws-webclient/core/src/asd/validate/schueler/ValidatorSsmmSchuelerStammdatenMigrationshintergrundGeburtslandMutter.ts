import { ValidatorSsmm00SchuelerStammdatenMigrationshintergrundGeburtslandMutter } from '../../../asd/validate/schueler/ValidatorSsmm00SchuelerStammdatenMigrationshintergrundGeburtslandMutter';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter extends Validator {

	/**
	 * Geburtsland der Mutter
	 */
	private readonly _idGeburtslandMutter: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandMutter        die ID des Geburtslandes der Mutter
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(idGeburtslandMutter: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtslandMutter = idGeburtslandMutter;
		this._validatoren.add(new ValidatorSsmm00SchuelerStammdatenMigrationshintergrundGeburtslandMutter(this._idGeburtslandMutter, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter>('de.svws_nrw.asd.validate.schueler.ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter(obj: unknown): ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter {
	return obj as ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter;
}
