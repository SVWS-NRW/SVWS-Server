import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import { LehrerLehrbefaehigung } from '../../../asd/types/lehrer/LehrerLehrbefaehigung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/**
	 * Lehrbefähigung
	 */
	private readonly _idLehrbefaehigung: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param idLehrbefaehigung   eine idLehrbefaehigung des Lehrers
	 * @param lehrerLehramt       das Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(idLehrbefaehigung: Supplier<number>, lehrerLehramt: Supplier<LehrerLehramt | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idLehrbefaehigung = idLehrbefaehigung;
		this._validatoren.add(new ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung(idLehrbefaehigung, lehrerLehramt, kontext));
	}

	protected pruefe(): boolean {
		const lehrbefaehigungSchluessel: string | null = LehrerLehrbefaehigung.data().getSchluesselByIDOrNull(this._idLehrbefaehigung.get());
		if (lehrbefaehigungSchluessel === null) {
			this.addFehler(0, "Das Feld 'Lehrbefaehigung' muss zulässig sein. ");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung;
}
