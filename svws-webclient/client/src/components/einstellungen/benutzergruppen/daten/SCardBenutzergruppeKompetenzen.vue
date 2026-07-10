<template>
	<svws-ui-table :items="kompetenzgruppen" :disable-footer="true" scroll>
		<template #header>
			<tr class="svws-ui-tr">
				<th class="svws-ui-td">Kompetenz</th>
				<th class="svws-ui-td">
					<span class="icon cursor-pointer"
						:class="{ 'i-ri-question-line': !showInfo, 'i-ri-question-fill': showInfo }"
						@click="toggleShowInfo" />
				</th>
			</tr>
		</template>
		<template #body>
			<template v-for="(kompetenzgruppe, index) in kompetenzgruppen" :key="index">
				<s-benutzergruppe-kompetenzgruppe :kompetenzgruppe
					:show-info
					:ist-admin="manager().istAdmin()"
					:manager
					:add-kompetenz
					:remove-kompetenz
					:add-benutzer-kompetenz-gruppe
					:remove-benutzer-kompetenz-gruppe
					:benutzer-kompetenzen />
			</template>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { computed, shallowRef } from "vue";
	import type { BenutzerKompetenz, BenutzergruppenManager, List } from "@core";
	import { BenutzerKompetenzGruppe } from "@core";

	const props = defineProps<{
		manager: () => BenutzergruppenManager;
		addKompetenz: (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz: (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe: (kompetenzgruppe: BenutzerKompetenzGruppe) => Promise<boolean>,
		removeBenutzerKompetenzGruppe: (kompetenzgruppe: BenutzerKompetenzGruppe) => Promise<boolean>
		benutzerKompetenzen: (kompetenzgruppe: BenutzerKompetenzGruppe) => List<BenutzerKompetenz>;
	}>();

	const kompetenzgruppen = computed<BenutzerKompetenzGruppe[]>(() => BenutzerKompetenzGruppe.values().filter(gr => (gr.daten.id >= 0) && !props.benutzerKompetenzen(gr).isEmpty()));

	const showInfo = shallowRef<boolean>(false);
	function toggleShowInfo() {
		showInfo.value = !showInfo.value;
	}

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: minmax(4rem, 3fr) 0.15fr;
	}

</style>
