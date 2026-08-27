import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID } from '../../../asd/validate/lehrer/ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	private readonly _idStaatsangehoerigkeit: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
		this._validatoren.add(new ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID(this.getNotNullSupplierLong(idStaatsangehoerigkeit), kontext));
	}

	protected pruefe(): boolean {
		const staatsangehoerigkeitID: number | null = this._idStaatsangehoerigkeit.get();
		if (staatsangehoerigkeitID === null) {
			this.addFehler(0, "Das Feld 'Staatsangehörigkeit' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID>('de.svws_nrw.asd.validate.lehrer.ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID(obj: unknown): ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID {
	return obj as ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID;
}
