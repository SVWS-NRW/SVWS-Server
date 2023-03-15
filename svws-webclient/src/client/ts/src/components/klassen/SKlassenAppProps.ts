import { KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core";
import { AuswahlChildData } from "../AuswahlChildData";

export interface KlassenAppProps {
	auswahl: KlassenListeEintrag | undefined,
	mapLehrer: Map<number, LehrerListeEintrag>,
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}