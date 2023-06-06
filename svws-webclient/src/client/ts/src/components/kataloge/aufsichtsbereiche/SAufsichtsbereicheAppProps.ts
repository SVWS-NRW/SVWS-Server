import type { Aufsichtsbereich  } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface AufsichtsbereicheAppProps {
	auswahl: Aufsichtsbereich | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}