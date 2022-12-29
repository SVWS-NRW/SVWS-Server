<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<div>
				<i-ri-arrow-left-line class="inline-block cursor-pointer" @click="router.push({ name: routeSchule.name })" />
				Benutzergruppen
			</div>
		</template>
		<template #content>
			<!-- Auswahlliste für die Benutzergruppen -->
			<div class="px-6 mt-4">
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel">
					<i-ri-search-line />
				</svws-ui-text-input>
			</div>
			<div class="pl-3 pt-6 container">
				<svws-ui-table v-model="selected" v-model:selection="selection"  :columns="cols" :data="rowsFiltered" is-multi-select :footer="true">
					<template #footer>
						<s-modal-benutzergruppe-neu/>						
					</template>
				</svws-ui-table>
			</div>
			<!-- Wechsel zu den Benutzern -->
			<div class="px-1 pt-3 text-lg font-bold" @click="router.push({ name: routeSchuleBenutzer.name })"> 
				<i-ri-arrow-right-line class="inline-block cursor-pointer" /> 
				<div class="px-1 pt-3 text-lg font-bold inline-block cursor-pointer"> Benutzer  </div>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router";
	import { routeSchule } from "~/router/apps/RouteSchule";
	import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
	import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";

	const props = defineProps<{ id?: number; item?: BenutzergruppeListeEintrag, routename: string }>();

	const selected = routeSchuleBenutzergruppe.auswahl;
	const selection = ref([]);

	const main: Main = injectMainApp();

	const cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true }
	];

	const search: Ref<string> = ref("");

	const rows: ComputedRef<BenutzergruppeListeEintrag[] | undefined> = computed(() => {
		return main.apps.benutzergruppe.auswahl.liste;
	});

	const rowsFiltered: ComputedRef<BenutzergruppeListeEintrag[] | undefined> = computed(() => {
		if (rows.value === undefined) 
			return undefined;
		const rowsValue: BenutzergruppeListeEintrag[] = rows.value;
		return (search.value) 
			? rowsValue.filter((e: BenutzergruppeListeEintrag) => e.bezeichnung.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
			: rowsValue;
	});

</script>
