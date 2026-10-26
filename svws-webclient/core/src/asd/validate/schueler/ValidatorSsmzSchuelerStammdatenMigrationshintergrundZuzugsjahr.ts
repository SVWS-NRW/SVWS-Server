import { ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr } from '../../../asd/validate/schueler/ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr extends Validator {

	private readonly _idSchuljahresabschnitt: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt    Schuljahresabschnitt ID
	 * @param zuzugsjahr                das Zuzugsjahr des Schülers
	 * @param geburtsdatum              das Geburtsjahr des Schülers
	 * @param hatMigrationshintergrund  Gibt an, ob ein Migrationshintergrund vorhanden ist
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number | null>, zuzugsjahr: Supplier<number | null>, geburtsdatum: Supplier<string | null>, hatMigrationshintergrund: Supplier<boolean>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		const schuljahr: Supplier<number> = { get: () => kontext.getSchuljahrBySchuljahresabschnitt(this.getNotNullSupplierLong(idSchuljahresabschnitt)) };
		this._validatoren.add(new ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr(schuljahr, zuzugsjahr, geburtsdatum, hatMigrationshintergrund, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr>('de.svws_nrw.asd.validate.schueler.ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr(obj: unknown): ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr {
	return obj as ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr;
}
