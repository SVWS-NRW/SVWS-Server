import type { SchuelerLernabschnittListeEintrag, List } from "@core";

export interface SchuelerLeistungenProps {
	lernabschnitt: SchuelerLernabschnittListeEintrag | undefined;
	lernabschnitte: List<SchuelerLernabschnittListeEintrag>;
	gotoLernabschnitt: (value: SchuelerLernabschnittListeEintrag) => Promise<void>;
}
