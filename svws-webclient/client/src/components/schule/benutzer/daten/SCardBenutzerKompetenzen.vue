<template>
	<svws-ui-content-card title="Einstellungen">
		<template #actions>
			<template v-if="getBenutzerManager().istInAdminGruppe()">
				<span class="inline-flex gap-1 leading-none">
					<i-ri-shield-star-line class="flex-shrink-0 -mt-0.5" />
					<span>Administrator mit allen Kompetenzen</span>
				</span>
			</template>
			<template v-else>
				<svws-ui-checkbox type="toggle" v-model="inputIstAdmin">
					Alle Kompetenzen freigeben
				</svws-ui-checkbox>
			</template>
		</template>
		<svws-ui-table :items="kompetenzgruppen">
			<template #header>
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td" role="columnheader" :class="{'col-span-2': getBenutzerManager().istAdmin()}">Kompetenz</div>
					<div v-if="!getBenutzerManager().istAdmin()" class="svws-ui-td !pl-1 text-black/50 dark:text-white/50" role="columnheader">Übernommen aus der Gruppe</div>
				</div>
			</template>
			<template #body>
				<template v-for="(kompetenzgruppe, index) in kompetenzgruppen" :key="index">
					<s-benutzer-kompetenzgruppe :kompetenzgruppe="kompetenzgruppe" :get-benutzer-manager="getBenutzerManager"
						:add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" :get-gruppen4-kompetenz="getGruppen4Kompetenz"
						:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe"
						:remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe" :benutzer-kompetenzen="benutzerKompetenzen" />
				</template>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BenutzerKompetenz, BenutzerManager, List } from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { BenutzerKompetenzGruppe } from "@core";
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
	.svws-ui-tr {
		grid-template-columns: minmax(4rem, 2fr) minmax(4rem, 1fr) minmax(4rem, 0.25fr);
	}
</style>
