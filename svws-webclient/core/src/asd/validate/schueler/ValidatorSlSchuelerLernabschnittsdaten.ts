import { ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung } from '../../../asd/validate/schueler/ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung';
import { ValidatorSleSchuelerLernabschnittsdatenEpJahre } from '../../../asd/validate/schueler/ValidatorSleSchuelerLernabschnittsdatenEpJahre';
import { ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt } from '../../../asd/validate/schueler/ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt';
import { ValidatorSlobSchuelerLernabschnittsdatenOrganisationsformBerufsbildend } from '../../../asd/validate/schueler/ValidatorSlobSchuelerLernabschnittsdatenOrganisationsformBerufsbildend';
import { ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend } from '../../../asd/validate/schueler/ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend';
import { ValidatorSlowSchuelerLernabschnittsdatenOrganisationsformWeiterbildung } from '../../../asd/validate/schueler/ValidatorSlowSchuelerLernabschnittsdatenOrganisationsformWeiterbildung';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorSlkSchuelerLernabschnittsdatenKlassenart } from '../../../asd/validate/schueler/ValidatorSlkSchuelerLernabschnittsdatenKlassenart';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorSlbSchuelerLernabschnittsdatenFachklassen } from '../../../asd/validate/schueler/ValidatorSlbSchuelerLernabschnittsdatenFachklassen';

export class ValidatorSlSchuelerLernabschnittsdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idOrganisationsform             die ID der Organisationsform
	 * @param idSchulgliederung               die ID der Schulgliederung
	 * @param idFachklasse                    die ID der Fachklasse
	 * @param hatSchwerbehinderungsNachweis   der Schwerbehinderungsnachweis
	 * @param idFoerderschwerpunkt1           die ID des ersten Förderschwerpunktes
	 * @param idFoerderschwerpunkt2           die ID des zweiten Förderschwerpunktes
	 * @param idSchuljahresabschnitt          die ID des Schuljahresabschnitts
	 * @param idKlassenart                    die Klassenart ID
	 * @param idEpJahre                       die EPJahre ID
	 * @param kontext                         der Kontext des Validators
	 */
	public constructor(idOrganisationsform: Supplier<number | null>, idSchulgliederung: Supplier<number | null>, idFachklasse: Supplier<number | null>, hatSchwerbehinderungsNachweis: Supplier<boolean | null>, idFoerderschwerpunkt1: Supplier<number | null>, idFoerderschwerpunkt2: Supplier<number | null>, idSchuljahresabschnitt: Supplier<number | null>, idKlassenart: Supplier<number | null>, idEpJahre: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSlbSchuelerLernabschnittsdatenFachklassen(idSchuljahresabschnitt, idFachklasse, kontext));
		this._validatoren.add(new ValidatorSleSchuelerLernabschnittsdatenEpJahre(idSchuljahresabschnitt, idEpJahre, kontext));
		this._validatoren.add(new ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt(hatSchwerbehinderungsNachweis, idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
		this._validatoren.add(new ValidatorSlkSchuelerLernabschnittsdatenKlassenart(idSchuljahresabschnitt, idKlassenart, kontext));
		this._validatoren.add(new ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend(idSchuljahresabschnitt, idOrganisationsform, kontext));
		this._validatoren.add(new ValidatorSlobSchuelerLernabschnittsdatenOrganisationsformBerufsbildend(idSchuljahresabschnitt, idOrganisationsform, kontext));
		this._validatoren.add(new ValidatorSlowSchuelerLernabschnittsdatenOrganisationsformWeiterbildung(idSchuljahresabschnitt, idOrganisationsform, kontext));
		this._validatoren.add(new ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung(idSchuljahresabschnitt, idSchulgliederung, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlSchuelerLernabschnittsdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlSchuelerLernabschnittsdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlSchuelerLernabschnittsdaten>('de.svws_nrw.asd.validate.schueler.ValidatorSlSchuelerLernabschnittsdaten');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlSchuelerLernabschnittsdaten(obj: unknown): ValidatorSlSchuelerLernabschnittsdaten {
	return obj as ValidatorSlSchuelerLernabschnittsdaten;
}
