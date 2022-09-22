import { SchuelerBetriebsdaten } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";
import { ListSchuelerBetriebsdatenAnsprechpartner } from "./ListSchuelerBetriebsdatenAnsprechpartner";
import { ListSchuelerBetriebsdatenBetriebe } from "./ListSchuelerBetriebsdatenBetriebe";
import { ListSchuelerBetriebsdatenLehrer } from "./ListSchuelerBetriebsdatenLehrer";

export class ListSchuelerBetriebsdaten extends BaseList<SchuelerBetriebsdaten> {

	protected _ready = true;

	protected _filter = undefined;

	public lehrer : ListSchuelerBetriebsdatenLehrer = new ListSchuelerBetriebsdatenLehrer();
	public betriebe : ListSchuelerBetriebsdatenBetriebe = new ListSchuelerBetriebsdatenBetriebe();
	public betriebansprechpartner : ListSchuelerBetriebsdatenAnsprechpartner = new ListSchuelerBetriebsdatenAnsprechpartner();

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(schueler_id: number): Promise<void> {
		this.ausgewaehlt = undefined;
		const _pending : Promise<unknown>[] = [];
		_pending.push(super._update_list(() => App.api.getSchuelerBetriebe(App.schema, schueler_id)));
		_pending.push(this.lehrer.update_list());
		_pending.push(this.betriebe.update_list());
		_pending.push(this.betriebansprechpartner.update_list());
		await Promise.all(_pending)
			.then(() => (this._ready = true))
			.catch(() => (this._ready = false));
		this.ausgewaehlt = this.liste[0] || undefined;
	}

}
