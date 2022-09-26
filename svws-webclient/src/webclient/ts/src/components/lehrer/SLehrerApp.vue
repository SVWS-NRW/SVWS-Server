<template>
	<div v-if="app.stammdaten.daten">
		<svws-ui-header :badge="inputId" badge-variant="light" badge-size="normal"><span>{{ inputTitel }} {{ inputVorname }}
				{{ inputNachname }}</span>
			<svws-ui-badge variant="highlight" size="normal">{{
					inputKuerzel
			}}</svws-ui-badge>
		</svws-ui-header>
		<svws-ui-tab-bar v-model="app.selectedTab.value">
			<template #tabs>
				<svws-ui-tab-button>Daten</svws-ui-tab-button>
				<svws-ui-tab-button>Personaldaten</svws-ui-tab-button>
				<svws-ui-tab-button>Unterrichtsdaten</svws-ui-tab-button>
			</template>
			<template #panels>
				<svws-ui-tab-panel>
					<s-lehrer-individualdaten />
				</svws-ui-tab-panel>
				<svws-ui-tab-panel>
					<s-lehrer-personaldaten />
				</svws-ui-tab-panel>
				<svws-ui-tab-panel>
					<s-lehrer-unterrichtsdaten />
				</svws-ui-tab-panel>
			</template>
		</svws-ui-tab-bar>
	</div>
</template>

<script setup lang="ts">
import { computed, ComputedRef } from "vue";

import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.lehrer;

const inputId: ComputedRef<string | false> = computed(() => {
	if (app.auswahl.ausgewaehlt) {
		return "ID: " + app.auswahl.ausgewaehlt.id;
	}
	return false;
});

const inputNachname: ComputedRef<string | undefined> = computed(() => {
	return app.stammdaten.daten?.nachname.toString();
});

const inputVorname: ComputedRef<string | undefined> = computed(() => {
	return app.stammdaten.daten?.vorname.toString();
});

const inputKuerzel: ComputedRef<string | undefined> = computed(() => {
	return app.stammdaten.daten?.kuerzel.toString();
});

const inputTitel: ComputedRef<string | undefined> = computed(() => {
	return app.stammdaten.daten?.titel?.toString();
});
</script>
