import { createApp, defineAsyncComponent } from "vue";
import { Main } from "~/apps/Main";

declare module "@vue/runtime-core" {
	// provide typings for `this.$app`
	interface ComponentCustomProperties {
		$app: Main;
	}
}

const App = defineAsyncComponent(
	() => import("~/components/App.vue")
);

const app = createApp(App)
app.config.globalProperties.$app = new Main();
await app.config.globalProperties.$app.connectTo("localhost")

app.mount("#app");
