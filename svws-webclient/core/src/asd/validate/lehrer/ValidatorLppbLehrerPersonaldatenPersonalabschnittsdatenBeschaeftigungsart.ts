import { ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';

export class ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idBeschaeftigungsart    die Beschäftigungsart
	 * @param idEinsatzstatus     	der Einsatzstatus
	 * @param pflichtstundensoll    das Pflichtstundensoll
	 * @param kontext   			der Kontext des Validators
	 */
	public constructor(idBeschaeftigungsart: Supplier<number>, idEinsatzstatus: Supplier<number>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(idBeschaeftigungsart, idEinsatzstatus, kontext));
		this._validatoren.add(new ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(idBeschaeftigungsart, idEinsatzstatus, pflichtstundensoll, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(obj: unknown): ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {
	return obj as ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart;
}
