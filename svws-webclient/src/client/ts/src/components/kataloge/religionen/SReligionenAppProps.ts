import { ReligionEintrag } from "@svws-nrw/svws-core";
import { AuswahlChildData } from "~/components/AuswahlChildData";

export interface ReligionenAppProps {
	auswahl: ReligionEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}