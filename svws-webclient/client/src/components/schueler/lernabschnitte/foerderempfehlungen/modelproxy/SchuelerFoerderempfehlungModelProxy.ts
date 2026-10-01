import { computed } from "vue";

import type { SchuelerFoerderempfehlung } from "@core/asd/data/schueler/SchuelerFoerderempfehlung";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { List } from "@core/java/util/List";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

type StringNullableProps<T> = {
	[K in keyof T]: T[K] extends string | null ? K : never
}[keyof T];

export class SchuelerFoerderempfehlungModelProxy extends ModelProxy<SchuelerFoerderempfehlung> {

	private readonly _lehrer: () => List<LehrerListeEintrag>;

	/**
	 * Modelproxy für Förderempfehlungen
	 *
	 * @param data Lambda für den Zugriff auf die Originaldaten
	 * @param lehrer Lambda für den Zugriff auf die Lehrer
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	public constructor(
		data: () => SchuelerFoerderempfehlung,
		lehrer: () => List<LehrerListeEintrag>,
		patch?: (data: Partial<SchuelerFoerderempfehlung>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerFoerderempfehlung> = ["datumUmsetzungVon", "datumUmsetzungBis", "datumUeberpruefung",
			"datumNaechstesBeratungsgespraech", "abgeschlossen", "eingabeFertig", "idLehrer"];
		super({ data, patch, listOfAutopatchProps });
		this._lehrer = lehrer;
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

	lehrkraft = computed<LehrerListeEintrag | null>({
		get: () => this._lehrerMap.value.get(this.proxy.idLehrer ?? -1) ?? null,
		set: (v: LehrerListeEintrag | null) => this.proxy.idLehrer = v?.id ?? null,
	});

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

	private readonly _lehrerMap = computed<Map<number, LehrerListeEintrag>>(() => {
		const map = new Map<number, LehrerListeEintrag>();
		for (const l of this._lehrer()) {
			map.set(l.id, l);
		}
		return map;
	});

}
