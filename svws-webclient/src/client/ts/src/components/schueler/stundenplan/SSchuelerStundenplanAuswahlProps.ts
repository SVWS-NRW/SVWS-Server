import type { StundenplanKalenderwochenzuordnung, StundenplanListeEintrag, StundenplanManager } from "@core";

export interface SchuelerStundenplanAuswahlProps {
	stundenplan: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	gotoStundenplan: (value: StundenplanListeEintrag) => Promise<void>;
	gotoWochentyp: (value: number) => Promise<void>;
	gotoKalenderwoche: (value: StundenplanKalenderwochenzuordnung | undefined) => Promise<void>;
	manager: () => StundenplanManager;
	wochentyp: () => number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
}
