import type { SchuelerLernabschnittListeEintrag, List } from "@core";

export interface SchuelerLernabschnitteAuswahlChildData {
	name: string,
	text: string,
}

export interface SchuelerLernabschnitteProps {
	lernabschnitt: SchuelerLernabschnittListeEintrag | undefined;
	lernabschnitte: List<SchuelerLernabschnittListeEintrag>;
	gotoLernabschnitt: (value: SchuelerLernabschnittListeEintrag) => Promise<void>;
	setChild: (value: SchuelerLernabschnitteAuswahlChildData) => Promise<void>;
	child: SchuelerLernabschnitteAuswahlChildData;
	children: SchuelerLernabschnitteAuswahlChildData[];
	childrenHidden: boolean[];
}
