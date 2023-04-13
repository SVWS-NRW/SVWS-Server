import type { StundenplanListeEintrag } from "@svws-nrw/svws-core";

export interface SchuelerStundenplanAuswahlProps {
	stundenplan: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	gotoStundenplan: (value: StundenplanListeEintrag | undefined) => Promise<void>;
}
