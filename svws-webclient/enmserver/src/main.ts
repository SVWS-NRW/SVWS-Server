import { createApp } from "vue";

import "@ui/assets/styles/index.css";
import "./main.css";

import SWrapper from "@wenom/components/SWrapper.vue";
import { auskunftStateImpl } from "./states/AuskunftStateImpl";
import { registerStates } from "./states/registerStates";
import { AppContext } from "@ui/AppContext";
import { RouteManager } from "./router/RouteManager";

await auskunftStateImpl.init();

const context = AppContext.init(createApp(SWrapper));
RouteManager.create(AppContext.instance.router);

registerStates();

await context.mount();
