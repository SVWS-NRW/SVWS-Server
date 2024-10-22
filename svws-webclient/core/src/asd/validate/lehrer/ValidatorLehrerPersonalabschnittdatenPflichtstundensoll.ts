import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerPersonalabschnittdatenPflichtstundensoll extends Validator<LehrerPersonalabschnittsdaten> {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : LehrerPersonalabschnittsdaten, kontext : ValidatorKontext) {
		super(daten, kontext);
	}

	protected pruefe() : boolean {
		const pflichtstundensoll : number | null = this.daten().pflichtstundensoll;
		if (pflichtstundensoll === null) {
			this.addFehler("Kein Wert im Feld 'pflichtstundensoll'.");
			return false;
		}
		const success : boolean = true;
		if ((pflichtstundensoll <= 0.0) || (pflichtstundensoll > 41.0)) {
			this.addFehler("Unzulässiger Wert im Feld 'pflichtstundensoll'. Zulässig sind im Stundenmodell Werte im Bereich von 0,00 bis 41,00 Wochenstunden. Im Minutenmodell zwischen 0,00 und 1845,00 Minuten.");
		}
		const einsatzstatus : LehrerEinsatzstatus | null = LehrerEinsatzstatus.getBySchluessel(this.daten().einsatzstatus);
		if ((einsatzstatus as unknown === LehrerEinsatzstatus.B as unknown) && (pflichtstundensoll === 0.0))
			this.addFehler("Bei Lehrkräften, die von einer anderen Schule abgeordnet wurden (Einsatzstatus = 'B'), darf das Pflichtstundensoll nicht 0,00 betragen.");
		return success;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittdatenPflichtstundensoll';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittdatenPflichtstundensoll', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerPersonalabschnittdatenPflichtstundensoll>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittdatenPflichtstundensoll');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerPersonalabschnittdatenPflichtstundensoll(obj : unknown) : ValidatorLehrerPersonalabschnittdatenPflichtstundensoll {
	return obj as ValidatorLehrerPersonalabschnittdatenPflichtstundensoll;
}
