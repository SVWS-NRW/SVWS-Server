import type { Sprachpruefung } from "@core/asd/data/schueler/Sprachpruefung";
import { Sprachreferenzniveau } from "@core/asd/types/fach/Sprachreferenzniveau";
import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
import { Note } from "@core/asd/types/Note";
import { Sprachpruefungniveau } from "@core/core/types/fach/Sprachpruefungniveau";
import { ModelProxy } from "@ui/model/ModelProxy";
import { computed } from "vue";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

/**
 * Der spezielle ModelProxy für die Sprachbelegung
 */
export class SchuelerSprachpruefungModelProxy extends ModelProxy<Sprachpruefung> {

	private readonly _manager: () => SchuelerListeManager;
	public readonly ersetzt = [{ key: '1. Pflichtfremdsprache' }, { key: '2. Pflichtfremdsprache' }, { key: 'Wahlpflichtfremdsprache' }] as const;

	constructor(data: () => Sprachpruefung, manager: () => SchuelerListeManager, patch: (data: Partial<Sprachpruefung>) => Promise<boolean>) {

		const listOfAutopatchProps: Iterable<keyof Sprachpruefung> = [
			"kannBelegungAlsFortgefuehrteSpracheErlauben", "jahrgang", "note", "referenzniveau", "anspruchsniveauId",
		];
		super({ data, patch, listOfAutopatchProps });

		this._manager = manager;

		this.validate();
	}

	ersetztSprache = computed({
		get: () => {
			if (this.proxy.kannErstePflichtfremdspracheErsetzen) {
				return this.ersetzt[0];
			} else if (this.proxy.kannZweitePflichtfremdspracheErsetzen) {
				return this.ersetzt[1];
			} else if (this.proxy.kannWahlpflichtfremdspracheErsetzen) {
				return this.ersetzt[2];
			} else {
				return null;
			}
		},
		set: (value) => {
			this.proxy.kannErstePflichtfremdspracheErsetzen = value?.key === '1. Pflichtfremdsprache';
			this.proxy.kannZweitePflichtfremdspracheErsetzen = value?.key === '2. Pflichtfremdsprache';
			this.proxy.kannWahlpflichtfremdspracheErsetzen = value?.key === 'Wahlpflichtfremdsprache';
			void this.patch();
		},
	});

	jahrgang = computed<Jahrgaenge | null>({
		get: () => {
			if (this.proxy.jahrgang === null) {
				return null;
			} else {
				return Jahrgaenge.data().getWertByKuerzel(this.proxy.jahrgang);
			}
		},
		set: (value) => {
			if (value === null) {
				this.proxy.jahrgang = null;
				return;
			}
			const jahrgang = value.daten(this._manager().schuelerGetSchuljahrOrException());
			this.proxy.jahrgang = jahrgang?.kuerzel ?? null;
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

	anspruchsniveauId = computed<Sprachpruefungniveau | null>({
		get: () => Sprachpruefungniveau.getByID(this.proxy.anspruchsniveauId ?? null),
		set: (value) => this.proxy.anspruchsniveauId = value?.daten.id ?? null,
	});

	note = computed<Note | null>({
		get: () => Note.fromNoteSekI(this.proxy.note),
		set: (value) => this.proxy.note = value?.getNoteSekI(this._manager().schuelerGetSchuljahrOrException()) ?? null,
	});
}
