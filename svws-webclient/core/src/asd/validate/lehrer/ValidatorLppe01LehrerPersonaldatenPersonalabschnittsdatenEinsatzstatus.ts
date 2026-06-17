import { ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus } from '../../../asd/validate/lehrer/ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus extends Validator {

	/**
	 * Der Einsatzstatus
	 */
	private readonly _idEinsatzstatus: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Lehrer Einsatzstatus: Das Feld 'Einsatzstatus' muss zulässig sein.";


	/**
	 * Erstellt einen neuen Validator für das vorhandensein des Einsatzstatus im Katalog.
	 *
	 * @param idEinsatzstatus   der Einsatzstatus
	 * @param kontext           der Kontext des Validators
	 */
	public constructor(idEinsatzstatus: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idEinsatzstatus = idEinsatzstatus;
		this._validatoren.add(new ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(this.getNotNullSupplierLong(idEinsatzstatus), kontext));
	}

	protected pruefe(): boolean {
		const idEinsatzstatus: number | null = this._idEinsatzstatus.get();
		if (LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatzstatus) === null) {
			this.addFehler(0, ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus>('de.svws_nrw.asd.validate.lehrer.ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(obj: unknown): ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus {
	return obj as ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus;
}
