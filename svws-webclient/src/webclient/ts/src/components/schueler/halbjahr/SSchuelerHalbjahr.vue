<template>
	<div v-if="visible" class="app-container">
		<s-card-schueler-halbjahr />
		<s-card-schueler-zeugnis-abschluss />
		<s-card-schueler-abschluss-berechnung />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const SCardSchuelerHalbjahr = defineAsyncComponent(
		() => import("~/components/schueler/halbjahr/SCardSchuelerHalbjahr.vue")
	);
	const SCardSchuelerAbschlussBerechnung = defineAsyncComponent(
		() =>
			import(
				"~/components/schueler/halbjahr/SCardSchuelerAbschlussBerechnung.vue"
			)
	);
	const SCardSchuelerZeugnisAbschluss = defineAsyncComponent(
		() =>
			import(
				"~/components/schueler/halbjahr/SCardSchuelerZeugnisAbschluss.vue"
			)
	);
	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.halbjahr.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});
</script>
