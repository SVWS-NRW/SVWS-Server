import type { Schueler } from "@core/asd/data/schueler/Schueler";
import { Geschlecht } from "@core/asd/types/Geschlecht";
import type { GostBlockungKurs } from "@core/core/data/gost/GostBlockungKurs";
import type { GostFach } from "@core/core/data/gost/GostFach";
import type { GostKursart } from "@core/core/types/gost/GostKursart";
import { GostSchriftlichkeit } from "@core/core/types/gost/GostSchriftlichkeit";
import type { GostBlockungsdatenManager } from "@core/core/utils/gost/GostBlockungsdatenManager";
import type { GostBlockungsergebnisManager } from "@core/core/utils/gost/GostBlockungsergebnisManager";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { computed, ref } from "vue";


export class GostKursplanungSchuelerFilter {

	private	readonly kollisionen = ref(false);
	private	readonly nichtwahlen = ref(false);
	private	readonly kursfilter_negiert = ref(false);
	private readonly _name = ref("");
	private readonly _kurs = ref<GostBlockungKurs | undefined>();
	private readonly _fach = ref<number | undefined>();
	private readonly _kursart = ref<GostKursart | undefined>();
	private readonly datenmanager: GostBlockungsdatenManager;
	private readonly ergebnismanager: (() => GostBlockungsergebnisManager);
	private readonly faecher: List<GostFach>;

	public constructor(datenmanager: GostBlockungsdatenManager, ergebnismanager: (() => GostBlockungsergebnisManager), faecher: List<GostFach>) {
		this.datenmanager = datenmanager;
		this.ergebnismanager = ergebnismanager;
		this.faecher = faecher;
	}

	public getKurse(): List<GostBlockungKurs> {
		return new ArrayList(this.datenmanager.kursGetListeSortiertNachKursartFachNummer());
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
		if (value !== undefined) {
			this._kurs.value = undefined;
		}
	}

	get kursart(): GostKursart | undefined {
		return this._kursart.value;
	}

	set kursart(value: GostKursart | undefined) {
		this._kursart.value = value;
	}

	public filtered = computed<Schueler[]>(() => {
		const gefiltert: Schueler[] = [];
		// ... wenn sie definiert sind, dann findet die Filterung statt
		const pKonfliktTyp = 0 + (this.kollisionen.value ? 1 : 0) + (this.nichtwahlen.value ? 2 : 0);
		const res = this.ergebnismanager().getOfSchuelerMengeGefiltert(this.kurs?.id ?? -1, this.fach ?? -1, this.kursart?.id ?? -1, pKonfliktTyp, this._name.value);
		for (const s of res) {
			const ss = this.datenmanager.schuelerGet(s.id);
			gefiltert.push(ss);
		}
		return gefiltert;
	});

	public statistics = computed(() => {
		// ... ansonsten bestimme die Statistik aus dem Ergebnis-Manager
		const kurs = this.kurs?.id ?? -1;
		const fach = this.fach ?? -1;
		const kursart = this.kursart?.id ?? -1;
		const konfliktTyp = 0 + (this.kollisionen.value ? 1 : 0) + (this.nichtwahlen.value ? 2 : 0);
		return {
			m: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', Geschlecht.M, null),
			w: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', Geschlecht.W, null),
			d: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', Geschlecht.D, null),
			x: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', Geschlecht.X, null),
			schriftlich: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', null, GostSchriftlichkeit.SCHRIFTLICH),
			muendlich: this.ergebnismanager().getOfSchuelerAnzahlGefiltert(kurs, fach, kursart, konfliktTyp, '', null, GostSchriftlichkeit.MUENDLICH),
		};
	});

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
				this.setFachFilter(false);
				this.setKursFilter(false);
			}
		},
	});

	public fach_toggle = computed({
		get: () => (this._fach.value !== undefined) ? 'fach' : undefined,
		set: () => this.setFachFilter(true),
	});

	public kurs_toggle = computed({
		get: () => (this._kurs.value !== undefined) ? "kurs" : undefined,
		set: () => this.setKursFilter(true),
	});

	private setKursFilter(value: boolean) {
		if (value && (this.faecher.size() > 0)) {
			this.kurs = this.getKurse().get(0);
			this.setFachFilter(false);
		} else {
			this.kurs = undefined;
		}
	}

	public get radio_filter() {
		if (this.kollisionen.value && this.nichtwahlen.value) {
			return 'kollisionen_nichtwahlen';
		}
		if (this.kollisionen.value) {
			return 'kollisionen';
		}
		if (this.nichtwahlen.value) {
			return 'nichtwahlen';
		}
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
