import { StundenplanListeEintrag, List } from "@svws-nrw/svws-core";

export interface SchuelerStundenplanAuswahlProps {
	stundenplan: StundenplanListeEintrag | undefined;
	stundenplaene: List<StundenplanListeEintrag>;
	setStundenplan: (value: StundenplanListeEintrag | undefined) => Promise<void>;
}
