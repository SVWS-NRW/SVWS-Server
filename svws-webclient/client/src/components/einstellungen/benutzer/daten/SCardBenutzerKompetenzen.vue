<template>
	<svws-ui-content-card title="Einstellungen">
		<template #actions>
			<template v-if="getBenutzerManager().istInAdminGruppe()">
				<span class="inline-flex gap-1 leading-none">
					<span class="icon i-ri-shield-star-line flex-shrink-0 -mt-0.5" />
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
					<div class="svws-ui-td" role="columnheader">
						<span class="icon cursor-pointer" :class="{ 'i-ri-question-line': !showInfo, 'i-ri-question-fill': showInfo }" @click="toggleShowInfo" />
					</div>
					<div v-if="!getBenutzerManager().istAdmin()" class="svws-ui-td !pl-1 text-black/50 dark:text-white/50" role="columnheader">Übernommen aus der Gruppe</div>
				</div>
			</template>
			<template #body>
				<template v-for="(kompetenzgruppe, index) in kompetenzgruppen" :key="index">
					<s-benutzer-kompetenzgruppe :kompetenzgruppe :show-info :get-benutzer-manager :add-kompetenz :remove-kompetenz :add-benutzer-kompetenz-gruppe :remove-benutzer-kompetenz-gruppe :benutzer-kompetenzen />
				</template>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, shallowRef } from "vue";
	import type { BenutzerKompetenz, BenutzerManager, List } from "@core";
	import { BenutzerKompetenzGruppe } from "@core";

	const props = defineProps<{
		getBenutzerManager: () => BenutzerManager;
		setIstAdmin : (istAdmin: boolean) => Promise<void>;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		benutzerKompetenzen : ( kompetenz : BenutzerKompetenzGruppe ) => List<BenutzerKompetenz>;
	}>();

	const kompetenzgruppen = computed<BenutzerKompetenzGruppe[]>(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0));

	const inputIstAdmin = computed<boolean>({
		get: () => props.getBenutzerManager().istAdmin(),
		set: (value) => {
			if (value === props.getBenutzerManager().istAdmin())
				return;
			void props.setIstAdmin(value);
		}
	});

	const showInfo = shallowRef<boolean>(false);
	function toggleShowInfo() {
		showInfo.value = !showInfo.value;
	}

</script>

<style scoped lang="postcss">

	.svws-ui-tr {
		grid-template-columns: minmax(4rem, 2fr) 0.15fr minmax(4rem, 1fr);
	}

</style>
