import type { BenutzerKennwort, SchemaListeEintrag } from "@core";

export interface SchemaAuswahlProps {
	hasRootPrivileges: boolean;
	auswahl: SchemaListeEintrag | undefined;
	auswahlGruppe: SchemaListeEintrag[];
	mapSchema: () => Map<string, SchemaListeEintrag>;
	gotoSchema: (value: SchemaListeEintrag | undefined) => Promise<void>;
	setAuswahlGruppe: (value: SchemaListeEintrag[]) => void;
	removeSchemata: () => Promise<void>;
	addSchema: (data: BenutzerKennwort, schema: string) => Promise<void>;
	importSchema: (formData: FormData, schema: string) => Promise<void>;
	migrateSchema: (data: FormData) => Promise<void>;
}

