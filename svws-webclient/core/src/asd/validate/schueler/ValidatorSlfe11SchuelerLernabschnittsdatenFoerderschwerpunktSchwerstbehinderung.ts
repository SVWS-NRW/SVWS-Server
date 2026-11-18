import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung extends Validator {

	private readonly idFoerderschwerpunkt1: Supplier<number | null>;

	private readonly hatSchwerbehinderungsNachweis: Supplier<boolean | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idFoerderschwerpunkt1           die ID des ersten Förderschwerpunkts
	 * @param hatSchwerbehinderungsNachweis   Schwerbehinderungsnachweis vorhanden
	 * @param kontext                         der Kontext des Validators
	 */
	public constructor(idFoerderschwerpunkt1: Supplier<number | null>, hatSchwerbehinderungsNachweis: Supplier<boolean | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
		this.hatSchwerbehinderungsNachweis = hatSchwerbehinderungsNachweis;
	}

	protected pruefe(): boolean {
		const idFoerderschwerpunkt1: number | null = this.idFoerderschwerpunkt1.get();
		const hatSchwerbehinderungsNachweis: boolean | null = this.hatSchwerbehinderungsNachweis.get();
		if (hatSchwerbehinderungsNachweis !== null && hatSchwerbehinderungsNachweis && idFoerderschwerpunkt1 === null) {
			this.addFehler(0, "Wenn eine Schwerstbehinderung vorliegt, muss das Feld 'Förderschwerpunkt' ebenfalls besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung>('de.svws_nrw.asd.validate.schueler.ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung(obj: unknown): ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung {
	return obj as ValidatorSlfe11SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung;
}
