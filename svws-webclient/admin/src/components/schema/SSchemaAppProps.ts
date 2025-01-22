import type { SchemaListeEintrag } from "@core/core/data/db/SchemaListeEintrag";
import type { SchuleInfo } from "@core/core/data/schule/SchuleInfo";
import type { TabManager } from "@ui/ui/nav/TabManager";

export interface SchemaAppProps {
	auswahl: SchemaListeEintrag | undefined;
	schuleInfo: () => SchuleInfo | undefined;
	tabManager: () => TabManager;
}
