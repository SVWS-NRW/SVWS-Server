import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import { ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/**
	 * Die Beschaeftigungsart
	 */
	private readonly _idBeschaeftigungsart: Supplier<number>;

	private static readonly FEHLERTEXT: string = "Lehrer Beschäftigungsart: Das Feld 'Beschäftigungsart' muss zulässig sein.";


	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param idBeschaeftigungsart     die Beschäftigungsart
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus          der Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(idBeschaeftigungsart: Supplier<number>, pflichtstundensoll: Supplier<number | null>, einsatzstatus: Supplier<LehrerEinsatzstatus | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idBeschaeftigungsart = idBeschaeftigungsart;
		const schuljahr2: number = kontext.getSchuljahr();
		const schuljahr: Supplier<number> = { get: () => schuljahr2 };
		this._validatoren.add(new ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(idBeschaeftigungsart, pflichtstundensoll, einsatzstatus, kontext));
	}

	public pruefe(): boolean {
		const idBeschaeftigungsart: number = this._idBeschaeftigungsart.get();
		const ba: LehrerBeschaeftigungsart | null = LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigungsart);
		if (ba === null) {
			this.addFehler(0, ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(obj: unknown): ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {
	return obj as ValidatorLppb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart;
}
