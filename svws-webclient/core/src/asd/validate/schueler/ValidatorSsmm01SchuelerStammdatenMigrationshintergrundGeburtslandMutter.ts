import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter } from '../../../asd/validate/schueler/ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter extends Validator {

	/**
	 * Geburtsland der Mutter
	 */
	private readonly _idGeburtslandMutter: Supplier<number | null>;

	/**
	 * Gibt an, ob ein Migrationshintergrund vorhanden ist
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Geburtsland der Mutter: Das Feld muss zulässig sein.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandMutter        die ID des Geburtslandes der Mutter
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(idGeburtslandMutter: Supplier<number | null>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idGeburtslandMutter = idGeburtslandMutter;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
		this._validatoren.add(new ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter(idGeburtslandMutter, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		const idGeburtslandMutter: number | null = this._idGeburtslandMutter.get();
		if (idGeburtslandMutter === null) {
			return true;
		}
		if (Nationalitaeten.data().getWertByIDOrNull(idGeburtslandMutter) === null) {
			this.addFehler(0, ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.schueler.ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter>('de.svws_nrw.asd.validate.schueler.ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter(obj: unknown): ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter {
	return obj as ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter;
}
