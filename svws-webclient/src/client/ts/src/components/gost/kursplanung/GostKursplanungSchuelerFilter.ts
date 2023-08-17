import type { GostBlockungKurs, GostBlockungsdatenManager, GostBlockungsergebnisManager, GostFach, List, SchuelerListeEintrag, GostKursart} from "@core";
import type { ComputedRef, Ref } from "vue";
import { ArrayList } from "@core";
import { computed, ref } from "vue";


export class GostKursplanungSchuelerFilter {

	private	kollisionen: Ref<boolean> = ref(false);
	private	nichtwahlen: Ref<boolean> = ref(false);
	private	kursfilter_negiert: Ref<boolean> = ref(false);
	private _name: Ref<string> = ref("");
	private _kurs: Ref<GostBlockungKurs | undefined> = ref(undefined);
	private _fach: Ref<number | undefined> = ref(undefined);
	private _kursart: Ref<GostKursart | undefined> = ref(undefined);
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
		return this.datenmanager?.kursGetListeSortiertNachKursartFachNummer() || new ArrayList<GostBlockungKurs>();
	}

	get name(): string {
		return this._name.value;
	}

	get kurs(): GostBlockungKurs | undefined {
		return this._kurs.value;
	}

	set kurs(value: GostBlockungKurs | undefined) {
		this._kurs.value = value;
		if (value !== undefined) {
			this._fach.value = undefined;
			this._kursart.value = undefined;
		}
	}

	get fach(): number | undefined {
		return this._fach.value;
	}

	set fach(value: number | undefined) {
		this._fach.value = value;
		if (value !== undefined)
			this._kurs.value = undefined;
	}

	get kursart(): GostKursart | undefined {
		return this._kursart.value;
	}

	set kursart(value: GostKursart | undefined) {
		this._kursart.value = value;
	}

	public filtered: ComputedRef<SchuelerListeEintrag[]> = computed(() => {
		const gefiltert: SchuelerListeEintrag[] = [];
		if (this.ergebnismanager === undefined)
			return gefiltert;
		const pKonfliktTyp = 0 + (this.kollisionen.value ? 1:0) + (this.nichtwahlen.value ? 2:0)
		const res = this.ergebnismanager.getOfSchuelerMengeGefiltert(this.kurs?.id || 0,
			this.fach || 0,
			this.kursart?.id || 0,
			pKonfliktTyp,
			this._name.value);
		for (const s of res) {
			const ss = this.mapSchueler.get(s.id);
			if (ss !== undefined)
				gefiltert.push(ss);
		}
		return gefiltert;
	})

	public statistics = computed(()=>{
		if (this.fach !== undefined) {
			if (this.kursart !== undefined)
				return {
					m: this.ergebnismanager?.getOfFachartAnzahlSchuelerMaennlich(this.fach, this.kursart.id),
					w: this.ergebnismanager?.getOfFachartAnzahlSchuelerWeiblich(this.fach, this.kursart.id),
					d: this.ergebnismanager?.getOfFachartAnzahlSchuelerDivers(this.fach, this.kursart.id),
					x: this.ergebnismanager?.getOfFachartAnzahlSchuelerOhneAngabe(this.fach, this.kursart.id),
					schriftlich: this.ergebnismanager?.getOfFachartAnzahlSchuelerSchriftlich(this.fach, this.kursart.id),
					muendlich: this.ergebnismanager?.getOfFachartAnzahlSchuelerMuendlich(this.fach, this.kursart.id),
				}
			else
				return {
					m: this.ergebnismanager?.getOfFachAnzahlSchuelerMaennlich(this.fach),
					w: this.ergebnismanager?.getOfFachAnzahlSchuelerWeiblich(this.fach),
					d: this.ergebnismanager?.getOfFachAnzahlSchuelerDivers(this.fach),
					x: this.ergebnismanager?.getOfFachAnzahlSchuelerOhneAngabe(this.fach),
					schriftlich: this.ergebnismanager?.getOfFachAnzahlSchuelerSchriftlich(this.fach),
					muendlich: this.ergebnismanager?.getOfFachAnzahlSchuelerMuendlich(this.fach),
				}
		}	else if (this.kurs !== undefined)
			return {
				m: this.ergebnismanager?.getOfKursAnzahlSchuelerMaennlich(this.kurs.id),
				w: this.ergebnismanager?.getOfKursAnzahlSchuelerWeiblich(this.kurs.id),
				d: this.ergebnismanager?.getOfKursAnzahlSchuelerDivers(this.kurs.id),
				x: this.ergebnismanager?.getOfKursAnzahlSchuelerOhneAngabe(this.kurs.id),
				schriftlich: this.ergebnismanager?.getOfKursAnzahlSchuelerSchriftlich(this.kurs.id),
				muendlich: this.ergebnismanager?.getOfKursAnzahlSchuelerMuendlich(this.kurs.id),
			}
		else
			return {
				m: this.ergebnismanager?.getOfSchuelerAnzahlMaennlich(),
				w: this.ergebnismanager?.getOfSchuelerAnzahlWeiblich(),
				d: this.ergebnismanager?.getOfSchuelerAnzahlDivers(),
				x: this.ergebnismanager?.getOfSchuelerAnzahlOhneAngabe(),
				schriftlich: 0,
				muendlich: 0,
			}
	})

	public reset() {
		this.kollisionen.value = false;
		this.nichtwahlen.value = false;
		this._name.value = "";
		this.kurs = undefined;
		this.kursfilter_negiert.value = false;
		this.fach = undefined;
		this.kursart = undefined;
	}

	private setFachFilter(value: boolean) {
		if (value && (this.faecher.size() > 0)) {
			this.setKursFilter(false);
			this.fach = this.faecher.get(0).id;
			// this.kursart = GostKursart.GK;
		} else {
			this.fach = undefined;
			// this.kursart = undefined;
		}
	}

	public alle_toggle = computed({
		get: () => {
			if (this._fach.value === undefined && this._kurs.value === undefined) return "alle"
			else return undefined
		},
		set: (value) =>	{
			if (value === 'alle') {
				this.setFachFilter(false)
				this.setKursFilter(false)
			}
		}
	});

	public fach_toggle = computed({
		get: () => {
			if (this._fach.value !== undefined) return "fach"
			else return undefined
		},
		set: (value) =>	{
			this.setFachFilter(true)
		}
	});

	public kurs_toggle = computed({
		get: () => {
			if (this._kurs.value !== undefined) return "kurs"
			else return undefined
		},
		set: (value) =>	{
			this.setKursFilter(true)
		}
	})

	private setKursFilter(value: boolean) {
		if (value && (this.faecher.size() > 0)) {
			this.kurs = this.getKurse().get(0);
			console.log(1, this.kurs)
			this.setFachFilter(false);
			console.log(2, this.kurs)
		} else
			this.kurs = undefined;
	}

	public get radio_filter() {
		if (this.kollisionen.value && this.nichtwahlen.value)
			return 'kollisionen_nichtwahlen';
		if (this.kollisionen.value)
			return 'kollisionen';
		if (this.nichtwahlen.value)
			return 'nichtwahlen';
		return 'alle';
	}

	public set radio_filter(value) {
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
}
