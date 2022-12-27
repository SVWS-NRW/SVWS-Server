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
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{ id?: number; item?: LehrerListeEintrag, routename: string }>();

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.lehrerUnterrichtsdaten.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
