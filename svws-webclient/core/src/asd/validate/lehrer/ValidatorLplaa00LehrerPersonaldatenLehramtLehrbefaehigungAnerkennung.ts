import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung } from '../../../asd/validate/lehrer/ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung';

export class ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung extends Validator {

	/**
	 * Die Katalog-ID des Anerkennungsgrunds.
	 */
	private readonly _idAnerkennungsgrund: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idAnerkennungsgrund   die Katalog-ID des Anerkennungsgrunds
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(idAnerkennungsgrund: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idAnerkennungsgrund = idAnerkennungsgrund;
		this._validatoren.add(new ValidatorLplaa01LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(idAnerkennungsgrund, kontext));
	}

	protected pruefe(): boolean {
		const idAnerkennungsgrund: number | null = this._idAnerkennungsgrund.get();
		if (idAnerkennungsgrund === null) {
			this.addFehler(0, "Das Feld 'Anerkennungsgrund Lehramt' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(obj: unknown): ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung {
	return obj as ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung;
}
