import type { ReligionListeManager } from "@core";
import type { TabManager } from "@ui";

export interface ReligionenAppProps {
	religionListeManager: () => ReligionListeManager;
	tabManager: () => TabManager;
}