import type { SchuelerLernabschnittListeEintrag, List } from "@core";
import type { TabManager } from "@ui";

export interface SchuelerLernabschnitteProps {
	lernabschnitt: SchuelerLernabschnittListeEintrag | undefined;
	lernabschnitte: List<SchuelerLernabschnittListeEintrag>;
	gotoLernabschnitt: (value: SchuelerLernabschnittListeEintrag) => Promise<void>;
	tabManager: () => TabManager;
}
