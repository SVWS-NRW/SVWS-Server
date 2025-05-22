import type { DBSchemaListeEintrag, List, ServerMode } from "@core";

export interface LoginProps {
	authenticated: boolean;
	hostname: string;
	servermode: ServerMode;
	setHostname: (hostname: string) => void;
	setSchema: (schema: DBSchemaListeEintrag) => Promise<void>;
	login: (schema: string, username: string, password: string) => Promise<void>;
	connectTo: (url: string) => Promise<List<DBSchemaListeEintrag>>;
	schemaPrevious: string | null;
	setMapCoreTypeNameJsonData: (map: Map<string, string>) => void;
}
