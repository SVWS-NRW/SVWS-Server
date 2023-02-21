<template>
	<svws-ui-content-card title="Kompetenzen">
		<div class="overflow-y-scroll h-screen shadow-md sm:rounded-lg">
			<table class="w-full ">
				<template v-for="kompetenzgruppe in kompetenzgruppen" :key="kompetenzgruppe.daten.id">
					<s-benutzergruppe-kompetenzgruppe :kompetenzgruppe="kompetenzgruppe" :ist-admin="istAdmin" :data="data"
						:get-benutzergruppen-manager="getBenutzergruppenManager" :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz"
						:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe" :remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe"  />
				</template>
			</table>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzergruppeDaten, BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	const props = defineProps<{
		data: BenutzergruppeDaten;
		getBenutzergruppenManager: () => BenutzergruppenManager;
		addKompetenz : (kompetenz: BenutzerKompetenz) => Promise<void>;
		removeKompetenz : (kompetenz: BenutzerKompetenz) => Promise<void>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<void>,
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<void>
	}>();

	const istAdmin: ComputedRef<boolean> = computed(() => props.getBenutzergruppenManager().istAdmin());

	const kompetenzgruppen: ComputedRef<BenutzerKompetenzGruppe[]> = computed(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0));

</script>
