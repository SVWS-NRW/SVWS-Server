import type { AuskunftState } from "@ui/states/AuskunftState";
import { StateManager } from "@ui/ui/StateManager";

interface AuskunftReactiveState {
	/** Die Datenschutzauskunft */
	datenschutz: string | null;

	/** Das Impressum */
	impressum: string | null;
}

/**
 * Die Schnittstelle für den Zustand des der Auskunftdaten
 */
export class AuskunftStateImpl extends StateManager<AuskunftReactiveState> implements AuskunftState {

	public constructor() {
		super({
			datenschutz: null,
			impressum: null,
		});
	}


	public get datenschutz(): string | null {
		return this._state.value.datenschutz;
	}

	public get impressum(): string | null {
		return this._state.value.impressum;
	}

}

export const auskunftStateImpl = new AuskunftStateImpl();
