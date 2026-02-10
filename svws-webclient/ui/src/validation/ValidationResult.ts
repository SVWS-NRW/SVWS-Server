import type { List } from "../../../../svws-webclient/core/src/java/util/List";
import type { ValidatorFehler } from "../../../../svws-webclient/core/src/asd/validate/ValidatorFehler";
import { ValidatorFehlerart } from "../../../../svws-webclient/core/src/asd/validate/ValidatorFehlerart";

/**
 * Diese Klasse repräsentiert das Ergebnis einer Validierung, welche in Form einer Liste von Validator-Fehler-Objekten
 * vorliegt.
 */
export class ValidationResult {

	/** Die Liste der Fehler bei der Validierug durch einen oder mehrerere Validatoren */
	private readonly _fehler: List<ValidatorFehler>;

	/** Die "schlimmste" Fehlerart, welche bei den Validierungsfehlern auftaucht. */
	private readonly _fehlerart: ValidatorFehlerart;


	/**
	 * Erzeugt ein neues Validierungs-Ergebnis anhand der übergebenen Fehlerliste.
	 *
	 * @param fehler   die Fehlerliste
	 */
	public constructor(fehler: List<ValidatorFehler>) {
		this._fehler = fehler;
		this._fehlerart = ValidationResult.determineFehlerart(this._fehler);
	}

	/**
	 * Diese Methode bestimmt anhand der übergebenen Liste von Fehler die "schlimmste" vorkommende Fehlerart
	 * und gibt diese zurück.
	 *
	 * @param fehlerListe   die Liste von Validierungs-Fehlern
	 *
	 * @returns die "schlimmste" in der Liste vorkommende Fehlerart
	 */
	private static determineFehlerart(fehlerListe: List<ValidatorFehler>): ValidatorFehlerart {
		let highestFehlerart = ValidatorFehlerart.UNGENUTZT;
		for (const fehler of fehlerListe) {
			const fehlerart = fehler.getFehlerart();
			if (fehlerart.ordinal() < highestFehlerart.ordinal()) {
				highestFehlerart = fehlerart;
			}
		}
		return highestFehlerart;
	}


	/**
	 * Gibt die Liste der Fehler zurück.
	 *
	 * @returns die Liste der Fehler
	 */
	get fehler(): List<ValidatorFehler> {
		return this._fehler;
	}

	/**
	 * Gibt die "schlimmste" in der Fehlerliste vorkommende Fehlerart zurück.
	 *
	 * @returns die "schlimmste" in der Liste vorkommende Fehlerart
	 */
	get fehlerart(): ValidatorFehlerart {
		return this._fehlerart;
	}


	/**
	 * Gibt zurück, ob überhaupt ein Fehler in dem Ergebnis der Validierung vorkommt oder nicht.
	 *
	 * @returns true, wenn ein Fehler in dem Ergebnis vorhanden ist und ansonsten false
	 */
	get hasFehler(): boolean {
		return !this._fehler.isEmpty();
	}

}
