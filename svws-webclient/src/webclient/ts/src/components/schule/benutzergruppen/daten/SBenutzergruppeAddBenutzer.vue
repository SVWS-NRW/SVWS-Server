<template>
	<svws-ui-content-card title="Benutzerzuordnung">
		<div class="flex gap-5 divide-x-2 divide-gray-200 overflow-auto w-full">
			<s-benutzer-checkbox-list :list-benutzer="listBenutzergruppenBenutzer"
				title="Entfernen" :spalte-links="true"
				:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
				:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe" />
			<s-benutzer-checkbox-list class="pl-4"
				:list-benutzer="benutzer_nicht_in_gruppe" title="Einfügen" :spalte-links="false"
				:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
				:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzergruppeDaten, BenutzerListeEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		data: BenutzergruppeDaten;
		listBenutzerAlle: List<BenutzerListeEintrag>;
		listBenutzergruppenBenutzer: List<BenutzerListeEintrag>;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
	}>();

	const benutzer_nicht_in_gruppe: ComputedRef<List<BenutzerListeEintrag>> = computed(() => {
		props.listBenutzerAlle.removeAll(props.listBenutzergruppenBenutzer);
		return props.listBenutzerAlle;
	});

</script>

<style>

</style>