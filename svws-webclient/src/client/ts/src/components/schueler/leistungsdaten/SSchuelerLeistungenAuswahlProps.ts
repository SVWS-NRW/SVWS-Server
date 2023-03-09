import { SchuelerLernabschnittListeEintrag, List } from "@svws-nrw/svws-core";

export interface SchuelerLeistungenAuswahlProps {
	lernabschnitt: SchuelerLernabschnittListeEintrag | undefined;
	lernabschnitte: List<SchuelerLernabschnittListeEintrag>;
	setLernabschnitt: (value: SchuelerLernabschnittListeEintrag | undefined) => Promise<void>;
}