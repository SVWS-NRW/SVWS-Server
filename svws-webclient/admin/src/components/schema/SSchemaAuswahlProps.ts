import type { SchemaMigrationQuelle } from "./SchemaMigrationQuelle";
import type { ApiStatus } from "../ApiStatus";
import type { SchemaListeEintrag } from "../../../../core/src/core/data/db/SchemaListeEintrag";
import type { BenutzerKennwort } from "../../../../core/src/core/data/BenutzerKennwort";
import type { SimpleOperationResponse } from "../../../../core/src/core/data/SimpleOperationResponse";

export interface SchemaAuswahlProps {
	hasRootPrivileges: boolean;
	auswahl: SchemaListeEintrag | undefined;
	auswahlGruppe: SchemaListeEintrag[];
	migrationQuellinformationen: () => SchemaMigrationQuelle;
	mapSchema: () => Map<string, SchemaListeEintrag>;
	gotoSchema: (value: SchemaListeEintrag | undefined) => Promise<void>;
	gotoSchemaNeu: () => Promise<void>;
	setAuswahlGruppe: (value: SchemaListeEintrag[]) => Promise<void>;
	addSchema: (data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>;
	importSchema: (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
	migrateSchema: (data: FormData) => Promise<SimpleOperationResponse>;
	duplicateSchema: (formData: FormData, duplikat: string) => Promise<SimpleOperationResponse>;
	refresh: () => Promise<void>;
	apiStatus: ApiStatus;
	revision: number | null;
}

