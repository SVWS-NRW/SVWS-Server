import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { reactive } from "vue";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

interface Filter {
	kollision: { [index: number]: boolean } | undefined;
	negiert: { [index: number]: boolean } | undefined;
	kurs: { [index: number]: boolean } | undefined;
	kursid: number | undefined;
	name: string;
}

export class ListAbiturjahrgangSchueler extends BaseList<SchuelerListeEintrag, Filter> {

	protected _filter: Filter = reactive({
		kollision: undefined,
		negiert: undefined,
		kurs: undefined,
		kursid: undefined,
		name: ""
	});

	public reset_filter() {
		this._filter.kollision = undefined;
		this._filter.kurs = undefined;
		this._filter.negiert = undefined;
		this._filter.name = "";
		this._filter.kursid = undefined;
	}

	/**
	 * Diese Methode filtert die Daten der Auswahlliste und wird von dem
	 * AppState-Objekt dieser Klasse beim Laden des Katalogs automatisch aufgerufen.
	 *
	 * @returns Void
	 */
	protected filter_liste(): void {
		const man = App.apps.gost.dataKursblockung.ergebnismanager;
		this._state.gefiltert = this.liste
			.filter(s => (this.filter.kollision) ? this.filter.kollision[s.id] : s)
			.filter(s => (this.filter.kursid == undefined) || (man?.getOfKursSchuelerIDmenge(this.filter.kursid).contains(s.id) || false) ? s : undefined)
//			.filter(s => (this.filter.kurs && !this.filter.negiert) ? this.filter.kurs[s.id] : s)
//			.filter(s => (this.filter.negiert) ? this.filter.negiert[s.id] : s)
			.filter(s => 
				s.nachname.toLocaleLowerCase().includes(this.filter.name.toLocaleLowerCase()) ||
				s.vorname.toLocaleLowerCase().includes(this.filter.name.toLocaleLowerCase()));
	}

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(abiturjahr: number): Promise<void> {
		if (!abiturjahr) 
			return
		await super._update_list(() => App.api.getGostAbiturjahrgangSchueler(App.schema, abiturjahr));
	}
}
