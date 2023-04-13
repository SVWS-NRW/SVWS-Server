import type { SchuelerLernabschnittListeEintrag, List } from "@svws-nrw/svws-core";

export interface SchuelerAbschnittAuswahlProps {
	lernabschnitt: SchuelerLernabschnittListeEintrag | undefined;
	lernabschnitte: List<SchuelerLernabschnittListeEintrag>;
	setLernabschnitt: (value: SchuelerLernabschnittListeEintrag | undefined) => Promise<void>;
}