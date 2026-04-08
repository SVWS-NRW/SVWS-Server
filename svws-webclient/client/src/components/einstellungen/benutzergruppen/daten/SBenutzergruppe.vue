<template>
	<div v-if="visible" class="page page-flex-row">
		<div class="min-w-128 h-full flex flex-col gap-4">
			<s-card-benutzergruppe-daten :set-bezeichnung :set-ist-admin :get-benutzergruppen-manager />
			<s-card-benutzergruppe-kompetenzen :get-benutzergruppen-manager :add-kompetenz :remove-kompetenz
				:add-benutzer-kompetenz-gruppe :remove-benutzer-kompetenz-gruppe :benutzer-kompetenzen />
		</div>
		<div class="min-w-fit h-full flex flex-col gap-4">
			<s-benutzergruppe-add-benutzer :list-benutzer-alle :list-benutzergruppen-benutzer :aktualisiere-liste-benutzer-gruppen-benutzer
				:add-benutzer-to-benutzergruppe :remove-benutzer-from-benutzergruppe :goto-benutzer />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { BenutzergruppeProps } from "./SBenutzergruppeProps";

	const props = defineProps<BenutzergruppeProps>();

	const visible = computed(() => props.auswahl() !== undefined);

</script>
