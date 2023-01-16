<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<div>
				<i-ri-arrow-left-line class="inline-block cursor-pointer" @click="router.push({ name: routeSchule.name })" />
				Benutzer
			</div>
		</template>
		<template #content>
			<!-- Auswahlliste für die Benutzer -->
			<div class="px-6 mt-4">
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel">
					<i-ri-search-line />
				</svws-ui-text-input>
			</div>
			<div class="pl-3 pt-6 pb-6 container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rowsFiltered" v-model:selection="selectedItems" is-multi-select :footer="true">
					<template #footer>
						<s-modal-benutzer-neu :show_delete_icon="selectedItems.length > 0" />
					</template>
				</svws-ui-table>
			</div>
			<!-- Wechsel zu den Benutzer-Gruppen -->
			<div class="px-1 pt-3 text-lg font-bold" @click="router.push({ name: routeSchuleBenutzergruppe.name })">
				<i-ri-arrow-right-line class="inline-block cursor-pointer" />
				<div class="px-1 pt-3 text-lg font-bold inline-block cursor-pointer"> Benutzergruppen  </div>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router";
	import { routeSchule } from "~/router/apps/RouteSchule";
	import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
	import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";

	const props = defineProps<{ id?: number; item?: BenutzerListeEintrag, routename: string }>();

	const selected = routeSchuleBenutzer.auswahl;

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;

	const cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "anzeigename", label: "Anzeigename", sortable: true },
		{ key: "name", label: "Name", sortable: true }
	];

	const search: Ref<string> = ref("");

	const rows: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		return app.auswahl.liste;
	});

	const rowsFiltered: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		if (rows.value === undefined)
			return undefined;
		const rowsValue: BenutzerListeEintrag[] = rows.value;
		return (search.value)
			? rowsValue.filter((e: BenutzerListeEintrag) => e.name.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
			: rowsValue;
	});

	const selectedItems: WritableComputedRef<BenutzerListeEintrag[]> = computed({
		get: () => app.auswahl.ausgewaehlt_gruppe,
		set: (items: BenutzerListeEintrag[]) => {
			app.auswahl.ausgewaehlt_gruppe = items;
		}
	});

</script>
