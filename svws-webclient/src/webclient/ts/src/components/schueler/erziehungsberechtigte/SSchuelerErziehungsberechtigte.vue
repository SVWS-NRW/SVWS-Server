<template>
	<div v-if="visible" class="app-container relative">
		<div
			class="svws-ui-bg-white sticky top-0 z-50 col-span-3 flex justify-end py-4"
		>
			<s-card-schueler-add-erziehungsberechtigte />
		</div>
		<div v-for="(e, i) in erzieher" :key="i" class="col-span-3">
			<s-card-schueler-erziehungsberechtigte :erzieher="(e as unknown as ErzieherStammdaten)" />
		</div>
	</div>
</template>

<script setup lang="ts">
	import { ErzieherStammdaten, List } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, defineAsyncComponent } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const SCardSchuelerErziehungsberechtigte = defineAsyncComponent(
		() =>
			import(
				"~/components/schueler/erziehungsberechtigte/SCardSchuelerErziehungsberechtigte.vue"
			)
	);

	const SCardSchuelerAddErziehungsberechtigte = defineAsyncComponent(
		() =>
			import(
				"~/components/schueler/erziehungsberechtigte/SCardSchuelerAddErziehungsberechtigte.vue"
			)
	);

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.erzieher.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

	const erzieher: ComputedRef<List<ErzieherStammdaten> | undefined> = computed(
		() => {
			console.log(app.erzieher?.daten);
			return app.erzieher?.daten;
		}
	);
</script>
