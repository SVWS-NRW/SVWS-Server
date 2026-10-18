import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob das Enddatum eines Lernabschnitts nicht vor dem Anfangsdatum liegt.
 */
export class ValidatorSchuelerLernabschnittDatumVonBis extends BasicValidator {

	/** Eine Funktion, um auf das Anfangsdatum zugreifen zu können. */
	private readonly getDatumVon: () => string | null;

	/** Eine Funktion, um auf das Enddatum zugreifen zu können. */
	private readonly getDatumBis: () => string | null;

	/**
	 * Erzeugt einen neuen Validator
	 *
	 * @param datumVon   die Funktion zum Zugriff auf das Anfangsdatum
	 * @param datumBis   die Funktion zum Zugriff auf das Enddatum
	 */
	constructor(datumVon: () => string | null, datumBis: () => string | null) {
		super(ValidatorFehlerart.MUSS);
		this.getDatumVon = datumVon;
		this.getDatumBis = datumBis;
	}

	/**
	 * Prüft, ob das Enddatum nicht vor dem Anfangsdatum liegt. Ist eines der beiden Daten nicht gesetzt, so ist die Prüfung erfolgreich.
	 *
	 * @returns true, wenn die chronologische Reihenfolge eingehalten ist
	 */
	protected pruefe(): boolean {
		const von = this.getDatumVon();
		const bis = this.getDatumBis();

		if ((von === null) || (von === "") || (bis === null) || (bis === "")) {
			return true;
		}

		if (bis < von) {
			this.addFehler(0, `Das Enddatum ${this.formatDate(bis)} darf nicht vor dem Anfangsdatum ${this.formatDate(von)} liegen.`);
			return false;
		}
		return true;
	}

	private formatDate(date: string) {
		const [year, month, day] = date.split("-");
		return `${day}.${month}.${year}`;
	}
}
