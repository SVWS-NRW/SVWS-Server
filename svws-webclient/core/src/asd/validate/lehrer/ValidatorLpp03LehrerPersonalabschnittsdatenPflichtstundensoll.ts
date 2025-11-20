import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/**
	 * Die Lehrer-Personalabschnittsdaten
	 */
	private readonly daten: LehrerPersonalabschnittsdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerPersonalabschnittsdaten, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		let success: boolean = true;
		const pflichtstundensoll: number | null = this.daten.pflichtstundensoll;
		const einsatzstatus: LehrerEinsatzstatus | null = LehrerEinsatzstatus.getBySchluessel(this.daten.einsatzstatus);
		const beschaeftigungsart: string | null = this.daten.beschaeftigungsart;
		const setBeschaeftigungsart: JavaSet<string> = java_util_Set_of("WV", "WT");
		const fehlertext3: string | null = "Ist bei einer Lehrkraft im Feld 'Pflichtstundensoll' der Wert = 0.00 eingetragen, so muss das Feld 'Einsatzstatus' den Schlüssel 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' oder die 'Beschäftigungsart' den Schlüssel 'Beamte auf Widerruf (LAA) in Vollzeit' bzw. 'Beamte auf Widerruf (LAA) in Teilzeit' aufweisen.";
		if (!this.exec(3, { getAsBoolean: () => (pflichtstundensoll === 0) && (!JavaObject.equalsTranspiler(LehrerEinsatzstatus.A, (einsatzstatus)) && !setBeschaeftigungsart.contains(beschaeftigungsart)) }, fehlertext3))
			success = false;
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll>('de.svws_nrw.asd.validate.lehrer.ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll(obj: unknown): ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll {
	return obj as ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll;
}
