import type { BenutzerKennwort, SchemaListeEintrag } from "@core";

export interface SchemaAuswahlProps {
	auswahl: SchemaListeEintrag | undefined;
	auswahlGruppe: SchemaListeEintrag[];
	mapSchema: () => Map<string, SchemaListeEintrag>;
	gotoSchema: (value: SchemaListeEintrag | undefined) => Promise<void>;
	setAuswahlGruppe: (value: SchemaListeEintrag[]) => void;
	removeSchemata: () => Promise<void>;
	addSchema: (data: BenutzerKennwort, schema: string) => Promise<void>;
}

