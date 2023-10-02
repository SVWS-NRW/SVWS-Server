import type { SchuleStammdaten, SchuelerLernabschnittsdaten, LehrerListeEintrag, JahrgangsListeEintrag, KlassenListeEintrag, FoerderschwerpunktEintrag, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittManager } from "@core";

export interface SchuelerLernabschnittAllgemeinProps {
	schule: SchuleStammdaten;
	manager: () => SchuelerLernabschnittManager;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchBemerkungen: (data : Partial<SchuelerLernabschnittBemerkungen>) => Promise<void>;
}