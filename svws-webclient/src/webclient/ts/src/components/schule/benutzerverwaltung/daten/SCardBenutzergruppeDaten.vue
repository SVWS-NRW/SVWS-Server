<template>
	<svws-ui-content-card title="Benutzergruppe">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input
					v-model="bezeichnung"
					type="text"
					placeholder="Bezeichnung"
				/>
				<svws-ui-checkbox v-model="inputIstAdmin">
					Admin ?
				</svws-ui-checkbox>
				<svws-ui-table
					v-model="selected"
					v-model:selection="selection"
					:columns="cols"
					:data="rowsFiltered"
					is-multi-select
				/>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { BenutzergruppeListeEintrag, BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzKatalogEintrag, BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router";
	

	const main: Main = injectMainApp();
	const app = main.apps.benutzergruppe;

	const manager: ComputedRef<BenutzergruppenManager | undefined> = computed(() => {
		return app.dataBenutzergruppe.manager;
	})

	const bezeichnung: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return manager.value?.getBezeichnung().valueOf();
		},
		async set(val: string | undefined) {
			console.log(val);
			if ((val === undefined) || (val === "") || (val === manager.value?.getBezeichnung().valueOf()))
				return;
			app.dataBenutzergruppe.setBezeichnung(val);
		}
	});

	const inputIstAdmin: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return manager.value?.istAdmin();
		},
		set(val) {
			if ((val === undefined) || (val === manager.value?.istAdmin()))
				return;
			app.dataBenutzergruppe.setIstAdmin(val);
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
			return app.dataBenutzergruppe.listBenutzergruppenBenutzer.liste;
	});

	const rowsFiltered: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		if (rows.value) {
			const rowsValue: BenutzerListeEintrag[] = rows.value;
			if (search.value) {
				return rowsValue.filter((e: BenutzerListeEintrag) =>
					e.name
						.toLocaleLowerCase()
						.includes(search.value.toLocaleLowerCase())
				);
			}
			return rowsValue;
		}
		return undefined;
	});

	const search: Ref<string> = ref("");
	const selection: WritableComputedRef<BenutzerListeEintrag[] | undefined> = computed({
		get() : BenutzerListeEintrag[] | undefined {
			return app.dataBenutzergruppe.listBenutzergruppenBenutzer.liste;
		},
		set(val:BenutzerListeEintrag[] | undefined){
			console.log(val)
		}
	});

	const selected: WritableComputedRef<BenutzerListeEintrag | undefined> = computed({
		get():BenutzerListeEintrag | undefined {
			return app.dataBenutzergruppe.listBenutzergruppenBenutzer.liste[0] ;
		},
		set(val:BenutzerListeEintrag | undefined) {
			// TODO	
		}
	});

</script>
