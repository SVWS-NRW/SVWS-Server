<template>
	<div v-if="visible">
		<svws-ui-content-card title="Platzhalter">
			<div class="input-wrapper">
				<p>Hier werden zukünftig die Unterrichtsdaten angezeigt.</p>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";

	const { item } = defineProps<{ 
		item: ShallowRef<LehrerListeEintrag | undefined>;
		stammdaten: DataLehrerStammdaten;
		schule: DataSchuleStammdaten;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (item.value !== undefined) && (!routeLehrerUnterrichtsdaten.hidden);
	});

</script>
