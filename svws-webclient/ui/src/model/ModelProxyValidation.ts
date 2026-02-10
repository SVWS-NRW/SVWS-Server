import { shallowRef } from "vue";
import type { List } from "../../../core/src/java/util/List";
import type { ValidatorFehler } from "../../../core/src/asd/validate/ValidatorFehler";
import { ArrayList } from "../../../core/src/java/util/ArrayList";
import type { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";

/**
 * Eine interne Klasse für den Fehlerzustand der Validierung
 */
class ModelProxyValidationResult<T extends object> {

	/** Die Listen der Fehler bei den Validierungen für die einzelnen Props */
	props = new Map<keyof T, List<ValidatorFehler>>();

	/** Die Gesamtmenge der Fehler aller Props */
	all = new ArrayList<ValidatorFehler>();

}

/**
 * Diese Klasse handhabt die Validierung der einzelnen Attribute für einen ModelProxy.
 */
export class ModelProxyValidation<T extends object> {

	/** Gibt an, ob eine automatische Revalidierung ausgeführt wird, wenn ein neuer Validator hinzugefügt wird. */
	private readonly autoRevalidate: boolean;

	/** Ein Zuordnung einer Prop zu einem Validator, welcher die Validatorfehler zugewiesen werden. */
	private readonly mapPropByValidator = new Map<BasicValidator, keyof T>();

	/** Ein Zuordnung der Props, welche bei Ännderung das ausführen des Validator anstoßen. */
	private readonly mapPropsByValidator = new Map<BasicValidator, Set<keyof T>>();

	/** Das Ergebnis der Validierieung mit den Fehlerlisten */
	private readonly result = shallowRef(new ModelProxyValidationResult<T>());

	/** Die Menge an props, bei denen die Validierung deaktiviert wurde */
	private readonly setOfDisabledPropValidations = new Set<keyof T>();


	/**
	 * Der Konstruktor zur Initialisierung der Validierung für das Model-Proxy
	 *
	 * @param autoRevalidate                  gibt an, ob eine automatische Revalidierung ausgeführt wird, wenn ein neuer
	 *                                        Validator hinzugefügt wird
	 * @param listOfDisabledPropValidations   eine Menge von Props, wo die Validierung ausgeschaltet wird
	 */
	public constructor(autoRevalidate: boolean, listOfDisabledPropValidations: Iterable<keyof T>) {
		this.autoRevalidate = autoRevalidate;
		for (const prop of listOfDisabledPropValidations) {
			this.setOfDisabledPropValidations.add(prop);
		}
	}

	/**
	 * Fügt einen neuen Validator zur automatischen Validierung bei allen angegebenen Attributen hinzu.
	 * Die einzelnen Fehler werden dabei der ersten Prop in der Liste zugeordnet.
	 *
	 * @param validator   der hinzuzufügende Validator
	 * @param prop        das Attribut des Proxies, bei welchem der Validator ausgeführt werden soll und welchem die
	 *                    Validatorfehler zugeordnet werden.
	 * @param props       die zusätzlichen Attribute des Proxies, bei welchen der Validator ausgeführt werden soll.
	 */
	public addValidator(validator: BasicValidator, prop: keyof T, ...props: Array<keyof T>): void {
		if (this.mapPropByValidator.has(validator)) {
			throw new Error("Ein Validator sollte nur einmalig zu der Konfiguration hinzugefügt werden. Bitte fassen sie die Aufrufe zusammen.");
		}
		this.mapPropByValidator.set(validator, prop);
		this.mapPropsByValidator.set(validator, new Set<keyof T>([prop, ...props]));
		if (this.autoRevalidate) {
			this.validate();
		}
	}

	/**
	 * Gibt alle Validatoren in der Reihenfolge zurück, wie sie eingefügt wurden.
	 *
	 * @returns ein Iterator über die Validatoren
	 */
	private get validators(): MapIterator<BasicValidator> {
		return this.mapPropByValidator.keys();
	}

	/**
	 * Gibt zurück, ob der Validator bei dem übergebenen Attribut zur Validierung eingetragen ist
	 * oder nicht.
	 *
	 * @param prop        das Attribut
	 * @param validator   der Validator
	 *
	 * @returns true, wenn der Validator den Attributwert validiert, und ansonsten false
	 */
	private isValidatorForProp(prop: keyof T, validator: BasicValidator): boolean {
		const props = this.mapPropsByValidator.get(validator) ?? new Set<keyof T>();
		return props.has(prop);
	}

	/**
	 * Gibt die Menge der Attribute zurück, welche dem übergebenen Validator zugeordnet sind.
	 *
	 * @param validator   der Validator
	 *
	 * @returns die Menge der zugeordneten Attribute
	 */
	private getPropsForValidator(validator: BasicValidator): ReadonlySet<keyof T> {
		return this.mapPropsByValidator.get(validator) ?? new Set<keyof T>;
	}

	/**
	 * Gibt die Prop zurück, welcher die Validator-Fehler zugeordnet werden
	 *
	 * @param validator   der Validator
	 *
	 * @returns das Attribut oder null bei einem internen Fehler
	 */
	private getPropForValidatorResults(validator: BasicValidator): keyof T | null {
		return this.mapPropByValidator.get(validator) ?? null;
	}

	/**
	 * Prüft, ob es mindestens ein Attribut bei dem Validator gibt, für welches die
	 * Validierung nicht deaktiviert wurde.
	 *
	 * @param validator   der zu prüfuende Validator
	 *
	 * @returns true, wenn der Validator irgendwo aktiv ist, und ansonsten false
	 */
	private checkValidatorEnabled(validator: BasicValidator): boolean {
		for (const prop of this.getPropsForValidator(validator)) {
			if (!this.setOfDisabledPropValidations.has(prop)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Führt die Validierung aus und aktualisiert die Fehlerlisten.
	 *
	 * @param propFilter   falls hier ein Attribut angegeben wird, so wird die Validierung eingeschränkt auf die Validatoren
	 *                     dieses Attributes augeführt
	 */
	public validate(propFilter?: keyof T): void {
		// Erstelle alle Ergebnislisten neu, da diese später neu zusammengestellt werden
		const result = new ModelProxyValidationResult<T>();

		// Durchwandere die Liste aller Validatoren
		for (const validator of this.validators) {
			// Prüfe, ob der Validator überhaupt eine Prop hat, wo die Prüfung nicht deaktiviert ist
			const enabled = this.checkValidatorEnabled(validator);
			if (!enabled) {
				continue;
			}

			// Prüfe, ob der Validator ausgeführt werden soll - ist nötig, falls gefiltert wird
			if ((propFilter === undefined) || this.isValidatorForProp(propFilter, validator)) {
				validator.run();
			}

			// Bestimme die Prop, welcher die Validierungsergebnisse zugeordnet werden
			const prop = this.getPropForValidatorResults(validator);

			// Ignoriere alle props, bei denen die Validierung deaktiviert wurde - diese erhalten keine Fehlermeldung, selbst wenn die Validierung ausgeführt wurde
			if ((prop === null) || this.setOfDisabledPropValidations.has(prop)) {
				continue;
			}

			// Sammle die Fehler für Fehlerliste der Prop
			let fehlerListe = result.props.get(prop);
			if (fehlerListe === undefined) {
				fehlerListe = new ArrayList<ValidatorFehler>();
				result.props.set(prop, fehlerListe);
			}
			fehlerListe.addAll(validator.getFehler());

			// Füge alle Fehler der aktiven Validatoren zu der Gesamtfehlerliste hinzu
			result.all.addAll(validator.getFehler());
		}

		// Setze die Fehlerlisten neu, um die Reaktivität bei Zugriff auf die Validierungsergebnisse zu gewährleisten
		this.result.value = result;
	}


	/**
	 * Gibt alle Fehler zurück, welche bei der Validierung aufgetreten sind.
	 *
	 * @param prop   das Attribut, für welches die Fehlerliste erzeugt werden soll
	 *
	 * @returns die Fehlerliste für das Attribut
	 */
	public getFehler(prop: keyof T): List<ValidatorFehler> {
		const fehlerListe = this.result.value.props.get(prop);
		if (fehlerListe === undefined) {
			return new ArrayList<ValidatorFehler>();
		}
		return fehlerListe;
	}

	/**
	 * Gibt alle Fehler zurück, die bei den Validierungen aller Attribute aufgetreten sind.
	 *
	 * @returns die Fehlerliste mit allen Fehlern in Bezug auf das Proxy-DTO
	 */
	public getAlleFehler(): List<ValidatorFehler> {
		return this.result.value.all;
	}

	/**
	 * Aktiviert die Validierung für das übergebene Attribut.
	 *
	 * @param prop   das Attribut
	 */
	public enable(prop: keyof T): void {
		const removed = this.setOfDisabledPropValidations.delete(prop);
		if (removed) {
			this.validate(prop);
		}
	}

	/**
	 * Dektiviert die Validierung für das übergebene Attribut.
	 *
	 * @param prop   das Attribut
	 */
	public disable(prop: keyof T): void {
		if (!this.setOfDisabledPropValidations.has(prop)) {
			this.setOfDisabledPropValidations.add(prop);
			this.validate(prop);
		}
	}

	/**
	 * Wechselt den Status für das übergebene Attribut, ob eine Valididerung
	 * stattfindet oder nicht.
	 *
	 * @param prop   das Attribut
	 */
	public toggle(prop: keyof T): void {
		if (this.setOfDisabledPropValidations.has(prop)) {
			this.setOfDisabledPropValidations.delete(prop);
		} else {
			this.setOfDisabledPropValidations.add(prop);
		}
		this.validate(prop);
	}

}