import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import { ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';

export class ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/**
	 * Die Beschaeftigungsart
	 */
	private readonly _beschaeftigungsart: Supplier<LehrerBeschaeftigungsart | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus        der Einsatzstatus
	 * @param beschaeftigungsart   die Beschäftigungsart
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(beschaeftigungsart: Supplier<LehrerBeschaeftigungsart | null>, pflichtstundensoll: Supplier<number | null>, einsatzstatus: Supplier<LehrerEinsatzstatus | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._beschaeftigungsart = beschaeftigungsart;
		const beschaeftigungsartNotNull: Supplier<LehrerBeschaeftigungsart> = this.getNotNullObjectSupplier(beschaeftigungsart);
		this._validatoren.add(new ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsartNotNull, einsatzstatus, kontext));
		this._validatoren.add(new ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsartNotNull, einsatzstatus, pflichtstundensoll, kontext));
	}

	protected pruefe(): boolean {
		const beschaeftigungsart: LehrerBeschaeftigungsart | null = this._beschaeftigungsart.get();
		if (beschaeftigungsart === null) {
			this.addFehler(0, "Kein Wert im Feld 'beschaeftigungsart'.");
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
