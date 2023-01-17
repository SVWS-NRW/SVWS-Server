<template>
	<div v-if="visible" class="app-container relative">
		<div class="svws-ui-bg-white sticky top-0 z-50 col-span-3 flex justify-end py-4">
			<s-card-schueler-add-adresse :item="item.value" :list-schuelerbetriebe="listSchuelerbetriebe" />
		</div>
		<div v-if="sb_vorhanden" class="col-span-3">
			<s-card-schueler-beschaeftigung :list-schuelerbetriebe="listSchuelerbetriebe" />
			<s-card-schueler-adresse :list-schuelerbetriebe="listSchuelerbetriebe" :betriebs-stammdaten="betriebsStammdaten" />
		</div>
		<div v-else>
			<h1>
				<strong> <div style="text-align: center;"> Noch kein Schülerbetrieb vorhanden </div> </strong>
			</h1>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataBetriebsstammdaten } from "~/apps/schueler/DataBetriebsstammdaten";
	import { ListSchuelerBetriebsdaten } from "~/apps/schueler/ListSchuelerBetriebsdaten";
	import { routeSchuelerAdressen } from "~/router/apps/schueler/RouteSchuelerAdressen";

	const props = defineProps<{
		item: ShallowRef<SchuelerListeEintrag | undefined>;
		listSchuelerbetriebe : ListSchuelerBetriebsdaten;
		betriebsStammdaten: DataBetriebsstammdaten;
	}>();

	const sb_vorhanden : ComputedRef<boolean> = computed(() => {
		return props.listSchuelerbetriebe?.liste.length > 0 ? true : false;
	});

	const visible: ComputedRef<boolean> = computed(() => {
		return !routeSchuelerAdressen.hidden();
	});

</script>
