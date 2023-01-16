<template>
	<svws-ui-modal ref="modalNeueBenutzergruppeBenutzerauswahl" size="small">
		<template #modalTitle> Benutzer hinzufügen </template>

		<template #modalContent>
			<svws-ui-table v-model="selected" v-model:selection="selection" :columns="cols" :data="rowsFiltered" is-multi-select />
		</template>

		<template #modalActions>
			<svws-ui-button type="secondary" @click="modalNeueBenutzergruppeBenutzerauswahl.closeModal()"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="addBenutzer"> Weiter {{ selection.length }}</svws-ui-button>
		</template>
	</svws-ui-modal>
	<button class="button button--icon" @click="modalNeueBenutzergruppeBenutzerauswahl.openModal()">
		<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
	</button>
</template>

<script setup lang="ts">
	import { BenutzergruppenManager, BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.benutzergruppe;

	const modalNeueBenutzergruppeBenutzerauswahl = ref();

	// Die Spalte für die Tabelle der Gruppenbenutzer
	const cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "Bezeichnung", label: "Login-Nmae", sortable: true },
		{ key: "name", label: "Name", sortable: true },
		{ key: "istAdmin", label: "IstAdmin", sortable: false }
	];

	const benutzer_liste: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		return main.apps.benutzer.auswahl.liste;
	});

	const rowsFiltered: ComputedRef<BenutzerListeEintrag[]> = computed(() => {

		return benutzer_liste.value || [];
	});
	const selected = ref([]);
	const selection = ref(app.dataBenutzergruppe.listBenutzergruppenBenutzer.liste || []);
	console.log(selection.value);
	function addBenutzer(){
		console.log(selection.value)
	}

</script>
