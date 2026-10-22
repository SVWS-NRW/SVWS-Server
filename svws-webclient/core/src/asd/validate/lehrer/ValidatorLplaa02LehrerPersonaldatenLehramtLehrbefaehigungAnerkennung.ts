import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { LehrerLehramtAnerkennung } from '../../../asd/types/lehrer/LehrerLehramtAnerkennung';

export class ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung extends Validator {

	/**
	 * Das aktuelle Schuljahr des Lehrers
	 */
	private readonly _schuljahr: Supplier<number>;

	/**
	 * Die Katalog-ID des Anerkennungsgrunds.
	 */
	private readonly _idAnerkennungsgrund: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param schuljahr             Schuljahr
	 * @param idAnerkennungsgrund   die Katalog-ID des Anerkennungsgrunds
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idAnerkennungsgrund: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._idAnerkennungsgrund = idAnerkennungsgrund;
	}

	protected pruefe(): boolean {
		const idAnerkennungsgrund: number | null = this._idAnerkennungsgrund.get();
		if (!LehrerLehramtAnerkennung.data().isGueltig(idAnerkennungsgrund, this._schuljahr.get())) {
			this.addFehler(0, "Der eingetragene Wert für das Feld 'Anerkennung Lehramt' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(obj: unknown): ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung {
	return obj as ValidatorLplaa02LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung;
}
