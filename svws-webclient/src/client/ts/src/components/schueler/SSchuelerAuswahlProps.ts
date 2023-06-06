import type { SchuelerListeEintrag, List, KlassenListeEintrag, JahrgangsListeEintrag, KursListeEintrag, Schulgliederung, Schuljahresabschnitt, SchuelerStatus } from "@core";

export interface Filter {
	jahrgang: JahrgangsListeEintrag | undefined;
	kurs: KursListeEintrag | undefined;
	klasse: KlassenListeEintrag | undefined;
	schulgliederung: Schulgliederung | undefined;
	status: Array<SchuelerStatus>;
}

export interface SchuelerAuswahlProps {
	auswahl: SchuelerListeEintrag | undefined;
	auswahlGruppe: SchuelerListeEintrag[];
	filter: Filter,
	mapSchueler: Map<number, SchuelerListeEintrag>;
	mapKlassen: Map<number, KlassenListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapKurse: Map<number, KursListeEintrag>;
	schulgliederungen: List<Schulgliederung>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	gotoSchueler: (value: SchuelerListeEintrag | undefined) => Promise<void>;
	setFilter: (value: Filter) => void;
	setAuswahlGruppe: (value: SchuelerListeEintrag[]) => void;
}

