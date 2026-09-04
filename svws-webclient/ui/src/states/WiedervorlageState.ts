import type { BenutzergruppeListeEintrag } from "@core/core/data/benutzer/BenutzergruppeListeEintrag";
import type { WiedervorlageEintrag } from "@core/core/data/schule/WiedervorlageEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";
import type { InjectionKey } from "vue";

/**
 * Die Schnittstelle die Anzeige der Wiedervorlagenliste und ihrer API-Methoden
 */
export interface WiedervorlageState {
	get benutzerGruppen(): List<BenutzergruppeListeEintrag>;

	get wiedervorlagenListe(): List<WiedervorlageEintrag>;

	get anzahlOffeneWiedervorlagen(): number;

	init(): Promise<void>;

	updateWiedervorlagen(): Promise<void>;

	updateAnzahlOffeneWiedervorlagen(): Promise<void>;

	addWiedervorlage(data: Partial<WiedervorlageEintrag>): Promise<WiedervorlageEintrag>;

	patchWiedervorlage(data: Partial<WiedervorlageEintrag>, id: number): Promise<void>;

	toggleWiedervorlageErledigung(data: WiedervorlageEintrag): Promise<boolean>;
}

export const WiedervorlageStateKey: InjectionKey<WiedervorlageState> = Symbol('WiedervorlageState');

export function useWiedervorlageState(): WiedervorlageState {
	const state = AppContext.instance.inject(WiedervorlageStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des WiedervorlageState über provide in der main.ts eingebunden");
	}
	return state;
}
