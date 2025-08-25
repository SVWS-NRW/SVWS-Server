import { LehrerPersonaldaten } from '../../asd/data/lehrer/LehrerPersonaldaten';
import { ValidatorSchuleStammdaten } from '../../asd/validate/schule/ValidatorSchuleStammdaten';
import { HashMap } from '../../java/util/HashMap';
import { LehrerStammdaten } from '../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../java/lang/Class';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';
import { ValidatorLehrerPersonaldaten } from '../../asd/validate/lehrer/ValidatorLehrerPersonaldaten';
import { Validator, cast_de_svws_nrw_asd_validate_Validator } from '../../asd/validate/Validator';
import { SchuleStatistikdatenGesamt } from '../../asd/data/schule/SchuleStatistikdatenGesamt';
import { ValidatorLehrerStammdaten } from '../../asd/validate/lehrer/ValidatorLehrerStammdaten';

export class ValidatorGesamt extends Validator {

	private readonly daten : SchuleStatistikdatenGesamt;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : SchuleStatistikdatenGesamt, kontext : ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorSchuleStammdaten(kontext));
		const mapStammdaten : HashMap<number, LehrerStammdaten> | null = new HashMap<number, LehrerStammdaten>();
		for (const lehrerStammdaten of daten.lehrerStammdaten) {
			this._validatoren.add(new ValidatorLehrerStammdaten(lehrerStammdaten, kontext));
			mapStammdaten.put(lehrerStammdaten.id, lehrerStammdaten);
		}
		for (const lehrerPersonaldaten of daten.lehrerPersonaldaten) {
			const stammdaten : LehrerStammdaten | null = mapStammdaten.get(lehrerPersonaldaten.id);
			if (stammdaten === null)
				continue;
			this._validatoren.add(new ValidatorLehrerPersonaldaten(lehrerPersonaldaten, stammdaten, kontext));
		}
	}

	protected pruefe() : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorGesamt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorGesamt', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorGesamt>('de.svws_nrw.asd.validate.ValidatorGesamt');

}

export function cast_de_svws_nrw_asd_validate_ValidatorGesamt(obj : unknown) : ValidatorGesamt {
	return obj as ValidatorGesamt;
}
