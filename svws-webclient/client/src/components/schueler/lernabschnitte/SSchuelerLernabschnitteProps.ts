import type { SchuelerLernabschnittListeEintrag, List } from "@core";
import type { TabData } from "@ui";

export interface SchuelerLernabschnitteProps {
	lernabschnitt: SchuelerLernabschnittListeEintrag | undefined;
	lernabschnitte: List<SchuelerLernabschnittListeEintrag>;
	gotoLernabschnitt: (value: SchuelerLernabschnittListeEintrag) => Promise<void>;
	setChild: (value: TabData) => Promise<void>;
	child: TabData;
	children: TabData[];
	childrenHidden: boolean[];
}
