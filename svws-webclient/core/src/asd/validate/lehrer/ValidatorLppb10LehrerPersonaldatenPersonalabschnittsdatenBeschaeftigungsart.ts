import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/**
	 * Die Beschäftigungsart
	 */
	private readonly _idBeschaeftigungsart: Supplier<number>;

	/**
	 * Der Einsatzstatus
	 */
	private readonly _idEinsatzstatus: Supplier<number>;

	private static readonly setEinsatzstatus2: JavaSet<LehrerEinsatzstatus> = java_util_Set_of(LehrerEinsatzstatus.A, LehrerEinsatzstatus.B);

	private static readonly FEHLERTEXT: string = "Bei einer unentgeltlich beschäftigten Lehrkraft (Feld 'Beschäftigungsart' = 'Unentgeltlich Beschäftigte') dürfen im Feld 'Einsatzstatus' nicht die Einträge 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder 'nicht Stammschule, aber auch hier tätig' eingetragen sein.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idBeschaeftigungsart   die Beschäftigungsart
	 * @param idEinsatzstatus        der Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(idBeschaeftigungsart: Supplier<number>, idEinsatzstatus: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idBeschaeftigungsart = idBeschaeftigungsart;
		this._idEinsatzstatus = idEinsatzstatus;
	}

	protected pruefe(): boolean {
		const idBeschaeftigungsart: number | null = this._idBeschaeftigungsart.get();
		const idEinsatzstatus: number | null = this._idEinsatzstatus.get();
		if (ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart.setEinsatzstatus2.contains(LehrerEinsatzstatus.data().getWertByID(idEinsatzstatus)) && (LehrerBeschaeftigungsart.X as unknown === LehrerBeschaeftigungsart.data().getWertByID(idBeschaeftigungsart) as unknown)) {
			this.addFehler(2, ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(obj: unknown): ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {
	return obj as ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart;
}
