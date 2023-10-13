import type { BenutzerDaten } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface BenutzerprofilAppProps {
		benutzer: BenutzerDaten;
		setTab: (value: AuswahlChildData) => Promise<void>;
		tab: AuswahlChildData;
		tabs: AuswahlChildData[];
		tabsHidden: boolean[];
	}