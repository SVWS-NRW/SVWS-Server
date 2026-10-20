import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import type { EinwilligungsartenListeManager } from "@ui/ui/manager/kataloge/EinwilligungsartenListeManager";

/**
 * Prüft, ob die Bezeichnung in einer Liste pro PersonTyp eindeutig ist.
 */
export class ValidatorEinwilligungsartBezeichnungAndPersonTypIsUniqueInList extends BasicValidator {

	private readonly einwilligungsart: () => Einwilligungsart;
	private readonly manager: () => EinwilligungsartenListeManager;

	/**
	 * Prüft, ob die Bezeichnung in einer Liste pro PersonTyp eindeutig ist.
	 *
	 * @param einwilligungsart	Lambda für den zu prüfenden Eintrag (Proxy), unabhängig von der Auswahl im Manager
	 * @param manager			Manager
	 */
	constructor(einwilligungsart: () => Einwilligungsart, manager: () => EinwilligungsartenListeManager) {
		super(ValidatorFehlerart.MUSS);
		this.einwilligungsart = einwilligungsart;
		this.manager = manager;
	}

	/**
	 * Prüft die Eindeutigkeit des Strings.
	 *
	 * @returns true, wenn der Wert pro PersonTyp eindeutig ist
	 */
	protected pruefe(): boolean {
		const aktuell = this.einwilligungsart();
		if (aktuell.idPersonTyp === -1) {
			return true;
		}
		const bezeichnung = aktuell.bezeichnung.toLowerCase();
		for (const einwilligungsart of this.manager().liste.list()) {
			if ((einwilligungsart.id !== aktuell.id)
					&& (einwilligungsart.idPersonTyp === aktuell.idPersonTyp)
					&& (einwilligungsart.bezeichnung.toLowerCase() === bezeichnung)) {
				this.addFehler(0, "Der Wert ist bereits vergeben.");
				return false;
			}
		}
		return true;
	}

}
