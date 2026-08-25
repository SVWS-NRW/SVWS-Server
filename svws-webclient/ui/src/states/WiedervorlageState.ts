import { type InjectionKey } from "vue";
import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";
import type { List } from "../../../core/src/java/util/List";
import type { WiedervorlageEintrag } from "../../../core/src/core/data/schule/WiedervorlageEintrag";
import type { BenutzergruppeListeEintrag } from "../../../core/src/core/data/benutzer/BenutzergruppeListeEintrag";
import { AppContext } from "../AppContext";

/**
 * Die Schnittstelle die Anzeige der Wiedervorlagenliste und ihrer API-Methoden
 */
export interface WiedervorlageState {
	get benutzerGruppen(): List<BenutzergruppeListeEintrag>;

	get wiedervorlagenListe(): List<WiedervorlageEintrag>;

	init(): Promise<void>;

	ladeWiedervorlagen(): Promise<void>;

	addWiedervorlage(data: Partial<WiedervorlageEintrag>): Promise<WiedervorlageEintrag>;

	patchWiedervorlage(data: Partial<WiedervorlageEintrag>, id: number): Promise<void>;

	setWiedervorlageErledigt(data: WiedervorlageEintrag): Promise<void>;

	getBenutzergruppen(): Promise<List<BenutzergruppeListeEintrag>>;
}

export const WiedervorlageStateKey: InjectionKey<WiedervorlageState> = Symbol('WiedervorlageState');

export function useWiedervorlageState(): WiedervorlageState {
	const state = AppContext.instance.inject(WiedervorlageStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des WiedervorlageState über provide in der main.ts eingebunden");
	}
	return state;
}
