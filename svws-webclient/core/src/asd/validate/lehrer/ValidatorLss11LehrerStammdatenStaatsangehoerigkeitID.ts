import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: Supplier<string>;

	private readonly rechtsverhaeltnis: Supplier<string>;

	private static readonly setRechtsverhaeltnis: JavaSet<string> = java_util_Set_of("L", "N", "P", "W");

	private static readonly setStaatsangehoerigkeit: JavaSet<string> = java_util_Set_of("DEU", "BEL", "BGR", "DNK", "EST", "FIN", "FRA", "HRV", "SVN", "GRC", "IRL", "ISL", "ITA", "LVA", "LIE", "LTU", "LUX", "MLT", "NLD", "NOR", "AUT", "POL", "PRT", "ROU", "SVK", "SWE", "CHE", "ESP", "CZE", "HUN", "GBR", "CYP");

	private static readonly FEHLERTEXT: string = "Zu dieser verbeamteten Lehrkraft ist die Staatsangehörigkeit '\" + LehrerStammdaten.staatsangehoerigkeitID + \"' angegeben. Dabei handelt es sich jedoch nicht um eine Staatsangehörigkeit eines Mitgliedsstaats der Europäischen Union (EU) oder des Europäischen Wirtschaftsraums (EWR). Die vorgenommene Eintragung kann nur in Ausnahmefällen korrekt sein. Für Lehrkräfte, die neben einer ausländischen Staatsangehörigkeit auch die deutsche Staatsangehörigkeit besitzen, erfassen Sie bitte die Staatsangehörigkeit 'deutsch'. ";


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten              die StaatsangehoerigkeitID des Lehrers
	 * @param rechtsverhaeltnis  das Rechtsverhaeltnis des Lehrers
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, rechtsverhaeltnis: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this.rechtsverhaeltnis = rechtsverhaeltnis;
	}

	protected pruefe(): boolean {
		if (ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID.setRechtsverhaeltnis.contains(this.rechtsverhaeltnis.get()) && !ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID.setStaatsangehoerigkeit.contains(this.daten.get())) {
			this.addFehler(0, ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID.FEHLERTEXT);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID>('de.svws_nrw.asd.validate.lehrer.ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID(obj: unknown): ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID {
	return obj as ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID;
}
