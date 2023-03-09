import { SchuelerListeEintrag, SchuelerStammdaten, KlassenListeEintrag } from "@svws-nrw/svws-core";
import { AuswahlChildData } from "../AuswahlChildData";

export interface SchuelerAppProps {
	auswahl: SchuelerListeEintrag | undefined;
	stammdaten: SchuelerStammdaten | undefined;
	mapKlassen: Map<Number, KlassenListeEintrag>;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
