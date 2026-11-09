import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Das aktuelle Schuljahr des Lehrers
	 */
	private readonly _schuljahr: Supplier<number>;

	/**
	 * Der Einsatzstatus
	 */
	private readonly _idRechtsverhaeltnis: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator für das vorhandensein des Einsatzstatus im Katalog.
	 *
	 * @param schuljahr                Schuljahr
	 * @param idStaatsangehoerigkeit   die Staatsangehoerigkeit
	 * @param idRechtsverhaeltnis      das Rechtsverhaeltnis
	 * @param kontext                  der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, idStaatsangehoerigkeit: Supplier<number | null>, idRechtsverhaeltnis: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._idRechtsverhaeltnis = idRechtsverhaeltnis;
		const rechtsverhaeltnisNotNull: Supplier<LehrerRechtsverhaeltnis> = { get: () => LehrerRechtsverhaeltnis.data().getWertByID(this.getNotNullSupplierLong(idRechtsverhaeltnis).get()) };
		const staatsangehoerigkeitSchluessel: Supplier<string> = this.getNotNullSupplier({ get: () => Nationalitaeten.data().getSchluesselByIDOrNull(idStaatsangehoerigkeit.get()) });
		this._validatoren.add(new ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(staatsangehoerigkeitSchluessel, rechtsverhaeltnisNotNull, kontext));
	}

	public pruefe(): boolean {
		if (!LehrerRechtsverhaeltnis.data().isGueltig(this._idRechtsverhaeltnis.get(), this._schuljahr.get())) {
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
