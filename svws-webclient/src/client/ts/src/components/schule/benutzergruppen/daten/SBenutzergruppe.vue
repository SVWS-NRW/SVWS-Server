<template>
	<div v-if="visible" class="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-16 min-h-full">
		<div class="flex flex-col gap-12 lg:gap-16">
			<s-card-benutzergruppe-daten :set-bezeichnung="setBezeichnung" :set-ist-admin="setIstAdmin" :get-benutzergruppen-manager="getBenutzergruppenManager" />
			<s-benutzergruppe-add-benutzer :list-benutzer-alle="listBenutzerAlle"
										   :list-benutzergruppen-benutzer="listBenutzergruppenBenutzer"
										   :aktualisiere-liste-benutzer-gruppen-benutzer="aktualisiereListeBenutzerGruppenBenutzer"
										   :add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
										   :remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
										   :go-to-benutzer="goToBenutzer" />
		</div>
		<div>
			<s-card-benutzergruppe-kompetenzen :get-benutzergruppen-manager="getBenutzergruppenManager"
											   :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz"
											   :add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe"
											   :remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef} from "vue";
	import { BenutzergruppeProps } from "./SBenutzergruppeProps";

	const props = defineProps<BenutzergruppeProps>();

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
