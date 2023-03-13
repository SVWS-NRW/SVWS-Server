import { JahrgangsListeEintrag } from "@svws-nrw/svws-core";
import { AuswahlChildData } from "~/components/AuswahlChildData";

export interface JahrgaengeAppProps {
	auswahl: JahrgangsListeEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}