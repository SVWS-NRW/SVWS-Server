import type { FachklassenListeManager } from "@ui";
import { ValidatorStringIsUniqueInList, ModelProxy, StringPattern, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { DQRNiveauKatalogEintrag, FachklasseEintrag, FachklasseKatalogEintrag, SchulgliederungKatalogEintrag } from "@core";
import { DQRNiveau, Fachklasse, Schulgliederung } from "@core";
import { computed } from "vue";
import { ValidatorFachklasseFeldDifferentFromCoreType } from "~/components/schule/kataloge/fachklassen/modelproxy/ValidatorFachklasseFeldDifferentFromCoreType";

export class FachklassenModelProxy extends ModelProxy<FachklasseEintrag> {

	private readonly manager: () => FachklassenListeManager;
	private readonly schuljahr: number;

	constructor(
		data: () => FachklasseEintrag,
		manager: () => FachklassenListeManager,
		schuljahr: number,
		patch?: (data: Partial<FachklasseEintrag>) => Promise<boolean>
	) {
		const autopatchProps: Iterable<keyof FachklasseEintrag> = ["istSichtbar", "idFachklasse", "idDqrNiveau"];
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
		this.addValidator(new ValidatorFachklasseFeldDifferentFromCoreType(() => this.proxy.bezeichnung, () => this.proxy.idFachklasse, f => f.bezeichnungM), "bezeichnung");

		// bezeichnung (weibliche Form)
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnungWeiblich, null, 100), 'bezeichnungWeiblich');
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.bezeichnungWeiblich), 'bezeichnungWeiblich');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnungWeiblich, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bezeichnungWeiblich');
		this.addValidator(new ValidatorFachklasseFeldDifferentFromCoreType(() => this.proxy.bezeichnungWeiblich, () => this.proxy.idFachklasse, f => f.bezeichnungW), "bezeichnungWeiblich");

		// Berufsebene 1
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.berufsebene1, null, 255), 'berufsebene1');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.berufsebene1, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'berufsebene1');
		this.addValidator(new ValidatorFachklasseFeldDifferentFromCoreType(() => this.proxy.berufsebene1, () => this.proxy.idFachklasse, f => f.ebene1), "berufsebene1");

		// Berufsebene 2
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.berufsebene2, null, 255), 'berufsebene2');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.berufsebene2, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'berufsebene2');
		this.addValidator(new ValidatorFachklasseFeldDifferentFromCoreType(() => this.proxy.berufsebene2, () => this.proxy.idFachklasse, f => f.ebene2), "berufsebene2");

		// Berufsebene 3
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.berufsebene3, null, 255), 'berufsebene3');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.berufsebene3, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'berufsebene3');
		this.addValidator(new ValidatorFachklasseFeldDifferentFromCoreType(() => this.proxy.berufsebene3, () => this.proxy.idFachklasse, f => f.ebene3), "berufsebene3");
		const schluesselDQRNiveau = () => DQRNiveau.data().getEintragByID(this.proxy.idDqrNiveau ?? -1)?.schluessel ?? "";
		this.addValidator(new ValidatorFachklasseFeldDifferentFromCoreType(schluesselDQRNiveau, () => this.proxy.idFachklasse, f => f.dqrNiveau), "idDqrNiveau");

		this.addBlockingValidator(new ValidatorInputRequired((): number | null => this.proxy.idSchulgliederung), 'idSchulgliederung');
		this.addBlockingValidator(new ValidatorInputRequired((): number | null => this.proxy.idFachklasse), 'idFachklasse');

		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange((): number => this.proxy.sortierung, 0, 32000), "sortierung");
	}



	schulgliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => Schulgliederung.data().getEintragByID(this.proxy.idSchulgliederung ?? -1),
		set: (v: SchulgliederungKatalogEintrag | null) => this.proxy.idSchulgliederung = v?.id ?? null,
	});

	dqrNiveau = computed<DQRNiveauKatalogEintrag | null>({
		get: () => DQRNiveau.data().getEintragByID(this.proxy.idDqrNiveau ?? -1),
		set: (v: DQRNiveauKatalogEintrag | null) => this.proxy.idDqrNiveau = v?.id ?? null,
	});

	bezeichnungSchulgliederung = computed<string>(() => {
		const eintrag = Schulgliederung.data().getEintragByID(this.proxy.idSchulgliederung ?? -1);
		if (eintrag === null) {
			return "-";
		}
		return `${eintrag.kuerzel} - ${eintrag.text}`;
	});

	fachklasse = computed<FachklasseKatalogEintrag | null>({
		get: () => Fachklasse.data().getEintragByID(this.proxy.idFachklasse ?? -1),
		set: (v: FachklasseKatalogEintrag | null) => this.setDefaultValues(v),
	});

	private setDefaultValues(v: FachklasseKatalogEintrag | null) {
		this.proxy.idFachklasse = v?.id ?? null;
		this.proxy.bezeichnung = v?.bezeichnungM ?? null;
		this.proxy.bezeichnungWeiblich = v?.bezeichnungW ?? null;
		this.proxy.berufsebene1 = v?.ebene1 ?? null;
		this.proxy.berufsebene2 = v?.ebene2 ?? null;
		this.proxy.berufsebene3 = v?.ebene3 ?? null;
		this.proxy.idDqrNiveau = (v !== null) ? this.getIdDqrNiveau(v.dqrNiveau) : null;
	}

	private getIdDqrNiveau(schluesselDqrNiveau: string | null): number | null {
		if (schluesselDqrNiveau === null) {
			return null;
		}
		const eintrag = DQRNiveau.data().getEintragBySchuljahrUndSchluessel(this.schuljahr, schluesselDqrNiveau);
		return eintrag?.id ?? null;
	}

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

	schluesselFachklasse = computed<string>(() => {
		const eintrag = Fachklasse.data().getEintragByID(this.proxy.idFachklasse ?? -1);
		if (eintrag === null) {
			return "-";
		}
		return `${eintrag.fkSchluessel}-${eintrag.fkSchluessel2}`;
	});

}
