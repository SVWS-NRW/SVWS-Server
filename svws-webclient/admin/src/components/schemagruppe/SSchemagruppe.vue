<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Auswahl bearbeiten">
				<svws-ui-button type="error" @click="removeSchemata" title="Ausgewählte Schemata löschen"> <span class="icon i-ri-delete-bin-line h-[1.5em] w-[1.5em] !-m-[0.3em] !-mr-[0.1em]" />{{ auswahlGruppe.length }} Schema{{ auswahlGruppe.length === 1 ? '':'ta' }} löschen </svws-ui-button>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { SchemagruppeProps } from "./SSchemagruppeProps";

	const props = defineProps<SchemagruppeProps>();

</script>
