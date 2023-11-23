import type { BenutzerKennwort, SchemaListeEintrag, SimpleOperationResponse } from "@core";

export interface SchemaAuswahlProps {
	hasRootPrivileges: boolean;
	auswahl: SchemaListeEintrag | undefined;
	auswahlGruppe: SchemaListeEintrag[];
	mapSchema: () => Map<string, SchemaListeEintrag>;
	gotoSchema: (value: SchemaListeEintrag | undefined) => Promise<void>;
	setAuswahlGruppe: (value: SchemaListeEintrag[]) => void;
	removeSchemata: () => Promise<void>;
	addSchema: (data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>;
	importSchema: (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
	migrateSchema: (data: FormData) => Promise<SimpleOperationResponse>;
	duplicateSchema: (formData: FormData, duplikat: string) => Promise<SimpleOperationResponse>;
	refresh: () => Promise<void>;
}

