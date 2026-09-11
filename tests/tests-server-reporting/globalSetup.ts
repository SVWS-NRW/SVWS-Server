import { backendURL } from "@testUtils/APIUtils";

import { JsonCoreTypeReader } from "@core/asd/utils/JsonCoreTypeReader";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

export default async function setup() {
	const targetHost = backendURL;
	console.log("Target Host for CORE Types " + targetHost);
	const reader = new JsonCoreTypeReader(targetHost);
	await reader.loadAll();

	// initiiere alle Core Types
	reader.readAll();
}
