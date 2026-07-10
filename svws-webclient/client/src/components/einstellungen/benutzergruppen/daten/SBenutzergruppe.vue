<template>
	<div v-if="visible" class="page page-flex-row">
		<div class="min-w-128 h-full flex flex-col gap-4">
			<s-card-benutzergruppe-daten :manager
				:set-bezeichnung
				:set-ist-admin
				:aktueller-benutzer
				:map-benutzergruppen />
			<s-card-benutzergruppe-kompetenzen :manager
				:add-kompetenz
				:remove-kompetenz
				:add-benutzer-kompetenz-gruppe
				:remove-benutzer-kompetenz-gruppe
				:benutzer-kompetenzen />
		</div>
		<div class="min-w-fit h-full flex flex-col gap-4">
			<s-benutzergruppe-add-benutzer :alle-benutzer
				:benutzer-in-benutzergruppe
				:add-benutzer-to-benutzergruppe
				:remove-benutzer-from-benutzergruppe
				:goto-benutzer
				:aktueller-benutzer
				:map-benutzergruppen />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { BenutzergruppeProps } from "./SBenutzergruppeProps";

	const props = defineProps<BenutzergruppeProps>();

	const visible = computed(() => props.auswahl() !== undefined);

</script>
