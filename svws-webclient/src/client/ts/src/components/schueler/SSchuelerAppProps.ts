import type { SchuelerListeEintrag, SchuelerStammdaten, KlassenListeEintrag } from "@svws-nrw/svws-core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface SchuelerAppProps {
	auswahl: SchuelerListeEintrag | undefined;
	stammdaten: SchuelerStammdaten | undefined;
	mapKlassen: Map<number, KlassenListeEintrag>;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
