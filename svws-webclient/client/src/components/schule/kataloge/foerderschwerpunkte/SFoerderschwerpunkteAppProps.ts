import type { FoerderschwerpunktEintrag } from "@core";
import type { TabManager } from "@ui";

export interface FoerderschwerpunkteAppProps {
	auswahl: FoerderschwerpunktEintrag | undefined;
	tabManager: () => TabManager;
}