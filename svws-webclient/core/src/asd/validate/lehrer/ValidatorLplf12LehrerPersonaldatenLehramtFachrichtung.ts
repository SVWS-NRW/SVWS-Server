import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/**
	 * Das aktuelle Schuljahr des Lehrers
	 */
	private readonly _schuljahr: Supplier<number>;

	/**
	 * Die Katalog-ID der Fachrichtung.
	 */
	private readonly _idFachrichtung: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param schuljahr        Schuljahr
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idFachrichtung: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._idFachrichtung = idFachrichtung;
	}

	protected pruefe(): boolean {
		const fachrichtungID: number | null = this._idFachrichtung.get();
		if (fachrichtungID === null) {
			this.addFehler(0, "Fachrichtung des Lehrers: Bei allen Lehrkräften muss das Feld 'Fachrichtung' besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung(obj: unknown): ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung {
	return obj as ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung;
}
