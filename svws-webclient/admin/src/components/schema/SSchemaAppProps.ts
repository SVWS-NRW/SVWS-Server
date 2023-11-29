import type { SchemaListeEintrag, SchuleInfo } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface SchemaAppProps {
	auswahl: SchemaListeEintrag | undefined;
	schuleInfo: SchuleInfo | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
