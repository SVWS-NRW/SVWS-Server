import SWrapper from "@lupo/components/SWrapper.vue";
import { createApp } from "vue";

import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";
import { AppContext } from "@ui/AppContext";

import { RouteManager } from "./router/RouteManager";
import { auskunftStateImpl } from "./states/AuskunftStateImpl";
import { registerStates } from "./states/registerStates";

import "../../ui/src/assets/styles/index.css";
import "./main.css";

await auskunftStateImpl.init();

const context = AppContext.init(createApp(SWrapper));
RouteManager.create(AppContext.instance.router);

registerStates();

// Lese die Daten für die Core-Types ein
new JsonCoreTypeReaderStatic().readAll();

if (process.env.NODE_ENV === 'development') {
	const { registerSVWSDevTools } = await import("../../ui/src/devtools/stateInspector");
	registerSVWSDevTools(context.app);
}

await context.mount();
