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
		this._validatoren.add(new ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(idStaatsangehoerigkeit, kontext));
	}

	protected pruefe(): boolean {
		const nationalitaet: Nationalitaeten | null = Nationalitaeten.data().getWertByIDOrNull(this._idStaatsangehoerigkeit.get());
		if (nationalitaet === null) {
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
