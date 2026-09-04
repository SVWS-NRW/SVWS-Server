import type { SchuelerLernabschnittListeEintrag } from "@core/core/data/schueler/SchuelerLernabschnittListeEintrag";
import type { List } from "@core/java/util/List";
import type { TabManager } from "@ui/ui/nav/TabManager";

export interface SchuelerLernabschnitteProps {
	lernabschnitt: SchuelerLernabschnittListeEintrag | undefined;
	lernabschnitte: List<SchuelerLernabschnittListeEintrag>;
	gotoLernabschnitt: (value: SchuelerLernabschnittListeEintrag) => Promise<void>;
	tabManager: () => TabManager;
}
