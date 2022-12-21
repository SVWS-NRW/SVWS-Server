<template>
	<div v-if="appKurse.auswahl.ausgewaehlt" class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header>
				<span class="inline-block mr-3">{{ inputKuerzel }}</span>
				<svws-ui-badge variant="light">{{ inputId }}</svws-ui-badge>
				<span v-if="inputFachlehrer" class="opacity-50"><br/>{{ inputFachlehrer }}</span>
			</svws-ui-header
			>
			<svws-ui-tab-bar v-model="appKurse.selectedTab.value">
				<template #tabs>
					<svws-ui-tab-button>Daten</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
						<s-kurs-daten/>
					</svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-slideshow-line/>
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	// TODO Implementierung Kursdaten beim Server benötigt
	const SKursDaten = defineAsyncComponent(() => import("~/components/kurse/daten/SKursDaten.vue"));

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
