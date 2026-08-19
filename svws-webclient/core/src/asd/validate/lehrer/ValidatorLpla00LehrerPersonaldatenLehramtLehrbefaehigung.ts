import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/**
	 * Lehrbefähigung
	 */
	private readonly _idLehrbefaehigung: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idLehrbefaehigung   eine idLehrbefaehigung des Lehrers
	 * @param lehrerLehramt       Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(idLehrbefaehigung: Supplier<number | null>, lehrerLehramt: Supplier<LehrerLehramt | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idLehrbefaehigung = idLehrbefaehigung;
		this._validatoren.add(new ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung(this.getNotNullSupplierLong(idLehrbefaehigung), lehrerLehramt, kontext));
	}

	protected pruefe(): boolean {
		const lehrbefaehigungID: number | null = this._idLehrbefaehigung.get();
		if (lehrbefaehigungID === null) {
			this.addFehler(0, "Das Feld 'Lehrbefähigungen' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung;
}
