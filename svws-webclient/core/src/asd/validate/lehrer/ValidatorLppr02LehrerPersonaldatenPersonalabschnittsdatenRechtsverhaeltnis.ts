import { ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { ValidatorLppr12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { DateManager } from '../../../asd/validate/DateManager';
import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import { ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Der Einsatzstatus
	 */
	private readonly _idRechtsverhaeltnis: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator für das vorhandensein des Einsatzstatus im Katalog.
	 *
	 * @param idSchuljahresabschnitt   der Schuljahresabschnitt
	 * @param idStaatsangehoerigkeit   die Staatsangehoerigkeit
	 * @param idRechtsverhaeltnis      das Rechtsverhaeltnis
	 * @param geburtsdatum             das Geburtsdatum
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number>, idStaatsangehoerigkeit: Supplier<number | null>, idRechtsverhaeltnis: Supplier<number>, geburtsdatum: Supplier<DateManager>, kontext: ValidatorKontext) {
		super(kontext);
		this._idRechtsverhaeltnis = idRechtsverhaeltnis;
		const rechtsverhaeltnisNotNull: Supplier<LehrerRechtsverhaeltnis> = { get: () => LehrerRechtsverhaeltnis.data().getWertByID(this.getNotNullSupplierLong(idRechtsverhaeltnis).get()) };
		const staatsangehoerigkeitSchluessel: Supplier<string> = this.getNotNullSupplier({ get: () => Nationalitaeten.data().getSchluesselByIDOrNull(idStaatsangehoerigkeit.get()) });
		this._validatoren.add(new ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLppr11LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLppr12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(idSchuljahresabschnitt, rechtsverhaeltnisNotNull, geburtsdatum, kontext));
		this._validatoren.add(new ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(staatsangehoerigkeitSchluessel, rechtsverhaeltnisNotNull, kontext));
	}

	public pruefe(): boolean {
		if (!LehrerEinsatzstatus.data().isGueltig(this._idRechtsverhaeltnis.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Lehrer Rechtsverhältnis: Der eingetragene Wert für das Feld 'Rechtsverhältnis' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
