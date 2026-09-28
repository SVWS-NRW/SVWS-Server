import { NullPointerException } from '../../../java/lang/NullPointerException';
import { ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr } from '../../../asd/validate/schueler/ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr extends Validator {

	private readonly _idSchuljahresabschnitt: Supplier<number | null>;

	private readonly _kontextNeu: ValidatorKontext;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param zuzugsjahr                das Zuzugsjahr des Schülers
	 * @param geburtsdatum             das Geburtsjahr des Schülers
	 * @param hatMigrationshintergrund  Gibt an, ob ein Migrationshintergrund vorhanden ist
	 * @param kontext                   der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number | null>, zuzugsjahr: Supplier<number | null>, geburtsdatum: Supplier<string>, hatMigrationshintergrund: Supplier<boolean>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		this._kontextNeu = kontext;
		const schuljahr: Supplier<number> = { get: () => {
			const sja: Schuljahresabschnitt | null = this._kontextNeu.getSchuljahresabschnittByID(this.getNotNullSupplierLong(idSchuljahresabschnitt).get());
			if (sja === null) {
				throw new NullPointerException();
			}
			return sja.schuljahr;
		} };
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
