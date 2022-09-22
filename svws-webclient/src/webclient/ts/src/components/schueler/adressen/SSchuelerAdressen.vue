<template>
	<div v-if="visible" class="app-container relative">
		<div
			class="svws-ui-bg-white sticky top-0 z-50 col-span-3 flex justify-end py-4"
		>
			<s-card-schueler-add-adresse />
		</div>
		<div v-if="sb_vorhanden"  class="col-span-3">
			<s-card-schueler-beschaeftigung />
			<s-card-schueler-adresse />
		</div>
		<div v-else><h1><strong><center>Noch kein Schülerbetrieb vorhanden</center></strong> </h1></div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const SCardSchuelerAdresse = defineAsyncComponent(
		() => import("~/components/schueler/adressen/SCardSchuelerAdresse.vue")
	);

	const SCardSchuelerAddAdresse = defineAsyncComponent(
		() =>
			import("~/components/schueler/adressen/SCardSchuelerAddAdresse.vue")
	);

	const SCardSchuelerBeschaeftigung = defineAsyncComponent(
		() =>
			import(
				"~/components/schueler/adressen/SCardSchuelerBeschaeftigung.vue"
			)
	);

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.adressen.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

	const sb_vorhanden : ComputedRef<boolean> = computed(() => { 
		if(app.listSchuelerbetriebe) 
			return app.listSchuelerbetriebe?.liste.length > 0 ? true : false ;
		return false;
	});
</script>
