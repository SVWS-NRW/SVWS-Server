import type { KlassenDaten } from "@core";
import { ModelProxy, ValidatorKlassenKuerzel, ValidatorStringLength } from "@ui";

/**
 * Der spezielle ModelProxy für die Klassen-Daten
 */
export class KlassenModelProxy extends ModelProxy<KlassenDaten> {

	/**
	 * Erstellt einen validierenden Proxy für das Core-DTO KlassenDaten.
	 *
	 * @param data               ein Lambda für den Zugriff auf die "Original"-Daten
	 * @param vorhanden          die vorhandenen Klassen
	 * @param patchMethod        ggf. die Methode zum Patchen der einzelnen Attribute, sofern das automatische Patchen
	 *                           bei Änderungen gewünscht ist
	 */
	constructor(data: () => KlassenDaten, vorhanden: () => Iterable<KlassenDaten>, patchMethod?: (data: Partial<KlassenDaten>) => Promise<boolean>) {
		super({ data: data, patch: patchMethod });
		this.addValidator(new ValidatorKlassenKuerzel(() => this.proxy.kuerzel ?? null, vorhanden), "kuerzel");
		this.addValidator(new ValidatorStringLength(() => this.proxy.beschreibung, 150, 1), "beschreibung");
		this.validate();
	}

}
