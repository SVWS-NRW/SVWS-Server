import { NullPointerException } from '../../../java/lang/NullPointerException';
import type { Supplier } from '../../../java/util/function/Supplier';
import { ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	private readonly _idSchuljahresabschnitt: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param idBeschaeftigungsart     die Beschäftigungsart
	 * @param idEinsatzstatus     	   der Einsatzstatus
	 * @param pflichtstundensoll       das Pflichtstundensoll
	 * @param kontext   			   der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number | null>, idBeschaeftigungsart: Supplier<number | null>, idEinsatzstatus: Supplier<number | null>, pflichtstundensoll: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idSchuljahresabschnitt = idSchuljahresabschnitt;
		const schuljahr: Supplier<number> = { get: () => {
			const sja: Schuljahresabschnitt | null = kontext.getSchuljahresabschnittByID(this.getNotNullSupplierLong(idSchuljahresabschnitt).get());
			if (sja === null) {
				throw new NullPointerException();
			}
			return sja.schuljahr;
		} };
		const einsatzstatus: Supplier<LehrerEinsatzstatus | null> = { get: () => LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatzstatus.get()) };
		this._validatoren.add(new ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(schuljahr, idBeschaeftigungsart, pflichtstundensoll, einsatzstatus, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(obj: unknown): ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart {
	return obj as ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart;
}
