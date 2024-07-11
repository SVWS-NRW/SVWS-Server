import type {AuswahlChildData} from "~/components/AuswahlChildData";
import type {Einwilligungsart} from "@core";

export interface SEinwilligungsartenAppProps {
	auswahl: () => Einwilligungsart | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
