<template>
	<div v-if="visible" class="flex flex-row gap-3">
		<div class="felx flex-col w-2/5">
			<s-card-benutzergruppe-daten :data="data" :set-bezeichnung="setBezeichnung" :set-ist-admin="setIstAdmin" :get-benutzergruppen-manager="getBenutzergruppenManager" />
			<s-benutzergruppe-add-benutzer :data="data" :list-benutzer-alle="listBenutzerAlle"
				:list-benutzergruppen-benutzer="listBenutzergruppenBenutzer"
				:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
				:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe" />
		</div>

		<div class="w-3/5">
			<s-card-benutzergruppe-kompetenzen :data="data"
				:get-benutzergruppen-manager="getBenutzergruppenManager" :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz"
				:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe"
				:remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerListeEintrag, List } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";

	const props = defineProps<{
		item: ShallowRef<BenutzergruppeListeEintrag | undefined>;
		data: BenutzergruppeDaten;
		listBenutzerAlle: List<BenutzerListeEintrag>;
		listBenutzergruppenBenutzer: List<BenutzerListeEintrag>;
		getBenutzergruppenManager: () => BenutzergruppenManager;
		setBezeichnung : (anzeigename: string) => Promise<void>;
		setIstAdmin : (istAdmin: boolean) => Promise<void>;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		addKompetenz : (kompetenz: BenutzerKompetenz) => Promise<void>;
		removeKompetenz : (kompetenz: BenutzerKompetenz) => Promise<void>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<void>,
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<void>
	}>();

	const visible: ComputedRef<boolean> = computed(() => props.item !== undefined);

</script>
