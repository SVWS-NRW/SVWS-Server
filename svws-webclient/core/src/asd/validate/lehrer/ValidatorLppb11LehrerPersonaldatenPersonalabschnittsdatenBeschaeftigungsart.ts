import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/**
	 * Die Beschäftigungsart
	 */
	private readonly _beschaeftigungsart: Supplier<LehrerBeschaeftigungsart>;

	/**
	 * Der Einsatzstatus
	 */
	private readonly _einsatzstatus: Supplier<LehrerEinsatzstatus | null>;

	/**
	 * Das Pflichtstundensoll
	 */
	private readonly _pflichtstundensoll: Supplier<number | null>;

	private static readonly fehlertext: string = "Laut Ihren Angaben handelt es sich um eine voll abgeordnete Lehrkraft mit Gestellungsvertrag. Es ist zu erwarten, \"\n\"dass eine Lehrkraft mit Gestellungsvertrag Unterricht an Ihrer Schule erteilt. Bitte überprüfen Sie Ihre Angaben.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param beschaeftigungsart     	die Beschäftigungsart
	 * @param einsatzstatus     		der Einsatzstatus
	 * @param pflichtstundensoll     	das Pflichtstundensoll
	 * @param kontext   				der Kontext des Validators
	 */
	public constructor(beschaeftigungsart: Supplier<LehrerBeschaeftigungsart>, einsatzstatus: Supplier<LehrerEinsatzstatus | null>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._beschaeftigungsart = beschaeftigungsart;
		this._einsatzstatus = einsatzstatus;
		this._pflichtstundensoll = pflichtstundensoll;
	}

	protected pruefe(): boolean {
		const beschaeftigungsart: LehrerBeschaeftigungsart | null = this._beschaeftigungsart.get();
		const einsatzstatus: LehrerEinsatzstatus | null = this._einsatzstatus.get();
		const pflichtstundensoll: number | null = this._pflichtstundensoll.get();
		if (LehrerBeschaeftigungsart.G as unknown === beschaeftigungsart as unknown && LehrerEinsatzstatus.A as unknown === einsatzstatus as unknown && pflichtstundensoll === 0) {
			this.addFehler(3, ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart.fehlertext);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(obj: unknown): ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {
	return obj as ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart;
}
