import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/**
	 * Das Pflichtstundensoll
	 */
	private readonly _pflichtstundensoll: Supplier<number>;

	/**
	 * Der Einsatzstatus
	 */
	private readonly _einsatzstatus: Supplier<LehrerEinsatzstatus | null>;

	/**
	 * Die Beschäftigungsart
	 */
	private readonly _beschaeftigungsart: Supplier<LehrerBeschaeftigungsart | null>;

	private static readonly setBeschaeftigungsart: JavaSet<LehrerBeschaeftigungsart> = java_util_Set_of(LehrerBeschaeftigungsart.WV, LehrerBeschaeftigungsart.WT);

	private static readonly FEHLERTEXT: string = "Ist bei einer Lehrkraft im Feld 'Pflichtstundensoll' der Wert = 0.00 eingetragen, so muss das Feld 'Einsatzstatus' den Schlüssel 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder die 'Beschäftigungsart' den Schlüssel 'Beamte auf Widerruf (LAA) in Vollzeit' bzw. 'Beamte auf Widerruf (LAA) in Teilzeit' aufweisen.";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus        der Einsatzstatus
	 * @param beschaeftigungsart   die Beschäftigungsart
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(pflichtstundensoll: Supplier<number>, einsatzstatus: Supplier<LehrerEinsatzstatus | null>, beschaeftigungsart: Supplier<LehrerBeschaeftigungsart | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._pflichtstundensoll = pflichtstundensoll;
		this._einsatzstatus = einsatzstatus;
		this._beschaeftigungsart = beschaeftigungsart;
	}

	protected pruefe(): boolean {
		const pflichtstundensoll: number | null = this._pflichtstundensoll.get();
		const einsatzstatus: LehrerEinsatzstatus | null = this._einsatzstatus.get();
		const beschaeftigungsart: LehrerBeschaeftigungsart | null = this._beschaeftigungsart.get();
		if (pflichtstundensoll === 0.0 && !JavaObject.equalsTranspiler(LehrerEinsatzstatus.A, (einsatzstatus)) && (beschaeftigungsart === null || !ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll.setBeschaeftigungsart.contains(beschaeftigungsart))) {
			this.addFehler(3, ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll>('de.svws_nrw.asd.validate.lehrer.ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(obj: unknown): ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll {
	return obj as ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll;
}
