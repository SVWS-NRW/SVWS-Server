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
		const resImpressum = await fetch("/Impressum.md");
		let impressum: string | null = null;
		if (resImpressum.ok) {
			const impressumText = await resImpressum.text();
			impressum = this.get('Impressum', impressumText);
		}
		const resDatenschutz = await fetch("/Datenschutz.md");
		let datenschutz: string | null = null;
		if (resDatenschutz.ok) {
			const datenschutzText = await resDatenschutz.text();
			datenschutz = this.get('Datenschutz', datenschutzText);
		}
		this.setPatchedDefaultState({
			datenschutz: this.get('Datenschutz', datenschutz),
			impressum: this.get('Impressum', impressum),
		});
	}
}

export const auskunftStateImpl = new AuskunftStateImpl();
