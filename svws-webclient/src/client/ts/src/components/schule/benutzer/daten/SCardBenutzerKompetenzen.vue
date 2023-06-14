<template>
	<svws-ui-content-card title="Einstellungen" class="col-span-full">
		<template #actions>
			<svws-ui-toggle class="mb-4" v-model="inputIstAdmin" :disabled="getBenutzerManager().istInAdminGruppe()">
				Admin-Rechte
			</svws-ui-toggle>
		</template>
		<svws-ui-data-table :items="kompetenzgruppen" :disable-footer="true">
			<template #header>
				<svws-ui-table-row>
					<svws-ui-table-cell thead>
						Kompetenz
					</svws-ui-table-cell>
					<svws-ui-table-cell thead>
						Übernommen aus der Gruppe
					</svws-ui-table-cell>
					<svws-ui-table-cell thead class="font-mono">
						ID
					</svws-ui-table-cell>
				</svws-ui-table-row>
			</template>
			<template #body>
				<template v-for="(kompetenzgruppe, index) in kompetenzgruppen"
					:key="index">
					<s-benutzer-kompetenzgruppe :kompetenzgruppe="kompetenzgruppe" :get-benutzer-manager="getBenutzerManager"
						:add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" :get-gruppen4-kompetenz="getGruppen4Kompetenz"
						:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe"
						:remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe" :benutzer-kompetenzen="benutzerKompetenzen" />
				</template>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BenutzerKompetenz, BenutzerManager, List } from "@core";
	import { BenutzerKompetenzGruppe } from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		getBenutzerManager: () => BenutzerManager;
		setIstAdmin : (istAdmin: boolean) => Promise<void>;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		getGruppen4Kompetenz : ( kompetenz : BenutzerKompetenz ) => string;
		benutzerKompetenzen : ( kompetenz : BenutzerKompetenzGruppe ) => List<BenutzerKompetenz>;
	}>();

	const kompetenzgruppen: ComputedRef<BenutzerKompetenzGruppe[]> = computed(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0));

	const inputIstAdmin: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.getBenutzerManager().istAdmin(),
		set: (value) => {
			if ((value === undefined) || (value === props.getBenutzerManager().istAdmin()))
				return;
			void props.setIstAdmin(value);
		}
	});
</script>

<style scoped lang="postcss">
	.data-table__tr {
		grid-template-columns: minmax(4rem, 2fr) minmax(4rem, 1fr) minmax(4rem, 0.25fr);
	}
</style>
