import { ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung } from '../../../asd/validate/lehrer/ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idAnerkennungsgrund   die Katalog-ID des Anerkennungsgrunds
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(idAnerkennungsgrund: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(idAnerkennungsgrund, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(obj: unknown): ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung {
	return obj as ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung;
}
