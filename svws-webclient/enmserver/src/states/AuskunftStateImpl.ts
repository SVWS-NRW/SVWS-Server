import type { AuskunftState } from "@ui/states/AuskunftState";
import { StateManager } from "@ui/ui/StateManager";
import { ApiEnmServer } from "~/ApiEnmServer";

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

	private get(type: 'Impressum' | 'Datenschutz', text: string | null): string | null {
		if (text !== null) {
			try {
				const url = new URL(text.trim());
				if (url.protocol === 'https') {
					return `Alle Informationen zum ${type} finden Sie unter diesem Link: [${url.href}](${url.href})`;
				}
			} catch {
				return text;
			}
		}
		return text;
	}

	public async init(): Promise<void> {
		const api = new ApiEnmServer();
		const { impressum, datenschutz } = await api.getAuskunft();
		this.setPatchedDefaultState({
			datenschutz: this.get('Datenschutz', datenschutz),
			impressum: this.get('Impressum', impressum),
		});
	}

}

export const auskunftStateImpl = new AuskunftStateImpl();
