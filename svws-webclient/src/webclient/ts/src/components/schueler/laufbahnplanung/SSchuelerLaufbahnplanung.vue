<template>
	<div class="flex flex-row gap-4">
		<div class="flex-none">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturmanager="abiturmanager" :faechermanager="faechermanager" :fachkombinationen="fachkombinationen"
				:jahrgangsdaten="jahrgangsdaten" :item="item.value" :set-wahl="setWahl" :get-pdf-wahlbogen="getPdfWahlbogen" />
		</div>
		<div class="flex-auto">
			<s-laufbahnplanung-card-status v-if="visible" :abiturmanager="abiturmanager" :faechermanager="faechermanager" :fachkombinationen="fachkombinationen"
				:fehlerliste="fehlerliste" :belegpruefungsart="props.dataLaufbahn.gostAktuelleBelegpruefungsart" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostBelegpruefungErgebnisFehler, GostFach, GostJahrgangFachkombination, GostJahrgangsdaten, GostSchuelerFachwahl, JahrgangsListeEintrag,
		KlassenListeEintrag, KursListeEintrag, List, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
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
		setWahl: (fach: GostFach, wahl: GostSchuelerFachwahl) => Promise<void>;
		getPdfWahlbogen: () => Promise<Blob>;
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		stammdaten: DataSchuelerStammdaten;
		schule: DataSchuleStammdaten;
		listKlassen: ListKlassen;
		mapKlassen: Map<Number, KlassenListeEintrag>;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listKurse: ListKurse;
		mapKurs: Map<Number, KursListeEintrag>;
		jahrgangsdaten: GostJahrgangsdaten;
		dataLaufbahn: DataSchuelerLaufbahnplanung;
		dataFaecher: DataGostFaecher;
		dataFachkombinationen: DataGostFachkombinationen;
	}>();

	const abiturmanager = computed(() => {
		if (props.dataLaufbahn.manager === undefined)
			throw new Error("Unerwarteter Fehler: Abiturdaten-Manager nicht initialisiert");
		return props.dataLaufbahn.manager;
	});

	const faechermanager = computed(() => {
		if (props.dataFaecher.manager === undefined)
			throw new Error("Unerwarteter Fehler: Fächer-Manager nicht initialisiert");
		return props.dataFaecher.manager
	});

	const fachkombinationen: ComputedRef<List<GostJahrgangFachkombination>> = computed(()=>{
		const list = new Vector<GostJahrgangFachkombination>();
		if (props.dataFachkombinationen.daten === undefined)
			return list;
		for (const regel of	props.dataFachkombinationen.daten)
			if (regel.abiturjahr === props.item.value?.abiturjahrgang)
				list.add(regel)
		return list;
	})

	const fehlerliste: ComputedRef<List<GostBelegpruefungErgebnisFehler>> = computed(() => props.dataLaufbahn.gostBelegpruefungsErgebnis.fehlercodes);

	const visible: ComputedRef<boolean> = computed(() =>
		!(routeSchuelerLaufbahnplanung.hidden())
		&& (props.item.value?.id !== undefined)
		&& (props.item.value?.abiturjahrgang !== undefined)
	);

</script>
