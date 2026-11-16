import { ValidatorLplf11LehrerPersonaldatenLehramtFachrichtung } from '../../../asd/validate/lehrer/ValidatorLplf11LehrerPersonaldatenLehramtFachrichtung';
import { LehrerFachrichtung } from '../../../asd/types/lehrer/LehrerFachrichtung';
import { ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung } from '../../../asd/validate/lehrer/ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung';
import { ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung } from '../../../asd/validate/lehrer/ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung extends Validator {

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
	 * @param idLehramt        die Katalog-ID des Lehramtes
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idLehramt: Supplier<number | null>, idFachrichtung: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._idFachrichtung = idFachrichtung;
		this._validatoren.add(new ValidatorLplf10LehrerPersonaldatenLehramtFachrichtung(schuljahr, idLehramt, idFachrichtung, kontext));
		this._validatoren.add(new ValidatorLplf11LehrerPersonaldatenLehramtFachrichtung(schuljahr, idLehramt, idFachrichtung, kontext));
		this._validatoren.add(new ValidatorLplf12LehrerPersonaldatenLehramtFachrichtung(schuljahr, idFachrichtung, kontext));
	}

	protected pruefe(): boolean {
		const idFachrichtung: number | null = this._idFachrichtung.get();
		if (idFachrichtung !== null) {
			if (!LehrerFachrichtung.data().isGueltig(this._idFachrichtung.get(), this._schuljahr.get())) {
				this.addFehler(0, "Lehrer Fachrichtung: Der eingetragene Wert für das Feld 'Fachrichtung' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung(obj: unknown): ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung {
	return obj as ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung;
}
