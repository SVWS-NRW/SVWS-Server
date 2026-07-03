import { createApp } from "vue";

import "@ui/assets/styles/index.css";

import SWrapper from "~/components/SWrapper.vue";
import { AppContext } from "@ui/AppContext";
import { registerStates } from "./states/registerStates";
import { RouteManager } from "./router/RouteManager";

const context = AppContext.init(createApp(SWrapper));
RouteManager.create(AppContext.instance.router);

registerStates();

await context.mount();
