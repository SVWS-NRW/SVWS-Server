import type { StundenplanKalenderwochenzuordnung } from "../../../../../core/src/core/data/stundenplan/StundenplanKalenderwochenzuordnung";
import type { StundenplanListeEintrag } from "../../../../../core/src/core/data/stundenplan/StundenplanListeEintrag";
import type { StundenplanManager } from "../../../../../core/src/core/utils/stundenplan/StundenplanManager";

export interface SchuelerStundenplanProps {
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
