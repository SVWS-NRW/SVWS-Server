<template>
	<svws-ui-content-card title="Benutzergruppe">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input v-model="bezeichnung" type="text" placeholder="Bezeichnung" />
				<svws-ui-checkbox v-model="inputIstAdmin"> Admin ? </svws-ui-checkbox>
				<svws-ui-table v-model="selected" v-model:selection="selection" :columns="cols" :data="rowsFiltered" :footer="true" is-multi-select >
					<!-- Footer mit Button zum Hinzufügen einer Zeile -->
					<template #footer>
						<!-- <div class="text-sm-bold normal-case mr-auto">
							<span v-if="selectedItems.length">{{selectedItems.length}}/{{rowsFiltered.length}} ausgewählt</span>
							<span v-else>{{rowsFiltered.length}} Einträge</span>
						</div> -->
						<button class="button button--icon" @click="createUser()">
							<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
						</button>
						<button class="button button--icon">
							<svws-ui-icon><i-ri-file-copy-line /></svws-ui-icon>
						</button>
						<button class="button button--icon">
							<svws-ui-icon><i-ri-more-2-line /></svws-ui-icon>
						</button>
					</template>
				</svws-ui-table>	

			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { BenutzergruppenManager, BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

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

	function auswahl(test: BenutzerListeEintrag[]){
		console.log(test);
	}

	function ausgewaehlt(test: BenutzerListeEintrag){
		console.log(test);
	} 
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
		if (rows.value === undefined) 
			return undefined;
		const rowsValue: BenutzerListeEintrag[] = rows.value;
		if (search.value)
			return rowsValue.filter((e: BenutzerListeEintrag) => e.name.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()));
		return rowsValue;
	});

	const search: Ref<string> = ref("");
	const selection: WritableComputedRef<BenutzerListeEintrag[] | undefined> = computed({
		get() : BenutzerListeEintrag[] | undefined {
			return app.dataBenutzergruppe.listBenutzergruppenBenutzer.liste;
		},
		set(val:BenutzerListeEintrag[] | undefined){
			// TODO console.log(val)
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

	function createUser(){
		console.log("Halloe")
	}

</script>
