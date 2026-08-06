import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { SchuelerFoerderempfehlung } from "@core";

type StringNullableProps<T> = {
	[K in keyof T]: T[K] extends string | null ? K : never
}[keyof T];

export class SchuelerFoerderempfehlungModelProxy extends ModelProxy<SchuelerFoerderempfehlung> {


	/**
	 * Modelproxy für Förderempfehlungen
	 *
	 * @param data Lambda für den Zugriff auf die Originaldaten
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	public constructor(
		data: () => SchuelerFoerderempfehlung,
		patch?: (data: Partial<SchuelerFoerderempfehlung>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerFoerderempfehlung> = ["datumUmsetzungVon", "datumUmsetzungBis", "datumUeberpruefung",
			"datumNaechstesBeratungsgespraech", "abgeschlossen", "eingabeFertig"];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren();
		this.validate();
	}

	public addValidatoren() {

		// Angelegt am
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.datumAngelegt), "datumAngelegt");

		// Betroffene Fächer
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.faecher), "faecher");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.faecher, null, 255), "faecher");

		// Alle Freitext-Felder dürfen keine führenden/nachgestellten Leerzeichen enthalten
		this.addPatternValidatoren(StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES,
			"faecher",
			"diagnoseKompetenzenInhaltlichProzessbezogen", "diagnoseKompetenzenMethodisch", "diagnoseLernUndArbeitsverhalten",
			"massnahmeLernArbeitsverhalten", "massnahmeKompetenzenMethodische", "massnahmeKompetenzenInhaltlichProzessbezogen",
			"verantwortlichkeitEltern", "verantwortlichkeitSchueler");
	}

	/**
	 * Fügt für jedes übergebene Attribut einen {@link ValidatorStringMatchesPattern} mit dem angegebenen Muster hinzu.
	 *
	 * @param pattern   das zu prüfende String-Muster
	 * @param props     die Attribute, für die der Validator registriert werden soll
	 */
	private addPatternValidatoren(pattern: StringPattern, ...props: Array<StringNullableProps<SchuelerFoerderempfehlung>>): void {
		for (const prop of props) {
			this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy[prop], pattern), prop);
		}
	}

}
