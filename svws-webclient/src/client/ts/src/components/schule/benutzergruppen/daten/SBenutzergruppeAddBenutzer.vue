<template>
	<svws-ui-content-card title="Benutzerzuordnung">
		<div class="flex w-full gap-6 lg:gap-8">
			<s-benutzer-checkbox-list :list-benutzer="listBenutzerAlle" title="Einfügen" :spalte-links="true"
				:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
				:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
				:go-to-benutzer="goToBenutzer" />
			<s-benutzer-checkbox-list :list-benutzer="listBenutzergruppenBenutzer"
				title="Entfernen" :spalte-links="false"
				:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
				:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
				:go-to-benutzer="goToBenutzer" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BenutzerListeEintrag, List} from "@svws-nrw/svws-core";
	import { BenutzergruppeDaten, ArrayList } from "@svws-nrw/svws-core";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		listBenutzerAlle: () => List<BenutzerListeEintrag> ;
		listBenutzergruppenBenutzer: () => List<BenutzerListeEintrag>;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		aktualisiereListeBenutzerGruppenBenutzer: (benutzer: BenutzerListeEintrag) => Promise<void>;
		goToBenutzer: (b_id: number) => Promise<void>;
	}>();

</script>

<style>

</style>
