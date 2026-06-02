import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import { ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import { LehrerBeschaeftigungsartKatalogEintrag } from '../../../asd/data/lehrer/LehrerBeschaeftigungsartKatalogEintrag';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';

export class ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/**
	 * Die Beschaeftigungsart
	 */
	private readonly _beschaeftigungsartNotNull: Supplier<LehrerBeschaeftigungsart>;

	/**
	 * Das Schuljahr
	 */
	private readonly _schuljahr: number;

	private static readonly FEHLERTEXT: string = "Lehrer Beschäftigungsart: Der eingetragene Wert für das Feld 'Beschäftigungsart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";


	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param beschaeftigungsartNotNull     die Beschäftigungsart
	 * @param schuljahr
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus          der Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(beschaeftigungsartNotNull: Supplier<LehrerBeschaeftigungsart>, schuljahr: Supplier<number>, pflichtstundensoll: Supplier<number | null>, einsatzstatus: Supplier<LehrerEinsatzstatus | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._beschaeftigungsartNotNull = beschaeftigungsartNotNull;
		this._schuljahr = schuljahr.get().valueOf();
		this._validatoren.add(new ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsartNotNull, einsatzstatus, kontext));
		this._validatoren.add(new ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(beschaeftigungsartNotNull, einsatzstatus, pflichtstundensoll, kontext));
	}

	protected pruefe(): boolean {
		const lehrerBeschaeftigungsartKatalogEintrag: LehrerBeschaeftigungsartKatalogEintrag | null = this._beschaeftigungsartNotNull.get().daten(this._schuljahr);
		if (lehrerBeschaeftigungsartKatalogEintrag === null) {
			this.addFehler(0, ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(obj: unknown): ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {
	return obj as ValidatorLppb02LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart;
}
