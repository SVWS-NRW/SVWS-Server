import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { reactive } from "vue";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

interface Filter {
	kollisionsfilter: boolean;
	kursid: number | undefined;
	kursfilter_negiert: boolean;
	name: string;
}

export class ListAbiturjahrgangSchueler extends BaseList<SchuelerListeEintrag, Filter> {

	protected _filter: Filter = reactive({
		kollisionsfilter: false,
		kursid: undefined,
		kursfilter_negiert: false,
		name: ""
	});

	public reset_filter() {
		this._filter.kollisionsfilter = false;
		this._filter.name = "";
		this._filter.kursid = undefined;
		this._filter.kursfilter_negiert = false;
		this._state.gefiltert = this.liste;
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
			.filter(s => (this.filter.kollisionsfilter)
				? ((this.filter.kursid === undefined) ? man?.getOfSchuelerHatKollision(s.id) : man?.getOfKursSchuelermengeMitKollisionen(this.filter.kursid).contains(s.id))
				: s)
			.filter(s => (this.filter.kursid == undefined) || (man?.getOfKursSchuelerIDmenge(this.filter.kursid).contains(s.id) || false) 
				? ((this.filter.kursfilter_negiert) ? undefined : s)
				: ((this.filter.kursfilter_negiert) ? s : undefined))
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
		if ((!abiturjahr) || (abiturjahr === -1))
			return
		await super._update_list(() => App.api.getGostAbiturjahrgangSchueler(App.schema, abiturjahr));
	}
}
