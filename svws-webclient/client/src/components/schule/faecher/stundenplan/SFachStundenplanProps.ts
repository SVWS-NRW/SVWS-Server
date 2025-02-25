import type { StundenplanListeEintrag, StundenplanKalenderwochenzuordnung, StundenplanManager } from "@core";

export interface FachStundenplanProps {
	ignoreEmpty?: boolean;
	id: number,
	stundenplan: () => StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	gotoStundenplan: (value: StundenplanListeEintrag) => Promise<void>;
	gotoWochentyp: (value: number) => Promise<void>;
	gotoKalenderwoche: (value: StundenplanKalenderwochenzuordnung | undefined) => Promise<void>;
	manager: () => StundenplanManager;
	wochentyp: () => number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
	ganzerStundenplan: () => boolean;
	setGanzerStundenplan: (value: boolean) => Promise<void>;
	autofocus?: boolean;
}
