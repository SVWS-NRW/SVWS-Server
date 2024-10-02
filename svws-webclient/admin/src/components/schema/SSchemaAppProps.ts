import type { SchemaListeEintrag } from "../../../../core/src/core/data/db/SchemaListeEintrag";
import type { SchuleInfo } from "../../../../core/src/core/data/schule/SchuleInfo";
import type { TabData } from "../../../../ui/src/components/App/TabData";

export interface SchemaAppProps {
	auswahl: SchemaListeEintrag | undefined;
	schuleInfo: () => SchuleInfo | undefined;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}
