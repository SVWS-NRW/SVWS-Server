<template>
	<svws-ui-content-card title="Benutzerzuordnung">
		<div class="flex gap-5 divide-x-2 divide-gray-200 overflow-auto w-full">
			<s-benutzer-checkbox-list :list-benutzer="listBenutzergruppenBenutzer"
				title="Entfernen" :spalte-links="true"
				:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
				:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe" 
				:go-to-benutzer="goToBenutzer" />
			<s-benutzer-checkbox-list class="pl-4"
				:list-benutzer="listBenutzerAlle" title="Einfügen" :spalte-links="false"
				:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
				:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe" 
				:go-to-benutzer="goToBenutzer" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzergruppeDaten, BenutzerListeEintrag, List, Vector } from "@svws-nrw/svws-core";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		listBenutzerAlle: () => List<BenutzerListeEintrag> ;
		listBenutzergruppenBenutzer: () => List<BenutzerListeEintrag>;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		aktualisiereListeBenutzerGruppenBenutzer: (benutzer: BenutzerListeEintrag) => Promise<void>;
		goToBenutzer: (b_id: number) => Promise<void>;
	}>();

	const benutzer_nicht_in_gruppe: ComputedRef<List<BenutzerListeEintrag>> = computed(() => {
		const liste = props.listBenutzergruppenBenutzer();
		const benutzerGruppenbenutzer = new Set();
		for (const l of liste)
			benutzerGruppenbenutzer.add(l.id)
		const result = new Vector<BenutzerListeEintrag>();
		for (const l of props.listBenutzerAlle()){
			if (!benutzerGruppenbenutzer.has(l.id))
				result.add(l);
		}
		console.log("hallo");
		return result;
	});

</script>

<style>

</style>