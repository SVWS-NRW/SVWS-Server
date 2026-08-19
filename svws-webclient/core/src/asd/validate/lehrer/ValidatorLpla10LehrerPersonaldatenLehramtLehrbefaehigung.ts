import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import { LehrerLehrbefaehigung } from '../../../asd/types/lehrer/LehrerLehrbefaehigung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/**
	 * Lehrbefähigung
	 */
	private readonly _Lehrbefaehigung: Supplier<LehrerLehrbefaehigung>;

	/**
	 * Lehramt
	 */
	private readonly _lehrerLehramt: Supplier<LehrerLehramt | null>;


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param _lehrbefaehigung    eine Lehrbefaehigung des Lehrers
	 * @param lehrerLehramt       Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(_lehrbefaehigung: Supplier<LehrerLehrbefaehigung>, lehrerLehramt: Supplier<LehrerLehramt | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._Lehrbefaehigung = _lehrbefaehigung;
		this._lehrerLehramt = lehrerLehramt;
	}

	protected pruefe(): boolean {
		if (JavaObject.equalsTranspiler(LehrerLehramt.ID_70, (this._lehrerLehramt.get()))) {
			if (!JavaObject.equalsTranspiler(LehrerLehrbefaehigung.OA, (this._Lehrbefaehigung.get()))) {
				this.addFehler(0, "Für das Lehramt 'Schulverwaltungsassistent/-in' ist nur die Lehrbefähigung 'ohne Angabe' zulässig.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung;
}
