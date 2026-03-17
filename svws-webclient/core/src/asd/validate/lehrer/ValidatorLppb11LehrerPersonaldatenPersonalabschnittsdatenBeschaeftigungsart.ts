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
	private readonly _idBeschaeftigungsart: Supplier<number>;

	/**
	 * Der Einsatzstatus
	 */
	private readonly _idEinsatzstatus: Supplier<number>;

	/**
	 * Das Pflichtstundensoll
	 */
	private readonly _pflichtstundensoll: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idBeschaeftigungsart     	die Beschäftigungsart
	 * @param idEinsatzstatus     		der Einsatzstatus
	 * @param pflichtstundensoll     	das Pflichtstundensoll
	 * @param kontext   				der Kontext des Validators
	 */
	public constructor(idBeschaeftigungsart: Supplier<number>, idEinsatzstatus: Supplier<number>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idBeschaeftigungsart = idBeschaeftigungsart;
		this._idEinsatzstatus = idEinsatzstatus;
		this._pflichtstundensoll = pflichtstundensoll;
	}

	protected pruefe(): boolean {
		const idBeschaeftigungsart: number | null = this._idBeschaeftigungsart.get();
		const idEinsatzstatus: number | null = this._idEinsatzstatus.get();
		const pflichtstundensoll: number | null = this._pflichtstundensoll.get();
		if (pflichtstundensoll === null)
			return true;
		const fehlertext3: string | null = "Laut Ihren Angaben handelt es sich um eine voll abgeordnete Lehrkraft mit Gestellungsvertrag. Es ist zu erwarten, dass eine Lehrkraft mit Gestellungsvertrag Unterricht an Ihrer Schule erteilt. Bitte überprüfen Sie Ihre Angaben.";
		if ((LehrerBeschaeftigungsart.G as unknown === LehrerBeschaeftigungsart.data().getWertByID(idBeschaeftigungsart) as unknown) && (LehrerEinsatzstatus.A as unknown === LehrerEinsatzstatus.data().getWertByID(idEinsatzstatus) as unknown) && pflichtstundensoll === 0) {
			this.addFehler(3, fehlertext3);
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
