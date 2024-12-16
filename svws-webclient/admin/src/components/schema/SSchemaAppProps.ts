import type { SchemaListeEintrag } from "../../../../core/src/core/data/db/SchemaListeEintrag";
import type { SchuleInfo } from "../../../../core/src/core/data/schule/SchuleInfo";
import type { TabManager } from "../../../../ui/src/ui/nav/TabManager";

export interface SchemaAppProps {
	auswahl: SchemaListeEintrag | undefined;
	schuleInfo: () => SchuleInfo | undefined;
	tabManager: () => TabManager;
}
