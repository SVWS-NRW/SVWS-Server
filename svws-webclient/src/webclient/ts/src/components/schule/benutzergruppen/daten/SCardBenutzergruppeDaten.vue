<template>
	<svws-ui-content-card title="Benutzergruppe">
		<div class="flex flex-col">
			<svws-ui-text-input v-model="bezeichnung" type="text" placeholder="Bezeichnung" />
			<svws-ui-checkbox v-model="inputIstAdmin"> Admin ? </svws-ui-checkbox>
			<!-- <svws-ui-table v-model="selected" v-model:selection="selection" :columns="cols" :data="rowsFiltered" :footer="true"> -->
			<!-- Footer mit Button zum Hinzufügen einer Zeile -->
			<!-- <template #footer>
					<s-modal-benutzergruppe-benutzer-auswahl />
				</template>
			</svws-ui-table> -->
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzergruppenManager, BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
	import { DataBenutzergruppe } from "~/apps/schule/benutzerverwaltung/DataBenutzergruppe";

	const props = defineProps<{
		data: DataBenutzergruppe;
	}>();

	const manager: ComputedRef<BenutzergruppenManager | undefined> = computed(() => props.data.manager);

	const bezeichnung: WritableComputedRef<string | undefined> = computed({
		get: () => manager.value?.getBezeichnung().valueOf(),
		set: async (value) => {
			if ((value === undefined) || (value === "") || (value === manager.value?.getBezeichnung().valueOf()))
				return;
			props.data.setBezeichnung(value);
		}
	});

	const inputIstAdmin: WritableComputedRef<boolean | undefined> = computed({
		get: () => manager.value?.istAdmin(),
		set: async (value) => {
			if ((value === undefined) || (value === manager.value?.istAdmin()))
				return;
			props.data.setIstAdmin(value);
		}
	});

	// Die Spalte für die Tabelle der Gruppenbenutzer
	const cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "Bezeichnung", label: "Login-Nmae", sortable: true },
		{ key: "name", label: "Name", sortable: true },
		{ key: "istAdmin", label: "IstAdmin", sortable: false }
	];

	const rows: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		return props.data.listBenutzergruppenBenutzer.liste;
	});

	const rowsFiltered: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		if (rows.value === undefined)
			return undefined;
		const rowsValue: BenutzerListeEintrag[] = rows.value;
		if (search.value)
			return rowsValue.filter((e: BenutzerListeEintrag) => e.name.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()));
		return rowsValue;
	});

	const search: Ref<string> = ref("");
	const selection: WritableComputedRef<BenutzerListeEintrag[] | undefined> = computed({
		get: () => props.data.listBenutzergruppenBenutzer.liste,
		set: (value) => {
			// TODO console.log(val)
		}
	});

	const selected: WritableComputedRef<BenutzerListeEintrag | undefined> = computed({
		get: () => props.data.listBenutzergruppenBenutzer.liste[0],
		set: (value) => {
			// TODO
		}
	});

</script>
