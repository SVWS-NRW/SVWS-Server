import { DateManager } from '../../../asd/validate/DateManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr extends Validator {

	/**
	 * Das Schuljahr des Schülers
	 */
	private readonly _schuljahr: Supplier<number | null>;

	/**
	 * Das Zuzugsjahr des Schülers
	 */
	private readonly _zuzugsjahr: Supplier<number | null>;

	/**
	 * Das Geburtsdatumm des Schülers
	 */
	private readonly _geburtsdatum: Supplier<string>;

	/**
	 * Gibt an, ob ein Migrationshintergrund vorhanden ist
	 */
	private readonly _hatMigrationshintergrund: Supplier<boolean | null>;

	private static readonly FEHLERTEXT: string = "Das eingetragene 'Zuzugsjahr' darf nicht vor dem Geburtsdatum des Schülers und nicht in der Zukunft liegen.";


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
	}

	protected pruefe(): boolean {
		let geburtsdatum: DateManager | null = null;
		let errorMsg: string = "";
		try {
			geburtsdatum = DateManager.from(this._geburtsdatum.get());
		} catch (e: any) {
			errorMsg = e.getMessage();
		}
		if (geburtsdatum === null) {
			return true;
		}
		const finalGeburtsdatum: DateManager | null = geburtsdatum;
		let zuzugsjahrNeu: Supplier<number> = this.getNotNullSupplierInteger(this._zuzugsjahr);
		let schuljahrNeu: Supplier<number> = this.getNotNullSupplierInteger(this._schuljahr);
		if (zuzugsjahrNeu.get() < finalGeburtsdatum.getJahr() || zuzugsjahrNeu.get() > schuljahrNeu.get()) {
			this.addFehler(0, ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr.FEHLERTEXT + errorMsg);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr>('de.svws_nrw.asd.validate.schueler.ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr(obj: unknown): ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr {
	return obj as ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr;
}
