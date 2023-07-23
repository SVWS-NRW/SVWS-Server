import type { StundenplanListeEintrag } from "@core";

export interface LehrerStundenplanAuswahlProps {
	stundenplan: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	gotoStundenplan: (value: StundenplanListeEintrag | undefined) => Promise<void>;
}
