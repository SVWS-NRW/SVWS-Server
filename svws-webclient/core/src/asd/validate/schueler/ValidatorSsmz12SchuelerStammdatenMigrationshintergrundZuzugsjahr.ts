import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr extends Validator {

	/**
	 * Das Zuzugjahr des Schülers
	 */
	private readonly _zuzugsjahr: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Das Feld 'Zuzugsjahr' darf nur ausgefüllt werden, wenn ein Migrationshintergrund vorhanden ist.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param zuzugsjahr                das Zuzugsjahr des Schülers
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(zuzugsjahr: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._zuzugsjahr = zuzugsjahr;
	}

	protected pruefe(): boolean {
		const zuzugsjahr: number | null = this._zuzugsjahr.get();
		if (zuzugsjahr !== null) {
			this.addFehler(0, ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr>('de.svws_nrw.asd.validate.schueler.ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr(obj: unknown): ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr {
	return obj as ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr;
}
