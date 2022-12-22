<template>
	<div v-if="visible" class="app-container relative">
		<div class="svws-ui-bg-white sticky top-0 z-50 col-span-3 flex justify-end py-4">
			<svws-ui-button>Erziehungsberechtigten hinzufügen</svws-ui-button>
		</div>
		<div v-for="(e, i) in erzieher" :key="i" class="col-span-3">
			<s-card-schueler-erziehungsberechtigte :erzieher="(e as unknown as ErzieherStammdaten)" />
		</div>
	</div>
</template>

<script setup lang="ts">
	import { ErzieherStammdaten, List } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const erzieher: ComputedRef<List<ErzieherStammdaten> | undefined> = computed(() => app.erzieher?.daten);

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.erzieher.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
