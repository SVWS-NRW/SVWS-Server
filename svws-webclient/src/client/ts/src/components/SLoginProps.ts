import type { DBSchemaListeEintrag, List } from "@svws-nrw/svws-core";

export interface LoginProps {
	authenticated: boolean;
	hostname: string;
	setHostname: (hostname: string) => void;
	setSchema: (schema: DBSchemaListeEintrag) => Promise<void>;
	login: (schema: string, username: string, password: string) => Promise<void>;
	connectTo: (url: string) => Promise<List<DBSchemaListeEintrag>>;
	schema?: string
}