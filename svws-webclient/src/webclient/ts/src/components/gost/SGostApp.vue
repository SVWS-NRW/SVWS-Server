<template>
	<div v-if="bezeichnung_abiturjahr" class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header>
				<span class="inline-block mr-3">{{ bezeichnung_abiturjahr }}</span>
				<!--<svws-ui-badge variant="light">{{ jahrgang }}</svws-ui-badge>-->
				<br/>
				<span class="opacity-50">{{ jahrgang }}</span>
			</svws-ui-header>
			<svws-ui-tab-bar v-model="app.selectedTab.value">
				<template #tabs>
					<svws-ui-tab-button>Allgemein</svws-ui-tab-button>
					<svws-ui-tab-button>Fächer</svws-ui-tab-button>
					<svws-ui-tab-button :hidden="!jahrgang">Fachwahlen</svws-ui-tab-button>
					<svws-ui-tab-button :hidden="!abiturjahr">Kursplanung</svws-ui-tab-button>
					<svws-ui-tab-button :hidden="!abiturjahr">Klausurplanung</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel> <s-gost-stammdaten /> </svws-ui-tab-panel>
					<svws-ui-tab-panel> <s-gost-faecher /> </svws-ui-tab-panel>
					<svws-ui-tab-panel :hidden="!jahrgang"> <s-gost-fachwahlen /> </svws-ui-tab-panel>
					<svws-ui-tab-panel :hidden="!abiturjahr"> <s-gost-kursplanung /> </svws-ui-tab-panel>
					<svws-ui-tab-panel :hidden="!abiturjahr"> <s-gost-klausurplanung /> </svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1.2em" height="1.2em">
			<g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
			   stroke-width="2">
				<path
					d="m2.573 8.463l8.659-4.329a.6.6 0 0 1 .536 0l8.659 4.33a.6.6 0 0 1 0 1.073l-8.659 4.329a.6.6 0 0 1-.536 0l-8.659-4.33a.6.6 0 0 1 0-1.073Z"/>
				<path
					d="M22.5 13V9.5l-2-1m-16 2v5.412a2 2 0 0 0 1.142 1.807l5 2.374a2 2 0 0 0 1.716 0l5-2.374a2 2 0 0 0 1.142-1.807V10.5"/>
			</g>
		</svg>
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgang } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { injectMainApp } from "~/apps/Main";

	const app = injectMainApp().apps.gost;

	const props = defineProps<{ id: number | undefined; item: GostJahrgang | undefined }>();

	const jahrgang: ComputedRef<string | undefined> = computed(() => {
		return props.item?.jahrgang?.toString();
	});

	const abiturjahr: ComputedRef<number | undefined> = computed(() => {
		return props.id;
	});

	const bezeichnung_abiturjahr: ComputedRef<string | undefined> = computed(() => {
		return props.item?.bezeichnung?.toString();
	});

</script>
