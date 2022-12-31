<template>
	<div v-if="visible" class="app-container relative">
		<div class="svws-ui-bg-white sticky top-0 z-50 col-span-3 flex justify-end py-4">
			<s-card-schueler-add-adresse />
		</div>
		<div v-if="sb_vorhanden" class="col-span-3">
			<s-card-schueler-beschaeftigung />
			<s-card-schueler-adresse />
		</div>
		<div v-else>
			<h1>
				<strong> <div style="text-align: center;"> Noch kein Schülerbetrieb vorhanden </div> </strong>
			</h1>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { routeSchuelerAdressen } from "~/router/apps/schueler/RouteSchuelerAdressen";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const sb_vorhanden : ComputedRef<boolean> = computed(() => { 
		if (app.listSchuelerbetriebe)
			return app.listSchuelerbetriebe?.liste.length > 0 ? true : false ;
		return false;
	});

	const visible: ComputedRef<boolean> = computed(() => {
		return !routeSchuelerAdressen.hidden;
	});

</script>
