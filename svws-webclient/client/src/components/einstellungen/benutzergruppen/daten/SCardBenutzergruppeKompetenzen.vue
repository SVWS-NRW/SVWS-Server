<template>
	<svws-ui-content-card>
		<svws-ui-table :items="kompetenzgruppen" :disable-footer="true">
			<template #header>
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td" role="columnheader">Kompetenz</div>
					<div class="svws-ui-td" role="columnheader">
						<span class="icon cursor-pointer" :class="{ 'i-ri-question-line': !showInfo, 'i-ri-question-fill': showInfo }" @click="toggleShowInfo" />
					</div>
				</div>
			</template>
			<template #body>
				<template v-for="(kompetenzgruppe, index) in kompetenzgruppen" :key="index">
					<s-benutzergruppe-kompetenzgruppe :kompetenzgruppe :show-info :ist-admin="getBenutzergruppenManager().istAdmin()"
						:get-benutzergruppen-manager :add-kompetenz :remove-kompetenz :add-benutzer-kompetenz-gruppe :remove-benutzer-kompetenz-gruppe :benutzer-kompetenzen />
				</template>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, shallowRef } from "vue";
	import type { BenutzerKompetenz, BenutzergruppenManager, List } from "@core";
	import { BenutzerKompetenzGruppe } from "@core";

	const props = defineProps<{
		getBenutzergruppenManager: () => BenutzergruppenManager;
		addKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz: BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>,
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>
		benutzerKompetenzen : (kompetenzgruppe : BenutzerKompetenzGruppe) => List<BenutzerKompetenz>;
	}>();

	const kompetenzgruppen = computed<BenutzerKompetenzGruppe[]>(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0 && props.benutzerKompetenzen(gr).size() > 0));

	const showInfo = shallowRef<boolean>(false);
	function toggleShowInfo() {
		showInfo.value = !showInfo.value;
	}

</script>

<style scoped lang="postcss">

	.svws-ui-tr {
		grid-template-columns: minmax(4rem, 3fr) 0.15fr;
	}

</style>
