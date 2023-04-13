<template>
	<div class="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-16 min-h-full">
		<div class="flex flex-col gap-12 lg:gap-16">
			<s-card-benutzer-daten :get-benutzer-manager="getBenutzerManager"
				:set-anzeigename="setAnzeigename" :set-anmeldename="setAnmeldename"
				:set-password="setPassword" />
			<s-card-benutzer-gruppen-liste :list-benutzergruppen="listBenutzergruppen"
				:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
				:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
				:get-benutzer-manager="getBenutzerManager"
				:go-to-benutzergruppe="goToBenutzergruppe" />
		</div>
		<div>
			<s-card-benutzer-kompetenzen :get-benutzer-manager="getBenutzerManager"
				:set-ist-admin="setIstAdmin" :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz"
				:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe" :get-gruppen4-kompetenz="getGruppen4Kompetenz"
				:remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { BenutzerProps } from './SBenutzerProps';

	const props = defineProps<BenutzerProps>();

</script>
