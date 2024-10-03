import type { StundenplanListeEintrag } from "@core";
import type { TabManager } from "@ui";

export interface StundenplanAppProps {
	auswahl: StundenplanListeEintrag | undefined;
	tabManager: () => TabManager;
}
