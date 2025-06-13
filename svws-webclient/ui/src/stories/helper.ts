
export function logEvent(...params: any[]) {
	console.log(...params)
	document.dispatchEvent(new CustomEvent("log", { detail: params.join() }));
}