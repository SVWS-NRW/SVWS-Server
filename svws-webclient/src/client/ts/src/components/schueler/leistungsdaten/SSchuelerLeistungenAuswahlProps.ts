import type { SchuelerLernabschnittListeEintrag, List } from "@core";

export interface SchuelerLeistungenAuswahlProps {
	lernabschnitt: SchuelerLernabschnittListeEintrag | undefined;
	lernabschnitte: List<SchuelerLernabschnittListeEintrag>;
	gotoLernabschnitt: (value: SchuelerLernabschnittListeEintrag | undefined) => Promise<void>;
}