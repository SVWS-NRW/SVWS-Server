<template>
	<svws-ui-content-card title="Kompetenzen">
		<div class="overflow-y-scroll h-screen shadow-md sm:rounded-lg">
			<table class="w-full ">
				<s-benutzergruppe-kompetenzgruppe v-for="kompetenzgruppe in kompetenzgruppen" :key="kompetenzgruppe.daten.id" :kompetenzgruppe="kompetenzgruppe" :ist-admin="istAdmin" />
			</table>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzerKompetenzGruppe } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.benutzergruppe;

	const istAdmin: ComputedRef<boolean> = computed(() =>
		(app.dataBenutzergruppe.manager === undefined) ? false : app.dataBenutzergruppe.manager.istAdmin()
	);

	const kompetenzgruppen: ComputedRef<BenutzerKompetenzGruppe[]> = computed(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0));

</script>
