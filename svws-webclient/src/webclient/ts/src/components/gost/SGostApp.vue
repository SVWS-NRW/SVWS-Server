<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header :badge="jahrgang">{{ bezeichnung_abiturjahr }}
			</svws-ui-header>
			<svws-ui-tab-bar v-model="app.selectedTab.value">
				<template #tabs>
					<svws-ui-tab-button>Allgemein</svws-ui-tab-button>
					<svws-ui-tab-button>Fächer</svws-ui-tab-button>
					<svws-ui-tab-button :hidden="!jahrgang">Fachwahlen</svws-ui-tab-button>
					<svws-ui-tab-button :hidden="!abiturjahr">Kursplanung</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel> <s-gost-stammdaten /> </svws-ui-tab-panel>
					<svws-ui-tab-panel> <s-gost-faecher /> </svws-ui-tab-panel>
					<svws-ui-tab-panel :hidden="!jahrgang"> <s-gost-fachwahlen /> </svws-ui-tab-panel>
					<svws-ui-tab-panel :hidden="!abiturjahr"> <s-gost-kursplanung /> </svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
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
