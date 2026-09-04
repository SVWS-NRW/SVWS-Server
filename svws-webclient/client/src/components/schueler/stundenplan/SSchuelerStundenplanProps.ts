import type { StundenplanKalenderwochenzuordnung } from "@core/core/data/stundenplan/StundenplanKalenderwochenzuordnung";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchuelerStundenplanProps {
	apiStatus: ApiStatus;
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
