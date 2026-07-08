import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import { LehrerLehrbefaehigung } from '../../../asd/types/lehrer/LehrerLehrbefaehigung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/**
	 * Lehrbefähigung
	 */
	private readonly _Lehrbefaehigung: Supplier<LehrerLehrbefaehigung>;

	/**
	 * Lehramt
	 */
	private readonly _lehrerLehramt: Supplier<LehrerLehramt | null>;

	private static readonly zulaessigeLehraemter: JavaSet<LehrerLehramt> = java_util_Set_of(LehrerLehramt.ID_30, LehrerLehramt.ID_32, LehrerLehramt.ID_35);


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehrbefaehigung     eine Lehrbefaehigung des Lehrers
	 * @param lehrerLehramt       das Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(lehrbefaehigung: Supplier<LehrerLehrbefaehigung>, lehrerLehramt: Supplier<LehrerLehramt | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._Lehrbefaehigung = lehrbefaehigung;
		this._lehrerLehramt = lehrerLehramt;
	}

	protected pruefe(): boolean {
		if (!ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung.zulaessigeLehraemter.contains(this._lehrerLehramt.get())) {
			if (this._Lehrbefaehigung.get() === null) {
				this.addFehler(0, "Das Feld 'Lehrbefähigung' darf nur bei den Lehrämtern 'Berufsbildende Schulen - altes Lehramt -', 'Sekundarstufe II (mit beruflicher Fachrichtung)' und 'Berufskolleg' leer sein.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung;
}
