import type { GostBlockungKurs, GostBlockungsdatenManager, GostBlockungsergebnisManager, GostFach, List, SchuelerListeEintrag, GostKursart } from "@core";
import type { Ref } from "vue";
import { ArrayList, Geschlecht, GostSchriftlichkeit } from "@core";
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
	private ergebnismanager: (() => GostBlockungsergebnisManager) | undefined;
	private faecher: List<GostFach>;

	public constructor(datenmanager: GostBlockungsdatenManager | undefined, ergebnismanager: (() => GostBlockungsergebnisManager) | undefined,
		faecher: List<GostFach>, mapSchueler: Map<number, SchuelerListeEintrag>) {
		this.datenmanager = datenmanager;
		this.ergebnismanager = ergebnismanager;
		this.faecher = faecher;
		this.mapSchueler = mapSchueler;
	}

	public getKurse() : List<GostBlockungKurs> {
		return new ArrayList(this.datenmanager?.kursGetListeSortiertNachKursartFachNummer()) || new ArrayList<GostBlockungKurs>();
	}

	get name(): string {
		return this._name.value;
	}

	set name(value: string) {
		this._name.value = value;
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

	public filtered = computed<SchuelerListeEintrag[]>(() => {
		const gefiltert: SchuelerListeEintrag[] = [];
		// Wenn keine Manager initialisiert sind, dann kann nicht gefiltert werden ...
		if (this.ergebnismanager === undefined) {
			for (const s of this.mapSchueler.values())
				gefiltert.push(s);
			return gefiltert;
		}
		// ... wenn sie definiert sind, dann findet die Filterung statt
		const pKonfliktTyp = 0 + (this.kollisionen.value ? 1 : 0) + (this.nichtwahlen.value ? 2 : 0)
		const res = this.ergebnismanager().getOfSchuelerMengeGefiltert(this.kurs?.id || -1, this.fach || -1, this.kursart?.id || -1, pKonfliktTyp, this._name.value);
		if (res === undefined)
			return gefiltert;
		for (const s of res) {
			const ss = this.mapSchueler.get(s.id);
			if (ss !== undefined)
				gefiltert.push(ss);
		}
		return gefiltert;
	})

	public statistics = computed(() => {
		// Ist kein Manager vorhanden, dann gib eine leere Statistik zurück...
		if (this.ergebnismanager === undefined)
			return { m: 0, w: 0, d: 0, x: 0, schriftlich: 0, muendlich: 0 };
		// ... ansonsten bestimme die Statistik aus dem Ergebnis-Manager
		const kurs = this.kurs?.id || -1;
		const fach = this.fach || -1;
		const kursart = this.kursart?.id || -1;
		const konfliktTyp = 0 + (this.kollisionen.value ? 1:0) + (this.nichtwahlen.value ? 2:0)
		return {
			m: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', Geschlecht.M, null),
			w: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', Geschlecht.W, null),
			d: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', Geschlecht.D, null),
			x: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', Geschlecht.X, null),
			schriftlich: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', null, GostSchriftlichkeit.SCHRIFTLICH),
			muendlich: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', null, GostSchriftlichkeit.MUENDLICH),
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
		get: () => (this._fach.value === undefined && this._kurs.value === undefined) ? "alle" : undefined,
		set: (value) =>	{
			if (value === 'alle') {
				this.setFachFilter(false)
				this.setKursFilter(false)
			}
		}
	});

	public fach_toggle = computed({
		get: () => (this._fach.value !== undefined) ? 'fach' : undefined,
		set: () => this.setFachFilter(true)
	});

	public kurs_toggle = computed({
		get: () => (this._kurs.value !== undefined) ? "kurs" : undefined,
		set: () => this.setKursFilter(true)
	})

	private setKursFilter(value: boolean) {
		if (value && (this.faecher.size() > 0)) {
			this.kurs = this.getKurse().get(0);
			this.setFachFilter(false);
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
