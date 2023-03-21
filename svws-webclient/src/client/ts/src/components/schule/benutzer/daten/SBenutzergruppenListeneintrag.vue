<template>
	<div role="cell"
		 class="data-table__td data-table__cell-select">
		<svws-ui-checkbox class="data-table__cell-checkbox"
						  :model-value="selected"
						  @update:model-value="selected"
						  v-model="selected"
						  :disabled="istAlle" />
	</div>
	<div role="cell" class="data-table__td font-mono">
		<span class="data-table__td-content">
			{{ row.id }}
		</span>
	</div>
	<div role="cell" class="data-table__td">
		<div class="flex items-center gap-1">
			{{ row.bezeichnung }}
			<svws-ui-button type="icon" size="small" @click="doRoute()">
				<i-ri-external-link-fill />
			</svws-ui-button>
		</div>
	</div>
	<div role="cell" class="data-table__row-actions data-table__td">
		<span class="data-table__td-content">
			<i-ri-check-line v-if="row.istAdmin" />
			<i-ri-close-line v-else class="opacity-25" />
		</span>
	</div>
</template>

<script setup lang="ts">
	import { BenutzergruppeListeEintrag, BenutzerManager } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";
	import { useRouter } from "vue-router";
	import { routeSchuleBenutzergruppeDaten } from "~/router/apps/benutzergruppe/RouteSchuleBenutzergruppeDaten";

	const props = withDefaults(defineProps<{
		row: BenutzergruppeListeEintrag;
		istAlle?: boolean;
		getBenutzerManager: () => BenutzerManager;
		addBenutzerToBenutzergruppe: (bg_id : number) => Promise<void>;
		removeBenutzerFromBenutzergruppe: (bg_id : number) => Promise<void>;
	}>(), {
		istAlle: false
	});

	const router = useRouter();

	const selected: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.getBenutzerManager().IstInGruppe(props.row.id),
		set: (value) => {
			if (value)
				void props.addBenutzerToBenutzergruppe(props.row.id);
			else
				void props.removeBenutzerFromBenutzergruppe(props.row.id);
		}
	});

	async function doRoute() {
		await router.push({ name: routeSchuleBenutzergruppeDaten.name, params: { id: props.row.id } });
	}

</script>
