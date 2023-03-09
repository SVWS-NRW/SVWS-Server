<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Schule" @click="router.push({ name: routeSchule.name })">Schule</a>
				<span title="Benutzergruppen">Benutzergruppen</span>
			</nav>
		</template>
		<template #header>
			<div class="mb-2">
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel">
					<i-ri-search-line />
				</svws-ui-text-input>
			</div>
		</template>
		<template #content>
			<svws-ui-table v-model="selected" v-model:selection="selectedItems" :columns="cols" :data="rowsFiltered" is-multi-select :footer="true">
				<template #footerActions>
					<s-modal-benutzergruppe-neu :show-delete-icon="selectedItems.length > 0"
						:create-benutzergruppe="createBenutzergruppe"
						:delete-benutzergruppe_n="deleteBenutzergruppe_n" />
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, Ref, ref, ShallowRef, WritableComputedRef } from "vue";
	import { router } from "~/router/RouteManager";
	import { routeSchule } from "~/router/apps/RouteSchule";
	import { routeSchuleBenutzergruppe } from "~/router/apps/schule/RouteSchuleBenutzergruppe";

	const props = defineProps<{
		item: ShallowRef<BenutzergruppeListeEintrag | undefined>;
		createBenutzergruppe : (bezeichnung: string, istAdmin: boolean) => Promise<void>;
		// eslint-disable-next-line vue/prop-name-casing
		deleteBenutzergruppe_n : () => Promise<void>;
	}>();

	const selected = routeSchuleBenutzergruppe.auswahl;

	const cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 }
	];

	const search: Ref<string> = ref("");

	const rows: ComputedRef<BenutzergruppeListeEintrag[] | undefined> = computed(() => {
		return routeSchuleBenutzergruppe.liste.liste;
	});

	const rowsFiltered: ComputedRef<BenutzergruppeListeEintrag[]> = computed(() =>
		(rows.value === undefined) ? [] : (search.value)
			? rows.value.filter((e: BenutzergruppeListeEintrag) => e.bezeichnung.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
			: rows.value
	);

	const selectedItems: WritableComputedRef<BenutzergruppeListeEintrag[]> = computed({
		get: () => routeSchuleBenutzergruppe.liste.ausgewaehlt_gruppe,
		set: (items: BenutzergruppeListeEintrag[]) => {
			routeSchuleBenutzergruppe.liste.ausgewaehlt_gruppe = items;
		}
	});

</script>
