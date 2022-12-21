<template>
	<div v-if="app.auswahl.ausgewaehlt && app.auswahl.ausgewaehlt !== null" class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header>
				<span class="inline-block mr-3">{{ inputBezeichnung }}</span>
				<svws-ui-badge variant="light">{{ inputId }}</svws-ui-badge>
				<br/>
				<span class="opacity-50">{{ inputKuerzel }}</span>
			</svws-ui-header>
			<svws-ui-tab-bar v-model="app.selectedTab.value">
				<template #tabs>
					<svws-ui-tab-button>Daten</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
						<!-- TODO Implementierung Jahrgangsdaten beim Server benötigt -->
						<s-jahrgang-daten />
					</svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-archive-line/>
	</div>
</template>

<script setup lang="ts">
import { computed, ComputedRef } from "vue";
import { injectMainApp, Main } from "~/apps/Main";

// TODO Implementierung Jahrgangsdaten beim Server benötigt
// const SJahrgangsDaten = defineAsyncComponent(() => import("~/components/jahrgaenge/daten/SJahrgangsDaten.vue"));

// TODO Implementierung Jahrgangsdaten beim Server benötigt
// @Options({ name: 's-jahrgaenge-app', components: { SJahrgangsDaten } })
const main: Main = injectMainApp();
const app = main.apps.jahrgaenge;

const inputKuerzel: ComputedRef<string | null> = computed(() => {
	if (app.auswahl.ausgewaehlt && app.auswahl.ausgewaehlt !== null) {
		return String(app.auswahl.ausgewaehlt.kuerzel);
	} else if (app.auswahl.ausgewaehlt === null) {
		return null;
	}
	return "";
});

const inputId: ComputedRef<string | undefined> = computed(() => {
	if (app.auswahl.ausgewaehlt) {
		return "ID: " + app.auswahl.ausgewaehlt.id;
	}
	return "";
});

const inputBezeichnung: ComputedRef<string | null> = computed(() => {
	if (app.auswahl.ausgewaehlt && app.auswahl.ausgewaehlt !== null) {
		return String(app.auswahl.ausgewaehlt.bezeichnung);
	} else if (app.auswahl.ausgewaehlt === null) {
		return null;
	}
	return "";
});
</script>
