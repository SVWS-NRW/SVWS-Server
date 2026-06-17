import { ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus } from '../../../asd/validate/lehrer/ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idEinsatzstatus   die ID des Einsatzstatus.
	 * @param kontext           der Kontext des Validators
	 */
	public constructor(idEinsatzstatus: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(this.getNotNullSupplierLong(idEinsatzstatus), kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus>('de.svws_nrw.asd.validate.lehrer.ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(obj: unknown): ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus {
	return obj as ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus;
}
