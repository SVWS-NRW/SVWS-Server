import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { NationalitaetenKatalogEintrag } from '../../../asd/data/schule/NationalitaetenKatalogEintrag';
import { Nationalitaeten } from '../../../asd/types/schule/Nationalitaeten';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly _staatsangehoerigkeitSchluessel: Supplier<string>;

	private readonly _rechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis | null>;

	private static readonly setRechtsverhaeltnis: JavaSet<LehrerRechtsverhaeltnis> = java_util_Set_of(LehrerRechtsverhaeltnis.L, LehrerRechtsverhaeltnis.N, LehrerRechtsverhaeltnis.P, LehrerRechtsverhaeltnis.W);

	private static readonly setStaatsangehoerigkeit: JavaSet<string> = java_util_Set_of("DEU", "BEL", "BGR", "DNK", "EST", "FIN", "FRA", "HRV", "SVN", "GRC", "IRL", "ISL", "ITA", "LVA", "LIE", "LTU", "LUX", "MLT", "NLD", "NOR", "AUT", "POL", "PRT", "ROU", "SVK", "SWE", "CHE", "ESP", "CZE", "HUN", "GBR", "CYP");


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param staatsangehoerigkeitSchluessel   der staatsangehoerigkeitSchluessel des Lehrers
	 * @param rechtsverhaeltnis                das Rechtsverhältnis des Lehrers
	 * @param kontext                          der Kontext des Validators
	 */
	public constructor(staatsangehoerigkeitSchluessel: Supplier<string>, rechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._staatsangehoerigkeitSchluessel = staatsangehoerigkeitSchluessel;
		this._rechtsverhaeltnis = rechtsverhaeltnis;
	}

	protected pruefe(): boolean {
		if (this._rechtsverhaeltnis.get() === null) {
			return true;
		}
		const schuljahr: number = this.kontext().getSchuljahr();
		const schulform: Schulform | null = this.kontext().getSchulform();
		const nationalitaet: Nationalitaeten | null = Nationalitaeten.data().getBySchuljahrAndSchulformAndSchluessel(schuljahr, schulform, this._staatsangehoerigkeitSchluessel.get());
		if ((nationalitaet !== null)) {
			const katalogEintrag: NationalitaetenKatalogEintrag | null = nationalitaet.daten(schuljahr);
			if (ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis.setRechtsverhaeltnis.contains(this._rechtsverhaeltnis.get()) && (katalogEintrag !== null) && !ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis.setStaatsangehoerigkeit.contains(katalogEintrag.iso3)) {
				this.addFehler(0, "Zu dieser verbeamteten Lehrkraft ist die Staatsangehörigkeit '" + this._staatsangehoerigkeitSchluessel.get() + "' angegeben. Dabei handelt es sich jedoch nicht um eine Staatsangehörigkeit eines Mitgliedsstaats der Europäischen Union (EU) oder des Europäischen Wirtschaftsraums (EWR). Die vorgenommene Eintragung kann nur in Ausnahmefällen korrekt sein. Für Lehrkräfte, die neben einer ausländischen Staatsangehörigkeit auch die deutsche Staatsangehörigkeit besitzen, erfassen Sie bitte die Staatsangehörigkeit 'deutsch'. ");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLppr14LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
