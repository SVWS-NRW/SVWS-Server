import type { BenutzerListeEintrag } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface BenutzerAppProps {
	auswahl: () => BenutzerListeEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}