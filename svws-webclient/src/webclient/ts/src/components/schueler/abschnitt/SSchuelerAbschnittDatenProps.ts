import { SchuleStammdaten, SchuelerLernabschnittsdaten, LehrerListeEintrag, JahrgangsListeEintrag, KlassenListeEintrag, FoerderschwerpunktEintrag, SchuelerLernabschnittBemerkungen } from "@svws-nrw/svws-core-ts";

export interface SchuelerAbschnittDatenProps {
	schule: SchuleStammdaten;
	data: SchuelerLernabschnittsdaten | undefined;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapKlassen: Map<number, KlassenListeEintrag>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchBemerkungen: (data : Partial<SchuelerLernabschnittBemerkungen>) => Promise<void>;
}