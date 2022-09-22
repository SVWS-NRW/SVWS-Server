import "./assets/styles/index.css";
import { App, Plugin } from "vue";
import * as components from "./components";
import SmartTable from "vuejs-smart-table";

const install: Exclude<Plugin["install"], undefined> = function install(
	app: App
) {
	app.use(SmartTable);
	Object.entries(components).forEach(([componentName, component]) => {
		app.component(componentName, component);
	});
};

export default install;
