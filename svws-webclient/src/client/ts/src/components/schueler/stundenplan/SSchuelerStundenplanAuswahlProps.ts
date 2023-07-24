import type { StundenplanListeEintrag, StundenplanManager } from "@core";

export interface SchuelerStundenplanAuswahlProps {
	stundenplan: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	gotoStundenplan: (value: StundenplanListeEintrag | undefined) => Promise<void>;
	manager: () => StundenplanManager;
}
