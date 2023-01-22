<template>
	<svws-ui-content-card title="Kompetenzen">
		<div class="overflow-y-scroll h-screen shadow-md sm:rounded-lg">
			<table class="w-full ">
				<template v-for="kompetenzgruppe in kompetenzgruppen" :key="kompetenzgruppe.daten.id">
					<s-benutzergruppe-kompetenzgruppe :kompetenzgruppe="kompetenzgruppe" :ist-admin="istAdmin" :data="data" />
				</template>
			</table>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzerKompetenzGruppe } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataBenutzergruppe } from "~/apps/schule/benutzerverwaltung/DataBenutzergruppe";

	const props = defineProps<{
		data: DataBenutzergruppe;
	}>();

	const istAdmin: ComputedRef<boolean> = computed(() => (props.data.manager === undefined) ? false : props.data.manager.istAdmin());

	const kompetenzgruppen: ComputedRef<BenutzerKompetenzGruppe[]> = computed(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0));

</script>
