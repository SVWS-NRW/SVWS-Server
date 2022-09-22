import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { reactive } from "vue";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

interface Filter {
	kollision: { [index: number]: boolean } | undefined;
	name: string;
}
export class ListAbiturjahrgangSchueler extends BaseList<
	SchuelerListeEintrag,
	Filter
> {
	protected _filter: Filter = reactive({
		kollision: undefined,
		name: ""
	});

	/**
	 * Diese Methode filtert die Daten der Auswahlliste und wird von dem
	 * AppState-Objekt dieser Klasse beim Laden des Katalogs automatisch aufgerufen.
	 *
	 * @returns Void
	 */
	protected filter_liste(): void {
		this._state.gefiltert = this.liste
			.filter(s => {
				if (this.filter.kollision) return this.filter.kollision[s.id];
				else return s;
			})
			.filter(
				s =>
					s.nachname
						.toLocaleLowerCase()
						.includes(this.filter.name.toLocaleLowerCase()) ||
					s.vorname
						.toLocaleLowerCase()
						.includes(this.filter.name.toLocaleLowerCase())
			);
	}
	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(abiturjahr: number): Promise<void> {
		if (!abiturjahr) return
		await super._update_list(() =>
			App.api.getGostAbiturjahrgangSchueler(App.schema, abiturjahr)
		);
	}
}
