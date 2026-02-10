import { JavaObject } from '../../../java/lang/JavaObject';
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
	private readonly pflichtstundensoll: Supplier<number | null>;

	/**
	 * Der Einsatzstatus
	 */
	private readonly einsatzstatus: Supplier<string | null>;

	/**
	 * Die Beschäftigungsart
	 */
	private readonly beschaeftigungsart: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll    das Pflichtstundensoll
	 * @param einsatzstatus    		Der Einsatzstatus
	 * @param beschaeftigungsart    Die Beschäftigungsart
	 * @param kontext   			der Kontext des Validators
	 */
	public constructor(pflichtstundensoll: Supplier<number | null>, einsatzstatus: Supplier<string | null>, beschaeftigungsart: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.pflichtstundensoll = pflichtstundensoll;
		this.einsatzstatus = einsatzstatus;
		this.beschaeftigungsart = beschaeftigungsart;
	}

	protected pruefe(): boolean {
		const pflichtstundensoll: number | null = this.pflichtstundensoll.get();
		const einsatzstatus: LehrerEinsatzstatus | null = LehrerEinsatzstatus.getBySchluessel(this.einsatzstatus.get());
		const beschaeftigungsart: string | null = this.beschaeftigungsart.get();
		const setBeschaeftigungsart: JavaSet<string> = java_util_Set_of("WV", "WT");
		const fehlertext3: string | null = "Ist bei einer Lehrkraft im Feld 'Pflichtstundensoll' der Wert = 0.00 eingetragen, so muss das Feld 'Einsatzstatus' den Schlüssel 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder die 'Beschäftigungsart' den Schlüssel 'Beamte auf Widerruf (LAA) in Vollzeit' bzw. 'Beamte auf Widerruf (LAA) in Teilzeit' aufweisen.";
		if (pflichtstundensoll === 0.0 && !JavaObject.equalsTranspiler(LehrerEinsatzstatus.A, (einsatzstatus)) && !setBeschaeftigungsart.contains(beschaeftigungsart)) {
			this.addFehler(3, fehlertext3);
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
