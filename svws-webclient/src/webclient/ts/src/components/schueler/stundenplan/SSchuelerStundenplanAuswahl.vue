<template>
	<template v-if="visible">
		<svws-ui-table v-model="selected" :columns="cols" :data="liste" :footer="false" />
	</template>
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag, StundenplanListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef, WritableComputedRef } from "vue";
	import { DataStundenplan } from "~/apps/schueler/DataStundenplan";
	import { routeSchuelerStundenplanDaten } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplanDaten";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { DataTableColumn } from "@svws-nrw/svws-ui";

	const props = defineProps<{
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		stammdaten: DataSchuelerStammdaten;
		stundenplan?: StundenplanListeEintrag;
		data: DataStundenplan;
	}>();

	const liste: ComputedRef<StundenplanListeEintrag[]> = computed(() => routeSchuelerStundenplanDaten.data.auswahl.liste);

	const selected: WritableComputedRef<StundenplanListeEintrag | undefined> = routeSchuelerStundenplanDaten.auswahl;

	const cols: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "gueltigAb", label: "von", span: 1, sortable: false, defaultSort: 'asc' },
		{ key: "gueltigBis", label: "bis", span: 1, sortable: false }
	];

	const visible: ComputedRef<boolean> = computed<boolean>(() => {
		return !(routeSchuelerStundenplanDaten.hidden);
	});

</script>