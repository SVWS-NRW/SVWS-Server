<template>
	<svws-ui-badge class="cursor-pointer print:hidden" size="tiny" type="primary" @click="modal.openModal()"> Hilfe </svws-ui-badge>
	<svws-ui-modal ref="modal">
		<template #modalTitle>Hilfe</template>
		<template #modalDescription>
			<hilfe-laufbahnplanung />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal">
				Schließen
			</svws-ui-button>
		</template>
	</svws-ui-modal>
	<s-card-schueler-laufbahnplanung v-if="visible" :item="item" :stammdaten="stammdaten" :dataLaufbahn="dataLaufbahn" :dataFaecher="dataFaecher" :dataFachkombinationen="dataFachkombinationen" />
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref } from "vue";
	import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/RouteSchuelerLaufbahnplanung";

	const { id, item, stammdaten, dataLaufbahn, dataFaecher, dataFachkombinationen } = defineProps<{ 
		id?: number; 
		item?: SchuelerListeEintrag, 
		stammdaten: DataSchuelerStammdaten, 
		dataLaufbahn: DataSchuelerLaufbahnplanung, 
		dataFaecher: DataGostFaecher, 
		dataFachkombinationen: DataGostFachkombinationen,
		routename: string 
	}>();

	const modal = ref();

	const visible: ComputedRef<boolean> = computed(() => {
		return !(routeSchuelerLaufbahnplanung.hidden) && (id !== undefined) && (item?.abiturjahrgang !== undefined);
	});

</script>
