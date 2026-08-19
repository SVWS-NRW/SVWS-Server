import { ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter } from '../../../asd/validate/schueler/ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland } from '../../../asd/validate/schueler/ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland';
import { Class } from '../../../java/lang/Class';
import { ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater } from '../../../asd/validate/schueler/ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmSchuelerStammdatenMigrationshintergrund extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtsland              die ID des Geburtslands
	 * @param idGeburtslandMutter        die ID des Geburtslands der Mutter
	 * @param idGeburtslandVater         die ID des Geburtslands des Vaters
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(idGeburtsland: Supplier<number | null>, idGeburtslandMutter: Supplier<number | null>, idGeburtslandVater: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland(idGeburtsland, hatMigrationshintergrund, kontext));
		this._validatoren.add(new ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter(idGeburtslandMutter, hatMigrationshintergrund, kontext));
		this._validatoren.add(new ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater(idGeburtslandVater, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmSchuelerStammdatenMigrationshintergrund';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmSchuelerStammdatenMigrationshintergrund', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmSchuelerStammdatenMigrationshintergrund>('de.svws_nrw.asd.validate.schueler.ValidatorSsmSchuelerStammdatenMigrationshintergrund');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmSchuelerStammdatenMigrationshintergrund(obj: unknown): ValidatorSsmSchuelerStammdatenMigrationshintergrund {
	return obj as ValidatorSsmSchuelerStammdatenMigrationshintergrund;
}
