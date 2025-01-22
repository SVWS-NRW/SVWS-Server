import type { ApiStatus } from "~/components/ApiStatus";
import type { SchemaMigrationQuelle } from "../schema/SchemaMigrationQuelle";
import type { BenutzerKennwort } from "@core/core/data/BenutzerKennwort";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";

export interface SchemaNeuProps {
	apiStatus: ApiStatus;
	apiUsername: string;
	addSchema: (data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>;
	importSchema: (formData: FormData, schema: string) => Promise<SimpleOperationResponse>;
	migrateSchema: (data: FormData) => Promise<SimpleOperationResponse>;
	duplicateSchema: (formData: FormData, duplikat: string) => Promise<SimpleOperationResponse>;
	migrationQuellinformationen: () => SchemaMigrationQuelle;
	schema?: string;
}
