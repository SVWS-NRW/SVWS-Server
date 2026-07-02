import { Jahrgaenge, Sprachreferenzniveau, type Sprachbelegung } from "@core";
import { ModelProxy } from "@ui";
import { computed } from "vue";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

/**
 * Der spezielle ModelProxy für die Sprachbelegung
 */
export class SchuelerSprachbelegungModelProxy extends ModelProxy<Sprachbelegung> {

	private readonly _manager: () => SchuelerListeManager;
	constructor(data: () => Sprachbelegung, manager: () => SchuelerListeManager, patch: (data: Partial<Sprachbelegung>) => Promise<boolean>) {

		const listOfAutopatchProps: Iterable<keyof Sprachbelegung> = [
			"istNachweis",
			"belegungVonJahrgang", "belegungVonAbschnitt", "belegungBisJahrgang", "belegungBisAbschnitt",
			"hatGraecum", "hatHebraicum",
			"referenzniveau",
		];
		super({ data, patch, listOfAutopatchProps });

		this._manager = manager;

		this.validate();
	}

	belegungVonJahrgang = computed<Jahrgaenge | null>({
		get: () => {
			if (this.proxy.belegungVonJahrgang === null) {
				return null;
			} else {
				return Jahrgaenge.data().getWertByKuerzel(this.proxy.belegungVonJahrgang);
			}
		},
		set: (value) => {
			if (value === null) {
				this.proxy.belegungVonJahrgang = null;
				return;
			}
			const jahrgang = value.daten(this._manager().schuelerGetSchuljahrOrException());
			this.proxy.belegungVonJahrgang = jahrgang?.kuerzel ?? null;
		},
	});

	belegungVonAbschnitt = computed<number | null>({
		get: () => this.proxy.belegungVonAbschnitt,
		set: (value) => this.proxy.belegungVonAbschnitt = value === 1 ? 2 : 1,
	});

	belegungBisJahrgang = computed<Jahrgaenge | null>({
		get: () => {
			if (this.proxy.belegungBisJahrgang === null) {
				return null;
			} else {
				return Jahrgaenge.data().getWertByKuerzel(this.proxy.belegungBisJahrgang);
			}
		},
		set: (value) => {
			if (value === null) {
				this.proxy.belegungBisJahrgang = null;
				return;
			}
			const jahrgang = value.daten(this._manager().schuelerGetSchuljahrOrException());
			this.proxy.belegungBisJahrgang = jahrgang?.kuerzel ?? null;
		},
	});

	belegungBisAbschnitt = computed<number | null>({
		get: () => this.proxy.belegungBisAbschnitt,
		set: (value) => this.proxy.belegungBisAbschnitt = value === 1 ? 2 : 1,
	});

	referenzniveau = computed<Sprachreferenzniveau | null>({
		get: () => {
			if (this.proxy.referenzniveau === null) {
				return null;
			} else {
				return Sprachreferenzniveau.data().getWertBySchluessel(this.proxy.referenzniveau);
			}
		},
		set: (value) => {
			if (value === null) {
				this.proxy.referenzniveau = null;
				return;
			}
			const jahrgang = value.daten(this._manager().schuelerGetSchuljahrOrException());
			this.proxy.referenzniveau = jahrgang?.schluessel ?? null;
		},
	});
}
