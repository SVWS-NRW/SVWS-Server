import { ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung } from '../../../asd/validate/lehrer/ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung';
import { LehrerFachrichtung } from '../../../asd/types/lehrer/LehrerFachrichtung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/**
	 * Die Katalog-ID der Fachrichtung.
	 */
	private readonly _idFachrichtung: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param schuljahr        Schuljahr
	 * @param idLehramt        die Katalog-ID des Lehramtes
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idLehramt: Supplier<number | null>, idFachrichtung: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idFachrichtung = idFachrichtung;
		this._validatoren.add(new ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung(schuljahr, idLehramt, idFachrichtung, kontext));
	}

	protected pruefe(): boolean {
		const idFachrichtung: number | null = this._idFachrichtung.get();
		if (idFachrichtung !== null) {
			if (LehrerFachrichtung.data().getSchluesselByIDOrNull(idFachrichtung) === null) {
				this.addFehler(0, "Lehrer Fachrichtung: Das Feld 'Fachrichtung' muss zulässig sein.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung(obj: unknown): ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung {
	return obj as ValidatorLplf01LehrerPersonaldatenLehramtFachrichtung;
}
