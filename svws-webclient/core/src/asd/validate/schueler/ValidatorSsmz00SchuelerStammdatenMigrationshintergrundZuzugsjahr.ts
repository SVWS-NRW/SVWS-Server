import { ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr } from '../../../asd/validate/schueler/ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr } from '../../../asd/validate/schueler/ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr extends Validator {

	/**
	 * Das Zuzugjahr des Schülers
	 */
	private readonly _zuzugsjahr: Supplier<number | null>;

	/**
	 * Gibt an, ob ein Migrationshintergrund vorhanden ist
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	/**
	 * Das Schuljahr
	 */
	private _schuljahr: Supplier<number | null>;

	/**
	 * Das Geburtsdatum des Schülers
	 */
	private _geburtsdatum: Supplier<string>;

	/**
	 * Der Kontext
	 */
	private _kontextNeu: ValidatorKontext;

	private static readonly FEHLERTEXT: string = "Zuzugsjahr des Schülers: Wenn ein Migrationshintergrund vorhanden ist, muss das Feld 'Zuzugsjahr' besetzt sein.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param schuljahr                 das Schuljahr des Schülers
	 * @param zuzugsjahr                das Zuzugsjahr des Schülers
	 * @param geburtsdatum              das Geburtsdatum des Schülers
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number | null>, zuzugsjahr: Supplier<number | null>, geburtsdatum: Supplier<string>, hatMigrationshintergrund: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._zuzugsjahr = zuzugsjahr;
		this._geburtsdatum = geburtsdatum;
		this._hatMigrationshintergrund = hatMigrationshintergrund;
		this._kontextNeu = kontext;
	}

	protected pruefe(): boolean {
		const zuzugsjahr: number | null = this._zuzugsjahr.get();
		const hatMigrationshintergrundZwisch: boolean | null = this._hatMigrationshintergrund.get();
		const hatMigrationshintergrund: boolean = (hatMigrationshintergrundZwisch !== null) && hatMigrationshintergrundZwisch;
		if (hatMigrationshintergrund) {
			if (zuzugsjahr === null) {
				this.addFehler(0, ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr.FEHLERTEXT);
				return false;
			}
			this._validatoren.add(new ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr(this._schuljahr, { get: () => zuzugsjahr }, this._geburtsdatum, { get: () => hatMigrationshintergrund }, this._kontextNeu));
		} else {
			this._validatoren.add(new ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr({ get: () => zuzugsjahr }, this._kontextNeu));
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr>('de.svws_nrw.asd.validate.schueler.ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr(obj: unknown): ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr {
	return obj as ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr;
}
