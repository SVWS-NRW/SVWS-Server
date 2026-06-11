import { LehrerFachrichtung } from '../../../asd/types/lehrer/LehrerFachrichtung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerFachrichtungKatalogEintrag } from '../../../asd/data/lehrer/LehrerFachrichtungKatalogEintrag';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplf02LehrerPersonaldatenLehramtFachrichtung extends Validator {

	/**
	 * Die Katalog-ID der Fachrichtung.
	 */
	private readonly _idFachrichtung: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idFachrichtung   die Katalog-ID der Fachrichtung
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(idFachrichtung: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idFachrichtung = idFachrichtung;
	}

	protected pruefe(): boolean {
		const idFachrichtung: number | null = this._idFachrichtung.get();
		const wert: LehrerFachrichtung | null = LehrerFachrichtung.data().getWertByIDOrNull(idFachrichtung);
		const schuljahr: number = this.kontext().getSchuljahr();
		const eintragAktuell: LehrerFachrichtungKatalogEintrag | null = (wert === null) ? null : LehrerFachrichtung.data().getEintragBySchuljahrUndWert(schuljahr, wert);
		if ((eintragAktuell === null) || (eintragAktuell.id !== idFachrichtung)) {
			this.addFehler(0, "Lehrer Fachrichtung: Der eingetragene Wert für das Feld 'Fachrichtung' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
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
