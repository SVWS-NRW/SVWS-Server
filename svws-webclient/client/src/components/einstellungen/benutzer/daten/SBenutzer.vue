<template>
	<div class="page--content">
		<div class="flex flex-col gap-12 lg:gap-16">
			<s-card-benutzer-daten :get-benutzer-manager :set-anzeigename :set-anmeldename :set-password />
			<s-card-benutzer-gruppen-liste :list-benutzergruppen :add-benutzer-to-benutzergruppe :remove-benutzer-from-benutzergruppe :get-benutzer-manager :goto-benutzergruppe />
		</div>
		<div>
			<s-card-benutzer-kompetenzen :get-benutzer-manager :set-ist-admin :add-kompetenz :remove-kompetenz :add-benutzer-kompetenz-gruppe :remove-benutzer-kompetenz-gruppe :benutzer-kompetenzen />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { BenutzerProps } from './SBenutzerProps';

	const props = defineProps<BenutzerProps>();

</script>
