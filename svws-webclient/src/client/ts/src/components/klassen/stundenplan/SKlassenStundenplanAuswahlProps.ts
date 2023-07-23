import type { StundenplanListeEintrag } from "@core";

export interface KlassenStundenplanAuswahlProps {
	stundenplan: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	gotoStundenplan: (value: StundenplanListeEintrag | undefined) => Promise<void>;
}
