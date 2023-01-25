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
	<s-card-schueler-laufbahnplanung v-if="visible" :item="item.value" :stammdaten="stammdaten" :data-laufbahn="dataLaufbahn" :data-faecher="dataFaecher" :data-fachkombinationen="dataFachkombinationen" />
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag, KlassenListeEintrag, KursListeEintrag, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, ShallowRef } from "vue";
	import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { ListJahrgaenge } from "~/apps/kataloge/jahrgaenge/ListJahrgaenge";
	import { ListKlassen } from "~/apps/klassen/ListKlassen";
	import { ListKurse } from "~/apps/kurse/ListKurse";
	import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/RouteSchuelerLaufbahnplanung";

	const props = defineProps<{
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		stammdaten: DataSchuelerStammdaten;
		schule: DataSchuleStammdaten;
		listKlassen: ListKlassen;
		mapKlassen: Map<Number, KlassenListeEintrag>;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listKurse: ListKurse;
		mapKurs: Map<Number, KursListeEintrag>;
		dataLaufbahn: DataSchuelerLaufbahnplanung;
		dataFaecher: DataGostFaecher;
		dataFachkombinationen: DataGostFachkombinationen;
	}>();

	const modal = ref();

	const visible: ComputedRef<boolean> =
		computed(() =>
			!(routeSchuelerLaufbahnplanung.hidden())
			&& (props.item.value?.id !== undefined)
			&& (props.item.value?.abiturjahrgang !== undefined)
		);
</script>
