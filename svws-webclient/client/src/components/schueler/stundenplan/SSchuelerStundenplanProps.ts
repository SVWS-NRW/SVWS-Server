import type { ReportingParameter, ApiFile, StundenplanListeEintrag, StundenplanKalenderwochenzuordnung, StundenplanManager } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchuelerStundenplanProps {
	apiStatus: ApiStatus;
	getPDF: (parameter: ReportingParameter, idStundenplan: number) => Promise<ApiFile>;
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
