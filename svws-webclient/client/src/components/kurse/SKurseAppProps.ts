import type { KursDaten, LehrerListeEintrag } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface KurseAppProps {
		auswahl: () => KursDaten | undefined;
		mapLehrer: Map<number, LehrerListeEintrag>;
		setTab: (value: AuswahlChildData) => Promise<void>;
		tab: AuswahlChildData;
		tabs: AuswahlChildData[];
		tabsHidden: boolean[];
	}