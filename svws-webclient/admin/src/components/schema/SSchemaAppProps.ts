import type { SchemaListeEintrag } from "../../../../core/src/core/data/db/SchemaListeEintrag";
import type { SchuleInfo } from "../../../../core/src/core/data/schule/SchuleInfo";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface SchemaAppProps {
	auswahl: SchemaListeEintrag | undefined;
	schuleInfo: () => SchuleInfo | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
