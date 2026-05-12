import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idBeschaeftigungsart    die Beschäftigungsart
	 * @param idEinsatzstatus     	der Einsatzstatus
	 * @param pflichtstundensoll    das Pflichtstundensoll
	 * @param kontext   			der Kontext des Validators
	 */
	public constructor(idBeschaeftigungsart: Supplier<number | null>, idEinsatzstatus: Supplier<number | null>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		const einsatzstatus: Supplier<LehrerEinsatzstatus | null> = { get: () => LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatzstatus.get()) };
		const beschaeftigungsart: Supplier<LehrerBeschaeftigungsart | null> = { get: () => LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigungsart.get()) };
		this._validatoren.add(new ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsart, pflichtstundensoll, einsatzstatus, kontext));
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
