import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart extends Validator {

	/**
	 * Das Rechtsverhältnis
	 */
	private readonly _rechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis>;

	/**
	 * Die Beschäftigungs
	 */
	private readonly _beschaeftigungsart: Supplier<LehrerBeschaeftigungsart>;

	private static readonly setBeschaeftigungsart: JavaSet<LehrerBeschaeftigungsart> = java_util_Set_of(LehrerBeschaeftigungsart.G);


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerRechtsverhaeltnis    das Rechtsverhältnis
	 * @param lehrerBeschaeftigungsart   die Beschäftigungsart
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(lehrerRechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis>, lehrerBeschaeftigungsart: Supplier<LehrerBeschaeftigungsart>, kontext: ValidatorKontext) {
		super(kontext);
		this._rechtsverhaeltnis = lehrerRechtsverhaeltnis;
		this._beschaeftigungsart = lehrerBeschaeftigungsart;
	}

	protected pruefe(): boolean {
		if (JavaObject.equalsTranspiler(this._rechtsverhaeltnis.get(), (LehrerRechtsverhaeltnis.S)) && !ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart.setBeschaeftigungsart.contains(this._beschaeftigungsart.get())) {
			this.addFehler(0, "Für das Rechtsverhältnis 'Gestellungsvertrag' ist die Beschätigungsart 'Gestellungsvertrag' zulässig.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart>('de.svws_nrw.asd.validate.lehrer.ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(obj: unknown): ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart {
	return obj as ValidatorLpprb15LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart;
}
