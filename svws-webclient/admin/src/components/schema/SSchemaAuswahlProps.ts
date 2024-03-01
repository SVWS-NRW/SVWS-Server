import type { BenutzerKennwort, SchemaListeEintrag, SimpleOperationResponse } from "@core";
import type { SchemaMigrationQuelle } from "./SchemaMigrationQuelle";
import type { ApiStatus } from "../ApiStatus";

export interface SchemaAuswahlProps {
	hasRootPrivileges: boolean;
	auswahl: SchemaListeEintrag | undefined;
	auswahlGruppe: SchemaListeEintrag[];
	migrationQuellinformationen: () => SchemaMigrationQuelle;
	mapSchema: () => Map<string, SchemaListeEintrag>;
	gotoSchema: (value: SchemaListeEintrag | undefined) => Promise<void>;
	setAuswahlGruppe: (value: SchemaListeEintrag[]) => void;
	removeSchemata: () => Promise<void>;
	addSchema: (data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>;
	importSchema: (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
	migrateSchema: (data: FormData) => Promise<SimpleOperationResponse>;
	duplicateSchema: (formData: FormData, duplikat: string) => Promise<SimpleOperationResponse>;
	addExistingSchemaToConfig: (data: BenutzerKennwort, schema: string) => Promise<void>;
	refresh: () => Promise<void>;
	apiStatus: ApiStatus;
}

