import type { GostBlockungKurs, GostBlockungsdatenManager, GostBlockungsergebnisManager, GostFach, List, SchuelerListeEintrag} from "@core";
import { GostKursart, ArrayList } from "@core";
import type { ComputedRef, Ref, WritableComputedRef } from "vue";
import { computed, ref } from "vue";


export class GostKursplanungSchuelerFilter {

	private	kollisionen: Ref<boolean> = ref(false);
	private	nichtwahlen: Ref<boolean> = ref(false);
	private	kursfilter_negiert: Ref<boolean> = ref(false);
	public name: Ref<string> = ref("");
	public kurs: Ref<GostBlockungKurs | undefined> = ref(undefined);
	public fach: Ref<number | undefined> = ref(undefined);
	public kursart: Ref<GostKursart | undefined> = ref(undefined);
	private mapSchueler: Map<number, SchuelerListeEintrag>;
	private datenmanager: GostBlockungsdatenManager | undefined;
	private ergebnismanager: GostBlockungsergebnisManager | undefined;
	private faecher: List<GostFach>;

	public constructor(datenmanager: GostBlockungsdatenManager | undefined, ergebnismanager: GostBlockungsergebnisManager | undefined,
		faecher: List<GostFach>, mapSchueler: Map<number, SchuelerListeEintrag>) {
		this.datenmanager = datenmanager;
		this.ergebnismanager = ergebnismanager;
		this.faecher = faecher;
		this.mapSchueler = mapSchueler;
	}

	public getKurse() : List<GostBlockungKurs> {
		return this.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new ArrayList<GostBlockungKurs>();
	}

	public filtered: ComputedRef<Map<number, SchuelerListeEintrag>> = computed(() => {
		if (this.ergebnismanager === undefined)
			return new Map<number, SchuelerListeEintrag>();
		const pKonfliktTyp = 0 + (this.kollisionen.value ? 1:0) + (this.nichtwahlen.value ? 2:0)
		const res = this.ergebnismanager.getMengeDerSchuelerGefiltert(this.kurs.value?.id || 0,
			this.fach.value || 0,
			this.kursart.value?.id || 0,
			pKonfliktTyp,
			this.name.value);
		const gefiltert = new Map<number, SchuelerListeEintrag>();
		for (const s of res) {
			const ss = this.mapSchueler.get(s.id);
			if (ss !== undefined)
				gefiltert.set(s.id, ss);
		}
		return gefiltert;
	})

	public reset() {
		this.kollisionen.value = false;
		this.nichtwahlen.value = false;
		this.name.value = "";
		this.kurs.value = undefined;
		this.kursfilter_negiert.value = false;
		this.fach.value = undefined;
		this.kursart.value = undefined;
	}

	private setFachFilter(value: boolean) {
		if (value && (this.faecher.size() > 0)) {
			this.setKursFilter(false);
			this.fach.value = this.faecher.get(0).id;
			this.kursart.value = GostKursart.GK;
		} else {
			this.fach.value = undefined;
			this.kursart.value = undefined;
		}
	}

	public fach_filter_toggle: () => WritableComputedRef<boolean> = () => computed({
		get: () => (this.fach.value !== undefined),
		set: (value) => this.setFachFilter(value)
	});

	private setKursFilter(value: boolean) {
		if (value && (this.faecher.size() > 0)) {
			this.kurs.value = this.getKurse().get(0);
			this.setFachFilter(false);
		} else
			this.kurs.value = undefined;
	}

	public kurs_filter_toggle: () => WritableComputedRef<boolean> = () => computed({
		get: () => (this.kurs.value !== undefined),
		set: (value) => this.setKursFilter(value)
	})

	public radio_filter: () => WritableComputedRef<string> = () => computed({
		get: () => {
			if (this.kollisionen.value && this.nichtwahlen.value)
				return 'kollisionen_nichtwahlen';
			if (this.kollisionen.value)
				return 'kollisionen';
			if (this.nichtwahlen.value)
				return 'nichtwahlen';
			return 'alle';
		},
		set: (value) => {
			switch (value) {
				case 'alle':
					this.kollisionen.value = false;
					this.nichtwahlen.value = false;
					break;
				case 'kollisionen':
					this.kollisionen.value = true;
					this.nichtwahlen.value = false;
					break;
				case 'nichtwahlen':
					this.kollisionen.value = false;
					this.nichtwahlen.value = true;
					break;
				case 'kollisionen_nichtwahlen':
					this.kollisionen.value = true;
					this.nichtwahlen.value = true;
					break;
			}
		}
	});

}
