import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination extends Validator {

	/**
	 * Das Pflichtstundensoll des Lehrers
	 */
	private readonly _pflichtstundensoll: Supplier<number | null>;

	/**
	 * Die Katalog-ID des Einsatzstatus des Lehrers
	 */
	private readonly _idEinsatzstatus: Supplier<number | null>;

	/**
	 * Die Katalog-ID der Beschäftigungsart des Lehrers
	 */
	private readonly _idBeschaeftigungsart: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll des Lehrers
	 * @param idEinsatzstatus        die Katalog-ID des Einsatzstatus des Lehrers
	 * @param idBeschaeftigungsart   die Katalog-ID der Beschäftigungsart des Lehrers
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(pflichtstundensoll: Supplier<number | null>, idEinsatzstatus: Supplier<number | null>, idBeschaeftigungsart: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._pflichtstundensoll = pflichtstundensoll;
		this._idEinsatzstatus = idEinsatzstatus;
		this._idBeschaeftigungsart = idBeschaeftigungsart;
	}

	protected pruefe(): boolean {
		const pflichtstundensoll: number | null = this._pflichtstundensoll.get();
		if ((pflichtstundensoll === null) || (pflichtstundensoll !== 0.0)) {
			return true;
		}
		const idEinsatzstatus: number | null = this._idEinsatzstatus.get();
		const einsatzstatus: LehrerEinsatzstatus | null = (idEinsatzstatus === null) ? null : LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatzstatus);
		const idBeschaeftigungsart: number | null = this._idBeschaeftigungsart.get();
		const beschaeftigungsart: LehrerBeschaeftigungsart | null = (idBeschaeftigungsart === null) ? null : LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigungsart);
		if ((einsatzstatus as unknown !== LehrerEinsatzstatus.A as unknown) && (beschaeftigungsart as unknown !== LehrerBeschaeftigungsart.WT as unknown) && (beschaeftigungsart as unknown !== LehrerBeschaeftigungsart.WV as unknown)) {
			this.addFehler(0, "Pflichtstundensoll des Lehrers: Bei einer Lehrkraft mit 'Pflichtstundensoll' = 0.00 muss der Einsatzstatus 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder die Beschäftigungsart 'Beamte auf Widerruf (LAA) in Vollzeit' bzw. 'Beamte auf Widerruf (LAA) in Teilzeit' lauten.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination>('de.svws_nrw.asd.validate.lehrer.ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination(obj: unknown): ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination {
	return obj as ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination;
}
