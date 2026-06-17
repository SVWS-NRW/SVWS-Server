import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus } from '../../../asd/validate/lehrer/ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus';

export class ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus extends Validator {

	/**
	 * Der Einsatzstatus
	 */
	private readonly _idEinsatzstatus: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Lehrer Einsatzstatus: Das Feld darf nicht leer sein.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idEinsatzstatus   die ID des Einsatzstatus.
	 * @param kontext           der Kontext des Validators
	 */
	public constructor(idEinsatzstatus: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idEinsatzstatus = idEinsatzstatus;
		this._validatoren.add(new ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(this.getNotNullSupplierLong(idEinsatzstatus), kontext));
	}

	protected pruefe(): boolean {
		const idEinsatzstatus: number | null = this._idEinsatzstatus.get();
		if (idEinsatzstatus === null) {
			this.addFehler(0, ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus>('de.svws_nrw.asd.validate.lehrer.ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(obj: unknown): ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus {
	return obj as ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus;
}
