<template>
	<svws-ui-content-card title="Benutzerzuordnung" >
		<div class="flex gap-5 divide-x-2 divide-gray-200 overflow-auto w-full">
			<s-benutzer-checkbox-list :benutzer="benutzer_da" title="Entfernen" :icon="false" />
			<s-benutzer-checkbox-list class="pl-4" :benutzer="benutzer_liste" title="Einfügen" :icon="true" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app_b = main.apps.benutzer;
	const app_bg =  main.apps.benutzergruppe;


	const benutzer_da: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		return app_bg.dataBenutzergruppe.listBenutzergruppenBenutzer.liste
	});

	const benutzer_liste: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		return app_b.auswahl.liste.filter(item => !app_bg.dataBenutzergruppe.listBenutzergruppenBenutzer.liste.find(i => item.id===i.id));
	});

</script>

<style>

</style>