import { ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/**
	 * Die Beschaeftigungsart
	 */
	private readonly _idBeschaeftigungsart: Supplier<number | null>;

	private static readonly FEHLERTEXT: string = "Kein Wert im Feld 'beschaeftigungsart'.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus          der Einsatzstatus
	 * @param idBeschaeftigungsart   die ID der Beschäftigungsart
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(idBeschaeftigungsart: Supplier<number | null>, pflichtstundensoll: Supplier<number | null>, einsatzstatus: Supplier<LehrerEinsatzstatus | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idBeschaeftigungsart = idBeschaeftigungsart;
		this._validatoren.add(new ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(this.getNotNullSupplierLong(idBeschaeftigungsart), pflichtstundensoll, einsatzstatus, kontext));
	}

	public pruefe(): boolean {
		const idBeschaeftigungsart: number | null = this._idBeschaeftigungsart.get();
		if (idBeschaeftigungsart === null) {
			this.addFehler(0, ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(obj: unknown): ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {
	return obj as ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart;
}
