import { ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { LehrerLehrbefaehigung } from '../../../asd/types/lehrer/LehrerLehrbefaehigung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung';

export class ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/**
	 * IDLehrbefähigung
	 */
	private readonly _idLehrbefaehigung: Supplier<number>;

	/**
	 * Lehrbefähigung
	 */
	private readonly _lehrbefaehigung: Supplier<LehrerLehrbefaehigung>;


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
		this._lehrbefaehigung = { get: () => LehrerLehrbefaehigung.data().getWertByID(idLehrbefaehigung.get()) };
		this._validatoren.add(new ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung(this._lehrbefaehigung, lehrerLehramt, kontext));
		this._validatoren.add(new ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung(this._lehrbefaehigung, lehrerLehramt, kontext));
		this._validatoren.add(new ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung(this._lehrbefaehigung, lehrerLehramt, kontext));
	}

	protected pruefe(): boolean {
		if (!LehrerLehrbefaehigung.data().isGueltig(this._idLehrbefaehigung.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Der eingetragene Wert für das Feld 'Lehrbefähigungen' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung;
}
