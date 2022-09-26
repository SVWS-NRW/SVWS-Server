<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col px-4">
			<svws-ui-header
				:badge="inputId"
				badge-variant="light"
				badge-size="normal"
				><span>{{ inputKuerzel }}</span
				><svws-ui-badge variant="highlight" size="normal">{{
					inputFachlehrer
				}}</svws-ui-badge></svws-ui-header
			>
			<svws-ui-tab-bar v-model="app.selectedTab.value">
				<template #tabs>
					<svws-ui-tab-button>Daten</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
					</svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	// TODO Implementierung Kursdaten beim Server benötigt
	// const SKursDaten = defineAsyncComponent(() => import("~/components/kurse/daten/SKursDaten.vue"));

	// TODO Implementierung Kursdaten beim Server benötigt
	// @Options({ name: 's-kurs-app', components: { SKursDaten } })
	const main: Main = injectMainApp();

	const appKurse = main.apps.kurse;
	const appLehrer = main.apps.lehrer;

	const inputKuerzel: ComputedRef<string> = computed(() => {
		if (appKurse.auswahl.ausgewaehlt) {
			return appKurse.auswahl.ausgewaehlt.kuerzel.toString();
		}
		return "";
	});

	const inputId: ComputedRef<string> = computed(() => {
		if (appKurse.auswahl.ausgewaehlt) {
			return "ID: " + appKurse.auswahl.ausgewaehlt.id;
		}
		return "";
	});

	const inputFachlehrer: ComputedRef<string> = computed(() => {
		const id = appKurse.auswahl.ausgewaehlt?.lehrer;
		const leer = "kein Lehrer festgelegt";
		if (!id) return leer;
		const lehrer = appLehrer.auswahl.liste.find(l => l.id === id);
		return lehrer?.kuerzel.toString() || leer;
	});
</script>
