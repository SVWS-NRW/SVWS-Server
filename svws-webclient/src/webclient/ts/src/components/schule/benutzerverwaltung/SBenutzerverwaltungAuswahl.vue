<template>
	

	<svws-ui-secondary-menu>
		<template #headline>
			<div>
				<i-ri-arrow-left-line class="inline-block cursor-pointer" @click="router.push({ name: RouteSchule.name })" />
				Benutzerverwaltung
			</div>
		</template>
		<template #header>
		</template>
		<template #content>
			<!-- Auswahlliste für die Benutzer -->
			<div class="px-1 pt-3 text-lg font-bold"> Benutzer: </div>
			<div class="px-6 mt-4">
				<svws-ui-text-input v-model="benutzer_suche" type="search" placeholder="Suche nach Namen oder Kürzel">
					<i-ri-search-line />
				</svws-ui-text-input>
			</div>
			<div class="pl-3 pt-6 pb-6 container">
				<svws-ui-table v-model="benutzer_ausgewaehlt" :data="benutzer_rows_gefiltert" :columns="benutzer_cols" is-multi-select :footer="true">
					<!-- Footer mit Button zum Hinzufügen einer Zeile -->
					<template #footer>
						<!-- <div class="text-sm-bold normal-case mr-auto">
							<span v-if="selectedItems.length">{{selectedItems.length}}/{{rowsFiltered.length}} ausgewählt</span>
							<span v-else>{{rowsFiltered.length}} Einträge</span>
						</div> -->
						<!-- MODAL FENSTER-->
							<svws-ui-modal  ref="modalNeuerBenutzer"  size="small" >
								<template #modalTitle>
									Schüler hinzufügen
								</template>
								<template #modalContent>
									<div class="content-wrapper">
										<div class="input-wrapper">
											<svws-ui-text-input v-model="anzeigename" type="text" placeholder="Login-Name" />
											<svws-ui-text-input v-model="name" type="text" placeholder="Name" />
											<svws-ui-checkbox v-model="inputIstAdmin"> Admin ? </svws-ui-checkbox>
										</div>
									</div>	
								</template>
								<template #modalActions>
									<Button
									type="secondary"
									@click="modalNeuerBenutzer.closeModal()"
									>
									Abbrechen
									</Button>
									<Button @click="createUser">
									Weiter
									</Button>
								</template>
							</svws-ui-modal>
						<button class="button button--icon" @click="modalNeuerBenutzer.openModal()">
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
			<!-- Auswahlliste für die Benutzergruppen -->
			<div class="px-1 pt-3 text-lg font-bold"> Benutzergruppen: </div>
			<div class="px-6 mt-4">
				<svws-ui-text-input v-model="benutzergruppen_suche" type="search" placeholder="Suche nach Namen oder Kürzel">
					<i-ri-search-line />
				</svws-ui-text-input>
			</div>
			<div class="pl-3 pt-6 container">
				<svws-ui-table v-model="benutzergruppen_ausgewaehlt" v-model:selection="benutzergruppen_selection" :data="benutzergruppen_rows_gefiltert" :columns="benutzergruppen_cols" is-multi-select />
			</div>
		</template>
	</svws-ui-secondary-menu>
	
	
</template>

<script setup lang="ts">
	import type { BenutzerListeEintrag, BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router";
	import { routeAppAuswahl } from "~/router/RouteUtils";
	import { RouteSchule } from "~/router/apps/RouteSchule";
	import { RouteSchuleBenutzerverwaltungBenutzer } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzer";
	import { RouteSchuleBenutzerverwaltungBenutzergruppe } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzergruppe";

	// Allgemeines

	const main: Main = injectMainApp();
	const modalNeuerBenutzer = ref();
	// Auswahlliste der Benutzer

	const benutzer_cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "anzeigename", label: "Anzeigename", sortable: true },
		{ key: "name", label: "Name", sortable: true }
	];

	const benutzer_suche: Ref<string> = ref("");

	const benutzer_rows: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		return main.apps.benutzer.auswahl.liste;
	});

	const benutzer_rows_gefiltert: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		if (benutzer_rows.value === undefined)
			return undefined;
		const rowsValue: BenutzerListeEintrag[] = benutzer_rows.value;
		return (benutzer_suche.value) 
			? rowsValue.filter((e: BenutzerListeEintrag) => e.name.toLocaleLowerCase().includes(benutzer_suche.value.toLocaleLowerCase())) 
			: rowsValue;
	});

	const benutzer_ausgewaehlt = routeAppAuswahl(RouteSchuleBenutzerverwaltungBenutzer);

	// Auswahlliste der Benutzergruppen

	const benutzergruppen_cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true }
	];

	const benutzergruppen_suche: Ref<string> = ref("");

	const benutzergruppen_rows: ComputedRef<BenutzergruppeListeEintrag[] | undefined> = computed(() => {
		return main.apps.benutzergruppe.auswahl.liste;
	});

	const benutzergruppen_rows_gefiltert: ComputedRef<BenutzergruppeListeEintrag[] | undefined> = computed(() => {
		if (benutzergruppen_rows.value === undefined) 
			return undefined;
		const rowsValue: BenutzergruppeListeEintrag[] = benutzergruppen_rows.value;
		return (benutzergruppen_suche.value) 
			? rowsValue.filter((e: BenutzergruppeListeEintrag) => e.bezeichnung.toLocaleLowerCase().includes(benutzergruppen_suche.value.toLocaleLowerCase()))
			: rowsValue;
	});

	const benutzergruppen_ausgewaehlt = routeAppAuswahl(RouteSchuleBenutzerverwaltungBenutzergruppe);
	const benutzergruppen_selection = ref([]);

	//MODAL-FENSTER

	const anzeigename = ref();
	const name = ref();
	const inputIstAdmin=ref();
	function createUser(){
		console.log(anzeigename.value+"-"+name.value+"-"+inputIstAdmin.value)
	}
</script>
