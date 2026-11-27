import { BasicValidator } from '@core/asd/validate/BasicValidator';
import { ValidatorFehlerart } from '@core/asd/validate/ValidatorFehlerart';
import type { UvFach } from '@core/core/data/uv/UvFach';
import { DateUtils } from '@core/core/utils/DateUtils';
import type { UvManager } from '@core/core/utils/uv/UvManager';

/** Validiert einen Gültigkeitszeitraum und optional dessen Überschneidung mit vorhandenen Einträgen. */
export class UvGueltigkeitValidator extends BasicValidator {

	constructor(private readonly gueltigVon: () => string | null, private readonly gueltigBis: () => string | null,
		private readonly hatKonflikt: () => boolean = () => false) {
		super(ValidatorFehlerart.MUSS);
	}

	protected pruefe(): boolean {
		const von = this.gueltigVon();
		const bis = this.gueltigBis();
		if (von === null || !DateUtils.isValidDate(von)) {
			this.addFehler(0, 'Bitte einen gültigen Gültigkeitsbeginn angeben.');
			return false;
		}
		if (bis !== null && !DateUtils.isValidDate(bis)) {
			this.addFehler(1, 'Bitte ein gültiges Gültigkeitsende angeben oder das Feld leeren.');
			return false;
		}
		if (bis !== null && bis < von) {
			this.addFehler(2, 'Das Gültigkeitsende darf nicht vor dem Gültigkeitsbeginn liegen.');
			return false;
		}
		if (this.hatKonflikt()) {
			this.addFehler(3, 'Der Gültigkeitszeitraum überschneidet sich mit einem bestehenden Eintrag.');
			return false;
		}
		return true;
	}
}

/** Verwendet die zentrale Fach-Konfliktprüfung für Eingabefelder und Speicheraktionen. */
export function uvFachGueltigkeitValidator(manager: UvManager, fach: UvFach,
	gueltigVon: () => string | null = () => fach.gueltigVon, gueltigBis: () => string | null = () => fach.gueltigBis): UvGueltigkeitValidator {
	return new UvGueltigkeitValidator(gueltigVon, gueltigBis, () => {
		const von = gueltigVon();
		return von !== null && manager.fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(fach, von, gueltigBis()) !== null;
	});
}
