<template>
	<svws-ui-content-card>
		<svws-ui-table :items="kompetenzgruppen" :disable-footer="true">
			<template #header>
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td" role="columnheader">Kompetenz</div>
					<div class="svws-ui-td svws-align-right font-mono" role="columnheader">ID</div>
				</div>
			</template>
			<template #body>
				<template v-for="(kompetenzgruppe, index) in kompetenzgruppen" :key="index">
					<s-benutzergruppe-kompetenzgruppe :kompetenzgruppe="kompetenzgruppe" :ist-admin="istAdmin"
						:get-benutzergruppen-manager="getBenutzergruppenManager" :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz"
						:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe" :remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe"
						:benutzer-kompetenzen="benutzerKompetenzen" />
				</template>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BenutzerKompetenz, BenutzergruppenManager, List} from "@core";
	import { BenutzerKompetenzGruppe } from "@core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	const props = defineProps<{
		getBenutzergruppenManager: () => BenutzergruppenManager;
		addKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>,
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>
		benutzerKompetenzen : (kompetenzgruppe : BenutzerKompetenzGruppe) => List<BenutzerKompetenz>;
	}>();

	const istAdmin: ComputedRef<boolean> = computed(() => props.getBenutzergruppenManager().istAdmin());

	const kompetenzgruppen: ComputedRef<BenutzerKompetenzGruppe[]> = computed(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0 && props.benutzerKompetenzen(gr).size() > 0));

</script>

<style scoped lang="postcss">
.svws-ui-tr {
	grid-template-columns: minmax(4rem, 3fr) minmax(4rem, 0.25fr);
}
</style>
