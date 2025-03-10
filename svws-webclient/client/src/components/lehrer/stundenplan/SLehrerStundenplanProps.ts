import type { ApiFile, StundenplanListeEintrag, StundenplanKalenderwochenzuordnung, StundenplanManager } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";


export interface LehrerStundenplanProps {
	apiStatus: ApiStatus;
	getPDF: (title: DownloadPDFTypen) => Promise<ApiFile>;
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

export type DownloadPDFTypen = "Stundenplan" | "Stundenplan mit Pausenaufsichten" | "Stundenplan mit Pausenzeiten";
