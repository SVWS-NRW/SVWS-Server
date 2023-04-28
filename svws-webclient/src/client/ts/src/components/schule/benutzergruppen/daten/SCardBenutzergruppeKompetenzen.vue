<template>
	<svws-ui-content-card title="Kompetenzen">
		<svws-ui-data-table :items="kompetenzgruppen" :disable-footer="true">
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr">
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<span class="data-table__th-title">
								<span class="inline-flex items-center gap-1">
									<span>Kompetenz</span>
								</span>
							</span>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<span class="data-table__th-title font-mono">
								<span class="inline-flex items-center gap-1">
									<span>ID</span>
								</span>
							</span>
						</div>
					</div>
				</div>
			</template>
			<template #body>
				<template v-for="(kompetenzgruppe, index) in kompetenzgruppen"
					:key="index">
					<s-benutzergruppe-kompetenzgruppe :kompetenzgruppe="kompetenzgruppe" :ist-admin="istAdmin"
						:get-benutzergruppen-manager="getBenutzergruppenManager" :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz"
						:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe" :remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe" 
						:benutzer-kompetenzen="benutzerKompetenzen" />
				</template>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BenutzerKompetenz, BenutzergruppenManager, List} from "@svws-nrw/svws-core";
	import { BenutzerKompetenzGruppe } from "@svws-nrw/svws-core";
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
.data-table__tr {
	grid-template-columns: minmax(4rem, 2fr) minmax(4rem, 0.5fr);
}
</style>
