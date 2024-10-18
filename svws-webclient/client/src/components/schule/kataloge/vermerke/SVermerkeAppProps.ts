import type { VermerkartenManager } from "@core";
import type { TabManager } from "@ui";

export interface VermerkeAppProps {
	vermerkartenManager : () => VermerkartenManager;
	tabManager: () => TabManager;
}
