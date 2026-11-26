import { computed } from "vue";

import type { KursLehrer } from "@core/asd/data/kurse/KursLehrer";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";

import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";

/**
 * Der spezielle ModelProxy für die Kurslehrer
 */
export class KursLehrerModelProxy extends ModelProxy<KursLehrer> {

	private readonly _kurseAuswahlState = useKurseAuswahlState();

	/**
	 * Erzeugt einen neuen Modelproxy für die Kurslehrer-Daten, registriert die Validatoren
	 * und führt eine erste Validierung durch.
	 *
	 * @param data    eine Funktion, welche das aktuelle {@link KursLehrer}-Objekt liefert
	 * @param patch   eine Funktion, welche die geänderten Daten an den Server sendet und
	 *                zurückgibt, ob das Patchen erfolgreich war
	 */
	constructor(data: () => KursLehrer, patch?: (data: Partial<KursLehrer>) => Promise<boolean>) {
		const listOfAutopatchProps: Iterable<keyof KursLehrer> = [];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren(): void {

		// Lehrkraft (beim Neuanlegen ist idLehrer noch 0)
		this.addBlockingValidator(new ValidatorInputRequired(() => (this.proxy.idLehrer <= 0) ? null : this.proxy.idLehrer), "idLehrer");

		// Wochenstunden
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.wochenstundenLehrer), "wochenstundenLehrer");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.wochenstundenLehrer, 0, null), "wochenstundenLehrer");
	}

	lehrer = computed<LehrerListeEintrag | null>({
		get: () => this._kurseAuswahlState.lehrer.get(this.proxy.idLehrer),
		set: (value) => this.proxy.idLehrer = value?.id ?? 0,
	});
}
