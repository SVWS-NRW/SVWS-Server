import { GostBlockungKurs, GostFach, GostKursart, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { reactive } from "vue";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

interface Filter {
	kollisionen: boolean;
	nichtwahlen: boolean;
	kursfilter_negiert: boolean;
	name: string;
	kurs: GostBlockungKurs | undefined;
	fach: GostFach | undefined;
	kursart: GostKursart | undefined;
}

export class ListAbiturjahrgangSchueler extends BaseList<SchuelerListeEintrag, Filter> {

	protected _filter: Filter = reactive({
		kollisionen: false,
		nichtwahlen: false,
		kursid: undefined,
		kursfilter_negiert: false,
		name: "",
		kurs: undefined,
		fach: undefined,
		kursart: undefined
	});

	public reset_filter() {
		this._filter.kollisionen = false;
		this._filter.nichtwahlen = false;
		this._filter.name = "";
		this._filter.kurs = undefined;
		this._filter.kursfilter_negiert = false;
		this._filter.fach = undefined;
		this._filter.kursart = undefined;
		this._state.gefiltert = this.liste;
	}

	/**
	 * Diese Methode filtert die Daten der Auswahlliste und wird von dem
	 * AppState-Objekt dieser Klasse beim Laden des Katalogs automatisch aufgerufen.
	 *
	 * @returns Void
	 */
	protected filter_liste(): void {
		const manager = App.apps.gost.dataKursblockung.ergebnismanager;
		if (!manager)
			return;
		manager.getMengeDerSchuelerMitKollisionen
		const pKonfliktTyp = 0 + (this._filter.kollisionen ? 1:0) + (this._filter.nichtwahlen ? 2:0)
		const res = manager.getMengeDerSchuelerGefiltert(this._filter.kurs?.id || 0,
				this._filter.fach?.id || 0,
				this._filter.kursart?.id || 0,
				pKonfliktTyp,
				this._filter.name);
		const set = new Set();
		for (const s of res)
			set.add(s.id);
		this._state.gefiltert = this.liste.filter(s => set.has(s.id))
	}

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(abiturjahr: number): Promise<void> {
		if ((!abiturjahr) || (abiturjahr === -1))
			return
		await super._update_list(() => App.api.getGostAbiturjahrgangSchueler(App.schema, abiturjahr));
	}
}
