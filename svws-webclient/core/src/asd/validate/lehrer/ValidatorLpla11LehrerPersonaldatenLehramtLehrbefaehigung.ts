import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import { LehrerLehrbefaehigung } from '../../../asd/types/lehrer/LehrerLehrbefaehigung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/**
	 * Lehrbefähigung
	 */
	private readonly _Lehrbefaehigung: Supplier<LehrerLehrbefaehigung>;

	/**
	 * Lehramt
	 */
	private readonly _LehrerLehramt: Supplier<LehrerLehramt | null>;

	private static readonly zulaessigeLehraemter: JavaSet<LehrerLehramt> = java_util_Set_of(LehrerLehramt.ID_63, LehrerLehramt.ID_64, LehrerLehramt.ID_65);


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehrbefaehigung     eine Lehrbefaehigung des Lehrers
	 * @param lehrerLehramt       Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(lehrbefaehigung: Supplier<LehrerLehrbefaehigung>, lehrerLehramt: Supplier<LehrerLehramt | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._Lehrbefaehigung = lehrbefaehigung;
		this._LehrerLehramt = lehrerLehramt;
	}

	protected pruefe(): boolean {
		if (ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung.zulaessigeLehraemter.contains(this._LehrerLehramt.get())) {
			if (!JavaObject.equalsTranspiler(LehrerLehrbefaehigung.BE, (this._Lehrbefaehigung.get()))) {
				this.addFehler(0, "Für die Lehrämter 'Alltagshelfer/-in', 'Handwerksmeister/-in' und 'Heilpädagoge/-in' ist nur die Lehrbefähigung 'Betreuung' zulässig.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung;
}
