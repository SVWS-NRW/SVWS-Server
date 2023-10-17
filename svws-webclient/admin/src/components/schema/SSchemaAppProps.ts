import type { SchemaListeEintrag } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface SchemaAppProps {
	auswahl: SchemaListeEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
