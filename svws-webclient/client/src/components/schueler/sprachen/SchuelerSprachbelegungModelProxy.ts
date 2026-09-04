import type { Sprachbelegung } from "@core/asd/data/schueler/Sprachbelegung";
import { Sprachreferenzniveau } from "@core/asd/types/fach/Sprachreferenzniveau";
import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
import { ModelProxy } from "@ui/model/ModelProxy";
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
			"belegungVonJahrgang", "belegungBisJahrgang",
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
				this.proxy.belegungVonAbschnitt = null;
				this.proxy.belegungVonJahrgang = null;
				return;
			}

			const jahrgang = value.daten(this._manager().schuelerGetSchuljahrOrException());
			if ((jahrgang?.kuerzel === this.proxy.belegungBisJahrgang) && (this.proxy.belegungBisAbschnitt === 1)) {
				this.proxy.belegungVonAbschnitt = 1;
				void this.patch().then(_ => {
					this.proxy.belegungVonJahrgang = jahrgang.kuerzel;
				});
			} else {
				this.proxy.belegungVonJahrgang = jahrgang?.kuerzel ?? null;
			}
		},
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
				this.proxy.belegungBisAbschnitt = null;
				this.proxy.belegungBisJahrgang = null;
				return;
			}

			const jahrgang = value.daten(this._manager().schuelerGetSchuljahrOrException());
			if ((this.proxy.belegungVonJahrgang === jahrgang?.kuerzel) && (this.proxy.belegungVonAbschnitt === 2)) {
				this.proxy.belegungBisAbschnitt = 2;
				void this.patch().then(_ => {
					this.proxy.belegungBisJahrgang = jahrgang.kuerzel;
				});
			} else {
				this.proxy.belegungBisJahrgang = jahrgang?.kuerzel ?? null;
			}
		},
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
