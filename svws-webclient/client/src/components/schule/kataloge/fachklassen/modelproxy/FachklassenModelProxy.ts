import type { FachklassenListeManager } from "@ui";
import { ValidatorStringIsUniqueInList, ModelProxy, StringPattern, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { FachklasseEintrag, FachklasseKatalogEintrag, SchulgliederungKatalogEintrag } from "@core";
import { Fachklasse, Schulgliederung } from "@core";
import { computed } from "vue";

export class FachklassenModelProxy extends ModelProxy<FachklasseEintrag> {

	private readonly manager: () => FachklassenListeManager;
	private readonly schuljahr: number;

	constructor(
		data: () => FachklasseEintrag,
		manager: () => FachklassenListeManager,
		schuljahr: number,
		patch?: (data: Partial<FachklasseEintrag>) => Promise<boolean>
	) {
		const autopatchProps: Iterable<keyof FachklasseEintrag> = ["istSichtbar", "idFachklasse"];
		super({ data, patch, listOfAutopatchProps: autopatchProps });
		this.manager = manager;
		this.schuljahr = schuljahr;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		// kuerzel
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.kuerzel, null, 100), 'kuerzel');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.kuerzel, StringPattern.NO_WHITESPACES), 'kuerzel');
		this.addBlockingValidator(new ValidatorStringIsUniqueInList(
			() => this.proxy,
			(data: FachklasseEintrag) => data.id,
			(data: FachklasseEintrag) => data.kuerzel,
			() => this.manager().liste.list(),
			false), 'kuerzel');
		// bezeichnung
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnung, null, 100), 'bezeichnung');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung), 'bezeichnung');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnung');

		this.addBlockingValidator(new ValidatorInputRequired((): number | null => this.proxy.idSchulgliederung), 'idSchulgliederung');
		this.addBlockingValidator(new ValidatorInputRequired((): number | null => this.proxy.idFachklasse), 'idFachklasse');

		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange((): number => this.proxy.sortierung, 0, 32000), "sortierung");
	}

	fachklasse = computed<FachklasseKatalogEintrag | null>({
		get: () => Fachklasse.data().getEintragByID(this.proxy.idFachklasse ?? -1),
		set: (v: FachklasseKatalogEintrag | null) => this.proxy.idFachklasse = v?.id ?? null,
	});

	schulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragByID(this.proxy.idSchulgliederung ?? -1),
		set: (v: SchulgliederungKatalogEintrag | null) => this.proxy.idSchulgliederung = v?.id ?? null,
	});

	bezeichnungSchulgliederung = computed<string>(() => {
		const eintrag = Schulgliederung.data().getEintragByID(this.proxy.idSchulgliederung ?? -1);
		if (eintrag === null) {
			return "-";
		}
		return `${eintrag.kuerzel} - ${eintrag.text}`;
	});

	bezeichnungFachklasse = computed<string>(() => {
		const eintrag = Fachklasse.data().getEintragByID(this.proxy.idFachklasse ?? -1);
		if (eintrag === null) {
			return "-";
		}
		return `${eintrag.kuerzel} - ${eintrag.text}`;
	});

	fachklassen = computed<Iterable<FachklasseKatalogEintrag>>(() => {
		if (this.proxy.idSchulgliederung === null) {
			return [];
		}
		const eintrag = Schulgliederung.data().getEintragByID(this.proxy.idSchulgliederung);
		if (eintrag === null) {
			return [];
		}
		return Fachklasse.getBySchuljahrAndBKIndex(this.schuljahr, eintrag.bkIndex);
	});

}
