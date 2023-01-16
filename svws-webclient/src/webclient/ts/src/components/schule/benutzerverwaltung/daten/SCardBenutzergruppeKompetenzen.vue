<template>
	<svws-ui-content-card title="Kompetenzen">
		<div class="overflow-x-auto relative shadow-md sm:rounded-lg">
			<table class="w-full ">
				<s-kompetenzgruppe v-for="kompetenzgruppe in BenutzerKompetenzGruppe.values()" :key="kompetenzgruppe.daten.id" :kompetenzgruppe="kompetenzgruppe" :ist-admin="istAdmin" :benutzertyp="1" />
			</table>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzergruppenManager, BenutzerKompetenzGruppe } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.benutzergruppe;

	const manager: ComputedRef<BenutzergruppenManager | undefined> = computed(() => {
		return app.dataBenutzergruppe.manager;
	});

	const istAdmin: ComputedRef<boolean> = computed(() => {
		if (app.dataBenutzergruppe.manager === undefined)
			return false;
		return app.dataBenutzergruppe.manager.istAdmin();
	});

</script>
