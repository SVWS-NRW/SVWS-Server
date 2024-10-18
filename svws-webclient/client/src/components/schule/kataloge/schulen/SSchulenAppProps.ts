import type { SchulEintrag } from "@core";
import type { TabManager } from "@ui";

export interface SchulenAppProps {
	auswahl: SchulEintrag | undefined;
	tabManager: () => TabManager;
}