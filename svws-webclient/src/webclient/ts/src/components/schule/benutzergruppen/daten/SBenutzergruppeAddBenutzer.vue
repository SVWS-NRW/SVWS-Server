<template>
	<svws-ui-content-card title="Benutzerzuordnung" >
		<div class="flex gap-5 divide-x-2 divide-gray-200 overflow-auto w-full">
			<s-benutzer-checkbox-list :benutzer-liste="benutzer_da" title="Entfernen" :spalte-links="true" :data="data" />
			<s-benutzer-checkbox-list class="pl-4" :benutzer-liste="benutzer_liste" title="Einfügen" :spalte-links="false" :data="data" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataBenutzergruppe } from "~/apps/schule/benutzerverwaltung/DataBenutzergruppe";

	const props = defineProps<{
		data: DataBenutzergruppe;
		benutzer: BenutzerListeEintrag[];
	}>();

	const benutzer_da: ComputedRef<BenutzerListeEintrag[]> = computed(() => {
		return props.data.listBenutzergruppenBenutzer.liste || []
	});

	const benutzer_liste: ComputedRef<BenutzerListeEintrag[]> = computed(() => {
		return props.benutzer.filter(item => !props.data.listBenutzergruppenBenutzer.liste.find(i => item.id === i.id));
	});

</script>

<style>

</style>