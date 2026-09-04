import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { EinwilligungsartenListeManager } from "@ui/ui/manager/kataloge/EinwilligungsartenListeManager";

/**
 * Prüft, ob die Bezeichnung in einer Liste pro PersonTyp eindeutig ist.
 */
export class ValidatorEinwilligungsartBezeichnungIsUniqueInList extends BasicValidator {

	private readonly bezeichnung: () => string;
	private readonly manager: () => EinwilligungsartenListeManager;

	/**
	 * Prüft, ob die Bezeichnung in einer Liste pro PersonTyp eindeutig ist.
	 *
	 * @param bezeichnung	Bezeichnung
	 * @param manager		Manager
	 */
	constructor(bezeichnung: () => string, manager: () => EinwilligungsartenListeManager) {
		super(ValidatorFehlerart.MUSS);
		this.bezeichnung = bezeichnung;
		this.manager = manager;
	}

	/**
	 * Prüft die Eindeutigkeit des Strings.
	 *
	 * @returns true, wenn der Wert pro PersonTyp eindeutig ist
	 */
	protected pruefe(): boolean {
		if (this.manager().daten().idPersonTyp === -1) {
			return true;
		}
		for (const einwilligungsart of this.manager().liste.list()) {
			if ((einwilligungsart.id !== this.manager().daten().id)
					&& (einwilligungsart.idPersonTyp === this.manager().daten().idPersonTyp)
					&& (einwilligungsart.bezeichnung.toLowerCase() === this.bezeichnung().toLowerCase())) {
				this.addFehler(0, "Der Wert ist bereits vergeben.");
				return false;
			}
		}
		return true;
	}

}
