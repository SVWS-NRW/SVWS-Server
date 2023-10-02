import type { SchuelerLernabschnittListeEintrag, List } from "@core";

export interface SchuelerLernabschnittAuswahlChildData {
	name: string,
	text: string,
}

export interface SchuelerLeistungenProps {
	lernabschnitt: SchuelerLernabschnittListeEintrag | undefined;
	lernabschnitte: List<SchuelerLernabschnittListeEintrag>;
	gotoLernabschnitt: (value: SchuelerLernabschnittListeEintrag) => Promise<void>;
	setChild: (value: SchuelerLernabschnittAuswahlChildData) => Promise<void>;
	child: SchuelerLernabschnittAuswahlChildData;
	children: SchuelerLernabschnittAuswahlChildData[];
}
