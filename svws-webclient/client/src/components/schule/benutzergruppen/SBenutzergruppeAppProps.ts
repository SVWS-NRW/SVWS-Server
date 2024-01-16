import type { BenutzergruppeListeEintrag} from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface BenutzergruppeAppProps {
	auswahl:() =>  BenutzergruppeListeEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}