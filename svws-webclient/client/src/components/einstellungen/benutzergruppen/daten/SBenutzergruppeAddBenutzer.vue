<template>
	<div class="text-headline-md">Benutzer</div>
	<div class="flex flex-row min-w-fit gap-4 overflow-y-hidden">
		<s-benutzer-checkbox-list :list-benutzer="listBenutzerAlle" :list-benutzer-in-gruppe="listBenutzergruppenBenutzer"
			title="Einfügen" :spalte-links="true"
			:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
			:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
			:goto-benutzer="gotoBenutzer" />
		<s-benutzer-checkbox-list :list-benutzer="listBenutzergruppenBenutzer"
			title="Entfernen" :spalte-links="false" :list-benutzer-in-gruppe="listBenutzergruppenBenutzer"
			:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
			:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
			:goto-benutzer="gotoBenutzer" />
	</div>
</template>

<script setup lang="ts">

	import type { BenutzerListeEintrag, List} from "@core";

	const props = defineProps<{
		listBenutzerAlle: () => List<BenutzerListeEintrag> ;
		listBenutzergruppenBenutzer: () => List<BenutzerListeEintrag>;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		aktualisiereListeBenutzerGruppenBenutzer: (benutzer: BenutzerListeEintrag) => Promise<void>;
		gotoBenutzer: (b_id: number) => Promise<void>;
	}>();

</script>
