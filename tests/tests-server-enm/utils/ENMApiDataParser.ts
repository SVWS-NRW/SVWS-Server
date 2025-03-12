import { ENMDaten } from "@core";

/**
 * Parst Binärdaten, indem sie dekomprimiert und in ein ENMDaten-Objekt umgewandelt werden.
 *
 * @param {Blob} binaryData - Die zu parsenden Binärdaten.
 * @returns {Promise<ENMDaten>} - Ein Promise, das das geparste ENMDaten-Objekt zurückgibt.
*/
export async function parse(binaryData: Blob) {
	const blob = await new Response(binaryData.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
	return ENMDaten.transpilerFromJSON(await blob.text());
}
