import type { ValidatorKontext, LehrerPersonaldaten, LehrerAbgangsgrundKatalogEintrag, LehrerZugangsgrundKatalogEintrag } from "@core";
import { LehrerAbgangsgrund, LehrerZugangsgrund } from "@core";
import { ModelProxy, type LehrerListeManager } from "@ui";
import { computed } from "vue";

/**
 * Der spezielle ModelProxy für die Lehrerstammdaten
 */
export class LehrerPersonaldatenModelProxy extends ModelProxy<LehrerPersonaldaten> {

	protected readonly manager: () => LehrerListeManager;

	/*
	 * Erstellt einen ModelProxy für das Core-DTO LehrerIndividualdaten.
	 *
	 * @param data               ein Lambda für den Zugriff auf die "Original"-Daten
	 * @param validatorKontext   der Validator-Kontext für die Nutzung in den ASD-Validatoren
	 * @param patchMethod        ggf. die Methode zum Patchen der einzelnen Attribute, sofern das automatische Patchen
	 *                           bei Änderungen gewünscht ist
	 */
	constructor(data: () => LehrerPersonaldaten, _validatorKontext: () => ValidatorKontext, manager: () => LehrerListeManager, patch: (data: Partial<LehrerPersonaldaten>) => Promise<boolean>) {
		super({ data, patch, listOfAutopatchProps: ["zugangsgrund", "abgangsgrund"] });
		this.manager = manager;
	}

	zugangsgrund = computed<LehrerZugangsgrundKatalogEintrag | null>({
		get: () => {
			const wert = LehrerZugangsgrund.data().getWertByKuerzel(this.proxy.zugangsgrund ?? '');
			if (wert === null) {
				return null;
			}
			return LehrerZugangsgrund.data().getEintragBySchuljahrUndWert(this.manager().getSchuljahr(), wert);
		},
		set: (value) => this.proxy.zugangsgrund = value?.kuerzel ?? null,
	});

	abgangsgrund = computed<LehrerAbgangsgrundKatalogEintrag | null>({
		get: () => {
			const wert = LehrerAbgangsgrund.data().getWertByKuerzel(this.proxy.abgangsgrund ?? '');
			if (wert === null) {
				return null;
			}
			return LehrerAbgangsgrund.data().getEintragBySchuljahrUndWert(this.manager().getSchuljahr(), wert);
		},
		set: (value) => this.proxy.abgangsgrund = value?.kuerzel ?? null,
	});

}
