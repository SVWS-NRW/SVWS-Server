<template>
	<svws-ui-content-card title="Daten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input
					v-model="bezeichnung"
					type="text"
					placeholder="Login-Name"
				/>
				<svws-ui-checkbox v-model="inputIstAdmin">
					Admin ?
				</svws-ui-checkbox>

				<!-- <svws-ui-text-input
					v-model="name"
					type="text"
					placeholder="Name"
				/> -->
				<!-- 
				<svws-ui-text-input
					v-model="inputTextzeugnis"
					type="text"
					placeholder="Zeugnisbezeichnung"
				/> -->
			</div>
		</div>
	</svws-ui-content-card>
	<svws-ui-content-card title="Benutzer">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-new-table
					v-model="selected"
					v-model:selection="selection"
					:columns="cols"
					:data="rowsFiltered"
					is-multi-select
				/>

				<p>{{ selected }}</p>
				<p>{{ selection }}</p>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.benutzergruppe;

	// const bezeichnung: WritableComputedRef<BenutzerKompetenzKatalogEintrag[] | undefined> = computed({
	// 	get(): BenutzerKompetenzKatalogEintrag[] | undefined {
	// 		return app.dataBenutzergruppe.daten?.kompetenzen.
	// 	},
	// 	set(val: string | undefined) {
	// 		console.log(val);
	// 		//app.dataReligion.patch({ kuerzel: val });
	// 	}
	// });
	const bezeichnung: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataBenutzergruppe.daten?.bezeichnung.toString();
		},
		set(val: string | undefined) {
			console.log(val);
			//app.dataReligion.patch({ kuerzel: val });
		}
	});
	const inputIstAdmin: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return app.dataBenutzergruppe.daten?.istAdmin === true
				? true
				: false;
		},
		set(val) {
			console.log(
				app.dataBenutzergruppe.listBenutzergruppenBenutzer.liste
			);
			//app.stammdaten.patch({ istDuplikat: val });
		}
	});

	//Gruppenbenutzer
	const cols = [
		{
			key: "id",
			label: "ID",
			sortable: true
		},
		{
			key: "anzeigename",
			label: "Login-Nmae",
			sortable: true
		},
		{
			key: "name",
			label: "Name",
			sortable: true
		},
		{
			key: "istAdmin",
			label: "IstAdmin",
			sortable: false
		}
	];
	const search: Ref<string> = ref("");
	const selected = ref();
	const selection = ref([]);
	const rows: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(
		() => {
			return app.dataBenutzergruppe.listBenutzergruppenBenutzer.liste;
		}
	);
	const rowsFiltered: ComputedRef<BenutzerListeEintrag[] | undefined> =
		computed(() => {
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
	function onDelete(val: number) {
		console.log(val);
	}
</script>
