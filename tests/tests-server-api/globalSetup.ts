import { JsonCoreTypeReader } from "@core";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

const targetHost : string = process.env.VITE_targetHost ?? "https://localhost";

export default async function setup() {
	console.log("Target Host for CORE Types " + targetHost)
	const reader = new JsonCoreTypeReader(targetHost);
	await reader.loadAll();

	// initiiere alle Core Types
	reader.readAll();
}
