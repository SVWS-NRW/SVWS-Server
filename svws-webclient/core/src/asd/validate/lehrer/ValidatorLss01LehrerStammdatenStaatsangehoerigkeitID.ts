import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import { ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID } from '../../../asd/validate/lehrer/ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	private readonly _idStaatsangehoerigkeit: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idStaatsangehoerigkeit: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
		const staatsangehoerigkeitSchluessel: Supplier<string> = this.getNotNullSupplier({ get: () => Nationalitaeten.data().getSchluesselByIDOrNull(this._idStaatsangehoerigkeit.get()) });
		const schuljahr: Supplier<number> = { get: () => kontext.getSchuljahr() };
		this._validatoren.add(new ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(staatsangehoerigkeitSchluessel, schuljahr, kontext));
	}

	protected pruefe(): boolean {
		const staatsangehoerigkeitSchluessel: string | null = Nationalitaeten.data().getSchluesselByIDOrNull(this._idStaatsangehoerigkeit.get());
		if (staatsangehoerigkeitSchluessel === null) {
			this.addFehler(0, "Das Feld 'Staatsangehörigkeit' muss zulässig sein. ");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID>('de.svws_nrw.asd.validate.lehrer.ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID(obj: unknown): ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID {
	return obj as ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID;
}
